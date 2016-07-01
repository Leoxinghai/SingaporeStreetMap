

package streetdirectory.mobile.modules.locationdetail.businessin;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.*;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.xinghai.mycurve.R;

import java.util.ArrayList;
import streetdirectory.mobile.core.ui.FillRatioImageView;
import streetdirectory.mobile.core.ui.UIHelper;
import streetdirectory.mobile.modules.core.OfferCellViewHolder;
import streetdirectory.mobile.modules.locationdetail.businessin.service.BusinessInServiceOutput;
import streetdirectory.mobile.service.SDHttpServiceOutput;
import streetdirectory.mobile.service.URLFactory;

// Referenced classes of package streetdirectory.mobile.modules.locationdetail.businessin:
//            BusinessInAdapter

public class BusinessInWithOffersAdapter extends BusinessInAdapter
{

    public BusinessInWithOffersAdapter(Context context)
    {

        super(context);
    }

    public int getItemViewType(int i)
    {
        if(i < _data.childs.size())
            return ((BusinessInServiceOutput)getItem(i)).offer == null ? 0 : 2;
        else
            return 1;
    }

    public View getView(int i, View view, ViewGroup viewgroup)
    {
        OfferCellViewHolder temp;

        if(getItemViewType(i) == 2)
        {
            BusinessInServiceOutput businessinserviceoutput;
            if(view == null)
            {
                view = ((LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.cell_bus_offer, viewgroup, false);
                temp = new OfferCellViewHolder();
                temp.background = (RelativeLayout)view.findViewById(R.id.BackgroundLayout);
                temp.offerImage = (FillRatioImageView)view.findViewById(R.id.imageView_offer);
                temp.progressBar = (ProgressBar)view.findViewById(R.id.progress_indicator_offer_image);
                temp.distanceLabel = (TextView)view.findViewById(R.id.textViewDistance);
                temp.textViewCompanyName = (TextView)view.findViewById(R.id.textViewCompanyName);
                ((OfferCellViewHolder) (temp)).offerImage.getLayoutParams().height = (int)((float)UIHelper.getScreenDimension().widthPixels - UIHelper.toPixel(20F));
                ((OfferCellViewHolder) (temp)).offerImage.requestLayout();
                view.setTag(viewgroup);
            } else
            {
                temp = (OfferCellViewHolder)view.getTag();
            }
            businessinserviceoutput = (BusinessInServiceOutput)getItem(i);
            ((OfferCellViewHolder) (temp)).distanceLabel.setTag(businessinserviceoutput);
            plottingData(temp, businessinserviceoutput, i);
            return view;
        } else
        {
            return getView(i, view, viewgroup);
        }
    }

    public int getViewTypeCount()
    {
        return getViewTypeCount() + 1;
    }

    public void plottingData(OfferCellViewHolder offercellviewholder, BusinessInServiceOutput businessinserviceoutput, int i)
    {
        String s = URLFactory.createURLResizeImage(businessinserviceoutput.offerThumbnailImage, 699, 699, 1, 40);
        Picasso.with(_context).load(s).into(offercellviewholder.offerImage);
        offercellviewholder.offerImage.setTag(businessinserviceoutput);
        if(businessinserviceoutput.venue != null && businessinserviceoutput.venue.length() > 0)
        {
            offercellviewholder.textViewCompanyName.setVisibility(View.VISIBLE);
            offercellviewholder.textViewCompanyName.setText(businessinserviceoutput.venue);
            return;
        } else
        {
            offercellviewholder.textViewCompanyName.setVisibility(View.INVISIBLE);
            return;
        }
    }

    private static final int IMAGE_QUALITY = 40;
    private static final int IMAGE_WIDTH = 699;
    public static final int OFFER_CELL = 2;
}
