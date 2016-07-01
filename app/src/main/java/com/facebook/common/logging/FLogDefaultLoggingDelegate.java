

package com.facebook.common.logging;

import android.util.Log;
import java.io.PrintWriter;
import java.io.StringWriter;

// Referenced classes of package com.facebook.common.logging:
//            LoggingDelegate

public class FLogDefaultLoggingDelegate
    implements LoggingDelegate
{

    private FLogDefaultLoggingDelegate()
    {
        mApplicationTag = "unknown";
        mMinimumLoggingLevel = 5;
    }

    public static FLogDefaultLoggingDelegate getInstance()
    {
        return sInstance;
    }

    private static String getMsg(String s, Throwable throwable)
    {
        return (new StringBuilder()).append(s).append('\n').append(getStackTraceString(throwable)).toString();
    }

    private static String getStackTraceString(Throwable throwable)
    {
        if(throwable == null)
        {
            return "";
        } else
        {
            StringWriter stringwriter = new StringWriter();
            throwable.printStackTrace(new PrintWriter(stringwriter));
            return stringwriter.toString();
        }
    }

    private String prefixTag(String s)
    {
        String s1 = s;
        if(mApplicationTag != null)
            s1 = (new StringBuilder()).append(mApplicationTag).append(":").append(s).toString();
        return s1;
    }

    private void println(int j, String s, String s1)
    {
        Log.println(j, prefixTag(s), s1);
    }

    private void println(int j, String s, String s1, Throwable throwable)
    {
        Log.println(j, prefixTag(s), getMsg(s1, throwable));
    }

    public void d(String s, String s1)
    {
        println(3, s, s1);
    }

    public void d(String s, String s1, Throwable throwable)
    {
        println(3, s, s1, throwable);
    }

    public void e(String s, String s1)
    {
        println(6, s, s1);
    }

    public void e(String s, String s1, Throwable throwable)
    {
        println(6, s, s1, throwable);
    }

    public int getMinimumLoggingLevel()
    {
        return mMinimumLoggingLevel;
    }

    public void i(String s, String s1)
    {
        println(4, s, s1);
    }

    public void i(String s, String s1, Throwable throwable)
    {
        println(4, s, s1, throwable);
    }

    public boolean isLoggable(int j)
    {
        return mMinimumLoggingLevel <= j;
    }

    public void log(int j, String s, String s1)
    {
        println(j, s, s1);
    }

    public void setApplicationTag(String s)
    {
        mApplicationTag = s;
    }

    public void setMinimumLoggingLevel(int j)
    {
        mMinimumLoggingLevel = j;
    }

    public void v(String s, String s1)
    {
        println(2, s, s1);
    }

    public void v(String s, String s1, Throwable throwable)
    {
        println(2, s, s1, throwable);
    }

    public void w(String s, String s1)
    {
        println(5, s, s1);
    }

    public void w(String s, String s1, Throwable throwable)
    {
        println(5, s, s1, throwable);
    }

    public void wtf(String s, String s1)
    {
        println(6, s, s1);
    }

    public void wtf(String s, String s1, Throwable throwable)
    {
        println(6, s, s1, throwable);
    }

    public static final FLogDefaultLoggingDelegate sInstance = new FLogDefaultLoggingDelegate();
    private String mApplicationTag;
    private int mMinimumLoggingLevel;

}
