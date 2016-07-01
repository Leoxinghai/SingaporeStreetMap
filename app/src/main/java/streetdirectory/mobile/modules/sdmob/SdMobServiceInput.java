

package streetdirectory.mobile.modules.sdmob;

import streetdirectory.mobile.core.SDLogger;
import streetdirectory.mobile.service.SDHttpServiceInput;

public class SdMobServiceInput extends SDHttpServiceInput
{

    public SdMobServiceInput(String s)
    {
        countryCode = s;
    }

    public String getURL()
    {
        String s1 = "http://www.streetdirectory.com/api/?mode=sdmob&act=detail&output=xml&no_cache=1";
        String s = s1;
        if(countryCode != null)
        {
            s = s1;
            if(countryCode.length() > 0)
                s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=sdmob&act=detail&output=xml&country=").append(countryCode).append("&no_cache=1").toString();
        }
        SDLogger.debug(s);
        return s;
    }
}
