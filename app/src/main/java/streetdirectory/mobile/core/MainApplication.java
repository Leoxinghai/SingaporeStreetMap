// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.core;

import android.app.Application;
import android.content.*;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import java.util.*;

// Referenced classes of package streetdirectory.mobile.core:
//            SDStoryStats, SDStory, SDStoryGAProvider, SDStorySDProvider,
//            SDStoryLogglyProvider, SDLogger

public class MainApplication extends Application
{
    class GetInfoSDStory extends TimerTask
    {

        public void run()
        {
            Intent intent;
            IntentFilter intentfilter = new IntentFilter("android.intent.action.BATTERY_CHANGED");
            intent = MainApplication.context.registerReceiver(null, intentfilter);
            Object obj = null;
            if(intent != null) {
                switch (intent.getIntExtra("status", -1)) {
                    default:
                        break;
                    case 2:
                        obj = "Charging";
                        break;
                    case 3:
                        obj = "Discharging";
                        break;
                    case 5:
                        obj = "Full";
                        break;
                    case 4:
                        obj = "Not Charging";
                        break;
                    case 1:
                        obj = "Unknown";
                        break;
                }
                if (obj != null) {
                    SDStoryStats.batteryStatus = ((String) (obj));
                }

                String s = null;
                switch (intent.getIntExtra("plugged", -1)) {
                    default:
                        break;
                    case 1:
                        s = "AC";
                        break;
                    case 2:
                        s = "USB";
                        break;
                }

                if (obj != null) {
                    SDStoryStats.batteryCharger = s;
                }

                int i = intent.getIntExtra("level", -1);
                int j = intent.getIntExtra("scale", -1);
                SDStoryStats.batteryLevel = i;
                SDLogger.info((new StringBuilder()).append("Battery life ").append(i).append(" of ").append(j).toString());
            }

            try {
                tlpManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            }
            // Misplaced declaration of an exception variable
            catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
            obj = null;
            switch(tlpManager.getNetworkType()) {
                default:
                    break;
                case 7:
                    obj = "1xRTT";
                    break;
                case 0:
                    obj = "UNKNOWN";
                    break;
                case 4:
                    obj = "CDMA";
                    break;
                case 2:
                    obj = "EDGE";
                    break;
                case 14:
                    obj = "EHRPD";
                    break;
                case 5:
                    obj = "EVDO_0";
                    break;
                case 6:
                    obj = "EVDO_A";
                    break;
                case 12:
                    obj = "EVDO_B";
                    break;
                case 1:
                    obj = "GPRS";
                    break;
                case 8:
                    obj = "HSDPA";
                    break;
                case 10:
                    obj = "HSPA";
                    break;
                case 15:
                    obj = "HSPAP";
                    break;
                case 9:
                    obj = "HSUPA";
                    break;
                case 11:
                    obj = "IDEN";
                    break;
                case 13:
                    obj = "LTE";
                    break;
                case 3:
                    obj = "UMTS";
                    break;
            }
            if (obj != null)
                SDStoryStats.networkProvider = ((String) (obj));
            SDStoryStats.networkProviderName = tlpManager.getNetworkOperatorName();
            SDStoryStats.networkProviderName = tlpManager.getNetworkOperatorName();
            SDLogger.info((new StringBuilder()).append("Network info : ").append(((String) (obj))).toString());
            return;

        }


        GetInfoSDStory()
        {
            super();
        }
    }


    public MainApplication()
    {
    }

    public static Context getAppContext()
    {
        return context;
    }

    public static void setApplicationContext(Context _context) {
        context = _context;
    }

    public void onCreate()
    {
        super.onCreate();
        context = getApplicationContext();
        Object obj = getSharedPreferences("SDLG", 0);
        sid = ((SharedPreferences) (obj)).getString("sdlg_id", null);
        if(sid == null)
        {
            sid = UUID.randomUUID().toString();
            obj = ((SharedPreferences) (obj)).edit();
            ((android.content.SharedPreferences.Editor) (obj)).putString("sdlg_id", sid);
            ((android.content.SharedPreferences.Editor) (obj)).apply();
        }
        obj = android.provider.Settings.Secure.getString(getContentResolver(), "android_id");
        SDStoryStats.SDuId = sid;
        SDStoryStats.deviceID = ((String) (obj));
        SDStory.providers.add(new SDStoryGAProvider());
        SDStory.providers.add(new SDStorySDProvider());
        SDStory.providers.add(new SDStoryLogglyProvider());
        (new Timer()).schedule(new GetInfoSDStory(), 0L, 60000L);
    }

    private static Context context;
    private ConnectivityManager connManager;
    private String sid;
    private TelephonyManager tlpManager;
    private NetworkInfo wifiInfo;


/*
    static TelephonyManager access$102(MainApplication mainapplication, TelephonyManager telephonymanager)
    {
        mainapplication.tlpManager = telephonymanager;
        return telephonymanager;
    }

*/
}
