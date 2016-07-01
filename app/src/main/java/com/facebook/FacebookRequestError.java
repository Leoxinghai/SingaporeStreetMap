// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook;

import com.facebook.internal.Utility;
import java.net.HttpURLConnection;
import org.json.JSONException;
import org.json.JSONObject;

// Referenced classes of package com.facebook:
//            FacebookServiceException, FacebookException

public final class FacebookRequestError
{
    public static enum Category
    {

		AUTHENTICATION_RETRY("AUTHENTICATION_RETRY", 0),
		AUTHENTICATION_REOPEN_SESSION("AUTHENTICATION_REOPEN_SESSION", 1),
		PERMISSION("PERMISSION", 2),
		SERVER("SERVER", 3),
		THROTTLING("THROTTLING", 4),
		OTHER("OTHER", 5),
		BAD_REQUEST("BAD_REQUEST", 6),
		CLIENT("CLIENT", 7);
        String sType;
        int iType;
        private Category(String s, int i)
        {
            sType = s;
            iType = i;

        }
    }

    private static class Range
    {

        boolean contains(int i)
        {
            return start <= i && i <= end;
        }

        private final int end;
        private final int start;

        private Range(int i, int j)
        {
            start = i;
            end = j;
        }

    }


    private FacebookRequestError(int i, int j, int k, String s, String s1, String s2, String s3,
            boolean flag, JSONObject jsonobject, JSONObject jsonobject1, Object obj, HttpURLConnection httpurlconnection)
    {
        this(i, j, k, s, s1, s2, s3, flag, jsonobject, jsonobject1, obj, httpurlconnection, null);
    }

    private FacebookRequestError(int i, int j, int k, String s, String s1, String s2, String s3,
            boolean flag, JSONObject jsonobject, JSONObject jsonobject1, Object obj, HttpURLConnection httpurlconnection, FacebookException facebookexception)
    {
        boolean flag2;
        requestStatusCode = i;
        errorCode = j;
        subErrorCode = k;
        errorType = s;
        errorMessage = s1;
        requestResultBody = jsonobject;
        requestResult = jsonobject1;
        batchRequestResult = obj;
        connection = httpurlconnection;
        errorUserTitle = s2;
        errorUserMessage = s3;
        boolean errorIs= flag;
        int l = 0;

        boolean flag1 = false;
        if(facebookexception != null)
        {
            exception = facebookexception;
            flag1 = true;
        } else
        {
            exception = new FacebookServiceException(this, s1);
        }
        s = null;
        flag2 = false;
        if(flag1) {
            category = Category.CLIENT;
            j = 0;
        } else {
            if(j == 1 || j == 2)
            {
                category = Category.SERVER;
                l = ((flag2) ? 1 : 0);
            } else
            if(j == 4 || j == 17)
            {
                category = Category.THROTTLING;
                l = ((flag2) ? 1 : 0);
            } else if(j == 10 || EC_RANGE_PERMISSION.contains(j)) {
                category = Category.PERMISSION;
                l = com.facebook.android.R.string.com_facebook_requesterror_permissions;
            } else if(j != 102) {
                l = ((flag2) ? 1 : 0);
            }
            if(j == 190 || j == 102 ) {
                if (k == 459 || k == 464) {
                    category = Category.AUTHENTICATION_RETRY;
                    l = com.facebook.android.R.string.com_facebook_requesterror_web_login;
                } else {
                    category = Category.AUTHENTICATION_REOPEN_SESSION;
                    if (k == 458 || k == 463)
                        l = com.facebook.android.R.string.com_facebook_requesterror_relogin;
                    else if (k == 460)
                        l = com.facebook.android.R.string.com_facebook_requesterror_password_changed;
                    else
                        l = com.facebook.android.R.string.com_facebook_requesterror_reconnect;
                }
            }

            s1 = s;
            j = l;
            if(s == null)
                if(HTTP_RANGE_CLIENT_ERROR.contains(i))
                {
                    category = Category.BAD_REQUEST;
                    j = l;
                } else
                if(HTTP_RANGE_SERVER_ERROR.contains(i))
                {
                    category = Category.SERVER;
                    j = l;
                } else
                {
                    category = Category.OTHER;
                    j = l;
                }
        }

        if(s3 != null && s3.length() > 0)
            flag = true;
        else
            flag = false;

        //category = s1;
        userActionMessageId = j;
        shouldNotifyUser = flag;
        return;

    }

    public FacebookRequestError(int i, String s, String s1)
    {
        this(-1, i, -1, s, s1, null, null, false, null, null, null, null, null);
    }

    FacebookRequestError(HttpURLConnection httpurlconnection, Exception exception1)
    {
        this(-1, -1, -1, null, null, null, null, false, null, null, null, httpurlconnection, ((FacebookException) (exception1)));
        if(exception1 instanceof FacebookException)
            exception1 = (FacebookException)exception1;
        else
            exception1 = new FacebookException(exception1);
    }

    static FacebookRequestError checkResponseAndCreateError(JSONObject jsonobject, Object obj, HttpURLConnection httpurlconnection)
    {
        Object obj1;
        int k;
        String s;
        JSONObject jsonobject1;
        Object obj3;
        int i;
        int j;
        boolean flag;
        boolean flag2;

        Object obj2;
        String s1;
        boolean flag1;

        if(!jsonobject.has("code"))
            return null;

        try {


            k = jsonobject.getInt("code");
            obj1 = Utility.getStringPropertyAsJSON(jsonobject, "body", "FACEBOOK_NON_JSON_RESULT");
            if(obj1 != null && (obj1 instanceof JSONObject)) {

                JSONObject jsonobject2 = (JSONObject) obj1;
                obj1 = null;
                s = null;
                jsonobject1 = null;
                obj3 = null;
                flag2 = false;
                i = -1;
                j = -1;
                flag = false;
                if (!jsonobject2.has("error")) {
                    if (jsonobject2.has("error_code") || jsonobject2.has("error_msg")) {
                        obj1 = jsonobject2.optString("error_reason", null);
                        s = jsonobject2.optString("error_msg", null);
                        i = jsonobject2.optInt("error_code", -1);
                        j = jsonobject2.optInt("error_subcode", -1);
                        flag = true;
                        s1 = null;
                        obj2 = jsonobject1;
                        flag1 = flag2;
                    } else {
                        s1 = null;
                        obj2 = jsonobject1;
                        flag1 = flag2;
                        //                if (!jsonobject2.has("error_reason"))
                        //                    continue; /* Loop/switch isn't completed */
                    }

                } else {
                    jsonobject1 = (JSONObject) Utility.getStringPropertyAsJSON(jsonobject2, "error", null);
                    obj1 = jsonobject1.optString("type", null);
                    s = jsonobject1.optString("message", null);
                    i = jsonobject1.optInt("code", -1);
                    j = jsonobject1.optInt("error_subcode", -1);
                    obj2 = jsonobject1.optString("error_user_msg", null);
                    s1 = jsonobject1.optString("error_user_title", null);
                    flag1 = jsonobject1.optBoolean("is_transient", false);
                    flag = true;
                }

                if (flag)
                    return new FacebookRequestError(k, i, j, ((String) (obj1)), s, s1, ((String) (obj2)), flag1, jsonobject2, jsonobject, obj, httpurlconnection);

            }

            if(HTTP_RANGE_SUCCESS.contains(k))
                obj1 = null;
            else {
                if (jsonobject.has("body"))
                    obj1 = (JSONObject) Utility.getStringPropertyAsJSON(jsonobject, "body", "FACEBOOK_NON_JSON_RESULT");
            }

            FacebookRequestError error = new FacebookRequestError(k, -1, -1, null, null, null, null, false, ((JSONObject) (obj1)), jsonobject, obj, httpurlconnection);
            return error;
        } catch(Exception exception1) {
            exception1.printStackTrace();
            return null;
        }

    }

    public Object getBatchRequestResult()
    {
        return batchRequestResult;
    }

    public Category getCategory()
    {
        return category;
    }

    public HttpURLConnection getConnection()
    {
        return connection;
    }

    public int getErrorCode()
    {
        return errorCode;
    }

    public boolean getErrorIsTransient()
    {
        return errorIsTransient;
    }

    public String getErrorMessage()
    {
        if(errorMessage != null)
            return errorMessage;
        else
            return exception.getLocalizedMessage();
    }

    public String getErrorType()
    {
        return errorType;
    }

    public String getErrorUserMessage()
    {
        return errorUserMessage;
    }

    public String getErrorUserTitle()
    {
        return errorUserTitle;
    }

    public FacebookException getException()
    {
        return exception;
    }

    public JSONObject getRequestResult()
    {
        return requestResult;
    }

    public JSONObject getRequestResultBody()
    {
        return requestResultBody;
    }

    public int getRequestStatusCode()
    {
        return requestStatusCode;
    }

    public int getSubErrorCode()
    {
        return subErrorCode;
    }

    public int getUserActionMessageId()
    {
        return userActionMessageId;
    }

    public boolean shouldNotifyUser()
    {
        return shouldNotifyUser;
    }

    public String toString()
    {
        return (new StringBuilder("{HttpStatus: ")).append(requestStatusCode).append(", errorCode: ").append(errorCode).append(", errorType: ").append(errorType).append(", errorMessage: ").append(getErrorMessage()).append("}").toString();
    }

    private static final String BODY_KEY = "body";
    private static final String CODE_KEY = "code";
    private static final int EC_APP_NOT_INSTALLED = 458;
    private static final int EC_APP_TOO_MANY_CALLS = 4;
    private static final int EC_EXPIRED = 463;
    private static final int EC_INVALID_SESSION = 102;
    private static final int EC_INVALID_TOKEN = 190;
    private static final int EC_PASSWORD_CHANGED = 460;
    private static final int EC_PERMISSION_DENIED = 10;
    private static final Range EC_RANGE_PERMISSION = new Range(200, 299);
    private static final int EC_SERVICE_UNAVAILABLE = 2;
    private static final int EC_UNCONFIRMED_USER = 464;
    private static final int EC_UNKNOWN_ERROR = 1;
    private static final int EC_USER_CHECKPOINTED = 459;
    private static final int EC_USER_TOO_MANY_CALLS = 17;
    private static final String ERROR_CODE_FIELD_KEY = "code";
    private static final String ERROR_CODE_KEY = "error_code";
    private static final String ERROR_IS_TRANSIENT_KEY = "is_transient";
    private static final String ERROR_KEY = "error";
    private static final String ERROR_MESSAGE_FIELD_KEY = "message";
    private static final String ERROR_MSG_KEY = "error_msg";
    private static final String ERROR_REASON_KEY = "error_reason";
    private static final String ERROR_SUB_CODE_KEY = "error_subcode";
    private static final String ERROR_TYPE_FIELD_KEY = "type";
    private static final String ERROR_USER_MSG_KEY = "error_user_msg";
    private static final String ERROR_USER_TITLE_KEY = "error_user_title";
    private static final Range HTTP_RANGE_CLIENT_ERROR = new Range(400, 499);
    private static final Range HTTP_RANGE_SERVER_ERROR = new Range(500, 599);
    private static final Range HTTP_RANGE_SUCCESS = new Range(200, 299);
    public static final int INVALID_ERROR_CODE = -1;
    public static final int INVALID_HTTP_STATUS_CODE = -1;
    private static final int INVALID_MESSAGE_ID = 0;
    private final Object batchRequestResult;
    private Category category;
    private final HttpURLConnection connection;
    private final int errorCode;
    private boolean errorIsTransient;
    private final String errorMessage;
    private final String errorType;
    private final String errorUserMessage;
    private final String errorUserTitle;
    private final FacebookException exception;
    private final JSONObject requestResult;
    private final JSONObject requestResultBody;
    private final int requestStatusCode;
    private final boolean shouldNotifyUser;
    private final int subErrorCode;
    private final int userActionMessageId;

}
