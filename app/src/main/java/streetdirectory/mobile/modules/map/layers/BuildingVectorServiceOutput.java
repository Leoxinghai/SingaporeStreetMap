

package streetdirectory.mobile.modules.map.layers;

import java.util.HashMap;
import streetdirectory.mobile.core.StringTools;
import streetdirectory.mobile.modules.core.LocationBusinessServiceOutput;

public class BuildingVectorServiceOutput extends LocationBusinessServiceOutput
{

    public BuildingVectorServiceOutput()
    {
    }

    public void populateData()
    {
        super.populateData();
        if(hashData.get("vc") != null)
        {
            String as[] = ((String)hashData.get("vc")).split(",");
            vector = new double[as.length];
            for(int i = 0; i < as.length; i++)
                vector[i] = StringTools.tryParseDouble(as[i], 0.0D);

        }
        if(hashData.get("v") != null)
            v = (String)hashData.get("v");
        if(hashData.get("a") != null)
            a = (String)hashData.get("a");
        if(hashData.get("pid") != null)
            pid = StringTools.tryParseInt((String)hashData.get("pid"), 0);
        if(hashData.get("aid") != null)
            aid = StringTools.tryParseInt((String)hashData.get("aid"), 0);
        if(hashData.get("lid") != null)
            lid = StringTools.tryParseInt((String)hashData.get("lid"), 0);
        if(hashData.get("cat") != null)
            cat = StringTools.tryParseInt((String)hashData.get("cat"), 0);
        if(hashData.get("tbiz") != null)
            tbiz = StringTools.tryParseInt((String)hashData.get("tbiz"), 0);
        if(hashData.get("stid") != null)
            stid = StringTools.tryParseInt((String)hashData.get("stid"), 0);
    }

    private static final long serialVersionUID = 0xd24d1cbbbd300485L;
    public String a;
    public int aid;
    public int cat;
    public int lid;
    public int pid;
    public int stid;
    public int tbiz;
    public String v;
    double vector[];
}
