

package com.facebook.imagepipeline.memory;

import com.facebook.common.internal.Preconditions;
import java.io.*;

// Referenced classes of package com.facebook.imagepipeline.memory:
//            ByteArrayPool

public class PooledByteStreams
{

    public PooledByteStreams(ByteArrayPool bytearraypool)
    {
        this(bytearraypool, 16384);
    }

    PooledByteStreams(ByteArrayPool bytearraypool, int i)
    {
        boolean flag;
        if(i > 0)
            flag = true;
        else
            flag = false;
        Preconditions.checkArgument(flag);
        mTempBufSize = i;
        mByteArrayPool = bytearraypool;
    }

    public long copy(InputStream inputstream, OutputStream outputstream)
        throws IOException
    {
        byte abyte0[];
        long l;
        l = 0L;
        abyte0 = (byte[])mByteArrayPool.get(mTempBufSize);

		int i =0;
		for(;;) {
        	i = inputstream.read(abyte0, 0, mTempBufSize);
			if(i == -1)
			{
				mByteArrayPool.release(abyte0);
				return l;
			}
			outputstream.write(abyte0, 0, i);
			l += i;
	  	}
        //mByteArrayPool.release(abyte0);
    }

    public long copy(InputStream inputstream, OutputStream outputstream, long l)
        throws IOException
    {
        byte abyte0[];
        long l1;
        boolean flag = false;
        if(l > 0L)
            flag = true;
        Preconditions.checkState(flag);
        l1 = 0L;
        abyte0 = (byte[])mByteArrayPool.get(mTempBufSize);
		for(;l1 < l;) {
			int i = inputstream.read(abyte0, 0, (int)Math.min(mTempBufSize, l - l1));
			if(i == -1)
			{
				mByteArrayPool.release(abyte0);
				return l1;
			}
			outputstream.write(abyte0, 0, i);
			l1 += i;
		}
        mByteArrayPool.release(abyte0);
        return l1;
    }

    private static final int DEFAULT_TEMP_BUF_SIZE = 16384;
    private final ByteArrayPool mByteArrayPool;
    private final int mTempBufSize;
}
