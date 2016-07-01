

package streetdirectory.mobile.modules.locationdetail.bus.service;

import java.util.HashMap;
import streetdirectory.mobile.service.SDDataOutput;

public class BusArrivalServiceOutputV2 extends SDDataOutput
{

    public BusArrivalServiceOutputV2()
    {
    }

    public void populateData()
    {
        populateData();
        nextBus = (String)hashData.get("nb");
        subsequentBus = (String)hashData.get("sb");
        try
        {
            nextBusLoad = Integer.parseInt((String)hashData.get("nbl_sd"));
        }
        catch(Exception exception)
        {
            nextBusLoad = 0;
        }
        try
        {
            nextBusFeature = Integer.parseInt((String)hashData.get("nbf_sd"));
        }
        catch(Exception exception1)
        {
            nextBusFeature = 0;
        }
        try
        {
            subsequentBusLoad = Integer.parseInt((String)hashData.get("sbl_sd"));
        }
        catch(Exception exception2)
        {
            subsequentBusLoad = 0;
        }
        try
        {
            subsequentBusFeature = Integer.parseInt((String)hashData.get("sbf_sd"));
            return;
        }
        catch(Exception exception3)
        {
            subsequentBusFeature = 0;
        }
    }

    private static final long serialVersionUID = 0x1379e5436dfde7b3L;
    public String busNumber;
    public String nextBus;
    public int nextBusFeature;
    public int nextBusLoad;
    public String subsequentBus;
    public int subsequentBusFeature;
    public int subsequentBusLoad;
}
