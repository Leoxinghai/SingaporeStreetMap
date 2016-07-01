

package com.facebook.drawee.drawable;

import android.graphics.*;
import android.graphics.drawable.Drawable;
import com.facebook.common.internal.Preconditions;

// Referenced classes of package com.facebook.drawee.drawable:
//            TransformCallback, TransformAwareDrawable, DrawableProperties, DrawableUtils

public class ArrayDrawable extends Drawable
    implements android.graphics.drawable.Drawable.Callback, TransformCallback, TransformAwareDrawable
{

    public ArrayDrawable(Drawable adrawable[])
    {
        mIsStateful = false;
        mIsStatefulCalculated = false;
        mIsMutated = false;
        Preconditions.checkNotNull(adrawable);
        mLayers = adrawable;
        for(int i = 0; i < mLayers.length; i++)
            DrawableUtils.setCallbacks(mLayers[i], this, this);

    }

    public void draw(Canvas canvas)
    {
        for(int i = 0; i < mLayers.length; i++)
        {
            Drawable drawable = mLayers[i];
            if(drawable != null)
                drawable.draw(canvas);
        }

    }

    public Drawable getDrawable(int i)
    {
        return mLayers[i];
    }

    public int getIntrinsicHeight()
    {
        int j = -1;
        for(int i = 0; i < mLayers.length;)
        {
            Drawable drawable = mLayers[i];
            int k = j;
            if(drawable != null)
                k = Math.max(j, drawable.getIntrinsicHeight());
            i++;
            j = k;
        }

        if(j > 0)
            return j;
        else
            return -1;
    }

    public int getIntrinsicWidth()
    {
        int j = -1;
        for(int i = 0; i < mLayers.length;)
        {
            Drawable drawable = mLayers[i];
            int k = j;
            if(drawable != null)
                k = Math.max(j, drawable.getIntrinsicWidth());
            i++;
            j = k;
        }

        if(j > 0)
            return j;
        else
            return -1;
    }

    public int getNumberOfLayers()
    {
        return mLayers.length;
    }

    public int getOpacity()
    {
        int k = 0;
        if(mLayers.length != 0) {
            int i = -1;
            int j = 1;
            k = i;
            for(;j < mLayers.length;) {
                Drawable drawable = mLayers[j];
                k = i;
                if(drawable != null)
                    k = Drawable.resolveOpacity(i, drawable.getOpacity());
                j++;
                i = k;
            }

        } else
            k = -2;

        return k;

    }

    public boolean getPadding(Rect rect)
    {
        rect.left = 0;
        rect.top = 0;
        rect.right = 0;
        rect.bottom = 0;
        Rect rect1 = mTmpRect;
        for(int i = 0; i < mLayers.length; i++)
        {
            Drawable drawable = mLayers[i];
            if(drawable != null)
            {
                drawable.getPadding(rect1);
                rect.left = Math.max(rect.left, rect1.left);
                rect.top = Math.max(rect.top, rect1.top);
                rect.right = Math.max(rect.right, rect1.right);
                rect.bottom = Math.max(rect.bottom, rect1.bottom);
            }
        }

        return true;
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

    public void invalidateDrawable(Drawable drawable)
    {
        invalidateSelf();
    }

    public boolean isStateful()
    {
        if(!mIsStatefulCalculated)
        {
            mIsStateful = false;
            int i = 0;
            while(i < mLayers.length)
            {
                Drawable drawable = mLayers[i];
                boolean flag1 = mIsStateful;
                boolean flag;
                if(drawable != null && drawable.isStateful())
                    flag = true;
                else
                    flag = false;
                mIsStateful = flag | flag1;
                i++;
            }
            mIsStatefulCalculated = true;
        }
        return mIsStateful;
    }

    public Drawable mutate()
    {
        for(int i = 0; i < mLayers.length; i++)
        {
            Drawable drawable = mLayers[i];
            if(drawable != null)
                drawable.mutate();
        }

        mIsMutated = true;
        return this;
    }

    protected void onBoundsChange(Rect rect)
    {
        mBounds.set(rect);
        for(int i = 0; i < mLayers.length; i++)
        {
            Drawable drawable = mLayers[i];
            if(drawable != null)
                drawable.setBounds(rect);
        }

    }

    protected boolean onLevelChange(int i)
    {
        mLevel = i;
        boolean flag = false;
        for(int j = 0; j < mLayers.length;)
        {
            Drawable drawable = mLayers[j];
            boolean flag1 = flag;
            if(drawable != null)
            {
                flag1 = flag;
                if(drawable.setLevel(i))
                    flag1 = true;
            }
            j++;
            flag = flag1;
        }

        return flag;
    }

    protected boolean onStateChange(int ai[])
    {
        mState = ai;
        boolean flag = false;
        for(int i = 0; i < mLayers.length;)
        {
            Drawable drawable = mLayers[i];
            boolean flag1 = flag;
            if(drawable != null)
            {
                flag1 = flag;
                if(drawable.setState(ai))
                    flag1 = true;
            }
            i++;
            flag = flag1;
        }

        return flag;
    }

    public void scheduleDrawable(Drawable drawable, Runnable runnable, long l)
    {
        scheduleSelf(runnable, l);
    }

    public void setAlpha(int i)
    {
        mDrawableProperties.setAlpha(i);
        for(int j = 0; j < mLayers.length; j++)
        {
            Drawable drawable = mLayers[j];
            if(drawable != null)
                drawable.setAlpha(i);
        }

    }

    public void setColorFilter(ColorFilter colorfilter)
    {
        mDrawableProperties.setColorFilter(colorfilter);
        for(int i = 0; i < mLayers.length; i++)
        {
            Drawable drawable = mLayers[i];
            if(drawable != null)
                drawable.setColorFilter(colorfilter);
        }

    }

    public void setDither(boolean flag)
    {
        mDrawableProperties.setDither(flag);
        for(int i = 0; i < mLayers.length; i++)
        {
            Drawable drawable = mLayers[i];
            if(drawable != null)
                drawable.setDither(flag);
        }

    }

    public void setDrawable(int i, Drawable drawable)
    {
        boolean flag1 = true;
        boolean flag;
        if(i >= 0)
            flag = true;
        else
            flag = false;
        Preconditions.checkArgument(flag);
        if(i < mLayers.length)
            flag = flag1;
        else
            flag = false;
        Preconditions.checkArgument(flag);
        if(drawable != mLayers[i])
        {
            Drawable drawable1 = drawable;
            if(drawable != null)
            {
                drawable1 = drawable;
                if(mIsMutated)
                    drawable1 = drawable.mutate();
            }
            DrawableUtils.setCallbacks(mLayers[i], null, null);
            DrawableUtils.setCallbacks(drawable1, null, null);
            DrawableUtils.setDrawableProperties(drawable1, mDrawableProperties);
            if(drawable1 != null)
            {
                drawable1.setBounds(mBounds);
                drawable1.setLevel(mLevel);
                drawable1.setState(mState);
                drawable1.setVisible(mIsVisible, false);
            }
            DrawableUtils.setCallbacks(drawable1, this, this);
            mIsStatefulCalculated = false;
            mLayers[i] = drawable1;
            invalidateSelf();
        }
    }

    public void setFilterBitmap(boolean flag)
    {
        mDrawableProperties.setFilterBitmap(flag);
        for(int i = 0; i < mLayers.length; i++)
        {
            Drawable drawable = mLayers[i];
            if(drawable != null)
                drawable.setFilterBitmap(flag);
        }

    }

    public void setTransformCallback(TransformCallback transformcallback)
    {
        mTransformCallback = transformcallback;
    }

    public boolean setVisible(boolean flag, boolean flag1)
    {
        mIsVisible = flag;
        boolean flag2 = super.setVisible(flag, flag1);
        for(int i = 0; i < mLayers.length; i++)
        {
            Drawable drawable = mLayers[i];
            if(drawable != null)
                drawable.setVisible(flag, flag1);
        }

        return flag2;
    }

    public void unscheduleDrawable(Drawable drawable, Runnable runnable)
    {
        unscheduleSelf(runnable);
    }

    private final Rect mBounds = new Rect();
    private final DrawableProperties mDrawableProperties = new DrawableProperties();
    private boolean mIsMutated;
    private boolean mIsStateful;
    private boolean mIsStatefulCalculated;
    private boolean mIsVisible;
    private final Drawable mLayers[];
    private int mLevel;
    private int mState[];
    private final Rect mTmpRect = new Rect();
    private TransformCallback mTransformCallback;
}
