// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.internal;

import android.content.Context;
import com.facebook.LoggingBehavior;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

// Referenced classes of package com.facebook.internal:
//            FileLruCache, Utility, Logger

class UrlRedirectCache
{

    UrlRedirectCache()
    {
    }

    static void cacheUriRedirect(Context context, URI uri, URI uri1)
    {
        Context context1;
        Context context2;
        if(uri == null || uri1 == null)
            return;
        context2 = null;
        context1 = null;
        try
        {
            OutputStream temp = getCache(context).openPutStream(uri.toString(), REDIRECT_CONTENT_TAG);
            context1 = context;
            context2 = context;
            temp.write(uri1.toString().getBytes());
            Utility.closeQuietly(temp);
            return;
        }
        catch(Exception ex)
        {
            //Utility.closeQuietly(ex);
            return;
        }
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
        if(urlRedirectCache == null)
            urlRedirectCache = new FileLruCache(context.getApplicationContext(), TAG, new FileLruCache.Limits());

        return urlRedirectCache;
    }

    static URI getRedirectedUri(Context context, URI uri)
    {
        Object obj;
        Object obj1;
        Object obj2;
        if(uri == null)
            return null;
        String temp = uri.toString();
        obj1 = null;
        obj2 = null;
        obj = null;


    	try {
            FileLruCache filelrucache = getCache(context);
            boolean flag;
            flag = false;
            context = null;
            StringBuilder stringBuilder = new StringBuilder();

		for(;;) {
			obj = filelrucache.get(temp, REDIRECT_CONTENT_TAG);
			if(obj == null)
				break;

			flag = true;
            InputStreamReader inputStreamReader = new InputStreamReader(((java.io.InputStream) (obj)));
			obj = uri;
			obj1 = uri;
			obj2 = uri;
			char buff[] = new char[128];
			obj = uri;
			obj1 = uri;
			obj2 = uri;

			obj = uri;
			obj1 = uri;
			obj2 = uri;
			for(;;) {
				int i = inputStreamReader.read(buff, 0, buff.length);
				if(i <= 0)
					break; /* Loop/switch isn't completed */
				obj = uri;
				obj1 = uri;
				obj2 = uri;
                stringBuilder.append(buff, 0, i);
			}
			obj = uri;
			obj1 = uri;
			obj2 = uri;
			Utility.closeQuietly(inputStreamReader);
			obj = uri;
			obj1 = uri;
			obj2 = uri;
			//uri = stringBuilder.toString();

		}


        if(flag) {
			uri = new URI(stringBuilder.toString());
//			Utility.closeQuietly(context);
			return uri;
		}
        //Utility.closeQuietly(inputStreamReader);
        return null;

        } catch(Exception ex) {
			//Utility.closeQuietly(((java.io.Closeable) (inputStreamReader)));
			return null;
		}
    }

    static final String TAG = UrlRedirectCache.class.getSimpleName();
    private static final String REDIRECT_CONTENT_TAG = (new StringBuilder()).append(TAG).append("_Redirect").toString();
    private static FileLruCache urlRedirectCache;

}
