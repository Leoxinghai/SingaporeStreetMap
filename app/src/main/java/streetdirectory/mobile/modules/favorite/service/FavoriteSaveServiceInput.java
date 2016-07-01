

package streetdirectory.mobile.modules.favorite.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class FavoriteSaveServiceInput extends SDHttpServiceInput
{

    public FavoriteSaveServiceInput()
    {
    }

    public FavoriteSaveServiceInput(String s, String s1, String s2, String s3)
    {
        super(s);
        type = 2;
        companyID = s1;
        uid = s2;
        saveName = s3;
    }

    public FavoriteSaveServiceInput(String s, String s1, String s2, String s3, String s4)
    {
        super(s);
        type = 1;
        placeID = s1;
        addressID = s2;
        uid = s3;
        saveName = s4;
    }

    public String getURL()
    {
        if(type == 2)
            return URLFactory.createURLFavoriteBusinessSave(countryCode, companyID, uid, saveName, apiVersion);
        else
            return URLFactory.createURLFavoriteLocationSave(countryCode, placeID, addressID, uid, saveName, apiVersion);
    }

    public String addressID;
    public String companyID;
    public String placeID;
    public String saveName;
    public int type;
    public String uid;
}
