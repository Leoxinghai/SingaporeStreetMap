

package com.facebook.imagepipeline.animated.base;

import android.graphics.Canvas;
import com.facebook.common.references.CloseableReference;

// Referenced classes of package com.facebook.imagepipeline.animated.base:
//            AnimatedDrawableBackend, AnimatedImageResult, AnimatedDrawableFrameInfo

public abstract class DelegatingAnimatedDrawableBackend
    implements AnimatedDrawableBackend
{

    public DelegatingAnimatedDrawableBackend(AnimatedDrawableBackend animateddrawablebackend)
    {
        mAnimatedDrawableBackend = animateddrawablebackend;
    }

    public void dropCaches()
    {
        mAnimatedDrawableBackend.dropCaches();
    }

    public AnimatedImageResult getAnimatedImageResult()
    {
        return mAnimatedDrawableBackend.getAnimatedImageResult();
    }

    protected AnimatedDrawableBackend getDelegate()
    {
        return mAnimatedDrawableBackend;
    }

    public int getDurationMs()
    {
        return mAnimatedDrawableBackend.getDurationMs();
    }

    public int getDurationMsForFrame(int i)
    {
        return mAnimatedDrawableBackend.getDurationMsForFrame(i);
    }

    public int getFrameCount()
    {
        return mAnimatedDrawableBackend.getFrameCount();
    }

    public int getFrameForPreview()
    {
        return mAnimatedDrawableBackend.getFrameForPreview();
    }

    public int getFrameForTimestampMs(int i)
    {
        return mAnimatedDrawableBackend.getFrameForTimestampMs(i);
    }

    public AnimatedDrawableFrameInfo getFrameInfo(int i)
    {
        return mAnimatedDrawableBackend.getFrameInfo(i);
    }

    public int getHeight()
    {
        return mAnimatedDrawableBackend.getHeight();
    }

    public int getLoopCount()
    {
        return mAnimatedDrawableBackend.getLoopCount();
    }

    public int getMemoryUsage()
    {
        return mAnimatedDrawableBackend.getMemoryUsage();
    }

    public CloseableReference getPreDecodedFrame(int i)
    {
        return mAnimatedDrawableBackend.getPreDecodedFrame(i);
    }

    public int getRenderedHeight()
    {
        return mAnimatedDrawableBackend.getRenderedHeight();
    }

    public int getRenderedWidth()
    {
        return mAnimatedDrawableBackend.getRenderedWidth();
    }

    public int getTimestampMsForFrame(int i)
    {
        return mAnimatedDrawableBackend.getTimestampMsForFrame(i);
    }

    public int getWidth()
    {
        return mAnimatedDrawableBackend.getWidth();
    }

    public boolean hasPreDecodedFrame(int i)
    {
        return mAnimatedDrawableBackend.hasPreDecodedFrame(i);
    }

    public void renderFrame(int i, Canvas canvas)
    {
        mAnimatedDrawableBackend.renderFrame(i, canvas);
    }

    private final AnimatedDrawableBackend mAnimatedDrawableBackend;
}
