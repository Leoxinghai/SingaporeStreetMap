// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.common.internal;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Closeables
{

    private Closeables()
    {
    }

    public static void close(Closeable closeable, boolean flag)
        throws IOException
    {
        if(closeable == null)
            return;
        try
        {
            closeable.close();
            return;
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex) {
            if(flag)
            {
                logger.log(Level.WARNING, "IOException thrown while closing Closeable.", ex);
                return;
            } else
            {
                throw ex;
            }
        }
    }

    public static void closeQuietly(InputStream inputstream)
    {
        try
        {
            close(inputstream, true);
            return;
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            throw new AssertionError(ex);
        }
    }

    public static void closeQuietly(Reader reader)
    {
        try
        {
            close(reader, true);
            return;
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            throw new AssertionError(ex);
        }
    }

    static final Logger logger = Logger.getLogger(Closeables.class.getName());

}
