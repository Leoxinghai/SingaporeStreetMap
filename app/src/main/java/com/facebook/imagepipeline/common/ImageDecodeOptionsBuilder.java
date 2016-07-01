

package com.facebook.imagepipeline.common;


// Referenced classes of package com.facebook.imagepipeline.common:
//            ImageDecodeOptions

public class ImageDecodeOptionsBuilder
{

    ImageDecodeOptionsBuilder()
    {
        mMinDecodeIntervalMs = 100;
        mBackgroundColor = 0xffffff;
    }

    public ImageDecodeOptions build()
    {
        return new ImageDecodeOptions(this);
    }

    public int getBackgroundColor()
    {
        return mBackgroundColor;
    }

    public boolean getDecodeAllFrames()
    {
        return mDecodeAllFrames;
    }

    public boolean getDecodePreviewFrame()
    {
        return mDecodePreviewFrame;
    }

    public boolean getForceOldAnimationCode()
    {
        return mForceOldAnimationCode;
    }

    public int getMinDecodeIntervalMs()
    {
        return mMinDecodeIntervalMs;
    }

    public boolean getUseLastFrameForPreview()
    {
        return mUseLastFrameForPreview;
    }

    public ImageDecodeOptionsBuilder setBackgroundColor(int i)
    {
        mBackgroundColor = i;
        return this;
    }

    public ImageDecodeOptionsBuilder setDecodeAllFrames(boolean flag)
    {
        mDecodeAllFrames = flag;
        return this;
    }

    public ImageDecodeOptionsBuilder setDecodePreviewFrame(boolean flag)
    {
        mDecodePreviewFrame = flag;
        return this;
    }

    public ImageDecodeOptionsBuilder setForceOldAnimationCode(boolean flag)
    {
        mForceOldAnimationCode = flag;
        return this;
    }

    public ImageDecodeOptionsBuilder setFrom(ImageDecodeOptions imagedecodeoptions)
    {
        mBackgroundColor = imagedecodeoptions.backgroundColor;
        mForceOldAnimationCode = imagedecodeoptions.forceOldAnimationCode;
        mDecodePreviewFrame = imagedecodeoptions.decodePreviewFrame;
        mUseLastFrameForPreview = imagedecodeoptions.useLastFrameForPreview;
        mDecodeAllFrames = imagedecodeoptions.decodeAllFrames;
        return this;
    }

    public ImageDecodeOptionsBuilder setMinDecodeIntervalMs(int i)
    {
        mMinDecodeIntervalMs = i;
        return this;
    }

    public ImageDecodeOptionsBuilder setUseLastFrameForPreview(boolean flag)
    {
        mUseLastFrameForPreview = flag;
        return this;
    }

    private int mBackgroundColor;
    private boolean mDecodeAllFrames;
    private boolean mDecodePreviewFrame;
    private boolean mForceOldAnimationCode;
    private int mMinDecodeIntervalMs;
    private boolean mUseLastFrameForPreview;
}
