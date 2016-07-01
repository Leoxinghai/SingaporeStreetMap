

package com.facebook.imagepipeline.nativecode;

import android.graphics.Bitmap;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.soloader.SoLoaderShim;
import com.facebook.imageutils.BitmapUtil;

public class Bitmaps
{

    public Bitmaps()
    {
    }

    public static void copyBitmap(Bitmap bitmap, Bitmap bitmap1)
    {
        boolean flag1 = true;
        boolean flag;
        if(bitmap1.getConfig() == bitmap.getConfig())
            flag = true;
        else
            flag = false;
        Preconditions.checkArgument(flag);
        Preconditions.checkArgument(bitmap.isMutable());
        if(bitmap.getWidth() == bitmap1.getWidth())
            flag = true;
        else
            flag = false;
        Preconditions.checkArgument(flag);
        if(bitmap.getHeight() == bitmap1.getHeight())
            flag = flag1;
        else
            flag = false;
        Preconditions.checkArgument(flag);
        nativeCopyBitmap(bitmap, bitmap.getRowBytes(), bitmap1, bitmap1.getRowBytes(), bitmap.getHeight());
    }

    private static native void nativeCopyBitmap(Bitmap bitmap, int i, Bitmap bitmap1, int j, int k);

    private static native void nativePinBitmap(Bitmap bitmap);

    public static void pinBitmap(Bitmap bitmap)
    {
        Preconditions.checkNotNull(bitmap);
        nativePinBitmap(bitmap);
    }

    public static void reconfigureBitmap(Bitmap bitmap, int i, int j, android.graphics.Bitmap.Config config)
    {
        boolean flag;
        if(bitmap.getAllocationByteCount() >= i * j * BitmapUtil.getPixelSizeForBitmapConfig(config))
            flag = true;
        else
            flag = false;
        Preconditions.checkArgument(flag);
        bitmap.reconfigure(i, j, config);
    }

    static
    {
        SoLoaderShim.loadLibrary("bitmaps");
    }
}
