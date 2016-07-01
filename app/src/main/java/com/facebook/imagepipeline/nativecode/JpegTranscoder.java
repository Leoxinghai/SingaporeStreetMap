

package com.facebook.imagepipeline.nativecode;

import com.facebook.common.internal.Preconditions;
import java.io.*;

// Referenced classes of package com.facebook.imagepipeline.nativecode:
//            ImagePipelineNativeLoader

public class JpegTranscoder
{

    public JpegTranscoder()
    {
    }

    public static boolean isRotationAngleAllowed(int i)
    {
        return i >= 0 && i <= 270 && i % 90 == 0;
    }

    private static native void nativeTranscodeJpeg(InputStream inputstream, OutputStream outputstream, int i, int j, int k)
        throws IOException;

    public static void transcodeJpeg(InputStream inputstream, OutputStream outputstream, int i, int j, int k)
        throws IOException
    {
        boolean flag1 = false;
        boolean flag;
        if(j >= 1)
            flag = true;
        else
            flag = false;
        Preconditions.checkArgument(flag);
        if(j <= 16)
            flag = true;
        else
            flag = false;
        Preconditions.checkArgument(flag);
        if(k >= 0)
            flag = true;
        else
            flag = false;
        Preconditions.checkArgument(flag);
        if(k <= 100)
            flag = true;
        else
            flag = false;
        Preconditions.checkArgument(flag);
        Preconditions.checkArgument(isRotationAngleAllowed(i));
        if(j == 8)
        {
            flag = flag1;
        }
        if(j != 8 || i != 0) {
            flag = true;
        }
        Preconditions.checkArgument(flag, "no transformation requested");
        nativeTranscodeJpeg((InputStream)Preconditions.checkNotNull(inputstream), (OutputStream)Preconditions.checkNotNull(outputstream), i, j, k);
    }

    public static final int MAX_QUALITY = 100;
    public static final int MAX_SCALE_NUMERATOR = 16;
    public static final int MIN_QUALITY = 0;
    public static final int MIN_SCALE_NUMERATOR = 1;
    public static final int SCALE_DENOMINATOR = 8;

    static
    {
        ImagePipelineNativeLoader.load();
    }
}
