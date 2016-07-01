

package streetdirectory.mobile.modules.direction;

import java.util.HashMap;

public class JourneyPointDetail
{

    public JourneyPointDetail()
    {
        hashData = new HashMap();
    }

    public JourneyPointDetail(HashMap hashmap)
    {
        hashData = hashmap;
    }

    public void populateData()
    {
        title = (String)hashData.get("title");
        desc = (String)hashData.get("desc");
        directionEnum = (String)hashData.get("direction_enum");
        try
        {
            longitude = Double.parseDouble((String)hashData.get("x"));
        }
        catch(Exception exception)
        {
            longitude = 0.0D;
        }
        try
        {
            latitude = Double.parseDouble((String)hashData.get("y"));
        }
        catch(Exception exception1)
        {
            latitude = 0.0D;
        }
        try
        {
            index = Integer.parseInt((String)hashData.get("index"));
        }
        catch(Exception exception2)
        {
            index = 0;
        }
        time = (String)hashData.get("time");
        distanceInUnit = (String)hashData.get("unit");
        try
        {
            stdTime = Double.parseDouble((String)hashData.get("std_time"));
        }
        catch(Exception exception3)
        {
            stdTime = 0.0D;
        }
        try
        {
            stdDistance = Double.parseDouble((String)hashData.get("std_distance"));
            return;
        }
        catch(Exception exception4)
        {
            stdDistance = 0.0D;
        }
    }

    public String desc;
    public String directionEnum;
    public String distanceInUnit;
    public HashMap hashData;
    public int index;
    public double latitude;
    public double longitude;
    public double stdDistance;
    public double stdTime;
    public String time;
    public String title;
}
