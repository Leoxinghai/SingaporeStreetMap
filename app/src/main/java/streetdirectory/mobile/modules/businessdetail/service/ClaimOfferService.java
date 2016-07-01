// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.businessdetail.service;

import streetdirectory.mobile.service.*;

// Referenced classes of package streetdirectory.mobile.modules.businessdetail.service:
//            ClaimOfferServiceOutput

public class ClaimOfferService extends SDHttpService
{

    public ClaimOfferService(final int companyID, final int locationID, final String offerId, final String contactInfo, final boolean isSubmitMethodSms)
    {
        super(new SDHttpServiceInput() {

            public String getURL()
            {
                if(isSubmitMethodSms)
                    return URLFactory.createURLSendSmsOffer(countryCode, companyID, locationID, offerId, contactInfo);
                else
                    return URLFactory.createURLSendEmailOffer(countryCode, companyID, locationID, offerId, contactInfo);
            }

        }, ClaimOfferServiceOutput.class);
    }
}
