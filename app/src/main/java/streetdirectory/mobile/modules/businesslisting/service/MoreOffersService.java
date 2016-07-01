

package streetdirectory.mobile.modules.businesslisting.service;

import streetdirectory.mobile.modules.businessdetail.MoreOffersServiceOutput;
import streetdirectory.mobile.service.*;

public class MoreOffersService extends SDHttpService
{

    public MoreOffersService(final int companyId, final String countryCode)
    {
        super(new SDHttpServiceInput() {

            public String getURL()
            {
                return URLFactory.createURLMoreOffers(companyId, countryCode);
            }
        }, MoreOffersServiceOutput.class);
    }
}
