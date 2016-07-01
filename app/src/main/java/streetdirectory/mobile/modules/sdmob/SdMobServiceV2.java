

package streetdirectory.mobile.modules.sdmob;

import android.content.Context;
import java.util.HashMap;
import streetdirectory.mobile.core.SDLogger;
import streetdirectory.mobile.core.StringTools;
import streetdirectory.mobile.service.*;

public class SdMobServiceV2 extends SDHttpService
{
    public static class Input extends SDHttpServiceInput
    {

        public String getURL()
        {
            String s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=sdmob&output=xml&act=adunit&app_id=").append(appId).append("&os=").append(os).append("&no_cache=1").toString();
            SDLogger.debug(s);
            return s;
        }

        public String appId;
        public String os;

        public Input(Context context)
        {
            appId = "streetdirectory.mobile";//context.getPackageName();
            os = "android";
        }
    }

    public static class Output extends SDDataOutput
    {

        public void populateData()
        {
            populateData();
            try
            {
                unit = new SdMobHelper.SdMobUnit(StringTools.tryParseInt((String)hashData.get("type"), 1), (String)hashData.get("name"), (String)hashData.get("id"));
                return;
            }
            catch(Exception exception)
            {
                return;
            }
        }

        public SdMobHelper.SdMobUnit unit;

        public Output()
        {
            unit = null;
        }
    }


    public SdMobServiceV2(Input input)
    {
        super(input, SdMobServiceV2.Output.class);
        initialize();
    }

    private void initialize()
    {
    }
}
