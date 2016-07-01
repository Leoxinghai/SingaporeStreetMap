

package com.facebook.drawee.generic;

import android.content.res.Resources;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import com.facebook.common.internal.Preconditions;
import java.util.*;

// Referenced classes of package com.facebook.drawee.generic:
//            GenericDraweeHierarchy, RoundingParams

public class GenericDraweeHierarchyBuilder
{

    public GenericDraweeHierarchyBuilder(Resources resources)
    {
        mResources = resources;
        init();
    }

    private void init()
    {
        mFadeDuration = 300;
        mPlaceholderImage = null;
        mPlaceholderImageScaleType = null;
        mRetryImage = null;
        mRetryImageScaleType = null;
        mFailureImage = null;
        mFailureImageScaleType = null;
        mProgressBarImage = null;
        mProgressBarImageScaleType = null;
        mActualImageScaleType = DEFAULT_ACTUAL_IMAGE_SCALE_TYPE;
        mActualImageMatrix = null;
        mActualImageFocusPoint = null;
        mBackgrounds = null;
        mOverlays = null;
        mPressedStateOverlay = null;
        mRoundingParams = null;
        mActualImageColorFilter = null;
    }

    public static GenericDraweeHierarchyBuilder newInstance(Resources resources)
    {
        return new GenericDraweeHierarchyBuilder(resources);
    }

    private void validate()
    {
        if(mOverlays != null)
        {
            for(Iterator iterator = mOverlays.iterator(); iterator.hasNext(); Preconditions.checkNotNull((Drawable)iterator.next()));
        }
        if(mBackgrounds != null)
        {
            for(Iterator iterator1 = mBackgrounds.iterator(); iterator1.hasNext(); Preconditions.checkNotNull((Drawable)iterator1.next()));
        }
    }

    public GenericDraweeHierarchy build()
    {
        validate();
        return new GenericDraweeHierarchy(this);
    }

    public ColorFilter getActualImageColorFilter()
    {
        return mActualImageColorFilter;
    }

    public PointF getActualImageFocusPoint()
    {
        return mActualImageFocusPoint;
    }

    public Matrix getActualImageMatrix()
    {
        return mActualImageMatrix;
    }

    public com.facebook.drawee.drawable.ScalingUtils.ScaleType getActualImageScaleType()
    {
        return mActualImageScaleType;
    }

    public List getBackgrounds()
    {
        return mBackgrounds;
    }

    public int getFadeDuration()
    {
        return mFadeDuration;
    }

    public Drawable getFailureImage()
    {
        return mFailureImage;
    }

    public com.facebook.drawee.drawable.ScalingUtils.ScaleType getFailureImageScaleType()
    {
        return mFailureImageScaleType;
    }

    public List getOverlays()
    {
        return mOverlays;
    }

    public Drawable getPlaceholderImage()
    {
        return mPlaceholderImage;
    }

    public com.facebook.drawee.drawable.ScalingUtils.ScaleType getPlaceholderImageScaleType()
    {
        return mPlaceholderImageScaleType;
    }

    public Drawable getPressedStateOverlay()
    {
        return mPressedStateOverlay;
    }

    public Drawable getProgressBarImage()
    {
        return mProgressBarImage;
    }

    public com.facebook.drawee.drawable.ScalingUtils.ScaleType getProgressBarImageScaleType()
    {
        return mProgressBarImageScaleType;
    }

    public Resources getResources()
    {
        return mResources;
    }

    public Drawable getRetryImage()
    {
        return mRetryImage;
    }

    public com.facebook.drawee.drawable.ScalingUtils.ScaleType getRetryImageScaleType()
    {
        return mRetryImageScaleType;
    }

    public RoundingParams getRoundingParams()
    {
        return mRoundingParams;
    }

    public GenericDraweeHierarchyBuilder reset()
    {
        init();
        return this;
    }

    public GenericDraweeHierarchyBuilder setActualImageColorFilter(ColorFilter colorfilter)
    {
        mActualImageColorFilter = colorfilter;
        return this;
    }

    public GenericDraweeHierarchyBuilder setActualImageFocusPoint(PointF pointf)
    {
        mActualImageFocusPoint = pointf;
        return this;
    }

    public GenericDraweeHierarchyBuilder setActualImageMatrix(Matrix matrix)
    {
        mActualImageMatrix = matrix;
        mActualImageScaleType = null;
        return this;
    }

    public GenericDraweeHierarchyBuilder setActualImageScaleType(com.facebook.drawee.drawable.ScalingUtils.ScaleType scaletype)
    {
        mActualImageScaleType = scaletype;
        mActualImageMatrix = null;
        return this;
    }

    public GenericDraweeHierarchyBuilder setBackground(Drawable drawable)
    {
        mBackgrounds = Arrays.asList(new Drawable[] {
            drawable
        });
        return this;
    }

    public GenericDraweeHierarchyBuilder setBackgrounds(List list)
    {
        mBackgrounds = list;
        return this;
    }

    public GenericDraweeHierarchyBuilder setFadeDuration(int i)
    {
        mFadeDuration = i;
        return this;
    }

    public GenericDraweeHierarchyBuilder setFailureImage(Drawable drawable)
    {
        return setFailureImage(drawable, DEFAULT_SCALE_TYPE);
    }

    public GenericDraweeHierarchyBuilder setFailureImage(Drawable drawable, com.facebook.drawee.drawable.ScalingUtils.ScaleType scaletype)
    {
        mFailureImage = drawable;
        mFailureImageScaleType = scaletype;
        return this;
    }

    public GenericDraweeHierarchyBuilder setOverlay(Drawable drawable)
    {
        mOverlays = Arrays.asList(new Drawable[] {
            drawable
        });
        return this;
    }

    public GenericDraweeHierarchyBuilder setOverlays(List list)
    {
        mOverlays = list;
        return this;
    }

    public GenericDraweeHierarchyBuilder setPlaceholderImage(Drawable drawable)
    {
        return setPlaceholderImage(drawable, DEFAULT_SCALE_TYPE);
    }

    public GenericDraweeHierarchyBuilder setPlaceholderImage(Drawable drawable, com.facebook.drawee.drawable.ScalingUtils.ScaleType scaletype)
    {
        mPlaceholderImage = drawable;
        mPlaceholderImageScaleType = scaletype;
        return this;
    }

    public GenericDraweeHierarchyBuilder setPressedStateOverlay(Drawable drawable)
    {
        StateListDrawable statelistdrawable = new StateListDrawable();
        statelistdrawable.addState(new int[] {
            0x10100a7
        }, drawable);
        mPressedStateOverlay = statelistdrawable;
        return this;
    }

    public GenericDraweeHierarchyBuilder setProgressBarImage(Drawable drawable)
    {
        return setProgressBarImage(drawable, DEFAULT_SCALE_TYPE);
    }

    public GenericDraweeHierarchyBuilder setProgressBarImage(Drawable drawable, com.facebook.drawee.drawable.ScalingUtils.ScaleType scaletype)
    {
        mProgressBarImage = drawable;
        mProgressBarImageScaleType = scaletype;
        return this;
    }

    public GenericDraweeHierarchyBuilder setRetryImage(Drawable drawable)
    {
        return setRetryImage(drawable, DEFAULT_SCALE_TYPE);
    }

    public GenericDraweeHierarchyBuilder setRetryImage(Drawable drawable, com.facebook.drawee.drawable.ScalingUtils.ScaleType scaletype)
    {
        mRetryImage = drawable;
        mRetryImageScaleType = scaletype;
        return this;
    }

    public GenericDraweeHierarchyBuilder setRoundingParams(RoundingParams roundingparams)
    {
        mRoundingParams = roundingparams;
        return this;
    }

    public static final com.facebook.drawee.drawable.ScalingUtils.ScaleType DEFAULT_ACTUAL_IMAGE_SCALE_TYPE;
    public static final int DEFAULT_FADE_DURATION = 300;
    public static final com.facebook.drawee.drawable.ScalingUtils.ScaleType DEFAULT_SCALE_TYPE;
    private ColorFilter mActualImageColorFilter;
    private PointF mActualImageFocusPoint;
    private Matrix mActualImageMatrix;
    private com.facebook.drawee.drawable.ScalingUtils.ScaleType mActualImageScaleType;
    private List mBackgrounds;
    private int mFadeDuration;
    private Drawable mFailureImage;
    private com.facebook.drawee.drawable.ScalingUtils.ScaleType mFailureImageScaleType;
    private List mOverlays;
    private Drawable mPlaceholderImage;
    private com.facebook.drawee.drawable.ScalingUtils.ScaleType mPlaceholderImageScaleType;
    private Drawable mPressedStateOverlay;
    private Drawable mProgressBarImage;
    private com.facebook.drawee.drawable.ScalingUtils.ScaleType mProgressBarImageScaleType;
    private Resources mResources;
    private Drawable mRetryImage;
    private com.facebook.drawee.drawable.ScalingUtils.ScaleType mRetryImageScaleType;
    private RoundingParams mRoundingParams;

    static
    {
        DEFAULT_SCALE_TYPE = com.facebook.drawee.drawable.ScalingUtils.ScaleType.CENTER_INSIDE;
        DEFAULT_ACTUAL_IMAGE_SCALE_TYPE = com.facebook.drawee.drawable.ScalingUtils.ScaleType.CENTER_CROP;
    }
}
