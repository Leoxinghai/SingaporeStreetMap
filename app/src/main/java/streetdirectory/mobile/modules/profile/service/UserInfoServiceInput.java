

package streetdirectory.mobile.modules.profile.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class UserInfoServiceInput extends SDHttpServiceInput
{

    public UserInfoServiceInput()
    {
    }

    public UserInfoServiceInput(String s, String s1)
    {
        super(s);
        uid = s1;
    }

    public String getURL()
    {
        return URLFactory.createURLUserInfoStatus(uid, countryCode, apiVersion);
    }

    public String uid;
}
