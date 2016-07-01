

package streetdirectory.mobile.modules.locationdetail.bus.service;

import streetdirectory.mobile.service.SDHttpService;

// Referenced classes of package streetdirectory.mobile.modules.locationdetail.bus.service:
//            BusArrivalServiceOutputV2, BusArrivalServiceInputV2

public class BusArrivalServiceV2 extends SDHttpService
{

    public BusArrivalServiceV2(BusArrivalServiceInputV2 busarrivalserviceinputv2)
    {
        super(busarrivalserviceinputv2, BusArrivalServiceOutputV2.class);
    }
}
