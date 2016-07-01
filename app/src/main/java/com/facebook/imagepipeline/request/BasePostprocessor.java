

package com.facebook.imagepipeline.request;

import android.graphics.Bitmap;
import com.facebook.cache.common.CacheKey;
import com.facebook.common.references.CloseableReference;
import com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory;
import com.facebook.imagepipeline.nativecode.Bitmaps;

// Referenced classes of package com.facebook.imagepipeline.request:
//            Postprocessor

public abstract class BasePostprocessor
    implements Postprocessor
{

    public BasePostprocessor()
    {
    }

    public String getName()
    {
        return "Unknown postprocessor";
    }

    public CacheKey getPostprocessorCacheKey()
    {
        return null;
    }

    public CloseableReference process(Bitmap bitmap, PlatformBitmapFactory platformbitmapfactory)
    {
        CloseableReference closeableReference = platformbitmapfactory.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        process((Bitmap)closeableReference.get(), bitmap);
        CloseableReference closeableReference1 = CloseableReference.cloneOrNull(closeableReference);
        CloseableReference.closeSafely(closeableReference1);
        return closeableReference1;
    }

    public void process(Bitmap bitmap)
    {
    }

    public void process(Bitmap bitmap, Bitmap bitmap1)
    {
        Bitmaps.copyBitmap(bitmap, bitmap1);
        process(bitmap);
    }
}
