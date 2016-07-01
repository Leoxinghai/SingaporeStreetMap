

package com.facebook;


public class FacebookException extends RuntimeException
{

    public FacebookException()
    {
    }

    public FacebookException(String s)
    {
        super(s);
    }

    public FacebookException(String s, Throwable throwable)
    {
        super(s, throwable);
    }

    public FacebookException(Throwable throwable)
    {
        super(throwable);
    }

    static final long serialVersionUID = 1L;
}
