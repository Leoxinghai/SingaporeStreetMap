// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.map.layers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.util.ArrayList;
import java.util.Iterator;
import streetdirectory.mobile.core.PointF64;
import streetdirectory.mobile.core.SDLogger;
import streetdirectory.mobile.core.ui.AnimationTools;
import streetdirectory.mobile.core.ui.CanvasLayout;
import streetdirectory.mobile.gis.maps.MapView;
import streetdirectory.mobile.gis.maps.MapViewLayer;
import streetdirectory.mobile.modules.businessdetail.service.BusinessBranchServiceOutput;

public class BranchLayer extends MapViewLayer
{
    public static interface OnBranchPinClickListener
    {

        public abstract void onClicked(BusinessBranchServiceOutput businessbranchserviceoutput);
    }


    public BranchLayer(Context context)
    {
        this(context, null, 0);
    }

    public BranchLayer(Context context, AttributeSet attributeset)
    {
        this(context, attributeset, 0);
    }

    public BranchLayer(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        pinsLayout = new ArrayList();
        branches = new ArrayList();
        setWillNotDraw(false);
        canvas = new CanvasLayout(context);
        RelativeLayout.LayoutParams layoutParams = new android.widget.RelativeLayout.LayoutParams(-1, -1);
        canvas.setLayoutParams(layoutParams);
        addView(canvas);
    }

    private RelativeLayout createPinLayout(final BusinessBranchServiceOutput branch)
    {
        RelativeLayout relativelayout = (RelativeLayout)((Activity)getContext()).getLayoutInflater().inflate(R.layout.layout_map_tooltip, this, false);
        final RelativeLayout balloonLayout = (RelativeLayout)relativelayout.findViewById(R.id.tooltipLayout);
        balloonLayout.setVisibility(View.INVISIBLE);
        balloonLayout.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                if(onTapListener != null)
                    onTapListener.onClicked(branch);
            }

        });

        ((ImageView)relativelayout.findViewById(R.id.pinButton)).setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                if(balloonLayout.getVisibility() == 8)
                {
                    for(Iterator iterator = pinsLayout.iterator(); iterator.hasNext();)
                    {
                        RelativeLayout relativelayout1 = (RelativeLayout)iterator.next();
                        int i = 0;
                        while(i < relativelayout1.getChildCount())
                        {
                            View view1 = relativelayout1.getChildAt(i);
                            if((view1 instanceof RelativeLayout) && view1.getVisibility() == 0)
                                view1.setVisibility(View.INVISIBLE);
                            i++;
                        }
                    }

                    showBalloon(balloonLayout);
                    return;
                } else
                {
                    hideBalloon(balloonLayout);
                    return;
                }
            }

        }
);
        ((TextView)relativelayout.findViewById(R.id.textViewDesc)).setText(branch.branchName);
        return relativelayout;
    }

    public void hideBalloon(RelativeLayout relativelayout)
    {
        AnimationTools.fadeOut(relativelayout, 200);
    }

    protected void onDraw(Canvas canvas1)
    {
        super.onDraw(canvas1);
        if(getVisibility() == 8)
            return;
        int i;
        i = 0;
        try {
			for(;i < branches.size();) {
				View view = (View)pinsLayout.get(i);
				Object obj = (BusinessBranchServiceOutput)branches.get(i);
				android.widget.RelativeLayout.LayoutParams layoutparams = (android.widget.RelativeLayout.LayoutParams)view.getLayoutParams();
				obj = mapView.geoToPixel(((BusinessBranchServiceOutput) (obj)).longitude, ((BusinessBranchServiceOutput) (obj)).latitude);
				int j = (int)((PointF64) (obj)).x - layoutparams.width / 2;
				int k = (int)((PointF64) (obj)).y - layoutparams.height;
				int l = layoutparams.width;
				int i1 = layoutparams.height;
				layoutparams.leftMargin = (int)((PointF64) (obj)).x - layoutparams.width / 2;
				layoutparams.topMargin = (int)((PointF64) (obj)).y - layoutparams.height;
				layoutparams.rightMargin = layoutparams.leftMargin + layoutparams.width;
				layoutparams.bottomMargin = layoutparams.topMargin + layoutparams.height;
				int j1 = layoutparams.height;
				view.layout(j, k, j + l, k + i1);
				i++;
			}

		} catch(Exception exception) {
			SDLogger.printStackTrace(exception, "PinMapLayerBase onUpdate");
			return;
		}
    }

    protected void onUpdate()
    {
        invalidate();
    }

    public void populateBranchesPin(ArrayList arraylist)
    {
        branches = arraylist;
        RelativeLayout relativelayout;
        for(Iterator iterator  = arraylist.iterator(); iterator.hasNext(); canvas.addView(relativelayout))
        {
            relativelayout = createPinLayout((BusinessBranchServiceOutput)iterator.next());
            pinsLayout.add(relativelayout);
        }

        update();
    }

    public void setOnTapListener(OnBranchPinClickListener onbranchpinclicklistener)
    {
        onTapListener = onbranchpinclicklistener;
    }

    public void showBalloon(RelativeLayout relativelayout)
    {
        AnimationTools.fadeIn(relativelayout, 200);
    }

    private static final int minLevel = 7;
    ArrayList branches;
    CanvasLayout canvas;
    OnBranchPinClickListener onTapListener;
    ArrayList pinsLayout;
}
