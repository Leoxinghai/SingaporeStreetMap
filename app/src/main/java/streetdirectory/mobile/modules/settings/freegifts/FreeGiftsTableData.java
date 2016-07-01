

package streetdirectory.mobile.modules.settings.freegifts;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.modules.settings.SettingsTableData;

// Referenced classes of package streetdirectory.mobile.modules.settings.freegifts:
//            FreeGiftsActivity

public class FreeGiftsTableData extends SettingsTableData
{

    public FreeGiftsTableData(Context context)
    {
        super(context);
        mImageIcon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_freegift);
        mTitle = "Free Gift";
        mDetail = "Claim your free gift";
    }

    public void execute()
    {
        Intent intent = new Intent(mContext, FreeGiftsActivity.class);
        mContext.startActivity(intent);
    }
}
