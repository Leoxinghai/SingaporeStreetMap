

package streetdirectory.mobile.modules.direction;

import java.util.HashMap;

public class JourneyERPInfo
{

    public JourneyERPInfo()
    {
        hashData = new HashMap();
    }

    public JourneyERPInfo(HashMap hashmap)
    {
        hashData = hashmap;
    }

    public void populateData()
    {
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
        name = (String)hashData.get("name");
        try
        {
            erpID = Integer.parseInt((String)hashData.get("no"));
        }
        catch(Exception exception2)
        {
            erpID = 0;
        }
        try
        {
            locationID = Integer.parseInt((String)hashData.get("lid"));
        }
        catch(Exception exception3)
        {
            locationID = 0;
        }
        try
        {
            price = Double.parseDouble((String)hashData.get("price"));
            return;
        }
        catch(Exception exception4)
        {
            price = 0.0D;
        }
    }

    private static final long serialVersionUID = 0x610dafee978fd6eeL;
    public int erpID;
    public HashMap hashData;
    public double latitude;
    public int locationID;
    public double longitude;
    public String name;
    public double price;
}
