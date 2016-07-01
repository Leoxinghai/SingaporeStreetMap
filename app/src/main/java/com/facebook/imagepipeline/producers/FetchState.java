

package com.facebook.imagepipeline.producers;

import android.net.Uri;
import com.facebook.imagepipeline.request.ImageRequest;

// Referenced classes of package com.facebook.imagepipeline.producers:
//            ProducerContext, Consumer, ProducerListener

public class FetchState
{

    public FetchState(Consumer consumer, ProducerContext producercontext)
    {
        mConsumer = consumer;
        mContext = producercontext;
        mLastIntermediateResultTimeMs = 0L;
    }

    public Consumer getConsumer()
    {
        return mConsumer;
    }

    public ProducerContext getContext()
    {
        return mContext;
    }

    public String getId()
    {
        return mContext.getId();
    }

    public long getLastIntermediateResultTimeMs()
    {
        return mLastIntermediateResultTimeMs;
    }

    public ProducerListener getListener()
    {
        return mContext.getListener();
    }

    public Uri getUri()
    {
        return mContext.getImageRequest().getSourceUri();
    }

    public void setLastIntermediateResultTimeMs(long l)
    {
        mLastIntermediateResultTimeMs = l;
    }

    private final Consumer mConsumer;
    private final ProducerContext mContext;
    private long mLastIntermediateResultTimeMs;
}
