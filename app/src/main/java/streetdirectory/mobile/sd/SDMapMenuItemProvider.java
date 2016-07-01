// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.sd;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.*;
import streetdirectory.mobile.core.SDAsyncTask;
import streetdirectory.mobile.core.SDPreferences;
import streetdirectory.mobile.core.ui.sidemenu.*;
import streetdirectory.mobile.modules.businessfinder.BusinessFinderMenuItem;
import streetdirectory.mobile.modules.businessfinder.BusinessOffersMenuItem;
import streetdirectory.mobile.modules.direction.DirectionMenuItem;
import streetdirectory.mobile.modules.map.MapMenuItem;
import streetdirectory.mobile.modules.nearby.NearbyMenuItem;
import streetdirectory.mobile.modules.offlinemap.OfflineMapMenuItem;
import streetdirectory.mobile.modules.search.SearchMenuItem;
import streetdirectory.mobile.modules.search.service.SearchServiceOutput;
import streetdirectory.mobile.modules.settings.SettingsMenuItem;
import streetdirectory.mobile.modules.settings.sendfeedback.SendFeedBackMenuItem;
import streetdirectory.mobile.modules.trafficcamera.TrafficCameraMenuItem;

// Referenced classes of package streetdirectory.mobile.sd:
//            SDSearchHistoryMenuItem, SDHashDataArrayList

public class SDMapMenuItemProvider
{

    public SDMapMenuItemProvider()
    {
    }

    public static void addSearchHistory(SearchServiceOutput searchserviceoutput)
    {
        for(Iterator iterator = mSearchHistoryList.iterator(); iterator.hasNext();)
        {
            SearchServiceOutput searchserviceoutput1;
            for(searchserviceoutput1 = (SearchServiceOutput)iterator.next(); searchserviceoutput.type == 1 && searchserviceoutput.placeID == searchserviceoutput1.placeID && searchserviceoutput.addressID == searchserviceoutput1.addressID || searchserviceoutput.type == 2 && searchserviceoutput.companyID == searchserviceoutput1.companyID && searchserviceoutput.locationID == searchserviceoutput1.locationID;)
                return;

            if(searchserviceoutput.type == 11 && searchserviceoutput.categoryID == searchserviceoutput1.categoryID)
                return;
        }

        mSearchHistoryList.add(searchserviceoutput);
        if(mSearchHistoryList.size() > 3)
            mSearchHistoryList.remove(0);
        saveSearchHistory();
        reload(mCountryCode);
    }

    public static void init()
    {
        SDSideMenuItem.addTypeCount(SDSideMenuHeader.class);
        SDSideMenuItem.addTypeCount(SDSideMenuBasicItem.class);
        SDSideMenuItem.addTypeCount(SDSearchHistoryMenuItem.class);
        loadSearchHistory();
    }

    public static void loadSearchHistory()
    {
        Object obj = SDPreferences.getInstance().getStringForKey("search_history_key", null);
        if(obj != null)
        {
            SearchServiceOutput searchserviceoutput;
            for(obj = ((SDHashDataArrayList)(new GsonBuilder()).create().fromJson(((String) (obj)), SDHashDataArrayList.class)).hashData.iterator(); ((Iterator) (obj)).hasNext(); mSearchHistoryList.add(searchserviceoutput))
                searchserviceoutput = new SearchServiceOutput((HashMap)((Iterator) (obj)).next());

        }
    }

    public static void reload(String s)
    {
        mCountryCode = s;
        SDSideMenuProvider.items.clear();
        if(mSearchHistoryList.size() > 0)
        {
            SDSideMenuProvider.items.add(new SDSideMenuHeader("SEARCH HISTORY"));
            SearchServiceOutput searchserviceoutput;
            for(Iterator iterator = mSearchHistoryList.iterator(); iterator.hasNext(); SDSideMenuProvider.items.add(new SDSearchHistoryMenuItem(searchserviceoutput)))
                searchserviceoutput = (SearchServiceOutput)iterator.next();

        }
        SDSideMenuProvider.items.add(new SDSideMenuHeader("MENU"));
        SDSideMenuProvider.items.add(new MapMenuItem());
        SDSideMenuProvider.items.add(new SearchMenuItem());
        SDSideMenuProvider.items.add(new BusinessFinderMenuItem());
        if(s.toLowerCase().equals("sg"))
            SDSideMenuProvider.items.add(new BusinessOffersMenuItem());
        SDSideMenuProvider.items.add(new DirectionMenuItem());
        SDSideMenuProvider.items.add(new NearbyMenuItem());
        if(mFullSupportedCountryList.contains(s.toLowerCase()))
            SDSideMenuProvider.items.add(new TrafficCameraMenuItem());
        SDSideMenuProvider.items.add(new SDSideMenuHeader("OFFLINE MAPS"));
        SDSideMenuProvider.items.add(new OfflineMapMenuItem());
        SDSideMenuProvider.items.add(new SDSideMenuHeader("PERSONAL"));
        SDSideMenuProvider.items.add(new SendFeedBackMenuItem());
        SDSideMenuProvider.items.add(new SettingsMenuItem());
    }

    public static void removeSearchHistory(SearchServiceOutput searchserviceoutput)
    {
        mSearchHistoryList.remove(searchserviceoutput);
        saveSearchHistory();
        reload(mCountryCode);
    }

    public static void saveSearchHistory()
    {
        (new SDAsyncTask() {

            protected Object doInBackground(Object aobj[])
            {
                return doInBackground((Void[])aobj);
            }

            protected Void doInBackground(Void avoid[])
            {
                Gson gson = (new GsonBuilder()).create();
                SDHashDataArrayList sdhashdataarraylist = new SDHashDataArrayList();
                SearchServiceOutput searchserviceoutput;
                for(Iterator iterator = SDMapMenuItemProvider.mSearchHistoryList.iterator(); iterator.hasNext(); sdhashdataarraylist.hashData.add(searchserviceoutput.hashData))
                    searchserviceoutput = (SearchServiceOutput)iterator.next();

                String temp = gson.toJson(sdhashdataarraylist);
                SDPreferences.getInstance().setValueForKey("search_history_key", temp);
                return null;
            }

        }
).executeTask(new Void[0]);
    }

    public static final int SEARCH_HISTORY_LIMIT = 3;
    private static String mCountryCode;
    private static final String mFullSupportedCountries[] = {
        "id", "my", "sg"
    };
    private static final ArrayList mFullSupportedCountryList = new ArrayList(Arrays.asList(mFullSupportedCountries));
    public static final ArrayList mSearchHistoryList = new ArrayList();

}
