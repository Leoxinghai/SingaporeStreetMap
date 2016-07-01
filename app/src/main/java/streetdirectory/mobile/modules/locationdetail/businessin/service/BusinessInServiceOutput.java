

package streetdirectory.mobile.modules.locationdetail.businessin.service;

import android.content.Context;
import java.util.HashMap;
import streetdirectory.mobile.modules.core.LocationBusinessTipsServiceOutput;
import streetdirectory.mobile.service.URLFactory;

public class BusinessInServiceOutput extends LocationBusinessTipsServiceOutput
{

    public BusinessInServiceOutput()
    {
    }

    public BusinessInServiceOutput(HashMap hashmap)
    {

        super(hashmap);
    }

    public String generateImageURL(Context context, int i, int j)
    {
        String temp = null;
        if(offer != null && offerThumbnailImage != null)
            return URLFactory.createURLResizeImage(offerThumbnailImage, 384, 384, 1, 40);
        if(siteBanner == null || siteBanner.length() <= 2) {
            if(imageURL != null)
                temp = URLFactory.createURLResizeImage(imageURL, i, j);
        } else
            temp = URLFactory.createURLSiteBanner(countryCode, siteBanner, i, j);

        return temp;
    }

    public static final streetdirectory.mobile.service.SDOutput.Creator CREATOR = new Creator(BusinessInServiceOutput.class);
    private static final long serialVersionUID = 0x608faea2d4627229L;

}
