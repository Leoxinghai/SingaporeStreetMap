// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.memory;

import com.facebook.common.internal.Preconditions;
import com.facebook.common.references.CloseableReference;

// Referenced classes of package com.facebook.imagepipeline.memory:
//            PooledByteBuffer, NativeMemoryChunk

public class NativePooledByteBuffer
    implements PooledByteBuffer
{

    public NativePooledByteBuffer(CloseableReference closeablereference, int i)
    {
        Preconditions.checkNotNull(closeablereference);
        boolean flag;
        if(i >= 0 && i <= ((NativeMemoryChunk)closeablereference.get()).getSize())
            flag = true;
        else
            flag = false;
        Preconditions.checkArgument(flag);
        mBufRef = closeablereference.clone();
        mSize = i;
    }

    public void close()
    {
        CloseableReference.closeSafely(mBufRef);
        mBufRef = null;
        return;
    }

    void ensureValid()
    {
        if(isClosed())
            throw new PooledByteBuffer.ClosedException();
    }

    public long getNativePtr()
    {
        long l;
        ensureValid();
        l = ((NativeMemoryChunk)mBufRef.get()).getNativePtr();
        return l;
    }

    public boolean isClosed()
    {
        boolean flag = CloseableReference.isValid(mBufRef);
        if(!flag)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public byte read(int i)
    {
        boolean flag1 = true;
        ensureValid();
        byte byte0;
        boolean flag;
        if(i >= 0)
            flag = true;
        else
            flag = false;
        Preconditions.checkArgument(flag);
        if(i < mSize)
            flag = flag1;
        else
            flag = false;
        Preconditions.checkArgument(flag);
        byte0 = ((NativeMemoryChunk)mBufRef.get()).read(i);
        return byte0;
    }

    public void read(int i, byte abyte0[], int j, int k)
    {
        ensureValid();
        boolean flag;
        if(i + k <= mSize)
            flag = true;
        else
            flag = false;
        Preconditions.checkArgument(flag);
        ((NativeMemoryChunk)mBufRef.get()).read(i, abyte0, j, k);
        return;
    }

    public int size()
    {
        int i;
        ensureValid();
        i = mSize;
        return i;
    }

    CloseableReference mBufRef;
    private final int mSize;
}
