

package streetdirectory.mobile.modules.businesslisting.offers;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;

import com.xinghai.mycurve.R;

import java.util.ArrayList;
import java.util.Iterator;
import streetdirectory.mobile.core.MathTools;
import streetdirectory.mobile.core.PointF64;
import streetdirectory.mobile.core.ui.UIHelper;
import streetdirectory.mobile.gis.GeoPoint;
import streetdirectory.mobile.gis.maps.MapView;
import streetdirectory.mobile.gis.maps.MapViewLayer;
import streetdirectory.mobile.modules.nearby.service.NearbyServiceOutput;

public class BusinessListingLayer extends MapViewLayer
{

    public BusinessListingLayer(Context context)
    {
        this(context, null, 0);
    }

    public BusinessListingLayer(Context context, int i)
    {
        this(context, null, i);
    }

    public BusinessListingLayer(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        pinIcon = null;
        nearbyData = new ArrayList();
        currentPixel = new PointF64();
        pinIconRect = null;
        dest = null;
        minLevel = 10;
        isPinClicked = false;
        setWillNotDraw(false);
        paint = new Paint();
        paint.setColor(Color.rgb(200, 0, 0));
        paint.setStyle(android.graphics.Paint.Style.FILL_AND_STROKE);
        pinIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.map_pin_red);
        pinIconRect = new RectF(0.0F, 0.0F, UIHelper.toPixel(35F), UIHelper.toPixel(45F));
        dest = new RectF(pinIconRect);
    }

    public void draw(Canvas canvas)
    {
        if(pinIcon != null)
        {
            for(Iterator iterator = nearbyData.iterator(); iterator.hasNext(); canvas.drawBitmap(pinIcon, null, dest, paint))
            {
                NearbyServiceOutput nearbyserviceoutput = (NearbyServiceOutput)iterator.next();
                currentPixel.x = nearbyserviceoutput.longitude;
                currentPixel.y = nearbyserviceoutput.latitude;
                currentPixel = mapView.geoToPixelX(currentPixel);
                dest.set(pinIconRect);
                dest.offset((float)currentPixel.x - pinIconRect.width() / 2.0F, (float)currentPixel.y - pinIconRect.height());
            }

        }
        super.draw(canvas);
    }

    protected void onMapLayerClicked(GeoPoint geopoint, Point point)
    {
        super.onMapLayerClicked(geopoint, point);
        isPinClicked = false;
        if(pinIcon != null)
        {
            Object obj = null;
            Iterator iterator = nearbyData.iterator();
            NearbyServiceOutput temp = null;
            do
            {
                if(!iterator.hasNext())
                    break;
                temp = (NearbyServiceOutput)iterator.next();
                currentPixel.x = ((NearbyServiceOutput) (temp)).longitude;
                currentPixel.y = ((NearbyServiceOutput) (temp)).latitude;
                currentPixel = mapView.geoToPixelX(currentPixel);
            } while(MathTools.computeDistance(currentPixel.x, currentPixel.y, point.x, point.y) >= (double)pinIconRect.width());
            if(geopoint != null)
                onPinIconClicked(temp);
        }
    }

    protected void onPinIconClicked(NearbyServiceOutput nearbyserviceoutput)
    {
        isPinClicked = true;
    }

    protected void onUpdate()
    {
        invalidate();
    }

    PointF64 currentPixel;
    RectF dest;
    public boolean isPinClicked;
    private final int minLevel;
    final ArrayList nearbyData;
    Paint paint;
    public Bitmap pinIcon;
    RectF pinIconRect;
}
