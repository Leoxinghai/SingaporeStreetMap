

package streetdirectory.mobile.modules.tips.reply.service;

import java.util.HashMap;
import streetdirectory.mobile.modules.tips.service.TipsServiceOutput;

public class TipsReplyServiceOutput extends TipsServiceOutput
{

    public TipsReplyServiceOutput()
    {
    }

    public void populateData()
    {
        super.populateData();
        comment = (String)hashData.get("tx");
        dateTime = (String)hashData.get("tm_org");
    }

    private static final long serialVersionUID = 0xbc3b0d468f64bd58L;
}
