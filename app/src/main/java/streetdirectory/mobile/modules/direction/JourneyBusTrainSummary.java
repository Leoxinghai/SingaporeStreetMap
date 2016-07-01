

package streetdirectory.mobile.modules.direction;

import java.util.HashMap;

// Referenced classes of package streetdirectory.mobile.modules.direction:
//            JourneySummary

public class JourneyBusTrainSummary extends JourneySummary
{

    public JourneyBusTrainSummary()
    {
        hashData = new HashMap();
    }

    public JourneyBusTrainSummary(HashMap hashmap)
    {
        super(hashmap);
    }

    public void populateData()
    {
        super.populateData();
        try
        {
            totalFare = Double.parseDouble((String)hashData.get("total_fare"));
        }
        catch(Exception exception)
        {
            totalFare = 0.0D;
        }
        try
        {
            totalWalk = Double.parseDouble((String)hashData.get("total_walk"));
            return;
        }
        catch(Exception exception1)
        {
            totalWalk = 0.0D;
        }
    }

    public double totalFare;
    public double totalWalk;
}
