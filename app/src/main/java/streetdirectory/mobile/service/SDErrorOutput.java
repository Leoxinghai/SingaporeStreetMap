

package streetdirectory.mobile.service;

import java.util.HashMap;

// Referenced classes of package streetdirectory.mobile.service:
//            SDOutput

public class SDErrorOutput extends SDOutput
{

    public SDErrorOutput()
    {
        this(null, null, null, null);
    }

    public SDErrorOutput(String s, String s1, String s2, String s3)
    {
        status = s;
        code = s1;
        message = s2;
        description = s3;
    }

    public void populateData()
    {
        super.populateData();
        status = (String)hashData.get("status");
        code = (String)hashData.get("code");
        message = (String)hashData.get("message");
        description = (String)hashData.get("description");
    }

    public static final SDOutput.Creator CREATOR = new SDOutput.Creator(SDErrorOutput.class);
    private static final long serialVersionUID = 0x142fe484cd7b9fc4L;
    public String code;
    public String description;
    public String message;
    public String status;

}
