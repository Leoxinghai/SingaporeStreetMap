

package streetdirectory.mobile.service;

import java.util.HashMap;

// Referenced classes of package streetdirectory.mobile.service:
//            SDDataOutput

public class SDResponseOutput extends SDDataOutput
{

    public SDResponseOutput()
    {
    }

    public void populateData()
    {
        super.populateData();
        isSuccess = Boolean.valueOf("1".equals(hashData.get("data")));
    }

    private static final long serialVersionUID = 0x9ab1af730b40b4c2L;
    public Boolean isSuccess;
}
