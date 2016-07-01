

package com.facebook.imagepipeline.platform;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.facebook.common.internal.Preconditions;
import com.facebook.common.internal.Throwables;
import com.facebook.common.references.CloseableReference;
import com.facebook.imagepipeline.common.TooManyBitmapsException;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.memory.*;
import com.facebook.imagepipeline.nativecode.Bitmaps;

// Referenced classes of package com.facebook.imagepipeline.platform:
//            PlatformDecoder

abstract class DalvikPurgeableDecoder
    implements PlatformDecoder
{

    DalvikPurgeableDecoder()
    {
    }

    protected static boolean endsWithEOI(CloseableReference closeablereference, int i)
    {
        PooledByteBuffer pooledByteBuffer = (PooledByteBuffer)closeablereference.get();
        return i >= 2 && pooledByteBuffer.read(i - 2) == -1 && pooledByteBuffer.read(i - 1) == -39;
    }

    private static android.graphics.BitmapFactory.Options getBitmapFactoryOptions(int i, android.graphics.Bitmap.Config config)
    {
        android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
        options.inDither = true;
        options.inPreferredConfig = config;
        options.inPurgeable = true;
        options.inInputShareable = false;
        options.inSampleSize = i;
        if(android.os.Build.VERSION.SDK_INT >= 11)
            options.inMutable = true;
        return options;
    }

    abstract Bitmap decodeByteArrayAsPurgeable(CloseableReference closeablereference, android.graphics.BitmapFactory.Options options);

    public CloseableReference decodeFromEncodedImage(EncodedImage encodedimage, android.graphics.Bitmap.Config config)
    {
        BitmapFactory.Options options = getBitmapFactoryOptions(encodedimage.getSampleSize(), config);
        CloseableReference closeableReference = encodedimage.getByteBufferRef();
        Preconditions.checkNotNull(encodedimage);
        CloseableReference closeableReference1 = pinBitmap(decodeByteArrayAsPurgeable(closeableReference, options));
        CloseableReference.closeSafely(closeableReference);
        return closeableReference1;
    }

    abstract Bitmap decodeJPEGByteArrayAsPurgeable(CloseableReference closeablereference, int i, android.graphics.BitmapFactory.Options options);

    public CloseableReference decodeJPEGFromEncodedImage(EncodedImage encodedimage, android.graphics.Bitmap.Config config, int i)
    {
        BitmapFactory.Options options = getBitmapFactoryOptions(encodedimage.getSampleSize(), config);
        CloseableReference closeableReference = encodedimage.getByteBufferRef();
        Preconditions.checkNotNull(closeableReference);
        CloseableReference closeableReference1 = pinBitmap(decodeJPEGByteArrayAsPurgeable(closeableReference, i, options));
        CloseableReference.closeSafely(closeableReference);
        return closeableReference1;
    }

    public CloseableReference pinBitmap(Bitmap bitmap)
    {
        try
        {
            Bitmaps.pinBitmap(bitmap);
        }
        catch(Exception exception)
        {
            bitmap.recycle();
            //throw Throwables.propagate(exception);
        }
        if(!mUnpooledBitmapsCounter.increase(bitmap))
        {
            bitmap.recycle();
            throw new TooManyBitmapsException();
        } else
        {
            return CloseableReference.of(bitmap, mUnpooledBitmapsCounter.getReleaser());
        }
    }

    protected static final byte EOI[] = {
        -1, -39
    };
    private final BitmapCounter mUnpooledBitmapsCounter = BitmapCounterProvider.get();

}
