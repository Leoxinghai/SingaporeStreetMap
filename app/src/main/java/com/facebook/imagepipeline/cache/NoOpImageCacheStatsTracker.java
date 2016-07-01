// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.cache;


// Referenced classes of package com.facebook.imagepipeline.cache:
//            ImageCacheStatsTracker, CountingMemoryCache

public class NoOpImageCacheStatsTracker
    implements ImageCacheStatsTracker
{

    private NoOpImageCacheStatsTracker()
    {
    }

    public static NoOpImageCacheStatsTracker getInstance()
    {
        NoOpImageCacheStatsTracker noopimagecachestatstracker;
        if(sInstance == null)
            sInstance = new NoOpImageCacheStatsTracker();
        noopimagecachestatstracker = sInstance;
        return noopimagecachestatstracker;
    }

    public void onBitmapCacheHit()
    {
    }

    public void onBitmapCacheMiss()
    {
    }

    public void onBitmapCachePut()
    {
    }

    public void onDiskCacheGetFail()
    {
    }

    public void onDiskCacheHit()
    {
    }

    public void onDiskCacheMiss()
    {
    }

    public void onMemoryCacheHit()
    {
    }

    public void onMemoryCacheMiss()
    {
    }

    public void onMemoryCachePut()
    {
    }

    public void onStagingAreaHit()
    {
    }

    public void onStagingAreaMiss()
    {
    }

    public void registerBitmapMemoryCache(CountingMemoryCache countingmemorycache)
    {
    }

    public void registerEncodedMemoryCache(CountingMemoryCache countingmemorycache)
    {
    }

    private static NoOpImageCacheStatsTracker sInstance = null;

}
