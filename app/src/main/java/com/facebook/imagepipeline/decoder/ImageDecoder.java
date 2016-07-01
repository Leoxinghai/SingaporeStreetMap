

package com.facebook.imagepipeline.decoder;

import com.facebook.common.internal.Closeables;
import com.facebook.common.references.CloseableReference;
import com.facebook.imageformat.*;
import com.facebook.imagepipeline.animated.factory.AnimatedImageFactory;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.image.*;
import com.facebook.imagepipeline.platform.PlatformDecoder;

public class ImageDecoder
{

    public ImageDecoder(AnimatedImageFactory animatedimagefactory, PlatformDecoder platformdecoder, android.graphics.Bitmap.Config config)
    {
        mAnimatedImageFactory = animatedimagefactory;
        mBitmapConfig = config;
        mPlatformDecoder = platformdecoder;
    }

    public CloseableImage decodeAnimatedWebp(EncodedImage encodedimage, ImageDecodeOptions imagedecodeoptions)
    {
        return mAnimatedImageFactory.decodeWebP(encodedimage, imagedecodeoptions, mBitmapConfig);
    }

    public CloseableImage decodeGif(EncodedImage encodedimage, ImageDecodeOptions imagedecodeoptions)
    {
        java.io.InputStream inputstream;
        CloseableStaticBitmap temp;
        inputstream = encodedimage.getInputStream();
        if(inputstream == null)
            return null;
        if(!GifFormatChecker.isAnimated(inputstream)) {
            temp = decodeStaticImage(encodedimage);
            Closeables.closeQuietly(inputstream);
            return temp;
        } else {
            CloseableImage temp2 = mAnimatedImageFactory.decodeGif(encodedimage, imagedecodeoptions, mBitmapConfig);
            Closeables.closeQuietly(inputstream);
            return temp2;
        }

    }

    public CloseableImage decodeImage(EncodedImage encodedimage, int i, QualityInfo qualityinfo, ImageDecodeOptions imagedecodeoptions)
    {
        ImageFormat imageformat;
        ImageFormat imageformat1 = encodedimage.getImageFormat();
        imageformat = imageformat1;
        if(imageformat1 == null || imageformat1 == ImageFormat.UNKNOWN)
        {
            imageformat = ImageFormatChecker.getImageFormat_WrapIOException(encodedimage.getInputStream());
        }
        switch(imageformat.ordinal())
        {
        default:
            return decodeStaticImage(encodedimage);

        case 1: // '\001'
            throw new IllegalArgumentException("unknown image format");

        case 2: // '\002'
            return decodeJpeg(encodedimage, i, qualityinfo);

        case 3: // '\003'
            return decodeGif(encodedimage, imagedecodeoptions);

        case 4: // '\004'
            return decodeAnimatedWebp(encodedimage, imagedecodeoptions);
        }
    }

    public CloseableStaticBitmap decodeJpeg(EncodedImage encodedimage, int i, QualityInfo qualityinfo)
    {
        CloseableReference closeablereference = mPlatformDecoder.decodeJPEGFromEncodedImage(encodedimage, mBitmapConfig, i);
        CloseableStaticBitmap temp = new CloseableStaticBitmap(closeablereference, qualityinfo, encodedimage.getRotationAngle());
        closeablereference.close();
        return temp;
    }

    public CloseableStaticBitmap decodeStaticImage(EncodedImage encodedimage)
    {
        CloseableReference closeablereference = mPlatformDecoder.decodeFromEncodedImage(encodedimage, mBitmapConfig);
        CloseableStaticBitmap temp = new CloseableStaticBitmap(closeablereference, ImmutableQualityInfo.FULL_QUALITY, encodedimage.getRotationAngle());
        closeablereference.close();
        return temp;
    }

    private final AnimatedImageFactory mAnimatedImageFactory;
    private final android.graphics.Bitmap.Config mBitmapConfig;
    private final PlatformDecoder mPlatformDecoder;
}
