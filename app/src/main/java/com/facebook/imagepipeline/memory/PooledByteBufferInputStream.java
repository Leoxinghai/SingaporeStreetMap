

package com.facebook.imagepipeline.memory;

import com.facebook.common.internal.Preconditions;
import java.io.InputStream;

// Referenced classes of package com.facebook.imagepipeline.memory:
//            PooledByteBuffer

public class PooledByteBufferInputStream extends InputStream
{

    public PooledByteBufferInputStream(PooledByteBuffer pooledbytebuffer)
    {
        boolean flag;
        if(!pooledbytebuffer.isClosed())
            flag = true;
        else
            flag = false;
        Preconditions.checkArgument(flag);
        mPooledByteBuffer = (PooledByteBuffer)Preconditions.checkNotNull(pooledbytebuffer);
        mOffset = 0;
        mMark = 0;
    }

    public int available()
    {
        return mPooledByteBuffer.size() - mOffset;
    }

    public void mark(int i)
    {
        mMark = mOffset;
    }

    public boolean markSupported()
    {
        return true;
    }

    public int read()
    {
        if(available() <= 0)
        {
            return -1;
        } else
        {
            PooledByteBuffer pooledbytebuffer = mPooledByteBuffer;
            int i = mOffset;
            mOffset = i + 1;
            return pooledbytebuffer.read(i) & 0xff;
        }
    }

    public int read(byte abyte0[])
    {
        return read(abyte0, 0, abyte0.length);
    }

    public int read(byte abyte0[], int i, int j)
    {
        if(i < 0 || j < 0 || i + j > abyte0.length)
            throw new ArrayIndexOutOfBoundsException((new StringBuilder()).append("length=").append(abyte0.length).append("; regionStart=").append(i).append("; regionLength=").append(j).toString());
        int k = available();
        if(k <= 0)
            return -1;
        if(j <= 0)
        {
            return 0;
        } else
        {
            j = Math.min(k, j);
            mPooledByteBuffer.read(mOffset, abyte0, i, j);
            mOffset = mOffset + j;
            return j;
        }
    }

    public void reset()
    {
        mOffset = mMark;
    }

    public long skip(long l)
    {
        int i;
        boolean flag;
        if(l >= 0L)
            flag = true;
        else
            flag = false;
        Preconditions.checkArgument(flag);
        i = Math.min((int)l, available());
        mOffset = mOffset + i;
        return (long)i;
    }

    int mMark;
    int mOffset;
    final PooledByteBuffer mPooledByteBuffer;
}
