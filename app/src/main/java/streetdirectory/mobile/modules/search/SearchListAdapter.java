// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.search;

import android.content.Context;
import android.graphics.Color;
import android.view.*;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xinghai.mycurve.R;

import java.util.ArrayList;
import streetdirectory.mobile.modules.search.service.SearchServiceOutput;

// Referenced classes of package streetdirectory.mobile.modules.search:
//            SearchCellViewHolder

public class SearchListAdapter extends BaseAdapter
{

    public SearchListAdapter(Context context)
    {
        _data = new ArrayList();
        _context = context;
    }

    public void clear()
    {
        _data = new ArrayList();
        notifyDataSetInvalidated();
    }

    public int getCount()
    {
        return _data.size();
    }


    public SearchServiceOutput getItem(int i)
    {
        return (SearchServiceOutput)_data.get(i);
    }

    public long getItemId(int i)
    {
        return (long)i;
    }

    public View getView(int i, View view, ViewGroup viewgroup)
    {
        String s;
        SearchServiceOutput searchserviceoutput;
        TextView textview;
        SearchCellViewHolder temp;
        if(view == null)
        {
            view = ((LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.cell_search, viewgroup, false);
            temp  = new SearchCellViewHolder();
            temp.titleLabel = (TextView)view.findViewById(R.id.TitleLabel);
            temp.detailLabel = (TextView)view.findViewById(R.id.DetailLabel);
            temp.categoryLabel = (TextView)view.findViewById(R.id.CategoryLabel);
            view.setTag(viewgroup);
        } else
        {
            temp = (SearchCellViewHolder)view.getTag();
        }
        searchserviceoutput = getItem(i);
        ((SearchCellViewHolder) (temp)).titleLabel.setText(searchserviceoutput.venue);
        textview = ((SearchCellViewHolder) (temp)).detailLabel;
        if(searchserviceoutput.address != null)
            s = searchserviceoutput.address;
        else
            s = "";
        textview.setText(s);
        if(searchserviceoutput.type == 1)
        {
            ((SearchCellViewHolder) (temp)).categoryLabel.setTextColor(Color.parseColor("#1FFF1F"));
            ((SearchCellViewHolder) (temp)).categoryLabel.setText("Location");
            return view;
        }
        if(searchserviceoutput.type == 2)
        {
            ((SearchCellViewHolder) (temp)).categoryLabel.setTextColor(Color.parseColor("#FF8F1E"));
            ((SearchCellViewHolder) (temp)).categoryLabel.setText("Company");
            return view;
        } else
        {
            ((SearchCellViewHolder) (temp)).categoryLabel.setTextColor(Color.parseColor("#2C2CFB"));
            ((SearchCellViewHolder) (temp)).categoryLabel.setText("Directory");
            ((SearchCellViewHolder) (temp)).detailLabel.setText("See all listing for directory");
            return view;
        }
    }

    public void setData(ArrayList arraylist)
    {
        _data = arraylist;
        notifyDataSetInvalidated();
    }

    protected Context _context;
    protected ArrayList _data;
}
