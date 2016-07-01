// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.memory;

import com.facebook.common.internal.Preconditions;
import com.facebook.common.internal.Throwables;
import com.facebook.common.references.CloseableReference;
import java.io.IOException;
import java.io.InputStream;

// Referenced classes of package com.facebook.imagepipeline.memory:
//            PooledByteBufferFactory, PooledByteStreams, NativePooledByteBufferOutputStream, NativeMemoryChunkPool,
//            NativePooledByteBuffer, PooledByteBuffer, PooledByteBufferOutputStream

public class NativePooledByteBufferFactory
    implements PooledByteBufferFactory
{

    public NativePooledByteBufferFactory(NativeMemoryChunkPool nativememorychunkpool, PooledByteStreams pooledbytestreams)
    {
        mPool = nativememorychunkpool;
        mPooledByteStreams = pooledbytestreams;
    }

    NativePooledByteBuffer newByteBuf(InputStream inputstream, NativePooledByteBufferOutputStream nativepooledbytebufferoutputstream)
        throws IOException
    {
        mPooledByteStreams.copy(inputstream, nativepooledbytebufferoutputstream);
        return nativepooledbytebufferoutputstream.toByteBuffer();
    }

    public NativePooledByteBuffer newByteBuffer(int i)
    {
        CloseableReference closeablereference;
        NativePooledByteBuffer nativepooledbytebuffer;
        boolean flag;
        if(i > 0)
            flag = true;
        else
            flag = false;
        Preconditions.checkArgument(flag);
        closeablereference = CloseableReference.of(mPool.get(i), mPool);
        nativepooledbytebuffer = new NativePooledByteBuffer(closeablereference, i);
        closeablereference.close();
        return nativepooledbytebuffer;
    }

    public NativePooledByteBuffer newByteBuffer(InputStream inputstream)
        throws IOException
    {
        NativePooledByteBufferOutputStream nativepooledbytebufferoutputstream = new NativePooledByteBufferOutputStream(mPool);
        NativePooledByteBuffer tmep = newByteBuf(inputstream, nativepooledbytebufferoutputstream);
        nativepooledbytebufferoutputstream.close();
        return tmep;
    }

    public NativePooledByteBuffer newByteBuffer(InputStream inputstream, int i)
        throws IOException
    {
        NativePooledByteBufferOutputStream nativepooledbytebufferoutputstream = new NativePooledByteBufferOutputStream(mPool, i);
        NativePooledByteBuffer temp = newByteBuf(inputstream, nativepooledbytebufferoutputstream);
        nativepooledbytebufferoutputstream.close();
        return temp;
    }

    public NativePooledByteBuffer newByteBuffer(byte abyte0[])
    {
        NativePooledByteBufferOutputStream nativepooledbytebufferoutputstream = new NativePooledByteBufferOutputStream(mPool, abyte0.length);
        try {
            nativepooledbytebufferoutputstream.write(abyte0, 0, abyte0.length);
        } catch(IOException iex) {
            iex.printStackTrace();
        }

        NativePooledByteBuffer temp = nativepooledbytebufferoutputstream.toByteBuffer();
        nativepooledbytebufferoutputstream.close();
        return temp;

    }


    public NativePooledByteBufferOutputStream newOutputStream()
    {
        return new NativePooledByteBufferOutputStream(mPool);
    }

    public NativePooledByteBufferOutputStream newOutputStream(int i)
    {
        return new NativePooledByteBufferOutputStream(mPool, i);
    }


    private final NativeMemoryChunkPool mPool;
    private final PooledByteStreams mPooledByteStreams;
}
