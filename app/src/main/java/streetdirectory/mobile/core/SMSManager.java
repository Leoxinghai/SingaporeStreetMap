// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package streetdirectory.mobile.core;

import android.app.PendingIntent;
import android.content.*;
import android.telephony.SmsManager;

public class SMSManager
{

    public SMSManager(Context context)
    {
        mContext = context;
    }

    public static void send(Context context, String s, String s1)
    {
        PendingIntent temp = PendingIntent.getBroadcast(context, 0, new Intent("SMS_SENT"), 0);
        SmsManager.getDefault().sendTextMessage(s, null, s1, temp, null);
    }

    public void onFailed()
    {
    }

    public void onSuccess()
    {
    }

    public void send(String s, String s1)
    {
        PendingIntent pendingintent = PendingIntent.getBroadcast(mContext, 0, new Intent("SMS_SENT"), 0);
        mContext.registerReceiver(new BroadcastReceiver() {

            public void onReceive(Context context, Intent intent)
            {
                if(getResultCode() == -1)
                    onSuccess();
                else
                    onFailed();
                mContext.unregisterReceiver(this);
            }

        }, new IntentFilter("SMS_SENT"));
        SmsManager.getDefault().sendTextMessage(s, null, s1, pendingintent, null);
    }

    private Context mContext;

}
