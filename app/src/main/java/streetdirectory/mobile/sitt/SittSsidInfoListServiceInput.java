

package streetdirectory.mobile.sitt;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class SittSsidInfoListServiceInput extends SDHttpServiceInput
{

    public SittSsidInfoListServiceInput(long l)
    {
        modifiedTimeInSecond = l;
    }

    public SittSsidInfoListServiceInput(String s)
    {
        bssidParams = s;
    }

    public String getURL()
    {
        return URLFactory.createURLSittServerNodeListing(modifiedTimeInSecond);
    }

    public String bssidParams;
    public long modifiedTimeInSecond;
}
