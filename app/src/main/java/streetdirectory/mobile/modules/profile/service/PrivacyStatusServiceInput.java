

package streetdirectory.mobile.modules.profile.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class PrivacyStatusServiceInput extends SDHttpServiceInput
{

    public PrivacyStatusServiceInput()
    {
    }

    public PrivacyStatusServiceInput(String s, String s1)
    {
        super(s);
        uid = s1;
    }

    public String getURL()
    {
        return URLFactory.createURLPrivacyStatus(uid, apiVersion);
    }

    public String uid;
}
