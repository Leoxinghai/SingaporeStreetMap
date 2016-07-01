

package streetdirectory.mobile.modules.favorite.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class FavoriteListServiceInput extends SDHttpServiceInput
{

    public FavoriteListServiceInput()
    {
    }

    public FavoriteListServiceInput(String s, String s1, int i, int j, int k)
    {
        super(s);
        type = k;
        uid = s1;
        start = i;
        limit = j;
    }

    public String getURL()
    {
        return URLFactory.createURLFavoriteList(countryCode, uid, type, start, limit, apiVersion);
    }

    public int limit;
    public int start;
    public int type;
    public String uid;
}
