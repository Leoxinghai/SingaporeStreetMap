

package streetdirectory.mobile.modules.businessfinder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.core.ui.sidemenu.SDSideMenuBasicItem;
import streetdirectory.mobile.core.ui.sidemenu.SideMenuLayout;
import streetdirectory.mobile.gis.GeoSense;
import streetdirectory.mobile.gis.SdArea;
import streetdirectory.mobile.sd.SDBlackboard;

// Referenced classes of package streetdirectory.mobile.modules.businessfinder:
//            BusinessFinderActivityTwo

public class BusinessFinderMenuItem extends SDSideMenuBasicItem
{

    public BusinessFinderMenuItem()
    {
        super("Directory", R.drawable.menu_biz_black, R.drawable.menu_biz);
    }

    public void execute(Context context, SideMenuLayout sidemenulayout)
    {
        if(context instanceof BusinessFinderActivityTwo)
        {
            sidemenulayout.slideClose();
            return;
        } else
        {
            Intent intent = new Intent(context, BusinessFinderActivityTwo.class);
            intent.putExtra("countryCode", SDBlackboard.currentCountryCode);
            intent.putExtra("countryName", GeoSense.getArea(SDBlackboard.currentCountryCode).areaName);
            context.startActivity(intent);
            ((Activity)context).finish();
            return;
        }
    }
}
