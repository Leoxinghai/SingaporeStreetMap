

package com.facebook.imagepipeline.memory;


// Referenced classes of package com.facebook.imagepipeline.memory:
//            BasePool

public interface PoolStatsTracker
{

    public abstract void onAlloc(int i);

    public abstract void onFree(int i);

    public abstract void onHardCapReached();

    public abstract void onSoftCapReached();

    public abstract void onValueRelease(int i);

    public abstract void onValueReuse(int i);

    public abstract void setBasePool(BasePool basepool);

    public static final String BUCKETS_USED_PREFIX = "buckets_used_";
    public static final String FREE_BYTES = "free_bytes";
    public static final String FREE_COUNT = "free_count";
    public static final String HARD_CAP = "hard_cap";
    public static final String SOFT_CAP = "soft_cap";
    public static final String USED_BYTES = "used_bytes";
    public static final String USED_COUNT = "used_count";
}
