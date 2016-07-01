

package com.facebook;

import android.content.*;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import java.util.Iterator;
import java.util.Set;

// Referenced classes of package com.facebook:
//            AppEventsLogger

public class BoltsMeasurementEventListener extends BroadcastReceiver
{

    private BoltsMeasurementEventListener(Context context)
    {
        applicationContext = context.getApplicationContext();
    }

    private void close()
    {
        LocalBroadcastManager.getInstance(applicationContext).unregisterReceiver(this);
    }

    static BoltsMeasurementEventListener getInstance(Context context)
    {
        if(_instance != null)
        {
            return _instance;
        } else
        {
            _instance = new BoltsMeasurementEventListener(context);
            _instance.open();
            return _instance;
        }
    }

    private void open()
    {
        LocalBroadcastManager.getInstance(applicationContext).registerReceiver(this, new IntentFilter("com.parse.bolts.measurement_event"));
    }

    protected void finalize()
        throws Throwable
    {
        super.finalize();
        close();
        return;
    }

    public void onReceive(Context context, Intent intent)
    {
        AppEventsLogger temp = AppEventsLogger.newLogger(context);
        String s = (new StringBuilder()).append("bf_").append(intent.getStringExtra("event_name")).toString();
        Bundle bundle = new Bundle();
        bundle = intent.getBundleExtra("event_args");
        String s1;
        for(Iterator iterator = bundle.keySet().iterator(); iterator.hasNext(); bundle.putString(s1.replaceAll("[^0-9a-zA-Z _-]", "-").replaceAll("^[ -]*", "").replaceAll("[ -]*$", ""), (String)bundle.get(s1)))
            s1 = (String)iterator.next();

        temp.logEvent(s, bundle);
    }

    private static final String BOLTS_MEASUREMENT_EVENT_PREFIX = "bf_";
    private static final String MEASUREMENT_EVENT_ARGS_KEY = "event_args";
    private static final String MEASUREMENT_EVENT_NAME_KEY = "event_name";
    private static final String MEASUREMENT_EVENT_NOTIFICATION_NAME = "com.parse.bolts.measurement_event";
    private static BoltsMeasurementEventListener _instance;
    private Context applicationContext;
}
