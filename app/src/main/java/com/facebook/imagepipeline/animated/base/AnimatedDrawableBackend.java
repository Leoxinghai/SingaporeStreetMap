

package com.facebook.imagepipeline.animated.base;

import android.graphics.Canvas;
import android.graphics.Rect;
import com.facebook.common.references.CloseableReference;

// Referenced classes of package com.facebook.imagepipeline.animated.base:
//            AnimatedImageResult, AnimatedDrawableFrameInfo

public interface AnimatedDrawableBackend
{

    public abstract void dropCaches();

    public abstract AnimatedDrawableBackend forNewBounds(Rect rect);

    public abstract AnimatedImageResult getAnimatedImageResult();

    public abstract int getDurationMs();

    public abstract int getDurationMsForFrame(int i);

    public abstract int getFrameCount();

    public abstract int getFrameForPreview();

    public abstract int getFrameForTimestampMs(int i);

    public abstract AnimatedDrawableFrameInfo getFrameInfo(int i);

    public abstract int getHeight();

    public abstract int getLoopCount();

    public abstract int getMemoryUsage();

    public abstract CloseableReference getPreDecodedFrame(int i);

    public abstract int getRenderedHeight();

    public abstract int getRenderedWidth();

    public abstract int getTimestampMsForFrame(int i);

    public abstract int getWidth();

    public abstract boolean hasPreDecodedFrame(int i);

    public abstract void renderFrame(int i, Canvas canvas);
}
