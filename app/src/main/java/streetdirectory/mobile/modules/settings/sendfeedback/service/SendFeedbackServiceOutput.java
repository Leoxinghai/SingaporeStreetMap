

package streetdirectory.mobile.modules.settings.sendfeedback.service;

import java.util.HashMap;
import streetdirectory.mobile.service.SDDataOutput;

public class SendFeedbackServiceOutput extends SDDataOutput
{

    public SendFeedbackServiceOutput()
    {
    }

    public void populateData()
    {
        super.populateData();
        result = "1".equals(hashData.get("data"));
    }

    private static final long serialVersionUID = 0x96021dbf687b7bbcL;
    public boolean result;
}
