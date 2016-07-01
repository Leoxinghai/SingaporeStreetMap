

package streetdirectory.mobile.modules.friend.service;

import java.util.HashMap;
import streetdirectory.mobile.service.SDDataOutput;
import streetdirectory.mobile.service.URLFactory;

public class FriendListServiceOutput extends SDDataOutput
{

    public FriendListServiceOutput()
    {
    }

    public String generateUserPhotoURL(int i, int j)
    {
        Object obj = null;
        String s = null;
        if(userID != null)
        {
            s = null;
            if(userID.length() > 2)
                s = URLFactory.createURLFacebookPhoto(userID, i, j);
        }
        return s;
    }

    public void populateData()
    {
        super.populateData();
        userID = (String)hashData.get("uid");
        userName = (String)hashData.get("name");
        userSex = (String)hashData.get("sex");
        userBirthday = (String)hashData.get("bd");
    }

    private static final long serialVersionUID = 0x39f04477cff35520L;
    public String userBirthday;
    public String userFacebookImageURL;
    public String userID;
    public String userName;
    public String userSex;
}
