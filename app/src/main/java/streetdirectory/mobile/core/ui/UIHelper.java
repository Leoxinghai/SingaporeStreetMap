// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.core.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.FrameLayout;
import streetdirectory.mobile.SDApplication;

public class UIHelper
{

    public UIHelper()
    {
    }

    public static DisplayMetrics getScreenDimension()
    {
        Display display = ((WindowManager)SDApplication.getAppContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        new Point();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        display.getMetrics(displaymetrics);
        return displaymetrics;
    }

    public static boolean isTablet()
    {
        return (SDApplication.getAppContext().getResources().getConfiguration().screenLayout & 0xf) >= 3;
    }

    public static void showSdDialog(Context context, final View view)
    {
            final View tView = ((Activity)context).findViewById(0x1020002);
            if(tView instanceof FrameLayout)
            {
                if(((FrameLayout)tView).getChildCount() < 2) {
                    ((FrameLayout)tView).addView(view);
                    ((FrameLayout)tView).postDelayed(new Runnable() {

                        public void run() {
                            ((FrameLayout)tView).removeView(view);
                        }

                    }, 5000L);
                    return;
                }
            }
            return;
    }

    public static void showViewDialog(Context context, View view)
    {
    }

    public static float toPixel(float f)
    {
        if(screenDensity <= 0.0F)
            screenDensity = SDApplication.getAppContext().getResources().getDisplayMetrics().density;
        return screenDensity * f + 0.5F;
    }

    public static float screenDensity = 0.0F;

}
