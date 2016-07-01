

package com.facebook.imagepipeline.image;


public interface QualityInfo
{

    public abstract int getQuality();

    public abstract boolean isOfFullQuality();

    public abstract boolean isOfGoodEnoughQuality();
}
