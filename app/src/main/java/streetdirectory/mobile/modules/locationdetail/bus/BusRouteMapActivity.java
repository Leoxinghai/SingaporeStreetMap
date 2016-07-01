// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.locationdetail.bus;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.*;
import com.google.gson.Gson;
import com.xinghai.mycurve.R;

import java.util.ArrayList;
import java.util.Iterator;
import streetdirectory.mobile.core.MathTools;
import streetdirectory.mobile.core.PointF64;
import streetdirectory.mobile.core.ui.SanListViewAdapter;
import streetdirectory.mobile.gis.GeoPoint;
import streetdirectory.mobile.gis.maps.MapView;
import streetdirectory.mobile.gis.maps.configs.MapPresetCollection;
import streetdirectory.mobile.modules.SDActivity;
import streetdirectory.mobile.modules.direction.JourneyPointDetail;
import streetdirectory.mobile.modules.locationdetail.bus.service.BusArrivalService;
import streetdirectory.mobile.modules.locationdetail.bus.service.BusArrivalServiceInput;
import streetdirectory.mobile.modules.locationdetail.bus.service.BusArrivalServiceOutput;
import streetdirectory.mobile.modules.locationdetail.bus.service.BusRouteAlternate;
import streetdirectory.mobile.modules.locationdetail.bus.service.BusRouteSummary;
import streetdirectory.mobile.modules.locationdetail.bus.service.BusRoutesServiceOutput;
import streetdirectory.mobile.modules.sdmob.SdMobHelper;
import streetdirectory.mobile.modules.sdmob.SmallBanner;
import streetdirectory.mobile.sd.SDBlackboard;
import streetdirectory.mobile.sd.SdMob;
import streetdirectory.mobile.service.SDHttpServiceOutput;

// Referenced classes of package streetdirectory.mobile.modules.locationdetail.bus:
//            BusRouteMapLayer, BusRouteAlternateCell

public class BusRouteMapActivity extends SDActivity
{

    public BusRouteMapActivity()
    {
        adRequestRetryCount = 0;
        mJpdTouchBuffer = 32;
        mStepIndex = 0;
        mPixel = new PointF64();
        mJpdSize = 0;
        mTotalSize = 0;
        mEndMargin = 0;
        alternateRouteData = new ArrayList();
        mHandler = new Handler();
    }

    private void abortCurrentService()
    {
        if(mService != null)
        {
            mService.abort();
            mService = null;
            if(mRunnable != null)
                mHandler.removeCallbacks(mRunnable);
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    private void downloadBusStopInfo(final JourneyPointDetail journeypointdetail)
    {
        abortCurrentService();
        //mService = new BusArrivalService(journeypointdetail) {
        mService = new BusArrivalService(null) {
            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                String temp;
                if(sdhttpserviceoutput.childs.size() > 0)
                {
                    Object obj = (BusArrivalServiceOutput)sdhttpserviceoutput.childs.get(0);
                    if(((BusArrivalServiceOutput) (obj)).subsequentBus < 0)
                        temp = "N/A";
                    else
                    if(((BusArrivalServiceOutput) (obj)).subsequentBus == 0)
                        temp = "Arrived";
                    else
                        temp = (new StringBuilder()).append(((BusArrivalServiceOutput) (obj)).subsequentBus).append(" min").toString();
                    if(((BusArrivalServiceOutput) (obj)).nextBus == 0)
                        obj = "Arrived";
                    else
                    if(((BusArrivalServiceOutput) (obj)).nextBus < 0)
                        obj = "N/A";
                    else
                        obj = (new StringBuilder()).append(((BusArrivalServiceOutput) (obj)).nextBus).append(" min").toString();
                    mtextViewDesc.setText((new StringBuilder()).append(journeypointdetail.title).append(". ").append(journeypointdetail.desc).append("\nNext Bus : ").append(((String) (obj))).append("\nSubsequent Bus : ").append(sdhttpserviceoutput).toString());
                    downloadTimeDelayed(journeypointdetail);
                }
            }

        };
        mService.executeAsync();
    }

    private void downloadTimeDelayed(final JourneyPointDetail item)
    {
        mRunnable = new Runnable() {

            public void run()
            {
                downloadBusStopInfo(item);
            }

        };
        mHandler.postDelayed(mRunnable, 20000L);
    }

    private String getBusStopId(String s)
    {
        String s2 = "";
        String s1 = s2;
        if(s != null)
        {
            s1 = s2;
            if(s.startsWith("B"))
                s1 = s.substring(1);
        }
        return s1;
    }

    private void initData()
    {
        Intent intent = getIntent();
        String s = intent.getStringExtra("data");
        Gson gson = new Gson();
        mBusNumber = intent.getStringExtra("busNumber");
        mLongitude = intent.getDoubleExtra("longitude", 0.0D);
        mLatitude = intent.getDoubleExtra("latitude", 0.0D);
        mSelectedRouteIndex = intent.getIntExtra("selectedRouteIndex", 0);
        mStepIndex = intent.getIntExtra("stepIndex", 0);
        mData = (BusRoutesServiceOutput)gson.fromJson(s, BusRoutesServiceOutput.class);
        mData.populateData();
        mStartLongitude = intent.getDoubleExtra("longitude", 0.0D);
        mStartLatitude = intent.getDoubleExtra("latitude", 0.0D);
        mTextViewTitle.setText((new StringBuilder()).append("BUS ").append(mBusNumber).toString());
        alternateCellAdapter = new SanListViewAdapter(this, 0, alternateRouteData);
        mListViewAlternativeRoutes.setAdapter(alternateCellAdapter);
        start = (JourneyPointDetail)((ArrayList)mData.arrayOfArrayOfPointDetail.get(mSelectedRouteIndex)).get(0);
        stop = (JourneyPointDetail)((ArrayList)mData.arrayOfArrayOfPointDetail.get(mSelectedRouteIndex)).get(((ArrayList)mData.arrayOfArrayOfPointDetail.get(mSelectedRouteIndex)).size() - 1);
    }

    private void initEvent()
    {
        mapLayer = new BusRouteMapLayer(this);
        mapLayer.mData = mData;
        mapLayer.focusLongitude = mLongitude;
        mapLayer.focusLatitude = mLatitude;
        mapLayer.selectedRouteIndex = mSelectedRouteIndex;
        mMapView.addLayer(mapLayer);
        if(SDBlackboard.preset != null)
        {
            mMapView.setPreset(SDBlackboard.preset);
            mMapView.goTo(mStartLongitude, mStartLatitude, 12);
            mMapView.redraw();
        } else
        {
            SDBlackboard.reloadMapPreset(new streetdirectory.mobile.sd.SDBlackboard.LoadMapPresetCompleteListener() {

                protected void onComplete(MapPresetCollection mappresetcollection)
                {
                    mMapView.setPreset(SDBlackboard.preset);
                    mMapView.goTo(mStartLongitude, mStartLatitude, 12);
                    mMapView.redraw();
                }

            });
        }
        mImageViewHideNavBar.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                int i = mImageViewHideNavBar.getHeight();
                RelativeLayout.LayoutParams layoutParams = (android.widget.RelativeLayout.LayoutParams)mLayoutTop.getLayoutParams();
                i = mLayoutTop.getHeight() - i;
                TranslateAnimation translateAnimation;
                if(((android.widget.RelativeLayout.LayoutParams) (layoutParams)).topMargin < 0)
                {
                    translateAnimation = new TranslateAnimation(0.0F, 0.0F, 0.0F, i);
                    mEndMargin = 0;
                    mImageViewHideNavBar.setImageResource(R.drawable.button_hide_navbar);
                } else
                {
                    translateAnimation = new TranslateAnimation(0.0F, 0.0F, 0.0F, -i);
                    mEndMargin = -i;
                    mImageViewHideNavBar.setImageResource(R.drawable.button_show_navbar);
                }
                translateAnimation.setDuration(200L);
                translateAnimation.setFillAfter(false);
                translateAnimation.setAnimationListener(new android.view.animation.Animation.AnimationListener() {

                    public void onAnimationEnd(Animation animation) {
                        mLayoutTop.clearAnimation();
                        RelativeLayout.LayoutParams layoutParams1 = (android.widget.RelativeLayout.LayoutParams) mLayoutTop.getLayoutParams();
                        layoutParams1.setMargins(0, mEndMargin, 0, 0);
                        mLayoutTop.setLayoutParams(layoutParams1);
                    }

                    public void onAnimationRepeat(Animation animation) {
                    }

                    public void onAnimationStart(Animation animation) {
                    }

                });
                mLayoutTop.startAnimation(translateAnimation);
            }

        });
        mButtonBack.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                JourneyPointDetail journeyPointDetail = (JourneyPointDetail)((ArrayList)mData.arrayOfArrayOfPointDetail.get(mSelectedRouteIndex)).get(mStepIndex);
                Intent intent = getIntent();
                intent.putExtra("bus_stop_id", ((JourneyPointDetail) (journeyPointDetail)).title);
                intent.putExtra("bus_alternate_route_index", mSelectedRouteIndex);
                setResult(-1, intent);
                finish();
            }

        });
        //mimageViewNext.setOnClickListener(new android.view.View.OnClickListener() );
        //mimageViewPrev.setOnClickListener(new android.view.View.OnClickListener() );
        mMapView.setOnMapClicked(new streetdirectory.mobile.gis.maps.MapView.OnMapClickedListener() {

            public void onMapClicked(MapView mapview, GeoPoint geopoint, Point point)
            {
                if(start != null && tapHandler(point, start.longitude, start.latitude))
                {
                    mStepIndex = 0;
                    populateTopLayout();
                    return;
                }
                if(stop != null && tapHandler(point, stop.longitude, stop.latitude))
                {
                    mStepIndex = mJpdSize;
                    populateTopLayout();
                    return;
                }
                int i = 0;
                for(Iterator iterator = ((ArrayList)mData.arrayOfArrayOfPointDetail.get(mSelectedRouteIndex)).iterator(); iterator.hasNext();)
                {
                    JourneyPointDetail journeyPointDetail = (JourneyPointDetail)iterator.next();
                    if(tapHandler(point, ((JourneyPointDetail) (journeyPointDetail)).longitude, ((JourneyPointDetail) (journeyPointDetail)).latitude))
                    {
                        mStepIndex = i;
                        populateTopLayout();
                        return;
                    }
                    i++;
                }

            }

        });
        mButtonMoreRoutes.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                if(mLayoutAlternativeRoutes.getVisibility() != 0)
                {
                    mLayoutAlternativeRoutes.setVisibility(View.VISIBLE);
                    return;
                } else
                {
                    mLayoutAlternativeRoutes.setVisibility(View.INVISIBLE);
                    return;
                }
            }

        });
        mListViewAlternativeRoutes.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView adapterview, View view, int i, long l)
            {
                mLayoutAlternativeRoutes.setVisibility(View.INVISIBLE);
                mSelectedRouteIndex = i;
                mapLayer.selectedRouteIndex = mSelectedRouteIndex;
                mStepIndex = 0;
                start = (JourneyPointDetail)((ArrayList)mData.arrayOfArrayOfPointDetail.get(mSelectedRouteIndex)).get(0);
                stop = (JourneyPointDetail)((ArrayList)mData.arrayOfArrayOfPointDetail.get(mSelectedRouteIndex)).get(((ArrayList)mData.arrayOfArrayOfPointDetail.get(mSelectedRouteIndex)).size() - 1);
                populateData();
            }

        });
    }

    private void initLayout()
    {
        mMapView = (MapView)findViewById(R.id.MapView);
        mButtonBack = (Button)findViewById(R.id.button_back);
        mButtonMoreRoutes = (Button)findViewById(R.id.button_more_routes);
        mLayoutTop = (RelativeLayout)findViewById(R.id.layout_top);
        mimageViewPrev = (ImageView)findViewById(R.id.imageViewPrev);
        mimageViewNext = (ImageView)findViewById(R.id.imageViewNext);
        mtextViewDesc = (TextView)findViewById(R.id.textViewDesc);
        mImageViewHideNavBar = (ImageView)findViewById(R.id.ImageViewHideNavBar);
        mLayoutAlternativeRoutes = (LinearLayout)findViewById(R.id.layout_alternative_routes);
        mListViewAlternativeRoutes = (ListView)findViewById(R.id.list_view_alternative_routes);
        mTextViewTitle = (TextView)findViewById(R.id.textview_title);
        mSdSmallBanner = (RelativeLayout)findViewById(R.id.view_sdmob);
        mTextViewTitle.setText("BUS");
    }

    private void initialize()
    {
        initLayout();
        initData();
        initEvent();
        populateData();
    }

    private void loadSmallBanner()
    {
        Object obj = SmallBanner.getBannerFromSdMobUnit(this, SdMobHelper.getInstance(this).getSdMobUnit(SdMob.ad_bnr_direction_map));
        ((SmallBanner) (obj)).setAdMobSmallBannerListener(new streetdirectory.mobile.modules.sdmob.SmallBanner.AdMobSmallBannerListener() {

            public void onFailed()
            {
                if(adRequestRetryCount < 5)
                    loadSmallBanner();
                BusRouteMapActivity busroutemapactivity = BusRouteMapActivity.this;
                busroutemapactivity.adRequestRetryCount = busroutemapactivity.adRequestRetryCount + 1;
            }

            public void onSuccess()
            {
                adRequestRetryCount = 0;
            }

        });
        obj = ((SmallBanner) (obj)).getView(this);
        mSdSmallBanner.removeAllViews();
        mSdSmallBanner.addView(((View) (obj)), new android.widget.RelativeLayout.LayoutParams(-1, -1));
    }

    private void populateData()
    {
        if(mData.arrayOfRoutes.size() > 1)
        {
            mButtonMoreRoutes.setVisibility(View.VISIBLE);
            mButtonMoreRoutes.setText((new StringBuilder()).append("Routes (").append(mData.arrayOfRoutes.size()).append(")").toString());
            populateAlternate();
        } else
        {
            mButtonMoreRoutes.setVisibility(View.INVISIBLE);
        }
        mJpdSize = ((ArrayList)mData.arrayOfArrayOfPointDetail.get(mSelectedRouteIndex)).size();
        mTotalSize = mJpdSize;
        populateTopLayout();
    }

    private void populateTopLayout()
    {
        if(mStepIndex == 0)
        {
            mtextViewDesc.setText((new StringBuilder()).append(start.title).append(" ").append(start.desc).toString());
            mapLayer.focusLongitude = start.longitude;
            mapLayer.focusLatitude = start.latitude;
            mimageViewPrev.setVisibility(4);
            mimageViewNext.setVisibility(View.VISIBLE);
        } else
        if(mStepIndex < mJpdSize - 1)
        {
            JourneyPointDetail journeypointdetail = (JourneyPointDetail)((ArrayList)mData.arrayOfArrayOfPointDetail.get(mSelectedRouteIndex)).get(mStepIndex);
            mtextViewDesc.setText((new StringBuilder()).append(journeypointdetail.title).append(" ").append(journeypointdetail.desc).toString());
            mapLayer.focusLongitude = journeypointdetail.longitude;
            mapLayer.focusLatitude = journeypointdetail.latitude;
            mimageViewPrev.setVisibility(View.VISIBLE);
            mimageViewNext.setVisibility(View.VISIBLE);
        } else
        if(mStepIndex == mJpdSize - 1)
        {
            mtextViewDesc.setText((new StringBuilder()).append(stop.title).append(" ").append(stop.desc).toString());
            mapLayer.focusLongitude = stop.longitude;
            mapLayer.focusLatitude = stop.latitude;
            mimageViewPrev.setVisibility(View.VISIBLE);
            mimageViewNext.setVisibility(4);
        }
        if(mMapView.getPreset() != null)
            mMapView.goToAnimate(mapLayer.focusLongitude, mapLayer.focusLatitude, mMapView.getCurrentLevelOrdinal());
        else
            mMapView.center = new GeoPoint(mapLayer.focusLongitude, mapLayer.focusLatitude);
        mMapView.redraw();
    }

    private boolean tapHandler(Point point, double d, double d1)
    {
        mPixel.x = d;
        mPixel.y = d1;
        mPixel = mMapView.geoToPixelX(mPixel);
        return MathTools.computeDistance(mPixel.x, mPixel.y, point.x, point.y) < (double)mJpdTouchBuffer;
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_direction_map);
        initialize();
        loadSmallBanner();
    }

    protected void onDestroy()
    {
        super.onDestroy();
        abortCurrentService();
    }

    protected void populateAlternate()
    {
        alternateCellAdapter.clear();
        int i = 0;
        while(i < mData.arrayOfSummary.size())
        {
            Object obj = new BusRouteSummary(((BusRouteSummary)mData.arrayOfSummary.get(i)).hashData);
            ((BusRouteSummary) (obj)).populateData();
            BusRouteAlternate busroutealternate = new BusRouteAlternate();
            busroutealternate.title = ((BusRouteSummary) (obj)).title;
            busroutealternate.direction = ((BusRouteSummary) (obj)).direction;
            busroutealternate.start = ((BusRouteSummary) (obj)).start;
            busroutealternate.end = ((BusRouteSummary) (obj)).end;
            BusRouteAlternateCell temp = new BusRouteAlternateCell(busroutealternate);
            if(i == mSelectedRouteIndex)
                temp.routeChecked = true;
            else
                temp.routeChecked = false;
            alternateRouteData.add(obj);
            i++;
        }
        alternateCellAdapter.notifyDataSetChanged();
    }

    int adRequestRetryCount;
    private SanListViewAdapter alternateCellAdapter;
    private ArrayList alternateRouteData;
    private String mBusNumber;
    private Button mButtonBack;
    private Button mButtonMoreRoutes;
    private BusRoutesServiceOutput mData;
    private int mEndMargin;
    private Handler mHandler;
    private ImageView mImageViewHideNavBar;
    private int mJpdSize;
    private int mJpdTouchBuffer;
    private double mLatitude;
    private LinearLayout mLayoutAlternativeRoutes;
    private RelativeLayout mLayoutTop;
    private ListView mListViewAlternativeRoutes;
    private double mLongitude;
    private MapView mMapView;
    private PointF64 mPixel;
    private Runnable mRunnable;
    private RelativeLayout mSdSmallBanner;
    private int mSelectedRouteIndex;
    private BusArrivalService mService;
    private double mStartLatitude;
    private double mStartLongitude;
    private int mStepIndex;
    private TextView mTextViewTitle;
    private int mTotalSize;
    private BusRouteMapLayer mapLayer;
    private ImageView mimageViewNext;
    private ImageView mimageViewPrev;
    private TextView mtextViewDesc;
    private JourneyPointDetail start;
    private JourneyPointDetail stop;






/*
    static JourneyPointDetail access$1102(BusRouteMapActivity busroutemapactivity, JourneyPointDetail journeypointdetail)
    {
        busroutemapactivity.start = journeypointdetail;
        return journeypointdetail;
    }

*/




/*
    static JourneyPointDetail access$1302(BusRouteMapActivity busroutemapactivity, JourneyPointDetail journeypointdetail)
    {
        busroutemapactivity.stop = journeypointdetail;
        return journeypointdetail;
    }

*/














/*
    static int access$502(BusRouteMapActivity busroutemapactivity, int i)
    {
        busroutemapactivity.mEndMargin = i;
        return i;
    }

*/



/*
    static int access$602(BusRouteMapActivity busroutemapactivity, int i)
    {
        busroutemapactivity.mStepIndex = i;
        return i;
    }

*/


/*
    static int access$608(BusRouteMapActivity busroutemapactivity)
    {
        int i = busroutemapactivity.mStepIndex;
        busroutemapactivity.mStepIndex = i + 1;
        return i;
    }

*/


/*
    static int access$610(BusRouteMapActivity busroutemapactivity)
    {
        int i = busroutemapactivity.mStepIndex;
        busroutemapactivity.mStepIndex = i - 1;
        return i;
    }

*/



/*
    static int access$702(BusRouteMapActivity busroutemapactivity, int i)
    {
        busroutemapactivity.mSelectedRouteIndex = i;
        return i;
    }

*/


}
