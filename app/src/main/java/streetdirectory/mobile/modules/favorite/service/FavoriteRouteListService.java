// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.favorite.service;

import streetdirectory.mobile.service.SDHttpService;

// Referenced classes of package streetdirectory.mobile.modules.favorite.service:
//            FavoriteRouteListServiceOutput, FavoriteRouteListServiceInput

public class FavoriteRouteListService extends SDHttpService
{

    public FavoriteRouteListService(FavoriteRouteListServiceInput favoriteroutelistserviceinput)
    {
        super(favoriteroutelistserviceinput, FavoriteRouteListServiceOutput.class);
    }
}
