// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.common.logging;


// Referenced classes of package com.facebook.common.logging:
//            FLogDefaultLoggingDelegate, LoggingDelegate

public class FLog
{

    public FLog()
    {
    }

    public static void d(Class class1, String s)
    {
        if(sHandler.isLoggable(3))
            sHandler.d(getTag(class1), s);
    }

    public static void d(Class class1, String s, Object obj)
    {
        if(sHandler.isLoggable(3))
            sHandler.d(getTag(class1), formatString(s, new Object[] {
                obj
            }));
    }

    public static void d(Class class1, String s, Object obj, Object obj1)
    {
        if(sHandler.isLoggable(3))
            sHandler.d(getTag(class1), formatString(s, new Object[] {
                obj, obj1
            }));
    }

    public static void d(Class class1, String s, Object obj, Object obj1, Object obj2)
    {
        if(sHandler.isLoggable(3))
            sHandler.d(getTag(class1), formatString(s, new Object[] {
                obj, obj1, obj2
            }));
    }

    public static void d(Class class1, String s, Object obj, Object obj1, Object obj2, Object obj3)
    {
        if(sHandler.isLoggable(3))
            sHandler.d(getTag(class1), formatString(s, new Object[] {
                obj, obj1, obj2, obj3
            }));
    }

    public static void d(Class class1, String s, Throwable throwable)
    {
        if(sHandler.isLoggable(3))
            sHandler.d(getTag(class1), s, throwable);
    }

    public static void d(Class class1, String s, Object aobj[])
    {
        if(sHandler.isLoggable(3))
            sHandler.d(getTag(class1), formatString(s, aobj));
    }

    public static void d(Class class1, Throwable throwable, String s, Object aobj[])
    {
        if(sHandler.isLoggable(3))
            sHandler.d(getTag(class1), formatString(s, aobj), throwable);
    }

    public static void d(String s, String s1)
    {
        if(sHandler.isLoggable(3))
            sHandler.d(s, s1);
    }

    public static void d(String s, String s1, Object obj)
    {
        if(sHandler.isLoggable(3))
            sHandler.d(s, formatString(s1, new Object[] {
                obj
            }));
    }

    public static void d(String s, String s1, Object obj, Object obj1)
    {
        if(sHandler.isLoggable(3))
            sHandler.d(s, formatString(s1, new Object[] {
                obj, obj1
            }));
    }

    public static void d(String s, String s1, Object obj, Object obj1, Object obj2)
    {
        if(sHandler.isLoggable(3))
            sHandler.d(s, formatString(s1, new Object[] {
                obj, obj1, obj2
            }));
    }

    public static void d(String s, String s1, Object obj, Object obj1, Object obj2, Object obj3)
    {
        if(sHandler.isLoggable(3))
            sHandler.d(s, formatString(s1, new Object[] {
                obj, obj1, obj2, obj3
            }));
    }

    public static void d(String s, String s1, Throwable throwable)
    {
        if(sHandler.isLoggable(3))
            sHandler.d(s, s1, throwable);
    }

    public static void d(String s, String s1, Object aobj[])
    {
        if(sHandler.isLoggable(3))
            d(s, formatString(s1, aobj));
    }

    public static void d(String s, Throwable throwable, String s1, Object aobj[])
    {
        if(sHandler.isLoggable(3))
            d(s, formatString(s1, aobj), throwable);
    }

    public static void e(Class class1, String s)
    {
        if(sHandler.isLoggable(6))
            sHandler.e(getTag(class1), s);
    }

    public static void e(Class class1, String s, Throwable throwable)
    {
        if(sHandler.isLoggable(6))
            sHandler.e(getTag(class1), s, throwable);
    }

    public static void e(Class class1, String s, Object aobj[])
    {
        if(sHandler.isLoggable(6))
            sHandler.e(getTag(class1), formatString(s, aobj));
    }

    public static void e(Class class1, Throwable throwable, String s, Object aobj[])
    {
        if(sHandler.isLoggable(6))
            sHandler.e(getTag(class1), formatString(s, aobj), throwable);
    }

    public static void e(String s, String s1)
    {
        if(sHandler.isLoggable(6))
            sHandler.e(s, s1);
    }

    public static void e(String s, String s1, Throwable throwable)
    {
        if(sHandler.isLoggable(6))
            sHandler.e(s, s1, throwable);
    }

    public static void e(String s, String s1, Object aobj[])
    {
        if(sHandler.isLoggable(6))
            sHandler.e(s, formatString(s1, aobj));
    }

    public static void e(String s, Throwable throwable, String s1, Object aobj[])
    {
        if(sHandler.isLoggable(6))
            sHandler.e(s, formatString(s1, aobj), throwable);
    }

    private static String formatString(String s, Object aobj[])
    {
        return String.format(null, s, aobj);
    }

    public static int getMinimumLoggingLevel()
    {
        return sHandler.getMinimumLoggingLevel();
    }

    private static String getTag(Class class1)
    {
        return class1.getSimpleName();
    }

    public static void i(Class class1, String s)
    {
        if(sHandler.isLoggable(4))
            sHandler.i(getTag(class1), s);
    }

    public static void i(Class class1, String s, Object obj)
    {
        if(sHandler.isLoggable(4))
            sHandler.i(getTag(class1), formatString(s, new Object[] {
                obj
            }));
    }

    public static void i(Class class1, String s, Object obj, Object obj1)
    {
        if(sHandler.isLoggable(4))
            sHandler.i(getTag(class1), formatString(s, new Object[] {
                obj, obj1
            }));
    }

    public static void i(Class class1, String s, Object obj, Object obj1, Object obj2)
    {
        if(sHandler.isLoggable(4))
            sHandler.i(getTag(class1), formatString(s, new Object[] {
                obj, obj1, obj2
            }));
    }

    public static void i(Class class1, String s, Object obj, Object obj1, Object obj2, Object obj3)
    {
        if(sHandler.isLoggable(4))
            sHandler.i(getTag(class1), formatString(s, new Object[] {
                obj, obj1, obj2, obj3
            }));
    }

    public static void i(Class class1, String s, Throwable throwable)
    {
        if(sHandler.isLoggable(4))
            sHandler.i(getTag(class1), s, throwable);
    }

    public static void i(Class class1, String s, Object aobj[])
    {
        if(sHandler.isLoggable(4))
            sHandler.i(getTag(class1), formatString(s, aobj));
    }

    public static void i(Class class1, Throwable throwable, String s, Object aobj[])
    {
        if(isLoggable(4))
            sHandler.i(getTag(class1), formatString(s, aobj), throwable);
    }

    public static void i(String s, String s1)
    {
        if(sHandler.isLoggable(4))
            sHandler.i(s, s1);
    }

    public static void i(String s, String s1, Object obj)
    {
        if(sHandler.isLoggable(4))
            sHandler.i(s, formatString(s1, new Object[] {
                obj
            }));
    }

    public static void i(String s, String s1, Object obj, Object obj1)
    {
        if(sHandler.isLoggable(4))
            sHandler.i(s, formatString(s1, new Object[] {
                obj, obj1
            }));
    }

    public static void i(String s, String s1, Object obj, Object obj1, Object obj2)
    {
        if(sHandler.isLoggable(4))
            sHandler.i(s, formatString(s1, new Object[] {
                obj, obj1, obj2
            }));
    }

    public static void i(String s, String s1, Object obj, Object obj1, Object obj2, Object obj3)
    {
        if(sHandler.isLoggable(4))
            sHandler.i(s, formatString(s1, new Object[] {
                obj, obj1, obj2, obj3
            }));
    }

    public static void i(String s, String s1, Throwable throwable)
    {
        if(sHandler.isLoggable(4))
            sHandler.i(s, s1, throwable);
    }

    public static void i(String s, String s1, Object aobj[])
    {
        if(sHandler.isLoggable(4))
            sHandler.i(s, formatString(s1, aobj));
    }

    public static void i(String s, Throwable throwable, String s1, Object aobj[])
    {
        if(sHandler.isLoggable(4))
            sHandler.i(s, formatString(s1, aobj), throwable);
    }

    public static boolean isLoggable(int j)
    {
        return sHandler.isLoggable(j);
    }

    public static void setLoggingDelegate(LoggingDelegate loggingdelegate)
    {
        if(loggingdelegate == null)
        {
            throw new IllegalArgumentException();
        } else
        {
            sHandler = loggingdelegate;
            return;
        }
    }

    public static void setMinimumLoggingLevel(int j)
    {
        sHandler.setMinimumLoggingLevel(j);
    }

    public static void v(Class class1, String s)
    {
        if(sHandler.isLoggable(2))
            sHandler.v(getTag(class1), s);
    }

    public static void v(Class class1, String s, Object obj)
    {
        if(sHandler.isLoggable(2))
            sHandler.v(getTag(class1), formatString(s, new Object[] {
                obj
            }));
    }

    public static void v(Class class1, String s, Object obj, Object obj1)
    {
        if(sHandler.isLoggable(2))
            sHandler.v(getTag(class1), formatString(s, new Object[] {
                obj, obj1
            }));
    }

    public static void v(Class class1, String s, Object obj, Object obj1, Object obj2)
    {
        if(isLoggable(2))
            v(class1, formatString(s, new Object[] {
                obj, obj1, obj2
            }));
    }

    public static void v(Class class1, String s, Object obj, Object obj1, Object obj2, Object obj3)
    {
        if(sHandler.isLoggable(2))
            sHandler.v(getTag(class1), formatString(s, new Object[] {
                obj, obj1, obj2, obj3
            }));
    }

    public static void v(Class class1, String s, Throwable throwable)
    {
        if(sHandler.isLoggable(2))
            sHandler.v(getTag(class1), s, throwable);
    }

    public static void v(Class class1, String s, Object aobj[])
    {
        if(sHandler.isLoggable(2))
            sHandler.v(getTag(class1), formatString(s, aobj));
    }

    public static void v(Class class1, Throwable throwable, String s, Object aobj[])
    {
        if(sHandler.isLoggable(2))
            sHandler.v(getTag(class1), formatString(s, aobj), throwable);
    }

    public static void v(String s, String s1)
    {
        if(sHandler.isLoggable(2))
            sHandler.v(s, s1);
    }

    public static void v(String s, String s1, Object obj)
    {
        if(sHandler.isLoggable(2))
            sHandler.v(s, formatString(s1, new Object[] {
                obj
            }));
    }

    public static void v(String s, String s1, Object obj, Object obj1)
    {
        if(sHandler.isLoggable(2))
            sHandler.v(s, formatString(s1, new Object[] {
                obj, obj1
            }));
    }

    public static void v(String s, String s1, Object obj, Object obj1, Object obj2)
    {
        if(sHandler.isLoggable(2))
            sHandler.v(s, formatString(s1, new Object[] {
                obj, obj1, obj2
            }));
    }

    public static void v(String s, String s1, Object obj, Object obj1, Object obj2, Object obj3)
    {
        if(sHandler.isLoggable(2))
            sHandler.v(s, formatString(s1, new Object[] {
                obj, obj1, obj2, obj3
            }));
    }

    public static void v(String s, String s1, Throwable throwable)
    {
        if(sHandler.isLoggable(2))
            sHandler.v(s, s1, throwable);
    }

    public static void v(String s, String s1, Object aobj[])
    {
        if(sHandler.isLoggable(2))
            sHandler.v(s, formatString(s1, aobj));
    }

    public static void v(String s, Throwable throwable, String s1, Object aobj[])
    {
        if(sHandler.isLoggable(2))
            sHandler.v(s, formatString(s1, aobj), throwable);
    }

    public static void w(Class class1, String s)
    {
        if(sHandler.isLoggable(5))
            sHandler.w(getTag(class1), s);
    }

    public static void w(Class class1, String s, Throwable throwable)
    {
        if(sHandler.isLoggable(5))
            sHandler.w(getTag(class1), s, throwable);
    }

    public static void w(Class class1, String s, Object aobj[])
    {
        if(sHandler.isLoggable(5))
            sHandler.w(getTag(class1), formatString(s, aobj));
    }

    public static void w(Class class1, Throwable throwable, String s, Object aobj[])
    {
        if(isLoggable(5))
            w(class1, formatString(s, aobj), throwable);
    }

    public static void w(String s, String s1)
    {
        if(sHandler.isLoggable(5))
            sHandler.w(s, s1);
    }

    public static void w(String s, String s1, Throwable throwable)
    {
        if(sHandler.isLoggable(5))
            sHandler.w(s, s1, throwable);
    }

    public static void w(String s, String s1, Object aobj[])
    {
        if(sHandler.isLoggable(5))
            sHandler.w(s, formatString(s1, aobj));
    }

    public static void w(String s, Throwable throwable, String s1, Object aobj[])
    {
        if(sHandler.isLoggable(5))
            sHandler.w(s, formatString(s1, aobj), throwable);
    }

    public static void wtf(Class class1, String s)
    {
        if(sHandler.isLoggable(6))
            sHandler.e(getTag(class1), s);
    }

    public static void wtf(Class class1, String s, Throwable throwable)
    {
        if(sHandler.isLoggable(6))
            sHandler.wtf(getTag(class1), s, throwable);
    }

    public static void wtf(Class class1, String s, Object aobj[])
    {
        if(sHandler.isLoggable(6))
            sHandler.wtf(getTag(class1), formatString(s, aobj));
    }

    public static void wtf(Class class1, Throwable throwable, String s, Object aobj[])
    {
        if(sHandler.isLoggable(6))
            sHandler.wtf(getTag(class1), formatString(s, aobj), throwable);
    }

    public static void wtf(String s, String s1)
    {
        if(sHandler.isLoggable(6))
            sHandler.e(s, s1);
    }

    public static void wtf(String s, String s1, Throwable throwable)
    {
        if(sHandler.isLoggable(6))
            sHandler.wtf(s, s1, throwable);
    }

    public static void wtf(String s, String s1, Object aobj[])
    {
        if(sHandler.isLoggable(6))
            sHandler.wtf(s, formatString(s1, aobj));
    }

    public static void wtf(String s, Throwable throwable, String s1, Object aobj[])
    {
        if(sHandler.isLoggable(6))
            sHandler.wtf(s, formatString(s1, aobj), throwable);
    }

    public static final int ASSERT = 7;
    public static final int DEBUG = 3;
    public static final int ERROR = 6;
    public static final int INFO = 4;
    public static final int VERBOSE = 2;
    public static final int WARN = 5;
    private static LoggingDelegate sHandler = FLogDefaultLoggingDelegate.getInstance();

}
