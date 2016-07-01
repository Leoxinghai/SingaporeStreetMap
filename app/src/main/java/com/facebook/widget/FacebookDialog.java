// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.*;
import android.support.v4.app.Fragment;
import com.facebook.*;
import com.facebook.internal.*;
import com.facebook.model.*;
import java.io.File;
import java.util.*;
import org.json.*;

public class FacebookDialog
{
    public static abstract class Builder
    {

        protected Builder addImageAttachment(String s, Bitmap bitmap)
        {
            imageAttachments.put(s, bitmap);
            return this;
        }

        protected Builder addImageAttachment(String s, File file)
        {
            mediaAttachmentFiles.put(s, file);
            return this;
        }

        protected List addImageAttachmentFiles(Collection collection)
        {
            ArrayList arraylist = new ArrayList();
            String s;
            for(Iterator iterator = collection.iterator(); iterator.hasNext(); arraylist.add(NativeAppCallContentProvider.getAttachmentUrl(applicationId, appCall.getCallId(), s)))
            {
                File file = (File)iterator.next();
                s = UUID.randomUUID().toString();
                addImageAttachment(s, file);
            }

            return arraylist;
        }

        protected List addImageAttachments(Collection collection)
        {
            ArrayList arraylist = new ArrayList();
            String s;
            for(Iterator iterator = collection.iterator(); iterator.hasNext(); arraylist.add(NativeAppCallContentProvider.getAttachmentUrl(applicationId, appCall.getCallId(), s)))
            {
                Bitmap bitmap = (Bitmap)iterator.next();
                s = UUID.randomUUID().toString();
                addImageAttachment(s, bitmap);
            }

            return arraylist;
        }

        protected Builder addVideoAttachment(String s, File file)
        {
            mediaAttachmentFiles.put(s, file);
            return this;
        }

        protected String addVideoAttachmentFile(File file)
        {
            String s = UUID.randomUUID().toString();
            addVideoAttachment(s, file);
            return NativeAppCallContentProvider.getAttachmentUrl(applicationId, appCall.getCallId(), s);
        }

        public FacebookDialog build()
        {
            validate();
            String s = FacebookDialog.getActionForFeatures(getDialogFeatures());
            int i = FacebookDialog.getProtocolVersionForNativeDialog(activity, s, FacebookDialog.getVersionSpecForFeatures(applicationId, s, getDialogFeatures()));
            Bundle bundle;
            Intent intent;
            if(NativeProtocol.isVersionCompatibleWithBucketedIntent(i))
                bundle = getMethodArguments();
            else
                bundle = setBundleExtras(new Bundle());
            intent = NativeProtocol.createPlatformActivityIntent(activity, appCall.getCallId().toString(), s, i, applicationName, bundle);
            if(intent == null)
            {
                FacebookDialog.logDialogActivity(activity, fragment, FacebookDialog.getEventName(s, bundle.containsKey("com.facebook.platform.extra.PHOTOS"), false), "Failed");
                throw new FacebookException("Unable to create Intent; this likely means the Facebook app is not installed.");
            } else
            {
                appCall.setRequestIntent(intent);
                return new FacebookDialog(activity, fragment, appCall, getOnPresentCallback());
            }
        }

        public boolean canPresent()
        {
            return FacebookDialog.handleCanPresent(activity, getDialogFeatures());
        }

        protected abstract EnumSet getDialogFeatures();

        List getImageAttachmentNames()
        {
            return new ArrayList(imageAttachments.keySet());
        }

        protected abstract Bundle getMethodArguments();

        OnPresentCallback getOnPresentCallback()
        {
            return new OnPresentCallback() {

                public void onPresent(Context context)
                    throws Exception
                {
                    if(imageAttachments != null && imageAttachments.size() > 0)
                        FacebookDialog.getAttachmentStore().addAttachmentsForCall(context, appCall.getCallId(), imageAttachments);
                    if(mediaAttachmentFiles != null && mediaAttachmentFiles.size() > 0)
                        FacebookDialog.getAttachmentStore().addAttachmentFilesForCall(context, appCall.getCallId(), mediaAttachmentFiles);
                }

            };
        }

        protected String getWebFallbackUrlInternal()
        {
            Object obj1 = getDialogFeatures();
            String s = null;
            Object obj = null;
            obj1 = ((Iterable) (obj1)).iterator();
            if(((Iterator) (obj1)).hasNext())
            {
                obj = (DialogFeature)((Iterator) (obj1)).next();
                s = ((DialogFeature) (obj)).name();
                obj = ((DialogFeature) (obj)).getAction();
            }
            obj = Utility.getDialogFeatureConfig(applicationId, ((String) (obj)), s);
            if(obj != null)
            {
                Uri uri1 = ((com.facebook.internal.Utility.DialogFeatureConfig) (obj)).getFallbackUrl();
                if(uri1 != null)
                {
                    Bundle bundle = getMethodArguments();
                    int i = NativeProtocol.getLatestKnownVersion();
                    Bundle bundle1 = ServerProtocol.getQueryParamsForPlatformActivityIntentWebFallback(activity, appCall.getCallId().toString(), i, applicationName, bundle);
                    if(bundle1 != null)
                    {
                        Uri uri = uri1;
                        if(uri1.isRelative())
                            uri = Utility.buildUri(ServerProtocol.getDialogAuthority(), uri1.toString(), bundle1);
                        return uri.toString();
                    }
                }
            }
            return null;
        }

        protected void putExtra(Bundle bundle, String s, String s1)
        {
            if(s1 != null)
                bundle.putString(s, s1);
        }

        public Builder setApplicationName(String s)
        {
            applicationName = s;
            return this;
        }

        protected Bundle setBundleExtras(Bundle bundle)
        {
            return bundle;
        }

        public Builder setFragment(Fragment fragment1)
        {
            fragment = fragment1;
            return this;
        }

        public Builder setRequestCode(int i)
        {
            appCall.setRequestCode(i);
            return this;
        }

        void validate()
        {
        }

        protected final Activity activity;
        protected final PendingCall appCall = new PendingCall(64207);
        protected final String applicationId;
        protected String applicationName;
        protected Fragment fragment;
        protected HashMap imageAttachments;
        protected HashMap mediaAttachmentFiles;

        public Builder(Activity activity1)
        {
            imageAttachments = new HashMap();
            mediaAttachmentFiles = new HashMap();
            Validate.notNull(activity1, "activity");
            activity = activity1;
            applicationId = Utility.getMetadataApplicationId(activity1);
        }
    }

    public static interface Callback
    {

        public abstract void onComplete(PendingCall pendingcall, Bundle bundle);

        public abstract void onError(PendingCall pendingcall, Exception exception, Bundle bundle);
    }

    public static interface DialogFeature
    {

        public abstract String getAction();

        public abstract int getMinVersion();

        public abstract String name();
    }

    public static class MessageDialogBuilder extends ShareDialogBuilderBase
    {

        protected EnumSet getDialogFeatures()
        {
            return EnumSet.of(MessageDialogFeature.MESSAGE_DIALOG);
        }

        public MessageDialogBuilder setFriends(List list)
        {
            return this;
        }


        public MessageDialogBuilder setPlace(String s)
        {
            return this;
        }


        public MessageDialogBuilder(Activity activity1)
        {
            super(activity1);
        }
    }

    public static enum MessageDialogFeature
        implements DialogFeature
    {

		MESSAGE_DIALOG("MESSAGE_DIALOG", 0, 0x13350ac),
		PHOTOS("PHOTOS", 1, 0x1335124),
		VIDEO("VIDEO", 2, 0x13354a2);

        public String getAction()
        {
            return "com.facebook.platform.action.request.MESSAGE_DIALOG";
        }

        public int getMinVersion()
        {
            return minVersion;
        }
        int minVersion;
        String sType;
        int iType;

        private MessageDialogFeature(String s, int i, int j)
        {
		    sType = s;
		    iType = i;
            minVersion = j;
        }
    }

    static interface OnPresentCallback
    {

        public abstract void onPresent(Context context)
            throws Exception;
    }

    public static class OpenGraphActionDialogBuilder extends OpenGraphDialogBuilderBase
    {

        protected EnumSet getDialogFeatures()
        {
            return EnumSet.of(OpenGraphActionDialogFeature.OG_ACTION_DIALOG);
        }

        public OpenGraphActionDialogBuilder(Activity activity1, OpenGraphAction opengraphaction, String s)
        {
            super(activity1, opengraphaction, s);
        }

        public OpenGraphActionDialogBuilder(Activity activity1, OpenGraphAction opengraphaction, String s, String s1)
        {
            super(activity1, opengraphaction, s, s1);
        }
    }

    public static enum OpenGraphActionDialogFeature
        implements DialogFeature
    {

		OG_ACTION_DIALOG("OG_ACTION_DIALOG", 0, 0x1332b3a);

        public String getAction()
        {
            return "com.facebook.platform.action.request.OGACTIONPUBLISH_DIALOG";
        }

        public int getMinVersion()
        {
            return minVersion;
        }

        private int minVersion;
        String sType;
        int iType;


        private OpenGraphActionDialogFeature(String s, int i, int j)
        {
            sType = s;
            iType = i;
            minVersion = j;
        }
    }

    private static abstract class OpenGraphDialogBuilderBase extends Builder
    {

        private JSONObject flattenChildrenOfGraphObject(JSONObject jsonobject)
        {
            try
            {
                jsonobject = new JSONObject(jsonobject.toString());
				Iterator iterator = jsonobject.keys();
				for(;iterator.hasNext();) {
					String s = (String)iterator.next();
					if(!s.equalsIgnoreCase("image"))
						jsonobject.put(s, flattenObject(jsonobject.get(s)));
				}
				return jsonobject;
            }
            catch(Exception ex) {
                throw new FacebookException(ex);
            }


        }

        private Object flattenObject(Object obj)
            throws JSONException
        {
            Object obj1 = null;
            if(obj != null) {
				if(!(obj instanceof JSONObject)) {
					obj1 = obj;
					if(obj instanceof JSONArray)
					{
						obj = (JSONArray)obj;
						JSONArray jsonarray = new JSONArray();
						int j = ((JSONArray) (obj)).length();
						for(int i = 0; i < j; i++)
							jsonarray.put(flattenObject(((JSONArray) (obj)).get(i)));

						return jsonarray;
					}
				} else {

					JSONObject jsonobject = (JSONObject)obj;
					obj1 = obj;
					if(!jsonobject.optBoolean("fbsdk:create_object"))
					{
						if(jsonobject.has("id"))
							return jsonobject.getString("id");
						obj1 = obj;
						if(jsonobject.has("url"))
							return jsonobject.getString("url");
					}
				}
			}
            return obj1;

        }

        private void updateActionAttachmentUrls(List list, boolean flag)
        {
            Object obj;
            List list1 = action.getImage();
            obj = list1;
            if(list1 == null)
                obj = new ArrayList(list.size());
            Iterator iterator = list.iterator();

            for(;iterator.hasNext();) {
				String s = (String)iterator.next();
				JSONObject jsonobject = new JSONObject();
				try
				{
					jsonobject.put("url", s);
                    jsonobject.put("user_generated", true);
                    ((List) (obj)).add(jsonobject);
                    if(!flag)
                        continue; /* Loop/switch isn't completed */
				}
				catch(Exception ex)
				{
					throw new FacebookException("Unable to attach images", ex);
				}
			}
            action.setImage(((List) (obj)));
            return;
        }

        protected Bundle getMethodArguments()
        {
            Bundle bundle = new Bundle();
            putExtra(bundle, "PREVIEW_PROPERTY_NAME", previewPropertyName);
            putExtra(bundle, "ACTION_TYPE", actionType);
            bundle.putBoolean("DATA_FAILURES_FATAL", dataErrorsFatal);
            putExtra(bundle, "ACTION", flattenChildrenOfGraphObject(action.getInnerJSONObject()).toString());
            return bundle;
        }

        protected Bundle setBundleExtras(Bundle bundle)
        {
            putExtra(bundle, "com.facebook.platform.extra.PREVIEW_PROPERTY_NAME", previewPropertyName);
            putExtra(bundle, "com.facebook.platform.extra.ACTION_TYPE", actionType);
            bundle.putBoolean("com.facebook.platform.extra.DATA_FAILURES_FATAL", dataErrorsFatal);
            putExtra(bundle, "com.facebook.platform.extra.ACTION", flattenChildrenOfGraphObject(action.getInnerJSONObject()).toString());
            return bundle;
        }

        public OpenGraphDialogBuilderBase setDataErrorsFatal(boolean flag)
        {
            dataErrorsFatal = flag;
            return this;
        }

        public OpenGraphDialogBuilderBase setImageAttachmentFilesForAction(List list)
        {
            return setImageAttachmentFilesForAction(list, false);
        }

        public OpenGraphDialogBuilderBase setImageAttachmentFilesForAction(List list, boolean flag)
        {
            Validate.containsNoNulls(list, "bitmapFiles");
            if(action == null)
            {
                throw new FacebookException("Can not set attachments prior to setting action.");
            } else
            {
                updateActionAttachmentUrls(addImageAttachmentFiles(list), flag);
                return this;
            }
        }

        public OpenGraphDialogBuilderBase setImageAttachmentFilesForObject(String s, List list)
        {
            return setImageAttachmentFilesForObject(s, list, false);
        }

        public OpenGraphDialogBuilderBase setImageAttachmentFilesForObject(String s, List list, boolean flag)
        {
            Validate.notNull(s, "objectProperty");
            Validate.containsNoNulls(list, "bitmapFiles");
            if(action == null)
            {
                throw new FacebookException("Can not set attachments prior to setting action.");
            } else
            {
                updateObjectAttachmentUrls(s, addImageAttachmentFiles(list), flag);
                return this;
            }
        }

        public OpenGraphDialogBuilderBase setImageAttachmentsForAction(List list)
        {
            return setImageAttachmentsForAction(list, false);
        }

        public OpenGraphDialogBuilderBase setImageAttachmentsForAction(List list, boolean flag)
        {
            Validate.containsNoNulls(list, "bitmaps");
            if(action == null)
            {
                throw new FacebookException("Can not set attachments prior to setting action.");
            } else
            {
                updateActionAttachmentUrls(addImageAttachments(list), flag);
                return this;
            }
        }

        public OpenGraphDialogBuilderBase setImageAttachmentsForObject(String s, List list)
        {
            return setImageAttachmentsForObject(s, list, false);
        }

        public OpenGraphDialogBuilderBase setImageAttachmentsForObject(String s, List list, boolean flag)
        {
            Validate.notNull(s, "objectProperty");
            Validate.containsNoNulls(list, "bitmaps");
            if(action == null)
            {
                throw new FacebookException("Can not set attachments prior to setting action.");
            } else
            {
                updateObjectAttachmentUrls(s, addImageAttachments(list), flag);
                return this;
            }
        }

        void updateObjectAttachmentUrls(String s, List list, boolean flag)
        {
            OpenGraphObject opengraphobject;
            try
            {
                opengraphobject = (OpenGraphObject)action.getPropertyAs(s, OpenGraphObject.class);
            }
            // Misplaced declaration of an exception variable
            catch(Exception ex)
            {
                throw new IllegalArgumentException((new StringBuilder()).append("Property '").append(s).append("' is not a graph object").toString());
            }
            if(opengraphobject == null)
                throw new IllegalArgumentException((new StringBuilder()).append("Action does not contain a property '").append(s).append("'").toString());
            if(!opengraphobject.getCreateObject())
                throw new IllegalArgumentException((new StringBuilder()).append("The Open Graph object in '").append(s).append("' is not marked for creation").toString());
            GraphObjectList graphobjectlist = opengraphobject.getImage();

            if(graphobjectlist == null)
                graphobjectlist = com.facebook.model.GraphObject.Factory.createList(GraphObject.class);
            GraphObject graphobject;
            for(Iterator iterator = list.iterator(); iterator.hasNext(); graphobjectlist.add(graphobject))
            {
                String s1 = (String)iterator.next();
                graphobject = com.facebook.model.GraphObject.Factory.create();
                graphobject.setProperty("url", s1);
                if(flag)
                    graphobject.setProperty("user_generated", Boolean.valueOf(true));
            }

            opengraphobject.setImage(graphobjectlist);
            return;
        }

        private OpenGraphAction action;
        private String actionType;
        private boolean dataErrorsFatal;
        private String previewPropertyName;

        public OpenGraphDialogBuilderBase(Activity activity1, OpenGraphAction opengraphaction, String s)
        {
            super(activity1);
            Validate.notNull(opengraphaction, "action");
            Validate.notNullOrEmpty(opengraphaction.getType(), "action.getType()");
            Validate.notNullOrEmpty(s, "previewPropertyName");
            if(opengraphaction.getProperty(s) == null)
            {
                throw new IllegalArgumentException((new StringBuilder()).append("A property named \"").append(s).append("\" was not found on the action.  The name of ").append("the preview property must match the name of an action property.").toString());
            } else
            {
                action = opengraphaction;
                actionType = opengraphaction.getType();
                previewPropertyName = s;
                return;
            }
        }

        public OpenGraphDialogBuilderBase(Activity activity1, OpenGraphAction opengraphaction, String s, String s1)
        {
            super(activity1);
            Validate.notNull(opengraphaction, "action");
            Validate.notNullOrEmpty(s, "actionType");
            Validate.notNullOrEmpty(s1, "previewPropertyName");
            if(opengraphaction.getProperty(s1) == null)
                throw new IllegalArgumentException((new StringBuilder()).append("A property named \"").append(s1).append("\" was not found on the action.  The name of ").append("the preview property must match the name of an action property.").toString());

            String temp = opengraphaction.getType();
            if(!Utility.isNullOrEmpty(temp) && !activity1.equals(s))
            {
                throw new IllegalArgumentException("'actionType' must match the type of 'action' if it is specified. Consider using OpenGraphDialogBuilderBase(Activity activity, OpenGraphAction action, String previewPropertyName) instead.");
            } else
            {
                action = opengraphaction;
                actionType = s;
                previewPropertyName = s1;
                return;
            }
        }
    }

    public static class OpenGraphMessageDialogBuilder extends OpenGraphDialogBuilderBase
    {

        protected EnumSet getDialogFeatures()
        {
            return EnumSet.of(OpenGraphMessageDialogFeature.OG_MESSAGE_DIALOG);
        }

        public OpenGraphMessageDialogBuilder(Activity activity1, OpenGraphAction opengraphaction, String s)
        {
            super(activity1, opengraphaction, s);
        }
    }

    public static enum OpenGraphMessageDialogFeature
        implements DialogFeature
    {
		OG_MESSAGE_DIALOG("OG_MESSAGE_DIALOG", 0, 0x13350ac);


        public String getAction()
        {
            return "com.facebook.platform.action.request.OGMESSAGEPUBLISH_DIALOG";
        }

        public int getMinVersion()
        {
            return minVersion;
        }

        private int minVersion;
		String sType;
		int iType;

        private OpenGraphMessageDialogFeature(String s, int i, int j)
        {
		sType = s;
		iType = i;
            minVersion = j;
        }
    }

    public static class PendingCall
        implements Parcelable
    {

        private void setRequestCode(int i)
        {
            requestCode = i;
        }

        private void setRequestIntent(Intent intent)
        {
            requestIntent = intent;
        }

        public int describeContents()
        {
            return 0;
        }

        public UUID getCallId()
        {
            return callId;
        }

        public int getRequestCode()
        {
            return requestCode;
        }

        public Intent getRequestIntent()
        {
            return requestIntent;
        }

        public void writeToParcel(Parcel parcel, int i)
        {
            parcel.writeString(callId.toString());
            parcel.writeParcelable(requestIntent, 0);
            parcel.writeInt(requestCode);
        }

        public static final android.os.Parcelable.Creator CREATOR = new android.os.Parcelable.Creator() {

            public PendingCall createFromParcel(Parcel parcel)
            {
                return new PendingCall(parcel);
            }

            public PendingCall[] newArray(int i)
            {
                return new PendingCall[i];
            }

        };
        private UUID callId;
        private int requestCode;
        private Intent requestIntent;




        public PendingCall(int i)
        {
            callId = UUID.randomUUID();
            requestCode = i;
        }

        private PendingCall(Parcel parcel)
        {
            callId = UUID.fromString(parcel.readString());
            requestIntent = (Intent)parcel.readParcelable(null);
            requestCode = parcel.readInt();
        }

    }

    private static abstract class PhotoDialogBuilderBase extends Builder
    {

        public PhotoDialogBuilderBase addPhotoFiles(Collection collection)
        {
            imageAttachmentUrls.addAll(addImageAttachmentFiles(collection));
            return this;
        }

        public PhotoDialogBuilderBase addPhotos(Collection collection)
        {
            imageAttachmentUrls.addAll(addImageAttachments(collection));
            return this;
        }

        abstract int getMaximumNumberOfPhotos();

        protected Bundle getMethodArguments()
        {
            Bundle bundle = new Bundle();
            putExtra(bundle, "PLACE", place);
            bundle.putStringArrayList("PHOTOS", imageAttachmentUrls);
            if(!Utility.isNullOrEmpty(friends))
                bundle.putStringArrayList("FRIENDS", friends);
            return bundle;
        }

        protected Bundle setBundleExtras(Bundle bundle)
        {
            putExtra(bundle, "com.facebook.platform.extra.APPLICATION_ID", applicationId);
            putExtra(bundle, "com.facebook.platform.extra.APPLICATION_NAME", applicationName);
            putExtra(bundle, "com.facebook.platform.extra.PLACE", place);
            bundle.putStringArrayList("com.facebook.platform.extra.PHOTOS", imageAttachmentUrls);
            if(!Utility.isNullOrEmpty(friends))
                bundle.putStringArrayList("com.facebook.platform.extra.FRIENDS", friends);
            return bundle;
        }

        public PhotoDialogBuilderBase setFriends(List list)
        {
            if(list == null)
                friends = null;
            else
                friends = new ArrayList(list);
            return this;
        }

        public PhotoDialogBuilderBase setPlace(String s)
        {
            place = s;
            return this;
        }

        void validate()
        {
            super.validate();
            if(imageAttachmentUrls.isEmpty())
                throw new FacebookException("Must specify at least one photo.");
            if(imageAttachmentUrls.size() > getMaximumNumberOfPhotos())
                throw new FacebookException(String.format("Cannot add more than %d photos.", new Object[] {
                    Integer.valueOf(getMaximumNumberOfPhotos())
                }));
            else
                return;
        }

        static int MAXIMUM_PHOTO_COUNT = 6;
        private ArrayList friends;
        private ArrayList imageAttachmentUrls;
        private String place;


        public PhotoDialogBuilderBase(Activity activity1)
        {
            super(activity1);
            imageAttachmentUrls = new ArrayList();
        }
    }

    public static class PhotoMessageDialogBuilder extends PhotoDialogBuilderBase
    {

        protected EnumSet getDialogFeatures()
        {
            return EnumSet.of(MessageDialogFeature.MESSAGE_DIALOG, MessageDialogFeature.PHOTOS);
        }

        int getMaximumNumberOfPhotos()
        {
            return MAXIMUM_PHOTO_COUNT;
        }


        public PhotoMessageDialogBuilder setFriends(List list)
        {
            return this;
        }


        public PhotoMessageDialogBuilder setPlace(String s)
        {
            return this;
        }

        public PhotoMessageDialogBuilder(Activity activity1)
        {
            super(activity1);
        }
    }

    public static class PhotoShareDialogBuilder extends PhotoDialogBuilderBase
    {

        protected EnumSet getDialogFeatures()
        {
            return EnumSet.of(ShareDialogFeature.SHARE_DIALOG, ShareDialogFeature.PHOTOS);
        }

        int getMaximumNumberOfPhotos()
        {
            return MAXIMUM_PHOTO_COUNT;
        }

        public PhotoShareDialogBuilder(Activity activity1)
        {
            super(activity1);
        }
    }

    public static class ShareDialogBuilder extends ShareDialogBuilderBase
    {

        protected EnumSet getDialogFeatures()
        {
            return EnumSet.of(ShareDialogFeature.SHARE_DIALOG);
        }

        public ShareDialogBuilder(Activity activity1)
        {
            super(activity1);
        }
    }

    private static abstract class ShareDialogBuilderBase extends Builder
    {

        protected Bundle getMethodArguments()
        {
            Bundle bundle = new Bundle();
            putExtra(bundle, "TITLE", name);
            putExtra(bundle, "SUBTITLE", caption);
            putExtra(bundle, "DESCRIPTION", description);
            putExtra(bundle, "LINK", link);
            putExtra(bundle, "IMAGE", picture);
            putExtra(bundle, "PLACE", place);
            putExtra(bundle, "REF", ref);
            bundle.putBoolean("DATA_FAILURES_FATAL", dataErrorsFatal);
            if(!Utility.isNullOrEmpty(friends))
                bundle.putStringArrayList("FRIENDS", friends);
            return bundle;
        }

        protected Bundle setBundleExtras(Bundle bundle)
        {
            putExtra(bundle, "com.facebook.platform.extra.APPLICATION_ID", applicationId);
            putExtra(bundle, "com.facebook.platform.extra.APPLICATION_NAME", applicationName);
            putExtra(bundle, "com.facebook.platform.extra.TITLE", name);
            putExtra(bundle, "com.facebook.platform.extra.SUBTITLE", caption);
            putExtra(bundle, "com.facebook.platform.extra.DESCRIPTION", description);
            putExtra(bundle, "com.facebook.platform.extra.LINK", link);
            putExtra(bundle, "com.facebook.platform.extra.IMAGE", picture);
            putExtra(bundle, "com.facebook.platform.extra.PLACE", place);
            putExtra(bundle, "com.facebook.platform.extra.REF", ref);
            bundle.putBoolean("com.facebook.platform.extra.DATA_FAILURES_FATAL", dataErrorsFatal);
            if(!Utility.isNullOrEmpty(friends))
                bundle.putStringArrayList("com.facebook.platform.extra.FRIENDS", friends);
            return bundle;
        }

        public ShareDialogBuilderBase setCaption(String s)
        {
            caption = s;
            return this;
        }

        public ShareDialogBuilderBase setDataErrorsFatal(boolean flag)
        {
            dataErrorsFatal = flag;
            return this;
        }

        public ShareDialogBuilderBase setDescription(String s)
        {
            description = s;
            return this;
        }

        public ShareDialogBuilderBase setFriends(List list)
        {
            if(list == null)
                friends = null;
            else
                friends = new ArrayList(list);

            return this;
        }

        public ShareDialogBuilderBase setLink(String s)
        {
            link = s;
            return this;
        }

        public ShareDialogBuilderBase setName(String s)
        {
            name = s;
            return this;
        }

        public ShareDialogBuilderBase setPicture(String s)
        {
            picture = s;
            return this;
        }

        public ShareDialogBuilderBase setPlace(String s)
        {
            place = s;
            return this;
        }

        public ShareDialogBuilderBase setRef(String s)
        {
            ref = s;
            return this;
        }

        private String caption;
        private boolean dataErrorsFatal;
        private String description;
        private ArrayList friends;
        protected String link;
        private String name;
        private String picture;
        private String place;
        private String ref;

        public ShareDialogBuilderBase(Activity activity1)
        {
            super(activity1);
        }
    }

    public static enum ShareDialogFeature
        implements DialogFeature
    {

		SHARE_DIALOG("SHARE_DIALOG", 0, 0x1332b3a),
		PHOTOS("PHOTOS", 1, 0x13350ac),
		VIDEO("VIDEO", 2, 0x13353e4);

        public String getAction()
        {
            return "com.facebook.platform.action.request.FEED_DIALOG";
        }

        public int getMinVersion()
        {
            return minVersion;
        }

		String sType;
		int iType;
        int minVersion;

        private ShareDialogFeature(String s, int i, int j)
        {
			sType = s;
			iType = i;

            minVersion = j;
        }
    }

    private static abstract class VideoDialogBuilderBase extends Builder
    {

        public VideoDialogBuilderBase addVideoFile(File file)
        {
            videoAttachmentUrl = addVideoAttachmentFile(file);
            return this;
        }

        protected Bundle getMethodArguments()
        {
            Bundle bundle = new Bundle();
            putExtra(bundle, "PLACE", place);
            bundle.putString("VIDEO", videoAttachmentUrl);
            return bundle;
        }

        public VideoDialogBuilderBase setPlace(String s)
        {
            place = s;
            return this;
        }

        public VideoDialogBuilderBase setVideoUrl(String s)
        {
            videoAttachmentUrl = s;
            return this;
        }

        void validate()
        {
            super.validate();
            if(videoAttachmentUrl == null || videoAttachmentUrl.isEmpty())
                throw new FacebookException("Must specify at least one video.");
            else
                return;
        }

        private String place;
        private String videoAttachmentUrl;

        public VideoDialogBuilderBase(Activity activity1)
        {
            super(activity1);
        }
    }

    public static class VideoMessageDialogBuilder extends VideoDialogBuilderBase
    {

        protected EnumSet getDialogFeatures()
        {
            return EnumSet.of(MessageDialogFeature.MESSAGE_DIALOG, MessageDialogFeature.VIDEO);
        }


        public VideoMessageDialogBuilder setPlace(String s)
        {
            return this;
        }

        public VideoMessageDialogBuilder(Activity activity1)
        {
            super(activity1);
        }
    }

    public static class VideoShareDialogBuilder extends VideoDialogBuilderBase
    {

        protected EnumSet getDialogFeatures()
        {
            return EnumSet.of(ShareDialogFeature.SHARE_DIALOG, ShareDialogFeature.VIDEO);
        }

        public VideoShareDialogBuilder(Activity activity1)
        {
            super(activity1);
        }
    }


    private FacebookDialog(Activity activity1, Fragment fragment1, PendingCall pendingcall, OnPresentCallback onpresentcallback)
    {
        activity = activity1;
        fragment = fragment1;
        appCall = pendingcall;
        onPresentCallback = onpresentcallback;
    }


    public static boolean canPresentMessageDialog(Context context, MessageDialogFeature amessagedialogfeature[])
    {
        return handleCanPresent(context, EnumSet.of(MessageDialogFeature.MESSAGE_DIALOG, amessagedialogfeature));
    }

    public static boolean canPresentOpenGraphActionDialog(Context context, OpenGraphActionDialogFeature aopengraphactiondialogfeature[])
    {
        return handleCanPresent(context, EnumSet.of(OpenGraphActionDialogFeature.OG_ACTION_DIALOG, aopengraphactiondialogfeature));
    }

    public static boolean canPresentOpenGraphMessageDialog(Context context, OpenGraphMessageDialogFeature aopengraphmessagedialogfeature[])
    {
        return handleCanPresent(context, EnumSet.of(OpenGraphMessageDialogFeature.OG_MESSAGE_DIALOG, aopengraphmessagedialogfeature));
    }

    public static boolean canPresentShareDialog(Context context, ShareDialogFeature asharedialogfeature[])
    {
        return handleCanPresent(context, EnumSet.of(ShareDialogFeature.SHARE_DIALOG, asharedialogfeature));
    }

    private static String getActionForFeatures(Iterable iterable)
    {
        String temp = null;
        Iterator iterator = iterable.iterator();
        //iterable = obj;
        if(iterator.hasNext())
            temp = ((DialogFeature)iterator.next()).getAction();
        return temp;
    }

    private static NativeAppCallAttachmentStore getAttachmentStore()
    {
        if(attachmentStore == null)
            attachmentStore = new NativeAppCallAttachmentStore();
        return attachmentStore;
    }

    private static String getEventName(Intent intent)
    {
        String s = intent.getStringExtra("com.facebook.platform.protocol.PROTOCOL_ACTION");
        boolean flag1 = intent.hasExtra("com.facebook.platform.extra.PHOTOS");
        boolean flag4 = false;
        Object obj = intent.getBundleExtra("com.facebook.platform.protocol.METHOD_ARGS");
        boolean flag2 = flag1;
        boolean flag3 = flag4;
        ArrayList arrayList;
        if(obj != null)
        {
            arrayList = ((Bundle) (obj)).getStringArrayList("PHOTOS");
            obj = ((Bundle) (obj)).getString("VIDEO");
            boolean flag = flag1;
            if(intent != null)
            {
                flag = flag1;
                if(!arrayList.isEmpty())
                    flag = true;
            }
            flag2 = flag;
            flag3 = flag4;
            if(obj != null)
            {
                flag2 = flag;
                flag3 = flag4;
                if(!((String) (obj)).isEmpty())
                {
                    flag3 = true;
                    flag2 = flag;
                }
            }
        }
        return getEventName(s, flag2, flag3);
    }

    private static String getEventName(String s, boolean flag, boolean flag1)
    {
        if(s.equals("com.facebook.platform.action.request.FEED_DIALOG"))
        {
            if(flag1)
                return "fb_dialogs_present_share_video";
            if(flag)
                return "fb_dialogs_present_share_photo";
            else
                return "fb_dialogs_present_share";
        }
        if(s.equals("com.facebook.platform.action.request.MESSAGE_DIALOG"))
            if(flag)
                return "fb_dialogs_present_message_photo";
            else
                return "fb_dialogs_present_message";
        if(s.equals("com.facebook.platform.action.request.OGACTIONPUBLISH_DIALOG"))
            return "fb_dialogs_present_share_og";
        if(s.equals("com.facebook.platform.action.request.OGMESSAGEPUBLISH_DIALOG"))
            return "fb_dialogs_present_message_og";
        if(s.equals("com.facebook.platform.action.request.LIKE_DIALOG"))
            return "fb_dialogs_present_like";
        else
            throw new FacebookException("An unspecified action was presented");
    }

    public static String getNativeDialogCompletionGesture(Bundle bundle)
    {
        if(bundle.containsKey("completionGesture"))
            return bundle.getString("completionGesture");
        else
            return bundle.getString("com.facebook.platform.extra.COMPLETION_GESTURE");
    }

    public static boolean getNativeDialogDidComplete(Bundle bundle)
    {
        if(bundle.containsKey("didComplete"))
            return bundle.getBoolean("didComplete");
        else
            return bundle.getBoolean("com.facebook.platform.extra.DID_COMPLETE", false);
    }

    public static String getNativeDialogPostId(Bundle bundle)
    {
        if(bundle.containsKey("postId"))
            return bundle.getString("postId");
        else
            return bundle.getString("com.facebook.platform.extra.POST_ID");
    }

    private static int getProtocolVersionForNativeDialog(Context context, String s, int ai[])
    {
        return NativeProtocol.getLatestAvailableProtocolVersionForAction(context, s, ai);
    }

    private static int[] getVersionSpecForFeature(String s, String s1, DialogFeature dialogfeature)
    {
        Utility.DialogFeatureConfig dialogFeatureConfig = Utility.getDialogFeatureConfig(s, s1, dialogfeature.name());
        if(s != null)
            return dialogFeatureConfig.getVersionSpec();
        else
            return (new int[] {
                dialogfeature.getMinVersion()
            });
    }

    private static int[] getVersionSpecForFeatures(String s, String s1, Iterable iterable)
    {
        int result[] = null;
        Iterator iterator = iterable.iterator();
        for(; iterator.hasNext(); result = Utility.intersectRanges(result, getVersionSpecForFeature(s, s1, (DialogFeature)iterator.next())));
        return result;
    }

    public static boolean handleActivityResult(Context context, PendingCall pendingcall, int i, Intent intent, Callback callback)
    {
        if(i != pendingcall.getRequestCode())
            return false;
        if(attachmentStore != null)
            attachmentStore.cleanupAttachmentsForCall(context, pendingcall.getCallId());

        Bundle bundle;
        if(callback != null)
            if(NativeProtocol.isErrorResult(intent))
            {
                bundle = NativeProtocol.getErrorDataFromResultIntent(intent);
                callback.onError(pendingcall, NativeProtocol.getExceptionFromErrorData(bundle), bundle);
            } else
            {
                callback.onComplete(pendingcall, NativeProtocol.getSuccessResultsFromIntent(intent));
            }
        return true;
    }

    private static boolean handleCanPresent(Context context, Iterable iterable)
    {
        String s2 = getActionForFeatures(iterable);
        String s1 = Settings.getApplicationId();
        String s = s1;
        if(Utility.isNullOrEmpty(s1))
            s = Utility.getMetadataApplicationId(context);
        return getProtocolVersionForNativeDialog(context, s2, getVersionSpecForFeatures(s, s2, iterable)) != -1;
    }

    private static void logDialogActivity(Activity activity1, Fragment fragment1, String s, String s1)
    {
        if(fragment1 != null)
            activity1 = fragment1.getActivity();
        AppEventsLogger appEventsLogger = AppEventsLogger.newLogger(activity1);
        Bundle bundle = new Bundle();
        bundle.putString("fb_dialog_outcome", s1);
        appEventsLogger.logSdkEvent(s, null, bundle);
    }

    public PendingCall present()
    {
        logDialogActivity(activity, fragment, getEventName(appCall.getRequestIntent()), "Completed");
        if(onPresentCallback != null)
            try
            {
                onPresentCallback.onPresent(activity);
            }
            catch(Exception exception)
            {
                throw new FacebookException(exception);
            }
        if(fragment != null)
            fragment.startActivityForResult(appCall.getRequestIntent(), appCall.getRequestCode());
        else
            activity.startActivityForResult(appCall.getRequestIntent(), appCall.getRequestCode());
        return appCall;
    }

    public static final String COMPLETION_GESTURE_CANCEL = "cancel";
    private static final String EXTRA_DIALOG_COMPLETE_KEY = "com.facebook.platform.extra.DID_COMPLETE";
    private static final String EXTRA_DIALOG_COMPLETION_GESTURE_KEY = "com.facebook.platform.extra.COMPLETION_GESTURE";
    private static final String EXTRA_DIALOG_COMPLETION_ID_KEY = "com.facebook.platform.extra.POST_ID";
    public static final String RESULT_ARGS_DIALOG_COMPLETE_KEY = "didComplete";
    public static final String RESULT_ARGS_DIALOG_COMPLETION_GESTURE_KEY = "completionGesture";
    public static final String RESULT_ARGS_DIALOG_COMPLETION_ID_KEY = "postId";
    private static NativeAppCallAttachmentStore attachmentStore;
    private Activity activity;
    private PendingCall appCall;
    private Fragment fragment;
    private OnPresentCallback onPresentCallback;

}
