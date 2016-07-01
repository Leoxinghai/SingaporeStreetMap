

package streetdirectory.mobile.modules.businesslisting.service;

import java.util.HashMap;
import streetdirectory.mobile.core.StringTools;
import streetdirectory.mobile.service.SDDataOutput;

public class OffersCategoryListServiceOutput extends SDDataOutput
{

    public OffersCategoryListServiceOutput()
    {
    }

    public void populateData()
    {
        populateData();
        parentCategoryId = StringTools.tryParseInt((String)hashData.get("pcid"), -1);
        total = StringTools.tryParseInt((String)hashData.get("tot"), -1);
        parentCategoryName = (String)hashData.get("pcnm");
    }

    private static final long serialVersionUID = 0xe18f824b6d9ee3bbL;
    public int parentCategoryId;
    public String parentCategoryName;
    public int total;
}
