

package streetdirectory.mobile.modules.tips.reply.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class TipsReplyServiceInput extends SDHttpServiceInput
{

    public TipsReplyServiceInput()
    {
    }

    public TipsReplyServiceInput(String s, String s1, int i, int j)
    {
        super(s);
        commentID = s1;
        start = i;
        limit = j;
    }

    public String getURL()
    {
        return URLFactory.createURLTipsReplyList(countryCode, commentID, start, limit, apiVersion);
    }

    public String commentID;
    public int limit;
    public int start;
}
