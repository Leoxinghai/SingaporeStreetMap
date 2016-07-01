// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile;

import android.content.*;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.ads.InterstitialAd;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.Kit;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import streetdirectory.mobile.core.MainApplication;
import streetdirectory.mobile.core.SDLogger;
import streetdirectory.mobile.core.SDPreferences;
import streetdirectory.mobile.core.SDStory;
import streetdirectory.mobile.modules.offlinemap.OfflineMapManager;
import streetdirectory.mobile.modules.sdmob.SdMobHelper;
import streetdirectory.mobile.modules.sdmob.SdMobServiceV2;
import streetdirectory.mobile.sd.SDBlackboard;
import streetdirectory.mobile.sd.SDMapMenuItemProvider;
import streetdirectory.mobile.sd.SdMob;
import streetdirectory.mobile.service.SDHttpServiceOutput;
import streetdirectory.mobile.service.URLFactory;
import streetdirectory.mobile.service.countrylist.CountryListService;
import streetdirectory.mobile.sitt.SittClientNode;
import streetdirectory.mobile.sitt.SittManager;
import streetdirectory.mobile.sitt.SittSignalNode;

public class SDApplication extends MainApplication
{

    public SDApplication()
    {
        sittOldNodes = new ArrayList();
    }

    private static boolean checkAnnouncement()
    {
        boolean flag1 = false;
        Object obj = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date date;
        boolean flag;
        boolean flag2 = false;
        try
        {
            date = ((SimpleDateFormat) (obj)).parse(((SimpleDateFormat) (obj)).format(new Date()));
            obj = ((SimpleDateFormat) (obj)).parse(SDPreferences.getInstance().getAnnouncementCheckedDate());
        }
        catch(ParseException parseexception)
        {
            parseexception.printStackTrace();
            return false;
        }
        flag = flag1;
        if(!((Date) (obj)).equals(date)) {
            flag2 = ((Date) (obj)).before(date);
            flag = flag1;
        }
        if(flag2)
            flag = true;
        return flag;
    }

    public static void downloadCountryList()
    {
        (new CountryListService() {

            public void onFailed(Exception exception)
            {
                SDLogger.printStackTrace(exception);
            }

            public void onSuccess(Object obj)
            {
                onSuccess((SDHttpServiceOutput)obj);
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                if(sdhttpserviceoutput.childs.size() > 0)
                    SDBlackboard.countryList = sdhttpserviceoutput.childs;
            }

        }
).executeAsync();
    }

    public static String getGid()
    {
        if(MainApplication.getAppContext() == null)
            _gid = UUID.randomUUID().toString();
        else {
            Object obj = MainApplication.getAppContext().getSharedPreferences("SDMobile", 0);
            //Object obj = getSharedPreferences("SDMobile", 0);
            _gid = ((SharedPreferences) (obj)).getString("sdmobile_gid", null);
            if (_gid == null) {
                _gid = UUID.randomUUID().toString();
                obj = ((SharedPreferences) (obj)).edit();
                ((android.content.SharedPreferences.Editor) (obj)).putString("sdmobile_gid", _gid);
                ((android.content.SharedPreferences.Editor) (obj)).apply();
            }
        }
        return _gid;
    }

    public static InterstitialAd getInterstitialAd()
    {
        return mInterstitialAd;
    }

    public static void initFacebookInterstitialAd(String s)
    {
    }

    public static void initInterstitialAd(String s)
    {
        mInterstitialAd = new InterstitialAd(getAppContext());
        mInterstitialAd.setAdUnitId(s);
    }

    public static boolean isBannerRequested()
    {
        return mIsBannerRequested;
    }

    public static boolean isFBInsterstitialIsLoading()
    {
        return mIsFBInterstitialLoading;
    }

    public static void loadInterstitialAd()
    {
        streetdirectory.mobile.modules.sdmob.SdMobHelper.SdMobUnit sdmobunit = SdMobHelper.getInstance(getAppContext()).getSdMobUnit(SdMob.ad_int_splash);
        switch(sdmobunit.type)
        {
        default:
            return;

        case 1: // '\001'
            initInterstitialAd(sdmobunit.id);
            requestInterstitialAd();
            return;

        case 2: // '\002'
            initFacebookInterstitialAd(sdmobunit.id);
            break;
        }
        requestFBInterstitialAd();
    }

    private static void reqSdMob()
    {
        (new SdMobServiceV2(new streetdirectory.mobile.modules.sdmob.SdMobServiceV2.Input(getAppContext())) {

            public void onSuccess(Object obj)
            {
                onSuccess((SDHttpServiceOutput)obj);
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                super.onSuccess(sdhttpserviceoutput);
                SdMobHelper sdmobhelper = SdMobHelper.getInstance(MainApplication.getAppContext());
                for(Iterator iterator = sdhttpserviceoutput.childs.iterator(); iterator.hasNext(); sdmobhelper.saveSdMobUnit(((streetdirectory.mobile.modules.sdmob.SdMobServiceV2.Output)iterator.next()).unit));
                Intent intent = new Intent("sdmob_broadcast");
                LocalBroadcastManager.getInstance(MainApplication.getAppContext()).sendBroadcast(intent);
            }

        }
).executeAsync();
    }

    public static void requestFBInterstitialAd()
    {
    }

    public static void requestInterstitialAd()
    {
        if(mInterstitialAd.getAdUnitId() != null)
        {
            com.google.android.gms.ads.AdRequest adrequest = (new com.google.android.gms.ads.AdRequest.Builder()).addTestDevice("365F1FBD4BE02294A31FC529AAD9CDD7").build();
            mInterstitialAd.loadAd(adrequest);
        }
    }

    public static void setBannerRequested(boolean flag)
    {
        mIsBannerRequested = flag;
    }

    public static void setFullBannerVisible(boolean flag)
    {
        showFullBanner = flag;
    }

    public static void setIsFBInterstitialLoading(boolean flag)
    {
        mIsFBInterstitialLoading = flag;
    }

    public static boolean showFullBanner()
    {
        return showFullBanner;
    }

    public static boolean showInterstitialAd()
    {
        switch(SdMobHelper.getInstance(getAppContext()).getSdMobUnit(SdMob.ad_int_splash).type) {
            default:
                break;
            case 1:
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    return true;
                }
                break;
        }
        return false;
    }

    public static void showUploadResultToast(boolean flag)
    {
        if(flag)
        {
            Toast.makeText(getAppContext(), "Receipt uploaded successfully", 1).show();
            return;
        } else
        {
            Toast.makeText(getAppContext(), "Failed to upload receipt", 1).show();
            return;
        }
    }

    public static void startRequestBanner()
    {
        sdMobTimer = new Timer();
        sdMobTimer.schedule(new TimerTask() {

            public void run()
            {
                (new Handler(MainApplication.getAppContext().getMainLooper())).post(new Runnable() {
                    public void run()
                    {
                        SDApplication.reqSdMob();
                    }
                });
            }

        }
, 0, 0x36ee80);
        mIsBannerRequested = true;
    }

    public void onCreate()
    {
        super.onCreate();
        streetdirectory.mobile.modules.sdmob.SdMobHelper.SdMobUnit sdmobunit;
        Fabric.with(this, new Kit[] {
            new Crashlytics()
        });
        Fresco.initialize(this);
        sdmobunit = SdMobHelper.getInstance(this).getSdMobUnit(SdMob.ad_int_splash);
        switch(sdmobunit.type) {
            default:
                break;
            case 1:
                initInterstitialAd(sdmobunit.id);
                break;
            case 2:
                initFacebookInterstitialAd(sdmobunit.id);
                break;
        }
        sitt = SittManager.getInstance();
        sitt.start(MainApplication.getAppContext());
        sitt.addClientNodeUpdateListener(new streetdirectory.mobile.sitt.SittManager.ClientNodeUpdateListener() {
                 public void onUpdate(List list) {
                     StringBuilder stringbuilder = new StringBuilder();
                     Iterator iterator = list.iterator();
                     do {
                         if (!iterator.hasNext())
                             break;
                         SittClientNode sittclientnode = (SittClientNode) iterator.next();
                         if (SittManager.isQualifiedForRecord(SittManager.getClientNode(sittOldNodes, sittclientnode.signalNode.bssid), sittclientnode)) {
                             stringbuilder.append(sittclientnode.signalNode.bssid);
                             stringbuilder.append(";");
                             stringbuilder.append(String.format("%.2f", new Object[]{
                                     Double.valueOf(sittclientnode.signalNode.level)
                             }));
                             stringbuilder.append(";");
                             stringbuilder.append(String.format("%.2f", new Object[]{
                                     Double.valueOf(sittclientnode.distance)
                             }));
                             stringbuilder.append(",");
                         }
                     } while (true);
                     if (stringbuilder.length() > 0) {
                         stringbuilder.setLength(stringbuilder.length() - 1);
                         HashMap hashmap = SDStory.createDefaultParams();
                         hashmap.put("sitt", stringbuilder.toString());
                         SDStory.post(URLFactory.createGantSitt(), hashmap);
                     }
                     sittOldNodes = list;
                 }
             }
        );
        OfflineMapManager.moveMapData();
        SDMapMenuItemProvider.init();
        return;
    }

    private static String _gid = null;
    public static boolean firstTimeGps = true;
    private static InterstitialAd mInterstitialAd;
    private static boolean mIsBannerRequested = false;
    private static boolean mIsFBInterstitialLoading = false;
    private static Timer sdMobTimer;
    private static boolean showFullBanner = true;
    private SittManager sitt;
    private List sittOldNodes;




/*
    static List access$002(SDApplication sdapplication, List list)
    {
        sdapplication.sittOldNodes = list;
        return list;
    }

*/

}
