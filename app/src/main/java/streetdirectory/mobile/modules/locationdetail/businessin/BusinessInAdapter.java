// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.locationdetail.businessin;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import streetdirectory.mobile.modules.core.*;
import streetdirectory.mobile.modules.core.adapter.LocationBusinessTipsPremiumAdapter;
import streetdirectory.mobile.modules.locationdetail.businessin.service.BusinessInServiceOutput;

public class BusinessInAdapter extends LocationBusinessTipsPremiumAdapter
{

    public BusinessInAdapter(Context context)
    {

        super(context);
    }

    public void plottingData(LocationBusinessCellViewHolder locationbusinesscellviewholder, LocationBusinessServiceOutput locationbusinessserviceoutput, int i)
    {
        plottingData(locationbusinesscellviewholder, (BusinessInServiceOutput)locationbusinessserviceoutput, i);
    }

    public void plottingData(LocationBusinessCellViewHolder locationbusinesscellviewholder, LocationBusinessTipsServiceOutput locationbusinesstipsserviceoutput, int i)
    {
        plottingData(locationbusinesscellviewholder, (BusinessInServiceOutput)locationbusinesstipsserviceoutput, i);
    }

    public void plottingData(LocationBusinessCellViewHolder locationbusinesscellviewholder, BusinessInServiceOutput businessinserviceoutput, int i)
    {
        plottingData(locationbusinesscellviewholder, businessinserviceoutput, i);
        locationbusinesscellviewholder.titleLabel.setText(businessinserviceoutput.venue);
        String s;
        if(businessinserviceoutput.unitNo != null)
            s = (new StringBuilder()).append("Unit: ").append(businessinserviceoutput.unitNo).toString();
        else
            s = "";
        String tempp;
        if(businessinserviceoutput.phoneNumber != null)
            tempp = (new StringBuilder()).append("Phone: ").append(businessinserviceoutput.phoneNumber).toString();
        else
            tempp = "";
        if(!"".equals(s))
            locationbusinesscellviewholder.detailLabel.setText(s);
        else
            locationbusinesscellviewholder.detailLabel.setText("Unit: -");
        if(!"".equals(businessinserviceoutput))
            locationbusinesscellviewholder.subdetailLabel.setText(tempp);
        else
            locationbusinesscellviewholder.subdetailLabel.setText("Phone: -");
        locationbusinesscellviewholder.subdetailLabel.setVisibility(View.INVISIBLE);
    }
}
