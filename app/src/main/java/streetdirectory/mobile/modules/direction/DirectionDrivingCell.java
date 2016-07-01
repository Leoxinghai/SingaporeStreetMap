

package streetdirectory.mobile.modules.direction;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.core.ui.SanListViewAdapter;
import streetdirectory.mobile.core.ui.SanListViewItem;

// Referenced classes of package streetdirectory.mobile.modules.direction:
//            JourneyPointDetailCar

public class DirectionDrivingCell extends SanListViewItem
{
    public static class DirectionDrivingCellViewHolder
    {

        public TextView desc;
        public TextView distance;
        public ImageView imageViewDirection;
        public TextView number;
        public TextView titleLabel;

        public DirectionDrivingCellViewHolder()
        {
        }
    }


    public DirectionDrivingCell(JourneyPointDetailCar journeypointdetailcar, Bitmap bitmap)
    {
        data = journeypointdetailcar;
        directionBmp = bitmap;
    }

    protected Object createViewHolder(View view)
    {
        DirectionDrivingCellViewHolder directiondrivingcellviewholder = new DirectionDrivingCellViewHolder();
        directiondrivingcellviewholder.titleLabel = (TextView)view.findViewById(R.id.text_view_address_name);
        directiondrivingcellviewholder.desc = (TextView)view.findViewById(R.id.text_view_address);
        directiondrivingcellviewholder.distance = (TextView)view.findViewById(R.id.text_view_direction_distance);
        directiondrivingcellviewholder.number = (TextView)view.findViewById(R.id.text_view_direction_number);
        directiondrivingcellviewholder.imageViewDirection = (ImageView)view.findViewById(R.id.image_view_direction_icon);
        return directiondrivingcellviewholder;
    }

    public void execute(Context context, SanListViewAdapter sanlistviewadapter)
    {
    }

    protected int getLayoutId()
    {
        return R.layout.cell_direction_driving;
    }

    protected void populateViewHolder(Object obj)
    {
        obj = (DirectionDrivingCellViewHolder)obj;
        ((DirectionDrivingCellViewHolder) (obj)).titleLabel.setText(data.title);
        ((DirectionDrivingCellViewHolder) (obj)).desc.setText(data.desc);
        ((DirectionDrivingCellViewHolder) (obj)).distance.setText(data.distanceInUnit);
        ((DirectionDrivingCellViewHolder) (obj)).number.setText(String.valueOf(data.index));
        if(directionBmp != null)
            ((DirectionDrivingCellViewHolder) (obj)).imageViewDirection.setImageBitmap(directionBmp);
    }

    JourneyPointDetailCar data;
    Bitmap directionBmp;
}
