// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.producers;

import com.facebook.common.internal.Preconditions;
import java.util.concurrent.Executor;

// Referenced classes of package com.facebook.imagepipeline.producers:
//            Producer, ProducerContext, Consumer, StatefulProducerRunnable,
//            ProducerListener, BaseProducerContextCallbacks

public class ThreadHandoffProducer
    implements Producer
{

    public ThreadHandoffProducer(Executor executor, Producer producer)
    {
        mExecutor = (Executor)Preconditions.checkNotNull(executor);
        mInputProducer = (Producer)Preconditions.checkNotNull(producer);
    }

    public void produceResults(final Consumer final_consumer1, final ProducerContext producercontext)
    {
        final ProducerListener final_producerlistener = producercontext.getListener();
        final String s = producercontext.getId();
        final StatefulProducerRunnable statefulProducerRunnable = new StatefulProducerRunnable(final_consumer1, final_producerlistener, PRODUCER_NAME, s) {

            protected void disposeResult(Object obj)
            {
            }

            protected Object getResult()
                throws Exception
            {
                return null;
            }

            protected void onSuccess(Object obj)
            {
                final_producerlistener.onProducerFinishWithSuccess(s, "BackgroundThreadHandoffProducer", null);
                mInputProducer.produceResults(final_consumer1, producercontext);
            }

        };
        producercontext.addCallbacks(new BaseProducerContextCallbacks() {

            public void onCancellationRequested()
            {
                statefulProducerRunnable.cancel();
            }

        });
        mExecutor.execute(statefulProducerRunnable);
    }

    protected static final String PRODUCER_NAME = "BackgroundThreadHandoffProducer";
    private final Executor mExecutor;
    private final Producer mInputProducer;

}
