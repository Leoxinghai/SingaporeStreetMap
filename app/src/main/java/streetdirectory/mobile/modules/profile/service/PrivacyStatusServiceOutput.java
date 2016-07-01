

package streetdirectory.mobile.modules.profile.service;

import java.util.HashMap;
import streetdirectory.mobile.service.SDDataOutput;

public class PrivacyStatusServiceOutput extends SDDataOutput
{

    public PrivacyStatusServiceOutput()
    {
    }

    public void populateData()
    {
        super.populateData();
        try
        {
            likeBusiness = Integer.parseInt((String)hashData.get("lkb"));
        }
        catch(Exception exception)
        {
            likeBusiness = 0;
        }
        try
        {
            favoritePlace = Integer.parseInt((String)hashData.get("lkl"));
        }
        catch(Exception exception1)
        {
            favoritePlace = 0;
        }
        try
        {
            favoriteRoute = Integer.parseInt((String)hashData.get("rot"));
        }
        catch(Exception exception2)
        {
            favoriteRoute = 0;
        }
        try
        {
            shouts = Integer.parseInt((String)hashData.get("sho"));
        }
        catch(Exception exception3)
        {
            shouts = 0;
        }
        try
        {
            friends = Integer.parseInt((String)hashData.get("frd"));
            return;
        }
        catch(Exception exception4)
        {
            friends = 0;
        }
    }

    public static final int STATUS_EVERYONE = 1;
    public static final int STATUS_FRIEND = 3;
    public static final int STATUS_ONLY_ME = 4;
    public static final int STATUS_SD_USER = 2;
    private static final long serialVersionUID = 0x17e7a739409f1220L;
    public int favoritePlace;
    public int favoriteRoute;
    public int friends;
    public int likeBusiness;
    public int shouts;
}
