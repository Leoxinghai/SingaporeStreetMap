

package streetdirectory.mobile.modules.favorite.service;

import java.util.HashMap;

// Referenced classes of package streetdirectory.mobile.modules.favorite.service:
//            FavoriteListServiceOutput

public class FavoriteNearbyListServiceOutput extends FavoriteListServiceOutput
{

    public FavoriteNearbyListServiceOutput()
    {
    }

    public void populateData()
    {
        super.populateData();
        try
        {
            distance = Double.parseDouble((String)hashData.get("d"));
            return;
        }
        catch(Exception exception)
        {
            distance = 0.0D;
        }
    }

    private static final long serialVersionUID = 0x84b1320fc7b01d53L;
    public double distance;
}
