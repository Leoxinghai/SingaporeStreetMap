// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.core;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.lang.ref.WeakReference;

// Referenced classes of package streetdirectory.mobile.core:
//            SDAsyncTask

public class BitmapResourceLoader extends SDAsyncTask
{

    public BitmapResourceLoader(Context context, int i, int j, int k)
    {
        resourceId = 0;
        _contextReference = new WeakReference(context);
        _width = j;
        _height = k;
        resourceId = i;
    }

    public static int calculateInSampleSize(android.graphics.BitmapFactory.Options options, int i, int j)
    {
        int i1;
        int l = options.outHeight;
        i1 = options.outWidth;
        int k = 1;
        if(l > j || i1 > i)
        {
            if(i1 <= l)
                return Math.round((float)i1 / (float)i);
            k = Math.round((float)l / (float)j);
        }
        return k;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources resources, int i, int j, int k)
    {
        android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, i, options);
        options.inSampleSize = calculateInSampleSize(options, j, k);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, i, options);
    }

    public void abort()
    {
        cancel(true);
    }

    protected Bitmap doInBackground(Void avoid[])
    {
        if(_contextReference != null)
            return decodeSampledBitmapFromResource(((Context)_contextReference.get()).getResources(), resourceId, _width, _height);
        else
            return null;
    }

    protected Object doInBackground(Object aobj[])
    {
        return doInBackground((Void[])aobj);
    }

    public void onComplete(Bitmap bitmap)
    {
    }

    protected void onPostExecute(Bitmap bitmap)
    {
        if(isCancelled())
            bitmap = null;
        onComplete(bitmap);
    }

    protected void onPostExecute(Object obj)
    {
        onPostExecute((Bitmap)obj);
    }

    private final WeakReference _contextReference;
    private int _height;
    private int _width;
    public int resourceId;
}
