

package streetdirectory.mobile.service.countrylist;

import java.io.File;
import streetdirectory.mobile.core.SDPreferences;
import streetdirectory.mobile.core.storage.CacheStorage;
import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class CountryListServiceInput extends SDHttpServiceInput
{

    public CountryListServiceInput()
    {
        this(SDPreferences.getInstance().getCategoryVersion());
    }

    public CountryListServiceInput(int i)
    {
        categoryVersion = i;
    }

    public File getSaveFile()
    {
        return CacheStorage.getStorageFile("xml/country_state/country_list.xml");
    }

    public String getURL()
    {
        return URLFactory.createURLCountryList(categoryVersion);
    }

    public int categoryVersion;
}
