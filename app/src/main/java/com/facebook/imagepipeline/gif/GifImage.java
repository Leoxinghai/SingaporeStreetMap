// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.gif;

import com.facebook.common.internal.Preconditions;
import com.facebook.common.soloader.SoLoaderShim;
import com.facebook.imagepipeline.animated.base.*;
import java.nio.ByteBuffer;

// Referenced classes of package com.facebook.imagepipeline.gif:
//            GifFrame

public class GifImage
    implements AnimatedImage
{

    GifImage(long l)
    {
        mNativeContext = l;
    }

    public static GifImage create(long l, int i)
    {
        ensure();
        boolean flag;
        if(l != 0L)
            flag = true;
        else
            flag = false;
        Preconditions.checkArgument(flag);
        return nativeCreateFromNativeMemory(l, i);
    }

    public static GifImage create(byte abyte0[])
    {
        ensure();
        Preconditions.checkNotNull(abyte0);
        ByteBuffer bytebuffer = ByteBuffer.allocateDirect(abyte0.length);
        bytebuffer.put(abyte0);
        bytebuffer.rewind();
        return nativeCreateFromDirectByteBuffer(bytebuffer);
    }

    private static void ensure()
    {
        if(!sInitialized)
        {
            sInitialized = true;
            SoLoaderShim.loadLibrary("gifimage");
        }
        return;
    }

    private static com.facebook.imagepipeline.animated.base.AnimatedDrawableFrameInfo.DisposalMethod fromGifDisposalMethod(int i)
    {
        if(i == 0)
            return com.facebook.imagepipeline.animated.base.AnimatedDrawableFrameInfo.DisposalMethod.DISPOSE_DO_NOT;
        if(i == 1)
            return com.facebook.imagepipeline.animated.base.AnimatedDrawableFrameInfo.DisposalMethod.DISPOSE_DO_NOT;
        if(i == 2)
            return com.facebook.imagepipeline.animated.base.AnimatedDrawableFrameInfo.DisposalMethod.DISPOSE_TO_BACKGROUND;
        if(i == 3)
            return com.facebook.imagepipeline.animated.base.AnimatedDrawableFrameInfo.DisposalMethod.DISPOSE_TO_PREVIOUS;
        else
            return com.facebook.imagepipeline.animated.base.AnimatedDrawableFrameInfo.DisposalMethod.DISPOSE_DO_NOT;
    }

    private static native GifImage nativeCreateFromDirectByteBuffer(ByteBuffer bytebuffer);

    private static native GifImage nativeCreateFromNativeMemory(long l, int i);

    private native void nativeDispose();

    private native void nativeFinalize();

    private native int nativeGetDuration();

    private native GifFrame nativeGetFrame(int i);

    private native int nativeGetFrameCount();

    private native int[] nativeGetFrameDurations();

    private native int nativeGetHeight();

    private native int nativeGetLoopCount();

    private native int nativeGetSizeInBytes();

    private native int nativeGetWidth();

    public void dispose()
    {
        nativeDispose();
    }

    public boolean doesRenderSupportScaling()
    {
        return false;
    }

    protected void finalize()
    {
        nativeFinalize();
    }

    public int getDuration()
    {
        return nativeGetDuration();
    }


    public GifFrame getFrame(int i)
    {
        return nativeGetFrame(i);
    }

    public int getFrameCount()
    {
        return nativeGetFrameCount();
    }

    public int[] getFrameDurations()
    {
        return nativeGetFrameDurations();
    }

    public AnimatedDrawableFrameInfo getFrameInfo(int i)
    {
        GifFrame gifframe = getFrame(i);
        AnimatedDrawableFrameInfo animateddrawableframeinfo = new AnimatedDrawableFrameInfo(i, gifframe.getXOffset(), gifframe.getYOffset(), gifframe.getWidth(), gifframe.getHeight(), true, fromGifDisposalMethod(gifframe.getDisposalMode()));
        gifframe.dispose();
        return animateddrawableframeinfo;
    }

    public int getHeight()
    {
        return nativeGetHeight();
    }

    public int getLoopCount()
    {
        return nativeGetLoopCount();
    }

    public int getSizeInBytes()
    {
        return nativeGetSizeInBytes();
    }

    public int getWidth()
    {
        return nativeGetWidth();
    }

    private static boolean sInitialized;
    private long mNativeContext;
}
