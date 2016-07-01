

package streetdirectory.mobile.modules.photopreview.service;

import java.util.HashMap;
import streetdirectory.mobile.service.SDDataOutput;
import streetdirectory.mobile.service.URLFactory;

public class ImageListServiceOutput extends SDDataOutput
{

    public ImageListServiceOutput()
    {
    }

    public String generateImageURL(int i, int j)
    {
        return URLFactory.createURLResizeImage(imageURL, i, j);
    }

    public String generateImageURL(int i, int j, int k)
    {
        return URLFactory.createURLResizeImage(imageURL, i, j, k);
    }

    public void populateData()
    {
        super.populateData();
        imageURL = (String)hashData.get("img");
        description = (String)hashData.get("v");
    }

    private static final long serialVersionUID = 0xde68bfc517c59736L;
    public String description;
    public String imageURL;
}
