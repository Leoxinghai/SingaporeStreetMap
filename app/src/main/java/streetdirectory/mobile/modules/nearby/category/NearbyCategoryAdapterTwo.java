// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.nearby.category;

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

public class NearbyCategoryAdapterTwo extends BaseAdapter
{
    public static interface NearbyCategoryAdapterHandler
    {

        public abstract void onCategoryIconClicked(NearbyCategoryServiceOutput nearbycategoryserviceoutput);

        public abstract void onCategoryIconNotFound(NearbyCategoryServiceOutput nearbycategoryserviceoutput, int i, int j);
    }


    public NearbyCategoryAdapterTwo(Context context)
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
        NearbyCategoryServiceOutput nearbycategoryserviceoutput;
        NearbyCategoryGridViewHolder temp = null;
        if(view == null)
        {
            View view1 = ((LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.cell_business_finder_category, viewgroup, false);
            final NearbyCategoryGridViewHolder holder = new NearbyCategoryGridViewHolder();
            holder.cellLayout = (RelativeLayout)view1.findViewById(R.id.cell_business_finder);
            holder.icon = (ImageView)view1.findViewById(R.id.imageView1);
            holder.titleLabel = (TextView)view1.findViewById(R.id.textView1);
            view1.setTag(holder);
            //viewgroup = holder;
            view = view1;
            if(_treeObserver == null)
            {
                //viewgroup = holder;
                view = view1;
                if(iconSize == 0)
                {
                    _treeObserver = holder.icon.getViewTreeObserver();
                    _treeObserver.addOnGlobalLayoutListener(new android.view.ViewTreeObserver.OnGlobalLayoutListener() {

                        public void onGlobalLayout()
                        {
                            holder.icon.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            _treeObserver = null;
                            iconSize = holder.icon.getWidth();
                            holder.icon.getLayoutParams().height = iconSize;
                            SDLogger.info((new StringBuilder()).append("Icon size: ").append(iconSize).toString());
                            notifyDataSetChanged();
                        }


                    });
                    view = view1;
                    temp = holder;
                }
            }
        } else
        {
            temp = (NearbyCategoryGridViewHolder)view.getTag();
        }
        nearbycategoryserviceoutput = getItem(i);
        ((NearbyCategoryGridViewHolder) (temp)).icon.setTag(nearbycategoryserviceoutput);
        ((NearbyCategoryGridViewHolder) (temp)).titleLabel.setText(nearbycategoryserviceoutput.name);
        if(nearbycategoryserviceoutput.cellSelected)
            ((NearbyCategoryGridViewHolder) (temp)).cellLayout.setBackgroundResource(R.drawable.selector_color_cell_business_finder_category_selected);
        else
            ((NearbyCategoryGridViewHolder) (temp)).cellLayout.setBackgroundResource(R.drawable.selector_color_cell_business_finder_category);
        if(nearbycategoryserviceoutput.iconBitmap == null)
        {
            if(nearbycategoryserviceoutput.icon == null)
            {
                boolean flag = false;
                if(nearbycategoryserviceoutput.categoryID == -1 || nearbycategoryserviceoutput.categoryID == -2)
                {
                    if(nearbycategoryserviceoutput.type == 2)
                        i = R.drawable.nearby_category_all_biz;
                    else
                        i = R.drawable.nearby_category_all_places;
                } else
                {
                    i = ((flag) ? 1 : 0);
                    if(nearbycategoryserviceoutput.categoryType == 0)
                    {
                        i = ((flag) ? 1 : 0);
                        if(nearbycategoryserviceoutput.categoryID == 93)
                            i = R.drawable.nearby_category_bus_timing;
                    }
                }
                if(i != 0)
                {
                    Bitmap bitmap = (Bitmap)_images.get((new StringBuilder()).append(i).append("").toString());
                    if(bitmap != null)
                    {
                        nearbycategoryserviceoutput.iconBitmap = bitmap;
                        ((NearbyCategoryGridViewHolder) (temp)).icon.setImageBitmap(bitmap);
                    } else
                    if(iconSize != 0)
                        processBitmap(i, iconSize, iconSize);
                } else
                {
                    ((NearbyCategoryGridViewHolder) (temp)).icon.setImageBitmap(null);
                }
            } else
            {
                Bitmap bitmap1 = (Bitmap)_images.get(nearbycategoryserviceoutput.icon);
                if(bitmap1 != null)
                {
                    nearbycategoryserviceoutput.iconBitmap = bitmap1;
                    ((NearbyCategoryGridViewHolder) (temp)).icon.setImageBitmap(bitmap1);
                } else
                if(_handler != null && iconSize > 0)
                {
                    ((NearbyCategoryGridViewHolder) (temp)).icon.setImageBitmap(null);
                    _handler.onCategoryIconNotFound(nearbycategoryserviceoutput, iconSize, iconSize);
                }
            }
        } else
        {
            ((NearbyCategoryGridViewHolder) (temp)).icon.setImageBitmap(nearbycategoryserviceoutput.iconBitmap);
        }
        if(iconSize > 0)
            ((NearbyCategoryGridViewHolder) (temp)).icon.getLayoutParams().height = iconSize;
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


/*
    static ViewTreeObserver access$002(NearbyCategoryAdapterTwo nearbycategoryadaptertwo, ViewTreeObserver viewtreeobserver)
    {
        nearbycategoryadaptertwo._treeObserver = viewtreeobserver;
        return viewtreeobserver;
    }

*/



/*
    static int access$102(NearbyCategoryAdapterTwo nearbycategoryadaptertwo, int i)
    {
        nearbycategoryadaptertwo.iconSize = i;
        return i;
    }

*/


}
