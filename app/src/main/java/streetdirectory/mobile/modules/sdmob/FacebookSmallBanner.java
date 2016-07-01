

package streetdirectory.mobile.modules.sdmob;

import android.content.Context;
import android.view.View;

// Referenced classes of package streetdirectory.mobile.modules.sdmob:
//            SmallBanner, FacebookSmallBannerView

public class FacebookSmallBanner extends SmallBanner
{

    public FacebookSmallBanner(SdMobHelper.SdMobUnit sdmobunit)
    {
        super(sdmobunit);
    }

    public void destroyAdView()
    {
    }

    public View getView(Context context)
    {
        if(bannerView == null)
        {
            bannerView = new FacebookSmallBannerView(context);
            ((FacebookSmallBannerView)bannerView).placementId = unit.id;
            ((FacebookSmallBannerView)bannerView).setupAdView();
        }
        return bannerView;
    }
}
