

package com.facebook.imagepipeline.memory;

import java.io.Closeable;

public interface PooledByteBuffer
    extends Closeable
{
    public static class ClosedException extends RuntimeException
    {

        public ClosedException()
        {
            super("Invalid bytebuf. Already closed");
        }
    }


    public abstract void close();

    public abstract long getNativePtr();

    public abstract boolean isClosed();

    public abstract byte read(int i);

    public abstract void read(int i, byte abyte0[], int j, int k);

    public abstract int size();
}
