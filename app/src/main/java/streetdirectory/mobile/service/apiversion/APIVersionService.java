// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.service.apiversion;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import streetdirectory.mobile.core.MainApplication;
import streetdirectory.mobile.core.SDPreferences;
import streetdirectory.mobile.core.storage.CacheStorage;
import streetdirectory.mobile.core.storage.StorageUtil;
import streetdirectory.mobile.service.SDDataOutput;
import streetdirectory.mobile.service.SDHttpService;
import streetdirectory.mobile.service.countrylist.CountryListService;

// Referenced classes of package streetdirectory.mobile.service.apiversion:
//            APIVersionServiceInput, APIVersionServiceOutput

public class APIVersionService extends SDHttpService
{

    public APIVersionService()
    {
        super(new APIVersionServiceInput(), APIVersionServiceOutput.class);
    }

    public static void updateInBackground()
    {
        (new APIVersionService() {

            public void onReceiveData(SDDataOutput sddataoutput)
            {
                onReceiveData((APIVersionServiceOutput)sddataoutput);
            }

            public void onReceiveData(APIVersionServiceOutput apiversionserviceoutput)
            {
                String s;
                boolean flag;
                boolean flag1;
                android.content.SharedPreferences.Editor editor = SDPreferences.getInstance().createEditor();
                editor.putInt("apiVersion", apiversionserviceoutput.apiVersion);
                editor.putInt("categoryVersion", apiversionserviceoutput.categoryVersion);
                editor.putBoolean("googleAnalyticEnabled", apiversionserviceoutput.googleAnalyticEnabled);
                editor.putString("adMobID", apiversionserviceoutput.adMobID);
                editor.putInt("adMobRefreshRate", apiversionserviceoutput.adMobRefreshRate);
                if(apiversionserviceoutput.interstitialAdInterval >= 60)
                    editor.putInt("interstitialAdInterval", apiversionserviceoutput.interstitialAdInterval);
                if(!SDPreferences.getInstance().getMapVersion().equals(apiversionserviceoutput.mapVersion))
                {
                    editor.putString("mapVersion", apiversionserviceoutput.mapVersion);
                    StorageUtil.deleteDirectory(CacheStorage.getStorageFile(MainApplication.getAppContext(), "/data/map"));
                }
                if(apiversionserviceoutput.popupMessage != null && apiversionserviceoutput.popupMessage.length() > 0)
                {
                    editor.putString("newVersionPopupMessage", apiversionserviceoutput.popupMessage);
                    if(apiversionserviceoutput.popupURL != null && apiversionserviceoutput.popupURL.length() > 0)
                        editor.putString("newVersionPopupUrl", apiversionserviceoutput.popupURL);
                } else
                {
                    editor.putString("newVersionPopupMessage", "");
                    editor.putString("newVersionPopupUrl", "");
                }
                s = SDPreferences.getInstance().getPopUpMessageId();
                editor.putInt("app_ver", apiversionserviceoutput.appVersion);
                flag1 = true;
                flag = flag1;
                if(apiversionserviceoutput.popUpMessageID != null) {
                    flag = flag1;
                    if (apiversionserviceoutput.popUpMessageID.length() > 0) {
                        flag = flag1;
                        if (apiversionserviceoutput.popUpMessageID != "") {
                            if (!SDPreferences.getInstance().getPopUpMessageShown()) {
                                if (!SDPreferences.getInstance().getPopUpMessageShown() && !s.equals(apiversionserviceoutput.popUpMessageID)) {
                                    flag = true;
                                } else {
                                    flag = flag1;
                                    if (!SDPreferences.getInstance().getPopUpMessageShown())
                                        flag = false;
                                }
                            } else
                                flag = true;
                        }
                    }
                }

                if(flag)
                {
                    editor.putString("popup_message_id", apiversionserviceoutput.popUpMessageID);
                    editor.putString("popup_message_content", apiversionserviceoutput.popUpMessageContent);
                    editor.putString("popup_message_title", apiversionserviceoutput.popUpMessageTitle);
                    editor.putString("popup_message_type", apiversionserviceoutput.popUpMessageType);
                    editor.putString("popup_message_start_date", apiversionserviceoutput.popUpMessageStartDate);
                    editor.putString("popup_message_end_date", apiversionserviceoutput.popUpMessageEndDate);
                    editor.putString("popup_message_offer_id", apiversionserviceoutput.popUpMessageOfferId);
                    editor.putString("popup_message_business_id", apiversionserviceoutput.popUpMessageBusinessId);
                    editor.putString("popup_message_location_id", apiversionserviceoutput.popUpMessageLocationId);
                    editor.putString("popup_message_URL", apiversionserviceoutput.popUpMessageLinkUrl);
                    editor.putBoolean("popup_message_shown", true);
                    editor.putString("popup_message_country_code", apiversionserviceoutput.countryCode);
                    editor.putBoolean("is_downloading_popup_message", false);
                }
                editor.commit();
                Intent intent = new Intent("pop_up_message");
                LocalBroadcastManager.getInstance(MainApplication.getAppContext()).sendBroadcast(intent);
                CountryListService.updateInBackground();
                return;
            }

        }).executeAsync();
    }
}
