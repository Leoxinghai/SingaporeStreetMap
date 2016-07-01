

package streetdirectory.mobile.modules.direction;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.*;
import com.google.gson.Gson;
import com.xinghai.mycurve.R;

import java.util.ArrayList;
import java.util.Iterator;
import streetdirectory.mobile.core.*;
import streetdirectory.mobile.core.ui.SanListViewAdapter;
import streetdirectory.mobile.core.ui.UIHelper;
import streetdirectory.mobile.gis.GeoPoint;
import streetdirectory.mobile.gis.maps.MapView;
import streetdirectory.mobile.gis.maps.configs.MapPresetCollection;
import streetdirectory.mobile.modules.SDActivity;
import streetdirectory.mobile.modules.direction.service.DirectionDetailServiceOutput;
import streetdirectory.mobile.modules.map.layers.BusLayer;
import streetdirectory.mobile.modules.sdmob.SdMobHelper;
import streetdirectory.mobile.modules.sdmob.SmallBanner;
import streetdirectory.mobile.sd.SDBlackboard;
import streetdirectory.mobile.sd.SdMob;
import streetdirectory.mobile.service.URLFactory;

// Referenced classes of package streetdirectory.mobile.modules.direction:
//            JourneyPointDetail, JourneyPointDetailBus, DirectionListActivity, DirectionMapLayer,
//            Carpark, JourneyDrivingSummary, JourneySummary, JourneyERPInfo,
//            JourneyAlternateRoute, DirectionAlternateRouteCell, JourneyBusTrainSummary, DirectionAlternateRouteBusCell

public class DirectionMapActivity extends SDActivity
{

    public DirectionMapActivity()
    {
        mapLayer = null;
        mJpdTouchBuffer = 32;
        mStepIndex = 0;
        mPixel = new PointF64();
        mJpdSize = 0;
        mCarparkSize = 0;
        mTotalSize = 0;
        mEndMargin = 0;
        adRequestRetryCount = 0;
        alternateRouteData = new ArrayList();
    }

    private ArrayList getRouteIcons(int i)
    {
        ArrayList arraylist;
        android.widget.LinearLayout.LayoutParams layoutparams;
        ArrayList arraylist1;
        int j;
        arraylist = new ArrayList();
        mWh = (int)UIHelper.toPixel(25F);
        layoutparams = new android.widget.LinearLayout.LayoutParams(mWh, mWh);
        arraylist1 = (ArrayList)mData.arrayOfArrayOfPointDetail.get(i);
        j = arraylist1.size();
        i = 0;

        Object obj;
        Object obj1;
        for(;i < j;) {
            obj1 = (JourneyPointDetail) arraylist1.get(i);
            obj = new JourneyPointDetailBus(((JourneyPointDetail) (obj1)).hashData);
            ((JourneyPointDetailBus) (obj)).populateData();
            if (((JourneyPointDetailBus) (obj)).title.startsWith("Take")) {
                Button button = (Button) getLayoutInflater().inflate(R.layout.button_bus_icon, null);
                if (((JourneyPointDetailBus) (obj)).busNo != null) {
                    String as[] = ((JourneyPointDetailBus) (obj)).busNo.split(",");
                    if (as.length > 0) {
                        button.setText(as[0]);
                        arraylist.add(button);
                    }
                }
            }
            if (!((JourneyPointDetailBus) (obj)).title.startsWith("Walk") || i == 0 || i == j - 1) {
                obj = DirectionListActivity.getDrawable(this, ((JourneyPointDetail) (obj1)).title);
                obj1 = new ImageView(this);
                ((ImageView) (obj1)).setLayoutParams(layoutparams);
                ((ImageView) (obj1)).setImageDrawable(((android.graphics.drawable.Drawable) (obj)));
                arraylist.add(obj1);

            } else {
                i++;
                continue;
            }
            if(i != j - 1)
            {
                obj = new ImageView(this);
                ((ImageView) (obj)).setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_gray));
                ((ImageView) (obj)).setLayoutParams(layoutparams);
                arraylist.add(obj);
            }
            i++;
        }

        return arraylist;
    }

    private void initData()
    {
        Intent intent = getIntent();
        String s = intent.getStringExtra("data");
        Gson gson = new Gson();
        mLongitude = intent.getDoubleExtra("longitude", 0.0D);
        mLatitude = intent.getDoubleExtra("latitude", 0.0D);
        mSelectedRouteIndex = intent.getIntExtra("selectedRouteIndex", 0);
        mStepIndex = intent.getIntExtra("stepIndex", 0);
        mMethod = intent.getIntExtra("method", 0);
        mData = (DirectionDetailServiceOutput)gson.fromJson(s, DirectionDetailServiceOutput.class);
        mData.populateData();
        mStartLongitude = intent.getDoubleExtra("longitude", 0.0D);
        mStartLatitude = intent.getDoubleExtra("latitude", 0.0D);
        if(mMethod == 0)
        {
            mTextViewTitle.setText("Driving");
        } else
        {
            if(mMethod == 1)
            {
                mTextViewTitle.setText("Taxi");
                return;
            }
            if(mMethod == 2 || mMethod == 3)
                if(mMethod == 2)
                {
                    mTextViewTitle.setText("Bus");
                    return;
                } else
                {
                    mTextViewTitle.setText("Bus/MRT");
                    return;
                }
        }
    }

    private void initEvent()
    {
        mapLayer = new DirectionMapLayer(this);
        mapLayer.mData = mData;
        mapLayer.focusLongitude = mLongitude;
        mapLayer.focusLatitude = mLatitude;
        mapLayer.mMethod = mMethod;
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
                RelativeLayout.LayoutParams temp = (android.widget.RelativeLayout.LayoutParams)mLayoutTop.getLayoutParams();
                i = mLayoutTop.getHeight() - i;
                TranslateAnimation temp2;
                if(((android.widget.RelativeLayout.LayoutParams) (temp)).topMargin < 0)
                {
                    temp2 = new TranslateAnimation(0.0F, 0.0F, 0.0F, i);
                    mEndMargin = 0;
                    mImageViewHideNavBar.setImageResource(R.drawable.button_hide_navbar);
                } else
                {
                    temp2 = new TranslateAnimation(0.0F, 0.0F, 0.0F, -i);
                    mEndMargin = -i;
                    mImageViewHideNavBar.setImageResource(R.drawable.button_show_navbar);
                }
                temp2.setDuration(200L);
                temp2.setFillAfter(false);
                temp2.setAnimationListener(new android.view.animation.Animation.AnimationListener() {

                    public void onAnimationEnd(Animation animation)
                    {
                        mLayoutTop.clearAnimation();
                        RelativeLayout.LayoutParams temp3 = (android.widget.RelativeLayout.LayoutParams)mLayoutTop.getLayoutParams();
                        temp3.setMargins(0, mEndMargin, 0, 0);
                        mLayoutTop.setLayoutParams(temp3);
                    }

                    public void onAnimationRepeat(Animation animation)
                    {
                    }

                    public void onAnimationStart(Animation animation)
                    {
                    }

                });
                mLayoutTop.startAnimation(temp2);
            }

        });
        mButtonBack.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                finish();
            }

        });
        mimageViewNext.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {

                DirectionMapActivity.this.mStepIndex = DirectionMapActivity.this.mStepIndex + 1;
                if(mStepIndex >= mTotalSize)
                {
                    mStepIndex = mTotalSize - 1;
                    return;
                } else
                {
                    populateTopLayout();
                    return;
                }
            }

        });
        mimageViewPrev.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                DirectionMapActivity.this.mStepIndex = DirectionMapActivity.this.mStepIndex - 1;
                if(mStepIndex < 0)
                {
                    mStepIndex = 0;
                    return;
                } else
                {
                    populateTopLayout();
                    return;
                }
            }

        });

        mMapView.setOnMapClicked(new streetdirectory.mobile.gis.maps.MapView.OnMapClickedListener() {

            public void onMapClicked(MapView mapview, GeoPoint geopoint, Point point)
            {
                if(tapHandler(point, mData.start.longitude, mData.start.latitude)) {
                    mStepIndex = 0;
                    populateTopLayout();
                    return;
                }
                if(tapHandler(point, mData.end.longitude, mData.end.latitude))
                {
                    mStepIndex = mJpdSize;
                    populateTopLayout();
                    return;
                }
                int i = 0;
                Iterator iterator;
                JourneyPointDetail temp;
                for(iterator = ((ArrayList)mData.arrayOfArrayOfPointDetail.get(mSelectedRouteIndex)).iterator(); iterator.hasNext();)
                {
                    temp = (JourneyPointDetail)iterator.next();
                    if(tapHandler(point, ((JourneyPointDetail) (temp)).longitude, ((JourneyPointDetail) (temp)).latitude))
                    {
                        mStepIndex = i + 1;
                        populateTopLayout();
                        return;
                    }
                    i++;
                }

                i = 0;
                if(mData.arrayOfCarpark != null)
                {
                    Iterator iterator2 = mData.arrayOfCarpark.iterator();
                    while(iterator2.hasNext())
                    {
                        Carpark temp2 = (Carpark)iterator2.next();
                        if(tapHandler(point, ((Carpark) (temp2)).longitude, ((Carpark) (temp2)).latitude))
                        {
                            mtextViewDesc.setText(((Carpark) (temp2)).placeName);
                            mStepIndex = mJpdSize + i;
                            populateTopLayout();
                            return;
                        }
                        i++;
                    }
                }
                return;
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
        alternateCellAdapter = new SanListViewAdapter(this, 0, alternateRouteData);
        mListViewAlternativeRoutes.setAdapter(alternateCellAdapter);
        mListViewAlternativeRoutes.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView adapterview, View view, int i, long l)
            {
                mLayoutAlternativeRoutes.setVisibility(View.INVISIBLE);
                mSelectedRouteIndex = i;
                mapLayer.selectedRouteIndex = mSelectedRouteIndex;
                mStepIndex = 0;
                populateData();
            }

        });
    }

    private void initLayout()
    {
        mMapView = (MapView)findViewById(R.id.MapView);
        mLayoutMenubar = (RelativeLayout)findViewById(R.id.layout_menubar);
        mButtonBack = (Button)findViewById(R.id.button_back);
        mButtonMoreRoutes = (Button)findViewById(R.id.button_more_routes);
        mLayoutDesc = (RelativeLayout)findViewById(R.id.layout_desc);
        mLayoutTop = (RelativeLayout)findViewById(R.id.layout_top);
        mimageViewPrev = (ImageView)findViewById(R.id.imageViewPrev);
        mimageViewNext = (ImageView)findViewById(R.id.imageViewNext);
        mimageViewIcon = (ImageView)findViewById(R.id.imageViewIcon);
        mtextViewDesc = (TextView)findViewById(R.id.textViewDesc);
        mviewBar = findViewById(R.id.viewBar);
        mImageViewHideNavBar = (ImageView)findViewById(R.id.ImageViewHideNavBar);
        mLayoutAlternativeRoutes = (LinearLayout)findViewById(R.id.layout_alternative_routes);
        mListViewAlternativeRoutes = (ListView)findViewById(R.id.list_view_alternative_routes);
        mTextViewTitle = (TextView)findViewById(R.id.textview_title);
        mSdSmallBanner = (RelativeLayout)findViewById(R.id.view_sdmob);
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
                DirectionMapActivity directionmapactivity = DirectionMapActivity.this;
                directionmapactivity.adRequestRetryCount = directionmapactivity.adRequestRetryCount + 1;
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
            populateAlternate(mMethod);
        }
        mJpdSize = ((ArrayList)mData.arrayOfArrayOfPointDetail.get(mSelectedRouteIndex)).size();
        if(mData.arrayOfCarpark != null)
            mCarparkSize = mData.arrayOfCarpark.size();
        mTotalSize = mJpdSize + 2;
        if(mData.arrayOfCarpark != null)
            mTotalSize = mTotalSize + mData.arrayOfCarpark.size();
        populateTopLayout();
    }

    private void populateTopLayout()
    {
        if(mStepIndex == 0)
        {
            mtextViewDesc.setText((new StringBuilder()).append(mData.start.title).append(". ").append(mData.start.desc).toString());
            mMapView.center = new GeoPoint(mData.start.longitude, mData.start.latitude);
            mimageViewPrev.setVisibility(4);
            mimageViewNext.setVisibility(View.VISIBLE);
        } else
        if(mStepIndex <= mJpdSize)
        {
            JourneyPointDetail journeypointdetail = (JourneyPointDetail)((ArrayList)mData.arrayOfArrayOfPointDetail.get(mSelectedRouteIndex)).get(mStepIndex - 1);
            mtextViewDesc.setText((new StringBuilder()).append(journeypointdetail.title).append(". ").append(journeypointdetail.desc).toString());
            mMapView.center = new GeoPoint(journeypointdetail.longitude, journeypointdetail.latitude);
            mimageViewPrev.setVisibility(View.VISIBLE);
            mimageViewNext.setVisibility(View.VISIBLE);
        } else
        if(mStepIndex == mJpdSize + mCarparkSize + 1)
        {
            mtextViewDesc.setText((new StringBuilder()).append(mData.end.title).append(". ").append(mData.end.desc).toString());
            mMapView.center = new GeoPoint(mData.end.longitude, mData.end.latitude);
            mimageViewPrev.setVisibility(View.VISIBLE);
            mimageViewNext.setVisibility(4);
        } else
        if(mData.arrayOfCarpark != null && mStepIndex > mJpdSize)
        {
            int i = mStepIndex;
            int j = mJpdSize;
            Carpark carpark = (Carpark)mData.arrayOfCarpark.get(i - j - 1);
            mtextViewDesc.setText(carpark.placeName);
            mMapView.center = new GeoPoint(carpark.longitude, carpark.latitude);
            mimageViewPrev.setVisibility(View.VISIBLE);
            mimageViewNext.setVisibility(View.VISIBLE);
        } else
        {
            mtextViewDesc.setText("");
            mimageViewPrev.setVisibility(4);
            mimageViewNext.setVisibility(4);
        }
        mapLayer.focusLongitude = mMapView.center.longitude;
        mapLayer.focusLatitude = mMapView.center.latitude;
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
        SDStory.post(URLFactory.createGantDirectionsMap(), SDStory.createDefaultParams());
        initialize();
        loadSmallBanner();
    }

    protected void populateAlternate(int i)
    {
        alternateCellAdapter.clear();
        if(i == 0)
        {
            i = 0;
            while(i < mData.arrayOfSummary.size())
            {
                Object obj = new JourneyDrivingSummary(((JourneySummary)mData.arrayOfSummary.get(i)).hashData);
                ((JourneyDrivingSummary) (obj)).populateData();
                Object obj2 = (ArrayList)mData.arrayOfArrayOfERPInfo.get(i);
                double d = 0.0D;
                for(obj2 = ((ArrayList) (obj2)).iterator(); ((Iterator) (obj2)).hasNext();)
                    d += ((JourneyERPInfo)((Iterator) (obj2)).next()).price;

                JourneyAlternateRoute temp = new JourneyAlternateRoute();
                temp.erp = String.valueOf(d);
                temp.title = ((JourneyDrivingSummary) (obj)).title;
                temp.totalDistance = ((JourneyDrivingSummary) (obj)).totalLength;
                temp.totalTime = ((JourneyDrivingSummary) (obj)).totalTime;
                DirectionAlternateRouteCell temp2 = new DirectionAlternateRouteCell(((JourneyAlternateRoute) (temp)));
                if(i == mSelectedRouteIndex)
                    temp2.routeChecked = true;
                else
                    temp2.routeChecked = false;
                alternateCellAdapter.add(temp2);
                i++;
            }
        } else
        if(i == 2 || i == 3)
        {
            i = 0;
            while(i < mData.arrayOfSummary.size())
            {
                Object obj1 = new JourneyBusTrainSummary(((JourneySummary)mData.arrayOfSummary.get(i)).hashData);
                ((JourneyBusTrainSummary) (obj1)).populateData();
                DirectionAlternateRouteBusCell temp3 = new DirectionAlternateRouteBusCell((new StringBuilder()).append("walk ").append(((JourneyBusTrainSummary) (obj1)).totalWalk).append(" m, ").append(((JourneyBusTrainSummary) (obj1)).totalTime).append(" with $").append(((JourneyBusTrainSummary) (obj1)).totalFare).toString(), getRouteIcons(i));
                if(i == mSelectedRouteIndex)
                    temp3.routeChecked = true;
                else
                    temp3.routeChecked = false;
                alternateCellAdapter.add(temp3);
                i++;
            }
        }
        alternateCellAdapter.notifyDataSetChanged();
    }

    int adRequestRetryCount;
    SanListViewAdapter alternateCellAdapter;
    ArrayList alternateRouteData;
    public BusLayer mBusLayer;
    private Button mButtonBack;
    private Button mButtonMoreRoutes;
    int mCarparkSize;
    private DirectionDetailServiceOutput mData;
    int mEndMargin;
    private ImageView mImageViewHideNavBar;
    int mJpdSize;
    int mJpdTouchBuffer;
    double mLatitude;
    private LinearLayout mLayoutAlternativeRoutes;
    private RelativeLayout mLayoutDesc;
    private RelativeLayout mLayoutMenubar;
    private RelativeLayout mLayoutTop;
    private ListView mListViewAlternativeRoutes;
    double mLongitude;
    private MapView mMapView;
    int mMethod;
    PointF64 mPixel;
    private RelativeLayout mSdSmallBanner;
    int mSelectedRouteIndex;
    private double mStartLatitude;
    private double mStartLongitude;
    int mStepIndex;
    private TextView mTextViewTitle;
    int mTotalSize;
    int mWh;
    DirectionMapLayer mapLayer;
    private ImageView mimageViewIcon;
    private ImageView mimageViewNext;
    private ImageView mimageViewPrev;
    private TextView mtextViewDesc;
    private View mviewBar;

}
