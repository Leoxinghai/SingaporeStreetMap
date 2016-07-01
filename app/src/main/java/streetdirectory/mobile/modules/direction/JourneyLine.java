

package streetdirectory.mobile.modules.direction;

import java.util.ArrayList;
import streetdirectory.mobile.gis.GeoPoint;
import streetdirectory.mobile.service.SDDataOutput;

public class JourneyLine extends SDDataOutput
{

    public JourneyLine()
    {
    }

    public JourneyLine(int i)
    {
        color = i;
    }

    public void addNewPoint(GeoPoint geopoint)
    {
        if(geopoint != null)
            arrayOfPoints.add(geopoint);
    }

    public ArrayList arrayOfPoints;
    public int color;
}
