

package streetdirectory.mobile.core;

import android.content.Context;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

public class GantTools
{

    public GantTools()
    {
    }

    public static void dispatch()
    {
    }

    public static void startSession(Context context)
    {
        if(activityCount == 0 && mTracker == null)
            mTracker = GoogleAnalytics.getInstance(context.getApplicationContext()).newTracker(ACCOUNT);
        activityCount++;
    }

    public static void stopSession()
    {
    }

    public static void trackPageView(String s)
    {
        if(mTracker != null && s != null)
        {
            mTracker.setScreenName(s.replace(" ", "_"));
            mTracker.send((new com.google.android.gms.analytics.HitBuilders.ScreenViewBuilder()).build());
        }
    }

    private static String ACCOUNT = "UA-15222285-56";
    static int activityCount = 0;
    static Tracker mTracker = null;

}
