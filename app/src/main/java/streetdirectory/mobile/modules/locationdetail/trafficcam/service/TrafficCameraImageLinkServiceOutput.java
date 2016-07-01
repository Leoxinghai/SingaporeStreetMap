// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.locationdetail.trafficcam.service;

import java.util.HashMap;
import streetdirectory.mobile.service.SDDataOutput;

public class TrafficCameraImageLinkServiceOutput extends SDDataOutput
{

    public TrafficCameraImageLinkServiceOutput()
    {
    }

    public TrafficCameraImageLinkServiceOutput(HashMap hashmap)
    {
        super(hashmap);
    }

    public void populateData()
    {
        super.populateData();
        imageURL = (String)hashData.get("img");
    }

    public static final streetdirectory.mobile.service.SDOutput.Creator CREATOR = new streetdirectory.mobile.service.SDOutput.Creator(TrafficCameraImageLinkServiceOutput.class);
    private static final long serialVersionUID = 0xb3592ba9a8fee960L;
    public String imageURL;

}
