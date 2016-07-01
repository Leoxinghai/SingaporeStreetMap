

package streetdirectory.mobile.modules.sdmob;

import android.content.Context;
import android.view.View;

// Referenced classes of package streetdirectory.mobile.modules.sdmob:
//            SmallBanner, OfferSmallBannerView

public class OfferSmallBanner extends SmallBanner
{

    public OfferSmallBanner(SdMobHelper.SdMobUnit sdmobunit)
    {
        super(sdmobunit);
    }

    public View getView(Context context)
    {
        if(bannerView == null)
            bannerView = new OfferSmallBannerView(context);
        return bannerView;
    }
}
