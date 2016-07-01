// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.memory;


// Referenced classes of package com.facebook.imagepipeline.memory:
//            PoolStatsTracker, BasePool

public class NoOpPoolStatsTracker
    implements PoolStatsTracker
{

    private NoOpPoolStatsTracker()
    {
    }

    public static NoOpPoolStatsTracker getInstance()
    {
        NoOpPoolStatsTracker nooppoolstatstracker;
        if(sInstance == null)
            sInstance = new NoOpPoolStatsTracker();
        nooppoolstatstracker = sInstance;
        return nooppoolstatstracker;
    }

    public void onAlloc(int i)
    {
    }

    public void onFree(int i)
    {
    }

    public void onHardCapReached()
    {
    }

    public void onSoftCapReached()
    {
    }

    public void onValueRelease(int i)
    {
    }

    public void onValueReuse(int i)
    {
    }

    public void setBasePool(BasePool basepool)
    {
    }

    private static NoOpPoolStatsTracker sInstance = null;

}
