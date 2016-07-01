

package com.facebook.common.activitylistener;

import android.app.Activity;

public interface ActivityListener
{

    public abstract void onActivityCreate(Activity activity);

    public abstract void onDestroy(Activity activity);

    public abstract void onPause(Activity activity);

    public abstract void onResume(Activity activity);

    public abstract void onStart(Activity activity);

    public abstract void onStop(Activity activity);
}
