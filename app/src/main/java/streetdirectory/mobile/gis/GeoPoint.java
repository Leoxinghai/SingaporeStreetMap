

package streetdirectory.mobile.gis;


public class GeoPoint
{

    public GeoPoint()
    {
    }

    public GeoPoint(double d, double d1)
    {
        longitude = d;
        latitude = d1;
    }

    public static boolean isValid(double d, double d1)
    {
        while(d < -180D || d > 180D || d1 < -90D || d1 > 90D || d1 == 0.0D && d == 0.0D)
            return false;
        return true;
    }

    public double altitude;
    public double latitude;
    public double longitude;
}
