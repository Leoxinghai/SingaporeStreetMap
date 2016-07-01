// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.bottombanner;

import android.content.*;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.util.ArrayList;
import java.util.HashMap;
import streetdirectory.mobile.core.SDStory;
import streetdirectory.mobile.gis.GeoPoint;
import streetdirectory.mobile.modules.bottombanner.service.BottomBannerService;
import streetdirectory.mobile.modules.bottombanner.service.BottomBannerServiceInput;
import streetdirectory.mobile.modules.bottombanner.service.BottomBannerServiceOutput;
import streetdirectory.mobile.modules.businessdetail.BusinessDetailActivityV2;
import streetdirectory.mobile.modules.businesslisting.offers.OffersListingActivity;
import streetdirectory.mobile.sd.SDBlackboard;
import streetdirectory.mobile.service.*;

public class BottomBannerLayout extends LinearLayout
{
    public static interface OnPinClickedListener
    {

        public abstract void onPinClicked();
    }


    public BottomBannerLayout(Context context)
    {
        super(context);
        imagePool = new SDHttpImageServicePool();
        bannerVisible = true;
        mContext = context;
        initLayout(context);
        initEvent();
    }

    public BottomBannerLayout(Context context, String s, GeoPoint geopoint, GeoPoint geopoint1, Rect rect, int i)
    {
        super(context);
        imagePool = new SDHttpImageServicePool();
        bannerVisible = true;
        mContext = context;
        initLayout(context);
        initEvent();
        mCountryCode = s;
        downloadBottomBanner(rect, geopoint, geopoint1, i);
    }

    private void abortAllDownload()
    {
        if(mBottomBannerService != null)
        {
            mBottomBannerService.abort();
            mBottomBannerService = null;
        }
    }

    private void downloadBottomBanner(Rect rect, GeoPoint geopoint, GeoPoint geopoint1, int i)
    {
        abortAllDownload();
        mBottomBannerService = new BottomBannerService(new BottomBannerServiceInput(countryCode, geopoint.longitude, geopoint.latitude, geopoint1.longitude, geopoint1.latitude, rect.left, rect.top, rect.right, rect.bottom, mCategoryID, i, 1)) {

            public void onAborted(Exception exception)
            {
                super.onAborted(exception);
            }

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
                    mBottomBannerServiceOutput = (BottomBannerServiceOutput)sdhttpserviceoutput.childs.get(0);
                    setLayoutWithResult();
                    mImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.business_no_photo));
                    if(mBottomBannerServiceOutput.siteBanner != null)
                    {
                        String temp = mBottomBannerServiceOutput.generateSiteBannerURL(mImageView.getWidth(), mImageView.getHeight());
                        imagePool.queueRequest(temp, mImageView.getWidth(), mImageView.getHeight(), new streetdirectory.mobile.service.SDHttpImageServicePool.OnBitmapReceivedListener() {

                            public void bitmapReceived(String s, Bitmap bitmap)
                            {
                                mImageView.setImageBitmap(bitmap);
                            }

                        });

                    }
                    onPopulateViewCompleted();
                }
            }

        };
        mBottomBannerService.executeAsync();
    }

    private void downloadImage(String s, int i, int j)
    {
        if(mImageService != null)
        {
            mImageService.abort();
            mImageService = null;
        }
        mImageService = new SDHttpImageService(s, i, j) {

            public void onAborted(Exception exception)
            {
                mImageService = null;
            }

            public void onFailed(Exception exception)
            {
                mImageService = null;
            }

            public void onSuccess(Bitmap bitmap)
            {
                mImageView.setImageBitmap(bitmap);
                mImageService = null;
            }

            public void onSuccess(Object obj)
            {
                onSuccess((Bitmap)obj);
            }

        };
        mImageService.executeAsync();
    }

    private void initEvent()
    {
        mButtonCall.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                try
                {
                    Intent intent;
                    intent = new Intent("android.intent.action.DIAL");
                    intent.setData(Uri.parse((new StringBuilder()).append("tel:").append(mBottomBannerServiceOutput.phoneNumber).toString()));
                    HashMap hashmap = SDStory.createDefaultParams();
                    hashmap.put("c_id", Integer.toString(mBottomBannerServiceOutput.companyID));
                    hashmap.put("c_pid", mBottomBannerServiceOutput.phoneNumber);
                    hashmap.put("opg", "bottom_banner");
                    SDStory.post(URLFactory.createGantBusinessCall((new StringBuilder()).append(mBottomBannerServiceOutput.companyID).append("").toString()), hashmap);
                    mContext.startActivity(intent);
                    return;
                }
                // Misplaced declaration of an exception variable
                catch(Exception ex)
                {
                    Log.e("", "Call failed", ex);
                }
            }

        });
        mButtonPin.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                if(mPinClickedListener != null)
                    mPinClickedListener.onPinClicked();
            }

        });
        mLayoutContainer.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {

            	if(mBottomBannerServiceOutput != null)
                    if("1".equals(mBottomBannerServiceOutput.bannerType))
                    {
                        SDStory.post(URLFactory.createGantBottomBannerCategory(mBottomBannerServiceOutput.countryCode, Integer.toString(mBottomBannerServiceOutput.categoryID), mBottomBannerServiceOutput.categoryName), SDStory.createDefaultParams());
                        Intent intent = null;
                        intent = new Intent(mContext, OffersListingActivity.class);
                        intent.putExtra("countryCode", mBottomBannerServiceOutput.countryCode);
                        intent.putExtra("categoryID", mBottomBannerServiceOutput.categoryID);
                        intent.putExtra("categoryName", mBottomBannerServiceOutput.categoryName);
                        intent.putExtra("longitude", SDBlackboard.currentLongitude);
                        intent.putExtra("latitude", SDBlackboard.currentLatitude);
                        mContext.startActivity(intent);
                    } else
                    if("2".equals(mBottomBannerServiceOutput.bannerType))
                    {
                        Intent intent = null;
                        SDStory.post(URLFactory.createGantBottomBannerBusiness(Integer.toString(mBottomBannerServiceOutput.companyID)), SDStory.createDefaultParams());
                        intent = new Intent(mContext, BusinessDetailActivityV2.class);
                        intent.putExtra("data", (Parcelable)mBottomBannerServiceOutput);
                        mContext.startActivity(intent);
                        return;
                    }
            }

        }
);
    }

    private void initLayout(Context context)
    {
        ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_bottom_banner, this, true);
        mImageView = (ImageView)findViewById(R.id.image_view);
        mTextViewTitle = (TextView)findViewById(R.id.text_view_title);
        mTextViewDetail = (TextView)findViewById(R.id.text_view_detail);
        mButtonCall = (Button)findViewById(R.id.button_call);
        mButtonArrow = (Button)findViewById(R.id.button_arrow);
        mButtonPin = (Button)findViewById(R.id.button_pin);
        mLabelDistance = (TextView)findViewById(R.id.label_distance);
        mLayoutPin = (LinearLayout)findViewById(R.id.layout_pin);
        mLayoutContainer = (RelativeLayout)findViewById(R.id.layout_bottom_banner_container);
    }

    private void setLayoutWithResult()
    {
        if(mBottomBannerServiceOutput == null)
            return;

        if(!"1".equals(mBottomBannerServiceOutput.bannerType)) {
            if("2".equals(mBottomBannerServiceOutput.bannerType))
            {
                String s;
                TextView textview;
                if(mBottomBannerServiceOutput.phoneNumber == null)
                    mButtonCall.setVisibility(View.INVISIBLE);
                else
                    mButtonCall.setVisibility(View.VISIBLE);
                mTextViewDetail.setVisibility(View.VISIBLE);
                mButtonArrow.setVisibility(View.INVISIBLE);
                textview = mTextViewTitle;
                if(mBottomBannerServiceOutput.description == null)
                    s = "";
                else
                    s = mBottomBannerServiceOutput.description;
                textview.setText(s);
                textview = mTextViewDetail;
                if(mBottomBannerServiceOutput.address == null)
                    s = "";
                else
                    s = mBottomBannerServiceOutput.address;
                textview.setText(s);
            }
        } else {
	        mTextViewTitle.setText(mBottomBannerServiceOutput.categoryName);
	        mTextViewDetail.setText("Click for more");
	        mTextViewDetail.setVisibility(View.VISIBLE);
	        mButtonArrow.setVisibility(View.VISIBLE);
	        mButtonCall.setVisibility(View.INVISIBLE);
	        mLayoutPin.setVisibility(View.INVISIBLE);
        }
        onPopulateViewCompleted();
        return;
    }

    protected void onPopulateViewCompleted()
    {
    }

    public void populateData()
    {
        if(bannerVisible)
        {
            mLayoutContainer.setVisibility(View.VISIBLE);
            downloadBottomBanner(boundaries, top, bottom, level);
            return;
        } else
        {
            mLayoutContainer.setVisibility(View.INVISIBLE);
            return;
        }
    }

    public void setOnPinClickedListener(OnPinClickedListener onpinclickedlistener)
    {
        mPinClickedListener = onpinclickedlistener;
    }

    public boolean bannerVisible;
    public GeoPoint bottom;
    public Rect boundaries;
    public String countryCode;
    protected SDHttpImageServicePool imagePool;
    public int level;
    private BottomBannerService mBottomBannerService;
    private BottomBannerServiceOutput mBottomBannerServiceOutput;
    private Button mButtonArrow;
    private Button mButtonCall;
    private Button mButtonPin;
    private String mCategoryID;
    private Context mContext;
    private String mCountryCode;
    private SDHttpImageService mImageService;
    private ImageView mImageView;
    private TextView mLabelDistance;
    private RelativeLayout mLayoutContainer;
    private LinearLayout mLayoutPin;
    private double mPannedLatitude;
    private double mPannedLongitude;
    private OnPinClickedListener mPinClickedListener;
    private TextView mTextViewDetail;
    private TextView mTextViewTitle;
    public GeoPoint top;



/*
    static BottomBannerServiceOutput access$002(BottomBannerLayout bottombannerlayout, BottomBannerServiceOutput bottombannerserviceoutput)
    {
        bottombannerlayout.mBottomBannerServiceOutput = bottombannerserviceoutput;
        return bottombannerserviceoutput;
    }

*/






/*
    static SDHttpImageService access$502(BottomBannerLayout bottombannerlayout, SDHttpImageService sdhttpimageservice)
    {
        bottombannerlayout.mImageService = sdhttpimageservice;
        return sdhttpimageservice;
    }

*/
}
