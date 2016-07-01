

package streetdirectory.mobile.modules.tips.reply.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class PostReplyTipsServiceInput extends SDHttpServiceInput
{

    public PostReplyTipsServiceInput()
    {
    }

    public PostReplyTipsServiceInput(String s, String s1, String s2, String s3, int i, String s4)
    {
        super(s);
        dataType = i;
        dataID = s1;
        message = s2;
        uid = s3;
        commentID = s4;
    }

    public String getURL()
    {
        return URLFactory.createURLPostReplyTips(countryCode, dataID, dataType, message, commentID, uid, apiVersion);
    }

    public String commentID;
    public String dataID;
    public int dataType;
    public String message;
    public String uid;
}
