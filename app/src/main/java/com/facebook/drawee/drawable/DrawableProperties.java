

package com.facebook.drawee.drawable;

import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;

public class DrawableProperties
{

    public DrawableProperties()
    {
        mAlpha = -1;
        mIsSetColorFilter = false;
        mColorFilter = null;
        mDither = -1;
        mFilterBitmap = -1;
    }

    public void applyTo(Drawable drawable)
    {
        boolean flag1 = true;
        if(drawable != null)
        {
            if(mAlpha != -1)
                drawable.setAlpha(mAlpha);
            if(mIsSetColorFilter)
                drawable.setColorFilter(mColorFilter);
            boolean flag;
            if(mDither != -1)
            {
                if(mDither != 0)
                    flag = true;
                else
                    flag = false;
                drawable.setDither(flag);
            }
            if(mFilterBitmap != -1)
            {
                if(mFilterBitmap != 0)
                    flag = flag1;
                else
                    flag = false;
                drawable.setFilterBitmap(flag);
                return;
            }
        }
    }

    public void setAlpha(int i)
    {
        mAlpha = i;
    }

    public void setColorFilter(ColorFilter colorfilter)
    {
        mColorFilter = colorfilter;
        mIsSetColorFilter = true;
    }

    public void setDither(boolean flag)
    {
        int i;
        if(flag)
            i = 1;
        else
            i = 0;
        mDither = i;
    }

    public void setFilterBitmap(boolean flag)
    {
        int i;
        if(flag)
            i = 1;
        else
            i = 0;
        mFilterBitmap = i;
    }

    private static final int UNSET = -1;
    private int mAlpha;
    private ColorFilter mColorFilter;
    private int mDither;
    private int mFilterBitmap;
    private boolean mIsSetColorFilter;
}
