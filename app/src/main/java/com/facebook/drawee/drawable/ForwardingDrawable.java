

package com.facebook.drawee.drawable;

import android.graphics.*;
import android.graphics.drawable.Drawable;

// Referenced classes of package com.facebook.drawee.drawable:
//            TransformCallback, TransformAwareDrawable, DrawableProperties, DrawableUtils

public abstract class ForwardingDrawable extends Drawable
    implements android.graphics.drawable.Drawable.Callback, TransformCallback, TransformAwareDrawable
{

    public ForwardingDrawable(Drawable drawable)
    {
        mCurrentDelegate = drawable;
        DrawableUtils.setCallbacks(mCurrentDelegate, this, this);
    }

    public void draw(Canvas canvas)
    {
        mCurrentDelegate.draw(canvas);
    }

    public Drawable getCurrent()
    {
        return mCurrentDelegate;
    }

    public int getIntrinsicHeight()
    {
        return mCurrentDelegate.getIntrinsicHeight();
    }

    public int getIntrinsicWidth()
    {
        return mCurrentDelegate.getIntrinsicWidth();
    }

    public int getOpacity()
    {
        return mCurrentDelegate.getOpacity();
    }

    public boolean getPadding(Rect rect)
    {
        return mCurrentDelegate.getPadding(rect);
    }

    protected void getParentTransform(Matrix matrix)
    {
        if(mTransformCallback != null)
        {
            mTransformCallback.getTransform(matrix);
            return;
        } else
        {
            matrix.reset();
            return;
        }
    }

    public void getRootBounds(RectF rectf)
    {
        if(mTransformCallback != null)
        {
            mTransformCallback.getRootBounds(rectf);
            return;
        } else
        {
            rectf.set(getBounds());
            return;
        }
    }

    public void getTransform(Matrix matrix)
    {
        getParentTransform(matrix);
    }

    public void getTransformedBounds(RectF rectf)
    {
        getParentTransform(sTempTransform);
        rectf.set(getBounds());
        sTempTransform.mapRect(rectf);
    }

    public void invalidateDrawable(Drawable drawable)
    {
        invalidateSelf();
    }

    public boolean isStateful()
    {
        return mCurrentDelegate.isStateful();
    }

    public Drawable mutate()
    {
        mCurrentDelegate.mutate();
        return this;
    }

    protected void onBoundsChange(Rect rect)
    {
        mCurrentDelegate.setBounds(rect);
    }

    protected boolean onLevelChange(int i)
    {
        return mCurrentDelegate.setLevel(i);
    }

    protected boolean onStateChange(int ai[])
    {
        return mCurrentDelegate.setState(ai);
    }

    public void scheduleDrawable(Drawable drawable, Runnable runnable, long l)
    {
        scheduleSelf(runnable, l);
    }

    public void setAlpha(int i)
    {
        mDrawableProperties.setAlpha(i);
        mCurrentDelegate.setAlpha(i);
    }

    public void setColorFilter(ColorFilter colorfilter)
    {
        mDrawableProperties.setColorFilter(colorfilter);
        mCurrentDelegate.setColorFilter(colorfilter);
    }

    public Drawable setCurrent(Drawable drawable)
    {
        drawable = setCurrentWithoutInvalidate(drawable);
        invalidateSelf();
        return drawable;
    }

    protected Drawable setCurrentWithoutInvalidate(Drawable drawable)
    {
        Drawable drawable1 = mCurrentDelegate;
        DrawableUtils.setCallbacks(drawable1, null, null);
        DrawableUtils.setCallbacks(drawable, null, null);
        DrawableUtils.setDrawableProperties(drawable, mDrawableProperties);
        DrawableUtils.copyProperties(drawable, drawable1);
        DrawableUtils.setCallbacks(drawable, this, this);
        mCurrentDelegate = drawable;
        return drawable1;
    }

    public void setDither(boolean flag)
    {
        mDrawableProperties.setDither(flag);
        mCurrentDelegate.setDither(flag);
    }

    public void setFilterBitmap(boolean flag)
    {
        mDrawableProperties.setFilterBitmap(flag);
        mCurrentDelegate.setFilterBitmap(flag);
    }

    public void setTransformCallback(TransformCallback transformcallback)
    {
        mTransformCallback = transformcallback;
    }

    public boolean setVisible(boolean flag, boolean flag1)
    {
        super.setVisible(flag, flag1);
        return mCurrentDelegate.setVisible(flag, flag1);
    }

    public void unscheduleDrawable(Drawable drawable, Runnable runnable)
    {
        unscheduleSelf(runnable);
    }

    private static final Matrix sTempTransform = new Matrix();
    private Drawable mCurrentDelegate;
    private final DrawableProperties mDrawableProperties = new DrawableProperties();
    protected TransformCallback mTransformCallback;

}
