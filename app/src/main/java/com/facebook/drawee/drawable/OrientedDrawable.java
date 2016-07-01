

package com.facebook.drawee.drawable;

import android.graphics.*;
import android.graphics.drawable.Drawable;
import com.facebook.common.internal.Preconditions;

// Referenced classes of package com.facebook.drawee.drawable:
//            ForwardingDrawable

public class OrientedDrawable extends ForwardingDrawable
{

    public OrientedDrawable(Drawable drawable, int i)
    {
        super(drawable);
        boolean flag;
        if(i % 90 == 0)
            flag = true;
        else
            flag = false;
        Preconditions.checkArgument(flag);
        mRotationAngle = i;
    }

    public void draw(Canvas canvas)
    {
        if(mRotationAngle <= 0)
        {
            super.draw(canvas);
            return;
        } else
        {
            int i = canvas.save();
            canvas.concat(mRotationMatrix);
            super.draw(canvas);
            canvas.restoreToCount(i);
            return;
        }
    }

    public int getIntrinsicHeight()
    {
        if(mRotationAngle % 180 == 0)
            return super.getIntrinsicHeight();
        else
            return super.getIntrinsicWidth();
    }

    public int getIntrinsicWidth()
    {
        if(mRotationAngle % 180 == 0)
            return super.getIntrinsicWidth();
        else
            return super.getIntrinsicHeight();
    }

    public void getTransform(Matrix matrix)
    {
        getParentTransform(matrix);
        if(!mRotationMatrix.isIdentity())
            matrix.preConcat(mRotationMatrix);
    }

    protected void onBoundsChange(Rect rect)
    {
        Drawable drawable = getCurrent();
        if(mRotationAngle > 0)
        {
            mRotationMatrix.setRotate(mRotationAngle, rect.centerX(), rect.centerY());
            mTempMatrix.reset();
            mRotationMatrix.invert(mTempMatrix);
            mTempRectF.set(rect);
            mTempMatrix.mapRect(mTempRectF);
            drawable.setBounds((int)mTempRectF.left, (int)mTempRectF.top, (int)mTempRectF.right, (int)mTempRectF.bottom);
            return;
        } else
        {
            drawable.setBounds(rect);
            return;
        }
    }

    private int mRotationAngle;
    final Matrix mRotationMatrix = new Matrix();
    private final Matrix mTempMatrix = new Matrix();
    private final RectF mTempRectF = new RectF();
}
