
package streetdirectory.mobile.modules.businesslisting.offers;

import android.app.AlertDialog;
import android.content.*;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
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

import java.util.ArrayList;
import java.util.Arrays;
import streetdirectory.mobile.core.*;
import streetdirectory.mobile.gis.gps.LocationService;
import streetdirectory.mobile.gis.maps.MapPinLayer;
import streetdirectory.mobile.gis.maps.MapView;
import streetdirectory.mobile.gis.maps.configs.MapPresetCollection;
import streetdirectory.mobile.modules.SDActivity;
import streetdirectory.mobile.modules.businessdetail.BusinessDetailActivityV2;
import streetdirectory.mobile.modules.businesslisting.service.*;
import streetdirectory.mobile.modules.core.LocationBusinessServiceOutput;
import streetdirectory.mobile.modules.map.MapActivity;
import streetdirectory.mobile.modules.nearby.service.*;
import streetdirectory.mobile.modules.sdmob.SdMobHelper;
import streetdirectory.mobile.modules.sdmob.SmallBanner;
import streetdirectory.mobile.sd.*;
import streetdirectory.mobile.service.*;


public class OffersListingActivity extends SDActivity
    implements android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener
{

    public OffersListingActivity()
    {
        selection = 0;
        isMenuVisible = true;
        offersCategoryList = new ArrayList();
        isShareListing = false;
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

    private void downloadNearby()
    {
        if(_nearbyService == null)
        {
            _noDataLayout.setVisibility(View.INVISIBLE);
            if(_selectedTabLayout == _nearbyLayout)
                _swipeRefreshLayout.setVisibility(View.VISIBLE);
            int i = _offersListingNearbyAdapter.getItemSize();
            _nearbyService = new NearbyService(new NearbyServiceInput(_countryCode, _type, _longitude, _latitude, _kmRange, i, _limit, _categoryID, 0, _offerCategoryId)) {

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
                    if(_offersListingNearbyAdapter.getItemSize() == 0 && _selectedTabLayout != _mapLayout)
                        _noDataLayout.setVisibility(View.VISIBLE);
                }

                public void onReceiveData(NearbyServiceOutput nearbyserviceoutput)
                {
                    _offersListingNearbyAdapter.addItem(nearbyserviceoutput);
                }

                public void onReceiveData(SDDataOutput sddataoutput)
                {
                    onReceiveData((NearbyServiceOutput)sddataoutput);
                }

                public void onReceiveTotal(long l)
                {
                    _offersListingNearbyAdapter.setTotalItem(l);
                    _offersListingAdapter.setTotalItem(l);
                    if(l < 1L)
                        showDialog((new StringBuilder()).append("Oops, No ").append(offersCategories[selection]).append(" Offers Nearby").toString());
                }

                public void onSuccess(Object obj)
                {
                    onSuccess((SDHttpServiceOutput)obj);
                }

                public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
                {
                    _swipeRefreshLayout.setRefreshing(false);
                    _nearbyService = null;
                    SDLogger.info("Nearby Success");
                    _loadingIndicator.setVisibility(View.INVISIBLE);
                    _offersListingNearbyAdapter.notifyDataSetChanged();
                    if(_offersListingNearbyAdapter.getItemSize() == 0 && _selectedTabLayout != _mapLayout)
                        _noDataLayout.setVisibility(View.VISIBLE);
                    else
                    if(sdhttpserviceoutput.childs.size() > 0)
                    {
                        NearbyServiceOutput nearbyserviceoutput = (NearbyServiceOutput)sdhttpserviceoutput.childs.get(0);
                        if(nearbyserviceoutput != null)
                        {
                            _mapView.goTo(nearbyserviceoutput.longitude, nearbyserviceoutput.latitude, 12);
                            _mapView.redraw();
                        }
                        _businessListingLayer.nearbyData.addAll(sdhttpserviceoutput.childs);
                        _businessListingLayer.invalidate();
                        return;
                    }
                }

            };
            if(_offersListingNearbyAdapter.getItemSize() == 0)
                _loadingIndicator.setVisibility(View.VISIBLE);
            _nearbyService.executeAsync();
        }
    }

    private void downloadOffers()
    {
        int i = _offersListingAdapter.getCount();
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
                _offersListingAdapter.addItem(businesslistingserviceoutput);
            }

            public void onReceiveData(SDDataOutput sddataoutput)
            {
                onReceiveData((BusinessListingServiceOutput)sddataoutput);
            }

            public void onReceiveTotal(long l)
            {
                _offersListingAdapter.setTotalItem(l);
                if(l < 1L)
                    showDialog((new StringBuilder()).append("Oops, No ").append(offersCategories[selection]).append(" Offers").toString());
            }

            public void onSuccess(Object obj)
            {
                onSuccess((SDHttpServiceOutput)obj);
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                SDLogger.info("Business Listing Success");
                _swipeRefreshLayout.setRefreshing(false);
                _offersListingAdapter.notifyDataSetChanged();
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
                offersCategoryList.clear();
                offersCategoryList.addAll(sdhttpserviceoutput.childs);
                offersCategories = new String[offersCategoryList.size()];
                offersCategories[0] = "All";
                for(int i = 1; i < offersCategoryList.size(); i++)
                    offersCategories[i] = ((OffersCategoryListServiceOutput)offersCategoryList.get(i)).parentCategoryName;

                categoryAdapter = new ArrayAdapter(OffersListingActivity.this, R.layout.offer_category_checked_textview, offersCategories);
                mListViewCategory.setAdapter(categoryAdapter);
                mListViewCategory.setItemChecked(0, true);
                mListViewCategory.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView adapterview, View view, int i, long l)
                    {
                        selection = i;
                        _offersListingAdapter.clear();
                        _offersListingAdapter.notifyDataSetChanged();
                        if(i == 0 && offersCategories[i].equals("All"))
                            _offerCategoryId = -1;
                        else
                            _offerCategoryId = ((OffersCategoryListServiceOutput)offersCategoryList.get(i)).parentCategoryId;
                        mListViewCategory.setItemChecked(i, true);
                        String temp;

                        if(_offerCategoryId == -1)
                            temp = "all";
                        else
                            temp = String.valueOf(_offerCategoryId);
                        SDStory.post(URLFactory.createGantOffersListingPickCategory(temp), SDStory.createDefaultParams());
                        refreshList();
                        mViewCategoryDialog.setVisibility(View.INVISIBLE);
                    }

                });

                mProgressBarDialog.setVisibility(View.INVISIBLE);
            }

        };
        offersCategoryService.executeAsync();
    }

    private void initData()
    {
        locationService = LocationService.getInstance();
        Intent intent = getIntent();
        _countryCode = intent.getStringExtra("countryCode");
        _stateID = intent.getIntExtra("stateID", 0);
        _stateName = intent.getStringExtra("stateName");
        if(_stateName == null)
            _stateName = "All";
        _categoryID = intent.getIntExtra("categoryID", -1);
        isBottomBanner = intent.getBooleanExtra("isBottomBanner", false);
        _type = intent.getIntExtra("type", 2);
        _longitude = intent.getDoubleExtra("longitude", 0.0D);
        _latitude = intent.getDoubleExtra("latitude", 0.0D);
        _kmRange = intent.getIntExtra("kmRange", 2);
        if(_kmRangeButton != null)
            _kmRangeButton.setText((new StringBuilder()).append("Within ").append(_kmRange).append(" Km").toString());
        _limit = intent.getIntExtra("limit", 10);
        _menuID = intent.getIntExtra("menuID", -1);
        _menuName = intent.getStringExtra("menuName");
        _parentID = intent.getIntExtra("parentID", -1);
        _countryName = intent.getStringExtra("countryName");
        _fromBottomBanner = intent.getBooleanExtra("fromBottomBanner", false);
        _fromMenu = intent.getBooleanExtra("fromMenu", false);
        _offersListingAdapter = new OffersListingAdapter(this);
        _listView.setAdapter(_offersListingAdapter);
        _offersListingNearbyAdapter = new OffersListingNearbyAdapter(this, _kmRange);
        if(_fromBottomBanner)
            nearbyButtonClicked();
        if(_fromMenu)
        {
            _menuButton.setVisibility(View.VISIBLE);
            _backButton.setVisibility(View.INVISIBLE);
            SDStory.post(URLFactory.createGantOffersMenu(), SDStory.createDefaultParams());
        }
        _offerCategoryId = -1;
        _pinLayer = new MapPinLayer(this);
        _pinLayer.setVisibility(4);
        _businessListingLayer = new BusinessListingLayer(this) {

            protected void onPinIconClicked(NearbyServiceOutput nearbyserviceoutput)
            {
                super.onPinIconClicked(nearbyserviceoutput);
                if(_pinLayer.latitude == nearbyserviceoutput.latitude && _pinLayer.longitude == nearbyserviceoutput.longitude && _pinLayer.getVisibility() == 0)
                {
                    _pinLayer.showBallonVisibility(8, nearbyserviceoutput);
                } else
                {
                    _pinLayer.latitude = nearbyserviceoutput.latitude;
                    _pinLayer.longitude = nearbyserviceoutput.longitude;
                    _pinLayer.setTitle(nearbyserviceoutput.venue);
                    _pinLayer.showBallonVisibility(0, nearbyserviceoutput);
                }
                _mapView.redraw();
            }
        };
        _mapView.addLayer(_businessListingLayer);
        _mapView.addLayer(_pinLayer);
        if(SDBlackboard.preset != null)
        {
            _mapView.setPreset(SDBlackboard.preset);
            _mapView.redraw();
            return;
        } else
        {
            SDBlackboard.reloadMapPreset(new streetdirectory.mobile.sd.SDBlackboard.LoadMapPresetCompleteListener() {

                protected void onComplete(MapPresetCollection mappresetcollection)
                {
                    _mapView.setPreset(SDBlackboard.preset);
                    _mapView.redraw();
                }

            });
            return;
        }
    }

    private void initEvent()
    {
        _categoryButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                mViewCategoryDialog.setVisibility(View.VISIBLE);
            }

        });
        mButtonCloseDialog.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                mViewCategoryDialog.setVisibility(View.INVISIBLE);
            }

        });
        _pinLayer.setOnTapListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                if(view.getTag() instanceof NearbyServiceOutput)
                {
                    NearbyServiceOutput temp = (NearbyServiceOutput)view.getTag();
                    startActivityBusinessDetail(temp);
                }
            }

        });
        _sideMenuLayout.setOnSlideOpen(new Action() {

            public void execute()
            {
                _sideMenuBlocker.setVisibility(View.VISIBLE);
            }

        });
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
                SDStory.post(URLFactory.createGantOffersListingFacebookShare(), SDStory.createDefaultParams());
                if(FacebookDialog.canPresentShareDialog(getApplicationContext(), new com.facebook.widget.FacebookDialog.ShareDialogFeature[] {
            com.facebook.widget.FacebookDialog.ShareDialogFeature.SHARE_DIALOG
        }))

                {
                    FacebookDialog temp  = ((com.facebook.widget.FacebookDialog.ShareDialogBuilder)(new com.facebook.widget.FacebookDialog.ShareDialogBuilder(OffersListingActivity.this)).setLink("http://www.streetdirectory.com/businessfinder/company/11342/Offers/")).build();
                    uiHelper.trackPendingDialogCall(temp.present());
                    return;
                }
                isShareListing = true;
                Session temp2 = Session.getActiveSession();
                if(!temp2.isOpened() && !temp2.isClosed())
                {
                    temp2.openForRead((new com.facebook.Session.OpenRequest(OffersListingActivity.this)).setPermissions(Arrays.asList(new String[] {
                        "public_profile"
                    })).setCallback(mFacebookCallback));
                    return;
                } else
                {
                    Session.openActiveSession(OffersListingActivity.this, true, mFacebookCallback);
                    return;
                }
            }

        });
        _shareButton.setVisibility(View.VISIBLE);
        _kmRangeButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                kmRangeButtonClicked();
            }

        });
        _offersListingAdapter.setOnLoadMoreListener(new streetdirectory.mobile.modules.businesslisting.service.OffersListingAdapter.OnLoadMoreListener() {

            public void onLoadMoreList()
            {
                downloadOffers();
            }

        });
        _listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView adapterview, View view, int i, long l)
            {
                int j = 0;
                if(_listView.getAdapter() != _offersListingAdapter) {
                    if(_listView.getAdapter() == _offersListingNearbyAdapter)
                        j = _offersListingNearbyAdapter.getItemSize();
                } else {
                    j = _offersListingAdapter.getItemSize();
                }

                if(i >= j)
                    return;

                String temp;
                LocationBusinessServiceOutput temp2;

                if(_selectedTabLayout == _allLayout)
                    temp2 = _offersListingAdapter.getItem(i);
                else
                    temp2 = _offersListingNearbyAdapter.getItem(i);
                if(_offerCategoryId == -1)
                    temp = "all";
                else
                    temp = String.valueOf(_offerCategoryId);
                SDStory.post(URLFactory.createGantOffersListingItem(temp, ((LocationBusinessServiceOutput) (temp2)).promoId, String.valueOf(((LocationBusinessServiceOutput) (temp2)).companyID)), SDStory.createDefaultParams());
                startActivityBusinessDetail(temp2);
                return;
            }
        });
        _offersListingNearbyAdapter.setOnLoadMoreListener(new streetdirectory.mobile.modules.businesslisting.service.OffersListingAdapter.OnLoadMoreListener() {

            public void onLoadMoreList()
            {
                downloadNearby();
            }

        });
        _offersListingNearbyAdapter.setOnKmRangeClickListener(new streetdirectory.mobile.modules.businesslisting.service.OffersListingNearbyAdapter.OnKmRangeClickListener() {

            public void onKmRangeClicked()
            {
                kmRangeButtonClicked();
            }

        });
        _allLayout.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                if(_selectedTabLayout != _allLayout)
                {
                    setSelectedTab(_allLayout);
                    SDStory.post(URLFactory.createGantBusinessCategoryAll(mTitle), SDStory.createDefaultParams());
                    _noDataLayout.setVisibility(View.INVISIBLE);
                    _swipeRefreshLayout.setVisibility(View.VISIBLE);
                    _mapView.setVisibility(View.INVISIBLE);
                    _listView.setAdapter(_offersListingAdapter);
                    abortAllProcess();
                    if(_offersListingAdapter.getItemSize() == 0)
                        downloadOffers();
                }
            }

        });

        _nearbyLayout.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                nearbyButtonClicked();
            }

        });
        _mapLayout.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                if(_selectedTabLayout != _mapLayout)
                {
                    setSelectedTab(_mapLayout);
                    _swipeRefreshLayout.setVisibility(View.INVISIBLE);
                    _noDataLayout.setVisibility(View.INVISIBLE);
                    _mapView.setVisibility(View.VISIBLE);
                    abortAllProcess();
                    _mapView.redraw();
                    if(_offersListingNearbyAdapter.getItemSize() == 0)
                        downloadNearby();
                }
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
        _categoryButton = (LinearLayout)findViewById(R.id.layoutOffersCategory);
        _allLayout = (LinearLayout)findViewById(R.id.layoutOffersAll);
        _nearbyLayout = (LinearLayout)findViewById(R.id.layoutOffersNearby);
        _mapLayout = (LinearLayout)findViewById(R.id.layoutOffersMap);
        _swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.refresher);
        _swipeRefreshLayout.setOnRefreshListener(this);
        _swipeRefreshLayout.setColorScheme(new int[] {
            R.color.green_business_bar, R.color.red_direction, R.color.purple_direction, R.color.blue_direction
        });
        _listView = (ListView)findViewById(R.id.ListView);
        _mapView = (MapView)findViewById(R.id.MapView);
        _loadingIndicator = (ProgressBar)findViewById(R.id.LoadingIndicator);
        _noDataLayout = (LinearLayout)findViewById(R.id.NoDataLayout);
        _kmRangeButton = (Button)_noDataLayout.findViewById(R.id.KmRangeButton);
        _imageViewCategory = (ImageView)findViewById(R.id.imageViewCategory);
        _imageViewNearby = (ImageView)findViewById(R.id.imageViewNearby);
        _imageViewMap = (ImageView)findViewById(R.id.imageViewMap);
        _textViewAll = (TextView)findViewById(R.id.textViewAll);
        _textViewNearby = (TextView)findViewById(R.id.textViewNearby);
        _textViewMap = (TextView)findViewById(R.id.textViewMap);
        mViewCategoryDialog = findViewById(R.id.layout_offers_category_dialog);
        mListViewCategory = (ListView)findViewById(R.id.listView_offers_category);
        mListViewCategory.setChoiceMode(1);
        mButtonCloseDialog = (Button)findViewById(R.id.button_close_dialog);
        mProgressBarDialog = (ProgressBar)findViewById(R.id.progressBar_dialog);
        mSdSmallBanner = (RelativeLayout)findViewById(R.id.view_sdmob);
    }

    private boolean isNetworkProviderEnabled()
    {
        return ((LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled("network");
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
        Object obj = SmallBanner.getBannerFromSdMobUnit(this, SdMobHelper.getInstance(this).getSdMobUnit(SdMob.ad_bnr_offer_listing));
//        ((SmallBanner) (obj)).setAdMobSmallBannerListener(new streetdirectory.mobile.modules.sdmob.SmallBanner.AdMobSmallBannerListener() );
        obj = ((SmallBanner) (obj)).getView(this);
        mSdSmallBanner.removeAllViews();
        mSdSmallBanner.addView(((View) (obj)), new ViewGroup.LayoutParams(-1, -1));
    }

    private void nearbyButtonClicked()
    {
        if(_selectedTabLayout != _nearbyLayout)
        {
            setSelectedTab(_nearbyLayout);
            SDStory.post(URLFactory.createGantBusinessCategoryNearby(mTitle), SDStory.createDefaultParams());
            _swipeRefreshLayout.setVisibility(View.VISIBLE);
            _mapView.setVisibility(View.INVISIBLE);
            _listView.setAdapter(_offersListingNearbyAdapter);
            abortAllProcess();
            if(_offersListingNearbyAdapter.getItemSize() == 0)
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
        bundle.putString("name", "Offers Near You & Promotions in Singapore");
        bundle.putString("caption", "By Streetdirectory.com");
        bundle.putString("description", "Streetdirectory.com Business Finder allows users to search for Singapore private limited businesses or specific products and services.");
        bundle.putString("link", URLFactory.createURLShareBusinessListing(_categoryID, ""));
        bundle.putString("picture", "http://m1.sdimgs.com/sd_static/a/214461/1200-x-628.jpg");
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
        _offersListingAdapter.clear();
        _offersListingNearbyAdapter.clear();
        _businessListingLayer.nearbyData.clear();
        abortAllProcess();
        if(_selectedTabLayout == _allLayout)
        {
            downloadOffers();
            return;
        } else
        {
            downloadNearby();
            return;
        }
    }

    private void refreshNearbyList()
    {
        _kmRangeButton.setText((new StringBuilder()).append("Within ").append(_kmRange).append(" Km").toString());
        _offersListingNearbyAdapter.setKmRange(_kmRange);
        _offersListingNearbyAdapter.clear();
        _businessListingLayer.nearbyData.clear();
        abortAllProcess();
        downloadNearby();
    }

    private void setSelectedTab(LinearLayout linearlayout)
    {
        _selectedTabLayout = linearlayout;
        _imageViewCategory.setSelected(false);
        _imageViewNearby.setSelected(false);
        _imageViewMap.setSelected(false);
        _textViewAll.setSelected(false);
        _textViewNearby.setSelected(false);
        _textViewMap.setSelected(false);
        String temp;
        if(linearlayout == _allLayout)
        {
            _textViewAll.setSelected(true);
            if(_offerCategoryId == -1)
                temp = "All";
            else
                temp = String.valueOf(_offerCategoryId);
            SDStory.post(URLFactory.createGantOffersListingAllTab(temp), SDStory.createDefaultParams());
        } else
        {
            if(linearlayout == _nearbyLayout)
            {
                _textViewNearby.setSelected(true);
                _imageViewNearby.setSelected(true);
                if(_offerCategoryId == -1)
                    temp = "All";
                else
                    temp = String.valueOf(_offerCategoryId);
                SDStory.post(URLFactory.createGantOffersListingNearbyTab(temp), SDStory.createDefaultParams());
                return;
            }
            if(linearlayout == _mapLayout)
            {
                _textViewMap.setSelected(true);
                _imageViewMap.setSelected(true);
                if(_offerCategoryId == -1)
                    temp = "All";
                else
                    temp = String.valueOf(_offerCategoryId);
                SDStory.post(URLFactory.createGantOffersListingMapTab(temp
                ), SDStory.createDefaultParams());
                return;
            }
        }
    }

    private void showDialog(String s)
    {
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(s).setCancelable(false).setPositiveButton("OK", null);
        builder.show().show();
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
        intent.putExtra("offer_id", locationbusinessserviceoutput.promoId);
        startActivity(intent);
    }

    protected void abortAllProcess()
    {
        abortAllProcess();
        abortDownloadListing();
        abortDownloadNearby();
    }

    public void finish()
    {
        abortAllProcess();
        finish();
    }

    protected void onActivityResult(int i, int j, Intent intent)
    {
        Session.getActiveSession().onActivityResult(this, i, j, intent);
        switch(i) {
            default:
                return;
            case 1:
                if (j == -1) {
                    i = intent.getIntExtra("categoryID", -1);
                    String temp = intent.getStringExtra("categoryName");
                    if (i != -1 && temp != null && _categoryID != i) {
                        _categoryID = i;
                        refreshList();
                        return;
                    }
                }
                break;
        }
    }

    public void onBackPressed()
    {
        if(isBottomBanner)
        {
            finish();
            return;
        }
        if(!isMenuVisible)
        {
            finish();
            return;
        }
        if(_sideMenuLayout.getIsMenuOpen())
        {
            Intent intent = new Intent(this, MapActivity.class);
            intent.setFlags(0x4000000);
            intent.putExtra("EXIT", true);
            startActivity(intent);
            finish();
            return;
        } else
        {
            _sideMenuLayout.slideOpen();
            return;
        }
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_offers_listing);
        SDStory.post(URLFactory.createGantOffersListingPage(), SDStory.createDefaultParams());
        uiHelper = new UiLifecycleHelper(this, null);
        uiHelper.onCreate(bundle);
        initLayout();
        initData();
        initEvent();
        loadSmallBanner();
        downloadOffersCategoryListing();
        downloadOffers();
    }

    protected void onDestroy()
    {
        super.onDestroy();
        abortAllProcess();
        uiHelper.onDestroy();
    }

    public void onRefresh()
    {
        if(!isNetworkProviderEnabled())
        {
            _swipeRefreshLayout.setRefreshing(false);
        } else
        {
            if(!locationService.isEnable)
                locationService.enable();
            if(locationService != null && locationService.lastValidLocation != null)
            {
                _longitude = locationService.lastValidLocation.getLongitude();
                _latitude = locationService.lastValidLocation.getLatitude();
                if(locationService.isEnable)
                    locationService.disable();
                if(_selectedTabLayout == _allLayout)
                {
                    downloadOffers();
                    return;
                } else
                {
                    downloadNearby();
                    return;
                }
            }
        }
    }

    protected void onResume()
    {
        super.onResume();
        Session session = Session.getActiveSession();
        if(session != null && session.isOpened() && isShareListing)
        {
            isShareListing = false;
            onSessionStateChange(session, session.getState(), null);
        }
        uiHelper.onResume();
        if(_selectedTabLayout == null)
        {
            _selectedTabLayout = _allLayout;
            setSelectedTab(_allLayout);
        }
    }

    private static final int BUSINESS_CATEGORY_SELECTED = 1;
    private static final int OFFERS_ALL_CATEGORY = -1;
    private LinearLayout _allLayout;
    private Button _backButton;
    private BusinessListingLayer _businessListingLayer;
    private LinearLayout _categoryButton;
    private int _categoryID;
    private String _countryCode;
    private String _countryName;
    private boolean _fromBottomBanner;
    private boolean _fromMenu;
    private ImageView _imageViewCategory;
    private ImageView _imageViewMap;
    private ImageView _imageViewNearby;
    private int _kmRange;
    private Button _kmRangeButton;
    private double _latitude;
    private int _limit;
    private ListView _listView;
    private BusinessListingService _listingService;
    private ProgressBar _loadingIndicator;
    private double _longitude;
    private LinearLayout _mapLayout;
    private MapView _mapView;
    private ImageButton _menuButton;
    private int _menuID;
    private String _menuName;
    private LinearLayout _nearbyLayout;
    private NearbyService _nearbyService;
    private LinearLayout _noDataLayout;
    private int _offerCategoryId;
    private OffersListingService _offerService;
    private OffersListingAdapter _offersListingAdapter;
    private OffersListingNearbyAdapter _offersListingNearbyAdapter;
    private int _parentID;
    private MapPinLayer _pinLayer;
    private LinearLayout _selectedTabLayout;
    private Button _shareButton;
    private View _sideMenuBlocker;
    private SDMapSideMenuLayout _sideMenuLayout;
    private int _stateID;
    private String _stateName;
    private SwipeRefreshLayout _swipeRefreshLayout;
    private TextView _textViewAll;
    private TextView _textViewMap;
    private TextView _textViewNearby;
    private TextView _titleBar;
    private int _type;
    private int adRequestRetryCount;
    private ArrayAdapter categoryAdapter;
    private boolean isBottomBanner;
    private boolean isMenuVisible;
    private boolean isShareListing;
    private LocationService locationService;
    private Button mButtonCloseDialog;
    private com.facebook.Session.StatusCallback mFacebookCallback;
    private ListView mListViewCategory;
    private ProgressBar mProgressBarDialog;
    private RelativeLayout mSdSmallBanner;
    private String mTitle;
    private View mViewCategoryDialog;
    private String offersCategories[] = {
        "All"
    };
    private ArrayList offersCategoryList;
    private OffersCategoryListService offersCategoryService;
    private int selection;
    private UiLifecycleHelper uiHelper;

}
