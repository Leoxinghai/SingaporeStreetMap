

package streetdirectory.mobile.modules.favorite.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class FavoriteRouteListServiceInput extends SDHttpServiceInput
{

    public FavoriteRouteListServiceInput()
    {
    }

    public FavoriteRouteListServiceInput(String s, String s1)
    {
        super(s);
        uid = s1;
    }

    public String getURL()
    {
        return URLFactory.createURLFavoriteRouteList(countryCode, uid, apiVersion);
    }

    public String uid;
}
