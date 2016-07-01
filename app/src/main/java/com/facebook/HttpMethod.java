// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook;


public enum HttpMethod
{

	GET("GET", 0),
	POST("POST", 1),
	DELETE("DELETE", 2);
    String sType;
    int iType;
    private HttpMethod(String s, int i)
    {
        sType = s;
        iType = i;

    }

}
