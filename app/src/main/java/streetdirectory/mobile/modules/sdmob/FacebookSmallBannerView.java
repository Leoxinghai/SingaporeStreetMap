

package streetdirectory.mobile.modules.sdmob;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.core.StringTools;

public class FacebookSmallBannerView extends RelativeLayout
{

    public FacebookSmallBannerView(Context context)
    {
        super(context);
        init();
    }

    public FacebookSmallBannerView(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
        init();
    }

    public FacebookSmallBannerView(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        init();
    }

    public void init()
    {
        inflate(getContext(), R.layout.layout_sdmob_admob, this);
    }

    public void setupAdView()
    {
        if(StringTools.isStringEmpty(placementId));
    }

    public String placementId;
}
