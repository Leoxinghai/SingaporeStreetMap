// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v7.internal.widget;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

// Referenced classes of package android.support.v7.internal.widget:
//            TintManager, ResourcesWrapper

public class TintContextWrapper extends ContextWrapper
{
    static class TintResources extends ResourcesWrapper
    {

        public Drawable getDrawable(int i)
            throws android.content.res.Resources.NotFoundException
        {
            Drawable drawable = super.getDrawable(i);
            if(drawable != null)
                mTintManager.tintDrawableUsingColorFilter(i, drawable);
            return drawable;
        }

        private final TintManager mTintManager;

        public TintResources(Resources resources, TintManager tintmanager)
        {
            super(resources);
            mTintManager = tintmanager;
        }
    }


    private TintContextWrapper(Context context)
    {
        super(context);
    }

    public static Context wrap(Context context)
    {
        Object obj = context;
        if(!(context instanceof TintContextWrapper))
            obj = new TintContextWrapper(context);
        return ((Context) (obj));
    }

    public Resources getResources()
    {
        if(mResources == null)
            mResources = new TintResources(super.getResources(), TintManager.get(this));
        return mResources;
    }

    private Resources mResources;
}
