// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

// Referenced classes of package com.facebook:
//            AuthorizationClient

public class LoginActivity extends Activity
{

    public LoginActivity()
    {
    }

    private void onAuthClientCompleted(AuthorizationClient.Result result)
    {
        request = null;
        Bundle bundle;
        int i;
        if(result.code == AuthorizationClient.Result.Code.CANCEL)
            i = 0;
        else
            i = -1;
        bundle = new Bundle();
        bundle.putSerializable("com.facebook.LoginActivity:Result", result);
        Intent temp = new Intent();
        temp.putExtras(bundle);
        setResult(i, temp);
        finish();
    }

    static Bundle populateIntentExtras(AuthorizationClient.AuthorizationRequest authorizationrequest)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable("request", authorizationrequest);
        return bundle;
    }

    protected void onActivityResult(int i, int j, Intent intent)
    {
        authorizationClient.onActivityResult(i, j, intent);
    }

    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(com.facebook.android.R.layout.com_facebook_login_activity_layout);
        if(bundle != null)
        {
            callingPackage = bundle.getString("callingPackage");
            authorizationClient = (AuthorizationClient)bundle.getSerializable("authorizationClient");
        } else
        {
            callingPackage = getCallingPackage();
            authorizationClient = new AuthorizationClient();
            request = (AuthorizationClient.AuthorizationRequest)getIntent().getSerializableExtra("request");
        }
        authorizationClient.setContext(this);
        authorizationClient.setOnCompletedListener(new AuthorizationClient.OnCompletedListener() {

            public void onCompleted(AuthorizationClient.Result result)
            {
                onAuthClientCompleted(result);
            }

        });
        authorizationClient.setBackgroundProcessingListener(new AuthorizationClient.BackgroundProcessingListener() {

            public void onBackgroundProcessingStarted()
            {
                findViewById(com.facebook.android.R.id.com_facebook_login_activity_progress_bar).setVisibility(View.VISIBLE); //0
            }

            public void onBackgroundProcessingStopped()
            {
                findViewById(com.facebook.android.R.id.com_facebook_login_activity_progress_bar).setVisibility(View.INVISIBLE); //8
            }

        }
);
    }

    public void onPause()
    {
        super.onPause();
        authorizationClient.cancelCurrentHandler();
        findViewById(com.facebook.android.R.id.com_facebook_login_activity_progress_bar).setVisibility(View.INVISIBLE);
    }

    public void onResume()
    {
        super.onResume();
        if(callingPackage == null)
        {
            Log.e(TAG, "Cannot call LoginActivity with a null calling package. This can occur if the launchMode of the caller is singleInstance.");
            finish();
            return;
        } else
        {
            authorizationClient.startOrContinueAuth(request);
            return;
        }
    }

    public void onSaveInstanceState(Bundle bundle)
    {
        super.onSaveInstanceState(bundle);
        bundle.putString("callingPackage", callingPackage);
        bundle.putSerializable("authorizationClient", authorizationClient);
    }

    private static final String EXTRA_REQUEST = "request";
    private static final String NULL_CALLING_PKG_ERROR_MSG = "Cannot call LoginActivity with a null calling package. This can occur if the launchMode of the caller is singleInstance.";
    static final String RESULT_KEY = "com.facebook.LoginActivity:Result";
    private static final String SAVED_AUTH_CLIENT = "authorizationClient";
    private static final String SAVED_CALLING_PKG_KEY = "callingPackage";
    private static final String TAG = LoginActivity.class.getName();
    private AuthorizationClient authorizationClient;
    private String callingPackage;
    private AuthorizationClient.AuthorizationRequest request;


}
