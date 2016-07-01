

package streetdirectory.mobile.modules.direction.service;

import java.util.HashMap;
import streetdirectory.mobile.service.SDDataOutput;

public class DirectionOverviewServiceOutput extends SDDataOutput
{

    public DirectionOverviewServiceOutput()
    {
    }

    public void populateData()
    {
        populateData();
        drivingDistance = (String)hashData.get("drive_distance");
        drivingTime = (String)hashData.get("drive_time");
        taxiTime = (String)hashData.get("taxi_time");
        taxiFare = (String)hashData.get("taxi_fare");
        busTime = (String)hashData.get("bus_time");
        busFare = (String)hashData.get("bus_fare");
        busTrainTime = (String)hashData.get("bustrain_time");
        busTrainFare = (String)hashData.get("bustrain_fare");
    }

    private static final long serialVersionUID = 0xd8a87d65e250c8cfL;
    public String busFare;
    public String busTime;
    public String busTrainFare;
    public String busTrainTime;
    public String drivingDistance;
    public String drivingTime;
    public String taxiFare;
    public String taxiTime;
}
