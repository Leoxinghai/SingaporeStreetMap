
package streetdirectory.mobile.gis.maps;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import streetdirectory.mobile.core.*;
import streetdirectory.mobile.gis.GeoPoint;
import streetdirectory.mobile.gis.maps.configs.MapPreset;
import streetdirectory.mobile.gis.maps.configs.MapPresetLevel;
import streetdirectory.mobile.gis.maps.configs.MapPresetLevelMap;


public class MapViewCanvas extends View
{

    public MapViewCanvas(Context context)
    {
        this(context, null);
    }

    public MapViewCanvas(Context context, AttributeSet attributeset)
    {
        this(context, attributeset, 0);
    }

    public MapViewCanvas(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        debugMode = false;
        levels = new ArrayList();
        lastVisible = null;
        lastVisible2 = null;
        toDrawLevels = new MapViewLevel[3];
        ind1 = 0;
        ind2 = 0;
        ind3 = 0;
        isFastMode = false;
        r = new RectangleF64(0.0D, 0.0D, getWidth(), getHeight());
        tr = new RectangleF64();
        construct(context);
    }

    private void construct(Context context)
    {
        paint = new Paint();
        paint.setFilterBitmap(true);
        paint.setARGB(255, 200, 0, 0);
        paint.setStrokeWidth(1.0F);
        paint.setStyle(android.graphics.Paint.Style.STROKE);
        paintFast = new Paint();
        paintFast.setFilterBitmap(false);
        paintFast.setDither(false);
        paintFast.setAntiAlias(false);
        paintFast.setARGB(255, 200, 0, 0);
        paintFast.setStrokeWidth(1.0F);
        paintFast.setStyle(android.graphics.Paint.Style.STROKE);
    }

    public void arrange()
    {
        System.out.println("MapViewCanvas.arrage");
        Iterator iterator = levels.iterator();
        for(;iterator.hasNext();) {
            MapViewLevel mapviewlevel = (MapViewLevel)iterator.next();
            if(mapviewlevel != lastVisible)
            {
                mapviewlevel.cancelTiles();
                mapviewlevel.clearArrange();
            }
        }
        if(lastVisible != null)
            lastVisible.arrange();
    }

    public MapPreset getPreset()
    {
        return preset;
    }

    public boolean isFastMode()
    {
        return isFastMode;
    }

    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        MapViewLevel amapviewlevel[];
        int i;
        int j;
        int k;
        toDrawLevels[0] = null;
        toDrawLevels[1] = null;
        toDrawLevels[2] = null;
        i = 0;
        System.out.println("MapViewCanas." + levels.size());
        if(!isFastMode)
        {
            for(Iterator iterator = levels.iterator(); iterator.hasNext();)
            {
                MapViewLevel mapviewlevel = (MapViewLevel)iterator.next();
                if(mapviewlevel.isVisible)
                {
                    r.right = r.left + mapviewlevel.scaledWidth + mapviewlevel.scaledWidth * 0.5D;
                    r.bottom = r.top + mapviewlevel.scaledHeight + mapviewlevel.scaledHeight * 0.5D;
                    r.left = -(mapviewlevel.scaledWidth * 0.5D);
                    r.top = -(mapviewlevel.scaledHeight * 0.5D);
                    toDrawLevels[0] = toDrawLevels[1];
                    toDrawLevels[1] = toDrawLevels[2];
                    toDrawLevels[2] = mapviewlevel;
                }
                i++;
            }

        }
        toDrawLevels[2] = lastVisible;
        i = 0;
        amapviewlevel = toDrawLevels;
        k = amapviewlevel.length;
        j = 0;
        MapViewLevel mapviewlevel1;
        try
        {

            j = 0;
            j = k -1;
        //for(;j < k;) {
        //for(;j >= 0;) {
			mapviewlevel1 = amapviewlevel[j];
            System.out.println("MapViewCanvas.level. "+j +":"+k);
			if(mapviewlevel1 != null) {
				canvas.save();
				canvas.scale((float)mapviewlevel1.scaledDelta, (float)mapviewlevel1.scaledDelta);
				Object obj = mapviewlevel1.drawLock;
				Iterator iterator1 = mapviewlevel1.tiles.iterator();
				MapTile maptile;

				for(;iterator1.hasNext();) {
					maptile = (MapTile)iterator1.next();
					if(maptile.image != null) {
						tr.left = (double)maptile.x - mapviewlevel1.topLeftMap.x;
						tr.top = (double)maptile.y - mapviewlevel1.topLeftMap.y;
						tr.right = tr.left + (double)maptile.width;
						tr.bottom = tr.top + (double)maptile.height;
					}

                    if(maptile.image == null)
                        System.out.println("onDraw.null." + maptile.image+":"+maptile.row +":"+maptile.col);
					if(r.intersects(tr)) {
                        System.out.println("onDraw.1 ");
                        if(maptile.image != null) {
                            if (i >= 2)
                                canvas.drawBitmap(maptile.image, (float) ((double) maptile.x - mapviewlevel1.topLeftMap.x), (float) ((double) maptile.y - mapviewlevel1.topLeftMap.y), paint);
                            else
                                canvas.drawBitmap(maptile.image, (float) ((double) maptile.x - mapviewlevel1.topLeftMap.x), (float) ((double) maptile.y - mapviewlevel1.topLeftMap.y), paint);
                        }

						if(debugMode)
						{
							canvas.drawRect((float)((double)maptile.x - mapviewlevel1.topLeftMap.x), (float)((double)maptile.y - mapviewlevel1.topLeftMap.y), (float)(((double)maptile.x - mapviewlevel1.topLeftMap.x) + (double)maptile.image.getWidth()), (float)(((double)maptile.y - mapviewlevel1.topLeftMap.y) + (double)maptile.image.getHeight()), paint);
							canvas.drawText((new StringBuilder()).append(maptile.row).append("-").append(maptile.col).toString(), (float)(((double)maptile.x - mapviewlevel1.topLeftMap.x) + (double)(maptile.image.getWidth() / 2)), (float)(((double)maptile.y - mapviewlevel1.topLeftMap.y) + (double)(maptile.image.getHeight() / 2)), paint);
						}
					}
				}
			}
			i++;
			j++;
		//}

            canvas.restore();
        }
        catch(Exception exception) { }
        return;
    }

    protected void onDraw2(Canvas canvas)
    {
        canvas.drawColor(Color.rgb(204, 255, 204));
        int k = levels.size();
        int i = 0;
        RectangleF64 rectanglef64 = new RectangleF64(0.0D, 0.0D, getWidth(), getHeight());
        for(Iterator iterator = levels.iterator(); iterator.hasNext();)
        {
            MapViewLevel mapviewlevel = (MapViewLevel)iterator.next();
            canvas.save();
            int j = i;
            if(mapviewlevel.isVisible)
            {
                rectanglef64.right = rectanglef64.left + mapviewlevel.scaledHalfWidth * 2D;
                rectanglef64.bottom = rectanglef64.top + mapviewlevel.scaledHalfHeight * 2D;
                j = i + 1;
                canvas.scale((float)mapviewlevel.scaledDelta, (float)mapviewlevel.scaledDelta);
            }
            canvas.restore();
            i = j;
        }

        SDLogger.debug((new StringBuilder()).append("total bitmap = ").append(0).append(", total draw = ").append(0).toString());
        SDLogger.debug((new StringBuilder()).append("total layers = ").append(k).append(", visible level = ").append(i).toString());
    }

    protected void onDraw3(Canvas canvas)
    {
        ind1 = -3;
        ind2 = -2;
        ind3 = -1;
        toDrawLevels[0] = null;
        toDrawLevels[1] = null;
        toDrawLevels[2] = null;
        int i1 = levels.size();
        int i = 0;
        int l = 0;
        for(Iterator iterator = levels.iterator(); iterator.hasNext();)
        {
            MapViewLevel mapviewlevel = (MapViewLevel)iterator.next();
            int j = i;
            if(mapviewlevel.isVisible)
            {
                r.right = r.left + mapviewlevel.scaledWidth;
                r.bottom = r.top + mapviewlevel.scaledHeight;
                i++;
                j = i;
                if(false)
                {
                    toDrawLevels[0] = toDrawLevels[1];
                    toDrawLevels[1] = toDrawLevels[2];
                    toDrawLevels[2] = mapviewlevel;
                    ind1 = ind2;
                    ind2 = ind3;
                    ind3 = l;
                    j = i;
                }
            }
            l++;
            i = j;
        }

        l = 0;
        MapViewLevel amapviewlevel[] = toDrawLevels;
        int j1 = amapviewlevel.length;
        int k = 0;
        while(k < j1)
        {
            MapViewLevel mapviewlevel1 = amapviewlevel[k];
            if(mapviewlevel1 != null)
            {
                canvas.save();
                canvas.scale((float)mapviewlevel1.scaledDelta, (float)mapviewlevel1.scaledDelta);
                canvas.restore();
                l++;
            }
            k++;
        }
        SDLogger.debug((new StringBuilder()).append("total bitmap = ").append(0).append(", total draw = ").append(0).toString());
        SDLogger.debug((new StringBuilder()).append("total layers = ").append(i1).append(", visible level = ").append(i).toString());
    }

    protected void onSizeChanged(int i, int j, int k, int l)
    {
        for(Iterator iterator = levels.iterator(); iterator.hasNext(); ((MapViewLevel)iterator.next()).changedSize(i, j, k, l));
        super.onSizeChanged(i, j, k, l);
    }

    public void setFastMode(boolean flag)
    {
        isFastMode = flag;
    }

    public void setPreset(MapPreset mappreset, GeoPoint geopoint, double d)
    {
        preset = mappreset;
        levels.clear();
        MapViewLevel mapviewlevel;

        System.out.println("MapViewCanvas.setPreset." + mappreset.levels.size());

        for(Iterator iterator = mappreset.levels.iterator(); iterator.hasNext(); levels.add(mapviewlevel))
        {
            MapPresetLevel mappresetlevel = (MapPresetLevel)iterator.next();
            mapviewlevel = new MapViewLevel() {

                protected void onInvalidate()
                {
                    arrange();
                    invalidate();
                }

            };
            mapviewlevel.level = mappresetlevel;
            mapviewlevel.update(geopoint, d);
            mapviewlevel.isVisible = false;
            mapviewlevel.changedSize(getWidth(), getHeight(), 0, 0);
        }

        update(false, geopoint, d);
        arrange();
    }

    public void update(boolean flag, GeoPoint geopoint, double d)
    {
        System.out.println("MapViewCanvas.update." + levels.size());
        for(Iterator iterator = levels.iterator(); iterator.hasNext();)
        {
            MapViewLevel mapviewlevel = (MapViewLevel)iterator.next();
            mapviewlevel.update(geopoint, d);
            if(mapviewlevel.map != null && mapviewlevel.map.finalScale >= d)
            {
                mapviewlevel.isVisible = true;
                lastVisible = mapviewlevel;
            } else
            {
                mapviewlevel.isVisible = false;
            }
        }

        if(flag)
            invalidate();
    }

    public boolean debugMode;
    int ind1;
    int ind2;
    int ind3;
    private boolean isFastMode;
    public MapViewLevel lastVisible;
    MapViewLevel lastVisible2;
    ArrayList levels;
    Paint paint;
    Paint paintFast;
    MapPreset preset;
    RectangleF64 r;
    MapViewLevel toDrawLevels[];
    RectangleF64 tr;
}
