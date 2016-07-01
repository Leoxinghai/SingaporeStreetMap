// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.map.layers;

import streetdirectory.mobile.service.*;

// Referenced classes of package streetdirectory.mobile.modules.map.layers:
//            BuildingVectorServiceOutput

public class BuildingVectorService extends SDHttpService
{

    public BuildingVectorService( final double longitude, final double latitude,final String final_s)
    {
        super(new SDHttpServiceInput() {

            public String getURL()
            {
                return URLFactory.createURLBuildingVectorWithCountryCode(countryCode, longitude, latitude);
            }

        }
, BuildingVectorServiceOutput.class);
    }
}
