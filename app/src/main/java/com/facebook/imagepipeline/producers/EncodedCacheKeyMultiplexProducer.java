// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.producers;

import android.util.Pair;
import com.facebook.imagepipeline.cache.CacheKeyFactory;
import com.facebook.imagepipeline.image.EncodedImage;
import java.io.Closeable;

// Referenced classes of package com.facebook.imagepipeline.producers:
//            MultiplexProducer, ProducerContext, Producer

public class EncodedCacheKeyMultiplexProducer extends MultiplexProducer
{

    public EncodedCacheKeyMultiplexProducer(CacheKeyFactory cachekeyfactory, Producer producer)
    {
        super(producer);
        mCacheKeyFactory = cachekeyfactory;
    }

    public EncodedImage cloneOrNull(EncodedImage encodedimage)
    {
        return EncodedImage.cloneOrNull(encodedimage);
    }

    public Closeable cloneOrNull(Closeable closeable)
    {
        return cloneOrNull((EncodedImage)closeable);
    }

    protected Pair getKey(ProducerContext producercontext)
    {
        return Pair.create(mCacheKeyFactory.getEncodedCacheKey(producercontext.getImageRequest()), producercontext.getLowestPermittedRequestLevel());
    }


    private final CacheKeyFactory mCacheKeyFactory;
}
