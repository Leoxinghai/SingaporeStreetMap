// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.offlinemap.service;

import java.io.File;
import streetdirectory.mobile.core.storage.CacheStorage;
import streetdirectory.mobile.core.storage.StorageUtil;
import streetdirectory.mobile.service.*;

public class OfflineMapImageService extends SDHttpImageService
{

    public OfflineMapImageService(String s)
    {
        this(s, 0, 0);
    }

    public OfflineMapImageService(final String imageID, final int width, final int height)
    {
        super(new SDHttpImageServiceInput() {

            public File getSaveFile()
            {
                return CacheStorage.getStorageFile((new StringBuilder()).append("images/offline_map/icon/").append(imageID).toString());
            }

            public String getURL()
            {
                return URLFactory.createURLOfflineMapImage(imageID, width, height);
            }

        });
    }

    public static boolean deleteAllCache()
    {
        return StorageUtil.deleteDirectory(CacheStorage.getStorageFile("images/offline_map/icon/"));
    }
}
