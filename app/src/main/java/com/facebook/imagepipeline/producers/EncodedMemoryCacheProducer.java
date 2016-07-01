// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.producers;

import com.facebook.cache.common.CacheKey;
import com.facebook.common.internal.ImmutableMap;
import com.facebook.common.references.CloseableReference;
import com.facebook.imagepipeline.cache.CacheKeyFactory;
import com.facebook.imagepipeline.cache.MemoryCache;
import com.facebook.imagepipeline.image.EncodedImage;

import java.util.Map;

// Referenced classes of package com.facebook.imagepipeline.producers:
//            Producer, ProducerContext, ProducerListener, Consumer,
//            DelegatingConsumer

public class EncodedMemoryCacheProducer
    implements Producer
{

    public EncodedMemoryCacheProducer(MemoryCache memorycache, CacheKeyFactory cachekeyfactory, Producer producer)
    {
        mMemoryCache = memorycache;
        mCacheKeyFactory = cachekeyfactory;
        mInputProducer = producer;
    }

    public void produceResults(final Consumer final_consumer, ProducerContext producercontext)
    {
        DelegatingConsumer delegatingconsumer;
        EncodedImage encodedimage;
        Object obj;
        Object obj1;
        String s;
        ProducerListener producerlistener;
        final CacheKey cachekey;
        obj = null;
        encodedimage = null;
        delegatingconsumer = null;
        s = producercontext.getId();
        producerlistener = producercontext.getListener();
        producerlistener.onProducerStart(s, "EncodedMemoryCacheProducer");
        obj1 = producercontext.getImageRequest();
        cachekey = mCacheKeyFactory.getEncodedCacheKey(((com.facebook.imagepipeline.request.ImageRequest) (obj1)));
        obj1 = mMemoryCache.get(cachekey);
        if(obj1 == null) {
            if(producercontext.getLowestPermittedRequestLevel().getValue() < com.facebook.imagepipeline.request.ImageRequest.RequestLevel.ENCODED_MEMORY_CACHE.getValue()) {
                delegatingconsumer = new DelegatingConsumer(final_consumer) {

                    public void onNewResultImpl(EncodedImage encodedimage1, boolean flag) {
                        Object obj2;
                        if (!flag || encodedimage1 == null) {
                            getConsumer().onNewResult(encodedimage1, flag);
                            return;
                        }
                        obj2 = encodedimage1.getByteBufferRef();
                        if (obj2 == null)
                            return;
                        CloseableReference closeablereference = mMemoryCache.cache(cachekey, ((CloseableReference) (obj2)));
                        CloseableReference.closeSafely(((CloseableReference) (obj2)));
                        if (closeablereference == null)
                            return;
                        obj2 = new EncodedImage(closeablereference);
                        ((EncodedImage) (obj2)).copyMetaDataFrom(encodedimage1);
                        CloseableReference.closeSafely(closeablereference);
                        getConsumer().onProgressUpdate(1.0F);
                        getConsumer().onNewResult(obj2, true);
                        EncodedImage.closeSafely(((EncodedImage) (obj2)));
                        return;
                    }

                    public void onNewResultImpl(Object obj2, boolean flag) {
                        onNewResultImpl((EncodedImage) obj2, flag);
                    }

            };
            //final_consumer = encodedimage;
            Map map = null;
            if (producerlistener.requiresExtraMap(s))
                map = ImmutableMap.of("cached_value_found", "false");
            producerlistener.onProducerFinishWithSuccess(s, "EncodedMemoryCacheProducer", map);
            mInputProducer.produceResults(delegatingconsumer, producercontext);
            CloseableReference.closeSafely(((CloseableReference) (obj1)));
            return;
        }
        encodedimage = new EncodedImage(((CloseableReference) (obj1)));
        producercontext = null;
        Map map = null;
        if(producerlistener.requiresExtraMap(s))
            map = ImmutableMap.of("cached_value_found", "true");
        producerlistener.onProducerFinishWithSuccess(s, "EncodedMemoryCacheProducer", map);
        final_consumer.onProgressUpdate(1.0F);
        final_consumer.onNewResult(encodedimage, true);
        EncodedImage.closeSafely(encodedimage);
        CloseableReference.closeSafely(((CloseableReference) (obj1)));
        return;

        }
        //producercontext = obj;
        Map map = null;
        if(producerlistener.requiresExtraMap(s))
            map = ImmutableMap.of("cached_value_found", "false");
        producerlistener.onProducerFinishWithSuccess(s, "EncodedMemoryCacheProducer", map);
        final_consumer.onNewResult(null, true);
        CloseableReference.closeSafely(((CloseableReference) (obj1)));
        return;
    }

    static final String PRODUCER_NAME = "EncodedMemoryCacheProducer";
    static final String VALUE_FOUND = "cached_value_found";
    private final CacheKeyFactory mCacheKeyFactory;
    private final Producer mInputProducer;
    private final MemoryCache mMemoryCache;

}
