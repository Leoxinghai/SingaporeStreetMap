

package streetdirectory.mobile.modules.businessdetail.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class BusinessOfferVoteServiceInput extends SDHttpServiceInput
{

    public BusinessOfferVoteServiceInput()
    {
    }

    public BusinessOfferVoteServiceInput(String s, int i, String s1, String s2, int j)
    {
        super(s);
        companyID = i;
        sdUid = s2;
        promoId = s1;
        vote = j;
    }

    public String getURL()
    {
        return URLFactory.createURLOfferVote(countryCode, companyID, promoId, sdUid, vote);
    }

    public int companyID;
    public String promoId;
    public String sdUid;
    public int vote;
}
