

package streetdirectory.mobile.modules.businessfinder.service;

import java.io.File;
import streetdirectory.mobile.core.SDPreferences;
import streetdirectory.mobile.core.storage.CacheStorage;
import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class BusinessFinderServiceInput extends SDHttpServiceInput
{

    public BusinessFinderServiceInput(String s, int i)
    {
        this(s, i, SDPreferences.getInstance().getCategoryVersion());
    }

    public BusinessFinderServiceInput(String s, int i, int j)
    {
        super(s);
        menuID = i;
        categoryVersion = j;
    }

    public File getSaveFile()
    {
        return CacheStorage.getStorageFile((new StringBuilder()).append("xml/businessfinder/menu_").append(countryCode).append("_").append(menuID).append(".xml").toString());
    }

    public String getURL()
    {
        if(menuID == 0)
            return URLFactory.createURLBusinessFinderGenreList(countryCode, menuID, categoryVersion);
        else
            return URLFactory.createURLBusinessFinderIndustryList(countryCode, menuID, 0, categoryVersion);
    }

    public int categoryVersion;
    public int menuID;
}
