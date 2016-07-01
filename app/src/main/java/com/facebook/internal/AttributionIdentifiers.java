// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.internal;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Looper;
import android.util.Log;
import com.facebook.FacebookException;

// Referenced classes of package com.facebook.internal:
//            Utility

public class AttributionIdentifiers
{

    public AttributionIdentifiers()
    {
    }

    private static AttributionIdentifiers getAndroidId(Context context)
    {
        AttributionIdentifiers attributionidentifiers = new AttributionIdentifiers();
        if(Looper.myLooper() == Looper.getMainLooper())
            throw new FacebookException("getAndroidId cannot be called on the main thread.");


        Object obj = Utility.getMethodQuietly("com.google.android.gms.common.GooglePlayServicesUtil", "isGooglePlayServicesAvailable", new Class[] {
                Context.class
        });
        if(obj != null) {
            obj = Utility.invokeMethodQuietly(null, ((java.lang.reflect.Method) (obj)), new Object[]{
                    context
            });
            if (obj instanceof Integer && ((Integer) obj).intValue() == 0) {
                obj = Utility.getMethodQuietly("com.google.android.gms.ads.identifier.AdvertisingIdClient", "getAdvertisingIdInfo", new Class[]{
                        Context.class
                });
                if (obj != null) {
                    context = ((Context) (Utility.invokeMethodQuietly(null, ((java.lang.reflect.Method) (obj)), new Object[]{
                            context
                    })));
                    if (context != null) {
                        java.lang.reflect.Method method;
                        obj = Utility.getMethodQuietly(context.getClass(), "getId", new Class[0]);
                        method = Utility.getMethodQuietly(context.getClass(), "isLimitAdTrackingEnabled", new Class[0]);
                        if (obj != null && method != null) {
                            attributionidentifiers.androidAdvertiserId = (String) Utility.invokeMethodQuietly(context, ((java.lang.reflect.Method) (obj)), new Object[0]);
                            attributionidentifiers.limitTracking = ((Boolean) Utility.invokeMethodQuietly(context, method, new Object[0])).booleanValue();
                            return attributionidentifiers;
                        }
                    }
                }
            }
        }

        //Utility.logd("android_id", context);
        return attributionidentifiers;
    }

    public static AttributionIdentifiers getAttributionIdentifiers(Context context)
    {
        Object obj;
        if(recentlyFetchedIdentifiers != null && System.currentTimeMillis() - recentlyFetchedIdentifiers.fetchTime < 0x36ee80L) {
            obj = recentlyFetchedIdentifiers;
            return ((AttributionIdentifiers) (obj));
        }

        Object obj1;
        AttributionIdentifiers attributionidentifiers;
        attributionidentifiers = getAndroidId(context);
        obj1 = null;
        obj = null;
        Cursor cursor = context.getContentResolver().query(ATTRIBUTION_ID_CONTENT_URI, new String[] {
            "aid", "androidid", "limit_tracking"
        }, null, null, null);
        if(cursor == null) {
            if(obj1 != null)
                ((Cursor) (cursor)).close();
            return null;

        }
        obj = context;
        obj1 = context;
        boolean flag = cursor.moveToFirst();
        if(flag) {
            obj = context;
            obj1 = context;
            int i = cursor.getColumnIndex("aid");
            int j = cursor.getColumnIndex("androidid");
            int k = cursor.getColumnIndex("limit_tracking");
            attributionidentifiers.attributionId = cursor.getString(i);
            if(j <= 0 || k <= 0 ||attributionidentifiers.getAndroidAdvertiserId() != null) {
                obj1 = obj;
                Log.d(TAG, (new StringBuilder()).append("Caught unexpected exception in getAttributionId(): ").append(cursor.toString()).toString());
                if(cursor != null)
                    cursor.close();
                return null;
            }
            attributionidentifiers.androidAdvertiserId = cursor.getString(j);
            attributionidentifiers.limitTracking = Boolean.parseBoolean(cursor.getString(k));
            if(cursor != null)
                cursor.close();
            attributionidentifiers.fetchTime = System.currentTimeMillis();
            recentlyFetchedIdentifiers = attributionidentifiers;
            return attributionidentifiers;

        }
        obj = attributionidentifiers;
        if(cursor != null)
        {
            cursor.close();
            return attributionidentifiers;
        }
        return ((AttributionIdentifiers) (obj));



    }

    public String getAndroidAdvertiserId()
    {
        return androidAdvertiserId;
    }

    public String getAttributionId()
    {
        return attributionId;
    }

    public boolean isTrackingLimited()
    {
        return limitTracking;
    }

    private static final String ANDROID_ID_COLUMN_NAME = "androidid";
    private static final String ATTRIBUTION_ID_COLUMN_NAME = "aid";
    private static final Uri ATTRIBUTION_ID_CONTENT_URI = Uri.parse("content://com.facebook.katana.provider.AttributionIdProvider");
    private static final int CONNECTION_RESULT_SUCCESS = 0;
    private static final long IDENTIFIER_REFRESH_INTERVAL_MILLIS = 0x36ee80L;
    private static final String LIMIT_TRACKING_COLUMN_NAME = "limit_tracking";
    private static final String TAG = AttributionIdentifiers.class.getCanonicalName();
    private static AttributionIdentifiers recentlyFetchedIdentifiers;
    private String androidAdvertiserId;
    private String attributionId;
    private long fetchTime;
    private boolean limitTracking;

}
