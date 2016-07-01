

package com.facebook.imagepipeline.bitmaps;

import com.facebook.common.references.CloseableReference;

public abstract class PlatformBitmapFactory
{

    public PlatformBitmapFactory()
    {
    }

    public CloseableReference createBitmap(int i, int j)
    {
        return createBitmap(i, j, android.graphics.Bitmap.Config.ARGB_8888);
    }

    public abstract CloseableReference createBitmap(int i, int j, android.graphics.Bitmap.Config config);
}
