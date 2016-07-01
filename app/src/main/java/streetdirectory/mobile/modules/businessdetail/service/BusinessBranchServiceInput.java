

package streetdirectory.mobile.modules.businessdetail.service;

import streetdirectory.mobile.sd.SDBlackboard;
import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class BusinessBranchServiceInput extends SDHttpServiceInput
{

    public BusinessBranchServiceInput()
    {
    }

    public BusinessBranchServiceInput(String s, int i, int j, int k, String s1)
    {
        super(s);
        companyID = i;
        start = j;
        limit = k;
        promoId = s1;
    }

    public String getURL()
    {
        return URLFactory.createURLBusinessBranchList(countryCode, companyID, start, limit, apiVersion, SDBlackboard.currentLongitude, SDBlackboard.currentLatitude, promoId);
    }

    public int companyID;
    public int limit;
    public String promoId;
    public int start;
}
