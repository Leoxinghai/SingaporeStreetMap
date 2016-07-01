

package streetdirectory.mobile.modules.businesslisting.service;

import streetdirectory.mobile.service.SDHttpService;

// Referenced classes of package streetdirectory.mobile.modules.businesslisting.service:
//            BusinessListingServiceOutput, BusinessListingServiceInput

public class BusinessListingService extends SDHttpService
{

    public BusinessListingService(BusinessListingServiceInput businesslistingserviceinput)
    {
        super(businesslistingserviceinput, BusinessListingServiceOutput.class);
    }
}
