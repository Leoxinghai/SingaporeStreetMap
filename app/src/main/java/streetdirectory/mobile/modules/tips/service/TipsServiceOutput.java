

package streetdirectory.mobile.modules.tips.service;

import java.util.HashMap;
import streetdirectory.mobile.service.SDDataOutput;
import streetdirectory.mobile.service.URLFactory;

public class TipsServiceOutput extends SDDataOutput
{

    public TipsServiceOutput()
    {
    }

    public String generateUserPhotoURL(int i, int j)
    {
        Object obj = null;
        String s;
        if(imageURL != null && imageURL.length() > 2)
        {
            s = URLFactory.createURLResizeImage(imageURL, i, j);
        } else
        {
            s = null;
            if(uid != null)
            {
                s = null;
                if(uid.length() > 2)
                    return URLFactory.createURLFacebookPhoto(uid, i, j);
            }
        }
        return s;
    }

    public void populateData()
    {
        super.populateData();
        commentID = (String)hashData.get("com_id");
        uid = (String)hashData.get("uid");
        name = (String)hashData.get("nm");
        imageURL = (String)hashData.get("img");
        comment = (String)hashData.get("text");
        try
        {
            score = Integer.parseInt((String)hashData.get("scr"));
        }
        catch(Exception exception)
        {
            score = 0;
        }
        try
        {
            totalScore = Integer.parseInt((String)hashData.get("tscr"));
        }
        catch(Exception exception1)
        {
            totalScore = 0;
        }
        try
        {
            totalReplies = Integer.parseInt((String)hashData.get("trep"));
        }
        catch(Exception exception2)
        {
            totalReplies = 0;
        }
        dateTime = (String)hashData.get("time");
    }

    private static final long serialVersionUID = 0x7d581f5c03a1da07L;
    public String comment;
    public String commentID;
    public String dateTime;
    public String imageURL;
    public String name;
    public int score;
    public int totalReplies;
    public int totalScore;
    public String uid;
}
