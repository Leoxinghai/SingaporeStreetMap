// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.tips.reply.service;

import streetdirectory.mobile.service.SDHttpService;

// Referenced classes of package streetdirectory.mobile.modules.tips.reply.service:
//            TipsReplyServiceOutput, TipsReplyServiceInput

public class TipsReplyService extends SDHttpService
{

    public TipsReplyService(TipsReplyServiceInput tipsreplyserviceinput)
    {
        super(tipsreplyserviceinput, TipsReplyServiceOutput.class);
    }
}
