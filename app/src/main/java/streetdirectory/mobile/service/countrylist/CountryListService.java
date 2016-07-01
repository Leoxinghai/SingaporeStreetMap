// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.service.countrylist;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import streetdirectory.mobile.core.SDPreferences;
import streetdirectory.mobile.service.SDHttpService;
import streetdirectory.mobile.service.SDHttpServiceOutput;
import streetdirectory.mobile.service.statelist.StateListService;
import streetdirectory.mobile.service.statelist.StateListServiceInput;

// Referenced classes of package streetdirectory.mobile.service.countrylist:
//            CountryListServiceInput, CountryListServiceOutput

public class CountryListService extends SDHttpService
{

    public CountryListService()
    {
        super(new CountryListServiceInput(),CountryListServiceOutput.class);
    }

    public CountryListService(CountryListServiceInput countrylistserviceinput)
    {
        super(countrylistserviceinput, CountryListServiceOutput.class);
    }

    static class SUBCLASS1 extends StateListService
    {

        public SUBCLASS1(StateListServiceInput statelistserviceinput, ArrayList arraylist, StateListServiceInput statelistserviceinput1) {
            super(statelistserviceinput);
            countryCodes = arraylist;
            input = statelistserviceinput1;
        }
        public void onSuccess(Object obj)
        {
            onSuccess((SDHttpServiceOutput)obj);
        }

        public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
        {
            sdhttpserviceoutput.childs.clear();
            countryCodes.remove(input.countryCode);
            CountryListService.downloadStateAsyncQueue(countryCodes);
        }

        private ArrayList countryCodes;
        private StateListServiceInput input;
    }

    private static void downloadStateAsyncQueue(ArrayList arraylist)
    {
        if(arraylist.size() > 0)
        {
            StateListServiceInput statelistserviceinput = new StateListServiceInput((String)arraylist.get(0));
            statelistserviceinput.forceUpdate = true;
            (new SUBCLASS1(statelistserviceinput, arraylist, statelistserviceinput)).executeAsync();
        }
    }

    static class SUBCLASS2 extends CountryListService
    {

        public void onSuccess(Object obj)
        {
            onSuccess((SDHttpServiceOutput)obj);
        }

        public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
        {
            pref.setValueForKey("countryListVersion", categoryVersion);
            ArrayList arraylist = new ArrayList();
            Iterator iterator;
            for(iterator = sdhttpserviceoutput.childs.iterator(); iterator.hasNext(); arraylist.add(((CountryListServiceOutput)iterator.next()).countryCode));
            CountryListService.downloadStateAsyncQueue(arraylist);
        }

        final int categoryVersion;
        final SDPreferences pref;

        SUBCLASS2(CountryListServiceInput countrylistserviceinput, SDPreferences sdpreferences, int i)
        {
            super(countrylistserviceinput);
            pref = sdpreferences;
            categoryVersion = i;
        }
    }
    public static boolean updateCountryStateInBackground()
    {
        boolean flag = false;
        SDPreferences sdpreferences = SDPreferences.getInstance();
        int i = sdpreferences.getCategoryVersion();
        int j = sdpreferences.getIntForKey("countryListVersion", 0);
        CountryListServiceInput countrylistserviceinput = new CountryListServiceInput(i);
        if(j < i || !countrylistserviceinput.getSaveFile().exists())
        {
            countrylistserviceinput.forceUpdate = true;
            (new SUBCLASS2(countrylistserviceinput, sdpreferences, i)).executeAsync();
            flag = true;
        }
        return flag;
    }

    static class SUBCLASS3 extends CountryListService {

        public void onSuccess(Object obj)
        {
            onSuccess((SDHttpServiceOutput)obj);
        }

        public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
        {
            pref.setValueForKey("countryListVersion", categoryVersion);
        }

        final int categoryVersion;
        final SDPreferences pref;

        SUBCLASS3(CountryListServiceInput countrylistserviceinput, SDPreferences sdpreferences, int i)
        {
            super(countrylistserviceinput);
            pref = sdpreferences;
            categoryVersion = i;
        }
    }

    public static boolean updateInBackground()
    {
        boolean flag = false;
        SDPreferences sdpreferences = SDPreferences.getInstance();
        int i = sdpreferences.getCategoryVersion();
        int j = sdpreferences.getIntForKey("countryListVersion", 0);
        CountryListServiceInput countrylistserviceinput = new CountryListServiceInput(i);
        if(j < i || !countrylistserviceinput.getSaveFile().exists())
        {
            countrylistserviceinput.forceUpdate = true;
            (new SUBCLASS3(countrylistserviceinput, sdpreferences, i)).executeAsync();

            flag = true;
        }
        return flag;
    }

}
