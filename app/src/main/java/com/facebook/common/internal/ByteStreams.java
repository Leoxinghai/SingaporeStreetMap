

package com.facebook.common.internal;

import java.io.*;
import java.util.Arrays;

// Referenced classes of package com.facebook.common.internal:
//            Preconditions

public final class ByteStreams
{
    private static final class FastByteArrayOutputStream extends ByteArrayOutputStream
    {

        void writeTo(byte abyte0[], int i)
        {
            System.arraycopy(buf, 0, abyte0, i, count);
        }

        private FastByteArrayOutputStream()
        {
        }

    }


    private ByteStreams()
    {
    }

    public static long copy(InputStream inputstream, OutputStream outputstream)
        throws IOException
    {
        Preconditions.checkNotNull(inputstream);
        Preconditions.checkNotNull(outputstream);
        byte abyte0[] = new byte[4096];
        long l = 0L;
        do
        {
            int i = inputstream.read(abyte0);
            if(i == -1)
                return l;
            outputstream.write(abyte0, 0, i);
            l += i;
        } while(true);
    }

    public static int read(InputStream inputstream, byte abyte0[], int i, int j)
        throws IOException
    {
        Preconditions.checkNotNull(inputstream);
        Preconditions.checkNotNull(abyte0);
        if(j < 0)
            throw new IndexOutOfBoundsException("len is negative");
        int k = 0;
        int l;
        for(;k < j;)
        {
            l = inputstream.read(abyte0, i + k, j - k);
            if(l != -1)
                k += l;
        }
        return k;
    }

    public static void readFully(InputStream inputstream, byte abyte0[], int i, int j)
        throws IOException
    {
        i = read(inputstream, abyte0, i, j);
        if(i != j)
            throw new EOFException((new StringBuilder()).append("reached end of stream after reading ").append(i).append(" bytes; ").append(j).append(" bytes expected").toString());
        else
            return;
    }

    public static byte[] toByteArray(InputStream inputstream)
        throws IOException
    {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        copy(inputstream, bytearrayoutputstream);
        return bytearrayoutputstream.toByteArray();
    }

    public static byte[] toByteArray(InputStream inputstream, int i)
        throws IOException
    {
        byte abyte1[];
        byte abyte0[];
        int j;
        abyte1 = new byte[i];
        j = i;

        for(;j > 0;) {
            int k;
            int l;
            k = i - j;
            l = inputstream.read(abyte1, k, j);
            if (l == -1) {
                abyte0 = Arrays.copyOf(abyte1, k);
                return abyte0;
            }

            j -= l;
        }

        i = inputstream.read();
        abyte0 = abyte1;
        if(i == -1)
            return abyte0;

        FastByteArrayOutputStream fastbytearrayoutputstream = new FastByteArrayOutputStream();
        fastbytearrayoutputstream.write(i);
        copy(inputstream, fastbytearrayoutputstream);
        byte result[] = new byte[abyte1.length + fastbytearrayoutputstream.size()];
        System.arraycopy(abyte1, 0, inputstream, 0, abyte1.length);
        fastbytearrayoutputstream.writeTo(result, abyte1.length);
        return result;
    }

    private static final int BUF_SIZE = 4096;
}
