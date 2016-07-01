

package streetdirectory.mobile.service;

import streetdirectory.mobile.core.service.HttpImageService;

// Referenced classes of package streetdirectory.mobile.service:
//            SDHttpImageServiceInput

public class SDHttpImageService extends HttpImageService
{

    public SDHttpImageService(String s)
    {
        this(s, 0, 0, 0);
    }

    public SDHttpImageService(String s, int i, int j)
    {
        this(s, i, j, 1, 0);
    }

    public SDHttpImageService(String s, int i, int j, int k)
    {
        this(s, i, j, k, 0);
    }

    static class SUBCLASS1 extends SDHttpImageServiceInput {

        public String getURL()
        {
            return url;
        }
        final String url;

        SUBCLASS1(int final_i, int i, int j, int k, String ss)
        {
            super(final_i, j, k, i);
            url = ss;
        }
    }

    public SDHttpImageService(String s, final int final_i, int i, int j, int k)
    {
        super(new SUBCLASS1(final_i, i, j, k, s));
    }

    public SDHttpImageService(SDHttpImageServiceInput sdhttpimageserviceinput)
    {
        super(sdhttpimageserviceinput);
    }
}
