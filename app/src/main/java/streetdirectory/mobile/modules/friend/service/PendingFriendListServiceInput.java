

package streetdirectory.mobile.modules.friend.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class PendingFriendListServiceInput extends SDHttpServiceInput
{

    public PendingFriendListServiceInput()
    {
    }

    public PendingFriendListServiceInput(String s, String s1)
    {
        super(s);
        uid = s1;
    }

    public String getURL()
    {
        return URLFactory.createURLFriendPendingList(uid, countryCode, apiVersion);
    }

    public String uid;
}
