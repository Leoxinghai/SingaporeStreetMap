// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.nearby;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.*;
import android.widget.*;
import java.util.ArrayList;
import java.util.Iterator;
import streetdirectory.mobile.core.*;
import streetdirectory.mobile.core.service.HttpImageService;
import streetdirectory.mobile.modules.SDActivity;
import streetdirectory.mobile.modules.businessdetail.BusinessDetailActivityV2;
import streetdirectory.mobile.modules.core.LocationBusinessServiceOutput;
import streetdirectory.mobile.modules.core.adapter.LocationBusinessTipsNearbyAdapter;
import streetdirectory.mobile.modules.imagepreview.LocationBusinessImagePreviewActivity;
import streetdirectory.mobile.modules.locationdetail.bus.BusArrivalActivity;
import streetdirectory.mobile.modules.locationdetail.businessin.BusinessInActivity;
import streetdirectory.mobile.modules.locationdetail.erp.ErpDetailActivity;
import streetdirectory.mobile.modules.locationdetail.expresswayexit.ExpressWayExitActivity;
import streetdirectory.mobile.modules.locationdetail.trafficcam.TrafficCameraLocationDetailActivity;
import streetdirectory.mobile.modules.map.MapActivity;
import streetdirectory.mobile.modules.nearby.category.NearbyCategoryActivity;
import streetdirectory.mobile.modules.nearby.category.NearbyCategoryAdapter;
import streetdirectory.mobile.modules.nearby.category.service.NearbyCategoryService;
import streetdirectory.mobile.modules.nearby.category.service.NearbyCategoryServiceOutput;
import streetdirectory.mobile.modules.nearby.service.NearbyService;
import streetdirectory.mobile.modules.nearby.service.NearbyServiceInput;
import streetdirectory.mobile.modules.nearby.service.NearbyServiceOutput;
import streetdirectory.mobile.sd.SDMapSideMenuLayout;
import streetdirectory.mobile.service.*;

import com.xinghai.mycurve.R;

public class NearbyActivityTwo extends SDActivity
{

    public NearbyActivityTwo()
    {
        mImageServices = new ArrayList();
        _imageServices = new ArrayList();
    }

    private void categoryButtonClicked()
    {
        Intent intent = new Intent(this, NearbyCategoryActivity.class);
        intent.putExtra("countryCode", mCountryCode);
        startActivityForResult(intent, 1);
        overridePendingTransition(0x10a0000, 0x10a0001);
    }

    private void downloadFacebookPhoto(NearbyServiceOutput nearbyserviceoutput, int i, int j)
    {
        final NearbyServiceOutput data = nearbyserviceoutput;
        Iterator iterator = mImageServices.iterator();
        for(;iterator.hasNext();) {
            if (((HttpImageService) iterator.next()).tag.equals(nearbyserviceoutput.tipsUserID))
                return;
        }

        final String final_s = nearbyserviceoutput.generateUserPhotoURL(i, j);
        if(final_s != null)
        {
            SDFacebookImageService temp = new SDFacebookImageService(final_s, i, j) {

                public void onAborted(Exception exception)
                {
                    mImageServices.remove(this);
                }

                public void onFailed(Exception exception)
                {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.facebook_thumb);
                    mListAdapter.putImage(data.tipsUserID, bitmap);
                    mImageServices.remove(this);
                    mListAdapter.notifyDataSetChanged();
                }

                public void onSuccess(Bitmap bitmap)
                {
                    mListAdapter.putImage(data.tipsUserID, bitmap);
                    mImageServices.remove(this);
                    mListAdapter.notifyDataSetChanged();
                }

                public void onSuccess(Object obj)
                {
                    onSuccess((Bitmap)obj);
                }

            };
            temp.tag = nearbyserviceoutput.tipsUserID;
            mImageServices.add(temp);
            temp.executeAsync();
            return;
        }

    }

    private void downloadImage(NearbyServiceOutput nearbyserviceoutput, int i, int j)
    {
        final NearbyServiceOutput data = nearbyserviceoutput;
        Iterator iterator = mImageServices.iterator();
        for(;iterator.hasNext();) {
            if (((HttpImageService) iterator.next()).tag.equals(nearbyserviceoutput.uniqueID))
                return;
        }

        final String final_s = nearbyserviceoutput.generateImageURL(this, i, j);
        if(final_s != null)
        {
            SDHttpImageService temp = new SDHttpImageService(final_s) {

                public void onAborted(Exception exception)
                {
                    mImageServices.remove(this);
                }

                public void onFailed(Exception exception)
                {
                    mImageServices.remove(this);
                }

                public void onSuccess(Bitmap bitmap)
                {
                    mListAdapter.putImage(data.uniqueID, bitmap);
                    mImageServices.remove(this);
                    mListAdapter.notifyDataSetChanged();
                }

                public void onSuccess(Object obj)
                {
                    onSuccess((Bitmap)obj);
                }

            };

            temp.tag = nearbyserviceoutput.uniqueID;
            mImageServices.add(temp);
            temp.executeAsync();
            return;
        }

    }

    private void downloadNearbyData()
    {
        if(mService == null)
        {
            mService = new NearbyService(new NearbyServiceInput(mCountryCode, mType, mLongitude, mLatitude, mKmRange, mListAdapter.getItemSize(), mLimit, mCategoryID, mCategoryType)) {

                public void onAborted(Exception exception)
                {
                    mService = null;
                    SDLogger.info("Nearby Aborted");
                    mLoadingIndicator.setVisibility(View.INVISIBLE);
                }

                public void onFailed(Exception exception)
                {
                    mService = null;
                    SDLogger.printStackTrace(exception, "Nearby Failed");
                    mLoadingIndicator.setVisibility(View.INVISIBLE);
                    if(mListAdapter.getItemSize() == 0)
                        mNoDataLayout.setVisibility(View.VISIBLE);
                }

                public void onReceiveData(NearbyServiceOutput nearbyserviceoutput)
                {
                    mListAdapter.addItem(nearbyserviceoutput);
                }

                public void onReceiveData(SDDataOutput sddataoutput)
                {
                    onReceiveData((NearbyServiceOutput)sddataoutput);
                }

                public void onReceiveTotal(long l)
                {
                    mListAdapter.setTotalItem(l);
                }

                public void onSuccess(Object obj)
                {
                    onSuccess((SDHttpServiceOutput)obj);
                }

                public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
                {
                    mService = null;
                    SDLogger.info("Nearby Success");
                    mLoadingIndicator.setVisibility(View.INVISIBLE);
                    mListAdapter.notifyDataSetChanged();
                    if(mListAdapter.getItemSize() == 0)
                        mNoDataLayout.setVisibility(View.VISIBLE);
                }

            };
            if(mListAdapter.getItemSize() == 0)
                mLoadingIndicator.setVisibility(View.VISIBLE);
            mService.executeAsync();
        }
    }

    private void hideShowArrow(String s)
    {
        mImageArrowBusButton = (ImageView)mLayoutBoxButtonMrt.findViewById(R.id.imageViewArrow);
        mImageArrowBusinessButton = (ImageView)mLayoutBoxButtonBusiness.findViewById(R.id.imageViewArrow);
        mImageArrowPlaceButton = (ImageView)mLayoutBoxButtonPlace.findViewById(R.id.imageViewArrow);
        mImageArrowFoodButton = (ImageView)mLayoutBoxButtonFood.findViewById(R.id.imageViewArrow);
        mImageArrowMoreButton = (ImageView)mLayoutBoxButtonMore.findViewById(R.id.imageViewArrow);
        if(s.equals("places"))
        {
            mImageArrowPlaceButton.setVisibility(View.VISIBLE);
            mImageArrowBusinessButton.setVisibility(View.INVISIBLE);
            mImageArrowBusButton.setVisibility(View.INVISIBLE);
            mImageArrowFoodButton.setVisibility(View.INVISIBLE);
            mImageArrowMoreButton.setVisibility(View.INVISIBLE);
        } else
        {
            if(s.equals("mrt"))
            {
                mImageArrowPlaceButton.setVisibility(View.INVISIBLE);
                mImageArrowBusinessButton.setVisibility(View.INVISIBLE);
                mImageArrowBusButton.setVisibility(View.VISIBLE);
                mImageArrowFoodButton.setVisibility(View.INVISIBLE);
                mImageArrowMoreButton.setVisibility(View.INVISIBLE);
                return;
            }
            if(s.equals("businesses"))
            {
                mImageArrowPlaceButton.setVisibility(View.INVISIBLE);
                mImageArrowBusinessButton.setVisibility(View.VISIBLE);
                mImageArrowBusButton.setVisibility(View.INVISIBLE);
                mImageArrowFoodButton.setVisibility(View.INVISIBLE);
                mImageArrowMoreButton.setVisibility(View.INVISIBLE);
                return;
            }
            if(s.equals("food"))
            {
                mImageArrowPlaceButton.setVisibility(View.INVISIBLE);
                mImageArrowBusinessButton.setVisibility(View.INVISIBLE);
                mImageArrowBusButton.setVisibility(View.INVISIBLE);
                mImageArrowFoodButton.setVisibility(View.VISIBLE);
                mImageArrowMoreButton.setVisibility(View.INVISIBLE);
                return;
            }
            if(s.equals("more"))
            {
                mImageArrowPlaceButton.setVisibility(View.INVISIBLE);
                mImageArrowBusinessButton.setVisibility(View.INVISIBLE);
                mImageArrowBusButton.setVisibility(View.INVISIBLE);
                mImageArrowFoodButton.setVisibility(View.INVISIBLE);
                mImageArrowMoreButton.setVisibility(View.VISIBLE);
                return;
            }
        }
    }

    private void initData()
    {
        loadCategory();
        Intent intent = getIntent();
        mCountryCode = intent.getStringExtra("countryCode");
        mType = intent.getIntExtra("type", mType);
        mLongitude = intent.getDoubleExtra("longitude", 0.0D);
        mLatitude = intent.getDoubleExtra("latitude", 0.0D);
        mKmRange = intent.getIntExtra("kmRange", 3);
        if(mKmRangeButton != null)
            mKmRangeButton.setText((new StringBuilder()).append("Within ").append(mKmRange).append(" Km").toString());
        mLimit = intent.getIntExtra("limit", 10);
        mCategoryID = intent.getIntExtra("categoryID", mCategoryID);
        mCategoryType = intent.getIntExtra("categoryType", mCategoryType);
        mListAdapter = new LocationBusinessTipsNearbyAdapter(this, mKmRange);
        if(mListView != null)
            mListView.setAdapter(mListAdapter);
    }

    private void initEvent()
    {
        mLayoutBoxButtonMrt.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                hideShowArrow("mrt");
                setGraySelected(mLayoutBoxButtonMrtGray);
                mCategoryID = 11;
                mCategoryType = 1;
                mType = 1;
                saveCategory(mCategoryID, mCategoryType, mType, "Bus Time");
                refreshList();
            }

        });
        mLayoutBoxButtonBusiness.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                hideShowArrow("businesses");
                setGraySelected(mLayoutBoxButtonBusinessGray);
                mCategoryID = 0;
                mCategoryType = 1;
                mType = 2;
                saveCategory(mCategoryID, mCategoryType, mType, "Businesses");
                refreshList();
            }

        });
        mLayoutBoxButtonPlace.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                hideShowArrow("places");
                setGraySelected(mLayoutBoxButtonPlaceGray);
                mCategoryID = 0;
                mCategoryType = 1;
                mType = 1;
                saveCategory(mCategoryID, mCategoryType, mType, "Places");
                refreshList();
            }

        });
        mLayoutBoxButtonFood.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                hideShowArrow("food");
                setGraySelected(mLayoutBoxButtonFoodGray);
                mCategoryID = 15;
                mCategoryType = 1;
                mType = 2;
                saveCategory(mCategoryID, mCategoryType, mType, "Food");
                refreshList();
            }

        });
        mLayoutBoxButtonMore.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                hideShowArrow("more");
                setGraySelected(mLayoutBoxButtonMoreGray);
                Intent intent = new Intent(NearbyActivityTwo.this, NearbyCategoryActivity.class);
                intent.putExtra("countryCode", mCountryCode);
                startActivityForResult(intent, 1);
                overridePendingTransition(0x10a0000, 0x10a0001);
            }

        });
        mSideMenuLayout.setOnSlideOpen(new Action() {

            public void execute()
            {
            }

        });
        mSideMenuLayout.setOnSlideClose(new Action() {

            public void execute()
            {
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
        mListAdapter.setOnImageNotFoundListener(new streetdirectory.mobile.modules.core.adapter.LocationBusinessAdapter.OnImageNotFoundListener() {

            public void onImageNotFound(LocationBusinessServiceOutput locationbusinessserviceoutput, int i, int j, int k)
            {
                downloadImage((NearbyServiceOutput)locationbusinessserviceoutput, j, k);
            }
        });
        mListAdapter.setOnLoadMoreListener(new streetdirectory.mobile.modules.core.adapter.LocationBusinessAdapter.OnLoadMoreListener() {

            public void onLoadMoreList()
            {
                downloadNearbyData();
            }

        });
        mListAdapter.setOnTipsPhotoNotFoundListener(new streetdirectory.mobile.modules.core.adapter.LocationBusinessTipsAdapter.OnTipsPhotoNotFoundListener() {

            public void onTipsPhotoNotFound(int i, int j, int k)
            {
                downloadFacebookPhoto((NearbyServiceOutput)mListAdapter.getItem(i), j, k);
            }

        });
        mListAdapter.setOnImageClickedListener(new streetdirectory.mobile.modules.core.adapter.LocationBusinessAdapter.OnImageClickedListener() {

            public void onImageClicked(LocationBusinessServiceOutput locationbusinessserviceoutput, int i, Bitmap bitmap)
            {
                locationBusinessImageClicked(locationbusinessserviceoutput, bitmap);
            }

        });
        mListAdapter.setOnKmRangeClickListener(new streetdirectory.mobile.modules.core.adapter.LocationBusinessTipsNearbyAdapter.OnKmRangeClickListener() {

            public void onKmRangeClicked()
            {
                kmRangeButtonClicked();
            }

        });
        mListView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView adapterview, View view, int i, long l)
            {
                if(mListAdapter.getItemSize() != i)
                {
                    LocationBusinessServiceOutput temp = mListAdapter.getItem(i);
                    Intent intent = new Intent(NearbyActivityTwo.this, MapActivity.class);
                    intent.putExtra("data", (Parcelable)adapterview);
                    intent.putExtra("pinLongitude", ((LocationBusinessServiceOutput) (temp)).longitude);
                    intent.putExtra("pinLatitude", ((LocationBusinessServiceOutput) (temp)).latitude);
                    intent.putExtra("pinTitle", ((LocationBusinessServiceOutput) (temp)).venue);
                    if(((LocationBusinessServiceOutput) (temp)).type == 2)
                        intent.putExtra("pinActivityClass", BusinessDetailActivityV2.class);
                    else
                    if(((LocationBusinessServiceOutput) (temp)).categoryID == 1118)
                        intent.putExtra("pinActivityClass", TrafficCameraLocationDetailActivity.class);
                    else
                    if(((LocationBusinessServiceOutput) (temp)).categoryID == 93)
                        intent.putExtra("pinActivityClass", BusArrivalActivity.class);
                    else
                    if(((LocationBusinessServiceOutput) (temp)).categoryID == 29)
                        intent.putExtra("pinActivityClass", ExpressWayExitActivity.class);
                    else
                    if(((LocationBusinessServiceOutput) (temp)).categoryID == 28)
                        intent.putExtra("pinActivityClass", ErpDetailActivity.class);
                    else
                        intent.putExtra("pinActivityClass", BusinessInActivity.class);
                    startActivity(intent);
                }
            }

        });
        mKmRangeButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                kmRangeButtonClicked();
            }

        });
    }

    private void initLayout()
    {
        mSideMenuLayout = (SDMapSideMenuLayout)findViewById(R.id.side_menu);
        mMenuButton = (ImageButton)findViewById(R.id.MenuButton);
        mLoadingIndicator = (ProgressBar)findViewById(R.id.LoadingIndicator);
        mListView = (ListView)findViewById(R.id.ListView);
        mNoDataLayout = (LinearLayout)findViewById(R.id.NoDataLayout);
        mKmRangeButton = (Button)mNoDataLayout.findViewById(R.id.KmRangeButton);
        mLayoutBoxButtonBusiness = findViewById(R.id.layout_box_button_businesses);
        mLayoutBoxButtonBusinessGray = mLayoutBoxButtonBusiness.findViewById(R.id.box_grey);
        mLayoutBoxButtonPlace = findViewById(R.id.layout_box_button_places);
        mLayoutBoxButtonPlaceGray = mLayoutBoxButtonPlace.findViewById(R.id.box_grey);
        mLayoutBoxButtonFood = findViewById(R.id.layout_box_button_food);
        mLayoutBoxButtonFoodGray = mLayoutBoxButtonFood.findViewById(R.id.box_grey);
        mLayoutBoxButtonMrt = findViewById(R.id.layout_box_button_mrt);
        mLayoutBoxButtonMrtGray = mLayoutBoxButtonMrt.findViewById(R.id.box_grey);
        mLayoutBoxButtonMore = findViewById(R.id.layout_box_button_more);
        mLayoutBoxButtonMoreGray = mLayoutBoxButtonMore.findViewById(R.id.box_grey);
        mTextViewMrtButton = (TextView)mLayoutBoxButtonMrt.findViewById(R.id.textViewCategory);
        mTextViewPlaceButton = (TextView)mLayoutBoxButtonPlace.findViewById(R.id.textViewCategory);
        mTextViewBusinessButton = (TextView)mLayoutBoxButtonBusiness.findViewById(R.id.textViewCategory);
        mTextViewFoodButton = (TextView)mLayoutBoxButtonFood.findViewById(R.id.textViewCategory);
        mTextViewMoreButton = (TextView)mLayoutBoxButtonMore.findViewById(R.id.textViewCategory);
        mTextViewMrtButton.setText("MRT");
        mTextViewPlaceButton.setText("Place");
        mTextViewBusinessButton.setText("Business");
        mTextViewFoodButton.setText("Food");
        mTextViewMoreButton.setText("More");
        setGraySelected(mLayoutBoxButtonPlaceGray);
        hideShowArrow("places");
        mTextBusinessCount = (TextView)mLayoutBoxButtonBusiness.findViewById(R.id.textViewCount);
        mTextMoreCount = (TextView)mLayoutBoxButtonMore.findViewById(R.id.textViewCount);
        mTextFoodCount = (TextView)mLayoutBoxButtonFood.findViewById(R.id.textViewCount);
        mTextPlaceCount = (TextView)mLayoutBoxButtonPlace.findViewById(R.id.textViewCount);
        mTextBusCount = (TextView)mLayoutBoxButtonMrt.findViewById(R.id.textViewCount);
        mTextBusinessCount.setVisibility(4);
        mTextMoreCount.setVisibility(4);
        mTextFoodCount.setVisibility(4);
        mTextPlaceCount.setVisibility(4);
        mTextBusCount.setVisibility(4);
    }

    private void initialize()
    {
        initLayout();
        initData();
        initEvent();
    }

    private void kmRangeButtonClicked()
    {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Select Range");
        final int lastKmRange = mKmRange;
        int i = mKmRange;
        android.content.DialogInterface.OnClickListener onclicklistener = new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialoginterface, int j)
            {
                mKmRange = j + 1;
            }

        };
        builder.setSingleChoiceItems(new String[] {
            "1 Km", "2 Km", "3 Km", "4 Km", "5 Km"
        }, i - 1, onclicklistener);
        builder.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialoginterface, int j)
            {
                if(mKmRange != lastKmRange)
                    refreshList();
            }

        });
        builder.setNegativeButton("Cancel", new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialoginterface, int j)
            {
                mKmRange = lastKmRange;
            }

        });
        builder.show();
    }

    private void loadCategory()
    {
        SDPreferences sdpreferences = SDPreferences.getInstance();
        mCategoryID = sdpreferences.getIntForKey("nearby_lastCategoryID", 0);
        mCategoryType = sdpreferences.getIntForKey("nearby_lastCategoryType", 1);
        mType = sdpreferences.getIntForKey("nearby_lastType", 1);
        sdpreferences.getStringForKey("nearby_lastName", "Places");
    }

    private void locationBusinessImageClicked(LocationBusinessServiceOutput locationbusinessserviceoutput, Bitmap bitmap)
    {
        Intent intent = new Intent(this, LocationBusinessImagePreviewActivity.class);
        intent.putExtra("thumbnail", bitmap);
        intent.putExtra("data", (Parcelable)locationbusinessserviceoutput);
        startActivity(intent);
        overridePendingTransition(0x10a0000, 0x10a0001);
    }

    private void refreshList()
    {
        mKmRangeButton.setText((new StringBuilder()).append("Within ").append(mKmRange).append(" Km").toString());
        mNoDataLayout.setVisibility(View.INVISIBLE);
        mListAdapter.setKmRange(mKmRange);
        mListAdapter.clear();
        abortAllProcess();
        downloadNearbyData();
    }

    private void saveCategory(int i, int j, int k, String s)
    {
        android.content.SharedPreferences.Editor editor = SDPreferences.getInstance().createEditor();
        editor.putInt("nearby_lastCategoryID", i);
        editor.putInt("nearby_lastCategoryType", j);
        editor.putInt("nearby_lastType", k);
        editor.putString("nearby_lastName", s);
        editor.commit();
    }

    private void setGraySelected(View view)
    {
        mLayoutBoxButtonBusinessGray.setSelected(false);
        mLayoutBoxButtonPlaceGray.setSelected(false);
        mLayoutBoxButtonMrtGray.setSelected(false);
        mLayoutBoxButtonFoodGray.setSelected(false);
        mLayoutBoxButtonMoreGray.setSelected(false);
        view.setSelected(true);
    }

    private void startActivityBusinessDetail(LocationBusinessServiceOutput locationbusinessserviceoutput)
    {
        Intent intent = new Intent(this, BusinessDetailActivityV2.class);
        intent.putExtra("data", (Parcelable)locationbusinessserviceoutput);
        startActivity(intent);
    }

    public void abortAllProcess()
    {
        if(mService != null)
        {
            mService.abort();
            mService = null;
        }
        for(Iterator iterator = mImageServices.iterator(); iterator.hasNext(); ((HttpImageService)iterator.next()).abort());
        mImageServices.clear();
    }

    protected void onActivityResult(int i, int j, Intent intent)
    {
        switch(i) {
            default:
                return;
            case 1:
                NearbyCategoryServiceOutput temp;
                if (j == -1 && (temp = (NearbyCategoryServiceOutput) intent.getParcelableExtra("selectedCategory")) != null) {
                    mCategoryID = ((NearbyCategoryServiceOutput) (temp)).categoryID;
                    mCategoryType = ((NearbyCategoryServiceOutput) (temp)).categoryType;
                    mType = ((NearbyCategoryServiceOutput) (temp)).type;
                    saveCategory(mCategoryID, mCategoryType, mType, ((NearbyCategoryServiceOutput) (temp)).fullName);
                    if (((NearbyCategoryServiceOutput) (temp)).fullName.equals("Places")) {
                        setGraySelected(mLayoutBoxButtonPlaceGray);
                        hideShowArrow("places");
                    } else if (((NearbyCategoryServiceOutput) (temp)).fullName.equals("Businesses")) {
                        setGraySelected(mLayoutBoxButtonBusinessGray);
                        hideShowArrow("businesses");
                    } else if (((NearbyCategoryServiceOutput) (temp)).fullName.equals("MRT Station")) {
                        setGraySelected(mLayoutBoxButtonMrtGray);
                        hideShowArrow("mrt");
                    } else if (((NearbyCategoryServiceOutput) (temp)).fullName.equals("Food")) {
                        setGraySelected(mLayoutBoxButtonFoodGray);
                        hideShowArrow("food");
                    } else {
                        mTextViewMoreButton.setText((new StringBuilder()).append("More: ").append(((NearbyCategoryServiceOutput) (temp)).fullName).toString());
                        setGraySelected(mLayoutBoxButtonMoreGray);
                        hideShowArrow("more");
                    }
                    refreshList();
                    return;
                }
        }
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_nearby_two);
        initialize();
        downloadNearbyData();
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        if(mSideMenuLayout.getIsMenuOpen())
            mSideMenuLayout.slideClose();
        else
            mSideMenuLayout.slideOpen();
        return false;
    }

    private static final int NEARBY_CATEGORY_SELECTED = 1;
    private ArrayList _imageServices;
    private Button mCategoryButton;
    private int mCategoryID;
    private int mCategoryType;
    private String mCountryCode;
    private ImageView mImageArrowBusButton;
    private ImageView mImageArrowBusinessButton;
    private ImageView mImageArrowFoodButton;
    private ImageView mImageArrowMoreButton;
    private ImageView mImageArrowPlaceButton;
    private ArrayList mImageServices;
    private int mKmRange;
    private Button mKmRangeButton;
    private double mLatitude;
    private View mLayoutBoxButtonBusiness;
    private View mLayoutBoxButtonBusinessGray;
    private View mLayoutBoxButtonFood;
    private View mLayoutBoxButtonFoodGray;
    private View mLayoutBoxButtonMore;
    private View mLayoutBoxButtonMoreGray;
    private View mLayoutBoxButtonMrt;
    private View mLayoutBoxButtonMrtGray;
    private View mLayoutBoxButtonPlace;
    private View mLayoutBoxButtonPlaceGray;
    private int mLimit;
    private LocationBusinessTipsNearbyAdapter mListAdapter;
    private ListView mListView;
    private ProgressBar mLoadingIndicator;
    private double mLongitude;
    private ImageButton mMenuButton;
    private NearbyCategoryAdapter mNearbyCategoryAdapter;
    private NearbyCategoryService mNearbyCategoryService;
    private LinearLayout mNoDataLayout;
    private NearbyService mService;
    private View mSideMenuBlocker;
    private SDMapSideMenuLayout mSideMenuLayout;
    private TextView mTextBusCount;
    private TextView mTextBusinessCount;
    private TextView mTextFoodCount;
    private TextView mTextMoreCount;
    private TextView mTextPlaceCount;
    private TextView mTextViewBusinessButton;
    private TextView mTextViewFoodButton;
    private TextView mTextViewMoreButton;
    private TextView mTextViewMrtButton;
    private TextView mTextViewPlaceButton;
    private int mType;
















/*
    static int access$2002(NearbyActivityTwo nearbyactivitytwo, int i)
    {
        nearbyactivitytwo.mKmRange = i;
        return i;
    }

*/


/*
    static NearbyService access$2102(NearbyActivityTwo nearbyactivitytwo, NearbyService nearbyservice)
    {
        nearbyactivitytwo.mService = nearbyservice;
        return nearbyservice;
    }

*/






/*
    static int access$302(NearbyActivityTwo nearbyactivitytwo, int i)
    {
        nearbyactivitytwo.mCategoryID = i;
        return i;
    }

*/



/*
    static int access$402(NearbyActivityTwo nearbyactivitytwo, int i)
    {
        nearbyactivitytwo.mCategoryType = i;
        return i;
    }

*/



/*
    static int access$502(NearbyActivityTwo nearbyactivitytwo, int i)
    {
        nearbyactivitytwo.mType = i;
        return i;
    }

*/




}
