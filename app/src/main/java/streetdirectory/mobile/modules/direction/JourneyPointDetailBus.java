

package streetdirectory.mobile.modules.direction;

import java.util.HashMap;

// Referenced classes of package streetdirectory.mobile.modules.direction:
//            JourneyPointDetailPublicTransportation

public class JourneyPointDetailBus extends JourneyPointDetailPublicTransportation
{

    public JourneyPointDetailBus()
    {
        hashData = new HashMap();
    }

    public JourneyPointDetailBus(HashMap hashmap)
    {
        hashData = hashmap;
    }

    public void populateData()
    {
        super.populateData();
        busNo = (String)hashData.get("bus_no");
        handicap = (String)hashData.get("handicap");
    }

    public String busNo;
    public String handicap;
}
