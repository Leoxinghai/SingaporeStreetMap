// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package android.support.v7.internal;


public class VersionUtils
{

    private VersionUtils()
    {
    }

    public static boolean isAtLeastL()
    {
        return android.os.Build.VERSION.SDK_INT >= 21;
    }
}
