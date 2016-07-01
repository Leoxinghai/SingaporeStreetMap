// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v7.internal.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.PopupWindow;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

// Referenced classes of package android.support.v7.internal.widget:
//            TintTypedArray

public class AppCompatPopupWindow extends PopupWindow
{

    public AppCompatPopupWindow(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(context, attributeset, android.support.v7.appcompat.R.styleable.PopupWindow, i, 0);
        mOverlapAnchor = tintTypedArray.getBoolean(android.support.v7.appcompat.R.styleable.PopupWindow_overlapAnchor, false);
        setBackgroundDrawable(tintTypedArray.getDrawable(android.support.v7.appcompat.R.styleable.PopupWindow_android_popupBackground));
        tintTypedArray.recycle();
        if(android.os.Build.VERSION.SDK_INT < 14)
            wrapOnScrollChangedListener(this);
    }

    private static void wrapOnScrollChangedListener(final PopupWindow popupwindow)
    {
        try
        {
            final Field fieldAnchor = PopupWindow.class.getDeclaredField("mAnchor");
            fieldAnchor.setAccessible(true);
            Field field1 = PopupWindow.class.getDeclaredField("mOnScrollChangedListener");
            field1.setAccessible(true);
            final android.view.ViewTreeObserver.OnScrollChangedListener originalListener = (android.view.ViewTreeObserver.OnScrollChangedListener)field1.get(popupwindow);
            field1.set(popupwindow, new android.view.ViewTreeObserver.OnScrollChangedListener() {

                public void onScrollChanged()
                {
                    try {
                        WeakReference weakreference = (WeakReference) fieldAnchor.get(popupwindow);
                        if (weakreference != null && weakreference.get() != null)
                            originalListener.onScrollChanged();
                        return;
                    } catch(Exception ex) {
                        ex.printStackTrace();
                    }
                }

            });
            return;
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            ex.printStackTrace();
            Log.d("AppCompatPopupWindow", "Exception while installing workaround OnScrollChangedListener", ex);
        }
    }

    public void showAsDropDown(View view, int i, int j)
    {
        int k = j;
        if(android.os.Build.VERSION.SDK_INT < 21)
        {
            k = j;
            if(mOverlapAnchor)
                k = j - view.getHeight();
        }
        super.showAsDropDown(view, i, k);
    }

    public void showAsDropDown(View view, int i, int j, int k)
    {
        int l = j;
        if(android.os.Build.VERSION.SDK_INT < 21)
        {
            l = j;
            if(mOverlapAnchor)
                l = j - view.getHeight();
        }
        super.showAsDropDown(view, i, l, k);
    }

    public void update(View view, int i, int j, int k, int l)
    {
        int i1 = j;
        if(android.os.Build.VERSION.SDK_INT < 21)
        {
            i1 = j;
            if(mOverlapAnchor)
                i1 = j - view.getHeight();
        }
        super.update(view, i, i1, k, l);
    }

    private static final String TAG = "AppCompatPopupWindow";
    private final boolean mOverlapAnchor;
}
