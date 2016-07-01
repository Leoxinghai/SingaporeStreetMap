

package com.facebook.imageutils;

import com.facebook.common.internal.Preconditions;
import java.io.*;

// Referenced classes of package com.facebook.imageutils:
//            TiffUtil, StreamProcessor

public class JfifUtil
{

    private JfifUtil()
    {
    }

    public static int getAutoRotateAngleFromOrientation(int i)
    {
        return TiffUtil.getAutoRotateAngleFromOrientation(i);
    }

    public static int getOrientation(InputStream inputstream)
    {
        int i;
        try
        {
            i = moveToAPP1EXIF(inputstream);
            if(i == 0)
                return 0;
            i = TiffUtil.readOrientationFromTIFF(inputstream, i);
            return i;
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            return 0;
        }
    }

    public static int getOrientation(byte abyte0[])
    {
        return getOrientation(((InputStream) (new ByteArrayInputStream(abyte0))));
    }

    private static boolean isSOFn(int i)
    {
        switch(i)
        {
        case 196:
        case 200:
        case 204:
        default:
            return false;

        case 192:
        case 193:
        case 194:
        case 195:
        case 197:
        case 198:
        case 199:
        case 201:
        case 202:
        case 203:
        case 205:
        case 206:
        case 207:
            return true;
        }
    }

    private static int moveToAPP1EXIF(InputStream inputstream)
        throws IOException
    {
        if(moveToMarker(inputstream, 225))
        {
            int i = StreamProcessor.readPackedInt(inputstream, 2, false) - 2;
            if(i > 6)
            {
                int j = StreamProcessor.readPackedInt(inputstream, 4, false);
                int k = StreamProcessor.readPackedInt(inputstream, 2, false);
                if(j == 0x45786966 && k == 0)
                    return i - 4 - 2;
            }
        }
        return 0;
    }

    public static boolean moveToMarker(InputStream inputstream, int i)
        throws IOException
    {
        Preconditions.checkNotNull(inputstream);
        do
        {
            if(StreamProcessor.readPackedInt(inputstream, 1, false) != 255)
                break;
            int j;
            for(j = 255; j == 255; j = StreamProcessor.readPackedInt(inputstream, 1, false));
            while(i == 192 && isSOFn(j) || j == i)
                return true;
            if(j != 216 && j != 1)
            {
                if(j == 217 || j == 218)
                    return false;
                inputstream.skip(StreamProcessor.readPackedInt(inputstream, 2, false) - 2);
            }
        } while(true);
        return false;
    }

    public static final int APP1_EXIF_MAGIC = 0x45786966;
    public static final int MARKER_APP1 = 225;
    public static final int MARKER_EOI = 217;
    public static final int MARKER_ESCAPE_BYTE = 0;
    public static final int MARKER_FIRST_BYTE = 255;
    public static final int MARKER_RST0 = 208;
    public static final int MARKER_RST7 = 215;
    public static final int MARKER_SOFn = 192;
    public static final int MARKER_SOI = 216;
    public static final int MARKER_SOS = 218;
    public static final int MARKER_TEM = 1;
}
