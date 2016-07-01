// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.photopreview;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinghai.mycurve.R;

import java.io.File;
import streetdirectory.mobile.core.SDLogger;
import streetdirectory.mobile.modules.photopreview.service.ImageListServiceOutput;
import streetdirectory.mobile.modules.photopreview.service.PhotoThumbnailImageService;
import streetdirectory.mobile.modules.photopreview.service.PhotoThumbnailImageServiceInput;
import streetdirectory.mobile.service.SDHttpImageService;

public class PhotoPreviewFragment extends Fragment
{

    public PhotoPreviewFragment()
    {
    }

    private void abortDownloadPhotoImage()
    {
        if(mPhotoListImageService != null)
        {
            mPhotoListImageService.abort();
            mPhotoListImageService = null;
        }
    }

    private void abortDownloadPhotoPreviewImage()
    {
        if(mPhotoPreviewImageService != null)
        {
            mPhotoPreviewImageService.abort();
            mPhotoPreviewImageService = null;
        }
    }

    private void downloadPhotoImage(ImageListServiceOutput imagelistserviceoutput, int i, int j)
    {
        abortDownloadPhotoImage();
        abortDownloadPhotoPreviewImage();
        final ImageListServiceOutput data = imagelistserviceoutput;
        mPhotoListImageService = new PhotoThumbnailImageService("") {

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
                downloadPhotoPreviewImage(data.imageURL);
            }

            public void onSuccess(Object obj)
            {
                onSuccess((Bitmap)obj);
            }

        };
        mPhotoListImageService.executeAsync();
    }

    private void downloadPhotoPreviewImage(String s)
    {
        abortDownloadPhotoImage();
        mPhotoPreviewImageService = new SDHttpImageService(s) {

            public void onAborted(Exception exception)
            {
                SDLogger.info("Photo Image Preview Aborted");
            }

            public void onFailed(Exception exception)
            {
                SDLogger.printStackTrace(exception, "Photo Image Preview Failed");
                mPhotoPreviewImageService = null;
            }

            public void onSuccess(Bitmap bitmap)
            {
                mPhotoPreviewImageService = null;
                mPhoto.setImageBitmap(bitmap);
            }

            public void onSuccess(Object obj)
            {
                onSuccess((Bitmap)obj);
            }

        };
        mPhotoPreviewImageService.executeAsync();
    }

    private void initData()
    {
        mData = getData();
        if(mData != null)
        {
            mTitle.setVisibility(View.INVISIBLE);
            mPhoto.setVisibility(View.VISIBLE);
            File file = (new PhotoThumbnailImageServiceInput(mData.imageURL)).getSaveFile();
            if(file != null && file.exists())
            {
                downloadPhotoImage(mData, 0, 0);
                return;
            } else
            {
                downloadPhotoPreviewImage(mData.imageURL);
                return;
            }
        } else
        {
            mTitle.setVisibility(View.VISIBLE);
            Resources resources = getResources();
            mTitle.setText((new StringBuilder()).append(resources.getString(R.string.photo_preview_add_photo_title)).append(" ").append(resources.getString(R.string.app_name)).toString());
            mPhoto.setVisibility(View.INVISIBLE);
            return;
        }
    }

    private void initEvent()
    {
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
        View temp = layoutinflater.inflate(R.layout.fragment_photo_preview, viewgroup, false);
        mTitle = (TextView)temp.findViewById(R.id.textview_title);
        mPhoto = (ImageView)temp.findViewById(R.id.imageview_photo);
        initData();
        initEvent();
        return temp;
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

    private ImageListServiceOutput mData;
    private ImageView mPhoto;
    private PhotoThumbnailImageService mPhotoListImageService;
    private SDHttpImageService mPhotoPreviewImageService;
    private TextView mTitle;


/*
    static PhotoThumbnailImageService access$002(PhotoPreviewFragment photopreviewfragment, PhotoThumbnailImageService photothumbnailimageservice)
    {
        photopreviewfragment.mPhotoListImageService = photothumbnailimageservice;
        return photothumbnailimageservice;
    }

*/




/*
    static SDHttpImageService access$302(PhotoPreviewFragment photopreviewfragment, SDHttpImageService sdhttpimageservice)
    {
        photopreviewfragment.mPhotoPreviewImageService = sdhttpimageservice;
        return sdhttpimageservice;
    }

*/
}
