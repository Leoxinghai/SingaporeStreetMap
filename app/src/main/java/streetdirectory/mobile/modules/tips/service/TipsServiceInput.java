

package streetdirectory.mobile.modules.tips.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class TipsServiceInput extends SDHttpServiceInput
{

    public TipsServiceInput()
    {
    }

    public TipsServiceInput(String s, int i, String s1, int j, int k)
    {
        super(s);
        type = i;
        dataID = s1;
        start = j;
        limit = k;
    }

    public String getURL()
    {
        return URLFactory.createURLLocationBusinessTipsList(countryCode, type, dataID, start, limit, apiVersion);
    }

    public String dataID;
    public int limit;
    public int start;
    public int type;
}
