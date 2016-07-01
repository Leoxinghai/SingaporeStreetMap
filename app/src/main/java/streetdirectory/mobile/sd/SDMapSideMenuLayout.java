// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.sd;

import android.content.*;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.util.*;
import streetdirectory.mobile.core.*;
import streetdirectory.mobile.core.ui.sidemenu.*;
import streetdirectory.mobile.gis.maps.MapPinLayer;
import streetdirectory.mobile.gis.maps.MapView;
import streetdirectory.mobile.modules.businessdetail.BusinessDetailActivityV2;
import streetdirectory.mobile.modules.businesslisting.BusinessListingActivity;
import streetdirectory.mobile.modules.businesslisting.offers.OffersListingActivity;
import streetdirectory.mobile.modules.core.LocationBusinessServiceOutput;
import streetdirectory.mobile.modules.locationdetail.bus.BusArrivalActivity;
import streetdirectory.mobile.modules.locationdetail.businessin.BusinessInActivity;
import streetdirectory.mobile.modules.locationdetail.erp.ErpDetailActivity;
import streetdirectory.mobile.modules.locationdetail.expresswayexit.ExpressWayExitActivity;
import streetdirectory.mobile.modules.locationdetail.trafficcam.TrafficCameraLocationDetailActivity;
import streetdirectory.mobile.modules.map.MapActivity;
import streetdirectory.mobile.modules.search.SearchListAdapter;
import streetdirectory.mobile.modules.search.SearchMenuItem;
import streetdirectory.mobile.modules.search.service.*;
import streetdirectory.mobile.service.SDHttpServiceOutput;
import streetdirectory.mobile.service.URLFactory;
import streetdirectory.mobile.service.countrylist.CountryListService;
import streetdirectory.mobile.service.countrylist.CountryListServiceOutput;

// Referenced classes of package streetdirectory.mobile.sd:
//            SDBlackboard, SDMapMenuItemProvider, SDSearchHistoryMenuItem

public class SDMapSideMenuLayout extends SideMenuLayout
{

    public SDMapSideMenuLayout(Context context)
    {
        super(context);
        countries = new ArrayList();
    }

    public SDMapSideMenuLayout(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
        countries = new ArrayList();
    }

    private void abortAllProcess()
    {
        abortSearchDownload();
        abortCountryListDownload();
    }

    private void initBackViewData()
    {
        countries.add(new CountryListServiceOutput("sg", "Singapore", true));
        countryListCodes = (new String[] {
            "sg".toUpperCase(Locale.ENGLISH)
        });
        mMenuCountryButton.setText(countryListCodes[0]);
        countryListNames = (new String[] {
            "Singapore"
        });
    }

    private void initBackViewEvent(Context context)
    {
        mMenuListView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView adapterview, View view, int i, long l)
            {
                SDSideMenuItem sdSideMenuItem = (SDSideMenuItem)mMenuListView.getItemAtPosition(i);
                if(sdSideMenuItem instanceof SearchMenuItem)
                {
                    mSearchField.requestFocus();
                    return;
                }
                if(sdSideMenuItem instanceof SDSearchHistoryMenuItem)
                {
                    searchItemClicked(getContext(), ((SDSearchHistoryMenuItem)sdSideMenuItem).data);
                    return;
                } else
                {
                    sdSideMenuItem.execute(getContext(), SDMapSideMenuLayout.this);
                    return;
                }
            }

        });
        mSearchListView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView adapterview, View view, int i, long l)
            {
                Context context1 = getContext();
                ((InputMethodManager)context1.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mSearchField.getWindowToken(), 0);
                SearchServiceOutput searchServiceOutput = mSearchAdapter.getItem(i);
                SDMapMenuItemProvider.addSearchHistory(searchServiceOutput);
                mMenuAdapter.notifyDataSetChanged();
                SDStory.post(URLFactory.createGantOthersSearch(), SDStory.createDefaultParams());
                hideSearchList();
                searchItemClicked(context1, searchServiceOutput);
            }

        });
        mMenuCountryButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                countryButtonClicked();
            }

        });
        mButtonClearText.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                mSearchField.setText("");
            }

        });
        mSearchField.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {

            public void onFocusChange(View view, boolean flag)
            {
                if(flag)
                {
                    slideOpen(100);
                    if(mSearchField.getText().toString().length() <= 0)
                        mButtonClearText.setVisibility(View.INVISIBLE);
                    else
                        mButtonClearText.setVisibility(View.VISIBLE);
                }
                return;
            }
        });
        mSearchField.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable editable)
            {
                if(mSearchField.getText().toString().length() > 0)
                    mButtonClearText.setVisibility(View.VISIBLE);
                else
                    mButtonClearText.setVisibility(View.INVISIBLE);
                downloadSearchResult(mSearchField.getText().toString());
            }

            public void beforeTextChanged(CharSequence charsequence, int i, int j, int k)
            {
            }

            public void onTextChanged(CharSequence charsequence, int i, int j, int k)
            {
            }

        });
        mSearchCancelButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                hideSearchList();
                mSearchLoadingIndicator.setVisibility(View.INVISIBLE);
            }

        }
);
    }

    private void initBackViewLayout(Context context)
    {
        mgr = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mMenuCountryButton = (Button)mMenuLayout.findViewById(R.id.MenuCountryButton);
        mButtonClearText = (Button)mMenuLayout.findViewById(R.id.MenuCountryButton);
        mSearchField = (EditText)mMenuLayout.findViewById(R.id.MenuSearchField);
        mSearchCancelButton = (Button)mMenuLayout.findViewById(R.id.MenuCancelButton);
        mMenuListView = (ListView)mMenuLayout.findViewById(R.id.MenuListView);
        mMenuAdapter = new SDSideMenuAdapter(context, 0, SDSideMenuProvider.items, this) {

            public void onRemoveButtonClicked(SearchServiceOutput searchserviceoutput)
            {
                onSearchHistoryRemoveButtonClicked(searchserviceoutput);
            }

            public int getViewTypeCount() {
                return 1;
            }

        };
        mMenuListView.setAdapter(mMenuAdapter);
        mSearchListView = (ListView)mMenuLayout.findViewById(R.id.SearchListView);
        mSearchAdapter = new SearchListAdapter(context);
        mSearchListView.setAdapter(mSearchAdapter);
        mSearchLoadingIndicator = (ProgressBar)mMenuLayout.findViewById(R.id.SearchLoadingIndicator);
    }

    private void onSearchHistoryRemoveButtonClicked(final SearchServiceOutput item)
    {
        android.content.DialogInterface.OnClickListener onclicklistener = new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialoginterface, int i)
            {
                switch(i)
                {
                default:
                    return;

                case -1:
                    SDMapMenuItemProvider.removeSearchHistory(item);
                    break;
                }
                mMenuAdapter.notifyDataSetChanged();
            }

        }
;
        (new android.app.AlertDialog.Builder(getContext())).setTitle("Delete Search History").setMessage((new StringBuilder()).append("Are you sure to delete ").append(item.venue).toString()).setPositiveButton("Delete", onclicklistener).setNegativeButton("Cancel", onclicklistener).show();
    }

    private void searchItemClicked(Context context, LocationBusinessServiceOutput locationbusinessserviceoutput)
    {
        Object obj = SDPreferences.getInstance().createEditor();
        Intent intent;
        ((android.content.SharedPreferences.Editor) (obj)).putString("last_map_longitude", Double.toString(locationbusinessserviceoutput.longitude));
        ((android.content.SharedPreferences.Editor) (obj)).putString("last_map_latitude", Double.toString(locationbusinessserviceoutput.latitude));
        ((android.content.SharedPreferences.Editor) (obj)).putInt("last_map_item_type", locationbusinessserviceoutput.type);
        ((android.content.SharedPreferences.Editor) (obj)).putString("last_map_item_title", locationbusinessserviceoutput.venue);
        ((android.content.SharedPreferences.Editor) (obj)).commit();
        if(locationbusinessserviceoutput.type == 2 || locationbusinessserviceoutput.type == 1)
        {
            if(context instanceof MapActivity)
            {
                intent = new Intent(context, MapActivity.class);
                ((Intent) (intent)).putExtra("data", (Parcelable)locationbusinessserviceoutput);
                MapActivity mapActivity = (MapActivity)context;
                mapActivity.originalIntent = intent;
                ((MapActivity) (mapActivity)).pinLayer.longitude = locationbusinessserviceoutput.longitude;
                ((MapActivity) (mapActivity)).pinLayer.latitude = locationbusinessserviceoutput.latitude;
                ((MapActivity) (mapActivity)).pinLayer.setTitle(locationbusinessserviceoutput.venue);
                if(((MapActivity) (mapActivity)).pinLayer.mapView.getCurrentLevelOrdinal() < 12)
                    ((MapActivity) (mapActivity)).pinLayer.mapView.goToAnimate(locationbusinessserviceoutput.longitude, locationbusinessserviceoutput.latitude, 12);
                else
                    ((MapActivity) (mapActivity)).pinLayer.mapView.goToAnimate(locationbusinessserviceoutput.longitude, locationbusinessserviceoutput.latitude, ((MapActivity) (context)).pinLayer.mapView.mapScale);
                ((MapActivity) (mapActivity)).pinLayer.setVisibility(View.VISIBLE);
                ((MapActivity) (mapActivity)).pinLayer.mapView.redraw();
                ((MapActivity) (mapActivity)).pinLayer.invalidate();
                ((MapActivity) (mapActivity)).pinLayer.mapView.stopGps(true);
                if(locationbusinessserviceoutput.type == 2)
                    mapActivity.pinClass = BusinessDetailActivityV2.class;
                else
                if(locationbusinessserviceoutput.categoryID == 1118)
                    mapActivity.pinClass = TrafficCameraLocationDetailActivity.class;
                else
                if(locationbusinessserviceoutput.categoryID == 93)
                    mapActivity.pinClass = BusArrivalActivity.class;
                else
                if(locationbusinessserviceoutput.categoryID == 29)
                    mapActivity.pinClass = ExpressWayExitActivity.class;
                else
                if(locationbusinessserviceoutput.categoryID == 28)
                    mapActivity.pinClass = ErpDetailActivity.class;
                else
                    mapActivity.pinClass = BusinessInActivity.class;
                if(getIsMenuOpen())
                    slideClose();
                return;
            }
            intent = new Intent(context, MapActivity.class);
            ((Intent) (intent)).putExtra("data", (Parcelable)locationbusinessserviceoutput);
            ((Intent) (intent)).putExtra("pinLongitude", locationbusinessserviceoutput.longitude);
            ((Intent) (intent)).putExtra("pinLatitude", locationbusinessserviceoutput.latitude);
            ((Intent) (intent)).putExtra("pinTitle", locationbusinessserviceoutput.venue);
            if(locationbusinessserviceoutput.type == 2)
                ((Intent) (intent)).putExtra("pinActivityClass", BusinessDetailActivityV2.class);
            else
            if(locationbusinessserviceoutput.categoryID == 1118)
                ((Intent) (intent)).putExtra("pinActivityClass", TrafficCameraLocationDetailActivity.class);
            else
            if(locationbusinessserviceoutput.categoryID == 93)
                ((Intent) (intent)).putExtra("pinActivityClass", BusArrivalActivity.class);
            else
            if(locationbusinessserviceoutput.categoryID == 29)
                ((Intent) (intent)).putExtra("pinActivityClass", ExpressWayExitActivity.class);
            else
            if(locationbusinessserviceoutput.categoryID == 28)
                ((Intent) (intent)).putExtra("pinActivityClass", ErpDetailActivity.class);
            else
                ((Intent) (intent)).putExtra("pinActivityClass", BusinessInActivity.class);
            context.startActivity(((Intent) (intent)));
            return;
        }
        intent = new Intent(context, BusinessListingActivity.class);
        if(locationbusinessserviceoutput.categoryID == 11342)
            obj = new Intent(context, OffersListingActivity.class);
        ((Intent) (intent)).putExtra("countryCode", locationbusinessserviceoutput.countryCode);
        ((Intent) (intent)).putExtra("categoryID", locationbusinessserviceoutput.categoryID);
        ((Intent) (intent)).putExtra("categoryName", locationbusinessserviceoutput.venue);
        ((Intent) (intent)).putExtra("type", 2);
        ((Intent) (intent)).putExtra("longitude", SDBlackboard.currentLongitude);
        ((Intent) (intent)).putExtra("latitude", SDBlackboard.currentLatitude);
        context.startActivity(((Intent) (obj)));
    }

    public void abortCountryListDownload()
    {
        if(mCountryListService != null)
        {
            mCountryListService.abort();
            mCountryListService = null;
        }
    }

    public void abortSearchDownload()
    {
        if(mSearchService != null)
        {
            mSearchService.abort();
            mSearchService = null;
        }
    }

    protected void countryButtonClicked()
    {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("Select Country");
        final int lastSelectedCountryCodeIndex = mSelectedCountryCodeIndex;
        builder.setSingleChoiceItems(countryListNames, mSelectedCountryCodeIndex, new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialoginterface, int i)
            {
                mSelectedCountryCodeIndex = i;
            }

        });
        builder.setPositiveButton("OK", new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialoginterface, int i)
            {
                if(mSelectedCountryCodeIndex != lastSelectedCountryCodeIndex)
                {
                    String temp = countryListCodes[mSelectedCountryCodeIndex];
                    mMenuCountryButton.setText(temp);
                    SDBlackboard.currentCountryCode = temp;
                    downloadSearchResult(mSearchField.getText().toString());
                    SDMapMenuItemProvider.reload(temp);
                    mMenuAdapter.notifyDataSetChanged();
                }
            }

        });
        builder.setNegativeButton("Cancel", new android.content.DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialoginterface, int i)
            {
                mSelectedCountryCodeIndex = lastSelectedCountryCodeIndex;
            }

        });
        builder.show();
    }

    public void downloadCountryList()
    {
        mCountryListService = new CountryListService() {

            public void onAborted(Exception exception)
            {
                mCountryListService = null;
            }

            public void onFailed(Exception exception)
            {
                mCountryListService = null;
                SDLogger.printStackTrace(exception);
            }

            public void onSuccess(Object obj)
            {
                onSuccess((SDHttpServiceOutput)obj);
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                mCountryListService = null;
                int j = sdhttpserviceoutput.childs.size();
                if(j > 0)
                {
                    countries = sdhttpserviceoutput.childs;
                    SDBlackboard.countryList = sdhttpserviceoutput.childs;
                    countryListCodes = new String[j];
                    countryListNames = new String[j];
                    String temp = SDBlackboard.currentCountryCode;
                    for(int i = 0; i < j; i++)
                    {
                        CountryListServiceOutput countrylistserviceoutput = (CountryListServiceOutput)countries.get(i);
                        countryListCodes[i] = countrylistserviceoutput.countryCode.toUpperCase(Locale.ENGLISH);
                        countryListNames[i] = countrylistserviceoutput.countryName;
                        if(temp.equalsIgnoreCase(countrylistserviceoutput.countryCode))
                        {
                            mSelectedCountryCodeIndex = i;
                            mMenuCountryButton.setText(countrylistserviceoutput.countryCode.toUpperCase(Locale.ENGLISH));
                            SDBlackboard.currentCountryCode = countrylistserviceoutput.countryCode;
                        }
                    }

                }
            }

        };
        mCountryListService.executeAsync();
    }

    public void downloadSearchResult(String searchKeyword)
    {
        abortSearchDownload();
        searchKeyword = searchKeyword.trim();
        final String lsearchKeyword = searchKeyword;
        if(!searchKeyword.equals(""))
        {
            mSearchService = new SearchService(null) {

                public void onAborted(Exception exception)
                {
                    mSearchService = null;
                    SDLogger.info("Search Aborted");
                    mSearchLoadingIndicator.setVisibility(View.INVISIBLE);
                }

                public void onFailed(Exception exception)
                {
                    mSearchService = null;
                    SDLogger.printStackTrace(exception, "Search Failed");
                    mSearchLoadingIndicator.setVisibility(View.INVISIBLE);
                }

                public void onSuccess(Object obj)
                {
                    onSuccess((SDHttpServiceOutput)obj);
                }

                public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
                {
                    mSearchService = null;
                    getContext();
                    SDLogger.info("Search Success");
                    mSearchLoadingIndicator.setVisibility(View.INVISIBLE);
                    String s1 = mSearchField.getText().toString().trim();
                    if(s1.equals(""))
                        mSearchAdapter.clear();
                    else
                    if(lsearchKeyword.equals(s1))
                    {
                        mSearchAdapter.setData(sdhttpserviceoutput.childs);
                        return;
                    }
                }

            };
            if(mSearchListView.getVisibility() == 0)
                mSearchLoadingIndicator.setVisibility(View.VISIBLE);
            mSearchService.executeAsync();
            return;
        } else
        {
            mSearchAdapter.clear();
            return;
        }
    }

    protected void hideSearchList()
    {
        mMenuListView.setVisibility(View.VISIBLE);
        mSearchListView.setVisibility(View.INVISIBLE);
        mgr.hideSoftInputFromWindow(mSearchField.getWindowToken(), 0);
        mMenuLayout.requestFocus();
        slideOpen(77);
    }

    protected void initialize(Context context)
    {
        super.initialize(context);
        SDMapMenuItemProvider.reload(SDBlackboard.currentCountryCode);
    }

    protected void onDetachedFromWindow()
    {
        abortAllProcess();
        super.onDetachedFromWindow();
    }

    protected void onSlideClosed()
    {
        if(mSearchField.hasFocus())
            hideSearchList();
        super.onSlideClosed();
    }

    protected void onSlideOpened()
    {
        if(mSearchField.hasFocus())
            showSearchList();
        super.onSlideOpened();
    }

    protected void populateBackView(Context context)
    {
        mMenuLayout = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_menu, backview, true);
        initBackViewLayout(context);
        initBackViewData();
        initBackViewEvent(context);
        downloadCountryList();
    }

    protected void showSearchList()
    {
        if(mMenuLayout.getVisibility() == 0)
        {
            mMenuListView.setVisibility(View.INVISIBLE);
            mSearchListView.setVisibility(View.VISIBLE);
            ((InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(mSearchField, 1);
        }
    }

    public ArrayList countries;
    public String countryListCodes[];
    public String countryListNames[];
    private Button mButtonClearText;
    private CountryListService mCountryListService;
    private SDSideMenuAdapter mMenuAdapter;
    private Button mMenuCountryButton;
    private View mMenuLayout;
    private ListView mMenuListView;
    private SearchListAdapter mSearchAdapter;
    private Button mSearchCancelButton;
    private EditText mSearchField;
    private ListView mSearchListView;
    private ProgressBar mSearchLoadingIndicator;
    private SearchService mSearchService;
    private int mSelectedCountryCodeIndex;
    InputMethodManager mgr;




/*
    static SearchService access$1002(SDMapSideMenuLayout sdmapsidemenulayout, SearchService searchservice)
    {
        sdmapsidemenulayout.mSearchService = searchservice;
        return searchservice;
    }

*/


/*
    static CountryListService access$1102(SDMapSideMenuLayout sdmapsidemenulayout, CountryListService countrylistservice)
    {
        sdmapsidemenulayout.mCountryListService = countrylistservice;
        return countrylistservice;
    }

*/









/*
    static int access$802(SDMapSideMenuLayout sdmapsidemenulayout, int i)
    {
        sdmapsidemenulayout.mSelectedCountryCodeIndex = i;
        return i;
    }

*/

}
