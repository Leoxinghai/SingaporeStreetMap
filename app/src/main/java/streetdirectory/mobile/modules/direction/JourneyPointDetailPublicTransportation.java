

package streetdirectory.mobile.modules.direction;

import java.util.HashMap;

// Referenced classes of package streetdirectory.mobile.modules.direction:
//            JourneyPointDetail

public class JourneyPointDetailPublicTransportation extends JourneyPointDetail
{

    public JourneyPointDetailPublicTransportation()
    {
        hashData = new HashMap();
    }

    public JourneyPointDetailPublicTransportation(HashMap hashmap)
    {
        hashData = hashmap;
    }

    public void populateData()
    {
        super.populateData();
        serviceName = (String)hashData.get("service_name");
        destination = (String)hashData.get("destination");
        try
        {
            distance = Double.parseDouble((String)hashData.get("distance"));
        }
        catch(Exception exception)
        {
            distance = 0.0D;
        }
        try
        {
            fares = Double.parseDouble((String)hashData.get("fares"));
        }
        catch(Exception exception1)
        {
            fares = 0.0D;
        }
        if("1".equals(hashData.get("available")))
            available = true;
        else
            available = false;
        first = (String)hashData.get("first");
        if("1".equals(hashData.get("is_rechecked")))
            rechecked = true;
        else
            rechecked = false;
        vehicleType = (String)hashData.get("vehicletype");
    }

    private boolean available;
    public String destination;
    public double distance;
    public double fares;
    public String first;
    private boolean rechecked;
    public String serviceName;
    public String vehicleType;
}
