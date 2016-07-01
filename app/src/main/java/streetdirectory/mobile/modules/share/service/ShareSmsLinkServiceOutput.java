

package streetdirectory.mobile.modules.share.service;

import java.util.HashMap;
import streetdirectory.mobile.service.SDDataOutput;

public class ShareSmsLinkServiceOutput extends SDDataOutput
{

    public ShareSmsLinkServiceOutput()
    {
    }

    public void populateData()
    {
        super.populateData();
        url = (String)hashData.get("ur;");
    }

    private static final long serialVersionUID = 0x2bdc059754500a7cL;
    public String url;
}
