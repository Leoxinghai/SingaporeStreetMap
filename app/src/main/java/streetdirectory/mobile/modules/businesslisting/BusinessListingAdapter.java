// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.businesslisting;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import streetdirectory.mobile.core.ui.UIHelper;
import streetdirectory.mobile.modules.businesslisting.service.BusinessListingServiceOutput;
import streetdirectory.mobile.modules.core.*;
import streetdirectory.mobile.modules.core.adapter.LocationBusinessTipsPremiumAdapter;

public class BusinessListingAdapter extends LocationBusinessTipsPremiumAdapter
{

    public BusinessListingAdapter(Context context)
    {
        super(context);
        isOfferPage = false;
    }

    public void plottingData(LocationBusinessCellViewHolder locationbusinesscellviewholder, BusinessListingServiceOutput businesslistingserviceoutput, int i)
    {
        super.plottingData(locationbusinesscellviewholder, businesslistingserviceoutput, i);
        locationbusinesscellviewholder.titleLabel.setText(businesslistingserviceoutput.venue);
        locationbusinesscellviewholder.detailLabel.setVisibility(View.INVISIBLE);
        String s = "";
        String s1;
        if(businesslistingserviceoutput.address != null)
            if(businesslistingserviceoutput.unitNo != null)
                s = (new StringBuilder()).append(businesslistingserviceoutput.unitNo).append(" ").append(businesslistingserviceoutput.address).toString();
            else
                s = businesslistingserviceoutput.address;
        if(businesslistingserviceoutput.placeName != null)
        {
            s1 = businesslistingserviceoutput.placeName;
        } else
        {
            s1 = s;
            if(businesslistingserviceoutput.busNumbers != null)
                s = businesslistingserviceoutput.busNumbers;
            else
                s = "";
        }
        locationbusinesscellviewholder.detailLabel.setVisibility(View.INVISIBLE);
        if("".equals(s1))
            locationbusinesscellviewholder.subdetailLabel.setText(s);
        else
        if("".equals(s))
            locationbusinesscellviewholder.subdetailLabel.setText(s1);
        else
            locationbusinesscellviewholder.subdetailLabel.setText((new StringBuilder()).append(s1).append(", ").append(s).toString());
        if(isOfferPage)
        {
            String tempo = locationbusinesscellviewholder.adsLabel.getText().toString();
            locationbusinesscellviewholder.titleLabel.setLines(2);
            locationbusinesscellviewholder.adsLabel.setText(locationbusinesscellviewholder.titleLabel.getText().toString());
            locationbusinesscellviewholder.titleLabel.setText(tempo);
            locationbusinesscellviewholder.titleLabel.setTypeface(null, 0);
            locationbusinesscellviewholder.titleLabel.setTextColor(0xff000000);
            locationbusinesscellviewholder.viewOfferLabel.setVisibility(View.VISIBLE);
            locationbusinesscellviewholder.subdetailLabel.setVisibility(View.INVISIBLE);
            locationbusinesscellviewholder.callButton.setVisibility(View.INVISIBLE);
            locationbusinesscellviewholder.adsLabel.setVisibility(View.INVISIBLE);
            LinearLayout.LayoutParams temp  = (android.widget.LinearLayout.LayoutParams)locationbusinesscellviewholder.titleLabel.getLayoutParams();
            temp.setMargins(0, (int)UIHelper.toPixel(2.0F), 0, 0);
            locationbusinesscellviewholder.titleLabel.setLayoutParams(temp);
        }
    }

    public void plottingData(LocationBusinessCellViewHolder locationbusinesscellviewholder, LocationBusinessServiceOutput locationbusinessserviceoutput, int i)
    {
        plottingData(locationbusinesscellviewholder, (BusinessListingServiceOutput)locationbusinessserviceoutput, i);
    }

    public void plottingData(LocationBusinessCellViewHolder locationbusinesscellviewholder, LocationBusinessTipsServiceOutput locationbusinesstipsserviceoutput, int i)
    {
        plottingData(locationbusinesscellviewholder, (BusinessListingServiceOutput)locationbusinesstipsserviceoutput, i);
    }

    public boolean isOfferPage;
}
