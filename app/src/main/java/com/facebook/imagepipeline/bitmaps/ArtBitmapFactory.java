

package com.facebook.imagepipeline.bitmaps;

import android.graphics.Bitmap;
import com.facebook.common.references.CloseableReference;
import com.facebook.imagepipeline.memory.BitmapPool;
import com.facebook.imagepipeline.nativecode.Bitmaps;
import com.facebook.imageutils.BitmapUtil;

// Referenced classes of package com.facebook.imagepipeline.bitmaps:
//            PlatformBitmapFactory

public class ArtBitmapFactory extends PlatformBitmapFactory
{

    public ArtBitmapFactory(BitmapPool bitmappool)
    {
        mBitmapPool = bitmappool;
    }

    public CloseableReference createBitmap(int i, int j, android.graphics.Bitmap.Config config)
    {
        int k = BitmapUtil.getSizeInByteForBitmap(i, j, config);
        Bitmap bitmap = (Bitmap)mBitmapPool.get(k);
        Bitmaps.reconfigureBitmap(bitmap, i, j, config);
        return CloseableReference.of(bitmap, mBitmapPool);
    }

    private final BitmapPool mBitmapPool;
}
