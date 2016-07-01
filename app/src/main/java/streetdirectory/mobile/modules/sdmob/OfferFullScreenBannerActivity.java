// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.sdmob;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.*;
import android.widget.*;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.SDApplication;

// Referenced classes of package streetdirectory.mobile.modules.sdmob:
//            SdMobHelper

public class OfferFullScreenBannerActivity extends Activity
{

    public OfferFullScreenBannerActivity()
    {
    }

    protected void onCreate(final Bundle progressBar)
    {
        onCreate(progressBar);
        setContentView(R.layout.activity_offer_full_screen_banner);
        mWebView = (WebView)findViewById(R.id.webView);
        mButtonClose = (ImageButton)findViewById(R.id.button_close);
        mButtonRemove = (Button)findViewById(R.id.button_remove);
        final View lview = (ProgressBar)findViewById(R.id.progressBar1);
        mWebView.getSettings().setJavaScriptEnabled(true);
        getIntent().getIntExtra("banner_type", 0);
        SDApplication.setFullBannerVisible(false);
        mWebView.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView webview, int i)
            {
                onProgressChanged(webview, i);
                if(i >= 100)
                {
                    lview.setVisibility(View.INVISIBLE);
                    mButtonRemove.setVisibility(View.VISIBLE);
                    mButtonClose.setVisibility(View.VISIBLE);
                }
            }

        });
        final GestureDetectorCompat gestureDetectorCompat = new GestureDetectorCompat(this, new android.view.GestureDetector.SimpleOnGestureListener() {

            public boolean onSingleTapUp(MotionEvent motionevent)
            {
                SdMobHelper.getInstance(OfferFullScreenBannerActivity.this);
                return true;
            }

        });
        mWebView.setOnTouchListener(new android.view.View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionevent)
            {
                return gestureDetectorCompat.onTouchEvent(motionevent);
            }

        });
        mButtonClose.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                finish();
            }

        });

        mButtonRemove.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                finish();
            }

        });
    }

    private ImageButton mButtonClose;
    private Button mButtonRemove;
    private WebView mWebView;


}
