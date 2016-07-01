// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.locationdetail.businessin;

import android.content.Context;
import android.content.res.Resources;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.modules.core.*;
import streetdirectory.mobile.modules.core.adapter.LocationBusinessTipsPremiumAdapter;
import streetdirectory.mobile.modules.locationdetail.businessin.service.BusinessInByCategoryServiceOutput;

public class BusinessesByCategoryAdapter extends LocationBusinessTipsPremiumAdapter
{

    public BusinessesByCategoryAdapter(Context context)
    {
        super(context);
    }

    public void plottingData(LocationBusinessCellViewHolder locationbusinesscellviewholder, LocationBusinessServiceOutput locationbusinessserviceoutput, int i)
    {
        plottingData(locationbusinesscellviewholder, (BusinessInByCategoryServiceOutput)locationbusinessserviceoutput, i);
    }

    public void plottingData(LocationBusinessCellViewHolder locationbusinesscellviewholder, LocationBusinessTipsServiceOutput locationbusinesstipsserviceoutput, int i)
    {
        plottingData(locationbusinesscellviewholder, (BusinessInByCategoryServiceOutput)locationbusinesstipsserviceoutput, i);
    }

    public void plottingData(LocationBusinessCellViewHolder locationbusinesscellviewholder, BusinessInByCategoryServiceOutput businessinbycategoryserviceoutput, int i)
    {
        plottingData(locationbusinesscellviewholder, businessinbycategoryserviceoutput, i);
        String s;
        if(businessinbycategoryserviceoutput.image == null)
        {
            if(businessinbycategoryserviceoutput.type == 1)
                locationbusinesscellviewholder.icon.setImageDrawable(_context.getResources().getDrawable(R.drawable.location_no_photo));
            else
                locationbusinesscellviewholder.icon.setImageDrawable(_context.getResources().getDrawable(R.drawable.business_no_photo));
            if(_imageNotFoundListener != null)
                _imageNotFoundListener.onImageNotFound(businessinbycategoryserviceoutput, i, imageWidth, imageHeight);
        } else
        {
            locationbusinesscellviewholder.icon.setImageBitmap(businessinbycategoryserviceoutput.image);
        }
        locationbusinesscellviewholder.titleLabel.setText(businessinbycategoryserviceoutput.venue);
        if(businessinbycategoryserviceoutput.unitNo != null)
            s = (new StringBuilder()).append("Unit: ").append(businessinbycategoryserviceoutput.unitNo).toString();
        else
            s = "";
        String temp;
        if(businessinbycategoryserviceoutput.phoneNumber != null)
            temp = (new StringBuilder()).append("Phone: ").append(businessinbycategoryserviceoutput.phoneNumber).toString();
        else
            temp = "";
        if(!"".equals(s))
            locationbusinesscellviewholder.detailLabel.setText(s);
        else
            locationbusinesscellviewholder.detailLabel.setText("Unit: -");
        if(!"".equals(businessinbycategoryserviceoutput))
        {
            locationbusinesscellviewholder.subdetailLabel.setText(temp);
            return;
        } else
        {
            locationbusinesscellviewholder.subdetailLabel.setText("Phone: -");
            return;
        }
    }
}
