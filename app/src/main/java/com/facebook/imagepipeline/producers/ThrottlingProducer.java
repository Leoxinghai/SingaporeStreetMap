// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.producers;

import android.util.Pair;
import com.facebook.common.internal.Preconditions;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;

// Referenced classes of package com.facebook.imagepipeline.producers:
//            Producer, ProducerContext, ProducerListener, Consumer,
//            DelegatingConsumer

public class ThrottlingProducer
    implements Producer
{
    private class ThrottlerConsumer extends DelegatingConsumer
    {

        private void onRequestFinished() {
            ThrottlingProducer throttlingproducer = ThrottlingProducer.this;
            Pair pair = (Pair) mPendingRequests.poll();
            //if (pair != null)
                //break MISSING_BLOCK_LABEL_33;
            //int i =
        }


        protected void onCancellationImpl()
        {
            getConsumer().onCancellation();
            onRequestFinished();
        }

        protected void onFailureImpl(Throwable throwable)
        {
            getConsumer().onFailure(throwable);
            onRequestFinished();
        }

        protected void onNewResultImpl(Object obj, boolean flag)
        {
            getConsumer().onNewResult(obj, flag);
            if(flag)
                onRequestFinished();
        }

        private ThrottlerConsumer(Consumer consumer)
        {
            super(consumer);
        }

        class cls1
        implements Runnable
        {

            public void run()
            {
                //produceResultsInternal((Consumer)nextRequestPair.first, (ProducerContext)nextRequestPair.second);
            }

        }

    }


    public ThrottlingProducer(int i, Executor executor, Producer producer)
    {
        mMaxSimultaneousRequests = i;
        mExecutor = (Executor)Preconditions.checkNotNull(executor);
        mInputProducer = (Producer)Preconditions.checkNotNull(producer);
        mNumCurrentRequests = 0;
    }

    public void produceResults(Consumer consumer, ProducerContext producercontext)
    {
		boolean flag = true;
        producercontext.getListener().onProducerStart(producercontext.getId(), "ThrottlingProducer");
        if(mNumCurrentRequests < mMaxSimultaneousRequests) {
			mNumCurrentRequests = mNumCurrentRequests + 1;
			flag = false;
		} else {
			mPendingRequests.add(Pair.create(consumer, producercontext));
		}
        if(!flag)
            produceResultsInternal(consumer, producercontext);
        return;
    }

    void produceResultsInternal(Consumer consumer, ProducerContext producercontext)
    {
        producercontext.getListener().onProducerFinishWithSuccess(producercontext.getId(), "ThrottlingProducer", null);
        mInputProducer.produceResults(new ThrottlerConsumer(consumer), producercontext);
    }

    static final String PRODUCER_NAME = "ThrottlingProducer";
    private final Executor mExecutor;
    private final Producer mInputProducer;
    private final int mMaxSimultaneousRequests;
    private int mNumCurrentRequests;
    private final ConcurrentLinkedQueue mPendingRequests = new ConcurrentLinkedQueue();



/*
    static int access$210(ThrottlingProducer throttlingproducer)
    {
        int i = throttlingproducer.mNumCurrentRequests;
        throttlingproducer.mNumCurrentRequests = i - 1;
        return i;
    }

*/


}
