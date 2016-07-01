

package com.facebook.datasource;

import android.util.Pair;
import com.facebook.common.internal.Preconditions;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;

// Referenced classes of package com.facebook.datasource:
//            DataSource, DataSubscriber

public abstract class AbstractDataSource
    implements DataSource
{
    private static enum DataSourceStatus
    {

            IN_PROGRESS("IN_PROGRESS", 0),
            SUCCESS("SUCCESS", 1),
            FAILURE("FAILURE", 2);

        private DataSourceStatus(String s, int i){
        }
    }


    protected AbstractDataSource()
    {
        mResult = null;
        mFailureThrowable = null;
        mProgress = 0.0F;
        mIsClosed = false;
        mDataSourceStatus = DataSourceStatus.IN_PROGRESS;
    }

    private void notifyDataSubscriber(final DataSubscriber dataSubscriber, Executor executor, final boolean isFailure, final boolean isCancellation)
    {
        executor.execute(new Runnable() {

            public void run()
            {
                if(isFailure)
                {
                    dataSubscriber.onFailure(AbstractDataSource.this);
                    return;
                }
                if(isCancellation)
                {
                    dataSubscriber.onCancellation(AbstractDataSource.this);
                    return;
                } else
                {
                    dataSubscriber.onNewResult(AbstractDataSource.this);
                    return;
                }
            }

        });
    }

    private void notifyDataSubscribers()
    {
        boolean flag = hasFailed();
        boolean flag1 = wasCancelled();
        Pair pair;
        for(Iterator iterator = mSubscribers.iterator(); iterator.hasNext(); notifyDataSubscriber((DataSubscriber)pair.first, (Executor)pair.second, flag, flag1))
            pair = (Pair)iterator.next();

    }

    private boolean setFailureInternal(Throwable throwable)
    {
        boolean flag = false;
        if(mIsClosed)
            return flag;

        DataSourceStatus datasourcestatus;
        DataSourceStatus datasourcestatus1;
        datasourcestatus = mDataSourceStatus;
        datasourcestatus1 = DataSourceStatus.IN_PROGRESS;
        if(datasourcestatus == datasourcestatus1) {
            mDataSourceStatus = DataSourceStatus.FAILURE;
            mFailureThrowable = throwable;
            flag = true;
        }
        return flag;
    }

    private boolean setProgressInternal(float f)
    {
        boolean flag1 = false;
        boolean flag = flag1;
        if(mIsClosed)
            return flag;

        DataSourceStatus datasourcestatus;
        DataSourceStatus datasourcestatus1;
        datasourcestatus = mDataSourceStatus;
        datasourcestatus1 = DataSourceStatus.IN_PROGRESS;
        if(datasourcestatus == datasourcestatus1) {
            flag = flag1;
            if(f >= mProgress) {
                mProgress = f;
                flag = true;
            }
        } else {
            flag = flag1;
        }

        return flag;
    }

    private boolean setResultInternal(Object obj, boolean flag)
    {
        Object obj1;
        Object obj3;
        obj3 = null;
        obj1 = null;
        Object obj2 = null;
        obj1 = obj3;
        if(!mIsClosed) {
            obj1 = obj3;
            if (mDataSourceStatus == DataSourceStatus.IN_PROGRESS) {
                if(!flag)
                    return flag;
                obj1 = obj3;
                mDataSourceStatus = DataSourceStatus.SUCCESS;
                obj1 = obj3;
                mProgress = 1.0F;
                obj1 = obj3;
                if(mResult == obj) {
                    if(obj1 != null)
                        closeResult(obj1);
                    return flag;
                }
                obj1 = obj3;
                obj2 = mResult;
                obj1 = obj2;
                mResult = obj;
                flag = true;
                obj1 = obj2;
                if(obj2 != null) {
                    closeResult(obj2);
                    return true;
                }
                return flag;
            }
        }

        boolean flag1;
        flag1 = false;
        obj1 = obj;
        flag = flag1;
        if(obj != null)
        {
            closeResult(obj);
            flag = flag1;
        }
        return flag;

    }

    private boolean wasCancelled()
    {
        if(!isClosed())
            return true;

        boolean flag = isFinished();
        if(flag) {
            flag = false;
        } else {
            flag = true;
        }
        return flag;
    }

    public boolean close()
    {
        if(mIsClosed)
            return false;

        Object obj;
        mIsClosed = true;
        obj = mResult;
        mResult = null;
        if(obj != null)
            closeResult(obj);
        if(!isFinished())
            notifyDataSubscribers();
        mSubscribers.clear();
        return true;
    }

    protected void closeResult(Object obj)
    {
    }

    public Throwable getFailureCause()
    {
        Throwable throwable = mFailureThrowable;
        return throwable;
    }

    public float getProgress()
    {
        float f = mProgress;
        return f;
    }

    public Object getResult()
    {
        Object obj = mResult;
        return obj;
    }

    public boolean hasFailed()
    {
        DataSourceStatus datasourcestatus;
        DataSourceStatus datasourcestatus1;
        datasourcestatus = mDataSourceStatus;
        datasourcestatus1 = DataSourceStatus.FAILURE;
        boolean flag;
        if(datasourcestatus == datasourcestatus1)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public boolean hasResult()
    {
        Object obj = mResult;
        boolean flag;
        if(obj != null)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public boolean isClosed()
    {
        boolean flag = mIsClosed;
        return flag;
    }

    public boolean isFinished()
    {
        DataSourceStatus datasourcestatus;
        DataSourceStatus datasourcestatus1;
        datasourcestatus = mDataSourceStatus;
        datasourcestatus1 = DataSourceStatus.IN_PROGRESS;
        boolean flag;
        if(datasourcestatus != datasourcestatus1)
            flag = true;
        else
            flag = false;
        return flag;
    }

    class SUBCLASS1 implements Runnable {
        DataSubscriber subscriber;
        public void run()
        {
            subscriber.onProgressUpdate(AbstractDataSource.this);
        }

        SUBCLASS1(DataSubscriber _subscriber){
            subscriber = _subscriber;
        }

    }
    protected void notifyProgressUpdate()
    {
        Pair pair;
        DataSubscriber subscriber;
        for(Iterator iterator = mSubscribers.iterator(); iterator.hasNext(); ((Executor)pair.second).execute(new SUBCLASS1(subscriber)))
        {
            pair = (Pair)iterator.next();
            subscriber = (DataSubscriber)pair.first;
        }

    }

    protected boolean setFailure(Throwable throwable)
    {
        boolean flag = setFailureInternal(throwable);
        if(flag)
            notifyDataSubscribers();
        return flag;
    }

    protected boolean setProgress(float f)
    {
        boolean flag = setProgressInternal(f);
        if(flag)
            notifyProgressUpdate();
        return flag;
    }

    protected boolean setResult(Object obj, boolean flag)
    {
        flag = setResultInternal(obj, flag);
        if(flag)
            notifyDataSubscribers();
        return flag;
    }

    public void subscribe(DataSubscriber datasubscriber, Executor executor)
    {
        Preconditions.checkNotNull(datasubscriber);
        Preconditions.checkNotNull(executor);
        if(mIsClosed)
            return;
        if(mDataSourceStatus == DataSourceStatus.IN_PROGRESS)
            mSubscribers.add(Pair.create(datasubscriber, executor));
        boolean flag;
        if(!hasResult() && !isFinished() && !wasCancelled())
            flag = false;
        else
            flag = true;
        if(flag)
        {
            notifyDataSubscriber(datasubscriber, executor, hasFailed(), wasCancelled());
            return;
        } else
        {
            return;
        }
    }

    private DataSourceStatus mDataSourceStatus;
    private Throwable mFailureThrowable;
    private boolean mIsClosed;
    private float mProgress;
    private Object mResult;
    private final ConcurrentLinkedQueue mSubscribers = new ConcurrentLinkedQueue();
}
