// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.businessfindersubdirectory;

import android.content.Context;
import android.view.*;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xinghai.mycurve.R;

import java.util.ArrayList;
import streetdirectory.mobile.modules.businessfindersubdirectory.service.BusinessFinderSubDirectoryServiceOutput;
import streetdirectory.mobile.service.SDHttpServiceOutput;

// Referenced classes of package streetdirectory.mobile.modules.businessfindersubdirectory:
//            BusinessFinderSubDirectoryCellViewHolder

public class BusinessFinderSubDirectoryAdapter extends BaseAdapter
{

    public BusinessFinderSubDirectoryAdapter(Context context)
    {
        _data = new SDHttpServiceOutput();
        _context = context;
    }

    public void clear()
    {
        _data.childs.clear();
        notifyDataSetInvalidated();
    }

    public int getCount()
    {
        return _data.childs.size();
    }

    public BusinessFinderSubDirectoryServiceOutput getItem(int i)
    {
        return (BusinessFinderSubDirectoryServiceOutput)_data.childs.get(i);
    }

    public long getItemId(int i)
    {
        return (long)getItem(i).categoryID;
    }

    public View getView(int i, View view, ViewGroup viewgroup)
    {
        BusinessFinderSubDirectoryServiceOutput businessfindersubdirectoryserviceoutput;
        BusinessFinderSubDirectoryCellViewHolder temp;
        if(view == null)
        {
            view = ((LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.cell_business_finder_sub_directory, viewgroup, false);
            temp = new BusinessFinderSubDirectoryCellViewHolder();
            temp.titleLabel = (TextView)view.findViewById(R.id.TitleLabel);
            view.setTag(temp);
        } else
        {
            temp = (BusinessFinderSubDirectoryCellViewHolder)view.getTag();
        }
        businessfindersubdirectoryserviceoutput = getItem(i);
        ((BusinessFinderSubDirectoryCellViewHolder) (temp)).titleLabel.setText(businessfindersubdirectoryserviceoutput.name);
        return view;
    }

    public void setData(SDHttpServiceOutput sdhttpserviceoutput)
    {
        if(_data != null)
        {
            _data = sdhttpserviceoutput;
            notifyDataSetInvalidated();
        }
    }

    private Context _context;
    private SDHttpServiceOutput _data;
}
