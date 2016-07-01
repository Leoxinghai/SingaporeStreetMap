// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.favorite;

import android.content.Context;
import android.widget.TextView;
import streetdirectory.mobile.modules.core.*;
import streetdirectory.mobile.modules.core.adapter.LocationBusinessTipsPremiumAdapter;
import streetdirectory.mobile.modules.favorite.service.FavoriteListServiceOutput;

public class FavoriteAdapter extends LocationBusinessTipsPremiumAdapter
{

    public FavoriteAdapter(Context context)
    {
        super(context);
    }

    public void plottingData(LocationBusinessCellViewHolder locationbusinesscellviewholder, LocationBusinessServiceOutput locationbusinessserviceoutput, int i)
    {
        plottingData(locationbusinesscellviewholder, (FavoriteListServiceOutput)locationbusinessserviceoutput, i);
    }

    public void plottingData(LocationBusinessCellViewHolder locationbusinesscellviewholder, LocationBusinessTipsServiceOutput locationbusinesstipsserviceoutput, int i)
    {
        plottingData(locationbusinesscellviewholder, (FavoriteListServiceOutput)locationbusinesstipsserviceoutput, i);
    }

    public void plottingData(LocationBusinessCellViewHolder locationbusinesscellviewholder, FavoriteListServiceOutput favoritelistserviceoutput, int i)
    {
        super.plottingData(locationbusinesscellviewholder, favoritelistserviceoutput, i);
        locationbusinesscellviewholder.titleLabel.setText(favoritelistserviceoutput.venue);
    }
}
