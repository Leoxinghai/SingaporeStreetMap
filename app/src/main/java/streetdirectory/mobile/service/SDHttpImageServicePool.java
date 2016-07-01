// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.service;

import android.graphics.Bitmap;
import java.util.*;

// Referenced classes of package streetdirectory.mobile.service:
//            SDHttpImageService, SDHttpImageServiceInput

public class SDHttpImageServicePool
{
    public static abstract class OnBitmapReceivedListener
    {

        public abstract void bitmapReceived(String s, Bitmap bitmap);

        public OnBitmapReceivedListener()
        {
        }
    }


    public SDHttpImageServicePool()
    {
    }

    public void abort()
    {
        for(Iterator iterator = services.entrySet().iterator(); iterator.hasNext(); iterator.remove())
            ((SDHttpImageService)((java.util.Map.Entry)iterator.next()).getValue()).abort();
    }

    protected void onBitmapReceived(String s, Bitmap bitmap)
    {
        removeQueue(s);
    }

    protected void onFailed(String s, Exception exception)
    {
        removeQueue(s);
    }

    class SUBCLASS1 extends SDHttpImageService {

        public void onAborted(Exception exception)
        {
            services.remove(url);
        }

        public void onFailed(Exception exception)
        {
            services.remove(url);
        }

        public void onSuccess(Bitmap bitmap)
        {
            services.remove(url);
            for(Iterator iterator = ((ArrayList)serviceListeners.get(url)).iterator(); iterator.hasNext(); ((OnBitmapReceivedListener)iterator.next()).bitmapReceived(url, bitmap));
            serviceListeners.remove(url);
        }

        public void onSuccess(Object obj)
        {
            onSuccess((Bitmap)obj);
        }

        final String url;

        SUBCLASS1(String ss, _cls1 final_sdhttpimageserviceinput)
        {
            super(final_sdhttpimageserviceinput);
            url = ss;
        }
    };

    public void queueRequest(String s, final int final_i, int i, OnBitmapReceivedListener onbitmapreceivedlistener)
    {
        SDHttpImageService sdhttpimageservice = new SUBCLASS1(s, new _cls1(s,final_i, i));

        if(!services.containsKey(s))
        {
            services.put(s, sdhttpimageservice);
            sdhttpimageservice.executeAsync();
        }
        if(!serviceListeners.containsKey(s))
            serviceListeners.put(s, new ArrayList());
        ((ArrayList)serviceListeners.get(s)).add(onbitmapreceivedlistener);
        return;
    }

    public void removeQueue(String s)
    {
        Iterator iterator = services.entrySet().iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
            if(((String)entry.getKey()).equals(s))
            {
                ((SDHttpImageService)entry.getValue()).abort();
                iterator.remove();
            }
        } while(true);
    }

    final HashMap serviceListeners = new HashMap();
    final HashMap services = new HashMap();

    // Unreferenced inner class streetdirectory/mobile/service/SDHttpImageServicePool$1

/* anonymous class */
    class _cls1 extends SDHttpImageServiceInput
    {

        public String getURL()
        {
            return url;
        }

        final String url;

        _cls1(String ss, int final_i, int j)
        {
            super(final_i, j);
            url = ss;
        }
    }

}
