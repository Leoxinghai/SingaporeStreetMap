

package streetdirectory.mobile.modules.direction;

import java.util.HashMap;

public class JourneyTaxiSummary
{

    public JourneyTaxiSummary()
    {
        hashData = new HashMap();
    }

    public JourneyTaxiSummary(HashMap hashmap)
    {
        hashData = hashmap;
    }

    public void populateData()
    {
        totalLength = (String)hashData.get("total_length");
        try
        {
            timeCost = Double.parseDouble("time_cost");
        }
        catch(Exception exception)
        {
            timeCost = 0.0D;
        }
        totalTime = (String)hashData.get("total_time");
    }

    public HashMap hashData;
    public double timeCost;
    public String totalLength;
    public String totalTime;
}
