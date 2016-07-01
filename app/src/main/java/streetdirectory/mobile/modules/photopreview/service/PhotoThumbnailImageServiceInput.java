

package streetdirectory.mobile.modules.photopreview.service;

import java.io.File;
import streetdirectory.mobile.core.SDLogger;
import streetdirectory.mobile.core.storage.CacheStorage;
import streetdirectory.mobile.service.SDHttpImageServiceInput;

// Referenced classes of package streetdirectory.mobile.modules.photopreview.service:
//            ImageListServiceOutput

public class PhotoThumbnailImageServiceInput extends SDHttpImageServiceInput
{

    public PhotoThumbnailImageServiceInput(String s)
    {
        mUrl = s;
        try
        {
            String ss[] = s.split("r=");
            if(ss.length > 0)
                fileName = (new StringBuilder()).append(ss[1]).append(".jpg").toString();
            return;
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            SDLogger.printStackTrace(ex, "Create File Name Error");
        }
    }

    public PhotoThumbnailImageServiceInput(ImageListServiceOutput imagelistserviceoutput, int i, int j)
    {
        mUrl = imagelistserviceoutput.generateImageURL(i, j);
        try
        {
            String ss[] = imagelistserviceoutput.imageURL.split("r=");
            if(ss.length > 0)
                fileName = (new StringBuilder()).append(ss[1]).append(".jpg").toString();
            return;
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            SDLogger.printStackTrace(ex, "Create File Name Error");
        }
    }

    public File getSaveFile()
    {
        if(fileName != null)
            return CacheStorage.getStorageFile((new StringBuilder()).append("images/photo/thumb/").append(fileName).toString());
        else
            return null;
    }

    public String getURL()
    {
        return mUrl;
    }

    public String fileName;
    private String mUrl;
}
