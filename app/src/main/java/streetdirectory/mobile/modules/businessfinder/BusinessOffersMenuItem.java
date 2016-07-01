

package streetdirectory.mobile.modules.businessfinder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.core.ui.sidemenu.SDSideMenuBasicItem;
import streetdirectory.mobile.core.ui.sidemenu.SideMenuLayout;
import streetdirectory.mobile.gis.GeoSense;
import streetdirectory.mobile.gis.SdArea;
import streetdirectory.mobile.modules.businesslisting.BusinessListingActivity;
import streetdirectory.mobile.modules.businesslisting.offers.OffersListingActivity;
import streetdirectory.mobile.sd.SDBlackboard;

public class BusinessOffersMenuItem extends SDSideMenuBasicItem
{

    public BusinessOffersMenuItem()
    {
        super("Offers near you", R.drawable.ic_menu_offers_black, R.drawable.ic_menu_offers);
    }

    public void execute(Context context, SideMenuLayout sidemenulayout)
    {
        if(context instanceof BusinessListingActivity)
        {
            sidemenulayout.slideClose();
            return;
        } else
        {
            Intent intent = new Intent(context, OffersListingActivity.class);
            intent.putExtra("categoryID", 11342);
            intent.putExtra("categoryName", "Offers");
            intent.putExtra("longitude", SDBlackboard.currentLongitude);
            intent.putExtra("latitude", SDBlackboard.currentLatitude);
            intent.putExtra("fromMenu", true);
            intent.putExtra("countryCode", SDBlackboard.currentCountryCode);
            intent.putExtra("countryName", GeoSense.getArea(SDBlackboard.currentCountryCode).areaName);
            intent.putExtra("fromOffers", true);
            context.startActivity(intent);
            ((Activity)context).finish();
            return;
        }
    }
}
