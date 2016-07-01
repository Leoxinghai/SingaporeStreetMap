// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.internal;

import android.content.Context;
import com.facebook.LoggingBehavior;
import java.io.*;
import java.net.*;

// Referenced classes of package com.facebook.internal:
//            FileLruCache, Logger, Utility

class ImageResponseCache
{
    private static class BufferedHttpInputStream extends BufferedInputStream
    {

        public void close()
            throws IOException
        {
            super.close();
            Utility.disconnectQuietly(connection);
        }

        HttpURLConnection connection;

        BufferedHttpInputStream(InputStream inputstream, HttpURLConnection httpurlconnection)
        {
            super(inputstream, 8192);
            connection = httpurlconnection;
        }
    }


    ImageResponseCache()
    {
    }

    static void clearCache(Context context)
    {
        try
        {
            getCache(context).clearCache();
            return;
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            Logger.log(LoggingBehavior.CACHE, 5, TAG, (new StringBuilder()).append("clearCache failed ").append(ex.getMessage()).toString());
        }
    }

    static FileLruCache getCache(Context context)
        throws IOException
    {
        FileLruCache fileLruCache = null;
        if(fileLruCache == null)
            fileLruCache = new FileLruCache(context.getApplicationContext(), TAG, new FileLruCache.Limits());

        return fileLruCache;
    }

    static InputStream getCachedImageStream(URI uri, Context context)
    {
        Object obj = null;
        InputStream inputstream = null;
        if(uri != null)
        {
            inputstream = null;
            if(isCDNURL(uri))
                try
                {
                    inputstream = getCache(context).get(uri.toString());
                }
                // Misplaced declaration of an exception variable
                catch(Exception ex)
                {
                    Logger.log(LoggingBehavior.CACHE, 5, TAG, uri.toString());
                    return null;
                }
        }
        return inputstream;
    }

    static InputStream interceptAndCacheImageStream(Context context, HttpURLConnection httpurlconnection)
        throws IOException
    {
        InputStream inputstream = null;
        if(httpurlconnection.getResponseCode() == 200)
        {
            URL url = httpurlconnection.getURL();
            InputStream inputstream1 = httpurlconnection.getInputStream();
            inputstream = inputstream1;
            try
            {
                if(isCDNURL(url.toURI()))
                    inputstream = getCache(context).interceptAndPut(url.toString(), new BufferedHttpInputStream(inputstream1, httpurlconnection));
            }
            // Misplaced declaration of an exception variable
            catch(Exception ex)
            {
                return inputstream1;
            }
        }
        return inputstream;
    }

    private static boolean isCDNURL(URI uri)
    {
        if(uri != null)
            for(String temp = uri.getHost(); temp.endsWith("fbcdn.net") || temp.startsWith("fbcdn") && temp.endsWith("akamaihd.net");)
                return true;

        return false;
    }

    static final String TAG = ImageResponseCache.class.getSimpleName();
    private static FileLruCache imageCache;

}
