

package streetdirectory.mobile.modules.locationdetail.bus.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class BusArrivalServiceInputV2 extends SDHttpServiceInput
{

    public BusArrivalServiceInputV2()
    {
    }

    public BusArrivalServiceInputV2(String s, String s1)
    {
        busStopID = s;
        busNumber = s1;
    }

    public String getURL()
    {
        return URLFactory.createURLBusArrival(busStopID, busNumber);
    }

    public String busNumber;
    public String busStopID;
}
