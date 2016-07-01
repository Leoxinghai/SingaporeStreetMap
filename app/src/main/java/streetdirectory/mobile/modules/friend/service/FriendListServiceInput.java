

package streetdirectory.mobile.modules.friend.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class FriendListServiceInput extends SDHttpServiceInput
{

    public FriendListServiceInput()
    {
    }

    public FriendListServiceInput(String s, String s1, String s2, boolean flag, int i, int j)
    {
        super(s);
        uid = s1;
        search = s2;
        isRandom = flag;
        start = i;
        limit = j;
    }

    public String getURL()
    {
        return URLFactory.createURLFriendList(uid, countryCode, search, isRandom, start, limit, apiVersion);
    }

    public boolean isRandom;
    public int limit;
    public String search;
    public int start;
    public String uid;
}
