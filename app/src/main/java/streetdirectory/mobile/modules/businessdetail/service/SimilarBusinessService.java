// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.businessdetail.service;

import streetdirectory.mobile.service.SDHttpService;

// Referenced classes of package streetdirectory.mobile.modules.businessdetail.service:
//            SimilarBusinessServiceOutput, SimilarBusinessServiceInput

public class SimilarBusinessService extends SDHttpService
{

    public SimilarBusinessService(SimilarBusinessServiceInput similarbusinessserviceinput)
    {
        super(similarbusinessserviceinput, SimilarBusinessServiceOutput.class);
    }
}
