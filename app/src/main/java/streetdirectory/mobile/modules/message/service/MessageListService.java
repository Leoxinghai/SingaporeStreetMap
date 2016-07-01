

package streetdirectory.mobile.modules.message.service;

import streetdirectory.mobile.service.SDHttpService;

// Referenced classes of package streetdirectory.mobile.modules.message.service:
//            MessageListServiceOutput, MessageListServiceInput

public class MessageListService extends SDHttpService
{

    public MessageListService(MessageListServiceInput messagelistserviceinput)
    {
        super(messagelistserviceinput, MessageListServiceOutput.class);
    }
}
