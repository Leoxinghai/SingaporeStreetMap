

package streetdirectory.mobile.modules.sdmob;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.modules.offlinemap.OfflineMapActivity;

public class OfflineMapSmallBannerView extends RelativeLayout
{

    public OfflineMapSmallBannerView(Context context)
    {
        super(context);
        init();
    }

    public OfflineMapSmallBannerView(Context context, AttributeSet attributeset)
    {
        super(context, attributeset);
        init();
    }

    public OfflineMapSmallBannerView(Context context, AttributeSet attributeset, int i)
    {
        super(context, attributeset, i);
        init();
    }

    private void init()
    {
        inflate(getContext(), R.layout.layout_sdmob_offline_map, this);
        setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View view)
            {
                getContext().startActivity(new Intent(getContext(), OfflineMapActivity.class));
            }

        });
    }
}
