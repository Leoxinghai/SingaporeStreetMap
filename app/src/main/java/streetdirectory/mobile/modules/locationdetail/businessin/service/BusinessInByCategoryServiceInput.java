

package streetdirectory.mobile.modules.locationdetail.businessin.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class BusinessInByCategoryServiceInput extends SDHttpServiceInput
{

    public BusinessInByCategoryServiceInput()
    {
    }

    public BusinessInByCategoryServiceInput(String s, int i, int j, int k, int l, boolean flag, int i1)
    {
        super(s);
        placeID = i;
        addressID = j;
        start = k;
        limit = l;
        returnAboutUs = flag;
        groupID = i1;
    }

    public String getURL()
    {
        return URLFactory.createURLBusinessInList(countryCode, placeID, addressID, groupID, start, limit, returnAboutUs, apiVersion);
    }

    public int addressID;
    public int groupID;
    public int limit;
    public int placeID;
    public boolean returnAboutUs;
    public int start;
}
