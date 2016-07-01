

package streetdirectory.mobile.modules.offlinemap.service;

import java.io.File;
import streetdirectory.mobile.core.storage.CacheStorage;
import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class OfflineMapPackageDetailServiceInput extends SDHttpServiceInput
{

    public OfflineMapPackageDetailServiceInput(int i)
    {
        forceUpdate = true;
        packageID = i;
    }

    public File getSaveFile()
    {
        return CacheStorage.getStorageFile((new StringBuilder()).append("xml/offline_map/package_detail_").append(packageID).append(".xml").toString());
    }

    public String getURL()
    {
        return URLFactory.createURLOfflineMapPackageDetail(packageID, apiVersion);
    }

    public int packageID;
}
