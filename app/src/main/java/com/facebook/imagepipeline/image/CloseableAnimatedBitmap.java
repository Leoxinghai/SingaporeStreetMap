// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.image;

import android.graphics.Bitmap;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.references.ResourceReleaser;
import com.facebook.imageutils.BitmapUtil;
import java.util.*;

// Referenced classes of package com.facebook.imagepipeline.image:
//            CloseableBitmap

public class CloseableAnimatedBitmap extends CloseableBitmap
{

    public CloseableAnimatedBitmap(List list, List list1)
    {
        super();
        boolean flag1 = true;
        Preconditions.checkNotNull(list);
        boolean flag;
        if(list.size() >= 1)
            flag = true;
        else
            flag = false;
        Preconditions.checkState(flag, "Need at least 1 frame!");
        mBitmapReferences = new ArrayList();
        mBitmaps = new ArrayList();
        CloseableReference closeablereference;
        for(Iterator iterator = list.iterator(); iterator.hasNext(); mBitmaps.add(closeablereference.get()))
        {
            closeablereference = (CloseableReference)iterator.next();
            mBitmapReferences.add(closeablereference.clone());
        }

        mDurations = (List)Preconditions.checkNotNull(list1);
        if(mDurations.size() == mBitmaps.size())
            flag = flag1;
        else
            flag = false;
        Preconditions.checkState(flag, "Arrays length mismatch!");
    }

    public CloseableAnimatedBitmap(List list, List list1, ResourceReleaser resourcereleaser)
    {
        super();
        boolean flag1 = true;
        Preconditions.checkNotNull(list);
        boolean flag;
        if(list.size() >= 1)
            flag = true;
        else
            flag = false;
        Preconditions.checkState(flag, "Need at least 1 frame!");
        mBitmaps = new ArrayList();
        mBitmapReferences = new ArrayList();
        Bitmap bitmap;
        for(Iterator iterator = list.iterator(); iterator.hasNext(); mBitmaps.add(bitmap))
        {
            bitmap = (Bitmap)iterator.next();
            mBitmapReferences.add(CloseableReference.of(bitmap, resourcereleaser));
        }

        mDurations = (List)Preconditions.checkNotNull(list1);
        if(mDurations.size() == mBitmaps.size())
            flag = flag1;
        else
            flag = false;
        Preconditions.checkState(flag, "Arrays length mismatch!");
    }

    public void close()
    {
        if(mBitmapReferences == null)
            return;
        List list;
        list = mBitmapReferences;
        mBitmapReferences = null;
        mBitmaps = null;
        mDurations = null;
        CloseableReference.closeSafely(list);
        return;
    }

    public List getBitmaps()
    {
        return mBitmaps;
    }

    public List getDurations()
    {
        return mDurations;
    }

    public int getHeight()
    {
        List list = mBitmaps;
        if(list == null)
            return 0;
        else
            return ((Bitmap)list.get(0)).getHeight();
    }

    public int getSizeInBytes()
    {
        List list = mBitmaps;
        if(list == null || list.size() == 0)
            return 0;
        else
            return BitmapUtil.getSizeInBytes((Bitmap)list.get(0)) * list.size();
    }

    public Bitmap getUnderlyingBitmap()
    {
        List list = mBitmaps;
        if(list != null)
            return (Bitmap)list.get(0);
        else
            return null;
    }

    public int getWidth()
    {
        List list = mBitmaps;
        if(list == null)
            return 0;
        else
            return ((Bitmap)list.get(0)).getWidth();
    }

    public boolean isClosed()
    {
        List list = mBitmaps;
        boolean flag;
        if(list == null)
            flag = true;
        else
            flag = false;
        return flag;
    }

    private List mBitmapReferences;
    private List mBitmaps;
    private List mDurations;
}
