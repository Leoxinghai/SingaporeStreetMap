

package streetdirectory.mobile.modules.profile.service;

import java.util.HashMap;

// Referenced classes of package streetdirectory.mobile.modules.profile.service:
//            UserInfoServiceOutput

public class UserInfoCountryServiceOutput extends UserInfoServiceOutput
{

    public UserInfoCountryServiceOutput()
    {
    }

    public void populateData()
    {
        super.populateData();
        countryUserCode = (String)hashData.get("country");
        countryName = (String)hashData.get("country_nm");
        try
        {
            totalLikeBusiness = Integer.parseInt((String)hashData.get("tlikebiz"));
        }
        catch(Exception exception)
        {
            totalLikeBusiness = 0;
        }
        try
        {
            totalFavoritePlace = Integer.parseInt((String)hashData.get("tlikelan"));
        }
        catch(Exception exception1)
        {
            totalFavoritePlace = 0;
        }
        try
        {
            totalTips = Integer.parseInt((String)hashData.get("ttips"));
        }
        catch(Exception exception2)
        {
            totalTips = 0;
        }
        try
        {
            totalFavoriteRoute = Integer.parseInt((String)hashData.get("troute"));
            return;
        }
        catch(Exception exception3)
        {
            totalFavoriteRoute = 0;
        }
    }

    private static final long serialVersionUID = 0x64d3f46038cfe3a1L;
    public String countryName;
    public String countryUserCode;
    public int totalFavoritePlace;
    public int totalFavoriteRoute;
    public int totalLikeBusiness;
    public int totalTips;
}
