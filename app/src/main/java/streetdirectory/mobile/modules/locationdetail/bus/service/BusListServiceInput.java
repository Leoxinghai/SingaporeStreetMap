

package streetdirectory.mobile.modules.locationdetail.bus.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class BusListServiceInput extends SDHttpServiceInput
{

    public BusListServiceInput()
    {
    }

    public BusListServiceInput(String s)
    {
        busStopID = s;
    }

    public String getURL()
    {
        return URLFactory.createURLBusListStringEdit(busStopID, apiVersion);
    }

    public String busStopID;
}
