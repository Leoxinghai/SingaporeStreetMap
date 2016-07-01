

package streetdirectory.mobile.modules.tips.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class LocationBusinessFBLinkServiceInput extends SDHttpServiceInput
{

    public LocationBusinessFBLinkServiceInput(String s, int i, int j, int k, int l)
    {
        super(s);
        companyPlaceID = j;
        locationAddressID = k;
        stateID = l;
    }

    public String getURL()
    {
        if(type == 2)
            return URLFactory.createURLBusinessFBLink(countryCode, stateID, companyPlaceID, locationAddressID, apiVersion);
        else
            return URLFactory.createURLLocationFBLink(countryCode, stateID, companyPlaceID, locationAddressID, apiVersion);
    }

    public int companyPlaceID;
    public int locationAddressID;
    public int stateID;
    public int type;
}
