// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.memory;

import com.facebook.common.internal.Preconditions;
import com.facebook.common.internal.Throwables;
import com.facebook.common.memory.*;
import com.facebook.common.references.*;
import java.util.concurrent.Semaphore;

// Referenced classes of package com.facebook.imagepipeline.memory:
//            PoolParams

public class SharedByteArray
    implements MemoryTrimmable
{

    public SharedByteArray(MemoryTrimmableRegistry memorytrimmableregistry, PoolParams poolparams)
    {
        boolean flag1 = false;
        Preconditions.checkNotNull(memorytrimmableregistry);
        boolean flag;
        if(poolparams.minBucketSize > 0)
            flag = true;
        else
            flag = false;
        Preconditions.checkArgument(flag);
        flag = flag1;
        if(poolparams.maxBucketSize >= poolparams.minBucketSize)
            flag = true;
        Preconditions.checkArgument(flag);
        mMaxByteArraySize = poolparams.maxBucketSize;
        mMinByteArraySize = poolparams.minBucketSize;
        memorytrimmableregistry.registerMemoryTrimmable(this);
    }

    private byte[] allocateByteArray(int i)
    {
        byte abyte0[];
        mByteArraySoftRef.clear();
        abyte0 = new byte[i];
        mByteArraySoftRef.set(abyte0);
        return abyte0;
    }

    private byte[] getByteArray(int i)
    {
        byte abyte0[];
        i = getBucketedSize(i);
        byte abyte1[] = (byte[])mByteArraySoftRef.get();
        if(abyte1 != null)
        {
            abyte0 = abyte1;
            if(abyte1.length >= i)
                return abyte0;
        }
        abyte0 = allocateByteArray(i);
        return abyte0;
    }

    public CloseableReference get(int i)
    {
        boolean flag1 = true;
        CloseableReference closeablereference;
        boolean flag;
        if(i > 0)
            flag = true;
        else
            flag = false;
        Preconditions.checkArgument(flag, "Size must be greater than zero");
        if(i <= mMaxByteArraySize)
            flag = flag1;
        else
            flag = false;
        Preconditions.checkArgument(flag, "Requested size is too big");
        mSemaphore.acquireUninterruptibly();
        try
        {
            closeablereference = CloseableReference.of(getByteArray(i), mResourceReleaser);
            return closeablereference;
        }
        catch(Throwable throwable)
        {
            mSemaphore.release();
            //throw Throwables.propagate(throwable);
        }
        return null;
    }

    int getBucketedSize(int i)
    {
        return Integer.highestOneBit(Math.max(i, mMinByteArraySize) - 1) * 2;
    }

    public void trim(MemoryTrimType memorytrimtype)
    {
        if(!mSemaphore.tryAcquire())
            return;
        mByteArraySoftRef.clear();
        mSemaphore.release();
        return;
    }

    final OOMSoftReference mByteArraySoftRef = new OOMSoftReference();
    final int mMaxByteArraySize;
    final int mMinByteArraySize;
    private final ResourceReleaser mResourceReleaser = new ResourceReleaser() {

        public void release(Object obj)
        {
            release((byte[])obj);
        }

        public void release(byte abyte0[])
        {
            mSemaphore.release();
        }

    };
    final Semaphore mSemaphore = new Semaphore(1);
}
