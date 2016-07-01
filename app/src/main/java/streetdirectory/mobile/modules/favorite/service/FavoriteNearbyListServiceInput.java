

package streetdirectory.mobile.modules.favorite.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class FavoriteNearbyListServiceInput extends SDHttpServiceInput
{

    public FavoriteNearbyListServiceInput()
    {
    }

    public FavoriteNearbyListServiceInput(String s, String s1, int i, int j, double d, double d1)
    {
        super(s);
        distance = i;
        type = j;
        longitudeNearby = d;
        latitudeNearby = d1;
        uid = s1;
    }

    public String getURL()
    {
        return URLFactory.createURLFavoriteNearbyListService(countryCode, uid, distance, type, longitudeNearby, latitudeNearby, apiVersion);
    }

    public int distance;
    public double latitudeNearby;
    public double longitudeNearby;
    public int type;
    public String uid;
}
