// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.producers;

import com.facebook.cache.common.CacheKey;
import com.facebook.common.internal.ImmutableMap;
import com.facebook.common.references.CloseableReference;
import com.facebook.imagepipeline.cache.CacheKeyFactory;
import com.facebook.imagepipeline.cache.MemoryCache;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.QualityInfo;

import java.util.Map;

// Referenced classes of package com.facebook.imagepipeline.producers:
//            Producer, ProducerContext, ProducerListener, Consumer,
//            DelegatingConsumer

public class BitmapMemoryCacheProducer
    implements Producer
{

    public BitmapMemoryCacheProducer(MemoryCache memorycache, CacheKeyFactory cachekeyfactory, Producer producer)
    {
        mMemoryCache = memorycache;
        mCacheKeyFactory = cachekeyfactory;
        mInputProducer = producer;
    }

    protected String getProducerName()
    {
        return "BitmapMemoryCacheProducer";
    }

    public void produceResults(Consumer consumer, ProducerContext producercontext)
    {
        Object obj1 = null;
        ProducerListener producerlistener = producercontext.getListener();
        String s = producercontext.getId();
        producerlistener.onProducerStart(s, getProducerName());
        Object obj = producercontext.getImageRequest();
        Object obj2 = mCacheKeyFactory.getBitmapCacheKey(((com.facebook.imagepipeline.request.ImageRequest) (obj)));
        CloseableReference closeablereference = mMemoryCache.get(obj2);
        if(closeablereference != null)
        {
            boolean flag = ((CloseableImage)closeablereference.get()).getQualityInfo().isOfFullQuality();
            if(flag)
            {
                String s1 = getProducerName();
                if(producerlistener.requiresExtraMap(s))
                    obj = ImmutableMap.of("cached_value_found", "true");
                else
                    obj = null;
                producerlistener.onProducerFinishWithSuccess(s, s1, ((java.util.Map) (obj)));
                consumer.onProgressUpdate(1.0F);
            }
            consumer.onNewResult(closeablereference, flag);
            closeablereference.close();
            if(flag)
                return;
        }
        Map map = null;
        if(producercontext.getLowestPermittedRequestLevel().getValue() >= com.facebook.imagepipeline.request.ImageRequest.RequestLevel.BITMAP_MEMORY_CACHE.getValue())
        {
            obj = getProducerName();
            if(producerlistener.requiresExtraMap(s))
                map = ImmutableMap.of("cached_value_found", "false");
            else
                map = null;
            producerlistener.onProducerFinishWithSuccess(s, ((String) (obj)), map);
            consumer.onNewResult(null, true);
            return;
        }
        obj = wrapConsumer(consumer, ((CacheKey) (obj2)));
        obj2 = getProducerName();
        consumer = null;
        if(producerlistener.requiresExtraMap(s))
            map = ImmutableMap.of("cached_value_found", "false");
        producerlistener.onProducerFinishWithSuccess(s, ((String) (obj2)), map);
        mInputProducer.produceResults(((Consumer) (obj)), producercontext);
    }

    protected Consumer wrapConsumer(final Consumer final_consumer, final CacheKey cachekey)
    {
        return new DelegatingConsumer(final_consumer) {

            public void onNewResultImpl(CloseableReference closeablereference, boolean flag)
            {
                CloseableReference closeablereference1 = null;
                if(closeablereference == null)
                {
                    if(flag)
                        getConsumer().onNewResult(null, true);
                    return;
                }
                if(((CloseableImage)closeablereference.get()).isStateful())
                {
                    getConsumer().onNewResult(closeablereference, flag);
                    return;
                }
                if(!flag) {
                    closeablereference1 = mMemoryCache.get(cachekey);
                    if (closeablereference1 != null) {
                        QualityInfo qualityinfo = ((CloseableImage) closeablereference.get()).getQualityInfo();
                        QualityInfo qualityinfo1 = ((CloseableImage) closeablereference1.get()).getQualityInfo();
                        if (qualityinfo1.isOfFullQuality() || qualityinfo1.getQuality() >= qualityinfo.getQuality())
                            getConsumer().onNewResult(closeablereference1, false);
                        CloseableReference.closeSafely(closeablereference1);
                        return;
                    }
                }

                CloseableReference.closeSafely(closeablereference1);
                closeablereference1 = mMemoryCache.cache(cachekey, closeablereference);
                if(flag) {
                    getConsumer().onProgressUpdate(1.0F);
                    Consumer consumer = getConsumer();
                    if (closeablereference1 != null)
                        closeablereference = closeablereference1;
                    consumer.onNewResult(closeablereference, flag);
                    CloseableReference.closeSafely(closeablereference1);
                    return;
                }
            }

            public void onNewResultImpl(Object obj, boolean flag)
            {
                onNewResultImpl((CloseableReference)obj, flag);
            }
        };
    }

    static final String PRODUCER_NAME = "BitmapMemoryCacheProducer";
    static final String VALUE_FOUND = "cached_value_found";
    private final CacheKeyFactory mCacheKeyFactory;
    private final Producer mInputProducer;
    private final MemoryCache mMemoryCache;

}
