// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.internal;

import android.util.Log;
import com.facebook.LoggingBehavior;
import com.facebook.Settings;
import java.util.*;

// Referenced classes of package com.facebook.internal:
//            Validate

public class Logger
{

    public Logger(LoggingBehavior loggingbehavior, String s)
    {
        priority = 3;
        Validate.notNullOrEmpty(s, "tag");
        behavior = loggingbehavior;
        tag = (new StringBuilder()).append("FacebookSDK.").append(s).toString();
        contents = new StringBuilder();
    }

    public static void log(LoggingBehavior loggingbehavior, int i, String s, String s1)
    {
        if(Settings.isLoggingBehaviorEnabled(loggingbehavior))
        {
            String s2 = replaceStrings(s1);
            s1 = s;
            if(!s.startsWith("FacebookSDK."))
                s1 = (new StringBuilder()).append("FacebookSDK.").append(s).toString();
            Log.println(i, s1, s2);
            if(loggingbehavior == LoggingBehavior.DEVELOPER_ERRORS)
                (new Exception()).printStackTrace();
        }
    }

    public static void log(LoggingBehavior loggingbehavior, String s, String s1)
    {
        log(loggingbehavior, 3, s, s1);
    }

    public static void log(LoggingBehavior loggingbehavior, String s, String s1, Object aobj[])
    {
        if(Settings.isLoggingBehaviorEnabled(loggingbehavior))
            log(loggingbehavior, 3, s, String.format(s1, aobj));
    }

    public static void registerAccessToken(String s)
    {
        if(!Settings.isLoggingBehaviorEnabled(LoggingBehavior.INCLUDE_ACCESS_TOKENS))
            registerStringToReplace(s, "ACCESS_TOKEN_REMOVED");
        return;
    }

    public static void registerStringToReplace(String s, String s1)
    {
        stringsToReplace.put(s, s1);
        return;
    }

    private static String replaceStrings(String s)
    {
        for(Iterator iterator = stringsToReplace.entrySet().iterator(); iterator.hasNext();)
        {
            java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
            s = s.replace((CharSequence)entry.getKey(), (CharSequence)entry.getValue());
        }

        return s;
    }

    private boolean shouldLog()
    {
        return Settings.isLoggingBehaviorEnabled(behavior);
    }

    public void append(String s)
    {
        if(shouldLog())
            contents.append(s);
    }

    public void append(String s, Object aobj[])
    {
        if(shouldLog())
            contents.append(String.format(s, aobj));
    }

    public void append(StringBuilder stringbuilder)
    {
        if(shouldLog())
            contents.append(stringbuilder);
    }

    public void appendKeyValue(String s, Object obj)
    {
        append("  %s:\t%s\n", new Object[] {
            s, obj
        });
    }

    public String getContents()
    {
        return replaceStrings(contents.toString());
    }

    public int getPriority()
    {
        return priority;
    }

    public void log()
    {
        logString(contents.toString());
        contents = new StringBuilder();
    }

    public void logString(String s)
    {
        log(behavior, priority, tag, s);
    }

    public void setPriority(int i)
    {
        Validate.oneOf(Integer.valueOf(i), "value", new Object[] {
            Integer.valueOf(7), Integer.valueOf(3), Integer.valueOf(6), Integer.valueOf(4), Integer.valueOf(2), Integer.valueOf(5)
        });
        priority = i;
    }

    public static final String LOG_TAG_BASE = "FacebookSDK.";
    private static final HashMap stringsToReplace = new HashMap();
    private final LoggingBehavior behavior;
    private StringBuilder contents;
    private int priority;
    private final String tag;

}
