

package com.facebook.imagepipeline.common;

import com.facebook.common.internal.Preconditions;
import com.facebook.common.util.HashCodeUtil;
import java.util.Locale;

public class ResizeOptions
{

    public ResizeOptions(int i, int j)
    {
        boolean flag1 = true;
        boolean flag;
        if(i > 0)
            flag = true;
        else
            flag = false;
        Preconditions.checkArgument(flag);
        if(j > 0)
            flag = flag1;
        else
            flag = false;
        Preconditions.checkArgument(flag);
        width = i;
        height = j;
    }

    public boolean equals(Object obj)
    {
        if(obj != this)
        {
            if(!(obj instanceof ResizeOptions))
                return false;
            obj = (ResizeOptions)obj;
            if(width != ((ResizeOptions) (obj)).width || height != ((ResizeOptions) (obj)).height)
                return false;
        }
        return true;
    }

    public int hashCode()
    {
        return HashCodeUtil.hashCode(width, height);
    }

    public String toString()
    {
        return String.format((Locale)null, "%dx%d", new Object[] {
            Integer.valueOf(width), Integer.valueOf(height)
        });
    }

    public final int height;
    public final int width;
}
