

package com.facebook.drawee.generic;

import android.content.res.Resources;
import android.graphics.*;
import android.graphics.drawable.*;
import com.facebook.common.internal.Preconditions;
import com.facebook.drawee.drawable.*;
import com.facebook.drawee.interfaces.SettableDraweeHierarchy;
import java.util.Iterator;
import java.util.List;

// Referenced classes of package com.facebook.drawee.generic:
//            GenericDraweeHierarchyBuilder, RoundingParams

public class GenericDraweeHierarchy
    implements SettableDraweeHierarchy
{
    public static class RootDrawable extends ForwardingDrawable
        implements VisibilityAwareDrawable
    {

        public void draw(Canvas canvas)
        {
            if(!isVisible())
                return;
            if(mVisibilityCallback != null)
                mVisibilityCallback.onDraw();
            super.draw(canvas);
        }

        public int getIntrinsicHeight()
        {
            return -1;
        }

        public int getIntrinsicWidth()
        {
            return -1;
        }

        public void setVisibilityCallback(VisibilityCallback visibilitycallback)
        {
            mVisibilityCallback = visibilitycallback;
        }

        public boolean setVisible(boolean flag, boolean flag1)
        {
            if(mVisibilityCallback != null)
                mVisibilityCallback.onVisibilityChange(flag);
            return super.setVisible(flag, flag1);
        }

        private VisibilityCallback mVisibilityCallback;

        public RootDrawable(Drawable drawable)
        {
            super(drawable);
        }
    }


    GenericDraweeHierarchy(GenericDraweeHierarchyBuilder genericdraweehierarchybuilder)
    {
        mResources = genericdraweehierarchybuilder.getResources();
        mRoundingParams = genericdraweehierarchybuilder.getRoundingParams();
        Drawable drawable;
        Drawable drawable1;
        Drawable drawable2;
        Drawable drawable3;
        Drawable adrawable[];
        Drawable drawable4;
        Drawable drawable5;
        int i;
        int k;
        int i1;
        int j1;
        if(genericdraweehierarchybuilder.getBackgrounds() != null)
            i = genericdraweehierarchybuilder.getBackgrounds().size();
        else
            i = 0;
        i1 = 0 + i;
        drawable1 = genericdraweehierarchybuilder.getPlaceholderImage();
        drawable = drawable1;
        if(drawable1 == null)
            drawable = getEmptyPlaceholderDrawable();
        drawable4 = maybeWrapWithScaleType(maybeApplyRoundingBitmapOnly(mRoundingParams, mResources, drawable), genericdraweehierarchybuilder.getPlaceholderImageScaleType());
        k = i1 + 1;
        mPlaceholderImageIndex = i1;
        mActualImageSettableDrawable = new SettableDrawable(mEmptyActualImageDrawable);
        drawable5 = maybeWrapWithMatrix(maybeWrapWithScaleType(mActualImageSettableDrawable, genericdraweehierarchybuilder.getActualImageScaleType(), genericdraweehierarchybuilder.getActualImageFocusPoint()), genericdraweehierarchybuilder.getActualImageMatrix());
        drawable5.setColorFilter(genericdraweehierarchybuilder.getActualImageColorFilter());
        i1 = k + 1;
        mActualImageIndex = k;
        drawable1 = genericdraweehierarchybuilder.getProgressBarImage();
        k = i1 + 1;
        mProgressBarImageIndex = i1;
        drawable = drawable1;
        if(drawable1 != null)
            drawable = maybeWrapWithScaleType(drawable1, genericdraweehierarchybuilder.getProgressBarImageScaleType());
        drawable2 = genericdraweehierarchybuilder.getRetryImage();
        i1 = k + 1;
        mRetryImageIndex = k;
        drawable1 = drawable2;
        if(drawable2 != null)
            drawable1 = maybeWrapWithScaleType(drawable2, genericdraweehierarchybuilder.getRetryImageScaleType());
        drawable3 = genericdraweehierarchybuilder.getFailureImage();
        j1 = i1 + 1;
        mFailureImageIndex = i1;
        drawable2 = drawable3;
        if(drawable3 != null)
            drawable2 = maybeWrapWithScaleType(drawable3, genericdraweehierarchybuilder.getFailureImageScaleType());
        if(genericdraweehierarchybuilder.getOverlays() != null)
            k = genericdraweehierarchybuilder.getOverlays().size();
        else
            k = 0;
        if(genericdraweehierarchybuilder.getPressedStateOverlay() != null)
            i1 = 1;
        else
            i1 = 0;
        k += i1;
        i1 = j1 + k;
        mControllerOverlayIndex = i1;
        adrawable = new Drawable[i1 + 1];
        if(i > 0)
        {
            i = 0;
            for(Iterator iterator1 = genericdraweehierarchybuilder.getBackgrounds().iterator(); iterator1.hasNext();)
            {
                Drawable drawable6 = (Drawable)iterator1.next();
                adrawable[i + 0] = maybeApplyRoundingBitmapOnly(mRoundingParams, mResources, drawable6);
                i++;
            }

        }
        adrawable[mPlaceholderImageIndex] = drawable4;
        adrawable[mActualImageIndex] = drawable5;
        adrawable[mProgressBarImageIndex] = drawable;
        adrawable[mRetryImageIndex] = drawable1;
        adrawable[mFailureImageIndex] = drawable2;
        if(k > 0)
        {
            int l = 0;
            int j = 0;
            if(genericdraweehierarchybuilder.getOverlays() != null)
            {
                Iterator iterator = genericdraweehierarchybuilder.getOverlays().iterator();
                do
                {
                    l = j;
                    if(!iterator.hasNext())
                        break;
                    adrawable[j1 + j] = (Drawable)iterator.next();
                    j++;
                } while(true);
            }
            if(genericdraweehierarchybuilder.getPressedStateOverlay() != null)
                adrawable[j1 + l] = genericdraweehierarchybuilder.getPressedStateOverlay();
        }
        if(mControllerOverlayIndex >= 0)
            adrawable[mControllerOverlayIndex] = mEmptyControllerOverlayDrawable;
        mFadeDrawable = new FadeDrawable(adrawable);
        mFadeDrawable.setTransitionDuration(genericdraweehierarchybuilder.getFadeDuration());
        mTopLevelDrawable = new RootDrawable(maybeWrapWithRoundedOverlayColor(mRoundingParams, mFadeDrawable));
        mTopLevelDrawable.mutate();
        resetFade();
    }

    private static Drawable applyRounding(RoundingParams roundingparams, Resources resources, Drawable drawable)
    {

        if(drawable instanceof BitmapDrawable)
        {
            RoundedBitmapDrawable temp = RoundedBitmapDrawable.fromBitmapDrawable(resources, (BitmapDrawable)drawable);
            applyRoundingParams(temp, roundingparams);
            return temp;
        }
        if((drawable instanceof ColorDrawable) && android.os.Build.VERSION.SDK_INT >= 11)
        {
            RoundedColorDrawable temp = RoundedColorDrawable.fromColorDrawable((ColorDrawable)drawable);
            applyRoundingParams(temp, roundingparams);
            return temp;
        } else
        {
            return drawable;
        }
    }

    private static void applyRoundingParams(Rounded rounded, RoundingParams roundingparams)
    {
        rounded.setCircle(roundingparams.getRoundAsCircle());
        rounded.setRadii(roundingparams.getCornersRadii());
        rounded.setBorder(roundingparams.getBorderColor(), roundingparams.getBorderWidth());
    }

    private void fadeInLayer(int i)
    {
        if(i >= 0)
            mFadeDrawable.fadeInLayer(i);
    }

    private void fadeOutBranches()
    {
        fadeOutLayer(mPlaceholderImageIndex);
        fadeOutLayer(mActualImageIndex);
        fadeOutLayer(mProgressBarImageIndex);
        fadeOutLayer(mRetryImageIndex);
        fadeOutLayer(mFailureImageIndex);
    }

    private void fadeOutLayer(int i)
    {
        if(i >= 0)
            mFadeDrawable.fadeOutLayer(i);
    }

    private ScaleTypeDrawable findLayerScaleTypeDrawable(int i)
    {
        Drawable drawable1 = mFadeDrawable.getDrawable(i);
        Drawable drawable = drawable1;
        if(drawable1 instanceof MatrixDrawable)
            drawable = drawable1.getCurrent();
        if(drawable instanceof ScaleTypeDrawable)
            return (ScaleTypeDrawable)drawable;
        else
            return null;
    }

    private Drawable getEmptyPlaceholderDrawable()
    {
        if(mEmptyPlaceholderDrawable == null)
            mEmptyPlaceholderDrawable = new ColorDrawable(0);
        return mEmptyPlaceholderDrawable;
    }

    private Drawable getLayerChildDrawable(int i)
    {
        return getLayerDrawable(i, false);
    }

    private Drawable getLayerDrawable(int i, boolean flag)
    {
        Object obj = mFadeDrawable;
        Drawable drawable1 = mFadeDrawable.getDrawable(i);
        Drawable drawable = drawable1;
        if(drawable1 instanceof MatrixDrawable)
        {
            obj = drawable1;
            drawable = ((Drawable) (obj)).getCurrent();
        }
        drawable1 = drawable;
        if(drawable instanceof ScaleTypeDrawable)
        {
            drawable1 = drawable.getCurrent();
            obj = drawable;
        }
        if(flag)
            return ((Drawable) (obj));
        else
            return drawable1;
    }

    private static Drawable maybeApplyRoundingBitmapOnly(RoundingParams roundingparams, Resources resources, Drawable drawable)
    {
        if(roundingparams != null && roundingparams.getRoundingMethod() == RoundingParams.RoundingMethod.BITMAP_ONLY)
        {
            if((drawable instanceof BitmapDrawable) || (drawable instanceof ColorDrawable))
                return applyRounding(roundingparams, resources, drawable);
            Drawable drawable2 = drawable;
            Drawable drawable1 = drawable2.getCurrent();
            while(drawable1 != null && drawable2 != drawable1)
            {
                if((drawable2 instanceof ForwardingDrawable) && ((drawable1 instanceof BitmapDrawable) || (drawable1 instanceof ColorDrawable)))
                    ((ForwardingDrawable)drawable2).setCurrent(applyRounding(roundingparams, resources, drawable1));
                drawable2 = drawable1;
                drawable1 = drawable2.getCurrent();
            }
        }
        return drawable;
    }

    private static Drawable maybeWrapWithMatrix(Drawable drawable, Matrix matrix)
    {
        Preconditions.checkNotNull(drawable);
        if(matrix == null)
            return drawable;
        else
            return new MatrixDrawable(drawable, matrix);
    }

    private static Drawable maybeWrapWithRoundedOverlayColor(RoundingParams roundingparams, Drawable drawable)
    {
        if(roundingparams != null && roundingparams.getRoundingMethod() == RoundingParams.RoundingMethod.OVERLAY_COLOR)
        {
            RoundedCornersDrawable temp = new RoundedCornersDrawable(drawable);
            applyRoundingParams(temp, roundingparams);
            temp.setOverlayColor(roundingparams.getOverlayColor());
            return temp;
        } else
        {
            return drawable;
        }
    }

    private static Drawable maybeWrapWithScaleType(Drawable drawable, com.facebook.drawee.drawable.ScalingUtils.ScaleType scaletype)
    {
        return maybeWrapWithScaleType(drawable, scaletype, null);
    }

    private static Drawable maybeWrapWithScaleType(Drawable drawable, com.facebook.drawee.drawable.ScalingUtils.ScaleType scaletype, PointF pointf)
    {
        Preconditions.checkNotNull(drawable);
        if(scaletype == null)
            return drawable;
        ScaleTypeDrawable temp = new ScaleTypeDrawable(drawable, scaletype);
        if(pointf != null)
            temp.setFocusPoint(pointf);
        return temp;
    }

    private void resetActualImages()
    {
        if(mActualImageSettableDrawable != null)
            mActualImageSettableDrawable.setDrawable(mEmptyActualImageDrawable);
    }

    private void resetFade()
    {
        if(mFadeDrawable != null)
        {
            mFadeDrawable.beginBatchMode();
            mFadeDrawable.fadeInAllLayers();
            fadeOutBranches();
            fadeInLayer(mPlaceholderImageIndex);
            mFadeDrawable.finishTransitionImmediately();
            mFadeDrawable.endBatchMode();
        }
    }

    private static void resetRoundedDrawable(Rounded rounded)
    {
        rounded.setCircle(false);
        rounded.setRadius(0.0F);
        rounded.setBorder(0, 0.0F);
    }

    private void setDrawableAndScaleType(Drawable drawable, com.facebook.drawee.drawable.ScalingUtils.ScaleType scaletype, int i)
    {
        if(drawable == null)
        {
            mFadeDrawable.setDrawable(i, null);
            return;
        }
        Drawable drawable1 = maybeApplyRoundingBitmapOnly(mRoundingParams, mResources, drawable);
        //drawable = drawable1;
        if(scaletype != null)
        {
            ScaleTypeDrawable temp = findLayerScaleTypeDrawable(i);
            if(drawable1 != null)
            {
                temp.setScaleType(scaletype);
                drawable1 = temp;
                //drawable = drawable1;
            } else
            {
                drawable1 = maybeWrapWithScaleType(drawable1, scaletype);
            }
        }
        setLayerChildDrawable(i, drawable1);
    }

    private void setLayerChildDrawable(int i, Drawable drawable)
    {
        Drawable drawable1 = getLayerDrawable(i, true);
        if(drawable1 == mFadeDrawable)
        {
            mFadeDrawable.setDrawable(i, drawable);
            return;
        } else
        {
            ((ForwardingDrawable)drawable1).setCurrent(drawable);
            return;
        }
    }

    private void setProgress(float f)
    {
        Drawable drawable = getLayerChildDrawable(mProgressBarImageIndex);
        if(drawable == null)
            return;
        if(f >= 0.999F)
        {
            if(drawable instanceof Animatable)
                ((Animatable)drawable).stop();
            fadeOutLayer(mProgressBarImageIndex);
        } else
        {
            if(drawable instanceof Animatable)
                ((Animatable)drawable).start();
            fadeInLayer(mProgressBarImageIndex);
        }
        drawable.setLevel(Math.round(10000F * f));
    }

    private void updateBitmapOnlyRounding()
    {
        if(mRoundingParams != null && mRoundingParams.getRoundingMethod() == RoundingParams.RoundingMethod.BITMAP_ONLY)
        {
            int i = 0;
            while(i < mFadeDrawable.getNumberOfLayers())
            {
                Drawable drawable = getLayerChildDrawable(i);
                if(drawable instanceof Rounded)
                    applyRoundingParams((Rounded)drawable, mRoundingParams);
                else
                if(drawable != null)
                {
                    setLayerChildDrawable(i, mEmptyActualImageDrawable);
                    setLayerChildDrawable(i, maybeApplyRoundingBitmapOnly(mRoundingParams, mResources, drawable));
                }
                i++;
            }
        } else
        {
            for(int j = 0; j < mFadeDrawable.getNumberOfLayers(); j++)
            {
                Drawable drawable1 = getLayerChildDrawable(j);
                if(drawable1 instanceof Rounded)
                    resetRoundedDrawable((Rounded)drawable1);
            }

        }
    }

    private void updateOverlayColorRounding()
    {
        Object obj = mTopLevelDrawable.getCurrent();
        if(mRoundingParams == null || mRoundingParams.getRoundingMethod() != RoundingParams.RoundingMethod.OVERLAY_COLOR) {
            if(obj instanceof RoundedCornersDrawable)
            {
                obj = ((RoundedCornersDrawable)obj).setCurrent(mEmptyActualImageDrawable);
                mTopLevelDrawable.setCurrent(((Drawable) (obj)));
                return;
            }
        } else {
            if (!(obj instanceof RoundedCornersDrawable)) {
                obj = mTopLevelDrawable.setCurrent(mEmptyActualImageDrawable);
                obj = maybeWrapWithRoundedOverlayColor(mRoundingParams, ((Drawable) (obj)));
                mTopLevelDrawable.setCurrent(((Drawable) (obj)));
                return;
            } else {
                obj = (RoundedCornersDrawable) obj;
                applyRoundingParams(((Rounded) (obj)), mRoundingParams);
                ((RoundedCornersDrawable) (obj)).setOverlayColor(mRoundingParams.getOverlayColor());
            }
        }

        return;
    }

    public void getActualImageBounds(RectF rectf)
    {
        mActualImageSettableDrawable.getTransformedBounds(rectf);
    }

    public RoundingParams getRoundingParams()
    {
        return mRoundingParams;
    }

    public Drawable getTopLevelDrawable()
    {
        return mTopLevelDrawable;
    }

    public void reset()
    {
        resetActualImages();
        resetFade();
    }

    public void setActualImageColorFilter(ColorFilter colorfilter)
    {
        mFadeDrawable.getDrawable(mActualImageIndex).setColorFilter(colorfilter);
    }

    public void setActualImageFocusPoint(PointF pointf)
    {
        Preconditions.checkNotNull(pointf);
        ScaleTypeDrawable scaletypedrawable = findLayerScaleTypeDrawable(mActualImageIndex);
        if(scaletypedrawable == null)
        {
            throw new UnsupportedOperationException("ScaleTypeDrawable not found!");
        } else
        {
            scaletypedrawable.setFocusPoint(pointf);
            return;
        }
    }

    public void setActualImageScaleType(com.facebook.drawee.drawable.ScalingUtils.ScaleType scaletype)
    {
        Preconditions.checkNotNull(scaletype);
        ScaleTypeDrawable scaletypedrawable = findLayerScaleTypeDrawable(mActualImageIndex);
        if(scaletypedrawable == null)
        {
            throw new UnsupportedOperationException("ScaleTypeDrawable not found!");
        } else
        {
            scaletypedrawable.setScaleType(scaletype);
            return;
        }
    }

    public void setControllerOverlay(Drawable drawable)
    {
        Drawable drawable1 = drawable;
        if(drawable == null)
            drawable1 = mEmptyControllerOverlayDrawable;
        mFadeDrawable.setDrawable(mControllerOverlayIndex, drawable1);
    }

    public void setFadeDuration(int i)
    {
        mFadeDrawable.setTransitionDuration(i);
    }

    public void setFailure(Throwable throwable)
    {
        mFadeDrawable.beginBatchMode();
        fadeOutBranches();
        if(mFadeDrawable.getDrawable(mFailureImageIndex) != null)
            fadeInLayer(mFailureImageIndex);
        else
            fadeInLayer(mPlaceholderImageIndex);
        mFadeDrawable.endBatchMode();
    }

    public void setFailureImage(Drawable drawable)
    {
        setFailureImage(drawable, null);
    }

    public void setFailureImage(Drawable drawable, com.facebook.drawee.drawable.ScalingUtils.ScaleType scaletype)
    {
        setDrawableAndScaleType(drawable, scaletype, mFailureImageIndex);
    }

    public void setImage(Drawable drawable, float f, boolean flag)
    {
        drawable = maybeApplyRoundingBitmapOnly(mRoundingParams, mResources, drawable);
        drawable.mutate();
        mActualImageSettableDrawable.setDrawable(drawable);
        mFadeDrawable.beginBatchMode();
        fadeOutBranches();
        fadeInLayer(mActualImageIndex);
        setProgress(f);
        if(flag)
            mFadeDrawable.finishTransitionImmediately();
        mFadeDrawable.endBatchMode();
    }

    public void setPlaceholderImage(int i)
    {
        setPlaceholderImage(mResources.getDrawable(i));
    }

    public void setPlaceholderImage(Drawable drawable)
    {
        setPlaceholderImage(drawable, null);
    }

    public void setPlaceholderImage(Drawable drawable, com.facebook.drawee.drawable.ScalingUtils.ScaleType scaletype)
    {
        Drawable drawable1 = drawable;
        if(drawable == null)
            drawable1 = getEmptyPlaceholderDrawable();
        setDrawableAndScaleType(drawable1, scaletype, mPlaceholderImageIndex);
    }

    public void setPlaceholderImageFocusPoint(PointF pointf)
    {
        Preconditions.checkNotNull(pointf);
        ScaleTypeDrawable scaletypedrawable = findLayerScaleTypeDrawable(mPlaceholderImageIndex);
        if(scaletypedrawable == null)
        {
            throw new UnsupportedOperationException("ScaleTypeDrawable not found!");
        } else
        {
            scaletypedrawable.setFocusPoint(pointf);
            return;
        }
    }

    public void setProgress(float f, boolean flag)
    {
        mFadeDrawable.beginBatchMode();
        setProgress(f);
        if(flag)
            mFadeDrawable.finishTransitionImmediately();
        mFadeDrawable.endBatchMode();
    }

    public void setProgressBarImage(Drawable drawable)
    {
        setProgressBarImage(drawable, null);
    }

    public void setProgressBarImage(Drawable drawable, com.facebook.drawee.drawable.ScalingUtils.ScaleType scaletype)
    {
        setDrawableAndScaleType(drawable, scaletype, mProgressBarImageIndex);
    }

    public void setRetry(Throwable throwable)
    {
        mFadeDrawable.beginBatchMode();
        fadeOutBranches();
        if(mFadeDrawable.getDrawable(mRetryImageIndex) != null)
            fadeInLayer(mRetryImageIndex);
        else
            fadeInLayer(mPlaceholderImageIndex);
        mFadeDrawable.endBatchMode();
    }

    public void setRetryImage(Drawable drawable)
    {
        setRetryImage(drawable, null);
    }

    public void setRetryImage(Drawable drawable, com.facebook.drawee.drawable.ScalingUtils.ScaleType scaletype)
    {
        setDrawableAndScaleType(drawable, scaletype, mRetryImageIndex);
    }

    public void setRoundingParams(RoundingParams roundingparams)
    {
        mRoundingParams = roundingparams;
        updateOverlayColorRounding();
        updateBitmapOnlyRounding();
    }

    private final int mActualImageIndex;
    private final SettableDrawable mActualImageSettableDrawable;
    private final int mControllerOverlayIndex;
    private final Drawable mEmptyActualImageDrawable = new ColorDrawable(0);
    private final Drawable mEmptyControllerOverlayDrawable = new ColorDrawable(0);
    private Drawable mEmptyPlaceholderDrawable;
    private final FadeDrawable mFadeDrawable;
    private final int mFailureImageIndex;
    private final int mPlaceholderImageIndex;
    private final int mProgressBarImageIndex;
    private final Resources mResources;
    private final int mRetryImageIndex;
    private RoundingParams mRoundingParams;
    private final RootDrawable mTopLevelDrawable;
}
