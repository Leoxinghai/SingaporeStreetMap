

package com.facebook.imagepipeline.request;

import android.net.Uri;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.util.UriUtil;
import com.facebook.imagepipeline.common.*;

// Referenced classes of package com.facebook.imagepipeline.request:
//            ImageRequest, Postprocessor

public class ImageRequestBuilder
{
    public static class BuilderException extends RuntimeException
    {

        public BuilderException(String s)
        {
            super((new StringBuilder()).append("Invalid request builder: ").append(s).toString());
        }
    }


    private ImageRequestBuilder()
    {
        mSourceUri = null;
        mLowestPermittedRequestLevel = ImageRequest.RequestLevel.FULL_FETCH;
        mAutoRotateEnabled = false;
        mResizeOptions = null;
        mImageDecodeOptions = ImageDecodeOptions.defaults();
        mImageType = ImageRequest.ImageType.DEFAULT;
        mProgressiveRenderingEnabled = false;
        mLocalThumbnailPreviewsEnabled = false;
        mRequestPriority = Priority.HIGH;
        mPostprocessor = null;
    }

    public static ImageRequestBuilder fromRequest(ImageRequest imagerequest)
    {
        return newBuilderWithSource(imagerequest.getSourceUri()).setAutoRotateEnabled(imagerequest.getAutoRotateEnabled()).setImageDecodeOptions(imagerequest.getImageDecodeOptions()).setImageType(imagerequest.getImageType()).setLocalThumbnailPreviewsEnabled(imagerequest.getLocalThumbnailPreviewsEnabled()).setLowestPermittedRequestLevel(imagerequest.getLowestPermittedRequestLevel()).setPostprocessor(imagerequest.getPostprocessor()).setProgressiveRenderingEnabled(imagerequest.getProgressiveRenderingEnabled()).setRequestPriority(imagerequest.getPriority()).setResizeOptions(imagerequest.getResizeOptions());
    }

    public static ImageRequestBuilder newBuilderWithResourceId(int i)
    {
        return newBuilderWithSource((new android.net.Uri.Builder()).scheme("res").path(String.valueOf(i)).build());
    }

    public static ImageRequestBuilder newBuilderWithSource(Uri uri)
    {
        return (new ImageRequestBuilder()).setSource(uri);
    }

    public ImageRequest build()
    {
        validate();
        return new ImageRequest(this);
    }

    public ImageDecodeOptions getImageDecodeOptions()
    {
        return mImageDecodeOptions;
    }

    public ImageRequest.ImageType getImageType()
    {
        return mImageType;
    }

    public ImageRequest.RequestLevel getLowestPermittedRequestLevel()
    {
        return mLowestPermittedRequestLevel;
    }

    public Postprocessor getPostprocessor()
    {
        return mPostprocessor;
    }

    public Priority getRequestPriority()
    {
        return mRequestPriority;
    }

    public ResizeOptions getResizeOptions()
    {
        return mResizeOptions;
    }

    public Uri getSourceUri()
    {
        return mSourceUri;
    }

    public boolean isAutoRotateEnabled()
    {
        return mAutoRotateEnabled;
    }

    public boolean isDiskCacheEnabled()
    {
        return UriUtil.isNetworkUri(mSourceUri);
    }

    public boolean isLocalThumbnailPreviewsEnabled()
    {
        return mLocalThumbnailPreviewsEnabled;
    }

    public boolean isProgressiveRenderingEnabled()
    {
        return mProgressiveRenderingEnabled;
    }

    public ImageRequestBuilder setAutoRotateEnabled(boolean flag)
    {
        mAutoRotateEnabled = flag;
        return this;
    }

    public ImageRequestBuilder setImageDecodeOptions(ImageDecodeOptions imagedecodeoptions)
    {
        mImageDecodeOptions = imagedecodeoptions;
        return this;
    }

    public ImageRequestBuilder setImageType(ImageRequest.ImageType imagetype)
    {
        mImageType = imagetype;
        return this;
    }

    public ImageRequestBuilder setLocalThumbnailPreviewsEnabled(boolean flag)
    {
        mLocalThumbnailPreviewsEnabled = flag;
        return this;
    }

    public ImageRequestBuilder setLowestPermittedRequestLevel(ImageRequest.RequestLevel requestlevel)
    {
        mLowestPermittedRequestLevel = requestlevel;
        return this;
    }

    public ImageRequestBuilder setPostprocessor(Postprocessor postprocessor)
    {
        mPostprocessor = postprocessor;
        return this;
    }

    public ImageRequestBuilder setProgressiveRenderingEnabled(boolean flag)
    {
        mProgressiveRenderingEnabled = flag;
        return this;
    }

    public ImageRequestBuilder setRequestPriority(Priority priority)
    {
        mRequestPriority = priority;
        return this;
    }

    public ImageRequestBuilder setResizeOptions(ResizeOptions resizeoptions)
    {
        mResizeOptions = resizeoptions;
        return this;
    }

    public ImageRequestBuilder setSource(Uri uri)
    {
        Preconditions.checkNotNull(uri);
        mSourceUri = uri;
        return this;
    }

    protected void validate()
    {
        if(mSourceUri == null)
            throw new BuilderException("Source must be set!");
        if(UriUtil.isLocalResourceUri(mSourceUri))
        {
            if(!mSourceUri.isAbsolute())
                throw new BuilderException("Resource URI path must be absolute.");
            if(mSourceUri.getPath().isEmpty())
                throw new BuilderException("Resource URI must not be empty");
            try
            {
                Integer.parseInt(mSourceUri.getPath().substring(1));
            }
            catch(NumberFormatException numberformatexception)
            {
                throw new BuilderException("Resource URI path must be a resource id.");
            }
        }
        if(UriUtil.isLocalAssetUri(mSourceUri) && !mSourceUri.isAbsolute())
            throw new BuilderException("Asset URI path must be absolute.");
        else
            return;
    }

    private boolean mAutoRotateEnabled;
    private ImageDecodeOptions mImageDecodeOptions;
    private ImageRequest.ImageType mImageType;
    private boolean mLocalThumbnailPreviewsEnabled;
    private ImageRequest.RequestLevel mLowestPermittedRequestLevel;
    private Postprocessor mPostprocessor;
    private boolean mProgressiveRenderingEnabled;
    private Priority mRequestPriority;
    private ResizeOptions mResizeOptions;
    private Uri mSourceUri;
}
