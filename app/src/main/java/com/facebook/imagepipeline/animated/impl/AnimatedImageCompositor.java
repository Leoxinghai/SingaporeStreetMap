// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.animated.impl;

import android.graphics.*;
import com.facebook.common.references.CloseableReference;
import com.facebook.imagepipeline.animated.base.AnimatedDrawableBackend;
import com.facebook.imagepipeline.animated.base.AnimatedDrawableFrameInfo;

public class AnimatedImageCompositor
{
    public static interface Callback
    {

        public abstract CloseableReference getCachedBitmap(int i);

        public abstract void onIntermediateResult(int i, Bitmap bitmap);
    }

    private static enum FrameNeededResult
    {

		REQUIRED("REQUIRED", 0),
		NOT_REQUIRED("NOT_REQUIRED", 1),
		SKIP("SKIP", 2),
		ABORT("ABORT", 3);
        String sType;
        int iType;

        private FrameNeededResult(String s, int i)
        {
            sType = s;
            iType = i;
        }
    }


    public AnimatedImageCompositor(AnimatedDrawableBackend animateddrawablebackend, Callback callback)
    {
        mAnimatedDrawableBackend = animateddrawablebackend;
        mCallback = callback;
        mTransparentFillPaint.setColor(0);
        mTransparentFillPaint.setStyle(android.graphics.Paint.Style.FILL);
        mTransparentFillPaint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC));
    }

    private void disposeToBackground(Canvas canvas, AnimatedDrawableFrameInfo animateddrawableframeinfo)
    {
        canvas.drawRect(animateddrawableframeinfo.xOffset, animateddrawableframeinfo.yOffset, animateddrawableframeinfo.xOffset + animateddrawableframeinfo.width, animateddrawableframeinfo.yOffset + animateddrawableframeinfo.height, mTransparentFillPaint);
    }

    private FrameNeededResult isFrameNeededForRendering(int i)
    {
        AnimatedDrawableFrameInfo animateddrawableframeinfo = mAnimatedDrawableBackend.getFrameInfo(i);
        com.facebook.imagepipeline.animated.base.AnimatedDrawableFrameInfo.DisposalMethod disposalmethod = animateddrawableframeinfo.disposalMethod;
        if(disposalmethod == com.facebook.imagepipeline.animated.base.AnimatedDrawableFrameInfo.DisposalMethod.DISPOSE_DO_NOT)
            return FrameNeededResult.REQUIRED;
        if(disposalmethod == com.facebook.imagepipeline.animated.base.AnimatedDrawableFrameInfo.DisposalMethod.DISPOSE_TO_BACKGROUND)
            if(animateddrawableframeinfo.xOffset == 0 && animateddrawableframeinfo.yOffset == 0 && animateddrawableframeinfo.width == mAnimatedDrawableBackend.getRenderedWidth() && animateddrawableframeinfo.height == mAnimatedDrawableBackend.getRenderedHeight())
                return FrameNeededResult.NOT_REQUIRED;
            else
                return FrameNeededResult.REQUIRED;
        if(disposalmethod == com.facebook.imagepipeline.animated.base.AnimatedDrawableFrameInfo.DisposalMethod.DISPOSE_TO_PREVIOUS)
            return FrameNeededResult.SKIP;
        else
            return FrameNeededResult.ABORT;
    }

    private int prepareCanvasWithClosestCachedFrame(int i, Canvas canvas)
    {

        for(;i >= 0;) {
            Object obj;
            int j;
            obj = isFrameNeededForRendering(i);
            j = i;

            switch (((FrameNeededResult) (obj)).ordinal()) {
                default:
                    break;
                case 1:
                    AnimatedDrawableFrameInfo animateddrawableframeinfo;
                    animateddrawableframeinfo = mAnimatedDrawableBackend.getFrameInfo(i);
                    obj = mCallback.getCachedBitmap(i);
                    if (obj == null) {
                        if (!animateddrawableframeinfo.shouldBlendWithPreviousFrame)
                            return i;
                        break;
                    }
                    canvas.drawBitmap((Bitmap) ((CloseableReference) (obj)).get(), 0.0F, 0.0F, null);
                    if (animateddrawableframeinfo.disposalMethod == com.facebook.imagepipeline.animated.base.AnimatedDrawableFrameInfo.DisposalMethod.DISPOSE_TO_BACKGROUND)
                        disposeToBackground(canvas, animateddrawableframeinfo);
                    j = i + 1;
                    ((CloseableReference) (obj)).close();
                    return j;
                case 3:
                    return j;
                case 2:
                    return i + 1;
            }
            i--;
        }

        return 0;
    }

    public void renderFrame(int i, Bitmap bitmap)
    {
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(0, android.graphics.PorterDuff.Mode.SRC);
        int j;
        if(mAnimatedDrawableBackend.getFrameInfo(i).shouldBlendWithPreviousFrame && i > 0)
            j = prepareCanvasWithClosestCachedFrame(i - 1, canvas);
        else
            j = i;
        while(j < i)
        {
            AnimatedDrawableFrameInfo animateddrawableframeinfo = mAnimatedDrawableBackend.getFrameInfo(j);
            com.facebook.imagepipeline.animated.base.AnimatedDrawableFrameInfo.DisposalMethod disposalmethod = animateddrawableframeinfo.disposalMethod;
            if(disposalmethod != com.facebook.imagepipeline.animated.base.AnimatedDrawableFrameInfo.DisposalMethod.DISPOSE_TO_PREVIOUS)
            {
                mAnimatedDrawableBackend.renderFrame(j, canvas);
                mCallback.onIntermediateResult(j, bitmap);
                if(disposalmethod == com.facebook.imagepipeline.animated.base.AnimatedDrawableFrameInfo.DisposalMethod.DISPOSE_TO_BACKGROUND)
                    disposeToBackground(canvas, animateddrawableframeinfo);
            }
            j++;
        }
        mAnimatedDrawableBackend.renderFrame(i, canvas);
    }

    private final AnimatedDrawableBackend mAnimatedDrawableBackend;
    private final Callback mCallback;
    private final Paint mTransparentFillPaint = new Paint();
}
