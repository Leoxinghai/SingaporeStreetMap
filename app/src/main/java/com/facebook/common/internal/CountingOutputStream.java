

package com.facebook.common.internal;

import java.io.*;

public class CountingOutputStream extends FilterOutputStream
{

    public CountingOutputStream(OutputStream outputstream)
    {
        super(outputstream);
        mCount = 0L;
    }

    public void close()
        throws IOException
    {
        out.close();
    }

    public long getCount()
    {
        return mCount;
    }

    public void write(int i)
        throws IOException
    {
        out.write(i);
        mCount = mCount + 1L;
    }

    public void write(byte abyte0[], int i, int j)
        throws IOException
    {
        out.write(abyte0, i, j);
        mCount = mCount + (long)j;
    }

    private long mCount;
}
