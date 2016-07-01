// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.businessdetail;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.*;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.util.*;
import streetdirectory.mobile.core.*;
import streetdirectory.mobile.modules.SDActivity;
import streetdirectory.mobile.modules.businessdetail.service.BusinessDetailService;
import streetdirectory.mobile.modules.businessdetail.service.BusinessDetailServiceInput;
import streetdirectory.mobile.modules.businessdetail.service.BusinessDetailServiceOutput;
import streetdirectory.mobile.modules.businessdetail.service.BusinessOfferVoteService;
import streetdirectory.mobile.modules.businessdetail.service.BusinessOfferVoteServiceInput;
import streetdirectory.mobile.modules.businessdetail.service.BusinessOfferVoteServiceOutput;
import streetdirectory.mobile.modules.core.LocationBusinessServiceOutput;
import streetdirectory.mobile.sd.SDBlackboard;
import streetdirectory.mobile.service.SDHttpServiceOutput;
import streetdirectory.mobile.service.URLFactory;

public class BusinessOfferWebPageActivity extends SDActivity
{

    public BusinessOfferWebPageActivity()
    {
    }

    private void downloadBusinessDetail()
    {
        if(mBusinessDetailService != null)
        {
            mBusinessDetailService.abort();
            mBusinessDetailService = null;
        }
        mBusinessDetailService = new BusinessDetailService(new BusinessDetailServiceInput(mData.countryCode, SDStoryStats.SDuId, mData.companyID, mData.locationID, mData.promoId)) {

            public void onAborted(Exception exception)
            {
                SDLogger.info("Business Detail Aborted");
            }

            public void onFailed(Exception exception)
            {
                SDLogger.printStackTrace(exception, "Business Detail Failed");
                mBusinessDetailService = null;
            }

            public void onSuccess(Object obj)
            {
                onSuccess((SDHttpServiceOutput)obj);
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                mBusinessDetailService = null;
                if(sdhttpserviceoutput.childs.size() > 0)
                {
                    java.util.Map.Entry entry;
                    Iterator iterator;
                    for(iterator = ((BusinessDetailServiceOutput)sdhttpserviceoutput.childs.get(0)).hashData.entrySet().iterator(); iterator.hasNext(); mData.hashData.put(entry.getKey(), entry.getValue()))
                        entry = (java.util.Map.Entry)iterator.next();

                    mData.populateData();
                    populateVoteCount();
                }
            }

        };
        mBusinessDetailService.executeAsync();
    }

    private void initData()
    {
        Intent intent = getIntent();
        url = intent.getStringExtra("offer_url");
        company = intent.getStringExtra("offer_title");
        mData = (LocationBusinessServiceOutput)intent.getParcelableExtra("data");
        showVoteBar = intent.getBooleanExtra("show_vote_bar", true);
        if(!showVoteBar)
        {
            mLayoutVoteBar.setVisibility(View.INVISIBLE);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.setWebViewClient(new WebViewClient() {

                public void onPageFinished(WebView webview, String s)
                {
                    mProgressBar.setVisibility(View.INVISIBLE);
                }

            });
        }
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        if(url != null && url.length() > 0)
            mWebView.loadUrl(url);
        if(company != null && company.length() > 0)
            mTextViewTitle.setText(company);
        if(mData != null)
        {
            downloadBusinessDetail();
            SDStory.post(URLFactory.createGantBusinessOffers(mData.companyID, mData.promoId), SDStory.createDefaultParams());
        }
    }

    private void initEvent()
    {
        mButtonCall.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                if(mData == null || mData.phoneNumber == null || mData.phoneNumber.length() <= 0) {
                    //Log.e("", "Call failed", view);
                    return;
                }
                Intent intent = new Intent("android.intent.action.DIAL");
                intent.setData(Uri.parse((new StringBuilder()).append("tel:").append(mData.phoneNumber.replace(" ", "")).toString()));
                HashMap hashmap = SDStory.createDefaultParams();
                hashmap.put("c_id", Integer.toString(mData.companyID));
                hashmap.put("c_pid", mData.phoneNumber);
                hashmap.put("opg", "bottom_banner");
                SDStory.post(URLFactory.createGantBusinessCall((new StringBuilder()).append(mData.companyID).append("").toString()), hashmap);
                startActivity(intent);
                return;
            }

        });

        mButtonUpVote.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                voteBusinessOffer(1);
                int i = Integer.parseInt(mLabelUpVote.getText().toString());
                mLabelUpVote.setText((new StringBuilder()).append(i + 1).append("").toString());
                mButtonUpVote.setSelected(true);
                mButtonUpVote.setEnabled(false);
                if(mData.promoVote.equals("-1"))
                {
                    int j = Integer.parseInt(mLabelDownVote.getText().toString());
                    mLabelDownVote.setText((new StringBuilder()).append(j - 1).append("").toString());
                    mButtonDownVote.setSelected(false);
                    mButtonDownVote.setEnabled(true);
                }
                mData.promoVote = "1";
            }

        });
        mButtonDownVote.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                voteBusinessOffer(-1);
                int i = Integer.parseInt(mLabelDownVote.getText().toString());
                mLabelDownVote.setText((new StringBuilder()).append(i + 1).append("").toString());
                mButtonDownVote.setSelected(true);
                mButtonDownVote.setEnabled(false);
                if(mData.promoVote.equals("1"))
                {
                    int j = Integer.parseInt(mLabelUpVote.getText().toString());
                    mLabelUpVote.setText((new StringBuilder()).append(j - 1).append("").toString());
                    mButtonUpVote.setSelected(false);
                    mButtonUpVote.setEnabled(true);
                }
                mData.promoVote = "-1";
            }

        });
        mWebView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView webview, String s) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            public boolean shouldOverrideUrlLoading(WebView webview, String s) {
                if (s.startsWith("mailto:")) ;
                return true;
            }

        });
        mBackButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                finish();
            }

        });
    }

    private void initLayout()
    {
        mLayoutVoteBar = (LinearLayout)findViewById(R.id.bottomBarVote);
        mButtonUpVote = (Button)findViewById(R.id.buttonVoteUp);
        mButtonDownVote = (Button)findViewById(R.id.buttonVoteDown);
        mLabelUpVote = (TextView)findViewById(R.id.textViewVoteUp);
        mLabelDownVote = (TextView)findViewById(R.id.textViewVoteDown);
        mBackButton = (Button)findViewById(R.id.BackButton);
        mTextViewTitle = (TextView)findViewById(R.id.TitleBar);
        mWebView = (WebView)findViewById(R.id.webView);
        mButtonCall = (ImageButton)findViewById(R.id.imageButtonCall);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar1);
    }

    private void populateVoteCount()
    {
        if(mData != null)
        {
            if(mData.promoMinus != null)
                mLabelDownVote.setText(mData.promoMinus);
            if(mData.promoPlus != null)
                mLabelUpVote.setText(mData.promoPlus);
            if(mData.promoVote.equals("1"))
            {
                mButtonUpVote.setSelected(true);
                mButtonUpVote.setEnabled(false);
            } else
            if(mData.promoVote.equals("-1"))
            {
                mButtonDownVote.setSelected(true);
                mButtonDownVote.setEnabled(false);
                return;
            }
        }
    }

    private void voteBusinessOffer(int i)
    {
        if(mService != null)
        {
            mService.abort();
            mService = null;
        }
        mService = new BusinessOfferVoteService(new BusinessOfferVoteServiceInput(SDBlackboard.currentCountryCode, mData.companyID, mData.promoId, SDStoryStats.SDuId, i)) {

            public void onFailed(Exception exception)
            {
                super.onFailed(exception);
                Toast.makeText(BusinessOfferWebPageActivity.this, "Failed to vote business offer", 0).show();
            }

            public void onSuccess(Object obj)
            {
                onSuccess((SDHttpServiceOutput)obj);
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                super.onSuccess(sdhttpserviceoutput);
                if(!((BusinessOfferVoteServiceOutput)sdhttpserviceoutput.childs.get(0)).result);
            }

        };
        mService.executeAsync();
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.layout_webview);
        initLayout();
        initEvent();
        initData();
    }

    private String company;
    private Button mBackButton;
    private BusinessDetailService mBusinessDetailService;
    private ImageButton mButtonCall;
    private Button mButtonDownVote;
    private Button mButtonUpVote;
    private LocationBusinessServiceOutput mData;
    private TextView mLabelDownVote;
    private TextView mLabelUpVote;
    private LinearLayout mLayoutVoteBar;
    private ProgressBar mProgressBar;
    private BusinessOfferVoteService mService;
    private TextView mTextViewTitle;
    private WebView mWebView;
    private boolean showVoteBar;
    private String url;









/*
    static BusinessDetailService access$702(BusinessOfferWebPageActivity businessofferwebpageactivity, BusinessDetailService businessdetailservice)
    {
        businessofferwebpageactivity.mBusinessDetailService = businessdetailservice;
        return businessdetailservice;
    }

*/

}
