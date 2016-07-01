// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.cache;

import android.net.Uri;
import com.facebook.cache.common.CacheKey;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.Postprocessor;

// Referenced classes of package com.facebook.imagepipeline.cache:
//            CacheKeyFactory, BitmapMemoryCacheKey

public class DefaultCacheKeyFactory
    implements CacheKeyFactory
{

    protected DefaultCacheKeyFactory()
    {
    }

    public static DefaultCacheKeyFactory getInstance()
    {
        DefaultCacheKeyFactory defaultcachekeyfactory;
        if(sInstance == null)
            sInstance = new DefaultCacheKeyFactory();
        defaultcachekeyfactory = sInstance;
        return defaultcachekeyfactory;
    }

    public CacheKey getBitmapCacheKey(ImageRequest imagerequest)
    {
        return new BitmapMemoryCacheKey(getCacheKeySourceUri(imagerequest.getSourceUri()).toString(), imagerequest.getResizeOptions(), imagerequest.getAutoRotateEnabled(), imagerequest.getImageDecodeOptions(), null, null);
    }

    public Uri getCacheKeySourceUri(Uri uri)
    {
        return uri;
    }

    public CacheKey getEncodedCacheKey(ImageRequest imagerequest)
    {
        return new SimpleCacheKey(getCacheKeySourceUri(imagerequest.getSourceUri()).toString());
    }

    public CacheKey getPostprocessedBitmapCacheKey(ImageRequest imagerequest)
    {
        Object obj = imagerequest.getPostprocessor();
        CacheKey cachekey;
        if(obj != null)
        {
            cachekey = ((Postprocessor) (obj)).getPostprocessorCacheKey();
            obj = obj.getClass().getName();
        } else
        {
            cachekey = null;
            obj = null;
        }
        return new BitmapMemoryCacheKey(getCacheKeySourceUri(imagerequest.getSourceUri()).toString(), imagerequest.getResizeOptions(), imagerequest.getAutoRotateEnabled(), imagerequest.getImageDecodeOptions(), cachekey, ((String) (obj)));
    }

    private static DefaultCacheKeyFactory sInstance = null;

}
