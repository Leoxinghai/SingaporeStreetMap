

package com.facebook.common.executors;

import android.os.Handler;
import android.os.Looper;

// Referenced classes of package com.facebook.common.executors:
//            HandlerExecutorServiceImpl

public class UiThreadExecutorService extends HandlerExecutorServiceImpl
{

    private UiThreadExecutorService()
    {
        super(new Handler(Looper.getMainLooper()));
    }

    public static UiThreadExecutorService getInstance()
    {
        if(sInstance == null)
            sInstance = new UiThreadExecutorService();
        return sInstance;
    }

    private static UiThreadExecutorService sInstance = null;

}
