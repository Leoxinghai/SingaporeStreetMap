

package com.facebook.drawee.drawable;

import android.graphics.drawable.Drawable;

// Referenced classes of package com.facebook.drawee.drawable:
//            TransformAwareDrawable, DrawableProperties, TransformCallback

public class DrawableUtils
{

    public DrawableUtils()
    {
    }

    public static void copyProperties(Drawable drawable, Drawable drawable1)
    {
        if(drawable1 == null || drawable == null || drawable == drawable1)
        {
            return;
        } else
        {
            drawable.setBounds(drawable1.getBounds());
            drawable.setChangingConfigurations(drawable1.getChangingConfigurations());
            drawable.setLevel(drawable1.getLevel());
            drawable.setVisible(drawable1.isVisible(), false);
            drawable.setState(drawable1.getState());
            return;
        }
    }

    public static int getOpacityFromColor(int i)
    {
        i >>>= 24;
        if(i == 255)
            return -1;
        return i != 0 ? -3 : -2;
    }

    public static int multiplyColorAlpha(int i, int j)
    {
        if(j == 255)
            return i;
        if(j == 0)
            return i & 0xffffff;
        else
            return ((i >>> 24) * (j + (j >> 7)) >> 8) << 24 | 0xffffff & i;
    }

    public static void setCallbacks(Drawable drawable, android.graphics.drawable.Drawable.Callback callback, TransformCallback transformcallback)
    {
        if(drawable != null)
        {
            drawable.setCallback(callback);
            if(drawable instanceof TransformAwareDrawable)
                ((TransformAwareDrawable)drawable).setTransformCallback(transformcallback);
        }
    }

    public static void setDrawableProperties(Drawable drawable, DrawableProperties drawableproperties)
    {
        if(drawable == null || drawableproperties == null)
        {
            return;
        } else
        {
            drawableproperties.applyTo(drawable);
            return;
        }
    }
}
