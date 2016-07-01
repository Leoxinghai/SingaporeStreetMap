// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.facebook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import com.facebook.*;
import com.facebook.model.GraphUser;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import streetdirectory.mobile.core.SDLogger;
import streetdirectory.mobile.core.storage.ApplicationPreferences;

// Referenced classes of package streetdirectory.mobile.facebook:
//            FacebookManagerHandler

public class FacebookManager
{
    private class SessionStatusCallback
        implements com.facebook.Session.StatusCallback
    {

        public void call(Session session, SessionState sessionstate, Exception exception)
        {
            if(sessionstate == SessionState.OPENED)
                onConnected(0);
            else
            if(sessionstate == SessionState.CLOSED)
                onDisconnected(0);
            else
            if(session.getState() != SessionState.OPENING);
            if(pendingPublishReauthorization && sessionstate.equals(SessionState.OPENED_TOKEN_UPDATED))
            {
                pendingPublishReauthorization = false;
                onReauthorizedSuccess(0);
            }
            if(exception != null)
                SDLogger.printStackTrace(exception, "open session failed");
        }


        private SessionStatusCallback()
        {
            super();
        }

    }


    public FacebookManager(String s)
    {
        _callbacks = new ArrayList();
        userImage = null;
        statusCallback = new SessionStatusCallback();
        pendingPublishReauthorization = false;
    }

    private boolean isSubsetOf(Collection collection, Collection collection1)
    {
        for(Iterator iterator = collection.iterator(); iterator.hasNext();)
            if(!collection1.contains((String)iterator.next()))
                return false;

        return true;
    }

    public void addCallback(FacebookManagerHandler facebookmanagerhandler)
    {
        for(Iterator iterator = (new ArrayList(_callbacks)).iterator(); iterator.hasNext();)
        {
            WeakReference weakreference = (WeakReference)iterator.next();
            if(weakreference != null && weakreference.get() == facebookmanagerhandler)
                return;
        }

        _callbacks.add(new WeakReference(facebookmanagerhandler));
    }

    public boolean clearSession()
    {
        android.content.SharedPreferences.Editor editor = (new ApplicationPreferences(_context)).createEditor();
        editor.remove("facebookUserToken");
        editor.remove("facebookUserExpires");
        editor.remove("facebookUserID");
        editor.remove("facebookUsername");
        return editor.commit();
    }

    public Context getContext()
    {
        return _context;
    }

    public void getUserAsync()
    {
        getUserAsync(0);
    }

    public void getUserAsync(int i)
    {
        AsyncTask asynctask = new AsyncTask() {

            protected Boolean doInBackground(Void avoid[])
            {
                return populateUserData(Session.getActiveSession());
            }

            protected Object doInBackground(Object aobj[])
            {
                return doInBackground((Void[])aobj);
            }

        };
        try
        {
            asynctask.execute(new Void[] {
                null
            });
            return;
        }
        catch(Exception exception)
        {
            return;
        }
    }

    public String getUserID(Context context)
    {
        _context = context;
        ApplicationPreferences temp = new ApplicationPreferences(_context);
        if(temp != null)
        {
            String temp2 = temp.getStringForKey("facebookUserID", "");
            if(temp2 != null)
                return temp2;
        }
        return "";
    }

    public boolean getUserImage()
    {
        if(userID == null)
            return false;

        try {
            Object obj;
            HttpURLConnection httpurlconnection;
            HttpURLConnection httpurlconnection1;
            java.io.InputStream inputstream;
            obj = null;
            inputstream = null;
            httpurlconnection1 = null;
            httpurlconnection = null;
            Object obj1 = new URL((new StringBuilder()).append("http://graph.facebook.com/").append(userID).append("/picture").toString());
            obj = inputstream;
            httpurlconnection = httpurlconnection1;
            httpurlconnection1 = (HttpURLConnection) ((URL) (obj1)).openConnection();
            obj = httpurlconnection1;
            httpurlconnection = httpurlconnection1;
            httpurlconnection1.setDoInput(true);
            obj = httpurlconnection1;
            httpurlconnection = httpurlconnection1;
            httpurlconnection1.connect();
            obj = httpurlconnection1;
            httpurlconnection = httpurlconnection1;
            inputstream = httpurlconnection1.getInputStream();
            obj = httpurlconnection1;
            httpurlconnection = httpurlconnection1;
            if (userImage != null) {
                obj = httpurlconnection1;
                httpurlconnection = httpurlconnection1;
                obj1 = userImage;
                obj = httpurlconnection1;
                httpurlconnection = httpurlconnection1;
                userImage = null;
                obj = httpurlconnection1;
                httpurlconnection = httpurlconnection1;
                onAvatarChanged(userImage);
                if (obj1 != null) {
                    obj = httpurlconnection1;
                    httpurlconnection = httpurlconnection1;
                    ((Bitmap) (obj1)).recycle();
                    obj = httpurlconnection1;
                    httpurlconnection = httpurlconnection1;
                    userImage = BitmapFactory.decodeStream(inputstream);
                    obj = httpurlconnection1;
                    httpurlconnection = httpurlconnection1;
                    onAvatarChanged(userImage);
                    httpurlconnection1.disconnect();
                    return true;
                }
            }
        } catch(Exception ex) {
            ex.printStackTrace();
            //httpurlconnection.disconnect();
        }
        return false;
    }

    public boolean hasLoginFacebook(Context context)
    {
        _context = context;
        Session temp = Session.getActiveSession();
        if(temp == null)
        {
            if(mSavedInstanceState != null)
                temp = Session.restoreSession(_context, null, statusCallback, mSavedInstanceState);
            Object obj = context;
            if(temp == null)
                temp = new Session(_context);
            Session.setActiveSession(((Session) (temp)));
            if(((Session) (temp)).getState().equals(SessionState.OPENED))
                return true;
            if(((Session) (temp)).getState().equals(SessionState.CREATED_TOKEN_LOADED))
            {
                ((Session) (temp)).openForRead((new com.facebook.Session.OpenRequest((Activity)_context)).setCallback(statusCallback));
                return true;
            }
        }
        return false;
    }

    public boolean isSessionValid()
    {
        return isSessionValid(false);
    }

    public boolean isSessionValid(boolean flag)
    {
        Session session = Session.getActiveSession();
        return session != null && !session.isClosed();
    }

    public void login(Activity activity, int i, Bundle bundle)
    {
        _context = activity;
        if(hasLoginFacebook(_context))
        {
            populateUserData(Session.getActiveSession());
            onConnected(0);
            return;
        } else
        {
            Session.openActiveSession(activity, true, statusCallback);
            return;
        }
    }

    public void login(Activity activity, Bundle bundle)
    {
        login(activity, 0, bundle);
    }

    public void logout(Context context)
    {
        logout(context, 0);
    }

    public void logout(Context context, final int senderId)
    {
        (new AsyncTask() {

            protected Boolean doInBackground(Void avoid[])
            {
                try
                {
                    Session temp = Session.getActiveSession();
                    if(!temp.isClosed())
                        temp.closeAndClearTokenInformation();
                    clearSession();
                    userID = null;
                    username = null;
                    Bitmap bitmap = userImage;
                    userImage = null;
                    onAvatarChanged(userImage);
                    if(bitmap != null)
                        bitmap.recycle();
                }
                // Misplaced declaration of an exception variable
                catch(Exception ex)
                {
                    return Boolean.valueOf(false);
                }
                return Boolean.valueOf(true);
            }

            protected Object doInBackground(Object aobj[])
            {
                return doInBackground((Void[])aobj);
            }

            protected void onPostExecute(Boolean boolean1)
            {
                super.onPostExecute(boolean1);
                onDisconnected(senderId);
            }

            protected void onPostExecute(Object obj)
            {
                onPostExecute((Boolean)obj);
            }

        }).execute(new Void[] {
            null
        });
    }

    public void onActivityResult(int i, int j, Intent intent)
    {
    }

    protected void onAvatarChanged(Bitmap bitmap)
    {
        Iterator iterator = _callbacks.iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            WeakReference weakreference = (WeakReference)iterator.next();
            if(weakreference != null)
                ((FacebookManagerHandler)weakreference.get()).onUserDidReceiveImage(bitmap);
        } while(true);
    }

    protected void onCanceled(int i)
    {
        Iterator iterator = _callbacks.iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            WeakReference weakreference = (WeakReference)iterator.next();
            if(weakreference != null)
                ((FacebookManagerHandler)weakreference.get()).onUserDidCancel(i);
        } while(true);
    }

    protected void onConnected(int i)
    {
        populateUserData(Session.getActiveSession());
        Iterator iterator = _callbacks.iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            WeakReference weakreference = (WeakReference)iterator.next();
            if(weakreference != null)
                ((FacebookManagerHandler)weakreference.get()).onUserDidLogin(i);
        } while(true);
    }

    protected void onDisconnected(int i)
    {
        Iterator iterator = _callbacks.iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            WeakReference weakreference = (WeakReference)iterator.next();
            if(weakreference != null)
                ((FacebookManagerHandler)weakreference.get()).onUserDidLogout(i);
        } while(true);
    }

    protected void onError(Exception exception, int i)
    {
        Iterator iterator = _callbacks.iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            WeakReference weakreference = (WeakReference)iterator.next();
            if(weakreference != null)
                ((FacebookManagerHandler)weakreference.get()).onUserDidFailedLoadInfo(i);
        } while(true);
    }

    protected void onPostCompleted(boolean flag, int i)
    {
        Iterator iterator = _callbacks.iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            WeakReference weakreference = (WeakReference)iterator.next();
            if(weakreference != null)
                ((FacebookManagerHandler)weakreference.get()).onUserDidFinishPosting(flag, i);
        } while(true);
    }

    protected void onReauthorizedSuccess(int i)
    {
        Iterator iterator = _callbacks.iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            WeakReference weakreference = (WeakReference)iterator.next();
            if(weakreference != null)
                ((FacebookManagerHandler)weakreference.get()).onReauthorizedSuccess(i);
        } while(true);
    }

    protected void onUserDataDidLoad(GraphUser graphuser)
    {
        Iterator iterator = _callbacks.iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            WeakReference weakreference = (WeakReference)iterator.next();
            if(weakreference != null)
                ((FacebookManagerHandler)weakreference.get()).onUserDataDidLoad(graphuser);
        } while(true);
    }

    protected void onUserDataFailedToLoad()
    {
        Iterator iterator = _callbacks.iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            WeakReference weakreference = (WeakReference)iterator.next();
            if(weakreference != null)
                ((FacebookManagerHandler)weakreference.get()).onUserDataFailedToLoad();
        } while(true);
    }

    protected void onUserReceived(String s, String s1, Bitmap bitmap, Integer integer)
    {
        Iterator iterator = _callbacks.iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            WeakReference temp2 = (WeakReference)iterator.next();
            if(temp2 != null)
                ((FacebookManagerHandler)temp2.get()).onUserDidReceiveImage(bitmap);
        } while(true);
    }

    public Boolean populateUserData(final Session session)
    {
        try
        {
            Request.newMeRequest(session, new com.facebook.Request.GraphUserCallback() {

                public void onCompleted(GraphUser graphuser, Response response)
                {
                    if(session == Session.getActiveSession() && graphuser != null)
                    {
                        userID = graphuser.getId();
                        username = graphuser.getName();
                        saveSession();
                        onUserDataDidLoad(graphuser);
                    }
                    if(response.getError() != null)
                        onUserDataFailedToLoad();
                }

            }
).executeAsync();
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            onUserDataFailedToLoad();
            return Boolean.valueOf(false);
        }
        return Boolean.valueOf(true);
    }

    public boolean postOnWall(String s, String s1, String s2, String s3, int i)
    {
        Session session;
        Object obj;
        try
        {
            session = Session.getActiveSession();
        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            onError(ex, i);
            return false;
        }
        if(session != null) {
            obj = session.getPermissions();
            if (!isSubsetOf(PERMISSIONS, ((Collection) (obj)))) {
                pendingPublishReauthorization = true;
                session.requestNewPublishPermissions(new com.facebook.Session.NewPermissionsRequest((Activity) _context, PERMISSIONS));
                return false;
            }
        }
        obj = new Bundle();
        ((Bundle) (obj)).putString("name", s);
        ((Bundle) (obj)).putString("caption", s1);
        ((Bundle) (obj)).putString("description", "");
        ((Bundle) (obj)).putString("link", s2);
        ((Bundle) (obj)).putString("picture", s3);
        Request.Callback temp2 = new com.facebook.Request.Callback() {

            public void onCompleted(Response response)
            {
                FacebookRequestError temp3 = response.getError();
                if(temp3 != null)
                {
                    SDLogger.debug((new StringBuilder()).append("error post to fb ").append(temp3.getErrorMessage()).toString());
                    return;
                } else
                {
                    SDLogger.debug("success post to fb");
                    return;
                }
            }

        };

        (new RequestAsyncTask(new Request[] {
            new Request(session, "me/feed", ((Bundle) (obj)), HttpMethod.POST, temp2)
        })).execute(new Void[0]);
        return true;
    }

    public void removeCallback(FacebookManagerHandler facebookmanagerhandler)
    {
        ArrayList arraylist = _callbacks;
        Iterator iterator = _callbacks.iterator();
        WeakReference weakreference;
        for(;iterator.hasNext();) {
            weakreference = (WeakReference)iterator.next();
            if(weakreference == null)
                continue;
            if(weakreference.get() == facebookmanagerhandler)
                _callbacks.remove(weakreference);
        }
    }

    public boolean restoreSession(Context context)
    {
        if(hasLoginFacebook(context))
        {
            populateUserData(Session.getActiveSession());
            onConnected(0);
        }
        return false;
    }

    public boolean saveSession()
    {
        android.content.SharedPreferences.Editor editor = (new ApplicationPreferences(_context)).createEditor();
        editor.putString("facebookUserToken", token);
        editor.putLong("facebookUserExpires", expires);
        editor.putString("facebookUserID", userID);
        editor.putString("facebookUsername", username);
        return editor.commit();
    }

    public static final String EXPIRES = "facebookUserExpires";
    private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
    private static final List PERMISSIONS = Arrays.asList(new String[] {
        "publish_actions"
    });
    public static final String TOKEN = "facebookUserToken";
    public static final String USERNAME = "facebookUsername";
    public static final String USER_ID = "facebookUserID";
    protected ArrayList _callbacks;
    protected Context _context;
    public long expires;
    private Bundle mSavedInstanceState;
    private boolean pendingPublishReauthorization;
    private com.facebook.Session.StatusCallback statusCallback;
    public String token;
    public String userID;
    public Bitmap userImage;
    public String username;




/*
    static boolean access$102(FacebookManager facebookmanager, boolean flag)
    {
        facebookmanager.pendingPublishReauthorization = flag;
        return flag;
    }

*/
}
