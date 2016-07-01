

package com.facebook.android;


public class FacebookError extends RuntimeException
{

    public FacebookError(String s)
    {
        super(s);
        mErrorCode = 0;
    }

    public FacebookError(String s, String s1, int i)
    {
        super(s);
        mErrorCode = 0;
        mErrorType = s1;
        mErrorCode = i;
    }

    public int getErrorCode()
    {
        return mErrorCode;
    }

    public String getErrorType()
    {
        return mErrorType;
    }

    private static final long serialVersionUID = 1L;
    private int mErrorCode;
    private String mErrorType;
}
