// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.businessdetail;

import android.content.Context;
import android.view.*;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.xinghai.mycurve.R;

import java.util.ArrayList;
import streetdirectory.mobile.service.SDHttpServiceOutput;
import streetdirectory.mobile.service.URLFactory;

// Referenced classes of package streetdirectory.mobile.modules.businessdetail:
//            MoreOffersServiceOutput

public class MoreOffersAdapter extends android.support.v7.widget.RecyclerView.Adapter
{
    public static interface OnImageClickedListener
    {

        public abstract void onImageClicked(MoreOffersServiceOutput moreoffersserviceoutput);
    }

    public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder
    {

        ImageView imageViewOffer;
        ImageView imageViewOfferChecked;

        public ViewHolder(View view)
        {
            super(view);
            imageViewOffer = (ImageView)view.findViewById(R.id.imageView_offer);
            imageViewOfferChecked = (ImageView)view.findViewById(R.id.imageView_offer_checked);
        }
    }


    public MoreOffersAdapter(Context context)
    {
        _data = new SDHttpServiceOutput();
        _context = context;
    }

    public int getItemCount()
    {
        return _data.childs.size();
    }

    public void onBindViewHolder(android.support.v7.widget.RecyclerView.ViewHolder viewholder, int i)
    {
        onBindViewHolder((ViewHolder) viewholder, i);
    }

    public void onBindViewHolder(ViewHolder viewholder, int i)
    {
        MoreOffersServiceOutput moreoffersserviceoutput = (MoreOffersServiceOutput)_data.childs.get(i);
        String s = URLFactory.createURLResizeImage(moreoffersserviceoutput.offerImage, 256, 256, 1, 40);
        Picasso.with(_context).load(s).into(viewholder.imageViewOffer);
        if(moreoffersserviceoutput.offerId.equals(selectedOfferId))
        {
            viewholder.imageViewOfferChecked.setVisibility(View.VISIBLE);
            return;
        } else
        {
            viewholder.imageViewOffer.setTag(moreoffersserviceoutput);
            viewholder.imageViewOffer.setOnClickListener(new android.view.View.OnClickListener() {

                public void onClick(View view)
                {
                    if(_imageClickedListener != null)
                    {
                        MoreOffersServiceOutput temp = (MoreOffersServiceOutput)view.getTag();
                        _imageClickedListener.onImageClicked(temp);
                    }
                }

            });
            return;
        }
    }


    public ViewHolder onCreateViewHolder(ViewGroup viewgroup, int i)
    {
        return new ViewHolder(LayoutInflater.from(viewgroup.getContext()).inflate(R.layout.cell_more_offer, viewgroup, false));
    }

    public void setData(SDHttpServiceOutput sdhttpserviceoutput)
    {
        if(_data != null)
        {
            _data = sdhttpserviceoutput;
            notifyDataSetChanged();
        }
    }

    public void setOnImageClickedListener(OnImageClickedListener onimageclickedlistener)
    {
        _imageClickedListener = onimageclickedlistener;
    }

    public void setSelectedOfferId(String s)
    {
        selectedOfferId = s;
        notifyDataSetChanged();
    }

    private static final int IMAGE_QUALITY = 40;
    private static final int IMAGE_WIDTH = 256;
    protected Context _context;
    protected SDHttpServiceOutput _data;
    protected OnImageClickedListener _imageClickedListener;
    private String selectedOfferId;
}
