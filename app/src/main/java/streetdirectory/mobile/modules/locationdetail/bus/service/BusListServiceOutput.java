

package streetdirectory.mobile.modules.locationdetail.bus.service;

import java.util.HashMap;
import streetdirectory.mobile.service.SDDataOutput;

public class BusListServiceOutput extends SDDataOutput
{

    public BusListServiceOutput()
    {
    }

    public void populateData()
    {
        populateData();
        busNumber = (String)hashData.get("b");
        try
        {
            busRoute = Integer.parseInt((String)hashData.get("d"));
            return;
        }
        catch(Exception exception)
        {
            busRoute = 0;
        }
    }

    private static final long serialVersionUID = 0x1c5c16da4d7300faL;
    public String busNumber;
    public int busRoute;
}
