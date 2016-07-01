// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.cache;

import com.facebook.common.internal.Supplier;
import com.facebook.common.memory.MemoryTrimmableRegistry;
import com.facebook.imagepipeline.memory.PooledByteBuffer;

// Referenced classes of package com.facebook.imagepipeline.cache:
//            CountingMemoryCache, NativeMemoryCacheTrimStrategy, ValueDescriptor

public class EncodedCountingMemoryCacheFactory
{

    public EncodedCountingMemoryCacheFactory()
    {
    }

    public static CountingMemoryCache get(Supplier supplier, MemoryTrimmableRegistry memorytrimmableregistry)
    {
        CountingMemoryCache temp = new CountingMemoryCache(new ValueDescriptor() {

            public int getSizeInBytes(PooledByteBuffer pooledbytebuffer)
            {
                return pooledbytebuffer.size();
            }
            public int getSizeInBytes(Object obj) {
                return getSizeInBytes((PooledByteBuffer)obj);
            }
        }, new NativeMemoryCacheTrimStrategy(), supplier);
        memorytrimmableregistry.registerMemoryTrimmable(temp);
        return temp;
    }
}
