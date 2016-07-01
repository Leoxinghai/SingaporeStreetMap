

package com.facebook.imagepipeline.animated.base;


// Referenced classes of package com.facebook.imagepipeline.animated.base:
//            AnimatedDrawableOptions

public class AnimatedDrawableOptionsBuilder
{

    public AnimatedDrawableOptionsBuilder()
    {
        mAllowPrefetching = true;
        mMaximumBytes = -1;
    }

    public AnimatedDrawableOptions build()
    {
        return new AnimatedDrawableOptions(this);
    }

    public boolean getAllowPrefetching()
    {
        return mAllowPrefetching;
    }

    public boolean getEnableDebugging()
    {
        return mEnableDebugging;
    }

    public boolean getForceKeepAllFramesInMemory()
    {
        return mForceKeepAllFramesInMemory;
    }

    public int getMaximumBytes()
    {
        return mMaximumBytes;
    }

    public AnimatedDrawableOptionsBuilder setAllowPrefetching(boolean flag)
    {
        mAllowPrefetching = flag;
        return this;
    }

    public AnimatedDrawableOptionsBuilder setEnableDebugging(boolean flag)
    {
        mEnableDebugging = flag;
        return this;
    }

    public AnimatedDrawableOptionsBuilder setForceKeepAllFramesInMemory(boolean flag)
    {
        mForceKeepAllFramesInMemory = flag;
        return this;
    }

    public AnimatedDrawableOptionsBuilder setMaximumBytes(int i)
    {
        mMaximumBytes = i;
        return this;
    }

    private boolean mAllowPrefetching;
    private boolean mEnableDebugging;
    private boolean mForceKeepAllFramesInMemory;
    private int mMaximumBytes;
}
