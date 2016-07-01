

package streetdirectory.mobile.modules.settings;

import android.content.Context;

import com.xinghai.mycurve.R;

// Referenced classes of package streetdirectory.mobile.modules.settings:
//            SettingsTableData

public class AutoLockTableData extends SettingsTableData
{

    public AutoLockTableData(Context context)
    {
        super(context);
        mTitle = String.format(context.getString(R.string.cell_auto_lock_title_label), new Object[0]);
        mDetail = String.format(context.getString(R.string.cell_auto_lock_detail_label), new Object[0]);
    }
}
