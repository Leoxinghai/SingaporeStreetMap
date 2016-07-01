// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.nearby;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;
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
import streetdirectory.mobile.modules.nearby.category.NearbyCategoryActivityTwo;
import streetdirectory.mobile.modules.nearby.category.service.NearbyCategoryServiceOutput;
import streetdirectory.mobile.modules.nearby.service.NearbyService;
import streetdirectory.mobile.modules.nearby.service.NearbyServiceInput;
import streetdirectory.mobile.modules.nearby.service.NearbyServiceOutput;
import streetdirectory.mobile.modules.sdmob.SdMobHelper;
import streetdirectory.mobile.modules.sdmob.SmallBanner;
import streetdirectory.mobile.sd.SDMapSideMenuLayout;
import streetdirectory.mobile.sd.SdMob;
import streetdirectory.mobile.service.*;

public class NearbyActivity extends SDActivity
{

    public NearbyActivity()
    {
        mImageServices = new ArrayList();
        adRequestRetryCount = 0;
    }

    private void callButtonClicked(LocationBusinessServiceOutput locationbusinessserviceoutput, String s)
    {
        Intent intent = new Intent("android.intent.action.DIAL", Uri.parse((new StringBuilder()).append("tel:").append(locationbusinessserviceoutput.phoneNumber).toString()));
        HashMap hashmap = SDStory.createDefaultParams();
        hashmap.put("c_id", Integer.toString(locationbusinessserviceoutput.companyID));
        hashmap.put("c_pid", locationbusinessserviceoutput.phoneNumber);
        hashmap.put("opg", "business_listing_nearby");
        SDStory.post(URLFactory.createGantBusinessCall((new StringBuilder()).append(locationbusinessserviceoutput.companyID).append("").toString()), hashmap);
        startActivity(intent);
    }

    private void categoryButtonClicked()
    {
        Intent intent = new Intent(this, NearbyCategoryActivityTwo.class);
        loadCategory();
        intent.putExtra("countryCode", mCountryCode);
        intent.putExtra("categoryID", mCategoryID);
        startActivityForResult(intent, 1);
        overridePendingTransition(0x10a0000, 0x10a0001);
    }

    private void downloadFacebookPhoto(final NearbyServiceOutput nearbyserviceoutput, int i, int j)
    {
        Iterator iterator = mImageServices.iterator();
        for(;iterator.hasNext();) {
            if (((HttpImageService) iterator.next()).tag.equals(nearbyserviceoutput.tipsUserID))
                return;
        }

        final String final_s = nearbyserviceoutput.generateUserPhotoURL(i, j);
        if(final_s != null)
        {
            SDFacebookImageService sdFacebookImageService = new SDFacebookImageService(final_s, i, j) {

                public void onAborted(Exception exception)
                {
                    mImageServices.remove(this);
                }

                public void onFailed(Exception exception)
                {
                    Bitmap tmp = BitmapFactory.decodeResource(getResources(), R.drawable.facebook_thumb);
                    mListAdapter.putImage(nearbyserviceoutput.tipsUserID, tmp);
                    mImageServices.remove(this);
                    mListAdapter.notifyDataSetChanged();
                }

                public void onSuccess(Bitmap bitmap)
                {
                    mListAdapter.putImage(nearbyserviceoutput.tipsUserID, bitmap);
                    mImageServices.remove(this);
                    mListAdapter.notifyDataSetChanged();
                }

                public void onSuccess(Object obj)
                {
                    onSuccess((Bitmap)obj);
                }
            };

            sdFacebookImageService.tag = nearbyserviceoutput.tipsUserID;
            mImageServices.add(sdFacebookImageService);
            sdFacebookImageService.executeAsync();
            return;
        }

    }

    private void downloadImage(final NearbyServiceOutput nearbyserviceoutput, int i, int j)
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
            SDHttpImageService sdHttpImageService = new SDHttpImageService(final_s) {

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

            sdHttpImageService.tag = nearbyserviceoutput.uniqueID;
            mImageServices.add(sdHttpImageService);
            sdHttpImageService.executeAsync();
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

    private void initData()
    {
        loadCategory();
        Object obj = getImageCategoryFromFile("image_category.png");
        if(obj != null)
        {
            mImageCategory.setImageBitmap(((Bitmap) (obj)));
        } else
        {
            mNearbyLastName = "Places";
            mImageCategory.setImageResource(R.drawable.nearby_category_all_places);
        }
        mTitleCategory.setText(mNearbyLastName);
        obj = getIntent();
        mCountryCode = ((Intent) (obj)).getStringExtra("countryCode");
        mType = ((Intent) (obj)).getIntExtra("type", mType);
        mLongitude = ((Intent) (obj)).getDoubleExtra("longitude", 0.0D);
        mLatitude = ((Intent) (obj)).getDoubleExtra("latitude", 0.0D);
        mKmRange = ((Intent) (obj)).getIntExtra("kmRange", 3);
        if(mKmRangeButton != null)
            mKmRangeButton.setText((new StringBuilder()).append("Within ").append(mKmRange).append(" Km").toString());
        mLimit = ((Intent) (obj)).getIntExtra("limit", 10);
        mCategoryType = ((Intent) (obj)).getIntExtra("categoryType", mCategoryType);
        mListAdapter = new LocationBusinessTipsNearbyAdapter(this, mKmRange);
        if(mListView != null)
            mListView.setAdapter(mListAdapter);
    }

    private void initEvent()
    {
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
        mCategoryButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                categoryButtonClicked();
            }

        });
        mListAdapter.setOnCallButtonClickedListener(new streetdirectory.mobile.modules.core.adapter.LocationBusinessAdapter.OnCallButtonClickedListener() {

            public void onCallButtonClicked(LocationBusinessServiceOutput locationbusinessserviceoutput, int i, String s)
            {
                callButtonClicked(locationbusinessserviceoutput, s);
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
                if(i >= mListAdapter.getItemSize())
                    return;
                LocationBusinessServiceOutput temp = mListAdapter.getItem(i);
                Intent intent = new Intent(NearbyActivity.this, MapActivity.class);
                intent.putExtra("data", (Parcelable)temp);
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
        mSideMenuBlocker = findViewById(R.id.side_menu_blocker);
        mMenuButton = (ImageButton)findViewById(R.id.MenuButton);
        mCategoryButton = (Button)findViewById(R.id.CategoryButton);
        mLoadingIndicator = (ProgressBar)findViewById(R.id.LoadingIndicator);
        mListView = (ListView)findViewById(R.id.ListView);
        mNoDataLayout = (LinearLayout)findViewById(R.id.NoDataLayout);
        mKmRangeButton = (Button)mNoDataLayout.findViewById(R.id.KmRangeButton);
        mImageCategory = (ImageView)findViewById(R.id.imageCategory);
        mTitleCategory = (TextView)findViewById(R.id.categoryTitle);
        mSearchListView = (ListView)mSideMenuLayout.findViewById(R.id.SearchListView);
        mMenuListView = (ListView)mSideMenuLayout.findViewById(R.id.MenuListView);
        mSearchField = (EditText)mSideMenuLayout.findViewById(R.id.MenuSearchField);
        mgr = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mSdSmallBanner = (RelativeLayout)findViewById(R.id.view_sdmob);
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
        mNearbyLastName = sdpreferences.getStringForKey("nearby_lastName", "Places");
        SDStory.post(URLFactory.createGantNearbyMain(mNearbyLastName), SDStory.createDefaultParams());
    }

    private void loadSmallBanner()
    {
        Object obj = SmallBanner.getBannerFromSdMobUnit(this, SdMobHelper.getInstance(this).getSdMobUnit(SdMob.ad_bnr_nearby));
//        ((SmallBanner) (obj)).setAdMobSmallBannerListener(new streetdirectory.mobile.modules.sdmob.SmallBanner.AdMobSmallBannerListener());
        obj = ((SmallBanner) (obj)).getView(this);
        mSdSmallBanner.removeAllViews();
        mSdSmallBanner.addView(((View) (obj)), new ViewGroup.LayoutParams(-1, -1));
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

    public Bitmap getImageCategoryFromFile(String s)
    {
        Bitmap temp = null;
        FileInputStream fileinputstream;
        try
        {
            fileinputstream = openFileInput(s);
            temp = BitmapFactory.decodeStream(fileinputstream);
            fileinputstream.close();
            return temp;
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            SDLogger.debug((new StringBuilder()).append("Nearby Activity : get image category failed").append(ex.toString()).toString());
            return null;
        }
    }

    protected void onActivityResult(int i, int j, Intent intent)
    {
        switch(i) {
            default:
                return;
            case 1:
                if (j == -1) {
                    Bitmap bitmap = (Bitmap) intent.getParcelableExtra("bitmapImage");
                    if (bitmap != null) {
                        mImageCategory.setImageBitmap(bitmap);
                        saveImageCategoryToFile(bitmap);
                    }
                    NearbyCategoryServiceOutput temp = (NearbyCategoryServiceOutput) intent.getParcelableExtra("selectedCategory");
                    if (intent != null) {
                        mCategoryID = ((NearbyCategoryServiceOutput) (temp)).categoryID;
                        mCategoryType = ((NearbyCategoryServiceOutput) (temp)).categoryType;
                        mType = ((NearbyCategoryServiceOutput) (temp)).type;
                        mTitleCategory.setText(((NearbyCategoryServiceOutput) (temp)).fullName);
                        saveCategory(mCategoryID, mCategoryType, mType, ((NearbyCategoryServiceOutput) (temp)).fullName);
                        SDStory.post(URLFactory.createGantNearbyMain(((NearbyCategoryServiceOutput) (temp)).fullName), SDStory.createDefaultParams());
                        refreshList();
                        return;
                    }
                }
                break;
        }
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

    public void onCreate(Bundle bundle)
    {
        onCreate(bundle);
        setContentView(R.layout.activity_nearby_four);
        initialize();
        downloadNearbyData();
        loadSmallBanner();
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        if(mSideMenuLayout.getIsMenuOpen())
            mSideMenuLayout.slideClose();
        else
            mSideMenuLayout.slideOpen();
        return false;
    }

    public void saveImageCategoryToFile(Bitmap bitmap)
    {
        try
        {
            FileOutputStream fileoutputstream = openFileOutput("image_category.png", 0);
            bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 90, fileoutputstream);
            fileoutputstream.close();
            return;
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            SDLogger.debug((new StringBuilder()).append("Nearby Activity : save image category failed").append(ex.toString()).toString());
        }
    }

    public static final String CATEGORY_ID = "categoryID";
    private static final String FILENAME = "image_category.png";
    private static final int NEARBY_CATEGORY_SELECTED = 1;
    private int adRequestRetryCount;
    private Button mCategoryButton;
    private int mCategoryID;
    private int mCategoryType;
    private String mCountryCode;
    private ImageView mImageCategory;
    private ArrayList mImageServices;
    private int mKmRange;
    private Button mKmRangeButton;
    private double mLatitude;
    private int mLimit;
    private LocationBusinessTipsNearbyAdapter mListAdapter;
    private ListView mListView;
    private ProgressBar mLoadingIndicator;
    private double mLongitude;
    private ImageButton mMenuButton;
    private ListView mMenuListView;
    private String mNearbyLastName;
    private LinearLayout mNoDataLayout;
    private RelativeLayout mSdSmallBanner;
    private EditText mSearchField;
    private ListView mSearchListView;
    private NearbyService mService;
    private View mSideMenuBlocker;
    private SDMapSideMenuLayout mSideMenuLayout;
    private TextView mTitleCategory;
    private int mType;
    private InputMethodManager mgr;





/*
    static int access$1002(NearbyActivity nearbyactivity, int i)
    {
        nearbyactivity.mKmRange = i;
        return i;
    }

*/



/*
    static NearbyService access$1202(NearbyActivity nearbyactivity, NearbyService nearbyservice)
    {
        nearbyactivity.mService = nearbyservice;
        return nearbyservice;
    }

*/






/*
    static int access$1602(NearbyActivity nearbyactivity, int i)
    {
        nearbyactivity.adRequestRetryCount = i;
        return i;
    }

*/


/*
    static int access$1608(NearbyActivity nearbyactivity)
    {
        int i = nearbyactivity.adRequestRetryCount;
        nearbyactivity.adRequestRetryCount = i + 1;
        return i;
    }

*/









}
