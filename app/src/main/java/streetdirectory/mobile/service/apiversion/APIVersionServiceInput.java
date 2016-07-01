

package streetdirectory.mobile.service.apiversion;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import streetdirectory.mobile.SDApplication;
import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class APIVersionServiceInput extends SDHttpServiceInput
{

    public APIVersionServiceInput()
    {
        Context context = SDApplication.getAppContext();
        applicationName = "streetdirectory.mobile";//context.getPackageName();
        try
        {
            applicationVersion = context.getPackageManager().getPackageInfo(applicationName, 0).versionCode;
            return;
        }
        catch(Exception exception)
        {
            applicationVersion = 0;
        }
    }

    public String getURL()
    {
        return URLFactory.createURLAPIVersion(applicationName, applicationVersion);
    }

    public String applicationName;
    public int applicationVersion;
}
