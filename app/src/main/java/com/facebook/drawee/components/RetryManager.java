

package com.facebook.drawee.components;


public class RetryManager
{

    public RetryManager()
    {
        init();
    }

    public static RetryManager newInstance()
    {
        return new RetryManager();
    }

    public void init()
    {
        mTapToRetryEnabled = false;
        mMaxTapToRetryAttempts = 4;
        reset();
    }

    public boolean isTapToRetryEnabled()
    {
        return mTapToRetryEnabled;
    }

    public void notifyTapToRetry()
    {
        mTapToRetryAttempts = mTapToRetryAttempts + 1;
    }

    public void reset()
    {
        mTapToRetryAttempts = 0;
    }

    public void setTapToRetryEnabled(boolean flag)
    {
        mTapToRetryEnabled = flag;
    }

    public boolean shouldRetryOnTap()
    {
        return mTapToRetryEnabled && mTapToRetryAttempts < mMaxTapToRetryAttempts;
    }

    private static final int MAX_TAP_TO_RETRY_ATTEMPTS = 4;
    private int mMaxTapToRetryAttempts;
    private int mTapToRetryAttempts;
    private boolean mTapToRetryEnabled;
}
