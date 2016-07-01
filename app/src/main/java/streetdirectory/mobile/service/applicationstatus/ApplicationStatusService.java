

package streetdirectory.mobile.service.applicationstatus;

import streetdirectory.mobile.service.SDHttpService;

// Referenced classes of package streetdirectory.mobile.service.applicationstatus:
//            ApplicationStatusServiceOutput, ApplicationStatusServiceInput

public class ApplicationStatusService extends SDHttpService
{

    public ApplicationStatusService(ApplicationStatusServiceInput applicationstatusserviceinput)
    {
        super(applicationstatusserviceinput, ApplicationStatusServiceOutput.class);
    }
}
