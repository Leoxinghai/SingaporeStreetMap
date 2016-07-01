// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.locationdetail.trafficcam.service;

import streetdirectory.mobile.service.SDHttpService;

// Referenced classes of package streetdirectory.mobile.modules.locationdetail.trafficcam.service:
//            TrafficCameraImageLinkServiceOutput, TrafficCameraImageLinkServiceInput

public class TrafficCameraImageLinkService extends SDHttpService
{

    public TrafficCameraImageLinkService(TrafficCameraImageLinkServiceInput trafficcameraimagelinkserviceinput)
    {
        super(trafficcameraimagelinkserviceinput, TrafficCameraImageLinkServiceOutput.class);
    }
}
