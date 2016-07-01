

package streetdirectory.mobile.modules.direction;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.core.ui.SanListViewAdapter;
import streetdirectory.mobile.core.ui.SanListViewItem;

// Referenced classes of package streetdirectory.mobile.modules.direction:
//            JourneyPointDetail

public class DirectionEndCell extends SanListViewItem
{
    public static class DirectionEndCellViewHolder
    {

        public TextView addressDetail;
        public TextView addressName;

        public DirectionEndCellViewHolder()
        {
        }
    }


    public DirectionEndCell(JourneyPointDetail journeypointdetail)
    {
        data = journeypointdetail;
    }

    protected Object createViewHolder(View view)
    {
        DirectionEndCellViewHolder directionendcellviewholder = new DirectionEndCellViewHolder();
        directionendcellviewholder.addressName = (TextView)view.findViewById(R.id.text_view_address_name);
        directionendcellviewholder.addressDetail = (TextView)view.findViewById(R.id.text_view_address);
        return directionendcellviewholder;
    }

    public void execute(Context context, SanListViewAdapter sanlistviewadapter)
    {
    }

    protected int getLayoutId()
    {
        return R.layout.cell_direction_end;
    }

    protected void populateViewHolder(Object obj)
    {
        obj = (DirectionEndCellViewHolder)obj;
        ((DirectionEndCellViewHolder) (obj)).addressName.setText(data.title);
        ((DirectionEndCellViewHolder) (obj)).addressDetail.setText(data.desc);
    }

    JourneyPointDetail data;
}
