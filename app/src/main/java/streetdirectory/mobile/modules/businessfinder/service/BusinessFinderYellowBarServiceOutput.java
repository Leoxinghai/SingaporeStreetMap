

package streetdirectory.mobile.modules.businessfinder.service;

import java.util.HashMap;
import streetdirectory.mobile.core.StringTools;
import streetdirectory.mobile.service.SDDataOutput;

public class BusinessFinderYellowBarServiceOutput extends SDDataOutput
{

    public BusinessFinderYellowBarServiceOutput()
    {
    }

    public void populateData()
    {
        super.populateData();
        id = StringTools.tryParseInt((String)hashData.get("id"), 0);
        name = (String)hashData.get("nm");
        categoryID = StringTools.tryParseInt((String)hashData.get("cid"), 0);
        total = StringTools.tryParseInt((String)hashData.get("tot"), 0);
    }

    private static final long serialVersionUID = 0x3ba1dca5777e7fdaL;
    public int categoryID;
    public int id;
    public String name;
    public int total;
}
