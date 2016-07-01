

package com.facebook.imagepipeline.cache;


public interface MemoryCacheTracker
{

    public abstract void onCacheHit();

    public abstract void onCacheMiss();

    public abstract void onCachePut();
}
