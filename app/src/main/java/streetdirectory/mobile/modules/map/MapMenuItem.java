

package streetdirectory.mobile.modules.map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.xinghai.mycurve.R;

import java.util.Date;
import streetdirectory.mobile.SDApplication;
import streetdirectory.mobile.core.SDPreferences;
import streetdirectory.mobile.core.ui.sidemenu.SDSideMenuBasicItem;
import streetdirectory.mobile.core.ui.sidemenu.SideMenuLayout;

// Referenced classes of package streetdirectory.mobile.modules.map:
//            MapActivity

public class MapMenuItem extends SDSideMenuBasicItem
{

    public MapMenuItem()
    {
        super("Map", R.drawable.menu_map_black, R.drawable.menu_map);
    }

    public void execute(Context context, SideMenuLayout sidemenulayout)
    {
        if(context instanceof MapActivity)
        {
            sidemenulayout.slideClose();
        } else
        {
            context.startActivity(new Intent(context, MapActivity.class));
            ((Activity)context).finish();
            int i = SDPreferences.getInstance().getInterstitialAdInterval();
            long l = SDPreferences.getInstance().getInterstitialAdLastShow();
            if((int)((new Date()).getTime() / 60000L - l / 60000L) >= i)
            {
                SDApplication.showInterstitialAd();
                return;
            }
        }
    }
}
