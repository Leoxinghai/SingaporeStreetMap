

package com.facebook.drawee.generic;

import com.facebook.common.internal.Preconditions;
import java.util.Arrays;

public class RoundingParams
{
    public static enum RoundingMethod
    {
        OVERLAY_COLOR("OVERLAY_COLOR", 0),
        BITMAP_ONLY("BITMAP_ONLY", 1);

        private String sType;
        private int iType;

        private RoundingMethod(String s, int i)
        {
            sType = s;
            iType = i;

        }
    }


    public RoundingParams()
    {
        mRoundingMethod = RoundingMethod.BITMAP_ONLY;
        mRoundAsCircle = false;
        mCornersRadii = null;
        mOverlayColor = 0;
        mBorderWidth = 0.0F;
        mBorderColor = 0;
    }

    public static RoundingParams asCircle()
    {
        return (new RoundingParams()).setRoundAsCircle(true);
    }

    public static RoundingParams fromCornersRadii(float f, float f1, float f2, float f3)
    {
        return (new RoundingParams()).setCornersRadii(f, f1, f2, f3);
    }

    public static RoundingParams fromCornersRadii(float af[])
    {
        return (new RoundingParams()).setCornersRadii(af);
    }

    public static RoundingParams fromCornersRadius(float f)
    {
        return (new RoundingParams()).setCornersRadius(f);
    }

    private float[] getOrCreateRoundedCornersRadii()
    {
        if(mCornersRadii == null)
            mCornersRadii = new float[8];
        return mCornersRadii;
    }

    public int getBorderColor()
    {
        return mBorderColor;
    }

    public float getBorderWidth()
    {
        return mBorderWidth;
    }

    public float[] getCornersRadii()
    {
        return mCornersRadii;
    }

    public int getOverlayColor()
    {
        return mOverlayColor;
    }

    public boolean getRoundAsCircle()
    {
        return mRoundAsCircle;
    }

    public RoundingMethod getRoundingMethod()
    {
        return mRoundingMethod;
    }

    public RoundingParams setBorder(int i, float f)
    {
        boolean flag;
        if(f >= 0.0F)
            flag = true;
        else
            flag = false;
        Preconditions.checkArgument(flag, "the border width cannot be < 0");
        mBorderWidth = f;
        mBorderColor = i;
        return this;
    }

    public RoundingParams setCornersRadii(float f, float f1, float f2, float f3)
    {
        float af[] = getOrCreateRoundedCornersRadii();
        af[1] = f;
        af[0] = f;
        af[3] = f1;
        af[2] = f1;
        af[5] = f2;
        af[4] = f2;
        af[7] = f3;
        af[6] = f3;
        return this;
    }

    public RoundingParams setCornersRadii(float af[])
    {
        Preconditions.checkNotNull(af);
        boolean flag;
        if(af.length == 8)
            flag = true;
        else
            flag = false;
        Preconditions.checkArgument(flag, "radii should have exactly 8 values");
        System.arraycopy(af, 0, getOrCreateRoundedCornersRadii(), 0, 8);
        return this;
    }

    public RoundingParams setCornersRadius(float f)
    {
        Arrays.fill(getOrCreateRoundedCornersRadii(), f);
        return this;
    }

    public RoundingParams setOverlayColor(int i)
    {
        mOverlayColor = i;
        mRoundingMethod = RoundingMethod.OVERLAY_COLOR;
        return this;
    }

    public RoundingParams setRoundAsCircle(boolean flag)
    {
        mRoundAsCircle = flag;
        return this;
    }

    public RoundingParams setRoundingMethod(RoundingMethod roundingmethod)
    {
        mRoundingMethod = roundingmethod;
        return this;
    }

    private int mBorderColor;
    private float mBorderWidth;
    private float mCornersRadii[];
    private int mOverlayColor;
    private boolean mRoundAsCircle;
    private RoundingMethod mRoundingMethod;
}
