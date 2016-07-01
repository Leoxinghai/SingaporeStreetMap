// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.producers;

import bolts.Continuation;
import bolts.Task;
import com.facebook.cache.common.CacheKey;
import com.facebook.common.internal.ImmutableMap;
import com.facebook.imagepipeline.cache.BufferedDiskCache;
import com.facebook.imagepipeline.cache.CacheKeyFactory;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.request.ImageRequest;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicBoolean;

// Referenced classes of package com.facebook.imagepipeline.producers:
//            Producer, ProducerListener, ProducerContext, Consumer,
//            DelegatingConsumer, BaseProducerContextCallbacks

public class DiskCacheProducer
    implements Producer
{
    private class DiskCacheConsumer extends DelegatingConsumer
    {

        public void onNewResultImpl(EncodedImage encodedimage, boolean flag)
        {
            if(encodedimage != null && flag)
                mCache.put(mCacheKey, encodedimage);
            getConsumer().onNewResult(encodedimage, flag);
        }

        public void onNewResultImpl(Object obj, boolean flag)
        {
            onNewResultImpl((EncodedImage)obj, flag);
        }

        private final BufferedDiskCache mCache;
        private final CacheKey mCacheKey;

        private DiskCacheConsumer(Consumer consumer, BufferedDiskCache buffereddiskcache, CacheKey cachekey)
        {
            super(consumer);
            mCache = buffereddiskcache;
            mCacheKey = cachekey;
        }

    }


    public DiskCacheProducer(BufferedDiskCache buffereddiskcache, BufferedDiskCache buffereddiskcache1, CacheKeyFactory cachekeyfactory, Producer producer)
    {
        mDefaultBufferedDiskCache = buffereddiskcache;
        mSmallImageBufferedDiskCache = buffereddiskcache1;
        mCacheKeyFactory = cachekeyfactory;
        mInputProducer = producer;
    }

    static Map getExtraMap(ProducerListener producerlistener, String s, boolean flag)
    {
        if(!producerlistener.requiresExtraMap(s))
            return null;
        else
            return ImmutableMap.of("cached_value_found", String.valueOf(flag));
    }

    private void maybeStartInputProducer(Consumer consumer, Consumer consumer1, ProducerContext producercontext)
    {
        if(producercontext.getLowestPermittedRequestLevel().getValue() >= com.facebook.imagepipeline.request.ImageRequest.RequestLevel.DISK_CACHE.getValue())
        {
            consumer.onNewResult(null, true);
            return;
        } else
        {
            mInputProducer.produceResults(consumer1, producercontext);
            return;
        }
    }

    private void subscribeTaskForRequestCancellation(final AtomicBoolean isCancelled, ProducerContext producercontext)
    {
        producercontext.addCallbacks(new BaseProducerContextCallbacks() {

            public void onCancellationRequested()
            {
                isCancelled.set(true);
            }

        });
    }

    public void produceResults(final Consumer consumer, final ProducerContext producerContext)
    {
        final ImageRequest imageRequest = producerContext.getImageRequest();
        if(!imageRequest.isDiskCacheEnabled())
        {
            maybeStartInputProducer(consumer, consumer, producerContext);
            return;
        }
        final ProducerListener listener = producerContext.getListener();
        final String requestId = producerContext.getId();
        listener.onProducerStart(requestId, "DiskCacheProducer");
        final CacheKey cacheKey = mCacheKeyFactory.getEncodedCacheKey(imageRequest);
        final BufferedDiskCache bufferedDiskCache;
        if(imageRequest.getImageType() == com.facebook.imagepipeline.request.ImageRequest.ImageType.SMALL)
            bufferedDiskCache = mSmallImageBufferedDiskCache;
        else
            bufferedDiskCache = mDefaultBufferedDiskCache;
        Continuation continuation = new Continuation() {


            public Void then(Task task)
                throws Exception
            {
                if(task.isCancelled() || task.isFaulted() && (task.getError() instanceof CancellationException))
                {
                    listener.onProducerFinishWithCancellation(requestId, "DiskCacheProducer", null);
                    consumer.onCancellation();
                    return null;
                }
                if(task.isFaulted())
                {
                    listener.onProducerFinishWithFailure(requestId, "DiskCacheProducer", task.getError(), null);
                    maybeStartInputProducer(consumer, new DiskCacheConsumer(consumer, bufferedDiskCache, cacheKey), producerContext);
                    return null;
                }
                EncodedImage encodedImage = (EncodedImage)task.getResult();
                if(encodedImage != null)
                {
                    listener.onProducerFinishWithSuccess(requestId, "DiskCacheProducer", DiskCacheProducer.getExtraMap(listener, requestId, true));
                    consumer.onProgressUpdate(1.0F);
                    consumer.onNewResult(task, true);
                    encodedImage.close();
                    return null;
                } else
                {
                    listener.onProducerFinishWithSuccess(requestId, "DiskCacheProducer", DiskCacheProducer.getExtraMap(listener, requestId, false));
                    maybeStartInputProducer(consumer, new DiskCacheConsumer(consumer, bufferedDiskCache, cacheKey), producerContext);
                    return null;
                }
            }

        };
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        bufferedDiskCache.get(cacheKey, atomicBoolean).continueWith(continuation);
        subscribeTaskForRequestCancellation(atomicBoolean, producerContext);
    }

    static final String PRODUCER_NAME = "DiskCacheProducer";
    static final String VALUE_FOUND = "cached_value_found";
    private final CacheKeyFactory mCacheKeyFactory;
    private final BufferedDiskCache mDefaultBufferedDiskCache;
    private final Producer mInputProducer;
    private final BufferedDiskCache mSmallImageBufferedDiskCache;

}
