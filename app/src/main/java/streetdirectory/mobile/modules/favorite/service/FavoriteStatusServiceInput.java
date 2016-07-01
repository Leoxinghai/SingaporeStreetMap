

package streetdirectory.mobile.modules.favorite.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class FavoriteStatusServiceInput extends SDHttpServiceInput
{

    public FavoriteStatusServiceInput()
    {
    }

    public FavoriteStatusServiceInput(String s, String s1, String s2)
    {
        super(s);
        type = 2;
        companyID = s1;
        uid = s2;
    }

    public FavoriteStatusServiceInput(String s, String s1, String s2, String s3)
    {
        super(s);
        type = 1;
        placeID = s1;
        addressID = s2;
        uid = s3;
    }

    public String getURL()
    {
        if(type == 2)
            return URLFactory.createURLFavoriteBusinessStatus(countryCode, companyID, uid, apiVersion);
        else
            return URLFactory.createURLFavoriteLocationStatus(countryCode, placeID, addressID, uid, apiVersion);
    }

    public String addressID;
    public String companyID;
    public String placeID;
    public int type;
    public String uid;
}
