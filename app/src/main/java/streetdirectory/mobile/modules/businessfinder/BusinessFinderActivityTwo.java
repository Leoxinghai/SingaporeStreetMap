// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.businessfinder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.util.*;
import streetdirectory.mobile.core.*;
import streetdirectory.mobile.core.ui.SanListViewAdapter;
import streetdirectory.mobile.core.ui.SanListViewItem;
import streetdirectory.mobile.modules.SDActivity;
import streetdirectory.mobile.modules.businessfinder.service.BusinessFinderYellowBarService;
import streetdirectory.mobile.modules.businessfinder.service.BusinessFinderYellowBarServiceInput;
import streetdirectory.mobile.modules.businessfinder.service.BusinessFinderYellowBarServiceOutput;
import streetdirectory.mobile.modules.businesslisting.BusinessListingActivity;
import streetdirectory.mobile.modules.businesslisting.offers.OffersListingActivity;
import streetdirectory.mobile.modules.map.MapActivity;
import streetdirectory.mobile.sd.SDBlackboard;
import streetdirectory.mobile.sd.SDMapSideMenuLayout;
import streetdirectory.mobile.service.*;
import streetdirectory.mobile.service.countrylist.CountryListServiceOutput;
import streetdirectory.mobile.service.statelist.*;

// Referenced classes of package streetdirectory.mobile.modules.businessfinder:
//            BusinessFinderSubYellowBarCell, BusinessFinderYellowBarActivity

public class BusinessFinderActivityTwo extends SDActivity
{

    public BusinessFinderActivityTwo()
    {
        mStates = new ArrayList();
        mSelectedCountryStateIndex = 0;
        mStateID = 0;
        mMenuID = 0;
        _data = new ArrayList();
    }

    private void abortStateListDownload()
    {
        if(mStateService != null)
        {
            mStateService.abort();
            mStateService = null;
        }
    }

    private void downloadBusinessFinderMenuItem(String s)
    {
        abortAllProcess();
        _categoryService = new BusinessFinderYellowBarService(new BusinessFinderYellowBarServiceInput(mCountryCode, 0)) {

            public void onAborted(Exception exception)
            {
                SDLogger.info("Business Finder Aborted");
                _categoryService = null;
            }

            public void onFailed(Exception exception)
            {
                SDLogger.printStackTrace(exception, "Business Finder Failed");
                _categoryService = null;
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                super.onSuccess(sdhttpserviceoutput);
                Collections.sort(sdhttpserviceoutput.childs, new Comparator() {

                    public int compare(Object obj, Object obj1)
                    {
                        return compare((BusinessFinderYellowBarServiceOutput)obj, (BusinessFinderYellowBarServiceOutput)obj1);
                    }

                    public int compare(BusinessFinderYellowBarServiceOutput businessfinderyellowbarserviceoutput, BusinessFinderYellowBarServiceOutput businessfinderyellowbarserviceoutput1)
                    {
                        return businessfinderyellowbarserviceoutput.name.compareToIgnoreCase(businessfinderyellowbarserviceoutput1.name);
                    }
                });

                BusinessFinderYellowBarServiceOutput temp = (BusinessFinderYellowBarServiceOutput)sdhttpserviceoutput.childs.get(0);
                mMenuID = ((BusinessFinderYellowBarServiceOutput) (temp)).id;
                mCategoryName = ((BusinessFinderYellowBarServiceOutput) (temp)).name;
                saveCategoryPreference();
                mTitleCategory.setText(mCategoryName);
                setDrawable();
                downloadSubDirectory(mMenuID);
                _categoryService = null;
            }

        };

        mLoadingIndicator.setVisibility(View.VISIBLE);
        _categoryService.executeAsync();
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

    private void downloadSubDirectory(int i)
    {
        mListViewSubDirectory.setVisibility(View.INVISIBLE);
        abortAllProcess();
        _adapter.clear();
        _service = new BusinessFinderYellowBarService(new BusinessFinderYellowBarServiceInput(mCountryCode, i)) {

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                super.onSuccess(sdhttpserviceoutput);
                SDLogger.info("Sub Directory Success");
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                mListViewSubDirectory.setVisibility(View.VISIBLE);
                _service = null;
                Collections.sort(sdhttpserviceoutput.childs, new Comparator() {

                    public int compare(Object obj, Object obj1)
                    {
                        return compare((BusinessFinderYellowBarServiceOutput)obj, (BusinessFinderYellowBarServiceOutput)obj1);
                    }

                    public int compare(BusinessFinderYellowBarServiceOutput businessfinderyellowbarserviceoutput, BusinessFinderYellowBarServiceOutput businessfinderyellowbarserviceoutput1)
                    {
                        return businessfinderyellowbarserviceoutput.name.compareToIgnoreCase(businessfinderyellowbarserviceoutput1.name);
                    }

                });

                BusinessFinderSubYellowBarCell businessfindersubyellowbarcell;
                Iterator iterator;
                for(iterator = sdhttpserviceoutput.childs.iterator(); iterator.hasNext(); _data.add(businessfindersubyellowbarcell))
                    businessfindersubyellowbarcell = new BusinessFinderSubYellowBarCell((BusinessFinderYellowBarServiceOutput)iterator.next());

                _adapter.notifyDataSetChanged();
            }
        };
        mLoadingIndicator.setVisibility(View.VISIBLE);
        _service.executeAsync();
    }

    private void initData()
    {
        Intent intent = getIntent();
        mCountryCode = intent.getStringExtra("countryCode");
        mStateID = intent.getIntExtra("stateId", 0);
        mStateName = intent.getStringExtra("stateName");
        _adapter = new SanListViewAdapter(this, 0, _data);
        mListViewSubDirectory.setAdapter(_adapter);
        if(mCountryStateButton != null)
        {
            if(mStateID == 0)
                mCountryStateButton.setText(mCountryCode.toUpperCase(Locale.ENGLISH));
            else
                mCountryStateButton.setText(mStateName);
            downloadStateList();
        }
        loadCategoryPreference();
        setDrawable();
        mTitleCategory.setText(mCategoryName);
        downloadSubDirectory(mMenuID);
        SDStory.post(URLFactory.createGantBusinessMain(mCategoryName, mMenuID), SDStory.createDefaultParams());
    }

    private void initEvent()
    {
        mButtonCategory.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                Intent intent = new Intent(BusinessFinderActivityTwo.this, BusinessFinderYellowBarActivity.class);
                intent.putExtra("countryCode", mCountryCode);
                intent.putExtra("menuID", mMenuID);
                startActivityForResult(intent, 0);
                overridePendingTransition(0x10a0000, 0x10a0001);
                abortAllProcess();
            }
        });
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

        mListViewSubDirectory.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView adapterview, View view, int i, long l)
            {
                BusinessFinderYellowBarServiceOutput temp = ((BusinessFinderSubYellowBarCell)_adapter.getItem(i)).data;
                Intent intent = new Intent(BusinessFinderActivityTwo.this, BusinessListingActivity.class);
                if(((BusinessFinderYellowBarServiceOutput) (temp)).categoryID == 11342)
                    intent = new Intent(BusinessFinderActivityTwo.this, OffersListingActivity.class);
                intent.putExtra("countryCode", mCountryCode);
                intent.putExtra("categoryID", ((BusinessFinderYellowBarServiceOutput) (temp)).categoryID);
                intent.putExtra("categoryName", ((BusinessFinderYellowBarServiceOutput) (temp)).name);
                intent.putExtra("longitude", SDBlackboard.currentLongitude);
                intent.putExtra("latitude", SDBlackboard.currentLatitude);
                startActivity(intent);
            }

        });
    }

    private void initLayout()
    {
        mSideMenuLayout = (SDMapSideMenuLayout)findViewById(R.id.side_menu);
        mSideMenuBlocker = findViewById(R.id.side_menu_blocker);
        mMenuButton = (ImageButton)findViewById(R.id.MenuButton);
        mCountryStateButton = (Button)findViewById(R.id.CountryStateButton);
        mListViewSubDirectory = (ListView)findViewById(R.id.listViewSubDirectory);
        mLoadingIndicator = (ProgressBar)findViewById(R.id.LoadingIndicator);
        mButtonCategory = (Button)findViewById(R.id.CategoryButton);
        mImageCategory = (ImageView)findViewById(R.id.imageCategory);
        mTitleCategory = (TextView)findViewById(R.id.categoryTitle);
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

    private void loadCategoryPreference()
    {
        SDPreferences sdpreferences = SDPreferences.getInstance();
        mMenuID = sdpreferences.getIntForKey("menuID", 1);
        mCategoryName = sdpreferences.getStringForKey("categoryName", "Food");
        mCategoryIcon = sdpreferences.getStringForKey("categoryIcon", "bg_0_1");
    }

    private void saveCategoryPreference()
    {
        android.content.SharedPreferences.Editor editor = SDPreferences.getInstance().createEditor();
        editor.putInt("menuID", mMenuID);
        editor.putString("categoryName", mCategoryName);
        editor.putString("categoryIcon", mCategoryIcon);
        editor.commit();
    }

    private void setDrawable()
    {
        int i = getResources().getIdentifier((new StringBuilder()).append("biz_").append(mMenuID).toString(), "drawable", getPackageName());
        if(i != 0)
        {
            android.graphics.drawable.Drawable drawable = getResources().getDrawable(i);
            if(drawable != null)
                mImageCategory.setImageDrawable(drawable);
        }
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
                    mStateID = 0;
                    mStateName = null;
                    mCountryStateButton.setText(mCountryCode.toUpperCase(Locale.ENGLISH));
                    downloadBusinessFinderMenuItem(mCountryCode);
                } else
                if(temp instanceof StateListServiceOutput)
                {
                    temp = (StateListServiceOutput)temp;
                    mStateID = ((StateListServiceOutput) (temp)).stateID;
                    mStateName = ((StateListServiceOutput) (temp)).stateName;
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

        builder.setPositiveButton("OK", new SUBCLASS1(lastSelectedIndex, countryState));
        builder.setNegativeButton("Cancel", new SUBCLASS2(lastSelectedIndex));
        builder.show();
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

    public void abortAllProcess()
    {
        if(_service != null)
        {
            _service.abort();
            _service = null;
        }
        if(_categoryService != null)
        {
            _categoryService.abort();
            _categoryService = null;
        }
    }

    protected void onActivityResult(int i, int j, Intent intent)
    {
        super.onActivityResult(i, j, intent);
        if(j == -1 && i == 0)
        {
            mMenuID = intent.getIntExtra("menuID", 1);
            mCategoryName = intent.getStringExtra("categoryName");
            mCategoryIcon = intent.getStringExtra("categoryIcon");
            setDrawable();
            saveCategoryPreference();
            mTitleCategory.setText(mCategoryName);
            SDStory.post(URLFactory.createGantBusinessMain(mCategoryName, mMenuID), SDStory.createDefaultParams());
            downloadSubDirectory(mMenuID);
        }
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
        setContentView(R.layout.activity_business_finder_two);
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

    public void startActivity(Intent intent)
    {
        super.startActivity(intent);
        overridePendingTransition(R.anim.side_menu_a, R.anim.side_menu_b);
    }

    public static final int REQUEST_CATEGORY = 0;
    private SanListViewAdapter _adapter;
    private BusinessFinderYellowBarService _categoryService;
    private ArrayList _data;
    private BusinessFinderYellowBarService _service;
    private Button mButtonCategory;
    private String mCategoryIcon;
    private String mCategoryName;
    private String mCountryCode;
    private Button mCountryStateButton;
    private ImageView mImageCategory;
    private ListView mListViewSubDirectory;
    protected ProgressBar mLoadingIndicator;
    private ImageButton mMenuButton;
    private int mMenuID;
    private ListView mMenuListView;
    private EditText mSearchField;
    private ListView mSearchListView;
    private int mSelectedCountryStateIndex;
    private View mSideMenuBlocker;
    private SDMapSideMenuLayout mSideMenuLayout;
    private int mStateID;
    private String mStateName;
    private StateListService mStateService;
    private ArrayList mStates;
    private TextView mTitleCategory;
    private InputMethodManager mgr;

    static
    {
        SanListViewItem.addTypeCount(BusinessFinderSubYellowBarCell.class);
    }



/*
    static String access$002(BusinessFinderActivityTwo businessfinderactivitytwo, String s)
    {
        businessfinderactivitytwo.mCountryCode = s;
        return s;
    }

*/




/*
    static int access$102(BusinessFinderActivityTwo businessfinderactivitytwo, int i)
    {
        businessfinderactivitytwo.mMenuID = i;
        return i;
    }

*/



/*
    static BusinessFinderYellowBarService access$1202(BusinessFinderActivityTwo businessfinderactivitytwo, BusinessFinderYellowBarService businessfinderyellowbarservice)
    {
        businessfinderactivitytwo._categoryService = businessfinderyellowbarservice;
        return businessfinderyellowbarservice;
    }

*/



/*
    static BusinessFinderYellowBarService access$1402(BusinessFinderActivityTwo businessfinderactivitytwo, BusinessFinderYellowBarService businessfinderyellowbarservice)
    {
        businessfinderactivitytwo._service = businessfinderyellowbarservice;
        return businessfinderyellowbarservice;
    }

*/




/*
    static int access$1602(BusinessFinderActivityTwo businessfinderactivitytwo, int i)
    {
        businessfinderactivitytwo.mSelectedCountryStateIndex = i;
        return i;
    }

*/


/*
    static int access$1702(BusinessFinderActivityTwo businessfinderactivitytwo, int i)
    {
        businessfinderactivitytwo.mStateID = i;
        return i;
    }

*/


/*
    static String access$1802(BusinessFinderActivityTwo businessfinderactivitytwo, String s)
    {
        businessfinderactivitytwo.mStateName = s;
        return s;
    }

*/








/*
    static ArrayList access$602(BusinessFinderActivityTwo businessfinderactivitytwo, ArrayList arraylist)
    {
        businessfinderactivitytwo.mStates = arraylist;
        return arraylist;
    }

*/



/*
    static String access$702(BusinessFinderActivityTwo businessfinderactivitytwo, String s)
    {
        businessfinderactivitytwo.mCategoryName = s;
        return s;
    }

*/


}
