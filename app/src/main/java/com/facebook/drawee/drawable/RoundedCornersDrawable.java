// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.drawee.drawable;

import android.graphics.*;
import android.graphics.drawable.Drawable;
import com.facebook.common.internal.Preconditions;
import java.util.Arrays;

// Referenced classes of package com.facebook.drawee.drawable:
//            ForwardingDrawable, Rounded

public class RoundedCornersDrawable extends ForwardingDrawable
    implements Rounded
{
    public static enum Type
    {

        OVERLAY_COLOR("OVERLAY_COLOR", 0),
        CLIPPING("CLIPPING", 1);

        String sType;
        int iType;
        private Type(String s, int i)
        {
            sType = s;
            iType = i;
        }
    }


    public RoundedCornersDrawable(Drawable drawable)
    {
        super((Drawable)Preconditions.checkNotNull(drawable));
        mType = Type.OVERLAY_COLOR;
        mIsCircle = false;
        mBorderWidth = 0.0F;
        mBorderColor = 0;
        mOverlayColor = 0;
    }

    private void updatePath()
    {
        mPath.reset();
        mTempRectangle.set(getBounds());
        mTempRectangle.inset(mBorderWidth / 2.0F, mBorderWidth / 2.0F);
        if(mIsCircle)
            mPath.addCircle(mTempRectangle.centerX(), mTempRectangle.centerY(), Math.min(mTempRectangle.width(), mTempRectangle.height()) / 2.0F, android.graphics.Path.Direction.CW);
        else
            mPath.addRoundRect(mTempRectangle, mRadii, android.graphics.Path.Direction.CW);
        mTempRectangle.inset(-mBorderWidth / 2.0F, -mBorderWidth / 2.0F);
    }

    public void draw(Canvas canvas)
    {
        Rect rect = getBounds();

        switch(mType.ordinal()) {
            default:
                break;
            case 1:
                int i = canvas.save();
                mPath.setFillType(android.graphics.Path.FillType.EVEN_ODD);
                canvas.clipPath(mPath);
                super.draw(canvas);
                canvas.restoreToCount(i);
                break;
            case 2:
                super.draw(canvas);
                mPaint.setColor(mOverlayColor);
                mPaint.setStyle(android.graphics.Paint.Style.FILL);
                mPath.setFillType(android.graphics.Path.FillType.INVERSE_EVEN_ODD);
                canvas.drawPath(mPath, mPaint);
                if (mIsCircle) {
                    float f = ((float) (rect.width() - rect.height()) + mBorderWidth) / 2.0F;
                    float f1 = ((float) (rect.height() - rect.width()) + mBorderWidth) / 2.0F;
                    if (f > 0.0F) {
                        canvas.drawRect(rect.left, rect.top, (float) rect.left + f, rect.bottom, mPaint);
                        canvas.drawRect((float) rect.right - f, rect.top, rect.right, rect.bottom, mPaint);
                    }
                    if (f1 > 0.0F) {
                        canvas.drawRect(rect.left, rect.top, rect.right, (float) rect.top + f1, mPaint);
                        canvas.drawRect(rect.left, (float) rect.bottom - f1, rect.right, rect.bottom, mPaint);
                    }
                }
                break;
        }
        if (mBorderColor != 0) {
            mPaint.setStyle(android.graphics.Paint.Style.STROKE);
            mPaint.setColor(mBorderColor);
            mPaint.setStrokeWidth(mBorderWidth);
            mPath.setFillType(android.graphics.Path.FillType.EVEN_ODD);
            canvas.drawPath(mPath, mPaint);
        }
        return;

    }

    protected void onBoundsChange(Rect rect)
    {
        super.onBoundsChange(rect);
        updatePath();
    }

    public void setBorder(int i, float f)
    {
        mBorderColor = i;
        mBorderWidth = f;
        updatePath();
        invalidateSelf();
    }

    public void setCircle(boolean flag)
    {
        mIsCircle = flag;
        updatePath();
        invalidateSelf();
    }

    public void setOverlayColor(int i)
    {
        mOverlayColor = i;
        invalidateSelf();
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
        Arrays.fill(mRadii, f);
        updatePath();
        invalidateSelf();
    }

    public void setType(Type type)
    {
        mType = type;
        invalidateSelf();
    }

    int mBorderColor;
    float mBorderWidth;
    boolean mIsCircle;
    int mOverlayColor;
    final Paint mPaint = new Paint(1);
    private final Path mPath = new Path();
    final float mRadii[] = new float[8];
    private final RectF mTempRectangle = new RectF();
    Type mType;
}
