

package streetdirectory.mobile.modules.locationdetail.bus;

import android.content.Context;
import android.content.res.Resources;
import android.view.*;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xinghai.mycurve.R;

import java.util.ArrayList;
import streetdirectory.mobile.modules.locationdetail.bus.service.BusArrivalServiceOutputV2;

// Referenced classes of package streetdirectory.mobile.modules.locationdetail.bus:
//            BusArrivalCellViewHolder

public class BusArrivalAdapterV2 extends BaseAdapter
{

    public BusArrivalAdapterV2(Context context)
    {
        items = new ArrayList();
        initialize(context);
    }

    private void add(BusArrivalServiceOutputV2 busarrivalserviceoutputv2)
    {
        mData.add(busarrivalserviceoutputv2);
    }

    private void initialize(Context context)
    {
        mContext = context;
        mData = new ArrayList();
    }

    private void removeData()
    {
        mData.clear();
    }

    public int getCount()
    {
        return items.size();
    }

    public Object getItem(int i)
    {
        return items.get(i);
    }

    public long getItemId(int i)
    {
        return (long)i;
    }

    public int getItemViewType(int i)
    {
        getItem(i);
        return 1;
    }

    public View getView(int i, View view, ViewGroup viewgroup)
    {
        if(i >= items.size())
            return view;
        Object obj1 = getItem(i);
        Object obj = view;
        BusArrivalCellViewHolder temp;
        if(obj1 instanceof BusArrivalServiceOutputV2)
        {
            if(view == null || !(view.getTag() instanceof BusArrivalCellViewHolder))
            {
                view = ((LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.cell_bus_arrival, viewgroup, false);
                temp = new BusArrivalCellViewHolder();
                temp.labelBusNumber = (TextView)view.findViewById(R.id.text_view_bus_number);
                temp.labelNextBus = (TextView)view.findViewById(R.id.text_view_next_bus);
                temp.labelSubSequentBus = (TextView)view.findViewById(R.id.text_view_subsequent_bus);
                view.setTag(viewgroup);
                ((BusArrivalCellViewHolder) (temp)).labelNextBus.setText("Loading ...");
                ((BusArrivalCellViewHolder) (temp)).labelSubSequentBus.setText("Loading...");
            } else
            {
                temp = (BusArrivalCellViewHolder)view.getTag();
            }
            obj = (BusArrivalServiceOutputV2)obj1;
            if(((BusArrivalServiceOutputV2) (obj)).subsequentBus == null)
            {
                ((BusArrivalCellViewHolder) (temp)).labelSubSequentBus.setText("Subsequent Bus: Loading...");
                ((BusArrivalCellViewHolder) (temp)).labelSubSequentBus.setTextColor(mContext.getResources().getColor(R.color.gray));
            } else
            if(((BusArrivalServiceOutputV2) (obj)).subsequentBus.length() > 0)
            {
                ((BusArrivalCellViewHolder) (temp)).labelSubSequentBus.setText((new StringBuilder()).append("Subsequent Bus: ").append(((BusArrivalServiceOutputV2) (obj)).subsequentBus).toString());
                if(((BusArrivalServiceOutputV2) (obj)).subsequentBus.contains("Not") || ((BusArrivalServiceOutputV2) (obj)).subsequentBus.contains("N/A"))
                    ((BusArrivalCellViewHolder) (temp)).labelSubSequentBus.setTextColor(mContext.getResources().getColor(R.color.gray_disabled));
                else
                    ((BusArrivalCellViewHolder) (temp)).labelSubSequentBus.setTextColor(mContext.getResources().getColor(R.color.gray));
            } else
            {
                ((BusArrivalCellViewHolder) (temp)).labelSubSequentBus.setText("Subsequent Bus: N/A");
                ((BusArrivalCellViewHolder) (temp)).labelNextBus.setTextColor(mContext.getResources().getColor(R.color.gray_disabled));
            }
            if(((BusArrivalServiceOutputV2) (obj)).nextBus == null)
            {
                ((BusArrivalCellViewHolder) (temp)).labelNextBus.setText("Next Bus: Loading...");
                ((BusArrivalCellViewHolder) (temp)).labelNextBus.setTextColor(mContext.getResources().getColor(0x106000c));
            } else
            if(((BusArrivalServiceOutputV2) (obj)).nextBus.length() > 0)
            {
                ((BusArrivalCellViewHolder) (temp)).labelNextBus.setText((new StringBuilder()).append("Next Bus: ").append(((BusArrivalServiceOutputV2) (obj)).nextBus).toString());
                if(((BusArrivalServiceOutputV2) (obj)).nextBus.contains("Not") || ((BusArrivalServiceOutputV2) (obj)).nextBus.contains("N/A"))
                    ((BusArrivalCellViewHolder) (temp)).labelNextBus.setTextColor(mContext.getResources().getColor(R.color.gray_disabled));
                else
                    ((BusArrivalCellViewHolder) (temp)).labelNextBus.setTextColor(mContext.getResources().getColor(0x106000c));
            } else
            {
                ((BusArrivalCellViewHolder) (temp)).labelNextBus.setText("Next Bus: N/A");
                ((BusArrivalCellViewHolder) (temp)).labelNextBus.setTextColor(mContext.getResources().getColor(R.color.gray_disabled));
            }
            ((BusArrivalCellViewHolder) (temp)).labelBusNumber.setText(((BusArrivalServiceOutputV2) (obj)).busNumber);
            obj = view;
        }
        return ((View) (obj));
    }

    private static final int LIST_ITEM_AD = 2;
    private static final int LIST_ITEM_BUS = 1;
    public ArrayList items;
    protected Context mContext;
    public ArrayList mData;
}
