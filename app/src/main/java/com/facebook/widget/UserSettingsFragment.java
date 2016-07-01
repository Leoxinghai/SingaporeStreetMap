// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.widget;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.widget.TextView;
import com.facebook.*;
import com.facebook.internal.*;
import com.facebook.model.GraphUser;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

// Referenced classes of package com.facebook.widget:
//            FacebookFragment, LoginButton

public class UserSettingsFragment extends FacebookFragment
{

    public UserSettingsFragment()
    {
        loginButtonProperties = new LoginButton.LoginButtonProperties();
    }

    private void fetchUserInfo()
    {
        final Session currentSession = getSession();
        if(currentSession != null && currentSession.isOpened())
        {
            if(currentSession != userInfoSession)
            {
                Request request = Request.newMeRequest(currentSession, new com.facebook.Request.GraphUserCallback() {

                    public void onCompleted(GraphUser graphuser, Response response)
                    {
                        if(currentSession == getSession())
                        {
                            user = graphuser;
                            updateUI();
                        }
                        if(response.getError() != null)
                            loginButton.handleError(response.getError().getException());
                    }

                });
                Bundle bundle = new Bundle();
                bundle.putString("fields", REQUEST_FIELDS);
                request.setParameters(bundle);
                Request.executeBatchAsync(new Request[] {
                    request
                });
                userInfoSession = currentSession;
            }
            return;
        } else
        {
            user = null;
            return;
        }
    }

    private ImageRequest getImageRequest()
    {
        ImageRequest imagerequest;
        try
        {
            imagerequest = (new com.facebook.internal.ImageRequest.Builder(getActivity(), ImageRequest.getProfilePictureUrl(user.getId(), getResources().getDimensionPixelSize(com.facebook.android.R.dimen.com_facebook_usersettingsfragment_profile_picture_width), getResources().getDimensionPixelSize(com.facebook.android.R.dimen.com_facebook_usersettingsfragment_profile_picture_height)))).setCallerTag(this).setCallback(new com.facebook.internal.ImageRequest.Callback() {

                public void onCompleted(ImageResponse imageresponse)
                {
                    processImageResponse(user.getId(), imageresponse);
                }

            }
).build();
        }
        catch(URISyntaxException urisyntaxexception)
        {
            return null;
        }
        return imagerequest;
    }

    private void processImageResponse(String s, ImageResponse imageresponse)
    {
        if(imageresponse != null)
        {
            Object obj = imageresponse.getBitmap();
            if(obj != null)
            {
                obj = new BitmapDrawable(getResources(), ((android.graphics.Bitmap) (obj)));
                ((BitmapDrawable) (obj)).setBounds(0, 0, getResources().getDimensionPixelSize(com.facebook.android.R.dimen.com_facebook_usersettingsfragment_profile_picture_width), getResources().getDimensionPixelSize(com.facebook.android.R.dimen.com_facebook_usersettingsfragment_profile_picture_height));
                userProfilePic = ((Drawable) (obj));
                userProfilePicID = s;
                connectedStateLabel.setCompoundDrawables(null, ((Drawable) (obj)), null, null);
                connectedStateLabel.setTag(imageresponse.getRequest().getImageUri());
            }
        }
    }

    private void updateUI()
    {
        if(!isAdded())
            return;
        if(isSessionOpen())
        {
            connectedStateLabel.setTextColor(getResources().getColor(com.facebook.android.R.color.com_facebook_usersettingsfragment_connected_text_color));
            connectedStateLabel.setShadowLayer(1.0F, 0.0F, -1F, getResources().getColor(com.facebook.android.R.color.com_facebook_usersettingsfragment_connected_shadow_color));
            if(user != null)
            {
                ImageRequest imagerequest = getImageRequest();
                if(imagerequest != null)
                {
                    URI uri = imagerequest.getImageUri();
                    if(!uri.equals(connectedStateLabel.getTag()))
                        if(user.getId().equals(userProfilePicID))
                        {
                            connectedStateLabel.setCompoundDrawables(null, userProfilePic, null, null);
                            connectedStateLabel.setTag(uri);
                        } else
                        {
                            ImageDownloader.downloadAsync(imagerequest);
                        }
                }
                connectedStateLabel.setText(user.getName());
                return;
            } else
            {
                connectedStateLabel.setText(getResources().getString(com.facebook.android.R.string.com_facebook_usersettingsfragment_logged_in));
                Drawable drawable = getResources().getDrawable(com.facebook.android.R.drawable.com_facebook_profile_default_icon);
                drawable.setBounds(0, 0, getResources().getDimensionPixelSize(com.facebook.android.R.dimen.com_facebook_usersettingsfragment_profile_picture_width), getResources().getDimensionPixelSize(com.facebook.android.R.dimen.com_facebook_usersettingsfragment_profile_picture_height));
                connectedStateLabel.setCompoundDrawables(null, drawable, null, null);
                return;
            }
        } else
        {
            int i = getResources().getColor(com.facebook.android.R.color.com_facebook_usersettingsfragment_not_connected_text_color);
            connectedStateLabel.setTextColor(i);
            connectedStateLabel.setShadowLayer(0.0F, 0.0F, 0.0F, i);
            connectedStateLabel.setText(getResources().getString(com.facebook.android.R.string.com_facebook_usersettingsfragment_not_logged_in));
            connectedStateLabel.setCompoundDrawables(null, null, null, null);
            connectedStateLabel.setTag(null);
            return;
        }
    }

    public void clearPermissions()
    {
        loginButtonProperties.clearPermissions();
    }

    public SessionDefaultAudience getDefaultAudience()
    {
        return loginButtonProperties.getDefaultAudience();
    }

    public SessionLoginBehavior getLoginBehavior()
    {
        return loginButtonProperties.getLoginBehavior();
    }

    public LoginButton.OnErrorListener getOnErrorListener()
    {
        return loginButtonProperties.getOnErrorListener();
    }

    List getPermissions()
    {
        return loginButtonProperties.getPermissions();
    }

    public com.facebook.Session.StatusCallback getSessionStatusCallback()
    {
        return sessionStatusCallback;
    }

    public void onActivityCreated(Bundle bundle)
    {
        super.onActivityCreated(bundle);
    }

    public void onActivityResult(int i, int j, Intent intent)
    {
        super.onActivityResult(i, j, intent);
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setRetainInstance(true);
    }

    public View onCreateView(LayoutInflater layoutinflater, ViewGroup viewgroup, Bundle bundle)
    {
        View lView = layoutinflater.inflate(com.facebook.android.R.layout.com_facebook_usersettingsfragment, viewgroup, false);
        loginButton = (LoginButton)lView.findViewById(com.facebook.android.R.id.com_facebook_usersettingsfragment_login_button);
        loginButton.setProperties(loginButtonProperties);
        loginButton.setFragment(this);
        loginButton.setLoginLogoutEventName("fb_user_settings_vc_usage");
        Session session = getSession();
        if(session != null && !session.equals(Session.getActiveSession()))
            loginButton.setSession(session);
        connectedStateLabel = (TextView)lView.findViewById(com.facebook.android.R.id.com_facebook_usersettingsfragment_profile_name);
        if(lView.getBackground() == null)
        {
            lView.setBackgroundColor(getResources().getColor(com.facebook.android.R.color.com_facebook_blue));
            return lView;
        } else
        {
            lView.getBackground().setDither(true);
            return lView;
        }
    }

    public void onDestroy()
    {
        super.onDestroy();
    }

    public void onResume()
    {
        super.onResume();
        fetchUserInfo();
        updateUI();
    }

    protected void onSessionStateChange(SessionState sessionstate, Exception exception)
    {
        fetchUserInfo();
        updateUI();
        if(sessionStatusCallback != null)
            sessionStatusCallback.call(getSession(), sessionstate, exception);
    }

    public void setDefaultAudience(SessionDefaultAudience sessiondefaultaudience)
    {
        loginButtonProperties.setDefaultAudience(sessiondefaultaudience);
    }

    public void setLoginBehavior(SessionLoginBehavior sessionloginbehavior)
    {
        loginButtonProperties.setLoginBehavior(sessionloginbehavior);
    }

    public void setOnErrorListener(LoginButton.OnErrorListener onerrorlistener)
    {
        loginButtonProperties.setOnErrorListener(onerrorlistener);
    }

    public void setPublishPermissions(List list)
    {
        loginButtonProperties.setPublishPermissions(list, getSession());
    }

    public void setPublishPermissions(String as[])
    {
        loginButtonProperties.setPublishPermissions(Arrays.asList(as), getSession());
    }

    public void setReadPermissions(List list)
    {
        loginButtonProperties.setReadPermissions(list, getSession());
    }

    public void setReadPermissions(String as[])
    {
        loginButtonProperties.setReadPermissions(Arrays.asList(as), getSession());
    }

    public void setSession(Session session)
    {
        super.setSession(session);
        if(loginButton != null)
            loginButton.setSession(session);
        fetchUserInfo();
        updateUI();
    }

    public void setSessionStatusCallback(com.facebook.Session.StatusCallback statuscallback)
    {
        sessionStatusCallback = statuscallback;
    }

    private static final String FIELDS = "fields";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String PICTURE = "picture";
    private static final String REQUEST_FIELDS = TextUtils.join(",", new String[] {
        "id", "name", "picture"
    });
    private TextView connectedStateLabel;
    private LoginButton loginButton;
    private LoginButton.LoginButtonProperties loginButtonProperties;
    private com.facebook.Session.StatusCallback sessionStatusCallback;
    private GraphUser user;
    private Session userInfoSession;
    private Drawable userProfilePic;
    private String userProfilePicID;




/*
    static GraphUser access$002(UserSettingsFragment usersettingsfragment, GraphUser graphuser)
    {
        usersettingsfragment.user = graphuser;
        return graphuser;
    }

*/



}
