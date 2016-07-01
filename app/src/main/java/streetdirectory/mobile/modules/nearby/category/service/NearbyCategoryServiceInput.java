

package streetdirectory.mobile.modules.nearby.category.service;

import java.io.File;
import streetdirectory.mobile.core.SDPreferences;
import streetdirectory.mobile.core.storage.CacheStorage;
import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class NearbyCategoryServiceInput extends SDHttpServiceInput
{

    public NearbyCategoryServiceInput(String s)
    {
        this(s, SDPreferences.getInstance().getCategoryVersion());
    }

    public NearbyCategoryServiceInput(String s, int i)
    {
        super(s);
        categoryVersion = i;
    }

    public File getSaveFile()
    {
        return CacheStorage.getStorageFile((new StringBuilder()).append("xml/nearby/category_").append(countryCode).append(".xml").toString());
    }

    public String getURL()
    {
        return URLFactory.createURLNearbyCategoryList(countryCode, categoryVersion);
    }

    public int categoryVersion;
}
