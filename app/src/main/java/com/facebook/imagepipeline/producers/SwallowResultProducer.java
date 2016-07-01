// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

package com.facebook.imagepipeline.producers;


// Referenced classes of package com.facebook.imagepipeline.producers:
//            Producer, Consumer, ProducerContext, DelegatingConsumer

public class SwallowResultProducer
    implements Producer
{

    public SwallowResultProducer(Producer producer)
    {
        mInputProducer = producer;
    }

    public void produceResults(Consumer consumer, ProducerContext producercontext)
    {
        consumer = new DelegatingConsumer(consumer) {

            protected void onNewResultImpl(Object obj, boolean flag)
            {
                if(flag)
                    getConsumer().onNewResult(null, flag);
            }

        };
        mInputProducer.produceResults(consumer, producercontext);
    }

    private final Producer mInputProducer;
}
