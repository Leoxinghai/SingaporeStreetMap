// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.animated.base;


public class AnimatedDrawableFrameInfo
{
    public enum DisposalMethod
    {

	DISPOSE_DO_NOT("DISPOSE_DO_NOT", 0),
	DISPOSE_TO_BACKGROUND("DISPOSE_TO_BACKGROUND", 1),
	DISPOSE_TO_PREVIOUS("DISPOSE_TO_PREVIOUS", 2);
        private DisposalMethod(String s, int i)
        {

        }
    }


    public AnimatedDrawableFrameInfo(int i, int j, int k, int l, int i1, boolean flag, DisposalMethod disposalmethod)
    {
        frameNumber = i;
        xOffset = j;
        yOffset = k;
        width = l;
        height = i1;
        shouldBlendWithPreviousFrame = flag;
        disposalMethod = disposalmethod;
    }

    public final DisposalMethod disposalMethod;
    public final int frameNumber;
    public final int height;
    public final boolean shouldBlendWithPreviousFrame;
    public final int width;
    public final int xOffset;
    public final int yOffset;
}
