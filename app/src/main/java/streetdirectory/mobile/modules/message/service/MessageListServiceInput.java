

package streetdirectory.mobile.modules.message.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class MessageListServiceInput extends SDHttpServiceInput
{

    public MessageListServiceInput()
    {
    }

    public MessageListServiceInput(String s, String s1, String s2, String s3, String s4, int i, int j)
    {
        super(s);
        uid = s1;
        messageType = s2;
        greaterThan = s3;
        lessThan = s4;
        start = i;
        limit = j;
    }

    public String getURL()
    {
        return URLFactory.createURLUserMessageHeader(countryCode, uid, messageType, greaterThan, lessThan, start, limit);
    }

    public String greaterThan;
    public String lessThan;
    public int limit;
    public String messageType;
    public int start;
    public String uid;
}
