

package streetdirectory.mobile.modules.locationdetail.erp.service;

import java.util.HashMap;
import streetdirectory.mobile.service.SDDataOutput;

public class ERPRectangleServiceOutput extends SDDataOutput
{

    public ERPRectangleServiceOutput()
    {
    }

    public void populateData()
    {
        populateData();
        zone = (String)hashData.get("zone");
        address = (String)hashData.get("a");
        erpID = (String)hashData.get("erpid");
        try
        {
            longitude = Double.parseDouble((String)hashData.get("x"));
        }
        catch(Exception exception)
        {
            longitude = 0.0D;
        }
        try
        {
            latitude = Double.parseDouble((String)hashData.get("y"));
        }
        catch(Exception exception1)
        {
            latitude = 0.0D;
        }
        try
        {
            placeID = Integer.parseInt((String)hashData.get("pid"));
        }
        catch(Exception exception2)
        {
            placeID = 0;
        }
        try
        {
            addressID = Integer.parseInt((String)hashData.get("aid"));
        }
        catch(Exception exception3)
        {
            addressID = 0;
        }
        try
        {
            locationID = Integer.parseInt((String)hashData.get("lid"));
        }
        catch(Exception exception4)
        {
            locationID = 0;
        }
        description = (String)hashData.get("desc");
        imageURL = (String)hashData.get("img");
    }

    private static final long serialVersionUID = 0xc169e525e5c263fdL;
    public String address;
    public int addressID;
    public String description;
    public String erpID;
    public String imageURL;
    public double latitude;
    public int locationID;
    public double longitude;
    public int placeID;
    public String zone;
}
