

package com.facebook.common.time;

import android.os.SystemClock;

// Referenced classes of package com.facebook.common.time:
//            MonotonicClock

public class RealtimeSinceBootClock
    implements MonotonicClock
{

    private RealtimeSinceBootClock()
    {
    }

    public static RealtimeSinceBootClock get()
    {
        return INSTANCE;
    }

    public long now()
    {
        return SystemClock.elapsedRealtime();
    }

    private static final RealtimeSinceBootClock INSTANCE = new RealtimeSinceBootClock();

}
