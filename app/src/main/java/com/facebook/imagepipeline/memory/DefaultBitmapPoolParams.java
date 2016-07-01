

package com.facebook.imagepipeline.memory;

import android.util.SparseIntArray;

// Referenced classes of package com.facebook.imagepipeline.memory:
//            PoolParams

public class DefaultBitmapPoolParams
{

    private DefaultBitmapPoolParams()
    {
    }

    public static PoolParams get()
    {
        return new PoolParams(0, getMaxSizeHardCap(), DEFAULT_BUCKETS);
    }

    private static int getMaxSizeHardCap()
    {
        int i = (int)Math.min(Runtime.getRuntime().maxMemory(), 0x7fffffffL);
        if(i > 0x1000000)
            return (i / 4) * 3;
        else
            return i / 2;
    }

    private static final SparseIntArray DEFAULT_BUCKETS = new SparseIntArray(0);
    private static final int MAX_SIZE_SOFT_CAP = 0;

}
