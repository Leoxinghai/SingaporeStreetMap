

package streetdirectory.mobile.modules.locationdetail.businessin.service;

import streetdirectory.mobile.service.SDHttpService;

// Referenced classes of package streetdirectory.mobile.modules.locationdetail.businessin.service:
//            BusinessInCategoryServiceOutput, BusinessInCategoryServiceInput

public class BusinessInCategoryService extends SDHttpService
{

    public BusinessInCategoryService(BusinessInCategoryServiceInput businessincategoryserviceinput)
    {
        super(businessincategoryserviceinput, BusinessInCategoryServiceOutput.class);
    }
}
