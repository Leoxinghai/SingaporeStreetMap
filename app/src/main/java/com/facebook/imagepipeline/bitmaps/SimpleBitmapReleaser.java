// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.bitmaps;

import android.graphics.Bitmap;
import com.facebook.common.references.ResourceReleaser;

public class SimpleBitmapReleaser
    implements ResourceReleaser
{

    private SimpleBitmapReleaser()
    {
    }

    public static SimpleBitmapReleaser getInstance()
    {
        if(sInstance == null)
            sInstance = new SimpleBitmapReleaser();
        return sInstance;
    }

    public void release(Bitmap bitmap)
    {
        bitmap.recycle();
    }

    public void release(Object obj)
    {
        release((Bitmap)obj);
    }

    private static SimpleBitmapReleaser sInstance;
}
