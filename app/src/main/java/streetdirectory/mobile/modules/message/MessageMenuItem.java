

package streetdirectory.mobile.modules.message;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.core.ui.sidemenu.SDSideMenuBasicItem;
import streetdirectory.mobile.core.ui.sidemenu.SideMenuLayout;
import streetdirectory.mobile.sd.SDBlackboard;

// Referenced classes of package streetdirectory.mobile.modules.message:
//            MessageActivity

public class MessageMenuItem extends SDSideMenuBasicItem
{

    public MessageMenuItem()
    {
        super("Inbox and Friends", R.drawable.menu_settings_black, R.drawable.menu_settings);
    }

    public void execute(Context context, SideMenuLayout sidemenulayout)
    {
        if(context instanceof MessageActivity)
        {
            sidemenulayout.slideClose();
            return;
        } else
        {
            Intent intent = new Intent(context, MessageActivity.class);
            intent.putExtra("countryCode", SDBlackboard.currentCountryCode);
            context.startActivity(intent);
            ((Activity)context).finish();
            return;
        }
    }
}
