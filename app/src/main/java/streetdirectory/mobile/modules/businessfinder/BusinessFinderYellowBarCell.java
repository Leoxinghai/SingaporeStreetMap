

package streetdirectory.mobile.modules.businessfinder;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.*;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.core.ui.SanListViewAdapter;
import streetdirectory.mobile.core.ui.SanListViewItem;
import streetdirectory.mobile.modules.businessfinder.service.BusinessFinderYellowBarServiceOutput;

public class BusinessFinderYellowBarCell extends SanListViewItem
{
    public static class BusinessFinderYellowBarCellViewHolder
    {

        RelativeLayout cellLayout;
        ImageView icon;
        TextView title;

        public BusinessFinderYellowBarCellViewHolder()
        {
        }
    }


    public BusinessFinderYellowBarCell(BusinessFinderYellowBarServiceOutput businessfinderyellowbarserviceoutput)
    {
        cellSelected = false;
        data = businessfinderyellowbarserviceoutput;
    }

    protected Object createViewHolder(View view)
    {
        BusinessFinderYellowBarCellViewHolder businessfinderyellowbarcellviewholder = new BusinessFinderYellowBarCellViewHolder();
        businessfinderyellowbarcellviewholder.cellLayout = (RelativeLayout)view.findViewById(R.id.cell_business_finder);
        businessfinderyellowbarcellviewholder.icon = (ImageView)view.findViewById(R.id.imageView1);
        businessfinderyellowbarcellviewholder.title = (TextView)view.findViewById(R.id.textView1);
        return businessfinderyellowbarcellviewholder;
    }

    public void execute(Context context, SanListViewAdapter sanlistviewadapter)
    {
    }

    protected int getLayoutId()
    {
        return R.layout.cell_business_finder_category;
    }

    protected void populateViewHolder(Object obj)
    {
        obj = (BusinessFinderYellowBarCellViewHolder)obj;
        if(cellSelected)
            ((BusinessFinderYellowBarCellViewHolder) (obj)).cellLayout.setBackgroundResource(R.drawable.selector_color_cell_business_finder_category_selected);
        else
            ((BusinessFinderYellowBarCellViewHolder) (obj)).cellLayout.setBackgroundResource(R.drawable.selector_color_cell_business_finder_category);
        if(data != null)
            ((BusinessFinderYellowBarCellViewHolder) (obj)).title.setText(data.name);
        if(bmp != null)
            ((BusinessFinderYellowBarCellViewHolder) (obj)).icon.setImageBitmap(bmp);
    }

    public Bitmap bmp;
    public boolean cellSelected;
    public BusinessFinderYellowBarServiceOutput data;
}
