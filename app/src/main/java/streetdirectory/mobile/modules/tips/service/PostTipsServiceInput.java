

package streetdirectory.mobile.modules.tips.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class PostTipsServiceInput extends SDHttpServiceInput
{

    public PostTipsServiceInput()
    {
    }

    public PostTipsServiceInput(String s, String s1, String s2, String s3, String s4, int i)
    {
        super(s);
        dataType = i;
        dataID = s1;
        message = s2;
        uid = s3;
        imageURL = s4;
    }

    public String getURL()
    {
        return URLFactory.createURLPostTips(imageURL, dataID, message, uid, imageURL, dataType, apiVersion);
    }

    public String dataID;
    public int dataType;
    public String imageURL;
    public String message;
    public String uid;
}
