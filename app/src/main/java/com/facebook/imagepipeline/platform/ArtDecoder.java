

package com.facebook.imagepipeline.platform;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.streams.LimitedInputStream;
import com.facebook.common.streams.TailAppendingInputStream;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.memory.BitmapPool;
import com.facebook.imageutils.BitmapUtil;
import java.io.InputStream;
import java.nio.ByteBuffer;

// Referenced classes of package com.facebook.imagepipeline.platform:
//            PlatformDecoder

public class ArtDecoder
    implements PlatformDecoder
{

    public ArtDecoder(BitmapPool bitmappool, int i)
    {
        mBitmapPool = bitmappool;
        mDecodeBuffers = new android.support.v4.util.Pools.SynchronizedPool(i);
        for(int j = 0; j < i; j++)
            mDecodeBuffers.release(ByteBuffer.allocate(16384));

    }

    private CloseableReference decodeStaticImageFromStream(InputStream inputstream, android.graphics.BitmapFactory.Options options)
    {
        ByteBuffer bytebuffer = null;
        Bitmap bitmap = null;
        try {
            Preconditions.checkNotNull(inputstream);
            int i = BitmapUtil.getSizeInByteForBitmap(options.outWidth, options.outHeight, options.inPreferredConfig);
            bitmap = (Bitmap) mBitmapPool.get(i);
            if (bitmap == null)
                throw new NullPointerException("BitmapPool.get returned null");
            options.inBitmap = bitmap;
            ByteBuffer bytebuffer1 = (ByteBuffer) mDecodeBuffers.acquire();
            bytebuffer = bytebuffer1;
            if (bytebuffer1 == null)
                bytebuffer = ByteBuffer.allocate(16384);
            options.inTempStorage = bytebuffer.array();
            Bitmap temp3 = BitmapFactory.decodeStream(inputstream, null, options);
            mDecodeBuffers.release(bytebuffer);
            if (bitmap != temp3) {
                mBitmapPool.release(bitmap);
                temp3.recycle();
                throw new IllegalStateException();
            } else {
                return CloseableReference.of(inputstream, mBitmapPool);
            }
        } catch(Exception ex) {
            mBitmapPool.release(bitmap);
            mDecodeBuffers.release(bytebuffer);
            throw ex;
        }
    }

    private static android.graphics.BitmapFactory.Options getDecodeOptionsForStream(EncodedImage encodedimage, android.graphics.Bitmap.Config config)
    {
        android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
        options.inSampleSize = encodedimage.getSampleSize();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(encodedimage.getInputStream(), null, options);
        if(options.outWidth == -1 || options.outHeight == -1)
        {
            throw new IllegalArgumentException();
        } else
        {
            options.inJustDecodeBounds = false;
            options.inDither = true;
            options.inPreferredConfig = config;
            options.inMutable = true;
            return options;
        }
    }

    public CloseableReference decodeFromEncodedImage(EncodedImage encodedimage, android.graphics.Bitmap.Config config)
    {
        BitmapFactory.Options temp = getDecodeOptionsForStream(encodedimage, config);
        boolean flag;
        if(((android.graphics.BitmapFactory.Options) (temp)).inPreferredConfig != android.graphics.Bitmap.Config.ARGB_8888)
            flag = true;
        else
            flag = false;
        try
        {
            CloseableReference temp2 = decodeStaticImageFromStream(encodedimage.getInputStream(), temp);
            return temp2;
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            if(flag)
                return decodeFromEncodedImage(encodedimage, android.graphics.Bitmap.Config.ARGB_8888);
            else
                throw ex;
        }
    }

    public CloseableReference decodeJPEGFromEncodedImage(EncodedImage encodedimage, android.graphics.Bitmap.Config config, int i)
    {
        boolean flag = encodedimage.isCompleteAt(i);
        try
        {

            android.graphics.BitmapFactory.Options options = getDecodeOptionsForStream(encodedimage, config);
            InputStream temp = encodedimage.getInputStream();
            Preconditions.checkNotNull(config);
            InputStream temp2 = null;
            if(encodedimage.getSize() > i)
                temp2 = new LimitedInputStream(temp, i);
            if(!flag)
                temp2 = new TailAppendingInputStream(temp, EOI_TAIL);
            if(options.inPreferredConfig != android.graphics.Bitmap.Config.ARGB_8888)
                i = 1;
            else
                i = 0;
            CloseableReference temp3 = decodeStaticImageFromStream(temp2, options);
            return temp3;
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            if(i != 0)
                return decodeFromEncodedImage(encodedimage, android.graphics.Bitmap.Config.ARGB_8888);
            else
                throw ex;
        }
    }

    private static final int DECODE_BUFFER_SIZE = 16384;
    private static final byte EOI_TAIL[] = {
        -1, -39
    };
    private final BitmapPool mBitmapPool;
    final android.support.v4.util.Pools.SynchronizedPool mDecodeBuffers;

}
