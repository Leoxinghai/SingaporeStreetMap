// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.nearby.category.service;

import java.io.File;
import streetdirectory.mobile.core.SDPreferences;
import streetdirectory.mobile.service.SDHttpService;
import streetdirectory.mobile.service.SDHttpServiceOutput;

// Referenced classes of package streetdirectory.mobile.modules.nearby.category.service:
//            NearbyCategoryServiceInput, NearbyCategoryServiceOutput

public class NearbyCategoryService extends SDHttpService
{

    public NearbyCategoryService(String s)
    {
        super(new NearbyCategoryServiceInput(s), NearbyCategoryServiceOutput.class);
    }

    public NearbyCategoryService(NearbyCategoryServiceInput nearbycategoryserviceinput)
    {
        super(nearbycategoryserviceinput, NearbyCategoryServiceOutput.class);
    }

    public static int getVersion(String s)
    {
        return SDPreferences.getInstance().getIntForKey((new StringBuilder()).append(s).append("_nearbyCategoryVersion").toString(), 0);
    }

    public static void setVersion(String s, int i)
    {
        SDPreferences.getInstance().setValueForKey((new StringBuilder()).append(s).append("_nearbyCategoryVersion").toString(), i);
    }

    public static boolean updateInBackground(final String countryCode)
    {
        int i = SDPreferences.getInstance().getCategoryVersion();
        final int categoryVersion = getVersion(countryCode);
        NearbyCategoryServiceInput nearbycategoryserviceinput = new NearbyCategoryServiceInput(countryCode, i);
        if(categoryVersion < i || !nearbycategoryserviceinput.getSaveFile().exists())
        {
            nearbycategoryserviceinput.forceUpdate = true;
            (new NearbyCategoryService(countryCode) {

                public void onSuccess(Object obj)
                {
                    onSuccess((SDHttpServiceOutput)obj);
                }

                public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
                {
                    setVersion(countryCode, categoryVersion);
                }

            }).executeAsync();
            return true;
        } else
        {
            return false;
        }
    }
}
