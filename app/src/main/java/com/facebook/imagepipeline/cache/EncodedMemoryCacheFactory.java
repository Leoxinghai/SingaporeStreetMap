// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.cache;


// Referenced classes of package com.facebook.imagepipeline.cache:
//            ImageCacheStatsTracker, InstrumentedMemoryCache, CountingMemoryCache, MemoryCache,
//            MemoryCacheTracker

public class EncodedMemoryCacheFactory
{

    public EncodedMemoryCacheFactory()
    {
    }

    public static MemoryCache get(CountingMemoryCache countingmemorycache, final ImageCacheStatsTracker imagecachestatstracker)
    {
        imagecachestatstracker.registerEncodedMemoryCache(countingmemorycache);
        return new InstrumentedMemoryCache(countingmemorycache, new MemoryCacheTracker() {

            public void onCacheHit()
            {
                imagecachestatstracker.onMemoryCacheHit();
            }

            public void onCacheMiss()
            {
                imagecachestatstracker.onMemoryCacheMiss();
            }

            public void onCachePut()
            {
                imagecachestatstracker.onMemoryCachePut();
            }

        });
    }
}
