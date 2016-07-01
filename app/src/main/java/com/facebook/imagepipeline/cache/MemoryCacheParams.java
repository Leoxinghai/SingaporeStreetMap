

package com.facebook.imagepipeline.cache;


public class MemoryCacheParams
{

    public MemoryCacheParams(int i, int j, int k, int l, int i1)
    {
        maxCacheSize = i;
        maxCacheEntries = j;
        maxEvictionQueueSize = k;
        maxEvictionQueueEntries = l;
        maxCacheEntrySize = i1;
    }

    public final int maxCacheEntries;
    public final int maxCacheEntrySize;
    public final int maxCacheSize;
    public final int maxEvictionQueueEntries;
    public final int maxEvictionQueueSize;
}
