// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.friend.service;

import streetdirectory.mobile.service.SDHttpService;

// Referenced classes of package streetdirectory.mobile.modules.friend.service:
//            SuggestedFriendListServiceOutput, SuggestedFriendListServiceInput

public class SuggestedFriendListService extends SDHttpService
{

    public SuggestedFriendListService(SuggestedFriendListServiceInput suggestedfriendlistserviceinput)
    {
        super(suggestedfriendlistserviceinput, SuggestedFriendListServiceOutput.class);
    }
}
