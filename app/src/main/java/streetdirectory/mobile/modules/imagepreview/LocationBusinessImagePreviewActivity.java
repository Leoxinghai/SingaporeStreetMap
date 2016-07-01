// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.imagepreview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.*;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.core.SDLogger;
import streetdirectory.mobile.modules.SDActivity;
import streetdirectory.mobile.modules.core.LocationBusinessServiceOutput;
import streetdirectory.mobile.service.SDHttpImageService;

public class LocationBusinessImagePreviewActivity extends SDActivity
{

    public LocationBusinessImagePreviewActivity()
    {
    }

    private void downloadImage()
    {
        if(_data != null)
        {
            String s = _data.generateImageURL(this, _previewImage.getWidth(), _previewImage.getHeight());
            if(s != null)
            {
                SDLogger.info((new StringBuilder()).append("Image Preview URL: ").append(s).toString());
                _imageService = new SDHttpImageService(s) {

                    public void onAborted(Exception exception)
                    {
                        _imageService = null;
                    }

                    public void onFailed(Exception exception)
                    {
                        _imageService = null;
                    }

                    public void onSuccess(Bitmap bitmap)
                    {
                        _previewImage.setImageBitmap(bitmap);
                    }

                };
                _imageService.executeAsync();
            }
        }
    }

    private void initData()
    {
        Object obj = getIntent();
        Object obj1 = ((Intent) (obj)).getStringExtra("title");
        if(obj1 != null)
            _titleLabel.setText(((CharSequence) (obj1)));
        obj1 = (Bitmap)((Intent) (obj)).getParcelableExtra("thumbnail");
        if(obj1 != null)
            _previewImage.setImageBitmap(((Bitmap) (obj1)));
        obj = (LocationBusinessServiceOutput)((Intent) (obj)).getParcelableExtra("data");
        if(obj != null)
            _data = ((LocationBusinessServiceOutput) (obj));
    }

    private void initEvent()
    {
        _cancelButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                finish();
            }

        });
    }

    private void initLayout()
    {
        _titleLabel = (TextView)findViewById(R.id.TitleLabel);
        _cancelButton = (Button)findViewById(R.id.CancelButton);
        _previewImage = (ImageView)findViewById(R.id.PreviewImage);
    }

    protected void abortAllProcess()
    {
        if(_imageService != null)
        {
            _imageService.abort();
            _imageService = null;
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
        setContentView(R.layout.activity_image_preview);
        initLayout();
        initData();
        initEvent();
        _previewImage.getViewTreeObserver().addOnGlobalLayoutListener(new android.view.ViewTreeObserver.OnGlobalLayoutListener() {

            public void onGlobalLayout()
            {
                _previewImage.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                downloadImage();
            }

        });
    }

    private Button _cancelButton;
    private LocationBusinessServiceOutput _data;
    private SDHttpImageService _imageService;
    private ImageView _previewImage;
    private TextView _titleLabel;




/*
    static SDHttpImageService access$202(LocationBusinessImagePreviewActivity locationbusinessimagepreviewactivity, SDHttpImageService sdhttpimageservice)
    {
        locationbusinessimagepreviewactivity._imageService = sdhttpimageservice;
        return sdhttpimageservice;
    }

*/
}
