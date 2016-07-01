

package streetdirectory.mobile.modules.share.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class ShareSmsLinkServiceInput extends SDHttpServiceInput
{

    public ShareSmsLinkServiceInput()
    {
    }

    public ShareSmsLinkServiceInput(String s, int i, String s1, String s2)
    {
        super(s);
        type = i;
        shareType = s1;
        categoryID = s2;
    }

    public ShareSmsLinkServiceInput(String s, int i, String s1, String s2, String s3)
    {
        super(s);
        type = i;
        shareType = s1;
        if(2 == i)
        {
            companyID = s2;
            locationID = s3;
            return;
        } else
        {
            placeID = s2;
            addressID = s3;
            return;
        }
    }

    public ShareSmsLinkServiceInput(String s, int i, String s1, String s2, String s3, String s4)
    {
        super(s);
        type = i;
        shareType = s1;
        placeID = s2;
        addressID = s3;
        if("1.22".equals(s1))
            busStopID = s4;
        else
        if("1.23".equals(s1))
        {
            erpID = s4;
            return;
        }
    }

    public String getURL()
    {
        if("1.5".equals(shareType) || "2.5".equals(shareType))
            return URLFactory.createURLSendSmsLinkCategory(countryCode, shareType, categoryID, apiVersion);
        if(type == 2)
            return URLFactory.createURLSendSmsLinkBusiness(countryCode, shareType, companyID, locationID, apiVersion);
        if(type == 1)
        {
            if("1.22".equals(shareType))
                return URLFactory.createURLSendSmsLinkBusStop(countryCode, shareType, placeID, addressID, busStopID, apiVersion);
            if("1.23".equals(shareType))
                return URLFactory.createURLSendSmsLinkERP(countryCode, shareType, placeID, addressID, erpID, apiVersion);
            else
                return URLFactory.createURLSendSmsLinkLocation(countryCode, shareType, placeID, addressID, apiVersion);
        } else
        {
            return "";
        }
    }

    public String addressID;
    public String busStopID;
    public String categoryID;
    public String companyID;
    public String erpID;
    public String locationID;
    public String placeID;
    public String shareType;
    public int type;
}
