

package com.facebook.imagepipeline.webp;

import android.graphics.Bitmap;
import com.facebook.imagepipeline.animated.base.AnimatedImageFrame;

public class WebPFrame
    implements AnimatedImageFrame
{

    WebPFrame(long l)
    {
        mNativeContext = l;
    }

    private native void nativeDispose();

    private native void nativeFinalize();

    private native int nativeGetDurationMs();

    private native int nativeGetHeight();

    private native int nativeGetWidth();

    private native int nativeGetXOffset();

    private native int nativeGetYOffset();

    private native void nativeRenderFrame(int i, int j, Bitmap bitmap);

    private native boolean nativeShouldBlendWithPreviousFrame();

    private native boolean nativeShouldDisposeToBackgroundColor();

    public void dispose()
    {
        nativeDispose();
    }

    protected void finalize()
    {
        nativeFinalize();
    }

    public int getDurationMs()
    {
        return nativeGetDurationMs();
    }

    public int getHeight()
    {
        return nativeGetHeight();
    }

    public int getWidth()
    {
        return nativeGetWidth();
    }

    public int getXOffset()
    {
        return nativeGetXOffset();
    }

    public int getYOffset()
    {
        return nativeGetYOffset();
    }

    public void renderFrame(int i, int j, Bitmap bitmap)
    {
        nativeRenderFrame(i, j, bitmap);
    }

    public boolean shouldBlendWithPreviousFrame()
    {
        return nativeShouldBlendWithPreviousFrame();
    }

    public boolean shouldDisposeToBackgroundColor()
    {
        return nativeShouldDisposeToBackgroundColor();
    }

    private long mNativeContext;
}
