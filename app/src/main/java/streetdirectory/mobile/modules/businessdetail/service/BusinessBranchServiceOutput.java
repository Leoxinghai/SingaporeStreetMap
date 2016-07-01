

package streetdirectory.mobile.modules.businessdetail.service;

import java.util.HashMap;
import streetdirectory.mobile.modules.core.LocationBusinessServiceOutput;

public class BusinessBranchServiceOutput extends LocationBusinessServiceOutput
{

    public BusinessBranchServiceOutput()
    {
    }

    public void populateData()
    {
        super.populateData();
        branchName = (String)hashData.get("bn");
        distance = (String)hashData.get("dist");
        isOfferAvailable = ((String)hashData.get("oa")).equals("1");
    }

    private static final long serialVersionUID = 0xa8a2d3e5c5be2d57L;
    public String branchName;
    public String distance;
    public boolean isOfferAvailable;
}
