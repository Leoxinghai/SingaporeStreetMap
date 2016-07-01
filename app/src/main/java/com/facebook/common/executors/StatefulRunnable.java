

package com.facebook.common.executors;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class StatefulRunnable
    implements Runnable
{

    public StatefulRunnable()
    {
    }

    public void cancel()
    {
        if(mState.compareAndSet(0, 2))
            onCancellation();
    }

    protected void disposeResult(Object obj)
    {
    }

    protected abstract Object getResult()
        throws Exception;

    protected void onCancellation()
    {
    }

    protected void onFailure(Exception exception)
    {
    }

    protected void onSuccess(Object obj)
    {
    }

    public final void run()
    {
        Object obj;
        if(!mState.compareAndSet(0, 1))
            return;
        try
        {
            obj = getResult();
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            mState.set(4);
            onFailure(ex);
            return;
        }
        mState.set(3);
        onSuccess(obj);
        disposeResult(obj);
        return;
    }

    protected static final int STATE_CANCELLED = 2;
    protected static final int STATE_CREATED = 0;
    protected static final int STATE_FAILED = 4;
    protected static final int STATE_FINISHED = 3;
    protected static final int STATE_STARTED = 1;
    protected final AtomicInteger mState = new AtomicInteger(0);
}
