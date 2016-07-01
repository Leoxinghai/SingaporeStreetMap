

package streetdirectory.mobile.modules.settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.core.ui.sidemenu.SDSideMenuBasicItem;
import streetdirectory.mobile.core.ui.sidemenu.SideMenuLayout;

// Referenced classes of package streetdirectory.mobile.modules.settings:
//            SettingsActivity

public class SettingsMenuItem extends SDSideMenuBasicItem
{

    public SettingsMenuItem()
    {
        super("About", R.drawable.menu_settings_black, R.drawable.menu_settings);
    }

    public void execute(Context context, SideMenuLayout sidemenulayout)
    {
        if(context instanceof SettingsActivity)
        {
            sidemenulayout.slideClose();
            return;
        } else
        {
            context.startActivity(new Intent(context, SettingsActivity.class));
            ((Activity)context).finish();
            return;
        }
    }
}
