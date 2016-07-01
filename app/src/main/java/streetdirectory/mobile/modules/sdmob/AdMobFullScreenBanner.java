// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.sdmob;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import com.google.android.gms.ads.AdListener;
//import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import streetdirectory.mobile.SDApplication;

// Referenced classes of package streetdirectory.mobile.modules.sdmob:
//            FullScreenBanner

public class AdMobFullScreenBanner extends FullScreenBanner
{

    public AdMobFullScreenBanner(SdMobHelper.SdMobUnit sdmobunit)
    {
        super(sdmobunit);
    }

    public void presentView(final Context context)
    {
        final InterstitialAd interstitialAd = SDApplication.getInterstitialAd();
        if(interstitialAd != null)
        {
            interstitialAd.setAdListener(new AdListener() {

                public void onAdClosed()
                {
                    onAdClosed();
                    if(listener != null)
                        listener.onAdClosed();
                }

                public void onAdFailedToLoad(int i)
                {
                    onAdFailedToLoad(i);
                    Log.d("SD", (new StringBuilder()).append("onAdFailedToLoad() called with: errorCode = [").append(i).append("]").toString());
                    if(listener != null)
                        listener.onAdFailed();
                }

                public void onAdLoaded()
                {
                    onAdLoaded();
                    interstitialAd.show();
                    if(listener != null)
                        listener.onAdLoaded();
                }
            });
            if(!interstitialAd.isLoaded())
                interstitialAd.loadAd((new AdRequest.Builder()).build());
            interstitialAd.show();
            if(listener != null)
                listener.onAdLoaded();
        }
        return;
    }
}
