

package streetdirectory.mobile.gis;

import android.content.Context;
import android.graphics.*;
import java.util.*;
import streetdirectory.mobile.core.PointF64;
import streetdirectory.mobile.gis.maps.MapView;
import streetdirectory.mobile.gis.maps.MapViewLayer;

// Referenced classes of package streetdirectory.mobile.gis:
//            SdArea, MapZone, GeoPoint

public class AreaMapLayer extends MapViewLayer
{
    public class AreaDrawer
    {

        public Paint brush;
        public Path path;
        public Paint stroke;
        public MapZone zone;
        public AreaDrawer()
        {
            super();
        }
    }


    public AreaMapLayer(Context context)
    {
        super(context);
        areaDrawers = new ArrayList();
        i = 0;
        randomEngine = new Random();
        setWillNotDraw(false);
    }

    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        AreaDrawer areadrawer;
        for(Iterator iterator = areaDrawers.iterator(); iterator.hasNext(); canvas.drawPath(areadrawer.path, areadrawer.stroke))
        {
            areadrawer = (AreaDrawer)iterator.next();
            canvas.drawPath(areadrawer.path, areadrawer.brush);
        }

    }

    protected void onUpdate()
    {
    }

    public void setArea(ArrayList arraylist)
    {
        if(arraylist != null)
            for(Iterator iterator = arraylist.iterator(); iterator.hasNext();)
            {
                Object obj = (SdArea)iterator.next();
                Paint paint = new Paint();
                paint.setFilterBitmap(false);
                paint.setDither(false);
                paint.setAntiAlias(false);
                paint.setARGB(255, 200, 0, 0);
                paint.setColor(Color.argb(110, 249, 100, 0));
                paint.setStyle(android.graphics.Paint.Style.FILL);
                Paint paint1 = new Paint();
                paint1.setFilterBitmap(false);
                paint1.setDither(false);
                paint1.setAntiAlias(false);
                paint1.setColor(Color.argb(255, 249, 100, 0));
                paint1.setStrokeWidth(5F);
                paint1.setStyle(android.graphics.Paint.Style.STROKE);
                int j = Color.argb(255, randomEngine.nextInt(255), randomEngine.nextInt(255), randomEngine.nextInt(255));
                paint.setColor(j);
                paint1.setColor(j);
                paint.setAlpha(110);
                obj = ((SdArea) (obj)).mapZones.iterator();
                while(((Iterator) (obj)).hasNext())
                {
                    MapZone mapzone = (MapZone)((Iterator) (obj)).next();
                    AreaDrawer areadrawer = new AreaDrawer();
                    areadrawer.brush = paint;
                    areadrawer.stroke = paint1;
                    Path path = new Path();
                    path.reset();
                    i = 0;
                    Iterator iterator2 = mapzone.vertices.iterator();
                    while(iterator2.hasNext())
                    {
                        GeoPoint geopoint = (GeoPoint)iterator2.next();
                        pt = mapView.geoToPixel(geopoint.longitude, geopoint.latitude);
                        if(i == 0)
                            path.moveTo((float)pt.x, (float)pt.y);
                        else
                            path.lineTo((float)pt.x, (float)pt.y);
                        i = i + 2;
                    }
                    path.close();
                    areadrawer.path = path;
                    areadrawer.zone = mapzone;
                    areaDrawers.add(areadrawer);
                }
            }

    }

    public void update()
    {
        Iterator iterator = areaDrawers.iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            AreaDrawer areadrawer = (AreaDrawer)iterator.next();
            if(areadrawer.zone != null && areadrawer.path != null)
            {
                areadrawer.path.reset();
                i = 0;
                Iterator iterator1 = areadrawer.zone.vertices.iterator();
                while(iterator1.hasNext())
                {
                    GeoPoint geopoint = (GeoPoint)iterator1.next();
                    pt = mapView.geoToPixel(geopoint.longitude, geopoint.latitude);
                    if(i == 0)
                        areadrawer.path.moveTo((float)pt.x, (float)pt.y);
                    else
                        areadrawer.path.lineTo((float)pt.x, (float)pt.y);
                    i = i + 2;
                }
                areadrawer.path.close();
            }
        } while(true);
        invalidate();
    }

    private ArrayList areaDrawers;
    int i;
    PointF64 pt;
    private Random randomEngine;
}
