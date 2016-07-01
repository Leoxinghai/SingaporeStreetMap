

package com.facebook.common.media;

import android.webkit.MimeTypeMap;
import com.facebook.common.internal.ImmutableMap;
import java.util.Locale;
import java.util.Map;

public class MediaUtils
{

    public MediaUtils()
    {
    }

    private static String extractExtension(String s)
    {
        int i = s.lastIndexOf('.');
        if(i < 0 || i == s.length() - 1)
            return null;
        else
            return s.substring(i + 1);
    }

    public static String extractMime(String s)
    {
        s = extractExtension(s);
        if(s == null)
        {
            s = null;
        } else
        {
            String s2 = s.toLowerCase(Locale.US);
            String s1 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(s2);
            s = s1;
            if(s1 == null)
                return (String)ADDITIONAL_ALLOWED_MIME_TYPES.get(s2);
        }
        return s;
    }

    public static boolean isNonNativeSupportedMimeType(String s)
    {
        return ADDITIONAL_ALLOWED_MIME_TYPES.containsValue(s);
    }

    public static boolean isPhoto(String s)
    {
        return s != null && s.startsWith("image/");
    }

    public static boolean isVideo(String s)
    {
        return s != null && s.startsWith("video/");
    }

    public static final Map ADDITIONAL_ALLOWED_MIME_TYPES = ImmutableMap.of("mkv", "video/x-matroska");

}
