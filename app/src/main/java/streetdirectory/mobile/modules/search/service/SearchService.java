// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.search.service;

import streetdirectory.mobile.service.SDHttpService;

// Referenced classes of package streetdirectory.mobile.modules.search.service:
//            SearchServiceOutput, SearchServiceInput

public class SearchService extends SDHttpService
{

    public SearchService(SearchServiceInput searchserviceinput)
    {
        super(searchserviceinput, SearchServiceOutput.class);
    }
}
