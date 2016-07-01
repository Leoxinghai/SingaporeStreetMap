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
import streetdirectory.mobile.core.*;
import streetdirectory.mobile.core.ui.UIHelper;
import streetdirectory.mobile.gis.*;
import streetdirectory.mobile.gis.maps.MapView;
import streetdirectory.mobile.gis.maps.MapViewLayer;
import streetdirectory.mobile.modules.nearby.service.*;
import streetdirectory.mobile.sd.SDBlackboard;
import streetdirectory.mobile.service.SDHttpServiceOutput;

// Referenced classes of package streetdirectory.mobile.modules.map.layers:
//            BusLayerService

public class OfferLayer extends MapViewLayer
{

    public OfferLayer(Context context)
    {
        this(context, null, 0);
    }

    public OfferLayer(Context context, int i)
    {
        this(context, null, i);
    }

    public OfferLayer(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        _nearbyService = null;
        mService = null;
        offerIcon = null;
        smallOfferIcon = null;
        mLastResult = new ArrayList();
        currentPixel = new PointF64();
        offerIconRect = null;
        dest = null;
        smallOfferIconRect = null;
        smallDest = null;
        minLevel = 10;
        lastLongitude = 0.0D;
        lastLatitude = 0.0D;
        lastRadius = 0.0D;
        setWillNotDraw(false);
        paint = new Paint();
        paint.setColor(Color.rgb(200, 0, 0));
        paint.setStyle(android.graphics.Paint.Style.FILL_AND_STROKE);
        offerIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_offer_pin);
        smallOfferIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_small_offer_pin);
        offerIconRect = new RectF(0.0F, 0.0F, UIHelper.toPixel(7F), UIHelper.toPixel(15F));
        dest = new RectF(offerIconRect);
        smallOfferIconRect = new RectF(0.0F, 0.0F, UIHelper.toPixel(5F), UIHelper.toPixel(5F));
        smallDest = new RectF(smallOfferIconRect);
    }

    private void downloadNearbyOffers()
    {
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
        float f = ProjectionCoordinateFactory.getWgs1984Mercator().distFrom(geopoint.latitude, geopoint.longitude, geopoint1.latitude, geopoint1.longitude) / 2.0F;
        if(MathTools.computeDistance(ProjectionCoordinateFactory.getWgs1984Mercator().geoToMetric(new GeoPoint(lastLongitude, lastLatitude)), ProjectionCoordinateFactory.getWgs1984Mercator().geoToMetric(mapView.center)) > lastRadius * 500D)
        {
            if(_nearbyService != null)
            {
                _nearbyService.abort();
                _nearbyService = null;
            }
            lastLongitude = mapView.center.longitude;
            lastLatitude = mapView.center.latitude;
            lastRadius = f;
            _nearbyService = new NearbyService(new NearbyServiceInput(SDBlackboard.currentCountryCode, 2, SDBlackboard.currentLongitude, SDBlackboard.currentLatitude, f, 0, 100, 11342, 0, true)) {

                public void onSuccess(Object obj)
                {
                    onSuccess((SDHttpServiceOutput)obj);
                }

                public void onSuccess(SDHttpServiceOutput sdhttpserviceoutput)
                {
                    super.onSuccess(sdhttpserviceoutput);
                    mLastResult.clear();
                    mLastResult.addAll(sdhttpserviceoutput.childs);
                    SDLogger.debug((new StringBuilder()).append("found offer:").append(mLastResult.size()).append(" r:").append(lastRadius).toString());
                    postInvalidate();
                }

            };
            _nearbyService.executeAsync();
        }
    }

    public void draw(Canvas canvas)
    {
        if(smallOfferIcon != null && mapView.getCurrentLevelOrdinal() == 10)
        {
            for(Iterator iterator = mLastResult.iterator(); iterator.hasNext(); canvas.drawBitmap(smallOfferIcon, null, smallDest, paint))
            {
                NearbyServiceOutput nearbyserviceoutput = (NearbyServiceOutput)iterator.next();
                currentPixel.x = nearbyserviceoutput.longitude;
                currentPixel.y = nearbyserviceoutput.latitude;
                currentPixel = mapView.geoToPixelX(currentPixel);
                smallDest.set(smallOfferIconRect);
                smallDest.offset((float)currentPixel.x - smallOfferIconRect.width() / 2.0F, (float)currentPixel.y - smallOfferIconRect.height() / 2.0F);
            }

        } else
        if(offerIcon != null && mapView.getCurrentLevelOrdinal() > 10)
        {
            for(Iterator iterator1 = mLastResult.iterator(); iterator1.hasNext(); canvas.drawBitmap(offerIcon, null, dest, paint))
            {
                NearbyServiceOutput nearbyserviceoutput1 = (NearbyServiceOutput)iterator1.next();
                currentPixel.x = nearbyserviceoutput1.longitude;
                currentPixel.y = nearbyserviceoutput1.latitude;
                currentPixel = mapView.geoToPixelX(currentPixel);
                dest.set(offerIconRect);
                dest.offset((float)currentPixel.x - offerIconRect.width() / 2.0F, (float)currentPixel.y - offerIconRect.height() / 2.0F);
            }

        }
        super.draw(canvas);
    }

    protected void onUpdate()
    {
        if(mapView.getCurrentLevelOrdinal() >= 10)
            downloadNearbyOffers();
        invalidate();
    }

    NearbyService _nearbyService;
    PointF64 currentPixel;
    RectF dest;
    public double lastLatitude;
    public double lastLongitude;
    public double lastRadius;
    final ArrayList mLastResult;
    BusLayerService mService;
    private final int minLevel;
    public Bitmap offerIcon;
    RectF offerIconRect;
    Paint paint;
    RectF smallDest;
    public Bitmap smallOfferIcon;
    RectF smallOfferIconRect;
}
