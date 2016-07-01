// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.locationdetail.trafficcam;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import streetdirectory.mobile.core.SDLogger;
import streetdirectory.mobile.modules.SDActivity;
import streetdirectory.mobile.modules.core.LocationBusinessServiceOutput;
import streetdirectory.mobile.modules.direction.DirectionActivity;
import streetdirectory.mobile.modules.locationdetail.trafficcam.service.TrafficCameraImageLinkService;
import streetdirectory.mobile.modules.locationdetail.trafficcam.service.TrafficCameraImageLinkServiceInput;
import streetdirectory.mobile.modules.locationdetail.trafficcam.service.TrafficCameraImageLinkServiceOutput;
import streetdirectory.mobile.modules.mappreview.MapPreviewActivity;
import streetdirectory.mobile.modules.tips.TipsActivity;
import streetdirectory.mobile.modules.trafficcamera.TrafficCameraActivity;
import streetdirectory.mobile.sd.SDBlackboard;
import streetdirectory.mobile.service.SDHttpImageService;
import streetdirectory.mobile.service.SDHttpServiceOutput;

public class TrafficCameraLocationDetailActivity extends SDActivity
{

    public TrafficCameraLocationDetailActivity()
    {
    }

    private void abortAllDownload()
    {
        abortDownloadPhotoImage();
        abortDownloadTrafficCameraImageLink();
        abortDownloadTrafficImage();
    }

    private void abortDownloadPhotoImage()
    {
        if(mPhotoImageService != null)
        {
            mPhotoImageService.abort();
            mPhotoImageService = null;
        }
    }

    private void abortDownloadTrafficCameraImageLink()
    {
        if(mTrafficCameraImageLinkService != null)
        {
            mTrafficCameraImageLinkService.abort();
            mTrafficCameraImageLinkService = null;
        }
    }

    private void abortDownloadTrafficImage()
    {
        if(mTrafficImageService != null)
        {
            mTrafficImageService.abort();
            mTrafficImageService = null;
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
                onSuccess((Bitmap) obj);
            }

        };
        mPhotoImageService.executeAsync();
    }

    private void downloadTrafficCameraImageLink()
    {
        abortDownloadTrafficCameraImageLink();
        mTrafficCameraImageLinkService = new TrafficCameraImageLinkService(new TrafficCameraImageLinkServiceInput(mData.countryCode, mData.placeID, mData.addressID)) {

            public void onAborted(Exception exception)
            {
                SDLogger.info("Traffic Camera Image Link Aborted");
            }

            public void onFailed(Exception exception)
            {
                SDLogger.printStackTrace(exception, "Traffic Camera Image Link Failed");
                mTrafficCameraImageLinkService = null;
            }

            public void onSuccess(Object obj)
            {
                onSuccess((SDHttpServiceOutput) obj);
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                    int i = 0;
                    int j;
                    mTrafficCameraImageLinkService = null;
                    if(sdhttpserviceoutput.childs.size() <= 0)
                        return;

                    TrafficCameraImageLinkServiceOutput temp = (TrafficCameraImageLinkServiceOutput)sdhttpserviceoutput.childs.get(0);
                    if(((TrafficCameraImageLinkServiceOutput) (temp)).imageURL == null || "".equals(((TrafficCameraImageLinkServiceOutput) (temp)).imageURL))
                        return;
                    j = mImageViewTrafficImage.getWidth();
                    int k = mImageViewTrafficImage.getHeight();
                    if(j != 0)
                    {
                        i = k;
                    }
                    if(j ==0 || k ==0) {
                        j = mImageViewTrafficImage.getLayoutParams().width;
                        i = mImageViewTrafficImage.getLayoutParams().height;
                    }

                    downloadTrafficImage(((TrafficCameraImageLinkServiceOutput) (temp)).imageURL, j, i);
            }

        };
        mTrafficCameraImageLinkService.executeAsync();
    }

    private void downloadTrafficImage(String s, int i, int j)
    {
        abortDownloadTrafficImage();
        mTrafficImageService = new SDHttpImageService(s) {

            public void onAborted(Exception exception)
            {
                SDLogger.info("Traffic Image Aborted");
            }

            public void onFailed(Exception exception)
            {
                SDLogger.printStackTrace(exception, "Traffic Image Failed");
                mTrafficImageService = null;
            }

            public void onSuccess(Bitmap bitmap)
            {
                mTrafficImageService = null;
                mImageViewTrafficImage.setImageBitmap(bitmap);
            }

            public void onSuccess(Object obj)
            {
                onSuccess((Bitmap)obj);
            }

        };
        mTrafficImageService.executeAsync();
    }

    private void initData()
    {
        LocationBusinessServiceOutput locationbusinessserviceoutput = (LocationBusinessServiceOutput)getIntent().getParcelableExtra("data");
        if(locationbusinessserviceoutput != null)
        {
            mData = locationbusinessserviceoutput;
            mTextviewTitle.setText(mData.venue);
            mTextviewDetail.setText(mData.address);
            if(mData.longitude == 0.0D && mData.latitude == 0.0D)
                mButtonMap.setEnabled(false);
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
        mShareButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
            }

        });
        mButtonDirection.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                Intent intent = new Intent(TrafficCameraLocationDetailActivity.this, DirectionActivity.class);
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

            public void onClick(View view) {
                startActivityMapPreview();
            }

        });
        mButtonViewAll.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                startActivityTrafficCamera(mData.countryCode);
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
        mImageViewTrafficImage = (ImageView)findViewById(R.id.image_view_traffic_image);
        mTextViewTrafficTime = (TextView)findViewById(R.id.text_view_traffic_time);
        setTimeText();
        mButtonViewAll = (Button)findViewById(R.id.button_view_all);
    }

    private void initialize()
    {
        initLayout();
        initData();
        initEvent();
    }

    private void setTimeText()
    {
        String s = (new SimpleDateFormat("HHmm")).format(new Date());
        int i = Integer.parseInt(s.substring(2, 4));
        mTextViewTrafficTime.setText((new StringBuilder()).append(s.substring(0, 2)).append(":").append(i - i % 5).toString());
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
        intent.putExtra("companyID", mData.companyID);
        intent.putExtra("locationID", mData.locationID);
        intent.putExtra("img", mData.imageURL);
        intent.putExtra("venue", mData.venue);
        startActivity(intent);
    }

    private void startActivityTrafficCamera(String s)
    {
        Intent intent = new Intent(this, TrafficCameraActivity.class);
        intent.putExtra("countryCode", SDBlackboard.currentCountryCode);
        startActivity(intent);
    }

    protected void onCreate(Bundle bundle)
    {
            int i = 0;
            int j;
            super.onCreate(bundle);
            setContentView(R.layout.activity_location_detail_traffic_cam);
            initialize();
            if(mData == null)
                return;
            j = mButtonBusinessPhoto.getWidth();
            int k = mButtonBusinessPhoto.getHeight();
            if(j != 0)
            {
                i = k;
            }
            if(j ==0 || k == 0) {
                j = mButtonBusinessPhoto.getLayoutParams().width;
                i = mButtonBusinessPhoto.getLayoutParams().height;
            }
            downloadPhotoImage(mData.imageURL, j, i);
            downloadTrafficCameraImageLink();
    }

    protected void onDestroy()
    {
        super.onDestroy();
        abortAllDownload();
    }

    private Button mBackButton;
    private ImageButton mButtonBusinessPhoto;
    private Button mButtonDirection;
    private Button mButtonMap;
    private Button mButtonTips;
    private Button mButtonViewAll;
    private LocationBusinessServiceOutput mData;
    private ImageView mImageViewTrafficImage;
    private RelativeLayout mLayoutHeader;
    private LinearLayout mLayoutHeaderButton;
    private RelativeLayout mMenuBar;
    private SDHttpImageService mPhotoImageService;
    private Button mShareButton;
    private TextView mTextViewTrafficTime;
    private TextView mTextviewDetail;
    private TextView mTextviewTitle;
    private TextView mTitleBar;
    private TrafficCameraImageLinkService mTrafficCameraImageLinkService;
    private SDHttpImageService mTrafficImageService;






/*
    static SDHttpImageService access$402(TrafficCameraLocationDetailActivity trafficcameralocationdetailactivity, SDHttpImageService sdhttpimageservice)
    {
        trafficcameralocationdetailactivity.mPhotoImageService = sdhttpimageservice;
        return sdhttpimageservice;
    }

*/



/*
    static TrafficCameraImageLinkService access$602(TrafficCameraLocationDetailActivity trafficcameralocationdetailactivity, TrafficCameraImageLinkService trafficcameraimagelinkservice)
    {
        trafficcameralocationdetailactivity.mTrafficCameraImageLinkService = trafficcameraimagelinkservice;
        return trafficcameraimagelinkservice;
    }

*/




/*
    static SDHttpImageService access$902(TrafficCameraLocationDetailActivity trafficcameralocationdetailactivity, SDHttpImageService sdhttpimageservice)
    {
        trafficcameralocationdetailactivity.mTrafficImageService = sdhttpimageservice;
        return sdhttpimageservice;
    }

*/
}
