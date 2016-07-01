

package com.facebook.imageutils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Pair;
import com.facebook.common.internal.Preconditions;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

public final class BitmapUtil
{

    public BitmapUtil()
    {
    }

    public static Pair decodeDimensions(InputStream inputstream)
    {
        ByteBuffer bytebuffer;
        Object obj;
        Object obj1;
        obj = null;
        Pair temp2 = null;
        Preconditions.checkNotNull(inputstream);
        bytebuffer = (ByteBuffer) DECODE_BUFFERS.acquire();
        try {

            if (bytebuffer == null)
                bytebuffer = ByteBuffer.allocate(16384);
            BitmapFactory.Options temp = new android.graphics.BitmapFactory.Options();
            temp.inJustDecodeBounds = true;
            temp.inTempStorage = bytebuffer.array();
            BitmapFactory.decodeStream(inputstream, null, ((android.graphics.BitmapFactory.Options) (temp)));
            int i = -1;
            if (((android.graphics.BitmapFactory.Options) (temp)).outWidth != -1) {
                i = ((android.graphics.BitmapFactory.Options) (temp)).outHeight;
            }

            if (i != -1) {
                temp2 = new Pair(Integer.valueOf(temp.outWidth), Integer.valueOf(temp.outHeight));
            }
        } catch(Exception ex) {
            DECODE_BUFFERS.release(bytebuffer);
            throw ex;
        }

        DECODE_BUFFERS.release(bytebuffer);
        return temp2;
    }

    public static Pair decodeDimensions(byte abyte0[])
    {
        return decodeDimensions(((InputStream) (new ByteArrayInputStream(abyte0))));
    }

    public static int getPixelSizeForBitmapConfig(android.graphics.Bitmap.Config config)
    {
        byte byte0 = 2;
//        config[android.graphics.Bitmap.Config.ARGB_8888.ordinal()] = 1;
//        config[android.graphics.Bitmap.Config.ALPHA_8.ordinal()] = 2;
//        config[android.graphics.Bitmap.Config.ARGB_4444.ordinal()] = 3;
        switch(config.ordinal())
        {
        default:
            throw new UnsupportedOperationException("The provided Bitmap.Config is not supported");

        case 1: // '\001'
            byte0 = 4;
            // fall through

        case 3: // '\003'
        case 4: // '\004'
            return 2;

        case 2: // '\002'
            return 1;
        }
    }

    public static int getSizeInByteForBitmap(int i, int j, android.graphics.Bitmap.Config config)
    {
        return i * j * getPixelSizeForBitmapConfig(config);
    }

    public static int getSizeInBytes(Bitmap bitmap)
    {
        if(bitmap == null)
            return 0;
        if(android.os.Build.VERSION.SDK_INT <= 19) {
            if(android.os.Build.VERSION.SDK_INT >= 12)
                return bitmap.getByteCount();
            else
                return bitmap.getWidth() * bitmap.getRowBytes();

        }
        int i = bitmap.getAllocationByteCount();
        return i;
    }

    public static final int ALPHA_8_BYTES_PER_PIXEL = 1;
    public static final int ARGB_4444_BYTES_PER_PIXEL = 2;
    public static final int ARGB_8888_BYTES_PER_PIXEL = 4;
    private static final android.support.v4.util.Pools.SynchronizedPool DECODE_BUFFERS = new android.support.v4.util.Pools.SynchronizedPool(12);
    private static final int DECODE_BUFFER_SIZE = 16384;
    public static final float MAX_BITMAP_SIZE = 2048F;
    private static final int POOL_SIZE = 12;
    public static final int RGB_565_BYTES_PER_PIXEL = 2;

}
