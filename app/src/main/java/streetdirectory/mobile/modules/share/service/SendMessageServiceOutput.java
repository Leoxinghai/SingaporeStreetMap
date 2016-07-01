

package streetdirectory.mobile.modules.share.service;

import java.util.HashMap;
import streetdirectory.mobile.service.SDDataOutput;

public class SendMessageServiceOutput extends SDDataOutput
{

    public SendMessageServiceOutput()
    {
    }

    public void populateData()
    {
        super.populateData();
        result = "1".equals(hashData.get("data"));
    }

    private static final long serialVersionUID = 0x163ef12fb43a38d2L;
    public boolean result;
}
