

package streetdirectory.mobile.modules.businesslisting.service;

import android.content.Context;
import android.view.*;
import android.widget.*;
import java.util.ArrayList;
import streetdirectory.mobile.modules.core.BannerCellViewHolder;
import streetdirectory.mobile.service.SDHttpServiceOutput;
import com.xinghai.mycurve.R;

// Referenced classes of package streetdirectory.mobile.modules.businesslisting.service:
//            OffersListingAdapter

public class OffersListingNearbyAdapter extends OffersListingAdapter
{
    public static interface OnKmRangeClickListener
    {

        public abstract void onKmRangeClicked();
    }


    public OffersListingNearbyAdapter(Context context, int i)
    {
        super(context);
        _kmRange = i;
    }

    public int getCount()
    {
        int i = _data.childs.size();
        if(i > 0)
            return i + 1;
        else
            return 0;
    }

    public int getItemViewType(int i)
    {
        int j = _data.childs.size();
        if(i < j)
            return 0;
        return i != j || (long)j < _data.total ? 1 : 2;
    }

    public View getView(int i, View view, ViewGroup viewgroup)
    {
        view = getView(i, view, viewgroup);
        View view1 = view;
        BannerCellViewHolder temp;
        if(getItemViewType(i) == 2)
        {
            if(view == null)
            {
                view = ((LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.cell_banner, viewgroup, false);
                temp = new BannerCellViewHolder();
                temp.background = (RelativeLayout)view.findViewById(R.id.BackgroundLayout);
                temp.buttonMain = (Button)view.findViewById(R.id.buttonMain);
                temp.imageViewArrow = (ImageView)view.findViewById(R.id.imageViewArrow);
                ((BannerCellViewHolder) (temp)).imageViewArrow.setVisibility(View.INVISIBLE);
                ((BannerCellViewHolder) (temp)).buttonMain.setOnClickListener(new android.view.View.OnClickListener() {

                    public void onClick(View view2)
                    {
                        if(_kmRangeClickListener != null)
                            _kmRangeClickListener.onKmRangeClicked();
                    }

                });

                view.setTag(viewgroup);
            } else
            {
                temp = (BannerCellViewHolder)view.getTag();
            }
            ((BannerCellViewHolder) (temp)).buttonMain.setText((new StringBuilder()).append("Within ").append(_kmRange).append(" Km").toString());
            view1 = view;
        }
        return view1;
    }

    public int getViewTypeCount()
    {
        return 3;
    }

    public void setKmRange(int i)
    {
        _kmRange = i;
    }

    public void setOnKmRangeClickListener(OnKmRangeClickListener onkmrangeclicklistener)
    {
        _kmRangeClickListener = onkmrangeclicklistener;
    }

    public static final int KM_RANGE_CELL = 2;
    protected int _kmRange;
    protected OnKmRangeClickListener _kmRangeClickListener;
}
