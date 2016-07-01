

package com.facebook.cache.common;


// Referenced classes of package com.facebook.cache.common:
//            CacheEventListener

public class NoOpCacheEventListener
    implements CacheEventListener
{

    private NoOpCacheEventListener()
    {
    }

    public static NoOpCacheEventListener getInstance()
    {
        NoOpCacheEventListener noopcacheeventlistener;
        if(sInstance == null)
            sInstance = new NoOpCacheEventListener();
        noopcacheeventlistener = sInstance;
        return noopcacheeventlistener;
    }

    public void onEviction(CacheEventListener.EvictionReason evictionreason, int i, long l)
    {
    }

    public void onHit()
    {
    }

    public void onMiss()
    {
    }

    public void onReadException()
    {
    }

    public void onWriteAttempt()
    {
    }

    public void onWriteException()
    {
    }

    private static NoOpCacheEventListener sInstance = null;

}
