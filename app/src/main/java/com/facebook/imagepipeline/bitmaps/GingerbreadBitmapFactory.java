// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.bitmaps;

import android.graphics.Bitmap;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.references.ResourceReleaser;

// Referenced classes of package com.facebook.imagepipeline.bitmaps:
//            PlatformBitmapFactory, SimpleBitmapReleaser

public class GingerbreadBitmapFactory extends PlatformBitmapFactory
{

    public GingerbreadBitmapFactory()
    {
    }

    public CloseableReference createBitmap(int i, int j, android.graphics.Bitmap.Config config)
    {
        return CloseableReference.of(Bitmap.createBitmap(i, j, config), SimpleBitmapReleaser.getInstance());
    }

    // Unreferenced inner class imagepipeline/bitmaps/GingerbreadBitmapFactory$1

/* anonymous class */
    class _cls1
        implements ResourceReleaser
    {

        public void release(Bitmap bitmap)
        {
            bitmap.recycle();
        }

        public void release(Object obj)
        {
            release((Bitmap)obj);
        }

    }

}
