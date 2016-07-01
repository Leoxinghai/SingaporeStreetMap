

package streetdirectory.mobile.modules.locationdetail.bus;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.core.ui.SanListViewAdapter;
import streetdirectory.mobile.core.ui.SanListViewItem;
import streetdirectory.mobile.modules.locationdetail.bus.service.BusRouteAlternate;

public class BusRouteAlternateCell extends SanListViewItem
{
    public static class BusRouteAlternateCellCellViewHolder
    {

        public TextView routeDetail;
        public TextView routeTitle;
        public ImageView tickIcon;

        public BusRouteAlternateCellCellViewHolder()
        {
        }
    }


    public BusRouteAlternateCell(BusRouteAlternate busroutealternate)
    {
        data = busroutealternate;
    }

    protected Object createViewHolder(View view)
    {
        BusRouteAlternateCellCellViewHolder busroutealternatecellcellviewholder = new BusRouteAlternateCellCellViewHolder();
        busroutealternatecellcellviewholder.routeTitle = (TextView)view.findViewById(R.id.text_view_route_label);
        busroutealternatecellcellviewholder.routeDetail = (TextView)view.findViewById(R.id.text_view_route_detail);
        busroutealternatecellcellviewholder.tickIcon = (ImageView)view.findViewById(R.id.image_view_tick);
        return busroutealternatecellcellviewholder;
    }

    public void execute(Context context, SanListViewAdapter sanlistviewadapter)
    {
    }

    protected int getLayoutId()
    {
        return R.layout.cell_direction_alternate_route;
    }

    protected void populateViewHolder(Object obj)
    {
        obj = (BusRouteAlternateCellCellViewHolder)obj;
        ((BusRouteAlternateCellCellViewHolder) (obj)).routeTitle.setText((new StringBuilder()).append("From : ").append(data.start).toString());
        ((BusRouteAlternateCellCellViewHolder) (obj)).routeDetail.setText((new StringBuilder()).append("To : ").append(data.end).toString());
        if(routeChecked)
        {
            ((BusRouteAlternateCellCellViewHolder) (obj)).tickIcon.setVisibility(View.VISIBLE);
            return;
        } else
        {
            ((BusRouteAlternateCellCellViewHolder) (obj)).tickIcon.setVisibility(4);
            return;
        }
    }

    BusRouteAlternate data;
    public boolean routeChecked;
}
