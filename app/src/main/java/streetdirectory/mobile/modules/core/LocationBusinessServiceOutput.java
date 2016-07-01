

package streetdirectory.mobile.modules.core;

import android.content.Context;
import android.text.Html;
import java.text.DecimalFormat;
import java.util.HashMap;
import streetdirectory.mobile.service.SDDataOutput;
import streetdirectory.mobile.service.URLFactory;

public class LocationBusinessServiceOutput extends SDDataOutput
{

    public LocationBusinessServiceOutput()
    {
    }

    public LocationBusinessServiceOutput(HashMap hashmap)
    {
        super(hashmap);
    }

    public String generateImageURL(Context context, int i, int j)
    {
        Object obj;
        obj = null;
        String temp = null;
        if(offer != null && offerThumbnailImage != null)
            return URLFactory.createURLResizeImage(offerThumbnailImage, 384, 384, 1, 40);
        if(imageURL == null) {
            //temp = obj;
            if(siteBanner != null)
            {
                //temp = obj;
                if(siteBanner.length() > 2)
                    temp = URLFactory.createURLSiteBanner(countryCode, siteBanner, i, j);
            }
        } else {
            temp = URLFactory.createURLResizeImage(imageURL, i, j);
        }

        return temp;
    }

    public String generateSiteBannerURL(int i, int j)
    {
        return URLFactory.createURLSiteBanner(countryCode, siteBanner, i, j);
    }

    public void populateData()
    {
        boolean flag;
        populateData();
        Object obj;
        try
        {
            companyID = Integer.parseInt((String)hashData.get("cid"));
        }
        catch(Exception exception)
        {
            companyID = -1;
        }
        try
        {
            locationID = Integer.parseInt((String)hashData.get("lid"));
        }
        catch(Exception exception1)
        {
            locationID = -1;
        }
        try
        {
            placeID = Integer.parseInt((String)hashData.get("pid"));
        }
        catch(Exception exception2)
        {
            placeID = -1;
        }
        try
        {
            addressID = Integer.parseInt((String)hashData.get("aid"));
        }
        catch(Exception exception3)
        {
            addressID = -1;
        }
        try
        {
            stateID = Integer.parseInt((String)hashData.get("stid"));
        }
        catch(Exception exception4)
        {
            stateID = 0;
        }
        obj = (String)hashData.get("cat");
        if(obj != null)
            try
            {
                categoryID = Integer.parseInt(((String) (obj)).split(",")[0]);
            }
            catch(Exception exception5)
            {
                categoryID = -1;
            }
        try
        {
            typeID = Integer.parseInt((String)hashData.get("t"));
        }
        catch(Exception exception6)
        {
            typeID = 1;
        }
        if(typeID == 1 || typeID == 4)
            type = 1;
        else
        if(typeID == 2)
            type = 2;
        else
            type = 11;
        uniqueID = (String)hashData.get("id");
        if(uniqueID == null)
            if(type == 1)
                uniqueID = (new StringBuilder()).append(placeID).append("_").append(addressID).toString();
            else
            if(type == 2)
                uniqueID = (new StringBuilder()).append(companyID).append("_").append(locationID).toString();

        venue = (String)hashData.get("v");
        address = (String)hashData.get("a");
        if(address != null)
        {
            address = address.trim();
            if("".equals(address))
                address = null;
        }
        placeName = (String)hashData.get("pcn");
        unitNo = (String)hashData.get("uno");
        if(unitNo != null)
            unitNo = unitNo.trim();
        phoneNumber = (String)hashData.get("ph");
        if(phoneNumber == null)
            phoneNumber = (String)hashData.get("phn");
        busNumbers = (String)hashData.get("bus");
        try
        {
            longitude = Double.parseDouble((String)hashData.get("x"));
            latitude = Double.parseDouble((String)hashData.get("y"));
        }
        catch(Exception exception7)
        {
            longitude = 0.0D;
            latitude = 0.0D;
        }
        Exception exception12;
        try
        {
            aboutUs = Html.fromHtml((String)hashData.get("ab")).toString();
        }
        catch(Exception exception13) { }
        try
        {
            totalBusiness = Integer.parseInt((String)hashData.get("tbiz"));
        }
        catch(Exception exception8)
        {
            totalBusiness = 0;
        }
        adsLabel = (String)hashData.get("ta");
        adOperatingHours = (String)hashData.get("ad_op");
        adPromotion = (String)hashData.get("ad_prm");
        offer = (String)hashData.get("offer");
        promoId = (String)hashData.get("prm_id");
        if(promoId == null)
            promoId = (String)hashData.get("offer_id");
        promoPlus = (String)hashData.get("prm_plus");
        promoMinus = (String)hashData.get("prm_minus");
        promoVote = (String)hashData.get("prm_vote");
        offerThumbnailImage = (String)hashData.get("oimg");
        offerImage = (String)hashData.get("offer_img");
        offerValidDate = (String)hashData.get("vd");
        offerTermsAndCondition = (String)hashData.get("offer_tc");
        offerAbout = (String)hashData.get("offer_about");
        linkOffer = (String)hashData.get("ol");
        isSubmitMethodSms = "1".equals(hashData.get("osm"));
        try
        {
            offerViewCount = Integer.parseInt((String)hashData.get("ovc"));
        }
        catch(Exception exception9)
        {
            offerViewCount = 1;
        }
        try
        {
            totalOffer = Integer.parseInt((String)hashData.get("tof"));
        }
        catch(Exception exception10)
        {
            totalOffer = 1;
        }
        siteBanner = (String)hashData.get("sb");
        imageURL = (String)hashData.get("img");
        isPremium = "1".equals(hashData.get("prem"));
        if(!"1".equals(hashData.get("nvld")))
            flag = true;
        else
            flag = false;
        isNotValid = flag;
        isPaid = "1".equals(hashData.get("paid"));
        try
        {
            redeemVoucherTemplate = Integer.parseInt((String)hashData.get("rvt"));
        }
        catch(Exception exception11)
        {
            redeemVoucherTemplate = 1;
        }
        distanceInMeter = Double.parseDouble((String)hashData.get("d"));
        if(distanceInMeter >= 1000D)
        {
            obj = new DecimalFormat("###.##");
            distance = (new StringBuilder()).append(((DecimalFormat) (obj)).format(distanceInMeter / 1000D)).append("km").toString();
            return;
        }
        try
        {
            distance = String.format("%.0fm", new Object[] {
                Double.valueOf(distanceInMeter)
            });
            return;
        }
        // Misplaced declaration of an exception variable
        catch(Exception exception122)
        {
            distanceInMeter = 0.0D;
        }
        distance = null;
        return;
    }

    public static final streetdirectory.mobile.service.SDOutput.Creator CREATOR = new Creator(LocationBusinessServiceOutput.class);
    private static final long serialVersionUID = 0xcfb66761cf83af83L;
    public String aboutUs;
    public String adOperatingHours;
    public String adPromotion;
    public String address;
    public int addressID;
    public String adsLabel;
    public String busNumbers;
    public int categoryID;
    public int companyID;
    public String distance;
    public double distanceInMeter;
    public String imageURL;
    public boolean isNotValid;
    public boolean isPaid;
    public boolean isPremium;
    public boolean isSubmitMethodSms;
    public double latitude;
    public String linkOffer;
    public int locationID;
    public double longitude;
    public String offer;
    public String offerAbout;
    public String offerImage;
    public String offerTermsAndCondition;
    public String offerThumbnailImage;
    public String offerValidDate;
    public int offerViewCount;
    public String phoneNumber;
    public int placeID;
    public String placeName;
    public String promoId;
    public String promoMinus;
    public String promoPlus;
    public String promoVote;
    public int redeemVoucherTemplate;
    public String siteBanner;
    public int stateID;
    public int totalBusiness;
    public int totalOffer;
    public int type;
    public int typeID;
    public String uniqueID;
    public String unitNo;
    public String venue;

}
