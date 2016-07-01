// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.direction;

import android.app.*;
import android.content.*;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.google.gson.Gson;
import com.xinghai.mycurve.R;

import java.util.*;
import streetdirectory.mobile.core.*;
import streetdirectory.mobile.gis.*;
import streetdirectory.mobile.gis.gps.LocationService;
import streetdirectory.mobile.modules.SDActivity;
import streetdirectory.mobile.modules.core.LocationBusinessServiceOutput;
import streetdirectory.mobile.modules.direction.service.DirectionOverviewService;
import streetdirectory.mobile.modules.direction.service.DirectionOverviewServiceInput;
import streetdirectory.mobile.modules.direction.service.DirectionOverviewServiceOutput;
import streetdirectory.mobile.modules.location.service.*;
import streetdirectory.mobile.modules.map.MapActivity;
import streetdirectory.mobile.modules.sdmob.SdMobHelper;
import streetdirectory.mobile.modules.sdmob.SmallBanner;
import streetdirectory.mobile.sd.SDMapSideMenuLayout;
import streetdirectory.mobile.sd.SdMob;
import streetdirectory.mobile.service.SDHttpServiceOutput;
import streetdirectory.mobile.service.URLFactory;

// Referenced classes of package streetdirectory.mobile.modules.direction:
//            DirectionOverviewViewHolder, DirectionListActivity, DirectionFromToActivity

public class DirectionActivity extends SDActivity
{

    public DirectionActivity()
    {
        currentLocationItem = null;
        isMenuVisible = true;
        currentLocation = new Location("");
        adRequestRetryCount = 0;
        timePickerListener = new android.app.TimePickerDialog.OnTimeSetListener() {

            public void onTimeSet(TimePicker timepicker, int i, int j)
            {
                hour = i;
                minute = j;
                setLatestDateTimeLabel();
                downloadJourneyOverView();
            }

        };
        datePickerListener = new android.app.DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker datepicker, int i, int j, int k)
            {
                year = i;
                month = j;
                day = k;
                setLatestDateTimeLabel();
                downloadJourneyOverView();
            }

        };
    }

    private void abortAllDownload()
    {
        mDirectionOverviewService.abort();
        mDirectionOverviewService = null;
    }

    private DirectionOverviewViewHolder createOverviewHolder(RelativeLayout relativelayout)
    {
        DirectionOverviewViewHolder directionoverviewviewholder = new DirectionOverviewViewHolder();
        directionoverviewviewholder.icon = (ImageView)relativelayout.findViewById(R.id.ImageIcon);
        directionoverviewviewholder.titleLabel = (TextView)relativelayout.findViewById(R.id.TitleLabel);
        directionoverviewviewholder.detailLabel = (TextView)relativelayout.findViewById(R.id.DetailLabel);
        directionoverviewviewholder.subDetailLabel = (TextView)relativelayout.findViewById(R.id.SubDetailLabel);
        return directionoverviewviewholder;
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
                    currentLocation.setLatitude(geopoint.latitude);
                    currentLocation.setLongitude(geopoint.longitude);
                    mCurrentLocationService = new CurrentLocationService(temp) {

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
                            super.onSuccess(sdhttpserviceoutput);
                            if(sdhttpserviceoutput.childs.size() > 0)
                            {
                                currentLocationItem = (CurrentLocationServiceOutput)sdhttpserviceoutput.childs.get(0);
                                if(currentLocationItem.venue != null)
                                {
                                    mStartLocation = currentLocationItem;
                                    downloadJourneyOverView();
                                    mLabelStart.setText(currentLocationItem.venue);
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

    private void downloadJourneyOverView()
    {
        if(mStartLocation != null && mEndLocation != null)
        {
            saveQueryState();
            setStateLoading();
            if(mDirectionOverviewService != null)
            {
                mDirectionOverviewService.abort();
                mDirectionOverviewService = null;
            }
            String s = URLFactory.encode(mLabelDate.getText().toString());
            String s1 = URLFactory.encode(mLabelTime.getText().toString());
            mCountryCode = GeoSense.getArea(mStartLocation.longitude, mStartLocation.latitude).apiAreaId;
            mDirectionOverviewService = new DirectionOverviewService(new DirectionOverviewServiceInput(mCountryCode, mStartLocation.longitude, mStartLocation.latitude, mStartLocation.placeID, mStartLocation.addressID, mEndLocation.longitude, mEndLocation.latitude, mEndLocation.placeID, mEndLocation.addressID, s, s1)) {

                public void onFailed(Exception exception)
                {
                    super.onFailed(exception);
                    mIndicatorLoading.setVisibility(View.INVISIBLE);
                }

                public void onSuccess(Object obj)
                {
                    onSuccess((SDHttpServiceOutput)obj);
                }

                public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
                {
                    super.onSuccess(sdhttpserviceoutput);
                    setStateActive();
                    DirectionOverviewServiceOutput temp;
                    if(sdhttpserviceoutput.childs.size() > 0)
                    {
                        temp = (DirectionOverviewServiceOutput)sdhttpserviceoutput.childs.get(0);
                        DirectionOverviewViewHolder directionoverviewviewholder = createOverviewHolder(mDriveLayout);
                        if(((DirectionOverviewServiceOutput) (temp)).drivingTime != null && ((DirectionOverviewServiceOutput) (temp)).drivingTime.length() > 1 && !((DirectionOverviewServiceOutput) (temp)).drivingTime.substring(0, 1).equals("0"))
                        {
                            directionoverviewviewholder.detailLabel.setText(((DirectionOverviewServiceOutput) (temp)).drivingTime);
                            directionoverviewviewholder.subDetailLabel.setText(((DirectionOverviewServiceOutput) (temp)).drivingDistance);
                            mDriveLayout.setEnabled(true);
                        } else
                        {
                            directionoverviewviewholder.detailLabel.setText("Sorry");
                            directionoverviewviewholder.subDetailLabel.setText("Not Available");
                            mDriveLayout.setEnabled(false);
                        }
                        mDriveLayout.setTag(directionoverviewviewholder);
                        directionoverviewviewholder = createOverviewHolder(mTaxiLayout);
                        if(((DirectionOverviewServiceOutput) (temp)).taxiTime != null && ((DirectionOverviewServiceOutput) (temp)).taxiTime.length() > 1 && !((DirectionOverviewServiceOutput) (temp)).taxiTime.substring(0, 1).equals("0"))
                        {
                            directionoverviewviewholder.detailLabel.setText((new StringBuilder()).append("$").append(((DirectionOverviewServiceOutput) (temp)).taxiFare).toString());
                            directionoverviewviewholder.subDetailLabel.setText(((DirectionOverviewServiceOutput) (temp)).taxiTime);
                            mTaxiLayout.setEnabled(true);
                        } else
                        {
                            directionoverviewviewholder.detailLabel.setText("Sorry");
                            directionoverviewviewholder.subDetailLabel.setText("Not Available");
                            mTaxiLayout.setEnabled(false);
                        }
                        mTaxiLayout.setTag(directionoverviewviewholder);
                        directionoverviewviewholder = createOverviewHolder(mBusLayout);
                        if(((DirectionOverviewServiceOutput) (temp)).busTime != null && ((DirectionOverviewServiceOutput) (temp)).busTime.length() > 1 && !((DirectionOverviewServiceOutput) (temp)).busTime.substring(0, 1).equals("0"))
                        {
                            directionoverviewviewholder.detailLabel.setText(((DirectionOverviewServiceOutput) (temp)).busTime);
                            directionoverviewviewholder.subDetailLabel.setText((new StringBuilder()).append("$").append(((DirectionOverviewServiceOutput) (temp)).busFare).toString());
                            mBusLayout.setEnabled(true);
                        } else
                        {
                            directionoverviewviewholder.detailLabel.setText("Sorry");
                            directionoverviewviewholder.subDetailLabel.setText("Not Available");
                            mBusLayout.setEnabled(false);
                        }
                        mBusLayout.setTag(directionoverviewviewholder);
                        directionoverviewviewholder = createOverviewHolder(mMrtLayout);
                        if(((DirectionOverviewServiceOutput) (temp)).busTrainTime != null && ((DirectionOverviewServiceOutput) (temp)).busTrainTime.length() > 1 && !((DirectionOverviewServiceOutput) (temp)).busTrainTime.substring(0, 1).equals("0"))
                        {
                            directionoverviewviewholder.detailLabel.setText(((DirectionOverviewServiceOutput) (temp)).busTrainTime);
                            directionoverviewviewholder.subDetailLabel.setText((new StringBuilder()).append("$").append(((DirectionOverviewServiceOutput) (temp)).busTrainFare).toString());
                            mMrtLayout.setEnabled(true);
                        } else
                        {
                            directionoverviewviewholder.detailLabel.setText("Sorry");
                            directionoverviewviewholder.subDetailLabel.setText("Not Available");
                            mMrtLayout.setEnabled(false);
                        }
                        mMrtLayout.setTag(directionoverviewviewholder);
                    }
                }

            };
            mDirectionOverviewService.executeAsync();
        }
    }

    private void initData()
    {
        Object obj = getIntent();
        Object obj1 = ((Intent) (obj)).getStringExtra("countryCode");
        if(obj1 != null)
            mCountryCode = ((String) (obj1));
        obj1 = (LocationBusinessServiceOutput)((Intent) (obj)).getParcelableExtra("startData");
        LocationBusinessServiceOutput locationbusinessserviceoutput = (LocationBusinessServiceOutput)((Intent) (obj)).getParcelableExtra("endData");
        isMenuVisible = ((Intent) (obj)).getBooleanExtra("menu_visible", true);
        if(!isMenuVisible)
        {
            mBackButton.setVisibility(View.VISIBLE);
            mMenuButton.setVisibility(View.INVISIBLE);
        } else
        {
            mBackButton.setVisibility(View.INVISIBLE);
            mMenuButton.setVisibility(View.VISIBLE);
        }
        if(obj1 != null)
        {
            mStartLocation = ((LocationBusinessServiceOutput) (obj1));
            mLabelStart.setText(((LocationBusinessServiceOutput) (obj1)).venue);
            mLabelTapToChangeStart.setVisibility(View.VISIBLE);
            mEndLocation = null;
        }
        if(locationbusinessserviceoutput != null)
        {
            if(obj1 == null)
            {
                mStartLocation = null;
                mLabelStart.setText("Current Location ...");
                mLabelTapToChangeStart.setVisibility(View.VISIBLE);
                downloadCurrentLocation();
            }
            mEndLocation = locationbusinessserviceoutput;
            mLabelEnd.setText(locationbusinessserviceoutput.venue);
            mLabelTapToChangeEnd.setVisibility(View.VISIBLE);
            downloadJourneyOverView();
        }
        obj = Calendar.getInstance();
        hour = ((Calendar) (obj)).get(11);
        minute = ((Calendar) (obj)).get(12);
        year = ((Calendar) (obj)).get(1);
        month = ((Calendar) (obj)).get(2);
        day = ((Calendar) (obj)).get(5);
        setLatestDateTimeLabel();
    }

    private void initEvent()
    {
        mBackButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                finish();
            }

        });
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
        mReverseButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                LocationBusinessServiceOutput temp = mStartLocation;
                mStartLocation = mEndLocation;
                mEndLocation = temp;
                if(mStartLocation != null)
                {
                    mLabelStart.setText(mStartLocation.venue);
                    mLabelTapToChangeStart.setVisibility(View.VISIBLE);
                }
                if(mEndLocation != null)
                {
                    mLabelEnd.setText(mEndLocation.venue);
                    mLabelTapToChangeEnd.setVisibility(View.VISIBLE);
                }
                if(mStartLocation != null && mEndLocation != null)
                    downloadJourneyOverView();
            }

        });
        mDriveLayout.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                SDStory.post(URLFactory.createGantDirectionsClickOnDrive(), SDStory.createDefaultParams());
                Intent intent = new Intent(DirectionActivity.this, DirectionListActivity.class);
                intent.putExtra("startData", (Parcelable)mStartLocation);
                intent.putExtra("endData", (Parcelable)mEndLocation);
                intent.putExtra("method", 0);
                intent.putExtra("countryCode", mCountryCode);
                intent.putExtra("date", mLabelDate.getText().toString());
                intent.putExtra("time", mLabelTime.getText().toString());
                if(currentLocation.getLatitude() != 0.0D && currentLocation.getLongitude() != 0.0D)
                {
                    intent.putExtra("latitude", currentLocation.getLatitude());
                    intent.putExtra("longitude", currentLocation.getLongitude());
                }
                startActivity(intent);
            }

        });
        mTaxiLayout.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                SDStory.post(URLFactory.createGantDirectionsClickOnTaxi(), SDStory.createDefaultParams());
                Intent intent = new Intent(DirectionActivity.this, DirectionListActivity.class);
                intent.putExtra("startData",(Parcelable) mStartLocation);
                intent.putExtra("endData", (Parcelable)mEndLocation);
                intent.putExtra("method", 1);
                intent.putExtra("countryCode", mCountryCode);
                intent.putExtra("date", mLabelDate.getText().toString());
                intent.putExtra("time", mLabelTime.getText().toString());
                if(currentLocation.getLatitude() != 0.0D && currentLocation.getLongitude() != 0.0D)
                {
                    intent.putExtra("latitude", currentLocation.getLatitude());
                    intent.putExtra("longitude", currentLocation.getLongitude());
                }
                startActivity(intent);
            }

        });

        mBusLayout.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                SDStory.post(URLFactory.createGantDirectionsClickOnBus(), SDStory.createDefaultParams());
                Intent intent = new Intent(DirectionActivity.this, DirectionListActivity.class);
                intent.putExtra("startData",(Parcelable) mStartLocation);
                intent.putExtra("endData", (Parcelable)mEndLocation);
                intent.putExtra("method", 2);
                intent.putExtra("countryCode", mCountryCode);
                intent.putExtra("date", mLabelDate.getText().toString());
                intent.putExtra("time", mLabelTime.getText().toString());
                if(currentLocation.getLatitude() != 0.0D && currentLocation.getLongitude() != 0.0D)
                {
                    intent.putExtra("latitude", currentLocation.getLatitude());
                    intent.putExtra("longitude", currentLocation.getLongitude());
                }
                startActivity(intent);
            }

        });
        mMrtLayout.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                SDStory.post(URLFactory.createGantDirectionsClickOnMrt(), SDStory.createDefaultParams());
                Intent intent = new Intent(DirectionActivity.this, DirectionListActivity.class);
                intent.putExtra("startData", (Parcelable)mStartLocation);
                intent.putExtra("endData", (Parcelable)mEndLocation);
                intent.putExtra("method", 3);
                intent.putExtra("countryCode", mCountryCode);
                intent.putExtra("date", mLabelDate.getText().toString());
                intent.putExtra("time", mLabelTime.getText().toString());
                if(currentLocation.getLatitude() != 0.0D && currentLocation.getLongitude() != 0.0D)
                {
                    intent.putExtra("latitude", currentLocation.getLatitude());
                    intent.putExtra("longitude", currentLocation.getLongitude());
                }
                startActivity(intent);
            }

        });
        mLayoutTextStart.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                Intent intent = new Intent(DirectionActivity.this, DirectionFromToActivity.class);
                intent.putExtra("countryCode", mCountryCode);
                intent.putExtra("requestCode", 0);
                startActivityForResult(intent, 0);
            }

        });
        mLayoutTextEnd.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                Intent intent = new Intent(DirectionActivity.this, DirectionFromToActivity.class);
                intent.putExtra("countryCode", mCountryCode);
                intent.putExtra("requestCode", 1);
                startActivityForResult(intent, 1);
            }

        });
        mRefreshButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                downloadJourneyOverView();
            }

        });
        mLabelTime.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                showDialog(999);
            }

        });
        mLabelDate.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                showDialog(888);
            }

        });
    }

    private void initLayout()
    {
        mSideMenuLayout = (SDMapSideMenuLayout)findViewById(R.id.side_menu);
        mSideMenuBlocker = findViewById(R.id.side_menu_blocker);
        mMenuButton = (ImageButton)findViewById(R.id.MenuButton);
        mReverseButton = (Button)findViewById(R.id.ReverseButton);
        mRefreshButton = (Button)findViewById(R.id.RefreshButton);
        mDriveLayout = (RelativeLayout)findViewById(R.id.DriveLayout);
        mTaxiLayout = (RelativeLayout)findViewById(R.id.TaxiLayout);
        mBusLayout = (RelativeLayout)findViewById(R.id.BusLayout);
        mMrtLayout = (RelativeLayout)findViewById(R.id.MRTLayout);
        mIndicatorLoading = (ProgressBar)findViewById(R.id.indicator_loading);
        mBackButton = (Button)findViewById(R.id.BackButton);
        mSdMobViewHolder = (RelativeLayout)findViewById(R.id.view_sdmob);
        mLabelTapToChangeStart = (TextView)findViewById(R.id.TapToChangeStartLabel);
        mLabelTapToChangeEnd = (TextView)findViewById(R.id.TapToChangeEndLabel);
        mLayoutTextStart = (LinearLayout)findViewById(R.id.layout_start);
        mLayoutTextEnd = (LinearLayout)findViewById(R.id.layout_end);
        Resources resources = getResources();
        Compatibility.setBackgroundDrawable(mDriveLayout, resources.getDrawable(R.drawable.selector_button_direction_top_left));
        DirectionOverviewViewHolder directionoverviewviewholder = createOverviewHolder(mDriveLayout);
        directionoverviewviewholder.icon.setImageDrawable(resources.getDrawable(R.drawable.direction_icon_car));
        directionoverviewviewholder.titleLabel.setText(resources.getString(R.string.direction_overview_title_drive));
        mDriveLayout.setTag(directionoverviewviewholder);
        Compatibility.setBackgroundDrawable(mTaxiLayout, resources.getDrawable(R.drawable.selector_button_direction_top_right));
        directionoverviewviewholder = createOverviewHolder(mTaxiLayout);
        directionoverviewviewholder.icon.setImageDrawable(resources.getDrawable(R.drawable.direction_icon_taxi));
        directionoverviewviewholder.titleLabel.setText(resources.getString(R.string.direction_overview_title_taxi));
        directionoverviewviewholder.detailLabel.setText("$0.00");
        mTaxiLayout.setTag(directionoverviewviewholder);
        Compatibility.setBackgroundDrawable(mBusLayout, resources.getDrawable(R.drawable.selector_button_direction_bottom_left));
        directionoverviewviewholder = createOverviewHolder(mBusLayout);
        directionoverviewviewholder.icon.setImageDrawable(resources.getDrawable(R.drawable.direction_icon_bus));
        directionoverviewviewholder.titleLabel.setText(resources.getString(R.string.direction_overview_title_bus));
        directionoverviewviewholder.subDetailLabel.setText("$0.00");
        mBusLayout.setTag(directionoverviewviewholder);
        Compatibility.setBackgroundDrawable(mMrtLayout, resources.getDrawable(R.drawable.selector_button_direction_bottom_right));
        directionoverviewviewholder = createOverviewHolder(mMrtLayout);
        directionoverviewviewholder.icon.setImageDrawable(resources.getDrawable(R.drawable.direction_icon_mrt));
        directionoverviewviewholder.titleLabel.setText(resources.getString(R.string.direction_overview_title_mrt));
        directionoverviewviewholder.subDetailLabel.setText("$0.00");
        mMrtLayout.setTag(directionoverviewviewholder);
        mLabelDate = (TextView)findViewById(R.id.DateLabel);
        mLabelTime = (TextView)findViewById(R.id.TimeLabel);
        mLabelStart = (TextView)findViewById(R.id.StartLabel);
        mLabelEnd = (TextView)findViewById(R.id.EndLabel);
        mDriveLayout.setEnabled(false);
        mTaxiLayout.setEnabled(false);
        mBusLayout.setEnabled(false);
        mMrtLayout.setEnabled(false);
        mSearchListView = (ListView)mSideMenuLayout.findViewById(R.id.SearchListView);
        mMenuListView = (ListView)mSideMenuLayout.findViewById(R.id.MenuListView);
        mSearchField = (EditText)mSideMenuLayout.findViewById(R.id.MenuSearchField);
        mgr = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        loadQueryState();
    }

    private void initialize()
    {
        initLayout();
        initData();
        initEvent();
    }

    private void loadQueryState()
    {
        Object obj2 = getPreferences(0);
        Object obj1 = ((SharedPreferences) (obj2)).getString("start", null);
        Object obj = ((SharedPreferences) (obj2)).getString("end", null);
        if(obj1 != null && obj != null)
        {
            mCountryCode = ((SharedPreferences) (obj2)).getString("countryCode", "sg");
            obj2 = new Gson();
            obj1 = (HashMap)((Gson) (obj2)).fromJson(((String) (obj1)), (new HashMap()).getClass());
            obj = (HashMap)((Gson) (obj2)).fromJson(((String) (obj)), obj1.getClass());
            mStartLocation = new LocationBusinessServiceOutput(((HashMap) (obj1)));
            mStartLocation.populateData();
            mLabelStart.setText(mStartLocation.venue);
            mLabelTapToChangeStart.setVisibility(View.VISIBLE);
            mEndLocation = new LocationBusinessServiceOutput(((HashMap) (obj)));
            mEndLocation.populateData();
            mLabelEnd.setText(mEndLocation.venue);
            mLabelTapToChangeEnd.setVisibility(View.VISIBLE);
        }
    }

    private static String pad(int i)
    {
        if(i >= 10)
            return String.valueOf(i);
        else
            return (new StringBuilder()).append("0").append(String.valueOf(i)).toString();
    }

    private void saveQueryState()
    {
        if(mStartLocation != null && mEndLocation != null)
        {
            android.content.SharedPreferences.Editor editor = getPreferences(0).edit();
            Object obj = new Gson();
            String s = ((Gson) (obj)).toJson(mStartLocation.hashData);
            obj = ((Gson) (obj)).toJson(mEndLocation.hashData);
            editor.putString("start", s);
            editor.putString("end", ((String) (obj)));
            editor.putString("countryCode", mCountryCode);
            editor.commit();
        }
    }

    private void setLatestDateTimeLabel()
    {
        int i = hour;
        String s;
        if(hour - 12 > 0)
        {
            s = "PM";
            i = hour - 12;
        } else
        {
            s = "AM";
        }
        mLabelTime.setText((new StringBuilder()).append(pad(i)).append(":").append(pad(minute)).append(" ").append(s));
        mLabelDate.setText((new StringBuilder()).append(day).append("/").append(month + 1).append("/").append(year));
    }

    private void setStateActive()
    {
        mIndicatorLoading.setVisibility(View.INVISIBLE);
        mDriveLayout.setEnabled(true);
        mTaxiLayout.setEnabled(true);
        mBusLayout.setEnabled(true);
        mMrtLayout.setEnabled(true);
    }

    private void setStateLoading()
    {
        mIndicatorLoading.setVisibility(View.VISIBLE);
        mDriveLayout.setEnabled(false);
        mTaxiLayout.setEnabled(false);
        mBusLayout.setEnabled(false);
        mMrtLayout.setEnabled(false);
    }

    private void showExitConfirmDialog()
    {
        (new android.app.AlertDialog.Builder(this)).setTitle("Confirmation").setMessage("Are you sure want to exit ?").setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {

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

    private void updateSmallBanner()
    {
        mCurrentSmallBanner = SmallBanner.getBannerFromSdMobUnit(this, SdMobHelper.getInstance(this).getSdMobUnit(SdMob.ad_bnr_direction_main));
//        mCurrentSmallBanner.setAdMobSmallBannerListener(new streetdirectory.mobile.modules.sdmob.SmallBanner.AdMobSmallBannerListener() );
        View view = mCurrentSmallBanner.getView(this);
        mSdMobViewHolder.removeAllViews();
        mSdMobViewHolder.addView(view, new ViewGroup.LayoutParams(-1, -1));
    }

    protected void onActivityResult(int i, int j, Intent intent)
    {
        onActivityResult(i, j, intent);
        LocationBusinessServiceOutput temp;
        switch(i) {
            default:
                return;
            case 1:
                if (j == -1 && (temp = (LocationBusinessServiceOutput) intent.getParcelableExtra("selectedData")) != null) {
                    mStartLocation = temp;
                    mLabelStart.setText(((LocationBusinessServiceOutput) (temp)).venue);
                    mLabelTapToChangeStart.setVisibility(View.VISIBLE);
                    downloadJourneyOverView();
                    return;
                }
                break;
            case 2:
                if (j == -1) {
                    temp = (LocationBusinessServiceOutput) intent.getParcelableExtra("selectedData");
                    if (temp != null) {
                        mEndLocation = temp;
                        mLabelEnd.setText(((LocationBusinessServiceOutput) (temp)).venue);
                        mLabelTapToChangeEnd.setVisibility(View.VISIBLE);
                        downloadJourneyOverView();
                        return;
                    }
                }
        }
    }

    public void onBackPressed()
    {
        if(!isMenuVisible)
        {
            finish();
            return;
        }
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

    protected void onCreate(Bundle bundle)
    {
        onCreate(bundle);
        SDStory.post(URLFactory.createGantDirectionsMain(), SDStory.createDefaultParams());
        setContentView(R.layout.activity_direction);
        initialize();
        downloadJourneyOverView();
        updateSmallBanner();
    }

    protected Dialog onCreateDialog(int i)
    {
        switch(i)
        {
        default:
            return null;

        case 999:
            return new TimePickerDialog(this, timePickerListener, hour, minute, false);

        case 888:
            return new DatePickerDialog(this, datePickerListener, year, month, day);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        if(mSideMenuLayout.getIsMenuOpen())
            mSideMenuLayout.slideClose();
        else
            mSideMenuLayout.slideOpen();
        return false;
    }

    public void onDetachedFromWindow()
    {
        LocationService.listeners.remove(locationServiceListener);
        onDetachedFromWindow();
    }

    protected void onResume()
    {
        onResume();
    }

    static final int DATE_DIALOG_ID = 888;
    public static final int END = 1;
    public static final int START = 0;
    static final int TIME_DIALOG_ID = 999;
    private int adRequestRetryCount;
    private Location currentLocation;
    private CurrentLocationServiceOutput currentLocationItem;
    private android.app.DatePickerDialog.OnDateSetListener datePickerListener;
    private int day;
    private int hour;
    private boolean isMenuVisible;
    streetdirectory.mobile.gis.gps.LocationService.PositionChangedListener locationServiceListener;
    private Button mBackButton;
    private RelativeLayout mBusLayout;
    private String mCountryCode;
    private CurrentLocationService mCurrentLocationService;
    private SmallBanner mCurrentSmallBanner;
    private DirectionOverviewService mDirectionOverviewService;
    private RelativeLayout mDriveLayout;
    private LocationBusinessServiceOutput mEndLocation;
    private ProgressBar mIndicatorLoading;
    private TextView mLabelDate;
    private TextView mLabelEnd;
    private TextView mLabelStart;
    private TextView mLabelTapToChangeEnd;
    private TextView mLabelTapToChangeStart;
    private TextView mLabelTime;
    private LinearLayout mLayoutTextEnd;
    private LinearLayout mLayoutTextStart;
    private LocationService mLocationService;
    private ImageButton mMenuButton;
    private ListView mMenuListView;
    private RelativeLayout mMrtLayout;
    private Button mRefreshButton;
    private Button mReverseButton;
    private RelativeLayout mSdMobViewHolder;
    private EditText mSearchField;
    private ListView mSearchListView;
    private View mSideMenuBlocker;
    private SDMapSideMenuLayout mSideMenuLayout;
    private LocationBusinessServiceOutput mStartLocation;
    private RelativeLayout mTaxiLayout;
    private InputMethodManager mgr;
    private int minute;
    private int month;
    private android.app.TimePickerDialog.OnTimeSetListener timePickerListener;
    private int year;
















/*
    static LocationBusinessServiceOutput access$202(DirectionActivity directionactivity, LocationBusinessServiceOutput locationbusinessserviceoutput)
    {
        directionactivity.mStartLocation = locationbusinessserviceoutput;
        return locationbusinessserviceoutput;
    }

*/



/*
    static CurrentLocationService access$2102(DirectionActivity directionactivity, CurrentLocationService currentlocationservice)
    {
        directionactivity.mCurrentLocationService = currentlocationservice;
        return currentlocationservice;
    }

*/



/*
    static CurrentLocationServiceOutput access$2202(DirectionActivity directionactivity, CurrentLocationServiceOutput currentlocationserviceoutput)
    {
        directionactivity.currentLocationItem = currentlocationserviceoutput;
        return currentlocationserviceoutput;
    }

*/


/*
    static int access$2302(DirectionActivity directionactivity, int i)
    {
        directionactivity.hour = i;
        return i;
    }

*/


/*
    static int access$2402(DirectionActivity directionactivity, int i)
    {
        directionactivity.minute = i;
        return i;
    }

*/



/*
    static int access$2602(DirectionActivity directionactivity, int i)
    {
        directionactivity.year = i;
        return i;
    }

*/


/*
    static int access$2702(DirectionActivity directionactivity, int i)
    {
        directionactivity.month = i;
        return i;
    }

*/


/*
    static int access$2802(DirectionActivity directionactivity, int i)
    {
        directionactivity.day = i;
        return i;
    }

*/



/*
    static int access$2902(DirectionActivity directionactivity, int i)
    {
        directionactivity.adRequestRetryCount = i;
        return i;
    }

*/


/*
    static int access$2908(DirectionActivity directionactivity)
    {
        int i = directionactivity.adRequestRetryCount;
        directionactivity.adRequestRetryCount = i + 1;
        return i;
    }

*/




/*
    static LocationBusinessServiceOutput access$302(DirectionActivity directionactivity, LocationBusinessServiceOutput locationbusinessserviceoutput)
    {
        directionactivity.mEndLocation = locationbusinessserviceoutput;
        return locationbusinessserviceoutput;
    }

*/






}
