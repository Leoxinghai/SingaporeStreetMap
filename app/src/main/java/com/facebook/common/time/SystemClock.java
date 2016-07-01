

package com.facebook.common.time;


// Referenced classes of package com.facebook.common.time:
//            Clock

public class SystemClock
    implements Clock
{

    private SystemClock()
    {
    }

    public static SystemClock get()
    {
        return INSTANCE;
    }

    public long now()
    {
        return System.currentTimeMillis();
    }

    private static final SystemClock INSTANCE = new SystemClock();

}
