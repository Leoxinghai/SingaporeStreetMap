

package streetdirectory.mobile.modules.locationdetail.businessin.service;

import java.util.HashMap;
import streetdirectory.mobile.service.SDDataOutput;

public class BusinessInCategoryServiceOutput extends SDDataOutput
{

    public BusinessInCategoryServiceOutput()
    {
    }

    public void populateData()
    {
        populateData();
        categoryID = (String)hashData.get("cat");
        categoryName = (String)hashData.get("cn");
        try
        {
            totalBusiness = Integer.parseInt((String)hashData.get("tbiz"));
            return;
        }
        catch(Exception exception)
        {
            totalBusiness = 0;
        }
    }

    private static final long serialVersionUID = 0x44d08f7242dd5a4aL;
    public String categoryID;
    public String categoryName;
    public int totalBusiness;
}
