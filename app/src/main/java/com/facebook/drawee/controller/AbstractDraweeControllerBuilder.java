// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.drawee.controller;

import android.content.Context;
import android.graphics.drawable.Animatable;
import com.facebook.common.internal.*;
import com.facebook.common.internal.Objects;
import com.facebook.datasource.*;
import com.facebook.drawee.components.RetryManager;
import com.facebook.drawee.gestures.GestureDetector;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.interfaces.SimpleDraweeControllerBuilder;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

// Referenced classes of package com.facebook.drawee.controller:
//            AbstractDraweeController, ControllerListener, BaseControllerListener

public abstract class AbstractDraweeControllerBuilder
    implements SimpleDraweeControllerBuilder
{

    protected AbstractDraweeControllerBuilder(Context context, Set set)
    {
        mContext = context;
        mBoundControllerListeners = set;
        init();
    }

    protected static String generateUniqueControllerId()
    {
        return String.valueOf(sIdCounter.getAndIncrement());
    }

    private void init()
    {
        mCallerContext = null;
        mImageRequest = null;
        mLowResImageRequest = null;
        mMultiImageRequests = null;
        mTryCacheOnlyFirst = true;
        mControllerListener = null;
        mTapToRetryEnabled = false;
        mAutoPlayAnimations = false;
        mOldController = null;
    }

    public AbstractDraweeController build()
    {
        validate();
        if(mImageRequest == null && mMultiImageRequests == null && mLowResImageRequest != null)
        {
            mImageRequest = mLowResImageRequest;
            mLowResImageRequest = null;
        }
        return buildController();
    }


    protected AbstractDraweeController buildController()
    {
        AbstractDraweeController abstractdraweecontroller = obtainController();
        abstractdraweecontroller.setRetainImageOnFailure(getRetainImageOnFailure());
        maybeBuildAndSetRetryManager(abstractdraweecontroller);
        maybeAttachListeners(abstractdraweecontroller);
        return abstractdraweecontroller;
    }

    public boolean getAutoPlayAnimations()
    {
        return mAutoPlayAnimations;
    }

    public Object getCallerContext()
    {
        return mCallerContext;
    }

    protected Context getContext()
    {
        return mContext;
    }

    public ControllerListener getControllerListener()
    {
        return mControllerListener;
    }

    protected abstract DataSource getDataSourceForRequest(Object obj, Object obj1, boolean flag);

    public Supplier getDataSourceSupplier()
    {
        return mDataSourceSupplier;
    }

    protected Supplier getDataSourceSupplierForRequest(Object obj)
    {
        return getDataSourceSupplierForRequest(obj, false);
    }

    protected Supplier getDataSourceSupplierForRequest(final Object imageRequest, final boolean bitmapCacheOnly)
    {
        return new Supplier() {

            public DataSource get()
            {
                return getDataSourceForRequest(imageRequest, mCallerContext, bitmapCacheOnly);
            }


            public String toString()
            {
                return Objects.toStringHelper(this).add("request", imageRequest.toString()).toString();
            }

        };
    }

    protected Supplier getFirstAvailableDataSourceSupplier(Object aobj[], boolean flag)
    {
        ArrayList arraylist = new ArrayList(aobj.length * 2);
        if(flag)
        {
            for(int i = 0; i < aobj.length; i++)
                arraylist.add(getDataSourceSupplierForRequest(aobj[i], true));

        }
        for(int j = 0; j < aobj.length; j++)
            arraylist.add(getDataSourceSupplierForRequest(aobj[j]));

        return FirstAvailableDataSourceSupplier.create(arraylist);
    }

    public Object[] getFirstAvailableImageRequests()
    {
        return mMultiImageRequests;
    }

    public Object getImageRequest()
    {
        return mImageRequest;
    }

    public Object getLowResImageRequest()
    {
        return mLowResImageRequest;
    }

    public DraweeController getOldController()
    {
        return mOldController;
    }

    public boolean getRetainImageOnFailure()
    {
        return mRetainImageOnFailure;
    }

    public boolean getTapToRetryEnabled()
    {
        return mTapToRetryEnabled;
    }

    protected abstract AbstractDraweeControllerBuilder getThis();

    protected void maybeAttachListeners(AbstractDraweeController abstractdraweecontroller)
    {
        if(mBoundControllerListeners != null)
        {
            for(Iterator iterator = mBoundControllerListeners.iterator(); iterator.hasNext(); abstractdraweecontroller.addControllerListener((ControllerListener)iterator.next()));
        }
        if(mControllerListener != null)
            abstractdraweecontroller.addControllerListener(mControllerListener);
        if(mAutoPlayAnimations)
            abstractdraweecontroller.addControllerListener(sAutoPlayAnimationsListener);
    }

    protected void maybeBuildAndSetGestureDetector(AbstractDraweeController abstractdraweecontroller)
    {
        if(abstractdraweecontroller.getGestureDetector() == null)
            abstractdraweecontroller.setGestureDetector(GestureDetector.newInstance(mContext));
    }

    protected void maybeBuildAndSetRetryManager(AbstractDraweeController abstractdraweecontroller)
    {
        if(!mTapToRetryEnabled)
            return;
        RetryManager retrymanager1 = abstractdraweecontroller.getRetryManager();
        RetryManager retrymanager = retrymanager1;
        if(retrymanager1 == null)
        {
            retrymanager = new RetryManager();
            abstractdraweecontroller.setRetryManager(retrymanager);
        }
        retrymanager.setTapToRetryEnabled(mTapToRetryEnabled);
        maybeBuildAndSetGestureDetector(abstractdraweecontroller);
    }

    protected abstract AbstractDraweeController obtainController();

    protected Supplier obtainDataSourceSupplier()
    {
        Object obj;
        if(mDataSourceSupplier != null)
        {
            obj = mDataSourceSupplier;
        } else
        {
            obj = null;
            Object obj1;
            if(mImageRequest != null)
                obj = getDataSourceSupplierForRequest(mImageRequest);
            else
            if(mMultiImageRequests != null)
                obj = getFirstAvailableDataSourceSupplier(mMultiImageRequests, mTryCacheOnlyFirst);
            obj1 = obj;
            if(obj != null)
            {
                obj1 = obj;
                if(mLowResImageRequest != null)
                {
                    obj1 = new ArrayList(2);
                    ((List) (obj1)).add(obj);
                    ((List) (obj1)).add(getDataSourceSupplierForRequest(mLowResImageRequest));
                    obj1 = IncreasingQualityDataSourceSupplier.create(((List) (obj1)));
                }
            }
            obj = obj1;
            if(obj1 == null)
                return DataSources.getFailedDataSourceSupplier(NO_REQUEST_EXCEPTION);
        }
        return ((Supplier) (obj));
    }

    public AbstractDraweeControllerBuilder reset()
    {
        init();
        return getThis();
    }

    public AbstractDraweeControllerBuilder setAutoPlayAnimations(boolean flag)
    {
        mAutoPlayAnimations = flag;
        return getThis();
    }

    public AbstractDraweeControllerBuilder setCallerContext(Object obj)
    {
        mCallerContext = obj;
        return getThis();
    }

    public AbstractDraweeControllerBuilder setControllerListener(ControllerListener controllerlistener)
    {
        mControllerListener = controllerlistener;
        return getThis();
    }

    public void setDataSourceSupplier(Supplier supplier)
    {
        mDataSourceSupplier = supplier;
    }

    public AbstractDraweeControllerBuilder setFirstAvailableImageRequests(Object aobj[])
    {
        return setFirstAvailableImageRequests(aobj, true);
    }

    public AbstractDraweeControllerBuilder setFirstAvailableImageRequests(Object aobj[], boolean flag)
    {
        mMultiImageRequests = aobj;
        mTryCacheOnlyFirst = flag;
        return getThis();
    }

    public AbstractDraweeControllerBuilder setImageRequest(Object obj)
    {
        mImageRequest = obj;
        return getThis();
    }

    public AbstractDraweeControllerBuilder setLowResImageRequest(Object obj)
    {
        mLowResImageRequest = obj;
        return getThis();
    }

    public AbstractDraweeControllerBuilder setOldController(DraweeController draweecontroller)
    {
        mOldController = draweecontroller;
        return getThis();
    }


    public AbstractDraweeControllerBuilder setRetainImageOnFailure(boolean flag)
    {
        mRetainImageOnFailure = flag;
        return getThis();
    }

    public AbstractDraweeControllerBuilder setTapToRetryEnabled(boolean flag)
    {
        mTapToRetryEnabled = flag;
        return getThis();
    }

    protected void validate()
    {
        boolean flag1 = false;
        boolean flag;
        if(mMultiImageRequests == null || mImageRequest == null)
            flag = true;
        else
            flag = false;
        Preconditions.checkState(flag, "Cannot specify both ImageRequest and FirstAvailableImageRequests!");
        if(mDataSourceSupplier != null)
        {
            flag = flag1;
            if(mMultiImageRequests == null && mImageRequest == null && mLowResImageRequest == null)
                flag = true;
        }
        Preconditions.checkState(flag, "Cannot specify DataSourceSupplier with other ImageRequests! Use one or the other.");
    }

    private static final NullPointerException NO_REQUEST_EXCEPTION = new NullPointerException("No image request was specified!");
    private static final ControllerListener sAutoPlayAnimationsListener = new BaseControllerListener() {

        public void onFinalImageSet(String s, Object obj, Animatable animatable)
        {
            if(animatable != null)
                animatable.start();
        }

    }
;
    private static final AtomicLong sIdCounter = new AtomicLong();
    private boolean mAutoPlayAnimations;
    private final Set mBoundControllerListeners;
    private Object mCallerContext;
    private final Context mContext;
    private ControllerListener mControllerListener;
    private Supplier mDataSourceSupplier;
    private Object mImageRequest;
    private Object mLowResImageRequest;
    private Object mMultiImageRequests[];
    private DraweeController mOldController;
    private boolean mRetainImageOnFailure;
    private boolean mTapToRetryEnabled;
    private boolean mTryCacheOnlyFirst;

}
