

package com.facebook.common.executors;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.TimeUnit;

public class CallerThreadExecutor extends AbstractExecutorService
{

    private CallerThreadExecutor()
    {
    }

    public static CallerThreadExecutor getInstance()
    {
        return sInstance;
    }

    public boolean awaitTermination(long l, TimeUnit timeunit)
        throws InterruptedException
    {
        return true;
    }

    public void execute(Runnable runnable)
    {
        runnable.run();
    }

    public boolean isShutdown()
    {
        return false;
    }

    public boolean isTerminated()
    {
        return false;
    }

    public void shutdown()
    {
    }

    public List shutdownNow()
    {
        shutdown();
        return Collections.emptyList();
    }

    private static final CallerThreadExecutor sInstance = new CallerThreadExecutor();

}
