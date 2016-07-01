

package streetdirectory.mobile.modules.direction;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.core.ui.SanListViewAdapter;
import streetdirectory.mobile.core.ui.SanListViewItem;

// Referenced classes of package streetdirectory.mobile.modules.direction:
//            JourneyAlternateRoute

public class DirectionAlternateRouteCell extends SanListViewItem
{
    public static class DirectionAlternateRouteCellViewHolder
    {

        public TextView routeDetail;
        public TextView routeTitle;
        public ImageView tickIcon;

        public DirectionAlternateRouteCellViewHolder()
        {
        }
    }


    public DirectionAlternateRouteCell(JourneyAlternateRoute journeyalternateroute)
    {
        data = journeyalternateroute;
    }

    protected Object createViewHolder(View view)
    {
        DirectionAlternateRouteCellViewHolder directionalternateroutecellviewholder = new DirectionAlternateRouteCellViewHolder();
        directionalternateroutecellviewholder.routeTitle = (TextView)view.findViewById(R.id.text_view_route_label);
        directionalternateroutecellviewholder.routeDetail = (TextView)view.findViewById(R.id.text_view_route_detail);
        directionalternateroutecellviewholder.tickIcon = (ImageView)view.findViewById(R.id.image_view_tick);
        return directionalternateroutecellviewholder;
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
        obj = (DirectionAlternateRouteCellViewHolder)obj;
        ((DirectionAlternateRouteCellViewHolder) (obj)).routeTitle.setText(data.title);
        ((DirectionAlternateRouteCellViewHolder) (obj)).routeDetail.setText((new StringBuilder()).append("ERP ").append(data.erp).append(", ").append(data.totalTime).append("min over ").append(data.totalDistance).append(" km").toString());
        if(routeChecked)
        {
            ((DirectionAlternateRouteCellViewHolder) (obj)).tickIcon.setVisibility(View.VISIBLE);
            return;
        } else
        {
            ((DirectionAlternateRouteCellViewHolder) (obj)).tickIcon.setVisibility(4);
            return;
        }
    }

    JourneyAlternateRoute data;
    public boolean routeChecked;
}
