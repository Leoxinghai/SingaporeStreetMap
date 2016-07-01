

package streetdirectory.mobile.gis.maps.configs;

import org.xml.sax.Attributes;
import streetdirectory.mobile.core.PointF64;
import streetdirectory.mobile.core.SDLogger;
import streetdirectory.mobile.gis.GeoPoint;
import streetdirectory.mobile.gis.ProjectionCoordinateSystem;

// Referenced classes of package streetdirectory.mobile.gis.maps.configs:
//            MapfileConfig

public class MapfileScale
{

    public MapfileScale()
    {
        fileExtension = ".gif";
    }

    public static MapfileScale create(Attributes attributes)
    {
        MapfileScale mapfilescale = new MapfileScale();
        String s;
        if(attributes.getIndex("FOLDER") > -1)
            mapfilescale.folder = attributes.getValue("FOLDER");
        if(attributes.getIndex("WIDTH") > -1)
            mapfilescale.width = Integer.parseInt(attributes.getValue("WIDTH"));
        if(attributes.getIndex("HEIGHT") > -1)
            mapfilescale.height = Integer.parseInt(attributes.getValue("HEIGHT"));
        if(attributes.getIndex("MAXROW") > -1)
            mapfilescale.maxRow = Integer.parseInt(attributes.getValue("MAXROW"));
        if(attributes.getIndex("MAXCOL") > -1)
            mapfilescale.maxCol = Integer.parseInt(attributes.getValue("MAXCOL"));
        if(attributes.getIndex("MINLONG") > -1)
            mapfilescale.minX = Double.parseDouble(attributes.getValue("MINLONG"));
        if(attributes.getIndex("MAXLONG") > -1)
            mapfilescale.maxX = Double.parseDouble(attributes.getValue("MAXLONG"));
        if(attributes.getIndex("MAXLAT") > -1)
            mapfilescale.maxY = Double.parseDouble(attributes.getValue("MAXLAT"));
        if(attributes.getIndex("GRIDROW") > -1)
            mapfilescale.gridRow = Integer.parseInt(attributes.getValue("GRIDROW"));
        if(attributes.getIndex("GRIDCOL") > -1)
            mapfilescale.gridCol = Integer.parseInt(attributes.getValue("GRIDCOL"));
        if(attributes.getIndex("GRIDWIDTH") > -1)
            mapfilescale.gridWidth = Integer.parseInt(attributes.getValue("GRIDWIDTH"));
        if(attributes.getIndex("GRIDHEIGHT") > -1)
            mapfilescale.gridHeight = Integer.parseInt(attributes.getValue("GRIDHEIGHT"));
        if(attributes.getIndex("USERSCALING") > -1)
            mapfilescale.userScaling = Boolean.parseBoolean(attributes.getValue("USERSCALING"));
        if(attributes.getIndex("USERSCALE") > -1)
            mapfilescale.userScale = Double.parseDouble(attributes.getValue("USERSCALE"));
        if(attributes.getIndex("LEVELNAME") > -1)
            mapfilescale.levelName = attributes.getValue("LEVELNAME");
        if(attributes.getIndex("LEVELCODE") > -1)
            mapfilescale.levelCode = attributes.getValue("LEVELCODE");
        if(attributes.getIndex("DISABLED") > -1)
            mapfilescale.disabled = Boolean.parseBoolean(attributes.getValue("DISABLED"));
        if(attributes.getIndex("ISTHUMBNAIL") > -1)
            mapfilescale.isThumbnail = Boolean.parseBoolean(attributes.getValue("ISTHUMBNAIL"));
        if(attributes.getIndex("FILEEXT") > -1) {
            s = attributes.getValue("FILEEXT");
            if (s != null) {
                if (!s.equals(""))
                    mapfilescale.fileExtension = s;
                if (attributes.getIndex("PROJECTION") > -1)
                    mapfilescale.projectionString = attributes.getValue("PROJECTION");
                return mapfilescale;
            }
        }

        return null;
    }

    public boolean build(MapfileConfig mapfileconfig)
    {
        config = mapfileconfig;
        projection = ProjectionCoordinateSystem.CreateInstance(projectionString);
        if(projection == null)
            projection = mapfileconfig.projection;
        totalWidth = width * maxCol;
        totalHeight = height * maxRow;
        scalePixelPerMeter = (maxX - minX) / (double)totalWidth;
        scaleMeterPerPixel = (double)totalWidth / (maxX - minX);
        minY = maxY - (double)totalHeight * scalePixelPerMeter;
        gridTotalWidth = gridCol * gridWidth;
        gridTotalHeight = gridRow * gridHeight;
        boolean flag;
        if(gridTotalWidth > 0 && gridTotalHeight > 0)
            flag = true;
        else
            flag = false;
        try
        {
            isUseGrid = flag;
            GeoPoint geoPoint0 = projection.metricToGeo(minX, minY);
            GeoPoint geopoint = projection.metricToGeo(maxX, maxY);
            minLong = ((GeoPoint) (geoPoint0)).longitude;
            minLat = ((GeoPoint) (geoPoint0)).latitude;
            maxLong = geopoint.longitude;
            maxLat = geopoint.latitude;
            topLeftPixel = projection.metricToPixel(minX, maxY, scalePixelPerMeter);
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            SDLogger.printStackTrace(ex, "MapfileScale build");
            return false;
        }
        return true;
    }

    public MapfileConfig config;
    public boolean disabled;
    public String fileExtension;
    public String folder;
    public int gridCol;
    public int gridHeight;
    public int gridRow;
    public int gridTotalHeight;
    public int gridTotalWidth;
    public int gridWidth;
    public int height;
    public boolean isThumbnail;
    public boolean isUseGrid;
    public String levelCode;
    public String levelName;
    public int maxCol;
    public double maxLat;
    public double maxLong;
    public int maxRow;
    public double maxX;
    public double maxY;
    public double minLat;
    public double minLong;
    public double minX;
    public double minY;
    public ProjectionCoordinateSystem projection;
    public String projectionString;
    public double scaleMeterPerPixel;
    public double scalePixelPerMeter;
    public PointF64 topLeftPixel;
    public int totalHeight;
    public int totalWidth;
    public double userScale;
    public boolean userScaling;
    public int width;
}
