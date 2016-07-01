

package com.facebook.drawee.drawable;

import android.graphics.*;
import android.graphics.drawable.Drawable;
import com.facebook.common.internal.Preconditions;

// Referenced classes of package com.facebook.drawee.drawable:
//            ForwardingDrawable, ScalingUtils

public class ScaleTypeDrawable extends ForwardingDrawable
{

    public ScaleTypeDrawable(Drawable drawable, ScalingUtils.ScaleType scaletype)
    {
        super((Drawable)Preconditions.checkNotNull(drawable));
        mFocusPoint = null;
        mUnderlyingWidth = 0;
        mUnderlyingHeight = 0;
        mTempMatrix = new Matrix();
        mScaleType = scaletype;
    }

    private void configureBoundsIfUnderlyingChanged()
    {
        if(mUnderlyingWidth != getCurrent().getIntrinsicWidth() || mUnderlyingHeight != getCurrent().getIntrinsicHeight())
            configureBounds();
    }

    void configureBounds()
    {
        float f1 = 0.5F;
        Object obj = getCurrent();
        Rect rect = getBounds();
        int i = rect.width();
        int j = rect.height();
        int k = ((Drawable) (obj)).getIntrinsicWidth();
        mUnderlyingWidth = k;
        int l = ((Drawable) (obj)).getIntrinsicHeight();
        mUnderlyingHeight = l;
        if(k <= 0 || l <= 0)
        {
            ((Drawable) (obj)).setBounds(rect);
            mDrawMatrix = null;
            return;
        }
        if(k == i && l == j)
        {
            ((Drawable) (obj)).setBounds(rect);
            mDrawMatrix = null;
            return;
        }
        if(mScaleType == ScalingUtils.ScaleType.FIT_XY)
        {
            ((Drawable) (obj)).setBounds(rect);
            mDrawMatrix = null;
            return;
        }
        ((Drawable) (obj)).setBounds(0, 0, k, l);
        obj = mTempMatrix;
        float f;
        if(mFocusPoint != null)
            f = mFocusPoint.x;
        else
            f = 0.5F;
        if(mFocusPoint != null)
            f1 = mFocusPoint.y;
        ScalingUtils.getTransform(((Matrix) (obj)), rect, k, l, f, f1, mScaleType);
        mDrawMatrix = mTempMatrix;
    }

    public void draw(Canvas canvas)
    {
        configureBoundsIfUnderlyingChanged();
        if(mDrawMatrix != null)
        {
            int i = canvas.save();
            canvas.clipRect(getBounds());
            canvas.concat(mDrawMatrix);
            super.draw(canvas);
            canvas.restoreToCount(i);
            return;
        } else
        {
            super.draw(canvas);
            return;
        }
    }

    public PointF getFocusPoint()
    {
        return mFocusPoint;
    }

    public ScalingUtils.ScaleType getScaleType()
    {
        return mScaleType;
    }

    public void getTransform(Matrix matrix)
    {
        getParentTransform(matrix);
        configureBoundsIfUnderlyingChanged();
        if(mDrawMatrix != null)
            matrix.preConcat(mDrawMatrix);
    }

    protected void onBoundsChange(Rect rect)
    {
        configureBounds();
    }

    public void setFocusPoint(PointF pointf)
    {
        if(mFocusPoint == null)
            mFocusPoint = new PointF();
        mFocusPoint.set(pointf);
        configureBounds();
        invalidateSelf();
    }

    public void setScaleType(ScalingUtils.ScaleType scaletype)
    {
        mScaleType = scaletype;
        configureBounds();
        invalidateSelf();
    }

    Matrix mDrawMatrix;
    PointF mFocusPoint;
    ScalingUtils.ScaleType mScaleType;
    private Matrix mTempMatrix;
    int mUnderlyingHeight;
    int mUnderlyingWidth;
}
