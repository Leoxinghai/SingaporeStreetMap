// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.branchlist;

import android.content.Context;
import android.view.*;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.util.ArrayList;
import streetdirectory.mobile.modules.businessdetail.service.BusinessBranchServiceOutput;
import streetdirectory.mobile.service.SDHttpServiceOutput;

public class BusinessBranchListAdapter extends BaseAdapter
{
    public static class BusinessBranchCellViewHolder
    {

        public TextView detail;
        public ImageView imageViewIndicator;
        public int position;

        public BusinessBranchCellViewHolder()
        {
        }
    }


    public BusinessBranchListAdapter(Context context)
    {
        mData = new SDHttpServiceOutput();
        mContext = context;
    }

    public void appendData(SDHttpServiceOutput sdhttpserviceoutput)
    {
        mData.childs.addAll(sdhttpserviceoutput.childs);
        notifyDataSetChanged();
    }

    public int getCount()
    {
        if(mData.childs.size() > 5)
            return 5;
        else
            return mData.childs.size();
    }

    public int getDataCount()
    {
        return mData.childs.size();
    }


    public BusinessBranchServiceOutput getItem(int i)
    {
        return (BusinessBranchServiceOutput)mData.childs.get(i);
    }

    public long getItemId(int i)
    {
        return (long)i;
    }

    public View getView(int i, View view, ViewGroup viewgroup)
    {
        if(view == null)
        {
            View lview = ((LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.cell_business_branch, viewgroup, false);
            BusinessBranchCellViewHolder businessbranchcellviewholder = new BusinessBranchCellViewHolder();
            businessbranchcellviewholder.position = i;
            businessbranchcellviewholder.detail = (TextView)viewgroup.findViewById(R.id.textView_detail);
            businessbranchcellviewholder.imageViewIndicator = (ImageView)viewgroup.findViewById(R.id.imageView_indicator);
            BusinessBranchServiceOutput businessbranchserviceoutput = getItem(i);
            String temp;
            if(businessbranchserviceoutput.address != null && businessbranchserviceoutput.address.length() > 0)
                temp = businessbranchserviceoutput.address.substring(0, businessbranchserviceoutput.address.length() - 7);
            else
                temp = "";
            businessbranchcellviewholder.detail.setText((new StringBuilder()).append(i + 1).append(". ").append(view).toString());
            if(businessbranchserviceoutput.isOfferAvailable)
                businessbranchcellviewholder.imageViewIndicator.setVisibility(View.VISIBLE);
            viewgroup.setTag(businessbranchcellviewholder);
            return viewgroup;
        } else
        {
            BusinessBranchCellViewHolder businessBranchCellViewHolder = (BusinessBranchCellViewHolder)view.getTag();
            return view;
        }
    }

    private Context mContext;
    private SDHttpServiceOutput mData;
}
