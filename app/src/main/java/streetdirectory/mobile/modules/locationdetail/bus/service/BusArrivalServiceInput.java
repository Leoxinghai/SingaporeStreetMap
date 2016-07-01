

package streetdirectory.mobile.modules.locationdetail.bus.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class BusArrivalServiceInput extends SDHttpServiceInput
{

    public BusArrivalServiceInput()
    {
    }

    public BusArrivalServiceInput(String s, String s1)
    {
        busStopID = s;
        busNumber = s1;
    }

    public String getURL()
    {
        return URLFactory.createURLBusArrivalStatusEdit(busStopID, busNumber, apiVersion);
    }

    public String busNumber;
    public String busStopID;
}
