// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.cache.common;

import com.facebook.common.internal.ByteStreams;
import java.io.*;

// Referenced classes of package com.facebook.cache.common:
//            WriterCallback

public class WriterCallbacks
{

    public WriterCallbacks()
    {
    }

    public static WriterCallback from(InputStream inputstream)
    {
        final InputStream is = inputstream;
        return new WriterCallback() {

            public void write(OutputStream outputstream)
                throws IOException
            {
                ByteStreams.copy(is, outputstream);
            }
        };
    }

    public static WriterCallback from(byte abyte0[])
    {
        final byte data[] = abyte0;
        return new WriterCallback() {

            public void write(OutputStream outputstream)
                throws IOException
            {
                outputstream.write(data);
            }
        };
    }
}
