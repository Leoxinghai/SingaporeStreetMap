// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.businessfinder.service;

import java.io.File;
import java.util.ArrayList;
import streetdirectory.mobile.core.SDPreferences;
import streetdirectory.mobile.service.SDHttpService;
import streetdirectory.mobile.service.SDHttpServiceOutput;

// Referenced classes of package streetdirectory.mobile.modules.businessfinder.service:
//            BusinessFinderServiceOutput, BusinessFinderServiceInput

public class BusinessFinderService extends SDHttpService
{

    public BusinessFinderService(BusinessFinderServiceInput businessfinderserviceinput)
    {
        super(businessfinderserviceinput, BusinessFinderServiceOutput.class);
    }

    public static int getVersion(String s, int i)
    {
        return SDPreferences.getInstance().getIntForKey((new StringBuilder()).append(s).append("_").append(i).append("_businessFinderVersion").toString(), 0);
    }

    public static void setVersion(String s, int i, int j)
    {
        SDPreferences.getInstance().setValueForKey((new StringBuilder()).append(s).append("_").append(i).append("_businessFinderVersion").toString(), j);
    }

    public static void updateInBackground(final String countryCode, final ArrayList menuIDs)
    {
        if(menuIDs.size() > 0)
        {
            final int menuID = ((Integer)menuIDs.get(0)).intValue();
            final int categoryVersion = SDPreferences.getInstance().getCategoryVersion();
            int k = getVersion(countryCode, menuID);
            BusinessFinderServiceInput businessfinderserviceinput = new BusinessFinderServiceInput(countryCode, menuID, categoryVersion);
            if(k < categoryVersion || !businessfinderserviceinput.getSaveFile().exists())
            {
                businessfinderserviceinput.forceUpdate = true;
                (new BusinessFinderService(businessfinderserviceinput) {

                    public void onSuccess(Object obj)
                    {
                        onSuccess((SDHttpServiceOutput)obj);
                    }

                    public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
                    {
                        sdhttpserviceoutput.childs.clear();
                        setVersion(countryCode, menuID, categoryVersion);
                        menuIDs.remove(menuID);
                        updateInBackground(countryCode, menuIDs);
                    }

                }).executeAsync();
            }
        }
    }

    public static boolean updateInBackground(final String countryCode, final int menuID)
    {
        final int categoryVersion = SDPreferences.getInstance().getCategoryVersion();
        int k = getVersion(countryCode, menuID);
        BusinessFinderServiceInput businessfinderserviceinput = new BusinessFinderServiceInput(countryCode, menuID, categoryVersion);
        if(k < categoryVersion || !businessfinderserviceinput.getSaveFile().exists())
        {
            businessfinderserviceinput.forceUpdate = true;
            (new BusinessFinderService(businessfinderserviceinput) {

                public void onSuccess(Object obj)
                {
                    onSuccess((SDHttpServiceOutput)obj);
                }

                public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
                {
                    setVersion(countryCode, menuID, categoryVersion);
                }

            }).executeAsync();
            return true;
        } else
        {
            return false;
        }
    }
}
