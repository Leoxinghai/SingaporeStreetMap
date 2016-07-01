

package com.facebook.imagepipeline.memory;

import android.util.SparseIntArray;

// Referenced classes of package com.facebook.imagepipeline.memory:
//            PoolParams

public class DefaultFlexByteArrayPoolParams
{

    private DefaultFlexByteArrayPoolParams()
    {
    }

    public static SparseIntArray generateBuckets(int i, int j, int k)
    {
        SparseIntArray sparseintarray = new SparseIntArray();
        for(; i <= j; i *= 2)
            sparseintarray.put(i, k);

        return sparseintarray;
    }

    public static PoolParams get()
    {
        return new PoolParams(0x400000, DEFAULT_MAX_NUM_THREADS * 0x400000, generateBuckets(0x20000, 0x400000, DEFAULT_MAX_NUM_THREADS), 0x20000, 0x400000, DEFAULT_MAX_NUM_THREADS);
    }

    public static final int DEFAULT_MAX_BYTE_ARRAY_SIZE = 0x400000;
    public static final int DEFAULT_MAX_NUM_THREADS = Runtime.getRuntime().availableProcessors();
    private static final int DEFAULT_MIN_BYTE_ARRAY_SIZE = 0x20000;

}
