// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook;

import android.os.Handler;

// Referenced classes of package com.facebook:
//            Settings, Request

class RequestProgress
{

    RequestProgress(Handler handler, Request request1)
    {
        request = request1;
        callbackHandler = handler;
    }

    void addProgress(long l)
    {
        progress = progress + l;
        if(progress >= lastReportedProgress + threshold || progress >= maxProgress)
            reportProgress();
    }

    void addToMax(long l)
    {
        maxProgress = maxProgress + l;
    }

    long getMaxProgress()
    {
        return maxProgress;
    }

    long getProgress()
    {
        return progress;
    }

    void reportProgress()
    {
        if(progress > lastReportedProgress)
        {
            final Request.Callback callbackCopy = request.getCallback();
            if(maxProgress > 0L && (callbackCopy instanceof Request.OnProgressCallback))
            {
                final long currentCopy = progress;
                final long maxProgressCopy = maxProgress;
                final Request.OnProgressCallback callbackCopy0 = (Request.OnProgressCallback)callbackCopy;
                if(callbackHandler == null)
                    callbackCopy0.onProgress(currentCopy, maxProgressCopy);
                else
                    callbackHandler.post(new Runnable() {

                        public void run()
                        {
                            callbackCopy0.onProgress(currentCopy, maxProgressCopy);
                        }

                    });
                lastReportedProgress = progress;
            }
        }
    }

    private final Handler callbackHandler;
    private long lastReportedProgress;
    private long maxProgress;
    private long progress;
    private final Request request;
    private final long threshold = Settings.getOnProgressThreshold();
}
