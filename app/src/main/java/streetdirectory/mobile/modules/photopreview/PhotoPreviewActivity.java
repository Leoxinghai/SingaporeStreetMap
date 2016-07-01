// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.photopreview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.util.ArrayList;
import streetdirectory.mobile.core.SDLogger;
import streetdirectory.mobile.core.service.HttpServiceListener;
import streetdirectory.mobile.modules.photopreview.service.BusinessImageListServiceInput;
import streetdirectory.mobile.modules.photopreview.service.ImageListService;
import streetdirectory.mobile.modules.photopreview.service.ImageListServiceOutput;
import streetdirectory.mobile.modules.photopreview.service.LocationImageListServiceInput;
import streetdirectory.mobile.service.SDHttpServiceOutput;

// Referenced classes of package streetdirectory.mobile.modules.photopreview:
//            PhotoThumbnailFragmentAdapter, PhotoThumbnailFragment, PhotoPreviewFragmentAdapter

public class PhotoPreviewActivity extends FragmentActivity
{

    public PhotoPreviewActivity()
    {
    }

    private void abortDownloadImageList()
    {
        if(mService != null)
        {
            mService.abort();
            mService = null;
        }
    }

    private void downloadImageList()
    {
        abortDownloadImageList();
        mProgressbar.setVisibility(View.VISIBLE);
        if(mType == 2)
            mService = new ImageListService(new BusinessImageListServiceInput(mCountryCode, mCompanyID, mLocationID), ImageListServiceOutput.class);
        else
            mService = new ImageListService(new LocationImageListServiceInput(mCountryCode, mPlaceID, mAddressID), ImageListServiceOutput.class);
        mService.setListener(new HttpServiceListener() {

            public void onAborted(Exception exception)
            {
                SDLogger.info("Photo Preview List Aborted");
            }

            public void onFailed(Exception exception)
            {
                SDLogger.printStackTrace(exception, "Photo Preview List Failed");
                mService = null;
            }

            public void onProgress(int i)
            {
            }

            public void onSuccess(Object obj)
            {
                onSuccess((SDHttpServiceOutput)obj);
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                mProgressbar.setVisibility(View.INVISIBLE);
                SDLogger.info("Photo Preview List Success");
                mService = null;
                mPhotoAdapter = new PhotoPreviewFragmentAdapter(getSupportFragmentManager());
                mPhotoAdapter.setData(sdhttpserviceoutput.childs);
                mViewpagerPhoto.setAdapter(mPhotoAdapter);
                mThumbnailAdapter = new PhotoThumbnailFragmentAdapter(getSupportFragmentManager(), PhotoPreviewActivity.this);
                mThumbnailAdapter.setOnImageClickedListener(new PhotoThumbnailFragmentAdapter.OnImageClickedListener() {

                    public void onAddImageClicked(int i)
                    {
                        thumbnailButtonClicked(i);
                    }

                    public void onImageClicked(ImageListServiceOutput imagelistserviceoutput, int i)
                    {
                        thumbnailButtonClicked(i);
                    }

                });
                mThumbnailAdapter.setData(sdhttpserviceoutput.childs);
                mViewpagerThumb.setAdapter(mThumbnailAdapter);
                if(mPosition <= sdhttpserviceoutput.childs.size())
                    thumbnailButtonClicked(mPosition);
            }

        });
        mService.executeAsync();
    }

    private void initData()
    {
        Intent intent = getIntent();
        mCompanyID = intent.getIntExtra("companyID", 0);
        mLocationID = intent.getIntExtra("locationID", 0);
        mPlaceID = intent.getIntExtra("placeID", 0);
        mAddressID = intent.getIntExtra("addressID", 0);
        mType = intent.getIntExtra("type", 1);
        mCountryCode = intent.getStringExtra("countryCode");
        mPosition = intent.getIntExtra("position", 0);
    }

    private void initEvent()
    {
        mButtonBack.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                finish();
            }

        });
        mButtonShare.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
            }

        });
        mButtonPostImage.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
            }

        });
        mViewpagerPhoto.setOnPageChangeListener(new android.support.v4.view.ViewPager.OnPageChangeListener() {

            public void onPageScrollStateChanged(int i)
            {
            }

            public void onPageScrolled(int i, float f, int j)
            {
            }

            public void onPageSelected(int i)
            {
                photoPagerScrolled(i);
            }

        });
    }

    private void initLayout()
    {
        mProgressbar = (ProgressBar)findViewById(R.id.progressBar1);
        mButtonBack = (Button)findViewById(R.id.button_back);
        mButtonShare = (Button)findViewById(R.id.button_share);
        mTextviewTitle = (TextView)findViewById(R.id.textview_title);
        mViewpagerPhoto = (ViewPager)findViewById(R.id.viewpager_photo);
        mButtonPostImage = (Button)findViewById(R.id.button_post_image);
        mViewpagerThumb = (ViewPager)findViewById(R.id.viewpager_thumb);
    }

    private void initialize()
    {
        initLayout();
        initData();
        initEvent();
    }

    private void photoPagerScrolled(int i)
    {
        mViewpagerThumb.setCurrentItem(i, true);
        redrawSelectedThumbnail(i);
    }

    private void redrawSelectedThumbnail(int i)
    {
        ImageListServiceOutput imagelistserviceoutput = mThumbnailAdapter.getData(i);
        int j = 0;
        do
        {
            Object obj = mThumbnailAdapter.getActiveFragment(mViewpagerThumb, j);
            if(obj == null)
                break;
            obj = (PhotoThumbnailFragment)obj;
            boolean flag;
            if(i == j)
                flag = true;
            else
                flag = false;
            ((PhotoThumbnailFragment) (obj)).setSelected(flag);
            j++;
        } while(true);
        if(imagelistserviceoutput != null)
        {
            mTextviewTitle.setText(imagelistserviceoutput.description);
            return;
        } else
        {
            mTextviewTitle.setText("");
            return;
        }
    }

    private void thumbnailButtonClicked(int i)
    {
        mViewpagerPhoto.setCurrentItem(i, true);
        if(i == 0)
            redrawSelectedThumbnail(i);
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_photo_preview);
        initialize();
        downloadImageList();
    }

    private int mAddressID;
    private Button mButtonBack;
    private Button mButtonPostImage;
    private Button mButtonShare;
    private int mCompanyID;
    private String mCountryCode;
    private int mLocationID;
    private PhotoPreviewFragmentAdapter mPhotoAdapter;
    private int mPlaceID;
    private int mPosition;
    private ProgressBar mProgressbar;
    private ImageListService mService;
    private TextView mTextviewTitle;
    private PhotoThumbnailFragmentAdapter mThumbnailAdapter;
    private int mType;
    private ViewPager mViewpagerPhoto;
    private ViewPager mViewpagerThumb;




/*
    static ImageListService access$202(PhotoPreviewActivity photopreviewactivity, ImageListService imagelistservice)
    {
        photopreviewactivity.mService = imagelistservice;
        return imagelistservice;
    }

*/



/*
    static PhotoPreviewFragmentAdapter access$302(PhotoPreviewActivity photopreviewactivity, PhotoPreviewFragmentAdapter photopreviewfragmentadapter)
    {
        photopreviewactivity.mPhotoAdapter = photopreviewfragmentadapter;
        return photopreviewfragmentadapter;
    }

*/




/*
    static PhotoThumbnailFragmentAdapter access$502(PhotoPreviewActivity photopreviewactivity, PhotoThumbnailFragmentAdapter photothumbnailfragmentadapter)
    {
        photopreviewactivity.mThumbnailAdapter = photothumbnailfragmentadapter;
        return photothumbnailfragmentadapter;
    }

*/



}
