

package streetdirectory.mobile.modules.locationdetail.bus.service;

import java.util.ArrayList;
import java.util.Iterator;
import streetdirectory.mobile.modules.direction.JourneyPointDetail;
import streetdirectory.mobile.modules.direction.JourneyRoute;
import streetdirectory.mobile.service.SDDataOutput;

// Referenced classes of package streetdirectory.mobile.modules.locationdetail.bus.service:
//            BusRouteSummary

public class BusRoutesServiceOutput extends SDDataOutput
{

    public BusRoutesServiceOutput()
    {
    }

    public void addNewPointDetailArray(ArrayList arraylist)
    {
        if(arraylist != null)
            arrayOfArrayOfPointDetail.add(arraylist);
    }

    public void addNewRoute(JourneyRoute journeyroute)
    {
        if(journeyroute != null)
            arrayOfRoutes.add(journeyroute);
    }

    public void addNewSummary(BusRouteSummary busroutesummary)
    {
        if(busroutesummary != null)
            arrayOfSummary.add(busroutesummary);
    }

    public void populateData()
    {
        populateData();
        if(arrayOfArrayOfPointDetail != null && arrayOfArrayOfPointDetail.size() > 0)
        {
            Iterator iterator = arrayOfArrayOfPointDetail.iterator();
            do
            {
                if(!iterator.hasNext())
                    break;
                JourneyPointDetail journeypointdetail = (JourneyPointDetail)((ArrayList)iterator.next()).get(0);
                if(journeypointdetail.longitude == 0.0D)
                {
                    journeypointdetail.longitude = start.longitude;
                    journeypointdetail.latitude = start.latitude;
                }
            } while(true);
        }
    }

    private static final long serialVersionUID = 0xfb227b28ca22bd4eL;
    public ArrayList arrayOfArrayOfPointDetail;
    public ArrayList arrayOfRoutes;
    public ArrayList arrayOfSummary;
    public JourneyPointDetail end;
    public JourneyPointDetail start;
}
