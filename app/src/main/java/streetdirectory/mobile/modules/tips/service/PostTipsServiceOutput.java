

package streetdirectory.mobile.modules.tips.service;

import java.util.HashMap;
import streetdirectory.mobile.service.SDDataOutput;

public class PostTipsServiceOutput extends SDDataOutput
{

    public PostTipsServiceOutput()
    {
    }

    public void populateData()
    {
        super.populateData();
        result = "1".equals(hashData.get("data"));
    }

    private static final long serialVersionUID = 0xd8a87d65e250c8cfL;
    public boolean result;
}
