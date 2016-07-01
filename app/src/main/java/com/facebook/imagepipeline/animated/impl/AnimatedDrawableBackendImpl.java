// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.animated.impl;

import android.graphics.*;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.references.CloseableReference;
import com.facebook.imagepipeline.animated.base.*;
import com.facebook.imagepipeline.animated.util.AnimatedDrawableUtil;

public class AnimatedDrawableBackendImpl
    implements AnimatedDrawableBackend
{

    public AnimatedDrawableBackendImpl(AnimatedDrawableUtil animateddrawableutil, AnimatedImageResult animatedimageresult, Rect rect)
    {
        mAnimatedDrawableUtil = animateddrawableutil;
        mAnimatedImageResult = animatedimageresult;
        mAnimatedImage = animatedimageresult.getImage();
        mFrameDurationsMs = mAnimatedImage.getFrameDurations();
        mAnimatedDrawableUtil.fixFrameDurations(mFrameDurationsMs);
        mDurationMs = mAnimatedDrawableUtil.getTotalDurationFromFrameDurations(mFrameDurationsMs);
        mFrameTimestampsMs = mAnimatedDrawableUtil.getFrameTimeStampsFromDurations(mFrameDurationsMs);
        mRenderedBounds = getBoundsToUse(mAnimatedImage, rect);
        mFrameInfos = new AnimatedDrawableFrameInfo[mAnimatedImage.getFrameCount()];
        for(int i = 0; i < mAnimatedImage.getFrameCount(); i++)
            mFrameInfos[i] = mAnimatedImage.getFrameInfo(i);

    }

    private static Rect getBoundsToUse(AnimatedImage animatedimage, Rect rect)
    {
        if(rect == null)
            return new Rect(0, 0, animatedimage.getWidth(), animatedimage.getHeight());
        else
            return new Rect(0, 0, Math.min(rect.width(), animatedimage.getWidth()), Math.min(rect.height(), animatedimage.getHeight()));
    }

    private void renderImageSupportsScaling(Canvas canvas, AnimatedImageFrame animatedimageframe)
    {
        double d1;
        int i;
        int j;
        int k;
        double d = (double)mRenderedBounds.width() / (double)mAnimatedImage.getWidth();
        d1 = (double)mRenderedBounds.height() / (double)mAnimatedImage.getHeight();
        i = (int)Math.round((double)animatedimageframe.getWidth() * d);
        j = (int)Math.round((double)animatedimageframe.getHeight() * d1);
        k = (int)((double)animatedimageframe.getXOffset() * d);
        int l = (int)((double)animatedimageframe.getYOffset() * d1);
        if(mTempBitmap == null)
            mTempBitmap = Bitmap.createBitmap(mRenderedBounds.width(), mRenderedBounds.height(), android.graphics.Bitmap.Config.ARGB_8888);
        mTempBitmap.eraseColor(0);
        animatedimageframe.renderFrame(i, j, mTempBitmap);
        canvas.drawBitmap(mTempBitmap, k, l, null);
        return;
    }

    public void dropCaches()
    {
        if(mTempBitmap != null)
        {
            mTempBitmap.recycle();
            mTempBitmap = null;
        }
        return;
    }

    public AnimatedDrawableBackend forNewBounds(Rect rect)
    {
        if(getBoundsToUse(mAnimatedImage, rect).equals(mRenderedBounds))
            return this;
        else
            return new AnimatedDrawableBackendImpl(mAnimatedDrawableUtil, mAnimatedImageResult, rect);
    }

    public AnimatedImageResult getAnimatedImageResult()
    {
        return mAnimatedImageResult;
    }

    public int getDurationMs()
    {
        return mDurationMs;
    }

    public int getDurationMsForFrame(int i)
    {
        return mFrameDurationsMs[i];
    }

    public int getFrameCount()
    {
        return mAnimatedImage.getFrameCount();
    }

    public int getFrameForPreview()
    {
        return mAnimatedImageResult.getFrameForPreview();
    }

    public int getFrameForTimestampMs(int i)
    {
        return mAnimatedDrawableUtil.getFrameForTimestampMs(mFrameTimestampsMs, i);
    }

    public AnimatedDrawableFrameInfo getFrameInfo(int i)
    {
        return mFrameInfos[i];
    }

    public int getHeight()
    {
        return mAnimatedImage.getHeight();
    }

    public int getLoopCount()
    {
        return mAnimatedImage.getLoopCount();
    }

    public int getMemoryUsage()
    {
        int i = 0;
        int j;
        if(mTempBitmap != null)
            i = 0 + mAnimatedDrawableUtil.getSizeOfBitmap(mTempBitmap);
        j = mAnimatedImage.getSizeInBytes();
        return i + j;
    }

    public CloseableReference getPreDecodedFrame(int i)
    {
        return mAnimatedImageResult.getDecodedFrame(i);
    }

    public int getRenderedHeight()
    {
        return mRenderedBounds.height();
    }

    public int getRenderedWidth()
    {
        return mRenderedBounds.width();
    }

    public int getTimestampMsForFrame(int i)
    {
        Preconditions.checkElementIndex(i, mFrameTimestampsMs.length);
        return mFrameTimestampsMs[i];
    }

    public int getWidth()
    {
        return mAnimatedImage.getWidth();
    }

    public boolean hasPreDecodedFrame(int i)
    {
        return mAnimatedImageResult.hasDecodedFrame(i);
    }

    public void renderFrame(int i, Canvas canvas)
    {
        AnimatedImageFrame animatedimageframe = mAnimatedImage.getFrame(i);
        if(!mAnimatedImage.doesRenderSupportScaling()) {
            renderImageDoesNotSupportScaling(canvas, animatedimageframe);
        } else {
            renderImageSupportsScaling(canvas, animatedimageframe);
        }

        animatedimageframe.dispose();
        return;
    }

    public void renderImageDoesNotSupportScaling(Canvas canvas, AnimatedImageFrame animatedimageframe)
    {
        int i;
        int j;
        int k;
        i = animatedimageframe.getWidth();
        j = animatedimageframe.getHeight();
        k = animatedimageframe.getXOffset();
        int l = animatedimageframe.getYOffset();
        if(mTempBitmap == null)
            mTempBitmap = Bitmap.createBitmap(mAnimatedImage.getWidth(), mAnimatedImage.getHeight(), android.graphics.Bitmap.Config.ARGB_8888);
        mTempBitmap.eraseColor(0);
        animatedimageframe.renderFrame(i, j, mTempBitmap);
        float f = (float)mRenderedBounds.width() / (float)mAnimatedImage.getWidth();
        float f1 = (float)mRenderedBounds.height() / (float)mAnimatedImage.getHeight();
        canvas.save();
        canvas.scale(f, f1);
        canvas.translate(k, l);
        canvas.drawBitmap(mTempBitmap, 0.0F, 0.0F, null);
        canvas.restore();
        return;
    }

    private final AnimatedDrawableUtil mAnimatedDrawableUtil;
    private final AnimatedImage mAnimatedImage;
    private final AnimatedImageResult mAnimatedImageResult;
    private final int mDurationMs;
    private final int mFrameDurationsMs[];
    private final AnimatedDrawableFrameInfo mFrameInfos[];
    private final int mFrameTimestampsMs[];
    private final Rect mRenderedBounds;
    private Bitmap mTempBitmap;
}
