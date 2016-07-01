

package streetdirectory.mobile.modules.mappreview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.core.SDStory;
import streetdirectory.mobile.gis.maps.MapPinLayer;
import streetdirectory.mobile.gis.maps.MapView;
import streetdirectory.mobile.gis.maps.configs.MapPresetCollection;
import streetdirectory.mobile.modules.SDActivity;
import streetdirectory.mobile.modules.sdmob.SdMobHelper;
import streetdirectory.mobile.modules.sdmob.SmallBanner;
import streetdirectory.mobile.sd.SDBlackboard;
import streetdirectory.mobile.sd.SdMob;
import streetdirectory.mobile.service.URLFactory;

public class MapPreviewActivity extends SDActivity
{

    public MapPreviewActivity()
    {
        adRequestRetryCount = 0;
    }

    private void initData()
    {
        Intent intent = getIntent();
        final double longitude = intent.getDoubleExtra("longitude", 103.80729837373376D);
        final double latitude = intent.getDoubleExtra("latitude", 1.2894823898199825D);
        String s = intent.getStringExtra("pinTitle");
        pinLayer.longitude = intent.getDoubleExtra("pinLongitude", 0.0D);
        pinLayer.latitude = intent.getDoubleExtra("pinLatitude", 0.0D);
        final double pinLongitude = pinLayer.longitude;
        final double pinLatitude = pinLayer.latitude;
        boolean flag;
        boolean flag1;
        if(pinLayer.longitude != 0.0D)
            flag = true;
        else
            flag = false;
        if(pinLayer.latitude != 0.0D)
            flag1 = true;
        else
            flag1 = false;
        if(flag & flag1)
        {
            pinLayer.setTitle(s);
            SDStory.post(URLFactory.createGantBuildingDirectoryMap(s), SDStory.createDefaultParams());
            pinLayer.setVisibility(View.VISIBLE);
        }


        MapPresetCollection.loadOfflineInBackground(new streetdirectory.mobile.gis.maps.configs.MapPresetCollection.OnLoadPresetCompleteListener() {

            public void onLoadPresetComplete(MapPresetCollection mappresetcollection)
            {
                if(mMapView != null)
                {
                    SDBlackboard.preset = mappresetcollection.get(0);
                    mMapView.setPreset(SDBlackboard.preset);
                    if(pinLongitude == 0.0D || pinLatitude == 0.0D)
                        mMapView.goTo(longitude, latitude, 12);
                    else
                        mMapView.goTo(pinLongitude, pinLatitude, 12);
                    mMapView.redraw();
                }
            }

            public void onLoadPresetFailed()
            {
            }

        });
    }

    private void initEvent()
    {
        mButtonBack.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                finish();
            }

        });
    }

    private void initLayout()
    {
        mButtonBack = (Button)findViewById(R.id.button_back);
        mMapView = (MapView)findViewById(R.id.view_map);
        mSdSmallBanner = (RelativeLayout)findViewById(R.id.view_sdmob);
        pinLayer = new MapPinLayer(this);
        mMapView.addLayer(pinLayer);
    }

    private void initialize()
    {
        initLayout();
        initData();
        initEvent();
    }

    private void loadSmallBanner()
    {
        Object obj = SmallBanner.getBannerFromSdMobUnit(this, SdMobHelper.getInstance(this).getSdMobUnit(SdMob.ad_bnr_map_secondary));
        ((SmallBanner) (obj)).setAdMobSmallBannerListener(new streetdirectory.mobile.modules.sdmob.SmallBanner.AdMobSmallBannerListener() {

            public void onFailed() {
                if (adRequestRetryCount < 5)
                    loadSmallBanner();
                //int i =
            }

            public void onSuccess()
            {
                adRequestRetryCount = 0;
            }

        });

        obj = ((SmallBanner) (obj)).getView(this);
        mSdSmallBanner.removeAllViews();
        mSdSmallBanner.addView(((View) (obj)), new ViewGroup.LayoutParams(-1, -1));
    }

    protected void onCreate(Bundle bundle)
    {
        onCreate(bundle);
        setContentView(R.layout.activity_map_preview);
        initialize();
        loadSmallBanner();
    }

    private int adRequestRetryCount;
    private Button mButtonBack;
    private MapView mMapView;
    private RelativeLayout mSdSmallBanner;
    public MapPinLayer pinLayer;




/*
    static int access$102(MapPreviewActivity mappreviewactivity, int i)
    {
        mappreviewactivity.adRequestRetryCount = i;
        return i;
    }

*/


/*
    static int access$108(MapPreviewActivity mappreviewactivity)
    {
        int i = mappreviewactivity.adRequestRetryCount;
        mappreviewactivity.adRequestRetryCount = i + 1;
        return i;
    }

*/

}
