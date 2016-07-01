

package streetdirectory.mobile.modules.businesslisting.service;

import streetdirectory.mobile.service.*;

// Referenced classes of package streetdirectory.mobile.modules.businesslisting.service:
//            BusinessListingServiceOutput

public class OffersListingService extends SDHttpService
{

    public OffersListingService(final int categoryId, final int start, final int limit, final String final_s)
    {
        super(new SDHttpServiceInput() {

            public String getURL()
            {
                if(categoryId == -1)
                    return URLFactory.createURLOffersListing(start, limit, countryCode);
                else
                    return URLFactory.createURLOffersListing(start, limit, categoryId, countryCode);
            }

        }, BusinessListingServiceOutput.class);
    }
}
