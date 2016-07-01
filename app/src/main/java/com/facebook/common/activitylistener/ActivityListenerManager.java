

package com.facebook.common.activitylistener;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import com.facebook.common.internal.Preconditions;
import java.lang.ref.WeakReference;

// Referenced classes of package com.facebook.common.activitylistener:
//            ListenableActivity, ActivityListener, BaseActivityListener

public class ActivityListenerManager
{
    private static class Listener extends BaseActivityListener
    {

        private ActivityListener getListenerOrCleanUp(Activity activity)
        {
            ActivityListener activitylistener = (ActivityListener)mActivityListenerRef.get();
            if(activitylistener == null)
            {
                Preconditions.checkArgument(activity instanceof ListenableActivity);
                ((ListenableActivity)activity).removeActivityListener(this);
            }
            return activitylistener;
        }

        public void onActivityCreate(Activity activity)
        {
            ActivityListener activitylistener = getListenerOrCleanUp(activity);
            if(activitylistener != null)
                activitylistener.onActivityCreate(activity);
        }

        public void onDestroy(Activity activity)
        {
            ActivityListener activitylistener = getListenerOrCleanUp(activity);
            if(activitylistener != null)
                activitylistener.onDestroy(activity);
        }

        public void onPause(Activity activity)
        {
            ActivityListener activitylistener = getListenerOrCleanUp(activity);
            if(activitylistener != null)
                activitylistener.onPause(activity);
        }

        public void onResume(Activity activity)
        {
            ActivityListener activitylistener = getListenerOrCleanUp(activity);
            if(activitylistener != null)
                activitylistener.onResume(activity);
        }

        public void onStart(Activity activity)
        {
            ActivityListener activitylistener = getListenerOrCleanUp(activity);
            if(activitylistener != null)
                activitylistener.onStart(activity);
        }

        public void onStop(Activity activity)
        {
            ActivityListener activitylistener = getListenerOrCleanUp(activity);
            if(activitylistener != null)
                activitylistener.onStop(activity);
        }

        private final WeakReference mActivityListenerRef;

        public Listener(ActivityListener activitylistener)
        {
            mActivityListenerRef = new WeakReference(activitylistener);
        }
    }


    public ActivityListenerManager()
    {
    }

    public static void register(ActivityListener activitylistener, Context context)
    {
        Context context1 = context;
        if(!(context instanceof ListenableActivity))
        {
            context1 = context;
            if(context instanceof ContextWrapper)
                context1 = ((ContextWrapper)context).getBaseContext();
        }
        if(context1 instanceof ListenableActivity)
            ((ListenableActivity)context1).addActivityListener(new Listener(activitylistener));
    }
}
