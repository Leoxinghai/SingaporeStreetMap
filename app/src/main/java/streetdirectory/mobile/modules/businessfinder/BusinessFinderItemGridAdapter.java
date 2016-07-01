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

public class BusinessFinderItemGridAdapter extends BaseAdapter
{
    public static interface BusinessFinderItemAdapterHandler
    {

        public abstract void onCategoryIconClicked(BusinessFinderServiceOutput businessfinderserviceoutput);

        public abstract void onCategoryIconNotFound(BusinessFinderServiceOutput businessfinderserviceoutput, int i, int j);
    }


    public BusinessFinderItemGridAdapter(Context context)
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


    public BusinessFinderServiceOutput getItem(int i)
    {
        return (BusinessFinderServiceOutput)_data.get(i);
    }

    public long getItemId(int i)
    {
        return (long)i;
    }

    public View getView(int i, View view, final ViewGroup holder)
    {
        BusinessFinderServiceOutput businessfinderserviceoutput;
        android.graphics.drawable.Drawable drawable;

        BusinessFinderItemGridViewHolder businessFinderItemGridViewHolder1;


        if(view == null)
        {
            view = ((LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.grid_business_finder_item, holder, false);
            final BusinessFinderItemGridViewHolder businessFinderItemGridViewHolder = new BusinessFinderItemGridViewHolder();
            businessFinderItemGridViewHolder.icon = (ImageView)view.findViewById(R.id.IconButton);
            businessFinderItemGridViewHolder.titleLabel = (TextView)view.findViewById(R.id.TitleLabel);
            view.setTag(holder);
            if(_treeObserver == null && iconSize == 0)
            {
                _treeObserver = ((BusinessFinderItemGridViewHolder) (businessFinderItemGridViewHolder)).icon.getViewTreeObserver();
                _treeObserver.addOnGlobalLayoutListener(new android.view.ViewTreeObserver.OnGlobalLayoutListener() {

                    public void onGlobalLayout()
                    {
                        businessFinderItemGridViewHolder.icon.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        _treeObserver = null;
                        iconSize = businessFinderItemGridViewHolder.icon.getWidth();
                        businessFinderItemGridViewHolder.icon.getLayoutParams().height = iconSize - 4;
                        notifyDataSetChanged();
                    }

                });
            }
            ((BusinessFinderItemGridViewHolder) (businessFinderItemGridViewHolder)).icon.setOnClickListener(new android.view.View.OnClickListener() {

                public void onClick(View view1)
                {
                    if(_handler != null)
                    {
                        BusinessFinderServiceOutput businessFinderServiceOutput = (BusinessFinderServiceOutput)view1.getTag();
                        _handler.onCategoryIconClicked(businessFinderServiceOutput);
                    }
                }

            });
            businessFinderItemGridViewHolder1 = businessFinderItemGridViewHolder;
        } else
        {
             businessFinderItemGridViewHolder1 = (BusinessFinderItemGridViewHolder)view.getTag();
        }

        businessfinderserviceoutput = getItem(i);
        ((BusinessFinderItemGridViewHolder) (businessFinderItemGridViewHolder1)).icon.setTag(businessfinderserviceoutput);
        ((BusinessFinderItemGridViewHolder) (businessFinderItemGridViewHolder1)).titleLabel.setText(businessfinderserviceoutput.name);
        i = _context.getResources().getIdentifier(businessfinderserviceoutput.icon, "drawable", _context.getPackageName());
        if(i != 0) {
			drawable = _context.getResources().getDrawable(i);
			if(drawable == null) {
				Bitmap bitmap = (Bitmap)_images.get(businessfinderserviceoutput.icon);
				if(bitmap != null)
					((BusinessFinderItemGridViewHolder) (businessFinderItemGridViewHolder1)).icon.setImageBitmap(bitmap);
				else
				if(_handler != null && iconSize > 0)
				{
					((BusinessFinderItemGridViewHolder) (businessFinderItemGridViewHolder1)).icon.setImageBitmap(null);
					_handler.onCategoryIconNotFound(businessfinderserviceoutput, iconSize, iconSize);
				}
			} else {
				((BusinessFinderItemGridViewHolder) (businessFinderItemGridViewHolder1)).icon.setImageDrawable(drawable);
			}
		}

        if(iconSize > 0)
            ((BusinessFinderItemGridViewHolder) (businessFinderItemGridViewHolder1)).icon.getLayoutParams().height = iconSize - 4;
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

    public void setHandler(BusinessFinderItemAdapterHandler businessfinderitemadapterhandler)
    {
        _handler = businessfinderitemadapterhandler;
    }

    private Context _context;
    private ArrayList _data;
    private BusinessFinderItemAdapterHandler _handler;
    private BitmapMemoryCaching _images;
    private ViewTreeObserver _treeObserver;
    private int iconSize;


/*
    static ViewTreeObserver access$002(BusinessFinderItemGridAdapter businessfinderitemgridadapter, ViewTreeObserver viewtreeobserver)
    {
        businessfinderitemgridadapter._treeObserver = viewtreeobserver;
        return viewtreeobserver;
    }

*/



/*
    static int access$102(BusinessFinderItemGridAdapter businessfinderitemgridadapter, int i)
    {
        businessfinderitemgridadapter.iconSize = i;
        return i;
    }

*/

}
