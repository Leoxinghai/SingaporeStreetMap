

package com.facebook.drawee.view;

import android.view.View;

public class AspectRatioMeasure
{
    public static class Spec
    {

        public int height;
        public int width;

        public Spec()
        {
        }
    }


    public AspectRatioMeasure()
    {
    }

    private static boolean shouldAdjust(int i)
    {
        return i == 0 || i == -2;
    }

    public static void updateMeasureSpec(Spec spec, float f, android.view.ViewGroup.LayoutParams layoutparams, int i, int j)
    {
        if(f > 0.0F && layoutparams != null)
        {
            if(shouldAdjust(layoutparams.height))
            {
                spec.height = android.view.View.MeasureSpec.makeMeasureSpec(View.resolveSize((int)((float)(android.view.View.MeasureSpec.getSize(spec.width) - i) / f + (float)j), spec.height), 0x40000000);
                return;
            }
            if(shouldAdjust(layoutparams.width))
            {
                spec.width = android.view.View.MeasureSpec.makeMeasureSpec(View.resolveSize((int)((float)(android.view.View.MeasureSpec.getSize(spec.height) - j) * f + (float)i), spec.width), 0x40000000);
                return;
            }
        }
    }
}
