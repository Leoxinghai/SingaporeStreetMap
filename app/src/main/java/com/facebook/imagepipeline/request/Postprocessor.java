

package com.facebook.imagepipeline.request;

import android.graphics.Bitmap;
import com.facebook.cache.common.CacheKey;
import com.facebook.common.references.CloseableReference;
import com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory;

public interface Postprocessor
{

    public abstract String getName();

    public abstract CacheKey getPostprocessorCacheKey();

    public abstract CloseableReference process(Bitmap bitmap, PlatformBitmapFactory platformbitmapfactory);
}
