// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.settings.sendfeedback.service;

import streetdirectory.mobile.service.SDHttpService;

// Referenced classes of package streetdirectory.mobile.modules.settings.sendfeedback.service:
//            SendFeedbackServiceOutput, SendFeedbackServiceInput

public class SendFeedbackService extends SDHttpService
{

    public SendFeedbackService(SendFeedbackServiceInput sendfeedbackserviceinput)
    {
        super(sendfeedbackserviceinput, SendFeedbackServiceOutput.class);
    }
}
