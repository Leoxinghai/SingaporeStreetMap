// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.facebook.internal.AttributionIdentifiers;
import com.facebook.internal.Utility;
import com.facebook.internal.Validate;
import com.facebook.model.GraphObject;
import java.util.Iterator;
import java.util.concurrent.Executor;
import org.json.*;

// Referenced classes of package com.facebook:
//            FacebookException, Settings, AppEventsLogger, Request,
//            Response

public class AppLinkData
{
    public static interface CompletionHandler
    {

        public abstract void onDeferredAppLinkDataFetched(AppLinkData applinkdata);
    }


    private AppLinkData()
    {
    }

    public static AppLinkData createFromActivity(Activity activity)
    {
        Validate.notNull(activity, "activity");
        Intent intent = activity.getIntent();
        Object obj;
        AppLinkData temp = null;
        if(intent == null)
        {
            obj = null;
        } else
        {
            obj = createFromAlApplinkData(intent);
            activity = ((Activity) (obj));
            if(obj == null)
                temp = createFromJson(intent.getStringExtra("com.facebook.platform.APPLINK_ARGS"));
            //obj = activity;
            if(temp == null)
                return createFromUri(intent.getData());
        }
        return ((AppLinkData) (obj));
    }

    private static AppLinkData createFromAlApplinkData(Intent intent)
    {
        Bundle bundle = intent.getBundleExtra("al_applink_data");
        String temp;
        if(bundle == null)
        {
            intent = null;
        } else
        {
            AppLinkData applinkdata = new AppLinkData();
            applinkdata.targetUri = intent.getData();
            if(applinkdata.targetUri == null)
            {
                temp = bundle.getString("target_url");
                if(intent != null)
                    applinkdata.targetUri = Uri.parse(temp);
            }
            applinkdata.argumentBundle = bundle;
            applinkdata.arguments = null;
            bundle = bundle.getBundle("referer_data");
            //temp = applinkdata;
            if(bundle != null)
            {
                applinkdata.ref = bundle.getString("fb_ref");
                return applinkdata;
            }
        }
        return null;
    }

    private static AppLinkData createFromJson(String s)
    {
        if(s == null)
            return null;
        //Object obj;
        try {
            JSONObject temp = new JSONObject(s);
            String stemp = temp.getString("version");
            if (!temp.getJSONObject("bridge_args").getString("method").equals("applink") || !((String) (stemp)).equals("2"))
                return null;

            AppLinkData temp2 = new AppLinkData();
            temp2.arguments = temp.getJSONObject("method_args");
            if (!((AppLinkData) (temp2)).arguments.has("ref")) {
                if (((AppLinkData) (temp2)).arguments.has("referer_data")) {
                    JSONObject temp3 = ((AppLinkData) (temp2)).arguments.getJSONObject("referer_data");
                    if (temp3.has("fb_ref"))
                        temp2.ref = temp3.getString("fb_ref");
                }
            } else
                temp2.ref = ((AppLinkData) (temp2)).arguments.getString("ref");

            if (((AppLinkData) (temp2)).arguments.has("target_url"))
                temp2.targetUri = Uri.parse(((AppLinkData) (temp2)).arguments.getString("target_url"));
            temp2.argumentBundle = toBundle(((AppLinkData) (temp2)).arguments);
            return ((AppLinkData) (temp2));
        } catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    private static AppLinkData createFromUri(Uri uri)
    {
        if(uri == null)
        {
            return null;
        } else
        {
            AppLinkData applinkdata = new AppLinkData();
            applinkdata.targetUri = uri;
            return applinkdata;
        }
    }

    public static void fetchDeferredAppLinkData(Context context, CompletionHandler completionhandler)
    {
        fetchDeferredAppLinkData(context, null, completionhandler);
    }

    public static void fetchDeferredAppLinkData(Context context, String s, CompletionHandler completionhandler)
    {
        Validate.notNull(context, "context");
        Validate.notNull(completionhandler, "completionHandler");
        String s1 = s;
        final Context applicationContext = context;
        final String applicationIdCopy = s;
        final CompletionHandler completionHandler = completionhandler;

        if(s == null)
            s1 = Utility.getMetadataApplicationId(context);
        Validate.notNull(s1, "applicationId");
        context = context.getApplicationContext();
        Settings.getExecutor().execute(new Runnable() {

            public void run() {
                AppLinkData.fetchDeferredAppLinkFromServer(applicationContext, applicationIdCopy, completionHandler);
            }

        });
    }

    private static void fetchDeferredAppLinkFromServer(Context context, String s, CompletionHandler completionhandler)
    {
        Object obj;
        Object obj1;
        Object obj2;
        obj = com.facebook.model.GraphObject.Factory.create();
        ((GraphObject) (obj)).setProperty("event", "DEFERRED_APP_LINK");
        Utility.setAppEventAttributionParameters(((GraphObject) (obj)), AttributionIdentifiers.getAttributionIdentifiers(context), AppEventsLogger.getAnonymousAppDeviceGUID(context), Settings.getLimitEventAndDataUsage(context));
        ((GraphObject) (obj)).setProperty("application_package_name", context.getPackageName());
        String sstemp = String.format("%s/activities", new Object[] {
            s
        });
        obj2 = null;
        obj1 = null;
        context = null;
        JSONObject jsonObject;
        GraphObject temp0 = Request.newPostRequest(null, s, ((GraphObject) (obj)), null).executeAndWait().getGraphObject();
        if(temp0 == null) {
            jsonObject = null;
        } else {
            context = null;
            jsonObject = temp0.getInnerJSONObject();
        }

        //s = null;
        AppLinkData appLinkData = null;
        try {
            if (obj != null) {
                String s3 = ((JSONObject) (jsonObject)).optString("applink_args");
                long l = ((JSONObject) (jsonObject)).optLong("click_time", -1L);
                String s1 = ((JSONObject) (jsonObject)).optString("applink_class");
                String s2 = ((JSONObject) (jsonObject)).optString("applink_url");
                s = null;
                if (!TextUtils.isEmpty(s3)) {
                    appLinkData = createFromJson(s3);
                    if (l != -1L) {
                        context = ((Context) (obj));
                        if (((AppLinkData) (appLinkData)).arguments != null)
                            ((AppLinkData) (appLinkData)).arguments.put("com.facebook.platform.APPLINK_TAP_TIME_UTC", l);
                        if (((AppLinkData) (appLinkData)).argumentBundle != null)
                            ((AppLinkData) (appLinkData)).argumentBundle.putString("com.facebook.platform.APPLINK_TAP_TIME_UTC", Long.toString(l));
                    }

                    if (s1 != null) {
                        context = ((Context) (obj));
                        if (((AppLinkData) (appLinkData)).arguments != null)
                            ((AppLinkData) (appLinkData)).arguments.put("com.facebook.platform.APPLINK_NATIVE_CLASS", s1);
                        if (((AppLinkData) (appLinkData)).argumentBundle != null)
                            ((AppLinkData) (appLinkData)).argumentBundle.putString("com.facebook.platform.APPLINK_NATIVE_CLASS", s1);
                    }

                    s = ((String) (obj));
                    if (s2 != null) {
                        context = ((Context) (obj));
                        if (((AppLinkData) (appLinkData)).arguments == null) {
                            Log.d(TAG, "Unable to put tap time in AppLinkData.arguments");
                            s = ((String) (obj));
                        } else {
                            context = ((Context) (obj));
                            ((AppLinkData) (appLinkData)).arguments.put("com.facebook.platform.APPLINK_NATIVE_URL", s2);
                            s = ((String) (obj));
                            context = ((Context) (obj));
                            if (((AppLinkData) (appLinkData)).argumentBundle != null) {
                                context = ((Context) (obj));
                                ((AppLinkData) (appLinkData)).argumentBundle.putString("com.facebook.platform.APPLINK_NATIVE_URL", s2);
                                s = ((String) (obj));
                            }
                        }
                    }
                }
            }
            completionhandler.onDeferredAppLinkDataFetched(appLinkData);
            return;
        } catch(Exception ex) {
            ex.printStackTrace();
        }

    }

    private static Bundle toBundle(JSONObject jsonobject)
        throws JSONException
    {
        Bundle bundle = new Bundle();
        for(Iterator iterator = jsonobject.keys(); iterator.hasNext();)
        {
            String s = (String)iterator.next();
            Object obj = jsonobject.get(s);
            if(obj instanceof JSONObject)
                bundle.putBundle(s, toBundle((JSONObject)obj));
            else
            if(obj instanceof JSONArray)
            {
                obj = (JSONArray)obj;
                if(((JSONArray) (obj)).length() == 0)
                {
                    bundle.putStringArray(s, new String[0]);
                } else
                {
                    Object aobj[] = ((Object []) (((JSONArray) (obj)).get(0)));
                    if(obj instanceof JSONObject)
                    {
                        aobj = new Bundle[((JSONArray) (obj)).length()];
                        for(int i = 0; i < ((JSONArray) (obj)).length(); i++)
                            aobj[i] = toBundle(((JSONArray) (obj)).getJSONObject(i));

                        bundle.putParcelableArray(s, ((android.os.Parcelable []) (aobj)));
                    } else
                    {
                        if(obj instanceof JSONArray)
                            throw new FacebookException("Nested arrays are not supported.");
                        aobj = new String[((JSONArray) (obj)).length()];
                        for(int j = 0; j < ((JSONArray) (obj)).length(); j++)
                            aobj[j] = ((JSONArray) (obj)).get(j).toString();

                        bundle.putStringArray(s, ((String []) (aobj)));
                    }
                }
            } else
            {
                bundle.putString(s, obj.toString());
            }
        }

        return bundle;
    }

    public Bundle getArgumentBundle()
    {
        return argumentBundle;
    }

    public JSONObject getArguments()
    {
        return arguments;
    }

    public String getRef()
    {
        return ref;
    }

    public Bundle getRefererData()
    {
        if(argumentBundle != null)
            return argumentBundle.getBundle("referer_data");
        else
            return null;
    }

    public Uri getTargetUri()
    {
        return targetUri;
    }

    private static final String APPLINK_BRIDGE_ARGS_KEY = "bridge_args";
    private static final String APPLINK_METHOD_ARGS_KEY = "method_args";
    private static final String APPLINK_VERSION_KEY = "version";
    public static final String ARGUMENTS_NATIVE_CLASS_KEY = "com.facebook.platform.APPLINK_NATIVE_CLASS";
    public static final String ARGUMENTS_NATIVE_URL = "com.facebook.platform.APPLINK_NATIVE_URL";
    public static final String ARGUMENTS_REFERER_DATA_KEY = "referer_data";
    public static final String ARGUMENTS_TAPTIME_KEY = "com.facebook.platform.APPLINK_TAP_TIME_UTC";
    private static final String BRIDGE_ARGS_METHOD_KEY = "method";
    private static final String BUNDLE_AL_APPLINK_DATA_KEY = "al_applink_data";
    static final String BUNDLE_APPLINK_ARGS_KEY = "com.facebook.platform.APPLINK_ARGS";
    private static final String DEFERRED_APP_LINK_ARGS_FIELD = "applink_args";
    private static final String DEFERRED_APP_LINK_CLASS_FIELD = "applink_class";
    private static final String DEFERRED_APP_LINK_CLICK_TIME_FIELD = "click_time";
    private static final String DEFERRED_APP_LINK_EVENT = "DEFERRED_APP_LINK";
    private static final String DEFERRED_APP_LINK_PATH = "%s/activities";
    private static final String DEFERRED_APP_LINK_URL_FIELD = "applink_url";
    private static final String METHOD_ARGS_REF_KEY = "ref";
    private static final String METHOD_ARGS_TARGET_URL_KEY = "target_url";
    private static final String REFERER_DATA_REF_KEY = "fb_ref";
    private static final String TAG = AppLinkData.class.getCanonicalName();
    private Bundle argumentBundle;
    private JSONObject arguments;
    private String ref;
    private Uri targetUri;


}
