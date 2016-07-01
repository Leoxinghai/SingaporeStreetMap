

package streetdirectory.mobile.modules.businessfinder.service;

import java.io.File;
import streetdirectory.mobile.core.storage.CacheStorage;
import streetdirectory.mobile.service.*;

public class BusinessFinderImageService extends SDHttpImageService
{

    public BusinessFinderImageService(final String businessFinderIcon, final int width, final int height)
    {
        super(new SDHttpImageServiceInput() {

            public File getSaveFile()
            {
                return CacheStorage.getStorageFile((new StringBuilder()).append("images/businessfinder/icon/").append(businessFinderIcon).toString());
            }

            public String getURL()
            {
                return URLFactory.createURLBusinessFinderMenu(businessFinderIcon, width, height);
            }

        });
    }
}
