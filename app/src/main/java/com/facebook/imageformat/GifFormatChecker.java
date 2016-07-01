

package com.facebook.imageformat;

import com.facebook.common.internal.Preconditions;
import java.io.IOException;
import java.io.InputStream;

public final class GifFormatChecker
{

    private GifFormatChecker()
    {
    }

    static boolean circularBufferMatchesBytePattern(byte abyte0[], int i, byte abyte1[]) {
        Preconditions.checkNotNull(abyte0);
        Preconditions.checkNotNull(abyte1);
        boolean flag;
        if (i >= 0)
            flag = true;
        else
            flag = false;
        Preconditions.checkArgument(flag);
        if (abyte1.length <= abyte0.length) {
            int j = 0;
            for (; j < abyte1.length; ) {
                if (abyte0[(j + i) % abyte0.length] != abyte1[j])
                    return true;
                j++;
            }
        }

        return false;
    }

    public static boolean isAnimated(InputStream inputstream)
    {
        int j;
        int k;
        byte abyte0[] = new byte[10];
        int i;
        i = 0;
        k = 0;
        try
        {
            inputstream.read(abyte0, 0, 10);

            for(;inputstream.read(abyte0, i, 1) > 0;) {
                j = k;
                if (circularBufferMatchesBytePattern(abyte0, i + 1, FRAME_HEADER_START)) {
                    if (circularBufferMatchesBytePattern(abyte0, i + 9, FRAME_HEADER_END_1) || circularBufferMatchesBytePattern(abyte0, i + 9, FRAME_HEADER_END_2)) {
                        k++;
                        j = k;
                        if (k > 1)
                            return true;
                    }
                }

                i = (i + 1) % abyte0.length;
                k = j;
            }
        }
        catch(Exception ex)
        {
            throw new RuntimeException(ex);
        }

        return false;
    }

    private static final byte FRAME_HEADER_END_1[] = {
        0, 44
    };
    private static final byte FRAME_HEADER_END_2[] = {
        0, 33
    };
    private static final int FRAME_HEADER_SIZE = 10;
    private static final byte FRAME_HEADER_START[] = {
        0, 33, -7, 4
    };

}
