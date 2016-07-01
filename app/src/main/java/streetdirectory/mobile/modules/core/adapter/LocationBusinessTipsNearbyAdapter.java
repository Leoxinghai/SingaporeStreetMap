// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.core.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.*;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.util.ArrayList;
import streetdirectory.mobile.core.ui.UIHelper;
import streetdirectory.mobile.modules.core.*;
import streetdirectory.mobile.modules.nearby.service.NearbyServiceOutput;
import streetdirectory.mobile.service.SDHttpServiceOutput;

// Referenced classes of package streetdirectory.mobile.modules.core.adapter:
//            LocationBusinessTipsAdapter

public class LocationBusinessTipsNearbyAdapter extends LocationBusinessTipsAdapter
{
    public static interface OnKmRangeClickListener
    {

        public abstract void onKmRangeClicked();
    }


    public LocationBusinessTipsNearbyAdapter(Context context, int i)
    {
        super(context);
        isOfferPage = false;
        _kmRange = i;
    }

    public int getCount()
    {
        int i = _data.childs.size();
        if(i > 0)
            return i + 1;
        else
            return 0;
    }

    public int getItemViewType(int i)
    {
        int j = _data.childs.size();
        if(i < j)
            return 0;
        return i != j || (long)j < _data.total ? 1 : 2;
    }

    public View getView(int i, View view, ViewGroup viewgroup)
    {
        view = getView(i, view, viewgroup);
        View view1 = view;
        KmRangeCellViewHolder kmRangeCellViewHolder;
        if(getItemViewType(i) == 2)
        {
            if(view == null)
            {
                view = ((LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.cell_km_range, viewgroup, false);
                kmRangeCellViewHolder = new KmRangeCellViewHolder();
                kmRangeCellViewHolder.kmRangeButton = (Button)view.findViewById(R.id.KmRangeButton);
                ((KmRangeCellViewHolder) (kmRangeCellViewHolder)).kmRangeButton.setOnClickListener(new android.view.View.OnClickListener() {

                    public void onClick(View view2)
                    {
                        if(_kmRangeClickListener != null)
                            _kmRangeClickListener.onKmRangeClicked();
                    }

                });
                view.setTag(viewgroup);
            } else
            {
                kmRangeCellViewHolder = (KmRangeCellViewHolder)view.getTag();
            }
            ((KmRangeCellViewHolder) (kmRangeCellViewHolder)).kmRangeButton.setText((new StringBuilder()).append("Within ").append(_kmRange).append(" Km").toString());
            view1 = view;
        }
        return view1;
    }

    public int getViewTypeCount()
    {
        return 3;
    }

    public void plottingData(LocationBusinessCellViewHolder locationbusinesscellviewholder, LocationBusinessServiceOutput locationbusinessserviceoutput, int i)
    {
        plottingData(locationbusinesscellviewholder, (NearbyServiceOutput)locationbusinessserviceoutput, i);
    }

    public void plottingData(LocationBusinessCellViewHolder locationbusinesscellviewholder, LocationBusinessTipsServiceOutput locationbusinesstipsserviceoutput, int i)
    {
        plottingData(locationbusinesscellviewholder, (NearbyServiceOutput)locationbusinesstipsserviceoutput, i);
    }

    public void plottingData(LocationBusinessCellViewHolder locationbusinesscellviewholder, NearbyServiceOutput nearbyserviceoutput, int i)
    {
        plottingData(locationbusinesscellviewholder, nearbyserviceoutput, i);
        locationbusinesscellviewholder.subdetailLabel.setVisibility(View.INVISIBLE);
        locationbusinesscellviewholder.titleLabel.setText(nearbyserviceoutput.venue);
        if(nearbyserviceoutput.distance != null && nearbyserviceoutput.distance.length() > 0)
        {
            locationbusinesscellviewholder.iconLocation.setVisibility(View.VISIBLE);
            locationbusinesscellviewholder.distanceLabel.setVisibility(View.VISIBLE);
            locationbusinesscellviewholder.distanceLabel.setText(nearbyserviceoutput.distance);
        } else
        {
            locationbusinesscellviewholder.iconLocation.setVisibility(View.INVISIBLE);
            locationbusinesscellviewholder.distanceLabel.setVisibility(View.INVISIBLE);
        }
        locationbusinesscellviewholder.detailLabel.setText(nearbyserviceoutput.address);
        if(nearbyserviceoutput.phoneNumber != null)
            locationbusinesscellviewholder.callButton.setVisibility(View.VISIBLE);
        if(isOfferPage)
        {
            Object obj = locationbusinesscellviewholder.adsLabel.getText().toString();
            locationbusinesscellviewholder.titleLabel.setLines(2);
            locationbusinesscellviewholder.adsLabel.setText(locationbusinesscellviewholder.titleLabel.getText().toString());
            locationbusinesscellviewholder.titleLabel.setText(((CharSequence) (obj)));
            locationbusinesscellviewholder.titleLabel.setTypeface(null, 0);
            locationbusinesscellviewholder.titleLabel.setTextColor(0xff000000);
            locationbusinesscellviewholder.viewOfferLabel.setVisibility(View.VISIBLE);
            locationbusinesscellviewholder.distanceLabel.setTextColor(Color.parseColor("#7788ff"));
            locationbusinesscellviewholder.distanceLabel.setTypeface(null, 0);
            locationbusinesscellviewholder.iconLocation.setVisibility(View.INVISIBLE);
            locationbusinesscellviewholder.detailLabel.setVisibility(View.INVISIBLE);
            locationbusinesscellviewholder.callButton.setVisibility(View.INVISIBLE);
            locationbusinesscellviewholder.adsLabel.setVisibility(View.INVISIBLE);
            obj = (android.widget.RelativeLayout.LayoutParams)locationbusinesscellviewholder.distanceLabel.getLayoutParams();
            ((android.widget.RelativeLayout.LayoutParams) (obj)).setMargins(0, (int)UIHelper.toPixel(18F), 0, 0);
            locationbusinesscellviewholder.distanceLabel.setLayoutParams(((android.view.ViewGroup.LayoutParams) (obj)));
            obj = (android.widget.LinearLayout.LayoutParams)locationbusinesscellviewholder.titleLabel.getLayoutParams();
            ((android.widget.LinearLayout.LayoutParams) (obj)).setMargins(0, (int)UIHelper.toPixel(2.0F), 0, 0);
            locationbusinesscellviewholder.titleLabel.setLayoutParams(((android.view.ViewGroup.LayoutParams) (obj)));
        }
        if(nearbyserviceoutput.offer != null && nearbyserviceoutput.offerThumbnailImage != null)
        {
            locationbusinesscellviewholder.background.setBackgroundResource(R.drawable.selector_color_cell_location_business_premium_layout);
            locationbusinesscellviewholder.icon.getLayoutParams().height = locationbusinesscellviewholder.icon.getLayoutParams().width;
            locationbusinesscellviewholder.icon.requestLayout();
            locationbusinesscellviewholder.callButton.setVisibility(View.INVISIBLE);
            locationbusinesscellviewholder.gotoOfferLabel.setVisibility(View.VISIBLE);
            return;
        } else
        {
            locationbusinesscellviewholder.icon.getLayoutParams().height = (locationbusinesscellviewholder.icon.getLayoutParams().width * 80) / 108;
            locationbusinesscellviewholder.icon.requestLayout();
            locationbusinesscellviewholder.gotoOfferLabel.setVisibility(View.INVISIBLE);
            locationbusinesscellviewholder.background.setBackgroundResource(R.drawable.selector_color_cell_location_business_layout);
            return;
        }
    }

    public void setKmRange(int i)
    {
        _kmRange = i;
    }

    public void setOnKmRangeClickListener(OnKmRangeClickListener onkmrangeclicklistener)
    {
        _kmRangeClickListener = onkmrangeclicklistener;
    }

    public static final int KM_RANGE_CELL = 2;
    protected int _kmRange;
    protected OnKmRangeClickListener _kmRangeClickListener;
    public boolean isOfferPage;
}
