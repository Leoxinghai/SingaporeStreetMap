
package streetdirectory.mobile.gis.maps;

import android.graphics.Bitmap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;
import streetdirectory.mobile.core.PointF64;
import streetdirectory.mobile.core.RectangleF64;
import streetdirectory.mobile.gis.GeoPoint;
import streetdirectory.mobile.gis.ProjectionCoordinateSystem;
import streetdirectory.mobile.gis.maps.configs.MapPresetLevel;
import streetdirectory.mobile.gis.maps.configs.MapPresetLevelMap;
import streetdirectory.mobile.gis.maps.configs.MapfileScale;


public class MapViewLevel
{

    public MapViewLevel()
    {
        incLoc = 0;
        tileMaxCache = 96;
        tileMinSize = 256;
        tiles = new CopyOnWriteArrayList();
        //tiles = new LinkedList();

        map = null;
        drawLock = new Object();
        scaledDelta = 0.0D;
        scaledHalfWidth = 0.0D;
        scaledHalfHeight = 0.0D;
        scaledWidth = 0.0D;
        scaledHeight = 0.0D;
        halfWidth = 1.0D;
        halfHeight = 1.0D;
        width = 1.0D;
        height = 1.0D;
        topLeftMap = new PointF64();
        topLeftWorld = new PointF64();
        topLeftWorld2 = new PointF64();
        geoBounds = new RectangleF64();
        scaledHalfWidth2 = 0.0D;
        scaledHalfHeight2 = 0.0D;
        lastPixel2 = new PointF64();
        inc++;
        incLoc = inc;
    }

    private MapTile getTile(int i, int j)
    {
        MapTile maptile1;
        maptile1 = null;
        Iterator iterator = tiles.iterator();
        MapTile maptile;
        maptile = maptile1;
        System.out.println("getRealTile0."+tiles.size());
        for(;iterator.hasNext();) {
            maptile = (MapTile)iterator.next();
            if(maptile.row != i || maptile.col != j)
                continue;
            maptile1 = maptile;
            if(!maptile.isReady)
                return maptile1;
            maptile1 = maptile;
            System.out.println("getRealTile." + i +":"+j + ":"+tiles.size()+":"+maptile.image);

            if(maptile.image != null)
                return maptile1;
            break;
        }
        //if(maptile != null)
//            tiles.remove(maptile);
        //maptile1 = null;
        return maptile1;
    }

    private void invalidate()
    {
        onInvalidate();
    }

    public void arrange()
    {
        System.out.println("MapViewLevel.arrange.1");
        if(level != null && map != null)
        {
            MapfileScale mapfilescale = map.scale;
            double d1 = scaledHalfWidth * 2D;
            double d2 = scaledHalfHeight * 2D;
            double d = d1;
            if(d1 > width)
                d = width;
            d1 = d2;
            if(d2 > height)
                d1 = height;
            int i1 = (int)Math.ceil((float)topLeftMap.x / (float)mapfilescale.width);
            int k1 = (int)Math.ceil(((float)topLeftMap.x + (float)d) / (float)mapfilescale.width);
            int j = (int)Math.ceil((float)topLeftMap.y / (float)mapfilescale.height);
            int k = (int)Math.ceil(((float)topLeftMap.y + (float)d1) / (float)mapfilescale.height);
            int i = j;
            if(j < 0)
                i = 0;
            j = k;
            if(k < 0)
                j = 0;
            k = i1;
            if(i1 < 0)
                k = 0;
            i1 = k1;
            if(k1 < 0)
                i1 = 0;
            k1 = i;
            if(i > mapfilescale.maxRow - 1)
                k1 = mapfilescale.maxRow;
            int l1 = j;
            if(j > mapfilescale.maxRow - 1)
                l1 = mapfilescale.maxRow;
            i = k;
            if(k > mapfilescale.maxCol - 1)
                i = mapfilescale.maxCol;
            j = i1;
            if(i1 > mapfilescale.maxCol - 1)
                j = mapfilescale.maxCol;
            for(int l = k1; l <= l1; l++)
            {
                int j1 = i;
                System.out.println("tiles.start."+tiles.size());

                while(j1 <= j)
                {
                    MapTile maptile = getTile(l, j1);
                    if(maptile != null)
                    {
                        //tiles.remove(maptile);
                        //tiles.add(0, maptile);
                    } else
                    {
                        MapTile maptile1 = new MapTile() {

                            public void onTileReady(Bitmap bitmap)
                            {
                                if(bitmap != null)
                                    invalidate();
                            }

                        };
                        maptile1.l = this;
                        maptile1.row = l;
                        maptile1.col = j1;
                        maptile1.x = (j1 - 1) * map.scale.width + 0;
                        maptile1.y = (l - 1) * map.scale.height + 0;
                        maptile1.width = map.scale.width;
                        maptile1.height = map.scale.height;
                        maptile1.buildBound();
                        //tiles.add(0, maptile1);
                        tiles.add(maptile1);
                        System.out.println("tiles.add.length."+tiles.size());
                        maptile1.beginRead(map.getTileUri(l, j1, inf));
                    }
                    j1++;
                }
            }

            Object obj;
/*
//            synchronized(drawLock) {
                i = tiles.size();
                if (i > tileMaxCache)
                    for (; i > tileMaxCache - 1; i--) {
                        obj = (MapTile) tiles.get(i - 1);
                        ((MapTile) (obj)).cancel();
                        ((MapTile) (obj)).recycle();
                        tiles.remove(i - 1);
                    }
//            }
*/
        }

    }

    public void cancelTiles()
    {
        Iterator iterator = tiles.iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            MapTile maptile = (MapTile)iterator.next();
            if(maptile != null)
                maptile.cancel();
        } while(true);
    }

    protected void changedSize(int i, int j, int k, int l)
    {
        if(i > 0 && j > 0)
        {
            width = i;
            height = j;
            tileMaxCache = (int)((Math.floor(((double)i * 1.5D) / (double)tileMinSize) + 2D) * (Math.floor(((double)j * 1.5D) / (double)tileMinSize) + 2D));
            halfWidth = i / 2;
            halfHeight = j / 2;
            update(mCenter, mScale);
            arrange();
        }
    }

    public void clearArrange()
    {
        int i = tiles.size();
        if(i > 6)
            for(; i > 6; i--)
            {
                MapTile maptile = (MapTile)tiles.get(i - 1);
                maptile.cancel();
                maptile.recycle();
                tiles.remove(i - 1);
            }

    }

    protected void onInvalidate()
    {
    }

    public void update(GeoPoint geopoint, double d)
    {
        mCenter = geopoint;
        mScale = d;
        try {
            if (level != null) {
                map = level.GetLevelMap(geopoint);
                if (map != null) {
                    lastMetric = map.scale.projection.geoToMetric(geopoint);
                    lastPixel = map.scale.projection.metricToPixel(lastMetric, map.finalScale);
                    scaledDelta = map.finalScale / d;
                    scaledHalfWidth2 = halfWidth / d;
                    scaledHalfHeight2 = halfHeight / d;
                    lastPixel2.x = lastPixel2.x - scaledHalfWidth2;
                    lastPixel2.y = lastPixel2.y - scaledHalfHeight2;
                    scaledHalfWidth = halfWidth / scaledDelta;
                    scaledHalfHeight = halfHeight / scaledDelta;
                    scaledWidth = scaledHalfWidth * 2D;
                    scaledHeight = scaledHalfHeight * 2D;
                    topLeftWorld.x = lastPixel.x - scaledHalfWidth;
                    topLeftWorld.y = lastPixel.y - scaledHalfHeight;
                    topLeftWorld2.x = topLeftWorld.x * map.finalScale;
                    topLeftWorld2.y = topLeftWorld.y * map.finalScale;
                    topLeftMap.x = topLeftWorld.x - map.scale.topLeftPixel.x;
                    topLeftMap.y = topLeftWorld.y - map.scale.topLeftPixel.y;
                }
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public static int inc = 0;
    public Object drawLock;
    public RectangleF64 geoBounds;
    double halfHeight;
    double halfWidth;
    double height;
    public int incLoc;
    streetdirectory.mobile.gis.maps.configs.MapPresetLevelMap.MapTileInfo inf;
    public boolean isVisible;
    PointF64 lastMetric;
    PointF64 lastPixel;
    public PointF64 lastPixel2;
    public MapPresetLevel level;
    GeoPoint mCenter;
    double mScale;
    public MapPresetLevelMap map;
    public double scaledDelta;
    double scaledHalfHeight;
    double scaledHalfHeight2;
    double scaledHalfWidth;
    double scaledHalfWidth2;
    double scaledHeight;
    double scaledWidth;
    public int tileMaxCache;
    public int tileMinSize;
    CopyOnWriteArrayList tiles;
    //LinkedList tiles;
    PointF64 topLeftMap;
    public PointF64 topLeftWorld;
    public PointF64 topLeftWorld2;
    double width;


}
