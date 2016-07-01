

package com.facebook.imagepipeline.memory;

import android.util.SparseIntArray;

// Referenced classes of package com.facebook.imagepipeline.memory:
//            PoolParams

public class DefaultNativeMemoryChunkPoolParams
{

    public DefaultNativeMemoryChunkPoolParams()
    {
    }

    public static PoolParams get()
    {
        SparseIntArray sparseintarray = new SparseIntArray();
        sparseintarray.put(1024, 5);
        sparseintarray.put(2048, 5);
        sparseintarray.put(4096, 5);
        sparseintarray.put(8192, 5);
        sparseintarray.put(16384, 5);
        sparseintarray.put(32768, 5);
        sparseintarray.put(0x10000, 5);
        sparseintarray.put(0x20000, 5);
        sparseintarray.put(0x40000, 2);
        sparseintarray.put(0x80000, 2);
        sparseintarray.put(0x100000, 2);
        return new PoolParams(getMaxSizeSoftCap(), getMaxSizeHardCap(), sparseintarray);
    }

    private static int getMaxSizeHardCap()
    {
        int i = (int)Math.min(Runtime.getRuntime().maxMemory(), 0x7fffffffL);
        if(i < 0x1000000)
            return i / 2;
        else
            return (i / 4) * 3;
    }

    private static int getMaxSizeSoftCap()
    {
        int i = (int)Math.min(Runtime.getRuntime().maxMemory(), 0x7fffffffL);
        if(i < 0x1000000)
            return 0x300000;
        return i >= 0x2000000 ? 0xc00000 : 0x600000;
    }

    private static final int LARGE_BUCKET_LENGTH = 2;
    private static final int SMALL_BUCKET_LENGTH = 5;
}
