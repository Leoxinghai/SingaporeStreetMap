// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.businessfinder;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.*;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.util.*;
import streetdirectory.mobile.core.*;
import streetdirectory.mobile.modules.businessfinder.service.BusinessFinderServiceOutput;
import streetdirectory.mobile.modules.businesslisting.BusinessListingActivity;
import streetdirectory.mobile.modules.businesslisting.offers.OffersListingActivity;
import streetdirectory.mobile.sd.SDBlackboard;
import streetdirectory.mobile.sd.SDMapSideMenuLayout;
import streetdirectory.mobile.service.*;
import streetdirectory.mobile.service.countrylist.CountryListServiceOutput;
import streetdirectory.mobile.service.statelist.*;

// Referenced classes of package streetdirectory.mobile.modules.businessfinder:
//            BusinessFinderBlankView, BusinessFinderAdapter

public class BusinessFinderActivity extends FragmentActivity
{

    public BusinessFinderActivity()
    {
        mMenus = new ArrayList();
        mStates = new ArrayList();
        mSelectedCountryStateIndex = 0;
        mStateID = 0;
    }

    private void abortStateListDownload()
    {
        if(mStateService != null)
        {
            mStateService.abort();
            mStateService = null;
        }
    }

    private android.widget.TabHost.TabSpec createSegmentedButton(String s, String s1, int i)
    {
        TabHost.TabSpec temp = null;
        try
        {
            View view = LayoutInflater.from(mSegmentedCategoryButton.getContext()).inflate(R.layout.view_segmented_button, null);
            ((LinearLayout)view.findViewById(R.id.ButtonLayout)).setBackgroundResource(i);
            TextView textview = (TextView)view.findViewById(R.id.TitleLabel);
            textview.setTextColor(-1);
            textview.setText(s);
            temp = mSegmentedCategoryButton.newTabSpec(s1).setIndicator(view).setContent(new BusinessFinderBlankView(this));
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            SDLogger.printStackTrace(ex, "Create SegmentedButton");
            return null;
        }
        return temp;
    }

    private void downloadStateList()
    {
        abortStateListDownload();
        mStates.clear();
        mStateService = new StateListService(new StateListServiceInput(mCountryCode)) {

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                mStates = sdhttpserviceoutput.childs;
            }

        };
        mStateService.executeAsync();
        StateListService.updateInBackground(mCountryCode);
    }

    private void initData()
    {
        Intent intent = getIntent();
        mCountryCode = intent.getStringExtra("countryCode");
        mCountryName = intent.getStringExtra("countryName");
        mStateID = intent.getIntExtra("stateId", 0);
        mStateName = intent.getStringExtra("stateName");
        mMenus.add(new BusinessFinderServiceOutput(0, "Genre"));
        mMenus.add(new BusinessFinderServiceOutput(6, "F&B"));
        mMenus.add(new BusinessFinderServiceOutput(1, "Consumer"));
        mMenus.add(new BusinessFinderServiceOutput(4, "Medical"));
        mMenus.add(new BusinessFinderServiceOutput(5, "Automotive"));
        mMenus.add(new BusinessFinderServiceOutput(3, "Business"));
        mMenus.add(new BusinessFinderServiceOutput(2, "Industrial"));
        if(mSegmentedCategoryButton != null)
            setupCategoryButton();
        if(mBusinessItemPager != null)
        {
            mViewPagerAdapter = new BusinessFinderAdapter(getSupportFragmentManager(), mCountryCode, mCountryName, mMenus);
            mViewPagerAdapter.stateID = mStateID;
            mViewPagerAdapter.stateName = mStateName;
            mBusinessItemPager.setAdapter(mViewPagerAdapter);
        }
        if(mCountryStateButton != null)
        {
            if(mStateID == 0)
                mCountryStateButton.setText(mCountryCode.toUpperCase(Locale.ENGLISH));
            else
                mCountryStateButton.setText(mStateName);
            downloadStateList();
        }
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
        mCountryStateButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                showCountryStateDialog();
            }

        });
        mSegmentedCategoryButton.setOnTabChangedListener(new android.widget.TabHost.OnTabChangeListener() {

            public void onTabChanged(String s)
            {
                try
                {
                    int i = Integer.parseInt(s);
                    BusinessFinderServiceOutput temp = mViewPagerAdapter.getItemAtIndex(i);
                    SDStory.post(URLFactory.createGantBusinessMain(((BusinessFinderServiceOutput) (temp)).fullName, ((BusinessFinderServiceOutput) (temp)).menuID), SDStory.createDefaultParams());
                    GantTools.dispatch();
                    mBusinessItemPager.setCurrentItem(i);
                    return;
                }
                catch(Exception ex)
                {
                    SDLogger.printStackTrace(ex, "BusinessFinder onTabChanged");
                }
            }

        });

        mBusinessItemPager.setOnPageChangeListener(new android.support.v4.view.ViewPager.OnPageChangeListener() {

            public void onPageScrollStateChanged(int i)
            {
            }

            public void onPageScrolled(int i, float f, int j)
            {
            }

            public void onPageSelected(int i)
            {
                mSegmentedCategoryButton.setCurrentTab(i);
                View view = mSegmentedCategoryButton.getCurrentTabView();
                if(view != null)
                {
                    int l = view.getLeft();
                    i = view.getRight();
                    int j = mHorizontalScrollView.getScrollX();
                    int k = j + mHorizontalScrollView.getMeasuredWidth();
                    if(i > k)
                    {
                        l = mHorizontalScrollView.getPaddingLeft();
                        int i1 = mHorizontalScrollView.getPaddingRight();
                        mHorizontalScrollView.scrollTo((i - k) + j + l + i1, 0);
                    } else
                    if(l < j)
                    {
                        mHorizontalScrollView.scrollTo(l, 0);
                        return;
                    }
                }
            }

        });
    }

    private void initLayout()
    {
        mSideMenuLayout = (SDMapSideMenuLayout)findViewById(R.id.side_menu);
        mSideMenuBlocker = findViewById(R.id.side_menu_blocker);
        mMenuButton = (ImageButton)findViewById(R.id.MenuButton);
        mCountryStateButton = (Button)findViewById(R.id.CountryStateButton);
        mHorizontalScrollView = (HorizontalScrollView)findViewById(R.id.HorizontalScrollView);
        mSegmentedCategoryButton = (TabHost)findViewById(0x1020012);
        mBusinessItemPager = (ViewPager)findViewById(R.id.ViewPager);
    }

    private void initialize()
    {
        initLayout();
        initData();
        initEvent();
    }

    private void refreshList()
    {
        downloadStateList();
        mViewPagerAdapter.countryCode = mCountryCode;
        mViewPagerAdapter.countryName = mCountryName;
        mViewPagerAdapter.stateID = mStateID;
        mViewPagerAdapter.stateName = mStateName;
        mViewPagerAdapter.notifyDataSetChanged();
    }

    private void setupCategoryButton()
    {
        mSegmentedCategoryButton.setup();
        int j = mMenus.size();
        int i = 0;
        while(i < j)
        {
            BusinessFinderServiceOutput businessfinderserviceoutput = (BusinessFinderServiceOutput)mMenus.get(i);
            if(i == 0)
                mSegmentedCategoryButton.addTab(createSegmentedButton(businessfinderserviceoutput.name, (new StringBuilder()).append(i).append("").toString(), R.drawable.selector_button_green_left));
            else
            if(i == j - 1)
                mSegmentedCategoryButton.addTab(createSegmentedButton(businessfinderserviceoutput.name, (new StringBuilder()).append(i).append("").toString(), R.drawable.selector_button_green_right));
            else
                mSegmentedCategoryButton.addTab(createSegmentedButton(businessfinderserviceoutput.name, (new StringBuilder()).append(i).append("").toString(), R.drawable.selector_button_green_center));
            i++;
        }
        mSegmentedCategoryButton.setCurrentTab(0);
    }

    class SUBCLASS1 implements android.content.DialogInterface.OnClickListener {

        public void onClick(DialogInterface dialoginterface, int k)
        {
            SDDataOutput temp;
            if(mSelectedCountryStateIndex != lastSelectedIndex)
            {
                temp = (SDDataOutput)countryState.get(mSelectedCountryStateIndex);
                if(temp instanceof CountryListServiceOutput)
                {
                    temp = (CountryListServiceOutput)temp;
                    mCountryCode = ((CountryListServiceOutput) (temp)).countryCode;
                    mCountryName = ((CountryListServiceOutput) (temp)).countryName;
                    mStateID = 0;
                    mStateName = null;
                    mCountryStateButton.setText(mCountryCode.toUpperCase(Locale.ENGLISH));
                    refreshList();
                } else
                if(temp instanceof StateListServiceOutput)
                {
                    temp = (StateListServiceOutput)temp;
                    mStateID = ((StateListServiceOutput) (temp)).stateID;
                    mStateName = ((StateListServiceOutput) (temp)).stateName;
                    mViewPagerAdapter.stateID = mStateID;
                    mViewPagerAdapter.stateName = mStateName;
                    mCountryStateButton.setText(((StateListServiceOutput) (temp)).stateName);
                    return;
                }
            }
        }

        final ArrayList countryState;
        final int lastSelectedIndex;

        SUBCLASS1(int i, ArrayList arraylist)
        {
            lastSelectedIndex = i;
            countryState = arraylist;
        }
    }

    class SUBCLASS2 implements android.content.DialogInterface.OnClickListener {

        public void onClick(DialogInterface dialoginterface, int k)
        {
            mSelectedCountryStateIndex = lastSelectedIndex;
        }

        final int lastSelectedIndex;

        SUBCLASS2(int i)
        {
            lastSelectedIndex = i;
        }
    }

    private void showCountryStateDialog()
    {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Select Country");
        mSelectedCountryStateIndex = 0;
        final ArrayList countryState = new ArrayList();
        ArrayList arraylist = new ArrayList();
        Object obj = new ArrayList(mSideMenuLayout.countries);
        ArrayList arraylist1 = new ArrayList();
        ArrayList arraylist2 = new ArrayList(mStates);
        String s = mCountryStateButton.getText().toString();
        int j = arraylist2.size();
        for(obj = ((ArrayList) (obj)).iterator(); ((Iterator) (obj)).hasNext();)
        {
            CountryListServiceOutput countrylistserviceoutput = (CountryListServiceOutput)((Iterator) (obj)).next();
            if(countrylistserviceoutput.countryCode.equals(mCountryCode))
            {
                for(int i = 0; i < j; i++)
                {
                    StateListServiceOutput statelistserviceoutput = (StateListServiceOutput)arraylist2.get(i);
                    arraylist1.add((new StringBuilder()).append(" - ").append(statelistserviceoutput.stateName).toString());
                    if(statelistserviceoutput.stateName.equals(s))
                        mSelectedCountryStateIndex = i + 1;
                }

                arraylist.addAll(0, arraylist1);
                countryState.addAll(0, arraylist2);
                arraylist.add(0, countrylistserviceoutput.countryName);
                countryState.add(0, countrylistserviceoutput);
            } else
            {
                arraylist.add(countrylistserviceoutput.countryName);
                countryState.add(countrylistserviceoutput);
            }
        }

        final int lastSelectedIndex = mSelectedCountryStateIndex;
        builder.setSingleChoiceItems((CharSequence[])arraylist.toArray(new String[0]), mSelectedCountryStateIndex, new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialoginterface, int k)
            {
                mSelectedCountryStateIndex = k;
            }

        });
        builder.setPositiveButton("OK", new SUBCLASS1(lastSelectedIndex, arraylist));
        builder.setNegativeButton("Cancel", new SUBCLASS2(lastSelectedIndex));
        builder.show();
    }

    protected void abortAllProcess()
    {
        abortStateListDownload();
    }

    protected void onActivityResult(int i, int j, Intent intent)
    {
        switch(i) {
            default:
                return;
            case 1:
                if (j == -1) {
                    i = intent.getIntExtra("type", 2);
                    j = intent.getIntExtra("menuID", -1);
                    String s = intent.getStringExtra("menuName");
                    int k = intent.getIntExtra("parentID", -1);
                    int l = intent.getIntExtra("categoryID", -1);
                    String s1 = intent.getStringExtra("categoryName");
                    if (j != -1 && k != -1 && l != -1) {
                        intent = new Intent(this, BusinessListingActivity.class);
                        if (l == 11342)
                            intent = new Intent(this, OffersListingActivity.class);
                        intent.putExtra("countryCode", mCountryCode);
                        intent.putExtra("stateID", mStateID);
                        intent.putExtra("stateName", mStateName);
                        intent.putExtra("categoryID", l);
                        intent.putExtra("categoryName", s1);
                        intent.putExtra("type", i);
                        intent.putExtra("longitude", SDBlackboard.currentLongitude);
                        intent.putExtra("latitude", SDBlackboard.currentLatitude);
                        intent.putExtra("menuID", j);
                        intent.putExtra("menuName", s);
                        intent.putExtra("parentID", k);
                        intent.putExtra("countryName", mCountryName);
                        startActivity(intent);
                        return;
                    }
                }
        }
    }

    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.activity_business_finder);
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

    protected void onDestroy()
    {
        abortAllProcess();
        super.onDestroy();
    }

    public void startActivity(Intent intent)
    {
        super.startActivity(intent);
        overridePendingTransition(R.anim.side_menu_a, R.anim.side_menu_b);
    }

    private ViewPager mBusinessItemPager;
    private String mCountryCode;
    private String mCountryName;
    private Button mCountryStateButton;
    private HorizontalScrollView mHorizontalScrollView;
    private ImageButton mMenuButton;
    private ArrayList mMenus;
    private TabHost mSegmentedCategoryButton;
    private int mSelectedCountryStateIndex;
    private View mSideMenuBlocker;
    private SDMapSideMenuLayout mSideMenuLayout;
    private int mStateID;
    private String mStateName;
    private StateListService mStateService;
    private ArrayList mStates;
    private BusinessFinderAdapter mViewPagerAdapter;




/*
    static String access$1002(BusinessFinderActivity businessfinderactivity, String s)
    {
        businessfinderactivity.mCountryName = s;
        return s;
    }

*/



/*
    static int access$1102(BusinessFinderActivity businessfinderactivity, int i)
    {
        businessfinderactivity.mStateID = i;
        return i;
    }

*/



/*
    static String access$1202(BusinessFinderActivity businessfinderactivity, String s)
    {
        businessfinderactivity.mStateName = s;
        return s;
    }

*/









/*
    static ArrayList access$702(BusinessFinderActivity businessfinderactivity, ArrayList arraylist)
    {
        businessfinderactivity.mStates = arraylist;
        return arraylist;
    }

*/



/*
    static int access$802(BusinessFinderActivity businessfinderactivity, int i)
    {
        businessfinderactivity.mSelectedCountryStateIndex = i;
        return i;
    }

*/



/*
    static String access$902(BusinessFinderActivity businessfinderactivity, String s)
    {
        businessfinderactivity.mCountryCode = s;
        return s;
    }

*/
}
