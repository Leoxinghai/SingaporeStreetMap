// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.internal;

import android.content.*;
import android.os.*;

// Referenced classes of package com.facebook.internal:
//            NativeProtocol

public abstract class PlatformServiceClient
    implements ServiceConnection
{
    public static interface CompletedListener
    {

        public abstract void completed(Bundle bundle);
    }


    public PlatformServiceClient(Context context1, int i, int j, int k, String s)
    {
        Context context2 = context1.getApplicationContext();
        if(context2 != null)
            context1 = context2;
        context = context1;
        requestMessage = i;
        replyMessage = j;
        applicationId = s;
        protocolVersion = k;
    }

    private void callback(Bundle bundle)
    {
        if(running)
        {
            running = false;
            CompletedListener completedlistener = listener;
            if(completedlistener != null)
            {
                completedlistener.completed(bundle);
                return;
            }
        }
    }

    private void sendMessage()
    {
        Bundle bundle = new Bundle();
        bundle.putString("com.facebook.platform.extra.APPLICATION_ID", applicationId);
        populateRequestBundle(bundle);
        Message message = Message.obtain(null, requestMessage);
        message.arg1 = protocolVersion;
        message.setData(bundle);
        message.replyTo = new Messenger(handler);
        try
        {
            sender.send(message);
            return;
        }
        catch(RemoteException remoteexception)
        {
            callback(null);
        }
    }

    public void cancel()
    {
        running = false;
    }

    protected Context getContext()
    {
        return context;
    }

    protected void handleMessage(Message message)
    {
        if(message.what == replyMessage)
        {
            Bundle temp = message.getData();
            if(temp.getString("com.facebook.platform.status.ERROR_TYPE") != null)
                callback(null);
            else
                callback(temp);
            context.unbindService(this);
        }
    }

    public void onServiceConnected(ComponentName componentname, IBinder ibinder)
    {
        sender = new Messenger(ibinder);
        sendMessage();
    }

    public void onServiceDisconnected(ComponentName componentname)
    {
        sender = null;
        try
        {
            context.unbindService(this);
        }
        catch(Exception ex) { }
        callback(null);
    }

    protected abstract void populateRequestBundle(Bundle bundle);

    public void setCompletedListener(CompletedListener completedlistener)
    {
        listener = completedlistener;
    }

    public boolean start()
    {
        if(!running && NativeProtocol.getLatestAvailableProtocolVersionForService(context, protocolVersion) != -1)
        {
            android.content.Intent intent = NativeProtocol.createPlatformServiceIntent(context);
            if(intent != null)
            {
                running = true;
                context.bindService(intent, this, Context.BIND_AUTO_CREATE);
                return true;
            }
        }
        return false;
    }

    private final String applicationId;
    private final Context context;
    private final Handler handler = new Handler() {

        public void handleMessage(Message message)
        {
            PlatformServiceClient.this.handleMessage(message);
        }

    };
    private CompletedListener listener;
    private final int protocolVersion;
    private int replyMessage;
    private int requestMessage;
    private boolean running;
    private Messenger sender;
}
