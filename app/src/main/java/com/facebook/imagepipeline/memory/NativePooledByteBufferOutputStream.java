// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.memory;

import com.facebook.common.internal.Preconditions;
import com.facebook.common.references.CloseableReference;
import java.io.IOException;

// Referenced classes of package com.facebook.imagepipeline.memory:
//            PooledByteBufferOutputStream, NativeMemoryChunkPool, NativeMemoryChunk, NativePooledByteBuffer,
//            PooledByteBuffer

public class NativePooledByteBufferOutputStream extends PooledByteBufferOutputStream
{
    public static class InvalidStreamException extends RuntimeException
    {

        public InvalidStreamException()
        {
            super("OutputStream no longer valid");
        }
    }


    public NativePooledByteBufferOutputStream(NativeMemoryChunkPool nativememorychunkpool)
    {
        this(nativememorychunkpool, nativememorychunkpool.getMinBufferSize());
    }

    public NativePooledByteBufferOutputStream(NativeMemoryChunkPool nativememorychunkpool, int i)
    {
        boolean flag;
        if(i > 0)
            flag = true;
        else
            flag = false;
        Preconditions.checkArgument(flag);
        mPool = (NativeMemoryChunkPool)Preconditions.checkNotNull(nativememorychunkpool);
        mCount = 0;
        mBufRef = CloseableReference.of(mPool.get(i), mPool);
    }

    private void ensureValid()
    {
        if(!CloseableReference.isValid(mBufRef))
            throw new InvalidStreamException();
        else
            return;
    }

    public void close()
    {
        CloseableReference.closeSafely(mBufRef);
        mBufRef = null;
        mCount = -1;
        super.close();
    }

    void realloc(int i)
    {
        ensureValid();
        if(i <= ((NativeMemoryChunk)mBufRef.get()).getSize())
        {
            return;
        } else
        {
            NativeMemoryChunk nativememorychunk = (NativeMemoryChunk)mPool.get(i);
            ((NativeMemoryChunk)mBufRef.get()).copy(0, nativememorychunk, 0, mCount);
            mBufRef.close();
            mBufRef = CloseableReference.of(nativememorychunk, mPool);
            return;
        }
    }

    public int size()
    {
        return mCount;
    }

    public NativePooledByteBuffer toByteBuffer()
    {
        ensureValid();
        return new NativePooledByteBuffer(mBufRef, mCount);
    }


    public void write(int i)
        throws IOException
    {
        write(new byte[] {
            (byte)i
        });
    }

    public void write(byte abyte0[], int i, int j)
        throws IOException
    {
        if(i < 0 || j < 0 || i + j > abyte0.length)
        {
            throw new ArrayIndexOutOfBoundsException((new StringBuilder()).append("length=").append(abyte0.length).append("; regionStart=").append(i).append("; regionLength=").append(j).toString());
        } else
        {
            ensureValid();
            realloc(mCount + j);
            ((NativeMemoryChunk)mBufRef.get()).write(mCount, abyte0, i, j);
            mCount = mCount + j;
            return;
        }
    }

    private CloseableReference mBufRef;
    private int mCount;
    private final NativeMemoryChunkPool mPool;
}
