// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.internal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import com.facebook.FacebookException;
import com.facebook.Request;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

// Referenced classes of package com.facebook.internal:
//            WorkQueue, ImageRequest, ImageResponseCache, UrlRedirectCache,
//            Utility, ImageResponse

public class ImageDownloader
{
    private static class CacheReadWorkItem
        implements Runnable
    {

        public void run()
        {
            ImageDownloader.readFromCache(key, context, allowCachedRedirects);
        }

        private boolean allowCachedRedirects;
        private Context context;
        private RequestKey key;

        CacheReadWorkItem(Context context1, RequestKey requestkey, boolean flag)
        {
            context = context1;
            key = requestkey;
            allowCachedRedirects = flag;
        }
    }

    private static class DownloadImageWorkItem
        implements Runnable
    {

        public void run()
        {
            ImageDownloader.download(key, context);
        }

        private Context context;
        private RequestKey key;

        DownloadImageWorkItem(Context context1, RequestKey requestkey)
        {
            context = context1;
            key = requestkey;
        }
    }

    private static class DownloaderContext
    {

        boolean isCancelled;
        ImageRequest request;
        WorkQueue.WorkItem workItem;

        private DownloaderContext()
        {
        }

    }

    private static class RequestKey
    {

        public boolean equals(Object obj)
        {
			boolean flag1 = false;
			boolean flag = flag1;
			if(obj != null)
			{
				flag = flag1;
				if(obj instanceof RequestKey)
				{
					obj = (RequestKey)obj;
					if(((RequestKey) (obj)).uri != uri || ((RequestKey) (obj)).tag != tag)
						return false;
					flag = true;
				}
			}
			return flag;

        }

        public int hashCode()
        {
            return (uri.hashCode() + 1073) * 37 + tag.hashCode();
        }

        private static final int HASH_MULTIPLIER = 37;
        private static final int HASH_SEED = 29;
        Object tag;
        URI uri;

        RequestKey(URI uri1, Object obj)
        {
            uri = uri1;
            tag = obj;
        }
    }


    public ImageDownloader()
    {
    }

    public static boolean cancelRequest(ImageRequest imagerequest)
    {
        RequestKey requestkey;
        boolean flag;
        flag = false;
        requestkey = new RequestKey(imagerequest.getImageUri(), imagerequest.getCallerTag());
        //imagerequest = pendingRequests;
        DownloaderContext downloadercontext = (DownloaderContext)pendingRequests.get(requestkey);
        if(downloadercontext != null) {
			flag = true;
			if(!downloadercontext.workItem.cancel())
				downloadercontext.isCancelled = true;
			else
				pendingRequests.remove(requestkey);
		}
        return flag;
    }

    public static void clearCache(Context context)
    {
        ImageResponseCache.clearCache(context);
        UrlRedirectCache.clearCache(context);
    }

    private static void download(RequestKey requestkey, Context context)
    {
        Object obj1;
        Object obj2;
        Object obj3;
        Object obj5;
        Object obj6;
        Object obj7;
        Object obj8;
        Object obj9;
        Object obj10;
        Object obj11;
        Object obj12;
        boolean flag;
        boolean flag1;
        boolean flag2;
        boolean flag3;
        boolean flag4;
        HttpURLConnection httpurlconnection1;
        HttpURLConnection httpurlconnection2;
        HttpURLConnection httpurlconnection3;
        httpurlconnection2 = null;
        httpurlconnection3 = null;
        httpurlconnection1 = null;
        obj10 = null;
        obj11 = null;
        obj12 = null;
        obj9 = null;
        obj8 = null;
        obj7 = null;
        obj6 = null;
        obj1 = null;
        obj2 = null;
        obj3 = null;
        flag3 = true;
        flag4 = true;
        flag2 = true;
        flag = true;
        flag1 = true;
        int i;

        try {

            HttpURLConnection httpurlconnection = (HttpURLConnection) (new URL(requestkey.uri.toString())).openConnection();
            httpurlconnection1 = httpurlconnection;
            httpurlconnection2 = httpurlconnection;
            httpurlconnection3 = httpurlconnection;
            httpurlconnection.setInstanceFollowRedirects(false);

            switch (httpurlconnection.getResponseCode()) {
                default:
                    httpurlconnection1 = httpurlconnection;
                    httpurlconnection2 = httpurlconnection;
                    httpurlconnection3 = httpurlconnection;
                    flag = flag2;
                    flag1 = flag4;
                    Object obj = httpurlconnection.getErrorStream();
                    obj1 = obj;
                    obj2 = obj;
                    obj3 = obj;
                    Object obj4 = new StringBuilder();
                    if (obj == null) {
                        ((StringBuilder) (obj4)).append(context.getString(com.facebook.android.R.string.com_facebook_image_download_unknown_error));
                        flag = flag2;
                        flag1 = flag4;
                        flag2 = flag3;
                        obj1 = obj;
                        obj2 = obj;
                        obj3 = obj;
                        obj5 = new FacebookException(((StringBuilder) (obj4)).toString());
                        obj4 = obj7;
                        break;
                    } else {
                        InputStreamReader inputStreamReader = new InputStreamReader(((java.io.InputStream) (obj)));
                        char ac[] = new char[128];
                        for (; ; ) {
                            i = inputStreamReader.read(ac, 0, ac.length);
                            if (i <= 0)
                                break;
                            ((StringBuilder) (obj4)).append(ac, 0, i);
                        }


                        Utility.closeQuietly(((java.io.Closeable) (obj1)));
                        Utility.disconnectQuietly(httpurlconnection1);
                        obj4 = obj6;
//                        if (flag)
//                            issueResponse(requestkey, ((Exception) (obj5)), ((Bitmap) (obj4)), false);
                        return;
                    }
                case 301:
                case 302:
                    flag4 = false;
                    i = 0;
                    flag3 = false;
                    flag = flag3;
                    flag1 = false;
                    obj1 = obj9;
                    obj2 = obj11;
                    obj3 = obj12;
                    Object obj13 = httpurlconnection.getHeaderField("location");
                    if (Utility.isNullOrEmpty(((String) (obj13))))
                        break;
                    obj13 = new URI(((String) (obj13)));
                    UrlRedirectCache.cacheUriRedirect(context, requestkey.uri, ((URI) (obj13)));
                    DownloaderContext downloaderContext = removePendingRequest(requestkey);
                    obj4 = obj7;
                    obj5 = obj8;
                    flag2 = flag4;
                    obj = obj10;
                    if (downloaderContext == null)
                        break;

                    if (((DownloaderContext) (downloaderContext)).isCancelled)
                        break;
                    enqueueCacheRead(((DownloaderContext) (downloaderContext)).request, new RequestKey(((URI) (obj13)), requestkey.tag), false);
                    break;
                case 200:
                    InputStream inputStream = ImageResponseCache.interceptAndCacheImageStream(context, httpurlconnection);
                    obj4 = BitmapFactory.decodeStream(inputStream);
                    obj = inputStream;
                    break;
            }
//            Utility.closeQuietly(((java.io.Closeable) (obj)));
            Utility.disconnectQuietly(httpurlconnection);
            flag = flag2;
            return;
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void downloadAsync(ImageRequest imagerequest)
    {
        RequestKey requestkey;
        if(imagerequest == null)
            return;
        requestkey = new RequestKey(imagerequest.getImageUri(), imagerequest.getCallerTag());
        Map map = null;
        DownloaderContext downloadercontext = (DownloaderContext)pendingRequests.get(requestkey);
        if(downloadercontext != null) {
            downloadercontext.request = imagerequest;
            downloadercontext.isCancelled = false;
            downloadercontext.workItem.moveToFront();
        } else {
            enqueueCacheRead(imagerequest, requestkey, imagerequest.isCachedRedirectAllowed());
        }
        return;
    }

    private static void enqueueCacheRead(ImageRequest imagerequest, RequestKey requestkey, boolean flag)
    {
        enqueueRequest(imagerequest, requestkey, cacheReadQueue, new CacheReadWorkItem(imagerequest.getContext(), requestkey, flag));
    }

    private static void enqueueDownload(ImageRequest imagerequest, RequestKey requestkey)
    {
        enqueueRequest(imagerequest, requestkey, downloadQueue, new DownloadImageWorkItem(imagerequest.getContext(), requestkey));
    }

    private static void enqueueRequest(ImageRequest imagerequest, RequestKey requestkey, WorkQueue workqueue, Runnable runnable)
    {
        synchronized(pendingRequests)
        {
            DownloaderContext downloadercontext = new DownloaderContext();
            downloadercontext.request = imagerequest;
            pendingRequests.put(requestkey, downloadercontext);
            downloadercontext.workItem = workqueue.addActiveWorkItem(runnable);
        }
        return;
    }

    private static Handler getHandler()
    {
        Handler handler1;
        if(handler == null)
            handler = new Handler(Looper.getMainLooper());
        handler1 = handler;
        return handler1;
    }

    private static void issueResponse(final RequestKey requestkey, final Exception exception, final Bitmap bitmap, final boolean flag)
    {
        DownloaderContext downloaderContext = removePendingRequest(requestkey);
        if(requestkey != null && !((DownloaderContext) (downloaderContext)).isCancelled)
        {
            final ImageRequest temp = ((DownloaderContext) (downloaderContext)).request;
            final ImageRequest.Callback callback = temp.getCallback();
            if(callback != null)
                getHandler().post(new Runnable() {

                    public void run()
                    {
                        ImageResponse imageresponse = new ImageResponse(temp,exception,flag,bitmap);
                        callback.onCompleted(imageresponse);
                    }

                });
        }
    }

    public static void prioritizeRequest(ImageRequest imagerequest)
    {
        Object obj = new RequestKey(imagerequest.getImageUri(), imagerequest.getCallerTag());
        //imagerequest = pendingRequests;
        obj = (DownloaderContext)pendingRequests.get(obj);
        if(obj != null)
            ((DownloaderContext) (obj)).workItem.moveToFront();
        return;
    }

    private static void readFromCache(RequestKey requestkey, Context context, boolean flag)
    {
        Object obj = null;
        boolean flag2 = false;
        java.io.InputStream inputstream = null;
        boolean flag1 = flag2;
        if(flag)
        {
            URI uri = UrlRedirectCache.getRedirectedUri(context, requestkey.uri);
            inputstream = null;
            flag1 = flag2;
            if(uri != null)
            {
                inputstream = ImageResponseCache.getCachedImageStream(uri, context);
                if(inputstream != null)
                    flag1 = true;
                else
                    flag1 = false;
            }
        }
        if(!flag1)
            inputstream = ImageResponseCache.getCachedImageStream(requestkey.uri, context);
        Bitmap bitmap;
        DownloaderContext downloaderContext;
        if(inputstream != null)
        {
            bitmap = BitmapFactory.decodeStream(inputstream);
            Utility.closeQuietly(inputstream);
            issueResponse(requestkey, null, bitmap, flag1);
        } else
        {
            downloaderContext = removePendingRequest(requestkey);
            if(context != null && !((DownloaderContext) (downloaderContext)).isCancelled)
            {
                enqueueDownload(((DownloaderContext) (downloaderContext)).request, requestkey);
                return;
            }
        }
    }

    private static DownloaderContext removePendingRequest(RequestKey requestkey)
    {
        synchronized(pendingRequests)
        {
            DownloaderContext downloaderContext = (DownloaderContext)pendingRequests.remove(requestkey);
            return downloaderContext;
        }
    }

    private static final int CACHE_READ_QUEUE_MAX_CONCURRENT = 2;
    private static final int DOWNLOAD_QUEUE_MAX_CONCURRENT = 8;
    private static WorkQueue cacheReadQueue = new WorkQueue(2);
    private static WorkQueue downloadQueue = new WorkQueue(8);
    private static Handler handler;
    private static final Map pendingRequests = new HashMap();



}
