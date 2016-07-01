// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.locationdetail.businessin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.util.*;
import streetdirectory.mobile.core.SDLogger;
import streetdirectory.mobile.core.SDStory;
import streetdirectory.mobile.core.service.HttpImageService;
import streetdirectory.mobile.modules.SDActivity;
import streetdirectory.mobile.modules.businessdetail.BusinessDetailActivityV2;
import streetdirectory.mobile.modules.core.LocationBusinessServiceOutput;
import streetdirectory.mobile.modules.direction.DirectionActivity;
import streetdirectory.mobile.modules.locationdetail.businessin.service.BusinessInByCategoryService;
import streetdirectory.mobile.modules.locationdetail.businessin.service.BusinessInByCategoryServiceInput;
import streetdirectory.mobile.modules.locationdetail.businessin.service.BusinessInByCategoryServiceOutput;
import streetdirectory.mobile.modules.locationdetail.businessin.service.BusinessInCategoryService;
import streetdirectory.mobile.modules.locationdetail.businessin.service.BusinessInCategoryServiceInput;
import streetdirectory.mobile.modules.locationdetail.businessin.service.BusinessInCategoryServiceOutput;
import streetdirectory.mobile.modules.locationdetail.businessin.service.BusinessInService;
import streetdirectory.mobile.modules.locationdetail.businessin.service.BusinessInServiceInput;
import streetdirectory.mobile.modules.locationdetail.businessin.service.BusinessInServiceOutput;
import streetdirectory.mobile.modules.mappreview.MapPreviewActivity;
import streetdirectory.mobile.modules.sdmob.SdMobHelper;
import streetdirectory.mobile.modules.sdmob.SmallBanner;
import streetdirectory.mobile.modules.tips.TipsActivity;
import streetdirectory.mobile.sd.SdMob;
import streetdirectory.mobile.service.*;

// Referenced classes of package streetdirectory.mobile.modules.locationdetail.businessin:
//            BusinessInAdapter, BusinessesByCategoryAdapter, BusinessInCategoryAdapter

public class BusinessInActivity extends SDActivity
{

    public BusinessInActivity()
    {
        isInAll = true;
        businessIn = new ArrayList();
        businessInCategories = new ArrayList();
        businessesByCategory = new ArrayList();
        adRequestRetryCount = 0;
    }

    private void abortAllDownload()
    {
        abortDownloadBusinessIn();
        abortDownloadPhotoImage();
        abortListingDownloadImage();
        abortDownloadBusinessInCategory();
        abortDownloadBusinessesByCategory();
    }

    private void abortDownloadBusinessIn()
    {
        if(mBusinessInService != null)
        {
            mBusinessInService.abort();
            mBusinessInService = null;
        }
    }

    private void abortDownloadBusinessInCategory()
    {
        if(mBusinessInCategoryService != null)
        {
            mBusinessInCategoryService.abort();
            mBusinessInCategoryService = null;
            businessInCategories.clear();
        }
    }

    private void abortDownloadBusinessesByCategory()
    {
        if(mBusinessInByCategoryService != null)
        {
            mBusinessInByCategoryService.abort();
            mBusinessInByCategoryService = null;
            businessesByCategory.clear();
        }
    }

    private void abortDownloadPhotoImage()
    {
        if(mPhotoImageService != null)
        {
            mPhotoImageService.abort();
            mPhotoImageService = null;
        }
    }

    private void abortListingDownloadImage()
    {
        for(Iterator iterator = mBusinessInImageServices.iterator(); iterator.hasNext(); ((SDHttpImageService)iterator.next()).abort());
        mBusinessInImageServices.clear();
    }

    private void callButtonClicked(LocationBusinessServiceOutput locationbusinessserviceoutput, String s)
    {
        Intent intent = new Intent("android.intent.action.DIAL", Uri.parse((new StringBuilder()).append("tel:").append(locationbusinessserviceoutput.phoneNumber).toString()));
        HashMap hashmap = SDStory.createDefaultParams();
        hashmap.put("c_id", Integer.toString(locationbusinessserviceoutput.companyID));
        hashmap.put("c_pid", locationbusinessserviceoutput.phoneNumber);
        hashmap.put("opg", "business_listing_in");
        SDStory.post(URLFactory.createGantBusinessCall((new StringBuilder()).append(locationbusinessserviceoutput.companyID).append("").toString()), hashmap);
        startActivity(intent);
    }

    private void downloadBusinessByCategoryImage(final BusinessInByCategoryServiceOutput businessinbycategoryserviceoutput, int i, int j)
    {
        Iterator iterator = mBusinessInImageServices.iterator();
        for(;iterator.hasNext();) {
            if (((HttpImageService) iterator.next()).tag.equals(businessinbycategoryserviceoutput.uniqueID))
                return;
        }

        final String final_s = businessinbycategoryserviceoutput.generateImageURL(this, i, j);
        if(final_s != null)
        {
            SDHttpImageService sdHttpImageService = new SDHttpImageService(final_s) {

                public void onAborted(Exception exception)
                {
                    mBusinessInImageServices.remove(this);
                }

                public void onFailed(Exception exception)
                {
                    mBusinessInImageServices.remove(this);
                }

                public void onSuccess(Bitmap bitmap)
                {
                    businessinbycategoryserviceoutput.image = bitmap;
                    mBusinessInImageServices.remove(this);
                    mBusinessesByCategoryAdapter.notifyDataSetChanged();
                }

                public void onSuccess(Object obj)
                {
                    onSuccess((Bitmap)obj);
                }

            };
            sdHttpImageService.tag = businessinbycategoryserviceoutput.uniqueID;
            mBusinessInImageServices.add(sdHttpImageService);
            sdHttpImageService.executeAsync();
            return;
        }
    }

    private void downloadBusinessIn()
    {
        if(mBusinessInService == null)
        {
            int i = mBusinessInAdapter.getItemSize();
            if(mBusinessInAdapter.getItemSize() <= 1)
                mProgressBar.setVisibility(View.VISIBLE);
            mBusinessInService = new BusinessInService(new BusinessInServiceInput(mData.countryCode, mData.placeID, mData.addressID, i, 5, false)) {

                public void onAborted(Exception exception)
                {
                    SDLogger.info("Business In Aborted");
                }

                public void onFailed(Exception exception)
                {
                    SDLogger.printStackTrace(exception, "Business In Failed");
                    mBusinessInService = null;
                }

                public void onProgress(int j)
                {
                    mProgressBar.setProgress(j);
                }

                public void onReceiveData(BusinessInServiceOutput businessinserviceoutput)
                {
                    mBusinessInAdapter.addItem(businessinserviceoutput);
                }

                public void onReceiveData(SDDataOutput sddataoutput)
                {
                    onReceiveData((BusinessInServiceOutput)sddataoutput);
                }

                public void onReceiveTotal(long l)
                {
                    mBusinessInAdapter.setTotalItem(l);
                }

                public void onSuccess(Object obj)
                {
                    onSuccess((SDHttpServiceOutput)obj);
                }

                public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
                {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mBusinessInAdapter.notifyDataSetChanged();
                    mBusinessInService = null;
                }

            };
            mBusinessInService.executeAsync();
        }
    }

    private void downloadBusinessInByCategory(int i)
    {
        mBusinessesByCategoryAdapter.clear();
        mProgressBar.setVisibility(View.VISIBLE);
        mBusinessInByCategoryService = new BusinessInByCategoryService(new BusinessInByCategoryServiceInput(mData.countryCode, mData.placeID, mData.addressID, 0, 5000, false, i)) {

            public void onFailed(Exception exception)
            {
                SDLogger.printStackTrace(exception, "Business By Category In Failed");
                mBusinessInByCategoryService = null;
            }

            public void onProgress(int j)
            {
                mProgressBar.setProgress(j);
            }

            public void onReceiveData(BusinessInByCategoryServiceOutput businessinbycategoryserviceoutput)
            {
                mBusinessesByCategoryAdapter.addItem(businessinbycategoryserviceoutput);
            }

            public void onReceiveData(SDDataOutput sddataoutput)
            {
                onReceiveData((BusinessInByCategoryServiceOutput)sddataoutput);
            }

            public void onReceiveTotal(long l)
            {
                mBusinessesByCategoryAdapter.setTotalItem(l);
            }

            public void onSuccess(Object obj)
            {
                onSuccess((SDHttpServiceOutput)obj);
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                int j = 0;
                int k;
                mProgressBar.setVisibility(View.INVISIBLE);
                k = mButtonBusinessPhoto.getWidth();
                int l = mButtonBusinessPhoto.getHeight();
                if(k != 0) {
                    j = l;
                    if (l == 0) {
                        k = mButtonBusinessPhoto.getLayoutParams().width;
                        j = mButtonBusinessPhoto.getLayoutParams().height;
                    }
                }
                if(l == 0 || k ==0)
                {
                }
                BusinessInByCategoryServiceOutput businessinbycategoryserviceoutput;
                for(Iterator iterator = sdhttpserviceoutput.childs.iterator(); iterator.hasNext(); downloadBusinessByCategoryImage(businessinbycategoryserviceoutput, k, j))
                {
                    businessinbycategoryserviceoutput = (BusinessInByCategoryServiceOutput)iterator.next();
                    businessesByCategory.add(businessinbycategoryserviceoutput);
                }

                mBusinessesByCategoryAdapter.notifyDataSetChanged();
            }

        };
        mBusinessInByCategoryService.executeAsync();
    }

    private void downloadBusinessInCategory()
    {
        if(mBusinessInCategoryService != null)
        {
            return;
        } else
        {
            mProgressBar.setVisibility(View.VISIBLE);
            mBusinessInCategoryService = new BusinessInCategoryService(new BusinessInCategoryServiceInput(mData.countryCode, mData.placeID, mData.addressID, 0, 1000)) {

                public void onFailed(Exception exception)
                {
                    SDLogger.printStackTrace(exception, "Business Category In Failed");
                    mBusinessInCategoryService = null;
                }

                public void onProgress(int i)
                {
                    mProgressBar.setProgress(i);
                }

                public void onSuccess(Object obj)
                {
                    onSuccess((SDHttpServiceOutput)obj);
                }

                public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
                {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    BusinessInCategoryServiceOutput businessincategoryserviceoutput;
                    for(Iterator iterator = sdhttpserviceoutput.childs.iterator(); iterator.hasNext(); businessInCategories.add(businessincategoryserviceoutput))
                        businessincategoryserviceoutput = (BusinessInCategoryServiceOutput)iterator.next();

                    mBusinessInCategoryAdapter.notifyDataSetChanged();
                }

            };
            mBusinessInCategoryService.executeAsync();
            return;
        }
    }

    private void downloadBusinessInImage(final BusinessInServiceOutput businessinserviceoutput, int i, int j)
    {
        Iterator iterator = mBusinessInImageServices.iterator();
        for(;iterator.hasNext();) {
			if(((HttpImageService)iterator.next()).tag.equals(businessinserviceoutput.uniqueID))
				return;
		}

        final String final_s = businessinserviceoutput.generateImageURL(this, i, j);
        if(final_s != null)
        {
            SDHttpImageService sdHttpImageService = new SDHttpImageService(final_s) {

                public void onAborted(Exception exception)
                {
                    mBusinessInImageServices.remove(this);
                }

                public void onFailed(Exception exception)
                {
                    mBusinessInImageServices.remove(this);
                }

                public void onSuccess(Bitmap bitmap)
                {
                    mBusinessInAdapter.putImage(businessinserviceoutput.uniqueID, bitmap);
                    mBusinessInImageServices.remove(this);
                    mBusinessInAdapter.notifyDataSetChanged();
                }

                public void onSuccess(Object obj)
                {
                    onSuccess((Bitmap)obj);
                }

            };
            sdHttpImageService.tag = businessinserviceoutput.uniqueID;
            mBusinessInImageServices.add(sdHttpImageService);
            sdHttpImageService.executeAsync();
            return;
        }
    }

    private void downloadPhotoImage(String s, int i, int j)
    {
        abortDownloadPhotoImage();
        mPhotoImageService = new SDHttpImageService(s) {

            public void onAborted(Exception exception)
            {
                SDLogger.info("Photo Image Aborted");
            }

            public void onFailed(Exception exception)
            {
                SDLogger.printStackTrace(exception, "Photo Image Failed");
                mPhotoImageService = null;
            }

            public void onSuccess(Bitmap bitmap)
            {
                mPhotoImageService = null;
                mButtonBusinessPhoto.setImageBitmap(bitmap);
            }

            public void onSuccess(Object obj)
            {
                onSuccess((Bitmap)obj);
            }

        };
        mPhotoImageService.executeAsync();
    }

    private void initData()
    {
        LocationBusinessServiceOutput locationbusinessserviceoutput = (LocationBusinessServiceOutput)getIntent().getParcelableExtra("data");
        if(locationbusinessserviceoutput != null)
        {
            mData = new BusinessInServiceOutput(locationbusinessserviceoutput.hashData);
            mTextviewTitle.setText(mData.venue);
            SDStory.post(URLFactory.createGantBuildingDirectoryMain(mData.venue), SDStory.createDefaultParams());
            mTextviewDetail.setText(mData.address);
            if(mData.longitude == 0.0D && mData.latitude == 0.0D)
                mButtonMap.setEnabled(false);
        }
        mBusinessInImageServices = new ArrayList();
        mBusinessInAdapter = new BusinessInAdapter(this);
        mListViewAllBusiness.setAdapter(mBusinessInAdapter);
        mBusinessesByCategoryAdapter = new BusinessesByCategoryAdapter(this);
        mListViewBusinessesByCategory.setAdapter(mBusinessesByCategoryAdapter);
        mBusinessInCategoryAdapter = new BusinessInCategoryAdapter(this);
        mBusinessInCategoryAdapter.mData = businessInCategories;
        mListViewCategoryBusiness.setAdapter(mBusinessInCategoryAdapter);
    }

    private void initEvent()
    {
        mBackButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                finish();
            }

        }
);
        mShareButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
            }

        });
        mButtonDirection.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                Intent intent = new Intent(BusinessInActivity.this, DirectionActivity.class);
                intent.putExtra("endData", (Parcelable)mData);
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
        mBusinessInAdapter.setOnLoadMoreListener(new streetdirectory.mobile.modules.core.adapter.LocationBusinessAdapter.OnLoadMoreListener() {

            public void onLoadMoreList()
            {
                downloadBusinessIn();
            }

        });
        mBusinessInAdapter.setOnImageNotFoundListener(new streetdirectory.mobile.modules.core.adapter.LocationBusinessAdapter.OnImageNotFoundListener() {

            public void onImageNotFound(LocationBusinessServiceOutput locationbusinessserviceoutput, int i, int j, int k)
            {
                downloadBusinessInImage((BusinessInServiceOutput)locationbusinessserviceoutput, j, k);
            }

        });
        mBusinessInAdapter.setOnCallButtonClickedListener(new streetdirectory.mobile.modules.core.adapter.LocationBusinessAdapter.OnCallButtonClickedListener() {

            public void onCallButtonClicked(LocationBusinessServiceOutput locationbusinessserviceoutput, int i, String s)
            {
                callButtonClicked(locationbusinessserviceoutput, s);
            }

        });
        mButtonSearch.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                mButtonSearch.setSelected(true);
                mButtonAll.setSelected(false);
                mButtonCategory.setSelected(false);
                RelativeLayout.LayoutParams layoutParams;
                layoutParams = (android.widget.RelativeLayout.LayoutParams)layoutAnimate.getLayoutParams();
                int i = -(mLayoutHeader.getHeight() - 1);
                int j;
                if(((android.widget.RelativeLayout.LayoutParams) (layoutParams)).topMargin < -1)
                {
                    i = mLayoutHeader.getMeasuredHeight();
                    j = 0;
                } else
                {
                    i = -mLayoutHeader.getMeasuredHeight();
                    j = i;
                }
                SDLogger.debug((new StringBuilder()).append("m=").append(i).append(", mm=").append(j).append(", mmm=").append(j).toString());
                if(mLayoutHeader.getVisibility() == 8)
                {
                    mLayoutHeader.setVisibility(View.VISIBLE);
                    return;
                } else
                {
                    mLayoutHeader.setVisibility(View.INVISIBLE);
                    return;
                }
            }

        });
        mButtonCategory.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                mLayoutHeader.setVisibility(View.VISIBLE);
                mListViewAllBusiness.setVisibility(View.INVISIBLE);
                mListViewSearchBusiness.setVisibility(View.INVISIBLE);
                mListViewBusinessesByCategory.setVisibility(View.INVISIBLE);
                mListViewCategoryBusiness.setVisibility(View.VISIBLE);
                mButtonAll.setSelected(false);
                mButtonCategory.setSelected(true);
                downloadBusinessInCategory();
            }

        });

        mButtonAll.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                mLayoutHeader.setVisibility(View.VISIBLE);
                mListViewAllBusiness.setVisibility(View.VISIBLE);
                mListViewSearchBusiness.setVisibility(View.INVISIBLE);
                mListViewCategoryBusiness.setVisibility(View.INVISIBLE);
                mListViewBusinessesByCategory.setVisibility(View.INVISIBLE);
                mButtonCategory.setSelected(false);
                mButtonAll.setSelected(true);
            }

        });
        mListViewCategoryBusiness.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView adapterview, View view, int i, long l)
            {
                mButtonCategory.setSelected(false);
                mListViewAllBusiness.setVisibility(View.INVISIBLE);
                mListViewCategoryBusiness.setVisibility(View.INVISIBLE);
                mListViewSearchBusiness.setVisibility(View.INVISIBLE);
                mListViewBusinessesByCategory.setVisibility(View.VISIBLE);
                BusinessInCategoryServiceOutput businessInCategoryServiceOutput = (BusinessInCategoryServiceOutput)businessInCategories.get(i);
                mLastCategoryName = ((BusinessInCategoryServiceOutput) (businessInCategoryServiceOutput)).categoryName;
                SDStory.post(URLFactory.createGantBuildingDirectoryCategory(mData.venue, ((BusinessInCategoryServiceOutput) (businessInCategoryServiceOutput)).categoryName), SDStory.createDefaultParams());
                downloadBusinessInByCategory(Integer.parseInt(((BusinessInCategoryServiceOutput) (businessInCategoryServiceOutput)).categoryID));
            }

        });

        mListViewBusinessesByCategory.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView adapterview, View view, int i, long l)
            {
                BusinessInByCategoryServiceOutput businessInByCategoryServiceOutput = (BusinessInByCategoryServiceOutput)businessesByCategory.get(i);
                SDStory.post(URLFactory.createGantBuildingDirectoryClickOnCategory(mData.venue, mLastCategoryName, ((BusinessInByCategoryServiceOutput) (businessInByCategoryServiceOutput)).venue), SDStory.createDefaultParams());
                Intent intent = new Intent(BusinessInActivity.this, BusinessDetailActivityV2.class);
                intent.putExtra("data", (Parcelable)adapterview);
                startActivity(intent);
            }

        });
        mBusinessesByCategoryAdapter.setOnCallButtonClickedListener(new streetdirectory.mobile.modules.core.adapter.LocationBusinessAdapter.OnCallButtonClickedListener() {

            public void onCallButtonClicked(LocationBusinessServiceOutput locationbusinessserviceoutput, int i, String s)
            {
                callButtonClicked(locationbusinessserviceoutput, s);
            }

        });

        mListViewAllBusiness.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView adapterview, View view, int i, long l)
            {
                if(mBusinessInAdapter.getItemSize() != i)
                {
                    BusinessInServiceOutput businessInServiceOutput = (BusinessInServiceOutput)mBusinessInAdapter.getItem(i);
                    Intent intent = new Intent(BusinessInActivity.this, BusinessDetailActivityV2.class);
                    intent.putExtra("data", (Parcelable)adapterview);
                    startActivity(intent);
                }
            }

        });
    }

    private void initLayout()
    {
        mProgressBar = (ProgressBar)findViewById(R.id.progress_bar_business_in);
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
        mLayoutBusinessInContent = (RelativeLayout)findViewById(R.id.layout_business_in_content);
        mLayoutAllCategorySearch = (LinearLayout)findViewById(R.id.layout_all_category_search);
        mButtonAll = (Button)findViewById(R.id.button_all);
        mButtonAll.setSelected(true);
        mButtonSearch = (ImageButton)findViewById(R.id.button_search);
        mButtonCategory = (Button)findViewById(R.id.button_category);
        mImageViewShadow = (ImageView)findViewById(R.id.image_view_shadow);
        mListViewAllBusiness = (ListView)findViewById(R.id.list_view_all_business);
        mListViewCategoryBusiness = (ListView)findViewById(R.id.list_view_category_business);
        mListViewSearchBusiness = (ListView)findViewById(R.id.list_view_search_business);
        mListViewBusinessesByCategory = (ListView)findViewById(R.id.list_view_businesses_by_category);
        mImageViewSorry = (ImageView)findViewById(R.id.image_view_sorry);
        mTextViewSorry = (TextView)findViewById(R.id.text_view_sorry);
        mButtonAddBusiness = (Button)findViewById(R.id.button_add_business);
        mLayoutReviews = (LinearLayout)findViewById(R.id.layout_reviews);
        mLayoutTipsCell = findViewById(R.id.layout_tips_cell);
        mButtonMoreReviews = (Button)findViewById(R.id.button_more_reviews);
        mLayoutNoReviews = (LinearLayout)findViewById(R.id.layout_no_reviews);
        mLayoutPostReviews = findViewById(R.id.layout_post_reviews);
        mLayoutNoBusiness = (LinearLayout)findViewById(R.id.layout_no_business);
        layoutAnimate = (LinearLayout)findViewById(R.id.layout_animate);
        mSdSmallBanner = (RelativeLayout)findViewById(R.id.view_sdmob);
        mSdSmallBannerNoBusiness = (RelativeLayout)findViewById(R.id.view_sdmob_no_business);
        mButtonAddBusiness.setVisibility(View.INVISIBLE);
    }

    private void initialize()
    {
        initLayout();
        initData();
        initEvent();
    }

    private void loadSmallBanner()
    {
        RelativeLayout relativelayout;
        Object obj;
        if(mData.totalBusiness == 0)
            relativelayout = mSdSmallBannerNoBusiness;
        else
            relativelayout = mSdSmallBanner;
        obj = SmallBanner.getBannerFromSdMobUnit(this, SdMobHelper.getInstance(this).getSdMobUnit(SdMob.ad_bnr_building_directory));
//        ((SmallBanner) (obj)).setAdMobSmallBannerListener(new streetdirectory.mobile.modules.sdmob.SmallBanner.AdMobSmallBannerListener());
        obj = ((SmallBanner) (obj)).getView(this);
        relativelayout.removeAllViews();
        relativelayout.addView(((View) (obj)), new RelativeLayout.LayoutParams(-1, -1));
    }

    private void startActivityMapPreview()
    {
        Intent intent = new Intent(this, MapPreviewActivity.class);
        intent.putExtra("longitude", mData.longitude);
        intent.putExtra("latitude", mData.latitude);
        intent.putExtra("pinLongitude", mData.longitude);
        intent.putExtra("pinLatitude", mData.latitude);
        intent.putExtra("pinTitle", mData.venue);
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
        onCreate(bundle);
        setContentView(R.layout.activity_location_detail_business_in);
        int i = 0;
        int j;
        int k;
        initialize();
        if(mData != null) {
            if (mData.totalBusiness == 0) {
                mLayoutBusinessInContent.setVisibility(View.INVISIBLE);
                mLayoutNoBusiness.setVisibility(View.VISIBLE);
            } else {
                mLayoutBusinessInContent.setVisibility(View.VISIBLE);
                mLayoutNoBusiness.setVisibility(View.INVISIBLE);
            }
            j = mButtonBusinessPhoto.getWidth();
            k = mButtonBusinessPhoto.getHeight();
            if (j != 0) {
                i = k;
            }
            if (k == 0 || j == 0) {
                j = mButtonBusinessPhoto.getLayoutParams().width;
                i = mButtonBusinessPhoto.getLayoutParams().height;
            }
            downloadPhotoImage(mData.imageURL, j, i);
            downloadBusinessIn();
        }
        loadSmallBanner();
    }

    protected void onDestroy()
    {
        onDestroy();
        abortAllDownload();
    }

    private int adRequestRetryCount;
    private ArrayList businessIn;
    private ArrayList businessInCategories;
    private ArrayList businessesByCategory;
    boolean isInAll;
    private LinearLayout layoutAnimate;
    private Button mBackButton;
    private BusinessInAdapter mBusinessInAdapter;
    private BusinessInByCategoryService mBusinessInByCategoryService;
    private BusinessInCategoryAdapter mBusinessInCategoryAdapter;
    private BusinessInCategoryService mBusinessInCategoryService;
    private ArrayList mBusinessInImageServices;
    private BusinessInService mBusinessInService;
    private BusinessesByCategoryAdapter mBusinessesByCategoryAdapter;
    private Button mButtonAddBusiness;
    private Button mButtonAll;
    private ImageButton mButtonBusinessPhoto;
    private Button mButtonCategory;
    private Button mButtonDirection;
    private Button mButtonMap;
    private Button mButtonMoreReviews;
    private ImageButton mButtonSearch;
    private Button mButtonTips;
    private BusinessInServiceOutput mData;
    private ImageView mImageViewShadow;
    private ImageView mImageViewSorry;
    private String mLastCategoryName;
    private LinearLayout mLayoutAllCategorySearch;
    private RelativeLayout mLayoutBusinessInContent;
    private RelativeLayout mLayoutHeader;
    private LinearLayout mLayoutHeaderButton;
    private LinearLayout mLayoutNoBusiness;
    private LinearLayout mLayoutNoReviews;
    private View mLayoutPostReviews;
    private LinearLayout mLayoutReviews;
    private View mLayoutTipsCell;
    private ListView mListViewAllBusiness;
    private ListView mListViewBusinessesByCategory;
    private ListView mListViewCategoryBusiness;
    private ListView mListViewSearchBusiness;
    private RelativeLayout mMenuBar;
    private SDHttpImageService mPhotoImageService;
    private ProgressBar mProgressBar;
    private RelativeLayout mSdSmallBanner;
    private RelativeLayout mSdSmallBannerNoBusiness;
    private Button mShareButton;
    private TextView mTextViewSorry;
    private TextView mTextviewDetail;
    private TextView mTextviewTitle;
    private TextView mTitleBar;












/*
    static String access$1702(BusinessInActivity businessinactivity, String s)
    {
        businessinactivity.mLastCategoryName = s;
        return s;
    }

*/










/*
    static BusinessInByCategoryService access$2502(BusinessInActivity businessinactivity, BusinessInByCategoryService businessinbycategoryservice)
    {
        businessinactivity.mBusinessInByCategoryService = businessinbycategoryservice;
        return businessinbycategoryservice;
    }

*/



/*
    static BusinessInCategoryService access$2702(BusinessInActivity businessinactivity, BusinessInCategoryService businessincategoryservice)
    {
        businessinactivity.mBusinessInCategoryService = businessincategoryservice;
        return businessincategoryservice;
    }

*/


/*
    static BusinessInService access$2802(BusinessInActivity businessinactivity, BusinessInService businessinservice)
    {
        businessinactivity.mBusinessInService = businessinservice;
        return businessinservice;
    }

*/




/*
    static SDHttpImageService access$3002(BusinessInActivity businessinactivity, SDHttpImageService sdhttpimageservice)
    {
        businessinactivity.mPhotoImageService = sdhttpimageservice;
        return sdhttpimageservice;
    }

*/



/*
    static int access$3102(BusinessInActivity businessinactivity, int i)
    {
        businessinactivity.adRequestRetryCount = i;
        return i;
    }

*/


/*
    static int access$3108(BusinessInActivity businessinactivity)
    {
        int i = businessinactivity.adRequestRetryCount;
        businessinactivity.adRequestRetryCount = i + 1;
        return i;
    }

*/







}
