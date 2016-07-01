

package com.facebook.imagepipeline.memory;

import java.io.IOException;
import java.io.InputStream;

// Referenced classes of package com.facebook.imagepipeline.memory:
//            PooledByteBuffer, PooledByteBufferOutputStream

public interface PooledByteBufferFactory
{

    public abstract PooledByteBuffer newByteBuffer(int i);

    public abstract PooledByteBuffer newByteBuffer(InputStream inputstream)
        throws IOException;

    public abstract PooledByteBuffer newByteBuffer(InputStream inputstream, int i)
        throws IOException;

    public abstract PooledByteBuffer newByteBuffer(byte abyte0[]);

    public abstract PooledByteBufferOutputStream newOutputStream();

    public abstract PooledByteBufferOutputStream newOutputStream(int i);
}
