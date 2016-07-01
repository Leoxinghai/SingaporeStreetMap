// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.request;

import android.net.Uri;
import com.facebook.imagepipeline.common.*;
import java.io.File;

// Referenced classes of package com.facebook.imagepipeline.request:
//            ImageRequestBuilder, Postprocessor

public class ImageRequest
{
    public static enum ImageType
    {

		SMALL("SMALL", 0),
		DEFAULT("DEFAULT", 1);
        String sType;
        int iType;
		private ImageType(String s, int i)
		{
			sType = s;
            iType = i;
		}
    }

    public static enum RequestLevel
    {
		FULL_FETCH("FULL_FETCH", 0, 1),
		DISK_CACHE("DISK_CACHE", 1, 2),
		ENCODED_MEMORY_CACHE("ENCODED_MEMORY_CACHE", 2, 3),
		BITMAP_MEMORY_CACHE("BITMAP_MEMORY_CACHE", 3, 4);

        public static RequestLevel getMax(RequestLevel requestlevel, RequestLevel requestlevel1)
        {
            if(requestlevel.getValue() > requestlevel1.getValue())
                return requestlevel;
            else
                return requestlevel1;
        }

        public int getValue()
        {
            return mValue;
        }

        private int mValue;
		String sType;
		int iType;


        private RequestLevel(String s, int i, int j)
        {
		sType = s;
		iType = i;
            mValue = j;
        }
    }


    protected ImageRequest(ImageRequestBuilder imagerequestbuilder)
    {
        mResizeOptions = null;
        mImageType = imagerequestbuilder.getImageType();
        mSourceUri = imagerequestbuilder.getSourceUri();
        mProgressiveRenderingEnabled = imagerequestbuilder.isProgressiveRenderingEnabled();
        mLocalThumbnailPreviewsEnabled = imagerequestbuilder.isLocalThumbnailPreviewsEnabled();
        mImageDecodeOptions = imagerequestbuilder.getImageDecodeOptions();
        mResizeOptions = imagerequestbuilder.getResizeOptions();
        mAutoRotateEnabled = imagerequestbuilder.isAutoRotateEnabled();
        mRequestPriority = imagerequestbuilder.getRequestPriority();
        mLowestPermittedRequestLevel = imagerequestbuilder.getLowestPermittedRequestLevel();
        mIsDiskCacheEnabled = imagerequestbuilder.isDiskCacheEnabled();
        mPostprocessor = imagerequestbuilder.getPostprocessor();
    }

    public static ImageRequest fromUri(Uri uri)
    {
        if(uri == null)
            return null;
        else
            return ImageRequestBuilder.newBuilderWithSource(uri).build();
    }

    public static ImageRequest fromUri(String s)
    {
        if(s == null || s.length() == 0)
            return null;
        else
            return fromUri(Uri.parse(s));
    }

    public boolean getAutoRotateEnabled()
    {
        return mAutoRotateEnabled;
    }

    public ImageDecodeOptions getImageDecodeOptions()
    {
        return mImageDecodeOptions;
    }

    public ImageType getImageType()
    {
        return mImageType;
    }

    public boolean getLocalThumbnailPreviewsEnabled()
    {
        return mLocalThumbnailPreviewsEnabled;
    }

    public RequestLevel getLowestPermittedRequestLevel()
    {
        return mLowestPermittedRequestLevel;
    }

    public Postprocessor getPostprocessor()
    {
        return mPostprocessor;
    }

    public int getPreferredHeight()
    {
        if(mResizeOptions != null)
            return mResizeOptions.height;
        else
            return 2048;
    }

    public int getPreferredWidth()
    {
        if(mResizeOptions != null)
            return mResizeOptions.width;
        else
            return 2048;
    }

    public Priority getPriority()
    {
        return mRequestPriority;
    }

    public boolean getProgressiveRenderingEnabled()
    {
        return mProgressiveRenderingEnabled;
    }

    public ResizeOptions getResizeOptions()
    {
        return mResizeOptions;
    }

    public File getSourceFile()
    {
        File file;
        if(mSourceFile == null)
            mSourceFile = new File(mSourceUri.getPath());
        file = mSourceFile;
        return file;
    }

    public Uri getSourceUri()
    {
        return mSourceUri;
    }

    public boolean isDiskCacheEnabled()
    {
        return mIsDiskCacheEnabled;
    }

    private final boolean mAutoRotateEnabled;
    private final ImageDecodeOptions mImageDecodeOptions;
    private final ImageType mImageType;
    private final boolean mIsDiskCacheEnabled;
    private final boolean mLocalThumbnailPreviewsEnabled;
    private final RequestLevel mLowestPermittedRequestLevel;
    private final Postprocessor mPostprocessor;
    private final boolean mProgressiveRenderingEnabled;
    private final Priority mRequestPriority;
    ResizeOptions mResizeOptions;
    private File mSourceFile;
    private final Uri mSourceUri;
}
