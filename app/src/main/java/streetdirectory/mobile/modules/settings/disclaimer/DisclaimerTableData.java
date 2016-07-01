

package streetdirectory.mobile.modules.settings.disclaimer;

import android.content.Context;
import android.content.Intent;
import streetdirectory.mobile.modules.settings.SettingsTableData;

// Referenced classes of package streetdirectory.mobile.modules.settings.disclaimer:
//            DisclaimerActivity

public class DisclaimerTableData extends SettingsTableData
{

    public DisclaimerTableData(Context context)
    {
        super(context);
        mTitle = "Disclaimer & Terms of Use";
    }

    public void execute()
    {
        Intent intent = new Intent(mContext, DisclaimerActivity.class);
        mContext.startActivity(intent);
    }
}
