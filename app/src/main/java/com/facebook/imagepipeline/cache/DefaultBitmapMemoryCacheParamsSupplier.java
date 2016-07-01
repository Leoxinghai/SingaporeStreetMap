// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.cache;

import android.app.ActivityManager;
import com.facebook.common.internal.Supplier;

// Referenced classes of package com.facebook.imagepipeline.cache:
//            MemoryCacheParams

public class DefaultBitmapMemoryCacheParamsSupplier
    implements Supplier
{

    public DefaultBitmapMemoryCacheParamsSupplier(ActivityManager activitymanager)
    {
        mActivityManager = activitymanager;
    }

    private int getMaxCacheSize()
    {
        int i = Math.min(mActivityManager.getMemoryClass() * 0x100000, 0x7fffffff);
        if(i < 0x2000000)
            return 0x400000;
        if(i < 0x4000000)
            return 0x600000;
        if(android.os.Build.VERSION.SDK_INT < 11)
            return 0x800000;
        else
            return i / 4;
    }

    public MemoryCacheParams get()
    {
        return new MemoryCacheParams(getMaxCacheSize(), 256, 0x7fffffff, 0x7fffffff, 0x7fffffff);
    }


    private static final int MAX_CACHE_ENTRIES = 256;
    private static final int MAX_CACHE_ENTRY_SIZE = 0x7fffffff;
    private static final int MAX_EVICTION_QUEUE_ENTRIES = 0x7fffffff;
    private static final int MAX_EVICTION_QUEUE_SIZE = 0x7fffffff;
    private final ActivityManager mActivityManager;
}
