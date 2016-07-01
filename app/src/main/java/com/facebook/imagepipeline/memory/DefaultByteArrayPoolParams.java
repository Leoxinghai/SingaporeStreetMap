

package com.facebook.imagepipeline.memory;

import android.util.SparseIntArray;

// Referenced classes of package com.facebook.imagepipeline.memory:
//            PoolParams

public class DefaultByteArrayPoolParams
{

    public DefaultByteArrayPoolParams()
    {
    }

    public static PoolParams get()
    {
        SparseIntArray sparseintarray = new SparseIntArray();
        sparseintarray.put(16384, 5);
        return new PoolParams(0x14000, 0x100000, sparseintarray);
    }

    private static final int DEFAULT_BUCKET_SIZE = 5;
    private static final int DEFAULT_IO_BUFFER_SIZE = 16384;
    private static final int MAX_SIZE_HARD_CAP = 0x100000;
    private static final int MAX_SIZE_SOFT_CAP = 0x14000;
}
