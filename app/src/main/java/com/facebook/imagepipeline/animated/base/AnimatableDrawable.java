

package com.facebook.imagepipeline.animated.base;

import android.graphics.drawable.Animatable;
import com.nineoldandroids.animation.ValueAnimator;

public interface AnimatableDrawable
    extends Animatable
{

    public abstract com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener createAnimatorUpdateListener();

    public abstract ValueAnimator createValueAnimator();

    public abstract ValueAnimator createValueAnimator(int i);
}
