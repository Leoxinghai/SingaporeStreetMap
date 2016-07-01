// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.trafficcamera;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.util.ArrayList;
import java.util.Iterator;
import streetdirectory.mobile.core.Action;
import streetdirectory.mobile.core.SDStory;
import streetdirectory.mobile.modules.SDActivity;
import streetdirectory.mobile.modules.map.MapActivity;
import streetdirectory.mobile.sd.SDMapSideMenuLayout;
import streetdirectory.mobile.service.URLFactory;

// Referenced classes of package streetdirectory.mobile.modules.trafficcamera:
//            AreaList, AreaListAdapter, TrafficArea

public class TrafficCameraActivity extends SDActivity
{

    public TrafficCameraActivity()
    {
    }

    private void initData()
    {
        mCountryCode = getIntent().getStringExtra("countryCode");
        mAreaList = AreaList.getAreaList(mCountryCode);
        mAreaListAdapter = new AreaListAdapter(this);
        mAreaListAdapter.clear();
        mAreaListAdapter.notifyDataSetChanged();
        if(mAreaList != null)
        {
            TrafficArea trafficarea;
            for(Iterator iterator = mAreaList.iterator(); iterator.hasNext(); mAreaListAdapter.add(trafficarea))
                trafficarea = (TrafficArea)iterator.next();

            mAreaListAdapter.notifyDataSetChanged();
        }
        mSpinnerArea.setAdapter(mAreaListAdapter);
    }

    private void initEvent()
    {
        mSideMenuLayout.setOnSlideOpen(new Action() {

            public void execute()
            {
                mSideMenuBlocker.setVisibility(View.VISIBLE);
            }

        });
        mSideMenuLayout.setOnSlideClose(new Action() {

            public void execute()
            {
                mSideMenuBlocker.setVisibility(View.INVISIBLE);
            }

        });
        mSideMenuBlocker.setOnTouchListener(new android.view.View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionevent)
            {
                mSideMenuLayout.touchExecutor(motionevent);
                return true;
            }

        });
        mMenuButton.setOnTouchListener(new android.view.View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionevent)
            {
                mSideMenuLayout.touchExecutor(motionevent);
                return false;
            }

        });
        mMenuButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                if(mSideMenuLayout.getIsMenuOpen())
                {
                    mSideMenuLayout.slideClose();
                    return;
                } else
                {
                    mSideMenuLayout.slideOpen();
                    return;
                }
            }

        });
        mSpinnerArea.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView adapterview, View view, int i, long l)
            {
                l = mSpinnerArea.getSelectedItemId();
                mActiveIndex = (int)l;
                loadWebView((int)l);
            }

            public void onNothingSelected(AdapterView adapterview)
            {
            }

        });
        mRefreshButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                loadWebView(mActiveIndex);
            }

        });
        mWebView.setWebViewClient(new WebViewClient() {

            public void onReceivedError(WebView webview, int i, String s, String s1)
            {
                Toast.makeText(TrafficCameraActivity.this, "No internet connection, tap refresh to try again", 0).show();
            }

            public boolean shouldOverrideUrlLoading(WebView webview, String s)
            {
                return !"id".equals(mCountryCode);
            }

            public boolean shuldOverrideKeyEvent(WebView webview)
            {
                return true;
            }

        });
    }

    private void initLayout()
    {
        mSideMenuLayout = (SDMapSideMenuLayout)findViewById(R.id.side_menu);
        mSideMenuBlocker = findViewById(R.id.side_menu_blocker);
        mMenuButton = (ImageButton)findViewById(R.id.MenuButton);
        mSpinnerArea = (Spinner)findViewById(R.id.spinner_area);
        mWebView = (WebView)findViewById(R.id.web_view);
        mRefreshButton = (Button)findViewById(R.id.RefreshButton);
        mSearchListView = (ListView)mSideMenuLayout.findViewById(R.id.SearchListView);
        mMenuListView = (ListView)mSideMenuLayout.findViewById(R.id.MenuListView);
        mSearchField = (EditText)mSideMenuLayout.findViewById(R.id.MenuSearchField);
        mgr = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    private void initialize()
    {
        initLayout();
        initData();
        initEvent();
    }

    private void loadWebView(int i)
    {
        TrafficArea trafficarea = (TrafficArea)mAreaList.get(i);
        if("id".equals(mCountryCode))
        {
            mWebView.loadUrl(URLFactory.createURLTrafficCamera(trafficarea.title, trafficarea.title, mCountryCode));
            return;
        } else
        {
            mWebView.loadUrl(URLFactory.createURLTrafficCamera(trafficarea.type, trafficarea.title, mCountryCode));
            return;
        }
    }

    private void showExitConfirmDialog()
    {
        (new android.app.AlertDialog.Builder(this)).setTitle("Confirmation").setMessage("Are you sure want to exit ?").setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialoginterface, int i)
            {
                finish();
            }

        }).setNegativeButton("Cancel", new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialoginterface, int i)
            {
                dialoginterface.cancel();
            }

        }).create().show();
    }

    public void onBackPressed()
    {
        if(mSideMenuLayout.getIsMenuOpen())
        {
            if(mSearchListView.getVisibility() == 0)
            {
                mMenuListView.setVisibility(View.VISIBLE);
                mSearchListView.setVisibility(View.INVISIBLE);
                mgr.hideSoftInputFromWindow(mSearchField.getWindowToken(), 0);
                mSideMenuLayout.requestFocus();
                mSideMenuLayout.slideOpen(77);
                return;
            } else
            {
                Intent intent = new Intent(this, MapActivity.class);
                intent.setFlags(0x4000000);
                intent.putExtra("EXIT", true);
                startActivity(intent);
                finish();
                return;
            }
        } else
        {
            mSideMenuLayout.slideOpen();
            return;
        }
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_traffic_camera);
        SDStory.post(URLFactory.createGantTrafficMain(), SDStory.createDefaultParams());
        initialize();
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        if(mSideMenuLayout.getIsMenuOpen())
            mSideMenuLayout.slideClose();
        else
            mSideMenuLayout.slideOpen();
        return false;
    }

    protected void onResume()
    {
        super.onResume();
    }

    protected void onStop()
    {
        super.onStop();
        mWebView.stopLoading();
    }

    private int mActiveIndex;
    private ArrayList mAreaList;
    private AreaListAdapter mAreaListAdapter;
    private String mCountryCode;
    private ImageButton mMenuButton;
    private ListView mMenuListView;
    private Button mRefreshButton;
    private EditText mSearchField;
    private ListView mSearchListView;
    private View mSideMenuBlocker;
    private SDMapSideMenuLayout mSideMenuLayout;
    private Spinner mSpinnerArea;
    private WebView mWebView;
    private InputMethodManager mgr;






/*
    static int access$302(TrafficCameraActivity trafficcameraactivity, int i)
    {
        trafficcameraactivity.mActiveIndex = i;
        return i;
    }

*/


}
