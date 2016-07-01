// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.map.layers;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;

import com.xinghai.mycurve.R;

import java.util.ArrayList;
import java.util.Iterator;
import streetdirectory.mobile.core.MathTools;
import streetdirectory.mobile.core.PointF64;
import streetdirectory.mobile.core.ui.UIHelper;
import streetdirectory.mobile.gis.*;
import streetdirectory.mobile.gis.maps.MapView;
import streetdirectory.mobile.gis.maps.MapViewLayer;
import streetdirectory.mobile.service.SDHttpServiceOutput;

// Referenced classes of package streetdirectory.mobile.modules.map.layers:
//            BusLayerService, BusLayerServiceOutput

public class BusLayer extends MapViewLayer
{

    public BusLayer(Context context)
    {
        this(context, null, 0);
    }

    public BusLayer(Context context, int i)
    {
        this(context, null, i);
    }

    public BusLayer(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        mService = null;
        busIcon = null;
        mLastResult = new ArrayList();
        currentPixel = new PointF64();
        busIconRect = null;
        dest = null;
        strokeRect = null;
        tappedData = null;
        minLevel = 10;
        isBusClicked = false;
        setWillNotDraw(false);
        paint = new Paint();
        paint.setColor(Color.rgb(200, 0, 0));
        paint.setStyle(android.graphics.Paint.Style.FILL_AND_STROKE);
        busIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_bus);
        busIconRect = new RectF(0.0F, 0.0F, UIHelper.toPixel(15F), UIHelper.toPixel(15F));
        dest = new RectF(busIconRect);
        strokePaint = new Paint();
        strokePaint.setFilterBitmap(false);
        strokePaint.setDither(false);
        strokePaint.setAntiAlias(false);
        strokePaint.setStyle(android.graphics.Paint.Style.STROKE);
        strokePaint.setStrokeWidth(5F);
        strokePaint.setColor(Color.rgb(249, 50, 0));
        strokeRect = new RectF(busIconRect);
    }

    public void downloadBusService()
    {
        if(mService != null)
        {
            mService.abort();
            mService = null;
        }
        currentPixel.x = mapView.center.longitude;
        currentPixel.y = mapView.center.latitude;
        currentPixel = ProjectionCoordinateFactory.getWgs1984Mercator().geoToPixel(currentPixel.x, currentPixel.y, mapView.mapScale);
        int i = mapView.getWidth() / 2;
        int j = mapView.getHeight() / 2;
        double d = currentPixel.y;
        double d1 = j;
        double d2 = currentPixel.x;
        double d3 = i;
        double d4 = currentPixel.y;
        double d5 = j;
        double d6 = currentPixel.x;
        double d7 = i;
        GeoPoint geopoint = ProjectionCoordinateFactory.getWgs1984Mercator().pixelToGeo(d2 - d3, d - d1, mapView.mapScale);
        GeoPoint geopoint1 = ProjectionCoordinateFactory.getWgs1984Mercator().pixelToGeo(d6 + d7, d4 + d5, mapView.mapScale);
        mService = new BusLayerService(geopoint.longitude, geopoint.latitude, geopoint1.longitude, geopoint1.latitude) {

            public void onSuccess(Object obj)
            {
                onSuccess((SDHttpServiceOutput)obj);
            }

            public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
            {
                super.onSuccess(sdhttpserviceoutput);
                mLastResult.clear();
                mLastResult.addAll(sdhttpserviceoutput.childs);
                postInvalidate();
            }

        };
        mService.executeAsync();
    }

    public void draw(Canvas canvas)
    {
        if(busIcon != null && mapView.getCurrentLevelOrdinal() > 10)
        {
            Iterator iterator = mLastResult.iterator();
            do
            {
                if(!iterator.hasNext())
                    break;
                BusLayerServiceOutput buslayerserviceoutput = (BusLayerServiceOutput)iterator.next();
                currentPixel.x = buslayerserviceoutput.x;
                currentPixel.y = buslayerserviceoutput.y;
                currentPixel = mapView.geoToPixelX(currentPixel);
                dest.set(busIconRect);
                dest.offset((float)currentPixel.x - busIconRect.width() / 2.0F, (float)currentPixel.y - busIconRect.height() / 2.0F);
                canvas.drawBitmap(busIcon, null, dest, paint);
                if(isBusClicked && tappedData != null && tappedData.lid == buslayerserviceoutput.lid)
                {
                    strokeRect.set(busIconRect);
                    strokeRect.offset((float)currentPixel.x - busIconRect.width() / 2.0F, (float)currentPixel.y - busIconRect.height() / 2.0F);
                    canvas.drawRect(strokeRect, strokePaint);
                }
            } while(true);
        }
        super.draw(canvas);
    }

    protected void onBusIconClicked(BusLayerServiceOutput buslayerserviceoutput)
    {
        isBusClicked = true;
    }

    protected void onMapLayerClicked(GeoPoint geopoint, Point point)
    {
        super.onMapLayerClicked(geopoint, point);
        isBusClicked = false;
        if(busIcon != null && mapView.getCurrentLevelOrdinal() > 10)
        {
            boolean flag1 = false;
            Iterator iterator = mLastResult.iterator();
            boolean flag;
            do
            {
                flag = flag1;
                if(!iterator.hasNext())
                    break;
                BusLayerServiceOutput buslayerserviceoutput = (BusLayerServiceOutput)iterator.next();
                currentPixel.x = buslayerserviceoutput.x;
                currentPixel.y = buslayerserviceoutput.y;
                currentPixel = mapView.geoToPixelX(currentPixel);
                if(MathTools.computeDistance(currentPixel.x, currentPixel.y, point.x, point.y) >= (double)busIconRect.width())
                    continue;
                tappedData = buslayerserviceoutput;
                flag = true;
                break;
            } while(true);
            if(flag && tappedData != null)
                onBusIconClicked(tappedData);
        }
    }

    protected void onUpdate()
    {
        if(mapView.getCurrentLevelOrdinal() > 10)
            downloadBusService();
        invalidate();
    }

    public Bitmap busIcon;
    RectF busIconRect;
    PointF64 currentPixel;
    RectF dest;
    public boolean isBusClicked;
    final ArrayList mLastResult;
    BusLayerService mService;
    private final int minLevel;
    Paint paint;
    Paint strokePaint;
    RectF strokeRect;
    BusLayerServiceOutput tappedData;
}
