

package com.facebook;

import android.content.*;
import android.os.Bundle;
import com.facebook.internal.NativeProtocol;

public class FacebookBroadcastReceiver extends BroadcastReceiver
{

    public FacebookBroadcastReceiver()
    {
    }

    protected void onFailedAppCall(String s, String s1, Bundle bundle)
    {
    }

    public void onReceive(Context context, Intent intent)
    {
        String s;
        Bundle bundle;
        String temp = intent.getStringExtra("com.facebook.platform.protocol.CALL_ID");
        s = intent.getStringExtra("com.facebook.platform.protocol.PROTOCOL_ACTION");
        if(temp != null && s != null)
        {
            bundle = intent.getExtras();
            if(!NativeProtocol.isErrorResult(intent)) {
                onSuccessfulAppCall(temp, s, bundle);
                return;
            }
            onFailedAppCall(temp, s, bundle);
        }
    }

    protected void onSuccessfulAppCall(String s, String s1, Bundle bundle)
    {
    }
}
