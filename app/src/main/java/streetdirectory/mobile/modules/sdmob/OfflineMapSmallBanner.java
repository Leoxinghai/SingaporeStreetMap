

package streetdirectory.mobile.modules.sdmob;

import android.content.Context;
import android.view.View;

// Referenced classes of package streetdirectory.mobile.modules.sdmob:
//            SmallBanner, OfflineMapSmallBannerView

public class OfflineMapSmallBanner extends SmallBanner
{

    public OfflineMapSmallBanner(SdMobHelper.SdMobUnit sdmobunit)
    {
        super(sdmobunit);
    }

    public View getView(Context context)
    {
        if(bannerView == null)
            bannerView = new OfflineMapSmallBannerView(context);
        return bannerView;
    }
}
