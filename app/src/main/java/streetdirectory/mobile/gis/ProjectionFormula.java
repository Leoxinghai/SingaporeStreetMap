

package streetdirectory.mobile.gis;

import java.util.HashMap;
import java.util.Map;
import streetdirectory.mobile.core.PointF64;

// Referenced classes of package streetdirectory.mobile.gis:
//            GeographicCoordinateSystem, GeoPoint

public abstract class ProjectionFormula
{

    public ProjectionFormula()
    {
        params = new HashMap();
    }

    public void addParam(String s, String s1)
    {
        if(!params.containsKey(s))
            params.put(s, s1);
    }

    public ProjectionFormula create()
    {
        return null;
    }

    public String getName()
    {
        return "no name";
    }

    public String getParameterValue(String s)
    {
        if(params.containsKey(s))
            return (String)params.get(s);
        else
            return null;
    }

    public Map getParams()
    {
        return params;
    }

    public abstract GeoPoint inverseProjected(double d, double d1, GeographicCoordinateSystem geographiccoordinatesystem);

    public abstract PointF64 inverseProjectedX(PointF64 pointf64, GeographicCoordinateSystem geographiccoordinatesystem);

    public abstract PointF64 project(double d, double d1, GeographicCoordinateSystem geographiccoordinatesystem);

    public abstract PointF64 projectX(PointF64 pointf64, GeographicCoordinateSystem geographiccoordinatesystem);

    public void removeParam(String s)
    {
        if(params.containsKey(s))
            params.remove(s);
    }

    public void setParams(Map map)
        throws Exception
    {
    }

    public String name;
    protected Map params;
}
