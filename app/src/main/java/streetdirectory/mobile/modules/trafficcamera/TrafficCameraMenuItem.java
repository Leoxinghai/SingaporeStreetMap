

package streetdirectory.mobile.modules.trafficcamera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.core.ui.sidemenu.SDSideMenuBasicItem;
import streetdirectory.mobile.core.ui.sidemenu.SideMenuLayout;
import streetdirectory.mobile.sd.SDBlackboard;

// Referenced classes of package streetdirectory.mobile.modules.trafficcamera:
//            TrafficCameraActivity

public class TrafficCameraMenuItem extends SDSideMenuBasicItem
{

    public TrafficCameraMenuItem()
    {
        super("Traffic Camera", R.drawable.menu_cam_black, R.drawable.menu_cam);
    }

    public void execute(Context context, SideMenuLayout sidemenulayout)
    {
        if(context instanceof TrafficCameraActivity)
        {
            sidemenulayout.slideClose();
            return;
        } else
        {
            Intent intent = new Intent(context,TrafficCameraActivity.class);
            intent.putExtra("countryCode", SDBlackboard.currentCountryCode);
            context.startActivity(intent);
            ((Activity)context).finish();
            return;
        }
    }
}
