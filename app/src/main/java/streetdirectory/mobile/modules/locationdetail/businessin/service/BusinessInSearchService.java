

package streetdirectory.mobile.modules.locationdetail.businessin.service;

import streetdirectory.mobile.service.SDHttpService;

// Referenced classes of package streetdirectory.mobile.modules.locationdetail.businessin.service:
//            BusinessInSearchServiceOutput, BusinessInSearchServiceInput

public class BusinessInSearchService extends SDHttpService
{

    public BusinessInSearchService(BusinessInSearchServiceInput businessinsearchserviceinput)
    {
        super(businessinsearchserviceinput, BusinessInSearchServiceOutput.class);
    }
}
