

package streetdirectory.mobile.modules.locationdetail.bus.service;

import java.util.HashMap;

public class BusRouteSummary
{

    public BusRouteSummary()
    {
        hashData = new HashMap();
    }

    public BusRouteSummary(HashMap hashmap)
    {
        hashData = hashmap;
    }

    public void populateData()
    {
        title = (String)hashData.get("title");
        direction = (String)hashData.get("direction");
        start = (String)hashData.get("start");
        end = (String)hashData.get("end");
    }

    public String direction;
    public String end;
    public HashMap hashData;
    public String start;
    public String title;
}
