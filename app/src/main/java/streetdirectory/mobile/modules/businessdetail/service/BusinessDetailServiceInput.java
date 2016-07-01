

package streetdirectory.mobile.modules.businessdetail.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class BusinessDetailServiceInput extends SDHttpServiceInput
{

    public BusinessDetailServiceInput(String s, String s1, int i, int j, String s2)
    {
        super(s);
        companyID = i;
        locationID = j;
        sdUid = s1;
        offerId = s2;
    }

    public String getURL()
    {
        if(offerId != null && offerId.trim().length() > 0)
            return URLFactory.createURLBusinessDetail(countryCode, sdUid, companyID, locationID, apiVersion, offerId);
        else
            return URLFactory.createURLBusinessDetail(countryCode, sdUid, companyID, locationID, apiVersion);
    }

    public int companyID;
    public int locationID;
    public String offerId;
    public String sdUid;
}
