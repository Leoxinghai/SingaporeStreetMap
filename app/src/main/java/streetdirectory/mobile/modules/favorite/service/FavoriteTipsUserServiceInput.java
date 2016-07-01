

package streetdirectory.mobile.modules.favorite.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class FavoriteTipsUserServiceInput extends SDHttpServiceInput
{

    public FavoriteTipsUserServiceInput()
    {
    }

    public FavoriteTipsUserServiceInput(String s, String s1, int i, int j, int k)
    {
        super(s);
        uid = s1;
        start = i;
        limit = j;
    }

    public String getURL()
    {
        return URLFactory.createURLFavoriteUserTipsService(countryCode, uid, start, limit, apiVersion);
    }

    public int limit;
    public int start;
    public String uid;
}
