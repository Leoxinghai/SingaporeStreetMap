

package com.facebook.common.executors;

import android.os.Handler;
import android.os.Looper;

// Referenced classes of package com.facebook.common.executors:
//            HandlerExecutorServiceImpl

public class UiThreadImmediateExecutorService extends HandlerExecutorServiceImpl
{

    private UiThreadImmediateExecutorService()
    {
        super(new Handler(Looper.getMainLooper()));
    }

    public static UiThreadImmediateExecutorService getInstance()
    {
        if(sInstance == null)
            sInstance = new UiThreadImmediateExecutorService();
        return sInstance;
    }

    public void execute(Runnable runnable)
    {
        if(isHandlerThread())
        {
            runnable.run();
            return;
        } else
        {
            super.execute(runnable);
            return;
        }
    }

    private static UiThreadImmediateExecutorService sInstance = null;

}
