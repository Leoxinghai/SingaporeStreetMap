

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
import streetdirectory.mobile.modules.favorite.service.FavoriteNearbyListServiceOutput;
import streetdirectory.mobile.modules.locationdetail.bus.BusArrivalActivity;
import streetdirectory.mobile.modules.locationdetail.businessin.BusinessInActivity;
import streetdirectory.mobile.modules.locationdetail.erp.ErpDetailActivity;
import streetdirectory.mobile.modules.locationdetail.expresswayexit.ExpressWayExitActivity;
import streetdirectory.mobile.modules.locationdetail.trafficcam.TrafficCameraLocationDetailActivity;

public class FavoriteNearbyListCell extends SanListViewItem
{
    public static class FavoriteNearbyCellViewHolder
    {

        public TextView addressLabel;
        public ImageButton callButton;
        public ImageView iconButton;
        public TextView titleLabel;

        public FavoriteNearbyCellViewHolder()
        {
        }
    }


    public FavoriteNearbyListCell(FavoriteNearbyListServiceOutput favoritenearbylistserviceoutput)
    {
        data = favoritenearbylistserviceoutput;
    }

    protected Object createViewHolder(View view)
    {
        FavoriteNearbyCellViewHolder favoritenearbycellviewholder = new FavoriteNearbyCellViewHolder();
        favoritenearbycellviewholder.titleLabel = (TextView)view.findViewById(R.id.TitleLabel);
        favoritenearbycellviewholder.addressLabel = (TextView)view.findViewById(R.id.AboutUsLabel);
        favoritenearbycellviewholder.iconButton = (ImageView)view.findViewById(R.id.IconButton);
        favoritenearbycellviewholder.callButton = (ImageButton)view.findViewById(R.id.imageButtonCall);
        return favoritenearbycellviewholder;
    }

    public void execute(Context context, SanListViewAdapter sanlistviewadapter)
    {
        Class temp;
        if(data.type == 2)
            temp =BusinessDetailActivity.class;
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
        obj = (FavoriteNearbyCellViewHolder)obj;
        if(data.address != null)
            ((FavoriteNearbyCellViewHolder) (obj)).addressLabel.setText(data.address);
        if(data.venue != null)
        {
            double d = Math.round(data.distance);
            if(d >= 1000D)
            {
                String s = String.format("%.2f", new Object[] {
                    Double.valueOf(d * 0.001D)
                });
                ((FavoriteNearbyCellViewHolder) (obj)).titleLabel.setText((new StringBuilder()).append(s).append(" Km:  ").append(data.venue).toString());
            } else
            {
                ((FavoriteNearbyCellViewHolder) (obj)).titleLabel.setText((new StringBuilder()).append(d).append(" m:  ").append(data.venue).toString());
            }
        }
        if(businessImage != null)
            ((FavoriteNearbyCellViewHolder) (obj)).iconButton.setImageBitmap(businessImage);
        else
            ((FavoriteNearbyCellViewHolder) (obj)).iconButton.setImageResource(R.drawable.business_no_photo);
        ((FavoriteNearbyCellViewHolder) (obj)).callButton.setVisibility(View.INVISIBLE);
    }

    public Bitmap businessImage;
    FavoriteNearbyListServiceOutput data;
    public Bitmap siteBanner;
}
