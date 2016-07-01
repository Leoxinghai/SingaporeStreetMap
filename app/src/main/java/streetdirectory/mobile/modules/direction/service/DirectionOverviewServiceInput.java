

package streetdirectory.mobile.modules.direction.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class DirectionOverviewServiceInput extends SDHttpServiceInput
{

    public DirectionOverviewServiceInput()
    {
    }

    public DirectionOverviewServiceInput(String s, double d, double d1, int i, int j,
            double d2, double d3, int k, int l, String s1,
            String s2)
    {
        super(s);
        startLongitude = d;
        startLatitude = d1;
        startPlaceID = i;
        startAddressID = j;
        endLongitude = d2;
        endLatitude = d3;
        endPlaceID = k;
        endAddressID = l;
        date = s1;
        time = s2;
    }

    public String getURL()
    {
        return URLFactory.createURLDirectionOverview(countryCode, startLongitude, startLatitude, startPlaceID, startAddressID, endLongitude, endLatitude, endPlaceID, endAddressID, date, time, apiVersion);
    }

    public String date;
    public int endAddressID;
    public double endLatitude;
    public double endLongitude;
    public int endPlaceID;
    public int startAddressID;
    public double startLatitude;
    public double startLongitude;
    public int startPlaceID;
    public String time;
}
