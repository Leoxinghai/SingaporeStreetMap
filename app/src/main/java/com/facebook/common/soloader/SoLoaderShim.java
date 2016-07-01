// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.common.soloader;


public class SoLoaderShim
{
    public static class DefaultHandler
        implements Handler
    {

        public void loadLibrary(String s)
        {
            System.loadLibrary(s);
        }

        public DefaultHandler()
        {
        }
    }

    public static interface Handler
    {

        public abstract void loadLibrary(String s);
    }


    public SoLoaderShim()
    {
    }

    public static void loadLibrary(String s)
    {
        sHandler.loadLibrary(s);
    }

    public static void setHandler(Handler handler)
    {
        if(handler == null)
        {
            throw new NullPointerException("Handler cannot be null");
        } else
        {
            sHandler = handler;
            return;
        }
    }

    public static void setInTestMode()
    {
        setHandler(new Handler() {

            public void loadLibrary(String s)
            {
            }

        }
);
    }

    private static Handler sHandler = new DefaultHandler();

}
