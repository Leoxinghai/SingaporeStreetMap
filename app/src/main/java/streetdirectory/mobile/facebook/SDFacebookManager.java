

package streetdirectory.mobile.facebook;


// Referenced classes of package streetdirectory.mobile.facebook:
//            FacebookManager

public class SDFacebookManager extends FacebookManager
{

    private SDFacebookManager()
    {
        super("100154376712904");
    }

    public static SDFacebookManager getInstance()
    {
        if(INSTANCE == null)
            INSTANCE = new SDFacebookManager();
        return INSTANCE;
    }

    private static final String APPLICATION_ID = "100154376712904";
    private static SDFacebookManager INSTANCE;
}
