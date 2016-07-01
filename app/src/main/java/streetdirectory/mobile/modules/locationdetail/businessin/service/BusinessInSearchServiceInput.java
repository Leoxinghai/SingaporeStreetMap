

package streetdirectory.mobile.modules.locationdetail.businessin.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class BusinessInSearchServiceInput extends SDHttpServiceInput
{

    public BusinessInSearchServiceInput()
    {
    }

    public BusinessInSearchServiceInput(String s, int i, int j, int k, int l, String s1)
    {
        super(s);
        placeID = i;
        addressID = j;
        start = k;
        limit = l;
        searchString = s1;
    }

    public String getURL()
    {
        return URLFactory.createURLBusinessInList(countryCode, placeID, addressID, start, limit, searchString, apiVersion);
    }

    public int addressID;
    public int limit;
    public int placeID;
    public String searchString;
    public int start;
}
