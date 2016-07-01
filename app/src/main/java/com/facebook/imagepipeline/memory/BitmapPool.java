// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.memory;

import android.graphics.Bitmap;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.memory.MemoryTrimmableRegistry;

// Referenced classes of package com.facebook.imagepipeline.memory:
//            BasePool, PoolParams, PoolStatsTracker

public class BitmapPool extends BasePool
{

    public BitmapPool(MemoryTrimmableRegistry memorytrimmableregistry, PoolParams poolparams, PoolStatsTracker poolstatstracker)
    {
        super(memorytrimmableregistry, poolparams, poolstatstracker);
        initialize();
    }

    protected Bitmap alloc(int i)
    {
        return Bitmap.createBitmap(1, (int)Math.ceil((double)i / 2D), android.graphics.Bitmap.Config.RGB_565);
    }


    protected void free(Bitmap bitmap)
    {
        Preconditions.checkNotNull(bitmap);
        bitmap.recycle();
    }

    protected void free(Object obj)
    {
        free((Bitmap)obj);
    }

    protected int getBucketedSize(int i)
    {
        return i;
    }

    protected int getBucketedSizeForValue(Bitmap bitmap)
    {
        Preconditions.checkNotNull(bitmap);
        return bitmap.getAllocationByteCount();
    }

    protected int getBucketedSizeForValue(Object obj)
    {
        return getBucketedSizeForValue((Bitmap)obj);
    }

    protected int getSizeInBytes(int i)
    {
        return i;
    }

    protected boolean isReusable(Bitmap bitmap)
    {
        Preconditions.checkNotNull(bitmap);
        return !bitmap.isRecycled() && bitmap.isMutable();
    }

    protected boolean isReusable(Object obj)
    {
        return isReusable((Bitmap)obj);
    }
}
