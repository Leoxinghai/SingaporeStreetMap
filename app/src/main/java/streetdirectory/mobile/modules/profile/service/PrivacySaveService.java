

package streetdirectory.mobile.modules.profile.service;

import streetdirectory.mobile.service.SDHttpService;

// Referenced classes of package streetdirectory.mobile.modules.profile.service:
//            PrivacySaveServiceOutput, PrivacySaveServiceInput

public class PrivacySaveService extends SDHttpService
{

    public PrivacySaveService(PrivacySaveServiceInput privacysaveserviceinput)
    {
        super(privacysaveserviceinput, PrivacySaveServiceOutput.class);
    }
}
