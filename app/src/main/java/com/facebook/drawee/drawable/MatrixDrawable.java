

package com.facebook.drawee.drawable;

import android.graphics.*;
import android.graphics.drawable.Drawable;
import com.facebook.common.internal.Preconditions;

// Referenced classes of package com.facebook.drawee.drawable:
//            ForwardingDrawable

public class MatrixDrawable extends ForwardingDrawable
{

    public MatrixDrawable(Drawable drawable, Matrix matrix)
    {
        super((Drawable)Preconditions.checkNotNull(drawable));
        mUnderlyingWidth = 0;
        mUnderlyingHeight = 0;
        mMatrix = matrix;
    }

    private void configureBounds()
    {
        Drawable drawable = getCurrent();
        Rect rect = getBounds();
        int i = drawable.getIntrinsicWidth();
        mUnderlyingWidth = i;
        int j = drawable.getIntrinsicHeight();
        mUnderlyingHeight = j;
        if(i <= 0 || j <= 0)
        {
            drawable.setBounds(rect);
            mDrawMatrix = null;
            return;
        } else
        {
            drawable.setBounds(0, 0, i, j);
            mDrawMatrix = mMatrix;
            return;
        }
    }

    private void configureBoundsIfUnderlyingChanged()
    {
        if(mUnderlyingWidth != getCurrent().getIntrinsicWidth() || mUnderlyingHeight != getCurrent().getIntrinsicHeight())
            configureBounds();
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

    public Matrix getMatrix()
    {
        return mMatrix;
    }

    public void getTransform(Matrix matrix)
    {
        super.getTransform(matrix);
        if(mDrawMatrix != null)
            matrix.preConcat(mDrawMatrix);
    }

    protected void onBoundsChange(Rect rect)
    {
        super.onBoundsChange(rect);
        configureBounds();
    }

    public void setMatrix(Matrix matrix)
    {
        mMatrix = matrix;
        configureBounds();
        invalidateSelf();
    }

    private Matrix mDrawMatrix;
    private Matrix mMatrix;
    private int mUnderlyingHeight;
    private int mUnderlyingWidth;
}
