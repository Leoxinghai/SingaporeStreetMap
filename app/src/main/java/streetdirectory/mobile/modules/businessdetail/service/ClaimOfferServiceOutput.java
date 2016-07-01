

package streetdirectory.mobile.modules.businessdetail.service;

import java.util.HashMap;
import streetdirectory.mobile.service.SDDataOutput;

public class ClaimOfferServiceOutput extends SDDataOutput
{

    public ClaimOfferServiceOutput()
    {
    }

    public void populateData()
    {
        super.populateData();
        result = (String)hashData.get("result");
    }

    private static final long serialVersionUID = 0x7454db74fad87373L;
    public String result;
}
