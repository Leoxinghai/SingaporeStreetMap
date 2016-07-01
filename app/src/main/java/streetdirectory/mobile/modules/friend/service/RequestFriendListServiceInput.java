

package streetdirectory.mobile.modules.friend.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class RequestFriendListServiceInput extends SDHttpServiceInput
{

    public RequestFriendListServiceInput()
    {
    }

    public RequestFriendListServiceInput(String s, String s1)
    {
        super(s);
        uid = s1;
    }

    public String getURL()
    {
        return URLFactory.createURLFriendRequestList(uid, countryCode, apiVersion);
    }

    public String uid;
}
