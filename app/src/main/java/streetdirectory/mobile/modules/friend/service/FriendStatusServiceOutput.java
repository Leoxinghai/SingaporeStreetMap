

package streetdirectory.mobile.modules.friend.service;

import java.util.HashMap;
import streetdirectory.mobile.service.SDDataOutput;

public class FriendStatusServiceOutput extends SDDataOutput
{

    public FriendStatusServiceOutput()
    {
    }

    public void populateData()
    {
        super.populateData();
        try
        {
            friendStatus = Integer.parseInt((String)hashData.get("data"));
            return;
        }
        catch(Exception exception)
        {
            friendStatus = 0;
        }
    }

    public static final int STATUS_FRIEND = 1;
    public static final int STATUS_NOT_FRIEND = 0;
    public static final int STATUS_REQUEST_SENT = 2;
    public static final int STATUS_WAITING_APPROVAL = 3;
    private static final long serialVersionUID = 0xcaebc984120cca31L;
    public int friendStatus;
}
