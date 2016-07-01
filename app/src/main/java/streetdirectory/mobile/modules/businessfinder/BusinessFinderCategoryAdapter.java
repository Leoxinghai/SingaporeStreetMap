// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.businessfinder;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.*;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.util.ArrayList;
import streetdirectory.mobile.core.BitmapMemoryCaching;
import streetdirectory.mobile.modules.businessfinder.service.BusinessFinderServiceOutput;

// Referenced classes of package streetdirectory.mobile.modules.businessfinder:
//            BusinessFinderItemGridViewHolder

public class BusinessFinderCategoryAdapter extends BaseAdapter
{

    public BusinessFinderCategoryAdapter(Context context)
    {
        _data = new ArrayList();
        _context = context;
        _images = new BitmapMemoryCaching(context);
    }

    public void addItem(BusinessFinderServiceOutput businessfinderserviceoutput)
    {
        if(businessfinderserviceoutput != null)
            _data.add(businessfinderserviceoutput);
    }

    public void addItems(ArrayList arraylist)
    {
        if(arraylist != null)
            _data.addAll(arraylist);
    }

    public void clear()
    {
        _data.clear();
        _images.evictAll();
        notifyDataSetInvalidated();
    }

    public int getCount()
    {
        return _data.size();
    }


    public BusinessFinderServiceOutput getItem(int i)
    {
        return (BusinessFinderServiceOutput)_data.get(i);
    }

    public long getItemId(int i)
    {
        return (long)i;
    }

    public View getView(int i, View view, ViewGroup viewgroup)
    {
        Object obj;
        BusinessFinderItemGridViewHolder temp;
        if(view == null)
        {
            view = ((LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.cell_business_finder_category, viewgroup, false);
            temp = new BusinessFinderItemGridViewHolder();
            temp.icon = (ImageView)view.findViewById(R.id.imageView1);
            temp.titleLabel = (TextView)view.findViewById(R.id.textView1);
            view.setTag(temp);
        } else
        {
            temp = (BusinessFinderItemGridViewHolder)view.getTag();
        }
        obj = getItem(i);
        ((BusinessFinderItemGridViewHolder) (temp)).icon.setTag(obj);
        ((BusinessFinderItemGridViewHolder) (temp)).titleLabel.setText(((BusinessFinderServiceOutput) (obj)).name);
        i = _context.getResources().getIdentifier((new StringBuilder()).append("selector_").append(((BusinessFinderServiceOutput) (obj)).icon).toString(), "drawable", _context.getPackageName());
        if(i != 0)
        {
            android.graphics.drawable.Drawable drawable = _context.getResources().getDrawable(i);
            if(drawable != null)
            {
                ((BusinessFinderItemGridViewHolder) (temp)).icon.setImageDrawable(drawable);
            } else
            {
                obj = (Bitmap)_images.get(((BusinessFinderServiceOutput) (obj)).icon);
                if(obj != null)
                {
                    ((BusinessFinderItemGridViewHolder) (temp)).icon.setImageBitmap(((Bitmap) (obj)));
                    return view;
                }
            }
        }
        return view;
    }

    public void putImage(String s, Bitmap bitmap)
    {
        if(s != null && bitmap != null)
            _images.put(s, bitmap);
    }

    public void setData(ArrayList arraylist)
    {
        if(_data != null)
        {
            _data = arraylist;
            notifyDataSetInvalidated();
        }
    }

    private Context _context;
    private ArrayList _data;
    private BitmapMemoryCaching _images;
}
