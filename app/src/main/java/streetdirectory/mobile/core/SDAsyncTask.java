// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.core;

import android.os.AsyncTask;

import rx.*;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

// Referenced classes of package streetdirectory.mobile.core:
//            SDLogger

public abstract class SDAsyncTask extends AsyncTask
{

    public SDAsyncTask()
    {
        mIsCancelled = false;
    }

    /*
    public void cancel(boolean flag)
    {
        if(mSubscription != null && !mIsCancelled)
        {
            mSubscription.unsubscribe();
            mIsCancelled = flag;
        }
    }
    */

    protected Object doInBackground(Object aobj[])
    {
        return null;
    }

    public void executeTask(Object aobj[])
    {
        //Object obj = doInBackground(aobj);
        //onPostExecute(obj);
        execute(aobj);
        /*
        mSubscription = Observable.just(((Object) (aobj))).map(new Func1() {

            public Object call(Object obj)
            {
                return call((Object[])obj);
            }

            public Object call(Object aobj1[])
            {
                return doInBackground(aobj1);
            }

        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber() {

            public void onCompleted()
            {

                SDLogger.debug("SDAsyncTask onCompleted()");
            }

            public void onError(Throwable throwable)
            {
                SDLogger.debug("SDAsyncTask onError()", throwable.getMessage());
            }

            public void onNext(Object obj)
            {
                onPostExecute(obj);
            }

        });
        */
    }

/*
    public boolean isCancelled()

    {
        return mIsCancelled;
    }
*/

    protected void onPostExecute(Object obj)
    {
    }

    protected void onProgressUpdate(Object aobj[])
    {
    }

    private boolean mIsCancelled;
    private Subscription mSubscription;
}
