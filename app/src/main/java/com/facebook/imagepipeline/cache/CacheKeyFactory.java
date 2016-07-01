

package com.facebook.imagepipeline.cache;

import android.net.Uri;
import com.facebook.cache.common.CacheKey;
import com.facebook.imagepipeline.request.ImageRequest;

public interface CacheKeyFactory
{

    public abstract CacheKey getBitmapCacheKey(ImageRequest imagerequest);

    public abstract Uri getCacheKeySourceUri(Uri uri);

    public abstract CacheKey getEncodedCacheKey(ImageRequest imagerequest);

    public abstract CacheKey getPostprocessedBitmapCacheKey(ImageRequest imagerequest);
}
