

package com.facebook.imagepipeline.cache;

import com.facebook.cache.common.CacheKey;
import com.facebook.common.internal.Objects;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.util.HashCodeUtil;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.common.ResizeOptions;
import java.util.Locale;

public class BitmapMemoryCacheKey
    implements CacheKey
{

    public BitmapMemoryCacheKey(String s, ResizeOptions resizeoptions, boolean flag, ImageDecodeOptions imagedecodeoptions, CacheKey cachekey, String s1)
    {
        mSourceString = (String)Preconditions.checkNotNull(s);
        mResizeOptions = resizeoptions;
        mAutoRotated = flag;
        mImageDecodeOptions = imagedecodeoptions;
        mPostprocessorCacheKey = cachekey;
        mPostprocessorName = s1;
        int k = s.hashCode();
        int i;
        int j;
        if(resizeoptions != null)
            i = resizeoptions.hashCode();
        else
            i = 0;
        if(flag)
            j = Boolean.TRUE.hashCode();
        else
            j = Boolean.FALSE.hashCode();
        mHash = HashCodeUtil.hashCode(Integer.valueOf(k), Integer.valueOf(i), Integer.valueOf(j), mImageDecodeOptions, mPostprocessorCacheKey, s1);
    }

    public boolean equals(Object obj)
    {
        if(obj instanceof BitmapMemoryCacheKey)
            if(mHash == ((BitmapMemoryCacheKey) (obj = (BitmapMemoryCacheKey)obj)).mHash && mSourceString.equals(((BitmapMemoryCacheKey) (obj)).mSourceString) && Objects.equal(mResizeOptions, ((BitmapMemoryCacheKey) (obj)).mResizeOptions) && mAutoRotated == ((BitmapMemoryCacheKey) (obj)).mAutoRotated && Objects.equal(mImageDecodeOptions, ((BitmapMemoryCacheKey) (obj)).mImageDecodeOptions) && Objects.equal(mPostprocessorCacheKey, ((BitmapMemoryCacheKey) (obj)).mPostprocessorCacheKey) && Objects.equal(mPostprocessorName, ((BitmapMemoryCacheKey) (obj)).mPostprocessorName))
                return true;
        return false;
    }

    public String getPostprocessorName()
    {
        return mPostprocessorName;
    }

    public String getSourceUriString()
    {
        return mSourceString;
    }

    public int hashCode()
    {
        return mHash;
    }

    public String toString()
    {
        return String.format((Locale)null, "%s_%s_%s_%s_%s_%s_%d", new Object[] {
            mSourceString, mResizeOptions, Boolean.toString(mAutoRotated), mImageDecodeOptions, mPostprocessorCacheKey, mPostprocessorName, Integer.valueOf(mHash)
        });
    }

    private final boolean mAutoRotated;
    private final int mHash;
    private final ImageDecodeOptions mImageDecodeOptions;
    private final CacheKey mPostprocessorCacheKey;
    private final String mPostprocessorName;
    private final ResizeOptions mResizeOptions;
    private final String mSourceString;
}
