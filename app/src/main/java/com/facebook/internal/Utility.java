// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.internal;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.*;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import com.facebook.*;
import com.facebook.model.GraphObject;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.json.*;

// Referenced classes of package com.facebook.internal:
//            ImageDownloader, Validate, AttributionIdentifiers

public final class Utility
{
    public static class DialogFeatureConfig
    {

        private static DialogFeatureConfig parseDialogConfig(JSONObject jsonobject)
        {
            String s = jsonobject.optString("name");
            String as[];
            if(!Utility.isNullOrEmpty(s))
                if((as = s.split("\\|")).length == 2)
                {
                    String s1 = as[0];
                    String s2 = as[1];
                    if(!Utility.isNullOrEmpty(s1) && !Utility.isNullOrEmpty(s2))
                    {
                        String s3 = jsonobject.optString("url");
                        Uri uri = null;
                        if(!Utility.isNullOrEmpty(s3))
                            uri = Uri.parse(s3);
                        return new DialogFeatureConfig(s1, s2, uri, parseVersionSpec(jsonobject.optJSONArray("versions")));
                    }
                }
            return null;
        }

        private static int[] parseVersionSpec(JSONArray jsonarray)
        {
            Object obj = null;
            if(jsonarray != null)
            {
                int l = jsonarray.length();
                int ai[] = new int[l];
                int j = 0;
                do
                {
                    obj = ai;
                    if(j >= l)
                        break;
                    int k = jsonarray.optInt(j, -1);
                    int i = k;
                    if(k == -1)
                    {
                        obj = jsonarray.optString(j);
                        i = k;
                        if(!Utility.isNullOrEmpty(((String) (obj))))
                            try
                            {
                                i = Integer.parseInt(((String) (obj)));
                            }
                            // Misplaced declaration of an exception variable
                            catch(Exception ex)
                            {
                                Utility.logd("FacebookSDK", ((Exception) (obj)));
                                i = -1;
                            }
                    }
                    ai[j] = i;
                    j++;
                } while(true);
            }
            return ((int []) (obj));
        }

        public String getDialogName()
        {
            return dialogName;
        }

        public Uri getFallbackUrl()
        {
            return fallbackUrl;
        }

        public String getFeatureName()
        {
            return featureName;
        }

        public int[] getVersionSpec()
        {
            return featureVersionSpec;
        }

        private String dialogName;
        private Uri fallbackUrl;
        private String featureName;
        private int featureVersionSpec[];


        private DialogFeatureConfig(String s, String s1, Uri uri, int ai[])
        {
            dialogName = s;
            featureName = s1;
            fallbackUrl = uri;
            featureVersionSpec = ai;
        }
    }

    public static class FetchedAppSettings
    {

        public Map getDialogConfigurations()
        {
            return dialogConfigMap;
        }

        public String getNuxContent()
        {
            return nuxContent;
        }

        public boolean getNuxEnabled()
        {
            return nuxEnabled;
        }

        public boolean supportsImplicitLogging()
        {
            return supportsImplicitLogging;
        }

        private Map dialogConfigMap;
        private String nuxContent;
        private boolean nuxEnabled;
        private boolean supportsImplicitLogging;

        private FetchedAppSettings(boolean flag, String s, boolean flag1, Map map)
        {
            supportsImplicitLogging = flag;
            nuxContent = s;
            nuxEnabled = flag1;
            dialogConfigMap = map;
        }

    }


    public Utility()
    {
    }

    public static boolean areObjectsEqual(Object obj, Object obj1)
    {
        if(obj == null)
            return obj1 == null;
        else
            return obj.equals(obj1);
    }

    public static ArrayList arrayList(Object aobj[])
    {
        ArrayList arraylist = new ArrayList(aobj.length);
        int j = aobj.length;
        for(int i = 0; i < j; i++)
            arraylist.add(aobj[i]);

        return arraylist;
    }

    public static List asListNoNulls(Object aobj[])
    {
        ArrayList arraylist = new ArrayList();
        int j = aobj.length;
        for(int i = 0; i < j; i++)
        {
            Object obj = aobj[i];
            if(obj != null)
                arraylist.add(obj);
        }

        return arraylist;
    }

    public static Uri buildUri(String s, String s1, Bundle bundle)
    {
        android.net.Uri.Builder builder = new android.net.Uri.Builder();
        builder.scheme("https");
        builder.authority(s);
        builder.path(s1);
        Iterator iterator = bundle.keySet().iterator();
        String temp;
        for(;iterator.hasNext();) {
            temp = (String)iterator.next();
            Object obj = bundle.get(temp);
            if(obj instanceof String)
                builder.appendQueryParameter(temp, (String)obj);
        }
        return builder.build();
    }

    public static void clearCaches(Context context)
    {
        ImageDownloader.clearCache(context);
    }

    private static void clearCookiesForDomain(Context context, String s)
    {
        CookieSyncManager.createInstance(context).sync();
        CookieManager cookieManager= CookieManager.getInstance();
        String s1 = cookieManager.getCookie(s);
        if(s1 == null)
            return;
        String as[] = s1.split(";");
        int j = as.length;
        for(int i = 0; i < j; i++)
        {
            String as1[] = as[i].split("=");
            if(as1.length > 0)
                cookieManager.setCookie(s, (new StringBuilder()).append(as1[0].trim()).append("=;expires=Sat, 1 Jan 2000 00:00:01 UTC;").toString());
        }

        cookieManager.removeExpiredCookie();
    }

    public static void clearFacebookCookies(Context context)
    {
        clearCookiesForDomain(context, "facebook.com");
        clearCookiesForDomain(context, ".facebook.com");
        clearCookiesForDomain(context, "https://facebook.com");
        clearCookiesForDomain(context, "https://.facebook.com");
    }

    public static void closeQuietly(Closeable closeable)
    {
        try {
            if (closeable != null)
                closeable.close();
            return;
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String coerceValueIfNullOrEmpty(String s, String s1)
    {
        if(isNullOrEmpty(s))
            return s1;
        else
            return s;
    }

    static Map convertJSONObjectToHashMap(JSONObject jsonobject)
    {
        HashMap hashmap;
        JSONArray jsonarray;
        int i;
        hashmap = new HashMap();
        jsonarray = jsonobject.names();
        i = 0;
        for(;i < jsonarray.length();) {
			Object obj1;
			String s;
            try
            {

                s = jsonarray.getString(i);
                obj1 = jsonobject.get(s);
                Object obj = obj1;
				if(obj1 instanceof JSONObject)
					obj = convertJSONObjectToHashMap((JSONObject)obj1);
				hashmap.put(s, obj);
			}
			catch(JSONException jsonexception) { }
			i++;
		}
        return hashmap;
    }

    public static void deleteDirectory(File file)
    {
        if(!file.exists())
            return;
        if(file.isDirectory())
        {
            File afile[] = file.listFiles();
            int j = afile.length;
            for(int i = 0; i < j; i++)
                deleteDirectory(afile[i]);

        }
        file.delete();
    }

    public static void disconnectQuietly(URLConnection urlconnection)
    {
        if(urlconnection instanceof HttpURLConnection)
            ((HttpURLConnection)urlconnection).disconnect();
    }

    public static String getActivityName(Context context)
    {
        if(context == null)
            return "null";
        if(context == context.getApplicationContext())
            return "unknown";
        else
            return context.getClass().getSimpleName();
    }

    private static GraphObject getAppSettingsQueryResponse(String s)
    {
        Bundle bundle = new Bundle();
        bundle.putString("fields", TextUtils.join(",", APP_SETTING_FIELDS));
        Request request = Request.newGraphPathRequest(null, s, null);
        request.setSkipClientToken(true);
        request.setParameters(bundle);
        return request.executeAndWait().getGraphObject();
    }

    public static DialogFeatureConfig getDialogFeatureConfig(String s, String s1, String s2)
    {
        FetchedAppSettings temp;
        Map map;
        if(!isNullOrEmpty(s1) && !isNullOrEmpty(s2))
            if((temp = (FetchedAppSettings)fetchedAppSettings.get(s)) != null && (map = (Map)temp.getDialogConfigurations().get(s1)) != null)
                return (DialogFeatureConfig)map.get(s2);
        return null;
    }

    public static String getMetadataApplicationId(Context context)
    {
        Validate.notNull(context, "context");
        Settings.loadDefaultsFromMetadata(context);
        return Settings.getApplicationId();
    }

    public static Method getMethodQuietly(Class class1, String s, Class aclass[])
    {
        try
        {
            Method method = class1.getMethod(s, aclass);
            return method;
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            return null;
        }
    }

    public static Method getMethodQuietly(String s, String s1, Class aclass[])
    {
        try
        {
            Method method = getMethodQuietly(Class.forName(s), s1, aclass);
            return method;
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            return null;
        }
    }

    public static Object getStringPropertyAsJSON(JSONObject jsonobject, String s, String s1)
        throws JSONException
    {
        s = ((String) (jsonobject.opt(s)));

        if(s != null)
        {
            if(s instanceof String)
                jsonobject = ((JSONObject) ((new JSONTokener((String)s)).nextValue()));
        }
        if(jsonobject != null && !(jsonobject instanceof JSONObject)) // && !(jsonobject instanceof JSONArray))
        {
            if(s1 != null)
            {
                JSONObject temp0 = new JSONObject();
                temp0.putOpt(s1, jsonobject);
                return temp0;
            } else
            {
                throw new FacebookException("Got an unexpected non-JSON object.");
            }
        } else
        {
            return jsonobject;
        }
    }

    private static String hashBytes(MessageDigest messagedigest, byte abyte0[])
    {
        messagedigest.update(abyte0);
        byte temp[] = messagedigest.digest();
        StringBuilder stringBuilder = new StringBuilder();
        int j = temp.length;
        for(int i = 0; i < j; i++)
        {
            byte byte0 = temp[i];
            stringBuilder.append(Integer.toHexString(byte0 >> 4 & 0xf));
            stringBuilder.append(Integer.toHexString(byte0 >> 0 & 0xf));
        }

        return abyte0.toString();
    }

    private static String hashWithAlgorithm(String s, String s1)
    {
        return hashWithAlgorithm(s, s1.getBytes());
    }

    private static String hashWithAlgorithm(String s, byte abyte0[])
    {
        try
        {
            MessageDigest temp = MessageDigest.getInstance(s);
            return hashBytes(temp, abyte0);
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            return null;
        }
    }

    public static int[] intersectRanges(int ai[], int ai1[])
    {
        if(ai == null)
            return ai1;
        if(ai1 == null)
            return ai;
        int ai2[] = new int[ai.length + ai1.length];
        int l = 0;
        int k1 = 0;
        int j1 = 0;
        do
        {
            int i = l;
            if(k1 < ai.length)
            {
                i = l;
                if(j1 < ai1.length)
                {
                    int i1 = 0x80000000;
                    int l1 = 0x7fffffff;
                    int k = ai[k1];
                    i = 0x7fffffff;
                    int i2 = ai1[j1];
                    int j = 0x7fffffff;
                    if(k1 < ai.length - 1)
                        i = ai[k1 + 1];
                    if(j1 < ai1.length - 1)
                        j = ai1[j1 + 1];
                    if(k < i2)
                    {
                        if(i > i2)
                        {
                            i1 = i2;
                            if(i > j)
                            {
                                i = j;
                                k = j1 + 2;
                                j = k1;
                            } else
                            {
                                j = k1 + 2;
                                k = j1;
                            }
                        } else
                        {
                            j = k1 + 2;
                            k = j1;
                            i = l1;
                        }
                    } else
                    if(j > k)
                    {
                        i1 = k;
                        if(j > i)
                        {
                            j = k1 + 2;
                            k = j1;
                        } else
                        {
                            i = j;
                            k = j1 + 2;
                            j = k1;
                        }
                    } else
                    {
                        k = j1 + 2;
                        j = k1;
                        i = l1;
                    }
                    k1 = j;
                    j1 = k;
                    if(i1 == 0x80000000)
                        continue;
                    j1 = l + 1;
                    ai2[l] = i1;
                    if(i != 0x7fffffff)
                    {
                        l = j1 + 1;
                        ai2[j1] = i;
                        k1 = j;
                        j1 = k;
                        continue;
                    }
                    i = j1;
                }
            }
            return Arrays.copyOf(ai2, i);
        } while(true);
    }

    public static Object invokeMethodQuietly(Object obj, Method method, Object aobj[])
    {
        try
        {
            obj = method.invoke(obj, aobj);
            return obj;
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            return null;
        }
    }

    public static boolean isNullOrEmpty(String s)
    {
        return s == null || s.length() == 0;
    }

    public static boolean isNullOrEmpty(Collection collection)
    {
        return collection == null || collection.size() == 0;
    }

    public static boolean isSubset(Collection collection, Collection collection1)
    {
        boolean flag = false;
        if(collection1 == null || collection1.size() == 0)
        {
            if(collection == null || collection.size() == 0)
                flag = true;
            return flag;
        }
        collection1 = new HashSet(collection1);
        for(Iterator iterator = collection.iterator(); iterator.hasNext();)
            if(!collection1.contains(iterator.next()))
                return false;

        return true;
    }

    public static void loadAppSettingsAsync(final Context context, final String s)
    {
        if(isNullOrEmpty(s) || fetchedAppSettings.containsKey(s) || initialAppSettingsLoadTask != null)
	        return;

        final String settingKey;
        settingKey = String.format("com.facebook.internal.APP_SETTINGS.%s", new Object[] {
            s
        });
        initialAppSettingsLoadTask = new AsyncTask() {

            protected GraphObject doInBackground(Void avoid[])
            {
                return Utility.getAppSettingsQueryResponse(s);
            }

            protected Object doInBackground(Object aobj[])
            {
                return doInBackground((Void[])aobj);
            }

            protected void onPostExecute(GraphObject graphobject)
            {
                if(graphobject != null)
                {
                    JSONObject jsonObject = graphobject.getInnerJSONObject();
                    Utility.parseAppSettingsFromJSON(s, jsonObject);
                    context.getSharedPreferences("com.facebook.internal.preferences.APP_SETTINGS", 0).edit().putString(settingKey, graphobject.toString()).apply();
                }
                Utility.initialAppSettingsLoadTask = null;
            }

            protected void onPostExecute(Object obj1)
            {
                onPostExecute((GraphObject)obj1);
            }
        };
        initialAppSettingsLoadTask.execute((Void[])null);

        String settings = context.getSharedPreferences("com.facebook.internal.preferences.APP_SETTINGS", 0).getString(((String) (settingKey)), null);
        JSONObject jsonObject = null;
        try {
            if (!isNullOrEmpty(((String) (settings)))) {
                jsonObject = new JSONObject(((String) (settings)));
            }

            if (context != null) {
                parseAppSettingsFromJSON(s, jsonObject);
                return;
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return;
    }

    public static void logd(String s, Exception exception)
    {
        if(Settings.isDebugEnabled() && s != null && exception != null)
            Log.d(s, (new StringBuilder()).append(exception.getClass().getSimpleName()).append(": ").append(exception.getMessage()).toString());
    }

    public static void logd(String s, String s1)
    {
        if(Settings.isDebugEnabled() && s != null && s1 != null)
            Log.d(s, s1);
    }

    public static void logd(String s, String s1, Throwable throwable)
    {
        if(Settings.isDebugEnabled() && !isNullOrEmpty(s))
            Log.d(s, s1, throwable);
    }

    static String md5hash(String s)
    {
        return hashWithAlgorithm("MD5", s);
    }

    private static FetchedAppSettings parseAppSettingsFromJSON(String s, JSONObject jsonobject)
    {
        FetchedAppSettings temp = new FetchedAppSettings(jsonobject.optBoolean("supports_implicit_sdk_logging", false), jsonobject.optString("gdpv4_nux_content", ""), jsonobject.optBoolean("gdpv4_nux_enabled", false), parseDialogConfigurations(jsonobject.optJSONObject("android_dialog_configs")));
        fetchedAppSettings.put(s, temp);
        return temp;
    }

    private static Map parseDialogConfigurations(JSONObject jsonobject)
    {
        HashMap hashmap = new HashMap();
        if(jsonobject != null)
        {
            JSONArray jsonarray = jsonobject.optJSONArray("data");
            if(jsonarray != null)
            {
                int i = 0;
                while(i < jsonarray.length())
                {
                    DialogFeatureConfig dialogfeatureconfig = DialogFeatureConfig.parseDialogConfig(jsonarray.optJSONObject(i));
                    if(dialogfeatureconfig != null)
                    {
                        String s = dialogfeatureconfig.getDialogName();
                        Map map = (Map)hashmap.get(s);
                        if(map == null)
                        {
                            map = new HashMap();
                            hashmap.put(s, jsonobject);
                        }
                        map.put(dialogfeatureconfig.getFeatureName(), dialogfeatureconfig);
                    }
                    i++;
                }
            }
        }
        return hashmap;
    }

    public static Bundle parseUrlQueryString(String s)
    {
        Bundle bundle = new Bundle();
        if(!isNullOrEmpty(s)) {
			int i;
			int j;
			String ss[] = s.split("&");
			j = ss.length;
			i = 0;

			for(;i < j;) {
                try
                {
                    String as[] = ss[i].split("=");
                    if(as.length != 2) {
						if(as.length == 1)
							bundle.putString(URLDecoder.decode(as[0], "UTF-8"), "");
    				} else
	    				bundle.putString(URLDecoder.decode(as[0], "UTF-8"), URLDecoder.decode(as[1], "UTF-8"));

                }
                catch(UnsupportedEncodingException unsupportedencodingexception)
                {
                    logd("FacebookSDK", unsupportedencodingexception);
                }

				i++;
			}
		}
        return bundle;
    }

    public static void putObjectInBundle(Bundle bundle, String s, Object obj)
    {
        if(obj instanceof String)
        {
            bundle.putString(s, (String)obj);
            return;
        }
        if(obj instanceof Parcelable)
        {
            bundle.putParcelable(s, (Parcelable)obj);
            return;
        }
        if(obj instanceof byte[])
        {
            bundle.putByteArray(s, (byte[])(byte[])obj);
            return;
        } else
        {
            throw new FacebookException("attempted to add unsupported type to Bundle");
        }
    }

    public static FetchedAppSettings queryAppSettings(String s, boolean flag)
    {
        if(!flag && fetchedAppSettings.containsKey(s))
            return (FetchedAppSettings)fetchedAppSettings.get(s);
        GraphObject graphobject = getAppSettingsQueryResponse(s);
        if(graphobject == null)
            return null;
        else
            return parseAppSettingsFromJSON(s, graphobject.getInnerJSONObject());
    }

    public static String readStreamToString(InputStream inputstream)
        throws IOException
    {
        Object obj;
        Object obj1;
        obj = null;
        obj1 = null;
        inputstream = new BufferedInputStream(inputstream);
        obj = new InputStreamReader(inputstream);
        char ac[];
        StringBuilder stringBuilder = new StringBuilder();
        ac = new char[2048];

		try {
			for(;;) {
				int i = ((InputStreamReader) (obj)).read(ac);
				if(i != -1) {
					((StringBuilder) (stringBuilder)).append(ac, 0, i);
				} else
                    break;
			}


			closeQuietly(inputstream);
			closeQuietly(((Closeable) (obj)));
            return stringBuilder.toString();
		} catch(IOException ex) {
            throw ex;
		}
    }

    public static boolean safeGetBooleanFromResponse(GraphObject graphobject, String s)
    {
        Object obj = Boolean.valueOf(false);
        if(graphobject != null)
            obj = graphobject.getProperty(s);
        Boolean temp;
        if(!(obj instanceof Boolean))
            temp = Boolean.valueOf(false);
        else
            temp = ((Boolean) (obj));
        return ((Boolean)temp).booleanValue();
    }

    public static String safeGetStringFromResponse(GraphObject graphobject, String s)
    {
        String result = "";
        if(graphobject != null)
            result = (String)graphobject.getProperty(s);
        if(!(result instanceof String))
            result = "";
        return result;
    }

    public static void setAppEventAttributionParameters(GraphObject graphobject, AttributionIdentifiers attributionidentifiers, String s, boolean flag)
    {
        boolean flag2 = true;
        if(attributionidentifiers != null && attributionidentifiers.getAttributionId() != null)
            graphobject.setProperty("attribution", attributionidentifiers.getAttributionId());
        if(attributionidentifiers != null && attributionidentifiers.getAndroidAdvertiserId() != null)
        {
            graphobject.setProperty("advertiser_id", attributionidentifiers.getAndroidAdvertiserId());
            boolean flag1;
            if(!attributionidentifiers.isTrackingLimited())
                flag1 = true;
            else
                flag1 = false;
            graphobject.setProperty("advertiser_tracking_enabled", Boolean.valueOf(flag1));
        }
        graphobject.setProperty("anon_id", s);
        if(!flag)
            flag = flag2;
        else
            flag = false;
        graphobject.setProperty("application_tracking_enabled", Boolean.valueOf(flag));
    }

    public static void setAppEventExtendedDeviceInfoParameters(GraphObject graphobject, Context context)
    {
        String s;
        JSONArray jsonarray;
        String s1;
        int i;
        int j;
        jsonarray = new JSONArray();
        jsonarray.put("a1");
        s1 = context.getPackageName();
        j = -1;
        s = "";
        i = j;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(s1, 0);
            i = j;

            j = ((PackageInfo) (packageInfo)).versionCode;
            i = j;
            String temp = ((PackageInfo) (packageInfo)).versionName;
            i = j;
            jsonarray.put(s1);
            jsonarray.put(i);
            jsonarray.put(temp);
            graphobject.setProperty("extinfo", jsonarray.toString());
            return;
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    static String sha1hash(String s)
    {
        return hashWithAlgorithm("SHA-1", s);
    }

    static String sha1hash(byte abyte0[])
    {
        return hashWithAlgorithm("SHA-1", abyte0);
    }

    public static boolean stringsEqualOrEmpty(String s, String s1)
    {
        boolean flag = TextUtils.isEmpty(s);
        boolean flag1 = TextUtils.isEmpty(s1);
        if(flag && flag1)
            return true;
        if(!flag && !flag1)
            return s.equals(s1);
        else
            return false;
    }

    public static JSONArray tryGetJSONArrayFromResponse(GraphObject graphobject, String s)
    {
        if(graphobject == null)
            return null;
        graphobject = ((GraphObject) (graphobject.getProperty(s)));
        if(!(graphobject instanceof JSONArray))
            return null;
        else
            return (JSONArray)graphobject;
    }

    public static JSONObject tryGetJSONObjectFromResponse(GraphObject graphobject, String s)
    {
        if(graphobject == null)
            return null;
        graphobject = ((GraphObject) (graphobject.getProperty(s)));
        if(!(graphobject instanceof JSONObject))
            return null;
        else
            return (JSONObject)graphobject;
    }

    public static Collection unmodifiableCollection(Object aobj[])
    {
        return Collections.unmodifiableCollection(Arrays.asList(aobj));
    }

    private static final String APPLICATION_FIELDS = "fields";
    private static final String APP_SETTINGS_PREFS_KEY_FORMAT = "com.facebook.internal.APP_SETTINGS.%s";
    private static final String APP_SETTINGS_PREFS_STORE = "com.facebook.internal.preferences.APP_SETTINGS";
    private static final String APP_SETTING_DIALOG_CONFIGS = "android_dialog_configs";
    private static final String APP_SETTING_FIELDS[] = {
        "supports_implicit_sdk_logging", "gdpv4_nux_content", "gdpv4_nux_enabled", "android_dialog_configs"
    };
    private static final String APP_SETTING_NUX_CONTENT = "gdpv4_nux_content";
    private static final String APP_SETTING_NUX_ENABLED = "gdpv4_nux_enabled";
    private static final String APP_SETTING_SUPPORTS_IMPLICIT_SDK_LOGGING = "supports_implicit_sdk_logging";
    public static final int DEFAULT_STREAM_BUFFER_SIZE = 8192;
    private static final String DIALOG_CONFIG_DIALOG_NAME_FEATURE_NAME_SEPARATOR = "\\|";
    private static final String DIALOG_CONFIG_NAME_KEY = "name";
    private static final String DIALOG_CONFIG_URL_KEY = "url";
    private static final String DIALOG_CONFIG_VERSIONS_KEY = "versions";
    private static final String EXTRA_APP_EVENTS_INFO_FORMAT_VERSION = "a1";
    private static final String HASH_ALGORITHM_MD5 = "MD5";
    private static final String HASH_ALGORITHM_SHA1 = "SHA-1";
    static final String LOG_TAG = "FacebookSDK";
    private static final String URL_SCHEME = "https";
    private static final String UTF8 = "UTF-8";
    private static Map fetchedAppSettings = new ConcurrentHashMap();
    private static AsyncTask initialAppSettingsLoadTask;





/*
    static AsyncTask access$202(AsyncTask asynctask)
    {
        initialAppSettingsLoadTask = asynctask;
        return asynctask;
    }

*/
}
