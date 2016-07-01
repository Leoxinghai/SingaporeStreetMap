// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.listener;

import android.os.SystemClock;
import android.util.Pair;
import com.facebook.common.logging.FLog;
import com.facebook.imagepipeline.request.ImageRequest;
import java.util.HashMap;
import java.util.Map;

// Referenced classes of package com.facebook.imagepipeline.listener:
//            RequestListener

public class RequestLoggingListener
    implements RequestListener
{

    public RequestLoggingListener()
    {
    }

    private static long getElapsedTime(Long long1, long l)
    {
        if(long1 != null)
            return l - long1.longValue();
        else
            return -1L;
    }

    private static long getTime()
    {
        return SystemClock.elapsedRealtime();
    }

    public void onProducerEvent(String s, String s1, String s2)
    {
        if(FLog.isLoggable(2))
        {
            Object obj = Pair.create(s, s1);
            obj = (Long)mProducerStartTimeMap.get(obj);
            long l = getTime();
            FLog.v("RequestLoggingListener", "time %d: onProducerEvent: {requestId: %s, stage: %s, eventName: %s; elapsedTime: %d ms}", new Object[] {
                Long.valueOf(getTime()), s, s1, s2, Long.valueOf(getElapsedTime(((Long) (obj)), l))
            });
        }
        return;
    }

    public void onProducerFinishWithCancellation(String s, String s1, Map map)
    {
        if(FLog.isLoggable(2))
        {
            Object obj = Pair.create(s, s1);
            obj = (Long)mProducerStartTimeMap.remove(obj);
            long l = getTime();
            FLog.v("RequestLoggingListener", "time %d: onProducerFinishWithCancellation: {requestId: %s, stage: %s, elapsedTime: %d ms, extraMap: %s}", new Object[] {
                Long.valueOf(l), s, s1, Long.valueOf(getElapsedTime(((Long) (obj)), l)), map
            });
        }
        return;
    }

    public void onProducerFinishWithFailure(String s, String s1, Throwable throwable, Map map)
    {
        if(FLog.isLoggable(5))
        {
            Object obj = Pair.create(s, s1);
            obj = (Long)mProducerStartTimeMap.remove(obj);
            long l = getTime();
            FLog.w("RequestLoggingListener", "time %d: onProducerFinishWithFailure: {requestId: %s, stage: %s, elapsedTime: %d ms, extraMap: %s, throwable: %s}", new Object[] {
                Long.valueOf(l), s, s1, Long.valueOf(getElapsedTime(((Long) (obj)), l)), map, throwable.toString()
            });
        }
        return;
    }

    public void onProducerFinishWithSuccess(String s, String s1, Map map)
    {
        if(FLog.isLoggable(2))
        {
            Object obj = Pair.create(s, s1);
            obj = (Long)mProducerStartTimeMap.remove(obj);
            long l = getTime();
            FLog.v("RequestLoggingListener", "time %d: onProducerFinishWithSuccess: {requestId: %s, producer: %s, elapsedTime: %d ms, extraMap: %s}", new Object[] {
                Long.valueOf(l), s, s1, Long.valueOf(getElapsedTime(((Long) (obj)), l)), map
            });
        }
        return;
    }

    public void onProducerStart(String s, String s1)
    {
        if(FLog.isLoggable(2))
        {
            Pair pair = Pair.create(s, s1);
            long l = getTime();
            mProducerStartTimeMap.put(pair, Long.valueOf(l));
            FLog.v("RequestLoggingListener", "time %d: onProducerStart: {requestId: %s, producer: %s}", Long.valueOf(l), s, s1);
        }
        return;
    }

    public void onRequestCancellation(String s)
    {
        if(FLog.isLoggable(2))
        {
            Long long1 = (Long)mRequestStartTimeMap.remove(s);
            long l = getTime();
            FLog.v("RequestLoggingListener", "time %d: onRequestCancellation: {requestId: %s, elapsedTime: %d ms}", Long.valueOf(l), s, Long.valueOf(getElapsedTime(long1, l)));
        }
        return;
    }

    public void onRequestFailure(ImageRequest imagerequest, String s, Throwable throwable, boolean flag)
    {
        if(FLog.isLoggable(5))
        {
            Long temp = (Long)mRequestStartTimeMap.remove(s);
            long l = getTime();
            FLog.w("RequestLoggingListener", "time %d: onRequestFailure: {requestId: %s, elapsedTime: %d ms, throwable: %s}", new Object[] {
                Long.valueOf(l), s, Long.valueOf(getElapsedTime(temp, l)), throwable.toString()
            });
        }
        return;
    }

    public void onRequestStart(ImageRequest imagerequest, Object obj, String s, boolean flag)
    {
        if(FLog.isLoggable(2))
        {
            FLog.v("RequestLoggingListener", "time %d: onRequestSubmit: {requestId: %s, callerContext: %s, isPrefetch: %b}", Long.valueOf(getTime()), s, obj, Boolean.valueOf(flag));
            mRequestStartTimeMap.put(s, Long.valueOf(getTime()));
        }
        return;
    }

    public void onRequestSuccess(ImageRequest imagerequest, String s, boolean flag)
    {
        if(FLog.isLoggable(2))
        {
            Long temp = (Long)mRequestStartTimeMap.remove(s);
            long l = getTime();
            FLog.v("RequestLoggingListener", "time %d: onRequestSuccess: {requestId: %s, elapsedTime: %d ms}", Long.valueOf(l), s, Long.valueOf(getElapsedTime(temp, l)));
        }
        return;
    }

    public boolean requiresExtraMap(String s)
    {
        return FLog.isLoggable(2);
    }

    private static final String TAG = "RequestLoggingListener";
    private final Map mProducerStartTimeMap = new HashMap();
    private final Map mRequestStartTimeMap = new HashMap();
}
