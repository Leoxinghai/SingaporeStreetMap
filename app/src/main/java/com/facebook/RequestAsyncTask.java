// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;

// Referenced classes of package com.facebook:
//            RequestBatch, Request, Settings

public class RequestAsyncTask extends AsyncTask
{

    public RequestAsyncTask(RequestBatch requestbatch)
    {
        this(null, requestbatch);
    }

    public RequestAsyncTask(HttpURLConnection httpurlconnection, RequestBatch requestbatch)
    {
        requests = requestbatch;
        connection = httpurlconnection;
    }

    public RequestAsyncTask(HttpURLConnection httpurlconnection, Collection collection)
    {
        this(httpurlconnection, new RequestBatch(collection));
    }

    public RequestAsyncTask(HttpURLConnection httpurlconnection, Request arequest[])
    {
        this(httpurlconnection, new RequestBatch(arequest));
    }

    public RequestAsyncTask(Collection collection)
    {
        this(null, new RequestBatch(collection));
    }

    public RequestAsyncTask(Request arequest[])
    {
        this(null, new RequestBatch(arequest));
    }

    protected Object doInBackground(Object aobj[])
    {
        return doInBackground((Void[])aobj);
    }

    protected List doInBackground(Void avoid[])
    {
        try
        {
            if(connection == null)
                return requests.executeAndWait();
            List temp = Request.executeConnectionAndWait(connection, requests);
            return temp;
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            return null;
        }
    }

    RequestAsyncTask executeOnSettingsExecutor()
    {
        if(executeOnExecutorMethod != null)
        {
            try
            {
                executeOnExecutorMethod.invoke(this, new Object[] {
                    Settings.getExecutor(), null
                });
            }
            catch(InvocationTargetException invocationtargetexception)
            {
                return this;
            }
            catch(IllegalAccessException illegalaccessexception)
            {
                return this;
            }
            return this;
        } else
        {
            execute(new Void[0]);
            return this;
        }
    }

    protected final Exception getException()
    {
        return exception;
    }

    protected final RequestBatch getRequests()
    {
        return requests;
    }

    protected void onPostExecute(Object obj)
    {
        onPostExecute((List)obj);
    }

    protected void onPostExecute(List list)
    {
        super.onPostExecute(list);
        if(exception != null)
            Log.d(TAG, String.format("onPostExecute: exception encountered during request: %s", new Object[] {
                exception.getMessage()
            }));
    }

    protected void onPreExecute()
    {
        super.onPreExecute();
        if(requests.getCallbackHandler() == null)
            requests.setCallbackHandler(new Handler());
    }

    public String toString()
    {
        return (new StringBuilder()).append("{RequestAsyncTask: ").append(" connection: ").append(connection).append(", requests: ").append(requests).append("}").toString();
    }

    private static final String TAG = RequestAsyncTask.class.getCanonicalName();
    private static Method executeOnExecutorMethod;
    private final HttpURLConnection connection;
    private Exception exception;
    private final RequestBatch requests;

    static
    {
        Method amethod[] = AsyncTask.class.getMethods();
        int j = amethod.length;
        int i = 0;
        for(;i < j;)
        {
            Method method = amethod[i];
            if(!"executeOnExecutor".equals(method.getName()))
                break;
            Class aclass[] = method.getParameterTypes();
            if(aclass.length != 2 || aclass[0] != Executor.class || !aclass[1].isArray())
                break;
            executeOnExecutorMethod = method;
            i++;
        }
    }
}
