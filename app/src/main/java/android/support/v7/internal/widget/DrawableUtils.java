// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v7.internal.widget;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class DrawableUtils
{

    private DrawableUtils()
    {
    }

    public static Rect getOpticalBounds(Drawable drawable) {
        Object obj;
        drawable = DrawableCompat.unwrap(drawable);
        Rect rect;
        try {
            sInsetsClazz = Class.forName("android.graphics.Insets");
            obj = drawable.getClass().getMethod("getOpticalInsets", new Class[0]).invoke(drawable, new Object[0]);

            if (sInsetsClazz == null || obj == null) {
                rect = INSETS_NONE;
                return rect;

            }

            Field afield[];
            int j;
            rect = new Rect();
            afield = sInsetsClazz.getFields();
            j = afield.length;
            int i = 0;


            for (; i < j;) {
                Field field = afield[i];
                String s = field.getName();
                byte byte0 = -1;
                switch (s.hashCode()) {
                    case 3317767:
                        if (s.equals("left"))
                            byte0 = 0;
                        break;
                    case 115029:
                        if (s.equals("top"))
                            byte0 = 1;
                        break;
                    case 108511772:
                        if (s.equals("right"))
                            byte0 = 2;
                        break;
                    case -1383228885:
                        if (s.equals("bottom"))
                            byte0 = 3;
                        break;
                    default:
                        break;
                }

                switch (byte0) {
                    case 0:
                        rect.left = field.getInt(obj);
                        break;

                    case 1:
                        rect.top = field.getInt(obj);
                        break;
                    case 2:
                        rect.right = field.getInt(obj);
                        break;
                    case 3:
                        rect.bottom = field.getInt(obj);
                        break;
                    default:
                        break;
                }
                i++;
            }
            return rect;

        } catch(Exception ex ) {
            Log.e("DrawableUtils", "Couldn't obtain the optical insets. Ignoring.");
        }
        return INSETS_NONE;




    }

    public static final Rect INSETS_NONE = new Rect();
    private static final String TAG = "DrawableUtils";
    private static Class sInsetsClazz;// = Class.forName("android.graphics.Insets");


}
