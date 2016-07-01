

package streetdirectory.mobile.modules.tips.service;

import java.util.HashMap;
import streetdirectory.mobile.service.SDDataOutput;

public class LocationBusinessFBLinkServiceOutput extends SDDataOutput
{

    public LocationBusinessFBLinkServiceOutput()
    {
    }

    public void populateData()
    {
        super.populateData();
        facebookLink = (String)hashData.get("data");
    }

    private static final long serialVersionUID = 0x802c6a64c67faac2L;
    public String facebookLink;
}
