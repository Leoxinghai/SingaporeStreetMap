// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.businesslisting;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.facebook.*;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.WebDialog;
import com.xinghai.mycurve.R;

import java.util.*;
import streetdirectory.mobile.core.*;
import streetdirectory.mobile.core.service.HttpImageService;
import streetdirectory.mobile.modules.SDActivity;
import streetdirectory.mobile.modules.businessdetail.BusinessDetailActivityV2;
import streetdirectory.mobile.modules.businessfindersubdirectory.BusinessFinderSubDirectoryActivity;
import streetdirectory.mobile.modules.businesslisting.service.BusinessListingService;
import streetdirectory.mobile.modules.businesslisting.service.BusinessListingServiceInput;
import streetdirectory.mobile.modules.businesslisting.service.BusinessListingServiceOutput;
import streetdirectory.mobile.modules.businesslisting.service.OffersCategoryListService;
import streetdirectory.mobile.modules.businesslisting.service.OffersCategoryListServiceOutput;
import streetdirectory.mobile.modules.businesslisting.service.OffersListingService;
import streetdirectory.mobile.modules.core.LocationBusinessServiceOutput;
import streetdirectory.mobile.modules.core.adapter.LocationBusinessTipsNearbyAdapter;
import streetdirectory.mobile.modules.imagepreview.LocationBusinessImagePreviewActivity;
import streetdirectory.mobile.modules.nearby.service.*;
import streetdirectory.mobile.modules.sdmob.SdMobHelper;
import streetdirectory.mobile.modules.sdmob.SmallBanner;
import streetdirectory.mobile.sd.SDMapSideMenuLayout;
import streetdirectory.mobile.sd.SdMob;
import streetdirectory.mobile.service.*;

// Referenced classes of package streetdirectory.mobile.modules.businesslisting:
//            BusinessListingAdapter

public class BusinessListingActivity extends SDActivity
{

    public BusinessListingActivity()
    {
        isShareListing = false;
        _listingImageServices = new ArrayList();
        _nearbyImageServices = new ArrayList();
        offersCategoryList = new ArrayList();
        adRequestRetryCount = 0;
        mFacebookCallback = new com.facebook.Session.StatusCallback() {

            public void call(Session session, SessionState sessionstate, Exception exception)
            {
                onSessionStateChange(session, sessionstate, exception);
            }

        };
    }

    private void abortDownloadListing()
    {
        if(_listingService != null)
        {
            _listingService.abort();
            _listingService = null;
        }
    }

    private void abortDownloadNearby()
    {
        if(_nearbyService != null)
        {
            _nearbyService.abort();
            _nearbyService = null;
        }
    }

    private void abortListingDownloadImage()
    {
        for(Iterator iterator = _listingImageServices.iterator(); iterator.hasNext(); ((SDHttpImageService)iterator.next()).abort());
        _listingImageServices.clear();
    }

    private void abortNearbyDownloadImage()
    {
        for(Iterator iterator = _nearbyImageServices.iterator(); iterator.hasNext(); ((SDHttpImageService)iterator.next()).abort());
        _nearbyImageServices.clear();
    }

    private void callButtonClicked(LocationBusinessServiceOutput locationbusinessserviceoutput, String s)
    {
        Intent intent = new Intent("android.intent.action.DIAL", Uri.parse((new StringBuilder()).append("tel:").append(locationbusinessserviceoutput.phoneNumber).toString()));
        HashMap hashmap = SDStory.createDefaultParams();
        hashmap.put("c_id", Integer.toString(locationbusinessserviceoutput.companyID));
        hashmap.put("c_pid", locationbusinessserviceoutput.phoneNumber);
        hashmap.put("opg", "business_listing_dir");
        SDStory.post(URLFactory.createGantBusinessCall((new StringBuilder()).append(locationbusinessserviceoutput.companyID).append("").toString()), hashmap);
        startActivity(intent);
    }

    private void categoryButtonClicked()
    {
        Intent intent = new Intent(this, BusinessFinderSubDirectoryActivity.class);
        intent.putExtra("countryCode", _countryCode);
        intent.putExtra("stateID", _stateID);
        intent.putExtra("menuName", _menuName);
        if(_stateName != null)
            intent.putExtra("detailTitle", (new StringBuilder()).append(_countryName).append(" >> ").append(_stateName).toString());
        else
            intent.putExtra("detailTitle", _countryName);
        intent.putExtra("menuID", _menuID);
        intent.putExtra("categoryID", _parentID);
        startActivityForResult(intent, 1);
        overridePendingTransition(0x10a0000, 0x10a0001);
    }

    private void downloadListing()
    {
        if(_listingService == null)
        {
            _noDataLayout.setVisibility(View.INVISIBLE);
            _listView.setVisibility(View.VISIBLE);
            int i = _listingAdapter.getItemSize();
            _listingService = new BusinessListingService(new BusinessListingServiceInput(_countryCode, _type, _categoryID, _stateID, i, _limit)) {

                public void onAborted(Exception exception)
                {
                    SDLogger.info("Business Listing Aborted");
                    _loadingIndicator.setVisibility(View.INVISIBLE);
                    _listingService = null;
                }

                public void onFailed(Exception exception)
                {
                    SDLogger.printStackTrace(exception, "Business Listing Failed");
                    _loadingIndicator.setVisibility(View.INVISIBLE);
                    _listingService = null;
                }

                public void onReceiveData(BusinessListingServiceOutput businesslistingserviceoutput)
                {
                    _listingAdapter.addItem(businesslistingserviceoutput);
                }

                public void onReceiveData(SDDataOutput sddataoutput)
                {
                    onReceiveData((BusinessListingServiceOutput)sddataoutput);
                }

                public void onReceiveTotal(long l)
                {
                    _listingAdapter.setTotalItem(l);
                }

                public void onSuccess(Object obj)
                {
                    onSuccess((SDHttpServiceOutput)obj);
                }

                public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
                {
                    SDLogger.info("Business Listing Success");
                    _listingAdapter.notifyDataSetChanged();
                    _loadingIndicator.setVisibility(View.INVISIBLE);
                    _listingService = null;
                }

            };
            if(i == 0)
                _loadingIndicator.setVisibility(View.VISIBLE);
            _listingService.executeAsync();
        }
    }

    private void downloadListingFacebookPhoto(final BusinessListingServiceOutput businesslistingserviceoutput, int i, int j)
    {
        Iterator iterator = _listingImageServices.iterator();
        for(;iterator.hasNext();) {
            if (((HttpImageService) iterator.next()).tag.equals(businesslistingserviceoutput.tipsUserID))
                return;
        }

        final String final_s = businesslistingserviceoutput.generateUserPhotoURL(i, j);
        if(final_s != null)
        {
            SDFacebookImageService sdFacebookImageService = new SDFacebookImageService(final_s, i, j) {

                public void onAborted(Exception exception)
                {
                    _listingImageServices.remove(this);
                }

                public void onFailed(Exception exception)
                {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.facebook_thumb);
                    _listingAdapter.putImage(businesslistingserviceoutput.tipsUserID, bitmap);
                    _listingImageServices.remove(this);
                    _listingAdapter.notifyDataSetChanged();
                }

                public void onSuccess(Bitmap bitmap)
                {
                    _listingAdapter.putImage(businesslistingserviceoutput.tipsUserID, bitmap);
                    _listingImageServices.remove(this);
                    _listingAdapter.notifyDataSetChanged();
                }

                public void onSuccess(Object obj)
                {
                    onSuccess((Bitmap) obj);
                }

            };

            sdFacebookImageService.tag = businesslistingserviceoutput.tipsUserID;
            _listingImageServices.add(sdFacebookImageService);
            sdFacebookImageService.executeAsync();
            return;
        }

    }

    private void downloadListingImage(final BusinessListingServiceOutput businesslistingserviceoutput, int i, int j)
    {
        Iterator iterator = _listingImageServices.iterator();
        for(;iterator.hasNext();) {
            if (((HttpImageService) iterator.next()).tag.equals(businesslistingserviceoutput.uniqueID))
                return;
        }

        final String final_s = businesslistingserviceoutput.generateImageURL(this, i, j);
        if(final_s != null)
        {
            SDHttpImageService sdHttpImageService = new SDHttpImageService(final_s) {

                public void onAborted(Exception exception)
                {
                    _listingImageServices.remove(this);
                }

                public void onFailed(Exception exception)
                {
                    _listingImageServices.remove(this);
                }

                public void onSuccess(Bitmap bitmap)
                {
                    _listingAdapter.putImage(businesslistingserviceoutput.uniqueID, bitmap);
                    _listingImageServices.remove(this);
                    _listingAdapter.notifyDataSetChanged();
                }

            };

            sdHttpImageService.tag = businesslistingserviceoutput.uniqueID;
            _listingImageServices.add(sdHttpImageService);
            sdHttpImageService.executeAsync();
            return;
        }

    }

    private void downloadNearby()
    {
        if(_nearbyService == null)
        {
            _noDataLayout.setVisibility(View.INVISIBLE);
            if(_selectedTabButton == _nearbyButton)
                _listView.setVisibility(View.VISIBLE);
            int i = _nearbyAdapter.getItemSize();
            _nearbyService = new NearbyService(new NearbyServiceInput(_countryCode, _type, _longitude, _latitude, _kmRange, i, _limit, _categoryID, 0)) {

                public void onAborted(Exception exception)
                {
                    _nearbyService = null;
                    SDLogger.info("Nearby Aborted");
                    _loadingIndicator.setVisibility(View.INVISIBLE);
                }

                public void onFailed(Exception exception)
                {
                    _nearbyService = null;
                    SDLogger.printStackTrace(exception, "Nearby Failed");
                    _loadingIndicator.setVisibility(View.INVISIBLE);
                    if(_nearbyAdapter.getItemSize() == 0)
                        _noDataLayout.setVisibility(View.VISIBLE);
                }

                public void onReceiveData(NearbyServiceOutput nearbyserviceoutput)
                {
                    _nearbyAdapter.addItem(nearbyserviceoutput);
                }

                public void onReceiveData(SDDataOutput sddataoutput)
                {
                    onReceiveData((NearbyServiceOutput)sddataoutput);
                }

                public void onReceiveTotal(long l)
                {
                    _nearbyAdapter.setTotalItem(l);
                }

                public void onSuccess(Object obj)
                {
                    onSuccess((SDHttpServiceOutput)obj);
                }

                public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
                {
                    _nearbyService = null;
                    SDLogger.info("Nearby Success");
                    _nearbyAdapter.notifyDataSetChanged();
                    _loadingIndicator.setVisibility(View.INVISIBLE);
                    if(_nearbyAdapter.getItemSize() == 0)
                        _noDataLayout.setVisibility(View.VISIBLE);
                }
            };
            if(_nearbyAdapter.getItemSize() == 0)
                _loadingIndicator.setVisibility(View.VISIBLE);
            _nearbyService.executeAsync();
        }
    }

    private void downloadNearbyFacebookPhoto(final NearbyServiceOutput nearbyserviceoutput, int i, int j)
    {
        Iterator iterator = _nearbyImageServices.iterator();
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
                    _nearbyImageServices.remove(this);
                }

                public void onFailed(Exception exception)
                {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.facebook_thumb);
                    _nearbyAdapter.putImage(nearbyserviceoutput.tipsUserID, bitmap);
                    _nearbyImageServices.remove(this);
                    _nearbyAdapter.notifyDataSetChanged();
                }

                public void onSuccess(Bitmap bitmap)
                {
                    _nearbyAdapter.putImage(nearbyserviceoutput.tipsUserID, bitmap);
                    _nearbyImageServices.remove(this);
                    _nearbyAdapter.notifyDataSetChanged();
                }

                public void onSuccess(Object obj)
                {
                    onSuccess((Bitmap)obj);
                }

            };

            sdFacebookImageService.tag = nearbyserviceoutput.tipsUserID;
            _nearbyImageServices.add(sdFacebookImageService);
            sdFacebookImageService.executeAsync();
            return;
        }

    }

    private void downloadNearbyImage(final NearbyServiceOutput nearbyserviceoutput, int i, int j)
    {
        Iterator iterator = _nearbyImageServices.iterator();
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
                    _nearbyImageServices.remove(this);
                }

                public void onFailed(Exception exception)
                {
                    _nearbyImageServices.remove(this);
                }

                public void onSuccess(Bitmap bitmap)
                {
                    _nearbyAdapter.putImage(nearbyserviceoutput.uniqueID, bitmap);
                    _nearbyImageServices.remove(this);
                    _nearbyAdapter.notifyDataSetChanged();
                }

                public void onSuccess(Object obj)
                {
                    onSuccess((Bitmap)obj);
                }

            };

            sdHttpImageService.tag = nearbyserviceoutput.uniqueID;
            _nearbyImageServices.add(sdHttpImageService);
            sdHttpImageService.executeAsync();
            return;
        }

    }

    private void downloadOffers()
    {
        int i = _listingAdapter.getCount();
        _offerService = new OffersListingService(_offerCategoryId, i, _limit, _countryCode) {

            public void onAborted(Exception exception)
            {
                SDLogger.info("Business Listing Aborted");
                _loadingIndicator.setVisibility(View.INVISIBLE);
                _listingService = null;
            }

            public void onFailed(Exception exception)
            {
                SDLogger.printStackTrace(exception, "Business Listing Failed");
                _loadingIndicator.setVisibility(View.INVISIBLE);
                _listingService = null;
            }

            public void onReceiveData(BusinessListingServiceOutput businesslistingserviceoutput)
            {
                _listingAdapter.addItem(businesslistingserviceoutput);
            }

            public void onReceiveData(SDDataOutput sddataoutput)
            {
                onReceiveData((BusinessListingServiceOutput)sddataoutput);
            }

            public void onReceiveTotal(long l)
            {
                _listingAdapter.setTotalItem(l);
            }

            public void onSuccess(Object obj)
            {
                onSuccess((SDHttpServiceOutput)obj);
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                SDLogger.info("Business Listing Success");
                _listingAdapter.notifyDataSetChanged();
                _loadingIndicator.setVisibility(View.INVISIBLE);
                _listingService = null;
            }
        };

        if(i == 0)
            _loadingIndicator.setVisibility(View.VISIBLE);
        _offerService.executeAsync();
    }

    private void downloadOffersCategoryListing()
    {
        if(_countryCode == null)
            _countryCode = "sg";
        offersCategoryService = new OffersCategoryListService(_countryCode) {

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
                offersCategoryList.addAll(sdhttpserviceoutput.childs);
                offersCategories = new String[offersCategoryList.size()];
                for(int i = 0; i < offersCategoryList.size(); i++)
                    offersCategories[i] = ((OffersCategoryListServiceOutput)offersCategoryList.get(i)).parentCategoryName;

            }

        };
        offersCategoryService.executeAsync();
    }

    private void initData()
    {
        Intent intent = getIntent();
        _countryCode = intent.getStringExtra("countryCode");
        _stateID = intent.getIntExtra("stateID", 0);
        _stateName = intent.getStringExtra("stateName");
        if(_stateName == null)
            _stateName = "All";
        _categoryID = intent.getIntExtra("categoryID", -1);
        isFromOffers = intent.getExtras().getBoolean("fromOffers", false);
        mTitle = intent.getStringExtra("categoryName");
        if(mTitle != null)
        {
            setCategoryButtonText(mTitle, _countryCode, _stateName);
            SDStory.post(URLFactory.createGantBusinessCategoryListing(mTitle, _categoryID), SDStory.createDefaultParams());
        }
        _type = intent.getIntExtra("type", 2);
        _longitude = intent.getDoubleExtra("longitude", 0.0D);
        _latitude = intent.getDoubleExtra("latitude", 0.0D);
        _kmRange = intent.getIntExtra("kmRange", 3);
        if(_kmRangeButton != null)
            _kmRangeButton.setText((new StringBuilder()).append("Within ").append(_kmRange).append(" Km").toString());
        _limit = intent.getIntExtra("limit", 10);
        _menuID = intent.getIntExtra("menuID", -1);
        _menuName = intent.getStringExtra("menuName");
        _parentID = intent.getIntExtra("parentID", -1);
        _countryName = intent.getStringExtra("countryName");
        _fromBottomBanner = intent.getBooleanExtra("fromBottomBanner", false);
        _fromMenu = intent.getBooleanExtra("fromMenu", false);
        _listingAdapter = new BusinessListingAdapter(this);
        _listView.setAdapter(_listingAdapter);
        _nearbyAdapter = new LocationBusinessTipsNearbyAdapter(this, _kmRange);
        if(_fromBottomBanner)
            nearbyButtonClicked();
        if(_fromMenu)
        {
            _menuButton.setVisibility(View.VISIBLE);
            _backButton.setVisibility(View.INVISIBLE);
            SDStory.post(URLFactory.createGantOffersMenu(), SDStory.createDefaultParams());
        }
        if(_categoryID == 11342)
        {
            isOffersListing = true;
            _categoryButton.setVisibility(View.INVISIBLE);
            _titleBar.setText("Offers near you");
            _listingAdapter.isOfferPage = true;
            _nearbyAdapter.isOfferPage = true;
        } else
        {
            _categoryButton.setVisibility(View.VISIBLE);
            _listingAdapter.isOfferPage = false;
            _nearbyAdapter.isOfferPage = false;
        }
        _offerCategoryId = -1;
    }

    private void initEvent()
    {
        _sideMenuLayout.setOnSlideClose(new Action() {

            public void execute()
            {
                _sideMenuBlocker.setVisibility(View.INVISIBLE);
            }

        });
        _sideMenuBlocker.setOnTouchListener(new android.view.View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionevent)
            {
                _sideMenuLayout.touchExecutor(motionevent);
                return true;
            }

        });
        _menuButton.setOnTouchListener(new android.view.View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionevent)
            {
                _sideMenuLayout.touchExecutor(motionevent);
                return false;
            }

        });

        _menuButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                if(_sideMenuLayout.getIsMenuOpen())
                {
                    _sideMenuLayout.slideClose();
                    return;
                } else
                {
                    _sideMenuLayout.slideOpen();
                    return;
                }
            }

        });
        _backButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                finish();
            }

        });
        _shareButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                SDLogger.info("Share Button Clicked");
                if(FacebookDialog.canPresentShareDialog(getApplicationContext(), new com.facebook.widget.FacebookDialog.ShareDialogFeature[] {
            com.facebook.widget.FacebookDialog.ShareDialogFeature.SHARE_DIALOG
        }))
                {
                    FacebookDialog facebookDialog = ((com.facebook.widget.FacebookDialog.ShareDialogBuilder)(new com.facebook.widget.FacebookDialog.ShareDialogBuilder(BusinessListingActivity.this)).setLink(URLFactory.createURLShareBusinessListing(_categoryID, mTitle))).build();
                    uiHelper.trackPendingDialogCall(facebookDialog.present());
                    return;
                }
                isShareListing = true;
                Session session = Session.getActiveSession();
                if(!session.isOpened() && !session.isClosed())
                {
                    session.openForRead((new com.facebook.Session.OpenRequest(BusinessListingActivity.this)).setPermissions(Arrays.asList(new String[] {
                        "public_profile"
                    })).setCallback(mFacebookCallback));
                    return;
                } else
                {
                    Session.openActiveSession(BusinessListingActivity.this, true, mFacebookCallback);
                    return;
                }
            }

        });
        if(_menuID != -1 && _menuName != null && _parentID != -1)
            _categoryButton.setOnClickListener(new android.view.View.OnClickListener() {

                public void onClick(View view)
                {
                    categoryButtonClicked();
                }

            });
        _kmRangeButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                kmRangeButtonClicked();
            }

        });
        _listingAdapter.setOnImageNotFoundListener(new streetdirectory.mobile.modules.core.adapter.LocationBusinessAdapter.OnImageNotFoundListener() {

            public void onImageNotFound(LocationBusinessServiceOutput locationbusinessserviceoutput, int i, int j, int k)
            {
                downloadListingImage((BusinessListingServiceOutput)locationbusinessserviceoutput, j, k);
            }

        });
        _listingAdapter.setOnLoadMoreListener(new streetdirectory.mobile.modules.core.adapter.LocationBusinessAdapter.OnLoadMoreListener() {

            public void onLoadMoreList()
            {
                if(isOffersListing)
                {
                    downloadOffers();
                    return;
                } else
                {
                    downloadListing();
                    return;
                }
            }

        });
        _listingAdapter.setOnTipsPhotoNotFoundListener(new streetdirectory.mobile.modules.core.adapter.LocationBusinessTipsAdapter.OnTipsPhotoNotFoundListener() {

            public void onTipsPhotoNotFound(int i, int j, int k)
            {
                downloadListingFacebookPhoto((BusinessListingServiceOutput)_listingAdapter.getItem(i), j, k);
            }

        });
        _listingAdapter.setOnImageClickedListener(new streetdirectory.mobile.modules.core.adapter.LocationBusinessAdapter.OnImageClickedListener() {

            public void onImageClicked(LocationBusinessServiceOutput locationbusinessserviceoutput, int i, Bitmap bitmap)
            {
                locationBusinessImageClicked(locationbusinessserviceoutput, bitmap);
            }

        });
        _listingAdapter.setOnCallButtonClickedListener(new streetdirectory.mobile.modules.core.adapter.LocationBusinessAdapter.OnCallButtonClickedListener() {

            public void onCallButtonClicked(LocationBusinessServiceOutput locationbusinessserviceoutput, int i, String s)
            {
                callButtonClicked(locationbusinessserviceoutput, s);
            }

        });
        _listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView adapterview, View view, int i, long l)
            {
                int j = 0;
                if(_listView.getAdapter() != _listingAdapter) {
                    if(_listView.getAdapter() == _nearbyAdapter)
                        j = _nearbyAdapter.getItemSize();
                } else {
                    j = _listingAdapter.getItemSize();
                }

                if(i >= j)
                    return;

                LocationBusinessServiceOutput temp;
                if(_selectedTabButton == _allButton)
                    temp = _listingAdapter.getItem(i);
                else
                    temp = _nearbyAdapter.getItem(i);
                startActivityBusinessDetail(temp);
                return;
            }

        });
        _nearbyAdapter.setOnImageNotFoundListener(new streetdirectory.mobile.modules.core.adapter.LocationBusinessAdapter.OnImageNotFoundListener() {

            public void onImageNotFound(LocationBusinessServiceOutput locationbusinessserviceoutput, int i, int j, int k)
            {
                downloadNearbyImage((NearbyServiceOutput)locationbusinessserviceoutput, j, k);
            }

        });
        _nearbyAdapter.setOnLoadMoreListener(new streetdirectory.mobile.modules.core.adapter.LocationBusinessAdapter.OnLoadMoreListener() {

            public void onLoadMoreList()
            {
                downloadNearby();
            }

        });
        _nearbyAdapter.setOnTipsPhotoNotFoundListener(new streetdirectory.mobile.modules.core.adapter.LocationBusinessTipsAdapter.OnTipsPhotoNotFoundListener() {

            public void onTipsPhotoNotFound(int i, int j, int k)
            {
                downloadNearbyFacebookPhoto((NearbyServiceOutput)_nearbyAdapter.getItem(i), j, k);
            }

        });
        _nearbyAdapter.setOnKmRangeClickListener(new streetdirectory.mobile.modules.core.adapter.LocationBusinessTipsNearbyAdapter.OnKmRangeClickListener() {

            public void onKmRangeClicked()
            {
                kmRangeButtonClicked();
            }

        });
        _nearbyAdapter.setOnImageClickedListener(new streetdirectory.mobile.modules.core.adapter.LocationBusinessAdapter.OnImageClickedListener() {

            public void onImageClicked(LocationBusinessServiceOutput locationbusinessserviceoutput, int i, Bitmap bitmap)
            {
                locationBusinessImageClicked(locationbusinessserviceoutput, bitmap);
            }

        });
        _nearbyAdapter.setOnCallButtonClickedListener(new streetdirectory.mobile.modules.core.adapter.LocationBusinessAdapter.OnCallButtonClickedListener() {

            public void onCallButtonClicked(LocationBusinessServiceOutput locationbusinessserviceoutput, int i, String s)
            {
                callButtonClicked(locationbusinessserviceoutput, s);
            }

        });
        _allButton.setOnTouchListener(new android.view.View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionevent)
            {
                    if(!_allButton.isPressed())
                    {
                        _allButton.setPressed(true);
                        _nearbyButton.setPressed(false);
                        _mapButton.setPressed(false);
                        _selectedTabButton = _allButton;
                        SDStory.post(URLFactory.createGantBusinessCategoryAll(mTitle), SDStory.createDefaultParams());
                        _noDataLayout.setVisibility(View.INVISIBLE);
                        _listView.setVisibility(View.VISIBLE);
                        _listView.setAdapter(_listingAdapter);
                        abortAllProcess();
                        if(_listingAdapter.getItemSize() == 0)
                        {
                            if(!isOffersListing) {
                                downloadListing();
                                return true;
                            }
                            downloadOffers();
                        }
                    }
                    return true;
            }

        });
        _nearbyButton.setOnTouchListener(new android.view.View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionevent)
            {
                nearbyButtonClicked();
                return true;
            }

        });
        _mapButton.setOnTouchListener(new android.view.View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionevent)
            {
                if(!_mapButton.isPressed())
                {
                    _allButton.setPressed(false);
                    _nearbyButton.setPressed(false);
                    _mapButton.setPressed(true);
                    _selectedTabButton = _mapButton;
                    _listView.setVisibility(View.INVISIBLE);
                    _noDataLayout.setVisibility(View.INVISIBLE);
                    abortAllProcess();
                    if(_nearbyAdapter.getItemSize() == 0)
                        downloadNearby();
                }
                return true;
            }

        });
    }

    private void initLayout()
    {
        _titleBar = (TextView)findViewById(R.id.TitleBar);
        _sideMenuLayout = (SDMapSideMenuLayout)findViewById(R.id.side_menu);
        _sideMenuBlocker = findViewById(R.id.side_menu_blocker);
        _menuButton = (ImageButton)findViewById(R.id.MenuButton);
        _backButton = (Button)findViewById(R.id.BackButton);
        _shareButton = (Button)findViewById(R.id.ShareButton);
        _categoryButton = (Button)findViewById(R.id.CategoryButton);
        _allButton = (Button)findViewById(R.id.AllButton);
        _nearbyButton = (Button)findViewById(R.id.NearbyButton);
        _mapButton = (Button)findViewById(R.id.MapButton);
        _listView = (ListView)findViewById(R.id.ListView);
        _loadingIndicator = (ProgressBar)findViewById(R.id.LoadingIndicator);
        _noDataLayout = (LinearLayout)findViewById(R.id.NoDataLayout);
        _kmRangeButton = (Button)_noDataLayout.findViewById(R.id.KmRangeButton);
        mSdSmallBanner = (RelativeLayout)findViewById(R.id.view_sdmob);
    }

    private void kmRangeButtonClicked()
    {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Select Range");
        final int lastKmRange = _kmRange;
        int i = _kmRange;
        android.content.DialogInterface.OnClickListener onclicklistener = new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialoginterface, int j)
            {
                _kmRange = j + 1;
            }

        };
        builder.setSingleChoiceItems(new String[]{
                "1 Km", "2 Km", "3 Km", "4 Km", "5 Km"
        }, i - 1, onclicklistener);
        builder.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialoginterface, int j)
            {
                if(_kmRange != lastKmRange)
                    refreshNearbyList();
            }

        });

        builder.setNegativeButton("Cancel", new android.content.DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialoginterface, int j) {
                        _kmRange = lastKmRange;
                    }

                }
        );
        builder.show();
    }

    private void loadSmallBanner()
    {
        Object obj = SmallBanner.getBannerFromSdMobUnit(this, SdMobHelper.getInstance(this).getSdMobUnit(SdMob.ad_bnr_business_listing));
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

    private void nearbyButtonClicked()
    {
        if(!_nearbyButton.isPressed())
        {
            _allButton.setPressed(false);
            _nearbyButton.setPressed(true);
            _mapButton.setPressed(false);
            _selectedTabButton = _nearbyButton;
            SDStory.post(URLFactory.createGantBusinessCategoryNearby(mTitle), SDStory.createDefaultParams());
            _listView.setVisibility(View.VISIBLE);
            _listView.setAdapter(_nearbyAdapter);
            abortAllProcess();
            if(_nearbyAdapter.getItemSize() == 0)
                downloadNearby();
        }
    }

    private void onSessionStateChange(Session session, SessionState sessionstate, Exception exception)
    {
        if(sessionstate.isOpened())
        {
            Log.i("MainActivityFaceBook", session.getAccessToken());
            publishFeedDialog();
            Request.newMeRequest(session, new com.facebook.Request.GraphUserCallback() {

                public void onCompleted(GraphUser graphuser, Response response)
                {
                }

            }).executeAsync();
        } else
        if(sessionstate.isClosed())
            return;
    }

    private void publishFeedDialog()
    {
        Bundle bundle = new Bundle();
        bundle.putString("name", (new StringBuilder()).append(mTitle).append(" | Streetdirectory.com").toString());
        bundle.putString("caption", "By Streetdirectory.com");
        bundle.putString("description", "Streetdirectory.com Business Finder allows users to search for Singapore private limited businesses or specific products and services.");
        bundle.putString("link", URLFactory.createURLShareBusinessListing(_categoryID, mTitle));
        ((com.facebook.widget.WebDialog.FeedDialogBuilder)(new WebDialog.FeedDialogBuilder(this, Session.getActiveSession(), bundle)).setOnCompleteListener(new com.facebook.widget.WebDialog.OnCompleteListener() {

            public void onComplete(Bundle bundle1, FacebookException facebookexception)
            {
                if(facebookexception == null)
                {
                    String temp = bundle1.getString("post_id");
                    if(temp != null)
                    {
                        Log.i("SD_Facebook", (new StringBuilder()).append("Posted successfully with ID : ").append(temp).toString());
                        return;
                    } else
                    {
                        Log.i("SD_Facebook", (new StringBuilder()).append("Share cancelled").append(bundle1).toString());
                        return;
                    }
                }
                if(facebookexception instanceof FacebookOperationCanceledException)
                {
                    Log.i("SD_Facebook", "Share cancelled");
                    return;
                } else
                {
                    Log.i("SD_Facebook", "Error share listing");
                    return;
                }
            }

        })).build().show();
    }

    private void refreshList()
    {
        _listingAdapter.clear();
        _nearbyAdapter.clear();
        abortAllProcess();
        if(_selectedTabButton == _allButton)
        {
            if(isOffersListing)
            {
                downloadOffers();
                return;
            } else
            {
                downloadListing();
                return;
            }
        } else
        {
            downloadNearby();
            return;
        }
    }

    private void refreshNearbyList()
    {
        _kmRangeButton.setText((new StringBuilder()).append("Within ").append(_kmRange).append(" Km").toString());
        _nearbyAdapter.setKmRange(_kmRange);
        _nearbyAdapter.clear();
        abortAllProcess();
        downloadNearby();
    }

    private void setCategoryButtonText(String s, String s1, String s2)
    {
        if(_categoryButton != null)
            _categoryButton.setText(Html.fromHtml((new StringBuilder()).append("<b>").append(s).append("</b> <font color=\"#555555\">(").append(s1.toUpperCase(Locale.ENGLISH)).append(" &gt;&gt; ").append(s2).append(")</font>").toString()));
    }

    private void startActivityBusinessDetail(LocationBusinessServiceOutput locationbusinessserviceoutput)
    {
        Intent intent = new Intent(this, BusinessDetailActivityV2.class);
        intent.putExtra("data", (Parcelable)locationbusinessserviceoutput);
        startActivity(intent);
    }

    protected void abortAllProcess()
    {
        abortAllProcess();
        abortDownloadListing();
        abortDownloadNearby();
        abortListingDownloadImage();
        abortNearbyDownloadImage();
    }

    public void finish()
    {
        abortAllProcess();
        finish();
    }

    protected void onActivityResult(int i, int j, Intent intent)
    {
        switch(i) {
            default:
                return;
            case 1:
                if (j == -1) {
                    i = intent.getIntExtra("categoryID", -1);
                    String temp  = intent.getStringExtra("categoryName");
                    if (i != -1 && temp != null && _categoryID != i) {
                        _categoryID = i;
                        setCategoryButtonText(temp, _countryCode, _stateName);
                        refreshList();
                        return;
                    }
                }
        }
    }

    protected void onCreate(Bundle bundle)
    {
        onCreate(bundle);
        setContentView(R.layout.activity_business_listing);
        uiHelper = new UiLifecycleHelper(this, null);
        uiHelper.onCreate(bundle);
        initLayout();
        initData();
        initEvent();
        if(isOffersListing)
            downloadOffers();
        else
            downloadListing();
        loadSmallBanner();
        downloadOffersCategoryListing();
    }

    protected void onDestroy()
    {
        abortAllProcess();
        onDestroy();
    }

    protected void onResume()
    {
        onResume();
        Session session = Session.getActiveSession();
        if(session != null && session.isOpened() && isShareListing)
        {
            isShareListing = false;
            onSessionStateChange(session, session.getState(), null);
        }
        uiHelper.onResume();
        if(_selectedTabButton == null)
            _selectedTabButton = _allButton;
        _selectedTabButton.setPressed(true);
    }

    private static final int BUSINESS_CATEGORY_SELECTED = 1;
    private static final int OFFERS_ALL_CATEGORY = -1;
    private static final int OFFERS_COUNT_LIMIT = 20;
    private Button _allButton;
    private Button _backButton;
    private Button _categoryButton;
    private int _categoryID;
    private String _countryCode;
    private String _countryName;
    private boolean _fromBottomBanner;
    private boolean _fromMenu;
    private int _kmRange;
    private Button _kmRangeButton;
    private double _latitude;
    private int _limit;
    private ListView _listView;
    private BusinessListingAdapter _listingAdapter;
    private ArrayList _listingImageServices;
    private BusinessListingService _listingService;
    private ProgressBar _loadingIndicator;
    private double _longitude;
    private Button _mapButton;
    private ImageButton _menuButton;
    private int _menuID;
    private String _menuName;
    private LocationBusinessTipsNearbyAdapter _nearbyAdapter;
    private Button _nearbyButton;
    private ArrayList _nearbyImageServices;
    private NearbyService _nearbyService;
    private LinearLayout _noDataLayout;
    private int _offerCategoryId;
    private OffersListingService _offerService;
    private int _parentID;
    private Button _selectedTabButton;
    private Button _shareButton;
    private View _sideMenuBlocker;
    private SDMapSideMenuLayout _sideMenuLayout;
    private int _stateID;
    private String _stateName;
    private TextView _titleBar;
    private int _type;
    private int adRequestRetryCount;
    private boolean isFromOffers;
    private boolean isOffersListing;
    private boolean isShareListing;
    private com.facebook.Session.StatusCallback mFacebookCallback;
    private RelativeLayout mSdSmallBanner;
    private String mTitle;
    private String offersCategories[];
    private ArrayList offersCategoryList;
    private OffersCategoryListService offersCategoryService;
    private UiLifecycleHelper uiHelper;
















/*
    static Button access$2002(BusinessListingActivity businesslistingactivity, Button button)
    {
        businesslistingactivity._selectedTabButton = button;
        return button;
    }

*/













/*
    static int access$3002(BusinessListingActivity businesslistingactivity, int i)
    {
        businesslistingactivity._kmRange = i;
        return i;
    }

*/




/*
    static BusinessListingService access$3302(BusinessListingActivity businesslistingactivity, BusinessListingService businesslistingservice)
    {
        businesslistingactivity._listingService = businesslistingservice;
        return businesslistingservice;
    }

*/


/*
    static NearbyService access$3402(BusinessListingActivity businesslistingactivity, NearbyService nearbyservice)
    {
        businesslistingactivity._nearbyService = nearbyservice;
        return nearbyservice;
    }

*/






/*
    static String[] access$3802(BusinessListingActivity businesslistingactivity, String as[])
    {
        businesslistingactivity.offersCategories = as;
        return as;
    }

*/



/*
    static int access$3902(BusinessListingActivity businesslistingactivity, int i)
    {
        businesslistingactivity.adRequestRetryCount = i;
        return i;
    }

*/


/*
    static int access$3908(BusinessListingActivity businesslistingactivity)
    {
        int i = businesslistingactivity.adRequestRetryCount;
        businesslistingactivity.adRequestRetryCount = i + 1;
        return i;
    }

*/





/*
    static boolean access$602(BusinessListingActivity businesslistingactivity, boolean flag)
    {
        businesslistingactivity.isShareListing = flag;
        return flag;
    }

*/



}
