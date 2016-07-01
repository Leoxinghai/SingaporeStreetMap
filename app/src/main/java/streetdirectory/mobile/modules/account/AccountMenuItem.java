

package streetdirectory.mobile.modules.account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.core.ui.sidemenu.SDSideMenuBasicItem;
import streetdirectory.mobile.core.ui.sidemenu.SideMenuLayout;
import streetdirectory.mobile.sd.SDBlackboard;

// Referenced classes of package streetdirectory.mobile.modules.account:
//            AccountActivity

public class AccountMenuItem extends SDSideMenuBasicItem
{

    public AccountMenuItem()
    {
        super("My Inbox & Fren's", R.drawable.menu_acc_black, R.drawable.menu_acc);
    }

    public void execute(Context context, SideMenuLayout sidemenulayout)
    {
        if(context instanceof AccountActivity)
        {
            sidemenulayout.slideClose();
            return;
        } else
        {
            Intent intent = new Intent(context, AccountActivity.class);
            intent.putExtra("countryCode", SDBlackboard.currentCountryCode);
            intent.putExtra("uid", "100002297910526");
            intent.putExtra("username", "Miku Mayumi");
            context.startActivity(intent);
            ((Activity)context).finish();
            return;
        }
    }
}
