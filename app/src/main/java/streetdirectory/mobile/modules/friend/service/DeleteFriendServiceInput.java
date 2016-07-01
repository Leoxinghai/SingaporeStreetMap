

package streetdirectory.mobile.modules.friend.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class DeleteFriendServiceInput extends SDHttpServiceInput
{

    public DeleteFriendServiceInput()
    {
    }

    public DeleteFriendServiceInput(String s, String s1, String s2)
    {
        super(s);
        uid = s1;
        friendUID = s2;
    }

    public String getURL()
    {
        return URLFactory.createURLFriendDelete(uid, friendUID, countryCode, apiVersion);
    }

    public String friendUID;
    public String uid;
}
