

package streetdirectory.mobile.core.service;

import java.io.IOException;
import java.io.InputStream;

public class HttpConnectionResult
{

    public HttpConnectionResult()
    {
        inputStream = null;
        httpStatus = -1;
        contentLength = -1L;
    }

    public void close()
    {
        try
        {
            if(inputStream != null)
            {
                inputStream.close();
                inputStream = null;
            }
            return;
        }
        catch(IOException ioexception)
        {
            ioexception.printStackTrace();
        }
    }

    public long contentLength;
    public Exception exception;
    public int httpStatus;
    public InputStream inputStream;
}
