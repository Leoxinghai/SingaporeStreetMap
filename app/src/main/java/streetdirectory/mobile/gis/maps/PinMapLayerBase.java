
package streetdirectory.mobile.gis.maps;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.*;
import android.widget.*;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.core.PointF64;
import streetdirectory.mobile.core.SDLogger;


public class PinMapLayerBase extends MapViewLayer
{
    public class PinAnimation extends Animation
    {

        protected void applyTransformation(float f, Transformation transformation)
        {
            double d = toX;
            double d1 = fromX;
            double d2 = f;
            double d3 = toY;
            double d4 = fromY;
            double d5 = f;
            RelativeLayout.LayoutParams layoutParams = (android.widget.RelativeLayout.LayoutParams)pinButton.getLayoutParams();
            int i = (int)fromX + ((int)((d - d1) * d2) - ((android.widget.RelativeLayout.LayoutParams) (layoutParams)).width / 2);
            int j = (int)fromY + ((int)((d3 - d4) * d5) - ((android.widget.RelativeLayout.LayoutParams) (layoutParams)).height);
            int k = ((android.widget.RelativeLayout.LayoutParams) (layoutParams)).width;
            int l = ((android.widget.RelativeLayout.LayoutParams) (layoutParams)).height;
            pinButton.layout(i, j, i + k, j + l);
        }

        public double fromX;
        public double fromY;
        public double toX;
        public double toY;

        public PinAnimation()
        {
            super();
        }
    }


    public PinMapLayerBase(Context context)
    {
        this(context, null);
    }

    public PinMapLayerBase(Context context, AttributeSet attributeset)
    {
        this(context, attributeset, 0);
    }

    public PinMapLayerBase(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        pinAnimation = null;
        ((Activity)getContext()).getLayoutInflater().inflate(R.layout.layout_map_tooltip, this, true);
        initLayout();
        initEvent();
    }

    private void initEvent()
    {
        pinButton.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                toogleTooltipBallon();
            }

        });
    }

    private void initLayout()
    {
        tooltipLayout = (RelativeLayout)findViewById(R.id.tooltipLayout);
        titleLabel = (TextView)findViewById(R.id.TitleLabel);
        titleLabel.setMaxLines(2);
    }

    public void animatePin()
    {
        if(pinAnimation != null)
            pinAnimation.reset();
        pinAnimation = new PinAnimation();
        PointF64 pointf64 = mapView.geoToPixel(longitude, latitude);
        pinAnimation.toX = pointf64.x;
        pinAnimation.toY = pointf64.y;
        pinAnimation.fromX = pointf64.x;
        pinAnimation.fromY = 0.0D;
        pinAnimation.setInterpolator(new AccelerateInterpolator());
        pinAnimation.setDuration(200L);
        startAnimation(pinAnimation);
    }

    protected void onUpdate()
    {
        if(getVisibility() == 8)
            return;
        try
        {
            android.widget.RelativeLayout.LayoutParams layoutparams = (android.widget.RelativeLayout.LayoutParams)pinButton.getLayoutParams();
            PointF64 pointf64 = mapView.geoToPixel(longitude, latitude);
            int j = (int)pointf64.x - layoutparams.width / 2;
            int l = (int)pointf64.y - layoutparams.height;
            int j1 = layoutparams.width;
            int l1 = layoutparams.height;
            layoutparams.leftMargin = (int)pointf64.x - layoutparams.width / 2;
            layoutparams.topMargin = (int)pointf64.y - layoutparams.height;
            layoutparams.rightMargin = layoutparams.leftMargin + layoutparams.width;
            layoutparams.bottomMargin = layoutparams.topMargin + layoutparams.height;
            int i = layoutparams.height;
            pinButton.layout(j, l, j + j1, l + l1);
            if(tooltipLayout.isShown())
            {
                android.widget.RelativeLayout.LayoutParams layoutparams1 = (android.widget.RelativeLayout.LayoutParams)tooltipLayout.getLayoutParams();
                int k = (int)pointf64.x - layoutparams1.width / 2;
                int i1 = (int)pointf64.y - layoutparams1.height - i;
                int k1 = layoutparams1.width;
                int i2 = layoutparams1.height;
                layoutparams1.leftMargin = (int)pointf64.x - layoutparams1.width / 2;
                layoutparams1.topMargin = (int)pointf64.y - layoutparams1.height - i;
                layoutparams1.rightMargin = layoutparams1.leftMargin + layoutparams1.width;
                layoutparams1.bottomMargin = layoutparams1.topMargin + layoutparams1.height;
                tooltipLayout.layout(k, i1, k + k1, i1 + i2);
                return;
            }
        }
        catch(Exception exception)
        {
            SDLogger.printStackTrace(exception, "PinMapLayerBase onUpdate");
        }
        return;
    }

    public void toogleTooltipBallon()
    {
        if(tooltipLayout.getVisibility() == 0)
        {
            tooltipLayout.setVisibility(View.INVISIBLE);
            return;
        } else
        {
            tooltipLayout.setVisibility(View.VISIBLE);
            return;
        }
    }

    public double latitude;
    public double longitude;
    PinAnimation pinAnimation;
    protected ImageView pinButton;
    public TextView titleLabel;
    protected RelativeLayout tooltipLayout;
}
