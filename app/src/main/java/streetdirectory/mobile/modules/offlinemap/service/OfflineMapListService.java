// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.offlinemap.service;

import streetdirectory.mobile.service.SDHttpService;

// Referenced classes of package streetdirectory.mobile.modules.offlinemap.service:
//            OfflineMapListServiceInput, OfflineMapListServiceOutput

public class OfflineMapListService extends SDHttpService
{

    public OfflineMapListService()
    {
        this(new OfflineMapListServiceInput());
    }

    public OfflineMapListService(OfflineMapListServiceInput offlinemaplistserviceinput)
    {
        super(offlinemaplistserviceinput, OfflineMapListServiceOutput.class);
    }
}
