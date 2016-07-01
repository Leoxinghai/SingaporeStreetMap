

package com.facebook.drawee.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import com.facebook.drawee.drawable.AutoRotateDrawable;
import com.facebook.drawee.generic.*;

// Referenced classes of package com.facebook.drawee.view:
//            DraweeView, AspectRatioMeasure

public class GenericDraweeView extends DraweeView
{

    public GenericDraweeView(Context context)
    {
        super(context);
        mAspectRatio = 0.0F;
        mMeasureSpec = new AspectRatioMeasure.Spec();
        inflateHierarchy(context, null);
    }

    public GenericDraweeView(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
        mAspectRatio = 0.0F;
        mMeasureSpec = new AspectRatioMeasure.Spec();
        inflateHierarchy(context, attributeset);
    }

    public GenericDraweeView(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        mAspectRatio = 0.0F;
        mMeasureSpec = new AspectRatioMeasure.Spec();
        inflateHierarchy(context, attributeset);
    }

    public GenericDraweeView(Context context, AttributeSet attributeset, int i, int j)
    {
        super(context, attributeset, i, j);
        mAspectRatio = 0.0F;
        mMeasureSpec = new AspectRatioMeasure.Spec();
        inflateHierarchy(context, attributeset);
    }

    public GenericDraweeView(Context context, GenericDraweeHierarchy genericdraweehierarchy)
    {
        super(context);
        mAspectRatio = 0.0F;
        mMeasureSpec = new AspectRatioMeasure.Spec();
        setHierarchy(genericdraweehierarchy);
    }

    private static com.facebook.drawee.drawable.ScalingUtils.ScaleType getScaleTypeFromXml(TypedArray typedarray, int i, com.facebook.drawee.drawable.ScalingUtils.ScaleType scaletype)
    {
        i = typedarray.getInt(i, -1);
        if(i < 0)
            return scaletype;
        else
            return com.facebook.drawee.drawable.ScalingUtils.ScaleType.values()[i];
    }

    private void inflateHierarchy(Context context, AttributeSet attributeset)
    {
        com.facebook.drawee.drawable.ScalingUtils.ScaleType scaletype;
        com.facebook.drawee.drawable.ScalingUtils.ScaleType scaletype1;
        com.facebook.drawee.drawable.ScalingUtils.ScaleType scaletype2;
        com.facebook.drawee.drawable.ScalingUtils.ScaleType scaletype3;
        com.facebook.drawee.drawable.ScalingUtils.ScaleType scaletype4;
        com.facebook.drawee.drawable.ScalingUtils.ScaleType scaletype5;
        com.facebook.drawee.drawable.ScalingUtils.ScaleType scaletype6;
        com.facebook.drawee.drawable.ScalingUtils.ScaleType scaletype7;
        com.facebook.drawee.drawable.ScalingUtils.ScaleType scaletype8;
        com.facebook.drawee.drawable.ScalingUtils.ScaleType scaletype9;
        Resources resources;
        int i;
        int j;
        int k;
        int l;
        int i1;
        int j1;
        int k1;
        int l1;
        int i2;
        int j2;
        int k2;
        int l2;
        int i3;
        boolean flag;
        boolean flag1;
        boolean flag2;
        boolean flag3;
        boolean flag4;
        resources = context.getResources();
        j = 300;
        i1 = 0;
        scaletype9 = GenericDraweeHierarchyBuilder.DEFAULT_SCALE_TYPE;
        i2 = 0;
        scaletype8 = GenericDraweeHierarchyBuilder.DEFAULT_SCALE_TYPE;
        k = 0;
        scaletype7 = GenericDraweeHierarchyBuilder.DEFAULT_SCALE_TYPE;
        l1 = 0;
        scaletype6 = GenericDraweeHierarchyBuilder.DEFAULT_SCALE_TYPE;
        scaletype5 = GenericDraweeHierarchyBuilder.DEFAULT_ACTUAL_IMAGE_SCALE_TYPE;
        i = 0;
        l = 0;
        j1 = 0;
        flag = false;
        k2 = 0;
        flag3 = true;
        flag4 = true;
        flag2 = true;
        flag1 = true;
        j2 = 0;
        i3 = 0;
        l2 = 0;
        k1 = 0;
        scaletype2 = scaletype5;
        scaletype4 = scaletype7;
        scaletype3 = scaletype9;
        scaletype = scaletype6;
        scaletype1 = scaletype8;
        if(attributeset != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attributeset, com.facebook.drawee.R.styleable.GenericDraweeView);
            j = typedArray.getInt(com.facebook.drawee.R.styleable.GenericDraweeView_fadeDuration, 300);
            mAspectRatio = typedArray.getFloat(com.facebook.drawee.R.styleable.GenericDraweeView_viewAspectRatio, mAspectRatio);
            i1 = typedArray.getResourceId(com.facebook.drawee.R.styleable.GenericDraweeView_placeholderImage, 0);
            scaletype3 = getScaleTypeFromXml(typedArray, com.facebook.drawee.R.styleable.GenericDraweeView_placeholderImageScaleType, scaletype9);
            i2 = typedArray.getResourceId(com.facebook.drawee.R.styleable.GenericDraweeView_retryImage, 0);
            scaletype1 = getScaleTypeFromXml(typedArray, com.facebook.drawee.R.styleable.GenericDraweeView_retryImageScaleType, scaletype8);
            k = typedArray.getResourceId(com.facebook.drawee.R.styleable.GenericDraweeView_failureImage, 0);
            scaletype4 = getScaleTypeFromXml(typedArray, com.facebook.drawee.R.styleable.GenericDraweeView_failureImageScaleType, scaletype7);
            l1 = typedArray.getResourceId(com.facebook.drawee.R.styleable.GenericDraweeView_progressBarImage, 0);
            scaletype = getScaleTypeFromXml(typedArray, com.facebook.drawee.R.styleable.GenericDraweeView_progressBarImageScaleType, scaletype6);
            k1 = typedArray.getInteger(com.facebook.drawee.R.styleable.GenericDraweeView_progressBarAutoRotateInterval, 0);
            scaletype2 = getScaleTypeFromXml(typedArray, com.facebook.drawee.R.styleable.GenericDraweeView_actualImageScaleType, scaletype5);
            i = typedArray.getResourceId(com.facebook.drawee.R.styleable.GenericDraweeView_backgroundImage, 0);
            l = typedArray.getResourceId(com.facebook.drawee.R.styleable.GenericDraweeView_overlayImage, 0);
            j1 = typedArray.getResourceId(com.facebook.drawee.R.styleable.GenericDraweeView_pressedStateOverlayImage, 0);
            flag = typedArray.getBoolean(com.facebook.drawee.R.styleable.GenericDraweeView_roundAsCircle, false);
            k2 = typedArray.getDimensionPixelSize(com.facebook.drawee.R.styleable.GenericDraweeView_roundedCornerRadius, 0);
            flag3 = typedArray.getBoolean(com.facebook.drawee.R.styleable.GenericDraweeView_roundTopLeft, true);
            flag4 = typedArray.getBoolean(com.facebook.drawee.R.styleable.GenericDraweeView_roundTopRight, true);
            flag2 = typedArray.getBoolean(com.facebook.drawee.R.styleable.GenericDraweeView_roundBottomRight, true);
            flag1 = typedArray.getBoolean(com.facebook.drawee.R.styleable.GenericDraweeView_roundBottomLeft, true);
            j2 = typedArray.getColor(com.facebook.drawee.R.styleable.GenericDraweeView_roundWithOverlayColor, 0);
            i3 = typedArray.getDimensionPixelSize(com.facebook.drawee.R.styleable.GenericDraweeView_roundingBorderWidth, 0);
            l2 = typedArray.getColor(com.facebook.drawee.R.styleable.GenericDraweeView_roundingBorderColor, 0);
            typedArray.recycle();
            GenericDraweeHierarchyBuilder genericdraweehierarchybuilder = new GenericDraweeHierarchyBuilder(resources);
            genericdraweehierarchybuilder.setFadeDuration(j);
            if (i1 > 0)
                genericdraweehierarchybuilder.setPlaceholderImage(resources.getDrawable(i1), scaletype3);
            if (i2 > 0)
                genericdraweehierarchybuilder.setRetryImage(resources.getDrawable(i2), scaletype1);
            if (k > 0)
                genericdraweehierarchybuilder.setFailureImage(resources.getDrawable(k), scaletype4);
            if (l1 > 0) {
                Drawable drawable = resources.getDrawable(l1);
                AutoRotateDrawable autoRotateDrawable;
                if (k1 > 0)
                    autoRotateDrawable = new AutoRotateDrawable(drawable, k1);
                genericdraweehierarchybuilder.setProgressBarImage(drawable, scaletype);
            }
            if (i > 0)
                genericdraweehierarchybuilder.setBackground(resources.getDrawable(i));
            if (l > 0)
                genericdraweehierarchybuilder.setOverlay(resources.getDrawable(l));
            if (j1 > 0)
                genericdraweehierarchybuilder.setPressedStateOverlay(getResources().getDrawable(j1));
            genericdraweehierarchybuilder.setActualImageScaleType(scaletype2);
            if (flag || k2 > 0) {
                RoundingParams roundingParams = new RoundingParams();
                roundingParams.setRoundAsCircle(flag);
                if (k2 > 0) {
                    float f;
                    float f1;
                    float f2;
                    float f3;
                    if (flag3)
                        f = k2;
                    else
                        f = 0.0F;
                    if (flag4)
                        f1 = k2;
                    else
                        f1 = 0.0F;
                    if (flag2)
                        f2 = k2;
                    else
                        f2 = 0.0F;
                    if (flag1)
                        f3 = k2;
                    else
                        f3 = 0.0F;
                    roundingParams.setCornersRadii(f, f1, f2, f3);
                }
                if (j2 != 0)
                    roundingParams.setOverlayColor(j2);
                if (l2 != 0 && i3 > 0)
                    roundingParams.setBorder(l2, i3);
                genericdraweehierarchybuilder.setRoundingParams(roundingParams);
            }
            setHierarchy(genericdraweehierarchybuilder.build());
            return;
        }
    }

    public float getAspectRatio()
    {
        return mAspectRatio;
    }

    protected void onMeasure(int i, int j)
    {
        mMeasureSpec.width = i;
        mMeasureSpec.height = j;
        AspectRatioMeasure.updateMeasureSpec(mMeasureSpec, mAspectRatio, getLayoutParams(), getPaddingLeft() + getPaddingRight(), getPaddingTop() + getPaddingBottom());
        super.onMeasure(mMeasureSpec.width, mMeasureSpec.height);
    }

    public void setAspectRatio(float f)
    {
        if(f == mAspectRatio)
        {
            return;
        } else
        {
            mAspectRatio = f;
            requestLayout();
            return;
        }
    }

    private float mAspectRatio;
    private final AspectRatioMeasure.Spec mMeasureSpec;
}
