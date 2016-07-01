

package streetdirectory.mobile.modules.locationdetail.trafficcam.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class TrafficCameraImageLinkServiceInput extends SDHttpServiceInput
{

    public TrafficCameraImageLinkServiceInput()
    {
    }

    public TrafficCameraImageLinkServiceInput(String s, int i, int j)
    {
        super(s);
        placeID = i;
        addressID = j;
    }

    public String getURL()
    {
        return URLFactory.createURLTrafficCameraImageLink(countryCode, placeID, addressID, apiVersion);
    }

    public int addressID;
    public int placeID;
}
