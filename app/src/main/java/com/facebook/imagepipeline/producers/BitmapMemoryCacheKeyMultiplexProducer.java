// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.producers;

import android.util.Pair;
import com.facebook.common.references.CloseableReference;
import com.facebook.imagepipeline.cache.CacheKeyFactory;
import java.io.Closeable;

// Referenced classes of package com.facebook.imagepipeline.producers:
//            MultiplexProducer, ProducerContext, Producer

public class BitmapMemoryCacheKeyMultiplexProducer extends MultiplexProducer
{

    public BitmapMemoryCacheKeyMultiplexProducer(CacheKeyFactory cachekeyfactory, Producer producer)
    {
        super(producer);
        mCacheKeyFactory = cachekeyfactory;
    }

    public CloseableReference cloneOrNull(CloseableReference closeablereference)
    {
        return CloseableReference.cloneOrNull(closeablereference);
    }

    public Closeable cloneOrNull(Closeable closeable)
    {
        return cloneOrNull((CloseableReference)closeable);
    }

    protected Pair getKey(ProducerContext producercontext)
    {
        return Pair.create(mCacheKeyFactory.getBitmapCacheKey(producercontext.getImageRequest()), producercontext.getLowestPermittedRequestLevel());
    }


    private final CacheKeyFactory mCacheKeyFactory;
}
