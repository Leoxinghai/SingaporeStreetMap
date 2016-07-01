

package streetdirectory.mobile.modules.location.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class CurrentLocationServiceInput extends SDHttpServiceInput
{

    public CurrentLocationServiceInput()
    {
    }

    public CurrentLocationServiceInput(double d, double d1)
    {
        longitude = d;
        latitude = d1;
    }

    public String getURL()
    {
        return URLFactory.createURLCurrentLocation(longitude, latitude, apiVersion);
    }

    public double latitude;
    public double longitude;
}
