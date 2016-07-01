

package streetdirectory.mobile.modules.direction;

import java.util.HashMap;

public class JourneySummary
{

    public JourneySummary()
    {
        hashData = new HashMap();
    }

    public JourneySummary(HashMap hashmap)
    {
        hashData = hashmap;
    }

    public void populateData()
    {
        totalTime = (String)hashData.get("total_time");
        try
        {
            stdTime = Double.parseDouble((String)hashData.get("std_time"));
            return;
        }
        catch(Exception exception)
        {
            stdTime = 0.0D;
        }
    }

    public HashMap hashData;
    public double stdTime;
    public String totalTime;
}
