

package com.facebook.common.streams;

import java.io.*;

public class LimitedInputStream extends FilterInputStream
{

    public LimitedInputStream(InputStream inputstream, int i)
    {
        super(inputstream);
        if(inputstream == null)
            throw new NullPointerException();
        if(i < 0)
        {
            throw new IllegalArgumentException("limit must be >= 0");
        } else
        {
            mBytesToRead = i;
            mBytesToReadWhenMarked = -1;
            return;
        }
    }

    public int available()
        throws IOException
    {
        return Math.min(in.available(), mBytesToRead);
    }

    public void mark(int i)
    {
        if(in.markSupported())
        {
            in.mark(i);
            mBytesToReadWhenMarked = mBytesToRead;
        }
    }

    public int read()
        throws IOException
    {
        int i;
        if(mBytesToRead == 0)
        {
            i = -1;
        } else
        {
            int j = in.read();
            i = j;
            if(j != -1)
            {
                mBytesToRead = mBytesToRead - 1;
                return j;
            }
        }
        return i;
    }

    public int read(byte abyte0[], int i, int j)
        throws IOException
    {
        if(mBytesToRead == 0)
        {
            i = -1;
        } else
        {
            j = Math.min(j, mBytesToRead);
            j = in.read(abyte0, i, j);
            i = j;
            if(j > 0)
            {
                mBytesToRead = mBytesToRead - j;
                return j;
            }
        }
        return i;
    }

    public void reset()
        throws IOException
    {
        if(!in.markSupported())
            throw new IOException("mark is not supported");
        if(mBytesToReadWhenMarked == -1)
        {
            throw new IOException("mark not set");
        } else
        {
            in.reset();
            mBytesToRead = mBytesToReadWhenMarked;
            return;
        }
    }

    public long skip(long l)
        throws IOException
    {
        l = Math.min(l, mBytesToRead);
        l = in.skip(l);
        mBytesToRead = (int)((long)mBytesToRead - l);
        return l;
    }

    private int mBytesToRead;
    private int mBytesToReadWhenMarked;
}
