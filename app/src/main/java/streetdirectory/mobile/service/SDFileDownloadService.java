

package streetdirectory.mobile.service;

import java.io.File;
import streetdirectory.mobile.core.service.FileDownloadService;

// Referenced classes of package streetdirectory.mobile.service:
//            SDHttpServiceInput

public class SDFileDownloadService extends FileDownloadService
{
    static class SUBCLASS1 extends SDHttpServiceInput {

        public File getSaveFile()
        {
            return savePath;
        }

        public String getURL()
        {
            return url;
        }

        final File savePath;
        final String url;

        SUBCLASS1(File file, String s)
        {
            super();
            url = s;
            savePath = file;
        }
    }

    public SDFileDownloadService(final String url, final File savePath)
    {
        super(new SUBCLASS1(savePath, url));
    }

}
