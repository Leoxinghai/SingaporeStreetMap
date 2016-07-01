// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.locationdetail.erp;

import android.content.Context;
import android.view.*;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xinghai.mycurve.R;

import java.util.ArrayList;
import streetdirectory.mobile.modules.locationdetail.erp.service.ERPDetailServiceOutput;
import streetdirectory.mobile.modules.locationdetail.erp.service.ERPTimeRateInfo;
import streetdirectory.mobile.modules.locationdetail.erp.service.ERPVehicleInfo;

// Referenced classes of package streetdirectory.mobile.modules.locationdetail.erp:
//            ErpDetailCellViewHolder

public class ErpDetailAdapter extends BaseAdapter
{

    public ErpDetailAdapter(Context context)
    {
        initialize(context);
    }

    private void initialize(Context context)
    {
        mContext = context;
        mData = new ArrayList();
    }

    public void add(ERPDetailServiceOutput erpdetailserviceoutput)
    {
        mData.add(erpdetailserviceoutput);
    }

    public int getCount()
    {
        boolean flag = false;
        int i = ((flag) ? 1 : 0);
        if(mData.size() > 0)
        {
            Object obj = getItem(0);
            i = ((flag) ? 1 : 0);
            if(((ERPDetailServiceOutput) (obj)).arrayOfVehicleInfo.size() > mVehicleIndex)
            {
                obj = (ERPVehicleInfo)((ERPDetailServiceOutput) (obj)).arrayOfVehicleInfo.get(mVehicleIndex);
                if(mDay == 0 && ((ERPVehicleInfo) (obj)).arrayOfWeekdaysInfo != null)
                {
                    i = ((ERPVehicleInfo) (obj)).arrayOfWeekdaysInfo.size();
                } else
                {
                    i = ((flag) ? 1 : 0);
                    if(mDay == 1)
                    {
                        i = ((flag) ? 1 : 0);
                        if(((ERPVehicleInfo) (obj)).arrayOfWeekendInfo != null)
                            return ((ERPVehicleInfo) (obj)).arrayOfWeekendInfo.size();
                    }
                }
            }
        }
        return i;
    }


    public ERPDetailServiceOutput getItem(int i)
    {
        return (ERPDetailServiceOutput)mData.get(i);
    }

    public long getItemId(int i)
    {
        return (long)i;
    }

    public View getView(int i, View view, ViewGroup viewgroup)
    {
        Object obj;
        ErpDetailCellViewHolder temp;
        if(view == null)
        {
            view = ((LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.cell_erp_info, viewgroup, false);
            temp = new ErpDetailCellViewHolder();
            temp.labelTime = (TextView)view.findViewById(R.id.text_view_time);
            temp.labelPrice = (TextView)view.findViewById(R.id.text_view_price);
            view.setTag(viewgroup);
        } else
        {
            temp = (ErpDetailCellViewHolder)view.getTag();
        }
        obj = getItem(0);
        if(((ERPDetailServiceOutput) (obj)).arrayOfVehicleInfo.size() > mVehicleIndex)
        {
            obj = (ERPVehicleInfo)((ERPDetailServiceOutput) (obj)).arrayOfVehicleInfo.get(mVehicleIndex);
            if(mDay == 0 && ((ERPVehicleInfo) (obj)).arrayOfWeekdaysInfo.size() > i)
            {
                obj = (ERPTimeRateInfo)((ERPVehicleInfo) (obj)).arrayOfWeekdaysInfo.get(i);
                ((ErpDetailCellViewHolder) (temp)).labelTime.setText((new StringBuilder()).append(((ERPTimeRateInfo) (obj)).startTime).append(" - ").append(((ERPTimeRateInfo) (obj)).endTime).toString());
                ((ErpDetailCellViewHolder) (temp)).labelPrice.setText(((ERPTimeRateInfo) (obj)).amount);
            } else
            if(mDay == 1 && ((ERPVehicleInfo) (obj)).arrayOfWeekendInfo.size() > i)
            {
                obj = (ERPTimeRateInfo)((ERPVehicleInfo) (obj)).arrayOfWeekendInfo.get(i);
                ((ErpDetailCellViewHolder) (temp)).labelTime.setText((new StringBuilder()).append(((ERPTimeRateInfo) (obj)).startTime).append(" - ").append(((ERPTimeRateInfo) (obj)).endTime).toString());
                ((ErpDetailCellViewHolder) (temp)).labelPrice.setText(((ERPTimeRateInfo) (obj)).amount);
                return view;
            }
        }
        return view;
    }

    public int getViewTypeCount()
    {
        return 1;
    }

    public void removeAllData()
    {
        mData.clear();
    }

    public static final int DAY_SATURDAY = 1;
    public static final int DAY_WEEKDAYS = 0;
    private Context mContext;
    private ArrayList mData;
    public int mDay;
    public int mVehicleIndex;
    public int total;
}
