

package streetdirectory.mobile.gis.gps;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class NearbyLocServiceInput extends SDHttpServiceInput
{

    public NearbyLocServiceInput()
    {
    }

    public NearbyLocServiceInput(double d, double d1)
    {
        longitude = d;
        latitude = d1;
    }

    public String getURL()
    {
        return URLFactory.createURLGetNearbyLoc(longitude, latitude, apiVersion);
    }

    public double latitude;
    public double longitude;
}
