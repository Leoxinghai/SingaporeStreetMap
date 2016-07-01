

package streetdirectory.mobile.gis;

import java.util.*;
import streetdirectory.mobile.core.SDLogger;
import streetdirectory.mobile.gis.projectionformulas.MercatorFormula;
import streetdirectory.mobile.gis.projectionformulas.TranverseMercatorFormula;

// Referenced classes of package streetdirectory.mobile.gis:
//            ProjectionCoordinateSystem, GeographicCoordinateSystem, ProjectionFormula

public class ProjectionCoordinateFactory
{

    public ProjectionCoordinateFactory()
    {
    }

    public static ProjectionCoordinateSystem AddToUserProjections(String s)
    {
        if(s.equalsIgnoreCase(""))
            return null;
        ProjectionCoordinateSystem projectioncoordinatesystem = null;
        Iterator iterator = userProjections.iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            ProjectionCoordinateSystem projectioncoordinatesystem1 = (ProjectionCoordinateSystem)iterator.next();
            if(projectioncoordinatesystem1.createConnectionString().equals(s))
                projectioncoordinatesystem = projectioncoordinatesystem1;
        } while(true);
        if(projectioncoordinatesystem == null)
        {
            projectioncoordinatesystem = tryCreate(s);
            userProjections.add(projectioncoordinatesystem);
            return projectioncoordinatesystem;
        } else
        {
            return projectioncoordinatesystem;
        }
    }

    public static ProjectionCoordinateSystem create(String s)
        throws Exception
    {
        HashMap hashmap = new HashMap();
        String ss[] = s.split(";");
        int j = ss.length;
        for(int i = 0; i < j; i++)
        {
            String as[] = ss[i].split("=");
            if(as.length > 1)
                hashmap.put(as[0].trim(), as[1].trim());
        }

        ProjectionCoordinateSystem projectioncoordinatesystem = null;
        GeographicCoordinateSystem geographiccoordinatesystem = null;

        try
        {
            ProjectionFormula projectionFormula = null;
            if(hashmap.containsKey("formula"))
                projectionFormula = createFormula((String)hashmap.get("formula"));
            if(projectionFormula == null)
                projectionFormula = new MercatorFormula();

            projectionFormula.setParams(hashmap);
            if(true)
                geographiccoordinatesystem = GeographicCoordinateSystem.getWgs1984Gcs();
            projectioncoordinatesystem = new ProjectionCoordinateSystem();
            projectioncoordinatesystem.setProjectionFormula(projectionFormula);
            projectioncoordinatesystem.setGcs(geographiccoordinatesystem);
            try
            {
                if(hashmap.containsKey("name"))
                    projectioncoordinatesystem.setName((String)hashmap.get("name"));
            }
            catch(Exception exception) { }
            return projectioncoordinatesystem;
        }
        catch(Exception ex)
        {
            SDLogger.printStackTrace(ex, "ProjectCoordinateFactory create");
        }
        return null;
    }

    private static ProjectionFormula createFormula(String s)
    {
        if(s.equals("MercatorFormula"))
            return new MercatorFormula();
        if(s.equals("TranverseMercatorFormula"))
            return new TranverseMercatorFormula();
        else
            return null;
    }

    public static ProjectionCoordinateSystem getDefaultProjection()
    {
        return wgs1984Mercator;
    }

    public static ProjectionCoordinateSystem getDefaultUserProjection()
    {
        if(userProjections.size() > 1)
            return (ProjectionCoordinateSystem)userProjections.get(1);
        else
            return null;
    }

    public static List getUserProjections()
    {
        return userProjections;
    }

    public static ProjectionCoordinateSystem getWgs1984Mercator()
    {
        if(wgs1984Mercator == null)
        {
            wgs1984Mercator = new ProjectionCoordinateSystem();
            wgs1984Mercator.setProjectionFormula(new MercatorFormula());
            wgs1984Mercator.setGcs(GeographicCoordinateSystem.getWgs1984Gcs());
        }
        return wgs1984Mercator;
    }

    public static ProjectionCoordinateSystem getWgs1984Utm48North()
    {
        return wgs1984Utm48North;
    }

    public static ProjectionCoordinateSystem tryCreate(String s)
    {
        try
        {
            ProjectionCoordinateSystem projectionCoordinateSystem = create(s);
            return projectionCoordinateSystem;
        }
        catch(Exception ex)
        {
            return null;
        }
    }

    private static final String FORMULA = "formula";
    private static final String NAME = "name";
    private static List userProjections = new ArrayList();
    private static ProjectionCoordinateSystem wgs1984Mercator;
    private static ProjectionCoordinateSystem wgs1984Utm48North;

}
