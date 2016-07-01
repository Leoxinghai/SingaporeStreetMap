// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.memory;

import android.util.SparseIntArray;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.memory.MemoryTrimmableRegistry;

// Referenced classes of package com.facebook.imagepipeline.memory:
//            BasePool, ByteArrayPool, PoolParams, PoolStatsTracker

public class GenericByteArrayPool extends BasePool
    implements ByteArrayPool
{

    public GenericByteArrayPool(MemoryTrimmableRegistry memorytrimmableregistry, PoolParams poolparams, PoolStatsTracker poolstatstracker)
    {
        super(memorytrimmableregistry, poolparams, poolstatstracker);
        SparseIntArray temp = poolparams.bucketSizes;
        mBucketSizes = new int[temp.size()];
        for(int i = 0; i < temp.size(); i++)
            mBucketSizes[i] = temp.keyAt(i);

        initialize();
    }


    protected byte[] alloc(int i)
    {
        return new byte[i];
    }

    protected void free(Object obj)
    {
        free((byte[])obj);
    }

    protected void free(byte abyte0[])
    {
        Preconditions.checkNotNull(abyte0);
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

    protected int getBucketedSizeForValue(Object obj)
    {
        return getBucketedSizeForValue((byte[])obj);
    }

    protected int getBucketedSizeForValue(byte abyte0[])
    {
        Preconditions.checkNotNull(abyte0);
        return abyte0.length;
    }

    public int getMinBufferSize()
    {
        return mBucketSizes[0];
    }

    protected int getSizeInBytes(int i)
    {
        return i;
    }

    private final int mBucketSizes[];
}
