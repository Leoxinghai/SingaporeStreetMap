

package com.facebook.common.activitylistener;


// Referenced classes of package com.facebook.common.activitylistener:
//            ActivityListener

public interface ListenableActivity
{

    public abstract void addActivityListener(ActivityListener activitylistener);

    public abstract void removeActivityListener(ActivityListener activitylistener);
}
