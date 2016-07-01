

package streetdirectory.mobile.modules.tips.reply.service;

import java.util.HashMap;
import streetdirectory.mobile.service.SDDataOutput;

public class PostReplyTipsServiceOutput extends SDDataOutput
{

    public PostReplyTipsServiceOutput()
    {
    }

    public void populateData()
    {
        super.populateData();
        result = "1".equals(hashData.get("data"));
    }

    private static final long serialVersionUID = 0x1263518959b30f51L;
    public boolean result;
}
