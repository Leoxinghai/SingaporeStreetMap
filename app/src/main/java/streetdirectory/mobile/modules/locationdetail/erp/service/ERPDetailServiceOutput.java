

package streetdirectory.mobile.modules.locationdetail.erp.service;

import java.util.ArrayList;
import java.util.HashMap;
import streetdirectory.mobile.service.SDDataOutput;

// Referenced classes of package streetdirectory.mobile.modules.locationdetail.erp.service:
//            ERPVehicleInfo

public class ERPDetailServiceOutput extends SDDataOutput
{

    public ERPDetailServiceOutput()
    {
    }

    public void addNewVehicleInfo(ERPVehicleInfo erpvehicleinfo)
    {
        if(erpvehicleinfo != null)
            arrayOfVehicleInfo.add(erpvehicleinfo);
    }

    public void populateData()
    {
        populateData();
        zone = (String)hashData.get("zone");
        desc = (String)hashData.get("desc");
        id = (String)hashData.get("id");
        try
        {
            longitude = Double.parseDouble((String)hashData.get("x"));
        }
        catch(Exception exception)
        {
            longitude = 0.0D;
        }
        try
        {
            latitude = Double.parseDouble((String)hashData.get("y"));
            return;
        }
        catch(Exception exception1)
        {
            latitude = 0.0D;
        }
    }

    private static final long serialVersionUID = 0x9fe2059fcb3f9463L;
    public ArrayList arrayOfVehicleInfo;
    public String desc;
    public String id;
    public double latitude;
    public double longitude;
    public String zone;
}
