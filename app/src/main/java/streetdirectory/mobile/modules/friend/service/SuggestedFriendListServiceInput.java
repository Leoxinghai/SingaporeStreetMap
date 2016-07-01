

package streetdirectory.mobile.modules.friend.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class SuggestedFriendListServiceInput extends SDHttpServiceInput
{

    public SuggestedFriendListServiceInput()
    {
    }

    public SuggestedFriendListServiceInput(String s, String s1, String s2, int i, int j)
    {
        super(s);
        uid = s1;
        search = s2;
        start = i;
        limit = j;
    }

    public String getURL()
    {
        return URLFactory.createURLFriendSuggestedList(uid, countryCode, search, start, limit, apiVersion);
    }

    public int limit;
    public String search;
    public int start;
    public String uid;
}
