

package streetdirectory.mobile.service;


// Referenced classes of package streetdirectory.mobile.service:
//            SDErrorOutput

public class SDDatasetErrorFoundException extends RuntimeException
{

    public SDDatasetErrorFoundException()
    {
    }

    public SDDatasetErrorFoundException(SDErrorOutput sderroroutput)
    {
        errorOutput = sderroroutput;
    }

    private static final long serialVersionUID = 0xa97c9d9ae48e14c5L;
    public SDErrorOutput errorOutput;
}
