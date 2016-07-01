

package streetdirectory.mobile.core;

import android.graphics.Bitmap;

public class BitmapTools
{

    public BitmapTools()
    {
    }

    public static int calculateInSampleSize(android.graphics.BitmapFactory.Options options, int i, int j)
    {
        int i1 = options.outHeight;
        int j1 = options.outWidth;
        int l = 1;
        int k = 1;
        if(i1 > j || j1 > i)
        {
            i1 /= 2;
            j1 /= 2;
            do
            {
                l = k;
                if(i1 / k <= j)
                    break;
                l = k;
                if(j1 / k <= i)
                    break;
                k *= 2;
            } while(true);
        }
        return l;
    }

    public static Bitmap scale(Bitmap bitmap, int i, int j)
    {
        return Bitmap.createScaledBitmap(bitmap, i, j, false);
    }

    public static Bitmap scaleProportional(Bitmap bitmap, int i, int j)
    {
        int k = bitmap.getWidth();
        int l = bitmap.getHeight();
        float f;
        if(i > j)
            f = (float)j / (float)l;
        else
            f = (float)i / (float)k;
        return Bitmap.createScaledBitmap(bitmap, Math.round((float)k * f), Math.round((float)l * f), false);
    }

    public static Bitmap scaleToFill(Bitmap bitmap, int i, int j)
    {
        return scaleToFill(bitmap, i, j, 0);
    }

    public static Bitmap scaleToFill(Bitmap bitmap, int i, int j, int k)
    {
        bitmap = scaleProportional(bitmap, i, j);
        int l = 0;
        int i1 = 0;
        if((k & 1) == 1)
            l = (bitmap.getWidth() - i) / 2;
        if((k & 2) == 2)
            i1 = (bitmap.getHeight() - j) / 2;
        Bitmap bitmap1 = Bitmap.createBitmap(bitmap, l, i1, i, j);
        if(bitmap != bitmap1)
            bitmap.recycle();
        return bitmap1;
    }

    public static final int CENTER = 3;
    public static final int CENTER_HORIZONTAL = 1;
    public static final int CENTER_VERTICAL = 2;
    public static final int TOP_LEFT = 0;
}
