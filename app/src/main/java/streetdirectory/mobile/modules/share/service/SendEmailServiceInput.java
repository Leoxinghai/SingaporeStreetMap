

package streetdirectory.mobile.modules.share.service;

import java.util.ArrayList;
import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class SendEmailServiceInput extends SDHttpServiceInput
{

    public SendEmailServiceInput()
    {
    }

    public SendEmailServiceInput(String s, String s1, int i, String s2, String s3)
    {
        super(s);
        type = i;
        uid = s1;
        shareType = s2;
        categoryID = s3;
    }

    public SendEmailServiceInput(String s, String s1, int i, String s2, String s3, String s4, String s5,
            double d, double d1)
    {
        super(s);
        type = i;
        uid = s1;
        shareType = s2;
        if(i != 2) {
			if(i == 1)
			{
				placeID = s3;
				addressID = s4;
				placeName = s5;
			}
		} else {
			companyID = s3;
			locationID = s4;
			businessName = s5;
		}
        longitude = d;
        latitude = d1;
        return;
    }

    public SendEmailServiceInput(String s, String s1, String s2, String s3, String s4, String s5, double d, double d1, String s6, String s7, String s8, double d2, double d3)
    {
        super(s);
        uid = s1;
        shareType = "9.1";
        originType = s3;
        originDataString = s4;
        originName = s5;
        originLongitude = d;
        originLatitude = d1;
        destinationType = s6;
        destinationDataString = s7;
        destinationName = s8;
        destinationLongitude = d2;
        destinationLatitude = d3;
    }

    public String getURL()
    {
        if("1.5".equals(shareType) || "2.5".equals(shareType))
            return URLFactory.createURLSendEmailCategory(countryCode, shareType, uid, categoryID, contactDataArray);
        if("9.1".equals(shareType))
            return URLFactory.createURLSendEmailDirection(countryCode, shareType, uid, originType, originDataString, originName, originLongitude, originLatitude, destinationType, destinationDataString, destinationName, destinationLongitude, destinationLatitude, contactDataArray);
        if(type == 2)
            return URLFactory.createURLSendEmailBusiness(countryCode, shareType, uid, companyID, locationID, businessName, latitude, longitude, contactDataArray);
        if(type == 1)
            return URLFactory.createURLSendEmailLocation(countryCode, shareType, uid, placeID, addressID, placeName, destinationLatitude, destinationLongitude, contactDataArray);
        else
            return "";
    }

    public String addressID;
    public String businessName;
    public String categoryID;
    public String companyID;
    public ArrayList contactDataArray;
    public String destinationDataString;
    public double destinationLatitude;
    public double destinationLongitude;
    public String destinationName;
    public String destinationType;
    public double latitude;
    public String locationID;
    public double longitude;
    public String originDataString;
    public double originLatitude;
    public double originLongitude;
    public String originName;
    public String originType;
    public String placeID;
    public String placeName;
    public String shareType;
    public int type;
    public String uid;
}
