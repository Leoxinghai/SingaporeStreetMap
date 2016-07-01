// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v7.internal.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
//import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ViewUtils
{

    private ViewUtils()
    {
    }

    public static int combineMeasuredStates(int i, int j)
    {
        return i | j;
    }

    public static void computeFitSystemWindows(View view, Rect rect, Rect rect1)
    {
        try {
            if (sComputeFitSystemWindowsMethod != null) {
                sComputeFitSystemWindowsMethod.invoke(view, new Object[]{
                        rect, rect1
                });
                return;
            }
        } catch(Exception ex) {
            Log.d("ViewUtils", "Could not invoke computeFitSystemWindows", ex);
            return;
        }

    }

    public static boolean isLayoutRtl(View view)
    {
        return ViewCompat.getLayoutDirection(view) == 1;
    }

    public static void makeOptionalFitsSystemWindows(View view)
    {
        try {
            if (android.os.Build.VERSION.SDK_INT < 16) {
                Log.d("ViewUtils", "Could not find method makeOptionalFitsSystemWindows. Oh well...");
                return;

            }
            Method method = view.getClass().getMethod("makeOptionalFitsSystemWindows", new Class[0]);
            if (!method.isAccessible())
                method.setAccessible(true);
            method.invoke(view, new Object[0]);
            return;
        } catch(Exception ex) {

            Log.d("ViewUtils", "Could not invoke makeOptionalFitsSystemWindows", ex);
            return;
        }

    }

    public static Context themifyContext(Context context, AttributeSet attributeset, boolean flag, boolean flag1)
    {
        TypedArray typedArray = context.obtainStyledAttributes(attributeset, android.support.v7.appcompat.R.styleable.View, 0, 0);
        int i = 0;
        if(flag)
            i = typedArray.getResourceId(android.support.v7.appcompat.R.styleable.View_android_theme, 0);
        int k = i;
        if(flag1)
        {
            k = i;
            if(i == 0)
            {
                int j = typedArray.getResourceId(android.support.v7.appcompat.R.styleable.View_theme, 0);
                k = j;
                if(j != 0)
                {
                    Log.i("ViewUtils", "app:theme is now deprecated. Please move to using android:theme instead.");
                    k = j;
                }
            }
        }
        typedArray.recycle();
        if(k == 0)
            return context;
        /*
        if(context instanceof ContextThemeWrapper)
        {

            if(((ContextThemeWrapper)context).getThemeResId() == k)
                return context;
        }
        Context temp = new ContextThemeWrapper(context, k);
        */
        return null;
        //return temp;
    }

    private static final String TAG = "ViewUtils";
    private static Method sComputeFitSystemWindowsMethod;

    static 
    {
        if(android.os.Build.VERSION.SDK_INT < 18) {
            Log.d("ViewUtils", "Could not find method computeFitSystemWindows. Oh well.");
        }
        try {
            sComputeFitSystemWindowsMethod = View.class.getDeclaredMethod("computeFitSystemWindows", new Class[]{
                    Rect.class, Rect.class
            });
            if (!sComputeFitSystemWindowsMethod.isAccessible())
                sComputeFitSystemWindowsMethod.setAccessible(true);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
