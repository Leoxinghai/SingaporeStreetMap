// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.common;


public enum Priority
{

	LOW("LOW", 0),
	MEDIUM("MEDIUM", 1),
	HIGH("HIGH", 2);

    String sType;
    int iType;
    private Priority(String s, int i)
    {

        sType = s;
        iType = i;
    }

    public static Priority getHigherPriority(Priority priority, Priority priority1)
    {
        if(priority != null)
        {
            if(priority1 == null)
                return priority;
            if(priority.ordinal() > priority1.ordinal())
                return priority;
        }
        return priority1;
    }

}
