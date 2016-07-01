

package com.facebook.imagepipeline.producers;

import com.facebook.imagepipeline.common.Priority;
import com.facebook.imagepipeline.request.ImageRequest;

// Referenced classes of package com.facebook.imagepipeline.producers:
//            BaseProducerContext, ProducerListener

public class SettableProducerContext extends BaseProducerContext
{

    public SettableProducerContext(ImageRequest imagerequest, String s, ProducerListener producerlistener, Object obj, com.facebook.imagepipeline.request.ImageRequest.RequestLevel requestlevel, boolean flag, boolean flag1,
            Priority priority)
    {
        super(imagerequest, s, producerlistener, obj, requestlevel, flag, flag1, priority);
    }

    public void setIsIntermediateResultExpected(boolean flag)
    {
        BaseProducerContext.callOnIsIntermediateResultExpectedChanged(setIsIntermediateResultExpectedNoCallbacks(flag));
    }

    public void setIsPrefetch(boolean flag)
    {
        BaseProducerContext.callOnIsPrefetchChanged(setIsPrefetchNoCallbacks(flag));
    }

    public void setPriority(Priority priority)
    {
        BaseProducerContext.callOnPriorityChanged(setPriorityNoCallbacks(priority));
    }
}
