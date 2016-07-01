// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.webp;

import com.facebook.common.internal.Preconditions;
import com.facebook.common.soloader.SoLoaderShim;
import com.facebook.imagepipeline.animated.base.*;
import java.nio.ByteBuffer;

// Referenced classes of package com.facebook.imagepipeline.webp:
//            WebPFrame

public class WebPImage
    implements AnimatedImage
{

    WebPImage(long l)
    {
        mNativeContext = l;
    }

    public static WebPImage create(long l, int i)
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

    public static WebPImage create(byte abyte0[])
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
            SoLoaderShim.loadLibrary("webp");
            SoLoaderShim.loadLibrary("webpimage");
        }
        return;
    }

    private static native WebPImage nativeCreateFromDirectByteBuffer(ByteBuffer bytebuffer);

    private static native WebPImage nativeCreateFromNativeMemory(long l, int i);

    private native void nativeDispose();

    private native void nativeFinalize();

    private native int nativeGetDuration();

    private native WebPFrame nativeGetFrame(int i);

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
        return true;
    }

    protected void finalize()
    {
        nativeFinalize();
    }

    public int getDuration()
    {
        return nativeGetDuration();
    }


    public WebPFrame getFrame(int i)
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
        WebPFrame webpframe = getFrame(i);
        Object obj;
        int j;
        int k;
        int l;
        int i1;
        boolean flag;
        j = webpframe.getXOffset();
        k = webpframe.getYOffset();
        l = webpframe.getWidth();
        i1 = webpframe.getHeight();
        flag = webpframe.shouldBlendWithPreviousFrame();
        if(!webpframe.shouldDisposeToBackgroundColor())
	        obj = com.facebook.imagepipeline.animated.base.AnimatedDrawableFrameInfo.DisposalMethod.DISPOSE_DO_NOT;
	    else
        	obj = com.facebook.imagepipeline.animated.base.AnimatedDrawableFrameInfo.DisposalMethod.DISPOSE_TO_BACKGROUND;

        obj = new AnimatedDrawableFrameInfo(i, j, k, l, i1, flag, ((com.facebook.imagepipeline.animated.base.AnimatedDrawableFrameInfo.DisposalMethod) (obj)));
        webpframe.dispose();
        return ((AnimatedDrawableFrameInfo) (obj));
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
