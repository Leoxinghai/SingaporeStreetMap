

package com.facebook.imagepipeline.animated.base;


// Referenced classes of package com.facebook.imagepipeline.animated.base:
//            AnimatedImageFrame, AnimatedDrawableFrameInfo

public interface AnimatedImage
{

    public abstract void dispose();

    public abstract boolean doesRenderSupportScaling();

    public abstract int getDuration();

    public abstract AnimatedImageFrame getFrame(int i);

    public abstract int getFrameCount();

    public abstract int[] getFrameDurations();

    public abstract AnimatedDrawableFrameInfo getFrameInfo(int i);

    public abstract int getHeight();

    public abstract int getLoopCount();

    public abstract int getSizeInBytes();

    public abstract int getWidth();
}
