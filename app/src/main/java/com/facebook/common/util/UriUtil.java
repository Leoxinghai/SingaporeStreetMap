

package com.facebook.common.util;

import android.net.Uri;

public class UriUtil
{

    public UriUtil()
    {
    }

    public static String getSchemeOrNull(Uri uri)
    {
        if(uri == null)
            return null;
        else
            return uri.getScheme();
    }

    public static boolean isDataUri(Uri uri)
    {
        return "data".equals(getSchemeOrNull(uri));
    }

    public static boolean isLocalAssetUri(Uri uri)
    {
        return "asset".equals(getSchemeOrNull(uri));
    }

    public static boolean isLocalContentUri(Uri uri)
    {
        return "content".equals(getSchemeOrNull(uri));
    }

    public static boolean isLocalFileUri(Uri uri)
    {
        return "file".equals(getSchemeOrNull(uri));
    }

    public static boolean isLocalResourceUri(Uri uri)
    {
        return "res".equals(getSchemeOrNull(uri));
    }

    public static boolean isNetworkUri(Uri uri)
    {
        String urischeme = getSchemeOrNull(uri);
        return "https".equals(urischeme) || "http".equals(urischeme);
    }

    public static Uri parseUriOrNull(String s)
    {
        if(s != null)
            return Uri.parse(s);
        else
            return null;
    }

    public static final String DATA_SCHEME = "data";
    public static final String HTTPS_SCHEME = "https";
    public static final String HTTP_SCHEME = "http";
    public static final String LOCAL_ASSET_SCHEME = "asset";
    public static final String LOCAL_CONTENT_SCHEME = "content";
    public static final String LOCAL_FILE_SCHEME = "file";
    public static final String LOCAL_RESOURCE_SCHEME = "res";
}
