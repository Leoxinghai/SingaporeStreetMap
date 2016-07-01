

package streetdirectory.mobile.modules.direction;

import java.util.HashMap;

// Referenced classes of package streetdirectory.mobile.modules.direction:
//            JourneyPointDetail

public class JourneyPointDetailCar extends JourneyPointDetail
{

    public JourneyPointDetailCar()
    {
        hashData = new HashMap();
    }

    public JourneyPointDetailCar(HashMap hashmap)
    {
        hashData = hashmap;
    }

    public void populateData()
    {
        super.populateData();
        directionEnum = (String)hashData.get("direction_enum");
        try
        {
            timeCost = Double.parseDouble((String)hashData.get("time_cost"));
        }
        catch(Exception exception)
        {
            timeCost = 0.0D;
        }
        try
        {
            msLink = Integer.parseInt((String)hashData.get("mslink"));
        }
        catch(Exception exception1)
        {
            msLink = 0;
        }
        if("1".equals(hashData.get("inExpressway")))
            inExpressWay = true;
        else
            inExpressWay = false;
        type = (String)hashData.get("type");
    }

    public String directionEnum;
    public boolean inExpressWay;
    public int msLink;
    public double timeCost;
    public String type;
}
