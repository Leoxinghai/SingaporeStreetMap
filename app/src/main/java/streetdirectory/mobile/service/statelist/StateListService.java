// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.service.statelist;

import java.io.File;
import streetdirectory.mobile.core.SDPreferences;
import streetdirectory.mobile.service.SDHttpService;
import streetdirectory.mobile.service.SDHttpServiceOutput;

// Referenced classes of package streetdirectory.mobile.service.statelist:
//            StateListServiceInput, StateListServiceOutput

public class StateListService extends SDHttpService
{

    public StateListService(String s)
    {
        super(new StateListServiceInput(s), StateListServiceOutput.class);
    }

    public StateListService(StateListServiceInput statelistserviceinput)
    {
        super(statelistserviceinput, StateListServiceOutput.class);
    }

    public static int getVersion(String s)
    {
        return SDPreferences.getInstance().getIntForKey((new StringBuilder()).append(s).append("_stateListVersion").toString(), 0);
    }

    public static void setVersion(String s, int i)
    {
        SDPreferences.getInstance().setValueForKey((new StringBuilder()).append(s).append("_stateListVersion").toString(), i);
    }

    public static boolean updateInBackground(final String countryCode)
    {
        final int categoryVersion = SDPreferences.getInstance().getCategoryVersion();
        int j = getVersion(countryCode);
        StateListServiceInput statelistserviceinput = new StateListServiceInput(countryCode);
        if(j < categoryVersion || !statelistserviceinput.getSaveFile().exists())
        {
            statelistserviceinput.forceUpdate = true;
            (new StateListService(statelistserviceinput) {

                public void onSuccess(Object obj)
                {
                    onSuccess((SDHttpServiceOutput)obj);
                }

                public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
                {
                    setVersion(countryCode, categoryVersion);
                }

            }
).executeAsync();
            return true;
        } else
        {
            return false;
        }
    }
}
