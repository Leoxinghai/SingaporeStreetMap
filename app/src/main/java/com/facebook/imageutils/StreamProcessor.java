

package com.facebook.imageutils;

import java.io.IOException;
import java.io.InputStream;

class StreamProcessor
{

    StreamProcessor()
    {
    }

    public static int readPackedInt(InputStream inputstream, int i, boolean flag)
        throws IOException
    {
        int j = 0;
        int k = 0;
        while(k < i)
        {
            int l = inputstream.read();
            if(l == -1)
                throw new IOException("no more bytes");
            if(flag)
                j |= (l & 0xff) << k * 8;
            else
                j = j << 8 | l & 0xff;
            k++;
        }
        return j;
    }
}
