

package streetdirectory.mobile.modules.message.service;

import streetdirectory.mobile.service.SDHttpService;

// Referenced classes of package streetdirectory.mobile.modules.message.service:
//            MessageDetailServiceOutput, MessageDetailServiceInput

public class MessageDetailService extends SDHttpService
{

    public MessageDetailService(MessageDetailServiceInput messagedetailserviceinput)
    {
        super(messagedetailserviceinput, MessageDetailServiceOutput.class);
    }
}
