// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.map.layers;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import java.util.ArrayList;
import java.util.HashMap;
import streetdirectory.mobile.core.PointF64;
import streetdirectory.mobile.gis.GeoPoint;
import streetdirectory.mobile.gis.maps.MapView;
import streetdirectory.mobile.gis.maps.MapViewLayer;
import streetdirectory.mobile.service.SDHttpServiceOutput;

// Referenced classes of package streetdirectory.mobile.modules.map.layers:
//            BuildingVectorService, BuildingVectorServiceOutput

public class BuildingVectorLayer extends MapViewLayer
{

    public BuildingVectorLayer(Context context)
    {
        this(context, null, 0);
    }

    public BuildingVectorLayer(Context context, int i)
    {
        this(context, null, i);
    }

    public BuildingVectorLayer(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        minLevel = 10;
        setWillNotDraw(false);
        fillPaint = new Paint();
        fillPaint.setFilterBitmap(false);
        fillPaint.setDither(false);
        fillPaint.setAntiAlias(false);
        fillPaint.setStyle(android.graphics.Paint.Style.FILL);
        fillPaint.setColor(Color.argb(110, 249, 100, 0));
        strokePaint = new Paint();
        strokePaint.setFilterBitmap(false);
        strokePaint.setDither(false);
        strokePaint.setAntiAlias(false);
        strokePaint.setStyle(android.graphics.Paint.Style.STROKE);
        strokePaint.setStrokeWidth(5F);
        strokePaint.setColor(Color.argb(255, 249, 100, 0));
    }

    protected void downloadBuildingVector(final double longitude, final double latitude , String s)
    {
        if(mService != null)
        {
            mService.abort();
            mService = null;
        }
        mData = null;
        mService = new BuildingVectorService(longitude, latitude, s) {

            public void onFailed(Exception exception)
            {
                super.onFailed(exception);
            }

            public void onSuccess(Object obj)
            {
                onSuccess((SDHttpServiceOutput)obj);
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                if(sdhttpserviceoutput.childs.size() > 0)
                {
                    mData = (BuildingVectorServiceOutput)sdhttpserviceoutput.childs.get(0);
                    mData.hashData.put("x", String.valueOf(longitude));
                    mData.hashData.put("y", String.valueOf(latitude));
                    mData.longitude = longitude;
                    mData.latitude = latitude;
                    onBuildingVectorReceived(mData);
                    postInvalidate();
                }
            }

        };
        mService.executeAsync();
    }

    public void draw(Canvas canvas)
    {
        if(mData != null && mapView.getCurrentLevelOrdinal() > 10 && mData.vector != null)
        {
            path = new Path();
            int i = 0;
            while(i < mData.vector.length)
            {
                if(i % 2 == 0)
                {
                    PointF64 pointf64 = mapView.geoToPixel(mData.vector[i], mData.vector[i + 1]);
                    if(i == 0)
                        path.moveTo((float)pointf64.x, (float)pointf64.y);
                    else
                        path.lineTo((float)pointf64.x, (float)pointf64.y);
                }
                i++;
            }
            canvas.drawPath(path, fillPaint);
            canvas.drawPath(path, strokePaint);
        }
        super.draw(canvas);
    }

    protected void onBuildingVectorReceived(BuildingVectorServiceOutput buildingvectorserviceoutput)
    {
    }

    protected void onMapLayerClicked(GeoPoint geopoint, Point point)
    {
        super.onMapLayerClicked(geopoint, point);
        if(mService != null)
        {
            mService.abort();
            mService = null;
        }
    }

    protected void onUpdate()
    {
        invalidate();
    }

    private Paint fillPaint;
    protected double latitude;
    protected double longitude;
    protected BuildingVectorServiceOutput mData;
    protected BuildingVectorService mService;
    protected final int minLevel;
    private Path path;
    private Paint strokePaint;
}
