

package streetdirectory.mobile.modules.direction;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.core.ui.SanListViewAdapter;
import streetdirectory.mobile.core.ui.SanListViewItem;

// Referenced classes of package streetdirectory.mobile.modules.direction:
//            JourneyPointDetail

public class DirectionStartCell extends SanListViewItem
{
    public static class DirectionStartCellViewHolder
    {

        public TextView addressDetail;
        public TextView addressName;

        public DirectionStartCellViewHolder()
        {
        }
    }


    public DirectionStartCell(JourneyPointDetail journeypointdetail)
    {
        data = journeypointdetail;
    }

    protected Object createViewHolder(View view)
    {
        DirectionStartCellViewHolder directionstartcellviewholder = new DirectionStartCellViewHolder();
        directionstartcellviewholder.addressName = (TextView)view.findViewById(R.id.text_view_address_name);
        directionstartcellviewholder.addressDetail = (TextView)view.findViewById(R.id.text_view_address);
        return directionstartcellviewholder;
    }

    public void execute(Context context, SanListViewAdapter sanlistviewadapter)
    {
    }

    protected int getLayoutId()
    {
        return R.layout.cell_direction_start;
    }

    protected void populateViewHolder(Object obj)
    {
        obj = (DirectionStartCellViewHolder)obj;
        ((DirectionStartCellViewHolder) (obj)).addressName.setText(data.title);
        ((DirectionStartCellViewHolder) (obj)).addressDetail.setText(data.desc);
    }

    JourneyPointDetail data;
}
