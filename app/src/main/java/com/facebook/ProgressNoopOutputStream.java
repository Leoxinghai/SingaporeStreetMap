

package com.facebook;

import android.os.Handler;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

// Referenced classes of package com.facebook:
//            RequestOutputStream, RequestProgress, Request

class ProgressNoopOutputStream extends OutputStream
    implements RequestOutputStream
{

    ProgressNoopOutputStream(Handler handler)
    {
        callbackHandler = handler;
    }

    void addProgress(long l)
    {
        if(currentRequestProgress == null)
        {
            currentRequestProgress = new RequestProgress(callbackHandler, currentRequest);
            progressMap.put(currentRequest, currentRequestProgress);
        }
        currentRequestProgress.addToMax(l);
        batchMax = (int)((long)batchMax + l);
    }

    int getMaxProgress()
    {
        return batchMax;
    }

    Map getProgressMap()
    {
        return progressMap;
    }

    public void setCurrentRequest(Request request)
    {
        currentRequest = request;
        RequestProgress temp;
        if(request != null)
            temp = (RequestProgress)progressMap.get(request);
        else
            temp = null;
        currentRequestProgress = temp;
    }

    public void write(int i)
    {
        addProgress(1L);
    }

    public void write(byte abyte0[])
    {
        addProgress(abyte0.length);
    }

    public void write(byte abyte0[], int i, int j)
    {
        addProgress(j);
    }

    private int batchMax;
    private final Handler callbackHandler;
    private Request currentRequest;
    private RequestProgress currentRequestProgress;
    private final Map progressMap = new HashMap();
}
