

package streetdirectory.mobile.modules.location.service;

import streetdirectory.mobile.service.SDHttpService;

// Referenced classes of package streetdirectory.mobile.modules.location.service:
//            CurrentLocationServiceOutput, CurrentLocationServiceInput

public class CurrentLocationService extends SDHttpService
{

    public CurrentLocationService(CurrentLocationServiceInput currentlocationserviceinput)
    {
        super(currentlocationserviceinput, CurrentLocationServiceOutput.class);
    }
}
