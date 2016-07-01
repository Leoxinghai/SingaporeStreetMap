// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.direction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.util.ArrayList;
import streetdirectory.mobile.core.SDLogger;
import streetdirectory.mobile.gis.GeoPoint;
import streetdirectory.mobile.gis.gps.LocationService;
import streetdirectory.mobile.modules.SDActivity;
import streetdirectory.mobile.modules.location.service.*;
import streetdirectory.mobile.modules.nearby.NearbySearchAdapter;
import streetdirectory.mobile.modules.nearby.service.NearbyService;
import streetdirectory.mobile.modules.nearby.service.NearbyServiceInput;
import streetdirectory.mobile.modules.search.SearchListAdapter;
import streetdirectory.mobile.modules.search.service.SearchService;
import streetdirectory.mobile.modules.search.service.SearchServiceInput;
import streetdirectory.mobile.sd.SDBlackboard;
import streetdirectory.mobile.service.*;

public class DirectionFromToActivity extends SDActivity
{

    public DirectionFromToActivity()
    {
        currentLocationItem = null;
    }

    private void downloadCurrentLocation()
    {
        if(mLocationService == null)
        {
            mLocationService = LocationService.getInstance();
            LocationService.listeners.remove(locationServiceListener);
            locationServiceListener = new streetdirectory.mobile.gis.gps.LocationService.PositionChangedListener() {

                public void onPositionChanged(LocationService locationservice, GeoPoint geopoint)
                {
                    mLocationService.disable();
                    if(mCurrentLocationService != null)
                    {
                        mCurrentLocationService.abort();
                        mCurrentLocationService = null;
                    }
                    CurrentLocationServiceInput temp = new CurrentLocationServiceInput(geopoint.longitude, geopoint.latitude);
                    mCurrentLocationService = new CurrentLocationService(temp) {

                        public void onFailed(Exception exception)
                        {
                            onFailed(exception);
                        }

                        public void onSuccess(Object obj)
                        {
                            onSuccess((SDHttpServiceOutput)obj);
                        }

                        public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
                        {
                            onSuccess(sdhttpserviceoutput);
                            if(sdhttpserviceoutput.childs.size() > 0)
                            {
                                currentLocationItem = (CurrentLocationServiceOutput)sdhttpserviceoutput.childs.get(0);
                                if(currentLocationItem.venue != null)
                                    mTextViewCurrentLocation.setText(currentLocationItem.venue);
                                if(mTreeObserver == null && mImageWidth == 0 && mImageHeight == 0)
                                {
                                    mTreeObserver = mImageViewCurrentLocationMap.getViewTreeObserver();
                                    mTreeObserver.addOnGlobalLayoutListener(new android.view.ViewTreeObserver.OnGlobalLayoutListener() {

                                        public void onGlobalLayout()
                                        {
                                            mImageViewCurrentLocationMap.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                                            mTreeObserver = null;
                                            mImageWidth = mImageViewCurrentLocationMap.getWidth();
                                            mImageHeight = mImageViewCurrentLocationMap.getHeight();
                                            downloadMapImage(URLFactory.createURLMapImage(currentLocationItem.longitude, currentLocationItem.latitude, mImageWidth, mImageHeight, 11), mImageWidth, mImageHeight);
                                        }

                                    });
                                }
                            }
                        }

                    };
                    mCurrentLocationService.executeAsync();
                }

            };
            LocationService.listeners.add(locationServiceListener);
            mLocationService.initialize(this);
            mLocationService.enable();
        }
    }

    private void downloadMapImage(String s, int i, int j)
    {
        if(mImageServiceMap == null)
        {
            SDHttpImageService temp = new SDHttpImageService(s, i, j) {

                public void onAborted(Exception exception)
                {
                    mImageServiceMap = null;
                }

                public void onFailed(Exception exception)
                {
                    mImageServiceMap = null;
                }

                public void onSuccess(Bitmap bitmap)
                {
                    mImageServiceMap = null;
                    mImageViewCurrentLocationMap.setImageBitmap(bitmap);
                    mTextViewLoading.setVisibility(4);
                }

            };
            mImageServiceMap = temp;
            temp.executeAsync();
        }
    }

    private void downloadNearbyData()
    {
        if(mNearbyService == null)
        {
            mNearbyService = new NearbyService(new NearbyServiceInput(mCountryCode, 1, SDBlackboard.currentLongitude, SDBlackboard.currentLatitude, 5F, mSearchAdapter.getCount(), 10, 0, 0)) {

                public void onAborted(Exception exception)
                {
                    mNearbyService = null;
                    SDLogger.info("Nearby Aborted");
                }

                public void onFailed(Exception exception)
                {
                    mNearbyService = null;
                    SDLogger.printStackTrace(exception, "Nearby Failed");
                }

                public void onSuccess(Object obj)
                {
                    onSuccess((SDHttpServiceOutput)obj);
                }

                public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
                {
                    mNearbyService = null;
                    SDLogger.info("Nearby Success");
                    mNearbySearchAdapter.clear();
                    if(sdhttpserviceoutput.childs.size() > 0)
                        mNearbySearchAdapter.setData(sdhttpserviceoutput.childs);
                    mNearbySearchAdapter.notifyDataSetChanged();
                }

            };
            mNearbyService.executeAsync();
        }
    }

    private void downloadSearch(String s)
    {
        mProgressbar.setVisibility(View.VISIBLE);
        if(mSearchService != null)
        {
            mSearchService.abort();
            mSearchService = null;
        }
        mSearchService = new SearchService(new SearchServiceInput(mCountryCode, s, false)) {

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
                mProgressbar.setVisibility(View.INVISIBLE);
                super.onSuccess(sdhttpserviceoutput);
                mSearchAdapter.clear();
                if(sdhttpserviceoutput.childs.size() > 0)
                    mSearchAdapter.setData(sdhttpserviceoutput.childs);
                mSearchAdapter.notifyDataSetChanged();
            }

        };
        mSearchService.executeAsync();
    }

    private void initData()
    {
        Intent intent = getIntent();
        mCountryCode = intent.getStringExtra("countryCode");
        if(mCountryCode != null && !mCountryCode.equals(SDBlackboard.currentCountryCode))
            mCountryCode = SDBlackboard.currentCountryCode;
        if(intent.getIntExtra("requestCode", 0) == 0)
            mTitleBar.setText("Direction From");
        else
            mTitleBar.setText("Direction To");
        mSearchAdapter = new SearchListAdapter(this);
        mNearbySearchAdapter = new NearbySearchAdapter(this);
        mListView.setAdapter(mNearbySearchAdapter);
    }

    private void initEvent()
    {
        mBackButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                finish();
            }

        });
        mEditTextSearch.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable editable)
            {
                if(mEditTextSearch.getText().toString().length() > 0)
                {
                    downloadSearch(mEditTextSearch.getText().toString());
                    mListView.removeHeaderView(cellCurrentLocation);
                    mListView.setAdapter(mSearchAdapter);
                    return;
                } else
                {
                    mListView.setAdapter(null);
                    mListView.addHeaderView(cellCurrentLocation);
                    mListView.setAdapter(mNearbySearchAdapter);
                    return;
                }
            }

            public void beforeTextChanged(CharSequence charsequence, int i, int j, int k)
            {
            }

            public void onTextChanged(CharSequence charsequence, int i, int j, int k)
            {
            }

        });
        mListView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView adapterview, View view, int i, long l)
            {
                Intent intent = new Intent();
                if(mListView.getHeaderViewsCount() > 0 && i == 0)
                {
                    if(currentLocationItem != null)
                    {
                        intent.putExtra("selectedData",(Parcelable) currentLocationItem);
                        setResult(-1, intent);
                        finish();
                    }
                    return;
                }
                if(mListView.getAdapter() != mSearchAdapter) {
                    if(mNearbySearchAdapter.getCount() > 0 && mListView.getHeaderViewsCount() > 0 && i > 0)
                        intent.putExtra("selectedData",(Parcelable) mNearbySearchAdapter.getItem(i - 1));
                } else {
                    intent.putExtra("selectedData", (Parcelable)mSearchAdapter.getItem(i));
                }

                setResult(-1, intent);
                finish();
                return;
            }

        });

        mButtonFavorite.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
            }

        });
    }

    private void initLayout()
    {
        mProgressbar = (ProgressBar)findViewById(R.id.progressbar);
        mMenuBar = (RelativeLayout)findViewById(R.id.MenuBar);
        mBackButton = (Button)findViewById(R.id.BackButton);
        mTitleBar = (TextView)findViewById(R.id.TitleBar);
        mEditTextSearch = (EditText)findViewById(R.id.edit_text_search);
        mButtonFavorite = (Button)findViewById(R.id.button_favorite);
        mListView = (ListView)findViewById(R.id.list_view);
        cellCurrentLocation = getLayoutInflater().inflate(R.layout.cell_current_location, null);
        mListView.addHeaderView(cellCurrentLocation);
        mImageViewCurrentLocationMap = (ImageView)cellCurrentLocation.findViewById(R.id.image_view_map);
        mTextViewCurrentLocation = (TextView)cellCurrentLocation.findViewById(R.id.text_view_current_location);
        mTextViewLoading = (TextView)cellCurrentLocation.findViewById(R.id.text_view_place_name);
    }

    private void initialize()
    {
        initLayout();
        initData();
        initEvent();
        downloadCurrentLocation();
        downloadNearbyData();
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_direction_from_to);
        initialize();
    }

    public void onDetachedFromWindow()
    {
        LocationService.listeners.remove(locationServiceListener);
        if(mLocationService != null)
            mLocationService.disable();
        if(mNearbyService != null)
            mNearbyService.abort();
        if(mSearchService != null)
            mSearchService.abort();
        super.onDetachedFromWindow();
    }

    protected void onResume()
    {
        super.onResume();
    }

    private View cellCurrentLocation;
    CurrentLocationServiceOutput currentLocationItem;
    streetdirectory.mobile.gis.gps.LocationService.PositionChangedListener locationServiceListener;
    private Button mBackButton;
    private Button mButtonFavorite;
    private String mCountryCode;
    private CurrentLocationService mCurrentLocationService;
    private EditText mEditTextSearch;
    private int mImageHeight;
    private SDHttpImageService mImageServiceMap;
    private ImageView mImageViewCurrentLocationMap;
    private int mImageWidth;
    private ListView mListView;
    private LocationService mLocationService;
    private RelativeLayout mMenuBar;
    private NearbySearchAdapter mNearbySearchAdapter;
    private NearbyService mNearbyService;
    private ProgressBar mProgressbar;
    private SearchListAdapter mSearchAdapter;
    private SearchService mSearchService;
    private TextView mTextViewCurrentLocation;
    private TextView mTextViewLoading;
    private TextView mTitleBar;
    private ViewTreeObserver mTreeObserver;





/*
    static ViewTreeObserver access$1002(DirectionFromToActivity directionfromtoactivity, ViewTreeObserver viewtreeobserver)
    {
        directionfromtoactivity.mTreeObserver = viewtreeobserver;
        return viewtreeobserver;
    }

*/



/*
    static int access$1102(DirectionFromToActivity directionfromtoactivity, int i)
    {
        directionfromtoactivity.mImageWidth = i;
        return i;
    }

*/



/*
    static int access$1202(DirectionFromToActivity directionfromtoactivity, int i)
    {
        directionfromtoactivity.mImageHeight = i;
        return i;
    }

*/




/*
    static SDHttpImageService access$1502(DirectionFromToActivity directionfromtoactivity, SDHttpImageService sdhttpimageservice)
    {
        directionfromtoactivity.mImageServiceMap = sdhttpimageservice;
        return sdhttpimageservice;
    }

*/



/*
    static NearbyService access$1702(DirectionFromToActivity directionfromtoactivity, NearbyService nearbyservice)
    {
        directionfromtoactivity.mNearbyService = nearbyservice;
        return nearbyservice;
    }

*/









/*
    static CurrentLocationService access$802(DirectionFromToActivity directionfromtoactivity, CurrentLocationService currentlocationservice)
    {
        directionfromtoactivity.mCurrentLocationService = currentlocationservice;
        return currentlocationservice;
    }

*/

}
