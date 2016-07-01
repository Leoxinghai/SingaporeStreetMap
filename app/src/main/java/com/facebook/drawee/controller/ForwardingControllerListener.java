// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.drawee.controller;

import android.graphics.drawable.Animatable;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.facebook.drawee.controller:
//            ControllerListener

public class ForwardingControllerListener
    implements ControllerListener
{

    public ForwardingControllerListener()
    {
    }

    public static ForwardingControllerListener create()
    {
        return new ForwardingControllerListener();
    }

    public static ForwardingControllerListener of(ControllerListener controllerlistener)
    {
        ForwardingControllerListener forwardingcontrollerlistener = create();
        forwardingcontrollerlistener.addListener(controllerlistener);
        return forwardingcontrollerlistener;
    }

    public static ForwardingControllerListener of(ControllerListener controllerlistener, ControllerListener controllerlistener1)
    {
        ForwardingControllerListener forwardingcontrollerlistener = create();
        forwardingcontrollerlistener.addListener(controllerlistener);
        forwardingcontrollerlistener.addListener(controllerlistener1);
        return forwardingcontrollerlistener;
    }

    private void onException(String s, Throwable throwable)
    {
        Log.e("FdingControllerListener", s, throwable);
        return;
    }

    public void addListener(ControllerListener controllerlistener)
    {
        mListeners.add(controllerlistener);
        return;
    }

    public void clearListeners()
    {
        mListeners.clear();
        return;
    }

    public void onFailure(String s, Throwable throwable)
    {
        int j = mListeners.size();
        int i = 0;

        for(;i < j;) {
            ControllerListener controllerlistener = (ControllerListener) mListeners.get(i);
            controllerlistener.onFailure(s, throwable);
            i++;
        }
        //onException("InternalListener exception in onFailure", exception);
    }

    public void onFinalImageSet(String s, Object obj, Animatable animatable)
    {
        int j = mListeners.size();
        int i = 0;
        for(;i < j;) {
            ControllerListener controllerlistener = (ControllerListener) mListeners.get(i);
            controllerlistener.onFinalImageSet(s, obj, animatable);
            i++;
        }

//        onException("InternalListener exception in onFinalImageSet", exception);
    }

    public void onIntermediateImageFailed(String s, Throwable throwable)
    {
        int j = mListeners.size();
        int i = 0;
        while(i < j)
        {
            ControllerListener controllerlistener = (ControllerListener)mListeners.get(i);
            try
            {
                controllerlistener.onIntermediateImageFailed(s, throwable);
            }
            catch(Exception exception)
            {
                onException("InternalListener exception in onIntermediateImageFailed", exception);
            }
            i++;
        }
    }

    public void onIntermediateImageSet(String s, Object obj)
    {
        int j = mListeners.size();
        int i = 0;
        while(i < j)
        {
            ControllerListener controllerlistener = (ControllerListener)mListeners.get(i);
            try
            {
                controllerlistener.onIntermediateImageSet(s, obj);
            }
            catch(Exception exception)
            {
                onException("InternalListener exception in onIntermediateImageSet", exception);
            }
            i++;
        }
    }

    public void onRelease(String s)
    {
        int j = mListeners.size();
        int i = 0;
        for(;i < j;) {
            ControllerListener controllerlistener = (ControllerListener) mListeners.get(i);
            controllerlistener.onRelease(s);
            i++;
        }
//        onException("InternalListener exception in onRelease", exception);
    }

    public void onSubmit(String s, Object obj)
    {
        int j = mListeners.size();
        int i = 0;

        for(;i < j;) {
            ControllerListener controllerlistener = (ControllerListener) mListeners.get(i);
            controllerlistener.onSubmit(s, obj);
            i++;
        }
//        onException("InternalListener exception in onSubmit", exception);
    }

    public void removeListener(ControllerListener controllerlistener)
    {
        mListeners.remove(controllerlistener);
        return;
    }

    private static final String TAG = "FdingControllerListener";
    private final List mListeners = new ArrayList(2);
}
