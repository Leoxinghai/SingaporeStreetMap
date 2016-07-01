// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.message;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.util.ArrayList;
import streetdirectory.mobile.modules.SDActivity;
import streetdirectory.mobile.modules.message.service.MessageDetailService;
import streetdirectory.mobile.modules.message.service.MessageDetailServiceInput;
import streetdirectory.mobile.modules.message.service.MessageDetailServiceOutput;
import streetdirectory.mobile.service.SDHttpServiceOutput;

public class MessageDetailActivity extends SDActivity
{

    public MessageDetailActivity()
    {
    }

    private void abortAllDownload()
    {
        mMessageDetailService.abort();
        mMessageDetailService = null;
        mWebView.stopLoading();
    }

    private void downloadMessageDetail()
    {
        if(mMessageDetailService != null)
        {
            mMessageDetailService.abort();
            mMessageDetailService = null;
        }
        mMessageDetailService = new MessageDetailService(new MessageDetailServiceInput(mCountryCode, mUID, mMessageID, mMessageType, mAtt)) {

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
                    MessageDetailServiceOutput temp = (MessageDetailServiceOutput)sdhttpserviceoutput.childs.get(0);
                    mWebView.loadData(((MessageDetailServiceOutput) (temp)).message, "text/html; charset=UTF-8", null);
                }
            }

        };
        mMessageDetailService.executeAsync();
    }

    private void initData()
    {
        Intent intent = getIntent();
        mCountryCode = intent.getStringExtra("countryCode");
        mUID = intent.getStringExtra("uid");
        mMessageID = intent.getStringExtra("mid");
        mMessageType = intent.getStringExtra("mType");
        mAtt = "mini";
    }

    private void initEvent()
    {
        mBackButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                finish();
            }

        });
    }

    private void initLayout()
    {
        mMenuBar = (RelativeLayout)findViewById(R.id.MenuBar);
        mBackButton = (Button)findViewById(R.id.BackButton);
        mTitleBar = (TextView)findViewById(R.id.TitleBar);
        mButtonPrev = (Button)findViewById(R.id.button_prev);
        mButtonNext = (Button)findViewById(R.id.button_next);
        mTextViewSender = (TextView)findViewById(R.id.text_view_sender);
        mTextViewSubject = (TextView)findViewById(R.id.text_view_subject);
        mTextViewDate = (TextView)findViewById(R.id.text_view_date);
        mWebView = (WebView)findViewById(R.id.web_view);
        mButtonDelete = (Button)findViewById(R.id.button_delete);
        mButtonReply = (Button)findViewById(R.id.button_reply);
        mButtonNew = (Button)findViewById(R.id.button_new);
    }

    private void initialize()
    {
        initLayout();
        initData();
        initEvent();
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_message_detail);
        initialize();
    }

    protected void onDestroy()
    {
        abortAllDownload();
        super.onDestroy();
    }

    protected void onResume()
    {
        super.onResume();
        downloadMessageDetail();
    }

    private String mAtt;
    private Button mBackButton;
    private Button mButtonDelete;
    private Button mButtonNew;
    private Button mButtonNext;
    private Button mButtonPrev;
    private Button mButtonReply;
    private String mCountryCode;
    private RelativeLayout mMenuBar;
    private MessageDetailService mMessageDetailService;
    private String mMessageID;
    private String mMessageType;
    private TextView mTextViewDate;
    private TextView mTextViewSender;
    private TextView mTextViewSubject;
    private TextView mTitleBar;
    private String mUID;
    private WebView mWebView;

}
