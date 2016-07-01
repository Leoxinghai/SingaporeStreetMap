

package streetdirectory.mobile.modules.locationdetail.erp.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class ERPDetailServiceInput extends SDHttpServiceInput
{

    public ERPDetailServiceInput()
    {
    }

    public ERPDetailServiceInput(String s, String s1)
    {
        erpID = s;
        zone = s1;
    }

    public String getURL()
    {
        return URLFactory.createURLERP(erpID, zone);
    }

    public String erpID;
    public String zone;
}
