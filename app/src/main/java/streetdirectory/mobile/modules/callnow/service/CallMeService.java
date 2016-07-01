// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.callnow.service;

import streetdirectory.mobile.service.SDHttpService;

// Referenced classes of package streetdirectory.mobile.modules.callnow.service:
//            CallMeServiceOutput, CallMeServiceInput

public class CallMeService extends SDHttpService
{

    public CallMeService(CallMeServiceInput callmeserviceinput)
    {
        super(callmeserviceinput, CallMeServiceOutput.class);
    }
}
