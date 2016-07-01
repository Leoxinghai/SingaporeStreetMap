

package com.facebook.drawee.controller;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import com.facebook.common.internal.Objects;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.logging.FLog;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.components.*;
import com.facebook.drawee.gestures.GestureDetector;
import com.facebook.drawee.interfaces.*;
import java.util.concurrent.Executor;

// Referenced classes of package com.facebook.drawee.controller:
//            ControllerListener, BaseControllerListener, ForwardingControllerListener

public abstract class AbstractDraweeController
    implements DraweeController, com.facebook.drawee.components.DeferredReleaser.Releasable, com.facebook.drawee.gestures.GestureDetector.ClickListener
{
    private static class InternalForwardingListener extends ForwardingControllerListener
    {

        public static InternalForwardingListener createInternal(ControllerListener controllerlistener, ControllerListener controllerlistener1)
        {
            InternalForwardingListener internalforwardinglistener = new InternalForwardingListener();
            internalforwardinglistener.addListener(controllerlistener);
            internalforwardinglistener.addListener(controllerlistener1);
            return internalforwardinglistener;
        }

        private InternalForwardingListener()
        {
        }
    }


    public AbstractDraweeController(DeferredReleaser deferredreleaser, Executor executor, String s, Object obj)
    {
        mDeferredReleaser = deferredreleaser;
        mUiThreadImmediateExecutor = executor;
        init(s, obj);
    }

    private void init(String s, Object obj)
    {
        mEventTracker.recordEvent(com.facebook.drawee.components.DraweeEventTracker.Event.ON_INIT_CONTROLLER);
        if(mDeferredReleaser != null)
            mDeferredReleaser.cancelDeferredRelease(this);
        mIsAttached = false;
        releaseFetch();
        mRetainImageOnFailure = false;
        if(mRetryManager != null)
            mRetryManager.init();
        if(mGestureDetector != null)
        {
            mGestureDetector.init();
            mGestureDetector.setClickListener(this);
        }
        if(mControllerListener instanceof InternalForwardingListener)
            ((InternalForwardingListener)mControllerListener).clearListeners();
        else
            mControllerListener = null;
        if(mSettableDraweeHierarchy != null)
        {
            mSettableDraweeHierarchy.reset();
            mSettableDraweeHierarchy.setControllerOverlay(null);
            mSettableDraweeHierarchy = null;
        }
        mControllerOverlay = null;
        if(FLog.isLoggable(2))
            FLog.v(TAG, "controller %x %s -> %s: initialize", Integer.valueOf(System.identityHashCode(this)), mId, s);
        mId = s;
        mCallerContext = obj;
    }

    private boolean isExpectedDataSource(String s, DataSource datasource)
    {
        return s.equals(mId) && datasource == mDataSource && mIsRequestSubmitted;
    }

    private void logMessageAndFailure(String s, Throwable throwable)
    {
        if(FLog.isLoggable(2))
            FLog.v(TAG, "controller %x %s: %s: failure: %s", Integer.valueOf(System.identityHashCode(this)), mId, s, throwable);
    }

    private void logMessageAndImage(String s, Object obj)
    {
        if(FLog.isLoggable(2))
            FLog.v(TAG, "controller %x %s: %s: image: %s %x", new Object[] {
                Integer.valueOf(System.identityHashCode(this)), mId, s, getImageClass(obj), Integer.valueOf(getImageHash(obj))
            });
    }

    private void onFailureInternal(String s, DataSource datasource, Throwable throwable, boolean flag)
    {
        if(!isExpectedDataSource(s, datasource))
        {
            logMessageAndFailure("ignore_old_datasource @ onFailure", throwable);
            datasource.close();
            return;
        }
        //datasource = mEventTracker;
        DraweeEventTracker.Event tevent;
        if(flag)
            tevent = com.facebook.drawee.components.DraweeEventTracker.Event.ON_DATASOURCE_FAILURE;
        else
            tevent = com.facebook.drawee.components.DraweeEventTracker.Event.ON_DATASOURCE_FAILURE_INT;
        mEventTracker.recordEvent(tevent);
        if(flag)
        {
            logMessageAndFailure("final_failed @ onFailure", throwable);
            mDataSource = null;
            mHasFetchFailed = true;
            if(mRetainImageOnFailure && mDrawable != null)
                mSettableDraweeHierarchy.setImage(mDrawable, 1.0F, true);
            else
            if(shouldRetryOnTap())
                mSettableDraweeHierarchy.setRetry(throwable);
            else
                mSettableDraweeHierarchy.setFailure(throwable);
            getControllerListener().onFailure(mId, throwable);
            return;
        } else
        {
            logMessageAndFailure("intermediate_failed @ onFailure", throwable);
            getControllerListener().onIntermediateImageFailed(mId, throwable);
            return;
        }
    }

    private void onNewResultInternal(String s, DataSource datasource, Object obj, float f, boolean flag, boolean flag1)
    {
        if(!isExpectedDataSource(s, datasource)) {
            logMessageAndImage("ignore_old_datasource @ onNewResult", obj);
            releaseImage(obj);
            datasource.close();
            _L4:
            return;
        }

        Object obj1;
        Object obj2;
        obj2 = mEventTracker;
        if(flag)
            obj1 = com.facebook.drawee.components.DraweeEventTracker.Event.ON_DATASOURCE_RESULT;
        else
            obj1 = com.facebook.drawee.components.DraweeEventTracker.Event.ON_DATASOURCE_RESULT_INT;
        ((DraweeEventTracker) (obj2)).recordEvent(((com.facebook.drawee.components.DraweeEventTracker.Event) (obj1)));
        try
        {
            obj1 = createDrawable(obj);
            datasource = ((DataSource) (mFetchedImage));
            obj2 = mDrawable;
            mFetchedImage = obj;
            mDrawable = ((Drawable) (obj1));
            if(!flag) {
                logMessageAndImage("set_intermediate_result @ onNewResult", obj);
                mSettableDraweeHierarchy.setImage(((Drawable) (obj1)), f, flag1);
                getControllerListener().onIntermediateImageSet(s, getImageInfo(obj));
            } else {
                logMessageAndImage("set_final_result @ onNewResult", obj);
                mDataSource = null;
                mSettableDraweeHierarchy.setImage(((Drawable) (obj1)), 1.0F, flag1);
                getControllerListener().onFinalImageSet(s, getImageInfo(obj), getAnimatable());
            }

            if(obj2 != null && obj2 != obj1)
                releaseDrawable(((Drawable) (obj2)));
            if(datasource == null || datasource == obj)
                return;

            logMessageAndImage("release_previous_result @ onNewResult", datasource);
            releaseImage(datasource);
            return;

        }
        // Misplaced declaration of an exception variable
        catch(Exception ex)
        {
            logMessageAndImage("drawable_failed @ onNewResult", obj);
            releaseImage(obj);
            onFailureInternal(s, datasource, ((Throwable) (ex)), flag);
            return;
        }
        /*
        if(obj2 != null && obj2 != obj1)
            releaseDrawable(((Drawable) (obj2)));
        if(datasource != null && datasource != obj)
        {
            logMessageAndImage("release_previous_result @ onNewResult", datasource);
            releaseImage(datasource);
        }
        throw s;
        */
    }

    private void onProgressUpdateInternal(String s, DataSource datasource, float f, boolean flag)
    {
        if(!isExpectedDataSource(s, datasource))
        {
            logMessageAndFailure("ignore_old_datasource @ onProgress", null);
            datasource.close();
        } else
        if(!flag)
        {
            mSettableDraweeHierarchy.setProgress(f, false);
            return;
        }
    }

    private void releaseFetch()
    {
        boolean flag = mIsRequestSubmitted;
        mIsRequestSubmitted = false;
        mHasFetchFailed = false;
        if(mDataSource != null)
        {
            mDataSource.close();
            mDataSource = null;
        }
        if(mDrawable != null)
            releaseDrawable(mDrawable);
        mDrawable = null;
        if(mFetchedImage != null)
        {
            logMessageAndImage("release", mFetchedImage);
            releaseImage(mFetchedImage);
            mFetchedImage = null;
        }
        if(flag)
            getControllerListener().onRelease(mId);
    }

    private boolean shouldRetryOnTap()
    {
        return mHasFetchFailed && mRetryManager != null && mRetryManager.shouldRetryOnTap();
    }

    public void addControllerListener(ControllerListener controllerlistener)
    {
        Preconditions.checkNotNull(controllerlistener);
        if(mControllerListener instanceof InternalForwardingListener)
        {
            ((InternalForwardingListener)mControllerListener).addListener(controllerlistener);
            return;
        }
        if(mControllerListener != null)
        {
            mControllerListener = InternalForwardingListener.createInternal(mControllerListener, controllerlistener);
            return;
        } else
        {
            mControllerListener = controllerlistener;
            return;
        }
    }

    protected abstract Drawable createDrawable(Object obj);

    public Animatable getAnimatable()
    {
        if(mDrawable instanceof Animatable)
            return (Animatable)mDrawable;
        else
            return null;
    }

    public Object getCallerContext()
    {
        return mCallerContext;
    }

    protected ControllerListener getControllerListener()
    {
        if(mControllerListener == null)
            return BaseControllerListener.getNoOpListener();
        else
            return mControllerListener;
    }

    protected Drawable getControllerOverlay()
    {
        return mControllerOverlay;
    }

    protected abstract DataSource getDataSource();

    protected GestureDetector getGestureDetector()
    {
        return mGestureDetector;
    }

    public DraweeHierarchy getHierarchy()
    {
        return mSettableDraweeHierarchy;
    }

    public String getId()
    {
        return mId;
    }

    protected String getImageClass(Object obj)
    {
        if(obj != null)
            return obj.getClass().getSimpleName();
        else
            return "<null>";
    }

    protected int getImageHash(Object obj)
    {
        return System.identityHashCode(obj);
    }

    protected abstract Object getImageInfo(Object obj);

    protected RetryManager getRetryManager()
    {
        return mRetryManager;
    }

    protected void initialize(String s, Object obj)
    {
        init(s, obj);
    }

    public void onAttach()
    {
        if(FLog.isLoggable(2))
        {
            Class class1 = TAG;
            int i = System.identityHashCode(this);
            String s1 = mId;
            String s;
            if(mIsRequestSubmitted)
                s = "request already submitted";
            else
                s = "request needs submit";
            FLog.v(class1, "controller %x %s: onAttach: %s", Integer.valueOf(i), s1, s);
        }
        mEventTracker.recordEvent(com.facebook.drawee.components.DraweeEventTracker.Event.ON_ATTACH_CONTROLLER);
        Preconditions.checkNotNull(mSettableDraweeHierarchy);
        mDeferredReleaser.cancelDeferredRelease(this);
        mIsAttached = true;
        if(!mIsRequestSubmitted)
            submitRequest();
    }

    public boolean onClick()
    {
        if(FLog.isLoggable(2))
            FLog.v(TAG, "controller %x %s: onClick", Integer.valueOf(System.identityHashCode(this)), mId);
        if(shouldRetryOnTap())
        {
            mRetryManager.notifyTapToRetry();
            mSettableDraweeHierarchy.reset();
            submitRequest();
            return true;
        } else
        {
            return false;
        }
    }

    public void onDetach()
    {
        if(FLog.isLoggable(2))
            FLog.v(TAG, "controller %x %s: onDetach", Integer.valueOf(System.identityHashCode(this)), mId);
        mEventTracker.recordEvent(com.facebook.drawee.components.DraweeEventTracker.Event.ON_DETACH_CONTROLLER);
        mIsAttached = false;
        mDeferredReleaser.scheduleDeferredRelease(this);
    }

    public boolean onTouchEvent(MotionEvent motionevent)
    {
        if(FLog.isLoggable(2))
            FLog.v(TAG, "controller %x %s: onTouchEvent %s", Integer.valueOf(System.identityHashCode(this)), mId, motionevent);
        while(mGestureDetector == null || !mGestureDetector.isCapturingGesture() && !shouldHandleGesture())
            return false;
        mGestureDetector.onTouchEvent(motionevent);
        return true;
    }

    public void release()
    {
        mEventTracker.recordEvent(com.facebook.drawee.components.DraweeEventTracker.Event.ON_RELEASE_CONTROLLER);
        if(mRetryManager != null)
            mRetryManager.reset();
        if(mGestureDetector != null)
            mGestureDetector.reset();
        if(mSettableDraweeHierarchy != null)
            mSettableDraweeHierarchy.reset();
        releaseFetch();
    }

    protected abstract void releaseDrawable(Drawable drawable);

    protected abstract void releaseImage(Object obj);

    public void removeControllerListener(ControllerListener controllerlistener)
    {
        Preconditions.checkNotNull(controllerlistener);
        if(mControllerListener instanceof InternalForwardingListener)
            ((InternalForwardingListener)mControllerListener).removeListener(controllerlistener);
        else
        if(mControllerListener == controllerlistener)
        {
            mControllerListener = null;
            return;
        }
    }

    protected void setControllerOverlay(Drawable drawable)
    {
        mControllerOverlay = drawable;
        if(mSettableDraweeHierarchy != null)
            mSettableDraweeHierarchy.setControllerOverlay(mControllerOverlay);
    }

    protected void setGestureDetector(GestureDetector gesturedetector)
    {
        mGestureDetector = gesturedetector;
        if(mGestureDetector != null)
            mGestureDetector.setClickListener(this);
    }

    public void setHierarchy(DraweeHierarchy draweehierarchy)
    {
        if(FLog.isLoggable(2))
            FLog.v(TAG, "controller %x %s: setHierarchy: %s", Integer.valueOf(System.identityHashCode(this)), mId, draweehierarchy);
        DraweeEventTracker draweeeventtracker = mEventTracker;
        com.facebook.drawee.components.DraweeEventTracker.Event event;
        if(draweehierarchy != null)
            event = com.facebook.drawee.components.DraweeEventTracker.Event.ON_SET_HIERARCHY;
        else
            event = com.facebook.drawee.components.DraweeEventTracker.Event.ON_CLEAR_HIERARCHY;
        draweeeventtracker.recordEvent(event);
        if(mIsRequestSubmitted)
        {
            mDeferredReleaser.cancelDeferredRelease(this);
            release();
        }
        if(mSettableDraweeHierarchy != null)
        {
            mSettableDraweeHierarchy.setControllerOverlay(null);
            mSettableDraweeHierarchy = null;
        }
        if(draweehierarchy != null)
        {
            Preconditions.checkArgument(draweehierarchy instanceof SettableDraweeHierarchy);
            mSettableDraweeHierarchy = (SettableDraweeHierarchy)draweehierarchy;
            mSettableDraweeHierarchy.setControllerOverlay(mControllerOverlay);
        }
    }

    protected void setRetainImageOnFailure(boolean flag)
    {
        mRetainImageOnFailure = flag;
    }

    protected void setRetryManager(RetryManager retrymanager)
    {
        mRetryManager = retrymanager;
    }

    protected boolean shouldHandleGesture()
    {
        return shouldRetryOnTap();
    }

    protected void submitRequest()
    {
        mEventTracker.recordEvent(com.facebook.drawee.components.DraweeEventTracker.Event.ON_DATASOURCE_SUBMIT);
        getControllerListener().onSubmit(mId, mCallerContext);
        mSettableDraweeHierarchy.setProgress(0.0F, true);
        mIsRequestSubmitted = true;
        mHasFetchFailed = false;
        mDataSource = getDataSource();
        if(FLog.isLoggable(2))
            FLog.v(TAG, "controller %x %s: submitRequest: dataSource: %x", Integer.valueOf(System.identityHashCode(this)), mId, Integer.valueOf(System.identityHashCode(mDataSource)));

        final String id = mId;
        final boolean wasImmediate = mIsRequestSubmitted;

        BaseDataSubscriber basedatasubscriber = new BaseDataSubscriber() {

            public void onFailureImpl(DataSource datasource)
            {
                onFailureInternal(id, datasource, datasource.getFailureCause(), true);
            }

            public void onNewResultImpl(DataSource datasource)
            {
                boolean flag = datasource.isFinished();
                float f = datasource.getProgress();
                Object obj = datasource.getResult();
                if(obj != null)
                    onNewResultInternal(id, datasource, obj, f, flag, wasImmediate);
                else
                if(flag)
                {
                    onFailureInternal(id, datasource, new NullPointerException(), true);
                    return;
                }
            }

            public void onProgressUpdate(DataSource datasource)
            {
                boolean flag = datasource.isFinished();
                float f = datasource.getProgress();
                onProgressUpdateInternal(id, datasource, f, flag);
            }

        };

        mDataSource.subscribe(basedatasubscriber, mUiThreadImmediateExecutor);
    }

    public String toString()
    {
        return Objects.toStringHelper(this).add("isAttached", mIsAttached).add("isRequestSubmitted", mIsRequestSubmitted).add("hasFetchFailed", mHasFetchFailed).add("fetchedImage", getImageHash(mFetchedImage)).add("events", mEventTracker.toString()).toString();
    }

    private static final Class TAG = AbstractDraweeController.class;
    private Object mCallerContext;
    private ControllerListener mControllerListener;
    private Drawable mControllerOverlay;
    private DataSource mDataSource;
    private final DeferredReleaser mDeferredReleaser;
    private Drawable mDrawable;
    private final DraweeEventTracker mEventTracker = new DraweeEventTracker();
    private Object mFetchedImage;
    private GestureDetector mGestureDetector;
    private boolean mHasFetchFailed;
    private String mId;
    private boolean mIsAttached;
    private boolean mIsRequestSubmitted;
    private boolean mRetainImageOnFailure;
    private RetryManager mRetryManager;
    private SettableDraweeHierarchy mSettableDraweeHierarchy;
    private final Executor mUiThreadImmediateExecutor;

}
