// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.animated.base;

import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.view.animation.LinearInterpolator;
import com.facebook.common.logging.FLog;
import com.facebook.common.references.CloseableReference;
import com.facebook.common.time.MonotonicClock;
import com.facebook.drawable.base.DrawableWithCaches;
import com.nineoldandroids.animation.ValueAnimator;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// Referenced classes of package com.facebook.imagepipeline.animated.base:
//            AnimatableDrawable, AnimatedDrawableCachingBackend, AnimatedDrawableDiagnostics

public class AnimatedDrawable extends Drawable
    implements AnimatableDrawable, DrawableWithCaches
{

    public AnimatedDrawable(ScheduledExecutorService scheduledexecutorservice, AnimatedDrawableCachingBackend animateddrawablecachingbackend, AnimatedDrawableDiagnostics animateddrawablediagnostics, MonotonicClock monotonicclock)
    {
        mLastDrawnFrameNumber = -1;
        mLastDrawnFrameMonotonicNumber = -1;
        mLastInvalidateTimeMs = -1L;
        mSx = 1.0F;
        mSy = 1.0F;
        mNextFrameTaskMs = -1L;
        mScheduledExecutorServiceForUiThread = scheduledexecutorservice;
        mAnimatedDrawableBackend = animateddrawablecachingbackend;
        mAnimatedDrawableDiagnostics = animateddrawablediagnostics;
        mMonotonicClock = monotonicclock;
        mDurationMs = mAnimatedDrawableBackend.getDurationMs();
        mFrameCount = mAnimatedDrawableBackend.getFrameCount();
        mAnimatedDrawableDiagnostics.setBackend(mAnimatedDrawableBackend);
        mTotalLoops = mAnimatedDrawableBackend.getLoopCount();
        mTransparentPaint.setColor(0);
        mTransparentPaint.setStyle(android.graphics.Paint.Style.FILL);
        resetToPreviewFrame();
    }

    private void computeAndScheduleNextFrame(boolean flag)
    {
        if(mDurationMs != 0)
        {
            long l1 = mMonotonicClock.now();
            int j = (int)((l1 - mStartTimeMs) / (long)mDurationMs);
            if(mTotalLoops <= 0 || j < mTotalLoops)
            {
                int l = (int)((l1 - mStartTimeMs) % (long)mDurationMs);
                int i1 = mAnimatedDrawableBackend.getFrameForTimestampMs(l);
                boolean flag1;
                if(mScheduledFrameNumber != i1)
                    flag1 = true;
                else
                    flag1 = false;
                mScheduledFrameNumber = i1;
                mScheduledFrameMonotonicNumber = mFrameCount * j + i1;
                if(flag)
                {
                    if(flag1)
                    {
                        doInvalidateSelf();
                        return;
                    }
                    int i = (mAnimatedDrawableBackend.getTimestampMsForFrame(mScheduledFrameNumber) + mAnimatedDrawableBackend.getDurationMsForFrame(mScheduledFrameNumber)) - l;
                    int k = mScheduledFrameNumber;
                    l = mFrameCount;
                    l1 += i;
                    if(mNextFrameTaskMs == -1L || mNextFrameTaskMs > l1)
                    {
                        FLog.v(TAG, "(%s) Next frame (%d) in %d ms", mLogId, Integer.valueOf((k + 1) % l), Integer.valueOf(i));
                        unscheduleSelf(mNextFrameTask);
                        scheduleSelf(mNextFrameTask, l1);
                        mNextFrameTaskMs = l1;
                        return;
                    }
                }
            }
        }
    }

    private void doInvalidateSelf()
    {
        mWaitingForDraw = true;
        mLastInvalidateTimeMs = mMonotonicClock.now();
        invalidateSelf();
    }

    private void doWatchdogCheck()
    {
        mHaveWatchdogScheduled = false;
        if(!mIsRunning)
            return;
        long l = mMonotonicClock.now();
        boolean flag;
        boolean flag1;
        if(mWaitingForDraw && l - mLastInvalidateTimeMs > 1000L)
            flag = true;
        else
            flag = false;
        if(mNextFrameTaskMs != -1L && l - mNextFrameTaskMs > 1000L)
            flag1 = true;
        else
            flag1 = false;
        if(flag || flag1)
        {
            dropCaches();
            doInvalidateSelf();
            return;
        } else
        {
            mScheduledExecutorServiceForUiThread.schedule(mWatchdogTask, 2000L, TimeUnit.MILLISECONDS);
            mHaveWatchdogScheduled = true;
            return;
        }
    }

    private void onNextFrame()
    {
        for(mNextFrameTaskMs = -1L; !mIsRunning || mDurationMs == 0;)
            return;

        mAnimatedDrawableDiagnostics.onNextFrameMethodBegin();
        computeAndScheduleNextFrame(true);
        mAnimatedDrawableDiagnostics.onNextFrameMethodEnd();
        return;
    }

    private void onStart()
    {
        if(!mIsRunning)
            return;
        mAnimatedDrawableDiagnostics.onStartMethodBegin();
        mStartTimeMs = mMonotonicClock.now();
        mScheduledFrameNumber = 0;
        mScheduledFrameMonotonicNumber = 0;
        long l = mStartTimeMs + (long)mAnimatedDrawableBackend.getDurationMsForFrame(0);
        scheduleSelf(mNextFrameTask, l);
        mNextFrameTaskMs = l;
        doInvalidateSelf();
        mAnimatedDrawableDiagnostics.onStartMethodEnd();
        return;
    }

    private boolean renderFrame(Canvas canvas, int i, int j)
    {
        CloseableReference closeablereference = mAnimatedDrawableBackend.getBitmapForFrame(i);
        if(closeablereference != null)
        {
            canvas.drawBitmap((Bitmap)closeablereference.get(), 0.0F, 0.0F, mPaint);
            if(mLastDrawnFrame != null)
                mLastDrawnFrame.close();
            if(mIsRunning && j > mLastDrawnFrameMonotonicNumber)
            {
                int k = j - mLastDrawnFrameMonotonicNumber - 1;
                mAnimatedDrawableDiagnostics.incrementDrawnFrames(1);
                mAnimatedDrawableDiagnostics.incrementDroppedFrames(k);
                if(k > 0)
                    FLog.v(TAG, "(%s) Dropped %d frames", mLogId, Integer.valueOf(k));
            }
            mLastDrawnFrame = closeablereference;
            mLastDrawnFrameNumber = i;
            mLastDrawnFrameMonotonicNumber = j;
            FLog.v(TAG, "(%s) Drew frame %d", mLogId, Integer.valueOf(i));
            return true;
        } else
        {
            return false;
        }
    }

    private void resetToPreviewFrame()
    {
        mScheduledFrameNumber = mAnimatedDrawableBackend.getFrameForPreview();
        mScheduledFrameMonotonicNumber = mScheduledFrameNumber;
        mPendingRenderedFrameNumber = -1;
        mPendingRenderedFrameMonotonicNumber = -1;
    }

    private void scheduleInvalidatePoll()
    {
        if(mInvalidateTaskScheduled)
        {
            return;
        } else
        {
            mInvalidateTaskScheduled = true;
            scheduleSelf(mInvalidateTask, 5L);
            return;
        }
    }

    public com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener createAnimatorUpdateListener()
    {
        return new com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener() {

            public void onAnimationUpdate(ValueAnimator valueanimator)
            {
                setLevel(((Integer)valueanimator.getAnimatedValue()).intValue());
            }

        };
    }

    public ValueAnimator createValueAnimator()
    {
        int i = mAnimatedDrawableBackend.getLoopCount();
        ValueAnimator valueanimator = new ValueAnimator();
        valueanimator.setIntValues(new int[] {
            0, mDurationMs
        });
        valueanimator.setDuration(mDurationMs);
        if(i == 0)
            i = -1;
        valueanimator.setRepeatCount(i);
        valueanimator.setRepeatMode(1);
        valueanimator.setInterpolator(new LinearInterpolator());
        valueanimator.addUpdateListener(createAnimatorUpdateListener());
        return valueanimator;
    }

    public ValueAnimator createValueAnimator(int i)
    {
        ValueAnimator valueanimator = createValueAnimator();
        valueanimator.setRepeatCount(Math.max(i / mAnimatedDrawableBackend.getDurationMs(), 1));
        return valueanimator;
    }

    public boolean didLastDrawRender()
    {
        return mLastDrawnFrame != null;
    }

    public void draw(Canvas canvas)
    {
        mAnimatedDrawableDiagnostics.onDrawMethodBegin();
        boolean flag2;
        mWaitingForDraw = false;
        if(mIsRunning && !mHaveWatchdogScheduled)
        {
            mScheduledExecutorServiceForUiThread.schedule(mWatchdogTask, 2000L, TimeUnit.MILLISECONDS);
            mHaveWatchdogScheduled = true;
        }
        if(mApplyTransformation)
        {
            mDstRect.set(getBounds());
            if(!mDstRect.isEmpty())
            {
                AnimatedDrawableCachingBackend animateddrawablecachingbackend = mAnimatedDrawableBackend.forNewBounds(mDstRect);
                if(animateddrawablecachingbackend != mAnimatedDrawableBackend)
                {
                    mAnimatedDrawableBackend.dropCaches();
                    mAnimatedDrawableBackend = animateddrawablecachingbackend;
                    mAnimatedDrawableDiagnostics.setBackend(animateddrawablecachingbackend);
                }
                mSx = (float)mDstRect.width() / (float)mAnimatedDrawableBackend.getRenderedWidth();
                mSy = (float)mDstRect.height() / (float)mAnimatedDrawableBackend.getRenderedHeight();
                mApplyTransformation = false;
            }
        }
        flag2 = mDstRect.isEmpty();
        if(flag2)
        {
            mAnimatedDrawableDiagnostics.onDrawMethodEnd();
            return;
        }
        canvas.save();
        canvas.scale(mSx, mSy);
        boolean flag1 = false;
        if(mPendingRenderedFrameNumber != -1) {
            flag2 = renderFrame(canvas, mPendingRenderedFrameNumber, mPendingRenderedFrameMonotonicNumber);
            flag1 = false | flag2;
            if (!flag2) {
                FLog.v(TAG, "(%s) Trying again later for pending %d", mLogId, Integer.valueOf(mPendingRenderedFrameNumber));
                scheduleInvalidatePoll();
            } else {
                FLog.v(TAG, "(%s) Rendered pending frame %d", mLogId, Integer.valueOf(mPendingRenderedFrameNumber));
                mPendingRenderedFrameNumber = -1;
                mPendingRenderedFrameMonotonicNumber = -1;
            }
        }

        boolean flag = flag1;
        if(mPendingRenderedFrameNumber == -1) {
            if (mIsRunning)
                computeAndScheduleNextFrame(false);
            flag2 = renderFrame(canvas, mScheduledFrameNumber, mScheduledFrameMonotonicNumber);
            flag1 |= flag2;
            if (!flag2) {
                FLog.v(TAG, "(%s) Trying again later for current %d", mLogId, Integer.valueOf(mScheduledFrameNumber));
                mPendingRenderedFrameNumber = mScheduledFrameNumber;
                mPendingRenderedFrameMonotonicNumber = mScheduledFrameMonotonicNumber;
                scheduleInvalidatePoll();
                flag = flag1;
            } else {
                FLog.v(TAG, "(%s) Rendered current frame %d", mLogId, Integer.valueOf(mScheduledFrameNumber));
                flag = flag1;
                if (mIsRunning) {
                    computeAndScheduleNextFrame(true);
                    flag = flag1;
                }
            }
        }

        flag1 = flag;
        if(!flag && mLastDrawnFrame != null) {
            flag1 = flag;
            canvas.drawBitmap((Bitmap) mLastDrawnFrame.get(), 0.0F, 0.0F, mPaint);
            flag1 = true;
            FLog.v(TAG, "(%s) Rendered last known frame %d", mLogId, Integer.valueOf(mLastDrawnFrameNumber));
            flag = flag1;
        }

        CloseableReference closeablereference = mAnimatedDrawableBackend.getPreviewBitmap();
        if(!flag1 && closeablereference == null) {
            flag = flag1;

            canvas.drawBitmap((Bitmap) closeablereference.get(), 0.0F, 0.0F, mPaint);
            closeablereference.close();
            FLog.v(TAG, "(%s) Rendered preview frame", mLogId);
            flag = true;
        }
        if(!flag) {
            canvas.drawRect(0.0F, 0.0F, mDstRect.width(), mDstRect.height(), mTransparentPaint);
            FLog.v(TAG, "(%s) Failed to draw a frame", mLogId);
            canvas.restore();
            mAnimatedDrawableDiagnostics.drawDebugOverlay(canvas, mDstRect);
        }
        mAnimatedDrawableDiagnostics.onDrawMethodEnd();
        return;

    }

    public void dropCaches()
    {
        FLog.v(TAG, "(%s) Dropping caches", mLogId);
        if(mLastDrawnFrame != null)
        {
            mLastDrawnFrame.close();
            mLastDrawnFrame = null;
            mLastDrawnFrameNumber = -1;
            mLastDrawnFrameMonotonicNumber = -1;
        }
        mAnimatedDrawableBackend.dropCaches();
    }

    protected void finalize()
        throws Throwable
    {
        super.finalize();
        if(mLastDrawnFrame != null)
        {
            mLastDrawnFrame.close();
            mLastDrawnFrame = null;
        }
    }

    public int getIntrinsicHeight()
    {
        return mAnimatedDrawableBackend.getHeight();
    }

    public int getIntrinsicWidth()
    {
        return mAnimatedDrawableBackend.getWidth();
    }

    public int getOpacity()
    {
        return -3;
    }

    int getScheduledFrameNumber()
    {
        return mScheduledFrameNumber;
    }

    public boolean isRunning()
    {
        return mIsRunning;
    }

    boolean isWaitingForDraw()
    {
        return mWaitingForDraw;
    }

    boolean isWaitingForNextFrame()
    {
        return mNextFrameTaskMs != -1L;
    }

    protected void onBoundsChange(Rect rect)
    {
        super.onBoundsChange(rect);
        mApplyTransformation = true;
        if(mLastDrawnFrame != null)
        {
            mLastDrawnFrame.close();
            mLastDrawnFrame = null;
        }
        mLastDrawnFrameNumber = -1;
        mLastDrawnFrameMonotonicNumber = -1;
        mAnimatedDrawableBackend.dropCaches();
    }

    protected boolean onLevelChange(int i)
    {
        if(!mIsRunning)
            if((i = mAnimatedDrawableBackend.getFrameForTimestampMs(i)) != mScheduledFrameNumber)
            {
                try
                {
                    mScheduledFrameNumber = i;
                    mScheduledFrameMonotonicNumber = i;
                    doInvalidateSelf();
                }
                catch(IllegalStateException illegalstateexception)
                {
                    return false;
                }
                return true;
            }
        return false;
    }

    public void setAlpha(int i)
    {
        mPaint.setAlpha(i);
        doInvalidateSelf();
    }

    public void setColorFilter(ColorFilter colorfilter)
    {
        mPaint.setColorFilter(colorfilter);
        doInvalidateSelf();
    }

    public void setLogId(String s)
    {
        mLogId = s;
    }

    public void start()
    {
        if(mDurationMs == 0 || mFrameCount <= 1)
        {
            return;
        } else
        {
            mIsRunning = true;
            scheduleSelf(mStartTask, mMonotonicClock.now());
            return;
        }
    }

    public void stop()
    {
        mIsRunning = false;
    }

    private static final int NO_FRAME = -1;
    private static final int POLL_FOR_RENDERED_FRAME_MS = 5;
    private static final Class TAG = AnimatedDrawable.class;
    private static final long WATCH_DOG_TIMER_MIN_TIMEOUT_MS = 1000L;
    private static final long WATCH_DOG_TIMER_POLL_INTERVAL_MS = 2000L;
    private AnimatedDrawableCachingBackend mAnimatedDrawableBackend;
    private final AnimatedDrawableDiagnostics mAnimatedDrawableDiagnostics;
    private boolean mApplyTransformation;
    private final Rect mDstRect = new Rect();
    private final int mDurationMs;
    private final int mFrameCount;
    private boolean mHaveWatchdogScheduled;
    private final Runnable mInvalidateTask = new Runnable() {

        public void run()
        {
            FLog.v(AnimatedDrawable.TAG, "(%s) Invalidate Task", mLogId);
            mInvalidateTaskScheduled = false;
            doInvalidateSelf();
        }

    };
    private boolean mInvalidateTaskScheduled;
    private boolean mIsRunning;
    private CloseableReference mLastDrawnFrame;
    private int mLastDrawnFrameMonotonicNumber;
    private int mLastDrawnFrameNumber;
    private long mLastInvalidateTimeMs;
    private String mLogId;
    private final MonotonicClock mMonotonicClock;
    private final Runnable mNextFrameTask = new Runnable() {

        public void run()
        {
            FLog.v(AnimatedDrawable.TAG, "(%s) Next Frame Task", mLogId);
            onNextFrame();
        }

    };

    private long mNextFrameTaskMs;
    private final Paint mPaint = new Paint(6);
    private int mPendingRenderedFrameMonotonicNumber;
    private int mPendingRenderedFrameNumber;
    private final ScheduledExecutorService mScheduledExecutorServiceForUiThread;
    private int mScheduledFrameMonotonicNumber;
    private int mScheduledFrameNumber;
    private final Runnable mStartTask = new Runnable() {

        public void run()
        {
            onStart();
        }

    };
    private long mStartTimeMs;
    private float mSx;
    private float mSy;
    private final int mTotalLoops;
    private final Paint mTransparentPaint = new Paint();
    private boolean mWaitingForDraw;
    private final Runnable mWatchdogTask = new Runnable() {

        public void run()
        {
            FLog.v(AnimatedDrawable.TAG, "(%s) Watchdog Task", mLogId);
            doWatchdogCheck();
        }

    };







/*
    static boolean access$402(AnimatedDrawable animateddrawable, boolean flag)
    {
        animateddrawable.mInvalidateTaskScheduled = flag;
        return flag;
    }

*/


}
