

package streetdirectory.mobile.modules.locationdetail.businessin.service;

import streetdirectory.mobile.service.SDHttpService;

// Referenced classes of package streetdirectory.mobile.modules.locationdetail.businessin.service:
//            BusinessInServiceOutput, BusinessInServiceInput

public class BusinessInService extends SDHttpService
{

    public BusinessInService(BusinessInServiceInput businessinserviceinput)
    {
        super(businessinserviceinput, BusinessInServiceOutput.class);
    }
}
