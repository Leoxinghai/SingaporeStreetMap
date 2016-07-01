// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v7.internal.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.graphics.ColorUtils;
import android.util.TypedValue;

public class ThemeUtils
{

    public ThemeUtils()
    {
    }

    public static ColorStateList createDisabledStateList(int i, int j)
    {
        int ai[][] = new int[2][];
        int ai1[] = new int[2];
        ai[0] = DISABLED_STATE_SET;
        ai1[0] = j;
        j = 0 + 1;
        ai[j] = EMPTY_STATE_SET;
        ai1[j] = i;
        return new ColorStateList(ai, ai1);
    }

    public static int getDisabledThemeAttrColor(Context context, int i)
    {
        ColorStateList colorstatelist = getThemeAttrColorStateList(context, i);
        if(colorstatelist != null && colorstatelist.isStateful())
        {
            return colorstatelist.getColorForState(DISABLED_STATE_SET, colorstatelist.getDefaultColor());
        } else
        {
            TypedValue typedvalue = getTypedValue();
            context.getTheme().resolveAttribute(0x1010033, typedvalue, true);
            return getThemeAttrColor(context, i, typedvalue.getFloat());
        }
    }

    public static int getThemeAttrColor(Context context, int i)
    {
        TEMP_ARRAY[0] = i;
        TypedArray typedArray = context.obtainStyledAttributes(null, TEMP_ARRAY);
        i = typedArray.getColor(0, 0);
        typedArray.recycle();
        return i;
    }

    static int getThemeAttrColor(Context context, int i, float f)
    {
        i = getThemeAttrColor(context, i);
        return ColorUtils.setAlphaComponent(i, Math.round((float)Color.alpha(i) * f));
    }

    public static ColorStateList getThemeAttrColorStateList(Context context, int i)
    {
        TEMP_ARRAY[0] = i;
        TypedArray typedArray = context.obtainStyledAttributes(null, TEMP_ARRAY);
        ColorStateList colorstatelist = typedArray.getColorStateList(0);
        typedArray.recycle();
        return colorstatelist;
    }

    private static TypedValue getTypedValue()
    {
        TypedValue typedvalue1 = (TypedValue)TL_TYPED_VALUE.get();
        TypedValue typedvalue = typedvalue1;
        if(typedvalue1 == null)
        {
            typedvalue = new TypedValue();
            TL_TYPED_VALUE.set(typedvalue);
        }
        return typedvalue;
    }

    static final int ACTIVATED_STATE_SET[] = {
        0x10102fe
    };
    static final int CHECKED_STATE_SET[] = {
        0x10100a0
    };
    static final int DISABLED_STATE_SET[] = {
        0xfefeff62
    };
    static final int EMPTY_STATE_SET[] = new int[0];
    static final int FOCUSED_STATE_SET[] = {
        0x101009c
    };
    static final int NOT_PRESSED_OR_FOCUSED_STATE_SET[] = {
        0xfefeff59, 0xfefeff64
    };
    static final int PRESSED_STATE_SET[] = {
        0x10100a7
    };
    static final int SELECTED_STATE_SET[] = {
        0x10100a1
    };
    private static final int TEMP_ARRAY[] = new int[1];
    private static final ThreadLocal TL_TYPED_VALUE = new ThreadLocal();

}
