

package streetdirectory.mobile.modules.direction;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.core.ui.SanListViewAdapter;
import streetdirectory.mobile.core.ui.SanListViewItem;

// Referenced classes of package streetdirectory.mobile.modules.direction:
//            Carpark

public class DirectionCarparkCell extends SanListViewItem
{
    public static class DirectionCarparkCellViewHolder
    {

        public TextView carparkCode;
        public TextView carparkTitle;

        public DirectionCarparkCellViewHolder()
        {
        }
    }


    public DirectionCarparkCell(String s, String s1, Carpark carpark)
    {
        carparkCode = s1;
        carparkTitle = s;
        data = carpark;
    }

    protected Object createViewHolder(View view)
    {
        DirectionCarparkCellViewHolder directioncarparkcellviewholder = new DirectionCarparkCellViewHolder();
        directioncarparkcellviewholder.carparkTitle = (TextView)view.findViewById(R.id.textView_title);
        directioncarparkcellviewholder.carparkCode = (TextView)view.findViewById(R.id.text_view_carpark_code);
        return directioncarparkcellviewholder;
    }

    public void execute(Context context, SanListViewAdapter sanlistviewadapter)
    {
    }

    protected int getLayoutId()
    {
        return R.layout.cell_direction_carpark;
    }

    protected void populateViewHolder(Object obj)
    {
        obj = (DirectionCarparkCellViewHolder)obj;
        if(carparkTitle != null)
            ((DirectionCarparkCellViewHolder) (obj)).carparkTitle.setText(carparkTitle);
        ((DirectionCarparkCellViewHolder) (obj)).carparkCode.setText(carparkCode);
    }

    String carparkCode;
    String carparkTitle;
    public Carpark data;
}
