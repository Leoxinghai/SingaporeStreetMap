// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook;

import android.app.Activity;
import android.content.*;
import android.content.pm.PackageManager;
import android.os.*;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import com.facebook.internal.NativeProtocol;
import com.facebook.internal.SessionAuthorizationType;
import com.facebook.internal.Utility;
import com.facebook.internal.Validate;
import com.facebook.model.GraphMultiResult;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphObjectList;
import java.io.*;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.Executor;
import org.json.JSONException;
import org.json.JSONObject;

// Referenced classes of package com.facebook:
//            SharedPreferencesTokenCachingStrategy, SessionState, TokenCachingStrategy, AccessToken,
//            AppEventsLogger, LoginActivity, SessionLoginBehavior, FacebookAuthorizationException,
//            FacebookOperationCanceledException, Response, AuthorizationClient, SessionDefaultAudience,
//            Settings, FacebookException, Request, AccessTokenSource

public class Session
    implements Serializable
{
    public static class AuthorizationRequest
        implements Serializable
    {

        private void readObject(ObjectInputStream objectinputstream)
            throws InvalidObjectException
        {
            throw new InvalidObjectException("Cannot readObject, serialization proxy required");
        }

        String getApplicationId()
        {
            return applicationId;
        }

        String getAuthId()
        {
            return authId;
        }

        AuthorizationClient.AuthorizationRequest getAuthorizationClientRequest()
        {
            AuthorizationClient.StartActivityDelegate startactivitydelegate = new AuthorizationClient.StartActivityDelegate() {

                public Activity getActivityContext()
                {
                    return startActivityDelegate.getActivityContext();
                }

                public void startActivityForResult(Intent intent, int i)
                {
                    startActivityDelegate.startActivityForResult(intent, i);
                }
            };

            return new AuthorizationClient.AuthorizationRequest(loginBehavior, requestCode, isLegacy, permissions, defaultAudience, applicationId, validateSameFbidAsToken, startactivitydelegate, authId);
        }

        StatusCallback getCallback()
        {
            return statusCallback;
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

        int getRequestCode()
        {
            return requestCode;
        }

        StartActivityDelegate getStartActivityDelegate()
        {
            return startActivityDelegate;
        }

        String getValidateSameFbidAsToken()
        {
            return validateSameFbidAsToken;
        }

        boolean isLegacy()
        {
            return isLegacy;
        }

        void setApplicationId(String s)
        {
            applicationId = s;
        }

        AuthorizationRequest setCallback(StatusCallback statuscallback)
        {
            statusCallback = statuscallback;
            return this;
        }

        AuthorizationRequest setDefaultAudience(SessionDefaultAudience sessiondefaultaudience)
        {
            if(sessiondefaultaudience != null)
                defaultAudience = sessiondefaultaudience;
            return this;
        }

        public void setIsLegacy(boolean flag)
        {
            isLegacy = flag;
        }

        AuthorizationRequest setLoginBehavior(SessionLoginBehavior sessionloginbehavior)
        {
            if(sessionloginbehavior != null)
                loginBehavior = sessionloginbehavior;
            return this;
        }

        AuthorizationRequest setPermissions(List list)
        {
            if(list != null)
                permissions = list;
            return this;
        }

        AuthorizationRequest setPermissions(String as[])
        {
            return setPermissions(Arrays.asList(as));
        }

        AuthorizationRequest setRequestCode(int i)
        {
            if(i >= 0)
                requestCode = i;
            return this;
        }

        void setValidateSameFbidAsToken(String s)
        {
            validateSameFbidAsToken = s;
        }

        Object writeReplace()
        {
            return new AuthRequestSerializationProxyV1(loginBehavior, requestCode, permissions, defaultAudience.name(), isLegacy, applicationId, validateSameFbidAsToken);
        }

        private static final long serialVersionUID = 1L;
        private String applicationId;
        private final String authId;
        private SessionDefaultAudience defaultAudience;
        private boolean isLegacy;
        private final Map loggingExtras;
        private SessionLoginBehavior loginBehavior;
        private List permissions;
        private int requestCode;
        private final StartActivityDelegate startActivityDelegate;
        private StatusCallback statusCallback;
        private String validateSameFbidAsToken;








        AuthorizationRequest(Activity activity)
        {
            loginBehavior = SessionLoginBehavior.SSO_WITH_FALLBACK;
            requestCode = 64206;
            isLegacy = false;
            permissions = Collections.emptyList();
            defaultAudience = SessionDefaultAudience.FRIENDS;
            authId = UUID.randomUUID().toString();
            loggingExtras = new HashMap();
            startActivityDelegate = new _cls1(activity);
        }

        AuthorizationRequest(Fragment fragment)
        {
            loginBehavior = SessionLoginBehavior.SSO_WITH_FALLBACK;
            requestCode = 64206;
            isLegacy = false;
            permissions = Collections.emptyList();
            defaultAudience = SessionDefaultAudience.FRIENDS;
            authId = UUID.randomUUID().toString();
            loggingExtras = new HashMap();
            startActivityDelegate = new _cls2(fragment);
        }

        private AuthorizationRequest(SessionLoginBehavior sessionloginbehavior, int i, List list, String s, boolean flag, String s1, String s2)
        {
            loginBehavior = SessionLoginBehavior.SSO_WITH_FALLBACK;
            requestCode = 64206;
            isLegacy = false;
            permissions = Collections.emptyList();
            defaultAudience = SessionDefaultAudience.FRIENDS;
            authId = UUID.randomUUID().toString();
            loggingExtras = new HashMap();
            startActivityDelegate = new _cls3();
            loginBehavior = sessionloginbehavior;
            requestCode = i;
            permissions = list;
            defaultAudience = SessionDefaultAudience.valueOf(s);
            isLegacy = flag;
            applicationId = s1;
            validateSameFbidAsToken = s2;
        }


        private static class AuthRequestSerializationProxyV1
        implements Serializable
        {

            private Object readResolve()
            {
                return new AuthorizationRequest(loginBehavior, requestCode, permissions, defaultAudience, isLegacy, applicationId, validateSameFbidAsToken);
            }

            private static final long serialVersionUID = 0x8697a02191312db1L;
            private final String applicationId;
            private final String defaultAudience;
            private boolean isLegacy;
            private final SessionLoginBehavior loginBehavior;
            private final List permissions;
            private final int requestCode;
            private final String validateSameFbidAsToken;

            private AuthRequestSerializationProxyV1(SessionLoginBehavior sessionloginbehavior, int i, List list, String s, boolean flag, String s1, String s2)
            {
                loginBehavior = sessionloginbehavior;
                requestCode = i;
                permissions = list;
                defaultAudience = s;
                isLegacy = flag;
                applicationId = s1;
                validateSameFbidAsToken = s2;
            }

        }


        class _cls1
        implements StartActivityDelegate
        {
            public Activity getActivityContext()
            {
                return activity;
            }

            public void startActivityForResult(Intent intent, int i)
            {
                activity.startActivityForResult(intent, i);
            }

            final Activity activity;
            _cls1(Activity _activity)
            {
                super();
                activity = _activity;
            }
    }


    // Unreferenced inner class Session$AuthorizationRequest$2

    /* anonymous class */
    class _cls2
    implements StartActivityDelegate
    {

        public Activity getActivityContext()
        {
            return fragment.getActivity();
        }

        public void startActivityForResult(Intent intent, int i)
        {
            fragment.startActivityForResult(intent, i);
        }

        final Fragment fragment;

        _cls2(Fragment _fragment)
        {
            super();
            fragment = _fragment;
        }
    }


// Unreferenced inner class Session$AuthorizationRequest$3

/* anonymous class */
        class _cls3
        implements StartActivityDelegate
        {

            public Activity getActivityContext()
            {
            throw new UnsupportedOperationException("Cannot create an AuthorizationRequest without a valid Activity or Fragment");
            }

            public void startActivityForResult(Intent intent, int i)
            {
            throw new UnsupportedOperationException("Cannot create an AuthorizationRequest without a valid Activity or Fragment");
            }


        _cls3()
        {
            super();
        }
        }

    }


    public static final class Builder
    {

        public Session build()
        {
            return new Session(context, applicationId, tokenCachingStrategy);
        }

        public Builder setApplicationId(String s)
        {
            applicationId = s;
            return this;
        }

        public Builder setTokenCachingStrategy(TokenCachingStrategy tokencachingstrategy)
        {
            tokenCachingStrategy = tokencachingstrategy;
            return this;
        }

        private String applicationId;
        private final Context context;
        private TokenCachingStrategy tokenCachingStrategy;

        public Builder(Context context1)
        {
            context = context1;
        }
    }

    public static final class NewPermissionsRequest extends AuthorizationRequest
    {

        AuthorizationClient.AuthorizationRequest getAuthorizationClientRequest()
        {
            AuthorizationClient.AuthorizationRequest authorizationrequest = super.getAuthorizationClientRequest();
            authorizationrequest.setRerequest(true);
            return authorizationrequest;
        }

        /*
        public AuthorizationRequest setCallback(StatusCallback statuscallback)
        {
            return setCallback(statuscallback);
        }
        */

        public final NewPermissionsRequest setCallback(StatusCallback statuscallback)
        {
            super.setCallback(statuscallback);
            return this;
        }

        /*
        public AuthorizationRequest setDefaultAudience(SessionDefaultAudience sessiondefaultaudience)
        {
            return setDefaultAudience(sessiondefaultaudience);
        }
        */

        public final NewPermissionsRequest setDefaultAudience(SessionDefaultAudience sessiondefaultaudience)
        {
            super.setDefaultAudience(sessiondefaultaudience);
            return this;
        }

        /*
        public AuthorizationRequest setLoginBehavior(SessionLoginBehavior sessionloginbehavior)
        {
            return setLoginBehavior(sessionloginbehavior);
        }
        */

        public final NewPermissionsRequest setLoginBehavior(SessionLoginBehavior sessionloginbehavior)
        {
            super.setLoginBehavior(sessionloginbehavior);
            return this;
        }

        /*
        public AuthorizationRequest setRequestCode(int i)
        {
            return setRequestCode(i);
        }
        */

        public final NewPermissionsRequest setRequestCode(int i)
        {
            super.setRequestCode(i);
            return this;
        }

        private static final long serialVersionUID = 1L;

        public NewPermissionsRequest(Activity activity, List list)
        {
            super(activity);
            setPermissions(list);
        }

        public NewPermissionsRequest(Activity activity, String as[])
        {
            super(activity);
            setPermissions(as);
        }

        public NewPermissionsRequest(Fragment fragment, List list)
        {
            super(fragment);
            setPermissions(list);
        }

        public NewPermissionsRequest(Fragment fragment, String as[])
        {
            super(fragment);
            setPermissions(as);
        }
    }

    public static final class OpenRequest extends AuthorizationRequest
    {
        /*
        public AuthorizationRequest setCallback(StatusCallback statuscallback)
        {
            return setCallback(statuscallback);
        }
        */

        public final OpenRequest setCallback(StatusCallback statuscallback)
        {
            super.setCallback(statuscallback);
            return this;
        }

        /*
        public AuthorizationRequest setDefaultAudience(SessionDefaultAudience sessiondefaultaudience)
        {
            return setDefaultAudience(sessiondefaultaudience);
        }
        */

        public final OpenRequest setDefaultAudience(SessionDefaultAudience sessiondefaultaudience)
        {
            super.setDefaultAudience(sessiondefaultaudience);
            return this;
        }
        /*

        public AuthorizationRequest setLoginBehavior(SessionLoginBehavior sessionloginbehavior)
        {
            return setLoginBehavior(sessionloginbehavior);
        }
        */

        public final OpenRequest setLoginBehavior(SessionLoginBehavior sessionloginbehavior)
        {
            super.setLoginBehavior(sessionloginbehavior);
            return this;
        }

        /*
        public AuthorizationRequest setPermissions(List list)
        {
            return setPermissions(list);
        }

        public AuthorizationRequest setPermissions(String as[])
        {
            return setPermissions(as);
        }
*/
        public final OpenRequest setPermissions(List list)
        {
            super.setPermissions(list);
            return this;
        }

        public final OpenRequest setPermissions(String as[])
        {
            super.setPermissions(as);
            return this;
        }

        /*
        public AuthorizationRequest setRequestCode(int i)
        {
            return setRequestCode(i);
        }
        */

        public final OpenRequest setRequestCode(int i)
        {
            super.setRequestCode(i);
            return this;
        }

        private static final long serialVersionUID = 1L;

        public OpenRequest(Activity activity)
        {
            super(activity);
        }

        public OpenRequest(Fragment fragment)
        {
            super(fragment);
        }
    }

    static class PermissionsPair
    {

        public List getDeclinedPermissions()
        {
            return declinedPermissions;
        }

        public List getGrantedPermissions()
        {
            return grantedPermissions;
        }

        List declinedPermissions;
        List grantedPermissions;

        public PermissionsPair(List list, List list1)
        {
            grantedPermissions = list;
            declinedPermissions = list1;
        }
    }

    private static class SerializationProxyV1
        implements Serializable
    {

        private Object readResolve()
        {
            return new Session(applicationId, state, tokenInfo, lastAttemptedTokenExtendDate, shouldAutoPublish, pendingAuthorizationRequest);
        }

        private static final long serialVersionUID = 0x6a59fe98cd935affL;
        private final String applicationId;
        private final Date lastAttemptedTokenExtendDate;
        private final AuthorizationRequest pendingAuthorizationRequest;
        private final boolean shouldAutoPublish;
        private final SessionState state;
        private final AccessToken tokenInfo;

        SerializationProxyV1(String s, SessionState sessionstate, AccessToken accesstoken, Date date, boolean flag, AuthorizationRequest authorizationrequest)
        {
            applicationId = s;
            state = sessionstate;
            tokenInfo = accesstoken;
            lastAttemptedTokenExtendDate = date;
            shouldAutoPublish = flag;
            pendingAuthorizationRequest = authorizationrequest;
        }
    }

    private static class SerializationProxyV2
        implements Serializable
    {

        private Object readResolve()
        {
            return new Session(applicationId, state, tokenInfo, lastAttemptedTokenExtendDate, shouldAutoPublish, pendingAuthorizationRequest, requestedPermissions);
        }

        private static final long serialVersionUID = 0x6a59fe98cd935b00L;
        private final String applicationId;
        private final Date lastAttemptedTokenExtendDate;
        private final AuthorizationRequest pendingAuthorizationRequest;
        private final Set requestedPermissions;
        private final boolean shouldAutoPublish;
        private final SessionState state;
        private final AccessToken tokenInfo;

        SerializationProxyV2(String s, SessionState sessionstate, AccessToken accesstoken, Date date, boolean flag, AuthorizationRequest authorizationrequest, Set set)
        {
            applicationId = s;
            state = sessionstate;
            tokenInfo = accesstoken;
            lastAttemptedTokenExtendDate = date;
            shouldAutoPublish = flag;
            pendingAuthorizationRequest = authorizationrequest;
            requestedPermissions = set;
        }
    }

    static interface StartActivityDelegate
    {

        public abstract Activity getActivityContext();

        public abstract void startActivityForResult(Intent intent, int i);
    }

    public static interface StatusCallback
    {

        public abstract void call(Session session, SessionState sessionstate, Exception exception);
    }

    class TokenRefreshRequest
        implements ServiceConnection
    {

        private void cleanup()
        {
            if(currentTokenRefreshRequest == this)
                currentTokenRefreshRequest = null;
        }

        private void refreshToken()
        {
            Bundle bundle = new Bundle();
            bundle.putString("access_token", getTokenInfo().getToken());
            Message message = Message.obtain();
            message.setData(bundle);
            message.replyTo = messageReceiver;
            try
            {
                messageSender.send(message);
                return;
            }
            catch(RemoteException remoteexception)
            {
                cleanup();
            }
        }

        public void bind()
        {
            Intent intent = NativeProtocol.createTokenRefreshIntent(Session.getStaticContext());
            if(intent != null && Session.staticContext.bindService(intent, this, Context.BIND_AUTO_CREATE))
            {
                setLastAttemptedTokenExtendDate(new Date());
                return;
            } else
            {
                cleanup();
                return;
            }
        }

        public void onServiceConnected(ComponentName componentname, IBinder ibinder)
        {
            messageSender = new Messenger(ibinder);
            refreshToken();
        }

        public void onServiceDisconnected(ComponentName componentname)
        {
            cleanup();
            try
            {
                Session.staticContext.unbindService(this);
                return;
            }
            catch(Exception ex)
            {
                return;
            }
        }

        final Messenger messageReceiver;
        Messenger messageSender;


        TokenRefreshRequest()
        {
            super();
            messageReceiver = new Messenger(new TokenRefreshRequestHandler(Session.this, this));
            messageSender = null;
        }
    }

    static class TokenRefreshRequestHandler extends Handler
    {

        public void handleMessage(Message message)
        {
            String s = message.getData().getString("access_token");
            Session session = (Session)sessionWeakReference.get();
            if(session != null && s != null)
                session.extendTokenCompleted(message.getData());
            TokenRefreshRequest temp = (TokenRefreshRequest)refreshRequestWeakReference.get();
            if(message != null)
            {
                Session.staticContext.unbindService(temp);
                temp.cleanup();
            }
        }

        private WeakReference refreshRequestWeakReference;
        private WeakReference sessionWeakReference;

        TokenRefreshRequestHandler(Session session, TokenRefreshRequest tokenrefreshrequest)
        {
            super(Looper.getMainLooper());
            sessionWeakReference = new WeakReference(session);
            refreshRequestWeakReference = new WeakReference(tokenrefreshrequest);
        }
    }


    public Session(Context context)
    {
        this(context, null, null, true);
    }

    Session(Context context, String s, TokenCachingStrategy tokencachingstrategy)
    {
        this(context, s, tokencachingstrategy, true);
    }

    Session(Context context, String s, TokenCachingStrategy tokencachingstrategy, boolean flag)
    {
        super();
        Object obj = null;
        lastAttemptedTokenExtendDate = new Date(0L);
        lock = new Object();
        String s1 = s;
        if(context != null)
        {
            s1 = s;
            if(s == null)
                s1 = Utility.getMetadataApplicationId(context);
        }
        Validate.notNull(s1, "applicationId");
        initializeStaticContext(context);
        TokenCachingStrategy temp = tokencachingstrategy;
        //SharedPreferencesTokenCachingStrategy temp2 = (SharedPreferencesTokenCachingStrategy)tokencachingstrategy;
        if(temp == null)
            temp = new SharedPreferencesTokenCachingStrategy(staticContext);
        applicationId = s1;
        tokenCachingStrategy = temp;
        state = SessionState.CREATED;
        pendingAuthorizationRequest = null;
        callbacks = new ArrayList();
        handler = new Handler(Looper.getMainLooper());
        Bundle bundle = null;
        if(flag)
            bundle = temp.load();
        if(TokenCachingStrategy.hasTokenInformation(bundle))
        {
            Date tdate = TokenCachingStrategy.getDate(bundle, "com.facebook.TokenCachingStrategy.ExpirationDate");
            Date date = new Date();
            if(tdate == null || tdate.before(date))
            {
                temp.clear();
                tokenInfo = AccessToken.createEmptyToken();
                return;
            } else
            {
                tokenInfo = AccessToken.createFromCache(bundle);
                state = SessionState.CREATED_TOKEN_LOADED;
                return;
            }
        } else
        {
            tokenInfo = AccessToken.createEmptyToken();
            return;
        }
    }

    private Session(String s, SessionState sessionstate, AccessToken accesstoken, Date date, boolean flag, AuthorizationRequest authorizationrequest)
    {
        lastAttemptedTokenExtendDate = new Date(0L);
        lock = new Object();
        applicationId = s;
        state = sessionstate;
        tokenInfo = accesstoken;
        lastAttemptedTokenExtendDate = date;
        pendingAuthorizationRequest = authorizationrequest;
        handler = new Handler(Looper.getMainLooper());
        currentTokenRefreshRequest = null;
        tokenCachingStrategy = null;
        callbacks = new ArrayList();
    }


    private Session(String s, SessionState sessionstate, AccessToken accesstoken, Date date, boolean flag, AuthorizationRequest authorizationrequest, Set set)
    {
        lastAttemptedTokenExtendDate = new Date(0L);
        lock = new Object();
        applicationId = s;
        state = sessionstate;
        tokenInfo = accesstoken;
        lastAttemptedTokenExtendDate = date;
        pendingAuthorizationRequest = authorizationrequest;
        handler = new Handler(Looper.getMainLooper());
        currentTokenRefreshRequest = null;
        tokenCachingStrategy = null;
        callbacks = new ArrayList();
    }


    private static boolean areEqual(Object obj, Object obj1)
    {
        if(obj == null)
            return obj1 == null;
        else
            return obj.equals(obj1);
    }

    private void finishAuthorization(AccessToken accesstoken, Exception exception)
    {
        SessionState sessionstate = state;
        if(accesstoken == null) {
            if(exception != null)
                state = SessionState.CLOSED_LOGIN_FAILED;
        } else {
            tokenInfo = accesstoken;
            saveTokenToCache(accesstoken);
            state = SessionState.OPENED;
        }
        pendingAuthorizationRequest = null;
        postStateChange(sessionstate, state, exception);
        return;
    }

    private void finishReauthorization(AccessToken accesstoken, Exception exception)
    {
        SessionState sessionstate = state;
        if(accesstoken != null)
        {
            tokenInfo = accesstoken;
            saveTokenToCache(accesstoken);
            state = SessionState.OPENED_TOKEN_UPDATED;
        }
        pendingAuthorizationRequest = null;
        postStateChange(sessionstate, state, exception);
    }

    public static final Session getActiveSession()
    {
        Session session;
        synchronized(STATIC_LOCK)
        {
            session = activeSession;
        }
        return session;
    }

    private AppEventsLogger getAppEventsLogger()
    {
        AppEventsLogger appeventslogger;
        synchronized(lock)
        {
            if(appEventsLogger == null)
                appEventsLogger = AppEventsLogger.newLogger(staticContext, applicationId);
            appeventslogger = appEventsLogger;
        }
        return appeventslogger;
    }

    private Intent getLoginActivityIntent(AuthorizationRequest authorizationrequest)
    {
        Intent intent = new Intent();
        intent.setClass(getStaticContext(), LoginActivity.class);
        intent.setAction(authorizationrequest.getLoginBehavior().toString());
        intent.putExtras(LoginActivity.populateIntentExtras(authorizationrequest.getAuthorizationClientRequest()));
        return intent;
    }

    static Context getStaticContext()
    {
        return staticContext;
    }

    private void handleAuthorizationResult(int i, AuthorizationClient.Result result)
    {
        AccessToken accesstoken;
        Object obj;
        Object obj1;
        obj1 = null;
        obj = null;
        if(i != -1) {
            accesstoken = null;
            if(i == 0)
            {
                obj = new FacebookOperationCanceledException(result.errorMessage);
                accesstoken = null;
            }
        } else {
            if (result.code == AuthorizationClient.Result.Code.SUCCESS) {
                accesstoken = result.token;
            } else {
                obj = new FacebookAuthorizationException(result.errorMessage);
                accesstoken = null;
            }
        }
        logAuthorizationComplete(result.code, result.loggingExtras, ((Exception) (obj)));
        authorizationClient = null;
        finishAuthOrReauth(accesstoken, ((Exception) (obj)));
        return;
    }

    static PermissionsPair handlePermissionResponse(Response response)
    {
        Object obj;
        GraphMultiResult temp;
        if(response.getError() == null)
            if((temp = (GraphMultiResult)response.getGraphObjectAs(GraphMultiResult.class)) != null && ((obj = temp.getData()) != null && ((GraphObjectList) (obj)).size() != 0))
            {
                ArrayList temp2 = new ArrayList(((GraphObjectList) (obj)).size());
                ArrayList arraylist = new ArrayList(((GraphObjectList) (obj)).size());
                Object obj1 = (GraphObject)((GraphObjectList) (obj)).get(0);
                if(((GraphObject) (obj1)).getProperty("permission") != null)
                {
                    obj = ((GraphObjectList) (obj)).iterator();
                    do
                    {
                        if(!((Iterator) (obj)).hasNext())
                            break;
                        Object obj2 = (GraphObject)((Iterator) (obj)).next();
                        obj1 = (String)((GraphObject) (obj2)).getProperty("permission");
                        if(!((String) (obj1)).equals("installed"))
                        {
                            obj2 = (String)((GraphObject) (obj2)).getProperty("status");
                            if(((String) (obj2)).equals("granted"))
                                temp2.add(obj1);
                            else
                            if(((String) (obj2)).equals("declined"))
                                arraylist.add(obj1);
                        }
                    } while(true);
                } else
                {
                    Iterator iterator = ((GraphObject) (obj1)).asMap().entrySet().iterator();
                    for(;iterator.hasNext();) {
                        java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
                        if(!((String)entry.getKey()).equals("installed") && ((Integer)entry.getValue()).intValue() == 1)
                            temp2.add(entry.getKey());
                    }
                }
                return new PermissionsPair(temp2, arraylist);
            }
        return null;
    }

    static void initializeStaticContext(Context context)
    {
        if(context != null && staticContext == null)
        {
            Context context1 = context.getApplicationContext();
            if(context1 != null)
                context = context1;
            staticContext = context;
        }
    }

    public static boolean isPublishPermission(String s)
    {
        return s != null && (s.startsWith("publish") || s.startsWith("manage") || OTHER_PUBLISH_PERMISSIONS.contains(s));
    }

    private void logAuthorizationComplete(AuthorizationClient.Result.Code code, Map map, Exception exception)
    {
        Object obj;
        Bundle bundle;
        JSONObject jsonobj = null;
        try {
            if (pendingAuthorizationRequest != null) {
                bundle = AuthorizationClient.newAuthorizationLoggingBundle(pendingAuthorizationRequest.getAuthId());
                if (code != null)
                    bundle.putString("2_result", code.getLoggingValue());
                if (exception != null && exception.getMessage() != null)
                    bundle.putString("5_error_message", exception.getMessage());
                code = null;
                if (!pendingAuthorizationRequest.loggingExtras.isEmpty())
                    jsonobj = new JSONObject(pendingAuthorizationRequest.loggingExtras);

                if (map == null) {
                    if (jsonobj != null) {
                        bundle.putString("6_extras", ((JSONObject) (jsonobj)).toString());
                    }
                } else {
                    if (jsonobj == null)
                        jsonobj = new JSONObject();
                    Iterator iterator = map.entrySet().iterator();
                    for (; iterator.hasNext(); ) {
                        Map.Entry lentry = (java.util.Map.Entry) iterator.next();
                        jsonobj.put((String) lentry.getKey(), lentry.getValue());
                    }
                }
            } else {
                bundle = AuthorizationClient.newAuthorizationLoggingBundle("");
                bundle.putString("2_result", AuthorizationClient.Result.Code.ERROR.getLoggingValue());
                bundle.putString("5_error_message", "Unexpected call to logAuthorizationComplete with null pendingAuthorizationRequest.");
            }
            bundle.putLong("1_timestamp_ms", System.currentTimeMillis());
            getAppEventsLogger().logSdkEvent("fb_mobile_login_complete", null, bundle);
            return;
        } catch(Exception ex) {
            ex.printStackTrace();
        }

    }

    private void logAuthorizationStart()
    {
        Bundle bundle = AuthorizationClient.newAuthorizationLoggingBundle(pendingAuthorizationRequest.getAuthId());
        bundle.putLong("1_timestamp_ms", System.currentTimeMillis());
        try
        {
            JSONObject jsonobject = new JSONObject();
            jsonobject.put("login_behavior", pendingAuthorizationRequest.loginBehavior.toString());
            jsonobject.put("request_code", pendingAuthorizationRequest.requestCode);
            jsonobject.put("is_legacy", pendingAuthorizationRequest.isLegacy);
            jsonobject.put("permissions", TextUtils.join(",", pendingAuthorizationRequest.permissions));
            jsonobject.put("default_audience", pendingAuthorizationRequest.defaultAudience.toString());
            bundle.putString("6_extras", jsonobject.toString());
        }
        catch(JSONException jsonexception) { }
        getAppEventsLogger().logSdkEvent("fb_mobile_login_start", null, bundle);
    }

    private void open(OpenRequest openrequest, SessionAuthorizationType sessionauthorizationtype)
    {
        SessionState sessionstate = state;
            validatePermissions(openrequest, sessionauthorizationtype);
            validateLoginBehavior(openrequest);
            synchronized(lock)
            {
                if(pendingAuthorizationRequest == null) {
                    switch(state.ordinal()) {
                        default:
                        case 2:
                            throw new UnsupportedOperationException("Session: an attempt was made to open an already opened session.");
                        case 1:
                            state = SessionState.OPENING;
                            if (openrequest == null)
                                throw new IllegalArgumentException("openRequest cannot be null when opening a new Session");

                            pendingAuthorizationRequest = openrequest;
                            break;
                        case 3:
                            if (openrequest != null) {
                                if (!Utility.isNullOrEmpty(openrequest.getPermissions()) && !Utility.isSubset(openrequest.getPermissions(), getPermissions()))
                                    pendingAuthorizationRequest = openrequest;
                            }
                            if (pendingAuthorizationRequest != null) {
                                state = SessionState.OPENING;
                            } else {
                                state = SessionState.OPENED;
                            }
                            break;
                    }
                    if(openrequest != null) {
                        addCallback(openrequest.getCallback());
                    }

                    postStateChange(sessionstate, state, null);
                    if(state == SessionState.OPENING)
                    {
                        authorize(openrequest);
                        return;
                    } else
                    {
                        return;
                    }

                }
                postStateChange(state, state, new UnsupportedOperationException("Session: an attempt was made to open a session that has a pending request."));
            }
            return;
    }

    public static Session openActiveSession(Activity activity, boolean flag, StatusCallback statuscallback)
    {
        return openActiveSession(((Context) (activity)), flag, (new OpenRequest(activity)).setCallback(statuscallback));
    }

    public static Session openActiveSession(Activity activity, boolean flag, List list, StatusCallback statuscallback)
    {
        return openActiveSession(((Context) (activity)), flag, (new OpenRequest(activity)).setCallback(statuscallback).setPermissions(list));
    }

    public static Session openActiveSession(Context context, Fragment fragment, boolean flag, StatusCallback statuscallback)
    {
        return openActiveSession(context, flag, (new OpenRequest(fragment)).setCallback(statuscallback));
    }

    public static Session openActiveSession(Context context, Fragment fragment, boolean flag, List list, StatusCallback statuscallback)
    {
        return openActiveSession(context, flag, (new OpenRequest(fragment)).setCallback(statuscallback).setPermissions(list));
    }

    private static Session openActiveSession(Context context, boolean flag, OpenRequest openrequest)
    {
        Session temp = (new Builder(context)).build();
        if(SessionState.CREATED_TOKEN_LOADED.equals(temp.getState()) || flag)
        {
            setActiveSession(temp);
            temp.openForRead(openrequest);
            return temp;
        } else
        {
            return null;
        }
    }

    public static Session openActiveSessionFromCache(Context context)
    {
        return openActiveSession(context, false, ((OpenRequest) (null)));
    }

    public static Session openActiveSessionWithAccessToken(Context context, AccessToken accesstoken, StatusCallback statuscallback)
    {
        Session temp = new Session(context, null, null, false);
        setActiveSession(temp);
        temp.open(accesstoken, statuscallback);
        return temp;
    }

    static void postActiveSessionAction(String s)
    {
        Intent intent = new Intent(s);
        LocalBroadcastManager.getInstance(getStaticContext()).sendBroadcast(intent);
    }

    private void readObject(ObjectInputStream objectinputstream)
        throws InvalidObjectException
    {
        throw new InvalidObjectException("Cannot readObject, serialization proxy required");
    }

    private void requestNewPermissions(NewPermissionsRequest newpermissionsrequest, SessionAuthorizationType sessionauthorizationtype)
    {
        validatePermissions(newpermissionsrequest, sessionauthorizationtype);
        validateLoginBehavior(newpermissionsrequest);
        if(newpermissionsrequest == null)
            throw new UnsupportedOperationException("Session: an attempt was made to request new permissions for a session that has a pending request.");
        sessionauthorizationtype = ((SessionAuthorizationType) (lock));
        if(pendingAuthorizationRequest != null)
            throw new UnsupportedOperationException("Session: an attempt was made to request new permissions for a session that has a pending request.");

        if(!state.isOpened()) {
            if(state.isClosed())
                throw new UnsupportedOperationException("Session: an attempt was made to request new permissions for a session that has been closed.");
            else
                throw new UnsupportedOperationException("Session: an attempt was made to request new permissions for a session that is not currently open.");

        } else {
            pendingAuthorizationRequest = newpermissionsrequest;
            newpermissionsrequest.setValidateSameFbidAsToken(getAccessToken());
            addCallback(newpermissionsrequest.getCallback());
            authorize(newpermissionsrequest);
            return;
        }

    }

    private boolean resolveIntent(Intent intent)
    {
        return getStaticContext().getPackageManager().resolveActivity(intent, 0) != null;
    }

    public static final Session restoreSession(Context context, TokenCachingStrategy tokencachingstrategy, StatusCallback statuscallback, Bundle bundle)
    {
        byte abyte0[];
        if(bundle == null)
            return null;
        abyte0 = bundle.getByteArray("com.facebook.sdk.Session.saveSessionKey");
        if(abyte0 == null)
            return null;

        try {
            Object obj = new ByteArrayInputStream(abyte0);
            Session temp = (Session) (new ObjectInputStream(((java.io.InputStream) (obj)))).readObject();
            initializeStaticContext(context);
            if (tokencachingstrategy == null)
                temp.tokenCachingStrategy = new SharedPreferencesTokenCachingStrategy(context);
            else
                temp.tokenCachingStrategy = tokencachingstrategy;

            if (statuscallback != null)
                ((Session) (temp)).addCallback(statuscallback);
            temp.authorizationBundle = bundle.getBundle("com.facebook.sdk.Session.authBundleKey");
            return ((Session) (temp));
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static void runWithHandlerOrExecutor(Handler handler1, Runnable runnable)
    {
        if(handler1 != null)
        {
            handler1.post(runnable);
            return;
        } else
        {
            Settings.getExecutor().execute(runnable);
            return;
        }
    }

    public static final void saveSession(Session session, Bundle bundle)
    {
        if(bundle != null && session != null && !bundle.containsKey("com.facebook.sdk.Session.saveSessionKey"))
        {
            ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
            try
            {
                (new ObjectOutputStream(bytearrayoutputstream)).writeObject(session);
            }
            // Misplaced declaration of an exception variable
            catch(Exception ex)
            {
                throw new FacebookException("Unable to save session.", ex);
            }
            bundle.putByteArray("com.facebook.sdk.Session.saveSessionKey", bytearrayoutputstream.toByteArray());
            bundle.putBundle("com.facebook.sdk.Session.authBundleKey", session.authorizationBundle);
        }
    }

    private void saveTokenToCache(AccessToken accesstoken)
    {
        if(accesstoken != null && tokenCachingStrategy != null)
            tokenCachingStrategy.save(accesstoken.toCacheBundle());
    }

    public static final void setActiveSession(Session session)
    {
        Object obj = STATIC_LOCK;
        Session session1 = null;
        if(session != activeSession)
            session1 = activeSession;
        if(session1 != null)
            session1.close();
        activeSession = session;
        if(session1 != null)
            postActiveSessionAction("com.facebook.sdk.ACTIVE_SESSION_UNSET");
        if(session != null)
            postActiveSessionAction("com.facebook.sdk.ACTIVE_SESSION_SET");
        if(session.isOpened())
            postActiveSessionAction("com.facebook.sdk.ACTIVE_SESSION_OPENED");
        return;
    }

    private void tryLegacyAuth(AuthorizationRequest authorizationrequest)
    {
        authorizationClient = new AuthorizationClient();
        authorizationClient.setOnCompletedListener(new AuthorizationClient.OnCompletedListener() {

            public void onCompleted(AuthorizationClient.Result result)
            {
                int i;
                if(result.code == AuthorizationClient.Result.Code.CANCEL)
                    i = 0;
                else
                    i = -1;
                handleAuthorizationResult(i, result);
            }

        });
        authorizationClient.setContext(getStaticContext());
        authorizationClient.startOrContinueAuth(authorizationrequest.getAuthorizationClientRequest());
    }

    private boolean tryLoginActivity(AuthorizationRequest authorizationrequest)
    {
        Intent intent = getLoginActivityIntent(authorizationrequest);
        if(!resolveIntent(intent))
            return false;
        try
        {
            authorizationrequest.getStartActivityDelegate().startActivityForResult(intent, authorizationrequest.getRequestCode());
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            return false;
        }
        return true;
    }

    private void validateLoginBehavior(AuthorizationRequest authorizationrequest)
    {
        if(authorizationrequest != null && !authorizationrequest.isLegacy)
        {
            Intent intent = new Intent();
            intent.setClass(getStaticContext(), LoginActivity.class);
            if(!resolveIntent(intent))
                throw new FacebookException(String.format("Cannot use SessionLoginBehavior %s when %s is not declared as an activity in AndroidManifest.xml", new Object[] {
                    authorizationrequest.getLoginBehavior(), LoginActivity.class.getName()
                }));
        }
    }

    private void validatePermissions(AuthorizationRequest authorizationrequest, SessionAuthorizationType sessionauthorizationtype)
    {
        if(authorizationrequest == null || Utility.isNullOrEmpty(authorizationrequest.getPermissions()))
        {
            if(SessionAuthorizationType.PUBLISH.equals(sessionauthorizationtype))
                throw new FacebookException("Cannot request publish or manage authorization with no permissions.");
        } else
        {
            Iterator iterator = authorizationrequest.getPermissions().iterator();
            do
            {
                if(!iterator.hasNext())
                    break;
                String s = (String)iterator.next();
                if(isPublishPermission(s))
                {
                    if(SessionAuthorizationType.READ.equals(sessionauthorizationtype))
                        throw new FacebookException(String.format("Cannot pass a publish or manage permission (%s) to a request for read authorization", new Object[] {
                            s
                        }));
                } else
                if(SessionAuthorizationType.PUBLISH.equals(sessionauthorizationtype))
                    Log.w(TAG, String.format("Should not pass a read permission (%s) to a request for publish or manage authorization", new Object[] {
                        s
                    }));
            } while(true);
        }
    }

    private Object writeReplace()
    {
        return new SerializationProxyV1(applicationId, state, tokenInfo, lastAttemptedTokenExtendDate, false, pendingAuthorizationRequest);
    }

    public final void addCallback(StatusCallback statuscallback)
    {
        List list = callbacks;
        if(statuscallback != null)
            if(!callbacks.contains(statuscallback))
                callbacks.add(statuscallback);
        return;
    }

    void authorize(AuthorizationRequest authorizationrequest)
    {
        authorizationrequest.setApplicationId(applicationId);
        logAuthorizationStart();
        boolean flag1 = tryLoginActivity(authorizationrequest);
        Object obj1 = pendingAuthorizationRequest.loggingExtras;
        Object obj;
        boolean flag;
        if(flag1)
            obj = "1";
        else
            obj = "0";
        ((Map) (obj1)).put("try_login_activity", obj);
        flag = flag1;
        if(!flag1)
        {
            flag = flag1;
            if(authorizationrequest.isLegacy)
            {
                pendingAuthorizationRequest.loggingExtras.put("try_legacy", "1");
                tryLegacyAuth(authorizationrequest);
                flag = true;
            }
        }
        if(!flag) {
            authorizationrequest = ((AuthorizationRequest) (lock));
            obj = state;
            switch (state.ordinal()) {
                default:
                    state = SessionState.CLOSED_LOGIN_FAILED;
                    obj1 = new FacebookException("Log in attempt failed: LoginActivity could not be started, and not legacy request");
                    logAuthorizationComplete(AuthorizationClient.Result.Code.ERROR, null, ((Exception) (obj1)));
                    postStateChange(((SessionState) (obj)), state, ((Exception) (obj1)));
                    break;

                case 6: // '\006'
                case 7: // '\007'
                    return;
            }
        }
        return;
    }

    public final void close()
    {
        Object obj = lock;
        Object obj1 = state;
        switch(state.ordinal()) {
            default:
                break; /* Loop/switch isn't completed */
            case 3:
            case 4:
            case 5:
                state = SessionState.CLOSED;
                postStateChange(((SessionState) (obj1)), state, null);
            return;
            case 1:
            case 2:
                state = SessionState.CLOSED_LOGIN_FAILED;
                postStateChange(((SessionState) (obj1)), state, new FacebookException("Log in attempt aborted."));
        }
    }

    public final void closeAndClearTokenInformation()
    {
        if(tokenCachingStrategy != null)
            tokenCachingStrategy.clear();
        Utility.clearFacebookCookies(staticContext);
        Utility.clearCaches(staticContext);
        close();
    }

    public boolean equals(Object obj)
    {
        if(obj instanceof Session)
            if(areEqual(((Session) (obj = (Session)obj)).applicationId, applicationId) && areEqual(((Session) (obj)).authorizationBundle, authorizationBundle) && areEqual(((Session) (obj)).state, state) && areEqual(((Session) (obj)).getExpirationDate(), getExpirationDate()))
                return true;
        return false;
    }

    void extendAccessToken()
    {
        TokenRefreshRequest tokenrefreshrequest = null;
        Object obj = lock;
        if(currentTokenRefreshRequest == null)
            tokenrefreshrequest = new TokenRefreshRequest();
        currentTokenRefreshRequest = tokenrefreshrequest;
        if(tokenrefreshrequest != null)
            tokenrefreshrequest.bind();
        return;
    }

    void extendAccessTokenIfNeeded()
    {
        if(shouldExtendAccessToken())
            extendAccessToken();
    }

    void extendTokenCompleted(Bundle bundle)
    {
        Object obj = lock;
        SessionState sessionstate = state;
        switch(state.ordinal()) {
            default:
                Log.d(TAG, (new StringBuilder()).append("refreshToken ignored in state ").append(state).toString());
                return;
            case 4:
                state = SessionState.OPENED_TOKEN_UPDATED;
                postStateChange(sessionstate, state, null);
            case 5:
                tokenInfo = AccessToken.createFromRefresh(tokenInfo, bundle);
                if (tokenCachingStrategy != null)
                    tokenCachingStrategy.save(tokenInfo.toCacheBundle());
                return;
        }
    }

    void finishAuthOrReauth(AccessToken accesstoken, Exception exception)
    {
        Object obj;
        AccessToken accesstoken1;
        accesstoken1 = accesstoken;
        obj = exception;
        if(accesstoken != null)
        {
            accesstoken1 = accesstoken;
            obj = exception;
            if(accesstoken.isInvalid())
            {
                accesstoken1 = null;
                obj = new FacebookException("Invalid access token.");
            }
        }
        accesstoken = ((AccessToken) (lock));
        switch(state.ordinal()) {
            default:
                break; /* Loop/switch isn't completed */
            case 1:
            case 3:
            case 6:
            case 7:
                Log.d(TAG, (new StringBuilder()).append("Unexpected call to finishAuthOrReauth in state ").append(state).toString());
                return;
            case 2:
                finishAuthorization(accesstoken1, ((Exception) (obj)));
                return;
            case 4:
            case 5:
                finishReauthorization(accesstoken1, ((Exception) (obj)));
                return;
        }

    }

    public final String getAccessToken()
    {
        Object obj = lock;
        String s = null;
        if(tokenInfo != null) {
            s = tokenInfo.getToken();
        }
        return s;
    }

    public final String getApplicationId()
    {
        return applicationId;
    }

    public final Bundle getAuthorizationBundle()
    {
        Bundle bundle;
        synchronized(lock)
        {
            bundle = authorizationBundle;
        }
        return bundle;
    }

    public final List getDeclinedPermissions()
    {
        Object obj = lock;
        List list = null;
        if(tokenInfo != null) {
            list = tokenInfo.getDeclinedPermissions();
        }
        return list;
    }

    public final Date getExpirationDate()
    {
        Object obj = lock;
        Date date = null;
        if(tokenInfo != null)
            date = tokenInfo.getExpires();

        return date;
    }

    Date getLastAttemptedTokenExtendDate()
    {
        return lastAttemptedTokenExtendDate;
    }

    public final List getPermissions()
    {
        Object obj = lock;
        List list = null;
        if(tokenInfo != null)
            list = tokenInfo.getPermissions();

        return list;
    }

    public final SessionState getState()
    {
        SessionState sessionstate;
        synchronized(lock)
        {
            sessionstate = state;
        }
        return sessionstate;
    }

    AccessToken getTokenInfo()
    {
        return tokenInfo;
    }

    public int hashCode()
    {
        return 0;
    }

    public final boolean isClosed()
    {
        boolean flag;
        synchronized(lock)
        {
            flag = state.isClosed();
        }
        return flag;
    }

    public final boolean isOpened()
    {
        boolean flag;
        synchronized(lock)
        {
            flag = state.isOpened();
        }
        return flag;
    }

    public boolean isPermissionGranted(String s)
    {
        List list = getPermissions();
        if(list != null)
            return list.contains(s);
        else
            return false;
    }

    public final boolean onActivityResult(Activity activity, int i, int j, Intent intent)
    {
        Validate.notNull(activity, "currentActivity");
        initializeStaticContext(activity);
        synchronized(lock)
        {
            if(pendingAuthorizationRequest != null && i == pendingAuthorizationRequest.getRequestCode()) {
                AuthorizationClient.Result.Code code;
                activity = null;
                code = AuthorizationClient.Result.Code.ERROR;
                if(intent != null) {
                    AuthorizationClient.Result result = (AuthorizationClient.Result) intent.getSerializableExtra("com.facebook.LoginActivity:Result");
                    if (result != null) {
                        handleAuthorizationResult(j, result);
                        return true;
                    }
                    if (authorizationClient != null) {
                        authorizationClient.onActivityResult(i, j, intent);
                        return true;
                    }

                    Exception temp = null;
                    if (j == 0) {
                        temp = new FacebookOperationCanceledException("User canceled operation.");
                        code = AuthorizationClient.Result.Code.CANCEL;
                    }

                    if (temp == null)
                        temp = new FacebookException("Unexpected call to Session.onActivityResult");
                    logAuthorizationComplete(code, null, temp);
                    finishAuthOrReauth(null, temp);
                    return true;
                }

            }
        }
        return false;
    }

    public final void open(AccessToken accesstoken, StatusCallback statuscallback)
    {
        Object obj = lock;
        if(pendingAuthorizationRequest != null)
            throw new UnsupportedOperationException("Session: an attempt was made to open a session that has a pending request.");
        if(state.isClosed())
            throw new UnsupportedOperationException("Session: an attempt was made to open a previously-closed session.");
        if(state != SessionState.CREATED && state != SessionState.CREATED_TOKEN_LOADED)
            throw new UnsupportedOperationException("Session: an attempt was made to open an already opened session.");
        if(statuscallback != null) {
            addCallback(statuscallback);
        }
        tokenInfo = accesstoken;
        if(tokenCachingStrategy != null)
            tokenCachingStrategy.save(accesstoken.toCacheBundle());
        state = SessionState.OPENED;
        postStateChange(state, state, null);
    }

    public final void openForPublish(OpenRequest openrequest)
    {
        open(openrequest, SessionAuthorizationType.PUBLISH);
    }

    public final void openForRead(OpenRequest openrequest)
    {
        open(openrequest, SessionAuthorizationType.READ);
    }

    void postStateChange(SessionState sessionstate, final SessionState newState, final Exception exception)
    {
        if(sessionstate != newState || sessionstate == SessionState.OPENED_TOKEN_UPDATED || exception != null)
        {
            if(newState.isClosed())
                tokenInfo = AccessToken.createEmptyToken();
            Runnable runable = new Runnable() {

                public void run() {
                    List list = callbacks;
                    Runnable runnable;
                    for (final Iterator iterator = callbacks.iterator(); iterator.hasNext(); Session.runWithHandlerOrExecutor(handler, runnable)) {
                        runnable = new Runnable() {
                            public void run() {
                                ((StatusCallback) iterator.next()).call(Session.this, newState, exception);
                            }
                        };
                    }
                }
            };
            runWithHandlerOrExecutor(handler, runable);

            if(this == activeSession && sessionstate.isOpened() != newState.isOpened())
                if(newState.isOpened())
                {
                    postActiveSessionAction("com.facebook.sdk.ACTIVE_SESSION_OPENED");
                    return;
                } else
                {
                    postActiveSessionAction("com.facebook.sdk.ACTIVE_SESSION_CLOSED");
                    return;
                }
        }
    }

    public final void refreshPermissions()
    {
        Request request = new Request(this, "me/permissions");
        request.setCallback(new Request.Callback() {

            public void onCompleted(Response response)
            {
                PermissionsPair permissionspair = Session.handlePermissionResponse(response);
                if(permissionspair != null)
                {
                    synchronized(lock)
                    {
                        tokenInfo = AccessToken.createFromTokenWithRefreshedPermissions(tokenInfo, permissionspair.getGrantedPermissions(), permissionspair.getDeclinedPermissions());
                        postStateChange(state, SessionState.OPENED_TOKEN_UPDATED, null);
                    }
                    return;
                } else
                {
                    return;
                }
            }

        });

        request.executeAsync();
    }

    public final void removeCallback(StatusCallback statuscallback)
    {
        synchronized(callbacks)
        {
            callbacks.remove(statuscallback);
        }
        return;
    }

    public final void requestNewPublishPermissions(NewPermissionsRequest newpermissionsrequest)
    {
        requestNewPermissions(newpermissionsrequest, SessionAuthorizationType.PUBLISH);
    }

    public final void requestNewReadPermissions(NewPermissionsRequest newpermissionsrequest)
    {
        requestNewPermissions(newpermissionsrequest, SessionAuthorizationType.READ);
    }

    void setCurrentTokenRefreshRequest(TokenRefreshRequest tokenrefreshrequest)
    {
        currentTokenRefreshRequest = tokenrefreshrequest;
    }

    void setLastAttemptedTokenExtendDate(Date date)
    {
        lastAttemptedTokenExtendDate = date;
    }

    void setTokenInfo(AccessToken accesstoken)
    {
        tokenInfo = accesstoken;
    }

    boolean shouldExtendAccessToken()
    {
        if(currentTokenRefreshRequest == null)
        {
            Date date = new Date();
            if(state.isOpened() && tokenInfo.getSource().canExtendToken() && date.getTime() - lastAttemptedTokenExtendDate.getTime() > 0x36ee80L && date.getTime() - tokenInfo.getLastRefresh().getTime() > 0x5265c00L)
                return true;
        }
        return false;
    }

    public String toString()
    {
        StringBuilder stringbuilder = (new StringBuilder()).append("{Session").append(" state:").append(state).append(", token:");
        Object obj;
        if(tokenInfo == null)
            obj = "null";
        else
            obj = tokenInfo;
        stringbuilder = stringbuilder.append(obj).append(", appId:");
        if(applicationId == null)
            obj = "null";
        else
            obj = applicationId;
        return stringbuilder.append(((String) (obj))).append("}").toString();
    }

    public static final String ACTION_ACTIVE_SESSION_CLOSED = "com.facebook.sdk.ACTIVE_SESSION_CLOSED";
    public static final String ACTION_ACTIVE_SESSION_OPENED = "com.facebook.sdk.ACTIVE_SESSION_OPENED";
    public static final String ACTION_ACTIVE_SESSION_SET = "com.facebook.sdk.ACTIVE_SESSION_SET";
    public static final String ACTION_ACTIVE_SESSION_UNSET = "com.facebook.sdk.ACTIVE_SESSION_UNSET";
    private static final String AUTH_BUNDLE_SAVE_KEY = "com.facebook.sdk.Session.authBundleKey";
    public static final int DEFAULT_AUTHORIZE_ACTIVITY_CODE = 64206;
    private static final String MANAGE_PERMISSION_PREFIX = "manage";
    private static final Set OTHER_PUBLISH_PERMISSIONS = new HashSet() {


            {
                add("ads_management");
                add("create_event");
                add("rsvp_event");
            }
    }
;
    private static final String PUBLISH_PERMISSION_PREFIX = "publish";
    private static final String SESSION_BUNDLE_SAVE_KEY = "com.facebook.sdk.Session.saveSessionKey";
    private static final Object STATIC_LOCK = new Object();
    public static final String TAG = Session.class.getCanonicalName();
    private static final int TOKEN_EXTEND_RETRY_SECONDS = 3600;
    private static final int TOKEN_EXTEND_THRESHOLD_SECONDS = 0x15180;
    public static final String WEB_VIEW_ERROR_CODE_KEY = "com.facebook.sdk.WebViewErrorCode";
    public static final String WEB_VIEW_FAILING_URL_KEY = "com.facebook.sdk.FailingUrl";
    private static Session activeSession;
    private static final long serialVersionUID = 1L;
    private static Context staticContext;
    private AppEventsLogger appEventsLogger;
    private String applicationId;
    private Bundle authorizationBundle;
    private AuthorizationClient authorizationClient;
    private final List callbacks;
    private TokenRefreshRequest currentTokenRefreshRequest;
    private Handler handler;
    private Date lastAttemptedTokenExtendDate;
    private final Object lock;
    private AuthorizationRequest pendingAuthorizationRequest;
    private SessionState state;
    private TokenCachingStrategy tokenCachingStrategy;
    private AccessToken tokenInfo;









/*
    static TokenRefreshRequest access$1602(Session session, TokenRefreshRequest tokenrefreshrequest)
    {
        session.currentTokenRefreshRequest = tokenrefreshrequest;
        return tokenrefreshrequest;
    }

*/




/*
    static AccessToken access$302(Session session, AccessToken accesstoken)
    {
        session.tokenInfo = accesstoken;
        return accesstoken;
    }

*/


    // Unreferenced inner class Session$AuthorizationRequest$1

/* anonymous class */


}
