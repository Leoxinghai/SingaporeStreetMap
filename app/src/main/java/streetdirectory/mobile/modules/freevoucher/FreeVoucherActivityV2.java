// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.freevoucher;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.webkit.*;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.util.ArrayList;
import streetdirectory.mobile.core.SDLogger;
import streetdirectory.mobile.core.SDStory;
import streetdirectory.mobile.modules.SDActivity;
import streetdirectory.mobile.service.SDHttpServiceOutput;
import streetdirectory.mobile.service.URLFactory;

// Referenced classes of package streetdirectory.mobile.modules.freevoucher:
//            VoucherListService, VoucherListServiceOutput

public class FreeVoucherActivityV2 extends SDActivity
{

    public FreeVoucherActivityV2()
    {
        voucherList = new ArrayList();
    }

    private void initData()
    {
        Intent intent = getIntent();
        mCountryCode = intent.getStringExtra("country_code");
        companyName = intent.getStringExtra("company_name");
        mOfferId = intent.getStringExtra("offer_id");
        companyID = intent.getIntExtra("company_ID", 0);
        SDStory.post(URLFactory.createGantRedeemVoucherPage(mOfferId, companyID), SDStory.createDefaultParams());
        if(mCountryCode == null)
            mCountryCode = "sg";
        if(companyName != null)
        {
            String s = (new StringBuilder()).append("Simply show your receipt to the staff at\n").append(companyName).append("\nto collect your free voucher (while stocks last).").toString();
            SpannableString spannablestring = new SpannableString(s);
            spannablestring.setSpan(new ForegroundColorSpan(Color.parseColor("#339933")), s.indexOf(companyName), s.indexOf("to collect"), 33);
            mTextViewDescription.setText(spannablestring);
            mTextViewDescription.setMovementMethod(LinkMovementMethod.getInstance());
        }
        mSpinnerVoucher.setEnabled(false);
    }

    private void initEvent()
    {
        mSpinnerVoucher.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView adapterview, View view, int i, long l)
            {
                String temp = ((VoucherListServiceOutput)voucherList.get(mSpinnerVoucher.getSelectedItemPosition())).voucherId;
                SDStory.post(URLFactory.createGantRedeemVoucherVoucherList(mOfferId, companyID, temp), SDStory.createDefaultParams());
            }

            public void onNothingSelected(AdapterView adapterview)
            {
            }

        });
        mImageButtonClose.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                finish();
            }

        });
        mTextViewTerms.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                SDStory.post(URLFactory.createGantRedeemVoucherTAC(mOfferId, companyID), SDStory.createDefaultParams());
                showTermsDialog();
            }

        });
    }

    private void initView()
    {
        mTextViewDescription = (TextView)findViewById(R.id.textview_description);
        mImageButtonClose = (ImageView)findViewById(R.id.imageButton_close);
        mSpinnerVoucher = (Spinner)findViewById(R.id.spinner_voucher);
        mTextViewTerms = (TextView)findViewById(R.id.textView_terms);
        mViewTermsDialog = findViewById(R.id.layout_terms_dialog);
        mWebViewTermsDescription = (WebView)findViewById(R.id.webView_terms_description);
        mButtonOkTerms = (Button)findViewById(R.id.button_ok_terms);
        mProgressBarTerms = (ProgressBar)findViewById(R.id.progressBar_terms);
    }

    private void initVoucherList()
    {
        voucherListService = new VoucherListService(mCountryCode) {

            public void onFailed(Exception exception)
            {
                super.onFailed(exception);
                SDLogger.debug(exception.getMessage());
            }

            public void onSuccess(Object obj)
            {
                onSuccess((SDHttpServiceOutput)obj);
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                super.onSuccess(sdhttpserviceoutput);
                voucherList = sdhttpserviceoutput.childs;
                String temp[] = new String[sdhttpserviceoutput.childs.size() + 1];
                temp[0] = getString(R.string.free_voucher_available_voucher);
                for(int i = 0; i < voucherList.size(); i++)
                    temp[i + 1] = ((VoucherListServiceOutput)voucherList.get(i)).voucherName;

                ArrayAdapter temp2 = new ArrayAdapter(FreeVoucherActivityV2.this, R.layout.cell_spinner_voucher_item, temp);
                mSpinnerVoucher.setAdapter(temp2);
                mSpinnerVoucher.setEnabled(true);
            }

        };
        voucherListService.executeAsync();
    }

    private void showTermsDialog()
    {
        mViewTermsDialog.setVisibility(View.VISIBLE);
        mWebViewTermsDescription.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView webview, String s)
            {
                mProgressBarTerms.setVisibility(View.INVISIBLE);
            }

        });
        mWebViewTermsDescription.getSettings().setLoadWithOverviewMode(true);
        mWebViewTermsDescription.getSettings().setDefaultTextEncodingName("utf-8");
        mWebViewTermsDescription.loadUrl(URLFactory.createURLVoucherTermsAndConditions());
        mButtonOkTerms.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                mViewTermsDialog.setVisibility(View.INVISIBLE);
            }

        });
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_free_voucher_v2);
        initView();
        initData();
        initEvent();
        initVoucherList();
    }

    private int companyID;
    private String companyName;
    private Button mButtonOkTerms;
    private String mCountryCode;
    private ImageView mImageButtonClose;
    private String mOfferId;
    private ProgressBar mProgressBarTerms;
    private Spinner mSpinnerVoucher;
    private TextView mTextViewDescription;
    private TextView mTextViewTerms;
    private View mViewTermsDialog;
    private WebView mWebViewTermsDescription;
    private ArrayList voucherList;
    private VoucherListService voucherListService;




/*
    static ArrayList access$102(FreeVoucherActivityV2 freevoucheractivityv2, ArrayList arraylist)
    {
        freevoucheractivityv2.voucherList = arraylist;
        return arraylist;
    }

*/





}
