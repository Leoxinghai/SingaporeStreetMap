// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.favorite;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.util.ArrayList;
import java.util.Iterator;
import streetdirectory.mobile.core.SDLogger;
import streetdirectory.mobile.core.ui.*;
import streetdirectory.mobile.gis.maps.MapView;
import streetdirectory.mobile.modules.SDActivity;
import streetdirectory.mobile.modules.favorite.service.FavoriteListService;
import streetdirectory.mobile.modules.favorite.service.FavoriteListServiceInput;
import streetdirectory.mobile.modules.favorite.service.FavoriteListServiceOutput;
import streetdirectory.mobile.modules.favorite.service.FavoriteNearbyListService;
import streetdirectory.mobile.modules.favorite.service.FavoriteNearbyListServiceInput;
import streetdirectory.mobile.modules.favorite.service.FavoriteNearbyListServiceOutput;
import streetdirectory.mobile.modules.favorite.service.FavoriteRouteListService;
import streetdirectory.mobile.modules.favorite.service.FavoriteRouteListServiceInput;
import streetdirectory.mobile.modules.favorite.service.FavoriteRouteListServiceOutput;
import streetdirectory.mobile.modules.favorite.service.FavoriteTipsUserService;
import streetdirectory.mobile.modules.favorite.service.FavoriteTipsUserServiceInput;
import streetdirectory.mobile.modules.favorite.service.FavoriteTipsUserServiceOutput;
import streetdirectory.mobile.modules.profile.service.*;
import streetdirectory.mobile.modules.tips.TipsCell;
import streetdirectory.mobile.modules.tips.TipsWithImageCell;
import streetdirectory.mobile.sd.SDBlackboard;
import streetdirectory.mobile.sd.SDMapSideMenuLayout;
import streetdirectory.mobile.service.*;
import streetdirectory.mobile.service.countrylist.CountryListServiceOutput;
import streetdirectory.mobile.service.statelist.StateListServiceOutput;

import static com.xinghai.mycurve.R.*;

// Referenced classes of package streetdirectory.mobile.modules.favorite:
//            FavoriteNearbyListCell, FavoritePlaceCell, FavoriteCell, FavoriteRouteCell

public class FavoriteActivity extends SDActivity
{

    public FavoriteActivity()
    {
        favoriteNearbyData = new ArrayList();
        tipsData = new ArrayList();
        favoriteListData = new ArrayList();
        favoriteRoutesData = new ArrayList();
        favoritePlaceData = new ArrayList();
        mStates = new ArrayList();
        mSelectedCountryStateIndex = 0;
        mStateID = 0;
        totalRoutes = 0L;
        totalBusinesses = 0L;
        totalPlaces = 0L;
        totalTips = 0L;
        imagePool = new SDHttpImageServicePool();
    }

    private void downloadFavoriteList(final int i)
    {
        mProgressBar.setVisibility(View.VISIBLE);
        if(i == 1)
        {
            if(favoritePlaceData.size() > 0)
            {
                mListViewFavorite.setAdapter(favoritePlaceAdapter);
                favoritePlaceAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.INVISIBLE);
                return;
            }
        } else
        if(i == 2 && favoriteListData.size() > 0)
        {
            mListViewFavorite.setAdapter(favoriteListAdapter);
            favoriteListAdapter.notifyDataSetChanged();
            mProgressBar.setVisibility(View.INVISIBLE);
            return;
        }
        mFavoriteListService = new FavoriteListService(null) {

            public void onAborted(Exception exception)
            {
                SDLogger.info("Favorite List In Aborted");
                mFavoriteListService = null;
            }

            public void onFailed(Exception exception)
            {
                SDLogger.printStackTrace(exception, "Favorite List In Failed");
                mFavoriteListService = null;
            }

            public void onProgress(int j)
            {
                mProgressBar.setProgress(j);
            }

            public void onSuccess(Object obj)
            {
                onSuccess((SDHttpServiceOutput)obj);
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                mProgressBar.setVisibility(View.INVISIBLE);
                if(i == 1)
                {
                    favoritePlaceAdapter.clear();
                    Iterator iterator = sdhttpserviceoutput.childs.iterator();
                    do
                    {
                        if(!iterator.hasNext())
                            break;
                        FavoriteListServiceOutput favoritelistserviceoutput1 = (FavoriteListServiceOutput)iterator.next();
                        final FavoritePlaceCell favoriteplacecell = new FavoritePlaceCell(favoritelistserviceoutput1);
                        favoritePlaceAdapter.add(favoriteplacecell);
                        if(favoritelistserviceoutput1.imageURL != null)
                            imagePool.queueRequest(URLFactory.createURLResizeImage(favoritelistserviceoutput1.imageURL, 128, 128), 128, 128, new streetdirectory.mobile.service.SDHttpImageServicePool.OnBitmapReceivedListener() {

                                public void bitmapReceived(String s, Bitmap bitmap)
                                {
                                    favoriteplacecell.imagePlace = bitmap;
                                    notifyAdapter();
                                }

                            });

                    } while(true);
                    favoritePlaceAdapter.notifyDataSetChanged();
                }
                if(i == 2)
                {
                    favoriteListAdapter.clear();
                    Iterator iterator = sdhttpserviceoutput.childs.iterator();
                    do
                    {
                        if(!iterator.hasNext())
                            break;
                        FavoriteListServiceOutput favoritelistserviceoutput = (FavoriteListServiceOutput)iterator.next();
                        final FavoriteCell favoritecell = new FavoriteCell(favoritelistserviceoutput);
                        favoritecell.context = FavoriteActivity.this;
                        favoriteListAdapter.add(favoritecell);
                        if(favoritelistserviceoutput.siteBannerURL != null)
                            imagePool.queueRequest(URLFactory.createURLResizeImage(favoritelistserviceoutput.siteBannerURL, 130, 130), 130, 130,  new streetdirectory.mobile.service.SDHttpImageServicePool.OnBitmapReceivedListener() {

                                public void bitmapReceived(String s, Bitmap bitmap)
                                {
                                    favoritecell.image = bitmap;
                                    notifyAdapter();
                                }
                            });
                        else
                        if(favoritelistserviceoutput.imageURL != null)
                            imagePool.queueRequest(URLFactory.createURLResizeImage(favoritelistserviceoutput.imageURL, 128, 128), 128, 128, new streetdirectory.mobile.service.SDHttpImageServicePool.OnBitmapReceivedListener() {

                                public void bitmapReceived(String s, Bitmap bitmap)
                                {
                                    favoritecell.image = bitmap;
                                    notifyAdapter();
                                }
                            });
                    } while(true);

                    favoriteListAdapter.notifyDataSetChanged();
                }
                mListViewFavorite.invalidate();
            }

        };
        mFavoriteListService.executeAsync();
    }

    private void downloadFavoriteNearby(int i)
    {
        mProgressBar.setVisibility(View.VISIBLE);
        if(favoriteNearbyData.size() > 0)
        {
            mListViewFavorite.setAdapter(favoriteNearbyListAdapter);
            favoriteNearbyListAdapter.notifyDataSetChanged();
            mProgressBar.setVisibility(View.INVISIBLE);
            return;
        } else
        {
            mFavoriteNearbyListService = new FavoriteNearbyListService(new FavoriteNearbyListServiceInput(mCountryCode, mUID, 5, i, SDBlackboard.currentLongitude, SDBlackboard.currentLatitude)) {

                public void onFailed(Exception exception)
                {
                    SDLogger.printStackTrace(exception, "Favorite Nearby List In Failed");
                    mFavoriteNearbyListService = null;
                }

                public void onProgress(int j)
                {
                    mProgressBar.setProgress(j);
                }

                public void onSuccess(Object obj)
                {
                    onSuccess((SDHttpServiceOutput)obj);
                }

                public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
                {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    Iterator iterator = sdhttpserviceoutput.childs.iterator();
                    do
                    {
                        if(!iterator.hasNext())
                            break;
                        FavoriteNearbyListServiceOutput favoritenearbylistserviceoutput = (FavoriteNearbyListServiceOutput)iterator.next();
                        final FavoriteNearbyListCell favoritenearbylistcell = new FavoriteNearbyListCell(favoritenearbylistserviceoutput);
                        favoriteNearbyListAdapter.add(favoritenearbylistcell);
                        if(favoritenearbylistserviceoutput.siteBannerURL != null)
                            imagePool.queueRequest(URLFactory.createURLResizeImage(favoritenearbylistserviceoutput.siteBannerURL, 130, 130), 130, 130,  new streetdirectory.mobile.service.SDHttpImageServicePool.OnBitmapReceivedListener() {

                                public void bitmapReceived(String s, Bitmap bitmap)
                                {
                                    favoritenearbylistcell.businessImage = bitmap;
                                    notifyAdapter();
                                }
                            });
                        else
                        if(favoritenearbylistserviceoutput.imageURL != null)
                            imagePool.queueRequest(URLFactory.createURLResizeImage(favoritenearbylistserviceoutput.imageURL, 130, 130), 130, 130,  new streetdirectory.mobile.service.SDHttpImageServicePool.OnBitmapReceivedListener() {

                                public void bitmapReceived(String s, Bitmap bitmap)
                                {
                                    favoritenearbylistcell.businessImage = bitmap;
                                    notifyAdapter();
                                }

                            });
                    } while(true);
                    favoriteNearbyListAdapter.notifyDataSetChanged();
                    mListViewFavorite.invalidate();
                }
            };
            mFavoriteNearbyListService.executeAsync();
            return;
        }
    }

    private void downloadRoutes()
    {
        mProgressBar.setVisibility(View.VISIBLE);
        if(favoriteRoutesData.size() > 0)
        {
            mListViewFavorite.setAdapter(favoriteRoutesAdapter);
            favoriteRoutesAdapter.notifyDataSetChanged();
            mProgressBar.setVisibility(View.INVISIBLE);
            return;
        } else
        {
            mFavoriteRouteService = new FavoriteRouteListService(new FavoriteRouteListServiceInput(mCountryCode, mUID)) {

                public void onFailed(Exception exception)
                {
                    SDLogger.printStackTrace(exception, "Favorite Routes In Failed");
                    mFavoriteTipsUserService = null;
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
                    favoriteRoutesAdapter.clear();
                    mProgressBar.setVisibility(View.INVISIBLE);
                    FavoriteRouteCell favoriteroutecell;
                    Iterator iterator;
                    for(iterator = sdhttpserviceoutput.childs.iterator(); iterator.hasNext(); favoriteRoutesAdapter.add(favoriteroutecell))
                        favoriteroutecell = new FavoriteRouteCell((FavoriteRouteListServiceOutput)iterator.next());

                    favoriteRoutesAdapter.notifyDataSetChanged();
                    mListViewFavorite.invalidate();
                }
            };
            mFavoriteRouteService.executeAsync();
            return;
        }
    }

    private void downloadUserInfo()
    {
        mProgressBar.setVisibility(View.VISIBLE);
        mUserInfoService = new UserInfoService(new UserInfoServiceInput(mCountryCode, mUID)) {

            public void onFailed(Exception exception)
            {
                onFailed(exception);
            }

            public void onSuccess(Object obj)
            {
                onSuccess((SDHttpServiceOutput) obj);
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                mProgressBar.setVisibility(View.INVISIBLE);
                onSuccess(sdhttpserviceoutput);
                if(sdhttpserviceoutput.childs.size() > 0)
                    mUserInfoData = sdhttpserviceoutput.childs;
                mUserInfoData.remove(0);
                setFavoriteCount();
            }

        };
        mUserInfoService.executeAsync();
    }

    private void downloadUserTips()
    {
        final int i = tipsData.size();
        if(i == 0)
            mProgressBar.setVisibility(View.VISIBLE);
        //mFavoriteTipsUserService = new FavoriteTipsUserService(i) {
        mFavoriteTipsUserService = new FavoriteTipsUserService(null) {
            public void onFailed(Exception exception)
            {
                SDLogger.printStackTrace(exception, "Favorite Tips User In Failed");
                mFavoriteTipsUserService = null;
            }

            public void onProgress(int j)
            {
                mProgressBar.setProgress(j);
            }

            public void onSuccess(Object obj)
            {
                onSuccess((SDHttpServiceOutput)obj);
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                mProgressBar.setVisibility(View.INVISIBLE);
                Iterator iterator = sdhttpserviceoutput.childs.iterator();
                do
                {
                    if(!iterator.hasNext())
                        break;
                    FavoriteTipsUserServiceOutput favoritetipsuserserviceoutput = (FavoriteTipsUserServiceOutput)iterator.next();
                    favoritetipsuserserviceoutput.name = mName;
                    final TipsWithImageCell tipswithimagecell = new TipsWithImageCell(favoritetipsuserserviceoutput);
                    tipsAdapter.add(tipswithimagecell);
                    String s = URLFactory.createURLFacebookPhoto(mUID, 48, 48);
                    imagePool.queueRequest(s, 48, 48,  new streetdirectory.mobile.service.SDHttpImageServicePool.OnBitmapReceivedListener() {

                        public void bitmapReceived(String s, Bitmap bitmap)
                        {
                            tipswithimagecell.userImage = bitmap;
                            notifyAdapter();
                        }

                    });
                    if(favoritetipsuserserviceoutput.favoriteImageURL != null)
                        imagePool.queueRequest(URLFactory.createURLResizeImage(favoritetipsuserserviceoutput.favoriteImageURL, 130, 130), 130, 130,  new streetdirectory.mobile.service.SDHttpImageServicePool.OnBitmapReceivedListener() {

                            public void bitmapReceived(String s, Bitmap bitmap)
                            {
                                tipswithimagecell.tipsImage = bitmap;
                                notifyAdapter();
                            }

                        });

                } while(true);
                if(sdhttpserviceoutput.total > (long)tipsData.size())
                    tipsData.add(loadMoreTips);
                else
                    tipsData.remove(loadMoreTips);
                tipsAdapter.notifyDataSetChanged();
                mListViewFavorite.setSelection(i);
            }

        };
        mFavoriteTipsUserService.executeAsync();
    }

    private void hideShowArrow(String s)
    {
        if(s.equals("place"))
        {
            mImageArrowBusiness.setVisibility(View.INVISIBLE);
            mImageArrowPlaces.setVisibility(View.VISIBLE);
            mImageArrowRoutes.setVisibility(View.INVISIBLE);
            mImageArrowTips.setVisibility(View.INVISIBLE);
        } else
        {
            if(s.equals("tips"))
            {
                mImageArrowBusiness.setVisibility(View.INVISIBLE);
                mImageArrowPlaces.setVisibility(View.INVISIBLE);
                mImageArrowRoutes.setVisibility(View.INVISIBLE);
                mImageArrowTips.setVisibility(View.VISIBLE);
                return;
            }
            if(s.equals("business"))
            {
                mImageArrowBusiness.setVisibility(View.VISIBLE);
                mImageArrowPlaces.setVisibility(View.INVISIBLE);
                mImageArrowRoutes.setVisibility(View.INVISIBLE);
                mImageArrowTips.setVisibility(View.INVISIBLE);
                return;
            }
            if(s.equals("routes"))
            {
                mImageArrowBusiness.setVisibility(View.INVISIBLE);
                mImageArrowPlaces.setVisibility(View.INVISIBLE);
                mImageArrowRoutes.setVisibility(View.VISIBLE);
                mImageArrowTips.setVisibility(View.INVISIBLE);
                return;
            }
        }
    }

    private void initData()
    {
        mCountryCode = getIntent().getStringExtra("countryCode");
        Iterator iterator = (new ArrayList(mSideMenu.countries)).iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            CountryListServiceOutput countrylistserviceoutput = (CountryListServiceOutput)iterator.next();
            if(countrylistserviceoutput.countryCode.equals(mCountryCode))
                mButtonCountry.setText(countrylistserviceoutput.countryName);
        } while(true);
        mUID = "100002297910526";
        mName = "Miku Mayumi";
        loadMoreTips = new LoadMoreCell();
        loadMoreTips.loadMoreListener = new streetdirectory.mobile.core.ui.LoadMoreCell.OnLoadMoreListener() {

            public void onLoadMoreList()
            {
                downloadUserTips();
            }

        };
        tipsAdapter = new SanListViewAdapter(this, 0, tipsData);
        favoriteNearbyListAdapter = new SanListViewAdapter(this, 0, favoriteNearbyData);
        favoriteListAdapter = new SanListViewAdapter(this, 0, favoriteListData);
        favoriteRoutesAdapter = new SanListViewAdapter(this, 0, favoriteRoutesData);
        favoritePlaceAdapter = new SanListViewAdapter(this, 0, favoritePlaceData);
        mListViewFavorite.setAdapter(favoriteListAdapter);
        downloadUserInfo();
        downloadFavoriteList(2);
        hideShowArrow("business");
    }

    private void initEvent()
    {
        mButtonCountry.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                showCountryStateDialog();
            }

        });
        mButtonAll.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                mButtonNearby.setSelected(false);
                mButtonMap.setSelected(false);
                mButtonAll.setSelected(true);
                mLayoutTutorial.setVisibility(View.INVISIBLE);
                mMapView.setVisibility(View.INVISIBLE);
                mListViewFavorite.setVisibility(View.VISIBLE);
                downloadFavoriteList(2);
                mListViewFavorite.invalidate();
            }

        });
        mButtonNearby.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                mButtonAll.setSelected(false);
                mButtonNearby.setSelected(true);
                mButtonMap.setSelected(false);
                mLayoutTutorial.setVisibility(View.INVISIBLE);
                mListViewFavorite.setVisibility(View.VISIBLE);
                mMapView.setVisibility(View.INVISIBLE);
                mListViewFavorite.setAdapter(favoriteNearbyListAdapter);
                if(!mLayoutBoxButtonBusiness.isSelected()) {
                    if(mLayoutBoxButtonPlace.isSelected())
                        downloadFavoriteNearby(1);
                } else {
                    downloadFavoriteNearby(2);
                }
                mListViewFavorite.invalidate();
                return;
            }

        });
        mLayoutBoxButtonPlace.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                hideShowArrow("place");
                setGraySelected(mLayoutBoxButtonPlaceGray);
                mLayoutFavoriteTab.setVisibility(View.VISIBLE);
                mListViewFavorite.setVisibility(View.VISIBLE);
                mMapView.setVisibility(View.INVISIBLE);
                mLayoutTutorial.setVisibility(View.INVISIBLE);
                mLayoutBoxButtonTips.setSelected(false);
                mLayoutBoxButtonBusiness.setSelected(false);
                mLayoutBoxButtonPlace.setSelected(true);
                mButtonNearby.setSelected(false);
                mButtonAll.setSelected(true);
                mLayoutBoxButtonRoutes.setSelected(false);
                mListViewFavorite.setAdapter(favoritePlaceAdapter);
                downloadFavoriteList(1);
                mListViewFavorite.invalidate();
            }

        });
        mLayoutBoxButtonBusiness.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                hideShowArrow("business");
                setGraySelected(mLayoutBoxButtonBusinessGray);
                mButtonAll.setSelected(true);
                mButtonNearby.setSelected(false);
                mLayoutBoxButtonTips.setSelected(false);
                mLayoutBoxButtonBusiness.setSelected(true);
                mLayoutBoxButtonPlace.setSelected(false);
                mLayoutBoxButtonRoutes.setSelected(false);
                mLayoutTutorial.setVisibility(View.INVISIBLE);
                mLayoutFavoriteTab.setVisibility(View.VISIBLE);
                mListViewFavorite.setVisibility(View.VISIBLE);
                mMapView.setVisibility(View.INVISIBLE);
                mListViewFavorite.setAdapter(favoriteListAdapter);
                downloadFavoriteList(2);
                mListViewFavorite.invalidate();
            }

        });
        mLayoutBoxButtonRoutes.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                hideShowArrow("routes");
                setGraySelected(mLayoutBoxButtonRoutesGray);
                mLayoutBoxButtonRoutes.setSelected(true);
                mLayoutBoxButtonTips.setSelected(false);
                mLayoutBoxButtonBusiness.setSelected(false);
                mLayoutBoxButtonPlace.setSelected(false);
                mListViewFavorite.setAdapter(favoriteRoutesAdapter);
                if(totalRoutes < 1L)
                {
                    mLayoutFavoriteTab.setVisibility(View.INVISIBLE);
                    mListViewFavorite.setVisibility(View.INVISIBLE);
                    mLayoutTutorial.setVisibility(View.VISIBLE);
                    mMapView.setVisibility(View.INVISIBLE);
                } else
                {
                    downloadRoutes();
                    mLayoutFavoriteTab.setVisibility(View.INVISIBLE);
                    mListViewFavorite.setVisibility(View.VISIBLE);
                    mLayoutTutorial.setVisibility(View.INVISIBLE);
                    mMapView.setVisibility(View.INVISIBLE);
                }
                mListViewFavorite.invalidate();
            }

        });
        mLayoutBoxButtonTips.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                hideShowArrow("tips");
                setGraySelected(mLayoutBoxButtonTipsGray);
                mLayoutBoxButtonTips.setSelected(true);
                mLayoutBoxButtonRoutes.setSelected(false);
                mLayoutBoxButtonBusiness.setSelected(false);
                mLayoutBoxButtonPlace.setSelected(false);
                mLayoutFavoriteTab.setVisibility(View.INVISIBLE);
                mLayoutTutorial.setVisibility(View.INVISIBLE);
                mListViewFavorite.setVisibility(View.VISIBLE);
                mMapView.setVisibility(View.INVISIBLE);
                mListViewFavorite.setAdapter(tipsAdapter);
                if(tipsData.size() > 0)
                {
                    tipsAdapter.notifyDataSetChanged();
                    mProgressBar.setVisibility(View.INVISIBLE);
                } else
                {
                    downloadUserTips();
                }
                mListViewFavorite.invalidate();
            }

        });
        mMenuButton.setOnTouchListener(new android.view.View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionevent)
            {
                mSideMenu.touchExecutor(motionevent);
                return false;
            }

        });
        mMenuButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                if(mSideMenu.getIsMenuOpen())
                {
                    mSideMenu.slideClose();
                    return;
                } else
                {
                    mSideMenu.slideOpen();
                    return;
                }
            }

        });
        mListViewFavorite.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView adapterview, View view, int i, long l)
            {
                ((SanListViewItem)mListViewFavorite.getAdapter().getItem(i)).execute(FavoriteActivity.this, (SanListViewAdapter)mListViewFavorite.getAdapter());
            }

        });
    }

    private void initLayout()
    {
        mProgressBar = (ProgressBar)findViewById(R.id.progress_bar_favorites);
        mButtonCountry = (Button)findViewById(R.id.button_country);
        mMenuButton = (ImageButton)findViewById(R.id.MenuButton);
        mSideMenu = (SDMapSideMenuLayout)findViewById(R.id.side_menu);
        mLayoutMenubar = (RelativeLayout)findViewById(R.id.layout_menubar);
        mTextviewTitle = (TextView)findViewById(R.id.textview_title);
        mLayoutTutorial = findViewById(R.id.layout_tutorial_favorites);
        mLayoutBoxButton = (LinearLayout)findViewById(R.id.layout_box_button);
        mLayoutBoxButtonBusiness = findViewById(R.id.layout_box_button_business);
        mLayoutBoxButtonBusinessGray = mLayoutBoxButtonBusiness.findViewById(R.id.box_grey);
        mTextViewBusiness = (TextView)mLayoutBoxButtonBusiness.findViewById(R.id.textViewCategory);
        mTextViewTotalBusinesses = (TextView)mLayoutBoxButtonBusiness.findViewById(R.id.textViewCount);
        mImageArrowBusiness = (ImageView)mLayoutBoxButtonBusiness.findViewById(R.id.imageViewArrow);
        mLayoutBoxButtonPlace = findViewById(R.id.layout_box_button_place);
        mLayoutBoxButtonPlaceGray = mLayoutBoxButtonPlace.findViewById(R.id.box_grey);
        mTextViewPlaces = (TextView)mLayoutBoxButtonPlace.findViewById(R.id.textViewCategory);
        mTextViewTotalPlaces = (TextView)mLayoutBoxButtonPlace.findViewById(R.id.textViewCount);
        mImageArrowPlaces = (ImageView)mLayoutBoxButtonPlace.findViewById(R.id.imageViewArrow);
        mLayoutBoxButtonTips = findViewById(R.id.layout_box_button_tips);
        mLayoutBoxButtonTipsGray = mLayoutBoxButtonTips.findViewById(R.id.box_grey);
        mTextViewTips = (TextView)mLayoutBoxButtonTips.findViewById(R.id.textViewCategory);
        mTextViewTotalTips = (TextView)mLayoutBoxButtonTips.findViewById(R.id.textViewCount);
        mImageArrowTips = (ImageView)mLayoutBoxButtonTips.findViewById(R.id.imageViewArrow);
        mLayoutBoxButtonRoutes = findViewById(R.id.layout_box_button_routes);
        mLayoutBoxButtonRoutesGray = mLayoutBoxButtonRoutes.findViewById(R.id.box_grey);
        mTextViewRoutes = (TextView)mLayoutBoxButtonRoutes.findViewById(R.id.textViewCategory);
        mTextViewTotalRoutes = (TextView)mLayoutBoxButtonRoutes.findViewById(R.id.textViewCount);
        mImageArrowRoutes = (ImageView)mLayoutBoxButtonRoutes.findViewById(R.id.imageViewArrow);
        mListViewFavorite = (ListView)findViewById(R.id.list_view_favorite);
        mMapView = (MapView)findViewById(R.id.MapView);
        mLayoutFavoriteTab = (LinearLayout)findViewById(R.id.layout_favorite_tab);
        mButtonAll = (Button)findViewById(R.id.button_all);
        mButtonNearby = (Button)findViewById(R.id.button_nearby);
        mButtonMap = (Button)findViewById(R.id.button_map);
        mLayoutLogin = findViewById(R.id.layout_login);
        mLayoutBoxButtonBusiness.setSelected(true);
        mButtonAll.setSelected(true);
        mLayoutBoxButtonTips.setClickable(true);
        mLayoutBoxButtonPlace.setClickable(true);
        mLayoutBoxButtonRoutes.setClickable(true);
        mLayoutBoxButtonBusiness.setClickable(true);
        mLayoutBoxButtonRoutes.setClickable(true);
        mProgressBar.setVisibility(View.INVISIBLE);
        mMapView.setVisibility(View.INVISIBLE);
        mLayoutTutorial.setVisibility(View.INVISIBLE);
        mListViewFavorite.setVisibility(View.VISIBLE);
        mSideMenu = (SDMapSideMenuLayout)findViewById(R.id.side_menu);
        mTextViewBusiness.setText("Businesses");
        mTextViewPlaces.setText("Places");
        mTextViewRoutes.setText("Routes");
        mTextViewTips.setText("Tips");
    }

    private void initialize()
    {
        initLayout();
        initData();
        initEvent();
    }

    private void refreshList()
    {
        if(!mLayoutBoxButtonPlace.isSelected()) {
            if(mLayoutBoxButtonBusiness.isSelected())
            {
                if(mButtonAll.isSelected())
                    downloadFavoriteList(2);
                else
                if(mButtonNearby.isSelected())
                    downloadFavoriteNearby(2);
            } else
            if(mLayoutBoxButtonRoutes.isSelected())
            {
                if(totalRoutes < 1L)
                {
                    mLayoutFavoriteTab.setVisibility(View.INVISIBLE);
                    mListViewFavorite.setVisibility(View.INVISIBLE);
                    mLayoutTutorial.setVisibility(View.VISIBLE);
                    mMapView.setVisibility(View.INVISIBLE);
                } else
                {
                    downloadRoutes();
                    mLayoutFavoriteTab.setVisibility(View.INVISIBLE);
                    mListViewFavorite.setVisibility(View.VISIBLE);
                    mLayoutTutorial.setVisibility(View.INVISIBLE);
                    mMapView.setVisibility(View.INVISIBLE);
                }
            } else
            if(mLayoutBoxButtonTips.isSelected())
                downloadUserTips();
        } else {
            if (!mButtonAll.isSelected()){
                if(mButtonNearby.isSelected())
                    downloadFavoriteNearby(1);
            } else {
                downloadFavoriteList(1);
            }
        }

        mListViewFavorite.invalidate();
        return;
    }

    private void setFavoriteCount()
    {
        Iterator iterator = mUserInfoData.iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            UserInfoCountryServiceOutput userinfocountryserviceoutput = (UserInfoCountryServiceOutput)(UserInfoServiceOutput)iterator.next();
            if(userinfocountryserviceoutput.countryCode.equals(mCountryCode))
            {
                totalBusinesses = userinfocountryserviceoutput.totalLikeBusiness;
                totalPlaces = userinfocountryserviceoutput.totalFavoritePlace;
                totalTips = userinfocountryserviceoutput.totalTips;
                totalRoutes = userinfocountryserviceoutput.totalFavoriteRoute;
                mTextViewTotalBusinesses.setText(String.valueOf(userinfocountryserviceoutput.totalLikeBusiness));
                mTextViewTotalPlaces.setText(String.valueOf(userinfocountryserviceoutput.totalFavoritePlace));
                mTextViewTotalRoutes.setText(String.valueOf(userinfocountryserviceoutput.totalFavoriteRoute));
                mTextViewTotalTips.setText(String.valueOf(userinfocountryserviceoutput.totalTips));
                refreshList();
            }
        } while(true);
    }

    private void setGraySelected(View view)
    {
        mLayoutBoxButtonBusinessGray.setSelected(false);
        mLayoutBoxButtonRoutesGray.setSelected(false);
        mLayoutBoxButtonPlaceGray.setSelected(false);
        mLayoutBoxButtonTipsGray.setSelected(false);
        view.setSelected(true);
    }

    private void showCountryStateDialog()
    {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Select Country");
        mSelectedCountryStateIndex = 0;
        final ArrayList countryState = new ArrayList();
        ArrayList arraylist = new ArrayList();
        Object obj = new ArrayList(mSideMenu.countries);
        ArrayList arraylist1 = new ArrayList();
        ArrayList arraylist2 = new ArrayList(mStates);
        String s = mButtonCountry.getText().toString();
        int j = arraylist2.size();
        for(obj = ((ArrayList) (obj)).iterator(); ((Iterator) (obj)).hasNext();)
        {
            CountryListServiceOutput countrylistserviceoutput = (CountryListServiceOutput)((Iterator) (obj)).next();
            if(countrylistserviceoutput.countryCode.equals(mCountryCode))
            {
                for(int i = 0; i < j; i++)
                {
                    StateListServiceOutput statelistserviceoutput = (StateListServiceOutput)arraylist2.get(i);
                    arraylist1.add((new StringBuilder()).append(" - ").append(statelistserviceoutput.stateName).toString());
                    if(statelistserviceoutput.stateName.equals(s))
                        mSelectedCountryStateIndex = i + 1;
                }

                arraylist.addAll(0, arraylist1);
                countryState.addAll(0, arraylist2);
                arraylist.add(0, countrylistserviceoutput.countryName);
                countryState.add(0, countrylistserviceoutput);
            } else
            {
                arraylist.add(countrylistserviceoutput.countryName);
                countryState.add(countrylistserviceoutput);
            }
        }

        final int lastSelectedIndex = mSelectedCountryStateIndex;
        builder.setSingleChoiceItems((CharSequence[])arraylist.toArray(new String[0]), mSelectedCountryStateIndex, new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialoginterface, int k)
            {
                mSelectedCountryStateIndex = k;
            }

        });
        builder.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialoginterface, int k)
            {
                SDDataOutput temp;
                if(mSelectedCountryStateIndex != lastSelectedIndex)
                {
                    temp = (SDDataOutput)countryState.get(mSelectedCountryStateIndex);
                    if(temp instanceof CountryListServiceOutput)
                    {
                        temp = (CountryListServiceOutput)temp;
                        mCountryCode = ((CountryListServiceOutput) (temp)).countryCode;
                        mCountryName = ((CountryListServiceOutput) (temp)).countryName;
                        mStateID = 0;
                        mStateName = null;
                        mButtonCountry.setText(mCountryName);
                        favoriteListAdapter.clear();
                        favoriteNearbyListAdapter.clear();
                        tipsAdapter.clear();
                        favoriteRoutesAdapter.clear();
                        favoritePlaceAdapter.clear();
                        notifyAdapter();
                        downloadUserInfo();
                    } else
                    if(temp instanceof StateListServiceOutput)
                    {
                        temp = (StateListServiceOutput)temp;
                        mStateID = ((StateListServiceOutput) (temp)).stateID;
                        mStateName = ((StateListServiceOutput) (temp)).stateName;
                        return;
                    }
                }
            }

        });
        builder.setNegativeButton("Cancel", new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialoginterface, int k)
            {
                mSelectedCountryStateIndex = lastSelectedIndex;
            }

        });
        builder.show();
    }

    public void notifyAdapter()
    {
        mListViewFavorite.post(new Runnable() {

            public void run()
            {
                tipsAdapter.notifyDataSetChanged();
                favoriteListAdapter.notifyDataSetChanged();
                favoriteNearbyListAdapter.notifyDataSetChanged();
                favoriteRoutesAdapter.notifyDataSetChanged();
                favoritePlaceAdapter.notifyDataSetChanged();
            }

        });
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_favorite);
        initialize();
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        if(mSideMenu.getIsMenuOpen())
            mSideMenu.slideClose();
        else
            mSideMenu.slideOpen();
        return false;
    }

    SanListViewAdapter favoriteListAdapter;
    ArrayList favoriteListData;
    ArrayList favoriteNearbyData;
    SanListViewAdapter favoriteNearbyListAdapter;
    SanListViewAdapter favoritePlaceAdapter;
    ArrayList favoritePlaceData;
    SanListViewAdapter favoriteRoutesAdapter;
    ArrayList favoriteRoutesData;
    SDHttpImageServicePool imagePool;
    private LoadMoreCell loadMoreTips;
    private Button mButtonAll;
    private Button mButtonCountry;
    private Button mButtonMap;
    private Button mButtonNearby;
    private String mCountryCode;
    private String mCountryName;
    private FavoriteListServiceOutput mData;
    private ArrayList mFavoriteListImageService;
    private FavoriteListService mFavoriteListService;
    private FavoriteNearbyListService mFavoriteNearbyListService;
    private FavoriteListService mFavoritePlaceService;
    private FavoriteRouteListService mFavoriteRouteService;
    private FavoriteTipsUserService mFavoriteTipsUserService;
    private ImageView mImageArrowBusiness;
    private ImageView mImageArrowPlaces;
    private ImageView mImageArrowRoutes;
    private ImageView mImageArrowTips;
    private LinearLayout mLayoutBoxButton;
    private View mLayoutBoxButtonBusiness;
    private View mLayoutBoxButtonBusinessGray;
    private View mLayoutBoxButtonPlace;
    private View mLayoutBoxButtonPlaceGray;
    private View mLayoutBoxButtonRoutes;
    private View mLayoutBoxButtonRoutesGray;
    private View mLayoutBoxButtonTips;
    private View mLayoutBoxButtonTipsGray;
    private LinearLayout mLayoutFavoriteTab;
    private View mLayoutLogin;
    private RelativeLayout mLayoutMenubar;
    private View mLayoutTutorial;
    private View mLayoutTutorialDirection;
    private ListView mListViewFavorite;
    private MapView mMapView;
    private ImageButton mMenuButton;
    private String mName;
    private SDHttpImageService mPhotoImageService;
    private ProgressBar mProgressBar;
    private int mSelectedCountryStateIndex;
    private SDMapSideMenuLayout mSideMenu;
    private int mStateID;
    private String mStateName;
    private ArrayList mStates;
    private TextView mTextViewBusiness;
    private TextView mTextViewPlaces;
    private TextView mTextViewRoutes;
    private TextView mTextViewTips;
    private TextView mTextViewTotalBusinesses;
    private TextView mTextViewTotalPlaces;
    private TextView mTextViewTotalRoutes;
    private TextView mTextViewTotalTips;
    private TextView mTextviewTitle;
    private String mUID;
    private ArrayList mUserInfoData;
    private UserInfoService mUserInfoService;
    SanListViewAdapter tipsAdapter;
    ArrayList tipsData;
    private long totalBusinesses;
    private long totalPlaces;
    private long totalRoutes;
    private long totalTips;

    static
    {
        SanListViewItem.addTypeCount(LoadMoreCell.class);
        SanListViewItem.addTypeCount(FavoriteNearbyListCell.class);
        SanListViewItem.addTypeCount(FavoritePlaceCell.class);
        SanListViewItem.addTypeCount(FavoriteCell.class);
        SanListViewItem.addTypeCount(FavoriteRouteCell.class);
        SanListViewItem.addTypeCount(TipsCell.class);
        SanListViewItem.addTypeCount(TipsWithImageCell.class);
    }




















/*
    static FavoriteNearbyListService access$2502(FavoriteActivity favoriteactivity, FavoriteNearbyListService favoritenearbylistservice)
    {
        favoriteactivity.mFavoriteNearbyListService = favoritenearbylistservice;
        return favoritenearbylistservice;
    }

*/


/*
    static FavoriteTipsUserService access$2602(FavoriteActivity favoriteactivity, FavoriteTipsUserService favoritetipsuserservice)
    {
        favoriteactivity.mFavoriteTipsUserService = favoritetipsuserservice;
        return favoritetipsuserservice;
    }

*/






/*
    static FavoriteListService access$3002(FavoriteActivity favoriteactivity, FavoriteListService favoritelistservice)
    {
        favoriteactivity.mFavoriteListService = favoritelistservice;
        return favoritelistservice;
    }

*/



/*
    static ArrayList access$3102(FavoriteActivity favoriteactivity, ArrayList arraylist)
    {
        favoriteactivity.mUserInfoData = arraylist;
        return arraylist;
    }

*/




/*
    static int access$3302(FavoriteActivity favoriteactivity, int i)
    {
        favoriteactivity.mSelectedCountryStateIndex = i;
        return i;
    }

*/


/*
    static String access$3402(FavoriteActivity favoriteactivity, String s)
    {
        favoriteactivity.mCountryCode = s;
        return s;
    }

*/



/*
    static String access$3502(FavoriteActivity favoriteactivity, String s)
    {
        favoriteactivity.mCountryName = s;
        return s;
    }

*/


/*
    static int access$3602(FavoriteActivity favoriteactivity, int i)
    {
        favoriteactivity.mStateID = i;
        return i;
    }

*/


/*
    static String access$3702(FavoriteActivity favoriteactivity, String s)
    {
        favoriteactivity.mStateName = s;
        return s;
    }

*/








}
