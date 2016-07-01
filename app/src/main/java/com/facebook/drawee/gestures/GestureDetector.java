// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.drawee.gestures;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

public class GestureDetector
{
    public static interface ClickListener
    {

        public abstract boolean onClick();
    }


    public GestureDetector(Context context)
    {
        mSingleTapSlopPx = ViewConfiguration.get(context).getScaledTouchSlop();
        init();
    }

    public static GestureDetector newInstance(Context context)
    {
        return new GestureDetector(context);
    }

    public void init()
    {
        mClickListener = null;
        reset();
    }

    public boolean isCapturingGesture()
    {
        return mIsCapturingGesture;
    }

    public boolean onTouchEvent(MotionEvent motionevent)
    {
        switch(motionevent.getAction()) {
            default:
                return true;
            case 0:
                mIsCapturingGesture = true;
                mIsClickCandidate = true;
                mActionDownTime = motionevent.getEventTime();
                mActionDownX = motionevent.getX();
                mActionDownY = motionevent.getY();
                return true;
            case 2:
                if (Math.abs(motionevent.getX() - mActionDownX) > mSingleTapSlopPx || Math.abs(motionevent.getY() - mActionDownY) > mSingleTapSlopPx) {
                    mIsClickCandidate = false;
                    return true;
                }
            return true;
            case 3:
                mIsCapturingGesture = false;
                mIsClickCandidate = false;
                return true;
            case 1:
                mIsCapturingGesture = false;
                if (Math.abs(motionevent.getX() - mActionDownX) > mSingleTapSlopPx || Math.abs(motionevent.getY() - mActionDownY) > mSingleTapSlopPx)
                    mIsClickCandidate = false;
                if (mIsClickCandidate && motionevent.getEventTime() - mActionDownTime <= (long) ViewConfiguration.getLongPressTimeout() && mClickListener != null)
                    mClickListener.onClick();
                mIsClickCandidate = false;
                return true;
        }
    }

    public void reset()
    {
        mIsCapturingGesture = false;
        mIsClickCandidate = false;
    }

    public void setClickListener(ClickListener clicklistener)
    {
        mClickListener = clicklistener;
    }

    long mActionDownTime;
    float mActionDownX;
    float mActionDownY;
    ClickListener mClickListener;
    boolean mIsCapturingGesture;
    boolean mIsClickCandidate;
    final float mSingleTapSlopPx;
}
