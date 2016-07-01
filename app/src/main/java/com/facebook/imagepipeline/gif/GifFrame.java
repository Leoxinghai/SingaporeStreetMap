

package com.facebook.imagepipeline.gif;

import android.graphics.Bitmap;
import com.facebook.imagepipeline.animated.base.AnimatedImageFrame;

public class GifFrame
    implements AnimatedImageFrame
{

    GifFrame(long l)
    {
        mNativeContext = l;
    }

    private native void nativeDispose();

    private native void nativeFinalize();

    private native int nativeGetDisposalMode();

    private native int nativeGetDurationMs();

    private native int nativeGetHeight();

    private native int nativeGetWidth();

    private native int nativeGetXOffset();

    private native int nativeGetYOffset();

    private native boolean nativeHasTransparency();

    private native void nativeRenderFrame(int i, int j, Bitmap bitmap);

    public void dispose()
    {
        nativeDispose();
    }

    protected void finalize()
    {
        nativeFinalize();
    }

    public int getDisposalMode()
    {
        return nativeGetDisposalMode();
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

    public boolean hasTransparency()
    {
        return nativeHasTransparency();
    }

    public void renderFrame(int i, int j, Bitmap bitmap)
    {
        nativeRenderFrame(i, j, bitmap);
    }

    private long mNativeContext;
}
