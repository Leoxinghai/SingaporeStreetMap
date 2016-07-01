// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook;


public enum SessionDefaultAudience
{
	NONE("NONE", 0, null),
	ONLY_ME("ONLY_ME", 1, "only_me"),
	FRIENDS("FRIENDS", 2, "friends"),
	EVERYONE("EVERYONE", 3, "everyone");

    private SessionDefaultAudience(String s, int i, String s1)
    {

        nativeProtocolAudience = s1;
    }

    String nativeProtocolAudience;
    public String getNativeProtocolAudience()
    {
        return nativeProtocolAudience;
    }

}
