// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.internal;

import com.facebook.Request;
import com.facebook.RequestBatch;

public class CacheableRequestBatch extends RequestBatch
{

    public CacheableRequestBatch()
    {
    }

    public CacheableRequestBatch(Request arequest[])
    {
        super(arequest);
    }

    public final String getCacheKeyOverride()
    {
        return cacheKey;
    }

    public final boolean getForceRoundTrip()
    {
        return forceRoundTrip;
    }

    public final void setCacheKeyOverride(String s)
    {
        cacheKey = s;
    }

    public final void setForceRoundTrip(boolean flag)
    {
        forceRoundTrip = flag;
    }

    private String cacheKey;
    private boolean forceRoundTrip;
}
