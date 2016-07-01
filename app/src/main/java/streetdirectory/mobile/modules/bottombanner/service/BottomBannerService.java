

package streetdirectory.mobile.modules.bottombanner.service;

import streetdirectory.mobile.service.SDHttpService;

// Referenced classes of package streetdirectory.mobile.modules.bottombanner.service:
//            BottomBannerServiceOutput, BottomBannerServiceInput

public class BottomBannerService extends SDHttpService
{

    public BottomBannerService(BottomBannerServiceInput bottombannerserviceinput)
    {
        super(bottombannerserviceinput, BottomBannerServiceOutput.class);
    }
}
