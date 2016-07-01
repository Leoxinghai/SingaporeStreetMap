

package streetdirectory.mobile.gis.maps;

import android.net.Uri;
import java.io.File;
import streetdirectory.mobile.core.SDLogger;
import streetdirectory.mobile.core.storage.*;
import streetdirectory.mobile.service.SDHttpImageService;
import streetdirectory.mobile.service.SDHttpImageServiceInput;

public class MapTileImageService extends SDHttpImageService
{

    public MapTileImageService(final String url)
    {
        super(new SDHttpImageServiceInput() {

            public File getSaveFile()
            {
                File file;
                File file1;
                String s;
                try
                {
                    s = Uri.parse(url).getPath();
                    file1 = InternalStorage.getStorageFile((new StringBuilder()).append("offline_map/").append(s).toString());
                }
                catch(Exception exception)
                {
                    SDLogger.printStackTrace(exception, "MapTileImageService getSaveFile uri");
                    return null;
                }
                file = file1;
                if(!file1.exists()) {
                    file1 = ExternalStorage.getStorageFile((new StringBuilder()).append("offline_map/").append(s).toString());
                    file = file1;
                }
                if (!file1.exists())
                    file = CacheStorage.getStorageFile(s);
                return file;
            }

            public String getURL()
            {
                return url;
            }

        });
        System.out.println("MapTileImageService."+url);

    }
}
