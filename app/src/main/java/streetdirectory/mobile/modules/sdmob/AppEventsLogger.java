// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.modules.sdmob;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import bolts.AppLinks;

import com.facebook.FacebookException;
import com.facebook.FacebookRequestError;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Settings;
import com.facebook.internal.AttributionIdentifiers;
import com.facebook.internal.Logger;
import com.facebook.internal.Utility;
import com.facebook.internal.Validate;
import com.facebook.model.GraphObject;
import com.facebook.FacebookTimeSpentData;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;
import org.json.*;

// Referenced classes of package com.facebook:
//            Session, Settings, LoggingBehavior, Request,
//            Response, FacebookRequestError, FacebookException, FacebookTimeSpentData

public class AppEventsLogger
{
    private static class AccessTokenAppIdPair
        implements Serializable
    {

        private Object writeReplace()
        {
            return new SerializationProxyV1(accessToken, applicationId);
        }

        public boolean equals(Object obj)
        {
            if(obj instanceof AccessTokenAppIdPair)
                if(Utility.areObjectsEqual(((AccessTokenAppIdPair) (obj = (AccessTokenAppIdPair)obj)).accessToken, accessToken) && Utility.areObjectsEqual(((AccessTokenAppIdPair) (obj)).applicationId, applicationId))
                    return true;
            return false;
        }

        String getAccessToken()
        {
            return accessToken;
        }

        String getApplicationId()
        {
            return applicationId;
        }

        public int hashCode()
        {
            int j = 0;
            int i;
            if(accessToken == null)
                i = 0;
            else
                i = accessToken.hashCode();
            if(applicationId != null)
                j = applicationId.hashCode();
            return i ^ j;
        }

        private static final long serialVersionUID = 1L;
        private final String accessToken;
        private final String applicationId;

        AccessTokenAppIdPair(Session session)
        {
            this(session.getAccessToken(), session.getApplicationId());
        }

        AccessTokenAppIdPair(String s, String s1)
        {
            String s2 = s;
            if(Utility.isNullOrEmpty(s))
                s2 = null;
            accessToken = s2;
            applicationId = s1;
        }

        private static class SerializationProxyV1
        implements Serializable
        {

            private Object readResolve()
            {
                return new AccessTokenAppIdPair(accessToken, appId);
            }

            private static final long serialVersionUID = 0xdd772aee317ab613L;
            private final String accessToken;
            private final String appId;

            private SerializationProxyV1(String s, String s1)
            {
                accessToken = s;
                appId = s1;
            }

        }

    }


    static class AppEvent
        implements Serializable
    {

        private void validateIdentifier(String s)
            throws FacebookException
        {
            if(s == null || s.length() == 0 || s.length() > 40)
            {
                String s1 = s;
                if(s == null)
                    s1 = "<None Provided>";
                throw new FacebookException(String.format("Identifier '%s' must be less than %d characters", new Object[] {
                    s1, Integer.valueOf(40)
                }));
            }
            boolean flag;
            synchronized(validatedIdentifiers)
            {
                flag = validatedIdentifiers.contains(s);
            }
            if(!flag)
            {
                if(!s.matches("^[0-9a-zA-Z_]+[0-9a-zA-Z _-]*$"))
                    throw new FacebookException(String.format("Skipping event named '%s' due to illegal name - must be under 40 chars and alphanumeric, _, - or space, and not start with a space or hyphen.", new Object[] {
                            s
                    }));
                synchronized(validatedIdentifiers)
                {
                    validatedIdentifiers.add(s);
                }
            }
            return;
        }

        private Object writeReplace()
        {
            return new SerializationProxyV1(jsonObject.toString(), isImplicit);
        }

        public boolean getIsImplicit()
        {
            return isImplicit;
        }

        public JSONObject getJSONObject()
        {
            return jsonObject;
        }

        public String getName()
        {
            return name;
        }

        public String toString()
        {
            return String.format("\"%s\", implicit: %b, json: %s", new Object[] {
                jsonObject.optString("_eventName"), Boolean.valueOf(isImplicit), jsonObject.toString()
            });
        }

        private static final long serialVersionUID = 1L;
        private static final HashSet validatedIdentifiers = new HashSet();
        private boolean isImplicit;
        private JSONObject jsonObject;
        private String name;


        public AppEvent(Context context1, String s, Double double1, Bundle bundle, boolean flag)
        {
            validateIdentifier(s);
            name = s;
            isImplicit = flag;
            try {
                jsonObject = new JSONObject();
                jsonObject.put("_eventName", s);
                jsonObject.put("_logTime", System.currentTimeMillis() / 1000L);
                jsonObject.put("_ui", Utility.getActivityName(context1));
                if (double1 != null)
                    jsonObject.put("_valueToSum", double1.doubleValue());
                if (isImplicit)
                    jsonObject.put("_implicitlyLogged", "1");
                String temp = Settings.getAppVersion();
                if (temp != null)
                    jsonObject.put("_appVersion", temp);
                if (bundle != null) {
                    Iterator iterator = bundle.keySet().iterator();
                    for (;iterator.hasNext();) {
                        s = (String) iterator.next();
                        validateIdentifier(s);
                        double1 = ((Double) (bundle.get(s)));
                        if ( !(double1 instanceof Number))
                            throw new FacebookException(String.format("Parameter value '%s' for key '%s' should be a string or a numeric type.", new Object[]{
                                    double1, s
                            }));
                        jsonObject.put(s, double1.toString());
                    }
                }

                if (!isImplicit) {
                    Logger.log(LoggingBehavior.APP_EVENTS, "AppEvents", "Created app event '%s'", new Object[]{
                            jsonObject.toString()
                    });
                    return;
                }
                return;


            } catch(Exception ex) {
                Logger.log(LoggingBehavior.APP_EVENTS, "AppEvents", "JSON encoding for app event failed: '%s'", new Object[]{
                        context1.toString()
                });
                jsonObject = null;
            }

/*
            context1;
            Logger.log(LoggingBehavior.APP_EVENTS, "AppEvents", "Invalid app event name or parameter:", new Object[] {
                context1.toString()
            });
            jsonObject = null;
            return;
            */
        }

        private AppEvent(String s, boolean flag)
            throws JSONException
        {
            jsonObject = new JSONObject(s);
            isImplicit = flag;
        }

        private static class SerializationProxyV1
        implements Serializable
        {

            private Object readResolve()
            throws JSONException
            {
                return new AppEvent(jsonString, isImplicit);
            }

            private static final long serialVersionUID = 0xdd772aee317ab613L;
            private final boolean isImplicit;
            private final String jsonString;

            private SerializationProxyV1(String s, boolean flag)
            {
                jsonString = s;
                isImplicit = flag;
            }

        }


    }


    public static enum FlushBehavior
    {
		AUTO ("AUTO", 0),
		EXPLICIT_ONLY("EXPLICIT_ONLY", 1);

		String sType;
		int iType;
        private FlushBehavior(String s, int i)
        {
		sType = s;
		iType = i;
        }
    }

    private static enum FlushReason
    {

        EXPLICIT("EXPLICIT", 0),
        TIMER("TIMER", 1),
        SESSION_CHANGE("SESSION_CHANGE", 2),
        PERSISTED_EVENTS("PERSISTED_EVENTS", 3),
        EVENT_THRESHOLD("EVENT_THRESHOLD", 4),
        EAGER_FLUSHING_EVENT("EAGER_FLUSHING_EVENT", 5);
        String sType;
        int iType;
        private FlushReason(String s, int i)
        {
            sType = s;
            iType = i;
        }
    }

    private static enum FlushResult
    {

        SUCCESS("SUCCESS", 0),
        SERVER_ERROR("SERVER_ERROR", 1),
        NO_CONNECTIVITY("NO_CONNECTIVITY", 2),
        UNKNOWN_ERROR("UNKNOWN_ERROR", 3);

        String sType;
        int iType;
        private FlushResult(String s, int i)
        {
            sType = s;
            iType = i;
        }
    }

    private static class FlushStatistics
    {

        public int numEvents;
        public FlushResult result;

        private FlushStatistics()
        {
            numEvents = 0;
            result = FlushResult.SUCCESS;
        }

    }

    static class PersistedAppSessionInfo
    {

        private static FacebookTimeSpentData getTimeSpentData(Context context1, AccessTokenAppIdPair accesstokenappidpair)
        {
            restoreAppSessionInformation(context1);
            FacebookTimeSpentData facebooktimespentdata = (FacebookTimeSpentData)appSessionInfoMap.get(accesstokenappidpair);
            if(facebooktimespentdata == null)
            {
                facebooktimespentdata = new FacebookTimeSpentData();
                appSessionInfoMap.put(accesstokenappidpair, facebooktimespentdata);
            }
            return facebooktimespentdata;
        }

        static void onResume(Context context1, AccessTokenAppIdPair accesstokenappidpair, AppEventsLogger appeventslogger, long l, String s)
        {
            synchronized(staticLock)
            {
                //getTimeSpentData(context1, accesstokenappidpair).onResume(appeventslogger, l, s);
                onTimeSpentDataUpdate();
            }
            return;
        }

        static void onSuspend(Context context1, AccessTokenAppIdPair accesstokenappidpair, AppEventsLogger appeventslogger, long l)
        {
            synchronized(staticLock)
            {
                //getTimeSpentData(context1, accesstokenappidpair).onSuspend(appeventslogger, l);
                onTimeSpentDataUpdate();
            }
            return;
        }

        private static void onTimeSpentDataUpdate()
        {
            if(!hasChanges)
            {
                hasChanges = true;
                AppEventsLogger.backgroundExecutor.schedule(appSessionInfoFlushRunnable, 30L, TimeUnit.SECONDS);
            }
        }

        private static void restoreAppSessionInformation(Context context1)
        {
            Object obj1;
            Object obj2;
            Object obj3;
            obj3 = null;
            obj1 = null;
            obj2 = null;
            Object obj4 = staticLock;
            boolean flag = isLoaded;
            Object obj = null;
            try {
				if(flag) {
					Utility.closeQuietly(((java.io.Closeable) (obj)));
					context1.deleteFile("AppEventsLogger.persistedsessioninfo");
					if(appSessionInfoMap == null)
						appSessionInfoMap = new HashMap();
					isLoaded = true;
					hasChanges = false;
				} else {
					obj = new ObjectInputStream(context1.openFileInput("AppEventsLogger.persistedsessioninfo"));
					appSessionInfoMap = (HashMap)((ObjectInputStream) (obj)).readObject();
					Logger.log(LoggingBehavior.APP_EVENTS, "AppEvents", "App session info loaded");
					Utility.closeQuietly(((java.io.Closeable) (obj)));
					context1.deleteFile("AppEventsLogger.persistedsessioninfo");
					if(appSessionInfoMap == null)
						appSessionInfoMap = new HashMap();
					isLoaded = true;
					hasChanges = false;
				}
				return;
			} catch(Exception ex) {

				Log.d(AppEventsLogger.TAG, (new StringBuilder()).append("Got unexpected exception: ").append(((Exception) (obj2)).toString()).toString());
				Utility.closeQuietly(((java.io.Closeable) (obj)));
				context1.deleteFile("AppEventsLogger.persistedsessioninfo");
				if(appSessionInfoMap == null)
					appSessionInfoMap = new HashMap();
				isLoaded = true;
				hasChanges = false;
			}
        }

        static void saveAppSessionInformation(Context context1)
        {
            Object obj;
            Object obj1;
            obj = null;
            obj1 = null;
            Object obj2 = staticLock;
            boolean flag = hasChanges;
            ObjectOutputStream objectOutputStream = null;
            try {
				if(flag) {
                    objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(context1.openFileOutput("AppEventsLogger.persistedsessioninfo", 0)));
                    objectOutputStream.writeObject(appSessionInfoMap);
					hasChanges = false;
					Logger.log(LoggingBehavior.APP_EVENTS, "AppEvents", "App session info saved");
					Utility.closeQuietly(objectOutputStream);
				}
				return;
            } catch(Exception ex) {
				obj = context1;
				Log.d(AppEventsLogger.TAG, (new StringBuilder()).append("Got unexpected exception: ").append(((Exception) (obj1)).toString()).toString());
				Utility.closeQuietly(objectOutputStream);
			}
        }

        private static final String PERSISTED_SESSION_INFO_FILENAME = "AppEventsLogger.persistedsessioninfo";
        private static final Runnable appSessionInfoFlushRunnable = new Runnable() {

            public void run()
            {
                PersistedAppSessionInfo.saveAppSessionInformation(AppEventsLogger.applicationContext);
            }

        };
        private static Map appSessionInfoMap;
        private static boolean hasChanges = false;
        private static boolean isLoaded = false;
        private static final Object staticLock = new Object();


        PersistedAppSessionInfo()
        {
        }
    }

    static class PersistedEvents
    {

        public static void persistEvents(Context context1, AccessTokenAppIdPair accesstokenappidpair, SessionEventsState sessioneventsstate)
        {
            HashMap hashmap = new HashMap();
            hashmap.put(accesstokenappidpair, sessioneventsstate);
            persistEvents(context1, ((Map) (hashmap)));
        }

        public static void persistEvents(Context context1, Map map)
        {
            Object obj = staticLock;
            PersistedEvents persistedEvents = readAndClearStore(context1);
            Iterator iterator = map.entrySet().iterator();
            for(;iterator.hasNext();) {
                java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
                List list = ((SessionEventsState)entry.getValue()).getEventsToPersist();
                if(list.size() != 0)
                    persistedEvents.addEvents((AccessTokenAppIdPair)entry.getKey(), list);
            }
            persistedEvents.write();
        }

        public static PersistedEvents readAndClearStore(Context context1)
        {
            synchronized(staticLock)
            {
                PersistedEvents persistedEvents = new PersistedEvents(context1);
                persistedEvents.readAndClearStore();
                return persistedEvents;
            }
        }

        private void readAndClearStore()
        {
            Object obj1;
            Object obj2;
            Object obj4;
            obj4 = null;
            obj1 = null;
            obj2 = null;
            Object obj = null;
            try {
				obj = new ObjectInputStream(new BufferedInputStream(context.openFileInput("AppEventsLogger.persistedevents")));
				obj1 = (HashMap)((ObjectInputStream) (obj)).readObject();
				context.getFileStreamPath("AppEventsLogger.persistedevents").delete();
				persistedEvents = ((HashMap) (obj1));
				Utility.closeQuietly(((java.io.Closeable) (obj)));
				return;
			} catch(Exception ex) {
				Log.d(AppEventsLogger.TAG, (new StringBuilder()).append("Got unexpected exception: ").append(((Exception) (ex)).toString()).toString());
				Utility.closeQuietly(((java.io.Closeable) (obj)));
				return;
			}
        }

        private void write()
        {
            Object obj;
            Object obj2;
            obj = null;
            obj2 = null;
            Object obj1 = null;
            try {
				obj1 = new ObjectOutputStream(new BufferedOutputStream(context.openFileOutput("AppEventsLogger.persistedevents", 0)));
				((ObjectOutputStream) (obj1)).writeObject(persistedEvents);
				Utility.closeQuietly(((java.io.Closeable) (obj1)));
				return;
			} catch(Exception ex) {
				obj = obj1;
				Log.d(AppEventsLogger.TAG, (new StringBuilder()).append("Got unexpected exception: ").append(((Exception) (obj2)).toString()).toString());
				Utility.closeQuietly(((java.io.Closeable) (obj1)));
				return;
			}
        }

        public void addEvents(AccessTokenAppIdPair accesstokenappidpair, List list)
        {
            if(!persistedEvents.containsKey(accesstokenappidpair))
                persistedEvents.put(accesstokenappidpair, new ArrayList());
            ((List)persistedEvents.get(accesstokenappidpair)).addAll(list);
        }

        public List getEvents(AccessTokenAppIdPair accesstokenappidpair)
        {
            return (List)persistedEvents.get(accesstokenappidpair);
        }

        public Set keySet()
        {
            return persistedEvents.keySet();
        }

        static final String PERSISTED_EVENTS_FILENAME = "AppEventsLogger.persistedevents";
        private static Object staticLock = new Object();
        private Context context;
        private HashMap persistedEvents;


        private PersistedEvents(Context context1)
        {
            persistedEvents = new HashMap();
            context = context1;
        }
    }

    static class SessionEventsState
    {

        private byte[] getStringAsByteArray(String s)
        {
            byte temp[] =null;
            try
            {
                temp = s.getBytes("UTF-8");
            }
            // Misplaced declaration of an exception variable
            catch(Exception ex)
            {
                Utility.logd("Encoding exception: ", s);
                return null;
            }
            return temp;
        }

        private void populateRequest(Request request, int i, JSONArray jsonarray, boolean flag)
        {
            Object obj = com.facebook.model.GraphObject.Factory.create();
            ((GraphObject) (obj)).setProperty("event", "CUSTOM_APP_EVENTS");
            if(numSkippedEventsDueToFullBuffer > 0)
                ((GraphObject) (obj)).setProperty("num_skipped_events", Integer.valueOf(i));
            Utility.setAppEventAttributionParameters(((GraphObject) (obj)), attributionIdentifiers, anonymousAppDeviceGUID, flag);
            Bundle bundle;
            try
            {
                Utility.setAppEventExtendedDeviceInfoParameters(((GraphObject) (obj)), AppEventsLogger.applicationContext);
            }
            catch(Exception exception) { }
            ((GraphObject) (obj)).setProperty("application_package_name", packageName);
            request.setGraphObject(((GraphObject) (obj)));
            bundle = request.getParameters();
            obj = bundle;
            if(bundle == null)
                obj = new Bundle();
            String temp = jsonarray.toString();
            if(jsonarray != null)
            {
                ((Bundle) (obj)).putByteArray("custom_events_file", getStringAsByteArray(temp));
                request.setTag(jsonarray);
            }
            request.setParameters(((Bundle) (obj)));
        }

        public void accumulatePersistedEvents(List list)
        {
            accumulatedEvents.addAll(list);
            return;
        }

        public void addEvent(AppEvent appevent)
        {
            if(accumulatedEvents.size() + inFlightEvents.size() < 1000)
                accumulatedEvents.add(appevent);
            else
                numSkippedEventsDueToFullBuffer = numSkippedEventsDueToFullBuffer + 1;
            return;
        }

        public void clearInFlightAndStats(boolean flag)
        {
            if(flag) {
                accumulatedEvents.addAll(inFlightEvents);
                inFlightEvents.clear();
                numSkippedEventsDueToFullBuffer = 0;
                return;
            }
        }

        public int getAccumulatedEventCount()
        {
            int i = accumulatedEvents.size();
            return i;
        }

        public List getEventsToPersist()
        {
            List list;
            list = accumulatedEvents;
            accumulatedEvents = new ArrayList();
            return list;
        }

        public int populateRequest(Request request, boolean flag, boolean flag1)
        {
            JSONArray jsonarray;
            Iterator iterator;
            int i;
            i = numSkippedEventsDueToFullBuffer;
            inFlightEvents.addAll(accumulatedEvents);
            accumulatedEvents.clear();
            jsonarray = new JSONArray();
            iterator = inFlightEvents.iterator();
            AppEvent appevent;
            for(;iterator.hasNext();) {
                appevent = (AppEvent) iterator.next();
                if (flag || !appevent.getIsImplicit())
                    jsonarray.put(appevent.getJSONObject());
            }
            if(jsonarray.length() != 0) {
                populateRequest(request, i, jsonarray, flag1);
                return jsonarray.length();
            }
            return 0;
        }

        public static final String ENCODED_EVENTS_KEY = "encoded_events";
        public static final String EVENT_COUNT_KEY = "event_count";
        public static final String NUM_SKIPPED_KEY = "num_skipped";
        private final int MAX_ACCUMULATED_LOG_EVENTS = 1000;
        private List accumulatedEvents;
        private String anonymousAppDeviceGUID;
        private AttributionIdentifiers attributionIdentifiers;
        private List inFlightEvents;
        private int numSkippedEventsDueToFullBuffer;
        private String packageName;

        public SessionEventsState(AttributionIdentifiers attributionidentifiers, String s, String s1)
        {
            accumulatedEvents = new ArrayList();
            inFlightEvents = new ArrayList();
            attributionIdentifiers = attributionidentifiers;
            packageName = s;
            anonymousAppDeviceGUID = s1;
        }
    }


    private AppEventsLogger(Context context1, String s, Session session)
    {
        Validate.notNull(context1, "context");
        context = context1;
        Session session1 = session;
        if(session == null)
            session1 = Session.getActiveSession();
        if(session1 != null && (s == null || s.equals(session1.getApplicationId())))
        {
            accessTokenAppId = new AccessTokenAppIdPair(session);
        } else
        {
            String temp ="";
            if(s == null)
                temp = Utility.getMetadataApplicationId(context1);
            accessTokenAppId = new AccessTokenAppIdPair(null, temp);
        }
        synchronized(staticLock)
        {
            if(applicationContext == null)
                applicationContext = context1.getApplicationContext();
        }
        initializeTimersIfNeeded();
        return;
    }

    private static int accumulatePersistedEvents()
    {
        PersistedEvents persistedevents = PersistedEvents.readAndClearStore(applicationContext);
        int i = 0;
        for(Iterator iterator = persistedevents.keySet().iterator(); iterator.hasNext();)
        {
            Object obj = (AccessTokenAppIdPair)iterator.next();
            SessionEventsState sessioneventsstate = getSessionEventsState(applicationContext, ((AccessTokenAppIdPair) (obj)));
            obj = persistedevents.getEvents(((AccessTokenAppIdPair) (obj)));
            sessioneventsstate.accumulatePersistedEvents(((List) (obj)));
            i += ((List) (obj)).size();
        }

        return i;
    }

    public static void activateApp(Context context1)
    {
        Settings.sdkInitialize(context1);
        activateApp(context1, Utility.getMetadataApplicationId(context1));
    }

    public static void activateApp(Context context1, String s)
    {
        if(context1 == null || s == null)
            throw new IllegalArgumentException("Both context and applicationId must be non-null");
        long l;
        if(context1 instanceof Activity)
        {
            setSourceApplication((Activity)context1);
        } else
        {
            resetSourceApplication();
            Log.d(AppEventsLogger.class.getName(), "To set source application the context of activateApp must be an instance of Activity");
        }
        Settings.publishInstallAsync(context1, s, null);
        l = System.currentTimeMillis();
        s = getSourceApplication();
        final long eventTime = l;
        final AppEventsLogger logger = new AppEventsLogger(context1, s, null);
        final String sourceApplicationInfo = s;

        backgroundExecutor.execute(new Runnable() {
            public void run()
            {
                logger.logAppSessionResumeEvent(eventTime, sourceApplicationInfo);
            }

        });
    }

    private static FlushStatistics buildAndExecuteRequests(FlushReason flushreason, Set set)
    {
        FlushStatistics flushstatistics = new FlushStatistics();
        boolean flag = Settings.getLimitEventAndDataUsage(applicationContext);
        ArrayList arraylist = new ArrayList();
        Iterator iterator = set.iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            Object obj = (AccessTokenAppIdPair)iterator.next();
            SessionEventsState sessioneventsstate = getSessionEventsState(((AccessTokenAppIdPair) (obj)));
            if(sessioneventsstate != null)
            {
                obj = buildRequestForSession(((AccessTokenAppIdPair) (obj)), sessioneventsstate, flag, flushstatistics);
                if(obj != null)
                    arraylist.add(obj);
            }
        } while(true);
        if(arraylist.size() > 0)
        {
            Logger.log(LoggingBehavior.APP_EVENTS, TAG, "Flushing %d events due to %s.", new Object[] {
                Integer.valueOf(flushstatistics.numEvents), flushreason.toString()
            });
            Iterator iterator1 = arraylist.iterator();
            do
            {
                if(!iterator1.hasNext())
                    break;
                ((Request)iterator1.next()).executeAndWait();
            } while(true);
        } else
        {
            flushstatistics = null;
        }
        return flushstatistics;
    }

    private static Request buildRequestForSession(final AccessTokenAppIdPair accesstokenappidpair, final SessionEventsState sessioneventsstate, boolean flag, final FlushStatistics flushstatistics)
    {
        Object obj = accesstokenappidpair.getApplicationId();
        com.facebook.internal.Utility.FetchedAppSettings fetchedappsettings = Utility.queryAppSettings(((String) (obj)), false);
        final Request request = Request.newPostRequest(null, String.format("%s/activities", new Object[] {
            obj
        }), null, null);
        Bundle bundle = request.getParameters();
        obj = bundle;
        if(bundle == null)
            obj = new Bundle();
        ((Bundle) (obj)).putString("access_token", accesstokenappidpair.getAccessToken());
        request.setParameters(((Bundle) (obj)));
        if(fetchedappsettings == null)
            return null;
        int i = sessioneventsstate.populateRequest(request, fetchedappsettings.supportsImplicitLogging(), flag);
        if(i == 0)
        {
            return null;
        } else
        {
            flushstatistics.numEvents = flushstatistics.numEvents + i;
            request.setCallback(new Request.Callback() {

                public void onCompleted(Response response)
                {
                    AppEventsLogger.handleResponse(accesstokenappidpair, request, response, sessioneventsstate, flushstatistics);
                }

            });
            return request;
        }
    }

    public static void deactivateApp(Context context1)
    {
        deactivateApp(context1, Utility.getMetadataApplicationId(context1));
    }

    public static void deactivateApp(Context context1, String s)
    {
        if(context1 == null || s == null)
        {
            throw new IllegalArgumentException("Both context and applicationId must be non-null");
        } else
        {
            resetSourceApplication();
            final AppEventsLogger appeventslogger = new AppEventsLogger(context1, s, null);
            final long l = System.currentTimeMillis();
            backgroundExecutor.execute(new Runnable() {

                public void run()
                {
                    appeventslogger.logAppSessionSuspendEvent(l);
                }

            });
            return;
        }
    }

    static void eagerFlush()
    {
        if(getFlushBehavior() != FlushBehavior.EXPLICIT_ONLY)
            flush(FlushReason.EAGER_FLUSHING_EVENT);
    }

    private static void flush(final FlushReason flushreason)
    {
        Settings.getExecutor().execute(new Runnable() {

            public void run()
            {
                AppEventsLogger.flushAndWait(flushreason);
            }

        });
    }

    private static void flushAndWait(FlushReason flushreason)
    {
        synchronized(staticLock)
        {
            if(!requestInFlight) {
                HashSet hashset;
                requestInFlight = true;
                hashset = new HashSet(stateMap.keySet());
                accumulatePersistedEvents();
                try
                {
                    FlushStatistics flushStatistics = buildAndExecuteRequests(flushreason, hashset);
                    synchronized(staticLock)
                    {
                        requestInFlight = false;
                    }
                    if(flushreason != null)
                    {
                        Intent intent = new Intent("com.facebook.sdk.APP_EVENTS_FLUSHED");
                        ((Intent) (intent)).putExtra("com.facebook.sdk.APP_EVENTS_NUM_EVENTS_FLUSHED", ((FlushStatistics) (flushStatistics)).numEvents);
                        ((Intent) (intent)).putExtra("com.facebook.sdk.APP_EVENTS_FLUSH_RESULT", ((FlushStatistics) (flushStatistics)).result);
                        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(((Intent) (intent)));
                        return;
                    } else
                    {
                        return;
                    }
                }
                // Misplaced declaration of an exception variable
                catch(Exception ex)
                {
                    Utility.logd(TAG, "Caught unexpected exception while flushing: ", ex);

                }

            }
        }
        return;
    }

    private static void flushIfNecessary()
    {
        synchronized(staticLock)
        {
            if(getFlushBehavior() != FlushBehavior.EXPLICIT_ONLY && getAccumulatedEventCount() > 100)
                flush(FlushReason.EVENT_THRESHOLD);
        }
        return;
    }

    private static int getAccumulatedEventCount()
    {
        Object obj = staticLock;
        int i = 0;
        for(Iterator iterator = stateMap.values().iterator(); iterator.hasNext();)
            i += ((SessionEventsState)iterator.next()).getAccumulatedEventCount();

        return i;
    }

    static String getAnonymousAppDeviceGUID(Context context1)
    {
        if(anonymousAppDeviceGUID == null)
            synchronized(staticLock)
            {
                if(anonymousAppDeviceGUID == null)
                {
                    anonymousAppDeviceGUID = context1.getSharedPreferences("com.facebook.sdk.appEventPreferences", 0).getString("anonymousAppDeviceGUID", null);
                    if(anonymousAppDeviceGUID == null)
                    {
                        anonymousAppDeviceGUID = (new StringBuilder()).append("XZ").append(UUID.randomUUID().toString()).toString();
                        context1.getSharedPreferences("com.facebook.sdk.appEventPreferences", 0).edit().putString("anonymousAppDeviceGUID", anonymousAppDeviceGUID).apply();
                    }
                }
            }
        return anonymousAppDeviceGUID;
    }

    public static FlushBehavior getFlushBehavior()
    {
        FlushBehavior flushbehavior;
        synchronized(staticLock)
        {
            flushbehavior = flushBehavior;
        }
        return flushbehavior;
    }

    public static boolean getLimitEventUsage(Context context1)
    {
        return Settings.getLimitEventAndDataUsage(context1);
    }

    private static SessionEventsState getSessionEventsState(Context context1, AccessTokenAppIdPair accesstokenappidpair)
    {
        AttributionIdentifiers attributionidentifiers;
        SessionEventsState sessioneventsstate = (SessionEventsState)stateMap.get(accesstokenappidpair);
        attributionidentifiers = null;
        if(sessioneventsstate == null)
            attributionidentifiers = AttributionIdentifiers.getAttributionIdentifiers(context1);
        Object obj = staticLock;
        SessionEventsState sessioneventsstate2 = (SessionEventsState)stateMap.get(accesstokenappidpair);
        SessionEventsState sessioneventsstate1;
        sessioneventsstate1 = sessioneventsstate2;
        if(sessioneventsstate2 == null) {
			sessioneventsstate1 = new SessionEventsState(attributionidentifiers, context1.getPackageName(), getAnonymousAppDeviceGUID(context1));
			stateMap.put(accesstokenappidpair, sessioneventsstate1);
		}
		return sessioneventsstate1;
    }

    private static SessionEventsState getSessionEventsState(AccessTokenAppIdPair accesstokenappidpair)
    {
        synchronized (staticLock) {
            SessionEventsState sessionEventsState = (SessionEventsState) stateMap.get(accesstokenappidpair);
            return sessionEventsState;
        }
    }

    static String getSourceApplication()
    {
        String s = "Unclassified";
        if(isOpenedByApplink)
            s = "Applink";
        String s1 = s;
        if(sourceApplication != null)
            s1 = (new StringBuilder()).append(s).append("(").append(sourceApplication).append(")").toString();
        return s1;
    }

    private static void handleResponse(AccessTokenAppIdPair accesstokenappidpair, Request request, Response response, SessionEventsState sessioneventsstate, FlushStatistics flushstatistics)
    {
        FacebookRequestError facebookrequesterror = response.getError();
        String s = "Success";
        FlushResult flushresult = FlushResult.SUCCESS;
        boolean flag;
        if(facebookrequesterror != null)
            if(facebookrequesterror.getErrorCode() == -1)
            {
                s = "Failed: No Connectivity";
                flushresult = FlushResult.NO_CONNECTIVITY;
            } else
            {
                s = String.format("Failed:\n  Response: %s\n  Error %s", new Object[] {
                    response.toString(), facebookrequesterror.toString()
                });
                flushresult = FlushResult.SERVER_ERROR;
            }
        if(Settings.isLoggingBehaviorEnabled(LoggingBehavior.APP_EVENTS))
        {
            String temp = (String)request.getTag();
            try
            {
                temp = (new JSONArray(response)).toString(2);
            }
            // Misplaced declaration of an exception variable
            catch(Exception ex)
            {
                temp = "<Can't encode events for debug logging>";
            }
            Logger.log(LoggingBehavior.APP_EVENTS, TAG, "Flush completed\nParams: %s\n  Result: %s\n  Events JSON: %s", new Object[] {
                request.getGraphObject().toString(), s, response
            });
        }
        if(facebookrequesterror != null)
            flag = true;
        else
            flag = false;
        sessioneventsstate.clearInFlightAndStats(flag);
        if(flushresult == FlushResult.NO_CONNECTIVITY)
            PersistedEvents.persistEvents(applicationContext, accesstokenappidpair, sessioneventsstate);
        if(flushresult != FlushResult.SUCCESS && flushstatistics.result != FlushResult.NO_CONNECTIVITY)
            flushstatistics.result = flushresult;
    }

    private static void initializeTimersIfNeeded()
    {
        synchronized(staticLock)
        {
            if(backgroundExecutor == null) {
                backgroundExecutor = new ScheduledThreadPoolExecutor(1);
                Runnable runnable = new Runnable() {

                    public void run()
                    {
                        if(AppEventsLogger.getFlushBehavior() != FlushBehavior.EXPLICIT_ONLY)
                            AppEventsLogger.flushAndWait(FlushReason.TIMER);
                    }

                }
                ;
                backgroundExecutor.scheduleAtFixedRate(((Runnable) (runnable)), 0L, 15L, TimeUnit.SECONDS);
                runnable = new Runnable() {

                    public void run()
                    {
                        Object obj2 = new HashSet();
                        Object obj1 = AppEventsLogger.staticLock;
                        for(Iterator iterator1 = AppEventsLogger.stateMap.keySet().iterator(); iterator1.hasNext(); ((Set) (obj2)).add(((AccessTokenAppIdPair)iterator1.next()).getApplicationId()));

                        for(Iterator iterator = ((Set) (obj2)).iterator(); iterator.hasNext(); Utility.queryAppSettings((String)iterator.next(), true));
                        return;
                    }

                };
                backgroundExecutor.scheduleAtFixedRate(((Runnable) (runnable)), 0L, 0x15180L, TimeUnit.SECONDS);
                return;

            }
        }
        return;
    }

    private void logAppSessionResumeEvent(long l, String s)
    {
        PersistedAppSessionInfo.onResume(applicationContext, accessTokenAppId, this, l, s);
    }

    private void logAppSessionSuspendEvent(long l)
    {
        PersistedAppSessionInfo.onSuspend(applicationContext, accessTokenAppId, this, l);
    }

    private static void logEvent(final Context context1, final AppEvent appevent, final AccessTokenAppIdPair accessTokenAppId)
    {
        Settings.getExecutor().execute(new Runnable() {

            public void run()
            {
                AppEventsLogger.getSessionEventsState(context1, accessTokenAppId).addEvent(appevent);
                AppEventsLogger.flushIfNecessary();
            }

        }
);
    }

    private void logEvent(String s, Double double1, Bundle bundle, boolean flag)
    {
        AppEvent appEvent = new AppEvent(context, s, double1, bundle, flag);
        logEvent(context, ((AppEvent) (appEvent)), accessTokenAppId);
    }

    public static AppEventsLogger newLogger(Context context1)
    {
        return new AppEventsLogger(context1, null, null);
    }

    public static AppEventsLogger newLogger(Context context1, Session session)
    {
        return new AppEventsLogger(context1, null, session);
    }

    public static AppEventsLogger newLogger(Context context1, String s)
    {
        return new AppEventsLogger(context1, s, null);
    }

    public static AppEventsLogger newLogger(Context context1, String s, Session session)
    {
        return new AppEventsLogger(context1, s, session);
    }

    private static void notifyDeveloperError(String s)
    {
        Logger.log(LoggingBehavior.DEVELOPER_ERRORS, "AppEvents", s);
    }

    public static void onContextStop()
    {
        PersistedEvents.persistEvents(applicationContext, stateMap);
    }

    static void resetSourceApplication()
    {
        sourceApplication = null;
        isOpenedByApplink = false;
    }

    public static void setFlushBehavior(FlushBehavior flushbehavior)
    {
        synchronized(staticLock)
        {
            flushBehavior = flushbehavior;
        }
        return;
    }

    public static void setLimitEventUsage(Context context1, boolean flag)
    {
        Settings.setLimitEventAndDataUsage(context1, flag);
    }

    private static void setSourceApplication(Activity activity)
    {
        Object obj = activity.getCallingActivity();
        if(obj != null)
        {
            obj = ((ComponentName) (obj)).getPackageName();
            if(((String) (obj)).equals(activity.getPackageName()))
            {
                resetSourceApplication();
                return;
            }
            sourceApplication = ((String) (obj));
        }
        Intent intent = activity.getIntent();
        if(intent == null || intent.getBooleanExtra("_fbSourceApplicationHasBeenSet", false))
        {
            resetSourceApplication();
            return;
        }
        obj = AppLinks.getAppLinkData(intent);
        if(obj == null)
        {
            resetSourceApplication();
            return;
        }
        isOpenedByApplink = true;
        obj = ((Bundle) (obj)).getBundle("referer_app_link");
        if(obj == null)
        {
            sourceApplication = null;
            return;
        } else
        {
            sourceApplication = ((Bundle) (obj)).getString("package");
            intent.putExtra("_fbSourceApplicationHasBeenSet", true);
            return;
        }
    }

    static void setSourceApplication(String s, boolean flag)
    {
        sourceApplication = s;
        isOpenedByApplink = flag;
    }

    public void flush()
    {
        flush(FlushReason.EXPLICIT);
    }

    public String getApplicationId()
    {
        return accessTokenAppId.getApplicationId();
    }

    boolean isValidForSession(Session session)
    {
        AccessTokenAppIdPair accessTokenAppIdPair = new AccessTokenAppIdPair(session);
        return accessTokenAppId.equals(accessTokenAppIdPair);
    }

    public void logEvent(String s)
    {
        logEvent(s, ((Bundle) (null)));
    }

    public void logEvent(String s, double d)
    {
        logEvent(s, d, ((Bundle) (null)));
    }

    public void logEvent(String s, double d, Bundle bundle)
    {
        logEvent(s, Double.valueOf(d), bundle, false);
    }

    public void logEvent(String s, Bundle bundle)
    {
        logEvent(s, null, bundle, false);
    }

    public void logPurchase(BigDecimal bigdecimal, Currency currency)
    {
        logPurchase(bigdecimal, currency, null);
    }

    public void logPurchase(BigDecimal bigdecimal, Currency currency, Bundle bundle)
    {
        if(bigdecimal == null)
        {
            notifyDeveloperError("purchaseAmount cannot be null");
            return;
        }
        if(currency == null)
        {
            notifyDeveloperError("currency cannot be null");
            return;
        }
        Bundle bundle1 = bundle;
        if(bundle == null)
            bundle1 = new Bundle();
        bundle1.putString("fb_currency", currency.getCurrencyCode());
        logEvent("fb_mobile_purchase", bigdecimal.doubleValue(), bundle1);
        eagerFlush();
    }

    public void logSdkEvent(String s, Double double1, Bundle bundle)
    {
        logEvent(s, double1, bundle, true);
    }

    public static final String ACTION_APP_EVENTS_FLUSHED = "com.facebook.sdk.APP_EVENTS_FLUSHED";
    public static final String APP_EVENTS_EXTRA_FLUSH_RESULT = "com.facebook.sdk.APP_EVENTS_FLUSH_RESULT";
    public static final String APP_EVENTS_EXTRA_NUM_EVENTS_FLUSHED = "com.facebook.sdk.APP_EVENTS_NUM_EVENTS_FLUSHED";
    static final String APP_EVENT_PREFERENCES = "com.facebook.sdk.appEventPreferences";
    private static final int APP_SUPPORTS_ATTRIBUTION_ID_RECHECK_PERIOD_IN_SECONDS = 0x15180;
    private static final int FLUSH_APP_SESSION_INFO_IN_SECONDS = 30;
    private static final int FLUSH_PERIOD_IN_SECONDS = 15;
    private static final int NUM_LOG_EVENTS_TO_TRY_TO_FLUSH_AFTER = 100;
    private static final String SOURCE_APPLICATION_HAS_BEEN_SET_BY_THIS_INTENT = "_fbSourceApplicationHasBeenSet";
    private static final String TAG = AppEventsLogger.class.getCanonicalName();
    private static String anonymousAppDeviceGUID;
    private static Context applicationContext;
    private static ScheduledThreadPoolExecutor backgroundExecutor;
    private static FlushBehavior flushBehavior;
    private static boolean isOpenedByApplink;
    private static boolean requestInFlight;
    private static String sourceApplication;
    private static Map stateMap = new ConcurrentHashMap();
    private static Object staticLock = new Object();
    private final AccessTokenAppIdPair accessTokenAppId;
    private final Context context;

    static
    {
        flushBehavior = FlushBehavior.AUTO;
    }

}
