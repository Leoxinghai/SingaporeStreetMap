

package com.facebook.cache.common;


public interface CacheEventListener
{
    public static enum EvictionReason
    {
        CACHE_FULL("CACHE_FULL", 0),
        CONTENT_STALE("CONTENT_STALE", 1),
        USER_FORCED("USER_FORCED", 2),
        CACHE_MANAGER_TRIMMED("CACHE_MANAGER_TRIMMED", 3);

        private String sType;
        private int iType;
        private EvictionReason(String s, int i)
        {
            sType = s;
            iType = i;
        }
    }


    public abstract void onEviction(EvictionReason evictionreason, int i, long l);

    public abstract void onHit();

    public abstract void onMiss();

    public abstract void onReadException();

    public abstract void onWriteAttempt();

    public abstract void onWriteException();
}
