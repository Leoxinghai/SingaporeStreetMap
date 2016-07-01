// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.animated.impl;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.SystemClock;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import com.facebook.common.logging.FLog;
import com.facebook.imagepipeline.animated.base.AnimatedDrawableCachingBackend;
import com.facebook.imagepipeline.animated.base.AnimatedDrawableDiagnostics;
import com.facebook.imagepipeline.animated.util.AnimatedDrawableUtil;

// Referenced classes of package com.facebook.imagepipeline.animated.impl:
//            RollingStat

public class AnimatedDrawableDiagnosticsImpl
    implements AnimatedDrawableDiagnostics
{

    public AnimatedDrawableDiagnosticsImpl(AnimatedDrawableUtil animateddrawableutil, DisplayMetrics displaymetrics)
    {
        mAnimatedDrawableUtil = animateddrawableutil;
        mDisplayMetrics = displaymetrics;
        mDebugTextPaint.setColor(0xff0000ff);
        mDebugTextPaint.setTextSize(convertDpToPx(14));
    }

    private int convertDpToPx(int i)
    {
        return (int)TypedValue.applyDimension(1, i, mDisplayMetrics);
    }

    public void drawDebugOverlay(Canvas canvas, Rect rect)
    {
        int i = mDroppedFramesStat.getSum(10);
        int l = mDrawnFrames.getSum(10);
        int k1 = l + i;
        i = convertDpToPx(10);
        int j = i;
        int i1 = convertDpToPx(20);
        int j1 = convertDpToPx(5);
        int k = j;
        if(k1 > 0)
        {
            k = (l * 100) / k1;
            sbTemp.setLength(0);
            sbTemp.append(k);
            sbTemp.append("%");
            canvas.drawText(sbTemp, 0, sbTemp.length(), j, i1, mDebugTextPaint);
            k = (int)((float)j + mDebugTextPaint.measureText(sbTemp, 0, sbTemp.length())) + j1;
        }
        j = mAnimatedDrawableBackend.getMemoryUsage();
        sbTemp.setLength(0);
        mAnimatedDrawableUtil.appendMemoryString(sbTemp, j);
        float f = mDebugTextPaint.measureText(sbTemp, 0, sbTemp.length());
        l = k;
        j = i1;
        if((float)k + f > (float)rect.width())
        {
            k = i;
            j = (int)((float)i1 + (mDebugTextPaint.getTextSize() + (float)j1));
            l = k;
        }
        canvas.drawText(sbTemp, 0, sbTemp.length(), l, j, mDebugTextPaint);
        i1 = (int)((float)l + f) + j1;
        sbTemp.setLength(0);
        mAnimatedDrawableBackend.appendDebugOptionString(sbTemp);
        f = mDebugTextPaint.measureText(sbTemp, 0, sbTemp.length());
        l = i1;
        k = j;
        if((float)i1 + f > (float)rect.width())
        {
            k = (int)((float)j + (mDebugTextPaint.getTextSize() + (float)j1));
            l = i;
        }
        canvas.drawText(sbTemp, 0, sbTemp.length(), l, k, mDebugTextPaint);
    }

    public void incrementDrawnFrames(int i)
    {
        mDrawnFrames.incrementStats(i);
    }

    public void incrementDroppedFrames(int i)
    {
        mDroppedFramesStat.incrementStats(i);
        if(i > 0)
            FLog.v(TAG, "Dropped %d frames", Integer.valueOf(i));
    }

    public void onDrawMethodBegin()
    {
        mLastTimeStamp = SystemClock.elapsedRealtime();
    }

    public void onDrawMethodEnd()
    {
        long l = SystemClock.elapsedRealtime();
        long l1 = mLastTimeStamp;
        FLog.v(TAG, "draw took %d", Long.valueOf(l - l1));
    }

    public void onNextFrameMethodBegin()
    {
        mLastTimeStamp = SystemClock.elapsedRealtime();
    }

    public void onNextFrameMethodEnd()
    {
        long l = SystemClock.elapsedRealtime() - mLastTimeStamp;
        if(l > 3L)
            FLog.v(TAG, "onNextFrame took %d", Long.valueOf(l));
    }

    public void onStartMethodBegin()
    {
        mLastTimeStamp = SystemClock.elapsedRealtime();
    }

    public void onStartMethodEnd()
    {
        long l = SystemClock.elapsedRealtime() - mLastTimeStamp;
        if(l > 3L)
            FLog.v(TAG, "onStart took %d", Long.valueOf(l));
    }

    public void setBackend(AnimatedDrawableCachingBackend animateddrawablecachingbackend)
    {
        mAnimatedDrawableBackend = animateddrawablecachingbackend;
    }

    private static final Class TAG = AnimatedDrawableDiagnostics.class;
    private AnimatedDrawableCachingBackend mAnimatedDrawableBackend;
    private final AnimatedDrawableUtil mAnimatedDrawableUtil;
    private final TextPaint mDebugTextPaint = new TextPaint();
    private final DisplayMetrics mDisplayMetrics;
    private final RollingStat mDrawnFrames = new RollingStat();
    private final RollingStat mDroppedFramesStat = new RollingStat();
    private long mLastTimeStamp;
    private final StringBuilder sbTemp = new StringBuilder();

}
