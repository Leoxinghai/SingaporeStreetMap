

package streetdirectory.mobile.modules.sdmob;

import java.util.HashMap;
import streetdirectory.mobile.core.StringTools;
import streetdirectory.mobile.service.SDDataOutput;

public class SdMobServiceOutput extends SDDataOutput
{

    public SdMobServiceOutput()
    {
    }

    public void populateData()
    {
        populateData();
        smallBannerType = StringTools.tryParseInt((String)hashData.get("small_banner_type"), -1);
        smallBannerStart = (String)hashData.get("small_banner_date");
        smallBannerEnd = (String)hashData.get("small_banner_end");
        smallBannerAdmobId = (String)hashData.get("small_banner_admob_id");
        smallBannerOfferId = (String)hashData.get("small_banner_offer_id");
        fullBannerType = StringTools.tryParseInt((String)hashData.get("full_banner_type"), -1);
        fullBannerStart = (String)hashData.get("full_banner_date");
        fullBannerEnd = (String)hashData.get("full_banner_end");
        fullBannerAdmobId = (String)hashData.get("full_banner_admob_id");
        fullBannerOfferId = (String)hashData.get("full_banner_offer_id");
        fullBannerOfferHtml = (String)hashData.get("full_banner_offer_html");
        fullBannerOfferLocationId = (String)hashData.get("full_banner_offer_lid");
        fullBannerOfferBusinessId = (String)hashData.get("full_banner_offer_bid");
    }

    public String fullBannerAdmobId;
    public String fullBannerEnd;
    public String fullBannerOfferBusinessId;
    public String fullBannerOfferHtml;
    public String fullBannerOfferId;
    public String fullBannerOfferLocationId;
    public String fullBannerStart;
    public int fullBannerType;
    public String smallBannerAdmobId;
    public String smallBannerEnd;
    public String smallBannerOfferId;
    public String smallBannerStart;
    public int smallBannerType;
}
