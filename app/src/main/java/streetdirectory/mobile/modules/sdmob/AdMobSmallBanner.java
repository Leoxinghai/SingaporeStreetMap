

package streetdirectory.mobile.modules.sdmob;

import android.content.Context;
import android.view.View;

// Referenced classes of package streetdirectory.mobile.modules.sdmob:
//            SmallBanner, AdMobSmallBannerView

public class AdMobSmallBanner extends SmallBanner
{

    public AdMobSmallBanner(SdMobHelper.SdMobUnit sdmobunit)
    {
        super(sdmobunit);
    }
/*
    public View getView(Context context)
    {
        if(bannerView == null)
        {
            bannerView = new AdMobSmallBannerView(context);
            ((AdMobSmallBannerView)bannerView).setAdMobSmallBannerListener(new AdMobSmallBannerView.AdMobSmallBannerListener() {

                public void onFailed()
                {
                    if(listener != null)
                        listener.onFailed();
                }

                public void onSuccess()
                {
                    if(listener != null)
                        listener.onSuccess();
                }

            });

            ((AdMobSmallBannerView)bannerView).adMobId = unit.id;
            ((AdMobSmallBannerView)bannerView).setupAdView();
        }
        return bannerView;
    }
    */
}
