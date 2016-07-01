

package streetdirectory.mobile.modules.businesslisting.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class BusinessListingServiceInput extends SDHttpServiceInput
{

    public BusinessListingServiceInput()
    {
    }

    public BusinessListingServiceInput(String s, int i, int j, int k, int l, int i1)
    {
        super(s);
        dataType = i;
        categoryID = j;
        stateID = k;
        start = l;
        limit = i1;
    }

    public String getURL()
    {
        return URLFactory.createURLLocationBusinessListingList(countryCode, dataType, categoryID, start, limit, stateID, apiVersion);
    }

    public int categoryID;
    public int dataType;
    public int limit;
    public int start;
    public int stateID;
}
