

package com.facebook.imageformat;


// Referenced classes of package com.facebook.imageformat:
//            ImageFormat

public class ImageFileExtensionMap
{

    private ImageFileExtensionMap()
    {
    }

    public static String getFileExtension(ImageFormat imageformat)
        throws UnsupportedOperationException
    {

        switch(imageformat.ordinal())
        {
        default:
            throw new UnsupportedOperationException((new StringBuilder()).append("Unknown image format ").append(imageformat.name()).toString());

        case 1: // '\001'
        case 2: // '\002'
        case 3: // '\003'
        case 4: // '\004'
        case 5: // '\005'
            return "webp";

        case 6: // '\006'
            return "jpeg";

        case 7: // '\007'
            return "png";

        case 8: // '\b'
            return "gif";

        case 9: // '\t'
            return "bmp";
        }
    }
}
