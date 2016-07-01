// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imageutils;

import com.facebook.common.logging.FLog;
import java.io.IOException;
import java.io.InputStream;

// Referenced classes of package com.facebook.imageutils:
//            StreamProcessor

class TiffUtil
{
    private static class TiffHeader
    {

        int byteOrder;
        int firstIfdOffset;
        boolean isLittleEndian;

        private TiffHeader()
        {
        }

    }


    TiffUtil()
    {
    }

    public static int getAutoRotateAngleFromOrientation(int i)
    {
        switch(i)
        {
        case 2: // '\002'
        case 4: // '\004'
        case 5: // '\005'
        case 7: // '\007'
        default:
            FLog.i(TAG, "Unsupported orientation");
            // fall through

        case 1: // '\001'
            return 0;

        case 3: // '\003'
            return 180;

        case 6: // '\006'
            return 90;

        case 8: // '\b'
            return 270;
        }
    }

    private static int getOrientationFromTiffEntry(InputStream inputstream, int i, boolean flag)
        throws IOException
    {
        while(i < 10 || StreamProcessor.readPackedInt(inputstream, 2, flag) != 3 || StreamProcessor.readPackedInt(inputstream, 4, flag) != 1)
            return 0;
        i = StreamProcessor.readPackedInt(inputstream, 2, flag);
        StreamProcessor.readPackedInt(inputstream, 2, flag);
        return i;
    }

    private static int moveToTiffEntryWithTag(InputStream inputstream, int i, boolean flag, int j)
        throws IOException
    {
        if(i >= 14)
        {
            int k = StreamProcessor.readPackedInt(inputstream, 2, flag);
            int l = i - 2;
            i = k;
            k = l;
            while(i > 0 && k >= 12)
            {
                int i1 = StreamProcessor.readPackedInt(inputstream, 2, flag);
                k -= 2;
                if(i1 == j)
                    return k;
                inputstream.skip(10L);
                k -= 10;
                i--;
            }
        }
        return 0;
    }

    public static int readOrientationFromTIFF(InputStream inputstream, int i)
        throws IOException
    {
        TiffHeader tiffheader = new TiffHeader();
        i = readTiffHeader(inputstream, i, tiffheader);
        int j = tiffheader.firstIfdOffset - 8;
        if(i == 0 || j > i)
        {
            return 0;
        } else
        {
            inputstream.skip(j);
            return getOrientationFromTiffEntry(inputstream, moveToTiffEntryWithTag(inputstream, i - j, tiffheader.isLittleEndian, 274), tiffheader.isLittleEndian);
        }
    }

    private static int readTiffHeader(InputStream inputstream, int i, TiffHeader tiffheader)
        throws IOException
    {
        if(i <= 8)
            return 0;
        tiffheader.byteOrder = StreamProcessor.readPackedInt(inputstream, 4, false);
        if(tiffheader.byteOrder != 0x49492a00 && tiffheader.byteOrder != 0x4d4d002a)
        {
            FLog.e(TAG, "Invalid TIFF header");
            return 0;
        }
        boolean flag;
        if(tiffheader.byteOrder == 0x49492a00)
            flag = true;
        else
            flag = false;
        tiffheader.isLittleEndian = flag;
        tiffheader.firstIfdOffset = StreamProcessor.readPackedInt(inputstream, 4, tiffheader.isLittleEndian);
        i = i - 4 - 4;
        if(tiffheader.firstIfdOffset < 8 || tiffheader.firstIfdOffset - 8 > i)
        {
            FLog.e(TAG, "Invalid offset");
            return 0;
        } else
        {
            return i;
        }
    }

    private static final Class TAG = TiffUtil.class;
    public static final int TIFF_BYTE_ORDER_BIG_END = 0x4d4d002a;
    public static final int TIFF_BYTE_ORDER_LITTLE_END = 0x49492a00;
    public static final int TIFF_TAG_ORIENTATION = 274;
    public static final int TIFF_TYPE_SHORT = 3;

}
