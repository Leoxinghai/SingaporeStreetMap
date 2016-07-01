// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.*;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import com.facebook.internal.AttributionIdentifiers;
import com.facebook.internal.Logger;
import com.facebook.internal.ServerProtocol;
import com.facebook.internal.Utility;
import com.facebook.internal.Validate;
import com.facebook.model.GraphMultiResult;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphObjectList;
import com.facebook.model.GraphPlace;
import com.facebook.model.GraphUser;
import com.facebook.model.OpenGraphAction;
import com.facebook.model.OpenGraphObject;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;
import org.json.*;

// Referenced classes of package com.facebook:
//            HttpMethod, Session, FacebookException, Settings,
//            Response, RequestBatch, RequestAsyncTask, LoggingBehavior,
//            ProgressNoopOutputStream, ProgressOutputStream, RequestOutputStream

public class Request
{
    private static class Attachment
    {

        public Request getRequest()
        {
            return request;
        }

        public Object getValue()
        {
            return value;
        }

        private final Request request;
        private final Object value;

        public Attachment(Request request1, Object obj)
        {
            request = request1;
            value = obj;
        }
    }

    public static interface Callback
    {

        public abstract void onCompleted(Response response);
    }

    public static interface GraphPlaceListCallback
    {

        public abstract void onCompleted(List list, Response response);
    }

    public static interface GraphUserCallback
    {

        public abstract void onCompleted(GraphUser graphuser, Response response);
    }

    public static interface GraphUserListCallback
    {

        public abstract void onCompleted(List list, Response response);
    }

    private static interface KeyValueSerializer
    {

        public abstract void writeString(String s, String s1)
            throws IOException;
    }

    public static interface OnProgressCallback
        extends Callback
    {

        public abstract void onProgress(long l, long l1);
    }

    private static class ParcelFileDescriptorWithMimeType
        implements Parcelable
    {

        public int describeContents()
        {
            return 1;
        }

        public ParcelFileDescriptor getFileDescriptor()
        {
            return fileDescriptor;
        }

        public String getMimeType()
        {
            return mimeType;
        }

        public void writeToParcel(Parcel parcel, int i)
        {
            parcel.writeString(mimeType);
            parcel.writeFileDescriptor(fileDescriptor.getFileDescriptor());
        }

        public static final android.os.Parcelable.Creator CREATOR = new android.os.Parcelable.Creator() {

            public ParcelFileDescriptorWithMimeType createFromParcel(Parcel parcel)
            {
                return new ParcelFileDescriptorWithMimeType(parcel);
            }


            public ParcelFileDescriptorWithMimeType[] newArray(int i)
            {
                return new ParcelFileDescriptorWithMimeType[i];
            }


        }
;
        private final ParcelFileDescriptor fileDescriptor;
        private final String mimeType;


        private ParcelFileDescriptorWithMimeType(Parcel parcel)
        {
            mimeType = parcel.readString();
            fileDescriptor = parcel.readFileDescriptor();
        }


        public ParcelFileDescriptorWithMimeType(ParcelFileDescriptor parcelfiledescriptor, String s)
        {
            mimeType = s;
            fileDescriptor = parcelfiledescriptor;
        }
    }

    private static class Serializer
        implements KeyValueSerializer
    {

        public void write(String s, Object aobj[])
            throws IOException
        {
            if(!useUrlEncode)
            {
                if(firstWrite)
                {
                    outputStream.write("--".getBytes());
                    outputStream.write("3i2ndDfv2rTHiSisAbouNdArYfORhtTPEefj3q2f".getBytes());
                    outputStream.write("\r\n".getBytes());
                    firstWrite = false;
                }
                outputStream.write(String.format(s, aobj).getBytes());
                return;
            } else
            {
                outputStream.write(URLEncoder.encode(String.format(s, aobj), "UTF-8").getBytes());
                return;
            }
        }

        public void writeBitmap(String s, Bitmap bitmap)
            throws IOException
        {
            writeContentDisposition(s, s, "image/png");
            bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, outputStream);
            writeLine("", new Object[0]);
            writeRecordBoundary();
            if(logger != null)
                logger.appendKeyValue((new StringBuilder()).append("    ").append(s).toString(), "<Image>");
        }

        public void writeBytes(String s, byte abyte0[])
            throws IOException
        {
            writeContentDisposition(s, s, "content/unknown");
            outputStream.write(abyte0);
            writeLine("", new Object[0]);
            writeRecordBoundary();
            if(logger != null)
                logger.appendKeyValue((new StringBuilder()).append("    ").append(s).toString(), String.format("<Data: %d>", new Object[] {
                    Integer.valueOf(abyte0.length)
                }));
        }

        public void writeContentDisposition(String s, String s1, String s2)
            throws IOException
        {
            if(!useUrlEncode)
            {
                write("Content-Disposition: form-data; name=\"%s\"", new Object[] {
                    s
                });
                if(s1 != null)
                    write("; filename=\"%s\"", new Object[] {
                        s1
                    });
                writeLine("", new Object[0]);
                if(s2 != null)
                    writeLine("%s: %s", new Object[] {
                        "Content-Type", s2
                    });
                writeLine("", new Object[0]);
                return;
            } else
            {
                outputStream.write(String.format("%s=", new Object[] {
                    s
                }).getBytes());
                return;
            }
        }

        public void writeFile(String s, ParcelFileDescriptor parcelfiledescriptor, String s1)
            throws IOException
        {
            int i;
            int j;
            String s2 = s1;
            if(s1 == null)
                s2 = "content/unknown";
            writeContentDisposition(s, s, s2);
            i = 0;
            j = 0;
            try {
                if (!(outputStream instanceof ProgressNoopOutputStream)) {
                    Object obj;
                    obj = null;
                    s1 = null;
                    ParcelFileDescriptor.AutoCloseInputStream temp = new android.os.ParcelFileDescriptor.AutoCloseInputStream(parcelfiledescriptor);
                    BufferedInputStream bufferedinputstream = new BufferedInputStream(temp);
                    byte temp2[] = new byte[8192];

                    for (; ; ) {
                        j = bufferedinputstream.read(temp2);
                        if (j == -1)
                            break;
                        outputStream.write(temp2, 0, j);
                        i += j;
                    }

                    if (bufferedinputstream != null)
                        bufferedinputstream.close();
                    j = i;
                    if (temp != null) {
                        temp.close();
                        j = i;
                    }
                } else {
                    ((ProgressNoopOutputStream) outputStream).addProgress(parcelfiledescriptor.getStatSize());
                }

                writeLine("", new Object[0]);
                writeRecordBoundary();
                if (logger != null)
                    logger.appendKeyValue((new StringBuilder()).append("    ").append(s).toString(), String.format("<Data: %d>", new Object[]{
                            Integer.valueOf(j)
                    }));
                return;

            } catch(Exception ex) {
                throw ex;
            }
        }

        public void writeFile(String s, ParcelFileDescriptorWithMimeType parcelfiledescriptorwithmimetype)
            throws IOException
        {
            writeFile(s, parcelfiledescriptorwithmimetype.getFileDescriptor(), parcelfiledescriptorwithmimetype.getMimeType());
        }

        public void writeLine(String s, Object aobj[])
            throws IOException
        {
            write(s, aobj);
            if(!useUrlEncode)
                write("\r\n", new Object[0]);
        }

        public void writeObject(String s, Object obj, Request request)
            throws IOException
        {
            if(outputStream instanceof RequestOutputStream)
                ((RequestOutputStream)outputStream).setCurrentRequest(request);
            if(Request.isSupportedParameterType(obj))
            {
                writeString(s, Request.parameterToString(obj));
                return;
            }
            if(obj instanceof Bitmap)
            {
                writeBitmap(s, (Bitmap)obj);
                return;
            }
            if(obj instanceof byte[])
            {
                writeBytes(s, (byte[])(byte[])obj);
                return;
            }
            if(obj instanceof ParcelFileDescriptor)
            {
                writeFile(s, (ParcelFileDescriptor)obj, null);
                return;
            }
            if(obj instanceof ParcelFileDescriptorWithMimeType)
            {
                writeFile(s, (ParcelFileDescriptorWithMimeType)obj);
                return;
            } else
            {
                throw new IllegalArgumentException("value is not a supported type: String, Bitmap, byte[]");
            }
        }

        public void writeRecordBoundary()
            throws IOException
        {
            if(!useUrlEncode)
            {
                writeLine("--%s", new Object[] {
                    "3i2ndDfv2rTHiSisAbouNdArYfORhtTPEefj3q2f"
                });
                return;
            } else
            {
                outputStream.write("&".getBytes());
                return;
            }
        }

        public void writeRequestsAsJson(String s, JSONArray jsonarray, Collection collection)
            throws IOException, JSONException
        {
            if(!(outputStream instanceof RequestOutputStream))
            {
                writeString(s, jsonarray.toString());
            } else
            {
                RequestOutputStream requestoutputstream = (RequestOutputStream)outputStream;
                writeContentDisposition(s, null, null);
                write("[", new Object[0]);
                int i = 0;
                Iterator iterator = collection.iterator();
                while(iterator.hasNext())
                {
                    Request request = (Request)iterator.next();
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    requestoutputstream.setCurrentRequest(request);
                    if(i > 0)
                        write(",%s", new Object[] {
                            jsonobject.toString()
                        });
                    else
                        write("%s", new Object[] {
                            jsonobject.toString()
                        });
                    i++;
                }
                write("]", new Object[0]);
                if(logger != null)
                {
                    logger.appendKeyValue((new StringBuilder()).append("    ").append(s).toString(), jsonarray.toString());
                    return;
                }
            }
        }

        public void writeString(String s, String s1)
            throws IOException
        {
            writeContentDisposition(s, null, null);
            writeLine("%s", new Object[] {
                s1
            });
            writeRecordBoundary();
            if(logger != null)
                logger.appendKeyValue((new StringBuilder()).append("    ").append(s).toString(), s1);
        }

        private boolean firstWrite;
        private final Logger logger;
        private final OutputStream outputStream;
        private boolean useUrlEncode;

        public Serializer(OutputStream outputstream, Logger logger1, boolean flag)
        {
            firstWrite = true;
            useUrlEncode = false;
            outputStream = outputstream;
            logger = logger1;
            useUrlEncode = flag;
        }
    }


    public Request()
    {
        this(null, null, null, null, null);
    }

    public Request(Session session1, String s)
    {
        this(session1, s, null, null, null);
    }

    public Request(Session session1, String s, Bundle bundle, HttpMethod httpmethod)
    {
        this(session1, s, bundle, httpmethod, null);
    }

    public Request(Session session1, String s, Bundle bundle, HttpMethod httpmethod, Callback callback1)
    {
        this(session1, s, bundle, httpmethod, callback1, null);
    }

    public Request(Session session1, String s, Bundle bundle, HttpMethod httpmethod, Callback callback1, String s1)
    {
        batchEntryOmitResultOnSuccess = true;
        skipClientToken = false;
        session = session1;
        graphPath = s;
        callback = callback1;
        version = s1;
        setHttpMethod(httpmethod);
        if(bundle != null)
            parameters = new Bundle(bundle);
        else
            parameters = new Bundle();
        if(version == null)
            version = ServerProtocol.getAPIVersion();
    }

    Request(Session session1, URL url)
    {
        batchEntryOmitResultOnSuccess = true;
        skipClientToken = false;
        session = session1;
        overriddenURL = url.toString();
        setHttpMethod(HttpMethod.GET);
        parameters = new Bundle();
    }

    private void addCommonParameters()
    {
        if(session == null) {
            if(!skipClientToken && !parameters.containsKey("access_token"))
            {
                String s1 = Settings.getApplicationId();
                String s2 = Settings.getClientToken();
                if(!Utility.isNullOrEmpty(s1) && !Utility.isNullOrEmpty(s2))
                {
                    s1 = (new StringBuilder()).append(s1).append("|").append(s2).toString();
                    parameters.putString("access_token", s1);
                } else
                {
                    Log.d(TAG, "Warning: Sessionless Request needs token but missing either application ID or client token.");
                }
            }
        } else {
            if (!session.isOpened())
                throw new FacebookException("Session provided to a Request in un-opened state.");
            if (!parameters.containsKey("access_token")) {
                String s = session.getAccessToken();
                Logger.registerAccessToken(s);
                parameters.putString("access_token", s);
            }
        }

        parameters.putString("sdk", "android");
        parameters.putString("format", "json");
        return;
    }

    private String appendParametersToBaseUrl(String s)
    {
        android.net.Uri.Builder builder;
        builder = (new android.net.Uri.Builder()).encodedPath(s);
        String s1;
        for(Iterator iterator = parameters.keySet().iterator(); iterator.hasNext(); builder.appendQueryParameter(s1, parameterToString(s).toString()))
        {
            s1 = (String)iterator.next();
            Object obj = parameters.get(s1);
            s = ((String) (obj));
            if(obj == null)
                s = "";
            if(!isSupportedParameterType(s)) {
                if(httpMethod == HttpMethod.GET);
                throw new IllegalArgumentException(String.format("Unsupported parameter type for GET request: %s", new Object[] {
                        s.getClass().getSimpleName()
                }));

            }
        }
        return builder.toString();
    }

    private static HttpURLConnection createConnection(URL url)
        throws IOException
    {
        HttpURLConnection temp = (HttpURLConnection)url.openConnection();
        temp.setRequestProperty("User-Agent", getUserAgent());
        temp.setRequestProperty("Accept-Language", Locale.getDefault().toString());
        temp.setChunkedStreamingMode(0);
        return temp;
    }

    public static Response executeAndWait(Request request)
    {
        List temp = executeBatchAndWait(new Request[] {
            request
        });
        if(request == null || temp.size() != 1)
            throw new FacebookException("invalid state: expected a single response");
        else
            return (Response)temp.get(0);
    }

    public static List executeBatchAndWait(RequestBatch requestbatch)
    {
        Validate.notEmptyAndContainsNoNulls(requestbatch, "requests");
        HttpURLConnection httpurlconnection;
        try
        {
            httpurlconnection = toHttpConnection(requestbatch);
        }
        catch(Exception exception)
        {
            List list = Response.constructErrorResponses(requestbatch.getRequests(), null, new FacebookException(exception));
            runCallbacks(requestbatch, list);
            return list;
        }
        return executeConnectionAndWait(httpurlconnection, requestbatch);
    }

    public static List executeBatchAndWait(Collection collection)
    {
        return executeBatchAndWait(new RequestBatch(collection));
    }

    public static List executeBatchAndWait(Request arequest[])
    {
        Validate.notNull(arequest, "requests");
        return executeBatchAndWait(((Collection) (Arrays.asList(arequest))));
    }

    public static RequestAsyncTask executeBatchAsync(RequestBatch requestbatch)
    {
        Validate.notEmptyAndContainsNoNulls(requestbatch, "requests");
        RequestAsyncTask temp = new RequestAsyncTask(requestbatch);
        temp.executeOnSettingsExecutor();
        return temp;
    }

    public static RequestAsyncTask executeBatchAsync(Collection collection)
    {
        return executeBatchAsync(new RequestBatch(collection));
    }

    public static RequestAsyncTask executeBatchAsync(Request arequest[])
    {
        Validate.notNull(arequest, "requests");
        return executeBatchAsync(((Collection) (Arrays.asList(arequest))));
    }

    public static List executeConnectionAndWait(HttpURLConnection httpurlconnection, RequestBatch requestbatch)
    {
        List list = Response.fromHttpConnection(httpurlconnection, requestbatch);
        Utility.disconnectQuietly(httpurlconnection);
        int i = requestbatch.size();
        if(i != list.size())
            throw new FacebookException(String.format("Received %d responses while expecting %d", new Object[] {
                Integer.valueOf(list.size()), Integer.valueOf(i)
            }));
        runCallbacks(requestbatch, list);
        HashSet temp = new HashSet();
        Iterator iterator = requestbatch.iterator();
        for(;iterator.hasNext();) {
            Request request = (Request)iterator.next();
            if(request.session != null)
                temp.add(request.session);
        }

        for(Iterator iterator2  = temp.iterator(); iterator2.hasNext(); ((Session)iterator2.next()).extendAccessTokenIfNeeded());
        return list;
    }

    public static List executeConnectionAndWait(HttpURLConnection httpurlconnection, Collection collection)
    {
        return executeConnectionAndWait(httpurlconnection, new RequestBatch(collection));
    }

    public static RequestAsyncTask executeConnectionAsync(Handler handler, HttpURLConnection httpurlconnection, RequestBatch requestbatch)
    {
        Validate.notNull(httpurlconnection, "connection");
        RequestAsyncTask temp = new RequestAsyncTask(httpurlconnection, requestbatch);
        requestbatch.setCallbackHandler(handler);
        temp.executeOnSettingsExecutor();
        return temp;
    }

    public static RequestAsyncTask executeConnectionAsync(HttpURLConnection httpurlconnection, RequestBatch requestbatch)
    {
        return executeConnectionAsync(null, httpurlconnection, requestbatch);
    }

    public static RequestAsyncTask executeGraphPathRequestAsync(Session session1, String s, Callback callback1)
    {
        return newGraphPathRequest(session1, s, callback1).executeAsync();
    }

    public static RequestAsyncTask executeMeRequestAsync(Session session1, GraphUserCallback graphusercallback)
    {
        return newMeRequest(session1, graphusercallback).executeAsync();
    }

    public static RequestAsyncTask executeMyFriendsRequestAsync(Session session1, GraphUserListCallback graphuserlistcallback)
    {
        return newMyFriendsRequest(session1, graphuserlistcallback).executeAsync();
    }

    public static RequestAsyncTask executePlacesSearchRequestAsync(Session session1, Location location, int i, int j, String s, GraphPlaceListCallback graphplacelistcallback)
    {
        return newPlacesSearchRequest(session1, location, i, j, s, graphplacelistcallback).executeAsync();
    }

    public static RequestAsyncTask executePostRequestAsync(Session session1, String s, GraphObject graphobject, Callback callback1)
    {
        return newPostRequest(session1, s, graphobject, callback1).executeAsync();
    }

    public static RequestAsyncTask executeStatusUpdateRequestAsync(Session session1, String s, Callback callback1)
    {
        return newStatusUpdateRequest(session1, s, callback1).executeAsync();
    }

    public static RequestAsyncTask executeUploadPhotoRequestAsync(Session session1, Bitmap bitmap, Callback callback1)
    {
        return newUploadPhotoRequest(session1, bitmap, callback1).executeAsync();
    }

    public static RequestAsyncTask executeUploadPhotoRequestAsync(Session session1, File file, Callback callback1)
        throws FileNotFoundException
    {
        return newUploadPhotoRequest(session1, file, callback1).executeAsync();
    }

    private static String getBatchAppId(RequestBatch requestbatch)
    {
        if(!Utility.isNullOrEmpty(requestbatch.getBatchApplicationId()))
            return requestbatch.getBatchApplicationId();
        for(Iterator iterator = requestbatch.iterator(); iterator.hasNext();)
        {
            Session session1 = ((Request)iterator.next()).session;
            if(session1 != null)
                return session1.getApplicationId();
        }

        return defaultBatchApplicationId;
    }

    public static final String getDefaultBatchApplicationId()
    {
        return defaultBatchApplicationId;
    }

    private String getGraphPathWithVersion()
    {
        if(versionPattern.matcher(graphPath).matches())
            return graphPath;
        else
            return String.format("%s/%s", new Object[] {
                version, graphPath
            });
    }

    private static String getMimeContentType()
    {
        return String.format("multipart/form-data; boundary=%s", new Object[] {
            "3i2ndDfv2rTHiSisAbouNdArYfORhtTPEefj3q2f"
        });
    }

    private static String getUserAgent()
    {
        if(userAgent == null)
            userAgent = String.format("%s.%s", new Object[] {
                "FBAndroidSDK", "3.23.1"
            });
        return userAgent;
    }

    private static boolean hasOnProgressCallbacks(RequestBatch requestbatch)
    {
        for(Iterator iterator = requestbatch.getCallbacks().iterator(); iterator.hasNext();)
            if((RequestBatch.Callback)iterator.next() instanceof RequestBatch.OnProgressCallback)
                return true;

        for(Iterator iterator = requestbatch.iterator(); iterator.hasNext();)
            if(((Request)iterator.next()).getCallback() instanceof OnProgressCallback)
                return true;

        return false;
    }

    private static boolean isGzipCompressible(RequestBatch requestbatch)
    {
        Iterator iterator = requestbatch.iterator();
        for(;iterator.hasNext();)
        {
            Request request = (Request)iterator.next();
            Iterator iterator2 = request.parameters.keySet().iterator();
            String s;
            for(;iterator2.hasNext();) {
                s = (String)iterator2.next();
                if(isSupportedAttachmentType(request.parameters.get(s)))
                    return false;
            }
        }
        return true;
    }

    private static boolean isMeRequest(String s)
    {
        Matcher matcher = versionPattern.matcher(s);
        if(matcher.matches())
            s = matcher.group(1);
        return s.startsWith("me/") || s.startsWith("/me/");
    }

    private static boolean isSupportedAttachmentType(Object obj)
    {
        return (obj instanceof Bitmap) || (obj instanceof byte[]) || (obj instanceof ParcelFileDescriptor) || (obj instanceof ParcelFileDescriptorWithMimeType);
    }

    private static boolean isSupportedParameterType(Object obj)
    {
        return (obj instanceof String) || (obj instanceof Boolean) || (obj instanceof Number) || (obj instanceof Date);
    }

    public static Request newCustomAudienceThirdPartyIdRequest(Session session1, Context context, Callback callback1)
    {
        return newCustomAudienceThirdPartyIdRequest(session1, context, null, callback1);
    }

    public static Request newCustomAudienceThirdPartyIdRequest(Session session1, Context context, String s, Callback callback1)
    {
        Object obj = session1;
        if(session1 == null)
            obj = Session.getActiveSession();
        Session session2 = ((Session) (obj));
        if(obj != null)
        {
            session2 = ((Session) (obj));
            if(!((Session) (obj)).isOpened())
                session2 = null;
        }
        String temp = s;
        if(s == null)
            if(session2 != null)
                temp = session2.getApplicationId();
            else
                temp = Utility.getMetadataApplicationId(context);
        if(temp == null)
            throw new FacebookException("Facebook App ID cannot be determined");
        s = (new StringBuilder()).append(temp).append("/custom_audience_third_party_id").toString();
        obj = AttributionIdentifiers.getAttributionIdentifiers(context);
        Bundle bundle = new Bundle();
        if(session2 == null)
        {
            if(((AttributionIdentifiers) (obj)).getAttributionId() != null)
                temp = ((AttributionIdentifiers) (obj)).getAttributionId();
            else
                temp = ((AttributionIdentifiers) (obj)).getAndroidAdvertiserId();
            if(((AttributionIdentifiers) (obj)).getAttributionId() != null)
                bundle.putString("udid", temp);
        }
        if(Settings.getLimitEventAndDataUsage(context) || ((AttributionIdentifiers) (obj)).isTrackingLimited())
            bundle.putString("limit_event_usage", "1");
        return new Request(session2, s, bundle, HttpMethod.GET, callback1);
    }

    public static Request newDeleteObjectRequest(Session session1, String s, Callback callback1)
    {
        return new Request(session1, s, null, HttpMethod.DELETE, callback1);
    }

    public static Request newGraphPathRequest(Session session1, String s, Callback callback1)
    {
        return new Request(session1, s, null, null, callback1);
    }

    public static Request newMeRequest(Session session1, final GraphUserCallback graphusercallback)
    {
        return new Request(session1, "me", null, null, new Callback() {

            public void onCompleted(Response response)
            {
                if(graphusercallback != null)
                    graphusercallback.onCompleted((GraphUser)response.getGraphObjectAs(GraphUser.class), response);
            }

        }
);
    }

    public static Request newMyFriendsRequest(Session session1, final GraphUserListCallback graphuserlistcallback)
    {
        return new Request(session1, "me/friends", null, null, new Callback() {

            public void onCompleted(Response response)
            {
                if(graphuserlistcallback != null)
                    graphuserlistcallback.onCompleted(Request.typedListFromResponse(response, GraphUser.class), response);
            }

        });
    }

    public static Request newPlacesSearchRequest(Session session1, Location location, int i, int j, String s, final GraphPlaceListCallback graphplacelistcallback)
    {
        if(location == null && Utility.isNullOrEmpty(s))
            throw new FacebookException("Either location or searchText must be specified.");
        Bundle bundle = new Bundle(5);
        bundle.putString("type", "place");
        bundle.putInt("limit", j);
        if(location != null)
        {
            bundle.putString("center", String.format(Locale.US, "%f,%f", new Object[] {
                Double.valueOf(location.getLatitude()), Double.valueOf(location.getLongitude())
            }));
            bundle.putInt("distance", i);
        }
        if(!Utility.isNullOrEmpty(s))
            bundle.putString("q", s);
        Callback temp = new Callback() {

            public void onCompleted(Response response)
            {
                if(graphplacelistcallback != null)
                    graphplacelistcallback.onCompleted(Request.typedListFromResponse(response, GraphPlace.class), response);
            }

        };
        return new Request(session1, "search", bundle, HttpMethod.GET, temp);
    }

    public static Request newPostOpenGraphActionRequest(Session session1, OpenGraphAction opengraphaction, Callback callback1)
    {
        if(opengraphaction == null)
            throw new FacebookException("openGraphAction cannot be null");
        if(Utility.isNullOrEmpty(opengraphaction.getType()))
            throw new FacebookException("openGraphAction must have non-null 'type' property");
        else
            return newPostRequest(session1, String.format("me/%s", new Object[] {
                opengraphaction.getType()
            }), opengraphaction, callback1);
    }

    public static Request newPostOpenGraphObjectRequest(Session session1, OpenGraphObject opengraphobject, Callback callback1)
    {
        if(opengraphobject == null)
            throw new FacebookException("openGraphObject cannot be null");
        if(Utility.isNullOrEmpty(opengraphobject.getType()))
            throw new FacebookException("openGraphObject must have non-null 'type' property");
        if(Utility.isNullOrEmpty(opengraphobject.getTitle()))
        {
            throw new FacebookException("openGraphObject must have non-null 'title' property");
        } else
        {
            String s = String.format("me/objects/%s", new Object[] {
                opengraphobject.getType()
            });
            Bundle bundle = new Bundle();
            bundle.putString("object", opengraphobject.getInnerJSONObject().toString());
            return new Request(session1, s, bundle, HttpMethod.POST, callback1);
        }
    }

    public static Request newPostOpenGraphObjectRequest(Session session1, String s, String s1, String s2, String s3, String s4, GraphObject graphobject, Callback callback1)
    {
        OpenGraphObject temp = com.facebook.model.OpenGraphObject.Factory.createForPost(OpenGraphObject.class, s, s1, s2, s3, s4);
        if(graphobject != null)
            temp.setData(graphobject);
        return newPostOpenGraphObjectRequest(session1, ((OpenGraphObject) (temp)), callback1);
    }

    public static Request newPostRequest(Session session1, String s, GraphObject graphobject, Callback callback1)
    {
        Request temp = new Request(session1, s, null, HttpMethod.POST, callback1);
        temp.setGraphObject(graphobject);
        return temp;
    }

    public static Request newStatusUpdateRequest(Session session1, String s, Callback callback1)
    {
        return newStatusUpdateRequest(session1, s, (String)null, null, callback1);
    }

    public static Request newStatusUpdateRequest(Session session1, String s, GraphPlace graphplace, List list, Callback callback1)
    {
        ArrayList arraylist = null;
        if(list != null)
        {
            ArrayList arraylist1 = new ArrayList(list.size());
            Iterator iterator = list.iterator();
            arraylist = arraylist1;
            for(;iterator.hasNext();) {
                arraylist1.add(((GraphUser)iterator.next()).getId());
            }
        }
        String temp;
        if(graphplace == null)
            temp = null;
        else
            temp = graphplace.getId();
        return newStatusUpdateRequest(session1, s, ((String) (temp)), ((List) (arraylist)), callback1);
    }

    private static Request newStatusUpdateRequest(Session session1, String s, String s1, List list, Callback callback1)
    {
        Bundle bundle = new Bundle();
        bundle.putString("message", s);
        if(s1 != null)
            bundle.putString("place", s1);
        if(list != null && list.size() > 0)
            bundle.putString("tags", TextUtils.join(",", list));
        return new Request(session1, "me/feed", bundle, HttpMethod.POST, callback1);
    }

    public static Request newUpdateOpenGraphObjectRequest(Session session1, OpenGraphObject opengraphobject, Callback callback1)
    {
        if(opengraphobject == null)
            throw new FacebookException("openGraphObject cannot be null");
        String s = opengraphobject.getId();
        if(s == null)
        {
            throw new FacebookException("openGraphObject must have an id");
        } else
        {
            Bundle bundle = new Bundle();
            bundle.putString("object", opengraphobject.getInnerJSONObject().toString());
            return new Request(session1, s, bundle, HttpMethod.POST, callback1);
        }
    }

    public static Request newUpdateOpenGraphObjectRequest(Session session1, String s, String s1, String s2, String s3, String s4, GraphObject graphobject, Callback callback1)
    {
        OpenGraphObject temp = com.facebook.model.OpenGraphObject.Factory.createForPost(OpenGraphObject.class, null, s1, s2, s3, s4);
        temp.setId(s);
        temp.setData(graphobject);
        return newUpdateOpenGraphObjectRequest(session1, ((OpenGraphObject) (temp)), callback1);
    }

    public static Request newUploadPhotoRequest(Session session1, Bitmap bitmap, Callback callback1)
    {
        Bundle bundle = new Bundle(1);
        bundle.putParcelable("picture", bitmap);
        return new Request(session1, "me/photos", bundle, HttpMethod.POST, callback1);
    }

    public static Request newUploadPhotoRequest(Session session1, File file, Callback callback1)
        throws FileNotFoundException
    {
        ParcelFileDescriptor temp = ParcelFileDescriptor.open(file, 0x10000000);
        Bundle bundle = new Bundle(1);
        bundle.putParcelable("picture", temp);
        return new Request(session1, "me/photos", bundle, HttpMethod.POST, callback1);
    }

    public static Request newUploadStagingResourceWithImageRequest(Session session1, Bitmap bitmap, Callback callback1)
    {
        Bundle bundle = new Bundle(1);
        bundle.putParcelable("file", bitmap);
        return new Request(session1, "me/staging_resources", bundle, HttpMethod.POST, callback1);
    }

    public static Request newUploadStagingResourceWithImageRequest(Session session1, File file, Callback callback1)
        throws FileNotFoundException
    {
        ParcelFileDescriptorWithMimeType temp = new ParcelFileDescriptorWithMimeType(ParcelFileDescriptor.open(file, 0x10000000), "image/png");
        Bundle bundle = new Bundle(1);
        bundle.putParcelable("file", temp);
        return new Request(session1, "me/staging_resources", bundle, HttpMethod.POST, callback1);
    }

    public static Request newUploadVideoRequest(Session session1, File file, Callback callback1)
        throws FileNotFoundException
    {
        ParcelFileDescriptor parcelfiledescriptor = ParcelFileDescriptor.open(file, 0x10000000);
        Bundle bundle = new Bundle(1);
        bundle.putParcelable(file.getName(), parcelfiledescriptor);
        return new Request(session1, "me/videos", bundle, HttpMethod.POST, callback1);
    }

    private static String parameterToString(Object obj)
    {
        if(obj instanceof String)
            return (String)obj;
        if((obj instanceof Boolean) || (obj instanceof Number))
            return obj.toString();
        if(obj instanceof Date)
            return (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US)).format(obj);
        else
            throw new IllegalArgumentException("Unsupported parameter type.");
    }

    private static void processGraphObject(GraphObject graphobject, String s, KeyValueSerializer keyvalueserializer)
        throws IOException
    {
        int i = 0;
        if(isMeRequest(s))
        {
            i = s.indexOf(":");
            int j = s.indexOf("?");
            if(i > 3 && (j == -1 || i < j))
                i = 1;
            else
                i = 0;
        }
        Iterator iterator = graphobject.asMap().entrySet().iterator();
        while(iterator.hasNext())
        {
            Map.Entry temp2 = (java.util.Map.Entry)iterator.next();
            boolean flag;
            if(i != 0 && ((String)temp2.getKey()).equalsIgnoreCase("image"))
                flag = true;
            else
                flag = false;
            processGraphObjectProperty((String)temp2.getKey(), temp2.getValue(), keyvalueserializer, flag);
        }
    }

    private static void processGraphObjectProperty(String s, Object obj, KeyValueSerializer keyvalueserializer, boolean flag)
        throws IOException
    {
        Object obj1;
        Object obj2;
        Class class1 = obj.getClass();
        if(GraphObject.class.isAssignableFrom(class1))
        {
            obj2 = ((GraphObject)obj).getInnerJSONObject();
            obj1 = obj2.getClass();
        } else
        {
            obj1 = class1;
            obj2 = obj;
            if(GraphObjectList.class.isAssignableFrom(class1))
            {
                obj2 = ((GraphObjectList)obj).getInnerJSONArray();
                obj1 = obj2.getClass();
            }
        }
        if(JSONObject.class.isAssignableFrom(((Class) (obj1)))) {
            obj = (JSONObject) obj2;
            if (flag) {
                for (obj1 = ((JSONObject) (obj)).keys(); ((Iterator) (obj1)).hasNext(); processGraphObjectProperty(String.format("%s[%s]", new Object[]{
                        s, obj2
                }), ((JSONObject) (obj)).opt(((String) (obj2))), keyvalueserializer, flag))
                    obj2 = (String) ((Iterator) (obj1)).next();

            } else {
                if (!((JSONObject) (obj)).has("id")) {
                    if (((JSONObject) (obj)).has("url")) {
                        processGraphObjectProperty(s, ((JSONObject) (obj)).optString("url"), keyvalueserializer, flag);
                        return;
                    }
                    if (((JSONObject) (obj)).has("fbsdk:create_object")) {
                        processGraphObjectProperty(s, ((JSONObject) (obj)).toString(), keyvalueserializer, flag);
                        return;
                    }
                } else {
                    processGraphObjectProperty(s, ((JSONObject) (obj)).optString("id"), keyvalueserializer, flag);
                }
            }
        } else {
            if (!JSONArray.class.isAssignableFrom(((Class) (obj1)))) {
                if (String.class.isAssignableFrom(((Class) (obj1))) || Number.class.isAssignableFrom(((Class) (obj1))) || Boolean.class.isAssignableFrom(((Class) (obj1)))) {
                    keyvalueserializer.writeString(s, obj2.toString());
                    return;
                }
                if (Date.class.isAssignableFrom(((Class) (obj1)))) {
                    obj = (Date) obj2;
                    keyvalueserializer.writeString(s, (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US)).format(((Date) (obj))));
                    return;
                }
            } else {
                obj = (JSONArray) obj2;
                int j = ((JSONArray) (obj)).length();
                int i = 0;
                while (i < j) {
                    processGraphObjectProperty(String.format("%s[%d]", new Object[]{
                            s, Integer.valueOf(i)
                    }), ((JSONArray) (obj)).opt(i), keyvalueserializer, flag);
                    i++;
                }
            }
        }
    }

    private static void processRequest(RequestBatch requestbatch, Logger logger, int i, URL url, OutputStream outputstream, boolean flag)
        throws IOException, JSONException
    {
        Serializer temp = new Serializer(outputstream, logger, flag);
        Request temp2 = null;
        if(i == 1)
        {
            temp2 = requestbatch.get(0);
            HashMap hashmap = new HashMap();
            Iterator iterator = ((Request) (temp2)).parameters.keySet().iterator();
            for(;iterator.hasNext();) {
                String s = (String)iterator.next();
                Object obj = ((Request) (temp2)).parameters.get(s);
                if(isSupportedAttachmentType(obj))
                    hashmap.put(s, new Attachment(temp2, obj));
            }
            if(logger != null)
                logger.append("  Parameters:\n");
            serializeParameters(((Request) (temp2)).parameters, temp, temp2);
            if(logger != null)
                logger.append("  Attachments:\n");
            serializeAttachments(hashmap, temp);
            if(((Request) (temp2)).graphObject != null)
                processGraphObject(((Request) (temp2)).graphObject, url.getPath(), temp);
            return;
        }
        String temp3 = getBatchAppId(requestbatch);
        if(Utility.isNullOrEmpty(temp3))
            throw new FacebookException("At least one request in a batch must have an open Session, or a default app ID must be specified.");
        temp.writeString("batch_app_id", temp3);
        HashMap temp4 = new HashMap();
        serializeRequestsAsJSON(temp, requestbatch, temp4);
        if(logger != null)
            logger.append("  Attachments:\n");
        serializeAttachments(temp4, temp);
    }

    static void runCallbacks(final RequestBatch requestbatch, final List list)
    {
        int j = requestbatch.size();
        ArrayList arraylist = new ArrayList();
        for(int i = 0; i < j; i++)
        {
            Request request = requestbatch.get(i);
            if(request.callback != null)
                arraylist.add(new Pair(request.callback, list.get(i)));
        }

        if(arraylist.size() > 0)
        {
            Runnable temp = new Runnable() {

                public void run()
                {
                    Pair pair;
                    for(Iterator iterator = list.iterator(); iterator.hasNext(); ((Callback)pair.first).onCompleted((Response)pair.second))
                        pair = (Pair)iterator.next();

                    for(Iterator iterator1 = requestbatch.getCallbacks().iterator(); iterator1.hasNext(); ((RequestBatch.Callback)iterator1.next()).onBatchCompleted(requestbatch));
                }

            };
            Handler temp2 = requestbatch.getCallbackHandler();
            if(temp2 != null)
                temp2.post(temp);
            temp.run();
        }
        return;
    }

    private static void serializeAttachments(Map map, Serializer serializer)
        throws IOException
    {
        Iterator iterator = map.keySet().iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            String s = (String)iterator.next();
            Attachment attachment = (Attachment)map.get(s);
            if(isSupportedAttachmentType(attachment.getValue()))
                serializer.writeObject(s, attachment.getValue(), attachment.getRequest());
        } while(true);
    }

    private static void serializeParameters(Bundle bundle, Serializer serializer, Request request)
        throws IOException
    {
        Iterator iterator = bundle.keySet().iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            String s = (String)iterator.next();
            Object obj = bundle.get(s);
            if(isSupportedParameterType(obj))
                serializer.writeObject(s, obj, request);
        } while(true);
    }

    private static void serializeRequestsAsJSON(Serializer serializer, Collection collection, Map map)
        throws JSONException, IOException
    {
        JSONArray jsonarray = new JSONArray();
        for(Iterator iterator = collection.iterator(); iterator.hasNext(); ((Request)iterator.next()).serializeToBatch(jsonarray, map));
        serializer.writeRequestsAsJson("batch", jsonarray, collection);
    }

    private void serializeToBatch(JSONArray jsonarray, final Map keysAndValues)
        throws JSONException, IOException
    {
        JSONObject jsonobject = new JSONObject();
        if(batchEntryName != null)
        {
            jsonobject.put("name", batchEntryName);
            jsonobject.put("omit_response_on_success", batchEntryOmitResultOnSuccess);
        }
        if(batchEntryDependsOn != null)
            jsonobject.put("depends_on", batchEntryDependsOn);
        String s = getUrlForBatchedRequest();
        jsonobject.put("relative_url", s);
        jsonobject.put("method", httpMethod);
        if(session != null)
            Logger.registerAccessToken(session.getAccessToken());
        ArrayList arraylist = new ArrayList();
        Iterator iterator = parameters.keySet().iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            Object obj = (String)iterator.next();
            obj = parameters.get(((String) (obj)));
            if(isSupportedAttachmentType(obj))
            {
                String s1 = String.format("%s%d", new Object[] {
                    "file", Integer.valueOf(keysAndValues.size())
                });
                arraylist.add(s1);
                keysAndValues.put(s1, new Attachment(this, obj));
            }
        } while(true);
        if(!arraylist.isEmpty())
            jsonobject.put("attached_files", TextUtils.join(",", arraylist));
        if(graphObject != null)
        {
            final ArrayList arraylist0 = new ArrayList();
            processGraphObject(graphObject, s, new KeyValueSerializer() {

                public void writeString(String s2, String s3)
                    throws IOException
                {
                    arraylist0.add(String.format("%s=%s", new Object[] {
                        s2, URLEncoder.encode(s3, "UTF-8")
                    }));
                }

            });
            jsonobject.put("body", TextUtils.join("&", arraylist0));
        }
        jsonarray.put(jsonobject);
    }

    static final void serializeToUrlConnection(RequestBatch requestbatch, HttpURLConnection httpurlconnection)
        throws IOException, JSONException
    {
        Object obj;
        Logger logger;
        URL url;
        int j;
        boolean flag1;
        logger = new Logger(LoggingBehavior.REQUESTS, "Request");
        j = requestbatch.size();
        flag1 = isGzipCompressible(requestbatch);
        boolean flag;
        if(j == 1)
            obj = requestbatch.get(0).httpMethod;
        else
            obj = HttpMethod.POST;
        httpurlconnection.setRequestMethod(((HttpMethod) (obj)).name());
        setConnectionContentType(httpurlconnection, flag1);
        url = httpurlconnection.getURL();
        logger.append("Request:\n");
        logger.appendKeyValue("Id", requestbatch.getId());
        logger.appendKeyValue("URL", url);
        logger.appendKeyValue("Method", httpurlconnection.getRequestMethod());
        logger.appendKeyValue("User-Agent", httpurlconnection.getRequestProperty("User-Agent"));
        logger.appendKeyValue("Content-Type", httpurlconnection.getRequestProperty("Content-Type"));
        httpurlconnection.setConnectTimeout(requestbatch.getTimeout());
        httpurlconnection.setReadTimeout(requestbatch.getTimeout());
        if(obj == HttpMethod.POST)
            flag = true;
        else
            flag = false;
        if(!flag)
        {
            logger.log();
            return;
        }
        httpurlconnection.setDoOutput(true);
        obj = null;
        Object obj1 = new BufferedOutputStream(httpurlconnection.getOutputStream());
        httpurlconnection = ((HttpURLConnection) (obj1));
        OutputStream ops = null;
        if(flag1) {
            obj = obj1;
            ops = new GZIPOutputStream(((OutputStream) (obj1)));
        }
        if(hasOnProgressCallbacks(requestbatch)) {
            ops = new ProgressNoopOutputStream(requestbatch.getCallbackHandler());
        }
        processRequest(requestbatch, null, j, url, ((OutputStream) (obj1)), flag1);
        int i = ((ProgressNoopOutputStream) (ops)).getMaxProgress();
        obj = httpurlconnection;
        ops = new ProgressOutputStream(ops, requestbatch, ((ProgressNoopOutputStream) (ops)).getProgressMap(), i);
        obj = httpurlconnection;
        processRequest(requestbatch, logger, j, url, ops, flag1);
        if(ops != null)
            ops.close();
        logger.log();
        return;

    }

    private static void setConnectionContentType(HttpURLConnection httpurlconnection, boolean flag)
    {
        if(flag)
        {
            httpurlconnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpurlconnection.setRequestProperty("Content-Encoding", "gzip");
            return;
        } else
        {
            httpurlconnection.setRequestProperty("Content-Type", getMimeContentType());
            return;
        }
    }

    public static final void setDefaultBatchApplicationId(String s)
    {
        defaultBatchApplicationId = s;
    }

    public static HttpURLConnection toHttpConnection(RequestBatch requestbatch)
    {
        URL url;
        try
        {
            if(requestbatch.size() != 1) {
                url = new URL(ServerProtocol.getGraphUrlBase());
            } else {
                url = new URL(requestbatch.get(0).getUrlForSingleRequest());
            }
            HttpURLConnection temp = createConnection(((URL) (url)));
            serializeToUrlConnection(requestbatch, ((HttpURLConnection) (temp)));
            return ((HttpURLConnection) (temp));
        }
        catch(Exception ex)
        {
//            throw new FacebookException("could not construct URL for request", requestbatch);
        }
        return null;
    }

    public static HttpURLConnection toHttpConnection(Collection collection)
    {
        Validate.notEmptyAndContainsNoNulls(collection, "requests");
        return toHttpConnection(new RequestBatch(collection));
    }

    public static HttpURLConnection toHttpConnection(Request arequest[])
    {
        return toHttpConnection(((Collection) (Arrays.asList(arequest))));
    }

    private static List typedListFromResponse(Response response, Class class1)
    {
        GraphMultiResult temp = (GraphMultiResult)response.getGraphObjectAs(GraphMultiResult.class);
        GraphObjectList temp2;
        if(temp != null)
            if((temp2 = temp.getData()) != null)
                return temp2.castToListOf(class1);
        return null;
    }

    public final Response executeAndWait()
    {
        return executeAndWait(this);
    }

    public final RequestAsyncTask executeAsync()
    {
        return executeBatchAsync(new Request[] {
            this
        });
    }

    public final String getBatchEntryDependsOn()
    {
        return batchEntryDependsOn;
    }

    public final String getBatchEntryName()
    {
        return batchEntryName;
    }

    public final boolean getBatchEntryOmitResultOnSuccess()
    {
        return batchEntryOmitResultOnSuccess;
    }

    public final Callback getCallback()
    {
        return callback;
    }

    public final GraphObject getGraphObject()
    {
        return graphObject;
    }

    public final String getGraphPath()
    {
        return graphPath;
    }

    public final HttpMethod getHttpMethod()
    {
        return httpMethod;
    }

    public final Bundle getParameters()
    {
        return parameters;
    }

    public final Session getSession()
    {
        return session;
    }

    public final Object getTag()
    {
        return tag;
    }

    final String getUrlForBatchedRequest()
    {
        if(overriddenURL != null)
        {
            throw new FacebookException("Can't override URL for a batch request");
        } else
        {
            String s = getGraphPathWithVersion();
            addCommonParameters();
            return appendParametersToBaseUrl(s);
        }
    }

    final String getUrlForSingleRequest()
    {
        if(overriddenURL != null)
            return overriddenURL.toString();
        String s;
        if(getHttpMethod() == HttpMethod.POST && graphPath != null && graphPath.endsWith("/videos"))
            s = ServerProtocol.getGraphVideoUrlBase();
        else
            s = ServerProtocol.getGraphUrlBase();
        s = String.format("%s/%s", new Object[] {
            s, getGraphPathWithVersion()
        });
        addCommonParameters();
        return appendParametersToBaseUrl(s);
    }

    public final String getVersion()
    {
        return version;
    }

    public final void setBatchEntryDependsOn(String s)
    {
        batchEntryDependsOn = s;
    }

    public final void setBatchEntryName(String s)
    {
        batchEntryName = s;
    }

    public final void setBatchEntryOmitResultOnSuccess(boolean flag)
    {
        batchEntryOmitResultOnSuccess = flag;
    }

    public final void setCallback(Callback callback1)
    {
        callback = callback1;
    }

    public final void setGraphObject(GraphObject graphobject)
    {
        graphObject = graphobject;
    }

    public final void setGraphPath(String s)
    {
        graphPath = s;
    }

    public final void setHttpMethod(HttpMethod httpmethod)
    {
        if(overriddenURL != null && httpmethod != HttpMethod.GET)
            throw new FacebookException("Can't change HTTP method on request with overridden URL.");
        if(httpmethod == null)
            httpmethod = HttpMethod.GET;
        httpMethod = httpmethod;
    }

    public final void setParameters(Bundle bundle)
    {
        parameters = bundle;
    }

    public final void setSession(Session session1)
    {
        session = session1;
    }

    public final void setSkipClientToken(boolean flag)
    {
        skipClientToken = flag;
    }

    public final void setTag(Object obj)
    {
        tag = obj;
    }

    public final void setVersion(String s)
    {
        version = s;
    }

    public String toString()
    {
        return (new StringBuilder()).append("{Request: ").append(" session: ").append(session).append(", graphPath: ").append(graphPath).append(", graphObject: ").append(graphObject).append(", httpMethod: ").append(httpMethod).append(", parameters: ").append(parameters).append("}").toString();
    }

    private static final String ACCEPT_LANGUAGE_HEADER = "Accept-Language";
    private static final String ACCESS_TOKEN_PARAM = "access_token";
    private static final String ATTACHED_FILES_PARAM = "attached_files";
    private static final String ATTACHMENT_FILENAME_PREFIX = "file";
    private static final String BATCH_APP_ID_PARAM = "batch_app_id";
    private static final String BATCH_BODY_PARAM = "body";
    private static final String BATCH_ENTRY_DEPENDS_ON_PARAM = "depends_on";
    private static final String BATCH_ENTRY_NAME_PARAM = "name";
    private static final String BATCH_ENTRY_OMIT_RESPONSE_ON_SUCCESS_PARAM = "omit_response_on_success";
    private static final String BATCH_METHOD_PARAM = "method";
    private static final String BATCH_PARAM = "batch";
    private static final String BATCH_RELATIVE_URL_PARAM = "relative_url";
    private static final String CONTENT_ENCODING_HEADER = "Content-Encoding";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String FORMAT_JSON = "json";
    private static final String FORMAT_PARAM = "format";
    private static final String ISO_8601_FORMAT_STRING = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static final int MAXIMUM_BATCH_SIZE = 50;
    private static final String ME = "me";
    private static final String MIME_BOUNDARY = "3i2ndDfv2rTHiSisAbouNdArYfORhtTPEefj3q2f";
    private static final String MY_ACTION_FORMAT = "me/%s";
    private static final String MY_FEED = "me/feed";
    private static final String MY_FRIENDS = "me/friends";
    private static final String MY_OBJECTS_FORMAT = "me/objects/%s";
    private static final String MY_PHOTOS = "me/photos";
    private static final String MY_STAGING_RESOURCES = "me/staging_resources";
    private static final String MY_VIDEOS = "me/videos";
    private static final String OBJECT_PARAM = "object";
    private static final String PICTURE_PARAM = "picture";
    private static final String SDK_ANDROID = "android";
    private static final String SDK_PARAM = "sdk";
    private static final String SEARCH = "search";
    private static final String STAGING_PARAM = "file";
    public static final String TAG = Request.class.getSimpleName();
    private static final String USER_AGENT_BASE = "FBAndroidSDK";
    private static final String USER_AGENT_HEADER = "User-Agent";
    private static final String VIDEOS_SUFFIX = "/videos";
    private static String defaultBatchApplicationId;
    private static String userAgent;
    private static Pattern versionPattern = Pattern.compile("^/?v\\d+\\.\\d+/(.*)");
    private String batchEntryDependsOn;
    private String batchEntryName;
    private boolean batchEntryOmitResultOnSuccess;
    private Callback callback;
    private GraphObject graphObject;
    private String graphPath;
    private HttpMethod httpMethod;
    private String overriddenURL;
    private Bundle parameters;
    private Session session;
    private boolean skipClientToken;
    private Object tag;
    private String version;




}
