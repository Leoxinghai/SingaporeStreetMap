// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.cache;


// Referenced classes of package com.facebook.imagepipeline.cache:
//            ImageCacheStatsTracker, InstrumentedMemoryCache, CountingMemoryCache, MemoryCache,
//            MemoryCacheTracker

public class BitmapMemoryCacheFactory
{

    public BitmapMemoryCacheFactory()
    {
    }

    public static MemoryCache get(CountingMemoryCache countingmemorycache, final ImageCacheStatsTracker imagecachestatstracker)
    {
        imagecachestatstracker.registerBitmapMemoryCache(countingmemorycache);
        return new InstrumentedMemoryCache(countingmemorycache, new MemoryCacheTracker() {

            public void onCacheHit()
            {
                imagecachestatstracker.onBitmapCacheHit();
            }

            public void onCacheMiss()
            {
                imagecachestatstracker.onBitmapCacheMiss();
            }

            public void onCachePut()
            {
                imagecachestatstracker.onBitmapCachePut();
            }

        });
    }
}
