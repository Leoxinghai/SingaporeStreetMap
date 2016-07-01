

package streetdirectory.mobile.modules.map;

import android.app.AlertDialog;
import android.content.*;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.util.ArrayList;
import java.util.Iterator;
import streetdirectory.mobile.SDApplication;
import streetdirectory.mobile.core.*;
import streetdirectory.mobile.gis.GeoPoint;
import streetdirectory.mobile.gis.maps.MapPinLayer;
import streetdirectory.mobile.gis.maps.MapView;
import streetdirectory.mobile.gis.maps.configs.MapPresetCollection;
import streetdirectory.mobile.modules.SDActivity;
import streetdirectory.mobile.modules.bottombanner.BottomBannerLayout;
import streetdirectory.mobile.modules.core.LocationBusinessServiceOutput;
import streetdirectory.mobile.modules.locationdetail.bus.BusArrivalActivity;
import streetdirectory.mobile.modules.locationdetail.businessin.BusinessInActivity;
import streetdirectory.mobile.modules.locationdetail.erp.ErpDetailActivity;
import streetdirectory.mobile.modules.locationdetail.expresswayexit.ExpressWayExitActivity;
import streetdirectory.mobile.modules.locationdetail.trafficcam.TrafficCameraLocationDetailActivity;
import streetdirectory.mobile.modules.map.layers.BuildingVectorLayer;
import streetdirectory.mobile.modules.map.layers.BuildingVectorServiceOutput;
import streetdirectory.mobile.modules.map.layers.BusLayer;
import streetdirectory.mobile.modules.map.layers.BusLayerServiceOutput;
import streetdirectory.mobile.modules.map.layers.OfferLayer;
import streetdirectory.mobile.modules.nearby.service.NearbyService;
import streetdirectory.mobile.modules.sdmob.*;
import streetdirectory.mobile.sd.*;
import streetdirectory.mobile.service.URLFactory;
import streetdirectory.mobile.service.countrylist.CountryListServiceOutput;


public class MapActivity extends SDActivity
{

    public MapActivity()
    {
        defaultLongitude = 0.0D;
        defaultLatitude = 0.0D;
        mSdMobReceiver = new BroadcastReceiver() {

            public void onReceive(Context context, Intent intent)
            {
                populateBanner();
            }

        };
        adRequestRetryCount = 0;
        popUpMessageReceiver = new BroadcastReceiver() {

            public void onReceive(Context context, Intent intent)
            {
                onApiVersionLoaded();
            }

        };
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
                Intent intent = new Intent(MapActivity.this, MapActivity.class);
                intent.setFlags(0x4000000);
                intent.putExtra("EXIT", true);
                startActivity(intent);
                finish();
            }
        });
        button1.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                openPlayStore();
            }

        });
    }

    private void initData()
    {
        originalIntent = getIntent();
        pinClass = (Class)originalIntent.getSerializableExtra("pinActivityClass");
        if(pinClass != null)
        {
            pinLayer.longitude = originalIntent.getDoubleExtra("pinLongitude", 0.0D);
            pinLayer.latitude = originalIntent.getDoubleExtra("pinLatitude", 0.0D);
            pinLayer.setTitle(originalIntent.getStringExtra("pinTitle"));
            pinLayer.setVisibility(View.VISIBLE);
            defaultLongitude = pinLayer.longitude;
            defaultLatitude = pinLayer.latitude;
        }
        mMapView.setOnDragListener(new streetdirectory.mobile.gis.maps.MapView.OnDragListener() {

            public void onEndDrag(MapView mapview)
            {
                SDBlackboard.currentLongitude = mapview.center.longitude;
                SDBlackboard.currentLatitude = mapview.center.latitude;
                android.content.SharedPreferences.Editor temp = SDPreferences.getInstance().createEditor();
                temp.putString("last_map_longitude", Double.toString(SDBlackboard.currentLongitude));
                temp.putString("last_map_latitude", Double.toString(SDBlackboard.currentLatitude));
                temp.commit();
                SDLogger.info("mapview enddrag");
                setCurrentCountryCode();
            }

            public void onStartDrag(MapView mapview)
            {
                SDLogger.info("mapview startdrag");
            }

        });
        mMapView.setOnCompassChangedListener(new streetdirectory.mobile.gis.maps.MapView.OnCompassChangedListener() {

            public void onCompassChanged(boolean flag)
            {
                MapPinLayer mappinlayer = pinLayer;
                byte byte0;
                if(flag)
                    byte0 = 8;
                else
                    byte0 = 0;
                mappinlayer.setVisibility(byte0);
            }

        });
        mMapView.setOnAfterUpdateListener(new streetdirectory.mobile.gis.maps.MapView.OnFinishUpdateMapListener() {

            public void onFinishUpdateMap(MapView mapview)
            {
                SDBlackboard.currentLongitude = mapview.center.longitude;
                SDBlackboard.currentLatitude = mapview.center.latitude;
                SDLogger.info("mapview afterupdate");
                if(mMapView.getCurrentLevelOrdinal() != 0)
                    setCurrentCountryCode();
            }

        });

        //MapPresetCollection.loadOfflineInBackground(new streetdirectory.mobile.gis.maps.configs.MapPresetCollection.OnLoadPresetCompleteListener() {
        MapPresetCollection.loadOnlineInBackground(new streetdirectory.mobile.gis.maps.configs.MapPresetCollection.OnLoadPresetCompleteListener() {

            public void onLoadPresetComplete(MapPresetCollection mappresetcollection)
            {
                SDBlackboard.preset = mappresetcollection.get(0);
                mMapView.setPreset(SDBlackboard.preset);
                if(pinClass == null) {
                    SDPreferences temp = SDPreferences.getInstance();
                    defaultLongitude = StringTools.tryParseDouble(temp.getStringForKey("last_map_longitude", "0"), 0.0D);
                    defaultLatitude = StringTools.tryParseDouble(temp.getStringForKey("last_map_latitude", "0"), 0.0D);
                    if(defaultLongitude == 0.0D || defaultLatitude == 0.0D)
                    {
                        if(SDApplication.firstTimeGps)
                        {
                            mMapView.startGps(true);
                            SDApplication.firstTimeGps = false;
                        }
                    } else
                    {
                        mMapView.goTo(defaultLongitude, defaultLatitude, 12);
                    }
                } else {
                    mMapView.goTo(defaultLongitude, defaultLatitude, 12);
                }

                mMapView.redraw();
                /*
                MapPresetCollection.loadOnlineInBackground(new streetdirectory.mobile.gis.maps.configs.MapPresetCollection.OnLoadPresetCompleteListener() {

                    public void onLoadPresetComplete(MapPresetCollection mappresetcollection)
                    {
                        SDBlackboard.preset = mappresetcollection.get(0);
                        mMapView.setPreset(SDBlackboard.preset);
                        mMapView.redraw();
                    }

                    public void onLoadPresetFailed()
                    {
                    }

                });
                */
                setCurrentCountryCode();

                return;
            }

            public void onLoadPresetFailed()
            {
            }

        });
    }

    private void initLayout()
    {
        mMapView = (MapView)findViewById(R.id.MapView);
        sdMapView = (SdMapFragment)getSupportFragmentManager().findFragmentById(R.id.SdMapView);
        mMenuButton = (ImageButton)findViewById(R.id.MenuButton);
        mSideMenuLayout = (SDMapSideMenuLayout)findViewById(R.id.side_menu);
        mSideMenuBlocker = findViewById(R.id.side_menu_blocker);
        mWebViewBBWorld = (WebView)findViewById(R.id.webviewBottomBannerWorld);
        mSdMobView = (RelativeLayout)findViewById(R.id.view_sdmob);
        mLayoutBlocking = (LinearLayout)findViewById(R.id.layout_blocking);
        mLayoutBlocking.setVisibility(View.INVISIBLE);
        pinLayer = new MapPinLayer(this);
        pinLayer.setVisibility(4);
        mOfferLayer = new OfferLayer(this);
        mBusLayer = new BusLayer(this) {

            protected void onBusIconClicked(BusLayerServiceOutput buslayerserviceoutput)
            {
                super.onBusIconClicked(buslayerserviceoutput);
                LocationBusinessServiceOutput temp = new LocationBusinessServiceOutput(buslayerserviceoutput.hashData);
                temp.populateData();
                Intent intent = new Intent(MapActivity.this, MapActivity.class);
                intent.putExtra("data", (Parcelable)temp);
                originalIntent = intent;
                pinLayer.longitude = ((LocationBusinessServiceOutput) (temp)).longitude;
                pinLayer.latitude = ((LocationBusinessServiceOutput) (temp)).latitude;
                pinLayer.setTitle(((LocationBusinessServiceOutput) (temp)).venue);
                pinClass = BusArrivalActivity.class;
                pinLayer.setVisibility(View.VISIBLE);
                pinLayer.showBalloon();
                pinLayer.setVisibility(View.VISIBLE);
                if(!pinLayer.isBalloonVisible())
                    pinLayer.showBalloon();
                mMapView.redraw();
            }
        };
        mBuildingVectorLayer = new BuildingVectorLayer(this) {

            protected void onBuildingVectorReceived(BuildingVectorServiceOutput buildingvectorserviceoutput)
            {
                super.onBuildingVectorReceived(buildingvectorserviceoutput);
                if(mBusLayer.isBusClicked)
                    return;

                Intent intent = new Intent(MapActivity.this, MapActivity.class);
                intent.putExtra("data", (Parcelable)buildingvectorserviceoutput);
                originalIntent = intent;
                if(mData == null || mData.v != null) {
                    if(mData != null)
                    {
                        pinLayer.setTitle(mData.v);
                        if(mData.cat == 1118)
                            pinClass = TrafficCameraLocationDetailActivity.class;
                        else
                        if(mData.cat == 93)
                            pinClass = BusArrivalActivity.class;
                        else
                        if(mData.cat == 29)
                            pinClass = ExpressWayExitActivity.class;
                        else
                        if(mData.cat == 28)
                            pinClass = ErpDetailActivity.class;
                        else
                            pinClass = BusinessInActivity.class;
                    }

                } else
                    pinLayer.setTitle("Information not available");

                pinLayer.setVisibility(View.VISIBLE);
                if(!pinLayer.isBalloonVisible())
                    pinLayer.showBalloon();
                mMapView.redraw();
                return;
            }

            protected void onMapLayerClicked(GeoPoint geopoint, Point point)
            {
                super.onMapLayerClicked(geopoint, point);
                if(mapView.getCurrentLevelOrdinal() > 10 && !mBusLayer.isBusClicked)
                {
                    longitude = geopoint.longitude;
                    latitude = geopoint.latitude;
                    downloadBuildingVector(longitude, latitude, SDBlackboard.currentCountryCode);
                    pinLayer.longitude = geopoint.longitude;
                    pinLayer.latitude = geopoint.latitude;
                    pinLayer.setTitle("Loading ...");
                    pinLayer.setVisibility(View.VISIBLE);
                    pinLayer.showBalloon();
                    mMapView.redraw();
                    return;
                } else
                {
                    mData = null;
                    return;
                }
            }

        };
        mMapView.addLayer(mOfferLayer);
//        mMapView.addLayer(mBusLayer);
//        mMapView.addLayer(mBuildingVectorLayer);
        mMapView.addLayer(pinLayer);
        bannerLayout = new BottomBannerLayout(this) {

            protected void onPopulateViewCompleted()
            {
                super.onPopulateViewCompleted();
                /*
                if(bannerLayout.getParent() != null)
                    ((MapView)bannerLayout.getParent()).removeView(bannerLayout);
                mMapView.addView(bannerLayout);
                android.widget.RelativeLayout.LayoutParams layoutparams1 = (android.widget.RelativeLayout.LayoutParams)bannerLayout.getLayoutParams();
                layoutparams1.addRule(12);
                bannerLayout.setLayoutParams(layoutparams1);
                */
            }

        };
        bannerLayout.setVisibility(View.INVISIBLE);
        mSearchListView = (ListView)mSideMenuLayout.findViewById(R.id.SearchListView);
        mMenuListView = (ListView)mSideMenuLayout.findViewById(R.id.MenuListView);
        mSearchField = (EditText)mSideMenuLayout.findViewById(R.id.MenuSearchField);
        mgr = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        android.view.ViewGroup.LayoutParams layoutparams = mWebViewBBWorld.getLayoutParams();
        bannerHeight = layoutparams.height;
        bannerWidth = layoutparams.width;
        initBlockingLayout();
    }

    private void initialize()
    {
        initLayout();
        initData();
        initEvent();
    }

    private void onApiVersionLoaded()
    {
        Object obj;
        boolean flag;
        boolean flag1;
        int i;
        flag1 = true;
        obj = SDPreferences.getInstance();
        i = ((SDPreferences) (obj)).getAppVersion();
        flag = flag1;

        if(i != 0) {
            flag = flag1;
            try {
                if (getPackageManager().getPackageInfo(getPackageName(), 0).versionCode >= i)
                    flag = true;
                mLayoutBlocking.setVisibility(View.VISIBLE);
                flag = false;
            } catch(Exception namenotfoundexception) {
                namenotfoundexception.printStackTrace();
                flag = flag1;
            }
        }

        obj = ((SDPreferences) (obj)).getNewVersionPopupMessage();
        if(((String) (obj)).length() > 0 && flag)
            showUpdateAvailableDialog(((String) (obj)));
        return;

    }

    private void openPlayStore()
    {
        String s = getPackageName();
        try
        {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse((new StringBuilder()).append("market://details?id=").append(s).toString())));
            return;
        }
        catch(Exception ex)
        {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse((new StringBuilder()).append("https://play.google.com/store/apps/details?id=").append(s).toString())));
        }
    }

    private void populateBanner()
    {
        //mCurrentSmallBanner = SmallBanner.getBannerFromSdMobUnit(this, SdMobHelper.getInstance(this).getSdMobUnit(SdMob.ad_bnr_main_map));
        //mCurrentSmallBanner.setAdMobSmallBannerListener(new streetdirectory.mobile.modules.sdmob.SmallBanner.AdMobSmallBannerListener() );
        //View view = mCurrentSmallBanner.getView(this);
        //mSdMobView.removeAllViews();
        //mSdMobView.addView(view, new ViewGroup.LayoutParams(-1, -1));
    }

    private void setCurrentCountryCode()
    {
        Iterator iterator = SDBlackboard.countryList.iterator();
        for(;iterator.hasNext();) {
            CountryListServiceOutput countrylistserviceoutput = (CountryListServiceOutput)iterator.next();
            if(MathTools.ptInPolygon(mMapView.center, countrylistserviceoutput.boundaries))
                SDBlackboard.currentCountryCode = countrylistserviceoutput.countryCode;
        }
    }

    private void showExitConfirmDialog()
    {
        (new AlertDialog.Builder(this)).setTitle("Confirmation").setMessage("Are you sure want to exit ?").setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialoginterface, int i)
            {
                finish();
            }

        }).setNegativeButton("Cancel", new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialoginterface, int i)
            {
                dialoginterface.cancel();
            }

        }).create().show();
    }

    private void showUpdateAvailableDialog(String s)
    {
        (new AlertDialog.Builder(this)).setTitle("Streetdirectory Notification").setMessage(s).setPositiveButton("Update Now", new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialoginterface, int i)
            {
                openPlayStore();
            }

        }).setNegativeButton("Later", null).show();
    }

    public void goTo(double d, double d1, int i)
    {
        mMapView.goTo(d, d1, i);
        mMapView.redraw();
    }

    protected void initEvent()
    {
        mWebViewBBWorld.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView webview, String s)
            {
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(s));
                startActivity(intent);
                return true;
            }

        });
        mSideMenuLayout.setOnSlideOpen(new Action() {

            public void execute()
            {
                mSideMenuBlocker.setVisibility(View.VISIBLE);
            }

        });
        mSideMenuLayout.setOnSlideClose(new Action() {

            public void execute()
            {
                mSideMenuBlocker.setVisibility(View.INVISIBLE);
            }
        });
        mSideMenuBlocker.setOnTouchListener(new android.view.View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionevent)
            {
                mSideMenuLayout.touchExecutor(motionevent);
                return true;
            }

        });
        mMenuButton.setOnTouchListener(new android.view.View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionevent)
            {
                mSideMenuLayout.touchExecutor(motionevent);
                return false;
            }

        });
        mMenuButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                if(mSideMenuLayout.getIsMenuOpen())
                {
                    mSideMenuLayout.slideClose();
                    return;
                } else
                {
                    mSideMenuLayout.slideOpen();
                    return;
                }
            }

        });
        pinLayer.setOnTapListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                if(pinClass != null && !pinLayer.title.equals("Information not available") && !pinLayer.title.equals("Loading ..."))
                {
                    originalIntent.setClass(MapActivity.this, pinClass);
                    startActivity(originalIntent);
                }
            }

        });
    }

    public void onBackPressed()
    {
        if(mSideMenuLayout.getIsMenuOpen())
        {
            if(mSearchListView.getVisibility() == 0)
            {
                mMenuListView.setVisibility(View.VISIBLE);
                mSearchListView.setVisibility(View.INVISIBLE);
                mgr.hideSoftInputFromWindow(mSearchField.getWindowToken(), 0);
                mSideMenuLayout.requestFocus();
                mSideMenuLayout.slideOpen(77);
                return;
            } else
            {
                Intent intent = new Intent(this, MapActivity.class);
                intent.setFlags(0x4000000);
                intent.putExtra("EXIT", true);
                startActivity(intent);
                finish();
                return;
            }
        } else
        {
            mSideMenuLayout.slideOpen();
            return;
        }
    }

    class MyException implements Thread.UncaughtExceptionHandler {
        public void uncaughtException(Thread t, Throwable e) {
            System.out.println("Uncaught Exception in thread '" + t.getName() + "'" + e);
            e.printStackTrace();
            System.exit(1);
        }
        public MyException() {
        }
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        Thread.setDefaultUncaughtExceptionHandler(new MyException());
        MainApplication.setApplicationContext(this.getApplicationContext());

        setContentView(R.layout.activity_map);
        if(getIntent().getBooleanExtra("EXIT", false))
            finish();
        SDStory.post(URLFactory.createGantOthersMap(), SDStory.createDefaultParams());
        LocalBroadcastManager.getInstance(this).registerReceiver(popUpMessageReceiver, new IntentFilter("pop_up_message"));
        initialize();
        LocalBroadcastManager.getInstance(this).registerReceiver(mSdMobReceiver, new IntentFilter("sdmob_broadcast"));
        populateBanner();
        onApiVersionLoaded();
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        if(mSideMenuLayout.getIsMenuOpen())
            mSideMenuLayout.slideClose();
        else
            mSideMenuLayout.slideOpen();
        return false;
    }

    protected void onDestroy()
    {
        if(_nearbyService != null)
        {
            _nearbyService.abort();
            _nearbyService = null;
        }
        LocalBroadcastManager.getInstance(this).unregisterReceiver(popUpMessageReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mSdMobReceiver);
        if(mCurrentSmallBanner instanceof FacebookSmallBanner)
            ((FacebookSmallBanner)mCurrentSmallBanner).destroyAdView();
        super.onDestroy();
    }

    public static final int FINGER_DRAGGING = 2;
    public static final int FINGER_RELEASED = 0;
    public static final int FINGER_TOUCHED = 1;
    public static final int FINGER_UNDEFINED = 3;
    private static final String PIN_INFO_NOT_AVAILABLE = "Information not available";
    private static final String PIN_LOADING = "Loading ...";
    private NearbyService _nearbyService;
    private int adRequestRetryCount;
    private int bannerHeight;
    protected BottomBannerLayout bannerLayout;
    private int bannerWidth;
    double defaultLatitude;
    double defaultLongitude;
    private BuildingVectorLayer mBuildingVectorLayer;
    public BusLayer mBusLayer;
    private SmallBanner mCurrentSmallBanner;
    private LinearLayout mLayoutBlocking;
    private MapView mMapView;
    private ImageButton mMenuButton;
    private ListView mMenuListView;
    public OfferLayer mOfferLayer;
    private BroadcastReceiver mSdMobReceiver;
    private RelativeLayout mSdMobView;
    private EditText mSearchField;
    private ListView mSearchListView;
    private View mSideMenuBlocker;
    private SDMapSideMenuLayout mSideMenuLayout;
    private WebView mWebViewBBWorld;
    private InputMethodManager mgr;
    public Intent originalIntent;
    public Class pinClass;
    public MapPinLayer pinLayer;
    private BroadcastReceiver popUpMessageReceiver;
    private SdMapFragment sdMapView;

/*
    static int access$702(MapActivity mapactivity, int i)
    {
        mapactivity.adRequestRetryCount = i;
        return i;
    }

*/


/*
    static int access$708(MapActivity mapactivity)
    {
        int i = mapactivity.adRequestRetryCount;
        mapactivity.adRequestRetryCount = i + 1;
        return i;
    }

*/
}
