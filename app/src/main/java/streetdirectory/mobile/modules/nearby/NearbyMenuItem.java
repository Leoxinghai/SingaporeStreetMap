

package streetdirectory.mobile.modules.nearby;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.core.ui.sidemenu.SDSideMenuBasicItem;
import streetdirectory.mobile.core.ui.sidemenu.SideMenuLayout;
import streetdirectory.mobile.sd.SDBlackboard;

// Referenced classes of package streetdirectory.mobile.modules.nearby:
//            NearbyActivity

public class NearbyMenuItem extends SDSideMenuBasicItem
{

    public NearbyMenuItem()
    {
        super("Nearby", R.drawable.menu_nearby_black, R.drawable.menu_nearby);
    }

    public void execute(Context context, SideMenuLayout sidemenulayout)
    {
        if(context instanceof NearbyActivity)
        {
            sidemenulayout.slideClose();
            return;
        } else
        {
            Intent intent = new Intent(context, NearbyActivity.class);
            intent.putExtra("longitude", SDBlackboard.currentLongitude);
            intent.putExtra("latitude", SDBlackboard.currentLatitude);
            intent.putExtra("countryCode", SDBlackboard.currentCountryCode);
            context.startActivity(intent);
            ((Activity)context).finish();
            return;
        }
    }
}
