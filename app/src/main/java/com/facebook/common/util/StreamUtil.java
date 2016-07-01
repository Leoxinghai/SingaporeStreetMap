

package com.facebook.common.util;

import com.facebook.common.internal.ByteStreams;
import com.facebook.common.internal.Preconditions;
import java.io.*;

public class StreamUtil
{

    public StreamUtil()
    {
    }

    public static byte[] getBytesFromStream(InputStream inputstream)
        throws IOException
    {
        return getBytesFromStream(inputstream, inputstream.available());
    }

    public static byte[] getBytesFromStream(InputStream inputstream, int i)
        throws IOException
    {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream(i) {

            public byte[] toByteArray()
            {
                if(count == buf.length)
                    return buf;
                else
                    return super.toByteArray();
            }

        }
;
        ByteStreams.copy(inputstream, bytearrayoutputstream);
        return bytearrayoutputstream.toByteArray();
    }

    public static long skip(InputStream inputstream, long l)
        throws IOException
    {
        long l2;
label0:
        {
            Preconditions.checkNotNull(inputstream);
            long l1;
            boolean flag;
            if(l >= 0L)
                flag = true;
            else
                flag = false;
            Preconditions.checkArgument(flag);
            l1 = l;
            do
            {
                l2 = l;
                if(l1 <= 0L)
                    break label0;
                l2 = inputstream.skip(l1);
                if(l2 > 0L)
                {
                    l1 -= l2;
                    continue;
                }
                if(inputstream.read() == -1)
                    break;
                l1--;
            } while(true);
            l2 = l - l1;
        }
        return l2;
    }
}
