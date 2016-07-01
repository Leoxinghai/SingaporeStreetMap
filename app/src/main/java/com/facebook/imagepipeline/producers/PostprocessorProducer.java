// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.producers;

import com.facebook.common.internal.ImmutableMap;
import com.facebook.common.internal.Preconditions;
import com.facebook.common.references.CloseableReference;
import com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.CloseableStaticBitmap;
import com.facebook.imagepipeline.request.*;
import java.util.Map;
import java.util.concurrent.Executor;

// Referenced classes of package com.facebook.imagepipeline.producers:
//            Producer, ProducerContext, Consumer, DelegatingConsumer,
//            ProducerListener, BaseProducerContextCallbacks

public class PostprocessorProducer
    implements Producer
{
    private class PostprocessorConsumer extends DelegatingConsumer
    {

        private void clearRunningAndStartIfDirty()
        {
            boolean flag;
            mIsPostProcessingRunning = false;
            flag = setRunningIfDirtyAndNotRunning();

            if(flag)
                submitPostprocessing();
            return;
        }

        private boolean close()
        {
            if(!mIsClosed) {
                CloseableReference closeablereference;
                closeablereference = mSourceImageRef;
                mSourceImageRef = null;
                mIsClosed = true;

                CloseableReference.closeSafely(closeablereference);
                return true;

            }
            return false;

        }

        private void doPostprocessing(CloseableReference closeablereference, boolean flag)
        {
            CloseableReference closeablereference1;
            Object obj;
            Preconditions.checkArgument(CloseableReference.isValid(closeablereference));
            if(!shouldPostprocess((CloseableImage)closeablereference.get()))
            {
                maybeNotifyOnNewResult(closeablereference, flag);
                return;
            }
            mListener.onProducerStart(mRequestId, "PostprocessorProducer");
            obj = null;
            closeablereference1 = null;
            closeablereference = postprocessInternal((CloseableImage)closeablereference.get());
            closeablereference1 = closeablereference;
            mListener.onProducerFinishWithSuccess(mRequestId, "PostprocessorProducer", getExtraMap(mListener, mRequestId, mPostprocessor));
            closeablereference1 = closeablereference;
            maybeNotifyOnNewResult(closeablereference, flag);
            CloseableReference.closeSafely(closeablereference);
            return;
        }

        private Map getExtraMap(ProducerListener producerlistener, String s, Postprocessor postprocessor)
        {
            if(!producerlistener.requiresExtraMap(s))
                return null;
            else
                return ImmutableMap.of("Postprocessor", postprocessor.getName());
        }

        private boolean isClosed()
        {
            boolean flag = mIsClosed;

            return flag;
        }

        private void maybeNotifyOnCancellation()
        {
            if(close())
                getConsumer().onCancellation();
        }

        private void maybeNotifyOnFailure(Throwable throwable)
        {
            if(close())
                getConsumer().onFailure(throwable);
        }

        private void maybeNotifyOnNewResult(CloseableReference closeablereference, boolean flag)
        {
            if(!flag && !isClosed() || flag && close())
                getConsumer().onNewResult(closeablereference, flag);
        }

        private CloseableReference postprocessInternal(CloseableImage closeableimage)
        {
            Object obj;
            int i;
            CloseableStaticBitmap closeablestaticbitmap = (CloseableStaticBitmap)closeableimage;
            obj = closeablestaticbitmap.getUnderlyingBitmap();
            obj = mPostprocessor.process(((android.graphics.Bitmap) (obj)), mBitmapFactory);
            i = closeablestaticbitmap.getRotationAngle();
            CloseableReference closeableReference = CloseableReference.of(new CloseableStaticBitmap(((CloseableReference) (obj)), closeableimage.getQualityInfo(), i));
            CloseableReference.closeSafely(((CloseableReference) (obj)));
            return closeableReference;
        }

        private boolean setRunningIfDirtyAndNotRunning()
        {
            boolean flag = true;
            if(mIsClosed || !mIsDirty || mIsPostProcessingRunning || !CloseableReference.isValid(mSourceImageRef))
				flag = false;
			else
	            mIsPostProcessingRunning = true;
            return flag;
        }

        private boolean shouldPostprocess(CloseableImage closeableimage)
        {
            return closeableimage instanceof CloseableStaticBitmap;
        }

        private void submitPostprocessing()
        {
            mExecutor.execute(new Runnable() {

                public void run()
                {
                    boolean flag;
                    Object obj;
                    synchronized(PostprocessorConsumer.this)
                    {
                        obj = mSourceImageRef;
                        flag = mIsLast;
                        mSourceImageRef = null;
                        mIsDirty = false;
                    }
                    if(!CloseableReference.isValid(((CloseableReference) (obj))))
                        return;
                    doPostprocessing(((CloseableReference) (obj)), flag);
                    CloseableReference.closeSafely(((CloseableReference) (obj)));
                    clearRunningAndStartIfDirty();
                    return;
                }

            });
        }

        private void updateSourceImageRef(CloseableReference closeablereference, boolean flag)
        {
            if(mIsClosed)
                return;
            CloseableReference closeablereference1;
            closeablereference1 = mSourceImageRef;
            mSourceImageRef = CloseableReference.cloneOrNull(closeablereference);
            mIsLast = flag;
            mIsDirty = true;
            flag = setRunningIfDirtyAndNotRunning();

            CloseableReference.closeSafely(closeablereference1);
            if(flag)
            {
                submitPostprocessing();
                return;
            } else
            {
                return;
            }
        }

        protected void onCancellationImpl()
        {
            maybeNotifyOnCancellation();
        }

        protected void onFailureImpl(Throwable throwable)
        {
            maybeNotifyOnFailure(throwable);
        }

        protected void onNewResultImpl(CloseableReference closeablereference, boolean flag)
        {
            if(!CloseableReference.isValid(closeablereference))
            {
                if(flag)
                    maybeNotifyOnNewResult(null, true);
                return;
            } else
            {
                updateSourceImageRef(closeablereference, flag);
                return;
            }
        }

        protected void onNewResultImpl(Object obj, boolean flag)
        {
            onNewResultImpl((CloseableReference)obj, flag);
        }

        private boolean mIsClosed;
        private boolean mIsDirty;
        private boolean mIsLast;
        private boolean mIsPostProcessingRunning;
        private final ProducerListener mListener;
        private final Postprocessor mPostprocessor;
        private final String mRequestId;
        private CloseableReference mSourceImageRef;




/*
        static CloseableReference access$302(PostprocessorConsumer postprocessorconsumer, CloseableReference closeablereference)
        {
            postprocessorconsumer.mSourceImageRef = closeablereference;
            return closeablereference;
        }

*/



/*
        static boolean access$502(PostprocessorConsumer postprocessorconsumer, boolean flag)
        {
            postprocessorconsumer.mIsDirty = flag;
            return flag;
        }

*/



        public PostprocessorConsumer(Consumer consumer, ProducerListener producerlistener, String s, Postprocessor postprocessor, ProducerContext producercontext)
        {
            super(consumer);
            mSourceImageRef = null;
            mIsLast = false;
            mIsDirty = false;
            mIsPostProcessingRunning = false;
            mListener = producerlistener;
            mRequestId = s;
            mPostprocessor = postprocessor;
            producercontext.addCallbacks(new _cls1());
        }

        class _cls1 extends BaseProducerContextCallbacks
        {

            public void onCancellationRequested()
            {
                maybeNotifyOnCancellation();
            }

        }
    }

    class RepeatedPostprocessorConsumer extends DelegatingConsumer
        implements RepeatedPostprocessorRunner
    {

        private boolean close()
        {
            if(mIsClosed)
                return false;
            CloseableReference closeablereference;
            closeablereference = mSourceImageRef;
            mSourceImageRef = null;
            mIsClosed = true;

            CloseableReference.closeSafely(closeablereference);
            return true;
        }

        private void setSourceImageRef(CloseableReference closeablereference)
        {
            if(mIsClosed)
                return;
            CloseableReference closeablereference1;
            closeablereference1 = mSourceImageRef;
            mSourceImageRef = CloseableReference.cloneOrNull(closeablereference);

            CloseableReference.closeSafely(closeablereference1);
            return;
        }

        private void updateInternal()
        {
            if(mIsClosed)
                return;
            Object obj = CloseableReference.cloneOrNull(mSourceImageRef);

            getConsumer().onNewResult(obj, false);
            CloseableReference.closeSafely(((CloseableReference) (obj)));
            return;
            }

        protected void onCancellationImpl()
        {
            if(close())
                getConsumer().onCancellation();
        }

        protected void onFailureImpl(Throwable throwable)
        {
            if(close())
                getConsumer().onFailure(throwable);
        }

        protected void onNewResultImpl(CloseableReference closeablereference, boolean flag)
        {
            if(!flag)
            {
                return;
            } else
            {
                setSourceImageRef(closeablereference);
                updateInternal();
                return;
            }
        }

        protected void onNewResultImpl(Object obj, boolean flag)
        {
            onNewResultImpl((CloseableReference)obj, flag);
        }

        public void update()
        {
            updateInternal();

            return;
        }

        private boolean mIsClosed;
        private CloseableReference mSourceImageRef;


        private RepeatedPostprocessorConsumer(PostprocessorConsumer postprocessorconsumer, RepeatedPostprocessor repeatedpostprocessor, ProducerContext producercontext)
        {
            super(postprocessorconsumer);
            mIsClosed = false;
            mSourceImageRef = null;
            repeatedpostprocessor.setCallback(this);
            producercontext.addCallbacks(new _cls1());
        }

        class _cls1 extends BaseProducerContextCallbacks
        {

            public void onCancellationRequested()
            {
                if(close())
                    getConsumer().onCancellation();
            }

        }

    }

    class SingleUsePostprocessorConsumer extends DelegatingConsumer
    {

        protected void onNewResultImpl(CloseableReference closeablereference, boolean flag)
        {
            if(!flag)
            {
                return;
            } else
            {
                getConsumer().onNewResult(closeablereference, flag);
                return;
            }
        }

        protected void onNewResultImpl(Object obj, boolean flag)
        {
            onNewResultImpl((CloseableReference)obj, flag);
        }


        private SingleUsePostprocessorConsumer(PostprocessorConsumer postprocessorconsumer)
        {
            super(postprocessorconsumer);
        }

    }


    public PostprocessorProducer(Producer producer, PlatformBitmapFactory platformbitmapfactory, Executor executor)
    {
        mInputProducer = (Producer)Preconditions.checkNotNull(producer);
        mBitmapFactory = platformbitmapfactory;
        mExecutor = (Executor)Preconditions.checkNotNull(executor);
    }

    public void produceResults(Consumer consumer, ProducerContext producercontext)
    {
        ProducerListener producerlistener = producercontext.getListener();
        Postprocessor postprocessor = producercontext.getImageRequest().getPostprocessor();
        PostprocessorConsumer postprocessorConsumer = new PostprocessorConsumer(consumer, producerlistener, producercontext.getId(), postprocessor, producercontext);
        if(postprocessor instanceof RepeatedPostprocessor)
            consumer = new RepeatedPostprocessorConsumer(postprocessorConsumer, (RepeatedPostprocessor)postprocessor, producercontext);
        else
            consumer = new SingleUsePostprocessorConsumer(postprocessorConsumer);
        mInputProducer.produceResults(consumer, producercontext);
    }

    static final String NAME = "PostprocessorProducer";
    static final String POSTPROCESSOR = "Postprocessor";
    private final PlatformBitmapFactory mBitmapFactory;
    private final Executor mExecutor;
    private final Producer mInputProducer;





}
