// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.settings.sendfeedback;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.modules.settings.SettingsTableData;

// Referenced classes of package streetdirectory.mobile.modules.settings.sendfeedback:
//            SendFeedbackActivity

public class SendFeedbackTableData extends SettingsTableData
{

    public SendFeedbackTableData(Context context)
    {
        super(context);
        mImageIcon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_feedback);
        mTitle = "Send Feedback";
    }

    public void execute()
    {
        Intent intent = new Intent(mContext, SendFeedbackActivity.class);
        mContext.startActivity(intent);
    }
}
