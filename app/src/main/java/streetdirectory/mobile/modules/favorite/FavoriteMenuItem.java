

package streetdirectory.mobile.modules.favorite;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.core.ui.sidemenu.SDSideMenuBasicItem;
import streetdirectory.mobile.core.ui.sidemenu.SideMenuLayout;
import streetdirectory.mobile.sd.SDBlackboard;

// Referenced classes of package streetdirectory.mobile.modules.favorite:
//            FavoriteActivity

public class FavoriteMenuItem extends SDSideMenuBasicItem
{

    public FavoriteMenuItem()
    {
        super("My Save(s)", R.drawable.menu_fav_black, R.drawable.menu_fav);
    }

    public void execute(Context context, SideMenuLayout sidemenulayout)
    {
        if(context instanceof FavoriteActivity)
        {
            sidemenulayout.slideClose();
            return;
        } else
        {
            Intent intent = new Intent(context, FavoriteActivity.class);
            intent.putExtra("countryCode", SDBlackboard.currentCountryCode);
            context.startActivity(intent);
            ((Activity)context).finish();
            return;
        }
    }
}
