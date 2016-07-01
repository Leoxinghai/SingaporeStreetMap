package streetdirectory.mobile.gis.maps;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.core.PointF64;
import streetdirectory.mobile.core.SDLogger;
import streetdirectory.mobile.core.ui.AnimationTools;
import streetdirectory.mobile.core.ui.CanvasLayout;
import streetdirectory.mobile.modules.nearby.service.NearbyServiceOutput;

// Referenced classes of package streetdirectory.mobile.gis.maps:
//            MapViewLayer, MapView

public class MapPinLayer extends MapViewLayer
{

    public MapPinLayer(Context context)
    {
        this(context, null);
    }

    public MapPinLayer(Context context, AttributeSet attributeset)
    {
        this(context, attributeset, 0);
    }

    public MapPinLayer(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        longitude = 106.84517200000005D;
        latitude = -6.211544D;
        canvas = new CanvasLayout(context);
        RelativeLayout.LayoutParams layoutParams = new android.widget.RelativeLayout.LayoutParams(-1, -1);
        canvas.setLayoutParams(layoutParams);
        pinLayout = (RelativeLayout)((Activity)context).getLayoutInflater().inflate(R.layout.layout_map_tooltip, this, false);
        RelativeLayout.LayoutParams layoutParams1 = (android.widget.RelativeLayout.LayoutParams)pinLayout.getLayoutParams();
        canvas.addView(pinLayout);
        addView(canvas);
        balloonLayout = (RelativeLayout)pinLayout.findViewById(R.id.tooltipLayout);
        balloonLayout.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                if(onTapListener != null)
                    onTapListener.onClick(balloonLayout);
            }
        });

        pinButton = (ImageView)pinLayout.findViewById(R.id.pinButton);
        pinButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                if(balloonLayout.getVisibility() == 8)
                {
                    showBalloon();
                    return;
                } else
                {
                    hideBalloon();
                    return;
                }
            }

        });

        textViewDesc = (TextView)pinLayout.findViewById(R.id.textViewDesc);
    }

    public void hideBalloon()
    {
        AnimationTools.fadeOut(balloonLayout, 200);
    }

    public boolean isBalloonVisible()
    {
        return balloonLayout.getVisibility() == 0;
    }

    protected void onUpdate()
    {
        if(getVisibility() == 8)
            return;
        try
        {
            android.widget.RelativeLayout.LayoutParams layoutparams = (android.widget.RelativeLayout.LayoutParams)pinLayout.getLayoutParams();
            PointF64 pointf64 = mapView.geoToPixel(longitude, latitude);
            int i = (int)pointf64.x - layoutparams.width / 2;
            int j = (int)pointf64.y - layoutparams.height;
            int k = layoutparams.width;
            int l = layoutparams.height;
            layoutparams.leftMargin = (int)pointf64.x - layoutparams.width / 2;
            layoutparams.topMargin = (int)pointf64.y - layoutparams.height;
            layoutparams.rightMargin = layoutparams.leftMargin + layoutparams.width;
            layoutparams.bottomMargin = layoutparams.topMargin + layoutparams.height;
            int i1 = layoutparams.height;
            pinLayout.layout(i, j, i + k, j + l);
            return;
        }
        catch(Exception exception)
        {
            SDLogger.printStackTrace(exception, "PinMapLayerBase onUpdate");
        }
        return;
    }

    public void setOnTapListener(android.view.View.OnClickListener onclicklistener)
    {
        onTapListener = onclicklistener;
    }

    public void setTitle(String s)
    {
        title = s;
        textViewDesc.setText(s);
    }

    public void showBallonVisibility(int i, NearbyServiceOutput nearbyserviceoutput)
    {
        setVisibility(i);
        balloonLayout.setVisibility(i);
        pinButton.setVisibility(View.INVISIBLE);
        if(i == 0)
            balloonLayout.setTag(nearbyserviceoutput);
        invalidate();
    }

    public void showBalloon()
    {
        AnimationTools.fadeIn(balloonLayout, 200);
    }

    RelativeLayout balloonLayout;
    CanvasLayout canvas;
    public double latitude;
    public double longitude;
    android.view.View.OnClickListener onTapListener;
    ImageView pinButton;
    RelativeLayout pinLayout;
    TextView textViewDesc;
    public String title;
}
