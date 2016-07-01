

package streetdirectory.mobile.modules.businesslisting.service;

import android.content.Context;
import streetdirectory.mobile.modules.core.LocationBusinessTipsServiceOutput;
import streetdirectory.mobile.service.URLFactory;

public class BusinessListingServiceOutput extends LocationBusinessTipsServiceOutput
{

    public BusinessListingServiceOutput()
    {
    }

    public String generateImageURL(Context context, int i, int j)
    {
        String temp = null;
        if(offer != null && offerThumbnailImage != null)
            return URLFactory.createURLResizeImage(offerThumbnailImage, 384, 384, 1, 40);
        if(siteBanner == null || siteBanner.length() <= 2) {
            if(imageURL != null)
                temp = URLFactory.createURLResizeImage(imageURL, i, j);
        } else {
            temp = URLFactory.createURLSiteBanner(countryCode, siteBanner, i, j);
        }

        return temp;
    }

    private static final long serialVersionUID = 0xcc9689266e5843dfL;
}
