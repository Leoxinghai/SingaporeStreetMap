// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.locationdetail.bus;

import android.content.Context;
import android.view.*;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xinghai.mycurve.R;

import java.util.ArrayList;
import streetdirectory.mobile.modules.locationdetail.bus.service.BusArrivalServiceOutput;

// Referenced classes of package streetdirectory.mobile.modules.locationdetail.bus:
//            BusArrivalCellViewHolder

public class BusArrivalAdapter extends BaseAdapter
{

    public BusArrivalAdapter(Context context)
    {
        initialize(context);
    }

    private void add(BusArrivalServiceOutput busarrivalserviceoutput)
    {
        mData.add(busarrivalserviceoutput);
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
        return mData.size();
    }

    public BusArrivalServiceOutput getItem(int i)
    {
        return (BusArrivalServiceOutput)mData.get(i);
    }

    public long getItemId(int i)
    {
        return (long)i;
    }

    public View getView(int i, View view, ViewGroup viewgroup)
    {
        BusArrivalServiceOutput busarrivalserviceoutput;
        BusArrivalCellViewHolder temp;
        if(view == null)
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
        busarrivalserviceoutput = getItem(i);
        if(busarrivalserviceoutput.subsequentBus < -1)
            ((BusArrivalCellViewHolder) (temp)).labelSubSequentBus.setText("Subsequent Bus: Loading...");
        else
        if(busarrivalserviceoutput.subsequentBus < 0)
            ((BusArrivalCellViewHolder) (temp)).labelSubSequentBus.setText("Subsequent Bus: N/A");
        else
        if(busarrivalserviceoutput.subsequentBus == 0)
            ((BusArrivalCellViewHolder) (temp)).labelSubSequentBus.setText("Subsequent Bus: Arrived");
        else
            ((BusArrivalCellViewHolder) (temp)).labelSubSequentBus.setText((new StringBuilder()).append("Subsequent Bus: ").append(busarrivalserviceoutput.subsequentBus).append(" min").toString());
        if(busarrivalserviceoutput.nextBus < -1)
            ((BusArrivalCellViewHolder) (temp)).labelNextBus.setText("Next Bus: Loading...");
        else
        if(busarrivalserviceoutput.nextBus < 0)
            ((BusArrivalCellViewHolder) (temp)).labelNextBus.setText("Next Bus: N/A");
        else
        if(busarrivalserviceoutput.nextBus == 0)
            ((BusArrivalCellViewHolder) (temp)).labelNextBus.setText("Next Bus: Arrived");
        else
            ((BusArrivalCellViewHolder) (temp)).labelNextBus.setText((new StringBuilder()).append("Next Bus: ").append(busarrivalserviceoutput.nextBus).append(" min").toString());
        ((BusArrivalCellViewHolder) (temp)).labelBusNumber.setText(busarrivalserviceoutput.busNumber);
        return view;
    }

    private Context mContext;
    public ArrayList mData;
}
