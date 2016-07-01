// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.tips.reply.service;

import streetdirectory.mobile.service.SDHttpService;

// Referenced classes of package streetdirectory.mobile.modules.tips.reply.service:
//            PostReplyTipsServiceOutput, PostReplyTipsServiceInput

public class PostReplyTipsService extends SDHttpService
{

    public PostReplyTipsService(PostReplyTipsServiceInput postreplytipsserviceinput)
    {
        super(postreplytipsserviceinput, PostReplyTipsServiceOutput.class);
    }
}
