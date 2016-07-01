

package streetdirectory.mobile.service;

import android.content.Context;
import android.os.Build;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import streetdirectory.mobile.SDApplication;
import streetdirectory.mobile.core.*;

public class URLFactory
{

    public URLFactory()
    {
    }

    public static String createGantBottomBannerBusiness(String s)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/bottombanner/biz/").append(s).toString();
    }

    public static String createGantBottomBannerCategory(String s, String s1, String s2)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/bottombanner/cat/").append(s).append("/").append(s1).append("/").append(s2).toString();
    }

    public static String createGantBottomBannerNearbyOffers()
    {
        return (new StringBuilder()).append(getGantModelString()).append("/bottombanner/offers").toString();
    }

    public static String createGantBuildingDirectoryCategory(String s, String s1)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/building/").append(s).append("/").append(s1).append("/").toString();
    }

    public static String createGantBuildingDirectoryClickOnCategory(String s, String s1, String s2)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/building/").append(s).append("/").append(s1).append("/").append(s2).append("/").toString();
    }

    public static String createGantBuildingDirectoryClickOnSave(String s)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/building/").append(s).append("/save/").toString();
    }

    public static String createGantBuildingDirectoryClickOnSend(String s)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/building/").append(s).append("/send/").toString();
    }

    public static String createGantBuildingDirectoryMain(String s)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/building/").append(s).toString();
    }

    public static String createGantBuildingDirectoryMap(String s)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/building/").append(s).append("/map/").toString();
    }

    public static String createGantBuildingDirectorySearchBusiness(String s)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/building/").append(s).append("/search/").toString();
    }

    public static String createGantBuildingDirectoryTips(String s)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/building/").append(s).append("/tips/").toString();
    }

    public static String createGantBusinessCall(String s)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/business/call/").append(s).toString();
    }

    public static String createGantBusinessCategory(String s, String s1)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/business/category/").append(s1).append("/").append(s).toString();
    }

    public static String createGantBusinessCategoryAll(String s)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/business/all/").append(s).toString();
    }

    public static String createGantBusinessCategoryFree(String s, String s1)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/business/free/").append(s).append("/").append(s1).toString();
    }

    public static String createGantBusinessCategoryListing(String s, int i)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/business/listing/").append(i).append("/").append(s).toString();
    }

    public static String createGantBusinessCategoryMap(String s)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/business/map/").append(s).toString();
    }

    public static String createGantBusinessCategoryNearby(String s)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/business/nearby/").append(s).toString();
    }

    public static String createGantBusinessCategoryPremium(String s, String s1)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/business/premium/").append(s).append("/").append(s1).toString();
    }

    public static String createGantBusinessMain(String s, int i)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/business/main/").append(i).append("/").append(s).toString();
    }

    public static String createGantBusinessOffers(int i, String s)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/business/offer/").append(i).append("/").append(s).toString();
    }

    public static String createGantDirectionsClickOnBus()
    {
        return (new StringBuilder()).append(getGantModelString()).append("/directions/").append("bus/").toString();
    }

    public static String createGantDirectionsClickOnDrive()
    {
        return (new StringBuilder()).append(getGantModelString()).append("/directions/").append("drive/").toString();
    }

    public static String createGantDirectionsClickOnMrt()
    {
        return (new StringBuilder()).append(getGantModelString()).append("/directions/").append("mrt/").toString();
    }

    public static String createGantDirectionsClickOnSave()
    {
        return (new StringBuilder()).append(getGantModelString()).append("/directions/").append("save/").toString();
    }

    public static String createGantDirectionsClickOnSend()
    {
        return (new StringBuilder()).append(getGantModelString()).append("/directions/").append("send/").toString();
    }

    public static String createGantDirectionsClickOnTaxi()
    {
        return (new StringBuilder()).append(getGantModelString()).append("/directions/").append("taxi/").toString();
    }

    public static String createGantDirectionsMain()
    {
        return (new StringBuilder()).append(getGantModelString()).append("/directions/").toString();
    }

    public static String createGantDirectionsMap()
    {
        return (new StringBuilder()).append(getGantModelString()).append("/directions/").append("map/").toString();
    }

    public static String createGantNearbyCategory()
    {
        return (new StringBuilder()).append(getGantModelString()).append("/nearby/").append("category/").toString();
    }

    public static String createGantNearbyMain(String s)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/nearby/").append(s).toString();
    }

    public static String createGantNotificationPopUp(String s, String s1)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/notif_popup/").append(s).append("/").append(s1).append("/").toString();
    }

    public static String createGantNotificationPopUp(String s, String s1, String s2)
    {
        if(s2 != null)
            s2 = (new StringBuilder()).append(s2).append("/").toString();
        else
            s2 = "";
        s = (new StringBuilder()).append(getGantModelString()).append("/notif_popup/").append(s).append("/").append(s2).append(s1).append("/").toString();
        SDLogger.info((new StringBuilder()).append("GANT ALERT : ").append(s).toString());
        return s;
    }

    public static String createGantOfferDetailBranch(String s, int i)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/offer_detail_branch/").append(s).append("/").append(i).append("/").toString();
    }

    public static String createGantOfferDetailCall(String s, int i)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/offer_detail_call/").append(s).append("/").append(i).append("/").toString();
    }

    public static String createGantOfferDetailDirection(String s, int i)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/offer_detail_direction/").append(s).append("/").append(i).append("/").toString();
    }

    public static String createGantOfferDetailGetThisOffer(String s, int i)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/offer_detail_get_this_offer/").append(s).append("/").append(i).append("/").toString();
    }

    public static String createGantOfferDetailMap(String s, int i)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/offer_detail_map/").append(s).append("/").append(i).append("/").toString();
    }

    public static String createGantOfferDetailOfferBanner(String s, int i)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/offer_detail_offer_banner/").append(s).append("/").append(i).append("/").toString();
    }

    public static String createGantOfferDetailOfferTerms(String s, int i)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/offer_detail_offer_terms/").append(s).append("/").append(i).append("/").toString();
    }

    public static String createGantOfferDetailPage(String s, int i)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/offer_detail_page/").append(s).append("/").append(i).append("/").toString();
    }

    public static String createGantOfferDetailRedeemVoucher(String s, int i)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/offer_detail_redeem_voucher/").append(s).append("/").append(i).append("/").toString();
    }

    public static String createGantOfferDetailShareOffer(String s, int i)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/offer_detail_share_offer/").append(s).append("/").append(i).append("/").toString();
    }

    public static String createGantOffersListingAllTab(String s)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/offer_listing_all/").append(s).toString();
    }

    public static String createGantOffersListingFacebookShare()
    {
        return (new StringBuilder()).append(getGantModelString()).append("/offer_listing_facebook_share/").toString();
    }

    public static String createGantOffersListingItem(String s, String s1, String s2)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/offer_listing_item/").append(s).append("/").append(s1).append("/").append(s2).append("/").toString();
    }

    public static String createGantOffersListingMapTab(String s)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/offer_listing_map/").append(s).toString();
    }

    public static String createGantOffersListingNearbyTab(String s)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/offer_listing_nearby/").append(s).toString();
    }

    public static String createGantOffersListingPage()
    {
        return (new StringBuilder()).append(getGantModelString()).append("/offer_listing_page/").toString();
    }

    public static String createGantOffersListingPickCategory(String s)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/offer_listing_pick_category/").append(s).toString();
    }

    public static String createGantOffersMenu()
    {
        return (new StringBuilder()).append(getGantModelString()).append("/menu/offers").toString();
    }

    public static String createGantOfflineMapDeleteDownload(String s, String s1)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/offline_map/delete/").append(s).append("/").append(s1).append("/").toString();
    }

    public static String createGantOfflineMapDownloadPage()
    {
        return (new StringBuilder()).append(getGantModelString()).append("/offline_map/download/").toString();
    }

    public static String createGantOfflineMapStartDownload(String s, String s1)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/offline_map/start/").append(s).append("/").append(s1).append("/").toString();
    }

    public static String createGantOfflineMapSuccessDownload(String s, String s1)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/offline_map/success/").append(s).append("/").append(s1).append("/").toString();
    }

    public static String createGantOfflineMapUpdateDownload(String s, String s1)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/offline_map/update/").append(s).append("/").append(s1).append("/").toString();
    }

    public static String createGantOfflineMapWelcomePage()
    {
        return (new StringBuilder()).append(getGantModelString()).append("/offline_map/welcome/").toString();
    }

    public static String createGantOthersAboutThisApp()
    {
        return (new StringBuilder()).append(getGantModelString()).append("/others/about_app/").toString();
    }

    public static String createGantOthersMap()
    {
        return (new StringBuilder()).append(getGantModelString()).append("/others/map/").toString();
    }

    public static String createGantOthersMyInbox()
    {
        return (new StringBuilder()).append(getGantModelString()).append("/others/my_inbox/").toString();
    }

    public static String createGantOthersMySaves()
    {
        return (new StringBuilder()).append(getGantModelString()).append("/others/my_save/").toString();
    }

    public static String createGantOthersSearch()
    {
        return (new StringBuilder()).append(getGantModelString()).append("/others/search/").toString();
    }

    public static String createGantOthersSplash()
    {
        return (new StringBuilder()).append(getGantModelString()).append("/others/splash/").toString();
    }

    public static String createGantPlaceBusStop(String s)
    {
        s = (new StringBuilder()).append(getGantModelString()).append("/place/bus_stop/").append(s).toString();
        SDLogger.info((new StringBuilder()).append("Gant Place Bus Stop : ").append(s).toString());
        return s;
    }

    public static String createGantRedeemVoucherPage(String s, int i)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/redeem_voucher_page/").append(s).append("/").append(i).append("/").toString();
    }

    public static String createGantRedeemVoucherSubmit(String s, int i)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/redeem_voucher_submit/").append(s).append("/").append(i).append("/").toString();
    }

    public static String createGantRedeemVoucherTAC(String s, int i)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/redeem_voucher_tac/").append(s).append("/").append(i).append("/").toString();
    }

    public static String createGantRedeemVoucherTakePhoto(String s, int i)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/redeem_voucher_take_photo/").append(s).append("/").append(i).append("/").toString();
    }

    public static String createGantRedeemVoucherVoucherList(String s, int i, String s1)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/redeem_voucher_voucher_list/").append(s).append("/").append(i).append("/").append(s1).append("/").toString();
    }

    public static String createGantSitt()
    {
        return (new StringBuilder()).append(getGantModelString()).append("/sitt/index/").toString();
    }

    public static String createGantTipsLatestTips()
    {
        return (new StringBuilder()).append(getGantModelString()).append("/directions/").append("tips/").append("latest/").toString();
    }

    public static String createGantTipsNearbyTips()
    {
        return (new StringBuilder()).append(getGantModelString()).append("/directions/").append("tips/").append("nearby/").toString();
    }

    public static String createGantTipsPostTips()
    {
        return (new StringBuilder()).append(getGantModelString()).append("/directions/").append("tips/").append("post/").toString();
    }

    public static String createGantTipsTopUser()
    {
        return (new StringBuilder()).append(getGantModelString()).append("/directions/").append("tips/").append("top_user/").toString();
    }

    public static String createGantTipsUserDetail(String s)
    {
        return (new StringBuilder()).append(getGantModelString()).append("/directions/").append("tips/").append("user/").append(s).toString();
    }

    public static String createGantTrafficBKE()
    {
        return (new StringBuilder()).append(getGantModelString()).append("/traffic/bke/").toString();
    }

    public static String createGantTrafficCTE()
    {
        return (new StringBuilder()).append(getGantModelString()).append("/traffic/cte/").toString();
    }

    public static String createGantTrafficCW()
    {
        return (new StringBuilder()).append(getGantModelString()).append("/traffic/cw/").toString();
    }

    public static String createGantTrafficECP()
    {
        return (new StringBuilder()).append(getGantModelString()).append("/traffic/ecp/").toString();
    }

    public static String createGantTrafficKJE()
    {
        return (new StringBuilder()).append(getGantModelString()).append("/traffic/kje/").toString();
    }

    public static String createGantTrafficKPE()
    {
        return (new StringBuilder()).append(getGantModelString()).append("/traffic/kpe/").toString();
    }

    public static String createGantTrafficMain()
    {
        return (new StringBuilder()).append(getGantModelString()).append("/traffic/").toString();
    }

    public static String createGantTrafficPIE()
    {
        return (new StringBuilder()).append(getGantModelString()).append("/traffic/pie/").toString();
    }

    public static String createPlaceDataString(int i, int j)
    {
        String s1 = "";
        String s = s1;
        if(i != 0)
        {
            s = s1;
            if(j != 0)
                s = (new StringBuilder()).append(i).append("_").append(j).toString();
        }
        return s;
    }

    private static String createSiteBannerBaseURL()
    {
        Random random = new Random();
        return (new StringBuilder()).append("http://maps").append(random.nextInt(4) + 1).append(".streetdirectory.com/sd_static/images/side_banner").toString();
    }

    public static String createURLAPIVersion(String s, int i)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=version&act=detail&os=android&output=xml&no_cache=1&app_id=").append(s).append("&app_ver=").append(i).toString();
        SDLogger.info((new StringBuilder()).append("API Version URL: ").append(s).toString());
        return s;
    }

    public static String createURLAddNewPlace(String s, String s1, String s2, String s3, int i, String s4, String s5, String s6,
            String s7, double d, double d1, double d2,
            double d3)
    {
        s4 = encode(s4);
        s5 = encode(s5);
        s6 = encode(s6);
        s7 = encode(s7);
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=place_submit&act=add&output=xml&x=").append(d2).append("&y=").append(d3).append("&cx=").append(d).append("&cy=").append(d1).append("&country=").append(s).append("&place_name=").append(s4).append("&type=").append(i).append("&image_url=").append(s2).append("&image_url_2=").append(s3).append("&uid=").append(s1).append("&no_cache=1&address=").append(s5).append("&name=").append(s6).append("&phone=").append(s7).toString();
        SDLogger.info((new StringBuilder()).append("Add New Place URL: ").append(s).toString());
        return s;
    }

    public static String createURLApplicationStatus(String s, String s1, int i)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=status&act=all&output=xml&no_cache=1&uid=").append(s1).append("&country=").append(s).append("&ver=").append(i).toString();
        SDLogger.info((new StringBuilder()).append("Application Status URL: ").append(s).toString());
        return s;
    }

    public static String createURLBottomBanner(String s, double d, double d1, double d2, double d3, int i, int j, int k, int l, String s1, int i1,
            int j1, int k1)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=bottom_banner&act=listing&x1=").append(d).append("&y1=").append(d1).append("&x2=").append(d2).append("&y2=").append(d3).append("&col1=").append(i).append("&row1=").append(j).append("&col2=").append(k).append("&row2=").append(l).append("&level=").append(i1).append("&cat=").append(s1).append("&no_cache=1&limit=").append(j1).append("&output=xml&country=").append(s).append("&ver=").append(k1).toString();
        SDLogger.info((new StringBuilder()).append("Bottom Banner URL: ").append(s).toString());
        return s;
    }

    public static String createURLBottomBannerWorlds(String s, int i, int j)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=banner&act=bb_worlds&country=").append(s).append("&output=html&no_cache=1&").append(SDStory.createParameterString(SDStory.createDefaultParams())).append("&rw=").append(i).append("&rh=").append(j).toString();
        SDLogger.info((new StringBuilder()).append("Bottom Banner Worlds URL: ").append(s).toString());
        return s;
    }

    public static String createURLBuildingVectorWithCountryCode(String s, double d, double d1)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=building_vector&lon=").append(d).append("&lat=").append(d1).append("&l=13&act=mobile_2&output=xml&api=").append("2f4ddd9d469465022d26c61b521ffe329a952427").append("&ver=").append(SDPreferences.getInstance().getAPIVersion()).toString();
        SDLogger.info((new StringBuilder()).append("Building Vector URL ").append(s).toString());
        return s;
    }

    public static String createURLBusArrival(String s, String s1)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=bus&act=sbstransit&output=xml&nocache=1&busstopid=").append(encode(s)).append("&serviceNumber=").append(encode(s1)).append(SDStory.createParameterString(SDStory.createDefaultParams())).toString();
        SDLogger.info((new StringBuilder()).append("Bus Arrival URL: ").append(s).toString());
        return s;
    }

    public static String createURLBusArrivalStatus(int i, String s, int j)
    {
        String s1 = android.provider.Settings.Secure.getString(SDApplication.getAppContext().getContentResolver(), "android_id");
        s = (new StringBuilder()).append("http://www.sbstransit.com.sg/open_api/nextbus.aspx?iriskey=30323A06-88BB-4124-9EC3-8E2E344E4C99&svc=").append(s).append("&busstop=").append(i).append("&UDID=").append(s1).append("&IP=").append("192.168.0.1").toString();
        SDLogger.info((new StringBuilder()).append("Bus Arrival URL: ").append(s).toString());
        return s;
    }

    public static String createURLBusArrivalStatusEdit(String s, String s1, int i)
    {
        String s2 = android.provider.Settings.Secure.getString(SDApplication.getAppContext().getContentResolver(), "android_id");
        s = (new StringBuilder()).append("http://www.sbstransit.com.sg/open_api/nextbus.aspx?iriskey=30323A06-88BB-4124-9EC3-8E2E344E4C99&svc=").append(s1).append("&busstop=").append(s).append("&UDID=").append(s2).append("&IP=").append("192.168.0.1").toString();
        SDLogger.info((new StringBuilder()).append("Bus Arrival URL: ").append(s).toString());
        return s;
    }

    public static String createURLBusListString(int i, int j)
    {
        String s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=bus&act=bus_no_list_2&output=xml&bus_stop=").append(i).append("&ver=").append(j).toString();
        SDLogger.info((new StringBuilder()).append("Bus List URL: ").append(s).toString());
        return s;
    }

    public static String createURLBusListStringEdit(String s, int i)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=bus&act=bus_no_list_2&output=xml&nocache=1&bus_stop=").append(s).append("&ver=").append(i).toString();
        SDLogger.info((new StringBuilder()).append("Bus List URL: ").append(s).toString());
        return s;
    }

    public static String createURLBusRoutes(String s, String s1)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=public_transport&country=").append(s).append("&no=").append(s1).append("&output=xml&ver=").append(SDPreferences.getInstance().getAPIVersion()).toString();
        SDLogger.info((new StringBuilder()).append("Bus Routes URL ").append(s).toString());
        return s;
    }

    public static String createURLBusStopRectangle(double d, double d1, double d2, double d3)
    {
        String s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=bus_stop&output=xml&left=").append(d).append("&top=").append(d1).append("&right=").append(d2).append("&bottom=").append(d3).append("&act=rectangle").toString();
        SDLogger.info((new StringBuilder()).append("API Version URL Bus").append(s).toString());
        return s;
    }

    public static String createURLBusinessAboutUs(String s, int i, int j)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=business&act=about_us&output=html&no_cache=1&country=").append(s).append("&cid=").append(i).append("&ver=").append(j).toString();
        SDLogger.info((new StringBuilder()).append("Business About Us URL: ").append(s).toString());
        return s;
    }

    public static String createURLBusinessBranchList(String s, int i, int j, int k, int l, double d, double d1, String s1)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=business&act=detail&output=xml&att=bao&cid=").append(i).append("&country=").append(s).append("&start=").append(j).append("&limit=").append(k).append("&x=").append(d).append("&y=").append(d1).append("&ver=").append(l).append("&offer_id=").append(s1).toString();
        SDLogger.info((new StringBuilder()).append("Branch List URL: ").append(s).toString());
        return s;
    }

    public static String createURLBusinessDetail(String s, String s1, int i, int j, int k)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=business&act=detail&output=xml&country=").append(s).append("&cid=").append(i).append("&lid=").append(j).append("&ver=").append(k).append("&no_cache=1&sd_uid=").append(s1).toString();
        SDLogger.info((new StringBuilder()).append("Business Detail URL: ").append(s).toString());
        return s;
    }

    public static String createURLBusinessDetail(String s, String s1, int i, int j, int k, String s2)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=business&act=detail&output=xml&country=").append(s).append("&cid=").append(i).append("&lid=").append(j).append("&ver=").append(k).append("&no_cache=1&sd_uid=").append(s1).append("&offer_id=").append(s2).toString();
        SDLogger.info((new StringBuilder()).append("Business Detail Offer URL: ").append(s).toString());
        return s;
    }

    public static String createURLBusinessFBLink(String s, int i, int j, int k, int l)
    {
        String s1 = "";
        if(!s.equals("sg"))
            s1 = (new StringBuilder()).append("&state_id=").append(i).toString();
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=link&act=company_detail&output=xml&country=").append(s).append("&company_id=").append(j).append("&link_id=").append(k).append(s1).append("&ver=").append(l).toString();
        SDLogger.info((new StringBuilder()).append("Business FB Link URL: ").append(s).toString());
        return s;
    }

    public static String createURLBusinessFinderGenreList(String s, int i, int j)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=mobile_category&act=listing&output=xml&mpid=").append(i).append("&country=").append(s).append("&vercat=").append(j).toString();
        SDLogger.info((new StringBuilder()).append("Business Finder Genre URL: ").append(s).toString());
        return s;
    }

    public static String createURLBusinessFinderIndustryList(String s, int i, int j, int k)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=business&act=industry_2&output=xml&country=").append(s).append("&industry=").append(i).append("&group=").append(j).append("&vercat=").append(k).toString();
        SDLogger.info((new StringBuilder()).append("Business Finder Industry URL: ").append(s).toString());
        return s;
    }

    public static String createURLBusinessFinderMenu(String s, int i, int j)
    {
        s = createURLResizeImage((new StringBuilder()).append("http://www.streetdirectory.com/img/mobile/bf_category/").append(s).toString(), i, j);
        SDLogger.info((new StringBuilder()).append("Image URL: ").append(s).toString());
        return s;
    }

    public static String createURLBusinessFinderYellowBarList(String s, int i)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=mobile_category&act=yellow_menu&output=xml&mpid=").append(i).append("&country=").append(s).toString();
        SDLogger.info((new StringBuilder()).append("Business Finder Yellow Bar URL: ").append(s).toString());
        return s;
    }

    public static String createURLBusinessImageList(String s, int i, int j, int k)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=business&act=detail&output=xml&att=img_2&no_cache=1&country=").append(s).append("&cid=").append(i).append("&lid=").append(j).append("&ver=").append(k).toString();
        SDLogger.info((new StringBuilder()).append("Business Image List URL: ").append(s).toString());
        return s;
    }

    public static String createURLBusinessInCategoryList(String s, int i, int j, int k, int l, int i1)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=business&act=business_in_category&output=xml&profile=iphone&pid=").append(i).append("&aid=").append(j).append("&start=").append(k).append("&limit=").append(l).append("&country=").append(s).append("&ver=").append(i1).toString();
        SDLogger.info((new StringBuilder()).append("Business In Category URL: ").append(s).toString());
        return s;
    }

    public static String createURLBusinessInList(String s, int i, int j, int k, int l, int i1, String s1, boolean flag,
            int j1)
    {
        String s2 = "";
        if(flag)
            s2 = "&rab=1";
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=business&act=business_in&order_type=asc&order_field=position&output=xml&profile=iphone&pid=").append(i).append("&aid=").append(j).append("&group=").append(k).append("&start=").append(l).append("&limit=").append(i1).append("&country=").append(s).append("&q=").append(s1).append(s2).append("&ver=").append(j1).toString();
        SDLogger.info((new StringBuilder()).append("Business In URL: ").append(s).toString());
        return s;
    }

    public static String createURLBusinessInList(String s, int i, int j, int k, int l, int i1, boolean flag, int j1)
    {
        return createURLBusinessInList(s, i, j, k, l, i1, "", flag, j1);
    }

    public static String createURLBusinessInList(String s, int i, int j, int k, int l, String s1, int i1)
    {
        return createURLBusinessInList(s, i, j, 0, k, l, s1, false, i1);
    }

    public static String createURLBusinessInList(String s, int i, int j, int k, int l, boolean flag, int i1)
    {
        return createURLBusinessInList(s, i, j, 0, k, l, "", flag, i1);
    }

    public static String createURLCallMeRequest(String s, int i, int j, String s1, int k, int l)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=visitor_contact&act=add&&output=xml&no_cache=1&country=").append(s).append("&cid=").append(i).append("&lid=").append(j).append("&phn_ar=").append(s1).append("&phn=").append(k).append("&ver=").append(l).toString();
        SDLogger.info((new StringBuilder()).append("Call Me URL: ").append(s).toString());
        return s;
    }

    public static String createURLCountryList(int i)
    {
        String s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=country&act=listing_2&output=xml&vercat=").append(i).toString();
        SDLogger.info((new StringBuilder()).append("Country List URL: ").append(s).toString());
        return s;
    }

    public static String createURLCurrentLocation(double d, double d1, int i)
    {
        String s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=location_nearby&act=mobile&output=xml&x=").append(d).append("&y=").append(d1).append("&ver=").append(i).toString();
        SDLogger.info((new StringBuilder()).append("Current location URL: ").append(s).toString());
        return s;
    }

    public static String createURLDealingWithList(String s, int i, int j, int k)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=business&act=detail&output=xml&att=cat&start=0&limit=100&country=").append(s).append("&cid=").append(i).append("&lid=").append(j).append("&ver=").append(k).toString();
        SDLogger.info((new StringBuilder()).append("Dealing With URL: ").append(s).toString());
        return s;
    }

    public static String createURLDirectionDetail(String s, double d, double d1, int i, int j, double d2, double d3, int k, int l, String s1, String s2,
            String s3, String s4, int i1)
    {
        if(!s1.equals("bustrain"))
            s2 = s1;
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=journey&output=xml&act=sd_api&country=").append(s).append("&startlon=").append(d).append("&startlat=").append(d1).append("&pid_aid1=").append(createPlaceDataString(i, j)).append("&endlon=").append(d2).append("&endlat=").append(d3).append("&pid_aid2=").append(createPlaceDataString(k, l)).append("&methods=").append(s1).append("&vehicle=").append(s2).append("&info=1&no_infos=0&no_route=0&date=").append(s3).append("&time=").append(s4).append("&ver=").append(i1).toString();
        SDLogger.info((new StringBuilder()).append("Direction Detail URL: ").append(s).toString());
        return s;
    }

    public static String createURLDirectionOverview(String s, double d, double d1, int i, int j, double d2, double d3, int k, int l, String s1, String s2,
            int i1)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=journey&output=xml&act=sd_api&country=").append(s).append("&startlon=").append(d).append("&startlat=").append(d1).append("&pid_aid1=").append(createPlaceDataString(i, j)).append("&endlon=").append(d2).append("&endlat=").append(d3).append("&pid_aid2=").append(createPlaceDataString(k, l)).append("&methods=all&vehicle=both&info=1&no_infos=1&no_route=1&date=").append(s1).append("&time=").append(s2).append("&ver=").append(i1).toString();
        SDLogger.info((new StringBuilder()).append("Direction Overview URL: ").append(s).toString());
        return s;
    }

    public static String createURLERP(String s, String s1)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/ajax/ajax_traffic/get_erp/?&type=xml&detail=1&zone=").append(s1).append("&id=").append(s).toString();
        SDLogger.info((new StringBuilder()).append("ERP with Zone or ID URL: ").append(s).toString());
        return s;
    }

    public static String createURLERPRate(String s, String s1, String s2)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/ajax/ajax_traffic/get_erp/?&type=xml&detail=1&id=").append(s1).append("&time=").append(s).append("&day=").append(s2).toString();
        SDLogger.info((new StringBuilder()).append("ERP Rate with time URL: ").append(s).toString());
        return s;
    }

    public static String createURLERPRectangle(double d, double d1, double d2, double d3)
    {
        String s = (new StringBuilder()).append("http://www.streetdirectory.com/ajax/ajax_traffic/get_erp/?&type=xml&act=rectangle&top=").append(d).append("&left=").append(d1).append("&bottom=").append(d2).append("&right=").append(d3).toString();
        SDLogger.info((new StringBuilder()).append("ERP Rectagle URL: ").append(s).toString());
        return s;
    }

    public static String createURLFacebookPhoto(String s, int i, int j)
    {
        String s1;
        if(i < 50 && j < 50)
            s1 = "square";
        else
        if(i < 50)
            s1 = "small";
        else
        if(i < 100)
            s1 = "normal";
        else
            s1 = "large";
        return (new StringBuilder()).append("https://graph.facebook.com/").append(s).append("/picture?type=").append(s1).toString();
    }

    public static String createURLFacebookPhotoImage(String s, int i, int j)
    {
        s = (new StringBuilder()).append("http://graph.facebook.com/").append(s).append("/picture?width=").append(i).append("&height=").append(j).toString();
        SDLogger.info((new StringBuilder()).append("Facebook Photo Image URL: ").append(s).toString());
        return s;
    }

    public static String createURLFacebookSendData(String s, String s1, String s2, String s3, String s4, String s5, String s6, String s7,
            String s8, String s9, String s10, String s11, int i)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=fb_users&act=add&output=xml&no_cache=1&uid=").append(s).append("&name=").append(s1).append("&sex=").append(s2).append("&religion=").append(s3).append("&pic_square=").append(s4).append("&profile_url=").append(s5).append("&location=").append(s6).append("&postal=").append(s7).append("&email=").append(s8).append("&birthday=").append(s9).append("&country=").append(s10).append("&insert_time=").append(s11).append("&ver=").append(i).toString();
        SDLogger.info((new StringBuilder()).append("Facebook Send Data URL: ").append(s).toString());
        return s;
    }

    public static String createURLFavoriteBusinessDelete(String s, String s1, String s2, int i)
    {
        return createURLFavoriteDelete(s, 2, s1, s2, i);
    }

    public static String createURLFavoriteBusinessSave(String s, String s1, String s2, String s3, int i)
    {
        return createURLFavoriteSave(s, 2, s1, s2, s3, i);
    }

    public static String createURLFavoriteBusinessStatus(String s, String s1, String s2, int i)
    {
        return createURLFavoriteStatus(s, 2, s1, s2, i);
    }

    public static String createURLFavoriteDelete(String s, int i, String s1, String s2, int j)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=likes&act=unlike&country=").append(s).append("&data_id=").append(s1).append("&uid=").append(s2).append("&type=").append(i).append("&output=xml&no_cache=1&ver=").append(j).toString();
        SDLogger.info((new StringBuilder()).append("Favorite delete URL: ").append(s).toString());
        return s;
    }

    public static String createURLFavoriteList(String s, String s1, int i, int j, int k, int l)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=likes&act=list_by_user&profile=sd_mobile&uid=").append(s1).append("&country=").append(s).append("&type=").append(i).append("&output=xml&no_cache=1&start=").append(j).append("&limit=").append(k).append("&ver=").append(l).toString();
        SDLogger.info((new StringBuilder()).append("Favorite list URL: ").append(s).toString());
        return s;
    }

    public static String createURLFavoriteLocationDelete(String s, String s1, String s2, String s3, int i)
    {
        return createURLFavoriteDelete(s, 1, (new StringBuilder()).append(s1).append("_").append(s2).toString(), s3, i);
    }

    public static String createURLFavoriteLocationSave(String s, String s1, String s2, String s3, String s4, int i)
    {
        return createURLFavoriteSave(s, 1, (new StringBuilder()).append(s1).append("_").append(s2).toString(), s3, s4, i);
    }

    public static String createURLFavoriteLocationStatus(String s, String s1, String s2, String s3, int i)
    {
        return createURLFavoriteStatus(s, 1, (new StringBuilder()).append(s1).append("_").append(s2).toString(), s3, i);
    }

    public static String createURLFavoriteNearbyListService(String s, String s1, int i, int j, double d, double d1,
            int k)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=likes&act=list_by_user&profile=sd_mobile&uid=").append(s1).append("&country=").append(s).append("&output=xml&dist=").append(i).append("&no_cache=1&x=").append(d).append("&y=").append(d1).append("&type=").append(j).append("&ver=").append(k).toString();
        SDLogger.info((new StringBuilder()).append("Favorite Nearby List URL: ").append(s).toString());
        return s;
    }

    public static String createURLFavoriteRouteList(String s, String s1, int i)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=route_log&act=get_list&output=xml&no_cache=1&country=").append(s).append("&uid=").append(s1).append("&ver=").append(i).toString();
        SDLogger.info((new StringBuilder()).append("Favorite Route List URL: ").append(s).toString());
        return s;
    }

    public static String createURLFavoriteSave(String s, int i, String s1, String s2, String s3, int j)
    {
        s3 = encode(s3);
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=likes&act=like&country=").append(s).append("&data_id=").append(s1).append("&uid=").append(s2).append("&type=").append(i).append("+&svName=").append(s3).append("&output=xml&no_cache=1&ver=").append(j).toString();
        SDLogger.info((new StringBuilder()).append("Favorite save URL: ").append(s).toString());
        return s;
    }

    public static String createURLFavoriteStatus(String s, int i, String s1, String s2, int j)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=likes&act=is_liked&country=").append(s).append("&data_id=").append(s1).append("&uid=").append(s2).append("&type=").append(i).append("&ver=").append(j).toString();
        SDLogger.info((new StringBuilder()).append("Favorite status URL: ").append(s).toString());
        return s;
    }

    public static String createURLFavoriteUserTipsService(String s, String s1, int i, int j, int k)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/http://www.streetdirectory.com/api/?mode=tips&act=user&output=xml&no_cache=1&country=").append(s).append("&start=").append(i).append("&limit=").append(j).append("&uid=").append(s1).append("&ver=").append(k).toString();
        SDLogger.info((new StringBuilder()).append("Favorite User Tips in Failed").append(s).toString());
        return s;
    }

    public static String createURLFriendAdd(String s, String s1, String s2, int i)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=people&act=add_friend&output=xml&no_cache=1&uid=").append(s).append("&fuid=").append(s1).append("&country=").append(s2).append("&ver=").append(i).toString();
        SDLogger.info((new StringBuilder()).append("Friend Add URL: ").append(s).toString());
        return s;
    }

    public static String createURLFriendApprove(String s, String s1, String s2, int i)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=people&act=approve_friend&output=xml&no_cache=1&uid=").append(s).append("&fuid=").append(s1).append("&country=").append(s2).append("&ver=").append(i).toString();
        SDLogger.info((new StringBuilder()).append("Friend Approve URL: ").append(s).toString());
        return s;
    }

    public static String createURLFriendDelete(String s, String s1, String s2, int i)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=people&act=unfriend&output=xml&no_cache=1&uid=").append(s).append("&fuid=").append(s1).append("&country=").append(s2).append("&ver=").append(i).toString();
        SDLogger.info((new StringBuilder()).append("Friend Delete URL: ").append(s).toString());
        return s;
    }

    public static String createURLFriendList(String s, String s1, String s2, boolean flag, int i, int j, int k)
    {
        String s3;
        if(flag)
            s3 = "&random=1";
        else
            s3 = "&sort=name";
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=people&act=friends&output=xml&no_cache=1&uid=").append(s).append("&country=").append(s1).append("&start=").append(i).append("&limit=").append(j).append("&q=").append(s2).append(s3).append("&ver=").append(k).toString();
        SDLogger.info((new StringBuilder()).append("Friend List URL: ").append(s).toString());
        return s;
    }

    public static String createURLFriendPendingList(String s, String s1, int i)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=people&act=friend_pending&output=xml&no_cache=1&uid=").append(s).append("&country=").append(s1).append("&ver=").append(i).toString();
        SDLogger.info((new StringBuilder()).append("Friend Pending List URL: ").append(s).toString());
        return s;
    }

    public static String createURLFriendReject(String s, String s1, String s2, int i)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=people&act=reject_friend&output=xml&no_cache=1&uid=").append(s).append("&fuid=").append(s1).append("&country=").append(s2).append("&ver=").append(i).toString();
        SDLogger.info((new StringBuilder()).append("Friend Reject URL: ").append(s).toString());
        return s;
    }

    public static String createURLFriendRequestList(String s, String s1, int i)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=people&act=friend_request&output=xml&no_cache=1&uid=").append(s).append("&country=").append(s1).append("&ver=").append(i).toString();
        SDLogger.info((new StringBuilder()).append("Friend Request List URL: ").append(s).toString());
        return s;
    }

    public static String createURLFriendStatus(String s, String s1, String s2, int i)
    {
        String s3 = s;
        if(s == null)
            s3 = "";
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=people&act=friendship_status&output=xml&no_cache=1&uid=").append(s3).append("&fuid=").append(s1).append("&country=").append(s2).append("&ver=").append(i).toString();
        SDLogger.info((new StringBuilder()).append("Friend Status URL: ").append(s).toString());
        return s;
    }

    public static String createURLFriendSuggestedList(String s, String s1, String s2, int i, int j, int k)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=people&act=get_suggested_friend&output=xml&no_cache=1&uid=").append(s).append("&country=").append(s1).append("&start=").append(i).append("&limit=").append(j).append("&q=").append(s2).append("&ver=").append(k).toString();
        SDLogger.info((new StringBuilder()).append("Friend Suggested List URL: ").append(s).toString());
        return s;
    }

    public static String createURLGetNearbyLoc(double d, double d1, int i)
    {
        String s = null;
        String s1;
        try
        {
            s1 = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=location_nearby&act=nearby&output=xml&x=").append(d).append("&y=").append(d1).append("&ver=").append(i).append("&ref=").append(URLEncoder.encode(REFERER, "UTF-8")).toString();
        }
        catch(UnsupportedEncodingException unsupportedencodingexception)
        {
            unsupportedencodingexception.printStackTrace();
            return s;
        }
        s = s1;
        SDLogger.info((new StringBuilder()).append("Nearby location URL: ").append(s1).toString());
        return s1;
    }

    public static String createURLLocationBusinessListingList(String s, int i, int j, int k, int l, int i1, int j1)
    {
        String s1 = "";
        if(i != 1) {
            if(i == 2)
                s1 = "business";
        } else
            s1 = "location";

        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=").append(s1).append("&act=listing_2&output=xml&no_cache=1&profile=iphone&category=").append(j).append("&start=").append(k).append("&limit=").append(l).append("&state_id=").append(i1).append("&country=").append(s).append("&ver=").append(j1).toString();
        SDLogger.info((new StringBuilder()).append("Location Business Listing URL: ").append(s).toString());
        return s;
    }

    public static String createURLLocationBusinessTipsList(String s, int i, String s1, int j, int k, int l)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=tips&act=location&output=xml&type=1&no_cache=1&country=").append(s).append("&type=").append(i).append("&data_id=").append(s1).append("&start=").append(j).append("&limit=").append(k).append("&ver=").append(l).toString();
        SDLogger.info((new StringBuilder()).append("Location Business Tips URL: ").append(s).toString());
        return s;
    }

    public static String createURLLocationFBLink(String s, int i, int j, int k, int l)
    {
        String s1 = "";
        if(!s.equals("sg"))
            s1 = (new StringBuilder()).append("&state_id=").append(i).toString();
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=link&act=map&output=xml&country=").append(s).append("&place_id=").append(j).append("&address_id=").append(k).append(s1).append("&ver=").append(l).toString();
        SDLogger.info((new StringBuilder()).append("Location FB Link URL: ").append(s).toString());
        return s;
    }

    public static String createURLLocationImageList(String s, int i, int j, int k)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=location&act=detail&output=xml&att=img_2&no_cache=1&country=").append(s).append("&pid=").append(i).append("&aid=").append(j).append("&ver=").append(k).toString();
        SDLogger.info((new StringBuilder()).append("Location Image List URL: ").append(s).toString());
        return s;
    }

    public static String createURLMapImage(double d, double d1, int i, int j, int k)
    {
        String s = (new StringBuilder()).append("http://www.streetdirectory.com/world.cgi?star=1&level=").append(k).append("&sizex=").append(i).append("&sizey=").append(j).append("&lon=").append(d).append("&lat=").append(d1).append("&map_only=1").toString();
        SDLogger.info((new StringBuilder()).append("Map Image URL: ").append(s).toString());
        return s;
    }

    public static String createURLMapImage(boolean flag, double d, double d1, int i, int j, int k)
    {
        StringBuilder stringbuilder = (new StringBuilder()).append("http://www.streetdirectory.com/world.cgi?star=");
        Object obj;
        if(flag)
            obj = "_offer&ext=png";
        else
            obj = Integer.valueOf(1);
        obj = stringbuilder.append(obj).append("&level=").append(k).append("&sizex=").append(i).append("&sizey=").append(j).append("&lon=").append(d).append("&lat=").append(d1).append("&map_only=1").toString();
        SDLogger.info((new StringBuilder()).append("Map Image URL: ").append(((String) (obj))).toString());
        return ((String) (obj));
    }

    public static String createURLMessageChangeSingleStatus(String s, String s1, String s2, String s3, String s4)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=message&act=set_read&uid=").append(s1).append("&source=").append(s3).append("&mids=").append(s2).append("&sts=").append(s4).append("&output=xml&no_cache=1&country=").append(s).append("&api=").append("2f4ddd9d469465022d26c61b521ffe329a952427").toString();
        SDLogger.info((new StringBuilder()).append("User Message Change Single Status URL: ").append(s).toString());
        return s;
    }

    public static String createURLMessageChangeSingleStatus(String s, String s1, ArrayList arraylist, String s2, String s3)
    {
        String s4 = "";
        for(int i = 0; i < arraylist.size() - 1; i++)
            s4 = (new StringBuilder()).append(s4).append(",").toString();

        String temp  = (new StringBuilder()).append(s4).append((String)arraylist.get(arraylist.size() - 1)).toString();
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=message&act=set_read&uid=").append(s1).append("&source=").append(s2).append("&mids=").append(temp).append("&sts=").append(s3).append("&output=xml&no_cache=1&country=").append(s).append("&api=").append("2f4ddd9d469465022d26c61b521ffe329a952427").toString();
        SDLogger.info((new StringBuilder()).append("User Message Change Status URL: ").append(s).toString());
        return s;
    }

    public static String createURLMessageDelete(String s, String s1, ArrayList arraylist, String s2, String s3)
    {
        s3 = "";
        for(int i = 0; i < arraylist.size() - 1; i++)
            s3 = (new StringBuilder()).append(s3).append(",").toString();

        String temp = (new StringBuilder()).append(s3).append((String)arraylist.get(arraylist.size() - 1)).toString();
        String temp2 = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=message&act=delete&uid=").append(s1).append("&source=").append(s2).append("&mids=").append(temp).append("&output=xml&no_cache=1&country=").append(s).append("&api=").append("2f4ddd9d469465022d26c61b521ffe329a952427").toString();
        SDLogger.info((new StringBuilder()).append("User Message Change Status URL: ").append(temp2).toString());
        return s;
    }

    public static String createURLMessageDeleteSingle(String s, String s1, String s2, String s3)
    {
        String temp = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=message&act=delete&uid=").append(s1).append("&source=").append(s3).append("&mids=").append(s2).append("&output=xml&no_cache=1&country=").append(s).append("&api=").append("2f4ddd9d469465022d26c61b521ffe329a952427").toString();
        SDLogger.info((new StringBuilder()).append("User Message Delete Single Status URL: ").append(temp).toString());
        return s;
    }

    public static String createURLMessageDetail(String s, String s1, String s2, String s3, String s4)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=message&act=detail&uid=").append(s1).append("&source=").append(s3).append("&mid=").append(s2).append("&att=").append(s4).append("&output=xml&no_cache=1&country=").append(s).append("&api=").append("2f4ddd9d469465022d26c61b521ffe329a952427").toString();
        SDLogger.info((new StringBuilder()).append("User Message Detail URL: ").append(s).toString());
        return s;
    }

    public static String createURLMessageEmptyTrash(String s, String s1)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=message&act=empty_trash&uid=").append(s1).append("&output=xml&no_cache=1&country=").append(s).append("&api=").append("2f4ddd9d469465022d26c61b521ffe329a952427").toString();
        SDLogger.info((new StringBuilder()).append("User Message Empty Trash URL: ").append(s).toString());
        return s;
    }

    public static String createURLMessageLog(String s, String s1, String s2, String s3)
    {
        s3 = encode(s3);
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=message&act=get_log&uid=").append(s1).append("&source=").append(s2).append("&tm=").append(s3).append("&output=xml&no_cache=1&country=").append(s).append("&api=").append("2f4ddd9d469465022d26c61b521ffe329a952427").toString();
        SDLogger.info((new StringBuilder()).append("User Message Log URL: ").append(s).toString());
        return s;
    }

    public static String createURLMessageTotalNew(String s, String s1)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=message&act=count_new&uid=").append(s1).append("&output=xml&no_cache=1&country=").append(s).append("&api=").append("2f4ddd9d469465022d26c61b521ffe329a952427").toString();
        SDLogger.info((new StringBuilder()).append("User Message Total New URL: ").append(s).toString());
        return s;
    }

    public static String createURLMoreOffers(int i, String s)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=offers&act=list_offer&output=xml&country=").append(s).append("&cid=").append(i).toString();
        SDLogger.info((new StringBuilder()).append("More Offers URL ").append(s).toString());
        return s;
    }

    public static String createURLNearbyCategory(String s, int i, int j)
    {
        return createURLResizeImage((new StringBuilder()).append("http://x1.sdimgs.com/img/mobile/nearby_category_2/").append(s).toString(), i, j);
    }

    public static String createURLNearbyCategoryList(String s, int i)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=nearby_2&act=category&output=xml&profile=iphone&country=").append(s).append("&vercat=").append(i).toString();
        SDLogger.info((new StringBuilder()).append("Nearby Category URL: ").append(s).toString());
        return s;
    }

    public static String createURLNearbyList(String s, double d, double d1, int i, float f, int j,
            int k, int l, int i1, int j1, int k1, String s1)
    {
        String s2 = "";
        String s3;
        if(i == 1)
            s2 = "location";
        else
        if(i == 2)
            s2 = "business";
        s3 = "";
        if(l != 0)
            if(i1 == 0)
                s3 = (new StringBuilder()).append("&category=").append(l).toString();
            else
                s3 = (new StringBuilder()).append("&group=").append(l).toString();
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=nearby_2&act=").append(s2).append("&output=xml&profile=").append(s1).append("&no_cache=1&country=").append(s).append(s3).append("&x=").append(d).append("&y=").append(d1).append("&dist=").append(f).append("&start=").append(j).append("&limit=").append(k).append("&pcid=").append(k1).append("&ver=").append(j1).toString();
        SDLogger.info((new StringBuilder()).append("Nearby URL: ").append(s).toString());
        return s;
    }

    public static String createURLNearbyList(String s, double d, double d1, int i, float f, int j,
            int k, int l, int i1, int j1, String s1)
    {
        String s2 = "";
        String s3;
        if(i == 1)
            s2 = "location";
        else
        if(i == 2)
            s2 = "business";
        s3 = "";
        if(l != 0)
            if(i1 == 0)
                s3 = (new StringBuilder()).append("&category=").append(l).toString();
            else
                s3 = (new StringBuilder()).append("&group=").append(l).toString();
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=nearby_2&act=").append(s2).append("&output=xml&profile=").append(s1).append("&no_cache=1&country=").append(s).append(s3).append("&x=").append(d).append("&y=").append(d1).append("&dist=").append(f).append("&start=").append(j).append("&limit=").append(k).append("&ver=").append(j1).toString();
        SDLogger.info((new StringBuilder()).append("Nearby URL: ").append(s).toString());
        return s;
    }

    public static String createURLOfferCategoryListing(String s)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=offers&act=filter&output=xml&no_cache=1&country=").append(s).toString();
        SDLogger.info((new StringBuilder()).append("Offers Category Listing URL ").append(s).toString());
        return s;
    }

    public static String createURLOfferVote(String s, int i, String s1, String s2, int j)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=promo&act=vote&output=xml&country=").append(s).append("&no_cache=1&cid=").append(i).append("&promo_id=").append(s1).append("&sd_uid=").append(s2).append("&vote=").append(j).append("").toString();
        SDLogger.info((new StringBuilder()).append("Business Offer Vote URL: ").append(s).toString());
        return s;
    }

    public static String createURLOffersListing(int i, int j, int k, String s)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=offers&act=all&output=xml&no_cache=1&country=").append(s).append("&start=").append(i).append("&limit=").append(j).append("&pcid=").append(k).toString();
        SDLogger.info((new StringBuilder()).append("Offers Listing URL ").append(s).toString());
        return s;
    }

    public static String createURLOffersListing(int i, int j, String s)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=offers&act=all&output=xml&no_cache=1&country=").append(s).append("&start=").append(i).append("&limit=").append(j).toString();
        SDLogger.info((new StringBuilder()).append("Offers Listing URL ").append(s).toString());
        return s;
    }

    public static String createURLOfflineMapImage(String s, int i, int j)
    {
        String s1 = (new StringBuilder()).append("http://www.streetdirectory.com/map/").append(s).toString();
        if(i == 0)
        {
            s = s1;
        }
        if(j != 0) {
            s = createURLResizeImage(s1, i, j);
        }
        SDLogger.info((new StringBuilder()).append("Offline Map Thumb Image URL: ").append(s).toString());
        return s;
    }

    public static String createURLOfflineMapList(int i)
    {
        String s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=map_package&no_cache=1&output=xml&ver=").append(i).toString();
        SDLogger.info((new StringBuilder()).append("Offline List URL: ").append(s).toString());
        return s;
    }

    public static String createURLOfflineMapPackageDetail(int i, int j)
    {
        String s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=map_package&no_cache=1&output=xml&id=").append(i).append("&ver=").append(j).toString();
        SDLogger.info((new StringBuilder()).append("Offline Map Package Detail URL: ").append(s).toString());
        return s;
    }

    public static String createURLOfflineMapPackageList(int i)
    {
        String s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=map_package&no_cache=1&output=xml&ver=").append(i).toString();
        SDLogger.info((new StringBuilder()).append("Offline Map Package List URL: ").append(s).toString());
        return s;
    }

    public static String createURLPostReplyTips(String s, String s1, int i, String s2, String s3, String s4, int j)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/tips/fb/save/?msg=").append(encode(s2)).append("&type=").append(i).append("&com_id=").append(s3).append("&id=").append(s1).append("&uid=").append(s4).append("&country=").append(s).append("&ver=").append(j).toString();
        SDLogger.info((new StringBuilder()).append("Post Reply Tips URL: ").append(s).toString());
        return s;
    }

    public static String createURLPostTips(String s, String s1, String s2, String s3, String s4, int i, int j)
    {
        String s5 = s4;
        if(s4 == null)
            s5 = "";
        s = (new StringBuilder()).append("http://www.streetdirectory.com/tips/fb/save/?msg=").append(encode(s2)).append("&type=").append(i).append("&id=").append(s1).append("&uid=").append(s3).append("&share=1&country=").append(s).append("&img=").append(s5).append("&ver=").append(j).toString();
        SDLogger.info((new StringBuilder()).append("Post Tips URL: ").append(s).toString());
        return s;
    }

    public static String createURLPreset()
    {
        String s = (new StringBuilder()).append("http://www.streetdirectory.com/map/preset.xml?").append(MathTools.randomEngine.nextInt()).toString();
        SDLogger.info((new StringBuilder()).append("Preset URL: ").append(s).toString());
        return s;
    }

    public static String createURLPrivacySave(String s, int i, int j, int k, int l, int i1, int j1)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=setting&act=save_privacy&output=xml&no_cache=1&uid=").append(s).append("&lkb=").append(i).append("&lkl=").append(j).append("&rot=").append(k).append("&sho=").append(l).append("&frd=").append(i1).append("&ver=").append(j1).toString();
        SDLogger.info((new StringBuilder()).append("Privacy Save URL: ").append(s).toString());
        return s;
    }

    public static String createURLPrivacyStatus(String s, int i)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=setting&act=get_privacy&output=xml&no_cache=1&uid=").append(s).append("&ver=").append(i).toString();
        SDLogger.info((new StringBuilder()).append("Privacy Status URL: ").append(s).toString());
        return s;
    }

    public static String createURLReceiptSubmition(String s)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=voucher&act=request&output=xml&country=").append(s).append("&no_cache=1&rand=").append((new Date()).getTime()).toString();
        SDLogger.info((new StringBuilder()).append("Submit Receipt URL ").append(s).toString());
        return s;
    }

    public static String createURLRegisterDevice(String s, String s1, String s2, String s3, String s4)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=device&act=register_device&output=xml&type=2&no_cache=1&token=").append(s1).append("&device_id=").append(s2).append("&app_id=").append(s3).append("&user_agent=").append(encode(s4)).append("&country=").append(s).toString();
        SDLogger.info((new StringBuilder()).append("Register Device URL: ").append(s).toString());
        return s;
    }

    public static String createURLResizeImage(String s, int i, int j)
    {
        return createURLResizeImage(s, i, j, 2);
    }

    public static String createURLResizeImage(String s, int i, int j, int k)
    {
        if(s.startsWith("http"))
            return (new StringBuilder()).append("http://www.streetdirectory.com/asia_travel/travel/resize_images.php?w=").append(i).append("&h=").append(j).append("&scale=").append(k).append("&url=").append(encode(s)).toString();
        else
            return (new StringBuilder()).append("http://www.streetdirectory.com/asia_travel/travel/resize_images.php?w=").append(i).append("&h=").append(j).append("&scale=").append(k).append("&url=").append(encode((new StringBuilder()).append("http://www.streetdirectory.com/stock_images/travel/simg.php?r=").append(s).toString())).toString();
    }

    public static String createURLResizeImage(String s, int i, int j, int k, int l)
    {
        if(s.startsWith("http"))
            s = (new StringBuilder()).append("http://www.streetdirectory.com/asia_travel/travel/resize_images.php?w=").append(i).append("&h=").append(j).append("&q=").append(l).append("&scale=").append(k).append("&url=").append(encode(s)).toString();
        else
            s = (new StringBuilder()).append("http://www.streetdirectory.com/asia_travel/travel/resize_images.php?w=").append(i).append("&h=").append(j).append("&q=").append(l).append("&scale=").append(k).append("&url=").append(encode((new StringBuilder()).append("http://www.streetdirectory.com/stock_images/travel/simg.php?r=").append(s).toString())).toString();
        SDLogger.info((new StringBuilder()).append("Offers Listing Image : ").append(s).toString());
        return s;
    }

    public static String createURLSearchList(String s, String s1, boolean flag, int i)
    {
        byte byte0 = 0;
        if(flag)
            byte0 = 2;
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=search&act=all&profile=sd_mobile&country=").append(s).append("&q=").append(encode(s1)).append("&lang=en&output=xml&start=0&limit=20&no_cache=1&show_additional=").append(byte0).append("&ver=").append(i).toString();
        SDLogger.info((new StringBuilder()).append("Search URL: ").append(s).toString());
        return s;
    }

    public static String createURLSendEmail(String s, String s1, String s2, String s3, ArrayList arraylist)
    {
        String s4 = "";
        for(int i = 0; i < arraylist.size(); i++)
        {
            String s6 = (String)((HashMap)arraylist.get(i)).get("t");
            String s5 = (new StringBuilder()).append(s4).append("&tdata[").append(i).append("][t]=").append(s6).toString();
            String s7 = (String)((HashMap)arraylist.get(i)).get("em");
            s4 = s5;
            if(s7 != null)
                s4 = (new StringBuilder()).append(s5).append("&tdata[").append(i).append("][em]=").append(s7).toString();
            s7 = (String)((HashMap)arraylist.get(i)).get("nm");
            s5 = s4;
            if(s7 != null)
            {
                s5 = encode(s7);
                s5 = (new StringBuilder()).append(s4).append("&tdata[").append(i).append("][nm]=").append(s5).toString();
            }
            s7 = (String)((HashMap)arraylist.get(i)).get("cy");
            s4 = s5;
            if(!s6.equalsIgnoreCase("B"))
                continue;
            s4 = s5;
            if(s7 != null)
                s4 = (new StringBuilder()).append(s5).append("&tdata[").append(i).append("][cy]=").append(s7).toString();
        }

        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=message&act=send").append(s4).append("&dev_uname=").append("sdsgmap").append("&sbj=&uid=").append(s2).append("&country=").append(s).append("&mt=").append(s1).append(s3).append("&output=xml&no_cache=1&api=").append("2f4ddd9d469465022d26c61b521ffe329a952427").toString();
        SDLogger.info((new StringBuilder()).append("Message Email URL: ").append(s).toString());
        return s;
    }

    public static String createURLSendEmailBusiness(String s, String s1, String s2, String s3, String s4, String s5, double d,
            double d1, ArrayList arraylist)
    {
        String s6;
        String s7;
        if(s5 == null)
            s5 = "";
        else
            s5 = encode(s5);
        if(d1 == 0.0D)
            s6 = "";
        else
            s6 = (new StringBuilder()).append("").append(d1).toString();
        if(d == 0.0D)
            s7 = "";
        else
            s7 = (new StringBuilder()).append("").append(d).toString();
        return createURLSendEmail(s, s1, s2, (new StringBuilder()).append("cid=").append(s3).append("&lid=").append(s4).append("&t=2&v=").append(s5).append("&x=").append(s6).append("&y=").append(s7).toString(), arraylist);
    }

    public static String createURLSendEmailCategory(String s, String s1, String s2, String s3, ArrayList arraylist)
    {
        int i;
        if(s1.equals("1.5"))
            i = 1;
        else
            i = 2;
        return createURLSendEmail(s, s1, s2, (new StringBuilder()).append("category=").append(s3).append("&t=").append(i).toString(), arraylist);
    }

    public static String createURLSendEmailDirection(String s, String s1, String s2, String s3, String s4, String s5, double d,
            double d1, String s6, String s7, String s8, double d2,
            double d3, ArrayList arraylist)
    {
        String s9;
        String s10;
        String s11;
        String s12;
        if(s5 == null)
            s5 = "";
        else
            s5 = encode(s5);
        if(d == 0.0D)
            s9 = "";
        else
            s9 = (new StringBuilder()).append("").append(d).toString();
        if(d1 == 0.0D)
            s10 = "";
        else
            s10 = (new StringBuilder()).append("").append(d1).toString();
        if(s8 == null)
            s8 = "";
        else
            s8 = encode(s8);
        if(d2 == 0.0D)
            s11 = "";
        else
            s11 = (new StringBuilder()).append("").append(d2).toString();
        if(d3 == 0.0D)
            s12 = "";
        else
            s12 = (new StringBuilder()).append("").append(d3).toString();
        return createURLSendEmail(s, s1, s2, (new StringBuilder()).append(s4).append("t1=").append(s3).append("&v1=").append(s5).append("&x1=").append(s9).append("&y1=").append(s10).append(s7).append("t2=").append(s6).append("&v2=").append(s8).append("&x2=").append(s11).append("&y2=").append(s12).toString(), arraylist);
    }

    public static String createURLSendEmailLocation(String s, String s1, String s2, String s3, String s4, String s5, double d,
            double d1, ArrayList arraylist)
    {
        String s6;
        String s7;
        if(s5 == null)
            s5 = "";
        else
            s5 = encode(s5);
        if(d1 == 0.0D)
            s6 = "";
        else
            s6 = (new StringBuilder()).append("").append(d1).toString();
        if(d == 0.0D)
            s7 = "";
        else
            s7 = (new StringBuilder()).append("").append(d).toString();
        return createURLSendEmail(s, s1, s2, (new StringBuilder()).append("pid=").append(s3).append("&aid=").append(s4).append("&t=1&v=").append(s5).append("&x=").append(s6).append("&y=").append(s7).toString(), arraylist);
    }

    public static String createURLSendEmailOffer(String s, int i, int j, String s1, String s2)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=visitor_contact&act=add_visitor&output=xml&tracking=sdapps&no_cache=1&company_id=").append(i).append("&link_id=").append(j).append("&offer_id=").append(s1).append("&country=").append(s).append("&visitor_email=").append(s2).toString();
        SDLogger.info((new StringBuilder()).append("Send Email Offer URL: ").append(s).toString());
        return s;
    }

    public static String createURLSendMessage(String s, String s1, String s2, String s3, ArrayList arraylist)
    {
        String s5 = "";
        for(int i = 0; i < arraylist.size(); i++)
        {
            String s6 = (String)((HashMap)arraylist.get(i)).get("t");
            s5 = (new StringBuilder()).append(s5).append("&tdata[").append(i).append("][t]=").append(s6).toString();
            String s7 = (String)((HashMap)arraylist.get(i)).get("uid");
            String s4 = s5;
            if(s7 != null)
                s4 = (new StringBuilder()).append(s5).append("&tdata[").append(i).append("][tuid]=").append(s7).toString();
            s7 = (String)((HashMap)arraylist.get(i)).get("cy");
            s5 = s4;
            if(!s6.equalsIgnoreCase("B"))
                continue;
            s5 = s4;
            if(s7 != null)
                s5 = (new StringBuilder()).append(s4).append("&tdata[").append(i).append("][cy]=").append(s7).toString();
        }

        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=message&act=send").append(s5).append("&dev_uname=").append("sdsgmap").append("&sbj=&uid=").append(s2).append("&country=").append(s).append("&mt=").append(s1).append(s3).append("&output=xml&no_cache=1&api=").append("2f4ddd9d469465022d26c61b521ffe329a952427").toString();
        SDLogger.info((new StringBuilder()).append("Message URL: ").append(s).toString());
        return s;
    }

    public static String createURLSendMessageBusiness(String s, String s1, String s2, String s3, String s4, String s5, double d,
            double d1, ArrayList arraylist)
    {
        String s6;
        String s7;
        if(s5 == null)
            s5 = "";
        else
            s5 = encode(s5);
        if(d1 == 0.0D)
            s6 = "";
        else
            s6 = (new StringBuilder()).append("").append(d1).toString();
        if(d == 0.0D)
            s7 = "";
        else
            s7 = (new StringBuilder()).append("").append(d).toString();
        return createURLSendMessage(s, s1, s2, (new StringBuilder()).append("cid=").append(s3).append("&lid=").append(s4).append("&t=2&v=").append(s5).append("&x=").append(s6).append("&y=").append(s7).toString(), arraylist);
    }

    public static String createURLSendMessageCategory(String s, String s1, String s2, String s3, ArrayList arraylist)
    {
        int i;
        if(s1.equals("1.5"))
            i = 1;
        else
            i = 2;
        return createURLSendMessage(s, s1, s2, (new StringBuilder()).append("category=").append(s3).append("&t=").append(i).toString(), arraylist);
    }

    public static String createURLSendMessageDirection(String s, String s1, String s2, String s3, String s4, String s5, double d,
            double d1, String s6, String s7, String s8, double d2,
            double d3, ArrayList arraylist)
    {
        String s9;
        String s10;
        String s11;
        String s12;
        if(s5 == null)
            s5 = "";
        else
            s5 = encode(s5);
        if(d == 0.0D)
            s9 = "";
        else
            s9 = (new StringBuilder()).append("").append(d).toString();
        if(d1 == 0.0D)
            s10 = "";
        else
            s10 = (new StringBuilder()).append("").append(d1).toString();
        if(s8 == null)
            s8 = "";
        else
            s8 = encode(s8);
        if(d2 == 0.0D)
            s11 = "";
        else
            s11 = (new StringBuilder()).append("").append(d2).toString();
        if(d3 == 0.0D)
            s12 = "";
        else
            s12 = (new StringBuilder()).append("").append(d3).toString();
        return createURLSendMessage(s, s1, s2, (new StringBuilder()).append(s4).append("t1=").append(s3).append("&v1=").append(s5).append("&x1=").append(s9).append("&y1=").append(s10).append(s7).append("t2=").append(s6).append("&v2=").append(s8).append("&x2=").append(s11).append("&y2=").append(s12).toString(), arraylist);
    }

    public static String createURLSendMessageGroupNearby(String s, String s1, String s2, String s3, ArrayList arraylist)
    {
        int i;
        if(s1.equals("1.5"))
            i = 1;
        else
            i = 2;
        return createURLSendMessage(s, s1, s2, (new StringBuilder()).append("group_nearby=").append(s3).append("&t=").append(i).toString(), arraylist);
    }

    public static String createURLSendMessageLocation(String s, String s1, String s2, String s3, String s4, String s5, double d,
            double d1, ArrayList arraylist)
    {
        String s6;
        String s7;
        if(s5 == null)
            s5 = "";
        else
            s5 = encode(s5);
        if(d1 == 0.0D)
            s6 = "";
        else
            s6 = (new StringBuilder()).append("").append(d1).toString();
        if(d == 0.0D)
            s7 = "";
        else
            s7 = (new StringBuilder()).append("").append(d).toString();
        return createURLSendMessage(s, s1, s2, (new StringBuilder()).append("pid=").append(s3).append("&aid=").append(s4).append("&t=1&v=").append(s5).append("&x=").append(s6).append("&y=").append(s7).toString(), arraylist);
    }

    public static String createURLSendSmsLink(String s, String s1, int i, String s2, int j)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=message&act=share_url&dev_uname=sdsgmap&country=").append(s).append("&mt=").append(s1).append("&t=").append(i).append(s2).append("&output=xml&no_cache=1&ver=").append(j).toString();
        SDLogger.info((new StringBuilder()).append("SMS Link URL: ").append(s).toString());
        return s;
    }

    public static String createURLSendSmsLinkBusStop(String s, String s1, String s2, String s3, String s4, int i)
    {
        return createURLSendSmsLink(s, s1, 1, (new StringBuilder()).append("pid=").append(s2).append("aid=").append(s3).append("busstopid=").append(s4).toString(), i);
    }

    public static String createURLSendSmsLinkBusiness(String s, String s1, String s2, String s3, int i)
    {
        return createURLSendSmsLink(s, s1, 2, (new StringBuilder()).append("cid=").append(s2).append("lid=").append(s3).toString(), i);
    }

    public static String createURLSendSmsLinkCategory(String s, String s1, String s2, int i)
    {
        s2 = (new StringBuilder()).append("category=").append(s2).toString();
        int j;
        if(s1.equals("1.5"))
            j = 1;
        else
            j = 2;
        return createURLSendSmsLink(s, s1, j, s2, i);
    }

    public static String createURLSendSmsLinkERP(String s, String s1, String s2, String s3, String s4, int i)
    {
        return createURLSendSmsLink(s, s1, 1, (new StringBuilder()).append("pid=").append(s2).append("aid=").append(s3).append("erpid=").append(s4).toString(), i);
    }

    public static String createURLSendSmsLinkLocation(String s, String s1, String s2, String s3, int i)
    {
        return createURLSendSmsLink(s, s1, 1, (new StringBuilder()).append("pid=").append(s2).append("aid=").append(s3).toString(), i);
    }

    public static String createURLSendSmsOffer(String s, int i, int j, String s1, String s2)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=visitor_contact&act=add_visitor&output=xml&tracking=sdapps&no_cache=1&company_id=").append(i).append("&link_id=").append(j).append("&offer_id=").append(s1).append("&visitor_phone=").append(s2).append("&country=").append(s).toString();
        SDLogger.info((new StringBuilder()).append("Send SMS Offer URL: ").append(s).toString());
        return s;
    }

    public static String createURLShareBusinessListing(int i, String s)
    {
        if(i == 11342)
            s = "Offers";
        s = (new StringBuilder()).append("http://www.streetdirectory.com/businessfinder/company/").append(i).append("/").append(s.replaceAll(" ", "_")).append("/").toString();
        SDLogger.info((new StringBuilder()).append("Share Business Listing URL: ").append(s).toString());
        return s;
    }

    public static String createURLSimilarBusinessList(String s, int i, int j, int k, int l)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=business&act=similar_business&output=xml&no_cache=1&country=").append(s).append("&cid=").append(j).append("&stateID=").append(i).append("&limit=").append(k).append("&ver=").append(l).toString();
        SDLogger.info((new StringBuilder()).append("Similar Business URL: ").append(s).toString());
        return s;
    }

    public static String createURLSiteBanner(String s, String s1, int i, int j)
    {
        String s2;
        if(i < 30 && j < 30)
            s2 = "30_x_30";
        else
        if(i < 39 && j < 39)
            s2 = "39_x_39";
        else
        if(i < 47 && j < 39)
            s2 = "47_x_39";
        else
        if(i < 50 && j < 50)
            s2 = "50_x_50";
        else
        if(i < 89 && j < 75)
            s2 = "89_x_75";
        else
        if(i < 108 && j < 90)
            s2 = "108_x_90";
        else
        if(i < 128 && j < 102)
            s2 = "128_x_102";
        else
        if(i < 140 && j < 122)
            s2 = "140_x_122";
        else
        if(i < 188 && j < 158)
            s2 = "188_x_158";
        else
        if(i < 203 && j < 175)
            s2 = "203_x_175";
        else
            s2 = "238_x_208";
        if(s.equals("sg"))
            return (new StringBuilder()).append(createSiteBannerBaseURL()).append("/").append(s2).append("/").append(s1).toString();
        else
            return (new StringBuilder()).append(createSiteBannerBaseURL()).append("_").append(s).append("/").append(s2).append("/").append(s1).toString();
    }

    public static String createURLSittServerNodeListing(long l)
    {
        String s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=business_offers_and_wifi&act=get_all_wifi&output=xml&min_timestamp=").append(l).toString();
        SDLogger.info((new StringBuilder()).append("Sitt Server Node List With Modified Time URL ").append(s).toString());
        return s;
    }

    public static String createURLSittServerNodeListing(String s)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=business_offers_and_wifi&act=get_offers_and_wifi_by_list_bssid&output=xml&bssid=").append(s).toString();
        SDLogger.info((new StringBuilder()).append("Sitt Server Node List URL ").append(s).toString());
        return s;
    }

    public static String createURLStateList(String s, int i)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=state&act=listing&output=xml&country=").append(s).append("&vercat=").append(i).toString();
        SDLogger.info((new StringBuilder()).append("State List URL: ").append(s).toString());
        return s;
    }

    public static String createURLTipsReplyList(String s, String s1, int i, int j, int k)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=tips&act=reply_data&output=xml&no_cache=1&country=").append(s).append("&com_id=").append(s1).append("&start=").append(i).append("&limit=").append(j).append("&ver=").append(k).toString();
        SDLogger.info((new StringBuilder()).append("Tips Reply List URL: ").append(s).toString());
        return s;
    }

    public static String createURLTrafficCamera(String s, String s1, String s2)
    {
        Random random = new Random();
        if("my".equals(s2))
            return (new StringBuilder()).append("http://www.streetdirectory.com/").append("api/html/camera.php?type=").append(s).append("&country=my&title=").append(s1).append("&mod_ver=2&ver=").append(random.nextInt(100)).toString();
        if("id".equals(s2))
            return (new StringBuilder()).append("http://www.streetdirectory.com/").append("api/html/camera.php?type=").append(s).append("&country=id&title=").append(s1).append("&mod_ver=2&ver=").append(random.nextInt(100)).toString();
        else
            return (new StringBuilder()).append("http://www.streetdirectory.com/").append("iphone/iframe/traffic_iframe_native.php?type=").append(s).append("&title=").append(s1).append("&mod_ver=2&ver=").append(random.nextInt(100)).toString();
    }

    public static String createURLTrafficCameraImageLink(String s, int i, int j, int k)
    {
        String s1 = (new SimpleDateFormat("hhmm")).format(new Date());
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=traffic_camera&act=detail&output=xml&no_cache=1&country=").append(s).append("&pid=").append(i).append("&aid=").append(j).append("&rnd=").append(s1).append("&ver=").append(k).toString();
        SDLogger.info((new StringBuilder()).append("Traffic Camera Image Link URL: ").append(s).toString());
        return s;
    }

    public static String createURLUserInfoStatus(String s, String s1, int i)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=people&act=detail&output=xml&no_cache=1&uid=").append(s).append("&country=").append(s1).append("&ver=").append(i).toString();
        SDLogger.info((new StringBuilder()).append("User Info Status URL: ").append(s).toString());
        return s;
    }

    public static String createURLUserMessageHeader(String s, String s1, String s2, String s3, String s4, int i, int j)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=message&act=").append(s2).append("&uid=").append(s1).append("&output=xml&no_cache=1&country=").append(s).append("&lt=").append(s4).append("&gt=").append(s3).append("&start=").append(i).append("&limit=").append(j).append("&api=").append("2f4ddd9d469465022d26c61b521ffe329a952427").toString();
        SDLogger.info((new StringBuilder()).append("User Message Header URL: ").append(s).toString());
        return s;
    }

    public static String createURLVoucherList(String s)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=voucher&act=get&output=xml&country=").append(s).toString();
        SDLogger.info((new StringBuilder()).append("Voucher List URL ").append(s).toString());
        return s;
    }

    public static String createURLVoucherTermsAndConditions()
    {
        SDLogger.info((new StringBuilder()).append("Voucher Terms and Conditions URL ").append("http://www.streetdirectory.com/api/?mode=voucher&act=tc&output=html&country=sg").toString());
        return "http://www.streetdirectory.com/api/?mode=voucher&act=tc&output=html&country=sg";
    }

    public static String createURLYourFeedback(String s, String s1, String s2, String s3, String s4)
    {
        s = (new StringBuilder()).append("http://www.streetdirectory.com/api/?mode=feedback&act=add&output=xml&country=").append(s).append("&msg=").append(encode(s1)).append("&nt=").append(encode(s2)).append("&uid=").append(s3).append("&user_email=").append(s4).append("&no_cache=1").toString();
        SDLogger.info((new StringBuilder()).append("Your Feedback URL: ").append(s).toString());
        return s;
    }

    public static String encode(String s)
    {
        try
        {
            s = URLEncoder.encode(s, "UTF-8");
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            return "";
        }
        return s;
    }

    public static String getGantModelString()
    {
        if(GANT_BUILD_MODEL == null)
            GANT_BUILD_MODEL = "/android";
        return GANT_BUILD_MODEL;
    }

    public static final String API_KEY = "2f4ddd9d469465022d26c61b521ffe329a952427";
    public static final String API_URL = "http://www.streetdirectory.com/api/";
    public static final String BASE_URL = "http://www.streetdirectory.com/";
    public static final String BROADCAST_POP_UP_MESSAGE = "pop_up_message";
    public static final String DEFAULT_PROTOCOL = "sdsgmap";
    public static String GANT_BUILD_MODEL = null;
    public static final String HTTP_IMAGE_RESIZE_URL = "http://www.streetdirectory.com/asia_travel/travel/resize_images.php";
    public static final String HTTP_IMAGE_URL = "http://www.streetdirectory.com/stock_images/travel/simg.php?r=";
    public static final String IMAGE_BASE_URL = "http://x1.sdimgs.com/";
    public static final String MAP_DOMAIN_URL = "http://www.streetdirectory.com/";
    public static final String NOTIF_CLICK = "click";
    public static final String NOTIF_VIEW = "view";
    public static String REFERER;
    public static final int RESIZE_CLOSEST_SCALE = 2;
    public static final int RESIZE_PROPORTIONAL_FIT = 1;
    public static final int RESIZE_STRETCH = 0;

    static
    {
        REFERER = (new StringBuilder()).append(Build.MANUFACTURER).append("_").append(Build.MODEL).append("_").append(android.os.Build.VERSION.RELEASE).append("_").append(SDApplication.getGid()).toString();
    }
}
