// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.common.executors;

import com.facebook.common.logging.FLog;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ConstrainedExecutorService extends AbstractExecutorService
{
    private class Worker
        implements Runnable
    {

        public void run()
        {
            int i;
            try {
                for (;;) {
                    Runnable runnable = (Runnable) mWorkQueue.poll();
                    if (runnable != null)
                        runnable.run();

                    i = mPendingWorkers.decrementAndGet();
                    Exception exception;
                    if (!mWorkQueue.isEmpty()) {
                        startWorkerIfNeeded();
                        return;
                    } else {
                        FLog.v(ConstrainedExecutorService.TAG, "%s: worker finished; %d workers left", mName, Integer.valueOf(i));
                        return;
                    }
                    //FLog.v(ConstrainedExecutorService.TAG, "%s: Worker has nothing to run", mName);
                }
            } catch(Exception exception) {
                i = mPendingWorkers.decrementAndGet();
                if (!mWorkQueue.isEmpty())
                    startWorkerIfNeeded();
                else
                    FLog.v(ConstrainedExecutorService.TAG, "%s: worker finished; %d workers left", mName, Integer.valueOf(i));
                throw exception;
            }
        }


        private Worker()
        {
            super();
        }

    }


    public ConstrainedExecutorService(String s, int i, Executor executor, BlockingQueue blockingqueue)
    {
        if(i <= 0)
        {
            throw new IllegalArgumentException("max concurrency must be > 0");
        } else
        {
            mName = s;
            mExecutor = executor;
            mMaxConcurrency = i;
            mWorkQueue = blockingqueue;
            mTaskRunner = new Worker();
            mPendingWorkers = new AtomicInteger(0);
            mMaxQueueSize = new AtomicInteger(0);
            return;
        }
    }

    public static ConstrainedExecutorService newConstrainedExecutor(String s, int i, int j, Executor executor)
    {
        return new ConstrainedExecutorService(s, i, executor, new LinkedBlockingQueue(j));
    }

    private void startWorkerIfNeeded()
    {
        int i = mPendingWorkers.get();
        for(;i < mMaxConcurrency;)
        {
            int j = i + 1;
            if(mPendingWorkers.compareAndSet(i, j)) {
                FLog.v(TAG, "%s: starting worker %d of %d", mName, Integer.valueOf(j), Integer.valueOf(mMaxConcurrency));
                mExecutor.execute(mTaskRunner);
            } else {
                FLog.v(TAG, "%s: race in startWorkerIfNeeded; retrying", mName);
                i = mPendingWorkers.get();
            }
        }
        return;
    }

    public boolean awaitTermination(long l, TimeUnit timeunit)
        throws InterruptedException
    {
        throw new UnsupportedOperationException();
    }

    public void execute(Runnable runnable)
    {
        if(runnable == null)
            throw new NullPointerException("runnable parameter is null");
        if(!mWorkQueue.offer(runnable))
            throw new RejectedExecutionException((new StringBuilder()).append(mName).append(" queue is full, size=").append(mWorkQueue.size()).toString());
        int i = mWorkQueue.size();
        int j = mMaxQueueSize.get();
        if(i > j && mMaxQueueSize.compareAndSet(j, i))
            FLog.v(TAG, "%s: max pending work in queue = %d", mName, Integer.valueOf(i));
        startWorkerIfNeeded();
    }

    public boolean isIdle()
    {
        return mWorkQueue.isEmpty() && mPendingWorkers.get() == 0;
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
        throw new UnsupportedOperationException();
    }

    public List shutdownNow()
    {
        throw new UnsupportedOperationException();
    }

    private static final Class TAG = ConstrainedExecutorService.class;
    private final Executor mExecutor;
    private int mMaxConcurrency;
    private final AtomicInteger mMaxQueueSize;
    private final String mName;
    private final AtomicInteger mPendingWorkers;
    private final Worker mTaskRunner;
    private final BlockingQueue mWorkQueue;
}
