

package streetdirectory.mobile.modules.locationdetail.businessin.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class BusinessInServiceInput extends SDHttpServiceInput
{

    public BusinessInServiceInput()
    {
    }

    public BusinessInServiceInput(String s, int i, int j, int k, int l, boolean flag)
    {
        super(s);
        placeID = i;
        addressID = j;
        start = k;
        limit = l;
        returnAboutUs = flag;
    }

    public String getURL()
    {
        return URLFactory.createURLBusinessInList(countryCode, placeID, addressID, start, limit, returnAboutUs, apiVersion);
    }

    public int addressID;
    public int limit;
    public int placeID;
    public boolean returnAboutUs;
    public int start;
}
