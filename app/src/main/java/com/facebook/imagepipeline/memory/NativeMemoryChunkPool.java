// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.memory;

import android.util.SparseIntArray;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.memory.MemoryTrimmableRegistry;

// Referenced classes of package com.facebook.imagepipeline.memory:
//            BasePool, PoolParams, NativeMemoryChunk, PoolStatsTracker

public class NativeMemoryChunkPool extends BasePool
{

    public NativeMemoryChunkPool(MemoryTrimmableRegistry memorytrimmableregistry, PoolParams poolparams, PoolStatsTracker poolstatstracker)
    {
        super(memorytrimmableregistry, poolparams, poolstatstracker);
        SparseIntArray temp = poolparams.bucketSizes;
        mBucketSizes = new int[temp.size()];
        for(int i = 0; i < mBucketSizes.length; i++)
            mBucketSizes[i] = temp.keyAt(i);

        initialize();
    }

    protected NativeMemoryChunk alloc(int i)
    {
        return new NativeMemoryChunk(i);
    }


    protected void free(NativeMemoryChunk nativememorychunk)
    {
        Preconditions.checkNotNull(nativememorychunk);
        nativememorychunk.close();
    }

    protected void free(Object obj)
    {
        free((NativeMemoryChunk)obj);
    }

    protected int getBucketedSize(int i)
    {
        if(i <= 0)
            throw new BasePool.InvalidSizeException(Integer.valueOf(i));
        int ai[] = mBucketSizes;
        int k = ai.length;
        for(int j = 0; j < k; j++)
        {
            int l = ai[j];
            if(l >= i)
                return l;
        }

        return i;
    }

    protected int getBucketedSizeForValue(NativeMemoryChunk nativememorychunk)
    {
        Preconditions.checkNotNull(nativememorychunk);
        return nativememorychunk.getSize();
    }

    protected int getBucketedSizeForValue(Object obj)
    {
        return getBucketedSizeForValue((NativeMemoryChunk)obj);
    }

    public int getMinBufferSize()
    {
        return mBucketSizes[0];
    }

    protected int getSizeInBytes(int i)
    {
        return i;
    }

    protected boolean isReusable(NativeMemoryChunk nativememorychunk)
    {
        Preconditions.checkNotNull(nativememorychunk);
        return !nativememorychunk.isClosed();
    }

    protected boolean isReusable(Object obj)
    {
        return isReusable((NativeMemoryChunk)obj);
    }

    private final int mBucketSizes[];
}
