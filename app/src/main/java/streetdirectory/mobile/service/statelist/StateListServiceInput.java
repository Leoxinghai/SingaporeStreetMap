

package streetdirectory.mobile.service.statelist;

import java.io.File;
import streetdirectory.mobile.core.SDPreferences;
import streetdirectory.mobile.core.storage.CacheStorage;
import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class StateListServiceInput extends SDHttpServiceInput
{

    public StateListServiceInput(String s)
    {
        this(s, SDPreferences.getInstance().getCategoryVersion());
    }

    public StateListServiceInput(String s, int i)
    {
        super(s);
        categoryVersion = i;
    }

    public File getSaveFile()
    {
        return CacheStorage.getStorageFile((new StringBuilder()).append("xml/country_state/state_").append(countryCode).append(".xml").toString());
    }

    public String getURL()
    {
        return URLFactory.createURLStateList(countryCode, categoryVersion);
    }

    public int categoryVersion;
}
