

package streetdirectory.mobile.modules.settings.freemaps;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.modules.settings.SettingsTableData;

// Referenced classes of package streetdirectory.mobile.modules.settings.freemaps:
//            FreeMapsActivity

public class FreeMapsTableData extends SettingsTableData
{

    public FreeMapsTableData(Context context)
    {
        super(context);
        mImageIcon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_mr_sd);
        mTitle = "Free Maps";
        mDetail = "For websites, blogs, and invites.";
    }

    public void execute()
    {
        Intent intent = new Intent(mContext, FreeMapsActivity.class);
        mContext.startActivity(intent);
    }
}
