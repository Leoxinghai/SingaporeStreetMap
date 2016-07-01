

package streetdirectory.mobile.service.apiversion;

import java.util.HashMap;
import streetdirectory.mobile.service.SDDataOutput;

public class APIVersionServiceOutput extends SDDataOutput
{

    public APIVersionServiceOutput()
    {
    }

    public void populateData()
    {
        super.populateData();
        try
        {
            mapVersion = (String)hashData.get("map");
        }
        catch(Exception exception)
        {
            mapVersion = "";
        }
        try
        {
            apiVersion = Integer.parseInt((String)hashData.get("ver"));
        }
        catch(Exception exception1)
        {
            apiVersion = 0;
        }
        try
        {
            categoryVersion = Integer.parseInt((String)hashData.get("vercat"));
        }
        catch(Exception exception2)
        {
            categoryVersion = 0;
        }
        try
        {
            googleAnalyticEnabled = "1".equals(hashData.get("ga"));
        }
        catch(Exception exception3)
        {
            googleAnalyticEnabled = true;
        }
        adMobID = (String)hashData.get("ads_id");
        try
        {
            adMobRefreshRate = Integer.parseInt((String)hashData.get("ads_rf"));
        }
        catch(Exception exception4)
        {
            adMobRefreshRate = 60;
        }
        try
        {
            interstitialAdInterval = Integer.parseInt((String)hashData.get("ads_int"));
        }
        catch(Exception exception5)
        {
            adMobRefreshRate = 60;
        }
        try
        {
            countryCode = (String)hashData.get("pm_cid");
        }
        catch(Exception exception6)
        {
            countryCode = "sg";
        }
        try
        {
            appVersion = Integer.parseInt((String)hashData.get("app_ver"));
        }
        catch(Exception exception7)
        {
            adMobRefreshRate = 60;
        }
        popupMessage = (String)hashData.get("umsg");
        popupURL = (String)hashData.get("uurl");
        popUpMessageID = (String)hashData.get("pm_id");
        popUpMessageContent = (String)hashData.get("pm_m");
        popUpMessageTitle = (String)hashData.get("pm_tt");
        popUpMessageType = (String)hashData.get("pm_tp");
        popUpMessageStartDate = (String)hashData.get("pm_sd");
        popUpMessageEndDate = (String)hashData.get("pm_ed");
        popUpMessageOfferId = (String)hashData.get("pm_oid");
        popUpMessageBusinessId = (String)hashData.get("pm_bid");
        popUpMessageLocationId = (String)hashData.get("pm_lid");
        popUpMessageLinkUrl = (String)hashData.get("pm_url");
        announcementTitle = (String)hashData.get("ann_title");
        announcementMessage = (String)hashData.get("ann_msg");
    }

    public static final streetdirectory.mobile.service.SDOutput.Creator CREATOR = new streetdirectory.mobile.service.SDOutput.Creator(APIVersionServiceOutput.class);
    private static final long serialVersionUID = 0xab4415b29d250a2dL;
    public String adMobID;
    public int adMobRefreshRate;
    public String announcementMessage;
    public String announcementTitle;
    public int apiVersion;
    public int appVersion;
    public int categoryVersion;
    public String countryCode;
    public boolean googleAnalyticEnabled;
    public int interstitialAdInterval;
    public String mapVersion;
    public String popUpMessageBusinessId;
    public String popUpMessageContent;
    public String popUpMessageEndDate;
    public String popUpMessageID;
    public String popUpMessageLinkUrl;
    public String popUpMessageLocationId;
    public String popUpMessageOfferId;
    public String popUpMessageStartDate;
    public String popUpMessageTitle;
    public String popUpMessageType;
    public String popupMessage;
    public String popupURL;

}
