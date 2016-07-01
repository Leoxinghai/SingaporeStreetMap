// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.memory;

import android.graphics.Bitmap;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.internal.Throwables;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.references.ResourceReleaser;
import com.facebook.imagepipeline.common.TooManyBitmapsException;
import com.facebook.imagepipeline.nativecode.Bitmaps;
import com.facebook.imageutils.BitmapUtil;
import java.util.*;

public class BitmapCounter
{

    public BitmapCounter(int i, int j)
    {
        boolean flag1 = true;
        boolean flag;
        if(i > 0)
            flag = true;
        else
            flag = false;
        Preconditions.checkArgument(flag);
        if(j > 0)
            flag = flag1;
        else
            flag = false;
        Preconditions.checkArgument(flag);
        mMaxCount = i;
        mMaxSize = j;
    }

    public List associateBitmapsWithBitmapCounter(List list)
    {
        int i = 0;

        for(;i < list.size();) {
            Bitmap bitmap = (Bitmap) list.get(i);
            if (android.os.Build.VERSION.SDK_INT < 21)
                Bitmaps.pinBitmap(bitmap);
            if (!increase(bitmap))
                throw new TooManyBitmapsException();

            Object obj;
            if (list != null)
                for (Iterator iterator0 = list.iterator(); iterator0.hasNext(); ) {
                    Bitmap bitmap1 = (Bitmap) iterator0.next();
                    if (i > 0)
                        decrease(bitmap1);
                    bitmap1.recycle();
                    i--;
                }
            i++;
        }


        ArrayList temp = new ArrayList();
        for(Iterator iterator = list.iterator(); iterator.hasNext(); ((List) (temp)).add(CloseableReference.of((Bitmap)iterator.next(), mUnpooledBitmapsReleaser)));

        return ((List) (temp));
    }

    public void decrease(Bitmap bitmap)
    {
        boolean flag1 = true;
        int i = BitmapUtil.getSizeInBytes(bitmap);
        boolean flag;
        if(mCount > 0)
            flag = true;
        else
            flag = false;
        Preconditions.checkArgument(flag, "No bitmaps registered.");
        if((long)i <= mSize)
            flag = flag1;
        else
            flag = false;
        Preconditions.checkArgument(flag, "Bitmap size bigger than the total registered size: %d, %d", new Object[] {
            Integer.valueOf(i), Long.valueOf(mSize)
        });
        mSize = mSize - (long)i;
        mCount = mCount - 1;
        return;
    }

    public int getCount()
    {
        int i = mCount;
        return i;
    }

    public ResourceReleaser getReleaser()
    {
        return mUnpooledBitmapsReleaser;
    }

    public long getSize()
    {
        long l = mSize;
        return l;
    }

    public boolean increase(Bitmap bitmap)
    {
        int i = BitmapUtil.getSizeInBytes(bitmap);
        boolean flag = false;

        if(mCount < mMaxCount) {
            long l = mSize;
            long l1 = i;
            int j = mMaxSize;
            if (l + l1 <= (long) j) {
                mCount = mCount + 1;
                mSize = mSize + (long) i;
                flag = true;
            }
        }
        return flag;
    }

    private int mCount;
    private final int mMaxCount;
    private final int mMaxSize;
    private long mSize;
    private final ResourceReleaser mUnpooledBitmapsReleaser = new ResourceReleaser() {

        public void release(Bitmap bitmap)
        {
            decrease(bitmap);
            bitmap.recycle();
            return;
        }

        public void release(Object obj)
        {
            release((Bitmap)obj);
        }

    };
}
