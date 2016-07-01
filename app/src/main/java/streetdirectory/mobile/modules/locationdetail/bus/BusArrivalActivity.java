// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.locationdetail.bus;

import android.content.*;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.util.ArrayList;
import java.util.Iterator;
import streetdirectory.mobile.core.SDStory;
import streetdirectory.mobile.core.StringTools;
import streetdirectory.mobile.modules.SDActivity;
import streetdirectory.mobile.modules.core.LocationBusinessServiceOutput;
import streetdirectory.mobile.modules.locationdetail.bus.service.BusArrivalServiceInputV2;
import streetdirectory.mobile.modules.locationdetail.bus.service.BusArrivalServiceOutputV2;
import streetdirectory.mobile.modules.locationdetail.bus.service.BusArrivalServiceV2;
import streetdirectory.mobile.modules.locationdetail.bus.service.BusListService;
import streetdirectory.mobile.modules.locationdetail.bus.service.BusListServiceInput;
import streetdirectory.mobile.modules.locationdetail.bus.service.BusListServiceOutput;
import streetdirectory.mobile.modules.nearby.service.NearbyService;
import streetdirectory.mobile.modules.sdmob.SdMobHelper;
import streetdirectory.mobile.modules.sdmob.SmallBanner;
import streetdirectory.mobile.sd.SdMob;
import streetdirectory.mobile.service.*;

// Referenced classes of package streetdirectory.mobile.modules.locationdetail.bus:
//            BusArrivalAdapterV2, BusRouteActivity

public class BusArrivalActivity extends SDActivity
{
    public static class BusTimeHandler extends Handler
    {

        public Runnable r;
        public BusArrivalServiceV2 service;

        public BusTimeHandler()
        {
        }
    }


    public BusArrivalActivity()
    {
        busArrivals = new ArrayList();
        handlerList = new ArrayList();
        imagePool = new SDHttpImageServicePool();
        adRequestRetryCount = 0;
        mSdMobReceiver = new BroadcastReceiver() {

            public void onReceive(Context context, Intent intent)
            {
                updateSmallBanner();
            }

        };
    }

    private void abortServices()
    {
        BusTimeHandler bustimehandler;
        for(Iterator iterator = handlerList.iterator(); iterator.hasNext(); bustimehandler.removeCallbacks(bustimehandler.r))
        {
            bustimehandler = (BusTimeHandler)iterator.next();
            if(bustimehandler.service != null)
            {
                bustimehandler.service.abort();
                bustimehandler.service = null;
            }
        }

        if(_nearbyService != null)
        {
            _nearbyService.abort();
            _nearbyService = null;
        }
    }

    private void downloadBusList()
    {
        (new BusListService(new BusListServiceInput(busStopIdString)) {

            public void onFailed(Exception exception)
            {
                super.onFailed(exception);
            }

            public void onSuccess(Object obj)
            {
                onSuccess((SDHttpServiceOutput)obj);
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                mProgressBar.setVisibility(View.INVISIBLE);
                BusArrivalServiceOutputV2 busarrivalserviceoutputv2;
                Iterator iterator;
                for(iterator = sdhttpserviceoutput.childs.iterator(); iterator.hasNext(); downloadTime(busarrivalserviceoutputv2, null))
                {
                    BusListServiceOutput buslistserviceoutput = (BusListServiceOutput)iterator.next();
                    busarrivalserviceoutputv2 = new BusArrivalServiceOutputV2();
                    busarrivalserviceoutputv2.busNumber = buslistserviceoutput.busNumber;
                    busArrivals.add(busarrivalserviceoutputv2);
                    mBustArrivalAdapter.items.add(busarrivalserviceoutputv2);
                }

            }

        }).executeAsync();
    }

    private BusArrivalServiceV2 downloadTime(BusArrivalServiceOutputV2 busarrivalserviceoutputv2, BusTimeHandler bustimehandler)
    {
        final BusTimeHandler handle = bustimehandler;
        final BusArrivalServiceOutputV2 item = busarrivalserviceoutputv2;

        BusArrivalServiceV2 result = new BusArrivalServiceV2(null) {
            public void onFailed(Exception exception)
            {
                super.onFailed(exception);
                item.subsequentBus = "";
                item.nextBus = "";
                notifiyListView();
                downloadTimeDelayed(item, handle);
                if(handle != null)
                    handlerList.remove(handle);
            }

            public void onSuccess(Object obj)
            {
                onSuccess((SDHttpServiceOutput)obj);
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                if(sdhttpserviceoutput.childs.size() > 0)
                {
                    BusArrivalServiceOutputV2 temp = (BusArrivalServiceOutputV2)sdhttpserviceoutput.childs.get(0);
                    item.subsequentBus = ((BusArrivalServiceOutputV2) (temp)).subsequentBus;
                    item.nextBus = ((BusArrivalServiceOutputV2) (temp)).nextBus;
                } else
                {
                    item.subsequentBus = "";
                    item.nextBus = "";
                }
                notifiyListView();
                downloadTimeDelayed(item, handle);
                if(handle != null)
                    handlerList.remove(handle);
            }


        };
        result.executeAsync();
        return result;
    }

    private void downloadTimeDelayed(final BusArrivalServiceOutputV2 item, final BusTimeHandler busHandler)
    {
        final BusTimeHandler busHandler0 = busHandler;
        final BusArrivalServiceOutputV2 item0 = item;

        BusTimeHandler temp = new BusTimeHandler();
        temp.r = new Runnable() {

            public void run()
            {
                busHandler.service = downloadTime(item0, busHandler0);
            }
            {
            }
        };

        temp.postDelayed(busHandler.r, 30000L);
        handlerList.add(temp);
    }

    private void initData()
    {
        String s = "";
        LocationBusinessServiceOutput locationbusinessserviceoutput = (LocationBusinessServiceOutput)getIntent().getParcelableExtra("data");
        if(locationbusinessserviceoutput != null)
        {
            mData = locationbusinessserviceoutput;
            mTextviewTitle.setText(locationbusinessserviceoutput.venue);
            mTextviewDetail.setText(locationbusinessserviceoutput.address);
            if(locationbusinessserviceoutput.imageURL != null)
                imagePool.queueRequest(URLFactory.createURLResizeImage(locationbusinessserviceoutput.imageURL, 130, 130), 130, 130, new streetdirectory.mobile.service.SDHttpImageServicePool.OnBitmapReceivedListener() {

                    public void bitmapReceived(String s2, Bitmap bitmap)
                    {
                        mButtonBusinessPhoto.setImageBitmap(bitmap);
                    }

                });
            if(locationbusinessserviceoutput.uniqueID != null && locationbusinessserviceoutput.uniqueID.startsWith("B"))
            {
                s = locationbusinessserviceoutput.uniqueID.substring(1);
            } else
            {
                String s1 = "";
                s = s1;
                if(locationbusinessserviceoutput.address != null)
                {
                    s = s1;
                    if(locationbusinessserviceoutput.address.length() > 5)
                    {
                        s = s1;
                        if(locationbusinessserviceoutput.address != "No Address")
                            s = locationbusinessserviceoutput.address;
                    }
                }
                int i = s.indexOf(")");
                int j = s.indexOf("(B");
                if(j < 0)
                {
                    j = s.indexOf("B");
                    if(j >= 0)
                        s = s.substring(j + 1, i);
                } else
                {
                    s = s.substring(j + 2, i);
                }
            }
        }
        SDStory.post(URLFactory.createGantPlaceBusStop(s), SDStory.createDefaultParams());
        mBustArrivalAdapter = new BusArrivalAdapterV2(this);
        mBustArrivalAdapter.mData = busArrivals;
        mBustArrivalAdapter.items.addAll(busArrivals);
        mListViewBusArrival.setAdapter(mBustArrivalAdapter);
        busStopIdint = StringTools.tryParseInt(s, 0);
        busStopIdString = s;
        if(busStopIdString.matches("[0-9]+"))
        {
            downloadBusList();
            return;
        } else
        {
            mProgressBar.setVisibility(View.INVISIBLE);
            return;
        }
    }

    private void initEvent()
    {
        mRefreshButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                BusArrivalServiceOutputV2 busarrivalserviceoutputv2;
                Iterator iterator;
                for(iterator = busArrivals.iterator(); iterator.hasNext(); downloadTime(busarrivalserviceoutputv2, null))
                    busarrivalserviceoutputv2 = (BusArrivalServiceOutputV2)iterator.next();

            }

        });
        mBackButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                finish();
            }

        });
        mListViewBusArrival.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView adapterview, View view, int i, long l)
            {
                String temp = (new StringBuilder()).append("B").append(busStopIdString).toString();
                SDDataOutput temp2 = (SDDataOutput)(mBustArrivalAdapter.getItem(i));
                if(temp2 instanceof BusArrivalServiceOutputV2)
                {
                    temp2 = (BusArrivalServiceOutputV2)temp2;
                    Intent intent = new Intent(BusArrivalActivity.this, BusRouteActivity.class);
                    intent.putExtra("bus_number", ((BusArrivalServiceOutputV2) (temp2)).busNumber);
                    intent.putExtra("country_code", ((BusArrivalServiceOutputV2) (temp2)).countryCode);
                    intent.putExtra("bus_stop_id", temp);
                    startActivity(intent);
                }
            }

        });
    }

    private void initLayout()
    {
        mMenuBar = (RelativeLayout)findViewById(R.id.MenuBar);
        mBackButton = (Button)findViewById(R.id.BackButton);
        mTitleBar = (TextView)findViewById(R.id.TitleBar);
        mRefreshButton = (Button)findViewById(R.id.RefreshButton);
        mLayoutHeader = (RelativeLayout)findViewById(R.id.layout_header);
        mButtonBusinessPhoto = (ImageButton)findViewById(R.id.button_business_photo);
        mTextviewTitle = (TextView)findViewById(R.id.textview_title);
        mTextviewDetail = (TextView)findViewById(R.id.textview_detail);
        mLayoutHeaderButton = (LinearLayout)findViewById(R.id.layout_header_button);
        mButtonDirection = (Button)findViewById(R.id.button_direction);
        mButtonMap = (Button)findViewById(R.id.button_map);
        mButtonTips = (Button)findViewById(R.id.button_tips);
        mListViewBusArrival = (ListView)findViewById(R.id.list_view_bus_arrival);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar1);
        mSdMobViewHolder = (RelativeLayout)findViewById(R.id.view_sdmob);
        mButtonTips.setVisibility(View.INVISIBLE);
        mButtonMap.setVisibility(View.INVISIBLE);
        mButtonDirection.setVisibility(View.INVISIBLE);
        initListView();
    }

    private void initListView()
    {
        if(mListViewBusArrival != null)
        {
            mBustArrivalAdapter = new BusArrivalAdapterV2(this);
            mListViewBusArrival.setAdapter(mBustArrivalAdapter);
        }
    }

    private void initialize()
    {
        initLayout();
        initData();
        initEvent();
    }

    private void updateSmallBanner()
    {
        mCurrentSmallBanner = SmallBanner.getBannerFromSdMobUnit(this, SdMobHelper.getInstance(this).getSdMobUnit(SdMob.ad_bnr_bus_listing));
//        mCurrentSmallBanner.setAdMobSmallBannerListener(new streetdirectory.mobile.modules.sdmob.SmallBanner.AdMobSmallBannerListener() );
        View view = mCurrentSmallBanner.getView(this);
        mSdMobViewHolder.removeAllViews();
        mSdMobViewHolder.addView(view, new ViewGroup.LayoutParams(-1, -1));
    }

    protected void notifiyListView()
    {
        mListViewBusArrival.post(new Runnable() {

            public void run()
            {
                mBustArrivalAdapter.notifyDataSetChanged();
            }

        });
    }

    protected void onCreate(Bundle bundle)
    {
        onCreate(bundle);
        setContentView(R.layout.activity_location_detail_bus_stop);
        initialize();
        updateSmallBanner();
        LocalBroadcastManager.getInstance(this).registerReceiver(mSdMobReceiver, new IntentFilter("sdmob_broadcast"));
    }

    protected void onDestroy()
    {
        abortServices();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mSdMobReceiver);
        onDestroy();
    }

    protected void onPause()
    {
        abortServices();
        onPause();
    }

    protected void onResume()
    {
        for(Iterator iterator = busArrivals.iterator(); iterator.hasNext(); downloadTime((BusArrivalServiceOutputV2)iterator.next(), null));
        onResume();
    }

    private NearbyService _nearbyService;
    private int adRequestRetryCount;
    ArrayList busArrivals;
    private String busStopIdString;
    private int busStopIdint;
    private ArrayList handlerList;
    private SDHttpImageServicePool imagePool;
    private Button mBackButton;
    private BusArrivalAdapterV2 mBustArrivalAdapter;
    private ImageButton mButtonBusinessPhoto;
    private Button mButtonDirection;
    private Button mButtonMap;
    private Button mButtonTips;
    private SmallBanner mCurrentSmallBanner;
    LocationBusinessServiceOutput mData;
    private RelativeLayout mLayoutHeader;
    private LinearLayout mLayoutHeaderButton;
    private ListView mListViewBusArrival;
    private RelativeLayout mMenuBar;
    private ProgressBar mProgressBar;
    private Button mRefreshButton;
    private BroadcastReceiver mSdMobReceiver;
    private RelativeLayout mSdMobViewHolder;
    private TextView mTextviewDetail;
    private TextView mTextviewTitle;
    private TextView mTitleBar;











/*
    static int access$802(BusArrivalActivity busarrivalactivity, int i)
    {
        busarrivalactivity.adRequestRetryCount = i;
        return i;
    }

*/


/*
    static int access$808(BusArrivalActivity busarrivalactivity)
    {
        int i = busarrivalactivity.adRequestRetryCount;
        busarrivalactivity.adRequestRetryCount = i + 1;
        return i;
    }

*/
}
