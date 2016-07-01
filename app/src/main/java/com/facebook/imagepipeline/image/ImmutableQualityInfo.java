

package com.facebook.imagepipeline.image;


// Referenced classes of package com.facebook.imagepipeline.image:
//            QualityInfo

public class ImmutableQualityInfo
    implements QualityInfo
{

    private ImmutableQualityInfo(int i, boolean flag, boolean flag1)
    {
        mQuality = i;
        mIsOfGoodEnoughQuality = flag;
        mIsOfFullQuality = flag1;
    }

    public static QualityInfo of(int i, boolean flag, boolean flag1)
    {
        return new ImmutableQualityInfo(i, flag, flag1);
    }

    public boolean equals(Object obj)
    {
        if(obj != this)
        {
            if(!(obj instanceof ImmutableQualityInfo))
                return false;
            obj = (ImmutableQualityInfo)obj;
            if(mQuality != ((ImmutableQualityInfo) (obj)).mQuality || mIsOfGoodEnoughQuality != ((ImmutableQualityInfo) (obj)).mIsOfGoodEnoughQuality || mIsOfFullQuality != ((ImmutableQualityInfo) (obj)).mIsOfFullQuality)
                return false;
        }
        return true;
    }

    public int getQuality()
    {
        return mQuality;
    }

    public int hashCode()
    {
        int j = 0;
        int k = mQuality;
        int i;
        if(mIsOfGoodEnoughQuality)
            i = 0x400000;
        else
            i = 0;
        if(mIsOfFullQuality)
            j = 0x800000;
        return i ^ k ^ j;
    }

    public boolean isOfFullQuality()
    {
        return mIsOfFullQuality;
    }

    public boolean isOfGoodEnoughQuality()
    {
        return mIsOfGoodEnoughQuality;
    }

    public static final QualityInfo FULL_QUALITY = of(0x7fffffff, true, true);
    boolean mIsOfFullQuality;
    boolean mIsOfGoodEnoughQuality;
    int mQuality;

}
