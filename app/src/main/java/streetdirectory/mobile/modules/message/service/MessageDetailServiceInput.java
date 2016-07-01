

package streetdirectory.mobile.modules.message.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class MessageDetailServiceInput extends SDHttpServiceInput
{

    public MessageDetailServiceInput()
    {
    }

    public MessageDetailServiceInput(String s, String s1, String s2, String s3, String s4)
    {
        super(s);
        uid = s1;
        messageID = s2;
        messageType = s3;
        att = s4;
    }

    public String getURL()
    {
        return URLFactory.createURLMessageDetail(countryCode, uid, messageID, messageType, att);
    }

    public String att;
    public String messageID;
    public String messageType;
    public String uid;
}
