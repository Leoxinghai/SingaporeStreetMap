

package streetdirectory.mobile.modules.favorite.service;

import java.util.HashMap;
import streetdirectory.mobile.modules.tips.service.TipsServiceOutput;
import streetdirectory.mobile.service.URLFactory;

public class FavoriteTipsUserServiceOutput extends TipsServiceOutput
{

    public FavoriteTipsUserServiceOutput()
    {
    }

    public void populateData()
    {
        super.populateData();
        comment = (String)hashData.get("tx");
        dateTime = (String)hashData.get("tm_org");
        try
        {
            longitude = Double.parseDouble((String)hashData.get("x"));
            latitude = Double.parseDouble((String)hashData.get("y"));
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        if(imageURL != null)
            favoriteImageURL = URLFactory.createURLResizeImage(imageURL, 98, 70);
        else
        if(longitude != 0.0D && latitude != 0.0D)
        {
            favoriteImageURL = URLFactory.createURLMapImage(longitude, latitude, 180, 140, 12);
            return;
        }
    }

    public String favoriteImageURL;
    public double latitude;
    public double longitude;
    public String time;
    public int type;
    public String uniqueID;
}
