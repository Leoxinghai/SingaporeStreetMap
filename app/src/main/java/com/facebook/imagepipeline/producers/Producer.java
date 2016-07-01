

package com.facebook.imagepipeline.producers;


// Referenced classes of package com.facebook.imagepipeline.producers:
//            Consumer, ProducerContext

public interface Producer
{

    public abstract void produceResults(Consumer consumer, ProducerContext producercontext);
}
