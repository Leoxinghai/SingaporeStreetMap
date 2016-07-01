

package streetdirectory.mobile.modules.sdmob;

import android.content.Context;

// Referenced classes of package streetdirectory.mobile.modules.sdmob:
//            SDBanner, AdMobFullScreenBanner, FacebookFullScreenBanner

public class FullScreenBanner extends SDBanner
{
    public static interface FullScreenBannerListener
    {

        public abstract void onAdClosed();

        public abstract void onAdFailed();

        public abstract void onAdLoaded();
    }


    public FullScreenBanner(SdMobHelper.SdMobUnit sdmobunit)
    {
        super(sdmobunit);
    }

    public static FullScreenBanner getBannerFromSdMobUnit(SdMobHelper.SdMobUnit sdmobunit)
    {
        switch(sdmobunit.type)
        {
        default:
            return new AdMobFullScreenBanner(sdmobunit);

        case 1: // '\001'
            return new AdMobFullScreenBanner(sdmobunit);

        case 2: // '\002'
            return new FacebookFullScreenBanner(sdmobunit);
        }
    }

    public void presentView(Context context)
    {
    }

    public void setFullScreenBannerListener(FullScreenBannerListener fullscreenbannerlistener)
    {
        listener = fullscreenbannerlistener;
    }

    public FullScreenBannerListener listener;
}
