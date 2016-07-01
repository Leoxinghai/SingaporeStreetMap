

package streetdirectory.mobile.modules.settings.addbusiness;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.modules.settings.SettingsTableData;

// Referenced classes of package streetdirectory.mobile.modules.settings.addbusiness:
//            AddBusinessActivity

public class AddBusinessTableData extends SettingsTableData
{

    public AddBusinessTableData(Context context)
    {
        super(context);
        mImageIcon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_add_business);
        mTitle = "Add new business";
        mDetail = "Submit a photo of your namecard and we will add your business. Free!";
    }

    public void execute()
    {
        Intent intent = new Intent(mContext, AddBusinessActivity.class);
        mContext.startActivity(intent);
    }
}
