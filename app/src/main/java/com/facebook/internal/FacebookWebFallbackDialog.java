// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.internal;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.*;
import android.webkit.WebView;
import com.facebook.FacebookException;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.WebDialog;
import org.json.JSONException;
import org.json.JSONObject;

// Referenced classes of package com.facebook.internal:
//            Utility, BundleJSONConverter, NativeProtocol

public class FacebookWebFallbackDialog extends WebDialog
{

    private FacebookWebFallbackDialog(Context context, String s, String s1)
    {
        super(context, s);
        setExpectedRedirectUrl(s1);
    }

    public static boolean presentWebFallback(final Context context, String s, String s1, final com.facebook.widget.FacebookDialog.PendingCall pendingcall, final com.facebook.widget.FacebookDialog.Callback callback)
    {
        //final com.facebook.widget.FacebookDialog.PendingCall appCall = context;
        //final com.facebook.widget.FacebookDialog.Callback callback = pendingcall;;
        //final Context callback = callback;

        if(Utility.isNullOrEmpty(s))
        {
            return false;
        } else
        {
            FacebookWebFallbackDialog temp = new FacebookWebFallbackDialog(context, s, String.format("fb%s://bridge/", new Object[] {
                s1
            }));
            temp.setOnCompleteListener(new com.facebook.widget.WebDialog.OnCompleteListener() {

                public void onComplete(Bundle bundle, FacebookException facebookexception)
                {
                    Intent intent = new Intent();
                    Bundle temp = bundle;
                    if(bundle == null)
                        temp = new Bundle();
                    intent.putExtras(temp);
                    FacebookDialog.handleActivityResult(context, pendingcall, pendingcall.getRequestCode(), intent, callback);
                }

            });
            temp.show();
            return true;
        }
    }

    public void dismiss()
    {
        WebView webview = getWebView();
        if(isListenerCalled() || webview == null || !webview.isShown())
            super.dismiss();
        else
        if(!waitingForDialogToClose)
        {
            waitingForDialogToClose = true;
            webview.loadUrl((new StringBuilder()).append("javascript:").append("(function() {  var event = document.createEvent('Event');  event.initEvent('fbPlatformDialogMustClose',true,true);  document.dispatchEvent(event);})();").toString());
            (new Handler(Looper.getMainLooper())).postDelayed(new Runnable() {

                public void run()
                {
                    if(!isListenerCalled())
                        sendCancelToListener();
                }

            }, 1500L);
            return;
        }
    }

    protected Bundle parseResponseUri(String s)
    {
        Bundle bundle = Utility.parseUrlQueryString(Uri.parse(s).getQuery());
        s = bundle.getString("bridge_args");
        bundle.remove("bridge_args");
        String s1;
        if(!Utility.isNullOrEmpty(s))
            try
            {
                bundle.putBundle("com.facebook.platform.protocol.BRIDGE_ARGS", BundleJSONConverter.convertToBundle(new JSONObject(s)));
            }
            // Misplaced declaration of an exception variable
            catch(Exception ex)
            {
                Utility.logd(TAG, "Unable to parse bridge_args JSON", ex);
            }
        s1 = bundle.getString("method_results");
        bundle.remove("method_results");
        if(!Utility.isNullOrEmpty(s1))
        {
            s = s1;
            if(Utility.isNullOrEmpty(s1))
                s = "{}";
            try
            {
                bundle.putBundle("com.facebook.platform.protocol.RESULT_ARGS", BundleJSONConverter.convertToBundle(new JSONObject(s)));
            }
            catch(Exception ex)
            {
                Utility.logd(TAG, "Unable to parse bridge_args JSON", ex);
            }
        }
        bundle.remove("version");
        bundle.putInt("com.facebook.platform.protocol.PROTOCOL_VERSION", NativeProtocol.getLatestKnownVersion());
        return bundle;
    }

    private static final int OS_BACK_BUTTON_RESPONSE_TIMEOUT_MILLISECONDS = 1500;
    private static final String TAG = FacebookWebFallbackDialog.class.getName();
    private boolean waitingForDialogToClose;



}
