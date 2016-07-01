// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.settings.addbusiness.service;

import streetdirectory.mobile.service.SDHttpService;

// Referenced classes of package streetdirectory.mobile.modules.settings.addbusiness.service:
//            AddNewPlaceServiceOutput, AddNewPlaceServiceInput

public class AddNewPlaceService extends SDHttpService
{

    public AddNewPlaceService(AddNewPlaceServiceInput addnewplaceserviceinput)
    {
        super(addnewplaceserviceinput, AddNewPlaceServiceOutput.class);
    }
}
