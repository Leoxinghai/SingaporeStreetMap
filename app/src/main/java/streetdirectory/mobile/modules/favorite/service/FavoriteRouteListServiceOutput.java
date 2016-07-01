

package streetdirectory.mobile.modules.favorite.service;

import java.text.SimpleDateFormat;
import java.util.*;
import streetdirectory.mobile.core.SDLogger;
import streetdirectory.mobile.service.SDDataOutput;

public class FavoriteRouteListServiceOutput extends SDDataOutput
{

    public FavoriteRouteListServiceOutput()
    {
    }

    public void populateData()
    {
        super.populateData();
        try
        {
            routeID = Integer.parseInt((String)hashData.get("route_id"));
        }
        catch(Exception exception)
        {
            routeID = 0;
        }
        venueFrom = (String)hashData.get("v1");
        placeIDFrom = (String)hashData.get("pid1");
        addressIDFrom = (String)hashData.get("aid1");
        try
        {
            longFrom = Double.parseDouble("x1");
        }
        catch(Exception exception1)
        {
            longFrom = 0.0D;
        }
        try
        {
            latFrom = Double.parseDouble("y1");
        }
        catch(Exception exception2)
        {
            latFrom = 0.0D;
        }
        venueTo = (String)hashData.get("v2");
        placeIDTo = (String)hashData.get("pid2");
        addressIDTo = (String)hashData.get("aid2");
        try
        {
            longTo = Double.parseDouble("x2");
        }
        catch(Exception exception3)
        {
            longTo = 0.0D;
        }
        try
        {
            latTo = Double.parseDouble("y2");
        }
        catch(Exception exception4)
        {
            latTo = 0.0D;
        }
        saveName = (String)hashData.get("svName");
        if(format == null)
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        format.setTimeZone(TimeZone.getDefault());
        try
        {
            dateTime = format.parse((String)hashData.get("tm"));
            return;
        }
        catch(Exception exception5)
        {
            SDLogger.printStackTrace(exception5, "ServiceOutput parse date failed");
        }
    }

    private static final long serialVersionUID = 0x9ce11c567aa81c80L;
    public String addressIDFrom;
    public String addressIDTo;
    public Date dateTime;
    private SimpleDateFormat format;
    public double latFrom;
    public double latTo;
    public double longFrom;
    public double longTo;
    public String placeIDFrom;
    public String placeIDTo;
    public int routeID;
    public String saveName;
    public String venueFrom;
    public String venueTo;
}
