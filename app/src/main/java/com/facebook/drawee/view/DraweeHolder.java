// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.drawee.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import com.facebook.common.activitylistener.ActivityListener;
import com.facebook.common.activitylistener.BaseActivityListener;
import com.facebook.common.internal.Objects;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.logging.FLog;
import com.facebook.drawee.components.DraweeEventTracker;
import com.facebook.drawee.drawable.VisibilityAwareDrawable;
import com.facebook.drawee.drawable.VisibilityCallback;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.interfaces.DraweeHierarchy;

public class DraweeHolder
    implements VisibilityCallback
{

    public DraweeHolder(DraweeHierarchy draweehierarchy)
    {
        mIsControllerAttached = false;
        mIsHolderAttached = false;
        mIsVisible = true;
        mIsActivityStarted = true;
        mController = null;
        if(draweehierarchy != null)
            setHierarchy(draweehierarchy);
    }

    private void attachController()
    {
        if(!mIsControllerAttached)
        {
            mEventTracker.recordEvent(com.facebook.drawee.components.DraweeEventTracker.Event.ON_ATTACH_CONTROLLER);
            mIsControllerAttached = true;
            if(mController != null && mController.getHierarchy() != null)
            {
                mController.onAttach();
                return;
            }
        }
    }

    private void attachOrDetachController()
    {
        if(mIsHolderAttached && mIsVisible && mIsActivityStarted)
        {
            attachController();
            return;
        } else
        {
            detachController();
            return;
        }
    }

    public static DraweeHolder create(DraweeHierarchy draweehierarchy, Context context)
    {
        DraweeHolder draweeHolder = new DraweeHolder(draweehierarchy);
        draweeHolder.registerWithContext(context);
        return draweeHolder;
    }

    private void detachController()
    {
        if(mIsControllerAttached)
        {
            mEventTracker.recordEvent(com.facebook.drawee.components.DraweeEventTracker.Event.ON_DETACH_CONTROLLER);
            mIsControllerAttached = false;
            if(mController != null)
            {
                mController.onDetach();
                return;
            }
        }
    }

    private void setActivityStarted(boolean flag)
    {
        DraweeEventTracker draweeeventtracker = mEventTracker;
        com.facebook.drawee.components.DraweeEventTracker.Event event;
        if(flag)
            event = com.facebook.drawee.components.DraweeEventTracker.Event.ON_ACTIVITY_START;
        else
            event = com.facebook.drawee.components.DraweeEventTracker.Event.ON_ACTIVITY_STOP;
        draweeeventtracker.recordEvent(event);
        mIsActivityStarted = flag;
        attachOrDetachController();
    }

    private void setVisibilityCallback(VisibilityCallback visibilitycallback)
    {
        Drawable drawable = getTopLevelDrawable();
        if(drawable instanceof VisibilityAwareDrawable)
            ((VisibilityAwareDrawable)drawable).setVisibilityCallback(visibilitycallback);
    }

    public DraweeController getController()
    {
        return mController;
    }

    public DraweeHierarchy getHierarchy()
    {
        return (DraweeHierarchy)Preconditions.checkNotNull(mHierarchy);
    }

    public Drawable getTopLevelDrawable()
    {
        if(mHierarchy == null)
            return null;
        else
            return mHierarchy.getTopLevelDrawable();
    }

    public boolean hasHierarchy()
    {
        return mHierarchy != null;
    }

    public void onAttach()
    {
        mEventTracker.recordEvent(com.facebook.drawee.components.DraweeEventTracker.Event.ON_HOLDER_ATTACH);
        mIsHolderAttached = true;
        attachOrDetachController();
    }

    public void onDetach()
    {
        mEventTracker.recordEvent(com.facebook.drawee.components.DraweeEventTracker.Event.ON_HOLDER_DETACH);
        mIsHolderAttached = false;
        attachOrDetachController();
    }

    public void onDraw()
    {
        if(mIsControllerAttached)
        {
            return;
        } else
        {
            FLog.wtf(DraweeEventTracker.class, "%x: Draw requested for a non-attached controller %x. %s", new Object[] {
                Integer.valueOf(System.identityHashCode(this)), Integer.valueOf(System.identityHashCode(mController)), toString()
            });
            mIsHolderAttached = true;
            mIsVisible = true;
            mIsActivityStarted = true;
            attachOrDetachController();
            return;
        }
    }

    public boolean onTouchEvent(MotionEvent motionevent)
    {
        if(mController == null)
            return false;
        else
            return mController.onTouchEvent(motionevent);
    }

    public void onVisibilityChange(boolean flag)
    {
        if(mIsVisible == flag)
            return;
        DraweeEventTracker draweeeventtracker = mEventTracker;
        com.facebook.drawee.components.DraweeEventTracker.Event event;
        if(flag)
            event = com.facebook.drawee.components.DraweeEventTracker.Event.ON_DRAWABLE_SHOW;
        else
            event = com.facebook.drawee.components.DraweeEventTracker.Event.ON_DRAWABLE_HIDE;
        draweeeventtracker.recordEvent(event);
        mIsVisible = flag;
        attachOrDetachController();
    }

    public void registerWithContext(Context context)
    {
    }

    public void setController(DraweeController draweecontroller)
    {
        boolean flag = mIsControllerAttached;
        if(flag)
            detachController();
        if(mController != null)
        {
            mEventTracker.recordEvent(com.facebook.drawee.components.DraweeEventTracker.Event.ON_CLEAR_OLD_CONTROLLER);
            mController.setHierarchy(null);
        }
        mController = draweecontroller;
        if(mController != null)
        {
            mEventTracker.recordEvent(com.facebook.drawee.components.DraweeEventTracker.Event.ON_SET_CONTROLLER);
            mController.setHierarchy(mHierarchy);
        } else
        {
            mEventTracker.recordEvent(com.facebook.drawee.components.DraweeEventTracker.Event.ON_CLEAR_CONTROLLER);
        }
        if(flag)
            attachController();
    }

    public void setHierarchy(DraweeHierarchy draweehierarchy)
    {
        mEventTracker.recordEvent(com.facebook.drawee.components.DraweeEventTracker.Event.ON_SET_HIERARCHY);
        setVisibilityCallback(null);
        mHierarchy = (DraweeHierarchy)Preconditions.checkNotNull(draweehierarchy);
        onVisibilityChange(mHierarchy.getTopLevelDrawable().isVisible());
        setVisibilityCallback(this);
        if(mController != null)
            mController.setHierarchy(draweehierarchy);
    }

    public String toString()
    {
        return Objects.toStringHelper(this).add("controllerAttached", mIsControllerAttached).add("holderAttached", mIsHolderAttached).add("drawableVisible", mIsVisible).add("activityStarted", mIsActivityStarted).add("events", mEventTracker.toString()).toString();
    }

    private final ActivityListener mActivityListener = new BaseActivityListener() {

        public void onStart(Activity activity)
        {
            setActivityStarted(true);
        }

        public void onStop(Activity activity)
        {
            setActivityStarted(false);
        }

    };
    private DraweeController mController;
    private final DraweeEventTracker mEventTracker = new DraweeEventTracker();
    private DraweeHierarchy mHierarchy;
    private boolean mIsActivityStarted;
    private boolean mIsControllerAttached;
    private boolean mIsHolderAttached;
    private boolean mIsVisible;

}
