

package streetdirectory.mobile.modules.favorite;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.view.View;
import android.widget.*;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.core.ui.SanListViewAdapter;
import streetdirectory.mobile.core.ui.SanListViewItem;
import streetdirectory.mobile.modules.businessdetail.BusinessDetailActivity;
import streetdirectory.mobile.modules.favorite.service.FavoriteListServiceOutput;
import streetdirectory.mobile.modules.locationdetail.bus.BusArrivalActivity;
import streetdirectory.mobile.modules.locationdetail.businessin.BusinessInActivity;
import streetdirectory.mobile.modules.locationdetail.erp.ErpDetailActivity;
import streetdirectory.mobile.modules.locationdetail.expresswayexit.ExpressWayExitActivity;
import streetdirectory.mobile.modules.locationdetail.trafficcam.TrafficCameraLocationDetailActivity;

public class FavoritePlaceCell extends SanListViewItem
{
    public static class FavoriteCellViewHolder
    {

        public TextView addressLabel;
        public ImageButton callButton;
        public ImageView iconButton;
        public TextView titleLabel;

        public FavoriteCellViewHolder()
        {
        }
    }


    public FavoritePlaceCell(FavoriteListServiceOutput favoritelistserviceoutput)
    {
        data = favoritelistserviceoutput;
    }

    protected Object createViewHolder(View view)
    {
        FavoriteCellViewHolder favoritecellviewholder = new FavoriteCellViewHolder();
        favoritecellviewholder.titleLabel = (TextView)view.findViewById(R.id.TitleLabel);
        favoritecellviewholder.addressLabel = (TextView)view.findViewById(R.id.AboutUsLabel);
        favoritecellviewholder.iconButton = (ImageView)view.findViewById(R.id.IconButton);
        favoritecellviewholder.callButton = (ImageButton)view.findViewById(R.id.imageButtonCall);
        return favoritecellviewholder;
    }

    public void execute(Context context, SanListViewAdapter sanlistviewadapter)
    {
        Class temp;
        if(data.type == 2)
            temp = BusinessDetailActivity.class;
        else
        if(data.categoryID == 1118)
            temp = TrafficCameraLocationDetailActivity.class;
        else
        if(data.categoryID == 93)
            temp = BusArrivalActivity.class;
        else
        if(data.categoryID == 29)
            temp = ExpressWayExitActivity.class;
        else
        if(data.categoryID == 28)
            temp = ErpDetailActivity.class;
        else
            temp = BusinessInActivity.class;
        Intent intent = new Intent(context, temp);
        intent.putExtra("data", (Parcelable)data);
        context.startActivity(intent);
    }

    protected int getLayoutId()
    {
        return R.layout.cell_location_business;
    }

    protected void populateViewHolder(Object obj)
    {
        obj = (FavoriteCellViewHolder)obj;
        if(data.address != null)
            ((FavoriteCellViewHolder) (obj)).addressLabel.setText(data.address);
        if(data.saveName != null)
            ((FavoriteCellViewHolder) (obj)).titleLabel.setText(data.saveName);
        if(imagePlace != null)
            ((FavoriteCellViewHolder) (obj)).iconButton.setImageBitmap(imagePlace);
        else
            ((FavoriteCellViewHolder) (obj)).iconButton.setImageResource(R.drawable.business_no_photo_square);
        ((FavoriteCellViewHolder) (obj)).callButton.setVisibility(View.INVISIBLE);
    }

    FavoriteListServiceOutput data;
    public Bitmap imagePlace;
}
