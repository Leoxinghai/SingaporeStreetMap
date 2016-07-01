

package streetdirectory.mobile.modules.favorite.service;

import java.util.HashMap;
import streetdirectory.mobile.service.SDDataOutput;

public class FavoriteStatusServiceOutput extends SDDataOutput
{

    public FavoriteStatusServiceOutput()
    {
    }

    public void populateData()
    {
        super.populateData();
        result = "1".equals(hashData.get("data"));
    }

    private static final long serialVersionUID = 0xc4793b7d2d6af19L;
    public boolean result;
}
