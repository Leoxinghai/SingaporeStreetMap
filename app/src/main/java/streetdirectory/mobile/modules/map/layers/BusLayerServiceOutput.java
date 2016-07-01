

package streetdirectory.mobile.modules.map.layers;

import java.util.HashMap;
import streetdirectory.mobile.core.StringTools;
import streetdirectory.mobile.service.SDDataOutput;

public class BusLayerServiceOutput extends SDDataOutput
{

    public BusLayerServiceOutput()
    {
    }

    public void populateData()
    {
        super.populateData();
        v = (String)hashData.get("v");
        a = (String)hashData.get("a");
        busStopId = (String)hashData.get("bus_stop_id");
        x = StringTools.tryParseDouble((String)hashData.get("x"), 0.0D);
        y = StringTools.tryParseDouble((String)hashData.get("y"), 0.0D);
        pid = StringTools.tryParseInt((String)hashData.get("pid"), 0);
        aid = StringTools.tryParseInt((String)hashData.get("aid"), 0);
        lid = StringTools.tryParseInt((String)hashData.get("lid"), 0);
    }

    private static final long serialVersionUID = 0x36f94610f06fc01bL;
    public String a;
    public int aid;
    public String busStopId;
    public int lid;
    public int pid;
    public String v;
    public double x;
    public double y;
}
