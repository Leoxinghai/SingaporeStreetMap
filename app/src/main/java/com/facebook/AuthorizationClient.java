// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.CookieSyncManager;
import com.facebook.internal.NativeProtocol;
import com.facebook.internal.PlatformServiceClient;
import com.facebook.internal.ServerProtocol;
import com.facebook.internal.Utility;
import com.facebook.model.GraphUser;
import com.facebook.widget.WebDialog;
import java.io.Serializable;
import java.util.*;
import org.json.JSONException;
import org.json.JSONObject;

// Referenced classes of package com.facebook:
//            AppEventsLogger, SessionLoginBehavior, FacebookException, Request,
//            HttpMethod, AccessToken, RequestBatch, Settings,
//            SessionDefaultAudience, GetTokenClient, AccessTokenSource, FacebookOperationCanceledException,
//            FacebookServiceException, FacebookRequestError, Response, Session

class AuthorizationClient
    implements Serializable
{
    static class AuthDialogBuilder extends com.facebook.widget.WebDialog.Builder
    {

        public WebDialog build()
        {
            Bundle bundle = getParameters();
            bundle.putString("redirect_uri", "fbconnect://success");
            bundle.putString("client_id", getApplicationId());
            bundle.putString("e2e", e2e);
            bundle.putString("response_type", "token");
            bundle.putString("return_scopes", "true");
            if(isRerequest && !Settings.getPlatformCompatibilityEnabled())
                bundle.putString("auth_type", "rerequest");
            return new WebDialog(getContext(), "oauth", bundle, getTheme(), getListener());
        }

        public AuthDialogBuilder setE2E(String s)
        {
            e2e = s;
            return this;
        }

        public AuthDialogBuilder setIsRerequest(boolean flag)
        {
            isRerequest = flag;
            return this;
        }

        private static final String OAUTH_DIALOG = "oauth";
        static final String REDIRECT_URI = "fbconnect://success";
        private String e2e;
        private boolean isRerequest;

        public AuthDialogBuilder(Context context1, String s, Bundle bundle)
        {
            super(context1, s, "oauth", bundle);
        }
    }

    abstract class AuthHandler
        implements Serializable
    {

        protected void addLoggingExtra(String s, Object obj)
        {
            if(methodLoggingExtras == null)
                methodLoggingExtras = new HashMap();
            Map map = methodLoggingExtras;
            if(obj == null)
                obj = null;
            else
                obj = obj.toString();
            map.put(s, obj);
        }

        void cancel()
        {
        }

        abstract String getNameForLogging();

        boolean needsInternetPermission()
        {
            return false;
        }

        boolean needsRestart()
        {
            return false;
        }

        boolean onActivityResult(int i, int j, Intent intent)
        {
            return false;
        }

        abstract boolean tryAuthorize(AuthorizationRequest authorizationrequest);

        private static final long serialVersionUID = 1L;
        Map methodLoggingExtras;
    }

    static class AuthorizationRequest
        implements Serializable
    {

        String getApplicationId()
        {
            return applicationId;
        }

        String getAuthId()
        {
            return authId;
        }

        SessionDefaultAudience getDefaultAudience()
        {
            return defaultAudience;
        }

        SessionLoginBehavior getLoginBehavior()
        {
            return loginBehavior;
        }

        List getPermissions()
        {
            return permissions;
        }

        String getPreviousAccessToken()
        {
            return previousAccessToken;
        }

        int getRequestCode()
        {
            return requestCode;
        }

        StartActivityDelegate getStartActivityDelegate()
        {
            return startActivityDelegate;
        }

        boolean isLegacy()
        {
            return isLegacy;
        }

        boolean isRerequest()
        {
            return isRerequest;
        }

        boolean needsNewTokenValidation()
        {
            return previousAccessToken != null && !isLegacy;
        }

        void setIsLegacy(boolean flag)
        {
            isLegacy = flag;
        }

        void setPermissions(List list)
        {
            permissions = list;
        }

        void setRerequest(boolean flag)
        {
            isRerequest = flag;
        }

        private static final long serialVersionUID = 1L;
        private final String applicationId;
        private final String authId;
        private final SessionDefaultAudience defaultAudience;
        private boolean isLegacy;
        private boolean isRerequest;
        private final SessionLoginBehavior loginBehavior;
        private List permissions;
        private final String previousAccessToken;
        private final int requestCode;
        private final StartActivityDelegate startActivityDelegate;

        AuthorizationRequest(SessionLoginBehavior sessionloginbehavior, int i, boolean flag, List list, SessionDefaultAudience sessiondefaultaudience, String s, String s1,
                StartActivityDelegate startactivitydelegate, String s2)
        {
            isLegacy = false;
            isRerequest = false;
            loginBehavior = sessionloginbehavior;
            requestCode = i;
            isLegacy = flag;
            permissions = list;
            defaultAudience = sessiondefaultaudience;
            applicationId = s;
            previousAccessToken = s1;
            startActivityDelegate = startactivitydelegate;
            authId = s2;
        }
    }

    static interface BackgroundProcessingListener
    {

        public abstract void onBackgroundProcessingStarted();

        public abstract void onBackgroundProcessingStopped();
    }

    class GetTokenAuthHandler extends AuthHandler
    {

        void cancel()
        {
            if(getTokenClient != null)
            {
                getTokenClient.cancel();
                getTokenClient = null;
            }
        }

        String getNameForLogging()
        {
            return "get_token";
        }

        void getTokenCompleted(AuthorizationRequest authorizationrequest, Bundle bundle)
        {
            getTokenClient = null;
            notifyBackgroundProcessingStop();
            if(bundle != null)
            {
                ArrayList arraylist = bundle.getStringArrayList("com.facebook.platform.extra.PERMISSIONS");
                Object obj = authorizationrequest.getPermissions();
                AccessToken temp;
                AuthorizationClient.Result temp2;
                if(arraylist != null && (obj == null || arraylist.containsAll(((Collection) (obj)))))
                {
                    temp = AccessToken.createFromNativeLogin(bundle, AccessTokenSource.FACEBOOK_APPLICATION_SERVICE);
                    temp2 = Result.createTokenResult(pendingRequest, temp);
                    completeAndValidate(temp2);
                    return;
                }

                ArrayList temp3 = new ArrayList();
                obj = ((List) (obj)).iterator();
                do
                {
                    if(!((Iterator) (obj)).hasNext())
                        break;
                    String s = (String)((Iterator) (obj)).next();
                    if(!arraylist.contains(s))
                        temp3.add(s);
                } while(true);
                if(!bundle.isEmpty())
                    addLoggingExtra("new_permissions", TextUtils.join(",", temp3));
                authorizationrequest.setPermissions(temp3);
            }
            tryNextHandler();
        }

        boolean needsRestart()
        {
            return getTokenClient == null;
        }

        boolean tryAuthorize(final AuthorizationRequest authorizationrequest)
        {
            getTokenClient = new GetTokenClient(context, authorizationrequest.getApplicationId());
            if(!getTokenClient.start())
            {
                return false;
            } else
            {
                notifyBackgroundProcessingStart();
                PlatformServiceClient.CompletedListener completedListener = new com.facebook.internal.PlatformServiceClient.CompletedListener() {

                    public void completed(Bundle bundle)
                    {
                        getTokenCompleted(authorizationrequest, bundle);
                    }

                };

                getTokenClient.setCompletedListener(completedListener);
                return true;
            }
        }

        private static final long serialVersionUID = 1L;
        private GetTokenClient getTokenClient;
    }

    abstract class KatanaAuthHandler extends AuthHandler
    {

        protected boolean tryIntent(Intent intent, int i)
        {
            if(intent == null)
                return false;
            try
            {
                getStartActivityDelegate().startActivityForResult(intent, i);
            }
            // Misplaced declaration of an exception variable
            catch(Exception ex)
            {
                return false;
            }
            return true;
        }

        private static final long serialVersionUID = 1L;

        KatanaAuthHandler()
        {
            super();
        }
    }

    class KatanaProxyAuthHandler extends KatanaAuthHandler
    {

        private Result handleResultOk(Intent intent)
        {
            Result result = null;
            Bundle bundle = intent.getExtras();
            String s = bundle.getString("error");
            //intent = s;
            if(s == null)
                s = bundle.getString("error_type");
            String s2 = bundle.getString("error_code");
            String s1 = bundle.getString("error_message");
            s = s1;
            if(s1 == null)
                s = bundle.getString("error_description");
            s1 = bundle.getString("e2e");
            if(!Utility.isNullOrEmpty(s1))
                logWebLoginCompleted(applicationId, s1);
            if(intent == null && s2 == null && s == null)
            {
                AccessToken temp = AccessToken.createFromWebBundle(pendingRequest.getPermissions(), bundle, AccessTokenSource.FACEBOOK_APPLICATION_WEB);
                result = Result.createTokenResult(pendingRequest, temp);
            } else
            if(!ServerProtocol.errorsProxyAuthDisabled.contains(intent))
                if(ServerProtocol.errorsUserCanceled.contains(intent))
                    return Result.createCancelResult(pendingRequest, null);
                else
                    return Result.createErrorResult(pendingRequest, s1, s, s2);
            return result;
        }

        String getNameForLogging()
        {
            return "katana_proxy_auth";
        }

        boolean onActivityResult(int i, int j, Intent intent)
        {
            AuthorizationClient.Result temp;
            if(intent == null)
                temp = Result.createCancelResult(pendingRequest, "Operation canceled");
            else
            if(j == 0)
                temp = Result.createCancelResult(pendingRequest, intent.getStringExtra("error"));
            else
            if(j != -1)
                temp = Result.createErrorResult(pendingRequest, "Unexpected resultCode from authorization.", null);
            else
                temp = handleResultOk(intent);
            if(intent != null)
                completeAndValidate(temp);
            else
                tryNextHandler();
            return true;
        }

        boolean tryAuthorize(AuthorizationRequest authorizationrequest)
        {
            applicationId = authorizationrequest.getApplicationId();
            String s = AuthorizationClient.getE2E();
            Intent intent = NativeProtocol.createProxyAuthIntent(context, authorizationrequest.getApplicationId(), authorizationrequest.getPermissions(), s, authorizationrequest.isRerequest(), authorizationrequest.getDefaultAudience());
            addLoggingExtra("e2e", s);
            return tryIntent(intent, authorizationrequest.getRequestCode());
        }

        private static final long serialVersionUID = 1L;
        private String applicationId;

        KatanaProxyAuthHandler()
        {
            super();
        }
    }

    static interface OnCompletedListener
    {

        public abstract void onCompleted(Result result);
    }

    static class Result
        implements Serializable
    {

        static enum Code
        {
            SUCCESS("SUCCESS", 0, "success"),
            CANCEL("CANCEL", 1, "cancel"),
            ERROR("ERROR", 2, "error");

            String getLoggingValue()
            {
                return loggingValue;
            }
            String sType;
            int iType;

            private final String loggingValue;
            private Code(String s, int i, String s1)
            {
                sType = s;
                iType = i;
                loggingValue = s1;
            }
        }

        static Result createCancelResult(AuthorizationRequest authorizationrequest, String s)
        {
            return new Result(authorizationrequest, Code.CANCEL, null, s, null);
        }

        static Result createErrorResult(AuthorizationRequest authorizationrequest, String s, String s1)
        {
            return createErrorResult(authorizationrequest, s, s1, null);
        }

        static Result createErrorResult(AuthorizationRequest authorizationrequest, String s, String s1, String s2)
        {
            s = TextUtils.join(": ", Utility.asListNoNulls(new String[] {
                s, s1
            }));
            return new Result(authorizationrequest, Code.ERROR, null, s, s2);
        }

        static Result createTokenResult(AuthorizationRequest authorizationrequest, AccessToken accesstoken)
        {
            return new Result(authorizationrequest, Code.SUCCESS, accesstoken, null, null);
        }

        private static final long serialVersionUID = 1L;
        final Code code;
        final String errorCode;
        final String errorMessage;
        Map loggingExtras;
        final AuthorizationRequest request;
        final AccessToken token;

        private Result(AuthorizationRequest authorizationrequest, Code code1, AccessToken accesstoken, String s, String s1)
        {
            request = authorizationrequest;
            token = accesstoken;
            errorMessage = s;
            code = code1;
            errorCode = s1;
        }


    }


    static interface StartActivityDelegate
    {

        public abstract Activity getActivityContext();

        public abstract void startActivityForResult(Intent intent, int i);
    }

    class WebViewAuthHandler extends AuthHandler
    {

        private String loadCookieToken()
        {
            return getStartActivityDelegate().getActivityContext().getSharedPreferences("com.facebook.AuthorizationClient.WebViewAuthHandler.TOKEN_STORE_KEY", 0).getString("TOKEN", "");
        }

        private void saveCookieToken(String s)
        {
            getStartActivityDelegate().getActivityContext().getSharedPreferences("com.facebook.AuthorizationClient.WebViewAuthHandler.TOKEN_STORE_KEY", 0).edit().putString("TOKEN", s).apply();
        }

        void cancel()
        {
            if(loginDialog != null)
            {
                loginDialog.setOnCompleteListener(null);
                loginDialog.dismiss();
                loginDialog = null;
            }
        }

        String getNameForLogging()
        {
            return "web_view";
        }

        boolean needsInternetPermission()
        {
            return true;
        }

        boolean needsRestart()
        {
            return true;
        }

        void onWebDialogComplete(AuthorizationRequest authorizationrequest, Bundle bundle, FacebookException facebookexception)
        {
            AccessToken temp;
            AuthorizationClient.Result temp2;
            if(bundle != null)
            {
                if(bundle.containsKey("e2e"))
                    e2e = bundle.getString("e2e");
                temp = AccessToken.createFromWebBundle(authorizationrequest.getPermissions(), bundle, AccessTokenSource.WEB_VIEW);
                temp2 = Result.createTokenResult(pendingRequest, temp);
                CookieSyncManager.createInstance(context).sync();
                saveCookieToken(temp.getToken());
            } else
            if(facebookexception instanceof FacebookOperationCanceledException)
            {
                temp2 = Result.createCancelResult(pendingRequest, "User canceled log in.");
            } else
            {
                e2e = null;
                authorizationrequest = null;
                String temp3 = facebookexception.getMessage();
                String temp5 = null;
                if(facebookexception instanceof FacebookServiceException)
                {
                    FacebookRequestError temp4 = ((FacebookServiceException)facebookexception).getRequestError();
                    temp5 = String.format("%d", new Object[]{
                            Integer.valueOf(temp4.getErrorCode())
                    });
                    temp3 = temp3.toString();
                }
                temp2 = Result.createErrorResult(pendingRequest, null, temp3, temp5);
            }
            if(!Utility.isNullOrEmpty(e2e))
                logWebLoginCompleted(applicationId, e2e);
            completeAndValidate(temp2);
        }

        boolean tryAuthorize(final AuthorizationRequest authorizationrequest)
        {
            applicationId = authorizationrequest.getApplicationId();
            Bundle bundle = new Bundle();
            if(!Utility.isNullOrEmpty(authorizationrequest.getPermissions()))
            {
                String s = TextUtils.join(",", authorizationrequest.getPermissions());
                bundle.putString("scope", s);
                addLoggingExtra("scope", s);
            }
            bundle.putString("default_audience", authorizationrequest.getDefaultAudience().getNativeProtocolAudience());
            Object obj = authorizationrequest.getPreviousAccessToken();
            if(!Utility.isNullOrEmpty(((String) (obj))) && ((String) (obj)).equals(loadCookieToken()))
            {
                bundle.putString("access_token", ((String) (obj)));
                addLoggingExtra("access_token", "1");
            } else
            {
                Utility.clearFacebookCookies(context);
                addLoggingExtra("access_token", "0");
            }
            WebDialog.OnCompleteListener onCompleteListener = new com.facebook.widget.WebDialog.OnCompleteListener() {

                public void onComplete(Bundle bundle, FacebookException facebookexception)
                {
                    onWebDialogComplete(authorizationrequest, bundle, facebookexception);
                }

            };
            e2e = AuthorizationClient.getE2E();
            addLoggingExtra("e2e", e2e);
            loginDialog = ((com.facebook.widget.WebDialog.Builder)(new AuthDialogBuilder(getStartActivityDelegate().getActivityContext(), applicationId, bundle)).setE2E(e2e).setIsRerequest(authorizationrequest.isRerequest()).setOnCompleteListener(((com.facebook.widget.WebDialog.OnCompleteListener) (onCompleteListener)))).build();
            loginDialog.show();
            return true;
        }

        private static final long serialVersionUID = 1L;
        private String applicationId;
        private String e2e;
        private WebDialog loginDialog;

        WebViewAuthHandler()
        {
        }
    }


    AuthorizationClient()
    {
    }

    private void addLoggingExtra(String s, String s1, boolean flag)
    {
        if(loggingExtras == null)
            loggingExtras = new HashMap();
        String s2 = s1;
        if(loggingExtras.containsKey(s))
        {
            s2 = s1;
            if(flag)
                s2 = (new StringBuilder()).append((String)loggingExtras.get(s)).append(",").append(s1).toString();
        }
        loggingExtras.put(s, s2);
    }

    private void completeWithFailure()
    {
        complete(Result.createErrorResult(pendingRequest, "Login attempt failed.", null));
    }

    private AppEventsLogger getAppEventsLogger()
    {
        if(appEventsLogger == null || !appEventsLogger.getApplicationId().equals(pendingRequest.getApplicationId()))
            appEventsLogger = AppEventsLogger.newLogger(context, pendingRequest.getApplicationId());
        return appEventsLogger;
    }

    private static String getE2E()
    {
        JSONObject jsonobject = new JSONObject();
        try
        {
            jsonobject.put("init", System.currentTimeMillis());
        }
        catch(JSONException jsonexception) { }
        return jsonobject.toString();
    }

    private List getHandlerTypes(AuthorizationRequest authorizationrequest)
    {
        ArrayList arraylist = new ArrayList();
        SessionLoginBehavior sessionloginbehavior = authorizationrequest.getLoginBehavior();
        if(sessionloginbehavior.allowsKatanaAuth())
        {
            if(!authorizationrequest.isLegacy())
                arraylist.add(new GetTokenAuthHandler());
            arraylist.add(new KatanaProxyAuthHandler());
        }
        if(sessionloginbehavior.allowsWebViewAuth())
            arraylist.add(new WebViewAuthHandler());
        return arraylist;
    }

    private void logAuthorizationMethodComplete(String s, Result result, Map map)
    {
        logAuthorizationMethodComplete(s, result.code.getLoggingValue(), result.errorMessage, result.errorCode, map);
    }

    private void logAuthorizationMethodComplete(String s, String s1, String s2, String s3, Map map)
    {
        Bundle bundle;
        if(pendingRequest != null) {
            bundle = newAuthorizationLoggingBundle(pendingRequest.getAuthId());
            if(s1 != null)
                bundle.putString("2_result", s1);
            if(s2 != null)
                bundle.putString("5_error_message", s2);
            if(s3 != null)
                bundle.putString("4_error_code", s3);
            if(map != null)
            {
                if(!map.isEmpty())
                {
                    bundle.putString("6_extras", (new JSONObject(map)).toString());
                }
            }
        } else {
            bundle = newAuthorizationLoggingBundle("");
            bundle.putString("2_result", Result.Code.ERROR.getLoggingValue());
            bundle.putString("5_error_message", "Unexpected call to logAuthorizationMethodComplete with null pendingRequest.");
        }

        bundle.putString("3_method", s);
        bundle.putLong("1_timestamp_ms", System.currentTimeMillis());
        getAppEventsLogger().logSdkEvent("fb_mobile_login_method_complete", null, bundle);
        return;
    }

    private void logAuthorizationMethodStart(String s)
    {
        Bundle bundle = newAuthorizationLoggingBundle(pendingRequest.getAuthId());
        bundle.putLong("1_timestamp_ms", System.currentTimeMillis());
        bundle.putString("3_method", s);
        getAppEventsLogger().logSdkEvent("fb_mobile_login_method_start", null, bundle);
    }

    private void logWebLoginCompleted(String s, String s1)
    {
        AppEventsLogger appeventslogger = AppEventsLogger.newLogger(context, s);
        Bundle bundle = new Bundle();
        bundle.putString("fb_web_login_e2e", s1);
        bundle.putLong("fb_web_login_switchback_time", System.currentTimeMillis());
        bundle.putString("app_id", s);
        appeventslogger.logSdkEvent("fb_dialogs_web_login_dialog_complete", null, bundle);
    }

    static Bundle newAuthorizationLoggingBundle(String s)
    {
        Bundle bundle = new Bundle();
        bundle.putLong("1_timestamp_ms", System.currentTimeMillis());
        bundle.putString("0_auth_logger_id", s);
        bundle.putString("3_method", "");
        bundle.putString("2_result", "");
        bundle.putString("5_error_message", "");
        bundle.putString("4_error_code", "");
        bundle.putString("6_extras", "");
        return bundle;
    }

    private void notifyBackgroundProcessingStart()
    {
        if(backgroundProcessingListener != null)
            backgroundProcessingListener.onBackgroundProcessingStarted();
    }

    private void notifyBackgroundProcessingStop()
    {
        if(backgroundProcessingListener != null)
            backgroundProcessingListener.onBackgroundProcessingStopped();
    }

    private void notifyOnCompleteListener(Result result)
    {
        if(onCompletedListener != null)
            onCompletedListener.onCompleted(result);
    }

    void authorize(AuthorizationRequest authorizationrequest)
    {
        if(authorizationrequest != null)
        {
            if(pendingRequest != null)
                throw new FacebookException("Attempted to authorize while a request is pending.");
            if(!authorizationrequest.needsNewTokenValidation() || checkInternetPermission())
            {
                pendingRequest = authorizationrequest;
                handlersToTry = getHandlerTypes(authorizationrequest);
                tryNextHandler();
                return;
            }
        }
    }

    void cancelCurrentHandler()
    {
        if(currentHandler != null)
            currentHandler.cancel();
    }

    boolean checkInternetPermission()
    {
        if(checkedInternetPermission)
            return true;
        if(checkPermission("android.permission.INTERNET") != 0)
        {
            String s = context.getString(com.facebook.android.R.string.com_facebook_internet_permission_error_title);
            String s1 = context.getString(com.facebook.android.R.string.com_facebook_internet_permission_error_message);
            complete(Result.createErrorResult(pendingRequest, s, s1));
            return false;
        } else
        {
            checkedInternetPermission = true;
            return true;
        }
    }

    int checkPermission(String s)
    {
        return context.checkCallingOrSelfPermission(s);
    }

    void complete(Result result)
    {
        if(currentHandler != null)
            logAuthorizationMethodComplete(currentHandler.getNameForLogging(), result, currentHandler.methodLoggingExtras);
        if(loggingExtras != null)
            result.loggingExtras = loggingExtras;
        handlersToTry = null;
        currentHandler = null;
        pendingRequest = null;
        loggingExtras = null;
        notifyOnCompleteListener(result);
    }

    void completeAndValidate(Result result)
    {
        if(result.token != null && pendingRequest.needsNewTokenValidation())
        {
            validateSameFbidAndFinish(result);
            return;
        } else
        {
            complete(result);
            return;
        }
    }

    void continueAuth()
    {
        if(pendingRequest == null || currentHandler == null)
            throw new FacebookException("Attempted to continue authorization without a pending request.");
        if(currentHandler.needsRestart())
        {
            currentHandler.cancel();
            tryCurrentHandler();
        }
    }

    Request createGetPermissionsRequest(String s)
    {
        Bundle bundle = new Bundle();
        bundle.putString("access_token", s);
        return new Request(null, "me/permissions", bundle, HttpMethod.GET, null);
    }

    Request createGetProfileIdRequest(String s)
    {
        Bundle bundle = new Bundle();
        bundle.putString("fields", "id");
        bundle.putString("access_token", s);
        return new Request(null, "me", bundle, HttpMethod.GET, null);
    }

    RequestBatch createReauthValidationBatch(final Result pendingResult)
    {
        final ArrayList fbids = new ArrayList();
        final ArrayList grantedPermissions = new ArrayList();
        final ArrayList declinedPermissions = new ArrayList();
        Object obj2 = pendingResult.token.getToken();
        Object obj1 = new Request.Callback() {

            public void onCompleted(Response response)
            {
                try
                {
                    GraphUser temp = (GraphUser)response.getGraphObjectAs(GraphUser.class);
                    if(temp == null)
                        return;
                    fbids.add(temp.getId());
                }
                // Misplaced declaration of an exception variable
                catch(Exception ex)
                {
                    return;
                }
            }
        };

        String s = pendingRequest.getPreviousAccessToken();
        Request request = createGetProfileIdRequest(s);
        ((Request) (request)).setCallback(((Request.Callback) (obj1)));
        Request request2 = createGetProfileIdRequest(((String) (obj2)));
        ((Request) (request2)).setCallback(((Request.Callback) (obj1)));
        Request request3 = createGetPermissionsRequest(s);
        ((Request) (request3)).setCallback(new Request.Callback() {

            public void onCompleted(Response response)
            {
                Session.PermissionsPair temp = null;
                try
                {
                    temp = Session.handlePermissionResponse(response);
                    if(response == null)
                        return;
                    grantedPermissions.addAll(temp.getGrantedPermissions());
                    declinedPermissions.addAll(temp.getDeclinedPermissions());
                }
                catch(Exception ex)
                {
                    return;
                }
            }

        });

        RequestBatch requestBatch = new RequestBatch(new Request[] {
                request, request2, request3
        });
        ((RequestBatch) (requestBatch)).setBatchApplicationId(pendingRequest.getApplicationId());
        ((RequestBatch) (requestBatch)).addCallback(new RequestBatch.Callback() {

            public void onBatchCompleted(RequestBatch requestbatch)
            {
                AccessToken temp;
                AuthorizationClient.Result temp2;
                try {
                    if (fbids.size() != 2 || fbids.get(0) == null || fbids.get(1) == null || !((String) fbids.get(0)).equals(fbids.get(1)))
                        temp2 = Result.createErrorResult(pendingRequest, "User logged in as different Facebook user.", null);
                    else {
                        temp = AccessToken.createFromTokenWithRefreshedPermissions(pendingResult.token, grantedPermissions, declinedPermissions);
                        temp2 = Result.createTokenResult(pendingRequest, temp);
                    }

                    complete(temp2);
                    notifyBackgroundProcessingStop();
                    return;
                } catch(Exception ex) {
                    complete(Result.createErrorResult(pendingRequest, "Caught exception", ex.getMessage()));
                    notifyBackgroundProcessingStop();
                    return;
                }
            }

        }
);
        return ((RequestBatch) (requestBatch));
    }

    BackgroundProcessingListener getBackgroundProcessingListener()
    {
        return backgroundProcessingListener;
    }

    boolean getInProgress()
    {
        return pendingRequest != null && currentHandler != null;
    }

    OnCompletedListener getOnCompletedListener()
    {
        return onCompletedListener;
    }

    StartActivityDelegate getStartActivityDelegate()
    {
        if(startActivityDelegate != null)
            return startActivityDelegate;
        if(pendingRequest != null)
            return new StartActivityDelegate() {

                public Activity getActivityContext()
                {
                    return pendingRequest.getStartActivityDelegate().getActivityContext();
                }

                public void startActivityForResult(Intent intent, int i)
                {
                    pendingRequest.getStartActivityDelegate().startActivityForResult(intent, i);
                }

            };
        else
            return null;
    }

    boolean onActivityResult(int i, int j, Intent intent)
    {
        if(pendingRequest != null && i == pendingRequest.getRequestCode())
            return currentHandler.onActivityResult(i, j, intent);
        else
            return false;
    }

    void setBackgroundProcessingListener(BackgroundProcessingListener backgroundprocessinglistener)
    {
        backgroundProcessingListener = backgroundprocessinglistener;
    }

    void setContext(final Activity activity)
    {
        context = activity;
        startActivityDelegate = new StartActivityDelegate() {

            public Activity getActivityContext()
            {
                return activity;
            }

            public void startActivityForResult(Intent intent, int i)
            {
                activity.startActivityForResult(intent, i);
            }

        };
    }

    void setContext(Context context1)
    {
        context = context1;
        startActivityDelegate = null;
    }

    void setOnCompletedListener(OnCompletedListener oncompletedlistener)
    {
        onCompletedListener = oncompletedlistener;
    }

    void startOrContinueAuth(AuthorizationRequest authorizationrequest)
    {
        if(getInProgress())
        {
            continueAuth();
            return;
        } else
        {
            authorize(authorizationrequest);
            return;
        }
    }

    boolean tryCurrentHandler()
    {
        if(currentHandler.needsInternetPermission() && !checkInternetPermission())
        {
            addLoggingExtra("no_internet_permission", "1", false);
            return false;
        }
        boolean flag = currentHandler.tryAuthorize(pendingRequest);
        if(flag)
        {
            logAuthorizationMethodStart(currentHandler.getNameForLogging());
            return flag;
        } else
        {
            addLoggingExtra("not_tried", currentHandler.getNameForLogging(), true);
            return flag;
        }
    }

    void tryNextHandler()
    {
        if(currentHandler != null)
            logAuthorizationMethodComplete(currentHandler.getNameForLogging(), "skipped", null, null, currentHandler.methodLoggingExtras);

        for(;;) {
            if (handlersToTry == null || handlersToTry.isEmpty()) {
                if (pendingRequest != null) {
                    completeWithFailure();
                    return;
                }
                break;
            }
            currentHandler = (AuthHandler) handlersToTry.remove(0);
            if (tryCurrentHandler())
                break;
        }
        return;
    }

    void validateSameFbidAndFinish(Result result)
    {
        if(result.token == null)
        {
            throw new FacebookException("Can't validate without a token");
        } else
        {
            RequestBatch temp = createReauthValidationBatch(result);
            notifyBackgroundProcessingStart();
            temp.executeAsync();
            return;
        }
    }

    static final String EVENT_EXTRAS_DEFAULT_AUDIENCE = "default_audience";
    static final String EVENT_EXTRAS_IS_LEGACY = "is_legacy";
    static final String EVENT_EXTRAS_LOGIN_BEHAVIOR = "login_behavior";
    static final String EVENT_EXTRAS_MISSING_INTERNET_PERMISSION = "no_internet_permission";
    static final String EVENT_EXTRAS_NEW_PERMISSIONS = "new_permissions";
    static final String EVENT_EXTRAS_NOT_TRIED = "not_tried";
    static final String EVENT_EXTRAS_PERMISSIONS = "permissions";
    static final String EVENT_EXTRAS_REQUEST_CODE = "request_code";
    static final String EVENT_EXTRAS_TRY_LEGACY = "try_legacy";
    static final String EVENT_EXTRAS_TRY_LOGIN_ACTIVITY = "try_login_activity";
    static final String EVENT_NAME_LOGIN_COMPLETE = "fb_mobile_login_complete";
    private static final String EVENT_NAME_LOGIN_METHOD_COMPLETE = "fb_mobile_login_method_complete";
    private static final String EVENT_NAME_LOGIN_METHOD_START = "fb_mobile_login_method_start";
    static final String EVENT_NAME_LOGIN_START = "fb_mobile_login_start";
    static final String EVENT_PARAM_AUTH_LOGGER_ID = "0_auth_logger_id";
    static final String EVENT_PARAM_ERROR_CODE = "4_error_code";
    static final String EVENT_PARAM_ERROR_MESSAGE = "5_error_message";
    static final String EVENT_PARAM_EXTRAS = "6_extras";
    static final String EVENT_PARAM_LOGIN_RESULT = "2_result";
    static final String EVENT_PARAM_METHOD = "3_method";
    private static final String EVENT_PARAM_METHOD_RESULT_SKIPPED = "skipped";
    static final String EVENT_PARAM_TIMESTAMP = "1_timestamp_ms";
    private static final String TAG = "Facebook-AuthorizationClient";
    private static final String WEB_VIEW_AUTH_HANDLER_STORE = "com.facebook.AuthorizationClient.WebViewAuthHandler.TOKEN_STORE_KEY";
    private static final String WEB_VIEW_AUTH_HANDLER_TOKEN_KEY = "TOKEN";
    private static final long serialVersionUID = 1L;
    private AppEventsLogger appEventsLogger;
    BackgroundProcessingListener backgroundProcessingListener;
    boolean checkedInternetPermission;
    Context context;
    AuthHandler currentHandler;
    List handlersToTry;
    Map loggingExtras;
    OnCompletedListener onCompletedListener;
    AuthorizationRequest pendingRequest;
    StartActivityDelegate startActivityDelegate;




}
