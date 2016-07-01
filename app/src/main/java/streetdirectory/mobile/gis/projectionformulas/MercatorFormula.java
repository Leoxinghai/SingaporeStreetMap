

package streetdirectory.mobile.gis.projectionformulas;

import java.util.HashMap;
import java.util.Map;
import streetdirectory.mobile.core.PointF64;
import streetdirectory.mobile.gis.*;

public class MercatorFormula extends ProjectionFormula
{

    public MercatorFormula()
    {
        params = new HashMap();
    }

    public double degToRad(double d)
    {
        if(d == 0.0D)
            return 0.0D;
        else
            return 0.017453292519943295D * d;
    }

    public String getName()
    {
        return "Mercator";
    }

    public GeoPoint inverseProjected(double d, double d1, GeographicCoordinateSystem geographiccoordinatesystem)
    {
        return new GeoPoint(unmercX(d, geographiccoordinatesystem.datum.semimajorAxis), unmercY(d1, geographiccoordinatesystem.datum.semimajorAxis, geographiccoordinatesystem.datum.semiminorAxis));
    }

    public PointF64 inverseProjectedX(PointF64 pointf64, GeographicCoordinateSystem geographiccoordinatesystem)
    {
        pointf64.x = unmercX(pointf64.x, geographiccoordinatesystem.datum.semimajorAxis);
        pointf64.y = unmercY(pointf64.y, geographiccoordinatesystem.datum.semimajorAxis, geographiccoordinatesystem.datum.semiminorAxis);
        return pointf64;
    }

    public double mercX(double d, double d1)
    {
        return degToRad(d) * d1;
    }

    public double mercY(double d, double d1, double d2)
    {
        double d3 = d;
        if(d > 89.5D)
            d3 = 89.5D;
        d = d3;
        if(d3 < -89.5D)
            d = -89.5D;
        d2 /= d1;
        d2 = Math.sqrt(1.0D - d2 * d2);
        d = degToRad(d);
        d3 = d2 * Math.sin(d);
        d2 = Math.pow((1.0D - d3) / (1.0D + d3), 0.5D * d2);
        return 0.0D - Math.log(Math.tan(0.5D * (1.5707963267948966D - d)) / d2) * d1;
    }

    public PointF64 project(double d, double d1, GeographicCoordinateSystem geographiccoordinatesystem)
    {
        return new PointF64(mercX(d, geographiccoordinatesystem.datum.semimajorAxis), mercY(d1, geographiccoordinatesystem.datum.semimajorAxis, geographiccoordinatesystem.datum.semiminorAxis));
    }

    public PointF64 projectX(PointF64 pointf64, GeographicCoordinateSystem geographiccoordinatesystem)
    {
        pointf64.x = mercX(pointf64.x, geographiccoordinatesystem.datum.semimajorAxis);
        pointf64.y = mercY(pointf64.y, geographiccoordinatesystem.datum.semimajorAxis, geographiccoordinatesystem.datum.semiminorAxis);
        return pointf64;
    }

    public double radToDeg(double d)
    {
        if(d == 0.0D)
            return 0.0D;
        else
            return 57.295779513082323D * d;
    }

    public void setName(String s)
    {
    }

    public void setParams(Map map)
    {
    }

    public double unmercX(double d, double d1)
    {
        return radToDeg(d) / d1;
    }

    public double unmercY(double d, double d1, double d2)
    {
        if(d == 0.0D)
            return 0.0D;
        d2 /= d1;
        double d3 = Math.sqrt(1.0D - d2 * d2);
        double d4 = Math.exp(-d / d1);
        d1 = 1.5707963267948966D - 2D * Math.atan(d4);
        d = 0.0D;
        for(d2 = 1.0D; Math.abs(d2) > 1.0000000000000001E-009D && d < 15D; d++)
        {
            d2 = d3 * Math.sin(d1);
            d2 = 1.5707963267948966D - 2D * Math.atan(Math.pow((1.0D - d2) / (1.0D + d2), 0.5D * d3) * d4) - d1;
            d1 += d2;
        }

        return radToDeg(d1);
    }

    private static final double m_PI_2 = 1.5707963267948966D;
}
