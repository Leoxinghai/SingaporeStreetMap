// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.settings.sendfeedback;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.xinghai.mycurve.R;

import streetdirectory.mobile.core.ui.sidemenu.SDSideMenuBasicItem;
import streetdirectory.mobile.core.ui.sidemenu.SideMenuLayout;

// Referenced classes of package streetdirectory.mobile.modules.settings.sendfeedback:
//            SendFeedbackActivity

public class SendFeedBackMenuItem extends SDSideMenuBasicItem
{

    public SendFeedBackMenuItem()
    {
        super("Send Feedback", R.drawable.menu_feedback_black, R.drawable.menu_directions);
    }

    public void execute(Context context, SideMenuLayout sidemenulayout)
    {
        if(context instanceof SendFeedbackActivity)
        {
            sidemenulayout.slideClose();
            return;
        } else
        {
            context.startActivity(new Intent(context, SendFeedbackActivity.class));
            ((Activity)context).finish();
            return;
        }
    }
}
