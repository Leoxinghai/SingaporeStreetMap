
package streetdirectory.mobile.core.service;

import android.os.Handler;
import java.io.InputStream;
import streetdirectory.mobile.core.SDAsyncTask;
import streetdirectory.mobile.core.SDLogger;


public class HttpService
    implements HttpServiceListener
{

    public HttpService()
    {
        listener = this;
    }

    public HttpService(final String url)
    {
        this(((HttpConnectionInput) (new HttpConnectionInput() {

            public String getURL() {
                return url;
            }

        })));
        listener = this;
    }

    public HttpService(HttpConnectionInput httpconnectioninput)
    {
        setInput(httpconnectioninput);
        listener = this;
    }

    public void abort()
    {
        _isCanceled = true;
        closeConnection();
        if(_parser != null)
            _parser.abort();
    }

    public void closeConnection()
    {
        if(_connection != null)
            _connection.abort();
        _connection = null;
        return;
    }

    public Object http_execute()
    {
        Object obj;
        Object obj1;
        final HttpConnectionResult result;
        if(_connection == null)
            return null;

        result = _connection.http_execute(new HttpConnectionHandler() {

            public void onProgress(HttpConnection httpconnection, final int i)
            {
                if(_currentThread != null)
                    _currentThread.post(new Runnable() {

                        public void run()
                        {
                            if(listener != null)
                                listener.onProgress(i);
                        }
                    });
                else
                if(listener != null)
                {
                    listener.onProgress(i);
                    return;
                }
            }

        });
        obj1 = null;
        System.out.println("HttpService"+result.httpStatus +":"+result.inputStream+":"+_isCanceled);
        if(result.inputStream == null || _isCanceled) {
            System.out.println("HttpServie.trace.1");
            if(_isCanceled)
            {
                if(_currentThread != null)
                {
                    _currentThread.post(new Runnable() {

                        public void run()
                        {
                            if(listener != null)
                                listener.onAborted(result.exception);
                        }

                    });
                    obj = obj1;
                } else
                {
                    obj = obj1;
                    if(listener != null)
                    {
                        listener.onAborted(result.exception);
                        obj = obj1;
                    }
                }
            } else if(_currentThread != null)
            {
                _currentThread.post(new Runnable() {

                    public void run()
                    {
                        if(listener != null)
                            listener.onFailed(result.exception);
                    }

                });

                obj = obj1;
            } else {
                obj = obj1;
                if(listener != null)
                {
                    listener.onFailed(result.exception);
                    obj = obj1;
                }
            }
        } else {
            System.out.println("HttpServie.trace.3");
            obj = parse(result.inputStream);
            System.out.println("HttpServie.parse." + obj);
            result.close();
        }

        if(_connection != null)
            _connection.close();
        closeConnection();
        System.out.println("HttpServie.return." + obj);
        return obj;
    }

    public void executeAsync()
    {
        _currentThread = new Handler();

        (new SDAsyncTask() {

            protected Object doInBackground(Object aobj[])
            {
                try {
                    if(_connection!=null && _connection._input != null) {
                        System.out.println("HttpService." + _connection._input);
                        System.out.println("HttpService." + _connection._input.getURL());
                    }
                    Object obj = http_execute();
                    return obj;
                } catch(Exception ex) {
                    ((Exception) (ex)).printStackTrace();
                    SDLogger.error(((Exception) (ex)).getMessage());
                    if(listener != null)
                    {
                        listener.onFailed(((Exception) (ex)));
                        return null;
                    }

                }
                return null;

            }

            protected void onPostExecute(Object obj)
            {
                _currentThread = null;
            }

        }).execute(new Void[0]);
        return;
    }

    public HttpConnectionInput getInput()
    {
        return _input;
    }

    public HttpServiceListener getListener()
    {
        return listener;
    }

    public void onAborted(Exception exception)
    {
    }

    public void onFailed(Exception exception)
    {
    }

    public void onProgress(int i)
    {
    }

    public void onSuccess(Object obj)
    {
    }

    protected Object parse(InputStream inputstream)
    {
        if(_parser != null)
            return _parser.parse(inputstream);
        else
            return null;
    }

    public void setInput(HttpConnectionInput httpconnectioninput)
    {
        _input = httpconnectioninput;
        _connection = new HttpConnection(httpconnectioninput);
    }

    public void setListener(HttpServiceListener httpservicelistener)
    {
        listener = httpservicelistener;
    }

    protected HttpConnection _connection;
    protected Handler _currentThread;
    protected HttpConnectionInput _input;
    protected boolean _isCanceled;
    protected HttpServiceResultHandler _parser;
    protected HttpServiceListener listener;
    public Object tag;
}
