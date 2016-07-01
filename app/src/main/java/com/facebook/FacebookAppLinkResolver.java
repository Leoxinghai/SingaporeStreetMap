// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook;

import android.net.Uri;
import android.os.Bundle;
import bolts.*;
import com.facebook.model.GraphObject;
import java.util.*;
import org.json.*;

// Referenced classes of package com.facebook:
//            Request, Response, FacebookRequestError

public class FacebookAppLinkResolver
    implements AppLinkResolver
{

    public FacebookAppLinkResolver()
    {
    }

    private static bolts.AppLink.Target getAndroidTargetFromJson(JSONObject jsonobject)
    {
        String s = tryGetStringFromJson(jsonobject, "package", null);
        if(s == null)
            return null;
        String s1 = tryGetStringFromJson(jsonobject, "class", null);
        String s2 = tryGetStringFromJson(jsonobject, "app_name", null);
        String s3 = tryGetStringFromJson(jsonobject, "url", null);
        Uri temp = null;
        if(s3 != null)
            temp = Uri.parse(s3);
        return new bolts.AppLink.Target(s, s1, temp, s2);
    }

    private static Uri getWebFallbackUriFromJson(Uri uri, JSONObject jsonobject)
    {
        try {
            JSONObject temp = jsonobject.getJSONObject("web");
            if (!tryGetBooleanFromJson(temp, "should_fallback", true))
                return null;
            Object obj = tryGetStringFromJson(temp, "url", null);
            jsonobject = null;
            if (obj == null)
                return null;
            Uri temp2 = Uri.parse(((String) (obj)));
            obj = jsonobject;
            if (jsonobject == null)
                return uri;
            return ((Uri) (temp2));
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static boolean tryGetBooleanFromJson(JSONObject jsonobject, String s, boolean flag)
    {
        boolean flag1;
        try
        {
            flag1 = jsonobject.getBoolean(s);
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            return flag;
        }
        return flag1;
    }

    private static String tryGetStringFromJson(JSONObject jsonobject, String s, String s1)
    {
        try
        {
            String temp = jsonobject.getString(s);
            return temp;
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            return s1;
        }

    }

    public Task getAppLinkFromUrlInBackground(final Uri uri)
    {
        ArrayList arraylist = new ArrayList();
        arraylist.add(uri);
        return getAppLinkFromUrlsInBackground(arraylist).onSuccess(new Continuation() {

            public AppLink then(Task task)
                throws Exception
            {
                return (AppLink)((Map)task.getResult()).get(uri);
            }
        });
    }

    public Task getAppLinkFromUrlsInBackground(final List taskCompletionSource)
    {
        final HashSet urisToRequest;
        StringBuilder stringbuilder;
        final HashMap appLinkResults = new HashMap();
        urisToRequest = new HashSet();
        stringbuilder = new StringBuilder();
        for(Iterator iterator = taskCompletionSource.iterator(); iterator.hasNext();)
        {
            Uri uri = (Uri)iterator.next();
            AppLink applink;
            synchronized(cachedAppLinks)
            {
                applink = (AppLink)cachedAppLinks.get(uri);
            }
            if(applink != null)
            {
                ((Map) (appLinkResults)).put(uri, applink);
            } else
            {
                if(!urisToRequest.isEmpty())
                    stringbuilder.append(',');
                stringbuilder.append(uri.toString());
                urisToRequest.add(uri);
            }
        }

        if(urisToRequest.isEmpty())
        {
            return Task.forResult(appLinkResults);
        } else
        {
            final Task.TaskCompletionSource temp2 = Task.create();
            Bundle bundle = new Bundle();
            bundle.putString("ids", stringbuilder.toString());
            bundle.putString("fields", String.format("%s.fields(%s,%s)", new Object[]{
                    "app_links", "android", "web"
            }));

            //final Map appLinkResults = map;
            //final bolts.Task.TaskCompletionSource taskCompletionSource = taskcompletionsource;
            //final HashSet urisToRequest = hashset;

            (new Request(null, "", bundle, null, new Request.Callback() {

                public void onCompleted(Response response)
                {
                    Object obj;
                    obj = response.getError();
                    if(obj != null)
                    {
                        temp2.setError(((FacebookRequestError) (obj)).getException());
                        return;
                    }
                    GraphObject graphObject = response.getGraphObject();
                    JSONObject jsonObject;
                    if(graphObject != null)
                        jsonObject = graphObject.getInnerJSONObject();
                    else
                        jsonObject = null;
                    if(graphObject == null)
                    {
                        temp2.setResult(appLinkResults);
                        return;
                    }
                    Iterator iterator = urisToRequest.iterator();

                    try
                    {


//_L2:
                    Uri uri1;
                    for(;iterator.hasNext();) {
                        uri1 = (Uri) ((Iterator) (iterator)).next();
                        if (!jsonObject.has(uri1.toString()))
                            continue;
                        JSONObject jsonobject;
                        Object obj1;
                        ArrayList arraylist;
                        int j;
                        jsonobject = jsonObject.getJSONObject(uri1.toString()).getJSONObject("app_links");
                        obj1 = jsonobject.getJSONArray("android");
                        j = ((JSONArray) (obj1)).length();
                        arraylist = new ArrayList(j);
                        JSONException jsonexception;
                        bolts.AppLink.Target target;
                        for (int i = 0; i < j; i++) {

                            target = FacebookAppLinkResolver.getAndroidTargetFromJson(((JSONArray) (obj1)).getJSONObject(i));
                            if (target != null) {
                                arraylist.add(target);
                            }
                        }
                            obj1 = new AppLink(uri1, arraylist, FacebookAppLinkResolver.getWebFallbackUriFromJson(uri1, jsonobject));
                            appLinkResults.put(uri1, obj1);
                            synchronized (cachedAppLinks) {
                                cachedAppLinks.put(uri1, obj1);
                            }
                        }

                    }
                    catch(JSONException jsonexception) { }
                    temp2.setResult(appLinkResults);
                    return;
                }


            })).executeAsync();
            return temp2.getTask();
        }
    }

    private static final String APP_LINK_ANDROID_TARGET_KEY = "android";
    private static final String APP_LINK_KEY = "app_links";
    private static final String APP_LINK_TARGET_APP_NAME_KEY = "app_name";
    private static final String APP_LINK_TARGET_CLASS_KEY = "class";
    private static final String APP_LINK_TARGET_PACKAGE_KEY = "package";
    private static final String APP_LINK_TARGET_SHOULD_FALLBACK_KEY = "should_fallback";
    private static final String APP_LINK_TARGET_URL_KEY = "url";
    private static final String APP_LINK_WEB_TARGET_KEY = "web";
    private final HashMap cachedAppLinks = new HashMap();



}
