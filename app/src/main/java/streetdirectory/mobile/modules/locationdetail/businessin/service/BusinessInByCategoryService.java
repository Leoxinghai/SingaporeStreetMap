

package streetdirectory.mobile.modules.locationdetail.businessin.service;

import streetdirectory.mobile.service.SDHttpService;

// Referenced classes of package streetdirectory.mobile.modules.locationdetail.businessin.service:
//            BusinessInByCategoryServiceOutput, BusinessInByCategoryServiceInput

public class BusinessInByCategoryService extends SDHttpService
{

    public BusinessInByCategoryService(BusinessInByCategoryServiceInput businessinbycategoryserviceinput)
    {
        super(businessinbycategoryserviceinput, BusinessInByCategoryServiceOutput.class);
    }
}
