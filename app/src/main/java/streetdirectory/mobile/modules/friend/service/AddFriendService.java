// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.friend.service;

import streetdirectory.mobile.service.SDHttpService;

// Referenced classes of package streetdirectory.mobile.modules.friend.service:
//            AddFriendServiceOutput, AddFriendServiceInput

public class AddFriendService extends SDHttpService
{

    public AddFriendService(AddFriendServiceInput addfriendserviceinput)
    {
        super(addfriendserviceinput, AddFriendServiceOutput.class);
    }
}
