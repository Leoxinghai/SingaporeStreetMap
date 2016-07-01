

package streetdirectory.mobile.modules.settings.addbusiness.service;

import java.util.HashMap;
import streetdirectory.mobile.service.SDDataOutput;

public class AddNewPlaceServiceOutput extends SDDataOutput
{

    public AddNewPlaceServiceOutput()
    {
    }

    public void populateData()
    {
        super.populateData();
        result = "1".equals(hashData.get("data"));
    }

    private static final long serialVersionUID = 0x9331033140e7f0acL;
    public boolean result;
}
