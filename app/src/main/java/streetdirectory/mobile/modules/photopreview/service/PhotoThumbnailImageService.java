

package streetdirectory.mobile.modules.photopreview.service;

import streetdirectory.mobile.service.SDHttpImageService;

// Referenced classes of package streetdirectory.mobile.modules.photopreview.service:
//            PhotoThumbnailImageServiceInput

public class PhotoThumbnailImageService extends SDHttpImageService
{

    public PhotoThumbnailImageService(String s)
    {
        this(new PhotoThumbnailImageServiceInput(s));
    }

    public PhotoThumbnailImageService(PhotoThumbnailImageServiceInput photothumbnailimageserviceinput)
    {
        super(photothumbnailimageserviceinput);
    }
}
