

package streetdirectory.mobile.modules.favorite.service;

import java.util.HashMap;
import streetdirectory.mobile.service.SDDataOutput;

public class FavoriteDeleteServiceOutput extends SDDataOutput
{

    public FavoriteDeleteServiceOutput()
    {
    }

    public void populateData()
    {
        super.populateData();
        result = "1".equals(hashData.get("data"));
    }

    private static final long serialVersionUID = 0xaa3f1f4a3b3a5496L;
    public boolean result;
}
