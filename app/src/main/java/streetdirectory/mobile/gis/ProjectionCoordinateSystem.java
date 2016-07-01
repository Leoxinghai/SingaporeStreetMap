

package streetdirectory.mobile.gis;

import android.graphics.Point;
import java.util.*;
import streetdirectory.mobile.core.PointF64;

// Referenced classes of package streetdirectory.mobile.gis:
//            ProjectionCoordinateFactory, ProjectionFormula, GeographicCoordinateSystem, GeoPoint

public class ProjectionCoordinateSystem
{

    public ProjectionCoordinateSystem()
    {
        invertXPixel = false;
        invertYPixel = true;
    }

    public static ProjectionCoordinateSystem CreateInstance(String s)
    {
        return ProjectionCoordinateFactory.tryCreate(s);
    }

    public String ToXmlString()
    {
        return "";
    }

    public String createConnectionString()
    {
        String s = (new StringBuilder()).append("formula=").append(projectionFormula.getClass().getName()).append(";").toString();
        for(Iterator iterator = projectionFormula.getParams().entrySet().iterator(); iterator.hasNext();)
        {
            java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
            s = (new StringBuilder()).append(s).append((String)entry.getKey()).append("=").append((String)entry.getValue()).append(";").toString();
        }

        return (new StringBuilder()).append(s).append("gcs=").append(gcs.name).toString();
    }

    public float distFrom(double d, double d1, double d2, double d3)
    {
        double d4 = Math.toRadians(d2 - d);
        d1 = Math.toRadians(d3 - d1);
        d = Math.sin(d4 / 2D) * Math.sin(d4 / 2D) + Math.cos(Math.toRadians(d)) * Math.cos(Math.toRadians(d2)) * Math.sin(d1 / 2D) * Math.sin(d1 / 2D);
        return (float)(6371D * (2D * Math.atan2(Math.sqrt(d), Math.sqrt(1.0D - d))));
    }

    public PointF64 geoToMetric(double d, double d1)
    {
        if(projectionFormula != null)
            return projectionFormula.project(d, d1, gcs);
        else
            return new PointF64();
    }

    public PointF64 geoToMetric(GeoPoint geopoint)
    {
        if(projectionFormula != null)
            return projectionFormula.project(geopoint.longitude, geopoint.latitude, gcs);
        else
            return new PointF64();
    }

    public PointF64 geoToMetricX(PointF64 pointf64)
    {
        PointF64 pointf64_1 = pointf64;
        if(projectionFormula != null)
            pointf64_1 = projectionFormula.projectX(pointf64, gcs);
        return pointf64_1;
    }

    public PointF64 geoToPixel(double d, double d1, double d2)
    {
        PointF64 pointf64 = geoToMetric(d, d1);
        return metricToPixel(pointf64.x, pointf64.y, d2);
    }

    public PointF64 geoToPixel(double d, double d1, double d2, double d3)
    {
        PointF64 pointf64 = geoToMetric(d, d1);
        return metricToPixel(pointf64.x, pointf64.y, d2, d3);
    }

    public PointF64 geoToPixel(GeoPoint geopoint, double d)
    {
        return geoToPixel(geopoint.longitude, geopoint.latitude, d);
    }

    public PointF64 geoToPixel(GeoPoint geopoint, double d, double d1)
    {
        return geoToPixel(geopoint.longitude, geopoint.latitude, d, d1);
    }

    public PointF64 geoToPixel(GeoPoint geopoint, double d, double d1, PointF64 pointf64)
    {
        PointF64 pointF64 = geoToPixel(geopoint.longitude, geopoint.latitude, d, d1);
        pointF64.x = ((PointF64) (pointF64)).x - pointf64.x;
        pointF64.y = ((PointF64) (pointF64)).y - pointf64.y;
        return pointF64;
    }

    public PointF64 geoToPixel(GeoPoint geopoint, double d, PointF64 pointf64)
    {
        PointF64 pointF64 = geoToPixel(geopoint.longitude, geopoint.latitude, d);
        pointF64.x = ((PointF64) (pointF64)).x - pointf64.x;
        pointF64.y = ((PointF64) (pointF64)).y - pointf64.y;
        return pointF64;
    }

    public PointF64 geoToPixelF64(double d, double d1, double d2)
    {
        PointF64 pointf64 = geoToMetric(d, d1);
        return metricToPixelF64(pointf64.x, pointf64.y, d2);
    }

    public PointF64 geoToPixelF64(double d, double d1, double d2, double d3)
    {
        PointF64 pointf64 = geoToMetric(d, d1);
        return metricToPixelF64(pointf64.x, pointf64.y, d2, d3);
    }

    public PointF64 geoToPixelF64(double d, double d1, double d2, double d3, double d4)
    {
        PointF64 pointf64 = geoToMetric(d, d1);
        pointf64 = metricToPixelF64(pointf64.x, pointf64.y, d2);
        pointf64.x = pointf64.x - d3;
        pointf64.y = pointf64.y - d4;
        return pointf64;
    }

    public PointF64 geoToPixelF64(GeoPoint geopoint, double d)
    {
        return geoToPixelF64(geopoint.longitude, geopoint.latitude, d);
    }

    public PointF64 geoToPixelF64(GeoPoint geopoint, double d, double d1, Point point)
    {
        PointF64 pointF64 = geoToPixelF64(geopoint.longitude, geopoint.latitude, d, d1);
        pointF64.x = ((PointF64) (pointF64)).x - (double)point.x;
        pointF64.y = ((PointF64) (pointF64)).y - (double)point.y;
        return pointF64;
    }

    public PointF64 geoToPixelF64(GeoPoint geopoint, double d, double d1, PointF64 pointf64)
    {
        PointF64 pointF64 = geoToPixelF64(geopoint.longitude, geopoint.latitude, d, d1);
        pointF64.x = ((PointF64) (pointF64)).x - pointf64.x;
        pointF64.y = ((PointF64) (pointF64)).y - pointf64.y;
        return pointF64;
    }

    public PointF64 geoToPixelF64(GeoPoint geopoint, double d, Point point)
    {
        PointF64 pointF64 = geoToPixelF64(geopoint.longitude, geopoint.latitude, d);
        pointF64.x = ((PointF64) (pointF64)).x - (double)point.x;
        pointF64.y = ((PointF64) (pointF64)).y - (double)point.y;
        return pointF64;
    }

    public PointF64 geoToPixelF64(GeoPoint geopoint, double d, PointF64 pointf64)
    {
        return geoToPixelF64(geopoint, d, d, pointf64);
    }

    public PointF64 geoToPixelX(PointF64 pointf64, double d)
    {
        geoToMetricX(pointf64);
        return metricToPixelX(pointf64, d, d);
    }

    public GeographicCoordinateSystem getGcs()
    {
        return gcs;
    }

    public String getName()
    {
        return name;
    }

    public ProjectionFormula getProjectionFormula()
    {
        return projectionFormula;
    }

    public String getSystemName()
    {
        return (new StringBuilder()).append(gcs.name).append(" ").append(projectionFormula.getName()).toString();
    }

    public GeoPoint metricToGeo(double d, double d1)
    {
        if(projectionFormula != null)
            return projectionFormula.inverseProjected(d, d1, gcs);
        else
            return new GeoPoint();
    }

    public PointF64 metricToGeoX(PointF64 pointf64)
    {
        PointF64 pointf64_1 = pointf64;
        if(projectionFormula != null)
            pointf64_1 = projectionFormula.inverseProjectedX(pointf64, gcs);
        return pointf64_1;
    }

    public PointF64 metricToPixel(double d, double d1, double d2)
    {
        return metricToPixel(d, d1, d2, d2);
    }

    public PointF64 metricToPixel(double d, double d1, double d2, double d3)
    {
        PointF64 pointf64 = new PointF64();
        pointf64.x = d / d2;
        if(invertXPixel)
            pointf64.x = pointf64.x * -1D;
        pointf64.y = d1 / d3;
        if(invertYPixel)
            pointf64.y = pointf64.y * -1D;
        return pointf64;
    }

    public PointF64 metricToPixel(double d, double d1, double d2, Point point)
    {
        PointF64 pointf64 = metricToPixel(d, d1, d2);
        return new PointF64(pointf64.x - (double)point.x, pointf64.y - (double)point.y);
    }

    public PointF64 metricToPixel(PointF64 pointf64, double d)
    {
        return metricToPixel(pointf64.x, pointf64.y, d);
    }

    public PointF64 metricToPixel(PointF64 pointf64, double d, double d1)
    {
        return metricToPixel(pointf64.x, pointf64.y, d, d1);
    }

    public PointF64 metricToPixelF64(double d, double d1, double d2)
    {
        return metricToPixelF64(d, d1, d2, d2);
    }

    public PointF64 metricToPixelF64(double d, double d1, double d2, double d3)
    {
        PointF64 pointf64 = new PointF64();
        pointf64.x = d / d2;
        if(invertXPixel)
            pointf64.x = pointf64.x * -1D;
        pointf64.y = d1 / d3;
        if(invertYPixel)
            pointf64.y = pointf64.y * -1D;
        return pointf64;
    }

    public PointF64 metricToPixelF64(double d, double d1, double d2, double d3, double d4)
    {
        PointF64 pointf64 = metricToPixelF64(d, d1, d2, d2);
        return new PointF64(pointf64.x - d3, pointf64.y - d4);
    }

    public PointF64 metricToPixelF64(double d, double d1, double d2, PointF64 pointf64)
    {
        PointF64 pointf64_1 = metricToPixelF64(d, d1, d2, d2);
        return new PointF64(pointf64_1.x - pointf64.x, pointf64_1.y - pointf64.y);
    }

    public PointF64 metricToPixelF64(PointF64 pointf64, double d, PointF64 pointf64_1)
    {
        pointf64 = metricToPixelF64(pointf64.x, pointf64.y, d, d);
        return new PointF64(pointf64.x - pointf64_1.x, pointf64.y - pointf64_1.y);
    }

    public PointF64 metricToPixelX(PointF64 pointf64, double d, double d1)
    {
        pointf64.x = pointf64.x / d;
        if(invertXPixel)
            pointf64.x = pointf64.x * -1D;
        pointf64.y = pointf64.y / d1;
        if(invertYPixel)
            pointf64.y = pointf64.y * -1D;
        return pointf64;
    }

    public GeoPoint pixelF64ToGeo(double d, double d1, double d2)
    {
        PointF64 pointf64 = pixelToMetric(d, d1, d2);
        return metricToGeo(pointf64.x, pointf64.y);
    }

    public GeoPoint pixelF64ToGeo(PointF64 pointf64, double d, PointF64 pointf64_1)
    {
        pointf64.x = pointf64.x + pointf64_1.x;
        pointf64.y = pointf64.y + pointf64_1.y;
        return pixelF64ToGeo(pointf64.x, pointf64.y, d);
    }

    public PointF64 pixelF64ToMetric(Point point, double d, Point point1)
    {
        return pixelToMetric(point.x, point.y, d, point1.x, point1.y);
    }

    public GeoPoint pixelToGeo(double d, double d1, double d2)
    {
        PointF64 pointf64 = pixelToMetric(d, d1, d2);
        return metricToGeo(pointf64.x, pointf64.y);
    }

    public GeoPoint pixelToGeo(double d, double d1, double d2, double d3, double d4)
    {
        return pixelF64ToGeo(d + d3, d1 + d4, d2);
    }

    public GeoPoint pixelToGeo(int i, int j, double d)
    {
        PointF64 pointf64 = pixelToMetric(i, j, d);
        return metricToGeo(pointf64.x, pointf64.y);
    }

    public GeoPoint pixelToGeo(int i, int j, double d, double d1)
    {
        PointF64 pointf64 = pixelToMetric(i, j, d, d1);
        return metricToGeo(pointf64.x, pointf64.y);
    }

    public GeoPoint pixelToGeo(Point point, double d)
    {
        return pixelToGeo(point.x, point.y, d);
    }

    public GeoPoint pixelToGeo(Point point, double d, double d1)
    {
        return pixelToGeo(point.x, point.y, d, d1);
    }

    public GeoPoint pixelToGeo(Point point, double d, double d1, Point point1)
    {
        point.x = point.x + point1.x;
        point.y = point.y + point1.y;
        return pixelToGeo(point.x, point.y, d, d1);
    }

    public GeoPoint pixelToGeo(Point point, double d, Point point1)
    {
        point.x = point.x + point1.x;
        point.y = point.y + point1.y;
        return pixelToGeo(point.x, point.y, d);
    }

    public GeoPoint pixelToGeo(PointF64 pointf64, double d, PointF64 pointf64_1)
    {
        pointf64.x = pointf64.x + pointf64_1.x;
        pointf64.y = pointf64.y + pointf64_1.y;
        return pixelF64ToGeo(pointf64.x, pointf64.y, d);
    }

    public PointF64 pixelToMetric(double d, double d1, double d2)
    {
        return pixelToMetric(d, d1, d2, d2);
    }

    public PointF64 pixelToMetric(double d, double d1, double d2, double d3)
    {
        PointF64 pointf64 = new PointF64();
        pointf64.x = d * d2;
        if(invertXPixel)
            pointf64.x = pointf64.x * -1D;
        pointf64.y = d1 * d3;
        if(invertYPixel)
            pointf64.y = pointf64.y * -1D;
        return pointf64;
    }

    public PointF64 pixelToMetric(int i, int j, double d)
    {
        return pixelToMetric(i, j, d);
    }

    public PointF64 pixelToMetric(int i, int j, double d, double d1)
    {
        return pixelToMetric(i, j, d, d1);
    }

    public PointF64 pixelToMetric(int i, int j, double d, int k, int l)
    {
        return pixelToMetric(i + k, j + l, d, d);
    }

    public PointF64 pixelToMetricX(PointF64 pointf64, double d, double d1)
    {
        pointf64.x = pointf64.x * d;
        if(invertXPixel)
            pointf64.x = pointf64.x * -1D;
        pointf64.y = pointf64.y * d1;
        if(invertYPixel)
            pointf64.y = pointf64.y * -1D;
        return pointf64;
    }

    public void setGcs(GeographicCoordinateSystem geographiccoordinatesystem)
    {
        gcs = geographiccoordinatesystem;
    }

    public void setName(String s)
    {
        name = s;
    }

    public void setProjectionFormula(ProjectionFormula projectionformula)
    {
        projectionFormula = projectionformula;
    }

    private GeographicCoordinateSystem gcs;
    protected boolean invertXPixel;
    protected boolean invertYPixel;
    private String name;
    private ProjectionFormula projectionFormula;
}
