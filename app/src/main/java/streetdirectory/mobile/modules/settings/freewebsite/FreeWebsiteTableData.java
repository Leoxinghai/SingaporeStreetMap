

package streetdirectory.mobile.modules.settings.freewebsite;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.modules.settings.SettingsTableData;

// Referenced classes of package streetdirectory.mobile.modules.settings.freewebsite:
//            FreeWebsiteActivity

public class FreeWebsiteTableData extends SettingsTableData
{

    public FreeWebsiteTableData(Context context)
    {
        super(context);
        mImageIcon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_free_website);
        mTitle = "Free Website";
        mDetail = "Free website for your business!";
    }

    public void execute()
    {
        Intent intent = new Intent(mContext, FreeWebsiteActivity.class);
        mContext.startActivity(intent);
    }
}
