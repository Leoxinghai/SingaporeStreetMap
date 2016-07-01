

package streetdirectory.mobile.modules.friend.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class RejectFriendServiceInput extends SDHttpServiceInput
{

    public RejectFriendServiceInput()
    {
    }

    public RejectFriendServiceInput(String s, String s1, String s2)
    {
        super(s);
        uid = s1;
        friendUID = s2;
    }

    public String getURL()
    {
        return URLFactory.createURLFriendReject(uid, friendUID, countryCode, apiVersion);
    }

    public String friendUID;
    public String uid;
}
