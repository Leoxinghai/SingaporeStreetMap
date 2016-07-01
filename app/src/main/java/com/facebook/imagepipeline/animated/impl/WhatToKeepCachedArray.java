

package com.facebook.imagepipeline.animated.impl;

import com.facebook.imagepipeline.animated.util.AnimatedDrawableUtil;

class WhatToKeepCachedArray
{

    WhatToKeepCachedArray(int i)
    {
        mData = new boolean[i];
    }

    boolean get(int i)
    {
        return mData[i];
    }

    void removeOutsideRange(int i, int j)
    {
        for(int k = 0; k < mData.length; k++)
            if(AnimatedDrawableUtil.isOutsideRange(i, j, k))
                mData[k] = false;

    }

    void set(int i, boolean flag)
    {
        mData[i] = flag;
    }

    void setAll(boolean flag)
    {
        for(int i = 0; i < mData.length; i++)
            mData[i] = flag;

    }

    private final boolean mData[];
}
