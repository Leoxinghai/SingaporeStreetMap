

package streetdirectory.mobile.modules.locationdetail.erp.service;

import java.util.ArrayList;

// Referenced classes of package streetdirectory.mobile.modules.locationdetail.erp.service:
//            ERPTimeRateInfo

public class ERPVehicleInfo
{

    public ERPVehicleInfo()
    {
    }

    public ERPVehicleInfo(String s)
    {
        vehicleName = s;
    }

    public void addNewWeekdaysInfo(ERPTimeRateInfo erptimerateinfo)
    {
        if(erptimerateinfo != null)
            arrayOfWeekdaysInfo.add(erptimerateinfo);
    }

    public void addNewWeekendInfo(ERPTimeRateInfo erptimerateinfo)
    {
        if(erptimerateinfo != null)
            arrayOfWeekendInfo.add(erptimerateinfo);
    }

    public ArrayList arrayOfWeekdaysInfo;
    public ArrayList arrayOfWeekendInfo;
    public String vehicleName;
}
