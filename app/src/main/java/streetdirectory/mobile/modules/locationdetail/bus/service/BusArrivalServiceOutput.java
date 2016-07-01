

package streetdirectory.mobile.modules.locationdetail.bus.service;

import java.util.HashMap;
import streetdirectory.mobile.service.SDDataOutput;

public class BusArrivalServiceOutput extends SDDataOutput
{

    public BusArrivalServiceOutput()
    {
        nextBus = -2;
        subsequentBus = -2;
    }

    public void populateData()
    {
        populateData();
        busNumber = (String)hashData.get("service_no");
        try
        {
            nextBus = Integer.parseInt((String)hashData.get("nextbus"));
        }
        catch(Exception exception)
        {
            nextBus = -1;
        }
        try
        {
            subsequentBus = Integer.parseInt((String)hashData.get("subsequentbus"));
            return;
        }
        catch(Exception exception1)
        {
            subsequentBus = -1;
        }
    }

    private static final long serialVersionUID = 0xd617d2dc8698c85cL;
    public String busNumber;
    public int nextBus;
    public int subsequentBus;
}
