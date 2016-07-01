// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.offlinemap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.core.SDLogger;
import streetdirectory.mobile.modules.SDActivity;
import streetdirectory.mobile.modules.offlinemap.service.OfflineMapImageService;

public class OfflineMapImagePreviewActivity extends SDActivity
{

    public OfflineMapImagePreviewActivity()
    {
    }

    private void downloadImage()
    {
        if(mImageID != null)
        {
            SDLogger.info((new StringBuilder()).append("Image Preview URL: ").append(mImageID).toString());
            mImageService = new OfflineMapImageService(mImageID) {

                public void onAborted(Exception exception)
                {
                    mImageService = null;
                    mLoadingIndicator.setVisibility(View.INVISIBLE);
                }

                public void onFailed(Exception exception)
                {
                    mImageService = null;
                    mLoadingIndicator.setVisibility(View.INVISIBLE);
                }

                public void onSuccess(Bitmap bitmap)
                {
                    mPreviewImage.setImageBitmap(bitmap);
                    mLoadingIndicator.setVisibility(View.INVISIBLE);
                }

                public void onSuccess(Object obj)
                {
                    onSuccess((Bitmap)obj);
                }

            };
            mLoadingIndicator.setVisibility(View.VISIBLE);
            mImageService.executeAsync();
        }
    }

    private void initData()
    {
        Intent intent = getIntent();
        Object obj = intent.getStringExtra("title");
        if(obj != null)
            mTitleLabel.setText(((CharSequence) (obj)));
        obj = intent.getStringExtra("detail");
        if(obj != null)
            mDetailLabel.setText(((CharSequence) (obj)));
        obj = (Bitmap)intent.getParcelableExtra("thumbnail");
        if(obj != null)
            mPreviewImage.setImageBitmap(((Bitmap) (obj)));
        mImageID = intent.getStringExtra("imageID");
    }

    private void initEvent()
    {
        mCancelButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                finish();
            }

        });
    }

    private void initLayout()
    {
        mTitleLabel = (TextView)findViewById(R.id.TitleLabel);
        mCancelButton = (Button)findViewById(R.id.CancelButton);
        mPreviewImage = (ImageView)findViewById(R.id.PreviewImage);
        mDetailLabel = (TextView)findViewById(R.id.DetailLabel);
        mLoadingIndicator = (ProgressBar)findViewById(R.id.LoadingIndicator);
    }

    protected void abortAllProcess()
    {
        super.abortAllProcess();
        if(mImageService != null)
        {
            mImageService.abort();
            mImageService = null;
        }
    }

    public void finish()
    {
        abortAllProcess();
        super.finish();
        overridePendingTransition(0x10a0000, 0x10a0001);
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_offline_map_image_preview);
        initLayout();
        initData();
        initEvent();
        downloadImage();
    }

    private Button mCancelButton;
    private TextView mDetailLabel;
    private String mImageID;
    private OfflineMapImageService mImageService;
    private ProgressBar mLoadingIndicator;
    private ImageView mPreviewImage;
    private TextView mTitleLabel;




/*
    static OfflineMapImageService access$202(OfflineMapImagePreviewActivity offlinemapimagepreviewactivity, OfflineMapImageService offlinemapimageservice)
    {
        offlinemapimagepreviewactivity.mImageService = offlinemapimageservice;
        return offlinemapimageservice;
    }

*/
}
