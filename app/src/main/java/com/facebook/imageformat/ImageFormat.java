// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imageformat;


public enum ImageFormat
{
	WEBP_SIMPLE("WEBP_SIMPLE", 0),
	WEBP_LOSSLESS("WEBP_LOSSLESS", 1),
	WEBP_EXTENDED("WEBP_EXTENDED", 2),
	WEBP_EXTENDED_WITH_ALPHA("WEBP_EXTENDED_WITH_ALPHA", 3),
	WEBP_ANIMATED("WEBP_ANIMATED", 4),
	JPEG("JPEG", 5),
	PNG("PNG", 6),
	GIF("GIF", 7),
	BMP("BMP", 8),
	UNKNOWN("UNKNOWN", 9);

    private ImageFormat(String s, int i)
    {


    }

    public static boolean isWebpFormat(ImageFormat imageformat)
    {
        return imageformat == WEBP_SIMPLE || imageformat == WEBP_LOSSLESS || imageformat == WEBP_EXTENDED || imageformat == WEBP_EXTENDED_WITH_ALPHA || imageformat == WEBP_ANIMATED;
    }

}
