

package streetdirectory.mobile.modules.locationdetail.businessin.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class BusinessInCategoryServiceInput extends SDHttpServiceInput
{

    public BusinessInCategoryServiceInput()
    {
    }

    public BusinessInCategoryServiceInput(String s, int i, int j, int k, int l)
    {
        super(s);
        placeID = i;
        addressID = j;
        start = k;
        limit = l;
    }

    public String getURL()
    {
        return URLFactory.createURLBusinessInCategoryList(countryCode, placeID, addressID, start, limit, apiVersion);
    }

    public int addressID;
    public int limit;
    public int placeID;
    public int start;
}
