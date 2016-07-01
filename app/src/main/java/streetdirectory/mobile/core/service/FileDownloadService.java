// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.core.service;

import android.os.Handler;
import android.util.Log;
import java.io.*;
import java.util.zip.GZIPInputStream;
import org.apache.http.*;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import streetdirectory.mobile.core.SDAsyncTask;

// Referenced classes of package streetdirectory.mobile.core.service:
//            HttpConnection, HttpConnectionInput, HttpConnectionAbortException, HttpConnectionHandler,
//            HttpConnectionResult

public class FileDownloadService extends HttpConnection
{

    public FileDownloadService(final String url, final File savePath)
    {
        super(new HttpConnectionInput() {

            public File getSaveFile()
            {
                return savePath;
            }

            public String getURL()
            {
                return url;
            }

        });

        maxRetryCount = 3;
        useResume = false;
    }

    public FileDownloadService(HttpConnectionInput httpconnectioninput)
    {
        super(httpconnectioninput);
        maxRetryCount = 3;
        useResume = false;
        if(httpconnectioninput.getSaveFile() == null)
            throw new IllegalArgumentException("Save Path must not null");
        else
            return;
    }

    public HttpConnectionResult execute(final HttpConnectionHandler e)
    {
        File file;
        long l;
        long l1;
        l = 0L;
        Object obj;
        byte abyte0[];
        int j;
        final long totalLength;

        file = _input.getSaveFile();
        if(file.exists()) {
            l1 = l;
            if(useResume)
                l1 = file.length();
        } else {
            File pfile = file.getParentFile();
            l1 = l;
            if (!pfile.exists()) {
                pfile.mkdirs();
                l1 = l;
            }
        }


        try {
        int i;
//        e = null;
        HttpConnectionHandler httpconnectionhandler = e;
        HttpConnectionHandler httpconnectionhandler1 = e;

        i = 0;

        for(;i < maxRetryCount;) {

            l = l1;
            _httpClient = createHttpClient();
            _requestBase = createHttpRequestBase(_input);
            if (l1 > 0L) {
                _requestBase.addHeader("Range", (new StringBuilder()).append("bytes=").append(l1).append("-").toString());
                BasicHttpContext basichttpcontext = new BasicHttpContext();
                basichttpcontext.setAttribute("http.cookie-store", cookieStore);
                _response = _httpClient.execute(_requestBase, basichttpcontext);
            }

            if (_isCanceled) {
                httpconnectionhandler1 = httpconnectionhandler;
                if (_currentThread == null) {
                    onAborted(new HttpConnectionAbortException());
                } else {
                    _currentThread.post(new Runnable() {

                        public void run() {
                            onAborted(new HttpConnectionAbortException());
                        }

                    });

                }

                closeInputStream();
                if (httpconnectionhandler != null)
                try {
                        //httpconnectionhandler.close();
                    if (_inputStream != null)
                        _inputStream.close();
                }
                // Misplaced declaration of an exception variable
                catch (IOException iex0) {
                    return null;
                }
                return null;
            }

            j = i + 1;
            httpconnectionhandler1 = httpconnectionhandler;
            if (j >= maxRetryCount) {
                if (_currentThread == null) {
                    //onFailed(e);
                } else {
                    /*
                    _currentThread.post(new Runnable() {
                        public void run() {
                            onFailed(e);
                        }

                    });
                    */
                }

            }

            closeInputStream();

/*
            if (httpconnectionhandler != null)
                try {
                    httpconnectionhandler.close();
                }
                // Misplaced declaration of an exception variable
                catch (Exception ex05) {
                }
*/
            l1 = l;
            i = j;
            if (_inputStream != null) {
                _inputStream.close();
            }
        }

        l = l1;
        httpconnectionhandler = e;
        httpconnectionhandler1 = e;
        obj = _response.getEntity();
        totalLength = ((HttpEntity) (obj)).getContentLength() + l1;
        Log.d("Offline Map", Long.toString(totalLength));
        if(l1 >= totalLength) {
            l = l1;
            if(_currentThread == null) {
                onSuccess();
            } else {
                _currentThread.post(new Runnable() {

                    public void run() {
                        onSuccess();
                    }

                });
            }

            _inputStream = ((HttpEntity) (obj)).getContent();
            obj = _response.getFirstHeader("Content-Encoding");
            if(obj != null &&((Header) (obj)).getValue().equalsIgnoreCase("gzip")) {
                _inputStream = new GZIPInputStream(_inputStream);
                //if (!_isCanceled)
                //    break;
            }
        }


        httpconnectionhandler = e;
        httpconnectionhandler1 = e;
            OutputStream outputstream;
        if(l1 <= 0L) {
            l = l1;
            outputstream = new FileOutputStream(file);
        } else {
            l = l1;
            outputstream = new FileOutputStream(file, true);
        }

        l = l1;
        abyte0 = new byte[4096];

        l = l1;

        for(;;)  {
            j = _inputStream.read(abyte0);
            if(j <0)
                break;
            l = l1;
            outputstream.write(abyte0, 0, j);
            final long finalDownloadedSize;
            finalDownloadedSize = l1 + (long) j;
            l = finalDownloadedSize;

            if (_currentThread == null) {
                onProgress(finalDownloadedSize, totalLength);
            } else {
                _currentThread.post(new Runnable() {

                    public void run() {
                        onProgress(finalDownloadedSize, totalLength);
                    }
                });
            }

            l = finalDownloadedSize;
            l1 = finalDownloadedSize;
            if (_isCanceled)
                break;
        }



        l = l1;
        httpconnectionhandler = e;
        httpconnectionhandler1 = e;
        _inputStream.close();
        if(_currentThread == null) {
            onSuccess();
        } else {
            _currentThread.post(new Runnable() {

                public void run() {
                    onSuccess();
                }

            });
        }

        closeInputStream();
        if(e != null)
            try
            {
                outputstream.close();
            }
            // Misplaced declaration of an exception variable
            catch(Exception ex) { }
        if(_inputStream == null)
            return null;
        _inputStream.close();
        } catch(IOException iex) {
            iex.printStackTrace();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return null;

    }

    public void executeAsync(HttpConnectionHandler httpconnectionhandler)
    {
        _currentThread = new Handler();
        (new SDAsyncTask() {

            protected Object doInBackground(Object aobj[])
            {
                return doInBackground((Void[])aobj);
            }

            protected Void doInBackground(Void avoid[])
            {
                execute();
                return null;
            }

            protected void onPostExecute(Object obj)
            {
                onPostExecute((Void)obj);
            }

            protected void onPostExecute(Void void1)
            {
                _currentThread = null;
            }

        }).executeTask(new Void[0]);
    }

    public void onAborted(Exception exception)
    {
    }

    public void onFailed(Exception exception)
    {
    }

    public void onProgress(long l, long l1)
    {
    }

    public void onSuccess()
    {
    }

    public int maxRetryCount;
    public boolean useResume;
}
