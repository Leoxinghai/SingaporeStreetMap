

package streetdirectory.mobile.modules.direction.service;

import java.util.ArrayList;
import java.util.Iterator;
import streetdirectory.mobile.modules.direction.*;
import streetdirectory.mobile.service.SDDataOutput;

public class DirectionDetailServiceOutput extends SDDataOutput
{

    public DirectionDetailServiceOutput()
    {
        arrayOfRoutes = new ArrayList();
        start = new JourneyPointDetail();
        end = new JourneyPointDetail();
        arrayOfArrayOfPointDetail = new ArrayList();
        arrayOfSummary = new ArrayList();
        arrayOfCarpark = new ArrayList();
        arrayOfArrayOfERPInfo = new ArrayList();
        arrayOfTaxiFare = new ArrayList();
        taxiSummary = new JourneyTaxiSummary();
    }

    public void addNewCarpark(Carpark carpark)
    {
        if(carpark != null)
            arrayOfCarpark.add(carpark);
    }

    public void addNewERPInfo(ArrayList arraylist)
    {
        if(arraylist != null)
            arrayOfArrayOfERPInfo.add(arraylist);
    }

    public void addNewFare(JourneyFare journeyfare)
    {
        if(journeyfare != null)
            arrayOfTaxiFare.add(journeyfare);
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

    public void addNewSummary(JourneySummary journeysummary)
    {
        if(journeysummary != null)
            arrayOfSummary.add(journeysummary);
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

    private static final long serialVersionUID = 0x4058da29e1d2a665L;
    public ArrayList arrayOfArrayOfERPInfo;
    public ArrayList arrayOfArrayOfPointDetail;
    public ArrayList arrayOfCarpark;
    public ArrayList arrayOfRoutes;
    public ArrayList arrayOfSummary;
    public ArrayList arrayOfTaxiFare;
    public JourneyPointDetail end;
    public JourneyPointDetail start;
    public JourneyTaxiSummary taxiSummary;
}
