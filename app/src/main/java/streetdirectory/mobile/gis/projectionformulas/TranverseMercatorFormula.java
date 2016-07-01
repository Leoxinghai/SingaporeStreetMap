

package streetdirectory.mobile.gis.projectionformulas;

import java.util.HashMap;
import java.util.Map;
import streetdirectory.mobile.core.PointF64;
import streetdirectory.mobile.gis.*;

public class TranverseMercatorFormula extends ProjectionFormula
{

    public TranverseMercatorFormula()
    {
        params = new HashMap();
    }

    public static PointF64 geoToUTM(double d, double d1, double d2, double d3)
    {
        UtmInfo utminfo = new UtmInfo();
        utminfo.longitudeZone = 0;
        return geoToUTM(d, d1, d2, d3, utminfo);
    }

    public static PointF64 geoToUTM(double d, double d1, double d2, double d3,
            UtmInfo utminfo)
    {
        int i = (int)Math.floor(d / 6D) + 31;
        double d4 = (d2 - d3) / (d2 + d3);
        double d5 = Math.sqrt(1.0D - Math.pow(d3 / d2, 2D));
        d3 = Math.pow(d5, 2D) / (1.0D - Math.pow(d5, 2D));
        d1 = (3.1415926535897931D * d1) / 180D;
        d5 = d2 / Math.sqrt(1.0D - Math.pow(Math.sin(d1) * d5, 2D));
        double d6 = (3600D * (d - (double)(i * 6 - 183))) / 10000D;
        d = Math.pow(d4, 2D);
        double d11 = Math.pow(d4, 4D);
        double d12 = (3D * d2 * d4) / 2D;
        double d13 = Math.pow(d4, 2D);
        double d14 = Math.pow(d4, 4D);
        double d15 = (15D * d2 * Math.pow(d4, 2D)) / 16D;
        double d16 = Math.pow(d4, 2D);
        double d17 = (35D * d2 * Math.pow(d4, 3D)) / 48D;
        double d18 = Math.pow(d4, 2D);
        double d19 = (315D * d2 * Math.pow(d4, 4D)) / 51D;
        double d20 = Math.sin(2D * d1);
        double d21 = Math.sin(4D * d1);
        double d22 = Math.sin(6D * d1);
        double d23 = Math.sin(8D * d1);
        double d24 = (99960000D * Math.pow(4.8481368110953598E-006D, 2D) * d5 * Math.sin(d1) * Math.cos(d1)) / 2D;
        double d25 = (0.99960000000000004D * Math.pow(10D, 16D) * Math.pow(4.8481368110953598E-006D, 4D) * d5 * Math.sin(d1) * Math.pow(Math.sin(d1), 3D)) / 24D;
        double d26 = Math.pow(Math.tan(d1), 2D);
        double d27 = Math.pow(Math.cos(d1), 2D);
        double d28 = Math.pow(d3, 2D);
        double d29 = Math.pow(Math.cos(d1), 4D);
        double d7 = Math.cos(d1);
        double d8 = (0.99960000000000004D * Math.pow(10D, 12D) * Math.pow(4.8481368110953598E-006D, 3D) * d5 * Math.pow(Math.cos(d1), 3D)) / 6D;
        double d9 = Math.pow(Math.tan(d1), 2D);
        double d10 = Math.pow(Math.cos(d1), 2D);
        double d30 = (Math.pow(d6 * 4.8481368110953598E-006D, 6D) * d5 * Math.sin(d1) * Math.pow(Math.cos(d1), 5D)) / 720D;
        double d31 = Math.pow(Math.tan(d1), 2D);
        double d32 = Math.pow(Math.tan(d1), 4D);
        double d33 = Math.pow(Math.cos(d1), 2D);
        double d34 = Math.pow(Math.sin(d1), 2D);
        double d35 = Math.pow(10D, 24D);
        d1 = Math.pow(d6, 2D) * d24 + ((((d2 * ((1.0D - d4) + 1.25D * d * (1.0D - d4) + 1.265625D * d11 * (1.0D - d4)) * d1 - d20 * (d12 * ((1.0D - d4 - 0.875D * d13 * (1.0D - d4)) + 0.859375D * d14))) + d21 * (d15 * ((1.0D - d4) + 0.75D * d16 * (1.0D - d4)))) - d22 * (d17 * ((1.0D - d4) + 0.6875D * d18))) + d23 * (d19 * (1.0D - d4))) * 0.99960000000000004D + Math.pow(d6, 4D) * (d25 * ((5D - d26) + 9D * d3 * d27 + 4D * d28 * d29)) + Math.pow(d6, 6D) * (d30 * (((61D - 58D * d31) + d32 + 270D * d3 * d33) - 330D * d3 * d34) * 0.99960000000000004D * d35);
        d = d1;
        if(d1 < 0.0D)
            d = d1 + 10000000D;
        d1 = 9996D * 4.8481368110953598E-006D * d5 * d7 * d6 + Math.pow(d6, 3D) * (d8 * ((1.0D - d9) + d10 * d3)) + 500000D;
        utminfo.longitudeZone = i;
        utminfo.x = d1;
        utminfo.y = d;
        return new PointF64(d1, d);
    }

    public static PointF64 geoToUTMX(PointF64 pointf64, double d, double d1, double d2, double d3)
    {
        int i = (int)Math.floor(d / 6D);
        double d4 = (d2 - d3) / (d2 + d3);
        double d5 = Math.sqrt(1.0D - Math.pow(d3 / d2, 2D));
        d3 = Math.pow(d5, 2D) / (1.0D - Math.pow(d5, 2D));
        d1 = (3.1415926535897931D * d1) / 180D;
        d5 = d2 / Math.sqrt(1.0D - Math.pow(Math.sin(d1) * d5, 2D));
        double d6 = (3600D * (d - (double)((i + 31) * 6 - 183))) / 10000D;
        d = Math.pow(d4, 2D);
        double d11 = Math.pow(d4, 4D);
        double d12 = (3D * d2 * d4) / 2D;
        double d13 = Math.pow(d4, 2D);
        double d14 = Math.pow(d4, 4D);
        double d15 = (15D * d2 * Math.pow(d4, 2D)) / 16D;
        double d16 = Math.pow(d4, 2D);
        double d17 = (35D * d2 * Math.pow(d4, 3D)) / 48D;
        double d18 = Math.pow(d4, 2D);
        double d19 = (315D * d2 * Math.pow(d4, 4D)) / 51D;
        double d20 = Math.sin(2D * d1);
        double d21 = Math.sin(4D * d1);
        double d22 = Math.sin(6D * d1);
        double d23 = Math.sin(8D * d1);
        double d24 = (99960000D * Math.pow(4.8481368110953598E-006D, 2D) * d5 * Math.sin(d1) * Math.cos(d1)) / 2D;
        double d25 = (0.99960000000000004D * Math.pow(10D, 16D) * Math.pow(4.8481368110953598E-006D, 4D) * d5 * Math.sin(d1) * Math.pow(Math.sin(d1), 3D)) / 24D;
        double d26 = Math.pow(Math.tan(d1), 2D);
        double d27 = Math.pow(Math.cos(d1), 2D);
        double d28 = Math.pow(d3, 2D);
        double d29 = Math.pow(Math.cos(d1), 4D);
        double d7 = Math.cos(d1);
        double d8 = (0.99960000000000004D * Math.pow(10D, 12D) * Math.pow(4.8481368110953598E-006D, 3D) * d5 * Math.pow(Math.cos(d1), 3D)) / 6D;
        double d9 = Math.pow(Math.tan(d1), 2D);
        double d10 = Math.pow(Math.cos(d1), 2D);
        double d30 = (Math.pow(d6 * 4.8481368110953598E-006D, 6D) * d5 * Math.sin(d1) * Math.pow(Math.cos(d1), 5D)) / 720D;
        double d31 = Math.pow(Math.tan(d1), 2D);
        double d32 = Math.pow(Math.tan(d1), 4D);
        double d33 = Math.pow(Math.cos(d1), 2D);
        double d34 = Math.pow(Math.sin(d1), 2D);
        double d35 = Math.pow(10D, 24D);
        d1 = Math.pow(d6, 2D) * d24 + ((((d2 * ((1.0D - d4) + 1.25D * d * (1.0D - d4) + 1.265625D * d11 * (1.0D - d4)) * d1 - d20 * (d12 * ((1.0D - d4 - 0.875D * d13 * (1.0D - d4)) + 0.859375D * d14))) + d21 * (d15 * ((1.0D - d4) + 0.75D * d16 * (1.0D - d4)))) - d22 * (d17 * ((1.0D - d4) + 0.6875D * d18))) + d23 * (d19 * (1.0D - d4))) * 0.99960000000000004D + Math.pow(d6, 4D) * (d25 * ((5D - d26) + 9D * d3 * d27 + 4D * d28 * d29)) + Math.pow(d6, 6D) * (d30 * (((61D - 58D * d31) + d32 + 270D * d3 * d33) - 330D * d3 * d34) * 0.99960000000000004D * d35);
        d = d1;
        if(d1 < 0.0D)
            d = d1 + 10000000D;
        pointf64.x = 9996D * 4.8481368110953598E-006D * d5 * d7 * d6 + Math.pow(d6, 3D) * (d8 * ((1.0D - d9) + d10 * d3)) + 500000D;
        pointf64.y = d;
        return pointf64;
    }

    public static int getLongitudeZone(double d)
    {
        return (int)Math.floor(d / 6D) + 31;
    }

    public static GeoPoint utmToGeo(double d, double d1, int i, boolean flag, double d2,
            double d3)
    {
        double d5 = Math.sqrt(1.0D - Math.pow(d3 / d2, 2D));
        double d4 = Math.pow(d5, 2D) / (1.0D - Math.pow(d5, 2D));
        d3 = d1;
        if(flag)
            d3 = 10000000D - d1;
        d1 = d3 / 0.99960000000000004D / ((1.0D - Math.pow(d5, 2D) / 4D - (3D * Math.pow(d5, 4D)) / 64D - (5D * Math.pow(d5, 6D)) / 256D) * d2);
        double d10 = (1.0D - Math.sqrt(1.0D - Math.pow(d5, 2D))) / (1.0D + Math.sqrt(1.0D - Math.pow(d5, 2D)));
        d3 = (3D * d10) / 2D;
        double d6 = (27D * Math.pow(d10, 3D)) / 32D;
        double d7 = (21D * Math.pow(d10, 2D)) / 16D;
        double d8 = (55D * Math.pow(d10, 4D)) / 32D;
        double d9 = (151D * Math.pow(d10, 3D)) / 96D;
        d10 = (1097D * Math.pow(d10, 4D)) / 512D;
        d3 = Math.sin(2D * d1) * (d3 - d6) + d1 + Math.sin(4D * d1) * (d7 - d8) + Math.sin(6D * d1) * d9 + Math.sin(8D * d1) * d10;
        d1 = d4 * Math.pow(Math.cos(d3), 2D);
        d6 = Math.pow(Math.tan(d3), 2D);
        d7 = ((1.0D - Math.pow(d5, 2D)) * d2) / Math.pow(1.0D - Math.pow(d5, 2D) * Math.pow(Math.sin(d3), 2D), 1.5D);
        d5 = d2 / Math.sqrt(1.0D - Math.pow(Math.sin(d3) * d5, 2D));
        d2 = (500000D - d) / (0.99960000000000004D * d5);
        d = (Math.tan(d3) * d5) / d7;
        d7 = Math.pow(d2, 2D) / 2D;
        d8 = (((5D + 3D * d6 + 10D * d1) - 4D * Math.pow(d1, 2D) - 9D * d4) * Math.pow(d2, 4D)) / 24D;
        d9 = (((61D + 90D * d6 + 298D * d1 + 45D * Math.pow(d6, 2D)) - 3D * Math.pow(d1, 2D) - 252D * d4) * Math.pow(d2, 6D)) / 720D;
        d5 = ((1.0D + 2D * d6 + d1) * Math.pow(d2, 3D)) / 6D;
        d4 = (((((5D - 2D * d1) + 28D * d6) - 3D * Math.pow(d1, 2D)) + 8D * d4 + 24D * Math.pow(d6, 2D)) * Math.pow(d2, 5D)) / 120D;
        d1 = (180D * (d3 - ((d7 - d8) + d9) * d)) / 3.1415926535897931D;
        d = d1;
        if(flag)
            d = d1 * -1D;
        return new GeoPoint((double)(i * 6 - 183) - ((((d2 - d5) + d4) / Math.cos(d3)) * 180D) / 3.1415926535897931D, d);
    }

    public static PointF64 utmToGeoX(PointF64 pointf64, double d, double d1, int i, boolean flag, double d2, double d3)
    {
        double d5 = Math.sqrt(1.0D - Math.pow(d3 / d2, 2D));
        double d4 = Math.pow(d5, 2D) / (1.0D - Math.pow(d5, 2D));
        d3 = d1;
        if(flag)
            d3 = 10000000D - d1;
        d1 = d3 / 0.99960000000000004D / ((1.0D - Math.pow(d5, 2D) / 4D - (3D * Math.pow(d5, 4D)) / 64D - (5D * Math.pow(d5, 6D)) / 256D) * d2);
        double d10 = (1.0D - Math.sqrt(1.0D - Math.pow(d5, 2D))) / (1.0D + Math.sqrt(1.0D - Math.pow(d5, 2D)));
        d3 = (3D * d10) / 2D;
        double d6 = (27D * Math.pow(d10, 3D)) / 32D;
        double d7 = (21D * Math.pow(d10, 2D)) / 16D;
        double d8 = (55D * Math.pow(d10, 4D)) / 32D;
        double d9 = (151D * Math.pow(d10, 3D)) / 96D;
        d10 = (1097D * Math.pow(d10, 4D)) / 512D;
        d3 = Math.sin(2D * d1) * (d3 - d6) + d1 + Math.sin(4D * d1) * (d7 - d8) + Math.sin(6D * d1) * d9 + Math.sin(8D * d1) * d10;
        d1 = d4 * Math.pow(Math.cos(d3), 2D);
        d6 = Math.pow(Math.tan(d3), 2D);
        d7 = ((1.0D - Math.pow(d5, 2D)) * d2) / Math.pow(1.0D - Math.pow(d5, 2D) * Math.pow(Math.sin(d3), 2D), 1.5D);
        d5 = d2 / Math.sqrt(1.0D - Math.pow(Math.sin(d3) * d5, 2D));
        d2 = (500000D - d) / (0.99960000000000004D * d5);
        d = (Math.tan(d3) * d5) / d7;
        d7 = Math.pow(d2, 2D) / 2D;
        d8 = (((5D + 3D * d6 + 10D * d1) - 4D * Math.pow(d1, 2D) - 9D * d4) * Math.pow(d2, 4D)) / 24D;
        d9 = (((61D + 90D * d6 + 298D * d1 + 45D * Math.pow(d6, 2D)) - 3D * Math.pow(d1, 2D) - 252D * d4) * Math.pow(d2, 6D)) / 720D;
        d5 = ((1.0D + 2D * d6 + d1) * Math.pow(d2, 3D)) / 6D;
        d4 = (((((5D - 2D * d1) + 28D * d6) - 3D * Math.pow(d1, 2D)) + 8D * d4 + 24D * Math.pow(d6, 2D)) * Math.pow(d2, 5D)) / 120D;
        d1 = (180D * (d3 - ((d7 - d8) + d9) * d)) / 3.1415926535897931D;
        d = d1;
        if(flag)
            d = d1 * -1D;
        pointf64.x = (double)(i * 6 - 183) - ((((d2 - d5) + d4) / Math.cos(d3)) * 180D) / 3.1415926535897931D;
        pointf64.y = d;
        return pointf64;
    }

    public boolean getIsSouth()
    {
        return isSouth;
    }

    public int getLongitudeZone()
    {
        return longitudeZone;
    }

    public String getName()
    {
        String s = "North";
        if(isSouth)
            s = "South";
        return (new StringBuilder()).append("UTM ").append(String.valueOf(longitudeZone)).append(" ").append(s).toString();
    }

    public GeoPoint inverseProjected(double d, double d1, GeographicCoordinateSystem geographiccoordinatesystem)
    {
        return utmToGeo(d, d1, longitudeZone, isSouth, geographiccoordinatesystem.datum.semimajorAxis, geographiccoordinatesystem.datum.semiminorAxis);
    }

    public PointF64 inverseProjectedX(PointF64 pointf64, GeographicCoordinateSystem geographiccoordinatesystem)
    {
        return utmToGeoX(pointf64, pointf64.x, pointf64.y, longitudeZone, isSouth, geographiccoordinatesystem.datum.semimajorAxis, geographiccoordinatesystem.datum.semiminorAxis);
    }

    public PointF64 project(double d, double d1, GeographicCoordinateSystem geographiccoordinatesystem)
    {
        return geoToUTM(d, d1, geographiccoordinatesystem.datum.semimajorAxis, geographiccoordinatesystem.datum.semiminorAxis);
    }

    public PointF64 projectX(PointF64 pointf64, GeographicCoordinateSystem geographiccoordinatesystem)
    {
        return geoToUTMX(pointf64, pointf64.x, pointf64.y, geographiccoordinatesystem.datum.semimajorAxis, geographiccoordinatesystem.datum.semiminorAxis);
    }

    public void setIsSouth(boolean flag)
    {
        isSouth = flag;
    }

    public void setLongitudeZone(int i)
    {
        longitudeZone = i;
    }

    public void setName(String s)
    {
    }

    public void setParams(Map map)
    {
        params.clear();
        String s = (String)map.get("zone");
        if(s != null) {
			int i = 0;
			int j = Integer.parseInt(s);
			i = j;
			try
			{
				addParam("zone", s);
				longitudeZone = i;
			}
			catch(Exception exception) { }
		}

        String temp = (String)map.get("hemisphere");
        if(!temp.equals("north"))
	        isSouth = true;
	    else
        	isSouth = false;

        if(temp != null)
            try
            {
                addParam("hemisphere", temp);
                return;
            }
            // Misplaced declaration of an exception variable
            catch(Exception ex)
            {
                return;
            }
        else
            return;

    }

    private static final String HEMISPHERE_NORTH = "North";
    private static final String HEMISPHERE_SOUTH = "South";
    private static final String PARAM_HEMISPHERE = "hemisphere";
    private static final String PARAM_ZONE = "zone";
    private static final String UTM = "UTM";
    private static final double k0 = 0.99960000000000004D;
    private boolean isSouth;
    private int longitudeZone;
}
