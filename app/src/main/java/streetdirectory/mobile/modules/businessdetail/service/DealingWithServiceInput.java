

package streetdirectory.mobile.modules.businessdetail.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class DealingWithServiceInput extends SDHttpServiceInput
{

    public DealingWithServiceInput()
    {
    }

    public DealingWithServiceInput(String s, int i, int j)
    {
        super(s);
        companyID = i;
        locationID = j;
    }

    public String getURL()
    {
        return URLFactory.createURLDealingWithList(countryCode, companyID, locationID, apiVersion);
    }

    public int companyID;
    public int locationID;
}
