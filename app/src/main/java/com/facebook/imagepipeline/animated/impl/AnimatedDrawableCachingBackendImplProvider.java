

package com.facebook.imagepipeline.animated.impl;

import com.facebook.imagepipeline.animated.base.AnimatedDrawableBackend;
import com.facebook.imagepipeline.animated.base.AnimatedDrawableOptions;

// Referenced classes of package com.facebook.imagepipeline.animated.impl:
//            AnimatedDrawableCachingBackendImpl

public interface AnimatedDrawableCachingBackendImplProvider
{

    public abstract AnimatedDrawableCachingBackendImpl get(AnimatedDrawableBackend animateddrawablebackend, AnimatedDrawableOptions animateddrawableoptions);
}
