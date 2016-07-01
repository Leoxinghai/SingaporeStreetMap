// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.sd;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.io.File;
import java.util.Date;
import streetdirectory.mobile.SDApplication;
import streetdirectory.mobile.core.*;
import streetdirectory.mobile.core.storage.*;
import streetdirectory.mobile.gis.GeoSense;
import streetdirectory.mobile.modules.SDActivity;
import streetdirectory.mobile.modules.map.MapActivity;
import streetdirectory.mobile.modules.sdmob.FullScreenBanner;
import streetdirectory.mobile.modules.sdmob.SdMobHelper;
import streetdirectory.mobile.service.URLFactory;
import streetdirectory.mobile.service.apiversion.APIVersionService;

// Referenced classes of package streetdirectory.mobile.sd:
//            SdMob

public class SplashActivity extends SDActivity
{

    public SplashActivity()
    {
        mAdVisible = false;
    }

    private void copyAssets()
    {
        int i = 0;
        try {
            int j = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
            i = j;
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        long l = System.nanoTime();
        SDPreferences sdpreferences = SDPreferences.getInstance();
        double d;
        if(sdpreferences.getIntForKey("assetsVersion", 0) < i)
        {
            AssetsUtil.copy("internal", InternalStorage.getStorageDirectory(), true);
            File file = CacheStorage.getStorageDirectory();
            AssetsUtil.copy("cache/data", new File(file, "data"), false);
            AssetsUtil.copy("cache/xml", new File(file, "xml"), true);
            sdpreferences.setValueForKey("assetsVersion", i);
            if(ExternalStorage.getStorageDirectory() != null)
                StorageUtil.createNoMediaFile((new StringBuilder()).append(ExternalStorage.getStorageDirectory().getPath()).append("/offline_map/").toString());
            StorageUtil.createNoMediaFile((new StringBuilder()).append(InternalStorage.getStorageDirectory().getPath()).append("/offline_map/").toString());
        } else
        {
            AssetsUtil.copy("cache/xml", new File(CacheStorage.getStorageDirectory(), "xml"), false);
        }
        d = (double)(System.nanoTime() - l) / 1000000000D;
        runOnUiThread(new Runnable() {

            public void run()
            {
            }

        });
        return;
    }

    private void goToMap()
    {
        overridePendingTransition(0, 0);
        startActivity(new Intent(this, MapActivity.class));
        finish();
    }

    private void initBlockingLayout()
    {
        TextView textview = (TextView)mLayoutBlocking.findViewById(R.id.text_view_title);
        TextView textview1 = (TextView)mLayoutBlocking.findViewById(R.id.text_view_message);
        Button button = (Button)mLayoutBlocking.findViewById(R.id.button_exit);
        Button button1 = (Button)mLayoutBlocking.findViewById(R.id.button_update);
        textview.setText(getString(R.string.app_name));
        textview1.setText((new StringBuilder()).append("Your ").append(getString(R.string.app_name)).append(" is outdated. You can no longer use It. Please update to the latest version.").toString());
        button.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                Intent intent = new Intent(SplashActivity.this, SplashActivity.class);
                intent.setFlags(0x4000000);
                intent.putExtra("EXIT", true);
                startActivity(intent);
                finish();
            }

        });
        button1.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                String packageName = getPackageName();
                try
                {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse((new StringBuilder()).append("market://details?id=").append(packageName).toString())));
                    return;
                }
                catch(Exception ex)
                {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse((new StringBuilder()).append("https://play.google.com/store/apps/details?id=").append(packageName).toString())));
                }
            }

        });
    }

    private void loadFullScreenBanner()
    {
        FullScreenBanner fullscreenbanner = FullScreenBanner.getBannerFromSdMobUnit(SdMobHelper.getInstance(this).getSdMobUnit(SdMob.ad_int_splash));
        fullscreenbanner.setFullScreenBannerListener(new streetdirectory.mobile.modules.sdmob.FullScreenBanner.FullScreenBannerListener() {

            public void onAdClosed()
            {
                goToMap();
            }

            public void onAdFailed()
            {
                goToMap();
            }

            public void onAdLoaded()
            {
                mAdVisible = true;
                SDApplication.loadInterstitialAd();
                SDPreferences.getInstance().setInterstitialAdLastShow(new Date());
            }

        });
        fullscreenbanner.presentView(this);
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        MainApplication.setApplicationContext(this.getApplicationContext());


        boolean flag;
        boolean flag1;
        int i;
        setContentView(R.layout.activity_splash);
        SDStory.post(URLFactory.createGantOthersSplash(), SDStory.createDefaultParams());
        if(getIntent().getBooleanExtra("EXIT", false))
            finish();
        mLayoutBlocking = (LinearLayout)findViewById(R.id.layout_blocking);
        mLayoutBlocking.setVisibility(View.INVISIBLE);
        initBlockingLayout();
        flag1 = false;
        i = SDPreferences.getInstance().getAppVersion();
        flag = flag1;
        try {
            if (i != 0 && getPackageManager().getPackageInfo(getPackageName(), 0).versionCode < i) {
                mLayoutBlocking.setVisibility(View.VISIBLE);
                flag = true;
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        if(!flag)
        {
            setupDataInBackground();
            SDApplication.setBannerRequested(false);
            SDApplication.setFullBannerVisible(true);
            SDApplication.startRequestBanner();
        }
        (new Handler()).postDelayed(new Runnable() {

            public void run()
            {
                if(!mAdVisible)
                    goToMap();
            }

        }
, 15000L);
        return;
    }

    protected void onDestroy()
    {
        super.onDestroy();
    }

    public void setupDataInBackground()
    {
        (new SDAsyncTask() {

            protected Object doInBackground(Object aobj[])
            {
                return doInBackground((Void[])aobj);
            }

            protected Void doInBackground(Void avoid[])
            {
                copyAssets();
                GeoSense.readData();
                return null;
            }

            protected void onPostExecute(Object obj)
            {
                onPostExecute((Void)obj);
            }

            protected void onPostExecute(Void void1)
            {
                APIVersionService.updateInBackground();
                loadFullScreenBanner();
            }

        }
).executeTask(new Void[0]);
    }

    private boolean mAdVisible;
    private LinearLayout mLayoutBlocking;



/*
    static boolean access$002(SplashActivity splashactivity, boolean flag)
    {
        splashactivity.mAdVisible = flag;
        return flag;
    }

*/



}
