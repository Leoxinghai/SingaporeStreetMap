

package com.facebook.imagepipeline.memory;

import com.facebook.common.internal.Preconditions;
import java.util.LinkedList;
import java.util.Queue;

class Bucket
{

    public Bucket(int i, int j, int k)
    {
        boolean flag1 = true;
        boolean flag;
        if(i > 0)
            flag = true;
        else
            flag = false;
        Preconditions.checkState(flag);
        if(j >= 0)
            flag = true;
        else
            flag = false;
        Preconditions.checkState(flag);
        if(k >= 0)
            flag = flag1;
        else
            flag = false;
        Preconditions.checkState(flag);
        mItemSize = i;
        mMaxLength = j;
        mInUseLength = k;
    }

    void addToFreeList(Object obj)
    {
        mFreeList.add(obj);
    }

    public void decrementInUseCount()
    {
        boolean flag;
        if(mInUseLength > 0)
            flag = true;
        else
            flag = false;
        Preconditions.checkState(flag);
        mInUseLength = mInUseLength - 1;
    }

    public Object get()
    {
        Object obj = pop();
        if(obj != null)
            mInUseLength = mInUseLength + 1;
        return obj;
    }

    int getFreeListSize()
    {
        return mFreeList.size();
    }

    public int getInUseCount()
    {
        return mInUseLength;
    }

    public void incrementInUseCount()
    {
        mInUseLength = mInUseLength + 1;
    }

    public boolean isMaxLengthExceeded()
    {
        return mInUseLength + getFreeListSize() > mMaxLength;
    }

    public Object pop()
    {
        return mFreeList.poll();
    }

    public void release(Object obj)
    {
        Preconditions.checkNotNull(obj);
        boolean flag;
        if(mInUseLength > 0)
            flag = true;
        else
            flag = false;
        Preconditions.checkState(flag);
        mInUseLength = mInUseLength - 1;
        addToFreeList(obj);
    }

    private static final String TAG = "com.facebook.imagepipeline.common.Bucket";
    final Queue mFreeList = new LinkedList();
    private int mInUseLength;
    public final int mItemSize;
    public final int mMaxLength;
}
