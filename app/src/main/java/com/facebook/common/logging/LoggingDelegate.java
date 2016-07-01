

package com.facebook.common.logging;


public interface LoggingDelegate
{

    public abstract void d(String s, String s1);

    public abstract void d(String s, String s1, Throwable throwable);

    public abstract void e(String s, String s1);

    public abstract void e(String s, String s1, Throwable throwable);

    public abstract int getMinimumLoggingLevel();

    public abstract void i(String s, String s1);

    public abstract void i(String s, String s1, Throwable throwable);

    public abstract boolean isLoggable(int j);

    public abstract void log(int j, String s, String s1);

    public abstract void setMinimumLoggingLevel(int j);

    public abstract void v(String s, String s1);

    public abstract void v(String s, String s1, Throwable throwable);

    public abstract void w(String s, String s1);

    public abstract void w(String s, String s1, Throwable throwable);

    public abstract void wtf(String s, String s1);

    public abstract void wtf(String s, String s1, Throwable throwable);
}
