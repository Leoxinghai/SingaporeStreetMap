// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.producers;

import com.android.internal.util.Predicate;
import com.facebook.cache.common.CacheKey;
import com.facebook.common.internal.ImmutableMap;
import com.facebook.common.references.CloseableReference;
import com.facebook.imagepipeline.cache.*;
import com.facebook.imagepipeline.request.*;

import java.util.Map;

// Referenced classes of package com.facebook.imagepipeline.producers:
//            Producer, ProducerContext, ProducerListener, Consumer,
//            DelegatingConsumer

public class PostprocessedBitmapMemoryCacheProducer
    implements Producer
{
    public static class CachedPostprocessorConsumer extends DelegatingConsumer
    {

        protected void onNewResultImpl(CloseableReference closeablereference, boolean flag)
        {
            CloseableReference closeablereference1;
            if(!flag && !mIsRepeatedProcessor)
                return;
            if(closeablereference == null)
            {
                getConsumer().onNewResult(null, flag);
                return;
            }
            Consumer consumer;
            if(mCacheKey != null)
            {
                mMemoryCache.removeAll(new Predicate() {

                    public boolean apply(CacheKey cachekey)
                    {
                        if(cachekey instanceof BitmapMemoryCacheKey)
                            return mProcessorName.equals(((BitmapMemoryCacheKey)cachekey).getPostprocessorName());
                        else
                            return false;
                    }

                    public boolean apply(Object obj)
                    {
                        return apply((CacheKey)obj);
                    }

                });

                closeablereference1 = mMemoryCache.cache(mCacheKey, closeablereference);
            } else
            {
                closeablereference1 = closeablereference;
            }
            getConsumer().onProgressUpdate(1.0F);
            consumer = getConsumer();
            if(closeablereference1 != null)
                closeablereference = closeablereference1;
            consumer.onNewResult(closeablereference, flag);
            CloseableReference.closeSafely(closeablereference1);
            return;
        }

        protected void onNewResultImpl(Object obj, boolean flag)
        {
            onNewResultImpl((CloseableReference)obj, flag);
        }

        private final CacheKey mCacheKey;
        private final boolean mIsRepeatedProcessor;
        private final MemoryCache mMemoryCache;
        private final String mProcessorName;


        public CachedPostprocessorConsumer(Consumer consumer, CacheKey cachekey, boolean flag, String s, MemoryCache memorycache)
        {
            super(consumer);
            mCacheKey = cachekey;
            mIsRepeatedProcessor = flag;
            mProcessorName = s;
            mMemoryCache = memorycache;
        }
    }


    public PostprocessedBitmapMemoryCacheProducer(MemoryCache memorycache, CacheKeyFactory cachekeyfactory, Producer producer)
    {
        mMemoryCache = memorycache;
        mCacheKeyFactory = cachekeyfactory;
        mInputProducer = producer;
    }

    protected String getProducerName()
    {
        return "PostprocessedBitmapMemoryCacheProducer";
    }

    public void produceResults(Consumer consumer, ProducerContext producercontext)
    {
        Object obj2 = null;
        ProducerListener producerlistener = producercontext.getListener();
        String s = producercontext.getId();
        Object obj1 = producercontext.getImageRequest();
        Postprocessor postprocessor = ((ImageRequest) (obj1)).getPostprocessor();
        if(postprocessor == null)
        {
            mInputProducer.produceResults(consumer, producercontext);
            return;
        }
        producerlistener.onProducerStart(s, getProducerName());
        CacheKey cachekey = postprocessor.getPostprocessorCacheKey();
        Object obj = null;
        if(cachekey != null)
        {
            obj1 = mCacheKeyFactory.getPostprocessedBitmapCacheKey(((ImageRequest) (obj1)));
            obj = mMemoryCache.get(obj1);
        } else
        {
            obj1 = null;
        }
        Map map;
        if(obj != null)
        {
            obj1 = getProducerName();
            if(producerlistener.requiresExtraMap(s))
                map = ImmutableMap.of("cached_value_found", "true");
            else
                map = null;
            producerlistener.onProducerFinishWithSuccess(s, ((String) (obj1)), map);
            consumer.onProgressUpdate(1.0F);
            consumer.onNewResult(obj, true);
            ((CloseableReference) (obj)).close();
            return;
        }
        obj = new CachedPostprocessorConsumer(consumer, ((CacheKey) (obj1)), postprocessor instanceof RepeatedPostprocessor, postprocessor.getClass().getName(), mMemoryCache);
        obj1 = getProducerName();
        Map map2 = null;
        if(producerlistener.requiresExtraMap(s))
            map2 = ImmutableMap.of("cached_value_found", "false");
        producerlistener.onProducerFinishWithSuccess(s, ((String) (obj1)), map2);
        mInputProducer.produceResults(((Consumer) (obj)), producercontext);
    }

    static final String PRODUCER_NAME = "PostprocessedBitmapMemoryCacheProducer";
    static final String VALUE_FOUND = "cached_value_found";
    private final CacheKeyFactory mCacheKeyFactory;
    private final Producer mInputProducer;
    private final MemoryCache mMemoryCache;
}
