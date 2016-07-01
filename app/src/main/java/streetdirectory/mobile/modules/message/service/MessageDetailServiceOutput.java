

package streetdirectory.mobile.modules.message.service;

import java.util.HashMap;
import streetdirectory.mobile.service.SDDataOutput;

public class MessageDetailServiceOutput extends SDDataOutput
{

    public MessageDetailServiceOutput()
    {
    }

    public void populateData()
    {
        super.populateData();
        message = (String)hashData.get("msg");
    }

    private static final long serialVersionUID = 0x6b94108616c022f7L;
    public String message;
}
