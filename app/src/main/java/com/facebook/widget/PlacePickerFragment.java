// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.location.Location;
import android.os.*;
import android.support.v4.app.FragmentActivity;
import android.text.*;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import com.facebook.*;
import com.facebook.internal.Logger;
import com.facebook.internal.Utility;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphPlace;
import java.util.*;

// Referenced classes of package com.facebook.widget:
//            PickerFragment, GraphObjectAdapter, GraphObjectPagingLoader, SimpleGraphObjectCursor

public class PlacePickerFragment extends PickerFragment
{
    private class AsNeededLoadingStrategy extends PickerFragment.LoadingStrategy
    {

        public void attach(GraphObjectAdapter graphobjectadapter)
        {
            super.attach(graphobjectadapter);
            adapter.setDataNeededListener(new GraphObjectAdapter.DataNeededListener() {

                public void onDataNeeded()
                {
                    if(!loader.isLoading())
                        loader.followNextLink();
                }

            });
        }

        protected void onLoadFinished(GraphObjectPagingLoader graphobjectpagingloader, SimpleGraphObjectCursor simplegraphobjectcursor)
        {
            super.onLoadFinished(graphobjectpagingloader, simplegraphobjectcursor);
            if(simplegraphobjectcursor != null && !graphobjectpagingloader.isLoading())
            {
                hideActivityCircle();
                if(simplegraphobjectcursor.isFromCache())
                {
                    long l;
                    if(simplegraphobjectcursor.areMoreObjectsAvailable())
                        l = 2000L;
                    else
                        l = 0L;
                    graphobjectpagingloader.refreshOriginalRequest(l);
                    return;
                }
            }
        }


        private AsNeededLoadingStrategy()
        {
            super();
        }

    }

    private class SearchTextWatcher
        implements TextWatcher
    {

        public void afterTextChanged(Editable editable)
        {
        }

        public void beforeTextChanged(CharSequence charsequence, int i, int j, int k)
        {
        }

        public void onTextChanged(CharSequence charsequence, int i, int j, int k)
        {
            onSearchBoxTextChanged(charsequence.toString(), false);
        }


        private SearchTextWatcher()
        {
            super();
        }

    }


    public PlacePickerFragment()
    {
        this(null);
    }

    public PlacePickerFragment(Bundle bundle)
    {
        super(GraphPlace.class, com.facebook.android.R.layout.com_facebook_placepickerfragment, bundle);
        radiusInMeters = 1000;
        resultsLimit = 100;
        showSearchBox = true;
        setPlacePickerSettingsFromBundle(bundle);
    }

    private Request createRequest(Location location1, int i, int j, String s, Set set, Session session)
    {
        Request reqeust = Request.newPlacesSearchRequest(session, location1, i, j, s, null);
        HashSet hashSet = new HashSet(set);
        hashSet.addAll(Arrays.asList(new String[] {
            "id", "name", "location", "category", "were_here_count"
        }));
        String temp = adapter.getPictureFieldSpecifier();
        if(temp != null)
            hashSet.add(set);
        Bundle bundle = reqeust.getParameters();
        bundle.putString("fields", TextUtils.join(",", hashSet));
        reqeust.setParameters(bundle);
        return reqeust;
    }

    private Timer createSearchTextTimer()
    {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run()
            {
                onSearchTextTimerTriggered();
            }

        }
, 0L, 2000L);
        return timer;
    }

    private void onSearchTextTimerTriggered()
    {
        if(hasSearchTextChangedSinceLastQuery)
        {
            (new Handler(Looper.getMainLooper())).post(new Runnable() {

                public void run()
                {
                    try {
                    	loadData(true);
					} catch(Exception ex) {

						Object obj = getOnErrorListener();
						if(obj == null) {
							Logger.log(LoggingBehavior.REQUESTS, "PlacePickerFragment", "Error loading data : %s", new Object[] {
								null
							});
							return;
						} else {
							((PickerFragment.OnErrorListener) (obj)).onError(PlacePickerFragment.this, null);
						}
						return;
					}

                    /*
                    if(obj == null)
                    	return;

                    PickerFragment.OnErrorListener onerrorlistener = getOnErrorListener();
                    if(onerrorlistener != null)
                    {
                        onerrorlistener.onError(PlacePickerFragment.this, ((FacebookException) (obj)));
                        return;
                    } else
                    {
                        Logger.log(LoggingBehavior.REQUESTS, "PlacePickerFragment", "Error loading data : %s", new Object[] {
                            obj1
                        });
                        return;
                    }
                    obj1 = new FacebookException(((Throwable) (obj1)));
                    if(obj1 != null)
                    {
                        PickerFragment.OnErrorListener onerrorlistener1 = getOnErrorListener();
                        if(onerrorlistener1 != null)
                            onerrorlistener1.onError(PlacePickerFragment.this, ((FacebookException) (obj1)));
                        else
                            Logger.log(LoggingBehavior.REQUESTS, "PlacePickerFragment", "Error loading data : %s", new Object[] {
                                obj1
                            });
                        return;
                    } else
                    {
                        return;
                    }
                    if(false)
                    {
                        PickerFragment.OnErrorListener onerrorlistener2 = getOnErrorListener();
                        if(onerrorlistener2 != null)
                            onerrorlistener2.onError(PlacePickerFragment.this, null);
                        else
                            Logger.log(LoggingBehavior.REQUESTS, "PlacePickerFragment", "Error loading data : %s", new Object[] {
                                null
                            });
                    }
                    */
                }

            });
            return;
        } else
        {
            searchTextTimer.cancel();
            searchTextTimer = null;
            return;
        }
    }

    private void setPlacePickerSettingsFromBundle(Bundle bundle)
    {
        if(bundle != null)
        {
            setRadiusInMeters(bundle.getInt("com.facebook.widget.PlacePickerFragment.RadiusInMeters", radiusInMeters));
            setResultsLimit(bundle.getInt("com.facebook.widget.PlacePickerFragment.ResultsLimit", resultsLimit));
            if(bundle.containsKey("com.facebook.widget.PlacePickerFragment.SearchText"))
                setSearchText(bundle.getString("com.facebook.widget.PlacePickerFragment.SearchText"));
            if(bundle.containsKey("com.facebook.widget.PlacePickerFragment.Location"))
                setLocation((Location)bundle.getParcelable("com.facebook.widget.PlacePickerFragment.Location"));
            showSearchBox = bundle.getBoolean("com.facebook.widget.PlacePickerFragment.ShowSearchBox", showSearchBox);
        }
    }

    PickerFragment.PickerFragmentAdapter createAdapter()
    {
        PickerFragment.PickerFragmentAdapter pickerfragmentadapter = new PickerFragment.PickerFragmentAdapter(getActivity()) {

            protected int getDefaultPicture()
            {
                return com.facebook.android.R.drawable.com_facebook_place_default_icon;
            }

            protected int getGraphObjectRowLayoutId(GraphObject graphobject)
            {
                return getGraphObjectRowLayoutId((GraphPlace)graphobject);
            }

            protected int getGraphObjectRowLayoutId(GraphPlace graphplace)
            {
                return com.facebook.android.R.layout.com_facebook_placepickerfragment_list_row;
            }

            protected CharSequence getSubTitleOfGraphObject(GraphObject graphobject)
            {
                return getSubTitleOfGraphObject((GraphPlace)graphobject);
            }

            protected CharSequence getSubTitleOfGraphObject(GraphPlace graphplace)
            {
                String s = graphplace.getCategory();
                Integer integer = (Integer)graphplace.getProperty("were_here_count");
                Object obj = null;
                CharSequence charSequence;
                if(s != null && integer != null)
                {
                    charSequence = getString(com.facebook.android.R.string.com_facebook_placepicker_subtitle_format, new Object[] {
                        s, integer
                    });
                } else
                {
                    if(s == null && integer != null)
                        return getString(com.facebook.android.R.string.com_facebook_placepicker_subtitle_were_here_only_format, new Object[] {
                            integer
                        });
                    charSequence = null;
                    if(s != null)
                    {
                        charSequence = null;
                        if(integer == null)
                            return getString(com.facebook.android.R.string.com_facebook_placepicker_subtitle_catetory_only_format, new Object[] {
                                s
                            });
                    }
                }
                return charSequence;
            }

        };
        pickerfragmentadapter.setShowCheckbox(false);
        pickerfragmentadapter.setShowPicture(getShowPictures());
        return pickerfragmentadapter;
    }

    PickerFragment.LoadingStrategy createLoadingStrategy()
    {
        return new AsNeededLoadingStrategy();
    }

    PickerFragment.SelectionStrategy createSelectionStrategy()
    {
        return new PickerFragment.SingleSelectionStrategy();
    }

    String getDefaultTitleText()
    {
        return getString(com.facebook.android.R.string.com_facebook_nearby);
    }

    public Location getLocation()
    {
        return location;
    }

    public int getRadiusInMeters()
    {
        return radiusInMeters;
    }

    Request getRequestForLoadData(Session session)
    {
        return createRequest(location, radiusInMeters, resultsLimit, searchText, extraFields, session);
    }

    public int getResultsLimit()
    {
        return resultsLimit;
    }

    public String getSearchText()
    {
        return searchText;
    }

    public GraphPlace getSelection()
    {
        java.util.List list = getSelectedGraphObjects();
        if(list != null && !list.isEmpty())
            return (GraphPlace)list.iterator().next();
        else
            return null;
    }

    void logAppEvents(boolean flag)
    {
        AppEventsLogger appeventslogger = AppEventsLogger.newLogger(getActivity(), getSession());
        Bundle bundle = new Bundle();
        String s;
        int i;
        if(flag)
            s = "Completed";
        else
            s = "Unknown";
        bundle.putString("fb_dialog_outcome", s);
        if(getSelection() != null)
            i = 1;
        else
            i = 0;
        bundle.putInt("num_places_picked", i);
        appeventslogger.logSdkEvent("fb_place_picker_usage", null, bundle);
    }

    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        if(searchBox != null)
            ((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(searchBox, 1);
    }

    public void onDetach()
    {
        super.onDetach();
        if(searchBox != null)
            ((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(searchBox.getWindowToken(), 0);
    }

    public void onInflate(Activity activity, AttributeSet attributeset, Bundle bundle)
    {
        super.onInflate(activity, attributeset, bundle);
        TypedArray typedArray = activity.obtainStyledAttributes(attributeset, com.facebook.android.R.styleable.com_facebook_place_picker_fragment);
        setRadiusInMeters(typedArray.getInt(com.facebook.android.R.styleable.com_facebook_place_picker_fragment_radius_in_meters, radiusInMeters));
        setResultsLimit(typedArray.getInt(com.facebook.android.R.styleable.com_facebook_place_picker_fragment_results_limit, resultsLimit));
        if(typedArray.hasValue(com.facebook.android.R.styleable.com_facebook_place_picker_fragment_results_limit))
            setSearchText(typedArray.getString(com.facebook.android.R.styleable.com_facebook_place_picker_fragment_search_text));
        showSearchBox = typedArray.getBoolean(com.facebook.android.R.styleable.com_facebook_place_picker_fragment_show_search_box, showSearchBox);
        typedArray.recycle();
    }

    void onLoadingData()
    {
        hasSearchTextChangedSinceLastQuery = false;
    }

    public void onSearchBoxTextChanged(String s, boolean flag)
    {
        if(flag || !Utility.stringsEqualOrEmpty(searchText, s))
        {
            String s1 = s;
            if(TextUtils.isEmpty(s))
                s1 = null;
            searchText = s1;
            hasSearchTextChangedSinceLastQuery = true;
            if(searchTextTimer == null)
            {
                searchTextTimer = createSearchTextTimer();
                return;
            }
        }
    }

    void saveSettingsToBundle(Bundle bundle)
    {
        super.saveSettingsToBundle(bundle);
        bundle.putInt("com.facebook.widget.PlacePickerFragment.RadiusInMeters", radiusInMeters);
        bundle.putInt("com.facebook.widget.PlacePickerFragment.ResultsLimit", resultsLimit);
        bundle.putString("com.facebook.widget.PlacePickerFragment.SearchText", searchText);
        bundle.putParcelable("com.facebook.widget.PlacePickerFragment.Location", location);
        bundle.putBoolean("com.facebook.widget.PlacePickerFragment.ShowSearchBox", showSearchBox);
    }

    public void setLocation(Location location1)
    {
        location = location1;
    }

    public void setRadiusInMeters(int i)
    {
        radiusInMeters = i;
    }

    public void setResultsLimit(int i)
    {
        resultsLimit = i;
    }

    public void setSearchText(String s)
    {
        String s1 = s;
        if(TextUtils.isEmpty(s))
            s1 = null;
        searchText = s1;
        if(searchBox != null)
            searchBox.setText(s1);
    }

    public void setSettingsFromBundle(Bundle bundle)
    {
        super.setSettingsFromBundle(bundle);
        setPlacePickerSettingsFromBundle(bundle);
    }

    void setupViews(ViewGroup viewgroup)
    {
        if(showSearchBox)
        {
            ListView listview = (ListView)viewgroup.findViewById(com.facebook.android.R.id.com_facebook_picker_list_view);
            listview.addHeaderView(getActivity().getLayoutInflater().inflate(com.facebook.android.R.layout.com_facebook_picker_search_box, listview, false), null, false);
            searchBox = (EditText)viewgroup.findViewById(com.facebook.android.R.id.com_facebook_picker_search_text);
            searchBox.addTextChangedListener(new SearchTextWatcher());
            if(!TextUtils.isEmpty(searchText))
                searchBox.setText(searchText);
        }
    }

    private static final String CATEGORY = "category";
    public static final int DEFAULT_RADIUS_IN_METERS = 1000;
    public static final int DEFAULT_RESULTS_LIMIT = 100;
    private static final String ID = "id";
    private static final String LOCATION = "location";
    public static final String LOCATION_BUNDLE_KEY = "com.facebook.widget.PlacePickerFragment.Location";
    private static final String NAME = "name";
    public static final String RADIUS_IN_METERS_BUNDLE_KEY = "com.facebook.widget.PlacePickerFragment.RadiusInMeters";
    public static final String RESULTS_LIMIT_BUNDLE_KEY = "com.facebook.widget.PlacePickerFragment.ResultsLimit";
    public static final String SEARCH_TEXT_BUNDLE_KEY = "com.facebook.widget.PlacePickerFragment.SearchText";
    public static final String SHOW_SEARCH_BOX_BUNDLE_KEY = "com.facebook.widget.PlacePickerFragment.ShowSearchBox";
    private static final String TAG = "PlacePickerFragment";
    private static final String WERE_HERE_COUNT = "were_here_count";
    private static final int searchTextTimerDelayInMilliseconds = 2000;
    private boolean hasSearchTextChangedSinceLastQuery;
    private Location location;
    private int radiusInMeters;
    private int resultsLimit;
    private EditText searchBox;
    private String searchText;
    private Timer searchTextTimer;
    private boolean showSearchBox;

}
