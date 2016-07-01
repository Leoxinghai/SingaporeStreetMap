// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.core.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.TypedValue;
import android.view.*;
import android.widget.*;

import com.xinghai.mycurve.R;

import java.util.ArrayList;
import streetdirectory.mobile.core.BitmapMemoryCaching;
import streetdirectory.mobile.core.SDLogger;
import streetdirectory.mobile.modules.core.LocationBusinessCellViewHolder;
import streetdirectory.mobile.modules.core.LocationBusinessServiceOutput;
import streetdirectory.mobile.service.SDHttpServiceOutput;

public class LocationBusinessAdapter extends BaseAdapter
{
    public static interface OnCallButtonClickedListener
    {

        public abstract void onCallButtonClicked(LocationBusinessServiceOutput locationbusinessserviceoutput, int i, String s);
    }

    public static interface OnImageClickedListener
    {

        public abstract void onImageClicked(LocationBusinessServiceOutput locationbusinessserviceoutput, int i, Bitmap bitmap);
    }

    public static interface OnImageNotFoundListener
    {

        public abstract void onImageNotFound(LocationBusinessServiceOutput locationbusinessserviceoutput, int i, int j, int k);
    }

    public static interface OnLoadMoreListener
    {

        public abstract void onLoadMoreList();
    }


    public LocationBusinessAdapter(Context context)
    {
        _data = new SDHttpServiceOutput();
        _context = context;
        _images = new BitmapMemoryCaching(context);
        int j = 92;
        int i = 64;
        if(_context.getResources().getBoolean(R.bool.isTablet))
        {
            j = 152;
            i = 109;
        }
        imageWidth = Math.round(TypedValue.applyDimension(1, j, _context.getResources().getDisplayMetrics()));
        imageHeight = Math.round(TypedValue.applyDimension(1, i, _context.getResources().getDisplayMetrics()));
    }

    public void addItem(LocationBusinessServiceOutput locationbusinessserviceoutput)
    {
        if(locationbusinessserviceoutput != null)
        {
            _data.childs.add(locationbusinessserviceoutput);
            notifyDataSetChanged();
        }
    }

    public void clear()
    {
        _data.childs.clear();
        _images.evictAll();
        notifyDataSetInvalidated();
    }

    public int getCount()
    {
        int j = _data.childs.size();
        if(j > 0)
        {
            int i = j;
            if((long)j < _data.total)
                i = j + 1;
            return i;
        } else
        {
            return 0;
        }
    }


    public LocationBusinessServiceOutput getItem(int i)
    {
        return (LocationBusinessServiceOutput)_data.childs.get(i);
    }

    public long getItemId(int i)
    {
        return (long)i;
    }

    public int getItemSize()
    {
        return _data.childs.size();
    }

    public int getItemViewType(int i)
    {
        return i >= _data.childs.size() ? 1 : 0;
    }

    public View getView(final int position, View view, ViewGroup viewgroup)
    {
        int i = getItemViewType(position);
        Object obj;
        LocationBusinessCellViewHolder temp;
        if(i == 0)
        {
            if(view == null)
            {
                view = ((LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.cell_location_business, viewgroup, false);
                temp = new LocationBusinessCellViewHolder();
                temp.background = (LinearLayout)view.findViewById(R.id.BackgroundLayout);
                temp.icon = (ImageView)view.findViewById(R.id.IconButton);
                temp.titleLabel = (TextView)view.findViewById(R.id.TitleLabel);
                temp.aboutUsLabel = (TextView)view.findViewById(R.id.AboutUsLabel);
                temp.detailLabel = (TextView)view.findViewById(R.id.DetailLabel);
                temp.subdetailLabel = (TextView)view.findViewById(R.id.SubdetailLabel);
                temp.thumbupLayout = (LinearLayout)view.findViewById(R.id.ThumbupLayout);
                temp.tipsLayout = (LinearLayout)view.findViewById(R.id.TipsLayout);
                temp.tipsPhoto = (ImageView)view.findViewById(R.id.TipsPhoto);
                temp.commentLabel = (TextView)view.findViewById(R.id.CommentLabel);
                temp.callButton = (ImageButton)view.findViewById(R.id.imageButtonCall);
                temp.adsLabel = (TextView)view.findViewById(R.id.AdsLabel);
                temp.iconLocation = (ImageView)view.findViewById(R.id.imageViewIconLocation);
                temp.distanceLabel = (TextView)view.findViewById(R.id.textViewDistance);
                ((LocationBusinessCellViewHolder) (temp)).icon.setFocusable(false);
                ((LocationBusinessCellViewHolder) (temp)).icon.setClickable(false);
                temp.viewOfferLabel = (TextView)view.findViewById(R.id.textViewViewOffer);
                temp.gotoOfferLabel = (TextView)view.findViewById(R.id.textView_goto_offer);
                view.setTag(viewgroup);
            } else
            {
                temp = (LocationBusinessCellViewHolder)view.getTag();
            }
            obj = getItem(position);
            ((LocationBusinessCellViewHolder) (temp)).icon.setTag(obj);
            ((LocationBusinessCellViewHolder) (temp)).callButton.setTag(obj);
            plottingData(temp, ((LocationBusinessServiceOutput) (obj)), position);
            ((LocationBusinessCellViewHolder) (temp)).callButton.setOnClickListener(new android.view.View.OnClickListener() {

                public void onClick(View view2)
                {
                    if(_callButtonClickedListener != null)
                    {
                        LocationBusinessServiceOutput temp2 = (LocationBusinessServiceOutput)view2.getTag();
                        if(view2 != null)
                        {
                            String s = ((LocationBusinessServiceOutput) (temp2)).phoneNumber;
                            _callButtonClickedListener.onCallButtonClicked(temp2, position, s);
                        }
                    }
                    SDLogger.debug("call button clicked");
                }

            });

            obj = view;
        } else
        {
            obj = view;
            if(i == 1)
            {
                View view1 = view;
                if(view == null)
                    view1 = ((LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.cell_load_more, viewgroup, false);
                obj = view1;
                if(_loadMoreListener != null)
                {
                    _loadMoreListener.onLoadMoreList();
                    return view1;
                }
            }
        }
        return ((View) (obj));
    }

    public int getViewTypeCount()
    {
        return 2;
    }

    public void plottingData(LocationBusinessCellViewHolder locationbusinesscellviewholder, LocationBusinessServiceOutput locationbusinessserviceoutput, int i)
    {
        Bitmap bitmap = (Bitmap)_images.get(locationbusinessserviceoutput.uniqueID);
        if(bitmap == null)
        {
            if(locationbusinessserviceoutput.type == 1)
                locationbusinesscellviewholder.icon.setImageDrawable(_context.getResources().getDrawable(R.drawable.location_no_photo));
            else
                locationbusinesscellviewholder.icon.setImageDrawable(_context.getResources().getDrawable(R.drawable.business_no_photo));
            if(_imageNotFoundListener != null)
                _imageNotFoundListener.onImageNotFound(locationbusinessserviceoutput, i, imageWidth, imageHeight);
        } else
        {
            locationbusinesscellviewholder.icon.setImageBitmap(bitmap);
        }
        if(locationbusinessserviceoutput.aboutUs != null)
        {
            locationbusinesscellviewholder.aboutUsLabel.setText(locationbusinessserviceoutput.aboutUs);
            locationbusinesscellviewholder.aboutUsLabel.setVisibility(View.VISIBLE);
        } else
        {
            locationbusinesscellviewholder.aboutUsLabel.setVisibility(View.INVISIBLE);
        }
        if(locationbusinessserviceoutput.adsLabel != null && !locationbusinessserviceoutput.adsLabel.equals(""))
        {
            locationbusinesscellviewholder.adsLabel.setVisibility(View.VISIBLE);
            locationbusinesscellviewholder.adsLabel.setText(locationbusinessserviceoutput.adsLabel);
            return;
        } else
        {
            locationbusinesscellviewholder.adsLabel.setVisibility(View.INVISIBLE);
            return;
        }
    }

    public void putImage(String s, Bitmap bitmap)
    {
        if(s != null && bitmap != null)
            _images.put(s, bitmap);
    }

    public void setData(SDHttpServiceOutput sdhttpserviceoutput)
    {
        if(_data != null)
        {
            _data = sdhttpserviceoutput;
            notifyDataSetInvalidated();
        }
    }

    public void setOnCallButtonClickedListener(OnCallButtonClickedListener oncallbuttonclickedlistener)
    {
        _callButtonClickedListener = oncallbuttonclickedlistener;
    }

    public void setOnImageClickedListener(OnImageClickedListener onimageclickedlistener)
    {
        _imageClickedListener = onimageclickedlistener;
    }

    public void setOnImageNotFoundListener(OnImageNotFoundListener onimagenotfoundlistener)
    {
        _imageNotFoundListener = onimagenotfoundlistener;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onloadmorelistener)
    {
        _loadMoreListener = onloadmorelistener;
    }

    public void setTotalItem(long l)
    {
        _data.total = l;
    }

    public static final int IMAGE_HEIGHT_PHONE_DP = 64;
    public static final int IMAGE_HEIGHT_TABLET_DP = 109;
    public static final int IMAGE_WIDTH_PHONE_DP = 92;
    public static final int IMAGE_WIDTH_TABLET_DP = 152;
    public static final int LOAD_MORE_CELL = 1;
    public static final int LOCATION_BUSINESS_CELL = 0;
    protected OnCallButtonClickedListener _callButtonClickedListener;
    protected Context _context;
    protected SDHttpServiceOutput _data;
    protected OnImageClickedListener _imageClickedListener;
    protected OnImageNotFoundListener _imageNotFoundListener;
    protected BitmapMemoryCaching _images;
    protected OnLoadMoreListener _loadMoreListener;
    public int imageHeight;
    public int imageWidth;
}
