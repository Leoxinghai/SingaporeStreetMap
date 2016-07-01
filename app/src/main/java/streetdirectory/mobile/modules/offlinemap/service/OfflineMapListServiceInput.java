

package streetdirectory.mobile.modules.offlinemap.service;

import java.io.File;
import streetdirectory.mobile.core.storage.CacheStorage;
import streetdirectory.mobile.core.storage.StorageUtil;
import streetdirectory.mobile.service.SDHttpServiceInput;
import streetdirectory.mobile.service.URLFactory;

public class OfflineMapListServiceInput extends SDHttpServiceInput
{

    public OfflineMapListServiceInput()
    {
    }

    public static boolean deleteAllCache()
    {
        return StorageUtil.deleteDirectory(CacheStorage.getStorageFile("xml/offline_map/"));
    }

    public File getSaveFile()
    {
        return CacheStorage.getStorageFile("xml/offline_map/package_list.xml");
    }

    public String getURL()
    {
        return URLFactory.createURLOfflineMapPackageList(apiVersion);
    }
}
