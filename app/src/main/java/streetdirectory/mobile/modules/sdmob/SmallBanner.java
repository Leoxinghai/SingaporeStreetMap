

package streetdirectory.mobile.modules.sdmob;

import android.content.Context;
import android.view.View;

// Referenced classes of package streetdirectory.mobile.modules.sdmob:
//            SDBanner, AdMobSmallBanner, FacebookSmallBanner

public class SmallBanner extends SDBanner
{
    public static interface AdMobSmallBannerListener
    {

        public abstract void onFailed();

        public abstract void onSuccess();
    }


    public SmallBanner(SdMobHelper.SdMobUnit sdmobunit)
    {
        super(sdmobunit);
    }

    public static SmallBanner getBannerFromSdMobUnit(Context context, SdMobHelper.SdMobUnit sdmobunit)
    {

        switch(sdmobunit.type)
        {
        default:
            return new AdMobSmallBanner(sdmobunit);

        case 1: // '\001'
            return new AdMobSmallBanner(sdmobunit);

        case 2: // '\002'
            return new FacebookSmallBanner(sdmobunit);
        }
    }

    public View getView(Context context)
    {
        return bannerView;
    }

    public void setAdMobSmallBannerListener(AdMobSmallBannerListener admobsmallbannerlistener)
    {
        listener = admobsmallbannerlistener;
    }

    public View bannerView;
    public AdMobSmallBannerListener listener;
}
