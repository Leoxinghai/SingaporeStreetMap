

package com.facebook.common.internal;

import java.io.*;
import java.nio.channels.FileChannel;

// Referenced classes of package com.facebook.common.internal:
//            ByteStreams

public class Files
{

    private Files()
    {
    }

    static byte[] readFile(InputStream inputstream, long l)
        throws IOException
    {
        if(l > 0x7fffffffL)
            throw new OutOfMemoryError((new StringBuilder()).append("file is too large to fit in a byte array: ").append(l).append(" bytes").toString());
        if(l == 0L)
            return ByteStreams.toByteArray(inputstream);
        else
            return ByteStreams.toByteArray(inputstream, (int)l);
    }

    public static byte[] toByteArray(File file)
        throws IOException
    {
        Object obj = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            byte abyte0[] = readFile(fis, fis.getChannel().size());
            if (file != null)
                fis.close();
            return abyte0;

        } catch(Exception exception) {
            if (fis != null)
                fis.close();
            throw exception;
        }
    }
}
