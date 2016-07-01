

package com.facebook.imagepipeline.nativecode;

import com.facebook.common.internal.Preconditions;
import com.facebook.imageformat.ImageFormat;
import com.facebook.imagepipeline.webp.WebpSupportStatus;
import java.io.*;

// Referenced classes of package com.facebook.imagepipeline.nativecode:
//            ImagePipelineNativeLoader

public class WebpTranscoder
{

    public WebpTranscoder()
    {
    }

    public static boolean isWebpNativelySupported(ImageFormat imageformat)
    {
        switch(imageformat.ordinal()) {
            default:
                Preconditions.checkArgument(false);
            case 5:
                return false;
            case 1:
                if (android.os.Build.VERSION.SDK_INT < 14)
                    return false;
                return true;
            case 2:
            case 3:
            case 4:
                return WebpSupportStatus.sIsExtendedWebpSupported;
        }
    }

    private static native void nativeTranscodeWebpToJpeg(InputStream inputstream, OutputStream outputstream, int i)
        throws IOException;

    private static native void nativeTranscodeWebpToPng(InputStream inputstream, OutputStream outputstream)
        throws IOException;

    public static void transcodeWebpToJpeg(InputStream inputstream, OutputStream outputstream, int i)
        throws IOException
    {
        nativeTranscodeWebpToJpeg((InputStream)Preconditions.checkNotNull(inputstream), (OutputStream)Preconditions.checkNotNull(outputstream), i);
    }

    public static void transcodeWebpToPng(InputStream inputstream, OutputStream outputstream)
        throws IOException
    {
        nativeTranscodeWebpToPng((InputStream)Preconditions.checkNotNull(inputstream), (OutputStream)Preconditions.checkNotNull(outputstream));
    }

    static
    {
        ImagePipelineNativeLoader.load();
    }
}
