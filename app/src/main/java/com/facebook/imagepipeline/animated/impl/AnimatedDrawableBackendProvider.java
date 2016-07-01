

package com.facebook.imagepipeline.animated.impl;

import android.graphics.Rect;
import com.facebook.imagepipeline.animated.base.AnimatedDrawableBackend;
import com.facebook.imagepipeline.animated.base.AnimatedImageResult;

public interface AnimatedDrawableBackendProvider
{

    public abstract AnimatedDrawableBackend get(AnimatedImageResult animatedimageresult, Rect rect);
}
