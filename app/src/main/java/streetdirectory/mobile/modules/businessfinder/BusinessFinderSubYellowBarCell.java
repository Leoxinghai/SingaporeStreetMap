

package streetdirectory.mobile.modules.businessfinder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.core.ui.SanListViewAdapter;
import streetdirectory.mobile.core.ui.SanListViewItem;
import streetdirectory.mobile.modules.businessfinder.service.BusinessFinderYellowBarServiceOutput;

public class BusinessFinderSubYellowBarCell extends SanListViewItem
{
    public static class BusinessFinderYellowBarCellViewHolder
    {

        ImageView icon;
        TextView title;

        public BusinessFinderYellowBarCellViewHolder()
        {
        }
    }


    public BusinessFinderSubYellowBarCell(BusinessFinderYellowBarServiceOutput businessfinderyellowbarserviceoutput)
    {
        data = businessfinderyellowbarserviceoutput;
    }

    protected Object createViewHolder(View view)
    {
        BusinessFinderYellowBarCellViewHolder businessfinderyellowbarcellviewholder = new BusinessFinderYellowBarCellViewHolder();
        businessfinderyellowbarcellviewholder.title = (TextView)view.findViewById(R.id.TitleLabel);
        return businessfinderyellowbarcellviewholder;
    }

    public void execute(Context context, SanListViewAdapter sanlistviewadapter)
    {
    }

    protected int getLayoutId()
    {
        return R.layout.cell_business_finder_sub_directory;
    }

    protected void populateViewHolder(Object obj)
    {
        obj = (BusinessFinderYellowBarCellViewHolder)obj;
        if(data != null)
            ((BusinessFinderYellowBarCellViewHolder) (obj)).title.setText(data.name);
    }

    public BusinessFinderYellowBarServiceOutput data;
}
