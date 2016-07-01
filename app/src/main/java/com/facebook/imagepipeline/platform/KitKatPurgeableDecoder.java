// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.platform;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.references.CloseableReference;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.memory.FlexByteArrayPool;
import com.facebook.imagepipeline.memory.PooledByteBuffer;

// Referenced classes of package com.facebook.imagepipeline.platform:
//            DalvikPurgeableDecoder

public class KitKatPurgeableDecoder extends DalvikPurgeableDecoder
{

    public KitKatPurgeableDecoder(FlexByteArrayPool flexbytearraypool)
    {
        mFlexByteArrayPool = flexbytearraypool;
    }

    private static void putEOI(byte abyte0[], int i)
    {
        abyte0[i] = -1;
        abyte0[i + 1] = -39;
    }

    protected Bitmap decodeByteArrayAsPurgeable(CloseableReference closeablereference, android.graphics.BitmapFactory.Options options)
    {
        PooledByteBuffer pooledbytebuffer;
        int i;
        pooledbytebuffer = (PooledByteBuffer)closeablereference.get();
        i = pooledbytebuffer.size();
        closeablereference = mFlexByteArrayPool.get(i);
        byte abyte0[] = (byte[])closeablereference.get();
        pooledbytebuffer.read(0, abyte0, 0, i);
        Bitmap bitmap = (Bitmap)Preconditions.checkNotNull(BitmapFactory.decodeByteArray(abyte0, 0, i, options), "BitmapFactory returned null");
        CloseableReference.closeSafely(closeablereference);
        return bitmap;
    }

    public CloseableReference decodeFromEncodedImage(EncodedImage encodedimage, android.graphics.Bitmap.Config config)
    {
        return super.decodeFromEncodedImage(encodedimage, config);
    }

    protected Bitmap decodeJPEGByteArrayAsPurgeable(CloseableReference closeablereference, int i, android.graphics.BitmapFactory.Options options)
    {
        boolean flag = false;
        byte abyte0[];
        PooledByteBuffer pooledbytebuffer;
        byte abyte1[];
        int j;
        if(endsWithEOI(closeablereference, i))
            abyte0 = null;
        else
            abyte0 = EOI;
        pooledbytebuffer = (PooledByteBuffer)closeablereference.get();
        if(i <= pooledbytebuffer.size())
            flag = true;
        Preconditions.checkArgument(flag);
        closeablereference = mFlexByteArrayPool.get(i + 2);
        abyte1 = (byte[])closeablereference.get();
        pooledbytebuffer.read(0, abyte1, 0, i);
        j = i;
        if(abyte0 == null)
            return null;
        putEOI(abyte1, i);
        j = i + 2;
        Bitmap bitmap = (Bitmap)Preconditions.checkNotNull(BitmapFactory.decodeByteArray(abyte1, 0, j, options), "BitmapFactory returned null");
        CloseableReference.closeSafely(closeablereference);
        return bitmap;
    }

    public CloseableReference decodeJPEGFromEncodedImage(EncodedImage encodedimage, android.graphics.Bitmap.Config config, int i)
    {
        return super.decodeJPEGFromEncodedImage(encodedimage, config, i);
    }

    public CloseableReference pinBitmap(Bitmap bitmap)
    {
        return super.pinBitmap(bitmap);
    }

    private final FlexByteArrayPool mFlexByteArrayPool;
}
