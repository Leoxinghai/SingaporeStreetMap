// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook;


public enum LoggingBehavior
{

	REQUESTS("REQUESTS", 0),
	INCLUDE_ACCESS_TOKENS("INCLUDE_ACCESS_TOKENS", 1),
	INCLUDE_RAW_RESPONSES("INCLUDE_RAW_RESPONSES", 2),
	CACHE("CACHE", 3),
	APP_EVENTS("APP_EVENTS", 4),
	DEVELOPER_ERRORS("DEVELOPER_ERRORS", 5);

	String sType;
	int iType;
    private LoggingBehavior(String s, int i)
    {
       sType = s;
		iType = i;
    }

}
