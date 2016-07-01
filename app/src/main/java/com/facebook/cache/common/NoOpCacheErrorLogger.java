

package com.facebook.cache.common;


// Referenced classes of package com.facebook.cache.common:
//            CacheErrorLogger

public class NoOpCacheErrorLogger
    implements CacheErrorLogger
{

    private NoOpCacheErrorLogger()
    {
    }

    public static NoOpCacheErrorLogger getInstance()
    {
        NoOpCacheErrorLogger noopcacheerrorlogger;
        if(sInstance == null)
            sInstance = new NoOpCacheErrorLogger();
        noopcacheerrorlogger = sInstance;
        return noopcacheerrorlogger;
    }

    public void logError(CacheErrorLogger.CacheErrorCategory cacheerrorcategory, Class class1, String s, Throwable throwable)
    {
    }

    private static NoOpCacheErrorLogger sInstance = null;

}
