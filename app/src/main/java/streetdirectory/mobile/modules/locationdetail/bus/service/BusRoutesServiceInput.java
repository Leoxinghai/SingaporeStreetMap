

package streetdirectory.mobile.modules.locationdetail.bus.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class BusRoutesServiceInput extends SDHttpServiceInput
{

    public BusRoutesServiceInput()
    {
    }

    public BusRoutesServiceInput(String s)
    {
        busNumber = s;
    }

    public String getURL()
    {
        return URLFactory.createURLBusRoutes(countryCode, busNumber);
    }

    public String busNumber;
}
