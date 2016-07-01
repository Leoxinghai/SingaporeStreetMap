

package streetdirectory.mobile.modules.direction;

import java.util.HashMap;
import streetdirectory.mobile.service.SDDataOutput;

public class Carpark extends SDDataOutput
{

    public Carpark()
    {
    }

    public void populateData()
    {
        try
        {
            dx = Double.parseDouble((String)hashData.get("dx"));
        }
        catch(Exception exception)
        {
            dx = 0.0D;
        }
        try
        {
            dy = Double.parseDouble((String)hashData.get("dy"));
        }
        catch(Exception exception1)
        {
            dy = 0.0D;
        }
        try
        {
            distance = Double.parseDouble((String)hashData.get("distsq_place"));
        }
        catch(Exception exception2)
        {
            distance = 0.0D;
        }
        id = (String)hashData.get("id");
        location = (String)hashData.get("location");
        address = (String)hashData.get("address");
        weekdayAM = (String)hashData.get("weekday_am");
        weekdayPM = (String)hashData.get("weekday_pm");
        saturday = (String)hashData.get("saturday");
        sundayHoliday = (String)hashData.get("sunday_holiday");
        remarks = (String)hashData.get("remarks");
        sourceProvider = (String)hashData.get("source_provider");
        placeName = (String)hashData.get("place_name");
        streetName = (String)hashData.get("streetname");
        try
        {
            longitude = Double.parseDouble((String)hashData.get("longitude"));
        }
        catch(Exception exception3)
        {
            longitude = 0.0D;
        }
        try
        {
            latitude = Double.parseDouble((String)hashData.get("latitude"));
            return;
        }
        catch(Exception exception4)
        {
            latitude = 0.0D;
        }
    }

    private static final long serialVersionUID = 0x4de08171b7a1094dL;
    public String address;
    public double distance;
    public String distanceString;
    public double dx;
    public double dy;
    public String id;
    public double latitude;
    public String location;
    public double longitude;
    public String placeName;
    public String remarks;
    public String saturday;
    public String sourceProvider;
    public String streetName;
    public String sundayHoliday;
    public String weekdayAM;
    public String weekdayPM;
}
