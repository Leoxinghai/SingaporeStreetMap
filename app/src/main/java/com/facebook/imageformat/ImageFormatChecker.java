

package com.facebook.imageformat;

import com.facebook.common.internal.*;
import com.facebook.imagepipeline.webp.WebpSupportStatus;
import java.io.*;

// Referenced classes of package com.facebook.imageformat:
//            ImageFormat

public class ImageFormatChecker
{

    private ImageFormatChecker()
    {
    }

    private static byte[] asciiBytes(String s)
    {
        Preconditions.checkNotNull(s);
        try
        {
            byte temp[] = s.getBytes("ASCII");
            return temp;
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            throw new RuntimeException("ASCII not found!", ex);
        }

    }

    private static ImageFormat doGetImageFormat(byte abyte0[], int i)
    {
        Preconditions.checkNotNull(abyte0);
        if(WebpSupportStatus.isWebpHeader(abyte0, 0, i))
            return getWebpFormat(abyte0, i);
        if(isJpegHeader(abyte0, i))
            return ImageFormat.JPEG;
        if(isPngHeader(abyte0, i))
            return ImageFormat.PNG;
        if(isGifHeader(abyte0, i))
            return ImageFormat.GIF;
        if(isBmpHeader(abyte0, i))
            return ImageFormat.BMP;
        else
            return ImageFormat.UNKNOWN;
    }

    public static ImageFormat getImageFormat(InputStream inputstream)
        throws IOException
    {
        Preconditions.checkNotNull(inputstream);
        byte abyte0[] = new byte[MAX_HEADER_LENGTH];
        return doGetImageFormat(abyte0, readHeaderFromStream(inputstream, abyte0));
    }

    public static ImageFormat getImageFormat(String s)
    {
        Object obj;
        ImageFormat imageformat;
        obj = null;
        imageformat = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(s);
            obj = getImageFormat(((InputStream) (fileInputStream)));
            Closeables.closeQuietly(fileInputStream);
            return ((ImageFormat) (obj));
        } catch(IOException iex) {
            iex.printStackTrace();
            return null;
        }
    }

    public static ImageFormat getImageFormat_WrapIOException(InputStream inputstream)
    {
        try
        {
            ImageFormat temp = getImageFormat(inputstream);
            return temp;
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            //throw Throwables.propagate(ex);
            ex.printStackTrace();
            return null;
        }
    }

    private static ImageFormat getWebpFormat(byte abyte0[], int i)
    {
        Preconditions.checkArgument(WebpSupportStatus.isWebpHeader(abyte0, 0, i));
        if(WebpSupportStatus.isSimpleWebpHeader(abyte0, 0))
            return ImageFormat.WEBP_SIMPLE;
        if(WebpSupportStatus.isLosslessWebpHeader(abyte0, 0))
            return ImageFormat.WEBP_LOSSLESS;
        if(WebpSupportStatus.isExtendedWebpHeader(abyte0, 0, i))
        {
            if(WebpSupportStatus.isAnimatedWebpHeader(abyte0, 0))
                return ImageFormat.WEBP_ANIMATED;
            if(WebpSupportStatus.isExtendedWebpHeaderWithAlpha(abyte0, 0))
                return ImageFormat.WEBP_EXTENDED_WITH_ALPHA;
            else
                return ImageFormat.WEBP_EXTENDED;
        } else
        {
            return ImageFormat.UNKNOWN;
        }
    }

    private static boolean isBmpHeader(byte abyte0[], int i)
    {
        if(i < BMP_HEADER.length)
            return false;
        else
            return matchBytePattern(abyte0, 0, BMP_HEADER);
    }

    private static boolean isGifHeader(byte abyte0[], int i)
    {
        while(i < 6 || !matchBytePattern(abyte0, 0, GIF_HEADER_87A) && !matchBytePattern(abyte0, 0, GIF_HEADER_89A))
            return false;
        return true;
    }

    private static boolean isJpegHeader(byte abyte0[], int i)
    {
        boolean flag1 = false;
        boolean flag = flag1;
        if(i >= JPEG_HEADER.length)
        {
            flag = flag1;
            if(matchBytePattern(abyte0, 0, JPEG_HEADER))
                flag = true;
        }
        return flag;
    }

    private static boolean isPngHeader(byte abyte0[], int i)
    {
        boolean flag1 = false;
        boolean flag = flag1;
        if(i >= PNG_HEADER.length)
        {
            flag = flag1;
            if(matchBytePattern(abyte0, 0, PNG_HEADER))
                flag = true;
        }
        return flag;
    }

    private static boolean matchBytePattern(byte abyte0[], int i, byte abyte1[])
    {
        Preconditions.checkNotNull(abyte0);
        Preconditions.checkNotNull(abyte1);
        boolean flag;
        if(i >= 0)
            flag = true;
        else
            flag = false;
        Preconditions.checkArgument(flag);
        if(abyte1.length + i > abyte0.length)
	        return false;

        int j = 0;
		for(;j < abyte1.length;) {
			if(abyte0[j + i] != abyte1[j])
               return true;
            j++;
		}
        return false;
    }

    private static int readHeaderFromStream(InputStream inputstream, byte abyte0[])
        throws IOException
    {
        Preconditions.checkNotNull(inputstream);
        Preconditions.checkNotNull(abyte0);
        int i;
        boolean flag;
        if(abyte0.length >= MAX_HEADER_LENGTH)
            flag = true;
        else
            flag = false;
        Preconditions.checkArgument(flag);
        if(inputstream.markSupported()) {
            inputstream.mark(MAX_HEADER_LENGTH);
            i = ByteStreams.read(inputstream, abyte0, 0, MAX_HEADER_LENGTH);
            inputstream.reset();
            return i;
        }
        inputstream.reset();
        return ByteStreams.read(inputstream, abyte0, 0, MAX_HEADER_LENGTH);
    }

    private static final byte BMP_HEADER[];
    private static final int EXTENDED_WEBP_HEADER_LENGTH = 21;
    private static final byte GIF_HEADER_87A[] = asciiBytes("GIF87a");
    private static final byte GIF_HEADER_89A[] = asciiBytes("GIF89a");
    private static final int GIF_HEADER_LENGTH = 6;
    private static final byte JPEG_HEADER[] = {
        -1, -40, -1
    };
    private static final int MAX_HEADER_LENGTH;
    private static final byte PNG_HEADER[] = {
        -119, 80, 78, 71, 13, 10, 26, 10
    };
    private static final int SIMPLE_WEBP_HEADER_LENGTH = 20;

    static
    {
        BMP_HEADER = asciiBytes("BM");
        MAX_HEADER_LENGTH = Ints.max(new int[] {
            21, 20, JPEG_HEADER.length, PNG_HEADER.length, 6, BMP_HEADER.length
        });
    }
}
