

package streetdirectory.mobile.modules.offlinemap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.core.ui.sidemenu.SDSideMenuBasicItem;
import streetdirectory.mobile.core.ui.sidemenu.SideMenuLayout;

// Referenced classes of package streetdirectory.mobile.modules.offlinemap:
//            OfflineMapActivity

public class OfflineMapMenuItem extends SDSideMenuBasicItem
{

    public OfflineMapMenuItem()
    {
        super("Download Offline Maps", R.drawable.menu_dl_black, R.drawable.menu_dl);
    }

    public void execute(Context context, SideMenuLayout sidemenulayout)
    {
        if(context instanceof OfflineMapActivity)
        {
            sidemenulayout.slideClose();
            return;
        } else
        {
            context.startActivity(new Intent(context, OfflineMapActivity.class));
            ((Activity)context).finish();
            return;
        }
    }
}
