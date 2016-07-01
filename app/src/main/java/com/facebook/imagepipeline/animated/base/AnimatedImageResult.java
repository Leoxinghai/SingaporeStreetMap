// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.animated.base;

import com.facebook.common.internal.Preconditions;
import com.facebook.common.references.CloseableReference;
import java.util.List;

// Referenced classes of package com.facebook.imagepipeline.animated.base:
//            AnimatedImage, AnimatedImageResultBuilder

public class AnimatedImageResult
{

    private AnimatedImageResult(AnimatedImage animatedimage)
    {
        mImage = (AnimatedImage)Preconditions.checkNotNull(animatedimage);
        mFrameForPreview = 0;
    }

    AnimatedImageResult(AnimatedImageResultBuilder animatedimageresultbuilder)
    {
        mImage = (AnimatedImage)Preconditions.checkNotNull(animatedimageresultbuilder.getImage());
        mFrameForPreview = animatedimageresultbuilder.getFrameForPreview();
        mPreviewBitmap = animatedimageresultbuilder.getPreviewBitmap();
        mDecodedFrames = animatedimageresultbuilder.getDecodedFrames();
    }

    public static AnimatedImageResult forAnimatedImage(AnimatedImage animatedimage)
    {
        return new AnimatedImageResult(animatedimage);
    }

    public static AnimatedImageResultBuilder newBuilder(AnimatedImage animatedimage)
    {
        return new AnimatedImageResultBuilder(animatedimage);
    }

    public void dispose()
    {
        CloseableReference.closeSafely(mPreviewBitmap);
        mPreviewBitmap = null;
        CloseableReference.closeSafely(mDecodedFrames);
        mDecodedFrames = null;
        return;
    }

    public CloseableReference getDecodedFrame(int i)
    {
        CloseableReference closeablereference;
        if(mDecodedFrames == null)
            closeablereference = null;
        else
            closeablereference = CloseableReference.cloneOrNull((CloseableReference)mDecodedFrames.get(i));

        return closeablereference;
    }

    public int getFrameForPreview()
    {
        return mFrameForPreview;
    }

    public AnimatedImage getImage()
    {
        return mImage;
    }

    public CloseableReference getPreviewBitmap()
    {
        CloseableReference closeablereference = CloseableReference.cloneOrNull(mPreviewBitmap);
        return closeablereference;
    }

    public boolean hasDecodedFrame(int i)
    {
        boolean flag = false;
        if(mDecodedFrames != null) {
            Object obj = mDecodedFrames.get(i);
            if (obj != null)
                flag = true;
        }
        return flag;
    }

    private List mDecodedFrames;
    private final int mFrameForPreview;
    private final AnimatedImage mImage;
    private CloseableReference mPreviewBitmap;
}
