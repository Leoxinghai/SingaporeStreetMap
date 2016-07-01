

package streetdirectory.mobile.modules.direction;

import java.util.HashMap;

// Referenced classes of package streetdirectory.mobile.modules.direction:
//            JourneySummary

public class JourneyDrivingSummary extends JourneySummary
{

    public JourneyDrivingSummary(HashMap hashmap)
    {
        super(hashmap);
    }

    public void populateData()
    {
        super.populateData();
        title = (String)hashData.get("title");
        description = (String)hashData.get("desc");
        try
        {
            index = Integer.parseInt((String)hashData.get("index"));
        }
        catch(Exception exception)
        {
            index = 0;
        }
        directionEnum = (String)hashData.get("direction_enum");
        try
        {
            longitude = Double.parseDouble((String)hashData.get("x"));
        }
        catch(Exception exception1)
        {
            longitude = 0.0D;
        }
        try
        {
            latitude = Double.parseDouble((String)hashData.get("y"));
        }
        catch(Exception exception2)
        {
            latitude = 0.0D;
        }
        unit = (String)hashData.get("unit");
        time = (String)hashData.get("time");
        try
        {
            timeCost = Double.parseDouble((String)hashData.get("time_cost"));
        }
        catch(Exception exception3)
        {
            timeCost = 0.0D;
        }
        try
        {
            stdDistance = Double.parseDouble((String)hashData.get("std_distance"));
        }
        catch(Exception exception4)
        {
            stdDistance = 0.0D;
        }
        try
        {
            msLink = Integer.parseInt((String)hashData.get("mslink"));
        }
        catch(Exception exception5)
        {
            msLink = 0;
        }
        try
        {
            inExpressway = Integer.parseInt((String)hashData.get("inExpressway"));
        }
        catch(Exception exception6)
        {
            inExpressway = 0;
        }
        type = (String)hashData.get("type");
        totalLength = (String)hashData.get("total_length");
    }

    private static final long serialVersionUID = 0xa22d702f0a5d5828L;
    public String description;
    public String directionEnum;
    public int inExpressway;
    public int index;
    public double latitude;
    public double longitude;
    public int msLink;
    public double stdDistance;
    public double stdTimeTotal;
    public String time;
    public double timeCost;
    public String title;
    public String totalLength;
    public String type;
    public String unit;
}
