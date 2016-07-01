

package com.facebook.imagepipeline.bitmaps;

import android.graphics.Bitmap;
import com.facebook.common.references.CloseableReference;
import com.facebook.imageformat.ImageFormat;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.memory.PooledByteBuffer;
import com.facebook.imagepipeline.platform.PlatformDecoder;

// Referenced classes of package com.facebook.imagepipeline.bitmaps:
//            PlatformBitmapFactory, EmptyJpegGenerator

public class HoneycombBitmapFactory extends PlatformBitmapFactory
{

    public HoneycombBitmapFactory(EmptyJpegGenerator emptyjpeggenerator, PlatformDecoder platformdecoder)
    {
        mJpegGenerator = emptyjpeggenerator;
        mPurgeableDecoder = platformdecoder;
    }

    public CloseableReference createBitmap(int i, int j, android.graphics.Bitmap.Config config)
    {
        CloseableReference closeablereference = mJpegGenerator.generate((short)i, (short)j);
        EncodedImage encodedimage;
        encodedimage = new EncodedImage(closeablereference);
        encodedimage.setImageFormat(ImageFormat.JPEG);
        CloseableReference temp = mPurgeableDecoder.decodeJPEGFromEncodedImage(encodedimage, config, ((PooledByteBuffer)closeablereference.get()).size());
        ((Bitmap)temp.get()).eraseColor(0);
        EncodedImage.closeSafely(encodedimage);
        closeablereference.close();
        return temp;
    }

    private final EmptyJpegGenerator mJpegGenerator;
    private final PlatformDecoder mPurgeableDecoder;
}
