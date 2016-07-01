

package streetdirectory.mobile.core;

import android.content.Context;

import com.xinghai.mycurve.R;

import java.util.Date;
import streetdirectory.mobile.SDApplication;
import streetdirectory.mobile.core.storage.ApplicationPreferences;


public class SDPreferences extends ApplicationPreferences
{

    private SDPreferences()
    {
        super(SDApplication.getAppContext());
    }

    public static SDPreferences getInstance()
    {
        if(INSTANCE == null)
            INSTANCE = new SDPreferences();
        return INSTANCE;
    }

    public int getAPIVersion()
    {
        return getIntForKey("apiVersion", 0);
    }

    public String getAdMobID()
    {
        return getStringForKey("adMobID", "a1506c11818670d");
    }

    public int getAdMobRefreshRate()
    {
        return getIntForKey("adMobRefreshRate", 60);
    }

    public String getAnnoucementMessageType2()
    {
        return getStringForKey("announcement_message_type2", MainApplication.getAppContext().getString(R.string.announcement_msg_description));
    }

    public boolean getAnnoucementMessageType2Shown()
    {
        return getBooleanForKey("announcement_message_type2_shown", false);
    }

    public String getAnnoucementTitle()
    {
        return getStringForKey("announcement_title", MainApplication.getAppContext().getString(R.string.announcement_msg_title));
    }

    public boolean getAnnoucementWillShow()
    {
        return getBooleanForKey("announcement_will_show", true);
    }

    public String getAnnouncementCheckedDate()
    {
        return getStringForKey("announcement_checked_date", "1900-01-01");
    }

    public int getAppVersion()
    {
        return getIntForKey("app_ver", 0);
    }

    public int getCategoryVersion()
    {
        return getIntForKey("categoryVersion", 0);
    }

    public int getInterstitialAdInterval()
    {
        return getIntForKey("interstitialAdInterval", 60);
    }

    public long getInterstitialAdLastShow()
    {
        return getLongForKey("interstitial_ad_last_show", 0L);
    }

    public String getMapVersion()
    {
        return getStringForKey("mapVersion", "1.0.0.273");
    }

    public String getNewVersionPopupMessage()
    {
        return getStringForKey("newVersionPopupMessage", "");
    }

    public String getNewVersionPopupUrl()
    {
        return getStringForKey("newVersionPopupUrl", "");
    }

    public String getPopUpMessageBusinessId()
    {
        return getStringForKey("popup_message_business_id", "");
    }

    public String getPopUpMessageContent()
    {
        return getStringForKey("popup_message_content", "");
    }

    public String getPopUpMessageCountryCode()
    {
        return getStringForKey("popup_message_country_code", "");
    }

    public String getPopUpMessageEndDate()
    {
        return getStringForKey("popup_message_end_date", "1900-01-01 00:00");
    }

    public String getPopUpMessageId()
    {
        return getStringForKey("popup_message_id", "");
    }

    public String getPopUpMessageLocationId()
    {
        return getStringForKey("popup_message_location_id", "");
    }

    public String getPopUpMessageOfferId()
    {
        return getStringForKey("popup_message_offer_id", "");
    }

    public boolean getPopUpMessageShown()
    {
        return getBooleanForKey("popup_message_shown", true);
    }

    public boolean getPopUpMessageShownFT()
    {
        return getBooleanForKey("popup_message_shown_ft", false);
    }

    public String getPopUpMessageStartDate()
    {
        return getStringForKey("popup_message_start_date", "1900-01-01 00:00");
    }

    public String getPopUpMessageTitle()
    {
        return getStringForKey("popup_message_title", "");
    }

    public String getPopUpMessageType()
    {
        return getStringForKey("popup_message_type", "");
    }

    public String getPopUpMessageUrl()
    {
        return getStringForKey("popup_message_URL", "");
    }

    public String getRedeemVoucherAddress()
    {
        return getStringForKey("redeem_voucher_address", "");
    }

    public String getRedeemVoucherName()
    {
        return getStringForKey("redeem_voucher_name", "");
    }

    public String getRedeemVoucherPhoneNumber()
    {
        return getStringForKey("redeem_voucher_phone_number", "");
    }

    public boolean isDownloadingPopUpMessage()
    {
        return getBooleanForKey("is_downloading_popup_message", true);
    }

    public boolean isGoogleAnalyticEnabled()
    {
        return getBooleanForKey("googleAnalyticEnabled", true);
    }

    public void setAppVersion(int i)
    {
        setValueForKey("app_ver", i);
    }

    public void setInterstitialAdLastShow(Date date)
    {
        setValueForKey("interstitial_ad_last_show", date.getTime());
    }

    public static final String ADMOB_ID_KEY = "adMobID";
    public static final String ADMOB_REFRESH_RATE_KEY = "adMobRefreshRate";
    public static final String ANNOUNCEMENT_CHECKED_DATE = "announcement_checked_date";
    public static final String ANNOUNCEMENT_MESSAGE_TYPE2 = "announcement_message_type2";
    public static final String ANNOUNCEMENT_MESSAGE_TYPE2_SHOWN = "announcement_message_type2_shown";
    public static final String ANNOUNCEMENT_TITLE = "announcement_title";
    public static final String ANNOUNCEMENT_WILL_SHOW = "announcement_will_show";
    public static final String API_VERSION_KEY = "apiVersion";
    public static final String APP_VERSION = "app_ver";
    public static final String CATEGORY_VERSION_KEY = "categoryVersion";
    public static final String GOOGLE_ANALYIC_ENABLED_KEY = "googleAnalyticEnabled";
    private static SDPreferences INSTANCE;
    public static final String INTERSTITIAL_AD_INTERVAL_KEY = "interstitialAdInterval";
    public static final String INTERSTITIAL_AD_LAST_SHOW = "interstitial_ad_last_show";
    public static final String IS_DOWNLOADING_POPUP_MESSAGE = "is_downloading_popup_message";
    public static final String MAP_VERSION_KEY = "mapVersion";
    public static final String NEW_VERSION_POPUP_MESSAGE = "newVersionPopupMessage";
    public static final String NEW_VERSION_POPUP_URL = "newVersionPopupUrl";
    public static final String POPUP_MESSAGE_BUSINESS_ID = "popup_message_business_id";
    public static final String POPUP_MESSAGE_CONTENT = "popup_message_content";
    public static final String POPUP_MESSAGE_COUNTRY_CODE = "popup_message_country_code";
    public static final String POPUP_MESSAGE_END_DATE = "popup_message_end_date";
    public static final String POPUP_MESSAGE_ID = "popup_message_id";
    public static final String POPUP_MESSAGE_LINK_URL = "popup_message_URL";
    public static final String POPUP_MESSAGE_LOCATION_ID = "popup_message_location_id";
    public static final String POPUP_MESSAGE_OFFER_ID = "popup_message_offer_id";
    public static final String POPUP_MESSAGE_SHOWN = "popup_message_shown";
    public static final String POPUP_MESSAGE_SHOWN_FT = "popup_message_shown_ft";
    public static final String POPUP_MESSAGE_START_DATE = "popup_message_start_date";
    public static final String POPUP_MESSAGE_TITLE = "popup_message_title";
    public static final String POPUP_MESSAGE_TYPE = "popup_message_type";
    public static final String REDEEM_VOUCHER_ADDRESS = "redeem_voucher_address";
    public static final String REDEEM_VOUCHER_NAME = "redeem_voucher_name";
    public static final String REDEEM_VOUCHER_PHONE_NUMBER = "redeem_voucher_phone_number";
    public static final String SITT_SERVER_DOWNLOADED_TIME = "sitt_server_downloaded_time";
}
