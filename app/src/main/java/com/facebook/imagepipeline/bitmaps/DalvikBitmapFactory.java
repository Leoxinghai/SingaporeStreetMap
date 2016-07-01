// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.bitmaps;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.internal.Throwables;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.references.ResourceReleaser;
import com.facebook.imageformat.ImageFormat;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.memory.*;
import com.facebook.imagepipeline.nativecode.Bitmaps;
import java.util.*;

// Referenced classes of package com.facebook.imagepipeline.bitmaps:
//            TooManyBitmapsException, EmptyJpegGenerator

public class DalvikBitmapFactory
{

    public DalvikBitmapFactory(EmptyJpegGenerator emptyjpeggenerator, FlexByteArrayPool flexbytearraypool)
    {
        mJpegGenerator = emptyjpeggenerator;
        mFlexByteArrayPool = flexbytearraypool;
    }

    private Bitmap decodeByteArrayAsPurgeable(CloseableReference closeablereference, android.graphics.BitmapFactory.Options options)
    {
        PooledByteBuffer pooledbytebuffer;
        int i;
        pooledbytebuffer = (PooledByteBuffer)closeablereference.get();
        i = pooledbytebuffer.size();
        closeablereference = mFlexByteArrayPool.get(i);
        byte abyte0[] = (byte[])closeablereference.get();
        pooledbytebuffer.read(0, abyte0, 0, i);
        Bitmap temp = (Bitmap)Preconditions.checkNotNull(BitmapFactory.decodeByteArray(abyte0, 0, i, options), "BitmapFactory returned null");
        CloseableReference.closeSafely(closeablereference);
        return temp;
    }

    private Bitmap decodeJPEGByteArrayAsPurgeable(CloseableReference closeablereference, int i, android.graphics.BitmapFactory.Options options)
    {
        PooledByteBuffer pooledbytebuffer;
        boolean flag = false;
        pooledbytebuffer = (PooledByteBuffer)closeablereference.get();
        if(i <= pooledbytebuffer.size())
            flag = true;
        Preconditions.checkArgument(flag);
        closeablereference = mFlexByteArrayPool.get(i + 2);
        byte abyte0[];
        abyte0 = (byte[])closeablereference.get();
        pooledbytebuffer.read(0, abyte0, 0, i);
        int j = i;
        if(!endsWithEOI(abyte0, i))
            putEOI(abyte0, i);
        j = i + 2;
        Bitmap temp = (Bitmap)Preconditions.checkNotNull(BitmapFactory.decodeByteArray(abyte0, 0, j, options), "BitmapFactory returned null");
        CloseableReference.closeSafely(closeablereference);
        return temp;
    }

    private static boolean endsWithEOI(byte abyte0[], int i)
    {
        return i >= 2 && abyte0[i - 2] == -1 && abyte0[i - 1] == -39;
    }

    private static android.graphics.BitmapFactory.Options getBitmapFactoryOptions(int i)
    {
        android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
        options.inDither = true;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;//Bitmaps.BITMAP_CONFIG;
        options.inPurgeable = true;
        options.inSampleSize = i;
        if(android.os.Build.VERSION.SDK_INT >= 11)
            options.inMutable = true;
        return options;
    }

    private CloseableReference pinBitmap(Bitmap bitmap)
    {
        try
        {
            Bitmaps.pinBitmap(bitmap);
            if(!mUnpooledBitmapsCounter.increase(bitmap))
            {
                bitmap.recycle();
                throw new TooManyBitmapsException();
            } else
            {
                return CloseableReference.of(bitmap, mUnpooledBitmapsReleaser);
            }
        }
        catch(Exception exception)
        {
            bitmap.recycle();
        }
        return null;
    }

    private static void putEOI(byte abyte0[], int i)
    {
        abyte0[i] = -1;
        abyte0[i + 1] = -39;
    }

    List associateBitmapsWithBitmapCounter(List list)
    {
        int i = 0;
        ArrayList obj = new ArrayList();

        for(;i < list.size();) {
            Bitmap bitmap = (Bitmap) list.get(i);
            Bitmaps.pinBitmap(bitmap);
            if (!mUnpooledBitmapsCounter.increase(bitmap))
                throw new TooManyBitmapsException();
            if (list != null)
                for (Iterator iterator1 = list.iterator(); iterator1.hasNext(); ) {
                    Bitmap bitmap1 = (Bitmap) iterator1.next();
                    if (i > 0)
                        mUnpooledBitmapsCounter.decrease(bitmap1);
                    bitmap1.recycle();
                    i--;
                }

            i++;
        }

        for(Iterator iterator = list.iterator(); iterator.hasNext(); ((List) (obj)).add(CloseableReference.of((Bitmap)iterator.next(), mUnpooledBitmapsReleaser)));

        return ((List) (obj));
    }

    CloseableReference createBitmap(short word0, short word1)
    {
        CloseableReference closeablereference = mJpegGenerator.generate(word0, word1);
        EncodedImage encodedimage;
        encodedimage = new EncodedImage(closeablereference);
        encodedimage.setImageFormat(ImageFormat.JPEG);
        CloseableReference closeablereference1;
        closeablereference1 = decodeJPEGFromEncodedImage(encodedimage, ((PooledByteBuffer)closeablereference.get()).size());
        ((Bitmap)closeablereference1.get()).eraseColor(0);
        EncodedImage.closeSafely(encodedimage);
        closeablereference.close();
        return closeablereference1;
    }

    CloseableReference decodeFromEncodedImage(EncodedImage encodedimage)
    {
        Object obj;
        obj = getBitmapFactoryOptions(encodedimage.getSampleSize());
        CloseableReference temp = encodedimage.getByteBufferRef();
        Preconditions.checkNotNull(temp);
        obj = pinBitmap(decodeByteArrayAsPurgeable(temp, ((android.graphics.BitmapFactory.Options) (obj))));
        CloseableReference.closeSafely(temp);
        return ((CloseableReference) (obj));
    }

    CloseableReference decodeJPEGFromEncodedImage(EncodedImage encodedimage, int i)
    {
        Object obj;
        obj = getBitmapFactoryOptions(encodedimage.getSampleSize());
        CloseableReference temp = encodedimage.getByteBufferRef();
        Preconditions.checkNotNull(temp);
        obj = pinBitmap(decodeJPEGByteArrayAsPurgeable(temp, i, ((android.graphics.BitmapFactory.Options) (obj))));
        CloseableReference.closeSafely(temp);
        return ((CloseableReference) (obj));
    }

    private final FlexByteArrayPool mFlexByteArrayPool;
    private final EmptyJpegGenerator mJpegGenerator;
    private final BitmapCounter mUnpooledBitmapsCounter = BitmapCounterProvider.get();
    private final ResourceReleaser mUnpooledBitmapsReleaser = new ResourceReleaser() {

        public void release(Bitmap bitmap)
        {
            mUnpooledBitmapsCounter.decrease(bitmap);
            bitmap.recycle();
            return;
        }

        public void release(Object obj)
        {
            release((Bitmap)obj);
        }

    };

}
