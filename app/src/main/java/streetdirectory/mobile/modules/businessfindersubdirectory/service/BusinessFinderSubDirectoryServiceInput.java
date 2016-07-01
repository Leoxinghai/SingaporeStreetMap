

package streetdirectory.mobile.modules.businessfindersubdirectory.service;

import streetdirectory.mobile.core.SDPreferences;
import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class BusinessFinderSubDirectoryServiceInput extends SDHttpServiceInput
{

    public BusinessFinderSubDirectoryServiceInput(String s, int i, int j)
    {
        this(s, i, j, SDPreferences.getInstance().getCategoryVersion());
    }

    public BusinessFinderSubDirectoryServiceInput(String s, int i, int j, int k)
    {
        super(s);
        menuID = i;
        categoryID = j;
        categoryVersion = k;
    }

    public String getURL()
    {
        if(menuID == 0)
            return URLFactory.createURLBusinessFinderGenreList(countryCode, categoryID, categoryVersion);
        else
            return URLFactory.createURLBusinessFinderIndustryList(countryCode, menuID, categoryID, categoryVersion);
    }

    public int categoryID;
    public int categoryVersion;
    public int menuID;
}
