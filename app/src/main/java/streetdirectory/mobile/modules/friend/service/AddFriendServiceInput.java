

package streetdirectory.mobile.modules.friend.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class AddFriendServiceInput extends SDHttpServiceInput
{

    public AddFriendServiceInput()
    {
    }

    public AddFriendServiceInput(String s, String s1, String s2)
    {
        super(s);
        uid = s1;
        friendUID = s2;
    }

    public String getURL()
    {
        return URLFactory.createURLFriendAdd(uid, friendUID, countryCode, apiVersion);
    }

    public String friendUID;
    public String uid;
}
