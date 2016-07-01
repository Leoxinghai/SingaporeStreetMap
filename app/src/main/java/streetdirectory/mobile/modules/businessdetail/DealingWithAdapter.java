// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.businessdetail;

import android.content.Context;
import android.view.*;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.xinghai.mycurve.R;

import java.util.ArrayList;
import java.util.Collection;
import streetdirectory.mobile.modules.businessdetail.service.DealingWithServiceOutput;

public class DealingWithAdapter extends BaseAdapter
{
    private static class DealingWithViewHolder
    {

        public Button titleButton;

        private DealingWithViewHolder()
        {
        }

    }

    public static interface OnDealingWithClickedListener
    {

        public abstract void onClicked(DealingWithServiceOutput dealingwithserviceoutput, int i);
    }


    public DealingWithAdapter(Context context)
    {
        mData = new ArrayList();
        mContext = context;
    }

    public void add(Collection collection)
    {
        mData.addAll(collection);
    }

    public void add(DealingWithServiceOutput dealingwithserviceoutput)
    {
        mData.add(dealingwithserviceoutput);
    }

    public void clear()
    {
        mData.clear();
    }

    public int getCount()
    {
        return mData.size();
    }

    public DealingWithServiceOutput getItem(int i)
    {
        return (DealingWithServiceOutput)mData.get(i);
    }

    public long getItemId(int i)
    {
        return (long)i;
    }

    class SUBCLASS1 implements android.view.View.OnClickListener {

        public void onClick(View view1)
        {
            if(mOnDealingWithClickedListener != null)
                mOnDealingWithClickedListener.onClicked(item, position);
        }

        DealingWithServiceOutput item;
        int position;

        SUBCLASS1(DealingWithServiceOutput dealingwithserviceoutput, int i)
        {
            item = dealingwithserviceoutput;
            position = i;
        }
    }

    public View getView(final int position, View view, ViewGroup viewgroup)
    {
        final DealingWithServiceOutput item = getItem(position);
        DealingWithViewHolder temp;
        if(view == null)
        {
            view = ((LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.grid_business_dealing_with, viewgroup, false);
             temp = new DealingWithViewHolder();
            temp.titleButton = (Button)view.findViewById(R.id.button_title);
            ((DealingWithViewHolder) (temp)).titleButton.setOnClickListener(new SUBCLASS1(item,position));
            view.setTag(viewgroup);
        } else
        {
            temp = (DealingWithViewHolder)view.getTag();
        }
        ((DealingWithViewHolder) (temp)).titleButton.setText(item.name);
        return view;
    }

    public void setOnDealingWithClickedListener(OnDealingWithClickedListener ondealingwithclickedlistener)
    {
        mOnDealingWithClickedListener = ondealingwithclickedlistener;
    }

    private Context mContext;
    private ArrayList mData;
    private OnDealingWithClickedListener mOnDealingWithClickedListener;

}
