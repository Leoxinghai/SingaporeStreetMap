// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.common.executors;

import android.os.Handler;
import java.util.concurrent.*;

public class ScheduledFutureImpl
    implements RunnableFuture, ScheduledFuture
{

    public ScheduledFutureImpl(Handler handler, Runnable runnable, Object obj)
    {
        mHandler = handler;
        mListenableFuture = new FutureTask(runnable, obj);
    }

    public ScheduledFutureImpl(Handler handler, Callable callable)
    {
        mHandler = handler;
        mListenableFuture = new FutureTask(callable);
    }

    public boolean cancel(boolean flag)
    {
        return mListenableFuture.cancel(flag);
    }


    public int compareTo(Object delayed)
    {
        throw new UnsupportedOperationException();
    }
    //public int compareTo(Delayed delayed)
    //{
//        throw new UnsupportedOperationException();
//    }

    public Object get()
        throws InterruptedException, ExecutionException
    {
        return mListenableFuture.get();
    }

    public Object get(long l, TimeUnit timeunit)
        throws InterruptedException, ExecutionException, TimeoutException
    {
        return mListenableFuture.get(l, timeunit);
    }

    public long getDelay(TimeUnit timeunit)
    {
        throw new UnsupportedOperationException();
    }

    public boolean isCancelled()
    {
        return mListenableFuture.isCancelled();
    }

    public boolean isDone()
    {
        return mListenableFuture.isDone();
    }

    public void run()
    {
        mListenableFuture.run();
    }

    private final Handler mHandler;
    private final FutureTask mListenableFuture;
}
