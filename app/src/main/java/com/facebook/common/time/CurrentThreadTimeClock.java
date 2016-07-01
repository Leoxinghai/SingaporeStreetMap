

package com.facebook.common.time;

import android.os.SystemClock;

// Referenced classes of package com.facebook.common.time:
//            Clock

public class CurrentThreadTimeClock
    implements Clock
{

    public CurrentThreadTimeClock()
    {
    }

    public long now()
    {
        return SystemClock.currentThreadTimeMillis();
    }
}
