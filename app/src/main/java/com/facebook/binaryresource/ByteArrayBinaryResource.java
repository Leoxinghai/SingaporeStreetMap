

package com.facebook.binaryresource;

import com.facebook.common.internal.Preconditions;
import java.io.*;

// Referenced classes of package com.facebook.binaryresource:
//            BinaryResource

public class ByteArrayBinaryResource
    implements BinaryResource
{

    public ByteArrayBinaryResource(byte abyte0[])
    {
        mBytes = (byte[])Preconditions.checkNotNull(abyte0);
    }

    public InputStream openStream()
        throws IOException
    {
        return new ByteArrayInputStream(mBytes);
    }

    public byte[] read()
    {
        return mBytes;
    }

    public long size()
    {
        return (long)mBytes.length;
    }

    private final byte mBytes[];
}
