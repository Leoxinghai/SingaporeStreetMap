

package com.facebook.imagepipeline.animated.base;

import com.facebook.common.references.CloseableReference;
import java.util.List;

// Referenced classes of package com.facebook.imagepipeline.animated.base:
//            AnimatedImageResult, AnimatedImage

public class AnimatedImageResultBuilder
{

    AnimatedImageResultBuilder(AnimatedImage animatedimage)
    {
        mImage = animatedimage;
    }

    public AnimatedImageResult build()
    {
        try {
            AnimatedImageResult animatedimageresult = new AnimatedImageResult(this);
            CloseableReference.closeSafely(mPreviewBitmap);
            mPreviewBitmap = null;
            CloseableReference.closeSafely(mDecodedFrames);
            mDecodedFrames = null;
            return animatedimageresult;
        } catch(Exception exception) {
            CloseableReference.closeSafely(mPreviewBitmap);
            mPreviewBitmap = null;
            CloseableReference.closeSafely(mDecodedFrames);
            mDecodedFrames = null;
            throw exception;
        }
    }

    public List getDecodedFrames()
    {
        return CloseableReference.cloneOrNull(mDecodedFrames);
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
        return CloseableReference.cloneOrNull(mPreviewBitmap);
    }

    public AnimatedImageResultBuilder setDecodedFrames(List list)
    {
        mDecodedFrames = CloseableReference.cloneOrNull(list);
        return this;
    }

    public AnimatedImageResultBuilder setFrameForPreview(int i)
    {
        mFrameForPreview = i;
        return this;
    }

    public AnimatedImageResultBuilder setPreviewBitmap(CloseableReference closeablereference)
    {
        mPreviewBitmap = CloseableReference.cloneOrNull(closeablereference);
        return this;
    }

    private List mDecodedFrames;
    private int mFrameForPreview;
    private final AnimatedImage mImage;
    private CloseableReference mPreviewBitmap;
}
