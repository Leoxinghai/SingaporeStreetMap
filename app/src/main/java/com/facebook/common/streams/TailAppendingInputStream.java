

package com.facebook.common.streams;

import java.io.*;

public class TailAppendingInputStream extends FilterInputStream
{

    public TailAppendingInputStream(InputStream inputstream, byte abyte0[])
    {
        super(inputstream);
        if(inputstream == null)
            throw new NullPointerException();
        if(abyte0 == null)
        {
            throw new NullPointerException();
        } else
        {
            mTail = abyte0;
            return;
        }
    }

    private int readNextTailByte()
    {
        if(mTailOffset >= mTail.length)
        {
            return -1;
        } else
        {
            byte abyte0[] = mTail;
            int i = mTailOffset;
            mTailOffset = i + 1;
            return abyte0[i] & 0xff;
        }
    }

    public void mark(int i)
    {
        if(in.markSupported())
        {
            super.mark(i);
            mMarkedTailOffset = mTailOffset;
        }
    }

    public int read()
        throws IOException
    {
        int i = in.read();
        if(i != -1)
            return i;
        else
            return readNextTailByte();
    }

    public int read(byte abyte0[])
        throws IOException
    {
        return read(abyte0, 0, abyte0.length);
    }

    public int read(byte abyte0[], int i, int j)
        throws IOException
    {
        int k = in.read(abyte0, i, j);
        if(k != -1)
            return k;
        if(j == 0)
            return 0;
        k = 0;
            int l;
        for(;k < j;k++)
        {
            l = readNextTailByte();
            if(l != -1)
                break;
            abyte0[i + k] = (byte)l;
        }
        if(k <= 0)
            k = -1;
        return k;

    }

    public void reset()
        throws IOException
    {
        if(in.markSupported())
        {
            in.reset();
            mTailOffset = mMarkedTailOffset;
            return;
        } else
        {
            throw new IOException("mark is not supported");
        }
    }

    private int mMarkedTailOffset;
    private final byte mTail[];
    private int mTailOffset;
}
