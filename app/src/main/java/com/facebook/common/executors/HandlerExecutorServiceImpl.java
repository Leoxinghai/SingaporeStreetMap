// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.common.executors;

import android.os.Handler;
import android.os.Looper;
import java.util.List;
import java.util.concurrent.*;

// Referenced classes of package com.facebook.common.executors:
//            HandlerExecutorService, ScheduledFutureImpl

public class HandlerExecutorServiceImpl extends AbstractExecutorService
    implements HandlerExecutorService
{

    public HandlerExecutorServiceImpl(Handler handler)
    {
        mHandler = handler;
    }

    public boolean awaitTermination(long l, TimeUnit timeunit)
        throws InterruptedException
    {
        throw new UnsupportedOperationException();
    }


    public void execute(Runnable runnable)
    {
        mHandler.post(runnable);
    }

    public void execute(ScheduledFutureImpl runnable)
    {
        mHandler.post(runnable);
    }

    public boolean isHandlerThread()
    {
        return Thread.currentThread() == mHandler.getLooper().getThread();
    }

    public boolean isShutdown()
    {
        return false;
    }

    public boolean isTerminated()
    {
        return false;
    }

    protected ScheduledFutureImpl newTaskFor(Runnable runnable, Object obj)
    {
        return new ScheduledFutureImpl(mHandler, runnable, obj);
    }

    protected ScheduledFutureImpl newTaskFor(Callable callable)
    {
        return new ScheduledFutureImpl(mHandler, callable);
    }


    public void quit()
    {
        mHandler.getLooper().quit();
    }

    public ScheduledFuture schedule(Runnable runnable, long l, TimeUnit timeunit)
    {
        ScheduledFuture temp = newTaskFor(runnable, null);
        mHandler.postDelayed(runnable, timeunit.toMillis(l));
        return temp;
    }

    public ScheduledFuture schedule(Callable callable, long l, TimeUnit timeunit)
    {
        ScheduledFutureImpl temp = newTaskFor(callable);
        mHandler.postDelayed(temp, timeunit.toMillis(l));
        return temp;
    }

    public ScheduledFuture scheduleAtFixedRate(Runnable runnable, long l, long l1, TimeUnit timeunit)
    {
        throw new UnsupportedOperationException();
    }

    public ScheduledFuture scheduleWithFixedDelay(Runnable runnable, long l, long l1, TimeUnit timeunit)
    {
        throw new UnsupportedOperationException();
    }

    public void shutdown()
    {
        throw new UnsupportedOperationException();
    }

    public List shutdownNow()
    {
        throw new UnsupportedOperationException();
    }

    public ScheduledFuture submit(Runnable runnable, Object obj)
    {
        if(runnable == null)
        {
            throw new NullPointerException();
        } else
        {
            ScheduledFuture temp = newTaskFor(runnable, obj);
            execute(runnable);
            return temp;
        }
    }

    public ScheduledFuture submit(Callable callable)
    {
        if(callable == null)
        {
            throw new NullPointerException();
        } else
        {
            ScheduledFuture temp = newTaskFor(callable);
            execute((ScheduledFutureImpl)temp);
            return temp;
        }
    }

    private final Handler mHandler;
}
