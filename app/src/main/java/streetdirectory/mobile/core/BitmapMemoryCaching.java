// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.core;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class BitmapMemoryCaching extends LruCache
{

    public BitmapMemoryCaching(Context context)
    {
        super((((ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass() * 1024 * 1024) / 8);
    }

    protected int sizeOf(Object obj, Object obj1)
    {
        return sizeOf((String)obj, (Bitmap)obj1);
    }

    protected int sizeOf(String s, Bitmap bitmap)
    {
        return bitmap.getRowBytes() * bitmap.getHeight();
    }
}
