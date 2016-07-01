

package com.facebook.imagepipeline.animated.util;

import android.graphics.Bitmap;
import java.util.Arrays;

public class AnimatedDrawableUtil
{

    public AnimatedDrawableUtil()
    {
    }

    public static boolean isOutsideRange(int i, int j, int k)
    {
        boolean flag1 = false;
        boolean flag = false;
        if(i == -1 || j == -1)
            return true;
        if(i <= j)
        {
            if(k < i || k > j)
                flag = true;
            return flag;
        }
        flag = flag1;
        if(k < i)
        {
            flag = flag1;
            if(k > j)
                flag = true;
        }
        return flag;
    }

    public void appendMemoryString(StringBuilder stringbuilder, int i)
    {
        if(i < 1024)
        {
            stringbuilder.append(i);
            stringbuilder.append("KB");
            return;
        } else
        {
            int j = i / 1024;
            i = (i % 1024) / 100;
            stringbuilder.append(j);
            stringbuilder.append(".");
            stringbuilder.append(i);
            stringbuilder.append("MB");
            return;
        }
    }

    public void fixFrameDurations(int ai[])
    {
        for(int i = 0; i < ai.length; i++)
            if(ai[i] < 11)
                ai[i] = 100;

    }

    public int getFrameForTimestampMs(int ai[], int i)
    {
        int j = Arrays.binarySearch(ai, i);
        i = j;
        if(j < 0)
            i = -j - 1 - 1;
        return i;
    }

    public int[] getFrameTimeStampsFromDurations(int ai[])
    {
        int ai1[] = new int[ai.length];
        int j = 0;
        for(int i = 0; i < ai.length; i++)
        {
            ai1[i] = j;
            j += ai[i];
        }

        return ai1;
    }

    public int getSizeOfBitmap(Bitmap bitmap)
    {
        if(android.os.Build.VERSION.SDK_INT >= 19)
            return bitmap.getAllocationByteCount();
        if(android.os.Build.VERSION.SDK_INT >= 12)
            return bitmap.getByteCount();
        else
            return bitmap.getWidth() * bitmap.getHeight() * 4;
    }

    public int getTotalDurationFromFrameDurations(int ai[])
    {
        int j = 0;
        for(int i = 0; i < ai.length; i++)
            j += ai[i];

        return j;
    }

    private static final int FRAME_DURATION_MS_FOR_MIN = 100;
    private static final int MIN_FRAME_DURATION_MS = 11;
}
