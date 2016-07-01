// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.message;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.util.ArrayList;
import java.util.Iterator;
import streetdirectory.mobile.modules.SDActivity;
import streetdirectory.mobile.modules.message.service.MessageListService;
import streetdirectory.mobile.modules.message.service.MessageListServiceInput;
import streetdirectory.mobile.modules.message.service.MessageListServiceOutput;
import streetdirectory.mobile.sd.SDMapSideMenuLayout;
import streetdirectory.mobile.service.SDHttpServiceOutput;

// Referenced classes of package streetdirectory.mobile.modules.message:
//            MessageInboxAdapter, MessageDetailActivity

public class MessageActivity extends SDActivity
{

    public MessageActivity()
    {
    }

    private void abortAllDownload()
    {
        mMessageListService.abort();
        mMessageListService = null;
    }

    private void downloadInboxMessages(int i, int j)
    {
        final int start = i;

        mMessageListService = new MessageListService(null) {

            public void onFailed(Exception exception)
            {
                onFailed(exception);
                mMessageListService = null;
            }

            public void onSuccess(Object obj)
            {
                onSuccess((SDHttpServiceOutput)obj);
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                mMessageListService = null;
                if(sdhttpserviceoutput.childs.size() > 0)
                {
                    if(start == 0)
                        mListAdapter.removeAllData();
                    MessageListServiceOutput messagelistserviceoutput;
                    Iterator iterator;
                    for(iterator = sdhttpserviceoutput.childs.iterator(); iterator.hasNext(); mListAdapter.add(messagelistserviceoutput))
                        messagelistserviceoutput = (MessageListServiceOutput)iterator.next();

                }
                mListAdapter.notifyDataSetChanged();
                mMessageListService = null;
            }

        };
        mMessageListService.executeAsync();
    }

    private void initData()
    {
        Intent intent = getIntent();
        mCountryCode = intent.getStringExtra("countryCode");
        mUID = intent.getStringExtra("uid");
        mUID = "100002515725344";
        mListAdapter = new MessageInboxAdapter(this);
        mLimit = 10;
        initListView();
    }

    private void initEvent()
    {
        mImagebuttonMenu.setOnTouchListener(new android.view.View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionevent)
            {
                mSideMenu.touchExecutor(motionevent);
                return false;
            }

        });
        mImagebuttonMenu.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                if(mSideMenu.getIsMenuOpen())
                {
                    mSideMenu.slideClose();
                    return;
                } else
                {
                    mSideMenu.slideOpen();
                    return;
                }
            }

        });
        mListViewMessages.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView adapterview, View view, int i, long l)
            {
                MessageListServiceOutput temp = mListAdapter.getItem(i);
                Intent intent = new Intent(MessageActivity.this, MessageDetailActivity.class);
                intent.putExtra("countryCode", mCountryCode);
                intent.putExtra("uid", mUID);
                intent.putExtra("mid", ((MessageListServiceOutput) (temp)).messageID);
                intent.putExtra("mType", "inbox");
                startActivity(intent);
            }

        });
    }

    private void initLayout()
    {
        mSideMenu = (SDMapSideMenuLayout)findViewById(R.id.side_menu);
        mImagebuttonMenu = (ImageButton)findViewById(R.id.imagebutton_menu);
        mListViewMessages = (ListView)findViewById(R.id.list_view_messages);
    }

    private void initListView()
    {
        if(mListViewMessages != null)
        {
            mListAdapter = new MessageInboxAdapter(this);
            mListAdapter.setOnLoadMoreListener(new MessageInboxAdapter.OnLoadMoreListener() {

                public void onLoadMoreList()
                {
                    downloadInboxMessages(mListAdapter.getCount() - 1, mLimit);
                }
            });
            mListViewMessages.setAdapter(mListAdapter);
        }
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
        setContentView(R.layout.activity_message_inbox);
        initialize();
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        if(mSideMenu.getIsMenuOpen())
            mSideMenu.slideClose();
        else
            mSideMenu.slideOpen();
        return false;
    }

    protected void onResume()
    {
        super.onResume();
        downloadInboxMessages(0, mLimit);
    }

    private String mCountryCode;
    private ImageButton mImagebuttonMenu;
    private int mLimit;
    private MessageInboxAdapter mListAdapter;
    private ListView mListViewMessages;
    private MessageListService mMessageListService;
    private SDMapSideMenuLayout mSideMenu;
    private String mUID;








/*
    static MessageListService access$602(MessageActivity messageactivity, MessageListService messagelistservice)
    {
        messageactivity.mMessageListService = messagelistservice;
        return messagelistservice;
    }

*/
}
