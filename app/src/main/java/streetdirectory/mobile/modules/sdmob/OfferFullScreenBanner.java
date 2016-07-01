

package streetdirectory.mobile.modules.sdmob;

import android.content.Context;
import android.content.Intent;

// Referenced classes of package streetdirectory.mobile.modules.sdmob:
//            FullScreenBanner, OfferFullScreenBannerActivity

public class OfferFullScreenBanner extends FullScreenBanner
{

    public OfferFullScreenBanner(SdMobHelper.SdMobUnit sdmobunit)
    {
        super(sdmobunit);
    }

    public void presentView(Context context)
    {
        Intent intent = new Intent(context, OfferFullScreenBannerActivity.class);
        intent.putExtra("banner_type", bannerType);
        context.startActivity(intent);
    }
}
