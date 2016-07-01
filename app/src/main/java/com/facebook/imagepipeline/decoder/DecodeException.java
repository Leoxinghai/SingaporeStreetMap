

package com.facebook.imagepipeline.decoder;


public class DecodeException extends RuntimeException
{

    public DecodeException(String s)
    {
        super(s);
    }

    public DecodeException(String s, Throwable throwable)
    {
        super(s, throwable);
    }
}
