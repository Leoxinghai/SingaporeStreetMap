

package streetdirectory.mobile.modules.settings;

import android.content.Context;
import android.graphics.Bitmap;

public class SettingsTableData
{

    public SettingsTableData(Context context)
    {
        mContext = context;
    }

    public void execute()
    {
    }

    protected Context mContext;
    public String mDetail;
    public Bitmap mImageIcon;
    public String mTitle;
}
