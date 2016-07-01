

package streetdirectory.mobile.modules.settings.addbusiness.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class AddNewPlaceServiceInput extends SDHttpServiceInput
{

    public AddNewPlaceServiceInput()
    {
    }

    public AddNewPlaceServiceInput(String s, String s1, String s2, String s3, String s4, int i, String s5,
            String s6, String s7, String s8, double d, double d1,
            double d2, double d3)
    {
        super(s);
        message = s1;
        uid = s2;
        url = s3;
        url2 = s4;
        type = i;
        placeName = s5;
        address = s6;
        name = s7;
        phone = s8;
        gpsLongitude = d;
        gpsLatitude = d1;
        pannedLongitude = d2;
        pannedLatitude = d3;
    }

    public String getURL()
    {
        return URLFactory.createURLAddNewPlace(message, uid, url, url2, type, placeName, address, name, phone, gpsLongitude, gpsLatitude, pannedLongitude, pannedLatitude);
    }

    public String address;
    public double gpsLatitude;
    public double gpsLongitude;
    public String message;
    public String name;
    public double pannedLatitude;
    public double pannedLongitude;
    public String phone;
    public String placeName;
    public int type;
    public String uid;
    public String url;
    public String url2;
}
