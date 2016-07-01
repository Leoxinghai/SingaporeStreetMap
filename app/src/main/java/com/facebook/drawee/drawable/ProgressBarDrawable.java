

package com.facebook.drawee.drawable;

import android.graphics.*;
import android.graphics.drawable.Drawable;

// Referenced classes of package com.facebook.drawee.drawable:
//            DrawableUtils

public class ProgressBarDrawable extends Drawable
{

    public ProgressBarDrawable()
    {
        mBackgroundColor = 0x80000000;
        mColor = 0x800080ff;
        mPadding = 10;
        mBarWidth = 20;
        mLevel = 0;
        mHideWhenZero = false;
    }

    private void drawBar(Canvas canvas, int i, int j)
    {
        Rect rect = getBounds();
        i = ((rect.width() - mPadding * 2) * i) / 10000;
        int k = rect.left + mPadding;
        int l = rect.bottom - mPadding - mBarWidth;
        mPaint.setColor(j);
        canvas.drawRect(k, l, k + i, mBarWidth + l, mPaint);
    }

    public void draw(Canvas canvas)
    {
        if(mHideWhenZero && mLevel == 0)
        {
            return;
        } else
        {
            drawBar(canvas, 10000, mBackgroundColor);
            drawBar(canvas, mLevel, mColor);
            return;
        }
    }

    public int getBackgroundColor()
    {
        return mBackgroundColor;
    }

    public int getBarWidth()
    {
        return mBarWidth;
    }

    public int getColor()
    {
        return mColor;
    }

    public boolean getHideWhenZero()
    {
        return mHideWhenZero;
    }

    public int getOpacity()
    {
        return DrawableUtils.getOpacityFromColor(mPaint.getColor());
    }

    public boolean getPadding(Rect rect)
    {
        rect.set(mPadding, mPadding, mPadding, mPadding);
        return mPadding != 0;
    }

    protected boolean onLevelChange(int i)
    {
        mLevel = i;
        invalidateSelf();
        return true;
    }

    public void setAlpha(int i)
    {
        mPaint.setAlpha(i);
    }

    public void setBackgroundColor(int i)
    {
        if(mBackgroundColor != i)
        {
            mBackgroundColor = i;
            invalidateSelf();
        }
    }

    public void setBarWidth(int i)
    {
        if(mBarWidth != i)
        {
            mBarWidth = i;
            invalidateSelf();
        }
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
        mPaint.setColorFilter(colorfilter);
    }

    public void setHideWhenZero(boolean flag)
    {
        mHideWhenZero = flag;
    }

    public void setPadding(int i)
    {
        if(mPadding != i)
        {
            mPadding = i;
            invalidateSelf();
        }
    }

    private int mBackgroundColor;
    private int mBarWidth;
    private int mColor;
    private boolean mHideWhenZero;
    private int mLevel;
    private int mPadding;
    private final Paint mPaint = new Paint(1);
}
