// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.facebook.Session;
import com.xinghai.mycurve.R;

import java.util.ArrayList;
import streetdirectory.mobile.modules.SDActivity;
import streetdirectory.mobile.modules.map.MapActivity;
import streetdirectory.mobile.modules.settings.disclaimer.DisclaimerTableData;
import streetdirectory.mobile.modules.settings.freemaps.FreeMapsTableData;
import streetdirectory.mobile.sd.SDMapSideMenuLayout;

// Referenced classes of package streetdirectory.mobile.modules.settings:
//            SettingsAdapter, SettingsHeaderTableData, SettingsFooterTableData, SettingsTableData

public class SettingsActivity extends SDActivity
{

    public SettingsActivity()
    {
    }

    private void initData()
    {
        mListAdapter = new SettingsAdapter(this);
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

        }
);
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

        }
);
        mListViewSettings.setOnChildClickListener(new android.widget.ExpandableListView.OnChildClickListener() {

            public boolean onChildClick(ExpandableListView expandablelistview, View view, int i, int j, long l)
            {
                mListAdapter.getChild(i, j).execute();
                if(i == 4)
                {
                    ImageView imageView = (ImageView)view.findViewById(R.id.image_view_cell_arrow);
                    ProgressBar progressbar = (ProgressBar)view.findViewById(R.id.progressbar_fb);
                    ImageView imageview = (ImageView)imageView.findViewById(R.id.image_view_icon);
                    TextView textview = (TextView)view.findViewById(R.id.text_view_title_label);
                    view = (TextView)view.findViewById(R.id.text_view_detail_label);
                    expandablelistview.setVisibility(View.INVISIBLE);
                    imageview.setVisibility(View.INVISIBLE);
                    textview.setVisibility(View.INVISIBLE);
                    view.setVisibility(View.INVISIBLE);
                    progressbar.setVisibility(View.VISIBLE);
                }
                return false;
            }

        });
    }

    private void initLayout()
    {
        mSideMenu = (SDMapSideMenuLayout)findViewById(R.id.side_menu);
        mImagebuttonMenu = (ImageButton)findViewById(R.id.imagebutton_menu);
        mListViewSettings = (ExpandableListView)findViewById(R.id.list_view_settings);
        mSearchListView = (ListView)mSideMenu.findViewById(R.id.SearchListView);
        mMenuListView = (ListView)mSideMenu.findViewById(R.id.MenuListView);
        mSearchField = (EditText)mSideMenu.findViewById(R.id.MenuSearchField);
        mgr = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    private void initListView()
    {
        ArrayList arraylist = new ArrayList();
        mListAdapter.addSectionArray(arraylist);
        arraylist = new ArrayList();
        arraylist.add(new SettingsHeaderTableData(this));
        arraylist.add(new FreeMapsTableData(this));
        arraylist.add(new SettingsFooterTableData(this));
        mListAdapter.addSectionArray(arraylist);
        arraylist = new ArrayList();
        mListAdapter.addSectionArray(arraylist);
        arraylist = new ArrayList();
        mListAdapter.addSectionArray(arraylist);
        arraylist = new ArrayList();
        mListAdapter.addSectionArray(arraylist);
        arraylist = new ArrayList();
        arraylist.add(new SettingsHeaderTableData(this));
        arraylist.add(new DisclaimerTableData(this));
        arraylist.add(new SettingsFooterTableData(this));
        mListAdapter.addSectionArray(arraylist);
        if(mListViewSettings != null)
            mListViewSettings.setAdapter(mListAdapter);
        int j = mListAdapter.getGroupCount();
        for(int i = 0; i < j; i++)
            mListViewSettings.expandGroup(i);

    }

    private void initialize()
    {
        initLayout();
        initData();
        initEvent();
    }

    private void showExitConfirmDialog()
    {
        (new android.app.AlertDialog.Builder(this)).setTitle("Confirmation").setMessage("Are you sure want to exit ?").setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialoginterface, int i)
            {
                finish();
            }

        }
).setNegativeButton("Cancel", new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialoginterface, int i)
            {
                dialoginterface.cancel();
            }

        }).create().show();
    }

    protected void onActivityResult(int i, int j, Intent intent)
    {
        super.onActivityResult(i, j, intent);
        Session.getActiveSession().onActivityResult(this, i, j, intent);
        mListAdapter.notifyDataSetChanged();
    }

    public void onBackPressed()
    {
        if(mSideMenu.getIsMenuOpen())
        {
            if(mSearchListView.getVisibility() == 0)
            {
                mMenuListView.setVisibility(View.VISIBLE);
                mSearchListView.setVisibility(View.INVISIBLE);
                mgr.hideSoftInputFromWindow(mSearchField.getWindowToken(), 0);
                mSideMenu.requestFocus();
                mSideMenu.slideOpen(77);
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
            mSideMenu.slideOpen();
            return;
        }
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_settings);
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
    }

    private ImageButton mImagebuttonMenu;
    private SettingsAdapter mListAdapter;
    private ExpandableListView mListViewSettings;
    private ListView mMenuListView;
    private EditText mSearchField;
    private ListView mSearchListView;
    private SDMapSideMenuLayout mSideMenu;
    private InputMethodManager mgr;


}
