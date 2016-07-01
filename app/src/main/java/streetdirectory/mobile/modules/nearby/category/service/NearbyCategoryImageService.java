// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.nearby.category.service;

import java.io.File;
import streetdirectory.mobile.core.SDLogger;
import streetdirectory.mobile.core.storage.CacheStorage;
import streetdirectory.mobile.service.*;

public class NearbyCategoryImageService extends SDHttpImageService
{

    public NearbyCategoryImageService(final String nearbyCategoryIcon, final int width, final int height)
    {
        super(new SDHttpImageServiceInput() {

            public File getSaveFile()
            {
                return CacheStorage.getStorageFile((new StringBuilder()).append("images/nearby/icon/").append(nearbyCategoryIcon).toString());
            }

            public String getURL()
            {
                SDLogger.debug(URLFactory.createURLNearbyCategory(nearbyCategoryIcon, width, height));
                return URLFactory.createURLNearbyCategory(nearbyCategoryIcon, width, height);
            }

        }
);
    }
}
