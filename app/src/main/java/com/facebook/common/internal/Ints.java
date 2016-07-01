// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.common.internal;


// Referenced classes of package com.facebook.common.internal:
//            Preconditions

public class Ints
{

    private Ints()
    {
    }

    public static int max(int ai[])
    {
        int j;
        boolean flag;
        if(ai.length > 0)
            flag = true;
        else
            flag = false;
        Preconditions.checkArgument(flag);
        j = ai[0];
        for(int i = 1; i < ai.length;)
        {
            int k = j;
            if(ai[i] > j)
                k = ai[i];
            i++;
            j = k;
        }

        return j;
    }
}
