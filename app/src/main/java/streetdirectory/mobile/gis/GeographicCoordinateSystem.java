

package streetdirectory.mobile.gis;


// Referenced classes of package streetdirectory.mobile.gis:
//            Datum

public class GeographicCoordinateSystem
{

    public GeographicCoordinateSystem()
    {
        datum = new Datum();
    }

    public static GeographicCoordinateSystem getWgs1984Gcs()
    {
        GeographicCoordinateSystem geographiccoordinatesystem = new GeographicCoordinateSystem();
        Datum datum1 = geographiccoordinatesystem.datum;
        datum1.name = "1984Spheroid";
        datum1.semimajorAxis = 6378137D;
        datum1.semiminorAxis = 6356752.3141999999D;
        datum1.inverseFlatenning = -1D;
        geographiccoordinatesystem.primeMeridian = 0.0D;
        geographiccoordinatesystem.radianPerUnit = 1.0D;
        geographiccoordinatesystem.name = "WGS 1984";
        return geographiccoordinatesystem;
    }

    public Datum datum;
    public String name;
    public double primeMeridian;
    public double radianPerUnit;
}
