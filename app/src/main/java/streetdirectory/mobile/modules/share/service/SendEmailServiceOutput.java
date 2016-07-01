

package streetdirectory.mobile.modules.share.service;

import java.util.HashMap;
import streetdirectory.mobile.service.SDDataOutput;

public class SendEmailServiceOutput extends SDDataOutput
{

    public SendEmailServiceOutput()
    {
    }

    public void populateData()
    {
        super.populateData();
        result = "1".equals(hashData.get("data"));
    }

    private static final long serialVersionUID = 0xc8e07be324285f50L;
    public boolean result;
}
