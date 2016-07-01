

package streetdirectory.mobile.modules.businesslisting.service;

import streetdirectory.mobile.service.*;

// Referenced classes of package streetdirectory.mobile.modules.businesslisting.service:
//            OffersCategoryListServiceOutput

public class OffersCategoryListService extends SDHttpService
{

    public OffersCategoryListService(String s)
    {
        super(new SDHttpServiceInput(s) {

            public String getURL()
            {
                return URLFactory.createURLOfferCategoryListing(countryCode);
            }

        }, OffersCategoryListServiceOutput.class);
    }
}
