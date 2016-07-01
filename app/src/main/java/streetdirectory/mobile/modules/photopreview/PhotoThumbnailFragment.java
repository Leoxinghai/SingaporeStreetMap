// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.photopreview;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.*;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.core.SDLogger;
import streetdirectory.mobile.modules.photopreview.service.ImageListServiceOutput;
import streetdirectory.mobile.modules.photopreview.service.PhotoThumbnailImageService;
import streetdirectory.mobile.modules.photopreview.service.PhotoThumbnailImageServiceInput;

public class PhotoThumbnailFragment extends Fragment
{

    public PhotoThumbnailFragment()
    {
        scale = 1.0F;
    }

    private void abortDownloadPhotoImage()
    {
        if(mPhotoListImageService != null)
        {
            mPhotoListImageService.abort();
            mPhotoListImageService = null;
        }
    }

    private void downloadPhotoImage(ImageListServiceOutput imagelistserviceoutput, int i, int j)
    {
        abortDownloadPhotoImage();
        mPhotoListImageService = new PhotoThumbnailImageService(new PhotoThumbnailImageServiceInput(imagelistserviceoutput, i, j)) {

            public void onAborted(Exception exception)
            {
                SDLogger.info("Photo Image Aborted");
            }

            public void onFailed(Exception exception)
            {
                SDLogger.printStackTrace(exception, "Photo Image Failed");
                mPhotoListImageService = null;
            }

            public void onSuccess(Bitmap bitmap)
            {
                mPhotoListImageService = null;
                mPhoto.setImageBitmap(bitmap);
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            public void onSuccess(Object obj)
            {
                onSuccess((Bitmap)obj);
            }

        };
        mPhotoListImageService.executeAsync();
    }

    private void initData()
    {
        mData = getData();
    }

    private void initEvent()
    {
        mPhoto.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                onPhotoClicked(mData);
            }

        });
    }

    public ImageListServiceOutput getData()
    {
        return null;
    }

    public void onActivityCreated(Bundle bundle)
    {
        super.onActivityCreated(bundle);
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
    }

    public View onCreateView(LayoutInflater layoutinflater, ViewGroup viewgroup, Bundle bundle)
    {
        View lview = layoutinflater.inflate(R.layout.fragment_photo_thumbnail, viewgroup, false);
        mLayout = (RelativeLayout)lview.findViewById(R.id.layout_photo);
        mPhoto = (ImageButton)lview.findViewById(R.id.imagebutton_photo);
        mProgressBar = (ProgressBar)lview.findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.VISIBLE);
        ViewGroup.LayoutParams temp;
        if(scale != 1.0F)
        {
            temp = mPhoto.getLayoutParams();
            temp.width = (int)((float)((android.view.ViewGroup.LayoutParams) (temp)).width * scale);
            temp.height = (int)((float)((android.view.ViewGroup.LayoutParams) (temp)).height * scale);
            mPhoto.setLayoutParams(temp);
        }
        mPhoto.getViewTreeObserver().addOnGlobalLayoutListener(new android.view.ViewTreeObserver.OnGlobalLayoutListener() {

            public void onGlobalLayout()
            {
                mPhoto.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                if(mData != null)
                    downloadPhotoImage(mData, mPhoto.getWidth(), mPhoto.getHeight());
            }

        });
        initData();
        initEvent();
        return lview;
    }

    public void onDestroy()
    {
        abortDownloadPhotoImage();
        super.onDestroy();
    }

    public void onDestroyView()
    {
        abortDownloadPhotoImage();
        super.onDestroyView();
    }

    public void onPhotoClicked(ImageListServiceOutput imagelistserviceoutput)
    {
    }

    public void setSelected(boolean flag)
    {
        if(flag)
        {
            mLayout.setBackgroundColor(0xff36b5ff);
            return;
        } else
        {
            mLayout.setBackgroundColor(0);
            return;
        }
    }

    private ImageListServiceOutput mData;
    private RelativeLayout mLayout;
    private ImageButton mPhoto;
    private PhotoThumbnailImageService mPhotoListImageService;
    private ProgressBar mProgressBar;
    public float scale;





/*
    static PhotoThumbnailImageService access$302(PhotoThumbnailFragment photothumbnailfragment, PhotoThumbnailImageService photothumbnailimageservice)
    {
        photothumbnailfragment.mPhotoListImageService = photothumbnailimageservice;
        return photothumbnailimageservice;
    }

*/

}
