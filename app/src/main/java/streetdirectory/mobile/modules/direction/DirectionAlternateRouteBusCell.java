

package streetdirectory.mobile.modules.direction;

import android.content.Context;
import android.view.View;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.util.ArrayList;
import streetdirectory.mobile.core.ui.SanListViewAdapter;
import streetdirectory.mobile.core.ui.SanListViewItem;

public class DirectionAlternateRouteBusCell extends SanListViewItem
{
    public static class DirectionAlternateRouteCellViewHolder
    {

        public LinearLayout linearLayoutTop;
        public TextView routeDetail;
        public ImageView tickIcon;

        public DirectionAlternateRouteCellViewHolder()
        {
        }
    }


    public DirectionAlternateRouteBusCell(String s, ArrayList arraylist)
    {
        desc = s;
        linearChilds.addAll(arraylist);
    }

    protected Object createViewHolder(View view)
    {
        DirectionAlternateRouteCellViewHolder directionalternateroutecellviewholder = new DirectionAlternateRouteCellViewHolder();
        directionalternateroutecellviewholder.linearLayoutTop = (LinearLayout)view.findViewById(R.id.linear_layout_route_label);
        directionalternateroutecellviewholder.routeDetail = (TextView)view.findViewById(R.id.text_view_route_detail);
        directionalternateroutecellviewholder.tickIcon = (ImageView)view.findViewById(R.id.image_view_tick);
        return directionalternateroutecellviewholder;
    }

    public void execute(Context context, SanListViewAdapter sanlistviewadapter)
    {
    }

    protected int getLayoutId()
    {
        return R.layout.cell_direction_alternate_bus_train;
    }

    protected void populateViewHolder(Object obj)
    {
        obj = (DirectionAlternateRouteCellViewHolder)obj;
        ((DirectionAlternateRouteCellViewHolder) (obj)).linearLayoutTop.removeAllViews();
        for(int i = 0; i < linearChilds.size(); i++)
        {
            View view = (View)linearChilds.get(i);
            LinearLayout linearlayout = (LinearLayout)view.getParent();
            if(linearlayout != null)
                linearlayout.removeView(view);
            ((DirectionAlternateRouteCellViewHolder) (obj)).linearLayoutTop.addView(view);
        }

        ((DirectionAlternateRouteCellViewHolder) (obj)).routeDetail.setText(desc);
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

    String desc;
    public final ArrayList linearChilds = new ArrayList();
    public boolean routeChecked;
}
