// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.producers;

import com.facebook.imagepipeline.common.Priority;
import com.facebook.imagepipeline.request.ImageRequest;
import java.util.*;

// Referenced classes of package com.facebook.imagepipeline.producers:
//            ProducerContext, ProducerContextCallbacks, ProducerListener

public class BaseProducerContext
    implements ProducerContext
{

    public BaseProducerContext(ImageRequest imagerequest, String s, ProducerListener producerlistener, Object obj, com.facebook.imagepipeline.request.ImageRequest.RequestLevel requestlevel, boolean flag, boolean flag1,
            Priority priority)
    {
        mImageRequest = imagerequest;
        mId = s;
        mProducerListener = producerlistener;
        mCallerContext = obj;
        mLowestPermittedRequestLevel = requestlevel;
        mIsPrefetch = flag;
        mPriority = priority;
        mIsIntermediateResultExpected = flag1;
        mIsCancelled = false;
    }

    public static void callOnCancellationRequested(List list)
    {
        if(list != null)
        {
            Iterator iterator = list.iterator();
            while(iterator.hasNext())
                ((ProducerContextCallbacks)iterator.next()).onCancellationRequested();
        }
    }

    public static void callOnIsIntermediateResultExpectedChanged(List list)
    {
        if(list != null)
        {
            Iterator iterator = list.iterator();
            while(iterator.hasNext())
                ((ProducerContextCallbacks)iterator.next()).onIsIntermediateResultExpectedChanged();
        }
    }

    public static void callOnIsPrefetchChanged(List list)
    {
        if(list != null)
        {
            Iterator iterator = list.iterator();
            while(iterator.hasNext())
                ((ProducerContextCallbacks)iterator.next()).onIsPrefetchChanged();
        }
    }

    public static void callOnPriorityChanged(List list)
    {
        if(list != null)
        {
            Iterator iterator = list.iterator();
            while(iterator.hasNext())
                ((ProducerContextCallbacks)iterator.next()).onPriorityChanged();
        }
    }

    public void addCallbacks(ProducerContextCallbacks producercontextcallbacks)
    {
        boolean flag = false;
        mCallbacks.add(producercontextcallbacks);
        if(mIsCancelled)
            flag = true;
        if(flag)
            producercontextcallbacks.onCancellationRequested();
        return;
    }

    public void cancel()
    {
        callOnCancellationRequested(cancelNoCallbacks());
    }

    public List cancelNoCallbacks()
    {
        boolean flag = mIsCancelled;
        Object obj = null;
        if(!flag) {
			mIsCancelled = true;
			obj = new ArrayList(mCallbacks);
		}

        return ((List) (obj));
    }

    public Object getCallerContext()
    {
        return mCallerContext;
    }

    public String getId()
    {
        return mId;
    }

    public ImageRequest getImageRequest()
    {
        return mImageRequest;
    }

    public ProducerListener getListener()
    {
        return mProducerListener;
    }

    public com.facebook.imagepipeline.request.ImageRequest.RequestLevel getLowestPermittedRequestLevel()
    {
        return mLowestPermittedRequestLevel;
    }

    public Priority getPriority()
    {
        Priority priority = mPriority;
        return priority;
    }

    public boolean isCancelled()
    {
        boolean flag = mIsCancelled;
        return flag;
    }

    public boolean isIntermediateResultExpected()
    {
        boolean flag = mIsIntermediateResultExpected;
        return flag;
    }

    public boolean isPrefetch()
    {
        boolean flag = mIsPrefetch;
        return flag;
    }

    public List setIsIntermediateResultExpectedNoCallbacks(boolean flag)
    {
        boolean flag1 = mIsIntermediateResultExpected;
        Object obj = null;
        if(flag != flag1) {
			mIsIntermediateResultExpected = flag;
			obj = new ArrayList(mCallbacks);
		}
        return ((List) (obj));
    }

    public List setIsPrefetchNoCallbacks(boolean flag)
    {
        boolean flag1 = mIsPrefetch;
        Object obj = null;
        if(flag != flag1) {
			mIsPrefetch = flag;
			obj = new ArrayList(mCallbacks);
		}
        return ((List) (obj));
    }

    public List setPriorityNoCallbacks(Priority priority)
    {
        Priority priority1 = mPriority;
        priority = null;
        ArrayList arrayList = null;
        if(priority != priority1) {
			mPriority = priority;
            arrayList = new ArrayList(mCallbacks);
		}
        return arrayList;
    }

    private final List mCallbacks = new ArrayList();
    private final Object mCallerContext;
    private final String mId;
    private final ImageRequest mImageRequest;
    private boolean mIsCancelled;
    private boolean mIsIntermediateResultExpected;
    private boolean mIsPrefetch;
    private final com.facebook.imagepipeline.request.ImageRequest.RequestLevel mLowestPermittedRequestLevel;
    private Priority mPriority;
    private final ProducerListener mProducerListener;
}
