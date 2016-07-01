// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.nearby.category;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.*;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.util.ArrayList;
import java.util.Iterator;
import streetdirectory.mobile.core.*;
import streetdirectory.mobile.modules.nearby.category.service.NearbyCategoryServiceOutput;

// Referenced classes of package streetdirectory.mobile.modules.nearby.category:
//            NearbyCategoryGridViewHolder

public class NearbyCategoryAdapter extends BaseAdapter
{
    public static interface NearbyCategoryAdapterHandler
    {

        public abstract void onCategoryIconClicked(NearbyCategoryServiceOutput nearbycategoryserviceoutput);

        public abstract void onCategoryIconNotFound(NearbyCategoryServiceOutput nearbycategoryserviceoutput, int i, int j);
    }


    public NearbyCategoryAdapter(Context context)
    {
        _data = new ArrayList();
        _loaders = new ArrayList();
        _context = context;
        _images = new BitmapMemoryCaching(context);
    }

    private void processBitmap(int i, int j, int k)
    {
        boolean flag1 = true;
        Iterator iterator = _loaders.iterator();
        boolean flag;
        do
        {
            flag = flag1;
            if(!iterator.hasNext())
                break;
            if(((BitmapResourceLoader)iterator.next()).resourceId != i)
                continue;
            flag = false;
            break;
        } while(true);
        if(flag)
        {
            BitmapResourceLoader bitmapresourceloader = new BitmapResourceLoader(_context, i, j, k) {

                public void onComplete(Bitmap bitmap)
                {
                    if(bitmap != null)
                    {
                        _images.put((new StringBuilder()).append(resourceId).append("").toString(), bitmap);
                        notifyDataSetChanged();
                    }
                    _loaders.remove(this);
                }

            };

            _loaders.add(bitmapresourceloader);
            bitmapresourceloader.executeTask(new Void[0]);
        }
    }

    public void abortAllProcess()
    {
        for(Iterator iterator = _loaders.iterator(); iterator.hasNext(); ((BitmapResourceLoader)iterator.next()).abort());
        _loaders.clear();
    }

    public void addItem(NearbyCategoryServiceOutput nearbycategoryserviceoutput)
    {
        if(nearbycategoryserviceoutput != null)
            _data.add(nearbycategoryserviceoutput);
    }

    public void addItems(ArrayList arraylist)
    {
        if(arraylist != null)
            _data.addAll(arraylist);
    }

    public boolean areAllItemsEnabled()
    {
        return false;
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


    public NearbyCategoryServiceOutput getItem(int i)
    {
        return (NearbyCategoryServiceOutput)_data.get(i);
    }

    public long getItemId(int i)
    {
        return (long)i;
    }

    public View getView(int i, View view, ViewGroup viewgroup)
    {
        Object obj;
        NearbyCategoryGridViewHolder temp;
        if(view == null)
        {
            view = ((LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.grid_nearby_category, viewgroup, false);
            temp = new NearbyCategoryGridViewHolder();
            temp.icon = (ImageView)view.findViewById(R.id.IconButton);
            temp.titleLabel = (TextView)view.findViewById(R.id.TitleLabel);
            view.setTag(viewgroup);
            iconSize = MathTools.dpToPixel(38F, (Activity)_context);
            ((NearbyCategoryGridViewHolder) (temp)).icon.setOnClickListener(new android.view.View.OnClickListener() {

                public void onClick(View view1)
                {
                    if(_handler != null)
                    {
                        NearbyCategoryServiceOutput temp2 = (NearbyCategoryServiceOutput)view1.getTag();
                        _handler.onCategoryIconClicked(temp2);
                    }
                }

            });
        } else
        {
            temp = (NearbyCategoryGridViewHolder)view.getTag();
        }
        obj = getItem(i);
        ((NearbyCategoryGridViewHolder) (temp)).icon.setTag(obj);
        ((NearbyCategoryGridViewHolder) (temp)).titleLabel.setText(((NearbyCategoryServiceOutput) (obj)).name);
        if(((NearbyCategoryServiceOutput) (obj)).icon == null)
        {
            boolean flag = false;
            if(((NearbyCategoryServiceOutput) (obj)).categoryID == 0)
            {
                if(((NearbyCategoryServiceOutput) (obj)).type == 2)
                    i = R.drawable.nearby_category_all_biz;
                else
                    i = R.drawable.nearby_category_all_places;
            } else
            {
                i = ((flag) ? 1 : 0);
                if(((NearbyCategoryServiceOutput) (obj)).categoryType == 0)
                {
                    i = ((flag) ? 1 : 0);
                    if(((NearbyCategoryServiceOutput) (obj)).categoryID == 93)
                        i = R.drawable.nearby_category_bus_timing;
                }
            }
            if(i != 0)
            {
                obj = (Bitmap)_images.get((new StringBuilder()).append(i).append("").toString());
                if(obj != null)
                    ((NearbyCategoryGridViewHolder) (temp)).icon.setImageBitmap(((Bitmap) (obj)));
                else
                if(iconSize != 0)
                    processBitmap(i, iconSize, iconSize);
            } else
            {
                ((NearbyCategoryGridViewHolder) (temp)).icon.setImageBitmap(null);
            }
        } else
        {
            Bitmap bitmap = (Bitmap)_images.get(((NearbyCategoryServiceOutput) (obj)).icon);
            if(bitmap != null)
                ((NearbyCategoryGridViewHolder) (temp)).icon.setImageBitmap(bitmap);
            else
            if(_handler != null && iconSize > 0)
            {
                ((NearbyCategoryGridViewHolder) (temp)).icon.setImageBitmap(null);
                _handler.onCategoryIconNotFound(((NearbyCategoryServiceOutput) (obj)), iconSize, iconSize);
            }
        }
        if(iconSize > 0)
            ((NearbyCategoryGridViewHolder) (temp)).icon.getLayoutParams().height = iconSize;
        return view;
    }

    public boolean isEnabled(int i)
    {
        return false;
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

    public void setHandler(NearbyCategoryAdapterHandler nearbycategoryadapterhandler)
    {
        _handler = nearbycategoryadapterhandler;
    }

    private Context _context;
    private ArrayList _data;
    private NearbyCategoryAdapterHandler _handler;
    private BitmapMemoryCaching _images;
    private ArrayList _loaders;
    private ViewTreeObserver _treeObserver;
    private int iconSize;



}
