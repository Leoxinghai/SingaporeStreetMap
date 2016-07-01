

package streetdirectory.mobile.modules.sdmob;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.core.SDStory;
import streetdirectory.mobile.gis.GeoSense;
import streetdirectory.mobile.gis.SdArea;
import streetdirectory.mobile.modules.businesslisting.offers.OffersListingActivity;
import streetdirectory.mobile.sd.SDBlackboard;
import streetdirectory.mobile.service.URLFactory;

public class OfferSmallBannerView extends RelativeLayout
{

    public OfferSmallBannerView(Context context)
    {
        super(context);
        init();
    }

    public OfferSmallBannerView(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
        init();
    }

    public OfferSmallBannerView(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        init();
    }

    private void init()
    {
        inflate(getContext(), R.layout.layout_sdmob_offer, this);
        mTextViewOffers = (TextView)findViewById(R.id.textViewOffer);
        mTextViewOffersLoading = (TextView)findViewById(R.id.textViewLoading);
        setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                Intent intent = new Intent(getContext(), OffersListingActivity.class);
                intent.putExtra("categoryID", 11342);
                intent.putExtra("categoryName", "Offers");
                intent.putExtra("longitude", SDBlackboard.currentLongitude);
                intent.putExtra("latitude", SDBlackboard.currentLatitude);
                intent.putExtra("kmRange", 2);
                boolean flag;
                if(!mTextViewOffers.getText().equals(getContext().getString(R.string.sdmob_view_all_offers)))
                    flag = true;
                else
                    flag = false;
                intent.putExtra("fromBottomBanner", flag);
                intent.putExtra("countryCode", SDBlackboard.currentCountryCode);
                intent.putExtra("countryName", GeoSense.getArea(SDBlackboard.currentCountryCode).areaName);
                intent.putExtra("isBottomBanner", true);
                getContext().startActivity(intent);
                SDStory.post(URLFactory.createGantBottomBannerNearbyOffers(), SDStory.createDefaultParams());
            }

        });

    }

    public void setToDownloadMode()
    {
        mTextViewOffersLoading.setVisibility(View.VISIBLE);
    }

    public void setTotalOffers(long l)
    {
        mTextViewOffersLoading.setVisibility(View.INVISIBLE);
        if(l > 0L)
        {
            String s = "Offer";
            if(l > 1L)
                s = "Offers";
            mTextViewOffers.setText((new StringBuilder()).append(l).append(" ").append(s).append(" in this area").toString());
            return;
        } else
        {
            mTextViewOffers.setText(getContext().getString(R.string.sdmob_view_all_offers));
            return;
        }
    }

    private ViewGroup mRoot;
    private TextView mTextViewOffers;
    private TextView mTextViewOffersLoading;

}
