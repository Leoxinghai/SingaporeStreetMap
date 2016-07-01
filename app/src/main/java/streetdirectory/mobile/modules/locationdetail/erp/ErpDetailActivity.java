// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.locationdetail.erp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import streetdirectory.mobile.core.SDLogger;
import streetdirectory.mobile.modules.SDActivity;
import streetdirectory.mobile.modules.core.LocationBusinessServiceOutput;
import streetdirectory.mobile.modules.direction.DirectionActivity;
import streetdirectory.mobile.modules.locationdetail.erp.service.ERPDetailService;
import streetdirectory.mobile.modules.locationdetail.erp.service.ERPDetailServiceInput;
import streetdirectory.mobile.modules.locationdetail.erp.service.ERPDetailServiceOutput;
import streetdirectory.mobile.modules.locationdetail.erp.service.ERPVehicleInfo;
import streetdirectory.mobile.modules.mappreview.MapPreviewActivity;
import streetdirectory.mobile.modules.tips.TipsActivity;
import streetdirectory.mobile.service.*;

// Referenced classes of package streetdirectory.mobile.modules.locationdetail.erp:
//            VehicleListAdapter, ErpDetailAdapter

public class ErpDetailActivity extends SDActivity
{

    public ErpDetailActivity()
    {
        imagePool = new SDHttpImageServicePool();
    }

    private void abortAllDownload()
    {
        abortDownloadERP();
    }

    private void abortDownloadERP()
    {
        if(mERPDetailService != null)
        {
            mERPDetailService.abort();
            mERPDetailService = null;
        }
    }

    private void downloadERPDetail()
    {
        if(mERPDetailService == null)
        {
            abortDownloadERP();
            mERPDetailService = new ERPDetailService(new ERPDetailServiceInput(mErpID, mZone)) {

                public void onAborted(Exception exception)
                {
                    SDLogger.info("Tips Aborted");
                    mERPDetailService = null;
                }

                public void onFailed(Exception exception)
                {
                    SDLogger.printStackTrace(exception, "ERP download failed");
                    mERPDetailService = null;
                    mERPDetailService = null;
                }

                public void onReceiveTotal(long l)
                {
                    mERPDetailAdapter.total = (int)l;
                }

                public void onSuccess(Object obj)
                {
                    onSuccess((SDHttpServiceOutput)obj);
                }

                public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
                {
                    mERPDetailService = null;
                    if(sdhttpserviceoutput.childs.size() > 0)
                    {
                        mERPDetailAdapter.removeAllData();
                        ERPDetailServiceOutput erpdetailserviceoutput;
                        for(Iterator iterator = sdhttpserviceoutput.childs.iterator(); iterator.hasNext(); mERPDetailAdapter.add(erpdetailserviceoutput))
                            erpdetailserviceoutput = (ERPDetailServiceOutput)iterator.next();

                        mERPDetailAdapter.mDay = 0;
                        mERPDetailAdapter.mVehicleIndex = 0;
                        mERPDetailAdapter.notifyDataSetChanged();
                        SDLogger.debug("success download erp detail");
                        mVehicleListAdapter.clear();
                        mVehicleListAdapter.notifyDataSetChanged();
                        ArrayList temp = ((ERPDetailServiceOutput)sdhttpserviceoutput.childs.get(0)).arrayOfVehicleInfo;
                        if(temp != null)
                        {
                            ERPVehicleInfo erpvehicleinfo;
                            Iterator iterator;
                            for(iterator = temp.iterator(); iterator.hasNext(); mVehicleListAdapter.add(erpvehicleinfo))
                                erpvehicleinfo = (ERPVehicleInfo)iterator.next();

                            mVehicleListAdapter.notifyDataSetChanged();
                        }
                    } else
                    {
                        SDLogger.debug("failed download erp detail");
                    }
                    mERPDetailService = null;
                }

            };
            mERPDetailService.executeAsync();
        }
    }

    private void initData()
    {
        LocationBusinessServiceOutput locationbusinessserviceoutput = (LocationBusinessServiceOutput)getIntent().getParcelableExtra("data");
        if(locationbusinessserviceoutput != null)
        {
            mData = locationbusinessserviceoutput;
            Object obj = "";
            String s;
            TextView textview;
            if(locationbusinessserviceoutput.venue != null)
                obj = locationbusinessserviceoutput.venue;
            else
            if(locationbusinessserviceoutput.address != null)
                obj = locationbusinessserviceoutput.address;
            textview = mTextviewTitle;
            if(locationbusinessserviceoutput.venue != null)
                s = locationbusinessserviceoutput.venue;
            else
                s = "-";
            textview.setText(s);
            textview = mTextviewDetail;
            if(locationbusinessserviceoutput.address != null)
                s = locationbusinessserviceoutput.address;
            else
                s = "-";
            textview.setText(s);
            if(locationbusinessserviceoutput.imageURL != null)
                imagePool.queueRequest(URLFactory.createURLResizeImage(locationbusinessserviceoutput.imageURL, 130, 130), 130, 130, new streetdirectory.mobile.service.SDHttpImageServicePool.OnBitmapReceivedListener() {

                    public void bitmapReceived(String s1, Bitmap bitmap)
                    {
                        mButtonBusinessPhoto.setImageBitmap(bitmap);
                    }

                });
            mErpID = "";
            if(!"".equals(obj))
            {
                obj = Pattern.compile("ERP\\s\\d+").matcher(((CharSequence) (obj)));
                if(((Matcher) (obj)).find())
                {
                    obj = ((Matcher) (obj)).group();
                    mErpID = ((String) (obj)).substring(4, ((String) (obj)).length());
                    mZone = "";
                }
            }
        }
    }

    private void initEvent()
    {
        mBackButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                finish();
            }

        });
        mButtonWeekdays.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                mButtonWeekdays.setSelected(true);
                mButtonSaturday.setSelected(false);
                mERPDetailAdapter.mDay = 0;
                mERPDetailAdapter.notifyDataSetChanged();
            }

        });
        mButtonSaturday.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                mButtonWeekdays.setSelected(false);
                mButtonSaturday.setSelected(true);
                mERPDetailAdapter.mDay = 1;
                mERPDetailAdapter.notifyDataSetChanged();
            }

        });
        mSpinnerVehicle.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView adapterview, View view, int i, long l)
            {
                l = mSpinnerVehicle.getSelectedItemId();
                mERPDetailAdapter.mVehicleIndex = (int)l;
                mERPDetailAdapter.notifyDataSetChanged();
            }

            public void onNothingSelected(AdapterView adapterview)
            {
            }

        });
        mButtonDirection.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                Intent intent = new Intent(ErpDetailActivity.this, DirectionActivity.class);
                startActivity(intent);
            }
        });
        mButtonTips.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                startActivityTips();
            }

        });
        mButtonMap.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                startActivityMapPreview();
            }

        });
    }

    private void initLayout()
    {
        mMenuBar = (RelativeLayout)findViewById(R.id.MenuBar);
        mBackButton = (Button)findViewById(R.id.BackButton);
        mTitleBar = (TextView)findViewById(R.id.TitleBar);
        mShareButton = (Button)findViewById(R.id.ShareButton);
        mLayoutHeader = (RelativeLayout)findViewById(R.id.layout_header);
        mButtonBusinessPhoto = (ImageButton)findViewById(R.id.button_business_photo);
        mTextviewTitle = (TextView)findViewById(R.id.textview_title);
        mTextviewDetail = (TextView)findViewById(R.id.textview_detail);
        mLayoutHeaderButton = (LinearLayout)findViewById(R.id.layout_header_button);
        mButtonDirection = (Button)findViewById(R.id.button_direction);
        mButtonMap = (Button)findViewById(R.id.button_map);
        mButtonTips = (Button)findViewById(R.id.button_tips);
        mButtonWeekdays = (Button)findViewById(R.id.button_weekdays);
        mButtonWeekdays.setSelected(true);
        mButtonSaturday = (Button)findViewById(R.id.button_saturday);
        mListViewErpInfo = (ListView)findViewById(R.id.list_view_erp_info);
        mSpinnerVehicle = (Spinner)findViewById(R.id.spinner_vehicle);
        initListView();
        mVehicleListAdapter = new VehicleListAdapter(this);
        mSpinnerVehicle.setAdapter(mVehicleListAdapter);
    }

    private void initListView()
    {
        if(mListViewErpInfo != null)
        {
            mERPDetailAdapter = new ErpDetailAdapter(this);
            mListViewErpInfo.setAdapter(mERPDetailAdapter);
        }
    }

    private void initialize()
    {
        initLayout();
        initData();
        initEvent();
    }

    private void startActivityMapPreview()
    {
        Intent intent = new Intent(this, MapPreviewActivity.class);
        intent.putExtra("longitude", mData.longitude);
        intent.putExtra("latitude", mData.latitude);
        startActivity(intent);
    }

    private void startActivityTips()
    {
        Intent intent = new Intent(this, TipsActivity.class);
        intent.putExtra("countryCode", mData.countryCode);
        intent.putExtra("placeID", mData.placeID);
        intent.putExtra("addressID", mData.addressID);
        intent.putExtra("img", mData.imageURL);
        intent.putExtra("venue", mData.venue);
        intent.putExtra("type", 1);
        startActivity(intent);
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_location_detail_erp);
        initialize();
    }

    protected void onDestroy()
    {
        abortAllDownload();
        super.onDestroy();
    }

    protected void onResume()
    {
        super.onResume();
        downloadERPDetail();
    }

    private SDHttpImageServicePool imagePool;
    private Button mBackButton;
    private ImageButton mButtonBusinessPhoto;
    private Button mButtonDirection;
    private Button mButtonMap;
    private Button mButtonSaturday;
    private Button mButtonTips;
    private Button mButtonWeekdays;
    private LocationBusinessServiceOutput mData;
    private ErpDetailAdapter mERPDetailAdapter;
    private ERPDetailService mERPDetailService;
    private String mErpID;
    private RelativeLayout mLayoutHeader;
    private LinearLayout mLayoutHeaderButton;
    private ListView mListViewErpInfo;
    private RelativeLayout mMenuBar;
    private Button mShareButton;
    private Spinner mSpinnerVehicle;
    private TextView mTextviewDetail;
    private TextView mTextviewTitle;
    private TextView mTitleBar;
    private VehicleListAdapter mVehicleListAdapter;
    private String mZone;









/*
    static ERPDetailService access$702(ErpDetailActivity erpdetailactivity, ERPDetailService erpdetailservice)
    {
        erpdetailactivity.mERPDetailService = erpdetailservice;
        return erpdetailservice;
    }

*/

}
