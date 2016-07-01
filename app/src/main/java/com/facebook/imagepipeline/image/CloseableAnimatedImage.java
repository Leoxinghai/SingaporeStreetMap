// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.image;

import com.facebook.imagepipeline.animated.base.AnimatedImage;
import com.facebook.imagepipeline.animated.base.AnimatedImageResult;

// Referenced classes of package com.facebook.imagepipeline.image:
//            CloseableImage

public class CloseableAnimatedImage extends CloseableImage
{

    public CloseableAnimatedImage(AnimatedImageResult animatedimageresult)
    {
        mImageResult = animatedimageresult;
    }

    public void close()
    {
        if(mImageResult == null)
            return;
        AnimatedImageResult animatedimageresult;
        animatedimageresult = mImageResult;
        mImageResult = null;
        animatedimageresult.dispose();
        return;
    }

    public int getHeight()
    {
        boolean flag = isClosed();
        int i = 0;
        if(!flag) {
			i = mImageResult.getImage().getHeight();
		}
        return i;
    }

    public AnimatedImage getImage()
    {
        boolean flag = isClosed();
        AnimatedImage animatedimage = null;
        if(!flag) {
			animatedimage = mImageResult.getImage();
		}
        return animatedimage;
    }

    public AnimatedImageResult getImageResult()
    {
        AnimatedImageResult animatedimageresult = mImageResult;
        return animatedimageresult;
    }

    public int getSizeInBytes()
    {
        boolean flag = isClosed();
        int i = 0;
        if(!flag) {
			i = mImageResult.getImage().getSizeInBytes();
		}
		return i;
    }

    public int getWidth()
    {
        boolean flag = isClosed();
        int i = 0;
        if(!flag) {
			i = mImageResult.getImage().getWidth();
		}
        return i;
    }

    public boolean isClosed()
    {
        AnimatedImageResult animatedimageresult = mImageResult;
        boolean flag;
        if(animatedimageresult == null)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public boolean isStateful()
    {
        return true;
    }

    private AnimatedImageResult mImageResult;
}
