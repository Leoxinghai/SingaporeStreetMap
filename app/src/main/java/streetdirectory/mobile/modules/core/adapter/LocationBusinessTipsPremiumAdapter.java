// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.core.adapter;

import android.content.Context;
import android.view.View;
import android.widget.*;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.modules.core.*;

// Referenced classes of package streetdirectory.mobile.modules.core.adapter:
//            LocationBusinessTipsAdapter

public class LocationBusinessTipsPremiumAdapter extends LocationBusinessTipsAdapter
{

    public LocationBusinessTipsPremiumAdapter(Context context)
    {
        super(context);
    }

    public void plottingData(LocationBusinessCellViewHolder locationbusinesscellviewholder, LocationBusinessServiceOutput locationbusinessserviceoutput, int i)
    {
        plottingData(locationbusinesscellviewholder, (LocationBusinessTipsServiceOutput)locationbusinessserviceoutput, i);
    }

    public void plottingData(LocationBusinessCellViewHolder locationbusinesscellviewholder, LocationBusinessTipsServiceOutput locationbusinesstipsserviceoutput, int i)
    {
        plottingData(locationbusinesscellviewholder, locationbusinesstipsserviceoutput, i);
        if(locationbusinesstipsserviceoutput.offer != null && locationbusinesstipsserviceoutput.offerThumbnailImage != null)
        {
            locationbusinesscellviewholder.background.setBackgroundResource(R.drawable.selector_color_cell_location_business_premium_layout);
            locationbusinesscellviewholder.icon.getLayoutParams().height = locationbusinesscellviewholder.icon.getLayoutParams().width;
            locationbusinesscellviewholder.icon.requestLayout();
            locationbusinesscellviewholder.callButton.setVisibility(View.INVISIBLE);
            locationbusinesscellviewholder.gotoOfferLabel.setVisibility(View.VISIBLE);
            return;
        }
        locationbusinesscellviewholder.icon.getLayoutParams().height = (locationbusinesscellviewholder.icon.getLayoutParams().width * 80) / 108;
        locationbusinesscellviewholder.icon.requestLayout();
        locationbusinesscellviewholder.gotoOfferLabel.setVisibility(View.INVISIBLE);
        locationbusinesscellviewholder.background.setBackgroundResource(R.drawable.selector_color_cell_location_business_layout);
        locationbusinesscellviewholder.thumbupLayout.setVisibility(View.INVISIBLE);
        if(locationbusinesstipsserviceoutput.phoneNumber != null)
        {
            locationbusinesscellviewholder.callButton.setVisibility(View.VISIBLE);
            return;
        } else
        {
            locationbusinesscellviewholder.callButton.setVisibility(View.INVISIBLE);
            return;
        }
    }
}
