// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.locationdetail.bus;

import android.content.*;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.*;
import com.google.gson.Gson;
import com.xinghai.mycurve.R;

import java.util.ArrayList;
import java.util.Iterator;
import streetdirectory.mobile.core.ui.SanListViewAdapter;
import streetdirectory.mobile.core.ui.SanListViewItem;
import streetdirectory.mobile.modules.SDActivity;
import streetdirectory.mobile.modules.direction.JourneyPointDetail;
import streetdirectory.mobile.modules.locationdetail.bus.service.BusArrivalServiceInputV2;
import streetdirectory.mobile.modules.locationdetail.bus.service.BusArrivalServiceOutputV2;
import streetdirectory.mobile.modules.locationdetail.bus.service.BusArrivalServiceV2;
import streetdirectory.mobile.modules.locationdetail.bus.service.BusRouteAlternate;
import streetdirectory.mobile.modules.locationdetail.bus.service.BusRouteSummary;
import streetdirectory.mobile.modules.locationdetail.bus.service.BusRoutesService;
import streetdirectory.mobile.modules.locationdetail.bus.service.BusRoutesServiceInput;
import streetdirectory.mobile.modules.locationdetail.bus.service.BusRoutesServiceOutput;
import streetdirectory.mobile.modules.sdmob.SdMobHelper;
import streetdirectory.mobile.modules.sdmob.SmallBanner;
import streetdirectory.mobile.sd.SdMob;
import streetdirectory.mobile.service.*;

// Referenced classes of package streetdirectory.mobile.modules.locationdetail.bus:
//            BusRouteCell, BusRouteAlternateCell, BusRouteMapActivity

public class BusRouteActivity extends SDActivity
{

    public BusRouteActivity()
    {
        mBusRouteData = new ArrayList();
        mAlternateRouteData = new ArrayList();
        imagePool = new SDHttpImageServicePool();
        mSelectedRouteIndex = 0;
        adRequestRetryCount = 0;
        mSdMobReceiver = new BroadcastReceiver() {

            public void onReceive(Context context, Intent intent)
            {
                updateSmallBanner();
            }

        };
    }

    private void checkSelectedRoute()
    {
        int i = 0;
        for(;i < mData.arrayOfRoutes.size();i++)
        {
            Iterator iterator = ((ArrayList)mData.arrayOfArrayOfPointDetail.get(i)).iterator();
            for(;iterator.hasNext();) {
                if (((JourneyPointDetail) iterator.next()).title.equals(mCurrentBusStopId)) {
                    mSelectedRouteIndex = i;
                    populateAlternateRoute();
                    populateListView();
                    return;
                }
            }
        }
    }

    private void downloadBusRoutes()
    {
        mService = new BusRoutesService(new BusRoutesServiceInput(busNumber)) {

            public void onFailed(Exception exception)
            {
                mLayoutLoading.setVisibility(View.INVISIBLE);
                super.onFailed(exception);
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                mLayoutLoading.setVisibility(View.INVISIBLE);
                mList.setVisibility(View.VISIBLE);
                if(sdhttpserviceoutput.childs.size() > 0)
                {
                    mData = (BusRoutesServiceOutput)sdhttpserviceoutput.childs.get(0);
                    if(mData != null)
                    {
                        mImageButtonMap.setEnabled(true);
                        checkSelectedRoute();
                        if(mData.arrayOfRoutes.size() <= 1) {
                            mButtonRoutes.setVisibility(View.INVISIBLE);
                            return;
                        }
                        mButtonRoutes.setVisibility(View.VISIBLE);
                        mButtonRoutes.setText((new StringBuilder()).append("Routes (").append(mData.arrayOfRoutes.size()).append(")").toString());
                    }
                }
                return;
            }

        };

        mService.executeAsync();
    }

    private void downloadBusStopInfo(JourneyPointDetail journeypointdetail, BusRouteCell busroutecell)
    {
        final BusRouteCell cell = busroutecell;
        (new BusArrivalServiceV2(null) {

            public void onSuccess(Object obj)
            {
                onSuccess((SDHttpServiceOutput)obj);
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                if(sdhttpserviceoutput.childs.size() > 0)
                {
                    cell.busInfoData = (BusArrivalServiceOutputV2)sdhttpserviceoutput.childs.get(0);
                    mAdapter.notifyDataSetChanged();
                }
            }

        }).executeAsync();
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
        busNumber = intent.getStringExtra("bus_number");
        mCurrentBusStopId = intent.getStringExtra("bus_stop_id");
        mTextViewHeaderTitle.setText((new StringBuilder()).append("Bus ").append(busNumber).toString());
        mAdapter = new SanListViewAdapter(this, 0, mBusRouteData);
        mAlternateCellAdapter = new SanListViewAdapter(this, 0, mAlternateRouteData);
        mList.setAdapter(mAdapter);
        mListViewRoutes.setAdapter(mAlternateCellAdapter);
        downloadBusRoutes();
    }

    private void initEvent()
    {
        mList.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView adapterview, View view, int i, long l)
            {
                BusRouteCell temp = (BusRouteCell)(SanListViewItem)adapterview.getItemAtPosition(i);
                double d = ((BusRouteCell) (temp)).data.longitude;
                double d1 = ((BusRouteCell) (temp)).data.latitude;
                Intent intent = new Intent(BusRouteActivity.this, BusRouteMapActivity.class);
                intent.putExtra("data", (new Gson()).toJson(mData));
                intent.putExtra("busNumber", busNumber);
                intent.putExtra("longitude", d);
                intent.putExtra("latitude", d1);
                intent.putExtra("selectedRouteIndex", mSelectedRouteIndex);
                intent.putExtra("stepIndex", i);
                startActivityForResult(intent, 0);
            }

        });
        mButtonBack.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                finish();
            }

        });
        mButtonRoutes.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                if(mLayoutAlternateRoutes.getVisibility() != 0)
                {
                    mLayoutAlternateRoutes.setVisibility(View.VISIBLE);
                    return;
                } else
                {
                    mLayoutAlternateRoutes.setVisibility(View.INVISIBLE);
                    return;
                }
            }

        });
        mListViewRoutes.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView adapterview, View view, int i, long l)
            {
                mLayoutAlternateRoutes.setVisibility(View.INVISIBLE);
                mSelectedRouteIndex = i;
                populateAlternateRoute();
                populateListView();
            }

        });
        mImageButtonMap.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(BusRouteActivity.this, BusRouteMapActivity.class);
                Gson gson = new Gson();
                JourneyPointDetail journeypointdetail = (JourneyPointDetail) ((ArrayList) mData.arrayOfArrayOfPointDetail.get(mSelectedRouteIndex)).get(0);
                intent.putExtra("data", gson.toJson(mData));
                intent.putExtra("busNumber", busNumber);
                intent.putExtra("longitude", journeypointdetail.longitude);
                intent.putExtra("latitude", journeypointdetail.latitude);
                intent.putExtra("selectedRouteIndex", mSelectedRouteIndex);
                intent.putExtra("stepIndex", 0);
                startActivity(intent);
            }

        });
    }

    private void initLayout()
    {
        mImageButtonMap = (ImageButton)findViewById(R.id.imageButtonMap);
        mTextViewHeaderTitle = (TextView)findViewById(R.id.textViewHeaderTitle);
        mButtonRoutes = (Button)findViewById(R.id.buttonRoutes);
        mButtonBack = (Button)findViewById(R.id.BackButton);
        mTextViewBusServices = (TextView)findViewById(R.id.textViewBusServices);
        mTextViewDirection = (TextView)findViewById(R.id.textViewDirection);
        mList = (ListView)findViewById(R.id.list);
        mLayoutLoading = (FrameLayout)findViewById(R.id.layoutLoading);
        mLayoutAlternateRoutes = (FrameLayout)findViewById(R.id.layoutAlternateRoutes);
        mListViewRoutes = (ListView)findViewById(R.id.listViewRoutes);
        mSdMobViewHolder = (RelativeLayout)findViewById(R.id.view_sdmob);
        mLayoutLoading.setVisibility(View.VISIBLE);
        mButtonRoutes.setVisibility(View.INVISIBLE);
        mList.setVisibility(View.INVISIBLE);
        mImageButtonMap.setEnabled(false);
    }

    private void initialize()
    {
        initLayout();
        initData();
        initEvent();
    }

    private void populateAlternateRoute()
    {
        mAlternateCellAdapter.clear();
        int i = 0;
        while(i < mData.arrayOfSummary.size())
        {
            BusRouteSummary busroutesummary = new BusRouteSummary(((BusRouteSummary)mData.arrayOfSummary.get(i)).hashData);
            busroutesummary.populateData();
            BusRouteAlternate temp = new BusRouteAlternate();
            temp.title = busroutesummary.title;
            temp.direction = busroutesummary.direction;
            temp.start = busroutesummary.start;
            temp.end = busroutesummary.end;
            BusRouteAlternateCell temp2 = new BusRouteAlternateCell(((BusRouteAlternate) (temp)));
            if(i == mSelectedRouteIndex)
            {
                temp2.routeChecked = true;
                mTextViewBusServices.setText(busroutesummary.start);
                mTextViewDirection.setText(busroutesummary.end);
            } else
            {
                temp2.routeChecked = false;
            }
            mAlternateCellAdapter.add(temp2);
            i++;
        }
    }

    private void populateListView()
    {
        int i = 0;
        int j = 0;
        mAdapter.clear();
        for(Iterator iterator = ((ArrayList)mData.arrayOfArrayOfPointDetail.get(mSelectedRouteIndex)).iterator(); iterator.hasNext();)
        {
            final JourneyPointDetail final_journeypointdetail = (JourneyPointDetail)iterator.next();
            final BusRouteCell cell = new BusRouteCell(final_journeypointdetail) {

                protected void onButtonRefreshClicked()
                {
                    super.onButtonRefreshClicked();
                    downloadBusStopInfo(final_journeypointdetail, this);
                }

                protected void onToggleInfoClicked()
                {
                    super.onToggleInfoClicked();
                    downloadBusStopInfo(final_journeypointdetail, this);
                    mAdapter.notifyDataSetChanged();
                }
            };
            mBusRouteData.add(cell);
            String s = URLFactory.createURLMapImage(final_journeypointdetail.longitude, final_journeypointdetail.latitude, 100, 60, 12);
            imagePool.queueRequest(URLFactory.createURLResizeImage(s, 130, 130), 130, 130, new streetdirectory.mobile.service.SDHttpImageServicePool.OnBitmapReceivedListener() {

                public void bitmapReceived(String s1, Bitmap bitmap)
                {
                    cell.mapImage = bitmap;
                    notifyAdapter();
                }

            }
);
            if(final_journeypointdetail.title.equals(mCurrentBusStopId))
            {
                j = i;
                cell.cellSelected = true;
            }
            i++;
        }

        mAdapter.notifyDataSetChanged();
        setSelectedPosition(j);
    }

    private void setSelectedPosition(int i)
    {
        mList.requestFocus();
        mList.setSelection(i);
    }

    private void updateSmallBanner()
    {
        mCurrentSmallBanner = SmallBanner.getBannerFromSdMobUnit(this, SdMobHelper.getInstance(this).getSdMobUnit(SdMob.ad_bnr_bus_route));
//        mCurrentSmallBanner.setAdMobSmallBannerListener(new streetdirectory.mobile.modules.sdmob.SmallBanner.AdMobSmallBannerListener());
        View view = mCurrentSmallBanner.getView(this);
        mSdMobViewHolder.removeAllViews();
        mSdMobViewHolder.addView(view, new RelativeLayout.LayoutParams(-1, -1));
    }

    protected void notifyAdapter() {
        mList.post(new Runnable() {

            public void run() {
                mAdapter.notifyDataSetChanged();
            }

        });
    }

    protected void onActivityResult(int i, int j, Intent intent)
    {
        onActivityResult(i, j, intent);
        if(j == -1 && i == 0 && intent != null)
        {
            mCurrentBusStopId = intent.getStringExtra("bus_stop_id");
            j = intent.getIntExtra("bus_alternate_route_index", 0);
            if(mData == null)
            {
                downloadBusRoutes();
            } else
            {
                for(i = 0; i < mAdapter.getCount(); i++)
                {
                    SanListViewItem temp = (SanListViewItem)mAdapter.getItem(i);
                    if(temp instanceof BusRouteCell)
                        ((BusRouteCell)temp).cellSelected = false;
                }

                mAdapter.notifyDataSetChanged();
                i = 0;
                if(mSelectedRouteIndex == j)
                {
                    Iterator iterator = ((ArrayList)mData.arrayOfArrayOfPointDetail.get(mSelectedRouteIndex)).iterator();
                    while(iterator.hasNext())
                    {
                        if(((JourneyPointDetail)iterator.next()).title.equals(mCurrentBusStopId))
                        {
                            ((BusRouteCell)mAdapter.getItem(i)).cellSelected = true;
                            mAdapter.notifyDataSetChanged();
                            setSelectedPosition(i);
                        }
                        i++;
                    }
                } else
                {
                    mSelectedRouteIndex = j;
                    checkSelectedRoute();
                    return;
                }
            }
        }
    }

    protected void onCreate(Bundle bundle)
    {
        onCreate(bundle);
        setContentView(R.layout.bus_routes_layout);
        initialize();
        updateSmallBanner();
        LocalBroadcastManager.getInstance(this).registerReceiver(mSdMobReceiver, new IntentFilter("sdmob_broadcast"));
    }

    public static final int BUS_ROUTE = 0;
    public static final String EXTRAS_BUSSTOP_ID = "bus_stop_id";
    public static final String EXTRAS_BUS_NUMBER = "bus_number";
    public static final String EXTRAS_COUNTRY_CODE = "country_code";
    public static final String EXTRAS_INITIAL_BUS_STOP = "initial_bus_stop";
    public static final String EXTRAS_INITIAL_DIRECTION = "initial_direction";
    private int adRequestRetryCount;
    private String busNumber;
    SDHttpImageServicePool imagePool;
    private SanListViewAdapter mAdapter;
    private SanListViewAdapter mAlternateCellAdapter;
    private ArrayList mAlternateRouteData;
    private ArrayList mBusRouteData;
    private Button mButtonBack;
    private Button mButtonRoutes;
    private String mCurrentBusStopId;
    private SmallBanner mCurrentSmallBanner;
    private BusRoutesServiceOutput mData;
    private ImageButton mImageButtonMap;
    private FrameLayout mLayoutAlternateRoutes;
    private FrameLayout mLayoutLoading;
    private ListView mList;
    private ListView mListViewRoutes;
    private BroadcastReceiver mSdMobReceiver;
    private RelativeLayout mSdMobViewHolder;
    private int mSelectedRouteIndex;
    private BusRoutesService mService;
    private TextView mTextViewBusServices;
    private TextView mTextViewDirection;
    private TextView mTextViewHeaderTitle;

    static
    {
        SanListViewItem.addTypeCount(BusRouteCell.class);
        SanListViewItem.addTypeCount(BusRouteAlternateCell.class);
    }



/*
    static BusRoutesServiceOutput access$002(BusRouteActivity busrouteactivity, BusRoutesServiceOutput busroutesserviceoutput)
    {
        busrouteactivity.mData = busroutesserviceoutput;
        return busroutesserviceoutput;
    }

*/








/*
    static int access$1402(BusRouteActivity busrouteactivity, int i)
    {
        busrouteactivity.adRequestRetryCount = i;
        return i;
    }

*/


/*
    static int access$1408(BusRouteActivity busrouteactivity)
    {
        int i = busrouteactivity.adRequestRetryCount;
        busrouteactivity.adRequestRetryCount = i + 1;
        return i;
    }

*/



/*
    static int access$202(BusRouteActivity busrouteactivity, int i)
    {
        busrouteactivity.mSelectedRouteIndex = i;
        return i;
    }

*/







}
