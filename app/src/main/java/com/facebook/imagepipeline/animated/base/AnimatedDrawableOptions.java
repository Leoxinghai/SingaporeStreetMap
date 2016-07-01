

package com.facebook.imagepipeline.animated.base;


// Referenced classes of package com.facebook.imagepipeline.animated.base:
//            AnimatedDrawableOptionsBuilder

public class AnimatedDrawableOptions
{

    public AnimatedDrawableOptions(AnimatedDrawableOptionsBuilder animateddrawableoptionsbuilder)
    {
        forceKeepAllFramesInMemory = animateddrawableoptionsbuilder.getForceKeepAllFramesInMemory();
        allowPrefetching = animateddrawableoptionsbuilder.getAllowPrefetching();
        maximumBytes = animateddrawableoptionsbuilder.getMaximumBytes();
        enableDebugging = animateddrawableoptionsbuilder.getEnableDebugging();
    }

    public static AnimatedDrawableOptionsBuilder newBuilder()
    {
        return new AnimatedDrawableOptionsBuilder();
    }

    public static AnimatedDrawableOptions DEFAULTS = newBuilder().build();
    public final boolean allowPrefetching;
    public final boolean enableDebugging;
    public final boolean forceKeepAllFramesInMemory;
    public final int maximumBytes;

}
