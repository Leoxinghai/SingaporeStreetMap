

package streetdirectory.mobile.modules.favorite;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.View;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.util.HashMap;
import streetdirectory.mobile.core.ui.SanListViewAdapter;
import streetdirectory.mobile.core.ui.SanListViewItem;
import streetdirectory.mobile.modules.core.LocationBusinessServiceOutput;
import streetdirectory.mobile.modules.direction.DirectionActivity;
import streetdirectory.mobile.modules.favorite.service.FavoriteRouteListServiceOutput;

public class FavoriteRouteCell extends SanListViewItem
{
    public static class FavoriteRouteCellViewHolder
    {

        public TextView addressLabel;
        public ImageButton callButton;
        public ImageView routeIcon;
        public TextView titleLabel;

        public FavoriteRouteCellViewHolder()
        {
        }
    }


    public FavoriteRouteCell(FavoriteRouteListServiceOutput favoriteroutelistserviceoutput)
    {
        data = favoriteroutelistserviceoutput;
    }

    protected Object createViewHolder(View view)
    {
        FavoriteRouteCellViewHolder favoriteroutecellviewholder = new FavoriteRouteCellViewHolder();
        favoriteroutecellviewholder.titleLabel = (TextView)view.findViewById(R.id.TitleLabel);
        favoriteroutecellviewholder.addressLabel = (TextView)view.findViewById(R.id.AboutUsLabel);
        favoriteroutecellviewholder.routeIcon = (ImageView)view.findViewById(R.id.IconButton);
        favoriteroutecellviewholder.callButton = (ImageButton)view.findViewById(R.id.imageButtonCall);
        return favoriteroutecellviewholder;
    }

    public void execute(Context context, SanListViewAdapter sanlistviewadapter)
    {
        LocationBusinessServiceOutput temp = new LocationBusinessServiceOutput();
        ((LocationBusinessServiceOutput) (temp)).hashData.put("v", data.venueFrom);
        ((LocationBusinessServiceOutput) (temp)).hashData.put("pid", data.placeIDFrom);
        ((LocationBusinessServiceOutput) (temp)).hashData.put("aid", data.addressIDFrom);
        ((LocationBusinessServiceOutput) (temp)).hashData.put("x", String.valueOf(data.longFrom));
        ((LocationBusinessServiceOutput) (temp)).hashData.put("y", String.valueOf(data.latFrom));
        temp.populateData();
        LocationBusinessServiceOutput locationbusinessserviceoutput = new LocationBusinessServiceOutput();
        locationbusinessserviceoutput.hashData.put("v", data.venueTo);
        locationbusinessserviceoutput.hashData.put("pid", data.placeIDTo);
        locationbusinessserviceoutput.hashData.put("aid", data.addressIDTo);
        locationbusinessserviceoutput.hashData.put("x", String.valueOf(data.longTo));
        locationbusinessserviceoutput.hashData.put("y", String.valueOf(data.latTo));
        locationbusinessserviceoutput.populateData();
        Intent intent = new Intent(context, DirectionActivity.class);
        intent.putExtra("startData",(Parcelable) temp);
        intent.putExtra("endData", (Parcelable)locationbusinessserviceoutput);
        context.startActivity(intent);
    }

    protected int getLayoutId()
    {
        return R.layout.cell_location_business;
    }

    protected void populateViewHolder(Object obj)
    {
        obj = (FavoriteRouteCellViewHolder)obj;
        if(data.saveName != null)
            ((FavoriteRouteCellViewHolder) (obj)).titleLabel.setText(data.saveName);
        if(data.venueFrom != null && data.venueTo != null)
            ((FavoriteRouteCellViewHolder) (obj)).addressLabel.setText((new StringBuilder()).append(data.venueFrom).append(" to ").append(data.venueTo).toString());
        ((FavoriteRouteCellViewHolder) (obj)).routeIcon.setImageResource(R.drawable.favorite_routes);
        ((FavoriteRouteCellViewHolder) (obj)).callButton.setVisibility(View.INVISIBLE);
    }

    FavoriteRouteListServiceOutput data;
    public long totalRoutes;
}
