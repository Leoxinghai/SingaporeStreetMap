

package streetdirectory.mobile.modules.photopreview.service;

import streetdirectory.mobile.service.URLFactory;

// Referenced classes of package streetdirectory.mobile.modules.photopreview.service:
//            ImageListServiceInput

public class BusinessImageListServiceInput extends ImageListServiceInput
{

    public BusinessImageListServiceInput()
    {
    }

    public BusinessImageListServiceInput(String s, int i, int j)
    {
        super(s);
        companyID = i;
        locationID = j;
    }

    public String getURL()
    {
        return URLFactory.createURLBusinessImageList(countryCode, companyID, locationID, apiVersion);
    }

    public int companyID;
    public int locationID;
}
