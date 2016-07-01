

package com.facebook.imagepipeline.producers;


// Referenced classes of package com.facebook.imagepipeline.producers:
//            Producer, Consumer, ProducerContext

public class NullProducer
    implements Producer
{

    public NullProducer()
    {
    }

    public void produceResults(Consumer consumer, ProducerContext producercontext)
    {
        consumer.onNewResult((Object)null, true);
    }
}
