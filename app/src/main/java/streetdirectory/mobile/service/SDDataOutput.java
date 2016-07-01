

package streetdirectory.mobile.service;

import java.util.HashMap;

// Referenced classes of package streetdirectory.mobile.service:
//            SDOutput

public class SDDataOutput extends SDOutput
{

    public SDDataOutput()
    {
    }

    public SDDataOutput(HashMap hashmap)
    {
        super(hashmap);
    }

    public void populateData()
    {
        super.populateData();
        countryCode = (String)hashData.get("country");
    }

    public static final SDOutput.Creator CREATOR = new SDOutput.Creator(SDDataOutput.class);
    private static final long serialVersionUID = 0xe86f06db3b07797dL;
    public String countryCode;

}
