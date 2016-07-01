

package streetdirectory.mobile.modules.profile.service;

import java.util.HashMap;

// Referenced classes of package streetdirectory.mobile.modules.profile.service:
//            UserInfoServiceOutput

public class UserInfoGeneralServiceOutput extends UserInfoServiceOutput
{

    public UserInfoGeneralServiceOutput()
    {
    }

    public void populateData()
    {
        super.populateData();
        name = (String)hashData.get("name");
        sex = (String)hashData.get("sex");
        birthday = (String)hashData.get("bday");
        try
        {
            totalNewMessage = Integer.parseInt((String)hashData.get("tnewmsg"));
        }
        catch(Exception exception)
        {
            totalNewMessage = 0;
        }
        try
        {
            totalFriends = Integer.parseInt((String)hashData.get("tfriends"));
            return;
        }
        catch(Exception exception1)
        {
            totalFriends = 0;
        }
    }

    private static final long serialVersionUID = 0xd0aa72ecf98e8a84L;
    public String birthday;
    public String name;
    public String sex;
    public int totalFriends;
    public int totalNewMessage;
}
