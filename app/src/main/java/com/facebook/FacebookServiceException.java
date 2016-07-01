

package com.facebook;


// Referenced classes of package com.facebook:
//            FacebookException, FacebookRequestError

public class FacebookServiceException extends FacebookException
{

    public FacebookServiceException(FacebookRequestError facebookrequesterror, String s)
    {
        super(s);
        error = facebookrequesterror;
    }

    public final FacebookRequestError getRequestError()
    {
        return error;
    }

    public final String toString()
    {
        return (new StringBuilder()).append("{FacebookServiceException: ").append("httpResponseCode: ").append(error.getRequestStatusCode()).append(", facebookErrorCode: ").append(error.getErrorCode()).append(", facebookErrorType: ").append(error.getErrorType()).append(", message: ").append(error.getErrorMessage()).append("}").toString();
    }

    private static final long serialVersionUID = 1L;
    private final FacebookRequestError error;
}
