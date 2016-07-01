// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.drawee.drawable;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import com.facebook.common.internal.Preconditions;
import java.util.Arrays;

// Referenced classes of package com.facebook.drawee.drawable:
//            ArrayDrawable

public class FadeDrawable extends ArrayDrawable
{

    public FadeDrawable(Drawable adrawable[])
    {
        super(adrawable);
        boolean flag = true;
        if(adrawable.length < 1)
            flag = false;
        Preconditions.checkState(flag, "At least one layer required!");
        mLayers = adrawable;
        mStartAlphas = new int[adrawable.length];
        mAlphas = new int[adrawable.length];
        mAlpha = 255;
        mIsLayerOn = new boolean[adrawable.length];
        mPreventInvalidateCount = 0;
        resetInternal();
    }

    private void drawDrawableWithAlpha(Canvas canvas, Drawable drawable, int i)
    {
        if(drawable != null && i > 0)
        {
            mPreventInvalidateCount = mPreventInvalidateCount + 1;
            drawable.mutate().setAlpha(i);
            mPreventInvalidateCount = mPreventInvalidateCount - 1;
            drawable.draw(canvas);
        }
    }

    private void resetInternal()
    {
        mTransitionState = 2;
        Arrays.fill(mStartAlphas, 0);
        mStartAlphas[0] = 255;
        Arrays.fill(mAlphas, 0);
        mAlphas[0] = 255;
        Arrays.fill(mIsLayerOn, false);
        mIsLayerOn[0] = true;
    }

    private boolean updateAlphas(float f)
    {
        boolean flag1 = true;
        int i = 0;
        while(i < mLayers.length)
        {
            int j;
            boolean flag;
            if(mIsLayerOn[i])
                j = 1;
            else
                j = -1;
            mAlphas[i] = (int)((float)mStartAlphas[i] + (float)(j * 255) * f);
            if(mAlphas[i] < 0)
                mAlphas[i] = 0;
            if(mAlphas[i] > 255)
                mAlphas[i] = 255;
            flag = flag1;
            if(mIsLayerOn[i])
            {
                flag = flag1;
                if(mAlphas[i] < 255)
                    flag = false;
            }
            flag1 = flag;
            if(mIsLayerOn[i])
                continue;
            flag1 = flag;
            if(mAlphas[i] > 0)
                flag1 = false;
            i++;
        }
        return flag1;
    }

    public void beginBatchMode()
    {
        mPreventInvalidateCount = mPreventInvalidateCount + 1;
    }

    public void draw(Canvas canvas)
    {
        int i;
        boolean flag;
        boolean flag1;
        i = 2;
        flag = false;
        flag1 = true;
        switch(mTransitionState) {
            default:
                flag = flag1;
                break;
            case 0:
                System.arraycopy(mAlphas, 0, mStartAlphas, 0, mLayers.length);
                mStartTimeMs = getCurrentTimeMs();
                float f;
                if (mDurationMs == 0)
                    f = 1.0F;
                else
                    f = 0.0F;
                flag = updateAlphas(f);
                if (!flag)
                    i = 1;
                mTransitionState = i;
                break;
            case 1:
                if (mDurationMs > 0)
                    flag = true;
                Preconditions.checkState(flag);
                flag = updateAlphas((float) (getCurrentTimeMs() - mStartTimeMs) / (float) mDurationMs);
                if (!flag)
                    i = 1;
                mTransitionState = i;
                break;
            case 2:
                flag = true;
                break;
        }
        for (i = 0; i < mLayers.length; i++)
            drawDrawableWithAlpha(canvas, mLayers[i], (mAlphas[i] * mAlpha) / 255);

        if(!flag)
            invalidateSelf();
        return;
    }

    public void endBatchMode()
    {
        mPreventInvalidateCount = mPreventInvalidateCount - 1;
        invalidateSelf();
    }

    public void fadeInAllLayers()
    {
        mTransitionState = 0;
        Arrays.fill(mIsLayerOn, true);
        invalidateSelf();
    }

    public void fadeInLayer(int i)
    {
        mTransitionState = 0;
        mIsLayerOn[i] = true;
        invalidateSelf();
    }

    public void fadeOutAllLayers()
    {
        mTransitionState = 0;
        Arrays.fill(mIsLayerOn, false);
        invalidateSelf();
    }

    public void fadeOutLayer(int i)
    {
        mTransitionState = 0;
        mIsLayerOn[i] = false;
        invalidateSelf();
    }

    public void fadeToLayer(int i)
    {
        mTransitionState = 0;
        Arrays.fill(mIsLayerOn, false);
        mIsLayerOn[i] = true;
        invalidateSelf();
    }

    public void fadeUpToLayer(int i)
    {
        mTransitionState = 0;
        Arrays.fill(mIsLayerOn, 0, i + 1, true);
        Arrays.fill(mIsLayerOn, i + 1, mLayers.length, false);
        invalidateSelf();
    }

    public void finishTransitionImmediately()
    {
        mTransitionState = 2;
        int i = 0;
        while(i < mLayers.length)
        {
            int ai[] = mAlphas;
            char c;
            if(mIsLayerOn[i])
                c = '\377';
            else
                c = '\0';
            ai[i] = c;
            i++;
        }
        invalidateSelf();
    }

    public int getAlpha()
    {
        return mAlpha;
    }

    protected long getCurrentTimeMs()
    {
        return SystemClock.uptimeMillis();
    }

    public int getTransitionDuration()
    {
        return mDurationMs;
    }

    public int getTransitionState()
    {
        return mTransitionState;
    }

    public void invalidateSelf()
    {
        if(mPreventInvalidateCount == 0)
            super.invalidateSelf();
    }

    public boolean isLayerOn(int i)
    {
        return mIsLayerOn[i];
    }

    public void reset()
    {
        resetInternal();
        invalidateSelf();
    }

    public void setAlpha(int i)
    {
        if(mAlpha != i)
        {
            mAlpha = i;
            invalidateSelf();
        }
    }

    public void setTransitionDuration(int i)
    {
        mDurationMs = i;
        if(mTransitionState == 1)
            mTransitionState = 0;
    }

    public static final int TRANSITION_NONE = 2;
    public static final int TRANSITION_RUNNING = 1;
    public static final int TRANSITION_STARTING = 0;
    int mAlpha;
    int mAlphas[];
    int mDurationMs;
    boolean mIsLayerOn[];
    private final Drawable mLayers[];
    int mPreventInvalidateCount;
    int mStartAlphas[];
    long mStartTimeMs;
    int mTransitionState;
}
