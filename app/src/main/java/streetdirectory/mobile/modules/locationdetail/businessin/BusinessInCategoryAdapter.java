// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.locationdetail.businessin;

import android.content.Context;
import android.content.res.Resources;
import android.view.*;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.util.ArrayList;
import streetdirectory.mobile.modules.locationdetail.businessin.service.BusinessInCategoryServiceOutput;

public class BusinessInCategoryAdapter extends BaseAdapter
{
    public static class BusinessInCategoryCellViewHolder
    {

        public ImageView imageViewCategory;
        public TextView textViewTitle;

        public BusinessInCategoryCellViewHolder()
        {
        }
    }


    public BusinessInCategoryAdapter(Context context)
    {
        initialize(context);
    }

    private void add(BusinessInCategoryServiceOutput businessincategoryserviceoutput)
    {
        mData.add(businessincategoryserviceoutput);
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


    public BusinessInCategoryServiceOutput getItem(int i)
    {
        return (BusinessInCategoryServiceOutput)mData.get(i);
    }

    public long getItemId(int i)
    {
        return (long)i;
    }

    public View getView(int i, View view, ViewGroup viewgroup)
    {
        BusinessInCategoryServiceOutput businessincategoryserviceoutput;
        BusinessInCategoryCellViewHolder temp;
        if(view == null)
        {
            view = ((LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.cell_business_in_category, viewgroup, false);
            temp = new BusinessInCategoryCellViewHolder();
            temp.imageViewCategory = (ImageView)view.findViewById(R.id.imageViewCategory);
            temp.textViewTitle = (TextView)view.findViewById(R.id.textViewTitle);
            view.setTag(viewgroup);
        } else
        {
            temp = (BusinessInCategoryCellViewHolder)view.getTag();
        }
        businessincategoryserviceoutput = getItem(i);
        ((BusinessInCategoryCellViewHolder) (temp)).imageViewCategory.setImageResource(view.getResources().getIdentifier((new StringBuilder()).append("biz_in_cat_").append(businessincategoryserviceoutput.categoryID).toString(), "drawable", view.getContext().getPackageName()));
        ((BusinessInCategoryCellViewHolder) (temp)).textViewTitle.setText(businessincategoryserviceoutput.categoryName);
        return view;
    }

    private Context mContext;
    public ArrayList mData;
}
