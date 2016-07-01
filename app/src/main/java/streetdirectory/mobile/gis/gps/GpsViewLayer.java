

package streetdirectory.mobile.gis.gps;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.core.PointF64;
import streetdirectory.mobile.gis.GeoPoint;
import streetdirectory.mobile.gis.maps.MapView;
import streetdirectory.mobile.gis.maps.MapViewLayer;

public class GpsViewLayer extends MapViewLayer
{

    public GpsViewLayer(Context context)
    {
        this(context, null);
    }

    public GpsViewLayer(Context context, AttributeSet attributeset)
    {
        this(context, attributeset, 0);
    }

    public GpsViewLayer(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        accuracy = 0.0F;
        paintFast = null;
        paintStroke = null;
        accuracyEnabled = false;
        GPSIcon = null;
        anim = null;
        longitude = -200D;
        latitude = -200D;
        setWillNotDraw(false);
        ((Activity)getContext()).getLayoutInflater().inflate(R.layout.view_map_gps, this, true);
        GPSIcon = (ImageView)findViewById(R.id.GPSIcon);
        if(GPSIcon != null)
            anim = (AnimationDrawable)GPSIcon.getDrawable();
        paintFast = new Paint();
        paintFast.setFilterBitmap(false);
        paintFast.setDither(false);
        paintFast.setAntiAlias(false);
        paintFast.setColor(Color.argb(80, 10, 100, 249));
        paintFast.setStyle(android.graphics.Paint.Style.FILL);
        paintStroke = new Paint();
        paintStroke.setFilterBitmap(false);
        paintStroke.setDither(false);
        paintStroke.setAntiAlias(false);
        paintStroke.setColor(Color.argb(200, 250, 250, 250));
        paintStroke.setStrokeWidth(2.0F);
        paintStroke.setStyle(android.graphics.Paint.Style.STROKE);
    }

    public void draw(Canvas canvas)
    {
        super.draw(canvas);
        if(accuracyEnabled)
        {
            PointF64 pointf64 = mapView.geoToPixel(longitude, latitude);
            canvas.drawCircle((float)pointf64.x, (float)pointf64.y, (float)((double)accuracy / mapView.mapScale), paintFast);
            canvas.drawCircle((float)pointf64.x, (float)pointf64.y, (float)((double)accuracy / mapView.mapScale), paintStroke);
        }
    }

    protected void onUpdate()
    {
        try
        {
            if(getVisibility() == 8)
                return;
        }
        catch(Exception exception)
        {
            return;
        }
        if(!GeoPoint.isValid(longitude, latitude))
        {
            GPSIcon.setVisibility(View.INVISIBLE);
            return;
        }
        GPSIcon.setVisibility(View.VISIBLE);
        android.widget.RelativeLayout.LayoutParams layoutparams = (android.widget.RelativeLayout.LayoutParams)GPSIcon.getLayoutParams();
        PointF64 pointf64 = mapView.geoToPixel(longitude, latitude);
        int i = (int)pointf64.x - layoutparams.width / 2;
        int j = (int)pointf64.y - layoutparams.height / 2;
        int k = i + layoutparams.width;
        int l = j + layoutparams.height;
        layoutparams.leftMargin = i;
        layoutparams.topMargin = j;
        layoutparams.rightMargin = k;
        layoutparams.bottomMargin = l;
        GPSIcon.layout(i, j, k, l);
        invalidate();
        return;
    }

    public void setAccuracyVisibility(boolean flag)
    {
        accuracyEnabled = flag;
        invalidate();
    }

    public void startAnimation()
    {
        if(anim != null)
            anim.start();
    }

    public void stopAnimation()
    {
        if(anim != null)
            anim.stop();
    }

    protected ImageView GPSIcon;
    public float accuracy;
    private boolean accuracyEnabled;
    protected AnimationDrawable anim;
    public double latitude;
    public double longitude;
    private Paint paintFast;
    private Paint paintStroke;
}
