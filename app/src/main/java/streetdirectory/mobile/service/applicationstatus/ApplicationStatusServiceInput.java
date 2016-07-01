

package streetdirectory.mobile.service.applicationstatus;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class ApplicationStatusServiceInput extends SDHttpServiceInput
{

    public ApplicationStatusServiceInput()
    {
    }

    public ApplicationStatusServiceInput(String s, String s1)
    {
        super(s);
        uid = s1;
    }

    public String getURL()
    {
        return URLFactory.createURLApplicationStatus(countryCode, uid, apiVersion);
    }

    public String uid;
}
