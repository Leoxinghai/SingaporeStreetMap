// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.image;

import android.graphics.Bitmap;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.references.ResourceReleaser;
import com.facebook.imageutils.BitmapUtil;

// Referenced classes of package com.facebook.imagepipeline.image:
//            CloseableBitmap, QualityInfo

public class CloseableStaticBitmap extends CloseableBitmap
{

    public CloseableStaticBitmap(Bitmap bitmap, ResourceReleaser resourcereleaser, QualityInfo qualityinfo, int i)
    {
        mBitmap = (Bitmap)Preconditions.checkNotNull(bitmap);
        mBitmapReference = CloseableReference.of(mBitmap, (ResourceReleaser)Preconditions.checkNotNull(resourcereleaser));
        mQualityInfo = qualityinfo;
        mRotationAngle = i;
    }

    public CloseableStaticBitmap(CloseableReference closeablereference, QualityInfo qualityinfo, int i)
    {
        mBitmapReference = (CloseableReference)Preconditions.checkNotNull(closeablereference.cloneOrNull());
        mBitmap = (Bitmap)mBitmapReference.get();
        mQualityInfo = qualityinfo;
        mRotationAngle = i;
    }

    private CloseableReference detachBitmapReference()
    {
        CloseableReference closeablereference;
        closeablereference = mBitmapReference;
        mBitmapReference = null;
        mBitmap = null;
        return closeablereference;
    }

    public void close()
    {
        CloseableReference closeablereference = detachBitmapReference();
        if(closeablereference != null)
            closeablereference.close();
    }

    public CloseableReference convertToBitmapReference()
    {
        CloseableReference closeablereference;
        Preconditions.checkNotNull(mBitmapReference, "Cannot convert a closed static bitmap");
        closeablereference = detachBitmapReference();
        return closeablereference;
    }

    public int getHeight()
    {
        Bitmap bitmap = mBitmap;
        if(bitmap == null)
            return 0;
        else
            return bitmap.getHeight();
    }

    public QualityInfo getQualityInfo()
    {
        return mQualityInfo;
    }

    public int getRotationAngle()
    {
        return mRotationAngle;
    }

    public int getSizeInBytes()
    {
        return BitmapUtil.getSizeInBytes(mBitmap);
    }

    public Bitmap getUnderlyingBitmap()
    {
        return mBitmap;
    }

    public int getWidth()
    {
        Bitmap bitmap = mBitmap;
        if(bitmap == null)
            return 0;
        else
            return bitmap.getWidth();
    }

    public boolean isClosed()
    {
        CloseableReference closeablereference = mBitmapReference;
        boolean flag;
        if(closeablereference == null)
            flag = true;
        else
            flag = false;
        return flag;
    }

    private Bitmap mBitmap;
    private CloseableReference mBitmapReference;
    private final QualityInfo mQualityInfo;
    private final int mRotationAngle;
}
