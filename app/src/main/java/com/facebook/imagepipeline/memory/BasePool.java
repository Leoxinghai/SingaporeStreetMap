// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.memory;

import android.util.SparseArray;
import android.util.SparseIntArray;
import com.facebook.common.internal.*;
import com.facebook.common.logging.FLog;
import com.facebook.common.memory.MemoryTrimType;
import com.facebook.common.memory.MemoryTrimmableRegistry;
import java.util.*;

// Referenced classes of package com.facebook.imagepipeline.memory:
//            Pool, PoolParams, PoolStatsTracker, Bucket

public abstract class BasePool
    implements Pool
{
    static class Counter
    {

        public void decrement(int i)
        {
            if(mNumBytes >= i && mCount > 0)
            {
                mCount = mCount - 1;
                mNumBytes = mNumBytes - i;
                return;
            } else
            {
                FLog.wtf("com.facebook.imagepipeline.common.BasePool.Counter", "Unexpected decrement of %d. Current numBytes = %d, count = %d", new Object[] {
                    Integer.valueOf(i), Integer.valueOf(mNumBytes), Integer.valueOf(mCount)
                });
                return;
            }
        }

        public void increment(int i)
        {
            mCount = mCount + 1;
            mNumBytes = mNumBytes + i;
        }

        public void reset()
        {
            mCount = 0;
            mNumBytes = 0;
        }

        private static final String TAG = "com.facebook.imagepipeline.common.BasePool.Counter";
        int mCount;
        int mNumBytes;

        Counter()
        {
        }
    }

    public static class InvalidSizeException extends RuntimeException
    {

        public InvalidSizeException(Object obj)
        {
            super((new StringBuilder()).append("Invalid size: ").append(obj.toString()).toString());
        }
    }

    public static class InvalidValueException extends RuntimeException
    {

        public InvalidValueException(Object obj)
        {
            super((new StringBuilder()).append("Invalid value: ").append(obj.toString()).toString());
        }
    }

    public static class PoolSizeViolationException extends RuntimeException
    {

        public PoolSizeViolationException(int i, int j, int k, int l)
        {
            super((new StringBuilder()).append("Pool hard cap violation? Hard cap = ").append(i).append(" Used size = ").append(j).append(" Free size = ").append(k).append(" Request size = ").append(l).toString());
        }
    }

    public static class SizeTooLargeException extends InvalidSizeException
    {

        public SizeTooLargeException(Object obj)
        {
            super(obj);
        }
    }


    public BasePool(MemoryTrimmableRegistry memorytrimmableregistry, PoolParams poolparams, PoolStatsTracker poolstatstracker)
    {
        mMemoryTrimmableRegistry = (MemoryTrimmableRegistry)Preconditions.checkNotNull(memorytrimmableregistry);
        mPoolParams = (PoolParams)Preconditions.checkNotNull(poolparams);
        mPoolStatsTracker = (PoolStatsTracker)Preconditions.checkNotNull(poolstatstracker);
        initBuckets(new SparseIntArray(0));
    }

    private void ensurePoolSizeInvariant()
    {
        Exception exception;
        boolean flag;
        if(isMaxSizeSoftCapExceeded() && mFree.mNumBytes != 0)
            flag = false;
        else
            flag = true;
        Preconditions.checkState(flag);
        return;
    }

    private void initBuckets(SparseIntArray sparseintarray)
    {
        SparseIntArray sparseintarray1;
        Preconditions.checkNotNull(sparseintarray);
        mBuckets.clear();
        sparseintarray1 = mPoolParams.bucketSizes;
        int i;
        mAllowNewBuckets = true;
        if(sparseintarray1 != null) {
            i = 0;
            for (; i < sparseintarray1.size(); ) {
                int j = sparseintarray1.keyAt(i);
                int k = sparseintarray1.valueAt(i);
                int l = sparseintarray.get(j, 0);
                mBuckets.put(j, new Bucket(getSizeInBytes(j), k, l));
                i++;
            }
            mAllowNewBuckets = false;
        }

        return;
    }

    private void logStats()
    {
        if(FLog.isLoggable(2))
            FLog.v(TAG, "Used = (%d, %d); Free = (%d, %d)", Integer.valueOf(mUsed.mCount), Integer.valueOf(mUsed.mNumBytes), Integer.valueOf(mFree.mCount), Integer.valueOf(mFree.mNumBytes));
    }

    protected abstract Object alloc(int i);

    boolean canAllocate(int i)
    {
        boolean flag = false;
        int j = mPoolParams.maxSizeHardCap;
        if(i <= j - mUsed.mNumBytes) {
            int k = mPoolParams.maxSizeSoftCap;
            if(i > k - (mUsed.mNumBytes + mFree.mNumBytes))
                trimToSize(k - i);
            if(i <= j - (mUsed.mNumBytes + mFree.mNumBytes))
                flag = true;
            else
                mPoolStatsTracker.onHardCapReached();
        } else
            mPoolStatsTracker.onHardCapReached();

        return flag;
    }

    protected abstract void free(Object obj);

    public Object get(int i)
    {
        ensurePoolSizeInvariant();
        i = getBucketedSize(i);
        Object obj = getBucket(i);
        Object obj1 = null;
        if(obj != null)
            obj1 = ((Bucket) (obj)).get();
        if(obj1 != null) {
            Preconditions.checkState(mInUseValues.add(obj1));
            i = getBucketedSizeForValue(obj1);
            int j = getSizeInBytes(i);
            mUsed.increment(j);
            mFree.decrement(j);
            mPoolStatsTracker.onValueReuse(j);
            logStats();
            if (FLog.isLoggable(2))
                FLog.v(TAG, "get (reuse) (object, size) = (%x, %s)", Integer.valueOf(System.identityHashCode(obj1)), Integer.valueOf(i));
            return obj1;
        }

        int k;
        k = getSizeInBytes(i);
        if(!canAllocate(k))
            throw new PoolSizeViolationException(mPoolParams.maxSizeHardCap, mUsed.mNumBytes, mFree.mNumBytes, k);
        mUsed.increment(k);
        if(obj != null) {
            ((Bucket) (obj)).incrementInUseCount();
            obj = null;
            obj1 = alloc(i);
            obj = obj1;
        }

        Preconditions.checkState(mInUseValues.add(obj));
        trimToSoftCap();
        mPoolStatsTracker.onAlloc(k);
        logStats();
        if(FLog.isLoggable(2))
            FLog.v(TAG, "get (alloc) (object, size) = (%x, %s)", Integer.valueOf(System.identityHashCode(obj)), Integer.valueOf(i));
        return obj;

/*

        Throwable throwable;
        throwable;
        Bucket bucket;
        mUsed.decrement(k);
        bucket = getBucket(i);
        if(bucket == null)
            Throwables.propagateIfPossible(throwable);
        bucket.decrementInUseCount();
  */
    }

    Bucket getBucket(int i)
    {
        Bucket bucket = (Bucket)mBuckets.get(i);
        if(bucket == null) {
            boolean flag = mAllowNewBuckets;
            if (flag) {
                if (FLog.isLoggable(2))
                    FLog.v(TAG, "creating new bucket %s", Integer.valueOf(i));
                bucket = newBucket(i);
                mBuckets.put(i, bucket);

            }
        }
        return bucket;
    }

    protected abstract int getBucketedSize(int i);

    protected abstract int getBucketedSizeForValue(Object obj);

    protected abstract int getSizeInBytes(int i);

    public Map getStats()
    {
        HashMap hashmap = new HashMap();
        int i = 0;

        for(;i < mBuckets.size();) {
            int j = mBuckets.keyAt(i);
            Bucket bucket = (Bucket) mBuckets.valueAt(i);
            hashmap.put((new StringBuilder()).append("buckets_used_").append(getSizeInBytes(j)).toString(), Integer.valueOf(bucket.getInUseCount()));
            i++;
        }
        hashmap.put("soft_cap", Integer.valueOf(mPoolParams.maxSizeSoftCap));
        hashmap.put("hard_cap", Integer.valueOf(mPoolParams.maxSizeHardCap));
        hashmap.put("used_count", Integer.valueOf(mUsed.mCount));
        hashmap.put("used_bytes", Integer.valueOf(mUsed.mNumBytes));
        hashmap.put("free_count", Integer.valueOf(mFree.mCount));
        hashmap.put("free_bytes", Integer.valueOf(mFree.mNumBytes));
        return hashmap;
    }

    protected void initialize()
    {
        mMemoryTrimmableRegistry.registerMemoryTrimmable(this);
        mPoolStatsTracker.setBasePool(this);
    }

    boolean isMaxSizeSoftCapExceeded()
    {
        boolean flag;
        if(mUsed.mNumBytes + mFree.mNumBytes > mPoolParams.maxSizeSoftCap)
            flag = true;
        else
            flag = false;
        if(flag)
            mPoolStatsTracker.onSoftCapReached();
        return flag;
    }

    protected boolean isReusable(Object obj)
    {
        Preconditions.checkNotNull(obj);
        return true;
    }

    Bucket newBucket(int i)
    {
        return new Bucket(getSizeInBytes(i), 0x7fffffff, 0);
    }

    protected void onParamsChanged()
    {
    }

    public void release(Object obj)
    {
        int i;
        Preconditions.checkNotNull(obj);
        i = getBucketedSizeForValue(obj);
        int j = getSizeInBytes(i);
        Bucket bucket = getBucket(i);
        if(mInUseValues.remove(obj)) {
            if(bucket != null) {
                if (!bucket.isMaxLengthExceeded() && !isMaxSizeSoftCapExceeded() && isReusable(obj)) {
                    logStats();
                    return;
                }
            }
            if(bucket == null) {

                bucket.release(obj);
                mFree.increment(j);
                mUsed.decrement(j);
                mPoolStatsTracker.onValueRelease(j);
                if(FLog.isLoggable(2))
                    FLog.v(TAG, "release (reuse) (object, size) = (%x, %s)", Integer.valueOf(System.identityHashCode(obj)), Integer.valueOf(i));
            } else {
                bucket.decrementInUseCount();
                if (FLog.isLoggable(2))
                    FLog.v(TAG, "release (free) (object, size) = (%x, %s)", Integer.valueOf(System.identityHashCode(obj)), Integer.valueOf(i));
                free(obj);
                mUsed.decrement(j);
                mPoolStatsTracker.onFree(j);
            }
        } else {
            FLog.e(TAG, "release (free, value unrecognized) (object, size) = (%x, %s)", new Object[]{
                    Integer.valueOf(System.identityHashCode(obj)), Integer.valueOf(i)
            });
            free(obj);
            mPoolStatsTracker.onFree(j);
        }

        logStats();
        return;

    }

    public void trim(MemoryTrimType memorytrimtype)
    {
        trimToNothing();
    }

    void trimToNothing() {
        ArrayList arraylist = new ArrayList(mBuckets.size());
        Object obj = new SparseIntArray();
        int i = 0;

        for (; i < mBuckets.size(); ) {
            Bucket bucket = (Bucket) mBuckets.valueAt(i);
            if (bucket.getFreeListSize() > 0)
                arraylist.add(bucket);
            ((SparseIntArray) (obj)).put(mBuckets.keyAt(i), bucket.getInUseCount());
            i++;
        }

        initBuckets(((SparseIntArray) (obj)));
        mFree.reset();
        logStats();
        onParamsChanged();
        i = 0;
        for(;i < arraylist.size();) {
            obj = (Bucket) arraylist.get(i);
            Object obj1;
            obj1 = ((Bucket) (obj)).pop();
            if (obj1 != null) {
                free(obj1);
            }
            i++;
        }

        return;
    }

    void trimToSize(int i)
    {
        int k = Math.min((mUsed.mNumBytes + mFree.mNumBytes) - i, mFree.mNumBytes);
        if(k <= 0)
            return;

        if(FLog.isLoggable(2))
            FLog.v(TAG, "trimToSize: TargetSize = %d; Initial Size = %d; Bytes to free = %d", Integer.valueOf(i), Integer.valueOf(mUsed.mNumBytes + mFree.mNumBytes), Integer.valueOf(k));
        logStats();
        int j = 0;

        for(;j < mBuckets.size() && k > 0;) {
            Bucket bucket = (Bucket)mBuckets.valueAt(j);

            for(;k > 0;) {
                Object obj = bucket.pop();
                if (obj != null)
                    free(obj);
                k -= bucket.mItemSize;
                mFree.decrement(bucket.mItemSize);
            }
            j++;
        }
        logStats();
        if(FLog.isLoggable(2))
            FLog.v(TAG, "trimToSize: TargetSize = %d; Final Size = %d", Integer.valueOf(i), Integer.valueOf(mUsed.mNumBytes + mFree.mNumBytes));
    }

    void trimToSoftCap()
    {
        if(isMaxSizeSoftCapExceeded())
            trimToSize(mPoolParams.maxSizeSoftCap);
        return;
    }

    private final Class TAG = getClass();
    private boolean mAllowNewBuckets;
    final SparseArray mBuckets = new SparseArray();
    final Counter mFree = new Counter();
    final Set mInUseValues = Sets.newIdentityHashSet();
    final MemoryTrimmableRegistry mMemoryTrimmableRegistry;
    final PoolParams mPoolParams;
    private final PoolStatsTracker mPoolStatsTracker;
    final Counter mUsed = new Counter();
}
