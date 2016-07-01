
package streetdirectory.mobile.core.service;

import android.os.Handler;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.zip.GZIPInputStream;
import org.apache.http.*;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.*;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import streetdirectory.mobile.core.SDAsyncTask;
import streetdirectory.mobile.core.storage.StorageUtil;


public class HttpConnection
{
    public static interface Method
    {

        public static final String GET = "get";
        public static final String POST = "post";
    }


    public HttpConnection()
    {
        _isCanceled = false;
        showProgress = false;
    }

    public HttpConnection(HttpConnectionInput httpconnectioninput)
    {
        _isCanceled = false;
        showProgress = false;
        _input = httpconnectioninput;
    }

    protected static HttpParams createHttpParams()
    {
        return new BasicHttpParams();
    }

    protected static ClientConnectionManager getConnectionManager()
    {
        ClientConnectionManager clientconnectionmanager;
        if(theConnectionManager == null)
        {
            HttpParams httpparams = createHttpParams();
            SchemeRegistry schemeregistry = new SchemeRegistry();
            schemeregistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            schemeregistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
            ConnManagerParams.setMaxTotalConnections(httpparams, 200);
            ConnManagerParams.setMaxConnectionsPerRoute(httpparams, new ConnPerRoute() {

                public int getMaxForRoute(HttpRoute httproute)
                {
                    return 50;
                }

            }
);
            theConnectionManager = new ThreadSafeClientConnManager(httpparams, schemeregistry);
        }
        clientconnectionmanager = theConnectionManager;
        return clientconnectionmanager;
    }

    public void abort()
    {
        _isCanceled = true;
        if(_requestBase != null)
            try
            {
                _requestBase.abort();
                _requestBase = null;
            }
            catch(Exception exception) { }
        consumeEntityFromResponse(_response);
    }

    public void close()
    {
        consumeEntityFromResponse(_response);
        try
        {
            if(_inputStream != null)
                _inputStream.close();
            return;
        }
        catch(Exception exception)
        {
            return;
        }
    }

    protected void closeInputStream()
    {
        try
        {
            if(_inputStream != null)
            {
                _inputStream.close();
                _inputStream = null;
            }
            return;
        }
        catch(IOException ioexception)
        {
            ioexception.printStackTrace();
        }
    }

    protected void consumeEntityFromResponse(HttpResponse httpresponse)
    {
        try
        {
            httpresponse.getEntity().consumeContent();
            return;
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            return;
        }
    }

    protected DefaultHttpClient  createHttpClient()
    {
        if(theHttpClient == null)
        {
            HttpParams httpParams = createHttpParams();
            theHttpClient = new DefaultHttpClient(getConnectionManager(), httpParams);
            Credentials credentials = _input.getCredentials();
            org.apache.http.auth.AuthScope authscope = _input.getAuthScope();
            if(credentials != null && authscope != null)
                theHttpClient.getCredentialsProvider().setCredentials(authscope, credentials);
        }
        return theHttpClient;
    }

    protected HttpRequestBase createHttpRequestBase(HttpConnectionInput httpconnectioninput)
        throws IllegalArgumentException, URISyntaxException
    {
        Object obj = httpconnectioninput.getMethodType();
        Header aheader[];
        if("get".equalsIgnoreCase(((String) (obj))))
            obj = new HttpGet(new URI(httpconnectioninput.getURL()));
        else
        if("post".equalsIgnoreCase(((String) (obj))))
            obj = new HttpPost(new URI(httpconnectioninput.getURL()));
        else
            obj = new HttpGet(new URI(httpconnectioninput.getURL()));
        ((HttpRequestBase) (obj)).addHeader("Accept-Encoding", "gzip");
        aheader = httpconnectioninput.getHeaders();
        if(aheader != null)
        {
            int j = aheader.length;
            for(int i = 0; i < j; i++)
                ((HttpRequestBase) (obj)).addHeader(aheader[i]);

        }
        if(obj instanceof HttpEntityEnclosingRequestBase)
        {
            HttpEntityEnclosingRequestBase httpentityenclosingrequestbase = (HttpEntityEnclosingRequestBase)obj;
            HttpEntity temp = httpconnectioninput.getRequestEntity();
            if(temp != null)
                httpentityenclosingrequestbase.setEntity(temp);
        }
        return ((HttpRequestBase) (obj));
    }

    public HttpConnectionResult execute()
    {
        return http_execute(null);
    }

    public HttpConnectionResult http_execute(final HttpConnectionHandler handler)
    {
        HttpConnectionResult httpconnectionresult ;
        boolean flag;
        httpconnectionresult = new HttpConnectionResult();
        try {
        flag = false;
        long l;
        int i;

        File file = _input.getSaveFile();
        if(_input.forceUpdate || file == null || !file.exists()) {
            System.out.println("HttpConnection.1");
            _httpClient = createHttpClient();
            _requestBase = createHttpRequestBase(_input);
            BasicHttpContext temp = new BasicHttpContext();
            ((HttpContext) (temp)).setAttribute("http.cookie-store", cookieStore);
            _response = _httpClient.execute(_requestBase, ((HttpContext) (temp)));

            if(_isCanceled)
                throw new InterruptedException();
            HttpEntity temp2 = _response.getEntity();
            l = ((HttpEntity) (temp2)).getContentLength();
            _inputStream = ((HttpEntity) (temp2)).getContent();
            Header temp3 = _response.getFirstHeader("Content-Encoding");
            if(temp3 != null) {
                if (((Header) (temp3)).getValue().equalsIgnoreCase("gzip"))
                    _inputStream = new GZIPInputStream(_inputStream);
            }
            i = _response.getStatusLine().getStatusCode();
            System.out.println("response.status."+i);
            if(_isCanceled)
                throw new InterruptedException();

        } else {
            System.out.println("HttpConnection.2");
            _inputStream = StorageUtil.load(file);
            System.out.println("HttpConnection.2"+_inputStream+":"+file.length());
            l = file.length();
            i = 200;
            flag = true;
        }

        long l1;

        l1 = 0L;
        Object obj;
        byte abyte0[];
        ByteArrayOutputStream byteos = new ByteArrayOutputStream();
        abyte0 = new byte[4096];
        int percentage =0;
        System.out.println("HttpConnection.3"+flag);
        if(!flag) {
//            break MISSING_BLOCK_LABEL_489;
            //final HttpConnectionHandler handler;
            System.out.println("HttpConnection.4");
            for (;;) {
                percentage = _inputStream.read(abyte0);
                if (percentage >= 0) {
                    ((ByteArrayOutputStream) (byteos)).write(abyte0, 0, percentage);
                    l1 += percentage;
                    percentage = (int) Math.floor((100L * l1) / l);
                    final int temp = percentage;
                    if (_currentThread == null) {
                        if (handler != null)
                            handler.onProgress(this, percentage);
                    } else {
                        _currentThread.post(new Runnable() {
                            public void run() {
                                if (handler != null)
                                    handler.onProgress(HttpConnection.this, temp);
                            }

                        });
                    }
                    if (_isCanceled)
                        throw new InterruptedException();
                } else
                    break;
            }
        }

        if(Math.floor(i / 200) == 1.0D && file != null && !flag) {
            //closeInputStream();
            StorageUtil.save(file, ((ByteArrayOutputStream) (byteos)).toByteArray());
            _inputStream = new ByteArrayInputStream(((ByteArrayOutputStream) (byteos)).toByteArray());
        }

        httpconnectionresult.inputStream = _inputStream;
        httpconnectionresult.contentLength = l;
        httpconnectionresult.httpStatus = i;
            System.out.println("HttpConnection.5."+httpconnectionresult.inputStream);
        return httpconnectionresult;
        //}


        } catch (Exception exx) {
            exx.printStackTrace();
            System.out.println("HttpConnection.exceptionurl."+_input.getURL());
        }
        return httpconnectionresult;

    }

    public void executeAsync(final HttpConnectionHandler handler)
    {
        _currentThread = new Handler();
        (new SDAsyncTask() {

            protected Object doInBackground(Object aobj[])
            {
                return doInBackground((Void[])aobj);
            }

            protected HttpConnectionResult doInBackground(Void avoid[])
            {
                return http_execute(handler);
            }

            protected void onPostExecute(Object obj)
            {
                onPostExecute((HttpConnectionResult)obj);
            }

            protected void onPostExecute(final HttpConnectionResult httpconnectionresult)
            {
                if(httpconnectionresult.exception == null)
                    _currentThread.post( new Runnable() {

                        public void run()
                        {
                            handler.onSuccess(HttpConnection.this, httpconnectionresult);
                        }

                    });
                else
                    _currentThread.post(new Runnable() {

                        public void run()
                        {
                            handler.onFailed(HttpConnection.this, httpconnectionresult);
                        }

                    });
                _currentThread = null;
            }

        }
).executeTask(new Void[0]);
    }

    private static ClientConnectionManager theConnectionManager = null;
    private static DefaultHttpClient theHttpClient = null;
    protected Handler _currentThread;
    protected DefaultHttpClient _httpClient;
    protected HttpConnectionInput _input;
    public InputStream _inputStream;
    protected boolean _isCanceled;
    protected HttpRequestBase _requestBase;
    protected HttpResponse _response;
    public CookieStore cookieStore;
    public boolean showProgress;

}
