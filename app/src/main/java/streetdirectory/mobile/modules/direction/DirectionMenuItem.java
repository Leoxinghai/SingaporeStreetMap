

package streetdirectory.mobile.modules.direction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.core.ui.sidemenu.SDSideMenuBasicItem;
import streetdirectory.mobile.core.ui.sidemenu.SideMenuLayout;
import streetdirectory.mobile.sd.SDBlackboard;

// Referenced classes of package streetdirectory.mobile.modules.direction:
//            DirectionActivity

public class DirectionMenuItem extends SDSideMenuBasicItem
{

    public DirectionMenuItem()
    {
        super("Direction", R.drawable.menu_directions_black, R.drawable.menu_directions);
    }

    public void execute(Context context, SideMenuLayout sidemenulayout)
    {
        if(context instanceof DirectionActivity)
        {
            sidemenulayout.slideClose();
            return;
        } else
        {
            Intent intent = new Intent(context, DirectionActivity.class);
            intent.putExtra("longitude", SDBlackboard.currentLongitude);
            intent.putExtra("latitude", SDBlackboard.currentLatitude);
            intent.putExtra("countryCode", SDBlackboard.currentCountryCode);
            context.startActivity(intent);
            ((Activity)context).finish();
            return;
        }
    }
}
