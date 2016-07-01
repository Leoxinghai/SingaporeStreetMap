// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.direction;

import android.content.*;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.google.gson.Gson;
import com.xinghai.mycurve.R;

import java.util.ArrayList;
import java.util.Iterator;
import streetdirectory.mobile.core.ui.SanListViewAdapter;
import streetdirectory.mobile.core.ui.SanListViewItem;
import streetdirectory.mobile.modules.SDActivity;
import streetdirectory.mobile.modules.core.LocationBusinessServiceOutput;
import streetdirectory.mobile.modules.direction.service.DirectionDetailService;
import streetdirectory.mobile.modules.direction.service.DirectionDetailServiceInput;
import streetdirectory.mobile.modules.direction.service.DirectionDetailServiceOutput;
import streetdirectory.mobile.modules.locationdetail.bus.service.*;
import streetdirectory.mobile.modules.nearby.service.NearbyService;
import streetdirectory.mobile.modules.sdmob.SdMobHelper;
import streetdirectory.mobile.modules.sdmob.SmallBanner;
import streetdirectory.mobile.sd.SdMob;
import streetdirectory.mobile.service.SDHttpServiceOutput;
import streetdirectory.mobile.service.URLFactory;

// Referenced classes of package streetdirectory.mobile.modules.direction:
//            DirectionStartCell, DirectionEndCell, DirectionBusTrainCell, DirectionDrivingCell,
//            DirectionAlternateRouteCell, DirectionAlternateRouteBusCell, DirectionCarparkCell, JourneyPointDetail,
//            JourneyPointDetailBus, JourneyDrivingSummary, JourneySummary, JourneyERPInfo,
//            JourneyAlternateRoute, JourneyBusTrainSummary, JourneyTaxiSummary, JourneyFare,
//            JourneyPointDetailCar, Carpark, DirectionMapActivity

public class DirectionListActivity extends SDActivity
{

    public DirectionListActivity()
    {
        mSelectedRouteIndex = 0;
        drivingDetailData = new ArrayList();
        busTrainDetailData = new ArrayList();
        alternateRouteData = new ArrayList();
        adRequestRetryCount = 0;
        mSdMobReceiver = new BroadcastReceiver() {

            public void onReceive(Context context, Intent intent)
            {
                updateSmallBanner();
            }


        };
    }

    private void downloadBusStopInfo(JourneyPointDetail journeypointdetail, JourneyPointDetailBus journeypointdetailbus, final DirectionBusTrainCell directionbustraincell)
    {
        (new BusArrivalServiceV2(null) {

            public void onSuccess(Object obj)
            {
                onSuccess((SDHttpServiceOutput)obj);
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                if(sdhttpserviceoutput.childs.size() > 0)
                {
                    directionbustraincell.busInfoData = (BusArrivalServiceOutputV2)sdhttpserviceoutput.childs.get(0);
                    busTrainCellAdapter.notifyDataSetChanged();
                }
            }

        }).executeAsync();

    }

    private void downloadJourneyDetail()
    {
        String s;
        String s1;
        mProgressBar.setVisibility(View.VISIBLE);
        if(mDirectionDetailService != null)
        {
            mDirectionDetailService.abort();
            mDirectionDetailService = null;
        }
        s = "";
        s1 = "";
        if(mMethod != 0) {
            if(mMethod == 1)
            {
                s = "taxi";
                s1 = "taxi";
            } else
            if(mMethod == 2)
            {
                s = "bustrain";
                s1 = "bus";
            } else
            if(mMethod == 3)
            {
                s = "bustrain";
                s1 = "both";
            }
        } else {
	        s = "driving";
	        s1 = "driving";
        }

        mSelectedRouteIndex = 0;
        mDirectionDetailService = new DirectionDetailService(new DirectionDetailServiceInput(mCountryCode, mStartLocation.longitude, mStartLocation.latitude, mStartLocation.placeID, mStartLocation.addressID, mEndLocation.longitude, mEndLocation.latitude, mEndLocation.placeID, mEndLocation.addressID, URLFactory.encode(mDate), URLFactory.encode(mTime), s, s1)) {

            public void onFailed(Exception exception)
            {
                mProgressBar.setVisibility(View.INVISIBLE);
                super.onFailed(exception);
            }

            public void onSuccess(Object obj)
            {
                onSuccess((SDHttpServiceOutput)obj);
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                super.onSuccess(sdhttpserviceoutput);
                mProgressBar.setVisibility(View.INVISIBLE);
                if(sdhttpserviceoutput.childs.size() > 0)
                {
                    mData = (DirectionDetailServiceOutput)sdhttpserviceoutput.childs.get(0);
                    if(mData != null)
                    {
                        populateHeader(mMethod);
                        populateAlternate(mMethod);
                        if(mSelectedRouteIndex < mData.arrayOfArrayOfPointDetail.size())
                            populateListView(mMethod);
                        if(mData.arrayOfRoutes != null && mData.arrayOfRoutes.size() > 1)
                        {
                            mButtonMoreRoutes.setVisibility(View.VISIBLE);
                            mButtonMoreRoutes.setText((new StringBuilder()).append("Routes (").append(mData.arrayOfRoutes.size()).append(")").toString());
                        }
                    }
                }
            }

        };
        mDirectionDetailService.executeAsync();
        return;
    }

    private String getBusStopId(String s)
    {
        String s2 = "";
        int i = s.indexOf("(B");
        int j = s.indexOf(")");
        String s1 = s2;
        if(s != null)
        {
            s1 = s2;
            if(s.substring(i + 1, j).startsWith("B"))
                s1 = s.substring(i + 2, j);
        }
        return s1;
    }

    private Bitmap getDirectionBitmap(String s)
    {
        Resources resources = getResources();
        if(s.equalsIgnoreCase("u_turn"))
            return BitmapFactory.decodeResource(resources, R.drawable.ic_direction_u_turn);
        if(s.equalsIgnoreCase("turn_left"))
            return BitmapFactory.decodeResource(resources, R.drawable.ic_direction_turn_left);
        if(s.equalsIgnoreCase("turn_right"))
            return BitmapFactory.decodeResource(resources, R.drawable.ic_direction_turn_right);
        if(s.equalsIgnoreCase("slight_left"))
            return BitmapFactory.decodeResource(resources, R.drawable.ic_direction_slip_left);
        if(s.equalsIgnoreCase("slight_right"))
            return BitmapFactory.decodeResource(resources, R.drawable.ic_direction_slip_right);
        else
            return BitmapFactory.decodeResource(resources, R.drawable.ic_direction_straight);
    }

    public static Drawable getDrawable(Context context, String s)
    {
        Resources resources = context.getResources();
        if(s.startsWith("Take"))
            s = "ic_bus_nine_patch";
        else
        if(s.startsWith("Walk"))
            s = "ic_walk";
        else
        if(s.startsWith("East West") || s.startsWith("Changi Line"))
            s = "ic_ewl";
        else
        if(s.startsWith("North East"))
            s = "ic_nel";
        else
        if(s.startsWith("North South"))
            s = "ic_nsl";
        else
        if(s.startsWith("Circle"))
            s = "ic_ccl";
        else
            s = "ic_lrt";
        return resources.getDrawable(resources.getIdentifier(s, "drawable", context.getPackageName()));
    }

    private ArrayList getRouteIcons(int i)
    {
        ArrayList arraylist;
        android.widget.LinearLayout.LayoutParams layoutparams;
        ArrayList arraylist1;
        int j;
        arraylist = new ArrayList();
        wh = mLayoutBusIconContainer.getHeight() - mLayoutBusIconContainer.getPaddingTop() - mLayoutBusIconContainer.getPaddingBottom();
        layoutparams = new android.widget.LinearLayout.LayoutParams(wh, wh);
        arraylist1 = (ArrayList)mData.arrayOfArrayOfPointDetail.get(i);
        j = arraylist1.size();
        i = 0;
        Object obj;
        Object obj1;
        for(;i < j;) {
	        obj1 = (JourneyPointDetail)arraylist1.get(i);
	        obj = new JourneyPointDetailBus(((JourneyPointDetail) (obj1)).hashData);
	        ((JourneyPointDetailBus) (obj)).populateData();
	        if(((JourneyPointDetailBus) (obj)).title.startsWith("Take")) {
	            Button button = (Button)getLayoutInflater().inflate(R.layout.button_bus_icon, null);
	            if(((JourneyPointDetailBus) (obj)).busNo != null)
	            {
	                String as[] = ((JourneyPointDetailBus) (obj)).busNo.split(",");
	                if(as.length > 0)
	                {
	                    button.setText(as[0]);
	                    arraylist.add(button);
	                }
	            }
	        } else {

		        if(!((JourneyPointDetailBus) (obj)).title.startsWith("Walk") || i == 0 || i == j - 1) {
			        obj = getDrawable(this, ((JourneyPointDetail) (obj1)).title);
			        obj1 = new ImageView(this);
			        ((ImageView) (obj1)).setLayoutParams(layoutparams);
			        ((ImageView) (obj1)).setImageDrawable(((Drawable) (obj)));
			        arraylist.add(obj1);
		        }
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
        mCountryCode = intent.getStringExtra("countryCode");
        mMethod = intent.getIntExtra("method", 3);
        mStartLocation = (LocationBusinessServiceOutput)intent.getParcelableExtra("startData");
        mEndLocation = (LocationBusinessServiceOutput)intent.getParcelableExtra("endData");
        mDate = intent.getStringExtra("date");
        mTime = intent.getStringExtra("time");
        mCurrentLatitude = intent.getDoubleExtra("latitude", 0.0D);
        mCurrentLongitude = intent.getDoubleExtra("longitude", 0.0D);
        setLayoutForMethod(mMethod);
        drivingCellAdapter = new SanListViewAdapter(this, 0, drivingDetailData);
        busTrainCellAdapter = new SanListViewAdapter(this, 0, busTrainDetailData);
        alternateCellAdapter = new SanListViewAdapter(this, 0, alternateRouteData);
        mListViewAlternativeRoutes.setAdapter(alternateCellAdapter);
        mListViewAlternativeRoutes.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView adapterview, View view, int i, long l)
            {
                mLayoutAlternativeRoutes.setVisibility(View.INVISIBLE);
                mSelectedRouteIndex = i;
                populateHeader(mMethod);
                populateAlternate(mMethod);
                populateListView(mMethod);
            }

        });
        mListViewRoute.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView adapterview, View view, int i, long l)
            {
                double d;
                double d1;
                SanListViewItem temp = (SanListViewItem)adapterview.getItemAtPosition(i);
                d1 = 0.0D;
                d = 0.0D;
                if((temp instanceof DirectionCarparkCell)) {
                    d1 = ((DirectionCarparkCell) temp).data.longitude;
                    d = ((DirectionCarparkCell) temp).data.latitude;
                } else if(temp instanceof DirectionStartCell)
                {
                    d1 = ((DirectionStartCell)temp).data.longitude;
                    d = ((DirectionStartCell)temp).data.latitude;
                } else if(temp instanceof DirectionEndCell)
                {
                    d1 = ((DirectionEndCell)temp).data.longitude;
                    d = ((DirectionEndCell)temp).data.latitude;
                } else if(temp instanceof DirectionBusTrainCell)
                {
                    d1 = ((DirectionBusTrainCell)temp).data.longitude;
                    d = ((DirectionBusTrainCell)temp).data.latitude;
                } else if(temp instanceof DirectionDrivingCell)
                {
                    d1 = ((DirectionDrivingCell)temp).data.longitude;
                    d = ((DirectionDrivingCell)temp).data.latitude;
                }

                Intent intent = new Intent(DirectionListActivity.this, DirectionMapActivity.class);
                intent.putExtra("data", (new Gson()).toJson(mData));
                intent.putExtra("longitude", d1);
                intent.putExtra("latitude", d);
                intent.putExtra("selectedRouteIndex", mSelectedRouteIndex);
                intent.putExtra("stepIndex", i);
                intent.putExtra("method", mMethod);
                startActivity(intent);
                return;
            }

        });
        wh = mLayoutBusIconContainer.getHeight() - mLayoutBusIconContainer.getPaddingTop() - mLayoutBusIconContainer.getPaddingBottom();
        downloadJourneyDetail();
    }

    private void initEvent()
    {
        mButtonBack.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                finish();
            }

        });
        mLayoutDirectionListTaxiHeader.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                if(mLayoutTaxiFare.getVisibility() == 0)
                {
                    mLayoutTaxiFare.setVisibility(View.INVISIBLE);
                    return;
                } else
                {
                    mLayoutTaxiFare.setVisibility(View.VISIBLE);
                    return;
                }
            }

        });
        mButtonMoreRoutes.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                if(mLayoutAlternativeRoutes.getVisibility() != 0)
                    mLayoutAlternativeRoutes.setVisibility(View.VISIBLE);
                else
                    mLayoutAlternativeRoutes.setVisibility(View.INVISIBLE);
                populateAlternate(mMethod);
            }

        });
    }

    private void initLayout()
    {
        mLayoutMenubar = (RelativeLayout)findViewById(R.id.layout_menubar);
        mButtonBack = (Button)findViewById(R.id.button_back);
        mButtonMoreRoutes = (Button)findViewById(R.id.button_more_routes);
        mLayoutDirectionListHeader = (RelativeLayout)findViewById(R.id.layout_direction_list_header);
        mLayoutDirectionListDrivingHeader = (RelativeLayout)findViewById(R.id.layout_direction_list_driving_header);
        mTextViewRouteTitle = (TextView)findViewById(R.id.text_view_route_title);
        mTextViewErpPrice = (TextView)findViewById(R.id.text_view_erp_price);
        mTextViewTime = (TextView)findViewById(R.id.text_view_time);
        mTextViewDistance = (TextView)findViewById(R.id.text_view_distance);
        mLayoutDirectionListTaxiHeader = (RelativeLayout)findViewById(R.id.layout_direction_list_taxi_header);
        mTextViewTotalTime = (TextView)findViewById(R.id.text_view_total_time);
        mTextViewApproximateFare = (TextView)findViewById(R.id.text_view_approximate_fare);
        mTextViewTotalTimeValue = (TextView)findViewById(R.id.text_view_total_time_value);
        mTextViewApproximateFareValue = (TextView)findViewById(R.id.text_view_approximate_fare_value);
        mButtonShowDetailFare = (Button)findViewById(R.id.button_show_detail_fare);
        mLayoutDirectionListBusTrainHeader = (RelativeLayout)findViewById(R.id.layout_direction_list_bus_train_header);
        mLayoutBusIconContainer = (LinearLayout)findViewById(R.id.layout_bus_icon_container);
        mTextViewWalk = (TextView)findViewById(R.id.text_view_walk);
        mTextViewWalkValue = (TextView)findViewById(R.id.text_view_walk_value);
        mTextViewBusFareValue = (TextView)findViewById(R.id.text_view_bus_fare_value);
        mTextViewWith = (TextView)findViewById(R.id.text_view_with);
        mTextViewBusTimeValue = (TextView)findViewById(R.id.text_view_bus_time_value);
        mLayoutTaxiFare = (RelativeLayout)findViewById(R.id.layout_taxi_fare);
        mTextViewMeteredFare = (TextView)findViewById(R.id.text_view_metered_fare);
        mTextViewFlagdownFare = (TextView)findViewById(R.id.text_view_flagdown_fare);
        mTextViewMeteredFareValue = (TextView)findViewById(R.id.text_view_metered_fare_value);
        mTextViewFlagdownFareValue = (TextView)findViewById(R.id.text_view_flagdown_fare_value);
        mListViewRoute = (ListView)findViewById(R.id.list_view_route);
        mListViewRoute.setDivider(null);
        mListViewRoute.setDividerHeight(0);
        mLayoutAlternativeRoutes = (LinearLayout)findViewById(R.id.layout_alternative_routes);
        mListViewAlternativeRoutes = (ListView)findViewById(R.id.list_view_alternative_routes);
        mTextViewTitle = (TextView)findViewById(R.id.text_view_title);
        mSdMobViewHolder = (RelativeLayout)findViewById(R.id.view_sdmob);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar1);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    private void initialize()
    {
        initLayout();
        initData();
        initEvent();
    }

    private void setLayoutForMethod(int i)
    {
        mLayoutDirectionListDrivingHeader.setVisibility(View.INVISIBLE);
        mLayoutDirectionListTaxiHeader.setVisibility(View.INVISIBLE);
        mLayoutDirectionListBusTrainHeader.setVisibility(View.INVISIBLE);
        if(i == 0)
        {
            mLayoutDirectionListDrivingHeader.setVisibility(View.VISIBLE);
            mTextViewTitle.setText("Driving");
        } else
        {
            if(i == 1)
            {
                mLayoutDirectionListTaxiHeader.setVisibility(View.VISIBLE);
                mTextViewTitle.setText("Taxi");
                return;
            }
            if(i == 2 || i == 3)
            {
                mLayoutDirectionListBusTrainHeader.setVisibility(View.VISIBLE);
                if(i == 2)
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
    }

    private void updateSmallBanner()
    {
        mCurrentSmallBanner = SmallBanner.getBannerFromSdMobUnit(this, SdMobHelper.getInstance(this).getSdMobUnit(SdMob.ad_bnr_direction_result));
//        mCurrentSmallBanner.setAdMobSmallBannerListener(new streetdirectory.mobile.modules.sdmob.SmallBanner.AdMobSmallBannerListener());
        View view = mCurrentSmallBanner.getView(this);
        mSdMobViewHolder.removeAllViews();
        mSdMobViewHolder.addView(view, new ViewGroup.LayoutParams(-1, -1));
    }

    protected void abortAllProcess()
    {
        abortAllProcess();
        mDirectionDetailService.abort();
        mDirectionDetailService = null;
    }

    protected void onCreate(Bundle bundle)
    {
        onCreate(bundle);
        setContentView(R.layout.activity_direction_list);
        initialize();
        updateSmallBanner();
        LocalBroadcastManager.getInstance(this).registerReceiver(mSdMobReceiver, new IntentFilter("sdmob_broadcast"));
    }

    protected void onDestroy()
    {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mSdMobReceiver);
        onDestroy();
    }

    protected void onResume()
    {
        onResume();
    }

    protected void populateAlternate(int i)
    {
        alternateCellAdapter.clear();
        if(i == 0)
        {
            if(mData.arrayOfSummary != null)
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

                    JourneyAlternateRoute temp  = new JourneyAlternateRoute();
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
            }
        } else
        if((i == 2 || i == 3) && mData.arrayOfSummary != null)
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

    protected void populateHeader(int i)
    {
        if(i == 0) {
            if (mData.arrayOfSummary != null) {
                Object obj = new JourneyDrivingSummary(((JourneySummary) mData.arrayOfSummary.get(mSelectedRouteIndex)).hashData);
                ((JourneyDrivingSummary) (obj)).populateData();
                mTextViewRouteTitle.setText((new StringBuilder()).append("Via ").append(((JourneyDrivingSummary) (obj)).title).toString());
                mTextViewDistance.setText(((JourneyDrivingSummary) (obj)).totalLength);
                mTextViewTime.setText(((JourneyDrivingSummary) (obj)).totalTime);
                obj = (ArrayList) mData.arrayOfArrayOfERPInfo.get(mSelectedRouteIndex);
                double d = 0.0D;
                for (obj = ((ArrayList) (obj)).iterator(); ((Iterator) (obj)).hasNext(); )
                    d += ((JourneyERPInfo) ((Iterator) (obj)).next()).price;

                mTextViewErpPrice.setText(String.valueOf(d));
            }
        }

        if(mData.taxiSummary == null)
            return;
        if(i == 1) {

            Object obj1 = mData.taxiSummary;
            mTextViewTotalTimeValue.setText(((JourneyTaxiSummary) (obj1)).totalTime);
            obj1 = (JourneyFare) mData.arrayOfTaxiFare.get(0);
            mTextViewApproximateFareValue.setText((new StringBuilder()).append(((JourneyFare) (obj1)).unit).append(((JourneyFare) (obj1)).value).toString());
            obj1 = (JourneyFare) mData.arrayOfTaxiFare.get(1);
            mTextViewMeteredFareValue.setText((new StringBuilder()).append(((JourneyFare) (obj1)).unit).append(((JourneyFare) (obj1)).value).toString());
            obj1 = (JourneyFare) mData.arrayOfTaxiFare.get(2);
            mTextViewFlagdownFareValue.setText((new StringBuilder()).append(((JourneyFare) (obj1)).unit).append(((JourneyFare) (obj1)).value).toString());
            return;
        }

        if(i == 2 || i != 3) {
            Object obj2 = new JourneyBusTrainSummary(((JourneySummary) mData.arrayOfSummary.get(mSelectedRouteIndex)).hashData);
            ((JourneyBusTrainSummary) (obj2)).populateData();
            mTextViewWalkValue.setText((new StringBuilder()).append(((JourneyBusTrainSummary) (obj2)).totalWalk).append("m").toString());
            mTextViewBusFareValue.setText((new StringBuilder()).append("$").append(((JourneyBusTrainSummary) (obj2)).totalFare).toString());
            mTextViewBusTimeValue.setText(((JourneyBusTrainSummary) (obj2)).totalTime);
            obj2 = getRouteIcons(mSelectedRouteIndex);
            mLayoutBusIconContainer.removeAllViews();
            View view;
            for (obj2 = ((ArrayList) (obj2)).iterator(); ((Iterator) (obj2)).hasNext(); mLayoutBusIconContainer.addView(view))
                view = (View) ((Iterator) (obj2)).next();

            mLayoutBusIconContainer.setVisibility(View.VISIBLE);
            return;
        }

        return;
    }

    protected void populateListView(int i)
    {
            if(mData.arrayOfArrayOfPointDetail != null)
            {
                boolean flag = false;
                if(i == 0 || i == 1) {
                    drivingCellAdapter.clear();
                    Object obj = new DirectionStartCell(mData.start);
                    drivingCellAdapter.add(obj);
                    obj = ((ArrayList) mData.arrayOfArrayOfPointDetail.get(mSelectedRouteIndex)).iterator();
                    for (i = ((flag) ? 1 : 0); ((Iterator) (obj)).hasNext(); i++) {
                        Object obj2 = new JourneyPointDetailCar(((JourneyPointDetail) ((Iterator) (obj)).next()).hashData);
                        ((JourneyPointDetailCar) (obj2)).populateData();
                        obj2 = new DirectionDrivingCell(((JourneyPointDetailCar) (obj2)), getDirectionBitmap(((JourneyPointDetailCar) (obj2)).directionEnum));
                        drivingCellAdapter.add(obj2);
                    }

                    if (mData.arrayOfCarpark != null) {
                        char c = 'A';
                        Object obj3;
                        for (obj = mData.arrayOfCarpark.iterator(); ((Iterator) (obj)).hasNext(); drivingCellAdapter.add(obj3)) {
                            obj3 = (Carpark) ((Iterator) (obj)).next();
                            obj3 = new DirectionCarparkCell(((Carpark) (obj3)).placeName, Character.toString(c), ((Carpark) (obj3)));
                            c++;
                        }

                    }
                    obj = new DirectionEndCell(mData.end);
                    drivingCellAdapter.add(obj);
                    mListViewRoute.setAdapter(drivingCellAdapter);
                    drivingCellAdapter.notifyDataSetChanged();
                } else {
                    busTrainCellAdapter.clear();
                    Object obj1 = new DirectionStartCell(mData.start);
                    busTrainCellAdapter.add(obj1);
                    Iterator iterator = ((ArrayList)mData.arrayOfArrayOfPointDetail.get(mSelectedRouteIndex)).iterator();
                    while(iterator.hasNext())
                    {
                        final JourneyPointDetail journeyPointDetail = (JourneyPointDetail)iterator.next();
                        final JourneyPointDetailBus final_journeypointdetailbus = new JourneyPointDetailBus(((JourneyPointDetail) (obj1)).hashData);
                        final_journeypointdetailbus.populateData();
                        DirectionBusTrainCell directionBusTrainCell;
                        if(final_journeypointdetailbus.title.startsWith("Walk"))
                            directionBusTrainCell = new DirectionBusTrainCell(final_journeypointdetailbus, getDrawable(this, final_journeypointdetailbus.title), false, false);
                        else
                        if(final_journeypointdetailbus.title.startsWith("Take"))
                            directionBusTrainCell = new DirectionBusTrainCell(final_journeypointdetailbus, null, true, true) {

                                protected void onButtonBusArrivalClicked()
                                {
                                    if(super.isShowBusArrival)
                                        downloadBusStopInfo(journeyPointDetail, final_journeypointdetailbus, this);
                                    busTrainCellAdapter.notifyDataSetChanged();
                                }

                                protected void onButtonRefreshClicked()
                                {
                                    downloadBusStopInfo(journeyPointDetail, final_journeypointdetailbus, this);
                                }

                            };
                        else
                            obj1 = new DirectionBusTrainCell(final_journeypointdetailbus, getDrawable(this, final_journeypointdetailbus.title), true, false);
                        busTrainCellAdapter.add(obj1);
                    }
                    obj1 = new DirectionEndCell(mData.end);
                    busTrainCellAdapter.add(obj1);
                    mListViewRoute.setAdapter(busTrainCellAdapter);
                    busTrainCellAdapter.notifyDataSetChanged();

                }
            }
            return;
    }

    public static final int FARE_APPROXIMATE = 0;
    public static final int FARE_FLAGDOWN = 2;
    public static final int FARE_METERED = 1;
    public static final int METHOD_BUS = 2;
    public static final int METHOD_BUS_TRAIN = 3;
    public static final int METHOD_DRIVING = 0;
    public static final int METHOD_TAXI = 1;
    private NearbyService _nearbyService;
    private int adRequestRetryCount;
    SanListViewAdapter alternateCellAdapter;
    ArrayList alternateRouteData;
    SanListViewAdapter busTrainCellAdapter;
    ArrayList busTrainDetailData;
    SanListViewAdapter drivingCellAdapter;
    ArrayList drivingDetailData;
    private Button mButtonBack;
    private Button mButtonMoreRoutes;
    private Button mButtonShowDetailFare;
    private String mCountryCode;
    private double mCurrentLatitude;
    private double mCurrentLongitude;
    private SmallBanner mCurrentSmallBanner;
    private DirectionDetailServiceOutput mData;
    private String mDate;
    private DirectionDetailService mDirectionDetailService;
    private LocationBusinessServiceOutput mEndLocation;
    private LinearLayout mLayoutAlternativeRoutes;
    private LinearLayout mLayoutBusIconContainer;
    private RelativeLayout mLayoutDirectionListBusTrainHeader;
    private RelativeLayout mLayoutDirectionListDrivingHeader;
    private RelativeLayout mLayoutDirectionListHeader;
    private RelativeLayout mLayoutDirectionListTaxiHeader;
    private RelativeLayout mLayoutMenubar;
    private RelativeLayout mLayoutTaxiFare;
    private ListView mListViewAlternativeRoutes;
    private ListView mListViewRoute;
    private int mMethod;
    ProgressBar mProgressBar;
    private BroadcastReceiver mSdMobReceiver;
    private RelativeLayout mSdMobViewHolder;
    private int mSelectedRouteIndex;
    private LocationBusinessServiceOutput mStartLocation;
    private TextView mTextViewApproximateFare;
    private TextView mTextViewApproximateFareValue;
    private TextView mTextViewBusFareValue;
    private TextView mTextViewBusTimeValue;
    private TextView mTextViewDistance;
    private TextView mTextViewErpPrice;
    private TextView mTextViewFlagdownFare;
    private TextView mTextViewFlagdownFareValue;
    private TextView mTextViewMeteredFare;
    private TextView mTextViewMeteredFareValue;
    private TextView mTextViewRouteTitle;
    private TextView mTextViewTime;
    private TextView mTextViewTitle;
    private TextView mTextViewTotalTime;
    private TextView mTextViewTotalTimeValue;
    private TextView mTextViewWalk;
    private TextView mTextViewWalkValue;
    private TextView mTextViewWith;
    private String mTime;
    int wh;

    static
    {
        SanListViewItem.addTypeCount(DirectionStartCell.class);
        SanListViewItem.addTypeCount(DirectionEndCell.class);
        SanListViewItem.addTypeCount(DirectionBusTrainCell.class);
        SanListViewItem.addTypeCount(DirectionDrivingCell.class);
        SanListViewItem.addTypeCount(DirectionAlternateRouteCell.class);
        SanListViewItem.addTypeCount(DirectionAlternateRouteBusCell.class);
        SanListViewItem.addTypeCount(DirectionCarparkCell.class);
    }




/*
    static int access$102(DirectionListActivity directionlistactivity, int i)
    {
        directionlistactivity.mSelectedRouteIndex = i;
        return i;
    }

*/




/*
    static DirectionDetailServiceOutput access$302(DirectionListActivity directionlistactivity, DirectionDetailServiceOutput directiondetailserviceoutput)
    {
        directionlistactivity.mData = directiondetailserviceoutput;
        return directiondetailserviceoutput;
    }

*/







/*
    static int access$802(DirectionListActivity directionlistactivity, int i)
    {
        directionlistactivity.adRequestRetryCount = i;
        return i;
    }

*/


/*
    static int access$808(DirectionListActivity directionlistactivity)
    {
        int i = directionlistactivity.adRequestRetryCount;
        directionlistactivity.adRequestRetryCount = i + 1;
        return i;
    }

*/
}
