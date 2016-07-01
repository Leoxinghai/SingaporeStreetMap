// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.producers;

import com.facebook.common.logging.FLog;

// Referenced classes of package com.facebook.imagepipeline.producers:
//            Consumer

public abstract class BaseConsumer
    implements Consumer
{

    public BaseConsumer()
    {
        mIsFinished = false;
    }

    public void onCancellation()
    {
        boolean flag = mIsFinished;
        if(!flag) {
			mIsFinished = true;
			onCancellationImpl();
		}
        return;
    }

    protected abstract void onCancellationImpl();

    public void onFailure(Throwable throwable)
    {
        boolean flag = mIsFinished;
        if(!flag) {
			mIsFinished = true;
			try
			{
				onFailureImpl(throwable);
			}
			catch(Exception ex) {
				onUnhandledException(ex);
			}
		}
        return;
    }

    protected abstract void onFailureImpl(Throwable throwable);

    public void onNewResult(Object obj, boolean flag)
    {
        boolean flag1 = mIsFinished;
        if(!flag1) {
			mIsFinished = flag;
			try
			{
				onNewResultImpl(obj, flag);
			}
			catch(Exception ex) {
				onUnhandledException(((Exception) (obj)));
			}
		}
        return;
    }

    protected abstract void onNewResultImpl(Object obj, boolean flag);

    public void onProgressUpdate(float f)
    {
        boolean flag = mIsFinished;
        if(!flag) {
			onProgressUpdateImpl(f);
		}
        return;
    }

    protected void onProgressUpdateImpl(float f)
    {
    }

    protected void onUnhandledException(Exception exception)
    {
        FLog.wtf(getClass(), "unhandled exception", exception);
    }

    private boolean mIsFinished;
}
