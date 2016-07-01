// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.friend.service;

import streetdirectory.mobile.service.SDHttpService;

// Referenced classes of package streetdirectory.mobile.modules.friend.service:
//            RequestFriendListServiceOutput, RequestFriendListServiceInput

public class RequestFriendListService extends SDHttpService
{

    public RequestFriendListService(RequestFriendListServiceInput requestfriendlistserviceinput)
    {
        super(requestfriendlistserviceinput, RequestFriendListServiceOutput.class);
    }
}
