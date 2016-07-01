

package streetdirectory.mobile.modules.search.service;

import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class SearchServiceInput extends SDHttpServiceInput
{

    public SearchServiceInput()
    {
    }

    public SearchServiceInput(String s, String s1, boolean flag)
    {
        super(s);
        keyword = s1;
        showCategory = flag;
    }

    public String getURL()
    {
        return URLFactory.createURLSearchList(countryCode, keyword, showCategory, apiVersion);
    }

    public String keyword;
    public boolean showCategory;
}
