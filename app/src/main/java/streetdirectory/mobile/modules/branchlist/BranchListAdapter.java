// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.branchlist;

import android.content.*;
import android.net.Uri;
import android.util.Log;
import android.view.*;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.text.DecimalFormat;
import java.util.*;
import streetdirectory.mobile.core.SDStory;
import streetdirectory.mobile.modules.businessdetail.service.BusinessBranchServiceOutput;
import streetdirectory.mobile.service.SDHttpServiceOutput;
import streetdirectory.mobile.service.URLFactory;

public class BranchListAdapter extends BaseAdapter
{
    public static class BranchCellViewHolder
    {

        public TextView detail;
        public ImageButton imageButtonCall;
        public ImageView imageViewIndicator;
        public int position;
        public TextView textViewIndicator;
        public TextView title;

        public BranchCellViewHolder()
        {
        }
    }

    public static interface OnBranchItemClickedListener
    {

        public abstract void onAddNewBranchClicked();

        public abstract void onBranchItemClicked(BusinessBranchServiceOutput businessbranchserviceoutput, int i);
    }

    public static interface OnLoadMoreListener
    {

        public abstract void onLoadMoreList();
    }


    public BranchListAdapter(Context context)
    {
        mData = new SDHttpServiceOutput();
        isNearbyData = false;
        mContext = context;
    }

    public void appendData(SDHttpServiceOutput sdhttpserviceoutput)
    {
        mData.total = sdhttpserviceoutput.total;
        mData.childs.addAll(sdhttpserviceoutput.childs);
    }

    public int getCount()
    {
        if(mData.childs.size() > 0)
            return mData.childs.size();
        else
            return 0;
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

    public int getItemViewType(int i)
    {
        int j = mData.childs.size();
        if(i < j)
            return 0;
        return i != j || (long)j < mData.total ? 1 : 2;
    }

    class SUBCLASS1 implements android.view.View.OnClickListener {

        public void onClick(View view2)
        {
            int j;
            if(mBranchItemClickedListener == null)
                return;
            j = holder.position;
            BusinessBranchServiceOutput temp = getItem(j);
            mBranchItemClickedListener.onBranchItemClicked(temp, j);
            return;
        }

        final BranchCellViewHolder holder;

        SUBCLASS1(BranchCellViewHolder branchcellviewholder)
        {
            holder = branchcellviewholder;
        }
    }

    class SUBCLASS2 implements android.view.View.OnClickListener {

        public void onClick(View view2)
        {
            BusinessBranchServiceOutput temp2 = getItem(position);
            if(((BusinessBranchServiceOutput) (temp2)).phoneNumber == null || ((BusinessBranchServiceOutput) (temp2)).phoneNumber.length() <= 0) {
//                Log.e("", "Call failed", temp2);
                return;

            }
            Intent intent = new Intent("android.intent.action.DIAL");
            String s = ((BusinessBranchServiceOutput) (temp2)).phoneNumber.replace(" ", "");
            intent.setData(Uri.parse((new StringBuilder()).append("tel:").append(s).toString()));
            HashMap hashmap = SDStory.createDefaultParams();
            hashmap.put("c_id", Integer.toString(((BusinessBranchServiceOutput) (temp2)).companyID));
            hashmap.put("c_pid", s);
            hashmap.put("opg", "branches");
            SDStory.post(URLFactory.createGantBusinessCall((new StringBuilder()).append(((BusinessBranchServiceOutput) (temp2)).companyID).append("").toString()), hashmap);
            mContext.startActivity(intent);
            return;
        }

        final int position;
        SUBCLASS2(int i)
        {
            position = i;
        }
    }

    public View getView(final int position, View view, final ViewGroup holder)
    {
        int i = getItemViewType(position);

        BranchCellViewHolder temp;
        Object obj;
        if(i != 0) {
            if(i != 1) {
                obj = view;
                if(i == 2)
                {
                    obj = view;
                    if(view == null)
                    {
                        view = ((LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.cell_add_new_branch, holder, false);
                        view.setOnClickListener(new android.view.View.OnClickListener() {

                            public void onClick(View view2)
                            {
                                if(mBranchItemClickedListener != null)
                                    mBranchItemClickedListener.onAddNewBranchClicked();
                            }

                        });
                        return view;
                    }
                }
            } else {
                View view1 = view;
                if (view == null)
                    view1 = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.cell_load_more, holder, false);
                obj = view1;
                if (mLoadMoreListener != null) {
                    mLoadMoreListener.onLoadMoreList();
                    return view1;
                }
            }
            return ((View) (obj));
        }


        if(view == null)
        {
            view = ((LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.cell_branch, holder, false);
            temp = new BranchCellViewHolder();
            temp.title = (TextView)view.findViewById(R.id.textview_title);
            temp.detail = (TextView)view.findViewById(R.id.textview_detail);
            temp.textViewIndicator = (TextView)view.findViewById(R.id.textview_indicator);
            temp.imageViewIndicator = (ImageView)view.findViewById(R.id.imageView_indicator);
            temp.imageButtonCall = (ImageButton)view.findViewById(R.id.imageButtonCall);
            view.setTag(temp);
            view.setOnClickListener(new SUBCLASS1(temp));

            ((BranchCellViewHolder) (temp)).imageButtonCall.setOnClickListener(new SUBCLASS2(i));

        } else
        {
            temp = (BranchCellViewHolder)view.getTag();
        }
        obj = getItem(position);
        if(isNearbyData)
        {
            if(((BusinessBranchServiceOutput) (obj)).distance != null && !((BusinessBranchServiceOutput) (obj)).distance.equalsIgnoreCase("-1") && !((BusinessBranchServiceOutput) (obj)).distance.equalsIgnoreCase("0"))
            {
                float f = Float.parseFloat(((BusinessBranchServiceOutput) (obj)).distance);
                distString = (new StringBuilder()).append((new DecimalFormat("##.##")).format(f)).append("m").toString();
                if((double)f > 999D)
                {
                    f = (float)((double)f / 1000D);
                    distString = (new StringBuilder()).append((new DecimalFormat("##.##")).format(f)).append("km").toString();
                }
                ((BranchCellViewHolder) (temp)).title.setText((new StringBuilder()).append(distString).append(" : ").append(((BusinessBranchServiceOutput) (obj)).branchName).toString());
            }
        } else
        {
            ((BranchCellViewHolder) (temp)).title.setText(((BusinessBranchServiceOutput) (obj)).branchName);
        }
        ((BranchCellViewHolder) (temp)).detail.setText((new StringBuilder()).append(((BusinessBranchServiceOutput) (obj)).unitNo).append(" ").append(((BusinessBranchServiceOutput) (obj)).address).toString());
        if(((BusinessBranchServiceOutput) (obj)).isOfferAvailable)
        {
            ((BranchCellViewHolder) (temp)).imageViewIndicator.setVisibility(View.VISIBLE);
            ((BranchCellViewHolder) (temp)).textViewIndicator.setVisibility(View.VISIBLE);
        } else
        {
            ((BranchCellViewHolder) (temp)).imageViewIndicator.setVisibility(View.INVISIBLE);
            ((BranchCellViewHolder) (temp)).textViewIndicator.setVisibility(View.INVISIBLE);
        }
        if(((BusinessBranchServiceOutput) (obj)).phoneNumber != null && ((BusinessBranchServiceOutput) (obj)).phoneNumber.length() > 0)
            ((BranchCellViewHolder) (temp)).imageButtonCall.setVisibility(View.VISIBLE);
        else
            ((BranchCellViewHolder) (temp)).imageButtonCall.setVisibility(View.INVISIBLE);
        temp.position = position;
        obj = view;
        return ((View) (obj));
    }

    public int getViewTypeCount()
    {
        return 3;
    }

    public void setOnBranchItemClickedListener(OnBranchItemClickedListener onbranchitemclickedlistener)
    {
        mBranchItemClickedListener = onbranchitemclickedlistener;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onloadmorelistener)
    {
        mLoadMoreListener = onloadmorelistener;
    }

    public void sortDataByDistance()
    {
        Collections.sort(mData.childs, new Comparator() {

            public int compare(Object obj, Object obj1)
            {
                return compare((BusinessBranchServiceOutput)obj, (BusinessBranchServiceOutput)obj1);
            }

            public int compare(BusinessBranchServiceOutput businessbranchserviceoutput, BusinessBranchServiceOutput businessbranchserviceoutput1)
            {
                float f = Float.parseFloat(businessbranchserviceoutput.distance);
                float f1 = Float.parseFloat(businessbranchserviceoutput1.distance);
                if(f1 > f)
                    return -1;
                return f1 != f ? 1 : 0;
            }

        });
    }

    public static final int ADD_NEW_BRANCH_CELL = 2;
    public static final int BRANCH_CELL = 0;
    public static final int LOAD_MORE_CELL = 1;
    private String distString;
    public boolean isNearbyData;
    private OnBranchItemClickedListener mBranchItemClickedListener;
    private Context mContext;
    private SDHttpServiceOutput mData;
    private OnLoadMoreListener mLoadMoreListener;


}
