

package streetdirectory.mobile.modules.favorite.service;

import java.util.HashMap;
import streetdirectory.mobile.service.SDDataOutput;

public class FavoriteSaveServiceOutput extends SDDataOutput
{

    public FavoriteSaveServiceOutput()
    {
    }

    public void populateData()
    {
        super.populateData();
        result = "1".equals(hashData.get("data"));
    }

    private static final long serialVersionUID = 0x84b1320fc7b01d53L;
    public boolean result;
}
