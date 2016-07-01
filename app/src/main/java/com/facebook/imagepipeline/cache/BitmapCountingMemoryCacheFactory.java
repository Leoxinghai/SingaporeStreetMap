// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.cache;

import com.facebook.common.internal.Supplier;
import com.facebook.common.memory.MemoryTrimmableRegistry;
import com.facebook.imagepipeline.image.CloseableImage;

// Referenced classes of package com.facebook.imagepipeline.cache:
//            CountingMemoryCache, BitmapMemoryCacheTrimStrategy, ValueDescriptor

public class BitmapCountingMemoryCacheFactory
{

    public BitmapCountingMemoryCacheFactory()
    {
    }

    public static CountingMemoryCache get(Supplier supplier, MemoryTrimmableRegistry memorytrimmableregistry)
    {
        CountingMemoryCache temp = new CountingMemoryCache(new ValueDescriptor() {

            public int getSizeInBytes(CloseableImage closeableimage)
            {
                return closeableimage.getSizeInBytes();
            }

            public int getSizeInBytes(Object obj)
            {
                return getSizeInBytes((CloseableImage)obj);
            }

        }, new BitmapMemoryCacheTrimStrategy(), supplier);
        memorytrimmableregistry.registerMemoryTrimmable(temp);
        return temp;
    }
}
