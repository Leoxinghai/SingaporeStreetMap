

package com.facebook.imagepipeline.producers;

import com.facebook.cache.common.CacheKey;
import com.facebook.imagepipeline.cache.CacheKeyFactory;
import com.facebook.imagepipeline.cache.MemoryCache;

// Referenced classes of package com.facebook.imagepipeline.producers:
//            BitmapMemoryCacheProducer, Producer, Consumer

public class BitmapMemoryCacheGetProducer extends BitmapMemoryCacheProducer
{

    public BitmapMemoryCacheGetProducer(MemoryCache memorycache, CacheKeyFactory cachekeyfactory, Producer producer)
    {
        super(memorycache, cachekeyfactory, producer);
    }

    protected String getProducerName()
    {
        return "BitmapMemoryCacheGetProducer";
    }

    protected Consumer wrapConsumer(Consumer consumer, CacheKey cachekey)
    {
        return consumer;
    }

    static final String PRODUCER_NAME = "BitmapMemoryCacheGetProducer";
}
