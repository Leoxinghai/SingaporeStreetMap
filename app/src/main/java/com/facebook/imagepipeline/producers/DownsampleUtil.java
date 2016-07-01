

package com.facebook.imagepipeline.producers;

import android.net.Uri;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.logging.FLog;
import com.facebook.imageformat.ImageFormat;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.request.ImageRequest;

public class DownsampleUtil
{

    private DownsampleUtil()
    {
    }

    static float determineDownsampleRatio(ImageRequest imagerequest, EncodedImage encodedimage)
    {
        Preconditions.checkArgument(EncodedImage.isMetaDataAvailable(encodedimage));
        ResizeOptions resizeoptions = imagerequest.getResizeOptions();
        if(resizeoptions == null || resizeoptions.height <= 0 || resizeoptions.width <= 0 || encodedimage.getWidth() == 0 || encodedimage.getHeight() == 0)
            return 1.0F;
        int i = getRotationAngle(imagerequest, encodedimage);
        float f;
        float f1;
        float f2;
        int j;
        if(i == 90 || i == 270)
            j = 1;
        else
            j = 0;
        if(j != 0)
            i = encodedimage.getHeight();
        else
            i = encodedimage.getWidth();
        if(j != 0)
            j = encodedimage.getWidth();
        else
            j = encodedimage.getHeight();
        f = (float)resizeoptions.width / (float)i;
        f1 = (float)resizeoptions.height / (float)j;
        f2 = Math.max(f, f1);
        FLog.v("DownsampleUtil", "Downsample - Specified size: %dx%d, image size: %dx%d ratio: %.1f x %.1f, ratio: %.3f for %s", new Object[] {
            Integer.valueOf(resizeoptions.width), Integer.valueOf(resizeoptions.height), Integer.valueOf(i), Integer.valueOf(j), Float.valueOf(f), Float.valueOf(f1), Float.valueOf(f2), imagerequest.getSourceUri().toString()
        });
        return f2;
    }

    public static int determineSampleSize(ImageRequest imagerequest, EncodedImage encodedimage)
    {
        int j = 1;
        if(EncodedImage.isMetaDataAvailable(encodedimage)) {
			int i;
			float f = determineDownsampleRatio(imagerequest, encodedimage);
			int k;
			if(encodedimage.getImageFormat() == ImageFormat.JPEG)
				i = ratioToSampleSizeJPEG(f);
			else
				i = ratioToSampleSize(f);
			k = Math.max(encodedimage.getHeight(), encodedimage.getWidth());
			j = i;
			for(;(float)(k / i) > 2048F;)
			{
				if(encodedimage.getImageFormat() == ImageFormat.JPEG)
					i *= 2;
				else
					i++;
			}
		}
        return j;
    }

    private static int getRotationAngle(ImageRequest imagerequest, EncodedImage encodedimage)
    {
        boolean flag = false;
        if(!imagerequest.getAutoRotateEnabled())
            return 0;
        int i = encodedimage.getRotationAngle();
        if(i == 0 || i == 90 || i == 180 || i == 270)
            flag = true;
        Preconditions.checkArgument(flag);
        return i;
    }

    static int ratioToSampleSize(float f)
    {
        if(f > 0.6666667F)
            return 1;
        int i = 2;
        do
        {
            double d = 1.0D / (Math.pow(i, 2D) - (double)i);
            if(1.0D / (double)i + 0.3333333432674408D * d <= (double)f)
                return i - 1;
            i++;
        } while(true);
    }

    static int ratioToSampleSizeJPEG(float f)
    {
        int j = 1;
        if(f <= 0.6666667F) {
			int i = 2;
			do
			{
				double d = 1.0D / (double)(i * 2);
				j = i;
				if(1.0D / (double)(i * 2) + 0.3333333432674408D * d <= (double)f)
					continue;
				i *= 2;
			} while(true);
		}
        return j;
    }

    static int roundToPowerOfTwo(int i)
    {
        int j = 1;
        do
        {
            if(j >= i)
                return j;
            j *= 2;
        } while(true);
    }

    private static final int DEFAULT_SAMPLE_SIZE = 1;
    private static final float INTERVAL_ROUNDING = 0.3333333F;
    private static final float MAX_BITMAP_SIZE = 2048F;
}
