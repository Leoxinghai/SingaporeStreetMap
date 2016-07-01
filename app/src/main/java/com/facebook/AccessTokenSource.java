// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook;


public enum AccessTokenSource
{

	NONE("NONE", 0, false),
	FACEBOOK_APPLICATION_WEB("FACEBOOK_APPLICATION_WEB", 1, true),
	FACEBOOK_APPLICATION_NATIVE("FACEBOOK_APPLICATION_NATIVE", 2, true),
	FACEBOOK_APPLICATION_SERVICE("FACEBOOK_APPLICATION_SERVICE", 3, true),
	WEB_VIEW("WEB_VIEW", 4, false),
	TEST_USER("TEST_USER", 5, true),
	CLIENT_TOKEN("CLIENT_TOKEN", 6, true);

	String sType;
	int iType;
	boolean canExtendToken;

    private AccessTokenSource(String s, int i, boolean flag)
    {
		sType = s;
		iType = i;

        canExtendToken = flag;
    }


    boolean canExtendToken()
    {
        return canExtendToken;
    }

}
