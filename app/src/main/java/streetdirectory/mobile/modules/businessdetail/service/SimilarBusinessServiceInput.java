

package streetdirectory.mobile.modules.businessdetail.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class SimilarBusinessServiceInput extends SDHttpServiceInput
{

    public SimilarBusinessServiceInput()
    {
    }

    public SimilarBusinessServiceInput(String s, int i, int j, int k)
    {
        super(s);
        stateID = i;
        companyID = j;
        limit = k;
    }

    public String getURL()
    {
        return URLFactory.createURLSimilarBusinessList(countryCode, stateID, companyID, limit, apiVersion);
    }

    public int companyID;
    public int limit;
    public int stateID;
}
