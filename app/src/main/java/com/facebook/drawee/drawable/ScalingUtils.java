

package com.facebook.drawee.drawable;

import android.graphics.Matrix;
import android.graphics.Rect;

public class ScalingUtils
{
    public static enum ScaleType
    {

        FIT_XY("FIT_XY", 0),
        FIT_START("FIT_START", 1),
        FIT_CENTER("FIT_CENTER", 2),
        FIT_END("FIT_END", 3),
        CENTER("CENTER", 4),
        CENTER_INSIDE("CENTER_INSIDE", 5),
        CENTER_CROP("CENTER_CROP", 6),
        FOCUS_CROP("FOCUS_CROP", 7);

        private String sType;
        private int iType;

        private ScaleType(String s, int i)
        {
            sType = s;
            iType = i;

        }
    }


    public ScalingUtils()
    {
    }

    public static Matrix getTransform(Matrix matrix, Rect rect, int i, int j, float f, float f1, ScaleType scaletype)
    {
        int k = rect.width();
        int l = rect.height();
        float f2 = (float)k / (float)i;
        float f3 = (float)l / (float)j;

        switch(scaletype.ordinal())
        {
        default:
            throw new UnsupportedOperationException((new StringBuilder()).append("Unsupported scale type: ").append(scaletype).toString());

        case 1: // '\001'
            f = rect.left;
            f1 = rect.top;
            matrix.setScale(f2, f3);
            matrix.postTranslate((int)(0.5F + f), (int)(0.5F + f1));
            return matrix;

        case 2: // '\002'
            f = Math.min(f2, f3);
            f1 = rect.left;
            f2 = rect.top;
            matrix.setScale(f, f);
            matrix.postTranslate((int)(0.5F + f1), (int)(0.5F + f2));
            return matrix;

        case 3: // '\003'
            f = Math.min(f2, f3);
            f1 = rect.left;
            f2 = k;
            f3 = i;
            float f5 = rect.top;
            float f9 = l;
            float f12 = j;
            matrix.setScale(f, f);
            matrix.postTranslate((int)(0.5F + (f1 + (f2 - f3 * f) * 0.5F)), (int)(0.5F + (f5 + (f9 - f12 * f) * 0.5F)));
            return matrix;

        case 4: // '\004'
            f = Math.min(f2, f3);
            f1 = rect.left;
            f2 = k;
            f3 = i;
            float f6 = rect.top;
            float f10 = l;
            float f13 = j;
            matrix.setScale(f, f);
            matrix.postTranslate((int)(0.5F + (f1 + (f2 - f3 * f))), (int)(0.5F + (f6 + (f10 - f13 * f))));
            return matrix;

        case 5: // '\005'
            f = rect.left;
            f1 = k - i;
            f2 = rect.top;
            f3 = l - j;
            matrix.setTranslate((int)(0.5F + (f + f1 * 0.5F)), (int)(0.5F + (f2 + f3 * 0.5F)));
            return matrix;

        case 6: // '\006'
            f = Math.min(Math.min(f2, f3), 1.0F);
            f1 = rect.left;
            f2 = k;
            f3 = i;
            float f7 = rect.top;
            float f11 = l;
            float f14 = j;
            matrix.setScale(f, f);
            matrix.postTranslate((int)(0.5F + (f1 + (f2 - f3 * f) * 0.5F)), (int)(0.5F + (f7 + (f11 - f14 * f) * 0.5F)));
            return matrix;

        case 7: // '\007'
            if(f3 > f2)
            {
                f2 = f3;
                f = (float)rect.left + ((float)k - (float)i * f2) * 0.5F;
                f1 = rect.top;
            } else
            {
                f = rect.left;
                f1 = (float)rect.top + ((float)l - (float)j * f2) * 0.5F;
            }
            matrix.setScale(f2, f2);
            matrix.postTranslate((int)(0.5F + f), (int)(0.5F + f1));
            return matrix;

        case 8: // '\b'
            break;
        }
        if(f3 > f2)
        {
            f1 = k;
            f2 = i;
            f1 = (float)rect.left + Math.max(Math.min(f1 * 0.5F - f2 * f3 * f, 0.0F), (float)k - (float)i * f3);
            f2 = rect.top;
            f = f3;
        } else
        {
            f = f2;
            f2 = rect.left;
            float f4 = l;
            float f8 = j;
            f4 = (float)rect.top + Math.max(Math.min(f4 * 0.5F - f8 * f * f1, 0.0F), (float)l - (float)j * f);
            f1 = f2;
            f2 = f4;
        }
        matrix.setScale(f, f);
        matrix.postTranslate((int)(0.5F + f1), (int)(0.5F + f2));
        return matrix;
    }
}
