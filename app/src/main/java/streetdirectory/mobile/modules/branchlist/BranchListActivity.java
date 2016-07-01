// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.branchlist;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.util.ArrayList;
import java.util.Iterator;
import streetdirectory.mobile.core.SDLogger;
import streetdirectory.mobile.core.ui.UIHelper;
import streetdirectory.mobile.gis.maps.MapView;
import streetdirectory.mobile.gis.maps.configs.MapPresetCollection;
import streetdirectory.mobile.modules.SDActivity;
import streetdirectory.mobile.modules.businessdetail.BusinessDetailActivityV2;
import streetdirectory.mobile.modules.businessdetail.service.*;
import streetdirectory.mobile.modules.core.LocationBusinessServiceOutput;
import streetdirectory.mobile.modules.map.layers.BranchLayer;
import streetdirectory.mobile.modules.sdmob.SdMobHelper;
import streetdirectory.mobile.modules.sdmob.SmallBanner;
import streetdirectory.mobile.sd.SDBlackboard;
import streetdirectory.mobile.sd.SdMob;
import streetdirectory.mobile.service.SDHttpServiceOutput;

// Referenced classes of package streetdirectory.mobile.modules.branchlist:
//            BranchListAdapter

public class BranchListActivity extends SDActivity
{

    public BranchListActivity()
    {
        adRequestRetryCount = 0;
    }

    private void abortDownloadBusinessBranch()
    {
        if(mBusinessBranchService != null)
        {
            mBusinessBranchService.abort();
            mBusinessBranchService = null;
        }
    }

    private void downloadBusinessBranch()
    {
        abortDownloadBusinessBranch();
        mBusinessBranchService = new BusinessBranchService(new BusinessBranchServiceInput(mCountryCode, mCompanyID, mAdapter.getDataCount(), 1000, mPromoId)) {

            public void onAborted(Exception exception)
            {
                SDLogger.info("Business Branch Aborted");
                mIndicatorLoading.setVisibility(View.INVISIBLE);
            }

            public void onFailed(Exception exception)
            {
                SDLogger.printStackTrace(exception, "Business Branch Failed");
                mBusinessBranchService = null;
                mIndicatorLoading.setVisibility(View.INVISIBLE);
            }

            public void onSuccess(Object obj)
            {
                onSuccess((SDHttpServiceOutput)obj);
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                mBusinessBranchService = null;
                mIndicatorLoading.setVisibility(View.INVISIBLE);
                if(sdhttpserviceoutput.childs.size() > 0)
                {
                    mAdapter.appendData(sdhttpserviceoutput);
                    mAdapter.notifyDataSetChanged();
                    mNearbyAdapter.appendData(sdhttpserviceoutput);
                    mNearbyAdapter.sortDataByDistance();
                    mNearbyAdapter.notifyDataSetChanged();
                }
                mBranchLayer.populateBranchesPin(sdhttpserviceoutput.childs);
                setCenterAndScaleMap(sdhttpserviceoutput.childs);
            }

        };
        mIndicatorLoading.setVisibility(View.VISIBLE);
        mBusinessBranchService.executeAsync();
    }

    private void initData()
    {
        Intent intent = getIntent();
        mCountryCode = intent.getStringExtra("countryCode");
        mCompanyID = intent.getIntExtra("companyID", 0);
        mPromoId = intent.getStringExtra("offerID");
        setSelectedButton(mAllButton);
    }

    private void initEvent()
    {
        mAllButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                setSelectedButton(mAllButton);
                mListViewBranch.setAdapter(mAdapter);
                mMapView.setVisibility(View.INVISIBLE);
            }

        });

        mNearbyButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                setSelectedButton(mNearbyButton);
                mListViewBranch.setAdapter(mNearbyAdapter);
                mMapView.setVisibility(View.INVISIBLE);
            }

        });
        mMapButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                mMapView.setVisibility(View.VISIBLE);
            }
        });
        mButtonBack.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                finish();
            }

        });
        mButtonShare.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
            }

        });
        mAdapter.setOnLoadMoreListener(new BranchListAdapter.OnLoadMoreListener() {

            public void onLoadMoreList()
            {
                downloadBusinessBranch();
            }

        });
        mAdapter.setOnBranchItemClickedListener(new BranchListAdapter.OnBranchItemClickedListener() {

            public void onAddNewBranchClicked()
            {
            }

            public void onBranchItemClicked(BusinessBranchServiceOutput businessbranchserviceoutput, int i)
            {
                LocationBusinessServiceOutput locationbusinessserviceoutput = new LocationBusinessServiceOutput(businessbranchserviceoutput.hashData);
                Intent intent = new Intent(BranchListActivity.this, BusinessDetailActivityV2.class);
                intent.putExtra("data", (Parcelable)locationbusinessserviceoutput);
                intent.putExtra("branch_name", businessbranchserviceoutput.branchName);
                startActivity(intent);
            }

        });
    }

    private void initLayout()
    {
        mButtonBack = (Button)findViewById(R.id.button_back);
        mListViewBranch = (ListView)findViewById(R.id.listview_branch);
        mAllButton = (Button)findViewById(R.id.AllButton);
        mNearbyButton = (Button)findViewById(R.id.NearbyButton);
        mMapButton = (Button)findViewById(R.id.MapButton);
        mAdapter = new BranchListAdapter(this);
        mNearbyAdapter = new BranchListAdapter(this);
        mNearbyAdapter.isNearbyData = true;
        mListViewBranch.setAdapter(mAdapter);
        mIndicatorLoading = (ProgressBar)findViewById(R.id.indicator_loading);
        mButtonShare = (Button)findViewById(R.id.button_share);
        mSdSmallBanner = (RelativeLayout)findViewById(R.id.view_sdmob);
        mMapView = (MapView)findViewById(R.id.MapView);
        mBranchLayer = new BranchLayer(this);
        mBranchLayer.setOnTapListener(new streetdirectory.mobile.modules.map.layers.BranchLayer.OnBranchPinClickListener() {

            public void onClicked(BusinessBranchServiceOutput businessbranchserviceoutput)
            {
                Intent intent = new Intent(BranchListActivity.this, BusinessDetailActivityV2.class);
                intent.putExtra("data", intent);
                startActivity(intent);
            }

        });
        mMapView.addLayer(mBranchLayer);
        if(SDBlackboard.preset != null)
        {
            mMapView.setPreset(SDBlackboard.preset);
            mMapView.redraw();
            return;
        } else
        {
            SDBlackboard.reloadMapPreset(new streetdirectory.mobile.sd.SDBlackboard.LoadMapPresetCompleteListener() {

                protected void onComplete(MapPresetCollection mappresetcollection)
                {
                    mMapView.setPreset(SDBlackboard.preset);
                    mMapView.redraw();
                }
            });
            return;
        }
    }

    private void initialize()
    {
        initLayout();
        initData();
        initEvent();
    }

    private void loadSmallBanner()
    {
        Object obj = SmallBanner.getBannerFromSdMobUnit(this, SdMobHelper.getInstance(this).getSdMobUnit(SdMob.ad_bnr_business_branch));
//        ((SmallBanner) (obj)).setAdMobSmallBannerListener(new streetdirectory.mobile.modules.sdmob.SmallBanner.AdMobSmallBannerListener() );
        obj = ((SmallBanner) (obj)).getView(this);
        mSdSmallBanner.removeAllViews();
        mSdSmallBanner.addView(((View) (obj)), new RadioGroup.LayoutParams(-1, -1));
    }

    private void setCenterAndScaleMap(ArrayList arraylist)
    {
        double d3 = -1.7976931348623157E+308D;
        double d4 = 1.7976931348623157E+308D;
        double d = -1.7976931348623157E+308D;
        double d1 = 1.7976931348623157E+308D;
        Iterator iterator = arraylist.iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            BusinessBranchServiceOutput businessbranchserviceoutput = (BusinessBranchServiceOutput)iterator.next();
            if(businessbranchserviceoutput.longitude != 0.0D && businessbranchserviceoutput.latitude != 0.0D)
            {
                double d2 = d4;
                if(businessbranchserviceoutput.longitude < d4)
                    d2 = businessbranchserviceoutput.longitude;
                double d5 = d3;
                if(businessbranchserviceoutput.longitude > d3)
                    d5 = businessbranchserviceoutput.longitude;
                double d6 = d1;
                if(businessbranchserviceoutput.latitude < d1)
                    d6 = businessbranchserviceoutput.latitude;
                d3 = d5;
                d4 = d2;
                d1 = d6;
                if(businessbranchserviceoutput.latitude > d)
                {
                    d = businessbranchserviceoutput.latitude;
                    d3 = d5;
                    d4 = d2;
                    d1 = d6;
                }
            }
        } while(true);
        mCenterLongitude = (d3 - d4) * 0.5D + d4;
        mCenterLatitude = (d - d1) * 0.5D + d1;
        Location location = new Location("");
        location.setLatitude(d1);
        location.setLongitude(d4);
        Location location2 = new Location("");
        location2.setLatitude(d);
        location2.setLongitude(d3);
        d = location2.distanceTo(location) / (float)(UIHelper.getScreenDimension().widthPixels - 50);
        mMapView.goTo(mCenterLongitude, mCenterLatitude, d);
        mMapView.redraw();
    }

    private void setSelectedButton(Button button)
    {
        mAllButton.setSelected(false);
        mNearbyButton.setSelected(false);
        button.setSelected(true);
    }

    protected void onCreate(Bundle bundle)
    {
        onCreate(bundle);
        setContentView(R.layout.activity_branch_list);
        initialize();
        downloadBusinessBranch();
        loadSmallBanner();
    }

    private int adRequestRetryCount;
    private BranchListAdapter mAdapter;
    private Button mAllButton;
    private BranchLayer mBranchLayer;
    private BusinessBranchService mBusinessBranchService;
    private Button mButtonBack;
    private Button mButtonShare;
    private double mCenterLatitude;
    private double mCenterLongitude;
    private int mCompanyID;
    private String mCountryCode;
    private ProgressBar mIndicatorLoading;
    private ListView mListViewBranch;
    private Button mMapButton;
    private MapView mMapView;
    private BranchListAdapter mNearbyAdapter;
    private Button mNearbyButton;
    private String mPromoId;
    private RelativeLayout mSdSmallBanner;







/*
    static int access$1202(BranchListActivity branchlistactivity, int i)
    {
        branchlistactivity.adRequestRetryCount = i;
        return i;
    }

*/


/*
    static int access$1208(BranchListActivity branchlistactivity)
    {
        int i = branchlistactivity.adRequestRetryCount;
        branchlistactivity.adRequestRetryCount = i + 1;
        return i;
    }

*/









/*
    static BusinessBranchService access$802(BranchListActivity branchlistactivity, BusinessBranchService businessbranchservice)
    {
        branchlistactivity.mBusinessBranchService = businessbranchservice;
        return businessbranchservice;
    }

*/

}
