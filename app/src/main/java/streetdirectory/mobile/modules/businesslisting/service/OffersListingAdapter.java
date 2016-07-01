// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.businesslisting.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.*;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.xinghai.mycurve.R;

import java.util.ArrayList;
import streetdirectory.mobile.core.ui.FillRatioImageView;
import streetdirectory.mobile.core.ui.UIHelper;
import streetdirectory.mobile.modules.core.LocationBusinessServiceOutput;
import streetdirectory.mobile.modules.core.OfferCellViewHolder;
import streetdirectory.mobile.service.SDHttpServiceOutput;
import streetdirectory.mobile.service.URLFactory;

public class OffersListingAdapter extends BaseAdapter
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

        public abstract void onImageNotFound(LocationBusinessServiceOutput locationbusinessserviceoutput, int i);
    }

    public static interface OnLoadMoreListener
    {

        public abstract void onLoadMoreList();
    }


    public OffersListingAdapter(Context context)
    {
        _data = new SDHttpServiceOutput();
        _context = context;
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

    public View getView(int i, View view, ViewGroup viewgroup)
    {
        int j = getItemViewType(i);
        View view1;
        OfferCellViewHolder temp1;
        if(j == 0)
        {
            if(view == null)
            {
                view = ((LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.cell_offer, viewgroup, false);
                temp1 = new OfferCellViewHolder();
                temp1.background = (RelativeLayout)view.findViewById(R.id.BackgroundLayout);
                temp1.offerImage = (FillRatioImageView)view.findViewById(R.id.imageView_offer);
                ((OfferCellViewHolder) (temp1)).offerImage.getLayoutParams().height = (int)((float)UIHelper.getScreenDimension().widthPixels - UIHelper.toPixel(40F));
                ((OfferCellViewHolder) (temp1)).offerImage.requestLayout();
                temp1.distanceLabel = (TextView)view.findViewById(R.id.textViewDistance);
                temp1.placeNameLabel = (TextView)view.findViewById(R.id.textViewPlaceName);
                temp1.textViewCompanyName = (TextView)view.findViewById(R.id.textViewCompany);
                temp1.imageViewPin = (ImageView)view.findViewById(R.id.imageViewPin);
                view.setTag(temp1);
            } else
            {
                temp1 = (OfferCellViewHolder)view.getTag();
            }
            plottingData(temp1, getItem(i));
            view1 = view;
        } else
        {
            view1 = view;
            if(j == 1)
            {
                View view2 = view;
                if(view == null)
                    view2 = ((LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.cell_load_more, viewgroup, false);
                view1 = view2;
                if(_loadMoreListener != null)
                {
                    _loadMoreListener.onLoadMoreList();
                    return view2;
                }
            }
        }
        return view1;
    }

    public int getViewTypeCount()
    {
        return 2;
    }

    public void plottingData(OfferCellViewHolder offercellviewholder, LocationBusinessServiceOutput locationbusinessserviceoutput)
    {
        String s = URLFactory.createURLResizeImage(locationbusinessserviceoutput.offerThumbnailImage, 512, 512, 1, 75);
        Picasso.with(_context).load(s).into(offercellviewholder.offerImage);
        offercellviewholder.offerImage.setTag(locationbusinessserviceoutput);
        String s1;
        if(locationbusinessserviceoutput.venue != null)
            s = locationbusinessserviceoutput.venue.replace(" Pte Ltd", "");
        else
            s = "";
        s1 = s;
        if(locationbusinessserviceoutput.unitNo != null)
        {
            s1 = s;
            if(locationbusinessserviceoutput.unitNo.length() > 0)
                s1 = (new StringBuilder()).append(s).append(" (").append(locationbusinessserviceoutput.unitNo).append(")").toString();
        }
        offercellviewholder.textViewCompanyName.setText(s1);
        if(locationbusinessserviceoutput.placeName != null && locationbusinessserviceoutput.placeName.length() > 0)
            s = locationbusinessserviceoutput.placeName;
        else
        if(locationbusinessserviceoutput.address != null && locationbusinessserviceoutput.address.length() > 0)
            s = locationbusinessserviceoutput.address.substring(0, locationbusinessserviceoutput.address.length() - 7);
        else
            s = "";
        offercellviewholder.placeNameLabel.setText(s);
        if(locationbusinessserviceoutput.distance != null && locationbusinessserviceoutput.distance.length() > 0)
        {
            offercellviewholder.imageViewPin.setVisibility(View.VISIBLE);
            offercellviewholder.distanceLabel.setPadding(0, 0, (int)UIHelper.toPixel(10F), 0);
            if(s.length() > 0)
            {
                offercellviewholder.distanceLabel.setText((new StringBuilder()).append(" @ ").append(locationbusinessserviceoutput.distance).toString());
                return;
            } else
            {
                offercellviewholder.distanceLabel.setText((new StringBuilder()).append(" @ ").append(locationbusinessserviceoutput.distance).toString());
                return;
            }
        } else
        {
            offercellviewholder.imageViewPin.setVisibility(View.INVISIBLE);
            offercellviewholder.distanceLabel.setText("");
            offercellviewholder.distanceLabel.setPadding(0, 0, 0, 0);
            return;
        }
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

    private static final int IMAGE_QUALITY = 75;
    private static final int IMAGE_WIDTH = 512;
    public static final int LOAD_MORE_CELL = 1;
    public static final int OFFER_CELL = 0;
    protected OnCallButtonClickedListener _callButtonClickedListener;
    protected Context _context;
    protected SDHttpServiceOutput _data;
    protected OnImageClickedListener _imageClickedListener;
    protected OnImageNotFoundListener _imageNotFoundListener;
    protected OnLoadMoreListener _loadMoreListener;
}
