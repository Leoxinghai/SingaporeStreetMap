// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.gis.gps;

import streetdirectory.mobile.service.SDDataOutput;
import streetdirectory.mobile.service.SDHttpService;

// Referenced classes of package streetdirectory.mobile.gis.gps:
//            NearbyLocServiceInput

public class NearbyLocService extends SDHttpService
{

    public NearbyLocService(NearbyLocServiceInput nearbylocserviceinput)
    {
        super(nearbylocserviceinput, SDDataOutput.class);
    }
}
