// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook;

import android.content.*;
import android.content.pm.*;
import android.database.Cursor;
import android.net.Uri;
import android.os.*;
import android.util.Base64;
import android.util.Log;
import com.facebook.internal.AttributionIdentifiers;
import com.facebook.internal.Utility;
import com.facebook.internal.Validate;
import com.facebook.model.GraphObject;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.json.JSONException;
import org.json.JSONObject;

// Referenced classes of package com.facebook:
//            LoggingBehavior, Response, FacebookRequestError, AppEventsLogger,
//            Request, RequestBatch, BoltsMeasurementEventListener

public final class Settings
{

    public Settings()
    {
    }

    public static final void addLoggingBehavior(LoggingBehavior loggingbehavior)
    {
        synchronized(loggingBehaviors)
        {
            loggingBehaviors.add(loggingbehavior);
        }
        return;
    }

    public static final void clearLoggingBehaviors()
    {
        synchronized(loggingBehaviors)
        {
            loggingBehaviors.clear();
        }
        return;
    }

    public static String getAppVersion()
    {
        return appVersion;
    }

    public static String getApplicationId()
    {
        return applicationId;
    }

    public static String getApplicationSignature(Context context)
    {
        PackageManager packagemanager;
        if(context != null)
            if((packagemanager = context.getPackageManager()) != null)
            {
                String packageName = context.getPackageName();
                Signature asignature[];
                try
                {
                    PackageInfo packageInfo = packagemanager.getPackageInfo(packageName, 64);
                    asignature = ((PackageInfo) (packageInfo)).signatures;
                    if(asignature != null && asignature.length != 0)
                    {
                        MessageDigest messagedigest;
                        try
                        {
                            messagedigest = MessageDigest.getInstance("SHA-1");
                        }
                        // Misplaced declaration of an exception variable
                        catch(Exception ex)
                        {
                            return null;
                        }
                        messagedigest.update(((PackageInfo) (packageInfo)).signatures[0].toByteArray());
                        return Base64.encodeToString(messagedigest.digest(), 9);
                    }
                }
                // Misplaced declaration of an exception variable
                catch(Exception ex)
                {
                    return null;
                }
            }
        return null;
    }

    private static Executor getAsyncTaskExecutor()
    {
        Object obj;
        try
        {
            obj = AsyncTask.class.getField("THREAD_POOL_EXECUTOR");
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            return null;
        }
        try
        {
            obj = ((Field) (obj)).get(null);
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            return null;
        }
        if(obj == null)
            return null;
        if(!(obj instanceof Executor))
            return null;
        else
            return (Executor)obj;
    }

    public static String getAttributionId(ContentResolver contentresolver)
    {
        Object obj;
        Object obj1;
        Object obj2;
        obj2 = null;
        obj1 = null;
        obj = null;
        Cursor temp = contentresolver.query(ATTRIBUTION_ID_CONTENT_URI, new String[] {
            "aid"
        }, null, null, null);
        if(contentresolver != null) {
            boolean flag = temp.moveToFirst();
            if (flag) {
                String s = temp.getString(temp.getColumnIndex("aid"));
                obj = s;
                obj1 = obj;
                if (temp != null) {
                    temp.close();
                    return ((String) (obj));
                }
                return ((String) (obj1));

            }
        }

        obj1 = obj2;
        if(temp != null)
        {
            temp.close();
            obj1 = obj2;
        }

        return ((String) (obj1));

        /*
        contentresolver;
        obj1 = obj;
        Log.d(TAG, (new StringBuilder()).append("Caught unexpected exception in getAttributionId(): ").append(contentresolver.toString()).toString());
        obj1 = obj2;
        if(obj == null) goto _L4; else goto _L5
_L5:
        ((Cursor) (obj)).close();
        return null;
        contentresolver;
        if(obj1 != null)
            ((Cursor) (obj1)).close();
        throw contentresolver;
        */
    }

    public static String getClientToken()
    {
        return appClientToken;
    }

    public static Executor getExecutor()
    {
        Object obj1 = LOCK;
        Executor executor1;
        if(executor == null)
            executor = getAsyncTaskExecutor();

        if(executor == null) {
            executor = new ThreadPoolExecutor(5, 128, 1L, TimeUnit.SECONDS, DEFAULT_WORK_QUEUE, DEFAULT_THREAD_FACTORY);
        }
        return executor;
    }

    public static String getFacebookDomain()
    {
        return facebookDomain;
    }

    public static boolean getLimitEventAndDataUsage(Context context)
    {
        return context.getSharedPreferences("com.facebook.sdk.appEventPreferences", 0).getBoolean("limitEventUsage", false);
    }

    public static final Set getLoggingBehaviors()
    {
        Set set;
        synchronized(loggingBehaviors)
        {
            set = Collections.unmodifiableSet(new HashSet(loggingBehaviors));
        }
        return set;
    }

    public static long getOnProgressThreshold()
    {
        return onProgressThreshold.get();
    }

    public static boolean getPlatformCompatibilityEnabled()
    {
        return platformCompatibilityEnabled;
    }

    public static String getSdkVersion()
    {
        return "3.23.1";
    }

    public static final boolean isDebugEnabled()
    {
        return isDebugEnabled;
    }

    public static final boolean isLoggingBehaviorEnabled(LoggingBehavior loggingbehavior)
    {
        HashSet hashset = loggingBehaviors;
        boolean flag;
        if(isDebugEnabled() && loggingBehaviors.contains(loggingbehavior))
            flag = true;
        else
            flag = false;
        return flag;
    }

    public static final boolean isLoggingEnabled()
    {
        return isDebugEnabled();
    }

    public static void loadDefaultsFromMetadata(Context context)
    {
        defaultsLoaded = true;
        if(context != null)
        {
            try
            {
                ApplicationInfo temp = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
                if(context != null && ((ApplicationInfo) (temp)).metaData != null)
                {
                    if(applicationId == null)
                        applicationId = ((ApplicationInfo) (temp)).metaData.getString("com.facebook.sdk.ApplicationId");
                    if(appClientToken == null)
                    {
                        appClientToken = ((ApplicationInfo) (temp)).metaData.getString("com.facebook.sdk.ClientToken");
                        return;
                    }
                }
            }
            // Misplaced declaration of an exception variable
            catch(Exception ex)
            {
                return;
            }
        }
    }

    static void loadDefaultsFromMetadataIfNeeded(Context context)
    {
        if(!defaultsLoaded)
            loadDefaultsFromMetadata(context);
    }

    static Response publishInstallAndWaitForResponse(Context context, String s)
    {
        try
        {
                if(context == null || s == null)
                {
                        throw new IllegalArgumentException("Both context and applicationId must be non-null");
                }
                String s1;
                String s2;
                String s3;
                SharedPreferences sharedpreferences;
                Object obj;
                long l;
                obj = AttributionIdentifiers.getAttributionIdentifiers(context);
                sharedpreferences = context.getSharedPreferences("com.facebook.sdk.attributionTracking", 0);
                s1 = (new StringBuilder()).append(s).append("ping").toString();
                s2 = (new StringBuilder()).append(s).append("json").toString();
                l = sharedpreferences.getLong(s1, 0L);
                s3 = sharedpreferences.getString(s2, null);
                GraphObject graphobject = com.facebook.model.GraphObject.Factory.create();
                graphobject.setProperty("event", "MOBILE_APP_INSTALL");
                Utility.setAppEventAttributionParameters(graphobject, ((AttributionIdentifiers) (obj)), AppEventsLogger.getAnonymousAppDeviceGUID(context), getLimitEventAndDataUsage(context));
                graphobject.setProperty("application_package_name", context.getPackageName());
                Request request = Request.newPostRequest(null, String.format("%s/activities", new Object[] {
                    s
                }), graphobject, null);

                Response temp = null;
                GraphObject graphObject = null;
                if(l == 0L) {
                    temp = ((Request) (request)).executeAndWait();
                    SharedPreferences.Editor temp2 = sharedpreferences.edit();
                    temp2.putLong(s1, System.currentTimeMillis());
                    if(temp.getGraphObject() != null && temp.getGraphObject().getInnerJSONObject() != null)
                        temp2.putString(s2, temp.getGraphObject().getInnerJSONObject().toString());
                    temp2.apply();
                    return temp;
                } else {
                    s = null;
                    if (s3 != null)
                        graphObject = com.facebook.model.GraphObject.Factory.create(new JSONObject(s3));
                }

                if(temp != null)
                    return new Response(null, null, null, graphObject, true);
                return (Response)Response.createResponsesFromString("true", null, new RequestBatch(new Request[] {
                        request
                }), true).get(0);

        }
        catch(Exception ex)
        {
            Utility.logd("Facebook-publish", ex);
            return new Response(null, null, new FacebookRequestError(null, ex));
        }

    }

    public static void publishInstallAsync(final Context context, final String s, final Request.Callback callback)
    {
        final Context applicationContext = context.getApplicationContext();
        getExecutor().execute(new Runnable() {

            public void run()
            {
                final Response response = Settings.publishInstallAndWaitForResponse(applicationContext, applicationId);
                if(callback != null)
                    (new Handler(Looper.getMainLooper())).post( new Runnable() {

                        public void run()
                        {
                            callback.onCompleted(response);
                        }
                    });
            }
        });
    }

    public static final void removeLoggingBehavior(LoggingBehavior loggingbehavior)
    {
        synchronized(loggingBehaviors)
        {
            loggingBehaviors.remove(loggingbehavior);
        }
        return;
    }

    public static void sdkInitialize(Context context)
    {
        boolean flag = sdkInitialized.booleanValue();
        if(flag)
            return;

        loadDefaultsFromMetadataIfNeeded(context);
        Utility.loadAppSettingsAsync(context, getApplicationId());
        BoltsMeasurementEventListener.getInstance(context.getApplicationContext());
        sdkInitialized = Boolean.valueOf(true);
    }

    public static void setAppVersion(String s)
    {
        appVersion = s;
    }

    public static void setApplicationId(String s)
    {
        applicationId = s;
    }

    public static void setClientToken(String s)
    {
        appClientToken = s;
    }

    public static void setExecutor(Executor executor1)
    {
        Validate.notNull(executor1, "executor");
        synchronized(LOCK)
        {
            executor = executor1;
        }
        return;
    }

    public static void setFacebookDomain(String s)
    {
        Log.w(TAG, "WARNING: Calling setFacebookDomain from non-DEBUG code.");
        facebookDomain = s;
    }

    public static final void setIsDebugEnabled(boolean flag)
    {
        isDebugEnabled = flag;
    }

    public static final void setIsLoggingEnabled(boolean flag)
    {
        setIsDebugEnabled(flag);
    }

    public static void setLimitEventAndDataUsage(Context context, boolean flag)
    {
        context.getSharedPreferences("com.facebook.sdk.appEventPreferences", 0).edit().putBoolean("limitEventUsage", flag).apply();
    }

    public static void setOnProgressThreshold(long l)
    {
        onProgressThreshold.set(l);
    }

    public static void setPlatformCompatibilityEnabled(boolean flag)
    {
        platformCompatibilityEnabled = flag;
    }

    private static final String ANALYTICS_EVENT = "event";
    public static final String APPLICATION_ID_PROPERTY = "com.facebook.sdk.ApplicationId";
    private static final String ATTRIBUTION_ID_COLUMN_NAME = "aid";
    private static final Uri ATTRIBUTION_ID_CONTENT_URI = Uri.parse("content://com.facebook.katana.provider.AttributionIdProvider");
    private static final String ATTRIBUTION_PREFERENCES = "com.facebook.sdk.attributionTracking";
    public static final String CLIENT_TOKEN_PROPERTY = "com.facebook.sdk.ClientToken";
    private static final int DEFAULT_CORE_POOL_SIZE = 5;
    private static final int DEFAULT_KEEP_ALIVE = 1;
    private static final int DEFAULT_MAXIMUM_POOL_SIZE = 128;
    private static final ThreadFactory DEFAULT_THREAD_FACTORY = new ThreadFactory() {

        public Thread newThread(Runnable runnable)
        {
            return new Thread(runnable, (new StringBuilder()).append("FacebookSdk #").append(counter.incrementAndGet()).toString());
        }

        private final AtomicInteger counter = new AtomicInteger(0);

    }
;
    private static final BlockingQueue DEFAULT_WORK_QUEUE = new LinkedBlockingQueue(10);
    private static final String FACEBOOK_COM = "facebook.com";
    private static final Object LOCK = new Object();
    private static final String MOBILE_INSTALL_EVENT = "MOBILE_APP_INSTALL";
    private static final String PUBLISH_ACTIVITY_PATH = "%s/activities";
    private static final String TAG = Settings.class.getCanonicalName();
    private static String appClientToken;
    private static String appVersion;
    private static String applicationId;
    private static boolean defaultsLoaded = false;
    private static Executor executor;
    private static String facebookDomain = "facebook.com";
    private static boolean isDebugEnabled = false;
    private static final HashSet loggingBehaviors;
    private static AtomicLong onProgressThreshold = new AtomicLong(0x10000L);
    private static boolean platformCompatibilityEnabled;
    private static Boolean sdkInitialized = Boolean.valueOf(false);

    static
    {
        loggingBehaviors = new HashSet(Arrays.asList(new LoggingBehavior[] {
            LoggingBehavior.DEVELOPER_ERRORS
        }));
    }
}
