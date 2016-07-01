

package com.facebook.imagepipeline.animated.base;

import android.graphics.Bitmap;

public interface AnimatedImageFrame
{

    public abstract void dispose();

    public abstract int getDurationMs();

    public abstract int getHeight();

    public abstract int getWidth();

    public abstract int getXOffset();

    public abstract int getYOffset();

    public abstract void renderFrame(int i, int j, Bitmap bitmap);
}
