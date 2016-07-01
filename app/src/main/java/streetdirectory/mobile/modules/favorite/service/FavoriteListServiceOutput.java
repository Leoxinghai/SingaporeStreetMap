

package streetdirectory.mobile.modules.favorite.service;

import java.util.HashMap;
import streetdirectory.mobile.modules.core.LocationBusinessTipsServiceOutput;
import streetdirectory.mobile.service.URLFactory;

public class FavoriteListServiceOutput extends LocationBusinessTipsServiceOutput
{

    public FavoriteListServiceOutput()
    {
    }

    public void populateData()
    {
        super.populateData();
        saveName = (String)hashData.get("svName");
        if(imageURL != null)
            favoriteImageURL = URLFactory.createURLResizeImage(imageURL, 98, 70);
        if(siteBanner != null)
            siteBannerURL = URLFactory.createURLSiteBanner(countryCode, siteBanner, 188, 158);
    }

    private static final long serialVersionUID = 0x6800df4c4e673947L;
    public String favoriteImageURL;
    public String saveName;
    public String siteBannerURL;
}
