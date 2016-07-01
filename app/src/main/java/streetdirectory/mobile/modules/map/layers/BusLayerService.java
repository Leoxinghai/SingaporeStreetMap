// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.map.layers;

import streetdirectory.mobile.service.*;

// Referenced classes of package streetdirectory.mobile.modules.map.layers:
//            BusLayerServiceOutput

public class BusLayerService extends SDHttpService
{

    public BusLayerService(final double left, final double top, final double right, final double bottom)
    {
        super(new SDHttpServiceInput() {

            public String getURL()
            {
                return URLFactory.createURLBusStopRectangle(left, top, right, bottom);
            }

        }, BusLayerServiceOutput.class);
    }
}
