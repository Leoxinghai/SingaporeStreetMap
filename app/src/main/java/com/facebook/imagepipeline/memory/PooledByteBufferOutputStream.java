

package com.facebook.imagepipeline.memory;

import com.facebook.common.internal.Throwables;
import java.io.IOException;
import java.io.OutputStream;

// Referenced classes of package com.facebook.imagepipeline.memory:
//            PooledByteBuffer

public abstract class PooledByteBufferOutputStream extends OutputStream
{

    public PooledByteBufferOutputStream()
    {
    }

    public void close()
    {
        try
        {
            super.close();
            return;
        }
        catch(IOException ioexception)
        {
            //Throwables.propagate(ioexception);
            ioexception.printStackTrace();
        }
    }

    public abstract int size();

    public abstract PooledByteBuffer toByteBuffer();
}
