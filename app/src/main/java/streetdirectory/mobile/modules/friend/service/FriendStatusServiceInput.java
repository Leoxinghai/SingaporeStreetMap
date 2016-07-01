

package streetdirectory.mobile.modules.friend.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class FriendStatusServiceInput extends SDHttpServiceInput
{

    public FriendStatusServiceInput()
    {
    }

    public FriendStatusServiceInput(String s, String s1, String s2)
    {
        super(s);
        personalUid = s1;
        friendUID = s2;
    }

    public String getURL()
    {
        return URLFactory.createURLFriendStatus(personalUid, friendUID, countryCode, apiVersion);
    }

    public String friendUID;
    public String personalUid;
}
