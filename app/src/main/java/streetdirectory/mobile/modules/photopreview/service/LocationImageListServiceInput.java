

package streetdirectory.mobile.modules.photopreview.service;

import streetdirectory.mobile.service.URLFactory;

// Referenced classes of package streetdirectory.mobile.modules.photopreview.service:
//            ImageListServiceInput

public class LocationImageListServiceInput extends ImageListServiceInput
{

    public LocationImageListServiceInput()
    {
    }

    public LocationImageListServiceInput(String s, int i, int j)
    {
        super(s);
        placeID = i;
        addressID = j;
    }

    public String getURL()
    {
        return URLFactory.createURLLocationImageList(countryCode, placeID, addressID, apiVersion);
    }

    public int addressID;
    public int placeID;
}
