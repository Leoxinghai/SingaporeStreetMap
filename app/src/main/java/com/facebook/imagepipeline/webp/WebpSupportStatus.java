

package com.facebook.imagepipeline.webp;

import android.graphics.BitmapFactory;
import android.util.Base64;
import java.io.UnsupportedEncodingException;

public class WebpSupportStatus
{

    public WebpSupportStatus()
    {
    }

    private static byte[] asciiBytes(String s)
    {
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

    public static boolean isAnimatedWebpHeader(byte abyte0[], int i)
    {
        boolean flag = matchBytePattern(abyte0, i + 12, WEBP_VP8X_BYTES);
        if((abyte0[i + 20] & 2) == 2)
            i = 1;
        else
            i = 0;
        return flag && i != 0;
    }

    public static boolean isExtendedWebpHeader(byte abyte0[], int i, int j)
    {
        return j >= 21 && matchBytePattern(abyte0, i + 12, WEBP_VP8X_BYTES);
    }

    public static boolean isExtendedWebpHeaderWithAlpha(byte abyte0[], int i)
    {
        boolean flag = matchBytePattern(abyte0, i + 12, WEBP_VP8X_BYTES);
        if((abyte0[i + 20] & 0x10) == 16)
            i = 1;
        else
            i = 0;
        return flag && i != 0;
    }

    private static boolean isExtendedWebpSupported()
    {
        if(android.os.Build.VERSION.SDK_INT < 17)
	        return false;

        android.graphics.BitmapFactory.Options options;
        if(android.os.Build.VERSION.SDK_INT != 17)
            return false;
        byte abyte0[] = Base64.decode("UklGRkoAAABXRUJQVlA4WAoAAAAQAAAAAAAAAAAAQUxQSAwAAAARBxAR/Q9ERP8DAABWUDggGAAAABQBAJ0BKgEAAQAAAP4AAA3AAP7mtQAAAA==", 0);
        options = new android.graphics.BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(abyte0, 0, abyte0.length, options);
        if(options.outHeight != 1 || options.outWidth != 1)
        	return false;

        return true;
    }

    public static boolean isLosslessWebpHeader(byte abyte0[], int i)
    {
        return matchBytePattern(abyte0, i + 12, WEBP_VP8L_BYTES);
    }

    public static boolean isSimpleWebpHeader(byte abyte0[], int i)
    {
        return matchBytePattern(abyte0, i + 12, WEBP_VP8_BYTES);
    }

    public static boolean isWebpHeader(byte abyte0[], int i, int j)
    {
        return j >= 20 && matchBytePattern(abyte0, i, WEBP_RIFF_BYTES) && matchBytePattern(abyte0, i + 8, WEBP_NAME_BYTES);
    }

    public static boolean isWebpPlatformSupported(byte abyte0[], int i, int j)
    {
        boolean flag1 = false;
        boolean flag;
        if(isSimpleWebpHeader(abyte0, i))
        {
            flag = sIsSimpleWebpSupported;
        } else
        {
            if(isLosslessWebpHeader(abyte0, i))
                return sIsExtendedWebpSupported;
            flag = flag1;
            if(isExtendedWebpHeader(abyte0, i, j))
            {
                flag = flag1;
                if(!isAnimatedWebpHeader(abyte0, i))
                    return sIsExtendedWebpSupported;
            }
        }
        return flag;
    }

    private static boolean matchBytePattern(byte abyte0[], int i, byte abyte1[])
    {
        if(abyte1.length + i > abyte0.length)
	        return false;

        int j = 0;
		for(;j < abyte1.length;) {
			if(abyte0[j + i] != abyte1[j])
				return false;
			j++;
        }
        return true;
    }

    private static final int EXTENDED_WEBP_HEADER_LENGTH = 21;
    private static final int SIMPLE_WEBP_HEADER_LENGTH = 20;
    private static final String VP8X_WEBP_BASE64 = "UklGRkoAAABXRUJQVlA4WAoAAAAQAAAAAAAAAAAAQUxQSAwAAAARBxAR/Q9ERP8DAABWUDggGAAAABQBAJ0BKgEAAQAAAP4AAA3AAP7mtQAAAA==";
    private static final byte WEBP_NAME_BYTES[] = asciiBytes("WEBP");
    private static final byte WEBP_RIFF_BYTES[] = asciiBytes("RIFF");
    private static final byte WEBP_VP8L_BYTES[] = asciiBytes("VP8L");
    private static final byte WEBP_VP8X_BYTES[] = asciiBytes("VP8X");
    private static final byte WEBP_VP8_BYTES[] = asciiBytes("VP8 ");
    public static final boolean sIsExtendedWebpSupported = isExtendedWebpSupported();
    public static final boolean sIsSimpleWebpSupported;
    public static final boolean sIsWebpSupportRequired;

    static
    {
        boolean flag1 = true;
        boolean flag;
        if(android.os.Build.VERSION.SDK_INT <= 17)
            flag = true;
        else
            flag = false;
        sIsWebpSupportRequired = flag;
        if(android.os.Build.VERSION.SDK_INT >= 14)
            flag = flag1;
        else
            flag = false;
        sIsSimpleWebpSupported = flag;
    }
}
