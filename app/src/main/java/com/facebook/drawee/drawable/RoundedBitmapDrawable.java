

package com.facebook.drawee.drawable;

import android.content.res.Resources;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import com.facebook.common.internal.Preconditions;
import java.lang.ref.WeakReference;
import java.util.Arrays;

// Referenced classes of package com.facebook.drawee.drawable:
//            TransformAwareDrawable, Rounded, TransformCallback, DrawableUtils

public class RoundedBitmapDrawable extends BitmapDrawable
    implements TransformAwareDrawable, Rounded
{

    public RoundedBitmapDrawable(Resources resources, Bitmap bitmap)
    {
        super(resources, bitmap);
        mIsCircle = false;
        mCornerRadii = new float[8];
        mRootBounds = new RectF();
        mBorderWidth = 0.0F;
        mBorderColor = 0;
        mIsNonzero = true;
        mIsPathDirty = true;
        mIsShaderTransformDirty = true;
        mBorderPaint.setStyle(android.graphics.Paint.Style.STROKE);
    }

    public static RoundedBitmapDrawable fromBitmapDrawable(Resources resources, BitmapDrawable bitmapdrawable)
    {
        return new RoundedBitmapDrawable(resources, bitmapdrawable.getBitmap());
    }

    private void updateNonzero()
    {
        if(mIsPathDirty)
        {
            mIsNonzero = false;
            if(mIsCircle || mBorderWidth > 0.0F)
                mIsNonzero = true;
            for(int i = 0; i < mCornerRadii.length; i++)
                if(mCornerRadii[i] > 0.0F)
                    mIsNonzero = true;

        }
    }

    private void updatePaint()
    {
        Bitmap bitmap = getBitmap();
        if(mLastBitmap == null || mLastBitmap.get() != bitmap)
        {
            mLastBitmap = new WeakReference(bitmap);
            mPaint.setShader(new BitmapShader(bitmap, android.graphics.Shader.TileMode.CLAMP, android.graphics.Shader.TileMode.CLAMP));
            mIsShaderTransformDirty = true;
        }
        if(mIsShaderTransformDirty)
        {
            mPaint.getShader().setLocalMatrix(mTransform);
            mIsShaderTransformDirty = false;
        }
    }

    private void updatePath()
    {
        if(mIsPathDirty)
        {
            mPath.reset();
            mRootBounds.inset(mBorderWidth / 2.0F, mBorderWidth / 2.0F);
            if(mIsCircle)
                mPath.addCircle(mRootBounds.centerX(), mRootBounds.centerY(), Math.min(mRootBounds.width(), mRootBounds.height()) / 2.0F, android.graphics.Path.Direction.CW);
            else
                mPath.addRoundRect(mRootBounds, mCornerRadii, android.graphics.Path.Direction.CW);
            mRootBounds.inset(-(mBorderWidth / 2.0F), -(mBorderWidth / 2.0F));
            mPath.setFillType(android.graphics.Path.FillType.WINDING);
            mIsPathDirty = false;
        }
    }

    private void updateTransform()
    {
        if(mTransformCallback != null)
        {
            mTransformCallback.getTransform(mTransform);
            mTransformCallback.getRootBounds(mRootBounds);
        } else
        {
            mTransform.reset();
            mRootBounds.set(getBounds());
        }
        if(!mTransform.equals(mLastTransform))
        {
            mIsShaderTransformDirty = true;
            if(!mTransform.invert(mInverseTransform))
            {
                mInverseTransform.reset();
                mTransform.reset();
            }
            mLastTransform.set(mTransform);
        }
        if(!mRootBounds.equals(mLastRootBounds))
        {
            mIsPathDirty = true;
            mLastRootBounds.set(mRootBounds);
        }
    }

    public void draw(Canvas canvas)
    {
        updateTransform();
        updateNonzero();
        if(!mIsNonzero)
        {
            super.draw(canvas);
            return;
        }
        updatePath();
        updatePaint();
        int i = canvas.save();
        canvas.concat(mInverseTransform);
        canvas.drawPath(mPath, mPaint);
        if(mBorderWidth != 0.0F)
        {
            mBorderPaint.setStrokeWidth(mBorderWidth);
            mBorderPaint.setColor(DrawableUtils.multiplyColorAlpha(mBorderColor, mPaint.getAlpha()));
            canvas.drawPath(mPath, mBorderPaint);
        }
        canvas.restoreToCount(i);
    }

    public void setAlpha(int i)
    {
        if(i != mPaint.getAlpha())
        {
            mPaint.setAlpha(i);
            invalidateSelf();
        }
    }

    public void setBorder(int i, float f)
    {
        if(mBorderColor != i || mBorderWidth != f)
        {
            mBorderColor = i;
            mBorderWidth = f;
            mIsPathDirty = true;
            invalidateSelf();
        }
    }

    public void setCircle(boolean flag)
    {
        mIsCircle = flag;
        mIsPathDirty = true;
        invalidateSelf();
    }

    public void setColorFilter(ColorFilter colorfilter)
    {
        mPaint.setColorFilter(colorfilter);
        super.setColorFilter(colorfilter);
    }

    public void setRadii(float af[])
    {
        if(af == null)
        {
            Arrays.fill(mCornerRadii, 0.0F);
        } else
        {
            boolean flag;
            if(af.length == 8)
                flag = true;
            else
                flag = false;
            Preconditions.checkArgument(flag, "radii should have exactly 8 values");
            System.arraycopy(af, 0, mCornerRadii, 0, 8);
        }
        mIsPathDirty = true;
        invalidateSelf();
    }

    public void setRadius(float f)
    {
        boolean flag;
        if(f >= 0.0F)
            flag = true;
        else
            flag = false;
        Preconditions.checkState(flag);
        Arrays.fill(mCornerRadii, f);
        mIsPathDirty = true;
        invalidateSelf();
    }

    public void setTransformCallback(TransformCallback transformcallback)
    {
        mTransformCallback = transformcallback;
    }

    int mBorderColor;
    private final Paint mBorderPaint = new Paint(1);
    float mBorderWidth;
    float mCornerRadii[];
    final Matrix mInverseTransform = new Matrix();
    boolean mIsCircle;
    boolean mIsNonzero;
    private boolean mIsPathDirty;
    private boolean mIsShaderTransformDirty;
    private WeakReference mLastBitmap;
    final RectF mLastRootBounds = new RectF();
    final Matrix mLastTransform = new Matrix();
    private final Paint mPaint = new Paint(1);
    private final Path mPath = new Path();
    RectF mRootBounds;
    final Matrix mTransform = new Matrix();
    private TransformCallback mTransformCallback;
}
