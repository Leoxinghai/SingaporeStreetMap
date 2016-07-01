

package com.facebook.imagepipeline.memory;

import com.facebook.common.internal.Preconditions;
import com.facebook.common.logging.FLog;
import com.facebook.common.references.ResourceReleaser;
import java.io.IOException;
import java.io.InputStream;

public class PooledByteArrayBufferedInputStream extends InputStream
{

    public PooledByteArrayBufferedInputStream(InputStream inputstream, byte abyte0[], ResourceReleaser resourcereleaser)
    {
        mInputStream = (InputStream)Preconditions.checkNotNull(inputstream);
        mByteArray = (byte[])Preconditions.checkNotNull(abyte0);
        mResourceReleaser = (ResourceReleaser)Preconditions.checkNotNull(resourcereleaser);
        mBufferedSize = 0;
        mBufferOffset = 0;
        mClosed = false;
    }

    private boolean ensureDataInBuffer()
        throws IOException
    {
        if(mBufferOffset < mBufferedSize)
            return true;
        int i = mInputStream.read(mByteArray);
        if(i <= 0)
        {
            return false;
        } else
        {
            mBufferedSize = i;
            mBufferOffset = 0;
            return true;
        }
    }

    private void ensureNotClosed()
        throws IOException
    {
        if(mClosed)
            throw new IOException("stream already closed");
        else
            return;
    }

    public int available()
        throws IOException
    {
        boolean flag;
        if(mBufferOffset <= mBufferedSize)
            flag = true;
        else
            flag = false;
        Preconditions.checkState(flag);
        ensureNotClosed();
        return (mBufferedSize - mBufferOffset) + mInputStream.available();
    }

    public void close()
        throws IOException
    {
        if(!mClosed)
        {
            mClosed = true;
            mResourceReleaser.release(mByteArray);
            super.close();
        }
    }

    protected void finalize()
        throws Throwable
    {
        if(!mClosed)
        {
            FLog.e("PooledByteInputStream", "Finalized without closing");
            close();
        }
        super.finalize();
    }

    public int read()
        throws IOException
    {
        boolean flag;
        if(mBufferOffset <= mBufferedSize)
            flag = true;
        else
            flag = false;
        Preconditions.checkState(flag);
        ensureNotClosed();
        if(!ensureDataInBuffer())
        {
            return -1;
        } else
        {
            byte abyte0[] = mByteArray;
            int i = mBufferOffset;
            mBufferOffset = i + 1;
            return abyte0[i] & 0xff;
        }
    }

    public int read(byte abyte0[], int i, int j)
        throws IOException
    {
        boolean flag;
        if(mBufferOffset <= mBufferedSize)
            flag = true;
        else
            flag = false;
        Preconditions.checkState(flag);
        ensureNotClosed();
        if(!ensureDataInBuffer())
        {
            return -1;
        } else
        {
            j = Math.min(mBufferedSize - mBufferOffset, j);
            System.arraycopy(mByteArray, mBufferOffset, abyte0, i, j);
            mBufferOffset = mBufferOffset + j;
            return j;
        }
    }

    public long skip(long l)
        throws IOException
    {
        int i;
        boolean flag;
        if(mBufferOffset <= mBufferedSize)
            flag = true;
        else
            flag = false;
        Preconditions.checkState(flag);
        ensureNotClosed();
        i = mBufferedSize - mBufferOffset;
        if((long)i >= l)
        {
            mBufferOffset = (int)((long)mBufferOffset + l);
            return l;
        } else
        {
            mBufferOffset = mBufferedSize;
            return (long)i + mInputStream.skip(l - (long)i);
        }
    }

    private static final String TAG = "PooledByteInputStream";
    private int mBufferOffset;
    private int mBufferedSize;
    private final byte mByteArray[];
    private boolean mClosed;
    private final InputStream mInputStream;
    private final ResourceReleaser mResourceReleaser;
}
