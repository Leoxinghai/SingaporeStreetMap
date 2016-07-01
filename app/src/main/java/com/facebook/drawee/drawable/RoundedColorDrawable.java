

package com.facebook.drawee.drawable;

import android.graphics.*;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import com.facebook.common.internal.Preconditions;
import java.util.Arrays;

// Referenced classes of package com.facebook.drawee.drawable:
//            Rounded, DrawableUtils

public class RoundedColorDrawable extends Drawable
    implements Rounded
{

    public RoundedColorDrawable(float f, int i)
    {
        this(i);
        setRadius(f);
    }

    public RoundedColorDrawable(int i)
    {
        mRadii = new float[8];
        mPaint = new Paint(1);
        mIsCircle = false;
        mBorderWidth = 0.0F;
        mBorderColor = 0;
        mPath = new Path();
        mColor = 0;
        mTempRect = new RectF();
        mAlpha = 255;
        setColor(i);
    }

    public RoundedColorDrawable(float af[], int i)
    {
        this(i);
        setRadii(af);
    }

    public static RoundedColorDrawable fromColorDrawable(ColorDrawable colordrawable)
    {
        return new RoundedColorDrawable(colordrawable.getColor());
    }

    private void updatePath()
    {
        mPath.reset();
        mTempRect.set(getBounds());
        mTempRect.inset(mBorderWidth / 2.0F, mBorderWidth / 2.0F);
        if(mIsCircle)
        {
            float f = Math.min(mTempRect.width(), mTempRect.height()) / 2.0F;
            mPath.addCircle(mTempRect.centerX(), mTempRect.centerY(), f, android.graphics.Path.Direction.CW);
        } else
        {
            mPath.addRoundRect(mTempRect, mRadii, android.graphics.Path.Direction.CW);
        }
        mTempRect.inset(-mBorderWidth / 2.0F, -mBorderWidth / 2.0F);
    }

    public void draw(Canvas canvas)
    {
        mPaint.setColor(DrawableUtils.multiplyColorAlpha(mColor, mAlpha));
        mPaint.setStyle(android.graphics.Paint.Style.FILL);
        canvas.drawPath(mPath, mPaint);
        if(mBorderWidth != 0.0F)
        {
            mPaint.setColor(DrawableUtils.multiplyColorAlpha(mBorderColor, mAlpha));
            mPaint.setStyle(android.graphics.Paint.Style.STROKE);
            mPaint.setStrokeWidth(mBorderWidth);
            canvas.drawPath(mPath, mPaint);
        }
    }

    public int getAlpha()
    {
        return mAlpha;
    }

    public int getColor()
    {
        return mColor;
    }

    public int getOpacity()
    {
        return DrawableUtils.getOpacityFromColor(DrawableUtils.multiplyColorAlpha(mColor, mAlpha));
    }

    protected void onBoundsChange(Rect rect)
    {
        super.onBoundsChange(rect);
        updatePath();
    }

    public void setAlpha(int i)
    {
        if(i != mAlpha)
        {
            mAlpha = i;
            invalidateSelf();
        }
    }

    public void setBorder(int i, float f)
    {
        if(mBorderColor != i)
        {
            mBorderColor = i;
            invalidateSelf();
        }
        if(mBorderWidth != f)
        {
            mBorderWidth = f;
            updatePath();
            invalidateSelf();
        }
    }

    public void setCircle(boolean flag)
    {
        mIsCircle = flag;
        updatePath();
        invalidateSelf();
    }

    public void setColor(int i)
    {
        if(mColor != i)
        {
            mColor = i;
            invalidateSelf();
        }
    }

    public void setColorFilter(ColorFilter colorfilter)
    {
    }

    public void setRadii(float af[])
    {
        if(af == null)
        {
            Arrays.fill(mRadii, 0.0F);
        } else
        {
            boolean flag;
            if(af.length == 8)
                flag = true;
            else
                flag = false;
            Preconditions.checkArgument(flag, "radii should have exactly 8 values");
            System.arraycopy(af, 0, mRadii, 0, 8);
        }
        updatePath();
        invalidateSelf();
    }

    public void setRadius(float f)
    {
        boolean flag;
        if(f >= 0.0F)
            flag = true;
        else
            flag = false;
        Preconditions.checkArgument(flag, "radius should be non negative");
        Arrays.fill(mRadii, f);
        updatePath();
        invalidateSelf();
    }

    private int mAlpha;
    int mBorderColor;
    float mBorderWidth;
    private int mColor;
    boolean mIsCircle;
    final Paint mPaint;
    final Path mPath;
    final float mRadii[];
    private final RectF mTempRect;
}
